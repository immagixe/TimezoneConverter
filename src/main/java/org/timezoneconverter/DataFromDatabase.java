package org.timezoneconverter;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataFromDatabase {

    private final String timestamp;
    private final String format;
    private final String formatInDB;

    public DataFromDatabase(String timestamp, String format) {
        this.timestamp = timestamp;
        this.format = format;
        this.formatInDB = "yyyyMM-dd'T'HH:mm:ssZ";
    }

    public String getTimeZoneIdFromDB(Connection connect, String city) {
        List<String> arrayFromTime = new ArrayList<>();
        List<String> arrayToTime = new ArrayList<>();
        String trueZoneId;
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement("select from_time, to_time" +
                    " from cities_timezones ct JOIN cities c ON (ct.city_id = c.id) where name = ?");
            ps.setString(1, city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                arrayFromTime.add(rs.getString("from_time"));
                arrayToTime.add(rs.getString("to_time"));
            }

            if (arrayFromTime.get(0) == null) {
                trueZoneId = getTrueZoneId(connect, city, null);
            } else {
                trueZoneId = getTimeZoneIdIfTwoTimeZones(connect, city, arrayFromTime, arrayToTime);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return trueZoneId;
    }

    private String getTimeZoneIdIfTwoTimeZones(Connection connect,
                                               String city,
                                               List<String> arrayFromTime,
                                               List<String> arrayToTime) {
        OffsetDateTime datetime = TimeConverter.convertDateAccordingToPattern(format, timestamp);
        String year = String.valueOf(datetime.getYear());
        List<OffsetDateTime> from = new ArrayList<>();
        List<OffsetDateTime> to = new ArrayList<>();
        int summerTimeIndex = 0;
        int winterTimeIndex = 0;
        String trueZoneId;

        for (int i = 0; i < 2; i++) {
            from.add(TimeConverter.convertDateAccordingToPattern(formatInDB,
                    (year + arrayFromTime.get(i))));
            to.add(TimeConverter.convertDateAccordingToPattern(formatInDB,
                    (year + arrayToTime.get(i))));

            if (from.get(i).isAfter(to.get(i))) {
                winterTimeIndex = i;
            }
            if (from.get(i).isBefore(to.get(i))) {
                summerTimeIndex = i;
            }
        }
        if (datetime.isAfter(from.get(summerTimeIndex)) && datetime.isBefore(to.get(summerTimeIndex))) {
            String offsetIfTwoTimeZones = from.get(summerTimeIndex).getOffset().toString();
            trueZoneId = getTrueZoneId(connect, city, offsetIfTwoTimeZones);
        } else {
            String offsetIfTwoTimeZones = from.get(winterTimeIndex).getOffset().toString();
            trueZoneId = getTrueZoneId(connect, city, offsetIfTwoTimeZones);
        }
        return trueZoneId;
    }

    private String getTrueZoneId(Connection connect, String city, String offsetIfTwoTimeZones) { // сократить код. if offset = null
        String timeZoneId = null;
        try {
            if (offsetIfTwoTimeZones == null) {
                PreparedStatement psTimeZoneId = connect.prepareStatement("select abbreviation" +
                        " from timezones t" +
                        " JOIN cities_timezones ct ON (t.id = ct.timezone_id)" +
                        " JOIN cities c ON (ct.city_id = c.id) where name = ?");
                psTimeZoneId.setString(1, city);
                ResultSet rsTimeZoneId = psTimeZoneId.executeQuery();
                while (rsTimeZoneId.next()) {
                    timeZoneId = rsTimeZoneId.getString("abbreviation");
                }
            } else {
                PreparedStatement psTimeZoneId = connect.prepareStatement("select abbreviation" +
                        " from timezones t" +
                        " JOIN cities_timezones ct ON (t.id = ct.timezone_id)" +
                        " JOIN cities c ON (ct.city_id = c.id) where c.name = ? and t.time_offset = ?");
                psTimeZoneId.setString(1, city);
                psTimeZoneId.setString(2, offsetIfTwoTimeZones);
                ResultSet rsTimeZoneId = psTimeZoneId.executeQuery();
                while (rsTimeZoneId.next()) {
                    timeZoneId = rsTimeZoneId.getString("abbreviation");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return timeZoneId;
    }

    public String getUTCShift(Connection connect, String timeZone) {
        String utcShift = null;
        try {
            PreparedStatement psUTCShift = connect.prepareStatement("select time_offset " +
                    "from timezones " +
                    "where abbreviation = ?");
            psUTCShift.setString(1, timeZone);
            ResultSet rsUTCShift = psUTCShift.executeQuery();
            while (rsUTCShift.next()) {
                utcShift = rsUTCShift.getString("time_offset");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utcShift;
    }
}
