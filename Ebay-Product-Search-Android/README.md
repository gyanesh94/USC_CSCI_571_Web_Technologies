# eBay Product Search Android Application

## Overview

This Android application serves as the Mobile counterpart of the project found at [Ebay Product Search Angular](../Ebay-Product-Search-Angular).

eBay Product Search application allows you to search for products information sold on eBay based on current location or any other location entered by the user. Users can also know more about product details, seller details, and related products.

## Technologies used

- Android
- Volley
- Glide

## API's used

- Ebay API
- IP API
- Google Customized Search API
- Facebook API

## To Run
- Convert [AppConfig.java.bak](android_app/app/src/main/java/edu/gyaneshm/ebay_product_search/data/AppConfig.java.bak) to AppConfig.java
  - Add all the parameters as mentioned
- Run the server given in [Ebay Product Search Angular](../Ebay-Product-Search-Angular)

## Features

- User can search products on Ebay from current location or a custom location
- User can view searched products details including highlights, specifications, seller details, shipping details etc.
- Shows user similar products based on the current product they are viewing
- Product Images are obtained using custom Google Search.
- User can share the item details on Facebook
- User can create their wish list which are accessible at all times even after the mobile application is closed.