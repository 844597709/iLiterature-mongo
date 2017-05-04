package com.swust.kelab.web.model;

public enum IndexLocation {
    TITLE(1, "标题"), SUMMARY(2, "摘要"), CONTENT(3, "正文"), REPLY(4, "回复");

    private int code;
    private String desc;

    private IndexLocation(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static IndexLocation codeOf(int code) {
        for (IndexLocation il : IndexLocation.values()) {
            if (il.getCode() == code) {
                return il;
            }
        }
        return null;
    }
}
