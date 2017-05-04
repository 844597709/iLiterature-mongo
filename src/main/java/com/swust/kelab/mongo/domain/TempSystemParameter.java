package com.swust.kelab.mongo.domain;

/**
 * Created by zengdan on 2017/1/6.
 */
public class TempSystemParameter {
    private Integer sypaId;
    private String sypaName;
    private String sypaEnName;
    private String sypaValue;
    private String sypaRemark;
    private Integer sypaOrder;

    public Integer getSypaId() {
        return sypaId;
    }

    public void setSypaId(Integer sypaId) {
        this.sypaId = sypaId;
    }

    public String getSypaName() {
        return sypaName;
    }

    public void setSypaName(String sypaName) {
        this.sypaName = sypaName;
    }

    public String getSypaEnName() {
        return sypaEnName;
    }

    public void setSypaEnName(String sypaEnName) {
        this.sypaEnName = sypaEnName;
    }

    public String getSypaValue() {
        return sypaValue;
    }

    public void setSypaValue(String sypaValue) {
        this.sypaValue = sypaValue;
    }

    public String getSypaRemark() {
        return sypaRemark;
    }

    public void setSypaRemark(String sypaRemark) {
        this.sypaRemark = sypaRemark;
    }

    public Integer getSypaOrder() {
        return sypaOrder;
    }

    public void setSypaOrder(Integer sypaOrder) {
        this.sypaOrder = sypaOrder;
    }
}
