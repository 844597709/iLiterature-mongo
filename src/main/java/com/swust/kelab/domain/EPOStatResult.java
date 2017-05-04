package com.swust.kelab.domain;

public class EPOStatResult {
    private int id;
    private String indicator;
    private int count;
    private String extra;

    public String getExtra() {
        return extra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
