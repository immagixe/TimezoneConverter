package org.timezoneconverter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeZoneConvert {

    private final String format;
    private final String fromTimestamp;
    private final String fromUTCShift;
    private final String toUTCShift;

    public TimeZoneConvert(String format, String fromTimestamp, String fromUTCShift, String toUTCShift) {
        this.format = format;
        this.fromTimestamp = fromTimestamp;
        this.fromUTCShift = fromUTCShift;
        this.toUTCShift = toUTCShift;
    }

    public String getNewDateTime() {

        // Converting string from request to date according to format
        OffsetDateTime datetimeFrom = TimeZoneConvert.convertDateAccordingToPattern (format, fromTimestamp);

        // Converting UTC shift difference to seconds
        int seconds = offsetToSeconds(toUTCShift) - offsetToSeconds(fromUTCShift);
        OffsetDateTime datetimeTo = datetimeFrom.plusSeconds(seconds);

        // Setting a new offset for the second city
        OffsetDateTime datetimeToChangeOffset = datetimeTo.withOffsetSameLocal(ZoneOffset.of(toUTCShift));

        return String.valueOf(datetimeToChangeOffset);
    }

    private int offsetToSeconds (String UTCShift) {
        ZoneOffset offsetFrom = ZoneOffset.of(UTCShift);
        return offsetFrom.getTotalSeconds();
    }

    public static OffsetDateTime convertDateAccordingToPattern (String format, String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return OffsetDateTime.parse(timestamp, formatter);
    }
}
