<html>
    <head>
        <title>Product Search</title>
        <style>
            body {
                display: flex;
                justify-content: center;
                align-items: flex-start;
                overflow-x: hidden;
                height: 100vh;
            }

            #productSearch {
                border: 2px solid #cbcbcb;
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
        </style>
    </head>
    <body>
        <div id="productSearch">
            <div id="searchHeading">
                <i>Product Search</i>
            </div>
            <div id="searchForm">
                <form name="productSearchForm" onsubmit="return false;">
                    <div class="field">
                        <span class="bold">Keyword</span>
                        <input name="keyword" type="text" required/>
                    </div>
                    <div class="field">
                        <span class="bold">Category</span>
                        <select name="category">
                            <option value="all" selected>All Categories</option>
                            <option value="art">Art</option>
                            <option value="baby">Baby</option>
                            <option value="books">Books</option>
                            <option value="clothing">Clothing</option>
                            <option value="shoes">Shoes & Accessories</option>
                            <option value="computer">Computers/Tablets & Networking</option>
                            <option value="health">Health & Beauty</option>
                            <option value="music">Music</option>
                            <option value="games">Video Games & Consoles</option>
                        </select>
                    </div>
                    <div class="field">
                        <span class="bold">Condition</span>
                        <input type="checkbox" class="checkbox" name="condition[]" value="New"/>New
                        <input type="checkbox" class="checkbox" name="condition[]" value="Used"/>Used
                        <input type="checkbox" class="checkbox" name="condition[]" value="unspecified"/>Unspecified
                    </div>
                    <div class="field">
                        <span class="bold">Shipping Options</span>
                        <input type="checkbox" class="shipping" name="shipping[]" value="LocalPickupOnly"/>Local Pickup
                        <input type="checkbox" class="shipping" name="shipping[]" value="FreeShippingOnly"/>Free
                        Shipping
                    </div>
                    <div class="field" id="distanceDiv">
                        <input type="checkbox" name="nearbySearch" value="true" onchange="changeDistance(this.form)" onload="changeDistance(this.form)"/>
                        <span class="bold">Enable Nearby Search</span>
                        <span>
                            <input type="number" id="distanceValue" placeholder="10" min="1" name="distance"/>
                            <span class="bold" id="distanceMilesFromText">miles from</span>
                        </span>
                        <span id="distanceFromSpan">
                            <input type="radio" name="fromRadio" value="here" checked/>
                            <span class="disabled" id="distanceHereText">Here</span>
                            <br/>
                            <span>
                                <input type="radio" name="fromRadio" value="here"/>
                                <input type="text" name="zipcode" placeholder="zip code" required/>
                            </span>
                        </span>
                    </div>
                    <div id="formButtons" class="field">
                        <input type="submit" id="submitButton" value="Search"/>
                        <input type="reset" value="Clear"/>
                    </div>
                </form>
            </div>
        </div>

        <script>
            window.onload = function () {
                let productSearchForm = document.forms.productSearchForm;
                changeDistance(productSearchForm);

                let distanceRadioButtons = productSearchForm.fromRadio;
                for (let i = 0; i < distanceRadioButtons.length; i++) {
                    distanceRadioButtons[i].onclick = function () {
                        if (document.forms.productSearchForm.fromRadio[1].checked) {
                            productSearchForm.zipcode.disabled = false;
                        } else {
                            productSearchForm.zipcode.disabled = true;
                        }
                    };
                }

                productSearchForm.onreset = function () {
                    productSearchForm.nearbySearch.checked = false;
                    changeDistance(productSearchForm);
                }


            };

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
                }
            }

        </script>
    </body>
</html>