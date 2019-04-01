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
        res.status(304).send(error);
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
        res.status(304).send("No Records.");
        return;
      }
      data = JSON.parse(data);

      if (!data || !data.hasOwnProperty("findItemsAdvancedResponse")) {
        res.status(304).send("No Records.");
        return;
      }

      data = data.findItemsAdvancedResponse[0];
      if (!data.hasOwnProperty("ack")) {
        res.status(304).send("No Records.");
        return;
      }
      if (data.ack[0] !== "Success") {
        if (data.hasOwnProperty("errorMessage") && data.errorMessage.length > 0) {
          let errorMessage = data.errorMessage[0];
          if (errorMessage.hasOwnProperty("error") && errorMessage.error.length > 0) {
            let error = errorMessage.error[0];
            if (error.hasOwnProperty("message") && error.message.length > 0) {
              res.status(304).send(error.message[0]);
              return;
            }
          }
        }
        res.status(304).send("Data fetching not successful.");
        return;
      }
      if (
        !data.hasOwnProperty("searchResult") ||
        data.searchResult.length === 0 ||
        data.searchResult[0]["@count"] === "0"
      ) {
        res.status(304).send("No Records.");
        return;
      }

      const items = data.searchResult[0].item;
      const searchResultModel = {
        image: null,
        inWishList: false,
        index: "",
        price: null,
        productId: null,
        sellerName: null,
        shipping: {
          cost: null,
          expedited: null,
          handlingTime: null,
          locations: null,
          oneDay: null,
          returnAccepted: null,
        },
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

        resultItem.productId = item.itemId[0];
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
          const shipping = Object.assign({}, searchResultModel.shipping);
          const shippingInfo = item.shippingInfo[0];

          if (shippingInfo.hasOwnProperty("shippingServiceCost") && shippingInfo.shippingServiceCost.length > 0) {
            const shippingServiceCost = shippingInfo.shippingServiceCost[0];
            if (shippingServiceCost.hasOwnProperty("@currencyId") && shippingServiceCost.hasOwnProperty("__value__")) {
              const value = shippingServiceCost.__value__;
              if (Number(value) === 0) {
                shipping.cost = "Free Shipping";
              } else {
                shipping.cost = "$" + value;
              }
            }
          }

          if (shippingInfo.hasOwnProperty("shipToLocations") && shippingInfo.shipToLocations.length > 0) {
            shipping.locations = shippingInfo.shipToLocations[0];
          }

          if (shippingInfo.hasOwnProperty("handlingTime") && shippingInfo.handlingTime.length > 0) {
            let handlingTime = shippingInfo.handlingTime[0];
            if (handlingTime === "0" || handlingTime === "1") {
              handlingTime += " Day";
            } else {
              handlingTime += " Days";
            }
            shipping.handlingTime = handlingTime;
          }

          if (shippingInfo.hasOwnProperty("expeditedShipping") && shippingInfo.expeditedShipping.length > 0) {
            if (shippingInfo.expeditedShipping[0] === "true") {
              shipping.expedited = true;
            } else {
              shipping.expedited = false;
            }
          }

          if (
            shippingInfo.hasOwnProperty("oneDayShippingAvailable") &&
            shippingInfo.oneDayShippingAvailable.length > 0
          ) {
            if (shippingInfo.oneDayShippingAvailable[0] === "true") {
              shipping.oneDay = true;
            } else {
              shipping.oneDay = false;
            }
          }

          resultItem.shipping = shipping;
        }

        if (item.hasOwnProperty("returnsAccepted") && item.returnsAccepted.length > 0) {
          if (item.returnsAccepted[0] === "true") {
            resultItem.shipping.returnAccepted = true;
          } else {
            resultItem.shipping.returnAccepted = false;
          }
        }

        result.push(resultItem);
      }

      res.send(JSON.stringify(result));
    });
});

