const express = require("express");
const request = require("request");
const path = require("path");
const app = express();
const config = require("./config");

const port = process.env.PORT || 8081;
app.listen(port, () => console.log(`Listening on port ${port}...`));

app.get("/api/zipcode", (req, res) => {
  request.get(
    `http://api.geonames.org/postalCodeSearchJSON?postalcode_startsWith=${req.query.zipcode}&username=${config.GEONAME_KEY}&country=US&maxRows=5`,
    (error, response, body) => {
      res.setHeader("Access-Control-Allow-Origin", "*");
      if (error) {
        res.send(error);
        return;
      }
      res.send(body);
    });
});

app.get("/api/search", (req, res) => {
  data = JSON.parse(req.query.data);
  let url = `http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=${config.EBAY_API}&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&paginationInput.entriesPerPage=50&keywords=${data.keyword}`;

  let filterIndex = 0;

  url = url + "&buyerPostalCode=" + data.zipcode;
  url = url + `&itemFilter(${filterIndex}).name=MaxDistance`;
  url = url + `&itemFilter(${filterIndex}).value=` + data.distance;
  filterIndex++;

  url = url + `&itemFilter(${filterIndex}).name=HideDuplicateItems`;
  url = url + `&itemFilter(${filterIndex}).value=True`;
  filterIndex++;

  let flag = true;
  let i = 0;
  for (conditionId in data.condition) {
    if (data.condition[conditionId]) {
      if (flag) {
        url = url + `&itemFilter(${filterIndex}).name=Condition`;
        flag = false;
      }
      url = url + `&itemFilter(${filterIndex}).value(${i})=` + conditionId;
      i++;
    }
  }
  if (!flag) {
    filterIndex++;
  }

  for (shippingId in data.shipping) {
    if (data.shipping[shippingId]) {
      url = url + `&itemFilter(${filterIndex}).name=` + shippingId;
      url = url + `&itemFilter(${filterIndex}).value=True`;
    }
  }

  if (data.category !== "all") {
    url = url + "&categoryId=" + data.category;
  }

  url = url + "&outputSelector(0)=SellerInfo&outputSelector(1)=StoreInfo";

  request.get(url,
    (errorResponse, response, data) => {
      res.setHeader("Access-Control-Allow-Origin", "*");
      let result = [];
      if (errorResponse) {
        res.send([{
          error: "No Records.",
        }]);
        return;
      }
      data = JSON.parse(data);

      if (!data || !data.hasOwnProperty("findItemsAdvancedResponse")) {
        res.send([{
          error: "No Records.",
        }]);
        return;
      }

      data = data.findItemsAdvancedResponse[0];
      if (!data.hasOwnProperty("ack")) {
        res.send(result);
      }
      if (data.ack[0] !== "Success") {
        if (data.hasOwnProperty("errorMessage") && data.errorMessage.length > 0) {
          let errorMessage = data.errorMessage[0];
          if (errorMessage.hasOwnProperty("error") && errorMessage.error.length > 0) {
            let error = errorMessage.error[0];
            if (error.hasOwnProperty("message") && error.message.length > 0) {
              res.send([{
                error: error.message[0],
              }]);
            }
          }
        }
        res.send([{
          error: "Data fetching not successful",
        }]);
        return;
      }
      if (
        !data.hasOwnProperty("searchResult") ||
        data.searchResult.length === 0 ||
        data.searchResult[0]["@count"] === "0"
      ) {
        showError("No Records.");
        return;
      }

      let items = data.searchResult[0].item;
      let searchResultModel = {
        highlighted: false,
        image: null,
        inWishList: false,
        index: "",
        itemId: null,
        price: null,
        sellerName: null,
        shippingPrice: null,
        title: null,
        zipcode: null,
      };

      for (let i = 0; i < items.length; i++) {
        let item = items[i];
        let resultItem = Object.assign({}, searchResultModel);
        resultItem.index = i + 1 + "";

        if (item.hasOwnProperty("galleryURL") && item.galleryURL.length > 0) {
          resultItem.image = item.galleryURL[0];
        }

        resultItem.itemId = item.itemId[0];
        if (item.hasOwnProperty("title") && item.title.length > 0) {
          resultItem.title = item.title[0];
        }

        if (item.hasOwnProperty("sellingStatus") && item.sellingStatus.length > 0) {
          let sellingStatus = item.sellingStatus[0];
          if (sellingStatus.hasOwnProperty("currentPrice") && sellingStatus.currentPrice.length > 0) {
            let currentPrice = sellingStatus.currentPrice[0];
            if (currentPrice.hasOwnProperty("__value__")) {
              resultItem.price = "$" + currentPrice.__value__;
            }
          }
        }

        if (item.hasOwnProperty("sellerInfo") && item.sellerInfo.length > 0) {
          let sellerInfo = item.sellerInfo[0];
          if (sellerInfo.hasOwnProperty("sellerUserName") && sellerInfo.sellerUserName.length > 0) {
            resultItem.sellerName = sellerInfo.sellerUserName[0];
          }
        }

        if (item.hasOwnProperty("postalCode") && item.postalCode.length > 0) {
          resultItem.zipcode = item.postalCode[0];
        }

        if (item.hasOwnProperty("shippingInfo") && item.shippingInfo.length > 0) {
          let shippingInfo = item.shippingInfo[0];
          if (shippingInfo.hasOwnProperty("shippingServiceCost") && shippingInfo.shippingServiceCost.length > 0) {
            let shippingServiceCost = shippingInfo.shippingServiceCost[0];
            if (shippingServiceCost.hasOwnProperty("@currencyId") && shippingServiceCost.hasOwnProperty("__value__")) {
              let value = shippingServiceCost.__value__;
              if (Number(value) === 0) {
                resultItem.shippingPrice = "Free Shipping";
              } else {
                resultItem.shippingPrice = "$" + value;
              }
            }
          }
        }

        result.push(resultItem);
      }

      res.send(JSON.stringify(result));
    });
});

app.use(express.static(path.join(__dirname, "dist/Ebay-Product-Search-Angular")));
