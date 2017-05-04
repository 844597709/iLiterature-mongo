package com.swust.kelab.mongo.domain;

public class TempColumn {
    private Integer coluId;
    private Integer coluParent;
    private String coluName;
    private String coluUrl;
    private Integer coluType;
    private Integer coluOrder;
    private String coluIcon;

    public Integer getColuId() {
        return coluId;
    }

    public void setColuId(Integer coluId) {
        this.coluId = coluId;
    }

    public Integer getColuParent() {
        return coluParent;
    }

    public void setColuParent(Integer coluParent) {
        this.coluParent = coluParent;
    }

    public String getColuName() {
        return coluName;
    }

    public void setColuName(String coluName) {
        this.coluName = coluName;
    }

    public String getColuUrl() {
        return coluUrl;
    }

    public void setColuUrl(String coluUrl) {
        this.coluUrl = coluUrl;
    }

    public Integer getColuType() {
        return coluType;
    }

    public void setColuType(Integer coluType) {
        this.coluType = coluType;
    }

    public Integer getColuOrder() {
        return coluOrder;
    }

    public void setColuOrder(Integer coluOrder) {
        this.coluOrder = coluOrder;
    }

    public String getColuIcon() {
        return coluIcon;
    }

    public void setColuIcon(String coluIcon) {
        this.coluIcon = coluIcon;
    }
}