app.get("/api/productInfo", (req, res) => {
  const productId = req.query.productId;
  const url = `http://open.api.ebay.com/shopping?callname=GetSingleItem&responseencoding=JSON&appid=${config.EBAY_API}&siteid=0&version=967&ItemID=${productId}&IncludeSelector=Description,Details,ItemSpecifics`;

  request.get(url,
    (errorResponse, response, data) => {
      res.setHeader("Access-Control-Allow-Origin", "*");

      if (errorResponse) {
        res.status(304).send("No data Found");
        return;
      }
      data = JSON.parse(data);

      if (!data) {
        res.status(304).send("No data Found");
        return;
      }
      if (!data.hasOwnProperty("Ack")) {
        res.status(304).send("No data Found");
        return;
      }
      if (data.Ack !== "Success") {
        if (data.hasOwnProperty("Errors") && data.Errors.length > 0) {
          let errors = data.Errors[0];
          if (errors.hasOwnProperty("LongMessage")) {
            res.status(304).send(errors.LongMessage.trim());
            return;
          }
          if (errors.hasOwnProperty("ShortMessage")) {
            res.status(304).send(errors.ShortMessage.trim());
            return;
          }
        }
        res.status(304).send("No data Found");
        return;
      }
      if (!data.hasOwnProperty("Item")) {
        res.status(304).send("No data Found");
        return;
      }

      let isEmpty = true;
      const item = data.Item;

      const productData = {
        itemSpecifics: [],
        location: null,
        price: null,
        productId: null,
        productImages: [],
        returnPolicy: null,
        seller: {
          feedbackScore: null,
          feedbackStarColor: null,
          popularity: null,
          storeName: null,
          storeUrl: null,
          topRated: null,
        },
        subtitle: null,
        title: null,
      };

      if (item.hasOwnProperty("ItemID") && item.ItemID.length > 0) {
        productData.productId = item.ItemID;
      }

      if (item.hasOwnProperty("PictureURL") && item.PictureURL.length > 0) {
        productData.productImages = item.PictureURL;
        isEmpty = false;
      }

      if (item.hasOwnProperty("Title") && item.Title.length > 0) {
        productData.title = item.Title;
        isEmpty = false;
      }

      if (item.hasOwnProperty("Subtitle") && item.Subtitle.length > 0) {
        productData.subtitle = item.Subtitle;
        isEmpty = false;
      }

      if (item.hasOwnProperty("CurrentPrice") && item.CurrentPrice.hasOwnProperty("Value")) {
        productData.price = "$" + item.CurrentPrice.Value;
        isEmpty = false;
      }

      if (
        item.hasOwnProperty("Location") &&
        item.hasOwnProperty("PostalCode") &&
        item.Location.length > 0 &&
        item.PostalCode.length > 0
      ) {
        productData.location = item.Location + ", " + item.PostalCode;
        isEmpty = false;
      } else if (item.hasOwnProperty("Location") && item.Location.length > 0) {
        productData.location = item.Location;
        isEmpty = false;
      } else if (item.hasOwnProperty("PostalCode") && item.PostalCode.length > 0) {
        productData.location = item.PostalCode;
        isEmpty = false;
      }

      if (item.hasOwnProperty("ReturnPolicy") && item.ReturnPolicy.hasOwnProperty("ReturnsAccepted")) {
        if (
          (
            item.ReturnPolicy.ReturnsAccepted === "Returns Accepted" ||
            item.ReturnPolicy.ReturnsAccepted === "ReturnsAccepted"
          ) &&
          item.ReturnPolicy.hasOwnProperty("ReturnsWithin") &&
          item.ReturnPolicy.ReturnsWithin.length > 0
        ) {
          productData.returnPolicy = "Returns Accepted within " + item.ReturnPolicy.ReturnsWithin;
        } else if (item.ReturnPolicy.ReturnsAccepted === "ReturnsNotAccepted") {
          productData.returnPolicy = "Returns not accepted";
        }
      }

      if (
        item.hasOwnProperty("ItemSpecifics") &&
        item.ItemSpecifics.hasOwnProperty("NameValueList") &&
        item.ItemSpecifics.NameValueList.length > 0
      ) {
        const itemSpecifics = item.ItemSpecifics.NameValueList;
        for (let i = 0; i < itemSpecifics.length; i++) {
          const itemSpecific = itemSpecifics[i];
          if (
            itemSpecific.hasOwnProperty("Name") &&
            itemSpecific.hasOwnProperty("Value") &&
            itemSpecific.Name.length > 0 &&
            itemSpecific.Value.length > 0
          ) {
            productData.itemSpecifics.push({
              "name": itemSpecific.Name,
              "value": itemSpecific.Value.join(","),
            });
            isEmpty = false;
          }
        }
      }

      if (item.hasOwnProperty("Seller")) {
        const seller = item.Seller;

        if (seller.hasOwnProperty("UserID") && seller.UserID.length > 0) {
          productData.seller.sellerName = seller.UserID;
        }

        if (seller.hasOwnProperty("FeedbackRatingStar") && seller.FeedbackRatingStar.length > 0) {
          productData.seller.feedbackStarColor = seller.FeedbackRatingStar;
        }

        if (seller.hasOwnProperty("TopRatedSeller")) {
          if (seller.TopRatedSeller) {
            productData.seller.topRated = true;
          } else {
            productData.seller.topRated = false;
          }
        }

        if (seller.hasOwnProperty("FeedbackScore")) {
          productData.seller.feedbackScore = seller.FeedbackScore;
        }

        if (seller.hasOwnProperty("PositiveFeedbackPercent")) {
          productData.seller.popularity = seller.PositiveFeedbackPercent;
        }
      }

      if (item.hasOwnProperty("Storefront")) {
        const store = item.Storefront;

        if (store.hasOwnProperty("StoreURL") && store.StoreURL.length > 0) {
          productData.seller.storeUrl = store.StoreURL;
        }

        if (store.hasOwnProperty("StoreName") && store.StoreName.length > 0) {
          productData.seller.storeName = store.StoreName;
        }
      }

      if (isEmpty) {
        res.status(304).send("No data Found");
        return;
      }

      res.send(productData);
    });
});

