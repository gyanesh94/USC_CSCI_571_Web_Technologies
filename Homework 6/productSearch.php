<?php
set_error_handler(function ($severity, $message, $file, $line) {
    throw new ErrorException($message, $severity, $severity, $file, $line);
});

$error = false;
$error_message = "";
$type = null;
$result = null;
$similar_item = null;

function type_search($data) {
    global $app_id;

    $url = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=%s&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&paginationInput.entriesPerPage=20&keywords=%s";
    $url = sprintf($url, urlencode($app_id), urlencode($data->keyword));
    if ($data->category != "all") {
        $url = $url . "&categoryId=" . $data->category;
    }

    $filter_index = 0;

    if ($data->nearby) {
        $url = $url . "&buyerPostalCode=" . $data->zipcode;
        $url = $url . "&itemFilter($filter_index).name=MaxDistance";
        $url = $url . "&itemFilter($filter_index).value=" . $data->distance;
        $filter_index++;
    }

    $url = $url . "&itemFilter($filter_index).name=HideDuplicateItems";
    $url = $url . "&itemFilter($filter_index).value=True";
    $filter_index++;

    if (count($data->condition) != 0) {
        $url = $url . "&itemFilter($filter_index).name=Condition";
        for ($i = 0; $i < count($data->condition); $i++) {
            $url = $url . "&itemFilter($filter_index).value($i)=" . $data->condition[$i];
        }
        $filter_index++;
    }

    if (count($data->shipping) != 0) {
        for ($i = 0; $i < count($data->shipping); $i++) {
            $url = $url . "&itemFilter($filter_index).name=" . $data->shipping[$i];
            $url = $url . "&itemFilter($filter_index).value=True";
            $filter_index++;
        }
    }

    $json_content = null;
    try {
        $json_content = file_get_contents($url);
    } catch (Exception $e) {
        global $error;
        global $error_message;
        $error = true;
        $error_message = "Error while fetching from Search API";
    }

    return $json_content;
}

function type_item_detail($data) {
    global $app_id;

    $url = "http://open.api.ebay.com/shopping?callname=GetSingleItem&responseencoding=JSON&appid=%s&siteid=0&version=967&ItemID=%s&IncludeSelector=Description,Details,ItemSpecifics";
    $url = sprintf($url, urlencode($app_id), urlencode($data->itemId));

    $json_content = null;
    try {
        $json_content = file_get_contents($url);
    } catch (Exception $e) {
        global $error;
        global $error_message;
        $error = true;
        $error_message = "Error while fetching Item Detail";
    }

    return $json_content;
}

function similar_item($data) {
    global $app_id;

    $url = "http://svcs.ebay.com/MerchandisingService?OPERATION-NAME=getSimilarItems&SERVICE-NAME=MerchandisingService&SERVICE-VERSION=1.1.0&CONSUMER-ID=%s&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&itemId=%s&maxResults=8";
    $url = sprintf($url, urlencode($app_id), urlencode($data->itemId));

    $json_content = null;
    try {
        $json_content = file_get_contents($url);
    } catch (Exception $e) {
        //        global $error;
        //        global $error_message;
        //        $error = true;
        //        $error_message = "Error while fetching from similar item";
    }

    return $json_content;
}

if (isset($_POST['data'])) {
    $app_id = "GyaneshM-ProductS-PRD-c16db7c91-88e4ea5d";

    $data = json_decode($_POST['data']);
    print_r($data);

    $type = $data->type;
    if ($type == 'search') {
        $result = type_search($data);
    } else if ($type == 'item') {
        $result = type_item_detail($data);
        $similar_item = similar_item($data);
    }
}
?>

