package com.swust.kelab.service.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;
import com.swust.kelab.domain.KEArticle;
import com.swust.kelab.domain.KESentence;
import com.swust.kelab.domain.KEWord;
import com.swust.kelab.service.nlp.NLPTool;

/**
 * 计算给定内容的情感强度，根据情感词典、程度副词词典、否定词典 考虑情感词周围的程度副词和否定词共同得出该情感词语的情感值。 一个句子中的情感词的情感值累加，归一化后得出句子的情感值。
 * 
 * @author longlongchang
 * 
 */
@Service
public class SentimentAnalysis implements NLPAnalysis, InitializingBean {
    private Logger logger = LoggerFactory.getLogger("datalog.opinion");
    private Map<String, Integer> sentiLexicon;
    private Set<String> negative;
    private Map<String, Float> degreeLexicon;
    private static String SENTI_LEXICON_PATH = "all_lexicon.txt";
    private static String NEGATIVE_LEXICON_PATH = "negative.txt";
    private static String DEGREE_LEXICON_PATH = "degree.txt";
    private static int AID_WINDOW = 3;// 否定词和程度副词的作用范围（只对后面的词语有效）

    private void initLexicon() {
        try {
            if (sentiLexicon == null) {
                loadSentiLexicon();
                logger.debug("加载情感词典");
            }
            if (negative == null) {
                loadNegatives();
                logger.debug("加载否定词");
            }
            if (degreeLexicon == null) {
                loadDegrees();
                logger.debug("加载程度副词");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Resource
    private NLPTool ansjTool;

    @Override
    public void analysis(KEArticle article) {
        article.getAnalysisResult().putAll(analysis(article.getSentenceList()));
    }

    @Override
    public Map<String, String> analysis(String content) {
        // 首先对原始内容进行分句和分词
        List<KESentence> sentenceList = ansjTool.splitSentence(content);
        for (KESentence sentence : sentenceList) {
            sentence.setWordList(ansjTool.segPos(sentence));
        }
        return analysis(sentenceList);
    }

    private Map<String, String> analysis(List<KESentence> sentenceList) {
        int sentiment = 0;
        int totalSentiWords = 0;
        // 识别内容中的情感词
        for (KESentence sentence : sentenceList) {
            List<KEWord> wordList = sentence.getWordList();
            for (int i = 0; i < wordList.size(); i++) {
                KEWord word = wordList.get(i);
                if (this.sentiLexicon.containsKey(word.getWord()) && isSentiWord(word)) {
                    // 识别出情感词前面距离K的否定词，对极性求反
                    int neg = 1;
                    float degree = 1;
                    for (int k = i - 1; k >= 0 && k >= i - AID_WINDOW; k--) {
                        String wordStr = wordList.get(k).getWord();
                        if (this.negative.contains(wordStr)) {
                            neg = -neg;
                        }
                        if (this.degreeLexicon.containsKey(wordStr)) {
                            degree *= this.degreeLexicon.get(wordStr);
                        }
                    }
                    sentiment += this.sentiLexicon.get(word.getWord()) * neg * degree;
                    totalSentiWords++;
                }
            }
        }
        // 累加极性值，返回结果
        Map<String, String> analysisResult = Maps.newHashMap();
        float sentimentValue = 0;
        if (totalSentiWords != 0) {
            sentimentValue = (float) sentiment / totalSentiWords;
        }
        analysisResult.put(AnalysisResult.SENTIMENT, sentimentValue + "");
        return analysisResult;
    }

    private boolean isSentiWord(KEWord word) {
        return ansjTool.isAdj(word) || ansjTool.isNoun(word) || ansjTool.isVerb(word) || word.getPos().equals("i");
    }

    private void loadDegrees() throws IOException {
        this.degreeLexicon = Maps.newHashMap();
        URL degreeLexiconUrl = Resources.getResource(DEGREE_LEXICON_PATH);
        float degree = 1;
        File file = new File(degreeLexiconUrl.getPath());
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charsets.UTF_8));
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (NumberUtils.isNumber(line)) {
                int level = NumberUtils.toInt(line);
                degree = Degree.getDegreeValue(level);
            } else {
                this.degreeLexicon.put(line, degree);
            }
        }
    }

    private void loadNegatives() throws IOException {
        this.negative = Sets.newHashSet();
        URL sentiLexiconUrl = Resources.getResource(NEGATIVE_LEXICON_PATH);
        Resources.readLines(sentiLexiconUrl, Charsets.UTF_8, new LineProcessor<String>() {
            @Override
            public boolean processLine(String line) throws IOException {
                if (!StringUtils.isBlank(line)) {
                    negative.add(line);
                    return true;
                }
                return false;
            }

            @Override
            public String getResult() {
                return null;
            }
        });
    }

    private void loadSentiLexicon() throws IOException {
        this.sentiLexicon = Maps.newHashMap();
        URL sentiLexiconUrl = Resources.getResource(SENTI_LEXICON_PATH);
        Resources.readLines(sentiLexiconUrl, Charsets.UTF_8, new LineProcessor<String>() {
            @Override
            public boolean processLine(String line) throws IOException {
                if (!StringUtils.isBlank(line)) {
                    String[] terms = line.split("\t");
                    if (terms.length != 2) {
                        return false;
                    }
                    sentiLexicon.put(terms[0], NumberUtils.toInt(terms[1]));
                    return true;
                }
                return false;
            }

            @Override
            public String getResult() {
                return null;
            }
        });
    }

    public enum Degree {
        MOST_DEGREE(3, 1), VERY_DEGREE(2, 2), MORE_DEGREE(1.5F, 3), LITTLE_DEGREE(0.5F, 4), INSUFFICIENT_DEGREE(-1.5F,
                5), OVER_DEGREE(-2F, 6);
        private float value;
        private int index;

        private Degree(float value, int index) {
            this.value = value;
            this.index = index;
        }

        public static float getDegreeValue(int index) {
            for (Degree degree : Degree.values()) {
                if (degree.getIndex() == index) {
                    return degree.value;
                }
            }
            return 0f;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initLexicon();
    }
}