app.get("/api/similarProduct", (req, res) => {
  const productId = req.query.productId;
  const url = `http://svcs.ebay.com/MerchandisingService?OPERATION-NAME=getSimilarItems&SERVICE-NAME=MerchandisingService&SERVICE-VERSION=1.1.0&CONSUMER-ID=${config.EBAY_API}&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&itemId=${productId}&maxResults=20`;

  request.get(url,
    (errorResponse, response, data) => {
      res.setHeader("Access-Control-Allow-Origin", "*");

      if (errorResponse) {
        res.status(304).send("No Records.");
        return;
      }
      data = JSON.parse(data);

      if (
        !data ||
        data === null ||
        !data.hasOwnProperty("getSimilarItemsResponse") ||
        !data.getSimilarItemsResponse.hasOwnProperty("ack") ||
        data.getSimilarItemsResponse.ack !== "Success" ||
        !data.getSimilarItemsResponse.hasOwnProperty("itemRecommendations") ||
        !data.getSimilarItemsResponse.itemRecommendations.hasOwnProperty("item") ||
        data.getSimilarItemsResponse.itemRecommendations.item.length <= 0
      ) {
        res.status(304).send("No Records.");
        return;
      }

      const similarProduct = {
        daysLeft: null,
        imageUrl: null,
        price: null,
        productId: null,
        shippingCost: null,
        title: null,
      };

      let result = [];

      let items = data.getSimilarItemsResponse.itemRecommendations.item;
      for (let i = 0; i < items.length; i++) {
        let item = items[i];
        const similarProductData = Object.assign({}, similarProduct);

        similarProductData.productId = item.itemId;
        if (item.hasOwnProperty("imageURL") && item.imageURL.length > 0) {
          similarProductData.imageUrl = item.imageURL;
        }

        if (item.hasOwnProperty("title") && item.title.length > 0) {
          similarProductData.title = item.title;
        }

        if (
          item.hasOwnProperty("buyItNowPrice") &&
          item.buyItNowPrice.hasOwnProperty("__value__") &&
          item.buyItNowPrice.__value__.length > 0
        ) {
          similarProductData.price = Number(item.buyItNowPrice.__value__);
          if (similarProductData.price === 0.0) {
            if (
              item.hasOwnProperty("currentPrice") &&
              item.currentPrice.hasOwnProperty("__value__") &&
              item.currentPrice.__value__.length > 0
            ) {
              similarProductData.price = Number(item.currentPrice.__value__);
            }
          }
        }

        if (
          item.hasOwnProperty("shippingCost") &&
          item.shippingCost.hasOwnProperty("__value__") &&
          item.shippingCost.__value__.length > 0
        ) {
          similarProductData.shippingCost = Number(item.shippingCost.__value__);
        }

        if (item.hasOwnProperty("timeLeft") && item.timeLeft.length > 0) {
          const timeLeft = item.timeLeft;
          const pIndex = timeLeft.indexOf("P") + 1;
          const dIndex = timeLeft.indexOf("D");
          similarProductData.daysLeft = Number(timeLeft.substring(pIndex, dIndex));
        }

        result.push(similarProductData);
      }

      res.send(result);
    });
});

app.get("/api/googleImages", (req, res) => {
  const query = req.query.query;
  const url = `https://www.googleapis.com/customsearch/v1?q=${query}&cx=${config.CX_ENGINE_ID}&imgSize=huge&imgType=news&num=8&searchType=image&key=${config.GOOGLE_SEARCH_ENGINE_KEY}`;
  
  request.get(url,
    (errorResponse, response, data) => {
      res.setHeader("Access-Control-Allow-Origin", "*");

      if (errorResponse) {
        res.status(304).send("No Records.");
        return;
      }
      data = JSON.parse(data);

      if (
        !data ||
        data === null ||
        !data.hasOwnProperty("items") ||
        data.items.length <= 0
      ) {
        res.status(304).send("No Records.");
        return;
      }

      let result = [];

      let items = data.items;
      for (let i = 0; i < items.length; i++) {
        let item = items[i];

        if (item.hasOwnProperty("link") && item.link.length > 0) {
          result.push(item.link);
        }
      }

      res.send(result);
    });
});

app.use(express.static(path.join(__dirname, "dist/Ebay-Product-Search-Angular")));
