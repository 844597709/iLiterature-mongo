package com.swust.kelab.web.model;

import java.util.Date;

import com.swust.kelab.utils.FormatUtil;

public class CollectModel {

    private Date collectDate;
    private String collectDateStr;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCollectDateStr() {
        return collectDateStr;
    }

    public void setCollectDateStr(String collectDateStr) {
        this.collectDateStr = collectDateStr;
    }

    public Date getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(Date collectDate) {
        this.collectDate = collectDate;
        this.collectDateStr = FormatUtil.formatDate(collectDate, "yyyy-MM");
    }
}