<style>
    body {
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: center;
        overflow-x: hidden;
        height: 100vh;
    }

    #productSearch {
        border: 3px solid #cbcbcb;
        background-color: #fafafa;
        padding-left: 8px;
        padding-right: 8px;
        width: 40%;
    }

    #searchHeading {
        width: 100%;
        font-size: 32px;
        text-align: center;
        border-bottom: 2px solid #cbcbcb;
        padding-bottom: 4px;
    }

    #searchForm {
        position: relative;
        left: 5px;
        top: 5px;
    }

    .bold {
        font-weight: bold;
    }

    .field {
        margin-bottom: 10px;
    }

    .checkbox {
        margin-left: 20px;
    }

    .shipping {
        margin-left: 43px;
    }

    #distanceValue {
        width: 50px;
        margin-left: 35px;
    }

    #distanceFromSpan {
        display: inline-block;
        margin-left: 5px;
    }

    #distanceDiv {
        display: flex;
    }

    .disabled {
        color: #999999;
        opacity: 0.8;
    }

    #formButtons {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 30px;
        margin-right: 50px;
    }

    #submitButton {
        margin-right: 10px;
    }

    .hidden {
        display: none;
    }

    #result {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: flex-start;
        margin-top: 20px;
        width: 100%;
    }

    #error {
        border: 2px solid #cbcbcb;
        background-color: #fafafa;
        text-align: center;
        width: 60%;
    }

    #itemTable {
        border-collapse: collapse;
        width: 90%;
        word-wrap: break-word;
    }

    #itemTable thead {
        text-align: center;
    }

    #itemTable td {
        border: 2px solid #cbcbcb;
    }

    #itemTable a {
        text-decoration: none;
        cursor: pointer;
    }

    #itemTable a:link, #itemTable a:visited {
        color: black;
    }

    #itemTable a:hover, #itemTable a:active {
        color: #aaaaaa;
        opacity: 0.7%;
    }

    #itemTable #imgCell {
        width: 90px;
        background-size: cover;
        padding: 0.5px;
    }

    #bottomPadding {
        margin: 10px;
        width: 100%;
        height: 20px;
    }

    #itemDetailHeading {
        font-size: 35px;
        font-weight: bold;
    }

    #itemDetailTable {
        border-collapse: collapse;
        word-wrap: break-word;
        max-width: 80%;
    }

    #itemDetailTable td {
        border: 2px solid #cbcbcb;
        padding-left: 10px;
        padding-right: 10px;
    }

    #itemDetailTable #imageCell {
        height: 200px;
    }

    #sellerMessageContainer {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: flex-start;
        margin-top: 30px;
        margin-bottom: 20px;
        width: 90%;
        min-height: 40px;
    }

    #sellerMessage {
        color: #999999;
        margin-bottom: 15px;
    }

    #sellerErrorMessage {
        background-color: #777777;
        text-align: center;
        width: 60%;
        font-weight: bold;
    }

    #sellerMessageiframe {
        border: 0;
        display: none;
    }

    #sellerMessageiframeContainer {
        display: flex;
    }

    .arrowImage {
        width: 40px;
        height: 15px;
    }
