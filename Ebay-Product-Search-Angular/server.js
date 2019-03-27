const express = require("express");
const http = require("http");
const path = require("path");
const app = express();

var Request = require("request");

const port = process.env.PORT || 8081;
app.listen(port, () => console.log(`Listening on port ${port}...`));

app.use(express.static(path.join(__dirname, "dist/Ebay-Product-Search-Angular")));
