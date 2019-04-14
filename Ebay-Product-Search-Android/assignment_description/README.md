## 4. High Level Design

This homework is a mobile app version of Homework 8. In this exercise, you will develop an Android application, which allows users to search for the products on eBay, look at information about them, save some to Wishlist and post on Facebook about the same. You should reuse the Node.js backend service you developed in Homework 8 and follow the same API call requirements.



## 5. Implementation

### 5.1 App Icon and Splash Screen

- In order to get this icon/image of the size of your choice, go to the icons web page specified in the **section 6.1** and search for an item called “Shopping”.
  - Using advanced export option, set the colors and a correct size to download the PNG icon [figure 1 - App Icon](./images/figure_1.png).
- The app begins with a welcome screen [Figure 2](./images/figure_2.png) which displays the icon downloaded above.
  - This screen is called splash screen.
  - This screen can be implemented using many different methods.
  - The simplest is to create a resource file for launcher screen and adding it as a style to AppTheme.Launcher (see hints).



### 5.2 Search Form

- The initial interface is shown in [Figure 3](./images/figure_3.png).
- There are **two tabs** in this interface: **search** and **WishList**.

#### 5.2.1 Search Tab

- **Keyword**
  - An **EditText component** - allowing the user to enter the keyword.
- **Category**
  - A **Spinner view** allowing the user to choose a category.
  - When the user taps on this field, a dropdown list should display for selecting a category, as shown in [Figure 5](./images/figure_5.png).
  - Make sure you include all the categories in homework 8.
- **Condition**
  - It is a set of **checkboxes** indicating the condition of the item the user wants to
    buy – new/used/unspecified.
- **Shipping Options**
  - A set of **checkboxes** indicating the shipping options the user is
    interested in - Local pickup/Free shipping.
- **Enable Nearby search**
  - A **checkbox** – selecting this will **open further distance-based** options as shown in [Figure 4](./images/figure_4.png) (which are initially hidden as in [Figure 3](./images/figure_3.png)) at the bottom.
  - The values of distance-based search considered for eBay query only when this checkbox is selected.
  - The options are as follows:
    - **Miles From**
      - An **EditText (type: number)** allowing the user to enter the distance and the **default value is 10**.
    - **From**
      - Two Radio Buttons to select “Current Location” or “Zipcode”.
      - **Current Location**
        - You can get the **location from your emulator** (see hints) or from **ip-api calls**, for users that choose “Here”.
      - **Zipcode**
        - An **AutoCompleteTextView** - It provides the autocomplete
          function as shown in [Figure 6](./images/figure_6.png).
        - Make sure you use the same API as Homwork 8. See hints.
        - When the **keyboard is opened**, the **layout should slide up** so that **zipcode textview is not hidden** by the keyboard.
- **Search**
  - A **button** to get the input information of each field, after validation.
  - If the validation is successful, then the products would be fetched from the server
  -  However, if the **validation is unsuccessful, appropriate messages** should be displayed and no further requests would be made to the server.
- **Clear**
  - A button to **clear the input fields and reset them to default values** when applicable.
  - It should also **remove any validation error messages**.
- The **validation** for an empty keyword has to be implemented.
  - If the **user does not enter anything in the EditText or just enters some empty spaces**, when he/she presses the Search button you need to display an appropriate message to indicate the error, as shown in [Figure 7](./images/figure_7.png).
  - The same should be done when “**Zipcode**” location is not entered, and that option in enabled using the radio button as shown in [Figure 8](./images/figure_8.png).
- Whenever a **validation error is found**, along with the error form, the error message should also be displayed on the bottom of the screen using a “**Toast**” message as shown in the [Figure 8](./images/figure_8.png).



### 5.3 Search Results

- When the user taps the SEARCH button and all validations pass, your app loads the search results page.
- Before **you get the data from your backend server**, **a progress bar should display** on the screen as indicated in [Figure 9](./images/figure_9.png).
- After you get the data from your backend, hide the ProgressBar and display the result page as a list using RecyclerView, as shown in [Figure 10](./images/figure_10.png).
  - Android RecyclerView and CardView Tutuorial
  - https://www.simplifiedcoding.net/android-recyclerview-cardview-tutorial/
