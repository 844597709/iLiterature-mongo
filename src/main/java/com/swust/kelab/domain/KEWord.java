package com.swust.kelab.domain;

/**
 * 中文的一个词语
 * @author longlongchang
 *
 */
public class KEWord {
    private String word;
    private String pos;
    private String position;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return word;
    }
    
}
