package com.swust.kelab.service.nlp;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.KESentence;
import com.swust.kelab.domain.KEWord;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Ansj，阿里孙健实现的ICTCLAS的java版本
 * 
 * @author longlongchang
 * 
 */
@Service
public class AnsjTool extends AbstractNLPTool implements NLPTool {
    private Logger logger = LoggerFactory.getLogger("datalog.nlp");

    @Override
    public List<KEWord> segPos(KESentence sent) {
        Result result = ToAnalysis.parse(sent.getSentence());
        List<Term> terms = result.getTerms();
        new NatureRecognition().recognition(result);
        logger.debug("句子:{},分词结果:{}", new Object[] { sent.getSentence(), terms });
        List<KEWord> wordList = Lists.newArrayList();
        for (Term term : terms) {
            KEWord word = new KEWord();
            word.setWord(term.getName());
            word.setPos(term.natrue().natureStr);
            wordList.add(word);
        }
        return wordList;
    }

    /**
     * 判断词语是否是形容词
     * 
     * @return
     */
    @Override
    public boolean isAdj(KEWord word) {
        String pos = word.getPos();
        return pos.equals("a") || pos.equals("ad") || pos.equals("ag") || pos.equals("an");
    }

    /**
     * 判断词语是否是名词
     * 
     * @return
     */
    @Override
    public boolean isNoun(KEWord word) {
        String pos = word.getPos();
        return pos.equals("n") || pos.equals("ng") || pos.equals("nr") || pos.equals("ns") || pos.equals("nt")
                || pos.equals("nx") || pos.equals("nz");
    }

    /**
     * 判断词语是否是动词
     * 
     * @return
     */
    @Override
    public boolean isVerb(KEWord word) {
        String pos = word.getPos();
        return pos.equals("v") || pos.equals("vd") || pos.equals("vg") || pos.equals("vn");
    }

	
}
