package com.swust.kelab.domain;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 表示一个句子，包含若干词语
 * @author longlongchang
 *
 */
public class KESentence {
    private String sentence;// 句子内容
    private List<KEWord> wordList;// 分词和词性标注后的结果

    public KESentence(String sentence) {
        this.sentence = sentence;
        this.wordList = Lists.newArrayList();
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public List<KEWord> getWordList() {
        return wordList;
    }

    public void setWordList(List<KEWord> wordList) {
        this.wordList = wordList;
    }
}
