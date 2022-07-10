package org.timezoneconverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;

public class TimeZoneConverter {

    // Data from the GET request
    private final String fromTimestamp;
    private final String format;
    private final String fromCity;
    private final String toCity;

    public TimeZoneConverter(String fromTimestamp, String format, String fromCity, String toCity) {
        this.fromTimestamp = fromTimestamp;
        this.format = format;
        this.fromCity = fromCity;
        this.toCity = toCity;
    }

    public String convert() throws JsonProcessingException {

        // Data from the database
        String fromTimezone;
        String fromUTCShift;
        String toTimezone;
        String toUTCShift;

        // Connecting and retrieving data from the database
        String url = "jdbc:sqlite:C:/Users/User/Desktop/JavaProjects/TimezoneConverter/src/main/resources/timezonesdb.db";
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection connect = DriverManager.getConnection(url)) {
            DataFromDatabase dataFromDatabase = new DataFromDatabase(fromTimestamp, format);
            fromTimezone = dataFromDatabase.getTimeZoneIdFromDB(connect, fromCity);
            fromUTCShift = dataFromDatabase.getUTCShift(connect, fromTimezone);
            toTimezone = dataFromDatabase.getTimeZoneIdFromDB(connect, toCity);
            toUTCShift = dataFromDatabase.getUTCShift(connect, toTimezone);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Getting time for the second city
        TimeConverter timeZoneConvert = new TimeConverter(format, fromTimestamp, fromUTCShift, toUTCShift);
        String toTimestamp = timeZoneConvert.getNewDateTime();

        // JSON creation
        ObjectMapper objectMapper = new ObjectMapper();
        CityTime from = new CityTime(fromTimestamp, format, fromCity, fromTimezone);
        CityTime to = new CityTime(toTimestamp, format, toCity, toTimezone);
        JsonBox jsonBox = new JsonBox(from, to);
        return objectMapper.writeValueAsString(jsonBox);
    }
}
