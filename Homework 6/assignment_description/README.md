- ## Form Data

  - users must **enter a keyword**

  - choose what ***Category*** of the product they want to search (categories include Art, Baby, Books, Clothing, Shoes & Accessories, Computers/Tablets & Networking, Health & Beauty, Music and Video Games & Consoles) from a **drop-down list**

    - The **default** value for the “Category” drop-down list is **“All Categories”**, which covers all of the “types” provided by the eBay Finding API.

  - **users** can also choose the **Condition** of the product they want based on new, used or unspecified

    - **default** value for Condition is all for which none have to be checked 

  - **Shipping** option for the **users** (Local Pickup and Free Shipping) which they choose as a filter.

    - The **default** option is all for which neither of the options need to checked

  - the **users** have option of **enabling nearby search** where they can enter **the distance (in miles),** which is the **radius for the search where the center point is “Here”** (zip code of current location returned from ip-api.com HTTP API) or the **zip code** entered in the edit box.

    - Only when the “**Enable Nearby Search**” is **checked**, the **options** to **put the distance** and select its **center point should be enabled** (it is initially greyed out and disabled when the page loads).
    - When **Enable option** is unchecked fields should **go to default value**. @440
    - When the “**Here**” radio button is **selected**, the **zip code edit box must be disabled** and it  must go back to placeholder values @440.
    - Note @352:
      - " Use HTML5 “placeholder” to show the string “zip code” in the zip code edit box and “10” in the distance edit box as the initial values." This is for the initial value when page loads but nothing is specified about performing a second query.
      - The video also does not show a situation like the one described. 
      - I will assume that the placeholder value should be displayed when here is selected and form is resubmitted. But will leave the old zipcode displayed if the user selects here but has not submitted the form yet.
    - When the **zip code** **edit box** is selected, it is a **required field**, and a **5-digit zip code** must be entered. *Note: value Zipcode error handling to be done on server side see [**Error Handling 1**](#Error%20Handling%201)*
    - The **default distance is 10 miles from the chosen location**. Use **HTML5 “placeholder”** to **show** the string **“zip code”** in the zip code edit box and **“10” in the distance edit box as the initial values.** An example is shown in Figure 1.
      - ![figure 1](images/figure%201.png)

  - **Search button**

    - You should use the ip-api.com **HTTP API** (See [hint 3.2](#Get%20geolocation%20using%20IP-API.com)) to fetch the **user’s geolocation**

      - after which the **search button should be enabled** (it is **initially greyed** out and **disabled** when the page loads)

    - The button must be **disabled** while the **page is fetching the user’s geolocation** and must be **enabled** once the **geolocation is obtained**. An example of valid input is shown in Figure 2.

      - ![figure 2](images/figure%202.png)

    - Once the user has **provided valid input**, your **client script (written in JavaScript**) should **send a request to your server script productSearch.php** with the form inputs. You can use *either GET or POST* to transfer the form data to the server script.

    - The **PHP server script** will **retrieve** the form inputs, **reformat** them to the syntax of the API and **send them to the eBay Finding API**.

    - #### Error Handling 1

      - If the user clicks on the search button **without providing a value in the “Keyword” field or “zip code” edit box**, you should show an **error “tooltip” that indicates which field is missing**. Examples are shown in Figure 3(a) and 3(b). If the input zip code is invalid, the page should display a corresponding error massage as shown in Figure 3(c).
      - ![figure 3(images/figure%203(a).png)](images/figure%203(a).png)
      - ![figure 3(images/figure%203(b).png)](images/figure%203(b).png)
      - ![figure 3(images/figure%203(c).png)](images/figure%203(c).png)

  - **Clear button**

    - This button **must clear the result area** (below the search area) and **set all form fields to the default values in the search area**.
    - The clear operation must be **done using a JavaScript function**.

  

  ## Displaying Products Results Table

  - The **eBay Finding API service** is documented here: 

    - https://developer.ebay.com/DevZone/finding/Concepts/FindingAPIGuide.html

  - The **eBay Finding API service to “make a call”** is documented here:

    - https://developer.ebay.com/DevZone/finding/Concepts/MakingACall.html

  - The eBay Finding API **service expects** the following **parameters**:

    - **OPERATION-NAME**: Set this field to be ‘findItemsAdvanced’.

    - **SERVICE-VERSION**: Set this field to be ‘1.0.0’.

    - **SECURITY-APPNAME**: Your application's API key. This key identifies your application
      for purposes of quota management.

    - **RESPONSE-DATA-FORMAT**: Set this field to ‘JSON’.

    - **REST-PAYLOAD**: Add it to the API call.

    - **paginationInput.entriesPerPage**: Set this to 20 to limit the number of results for a specific
      search.

    - **keywords**: A term to be matched against all content that eBay has indexed for this place,
      including name of the product to be searched.

    - **categoryId**: Filters the results to products matching the specified type id. Only one category
      may be specified (see Table 1). Searching without this field means searching in all categories.

      - ![table 1](images/table%201.png)

    - There are **4 filters**, namely **Condition, Shipping Options, MaxDistance, HideDuplicateItems**.

      - **Every filter** should have **two parameters** (name and value).

      - When listing the filters, they should be indexed starting from ZERO. An Example of listing two parameters:

        - itemFilter[0].name=filter1NAME&itemFilter[0].value=filter1Value&itemFilter[1].name=filt er2NAME&itemFilter[1].value=filter2Value.

      - If there are multiple values for a filter append it to the url using & operator:

        - itemFilter[0].name=filter1NAME&itemFilter[0].value=filter1V alue&itemFilter[0].value=filt
          er1V alue.

      - **HideDuplicateItems** always set to **true** @414

      - **Condition**

        - https://developer.ebay.com/devzone/finding/callref/types/ItemFilterType.html

        - ```
           &itemFilter(0).name=Condition
           &itemFilter(0).value(0)=New
           &itemFilter(0).value(1)=2000
           &itemFilter(0).value(2)=2500
          ```

    - **buyerPostalCode**: **Zip code** of where the **product needs to be searched**. You can locate items that have been listed for nearby-markets only by specifying a buyerPostalCode and item filters and MaxDistance.

    - **MaxDistance**: By **default, it is set to 10**. The user can set it to any number which specifies the radius from his location.

      - Should we check the distance value? I believe displaying such error messages is an implementation detail. You should check what are the acceptable data type and values from the API documentation. @344
      - MaxDistance has to work with buyerPostalCode. Otherwise, there is no meaning for this filter @414

  - An **example of an HTTP request to the eBay Finding API** that searches for the products related to USC within a 10 miles radius from the user’s current location is shown below:

    - http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=[YOUR_APP_ID]&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&paginationInput.entriesPerPage=20&keywords=usc&itemFilter.name=FreeShippingOnly&itemFilter.value=true&itemFilter.name=LocalPickupOnly&itemFilter.value=true&itemFilter.name=Condition&itemFilter.value=New&itemFilter.value=Used&itemFilter.value=Unspecified&buyerPostalCode=90007&itemFilter.name=MaxDistance&itemFilter.value=10&itemFilter.name=HideDuplicateItems&itemFilter.value=true
      - The JSON returned from eBay may contains one item multiple times. So add an additional item filter called "HideDuplicateItems" and set the value to "true" to avoid that. This change will save you a lot of time at homework8. @361
    - @364
      - http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=[APP-ID]&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&paginationInput.entriesPerPage=20&keywords=iphone&buyerPostalCode=90007&itemFilter(0).name=MaxDistance&itemFilter(0).value=10&itemFilter(1).name=FreeShippingOnly&itemFilter(1).value=true&itemFilter(2).name=LocalPickupOnly&itemFilter(2).value=true&itemFilter(3).name=HideDuplicateItems&itemFilter(3).value=true&itemFilter(4).name=Condition&itemFilter(4).value(0)=New&itemFilter(4).value(1)=Used&itemFilter(4).value(2)=Unspecified
        - Many students have problems with the eBay finding API. If you cannot get correct responses passing itemFilter as the one described in the homework description, try passing them as an array. Since the sample url in description works fine with my app-id, it's hard for me to find where the problem is.
        - If you meet such problem (like getting error message or incorrect return items), do not spending too much time on that, switch to pass itemFilter as an array. It's more stable. And if you do that, follow the below rules:
        - Use parenthetic notation to enumerate the entries in the array
        - Start the array index with zero ("0") for the first array entry
        - Keep all array entries together (i.e., do not insert other fields between array entries)

  - **Figure 5** shows an example of the corresponding JSON response returned by the eBay Finding API **service response**.

    - ![figure 5](images/figure%205.png)

  - ##### Important 1

    - The PHP script (i.e., productSearch.php) **should pass the returned JSON object to the client side unmodified or parse the returned JSON** and extract useful fields and pass these fields to the client side in JSON format.
    - You should **use JavaScript to parse the JSON object** and **display the results in a tabular format**. A sample output is shown in **Figure 6**.
      - ![figure 6](images/figure%206.png)

  - The **displayed table includes seven columns**:

    - Index, Photo, Name, Price, Zip code, Condition and Shipping Option.

  - ##### Important 2

    - If **returned JSON object is missing a certain key value pair, display “N/A”** in the corresponding field.
    - If the **API service returns an empty result set**, the page should display “**No records have been found**” as shown in Figure 7.
      - ![figure 7](images/figure%207.png)

  - When the search result **contains at least one record**, you need to **map the data extracted from the**
    **API result to render the HTML** result table as described in Table 2.

    - ![table 2_1](images/table%202_1.png)

      ![table 2_2](images/table%202_2.png)

  

  ## Displaying Product Details ([Product details](#Product%20details) and [Seller Message](#Seller%20Message) and [Similar Items](#Similar%20Items))

  ### Product details

  - In the **search result table**, if the **user clicks on the name of a product**, the page should make a request for the detailed information using the eBay shopping API documented at:

    - https://developer.ebay.com/devzone/shopping/docs/Concepts/ShoppingAPI_FormatOverview.html
    - https://developer.ebay.com/devzone/shopping/docs/CallRef/GetSingleItem.html

  - To **retrieve the details of a single item**, the **request** needs the following **parameters** (output should be JSON):
    • **callName**: Set it to “GetSingleItem” to get information for a specific product.
    • **responseencoding**: Set it to “JSON” to get a JSON response.
    • **appid**: Your application's API key. This key identifies your application for purposes of
    quota management.
    • **siteid**: Set it to ‘0’ for siteId purposes.
    • **version**: Set it to ‘967’ for API version purposes.
    • **ItemId**: It is the “itemId” of the product the user clicked.
    • **IncludeSelector**: Set it to “Description,Details,ItemSpecifics” to get required fields for that
    product.

  - An **example** of an HTTP request to the **eBay Shopping API** is shown below:

    - http://open.api.ebay.com/shopping?callname=GetSingleItem&responseencoding=JSON&appid=[APPID]&siteid=0&version=967&ItemID=[ITEMID]&IncludeSelector=Description,Details,ItemSpecifics

  - **Figure 8** shows a sample corresponding response.

    - ![figure 8](images/figure%208.png)

  - ##### Important 3

    - The **PHP script (i.e., productSearch.php)** should pass the returned **JSON object to the client side unmodified or parse the returned JSON** and **extract useful fields and pass these fields to the client side in JSON format**.

    - You should **use JavaScript to parse the JSON object** and display the results in a similar format as Figure 9.

      - ![figure 9(images/figure%209(a).png)](images/figure%209(a).png)

        ![figure 9(images/figure%209(b).png)](images/figure%209(b).png)

  - ##### Important 4

    - If the returned **JSON stream doesn’t contain certain fields**, those **fields will not appear on the detail page**.
      - A sample output is shown in Figure 9. **Figure 9(a) shows a result with all fields,** **Figure 9(b) shows a result with missing fields such as “Subtitle” and other item specific fields.**

  - When the **search result contains at least one field**, you need to **map the data extracted from the API result to render the HTML** result table as described in Table 3.

    - ![table 3.png](images/table%203.png)

  - For **Location** Attribute

    - Display what you get, “partial” location is fine (I.e only address or zipcode), and handle the missing filed for not showing error in console @436

  - If the **feature value is an array** just show the **first value** @314

  - For **Return Policy** -  If returns are not accepted, then I write a table row indicating that, rather than ignoring it @518

  ------

  ### Seller Message

  - Regarding the Seller Message on toggling Click to show seller message:

  - To retrieve the Seller Message, you should use the **IFRAME tag to implement it**. The **height of that message is dynamic depending on the height of the inner html page** embedded in it.

  - The **information to be embedded** in the IFRAME tag is the “**Description**” attribute which is present in the “**Item” object**.

  - An example of a Seller Message for an iPhone as a keyword is given below in Figure 11:

    - ![figure 11.png](images/figure%2011.png)

  - The details information includes two sub-sections: **Seller Message and Similar Items** which are by **default hidden (i.e., collapsed)** (as shown in Figure 12).

    - http://csci571.com/hw/hw6/images/arrow_up.png
    - http://csci571.com/hw/hw6/images/arrow_down.png

  - - ![figure 12.png](images/figure%2012.png)

  - The details information should over-write the result table and needs to be displayed under the search form.

  - ##### Important 5

    - When the **user** clicks on the button, the **“seller message” sub-section should be expanded,** and the “**similar items” sub-section should be hidden (if it is open) and vice versa** (see the video for the behavior).

  - The “seller message” sub-section should display the seller message, as shown in Figure 13.

    - ![figure 13.png](images/figure%2013.png)
    - 

  ------

  ### Similar Items

  - The “**similar products photos**” sub-section should display all photos (as shown in Figure 14) in a
    tabular format.

    - ![figure 14.png](images/figure%2014.png)

  - On **clicking** the button to **show similar items**, the page should request the detailed information
    using the **eBay Merchandising API documented** at:

    - https://developer.ebay.com/devzone/merchandising/docs/Concepts/MerchandisingAPI_FormatOverview.html
    - In HW description it says "On clicking the button to show similar items, the page should request the detailed information using the eBay Merchandising API". Is it fine if we get similar items details when we query for product details? or should we implement as it says in the description?
      - **Reply** both are fine @345

  - To retrieve details of **similar items**, the **request** needs the following **parameters** (output should be JSON):

    - **OPERATION-NAME**: Set it to “getSimilarItems” to get information for a related product.
    - **SERVICE-NAME**: Set it to “MerchandisingService” to specify Merchandising API calls.
    - **SERVICE-VERSION**: Set it to 1.1.0 for API version support.
    - **CONSUMER-ID**: Your application's API key. This key identifies your application for
      purposes of quota management.
    - **RESPONSE-DATA-FORMAT**: Set it to ‘JSON’.
    - **REST-PAYLOAD**: Add it to the API call.
    - **itemId**: Set it to “itemid” of the product the user clicked.
    - **maxResults**: Set it to 8 to limit the related products to 8 in a row.

  - An **example** of an HTTP request to the **eBay Merchandise API** that searches for similar products to the product clicked initially is shown below:

    - http://svcs.ebay.com/MerchandisingService?OPERATION-NAME=getSimilarItems&SERVICE-NAME=MerchandisingService&SERVICE-VERSION=1.1.0&CONSUMER-ID=[Your_APP_ID]&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&itemId=292862774875&maxResults=8

  - Figure 15 shows a sample corresponding response

    - ![figure 15.png](images/figure%2015.png)

  - ##### Important 6

    - If the **API service returns an empty result set,** the page should display “**No Seller Message Found**” instead of the **Seller Message section and should display “No Similar Items Found” instead of Similar Items section.** Sample outputs are shown in Figure 16 and Figure 17.
      - ![figure 16.png](images/figure%2016.png)
      - ![figure 17.png](images/figure%2017.png)

  - ##### Important 7

    - You must use **PHP server scripts to request all JSON objects** **except** when
      calling the **ip-api.com API** which should be called on the client side using
      **JavaScript**.
    - **Expanding or hiding sub-areas** should be implemented using **JavaScript** and
      **you are not allowed to use jQuery** for this exercise.

  

  ## Saving Previous Inputs

  - In addition to **displaying the results**, the **web page should maintain the provided values**.
    - For example, if a user searches for “Keyword: USC, Category: Books, Condition: Used, Shipping Options: Free Shipping, Enable Nearby Search with 15 miles from Here”, the user should see what was provided in the search form when displaying the results.
  - In addition, **when clicking on a “Product”**, the **page should display the information about the product, seller message and similar products and keep the values provided in the search form**.
  - It follows that you **need to keep the whole search box/input fields and buttons unmodified even while displaying results/errors**.
  - In summary, the search mechanism to be implemented behaves as follows:
    - Based on the **query in the search form**, construct a web service URL to **retrieve the output from the eBay API service**.
    - Pass the (possibly edited) JSON to the client side and parse JSON using JavaScript.
    - Display the product information and seller message along with the similar items in the
      proper format.

  

  ## Error Handlings

  - [Error Handling 1](#Error%20Handling%201) Related to search button

  - It seems some items we get from the **Merchandising API** are no longer existent.

    - When I click on them, I received an error response says "RequestError. Invalid or non-existent item ID.". What should we do to in this case? Should we display an error message like "No Item details have been found" under the form?

    - **Reply** *Display a proper error message for this case.* @326

    - Error Description:

      - Response:

        {
            Timestamp: "2019-02-11T06:58:13.735Z",
            Ack: "Failure",
            Errors: [ 
                {
                    ShortMessage: "Invalid item ID.",
                    LongMessage: "Invalid or non-existent item ID.",
                    ErrorCode: "10.12",
                    SeverityCode: "Error",
                    ErrorClassification: "RequestError"
                }
            ],
            Build: "E1089_CORE_APILW_18879361_R1",
            Version: "1089"
        }

    - TestCases:

      - Estate 2 1980 Olympic Beijing Auto Stickers @331

  - uBlock may cause error in fetching lat/long api @341

- Cases Search

  - Error occurs when I search External Battery Backup Charger Case @528
  - If you want to test for the case when there are no similar items, search for "prison bus" in the "Books" category and look for this item. @529
  - Beijing Watch 18K Yellow Gold “Atelier Series” Cloisonne Enamel– Naval Battle
  - Apple iPhone 7 32GB GSM Unlocked Smartphone
  - Apple iPhone 7 a1778 32GB GSM Unlocked - Good
  - 1923 Beijing Hankou Railway Membership Card
  - Hotel Transylvania [New Sealed DVD]
  - Item: Apple iPhone 6 16GB 64GB 128GB GSM"Factory Unlocked"Smartphone Gold Gray Silver ID: "162158935212" console error so frame doesn't load properly
  - ItemId: 232647836863
  - 113661399317  - innerHtml doesn't work on Firefox

  

  ## Hints

  1. ##### How to get eBay API Key

     - To get an eBay API key, please follow these steps given in the PDF file below:
       - https://developer.ebay.com/DevZone/building-blocks/eBB_Join.pdf
     - Use eBay **Production keyset** @307

  2. ##### Get geolocation using IP-API.com

     - You need to use ip-api.com for searching the geolocation based on IP addresses. An example call is as follows: http://ip-api.com/json. The response is a JSON object shown in Figure 22.
       - ![figure 22](images/figure%2022.png)
     - This article introduces some similar APIs, so you have more choice for your homework 6:
       - https://ahmadawais.com/best-api-geolocating-an-ip-address/
     - Note: Use of Freegeoip API is not recommended.
     - **Error on Server** @416
       - The http traffic looked normal for localhost/XAMPP (TCP SYN/ACKs, HTTP POST requesting IP-API JSON, and HTTP response 200 OK). The traffic for cloud server was completely empty because the default protocol was https instead of http (as Mi Li pointed out). When I changed the URL to http instead of https, everything works the same as on XAMPP. Thanks for the assistance in tracking down the issue.

  3. ##### Parsing JSON-formatted data in PHP

     - In PHP 5 and 7, you can parse JSON-formatted data using the “**json_decode**” function. For more information, please go to http://php.net/manual/en/function.json-decode.php.
     - You can encode data into JSON-formatted objects using the “**json_encode**” function. For more information, please go to http://php.net/manual/en/function.json-encode.php.

  4. ##### Read and save contents in PHP

     - To read the contents of a JSON-formatted object, you can use the “**file_get_contents**” function. To save contents on the server side, you can use “**file_put_contents**” function.

  5. ##### Deploy PHP file to the cloud (GAE/AWS/Azure)

     - You should use the domain name of the GAE/AWS/Azure service you created in HW #5 to make the request. For example, if your GAE/AWS/Azure server domain is called **example.appspot.co** or **example.elasticbeanstalk.com** or **example.azurewebsites.net**, the following links will be generated:
       - GAE - http://example.appspot.com/productSearch.php
       - AWS - http://example.elasticbeanstalk.com/productSearch.php
       - Azure - http://example.azurewebsites.net/productSearch.php
     - *Note* example in the above URLs will be replaced by your choice of subdomain.

  ## Files to Submit

  - In your course homework page, you should **update the Homework 6** link to **refer to your new initial web search page** for this exercise (for example, productSearch.php).
  - This **PHP file** must be **hosted on GAE, AWS or Azure cloud service**. Graders will verify that this link is indeed pointing to one of the cloud services.
  - Also, **submit your source code file** (it must be a **single .php file, e.g. productSearch.php**) to the GitHub Classroom repository so that it can be graded and compared to all other students’ source code via the MOSS code comparison tool.

  ## Important

  - All discussions and explanations in Piazza related to this homework are part of the homework description and grading guidelines. So please review all Piazza threads, before finishing the assignment. If there is a conflict between Piazza and this description and/or the grading guidelines, Piazza always rules.
  - You **should not use JQuery** for Homework 6.
  - You **should not call the eBay APIs** directly from JavaScript, bypassing the Apache/HTTP proxy. Implementing any one of them in JavaScript instead of PHP will result in a 4-point penalty.
  - The link to the video is: [Youtube](https://www.youtube.com/watch?v=VtVYSgOwHbI&feature=youtu.be)
  - **Parsing** **using** **only JavaScript** [Important1](#Important%201), [Important 3](#Important%203)
  - How to handle **empty response** and **missing field data in results, Product JSON** [Important 2](#Important%202), [Important 4](#Important%204)
  - **Behavior of Seller message and Similar Items** when Seller message clicked [Important 5](#Important%205)
  - **Empty Seller Message and Similar Items** JSON Response [Important 6](#Important%206)
  - Which **requests** to be **made** by **PHP** and **Javascript** [Important 7](#Important%207)
  - **session** **can not be used in this homework** @332