- The **RecyclerView** must be **scrollable**.
- On **top of the page** there should be a **line indicating how many results are being displayed** for this particular search query.
- There should also be a ‘**back button**’ **to navigate back the search/wishlist** interface.
- The **list view should be displayed** using **GridLayoutManager** with **CardLayout**.
  - Each of the item in the list should have the following:
    - Product Image
    - Title of the product
    - Zipcode
    - Shipping cost
    - Condition
    - A button to add to wishlist
  - See homework 8 for more details about these fields.
- You can **fix the size of the card to any size**, but **all the cards should be of SAME size**.
  - To do this you MUST do the following:
    - **Fix the size of the image** and wishlist icon on the card
    - All the **images should be centered** inside the ImageView and shou**ld NOT be cropped**
    - **Fix the title to span 3** (or any fixed number) **lines** – if there are less than 3 lines then
    - append/prepend a ‘\n’ characters.
    - **Fix the layout of zipcode and shipping information** lines
    - If any of **zipcode/shipping info/price/condition are missing – it should show N/A**
    - Ensure that the **price is the last line** and is shown at the bottom of the card.
- Tapping the **Cart button adds** the corresponding product into the **wishlist**, and a **message is displayed at the bottom of the app using a Toast**, as shown in [Figure 11](./images/figure_11.png).
  - Tapping the **button again** would remove that product from the wishlist, and a similar **message should also be displayed to indicate the product has been removed from the wishlist**, as shown in [Figure 12](./images/figure_12.png).



### 5.4 Product Details

- Tapping on an item in the result list should show details of that product with four tabs
  - Product Details Tab
  - Shipping Tab
  - Google images Tab
  - Similar Products Tab
- Note that the **ProgressBar should be shown on each tab** before you are ready to display the corresponding tab.
- The **tabs should be attached to the ActionBar** and a **ViewPager should be used to host all the tabs**, as shown in [Figure 13](./images/figure_13.png).
  - Users should be able to **switch between tabs** by both swiping **and tapping on a tab**.
  - Please refer to the video to see this in action.
- The ActionBar should also include the following elements:
  - **Back button**
    - Navigates back to the search results list WITHOUT page refresh. See video
      for more details.
  - **Title**
    - which is the name of the product.
  - **Facebook button**
    - to share the product details on Facebook.
    - Once the button is tapped, **a web page should open** to allow the user to share the product information on Facebook, as shown in [Figure 14](./images/figure_14.png).
    - This should work the same as homework 8
    - **Note the additional hashtag** that is added to the Facebook post.
  - **Wishlist button**
    - This button will **add/remove the product to/from the wishlist**.
    - This can be implemented using a **FloatingActionButton**.
    - The icon of the **button should also change** based on whether the item **currently belongs to wishlist or not**.
    - For more details see video.

#### 5.4.1 Product Details Tab

- This tab contains the following sections (See [Figure 15](./images/figure_15.png) and [Figure 16](./images/figure_16.png))
- **Product images**
  - PictureURL property in json.
  - This is a **minimal version** of image gallery.
  - The user of the app will be able to browse through the images provided by eBay.
  - This can be implemented by using a **HorizontalScrollView** (See hints).
  - Please note there are many other ways to implement this, you can use any of those.
- **Product Title**
  - Title property in json
  - A textview with big font which contains the title of the product.
- **Price and shipping**
  - CurrentPrice and ShippingCost properties in json
  - A textview with Price and shipping cost.
  - If the Shipping cost is \$0 then display “With Free Shipping”, otherwise show “With $XX Shipping” with XX being the shipping cost
- **Highlights**
  - **Price**: price of the product - **CurrentPrice** property in json
  - **Brand**: Brand of the product – **Brand** property in json
- **Specifications**
  - Display all the values inside **ItemSpecifics** of the JSON from EBay.
  - **Brand** has to be the **first value** in this list.
  - **For each value capitalize the first letter**.