</style>
<html lang="en">
    <head>
        <title>Product Search</title>
    </head>
    <body>
        <div id="productSearch">
            <div id="searchHeading">
                <i>Product Search</i>
            </div>
            <div id="searchForm">
                <form name="productSearchForm" method="post" onsubmit="return false;">
                    <div class="field">
                        <span class="bold">Keyword</span>
                        <input name="keyword" type="text" required/>
                    </div>
                    <div class="field">
                        <span class="bold">Category</span>
                        <select name="category">
                            <option value="all" selected>All Categories</option>
                            <option value="550">Art</option>
                            <option value="2984">Baby</option>
                            <option value="267">Books</option>
                            <option value="11450">Clothing, Shoes & Accessories</option>
                            <option value="58058">Computers/Tablets & Networking</option>
                            <option value="26395">Health & Beauty</option>
                            <option value="11233">Music</option>
                            <option value="1249">Video Games & Consoles</option>
                        </select>
                    </div>
                    <div class="field">
                        <span class="bold">Condition</span>
                        <input type="checkbox" class="checkbox" name="condition[]" value="New"/>New
                        <input type="checkbox" class="checkbox" name="condition[]" value="Used"/>Used
                        <input type="checkbox" class="checkbox" name="condition[]" value="Unspecified"/>Unspecified
                    </div>
                    <div class="field">
                        <span class="bold">Shipping Options</span>
                        <input type="checkbox" class="shipping" name="shipping[]" value="LocalPickupOnly"/>Local Pickup
                        <input type="checkbox" class="shipping" name="shipping[]" value="FreeShippingOnly"/>Free
                        Shipping
                    </div>
                    <div class="field" id="distanceDiv">
                        <input type="checkbox" name="nearbySearch" value="true" onchange="changeDistance(this.form)"/>
                        <span class="bold">Enable Nearby Search</span>
                        <span>
                            <input type="number" id="distanceValue" placeholder="10" min="1" name="distance" disabled/>
                            <span class="bold disabled" id="distanceMilesFromText">miles from</span>
                        </span>
                        <span id="distanceFromSpan">
                            <input type="radio" name="fromRadio" value="here" checked disabled/>
                            <span class="disabled disabled" id="distanceHereText">Here</span>
                            <br/>
                            <span>
                                <input type="radio" name="fromRadio" value="here" disabled/>
                                <input type="text" name="zipcode" placeholder="zip code" required disabled/>
                            </span>
                        </span>
                    </div>
                    <div class="hidden">
                        <input type="text" name="hidden_zipcode"/>
                        <input type="text" name="data"/>
                    </div>
                    <div id="formButtons" class="field">
                        <input type="submit" id="submitButton" name="submitButton" value="Search" onclick="sendSearchRequest(this.form)" disabled/>
                        <input type="reset" value="Clear"/>
                    </div>
                </form>
            </div>
        </div>

        <div id="result"></div>

        <script>
            window.onload = function () {
                let productSearchForm = document.forms.productSearchForm;
                changeDistance(productSearchForm);
                getLocation(productSearchForm);

                let distanceRadioButtons = productSearchForm.fromRadio;
                for (let i = 0; i < distanceRadioButtons.length; i++) {
                    distanceRadioButtons[i].onclick = function () {
                        if (productSearchForm.fromRadio[1].checked) {
                            productSearchForm.zipcode.disabled = false;
                        } else {
                            productSearchForm.zipcode.value = "";
                            productSearchForm.zipcode.disabled = true;
                        }
                    };
                }

                productSearchForm.onreset = function () {
                    productSearchForm.nearbySearch.checked = false;
                    changeDistance(productSearchForm);
                };

                this.defaultData = null;

                <?php
                if (isset($_POST['data'])) {
                    echo "this.defaultData = " . $_POST['data'] . ";";
                }
                ?>

                if (this.defaultData) {
                    loadSearchData(this.defaultData, productSearchForm);
                }

                let type = null;
                let searchData = null;
                let itemDetail = null;
                let similarItem = null;

                <?php
                if ($error) {
                    $error_message = json_encode($error_message);
                    echo "showError($error_message);";
                } else {
                    if ($type == 'search') {
                        echo "type = 'search';";
                        if ($result) {
                            echo "searchData = $result;";
                        }
                    } else if ($type == 'item') {
                        echo "type = 'item';";
                        if ($result) {
                            echo "itemDetail = $result;";
                        }
                        if ($similar_item) {
                            echo "similarItem = $similar_item;";
                        }
                    }
                }
                ?>

                if (type === "search") {
                    showSearchResult(searchData);
                } else if (type === "item") {
                    showItemDetail(itemDetail, similarItem);
                }
            };

            function debug(text) {
                console.log(text);
            }

            function resetError() {
                let resultEle = document.getElementById("result");
                resultEle.innerHTML = "";
            }

            function showError(text) {
                let resultEle = document.getElementById("result");

                let errorElement = document.createElement('div');
                errorElement.id = "error";
                errorElement.innerText = text;
                resultEle.innerHTML = "";
                resultEle.appendChild(errorElement);
            }

            function imageError(parent) {
                parent.innerHTML = "N/A";
            }

            function getLocation(productSearchForm) {
                let jsonRequest = new XMLHttpRequest();
                jsonRequest.overrideMimeType("application/json");
                jsonRequest.open("GET", "http://ip-api.com/json", false);


                try {
                    jsonRequest.send();
                } catch (exp) {
                    showError("Error while obtaining Geolocation3");
                    productSearchForm.submitButton.disabled = false;
                    return null;
                }

                if (jsonRequest.status === 200) {
                    try {
                        let jsonData = JSON.parse(jsonRequest.responseText);
                        if (jsonData == null) {
                            showError("Geolocation API response is Empty");
                        }
                        productSearchForm.hidden_zipcode.value = jsonData.zip || "";
                    } catch (exp) {
                        showError("Error while parsing Geolocation response1");
                    }
                } else {
                    showError("Error while obtaining Geolocation2");
                }
                productSearchForm.submitButton.disabled = false;
                return null;
            }

            function loadSearchData(data, productSearchForm) {
                productSearchForm.keyword.value = data['keyword'];

                let category = productSearchForm.category;
                productSearchForm.category[0].selected = false;
                for (let i = 0; i < category.length; i++) {
                    if (data['category'] === productSearchForm.category[i].value) {
                        productSearchForm.category[i].selected = true;
                    }
                }

                let condition = productSearchForm["condition[]"];
                for (let i = 0; i < data['condition'].length; i++) {
                    for (let j = 0; j < condition.length; j++) {
                        if (condition[j].value === data['condition'][i]) {
                            condition[j].checked = true;
                        }
                    }
                }

                let shipping = productSearchForm["shipping[]"];
                for (let i = 0; i < data['shipping'].length; i++) {
                    for (let j = 0; j < shipping.length; j++) {
                        if (shipping[j].value === data['shipping'][i]) {
                            shipping[j].checked = true;
                        }
                    }
                }

                if (data["nearby"]) {
                    productSearchForm.nearbySearch.checked = data["nearby"];
                    if (data["distance"] !== "10") {
                        productSearchForm.distance.value = Number(data["distance"]);
                    }
                    if (!data["hereChecked"]) {
                        productSearchForm.fromRadio[1].checked = true;
                        productSearchForm.zipcode.value = data["zipcode"];
                    }
                    changeDistance(productSearchForm);
                }

            }

            function changeDistance(productSearchForm) {
                let isChecked = productSearchForm.nearbySearch.checked;
                productSearchForm.distance.disabled = !isChecked;
                productSearchForm.zipcode.disabled = !(isChecked && productSearchForm.fromRadio[1].checked);

                let distanceRadioButtons = productSearchForm.fromRadio;
                for (let i = 0; i < distanceRadioButtons.length; i++) {
                    distanceRadioButtons[i].disabled = !isChecked;
                }

                if (isChecked) {
                    document.getElementById("distanceHereText").classList.remove("disabled");
                    document.getElementById("distanceMilesFromText").classList.remove("disabled");
                } else {
                    document.getElementById("distanceHereText").classList.add("disabled");
                    document.getElementById("distanceMilesFromText").classList.add("disabled");
                    productSearchForm.zipcode.value = "";
                    productSearchForm.distance.value = "";
                    productSearchForm.fromRadio[1].checked = false;
                    productSearchForm.fromRadio[0].checked = true;
                }
            }

            function validFormData(productSearchForm) {
                let keyword = productSearchForm.keyword.value;
                if (keyword.length === 0) {
                    return false;
                }
                keyword = keyword.trim();
                if (keyword.length === 0) {
                    showError("Keyword should contains alphabets or number");
                    return false;
                }
                if (productSearchForm.nearbySearch.checked && productSearchForm.fromRadio[1].checked) {
                    let zipcode = productSearchForm.zipcode.value.trim();
                    if (zipcode.length === 0) {
                        return false
                    }

                    let re = /^\d{5}$/;
                    if (zipcode.match(re)) {
                        return true;
                    }

                    showError("Zipcode is invalid");
                    return false;
                }

                return true;
            }

            function makeSearchData(productSearchForm) {
                let data = {};
                data['type'] = 'search';
                data['keyword'] = productSearchForm.keyword.value.trim();
                data['category'] = productSearchForm.category[productSearchForm.category.selectedIndex].value;

                let condition = productSearchForm["condition[]"];
                data['condition'] = [];
                for (let i = 0; i < condition.length; i++) {
                    if (condition[i].checked) {
                        data['condition'].push(condition[i].value);
                    }
                }

                let shipping = productSearchForm["shipping[]"];
                data['shipping'] = [];
                for (let i = 0; i < shipping.length; i++) {
                    if (shipping[i].checked) {
                        data['shipping'].push(shipping[i].value);
                    }
                }

                data["nearby"] = productSearchForm.nearbySearch.checked;
                data["distance"] = productSearchForm.distance.value || "10";
                data["hereChecked"] = productSearchForm.fromRadio[0].checked;
                if (productSearchForm.fromRadio[1].checked) {
                    data["zipcode"] = productSearchForm.zipcode.value;
                } else {
                    data["zipcode"] = productSearchForm.hidden_zipcode.value;
                }

                return data;
            }

            function sendSearchRequest(productSearchForm) {
                resetError();
                if (validFormData(productSearchForm)) {
                    let data = makeSearchData(productSearchForm);
                    productSearchForm.data.value = JSON.stringify(data);
                    document.productSearchForm.submit();
                }
            }

            function showSearchResult(data) {
                if (!data || !data.hasOwnProperty("findItemsAdvancedResponse")) {
                    showError("No data Found");
                    return;
                }

                data = data.findItemsAdvancedResponse[0];
                if (!data.hasOwnProperty("ack")) {
                    showError("No data found");
                    return;
                }
                if (data["ack"][0] !== "Success") {
                    if (data.hasOwnProperty("errorMessage") && data.errorMessage.length > 0) {
                        let errorMessage = data.errorMessage[0];
                        if (errorMessage.hasOwnProperty("error") && errorMessage.error.length > 0) {
                            let error = errorMessage.error[0];
                            if (error.hasOwnProperty("message") && error.message.length > 0) {
                                showError(error.message[0]);
                                return;
                            }
                        }
                    }
                    showError("Data fetching not successful");
                    return;
                }
                if (!data.hasOwnProperty("searchResult") || data.searchResult.length === 0 || data.searchResult[0]["@count"] === "0") {
                    showError("No Records has been found");
                    return;
                }

                let items = data.searchResult[0]["item"];
                let html = "<table id='itemTable'>" +
                    "<thead class='bold'>" +
                    "<td>Index</td>" +
                    "<td>Photos</td>" +
                    "<td>Name</td>" +
                    "<td>Price</td>" +
                    "<td>Zip code</td>" +
                    "<td>Condition</td>" +
                    "<td>Shipping Option</td>" +
                    "</thead>" +
                    "<tbody>";

                for (let i = 0; i < items.length; i++) {
                    let item = items[i];
                    html += "<tr>";
                    html += "<td id='indexCell'>" + (i + 1) + "</td>";

                    if (item.hasOwnProperty("galleryURL") && item.galleryURL.length > 0) {
                        html += "<td id='imgCell'><img id='imgCell' src='" + item.galleryURL[0] + "' onerror='imageError(this.parentElement)' /></td>";
                    } else {
                        html += "<td id='imgCell'>N/A</td>";
                    }

                    if (item.hasOwnProperty("title") && item.title.length > 0) {
                        html += "<td id='titleCell'><a onclick='getItemData(" + item.itemId[0] + ")'>" + item.title[0] + "</a></td>";
                    } else {
                        html += "<td id='titleCell'>N/A</td>";
                    }

                    let valid = false;
                    if (item.hasOwnProperty("sellingStatus") && item.sellingStatus.length > 0) {
                        let sellingStatus = item.sellingStatus[0];
                        if (sellingStatus.hasOwnProperty("currentPrice") && sellingStatus.currentPrice.length > 0) {
                            let currentPrice = sellingStatus.currentPrice[0];
                            if (currentPrice.hasOwnProperty("__value__") && currentPrice["__value__"] !== "0.0") {
                                html += "<td id='priceCell'>$" + currentPrice['__value__'] + "</td>";
                                valid = true;
                            }
                        }
                    }
                    if (!valid) {
                        html += "<td id='priceCell'>N/A</td>";
                    }

                    if (item.hasOwnProperty("postalCode") && item.postalCode.length > 0) {
                        html += "<td id='zipcodeCell'>" + item.postalCode[0].trim() + "</td>";
                    } else {
                        html += "<td id='zipcodeCell'>N/A</td>";
                    }

                    valid = false;
                    if (item.hasOwnProperty("condition") && item.condition.length > 0) {
                        let condition = item.condition[0];
                        if (condition.hasOwnProperty("conditionDisplayName") && condition.conditionDisplayName.length > 0) {
                            html += "<td id='conditionCell'>" + condition.conditionDisplayName[0].trim() + "</td>";
                            valid = true;
                        }
                    }
                    if (!valid) {
                        html += "<td id='conditionCell'>N/A</td>";
                    }

                    valid = false;
                    if (item.hasOwnProperty("shippingInfo") && item.shippingInfo.length > 0) {
                        let shippingInfo = item.shippingInfo[0];
                        if (shippingInfo.hasOwnProperty("shippingServiceCost") && shippingInfo.shippingServiceCost.length > 0) {
                            let shippingServiceCost = shippingInfo.shippingServiceCost[0];
                            if (shippingServiceCost.hasOwnProperty("@currencyId") && shippingServiceCost.hasOwnProperty("__value__")) {
                                let value = shippingServiceCost['__value__'];
                                if (Number(value) === 0) {
                                    html += "<td id='shippingCell'>Free Shipping</td>";
                                } else {
                                    html += "<td id='shippingCell'>$" + value + "</td>";
                                }
                                valid = true;
                            }
                        }
                    }
                    if (!valid) {
                        html += "<td id='shippingCell'>N/A</td>";
                    }

                    html += "</tr>";
                }

                html += "</tbody>" +
                    "</table>";

                html += "<div id='bottomPadding'></div>";

                let resultEle = document.getElementById("result");
                resultEle.innerHTML = html;
            }

            function showItemDetail(data, similarItem) {
                debug(data);
                debug(similarItem);

                let html = createItemDetail(data);
                html += createSellerMessage(data);

                let resultEle = document.getElementById("result");
                resultEle.innerHTML = html;
            }

            function getItemData(itemId) {
                if (!itemId) {
                    showError("This item doesn't have itemId to get details");
                    return;
                }
                let productSearchForm = document.forms.productSearchForm;
                let data = this.defaultData;
                data.type = "item";
                data.itemId = itemId;
                productSearchForm.data.value = JSON.stringify(data);
                document.productSearchForm.submit();
            }

            function createItemDetail(data) {
                let errorMessage = "";
                if (!data) {
                    errorMessage = "<div id='error'>No data Found</div>";
                    return errorMessage;
                }
                if (!data.hasOwnProperty("Ack")) {
                    errorMessage = "<div id='error'>No data Found</div>";
                    return errorMessage;
                }
                if (data["Ack"] !== "Success") {
                    if (data.hasOwnProperty("Errors") && data.Errors.length > 0) {
                        let errors = data.Errors[0];
                        if (errors.hasOwnProperty("LongMessage")) {
                            errorMessage = "<div id='error'>" + errors.LongMessage.trim() + "</div>";
                            return errorMessage;
                        }
                        if (errors.hasOwnProperty("ShortMessage")) {
                            errorMessage = "<div id='error'>" + errors.ShortMessage.trim() + "</div>";
                            return errorMessage;
                        }
                    }
                    errorMessage = "<div id='error'>Data fetching not successful</div>";
                    return errorMessage;
                }
                if (!data.hasOwnProperty("Item")) {
                    errorMessage = "<div id='error'>No data Found</div>";
                    return errorMessage;
                }

                let isEmpty = true;
                let item = data.Item;
                let html = "<div id='itemDetailHeading'>Item Details</div>";

                html += "<table id='itemDetailTable'>" +
                    "<tbody>";

                if (item.hasOwnProperty("PictureURL") && item.PictureURL.length > 0) {
                    html += "<tr>";
                    html += "<td class='bold'>Photo</td>";
                    html += "<td id='imageCell'><img id='imageCell' src='" + item.PictureURL[0] + "' onerror='imageError(this.parentElement)' /></td>";
                    html += "</tr>";
                    isEmpty = false;
                }

                if (item.hasOwnProperty("Subtitle") && item.Subtitle.length > 0) {
                    html += "<tr>";
                    html += "<td class='bold'>Subtitle</td>";
                    html += "<td>" + item.Subtitle.trim() + "</td>";
                    html += "</tr>";
                    isEmpty = false;
                }

                if (item.hasOwnProperty("Title") && item.Title.length > 0) {
                    html += "<tr>";
                    html += "<td class='bold'>Title</td>";
                    html += "<td>" + item.Title.trim() + "</td>";
                    html += "</tr>";
                    isEmpty = false;
                }

                if (item.hasOwnProperty("CurrentPrice") && item.CurrentPrice.hasOwnProperty("Value")) {
                    html += "<tr>";
                    html += "<td class='bold'>Price</td>";
                    html += "<td>" + item.CurrentPrice.Value + " USD</td>";
                    html += "</tr>";
                    isEmpty = false;
                }

                if (
                    item.hasOwnProperty("Location") &&
                    item.hasOwnProperty("PostalCode") &&
                    item.Location.length > 0 &&
                    item.PostalCode.length > 0
                ) {
                    html += "<tr>";
                    html += "<td class='bold'>Location</td>";
                    html += "<td>" + item.Location.trim() + ", " + item.PostalCode.trim() + "</td>";
                    html += "</tr>";
                    isEmpty = false;
                } else if (item.hasOwnProperty("Location") && item.Location.length > 0) {
                    html += "<tr>";
                    html += "<td class='bold'>Location</td>";
                    html += "<td>" + item.Location.trim() + "</td>";
                    html += "</tr>";
                    isEmpty = false;
                } else if (item.hasOwnProperty("PostalCode") && item.PostalCode.length > 0) {
                    html += "<tr>";
                    html += "<td class='bold'>Location</td>";
                    html += "<td>" + item.PostalCode.trim() + "</td>";
                    html += "</tr>";
                    isEmpty = false;
                }

                if (item.hasOwnProperty("Seller") && item.Seller.hasOwnProperty("UserID") && item.Seller.UserID.length > 0) {
                    html += "<tr>";
                    html += "<td class='bold'>Seller</td>";
                    html += "<td>" + item.Seller.UserID.trim() + "</td>";
                    html += "</tr>";
                    isEmpty = false;
                }

                if (item.hasOwnProperty("ReturnPolicy") && item.ReturnPolicy.hasOwnProperty("ReturnsAccepted")) {
                    let message = null;
                    if (
                        (
                            item.ReturnPolicy.ReturnsAccepted === "Returns Accepted" ||
                            item.ReturnPolicy.ReturnsAccepted === "ReturnsAccepted"
                        ) &&
                        item.ReturnPolicy.hasOwnProperty("ReturnsWithin") &&
                        item.ReturnPolicy.ReturnsWithin.length > 0
                    ) {
                        message = "Returns Accepted within " + item.ReturnPolicy.ReturnsWithin;
                    } else if (item.ReturnPolicy.ReturnsAccepted === "ReturnsNotAccepted") {
                        message = "Returns not accepted";
                    }
                    if (message !== null) {
                        html += "<tr>";
                        html += "<td class='bold'>Return Policy(US)</td>";
                        html += "<td>" + message.trim() + "</td>";
                        html += "</tr>";
                        isEmpty = false;
                    }
                }

                if (
                    item.hasOwnProperty("ItemSpecifics") &&
                    item.ItemSpecifics.hasOwnProperty("NameValueList") &&
                    item.ItemSpecifics.NameValueList.length > 0
                ) {
                    let itemSpecifics = item.ItemSpecifics.NameValueList;
                    for (let i = 0; i < itemSpecifics.length; i++) {
                        let itemSpecific = itemSpecifics[i];
                        if (
                            itemSpecific.hasOwnProperty("Name") &&
                            itemSpecific.hasOwnProperty("Value") &&
                            itemSpecific.Name.length > 0 &&
                            itemSpecific.Value.length > 0
                        ) {
                            html += "<tr>";
                            html += "<td class='bold'>" + itemSpecific.Name + "</td>";
                            html += "<td>" + itemSpecific.Value[0].trim() + "</td>";
                            html += "</tr>";
                            isEmpty = false;
                        }
                    }
                }

                html += "</tbody>" +
                    "</table>";

                if (isEmpty) {
                    errorMessage = "<div id='error'>No data Found</div>";
                    return errorMessage;
                }

                return html;
            }

            function sellerMessageClicked(img) {
                let sellerClickShowMessage = "click to show seller message";
                let sellerClickShowURL = "http://csci571.com/hw/hw6/images/arrow_down.png";
                let sellerClickHideMessage = "click to hide seller message";
                let sellerClickHideURL = "http://csci571.com/hw/hw6/images/arrow_up.png";

                let sellerMessage = document.getElementById("sellerMessage");
                let sellerMessageiframe = document.getElementById("sellerMessageiframe");


                if (img.ishidden) {
                    sellerMessageiframe.style.display = "block";
                    sellerMessage.innerText = sellerClickHideMessage;
                    img.src = sellerClickHideURL;
                    img.ishidden = false;
                    console.dir(sellerMessageiframe.contentWindow.document.body);
                    sellerMessageiframe.width = sellerMessageiframe.contentWindow.document.body.scrollWidth;
                    sellerMessageiframe.height = sellerMessageiframe.contentWindow.document.body.scrollHeight + 50;
                } else {
                    sellerMessageiframe.style.display = "none";
                    sellerMessage.innerText = sellerClickShowMessage;
                    img.src = sellerClickShowURL;
                    img.ishidden = true;
                }
            }

            function createSellerMessage(data) {
                let html = "<div id='sellerMessageContainer'>" +
                    "<div id='sellerMessage'>click to show seller message</div>" +
                    "<img id='sellerArrowImage' src='http://csci571.com/hw/hw6/images/arrow_down.png' class='arrowImage' onclick='sellerMessageClicked(this)'/>";
                let srcDoc = "";
                if (
                    !data ||
                    !data.hasOwnProperty("Ack") ||
                    data["Ack"] !== "Success" ||
                    !data.hasOwnProperty("Item") ||
                    !data.Item.hasOwnProperty("Description") ||
                    data.Item.Description.length <= 0
                ) {
                    srcDoc = "<div id='sellerErrorMessage'>No Seller Message found.</div>"
                } else {
                    srcDoc = data.Item.Description;
                }

                debug(srcDoc);

                html += "<div id='sellerMessageiframeContainer'><iframe  scrolling='no' id='sellerMessageiframe' srcdoc='" + srcDoc + "'></iframe></div>";
                html += "</div>";

                return html;
            }
        </script>
    </body>
</html>
<?php
restore_error_handler();
?>
