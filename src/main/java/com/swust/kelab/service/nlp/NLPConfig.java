package com.swust.kelab.service.nlp;

import java.util.regex.Pattern;

public class NLPConfig {
    public static final int MIN_SPLIT_LENGTH = 20;// 最小切割长度，内容小于此长度则不进行按照句号进行分割
    public static final int MAX_SENTENCE_LENGTH = 100;//最大句子长度，超过此长度，则按照逗号进行分割
    public static final Pattern SENTENCE_SPLIT = Pattern.compile("\\.|。");
    public static final Pattern SMALL_SENTENCE_SPLIT = Pattern.compile("，|,");
}