- ***Notes***
  - JSON property names are written for reference. For exact structure please
    refer to homework 8
  - Each of these **sections are separated with a faint horizontal line**.
  - If **any of the details are missing, skip that row**.
  - If for a **particular section, ALL the fields are missing then skip the entire section**.
  - If **ALL sections are missing, then display an appropriate no results message**.

#### 5.4.2 Shipping Tab

- This tab contains the following sections each of which can be implemented using a TableLayout (see [Figure 17](./images/figure_17.png) and [Figure 18](./images/figure_18.png))
- **Sold By**
  - This section contains 4 fields
    - **Store Name**
      - **StoreName** and **StoreUrl** properties in json
      - **Clicking on this should open a browser page** redirecting to the store URL.
    - **Feedback score**
      - **FeedbackScore** property in json
    - **Popularity**
      - **PositiveFeedbackPercent** property in json.
      - This can be implemented using an external library called **CircularScoreView** See hints.
    - **Feedback star**
      - **FeedbackRatingStar** property in json.
      - This section **displays a small icon** indicating the seller’s feedbackstar value.
      - The meaning of each of the values can be found here.
      - The stars have to be **styled based on their name** – the name indicates the color that the star will be displayed in, the icon can be taken from the icons table in section 6.1.
      - You can define multiple images/xmls for each color or you can set the colors dynamically (see hints)
- **Shipping info**
  - This section contains 4 fields 
    - **Shipping Cost**
      - Needs to be **passed from the Search Result Activity** to this.
      - This can be **done by intent**.
      - Display “Free shipping” if the cost is 0 else display $X
    - **Global Shipping**
      - **GlobalShipping** property in json.
      - Display **Yes**/No instead of true/false
    - **Handling Time**
      - **HandlingTime** property in json.
      - If 0 or 1 display use “day” else use “days” as suffix
    - **Condition**
      - **ConditionDescription** property in json
- **Return Policy**
  - This section contains 4 fields
    - **Policy**
      - **ReturnsAccepted** property in json
    - **Returnswithin**
      - **ReturnsWithin** property in Json
    - **Refund Mode**
      - **Refund** property in Json
    - **Shipped by**
      - **ShippingCostPaidBy** property in json
- **Notes**
  - JSON property names are written for reference. For exact structure please
    refer to homework 8
  - Each of these sections are **separated with a faint horizontal line**.
  - If **any of the details are missing, skip that row**.
  - If for a particular section, **ALL the fields are missing then skip the entire**
    **section**.
  - If **ALL sections are missing, then display an appropriate no results message**.

#### 5.4.3 Google Tab

- Same as in homework 8.
- Show 8 photos from Google custom search API.
- These photos are fetched using the title of the product received in the eBay response and not from the keyword that was entered by the user.
- See [Figure 19](./images/figure_19.png) and [Figure 20](./images/figure_20.png).
- You can use **Volley Network ImageView**, **Picasso or Glide** to load the image.
- The images **should NOT be cropped and must fit and be centered on the designated ImageView**
- See the hints and some useful links in section 6.3.5

#### 5.4.4 Similar Products Tab

- This tab displays the similar products received from eBay, same as homework 8.
- The Similar Products are shown in a **list using a RecyclerView**.
  - Note that each of the **cells can be tapped** and then a **web page should be opened** and navigates to the eBay page, see video for more detail and [Figure 21](./images/figure_21.png).
- **Product image**
  - **imageUrl** property in json
- **Title**
  - **title** property in json
- **Shipping**
  - **shippingcost**’s value property in json
  - display **Free Shipping** if the cost is 0
- **Days Left**
  - **timeLeft** property in json.
  - The days can be extracted by taking the characters between P and D in the
    specified field.
  - See hints for some helpful String methods in Java.
- **Price**
  - **buyItNowPrice** value property in json.
- **On click**
  - Open a browser and navigate to url specified by viewItemUrl property in json. See [Figure 23](./images/figure_23.png)
- As shown in [Figure 21](./images/figure_21.png) and [Figure 22](./images/figure_22.png), you should use **two Spinners to switch sort** **parameter** (Name/Price/Days/Default) and also its **order** (Ascending/Descending).
  - When “**Default**” is selected, the **Ascending or Descending** spinner should be **disabled**, and the **original order should be restored**.
  - There are many ways to handle sorting depending on how you store your data.
  - Recommended way is to create a Java Class to store all the fields of each row and use a comparator/comparable to sort the list and notify the adapter (see hints for examples)
