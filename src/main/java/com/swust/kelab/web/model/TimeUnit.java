package com.swust.kelab.web.model;

public enum TimeUnit {
    HOUR("时"), DAY("日"), WEEK("周"), MONTH("月"), QUARTER("季度"), YEAR("年");
    private String desc;

    private TimeUnit(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return this.desc;
    }
}
