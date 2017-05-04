package com.swust.kelab.service.nlp;

import java.util.List;

import com.swust.kelab.domain.KESentence;
import com.swust.kelab.domain.KEWord;

/**
 * 自然语言处理的接口类
 * 
 * @author longlongchang
 * 
 */
public interface NLPTool {
    // 读取filename指定的文件内容进行分句放入dataset中，初始方法	
    public List<KESentence> splitSentence(String article);
    
    //摘要需要的分句方法
    public List<KESentence> splitSent(String content);
    // 分词
    public List<KEWord> segment(KESentence sentence);

    // 分词+词性标注
    public List<KEWord> segPos(KESentence sentence);

    // 语法解析
    public List<KEWord> parse(KESentence sentence);

    public boolean isNoun(KEWord word);

    public boolean isAdj(KEWord word);

    public boolean isVerb(KEWord word);
}
