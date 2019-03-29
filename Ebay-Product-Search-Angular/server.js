const express = require("express");
const request = require("request");
const path = require("path");
const app = express();
const config = require("./config");

const port = process.env.PORT || 8081;
app.listen(port, () => console.log(`Listening on port ${port}...`));

app.get("/api/zipcode", (req, res) => {
  request.get(
    `http://api.geonames.org/postalCodeSearchJSON?postalcode_startsWith=${req.query.zipCode}&username=${config.GEONAME_KEY}&country=US&maxRows=5`,
    (error, response, body) => {
      res.setHeader("Access-Control-Allow-Origin", "*");
      if (error) {
        res.send(error);
        return;
      }
      res.send(body);
    });
});

app.use(express.static(path.join(__dirname, "dist/Ebay-Product-Search-Angular")));
