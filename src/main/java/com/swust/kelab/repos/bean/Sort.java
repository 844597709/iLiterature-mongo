package com.swust.kelab.repos.bean;

import java.io.Serializable;

public class Sort implements Serializable {

    private static final long serialVersionUID = -8009500426190155975L;

    public enum Order {
        ASC, DESC
    }

    private String name;

    private Order order;

    public Sort(String name) {
        this(name, Order.ASC);
    }

    public Sort(String name, Order order) {
        this.name = name;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public Order getOrder() {
        return order;
    }
}
