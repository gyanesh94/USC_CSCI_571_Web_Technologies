# eBay Product Search Angular

1. The project is live and deployed on Google Cloud Platform.

   http://ebay-product-search-angular.appspot.com

## Technologies used

- NodeJS
- ExpressJS
- Angular 7
- Bootstrap 4
- AJAX
- JSON
- HTML5

## API's used

- Ebay API
- IP API
- Google Customized Search API
- Facebook API

## Overview

Create a webpage that allows you to search for products information using the *eBay APIs*, and the results will be displayed in a tabular format. The page will also provide product details, seller details and related products.

## To Run
- Convert [config.js.bak](./config.js.bak) to config.js
  - Add all the keys mentioned in the config
    - Can be found in the [description](./assignment_description/HW8_Description.pdf) how to get them.
- Convert [app.config.ts.bak](./src/app/app.config.ts.bak) to app.config.ts
  - Add all the parameters as mentioned

## Features

- User can search products on Ebay from current location or a custom location
- User can view searched products details including features, seller details, shipping details etc.
- Shows user similar products based on the current product they are viewing
- Product Images are obtained using custom Google Search.
- User can share the item details on Facebook
- User can create their wish list which are accessible at all times even after the website is closed. This is done using LocalStorage.
- Bootstrap is used to make the website responsive and mobile friendly.
- **Tested on Firefox, Chrome, Safari and Mobile browsers (Chrome, Safari)**