package ru.allmoyki.bus;

import ru.allmoyki.pojo.OrderPojo;

/**
 * Created by Boichuk Dmitriy on 09.10.2015.
 */
public class OrderBus {
    private OrderPojo.Datum datum;

    public OrderPojo.Datum getDatum() {
        return datum;
    }

    public void setDatum(OrderPojo.Datum datum) {
        this.datum = datum;
    }
}
