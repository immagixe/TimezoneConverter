package org.timezoneconverter;

public class JsonBox {

    private final CityTime from;
    private final CityTime to;

    public JsonBox(CityTime from, CityTime to) {
        this.from = from;
        this.to = to;
    }

    public CityTime getFrom() {
        return from;
    }

    public CityTime getTo() {
        return to;
    }
}
