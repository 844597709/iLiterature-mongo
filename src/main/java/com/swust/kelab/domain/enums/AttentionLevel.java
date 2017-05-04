package com.swust.kelab.domain.enums;

/**
 * 关注级别枚举
 * 
 * @author longlongchang
 * 
 */
public enum AttentionLevel {
    GENERAL(1, "一般关注"), COMPARATIVE(2, "比较关注"), SPECIALLY(3, "特别关注");
    private int code;
    private String desc;

    private AttentionLevel(int code, String desc) {
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

    public static AttentionLevel codeOf(int code) {
        for (AttentionLevel atte : AttentionLevel.values()) {
            if (atte.getCode() == code) {
                return atte;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return desc;
    }
}
