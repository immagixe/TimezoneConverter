# Timezone converter

## Run instructions

- To run the program use a server, for example: Tomcat.
- In the TimeZoneConverter class, change the absolute path in string "String url = "jdbc:sqlite:C:/Users/User/Desktop/JavaProjects/TimezoneConverter/src/main/resources/timezonesdb.db";".

> **GET request example:**
/timeConvert?from=Moscow&to=Sydney&timestamp=2022-07-04T15:18:22%2B03:00&format=yyyy-MM-dd%27T%27HH:mm:ssXXX

> **Response example:**
{
"from": {
"timestamp": "2022-07-04T15:18:22+03:00",
"format": "YYYY-MM-DDThh:mm:ssXXX",
"city": "Moscow",
"timezone": "MSK"
},
"to": {
"timestamp": "2022-07-04T22:18:22+10:00",
"format": "YYYY-MM-DDThh:mm:ssXXX",
"city": "Sydney",
"timezone": "AEST"
}
}

## List of cities in database

- Shanghai
- Beijing
- Mumbai
- Sydney
- Tokyo
- Moscow
- Hong Kong
- Bangkok
- London
- Paris
- New York
