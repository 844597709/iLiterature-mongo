package com.swust.kelab.service.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.swust.kelab.domain.KESentence;
import com.swust.kelab.domain.KEWord;
import com.swust.kelab.service.nlp.NLPTool;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 获取关键词
 * 
 * @author htx
 * 
 */
@Service
public class KeyWordsService implements InitializingBean {
    @Resource
    private NLPTool ansjTool;

    public List<Map<String, Object>> keywordsCount(String content) {
        // 首先对原始内容进行分句和分词
        HashMap<String, Integer> map = Maps.newHashMap();
        List<KESentence> sentenceList = ansjTool.splitSentence(content);
        for (KESentence sentence : sentenceList) {
            Result result = ToAnalysis.parse(sentence.getSentence());
            List<Term> terms = result.getTerms();
            new NatureRecognition().recognition(result);
            for (Term term : terms) {
                KEWord word = new KEWord();
                word.setWord(term.getName());
                word.setPos(term.natrue().natureStr);
                if (isSentiWord(word)) {
                    String keyword = term.getName();
                    if (map.containsKey(keyword)) {
                        map.put(keyword, map.get(keyword) + 1);
                    } else {
                        map.put(keyword, 1);
                    }
                }
            }
        }
        List<Map<String, Object>> sortMap = mapSort(map);
        return sortMap;
    }

    public List<Map<String, Object>> mapSort(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> mappingList = null;
        List<Map<String, Object>> list = Lists.newArrayList();
        mappingList = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(mappingList, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> mapping1, Map.Entry<String, Integer> mapping2) {
                return mapping2.getValue().compareTo(mapping1.getValue());
            }
        });
        int count = 50;
        if (mappingList.size() < 50)
            count = mappingList.size();
        for (int i = 0; i < count; i++) {
            Map<String, Object> map2 = new HashMap<String, Object>();
            Map.Entry<String, Integer> value = mappingList.get(i);
            map2.put("text", value.getKey());
            map2.put("weight", value.getValue());
            list.add(map2);
        }
        return list;
    }

    private boolean isSentiWord(KEWord word) {
        return ansjTool.isAdj(word) || ansjTool.isNoun(word) || ansjTool.isVerb(word) || word.getPos().equals("i");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
