package org.timezoneconverter;

public class CityTime {

    private final String timestamp;
    private final String format;
    private final String city;
    private final String timezone;

    public CityTime(String timestamp, String format, String city, String timezone) {
        this.timestamp = timestamp;
        this.format = format;
        this.city = city;
        this.timezone = timezone;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getFormat() {
        return format;
    }

    public String getCity() {
        return city;
    }

    public String getTimezone() {
        return timezone;
    }
}
