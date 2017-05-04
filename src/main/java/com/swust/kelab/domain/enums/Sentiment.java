package com.swust.kelab.domain.enums;

/**
 * 情感分类级别
 * 
 * @author longlongchang
 * 
 */
public enum Sentiment {
    EXTREME_NEG(1, "极度负面"), STRONG_NEG(2, "比较负面"), LITTLE_NEG(3, "稍微负面"), NEUTRAL(4, "中性"), LITTLE_POS(5, "稍微正面"), STRONG_POS(
            6, "比较正面"), EXTREME_POS(7, "极度正面");
    private int code;
    private String desc;

    private Sentiment(int code, String desc) {
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

    public static Sentiment codeOf(int code) {
        for (Sentiment senti : Sentiment.values()) {
            if (senti.getCode() == code) {
                return senti;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return desc;
    }
}