- **Notes**
  - JSON property names are written for reference. For exact structure please
    refer to homework 8
  - If any of the details are **missing, you can either skip that row or mention N/A**.
  - If there are **no similar products – display “No results” and both spinners**
    **should be disabled**.



### 5.5 WishList

- Use **Tabs** with a **ViewPager** on the main screen to switch between the search page and the wishlist page.
- The wishlisted products should be displayed in a **list using a RecyclerView**.
- You can use the same view that you used in the search results. See [Figure 25](./images/figure_25.png).
- If there are **no wishlisted products, "No Wishes"** should be displayed at the center of the screen, as shown in [Figure 27](./images/figure_27.png).
- Like in search results, pressing the cart icon here should remove the product from the wishlist. See video for more detail.
- The **bottom of this page displays the summary of the wishlist**. This view should contain 2 things:
  - “Wishlist total (X items)” with X being the number of items in the wishlist
  - The **sum of prices of all products should be displayed on the right** end
  - Adding/removing items should update this view dynamically.
  - You can achieve this by updating the text every time you call this component’s adapter’s **notifyAdapterDataSetChanged** method. See [Figure 25](./images/figure_25.png) and [Figure 26](./images/figure_26.png).



### 5.6 Summary of detailing and error handling

- If **no products are found given a keyword**, a “**No Results” message should be displayed**, as shown in [Figure 28](./images/figure_28.png).
  - Similarly, appropriate messages should be displayed for **no similar products**, and **for the empty wishlist**.
- If for any reason an **error occurs** (**no network, API failure, cannot get location etc**.), an appropriate **error messages should be displayed at the bottom of screen using a Toast**.
- For **any cards (search results and wishlist)** if any field is missing then display **N/A**.
- For **any missing data on details tabs pages, skip that row**. **If all rows are missing in a section skip that entire section and if all sections are missing, then display a message similar to 1**.
- The name **title** of the product should be of **EXACTLY 3 lines in the list page**.
- Everywhere the shipping cost should be displayed as **“Free Shipping” if the cost is 0**
- Colors and icons for wishlist should be dynamic
- **Brand should be the first value under Specifications** section
- **Show the number of search results on top**
- Display 8 images in the Google tab
- **No photos should be cropped anywhere, center the image inside the ImageView**.
- Feedback star’s color and icon should be dynamic.
- **Sorting for price and days should work on the numeric values and not String comparisons 14**.
- Facebook post must include Hashtag
- Total cost and number of items in wishlist should update immediately
- **The keyboard should not hide the Zipcode textview** while typing.
- **Distance should only be considered if the location checkbox is enabled**
- Clear should clear all errors and form values



### 5.7 Additional Info

- For things not specified in the document, grading guideline, or the video, you can make your own decisions. But keep in mind about the following points:
  - **Always display a proper message** and don’t crash if an error happens.
  - **Always display a loading message if the data** is loading.
  - **You can only make HTTP requests to your backend Node.js on AWS/GAE/Azure** and use the Google Map SDK for Android.
  - All **HTTP requests should be asynchronous** and **should not block the main UI thread**.
    - You can use **third party libraries like Volley to achieve this in a simple** manner.



## 6. Implementation Hints

### 6.1 Icons

- The images used in this homework are taken from https://materialdesignicons.com/ 
- Here are the names and details of each page.
  - You can choose to work with xml/png/jpg versions.
  - We **recommend using xml** as it is easy to modify colors by setting the Fill Colors.
  - [Table 1](./images/table_1.png)



### 6.2 Getting current location

- In order to get the **current location**, you can use **either ip-api or location services**.
- For your location fetching code to work, **you must request the permission** from the user.
- You can read more about requesting permissions here:
  - https://developer.android.com/training/permissions/requesting.html
- You may need to mock the location in your emulator. This can be done from the emulator settings.



### 6.3 Third party libraries

