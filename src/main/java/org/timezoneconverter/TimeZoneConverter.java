package org.timezoneconverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;

public class Main {


    public static void main(String[] args) throws JsonProcessingException {

        //GET /timeConvert?from=Moscow&to=Sydney&timestamp=2022-07-04T15:18:22+03&format=YYYY-MM-DDThh:mm:ssXXX

        // Data from the GET request

        String fromTimestamp = "2022-07-09T13:29:22+03:00";
        String format = "yyyy-MM-dd'T'HH:mm:ssXXX";
        String fromCity = "Moscow";
        String toCity = "Hong Kong";

        // Data from the database

        String fromTimezone;
        String fromUTCShift;
        String toTimezone;
        String toUTCShift;

        // Connecting and retrieving data from the database

        String url = "jdbc:postgresql://localhost:5432/Timezone";
        String username = "postgres";
        String password = "postgres";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connect = DriverManager.getConnection(url, username, password)) {
            DataFromDatabase dataFromDatabase = new DataFromDatabase(fromTimestamp, format);
            fromTimezone = dataFromDatabase.getTimeZoneIdFromDB(connect, fromCity);
            fromUTCShift = dataFromDatabase.getUTCShift(connect, fromTimezone);
            toTimezone = dataFromDatabase.getTimeZoneIdFromDB(connect, toCity);
            toUTCShift = dataFromDatabase.getUTCShift(connect, toTimezone);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Getting time for second city
        TimeZoneConvert timeZoneConvert = new TimeZoneConvert(format, fromTimestamp, fromUTCShift, toUTCShift);
        String toTimestamp = timeZoneConvert.getNewDateTime();

        // JSON creation
        ObjectMapper objectMapper = new ObjectMapper();
        CityTime from = new CityTime(fromTimestamp, format, fromCity, fromTimezone);
        CityTime to = new CityTime(toTimestamp, format, toCity, toTimezone);
        JsonBox jsonBox = new JsonBox(from, to);
        String resultFrom = objectMapper.writeValueAsString(jsonBox);
        System.out.println(resultFrom);
    }
}