- Almost all functionalities of the app can be implemented without using third party libraries, but sometimes using them can make your implementation much easier and quicker.
- Some libraries you may have to use are:

#### 6.3.1 Google Play services

- You will need this for various features like getting the current location and using Google Maps in your app.
- You can learn about setting it up here:
  - https://developers.google.com/android/guides/setup

#### 6.3.2 Volley HTTP requests

- Volley can be helpful with asynchronously http request to load data.
- You can also use Volley network ImageView to load photos in Google tab.
- You can learn more about them here:
  - https://developer.android.com/training/volley/index.html

#### 6.3.3 Picasso

- Picasso is a powerful image downloading and caching library for Android.
  - http://square.github.io/picasso/
- If you decide to use **RecycleView** to display the photos with **Picasso Please use version 2.5.2** since **latest version does not support RecycleView** well.
  - https://github.com/codepath/android_guides/wiki/Displaying-Images-with-the-Picasso-Library

#### 6.3.4 Glide

- Glide is also powerful image downloading and caching library for Android.
- It is similar to Picasso.
- You can also use Glide to load photos in Google tab.
  - https://bumptech.github.io/glide/

#### 6.3.5 CircularScoreView

- This is a third-party library that can be used to display the feedback rating of a seller.
- This can be found here
  - https://github.com/wssholmes/CircularScore
- Add this library to your Gradle and you can directly use it in the resource files.
- Set the appropriate value and the color.



### 6.4 Implementing a Gallery view

- A simple way of implementing a gallery view is to use a **HorizontalScrollView** which basically contains the collection of image views of fixed size.
- A sample can be found here:
  - https://questdot.com/android-image-gallery-horizontalscrollview-tutorial/



### 6.5 Implementing Sorting techniques

- https://stackoverflow.com/questions/45790363/how-to-sort-recyclerview-item-in-android
- Do not forget to call NotifyDataSetChanged() method wherever needed.



### 6.6 Working with the AutoCompleteTextView

- Working with the AutoCompleteTextView to show the suggestions might be a little challenging.
- This tutorial goes over how it is done so that you get an idea of how to go about it.
  - https://www.truiton.com/2018/06/android-autocompletetextview-suggestions-from-webservice-call/
  - https://stackoverflow.com/questions/14618632/how-to-add-dynamic-image-with-horizontal-scrollview-listview
  - https://www.tutorialspoint.com/how-to-implement-horizontalscrollview-like-gallery-in-android
  - https://tutorialwing.com/create-android-horizontalscrollview-programmatically-android/



### 6.7 Implementing a Splash Screen

- There are many ways to implement a splash screen.
- This blog highlights almost all of them with examples:
  - https://android.jlelse.eu/the-complete-android-splash-screen-guide-c7db82bce565



### 6.8 Dynamic coloring using DrawableCompat

- https://medium.com/@hanru.yeh/tips-for-drawablecompat-settint-under-api-21-1e62a32fc033



### 6.9 String manipulation in Java

- https://www.guru99.com/java-strings.html



### 6.10 User location using Emulator

- If you choose to use location services to get the current location, then you can use the following emulator settings. [Figure 29: Location Setting of Emulator](./images/figure_29.png).



## 7. What to Upload to GitHub Classroom

- You should also **ZIP all your source code (the java/ and res/ directories excluding the vector drawables that we provide to you)** and submit the resulting ZIP file by the end of the demo day.
- Unlike other exercises, you will have to demo your submission in person during a special grading session.
- Details and logistics for the demo will be provided in class, on the Announcement page and on Piazza.
- **Demo is done an a laptop/ notebook/MacBook or Windows PC using the emulator**, and not a physical mobile device.



## IMPORTANT

- All videos are part of the homework description.
- All discussions and explanations on Piazza related to this homework are part of the homework description and will be accounted into grading.
- So please review all Piazza threads before finishing the assignment.



## Piazza

- [@920](https://piazza.com/class/jptzz79d354gb?cid=920).
  - You don’t need to change the colour of the toast. It’s okay if you leave it to default color.
  - Yes, mutate() is not needed anywhere.