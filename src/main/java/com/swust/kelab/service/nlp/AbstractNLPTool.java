package com.swust.kelab.service.nlp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.swust.kelab.domain.KESentence;
import com.swust.kelab.domain.KEWord;

/**
 * 接口的抽象实现类，默认实现了句子切割算法
 * 
 * @author longlongchang
 * 
 */
public abstract class AbstractNLPTool implements NLPTool {
	private Logger logger = LoggerFactory.getLogger("nlp");

	/**
	 * 从字符串中解析出多个句子
	 * 
	 * @param content
	 * @return
	 */
	@Override
	public List<KESentence> splitSentence(String content) {
		content = StringUtils.trimToEmpty(content);
		List<KESentence> senLists = Lists.newArrayList();
		if (content.length() <= NLPConfig.MIN_SPLIT_LENGTH) {
			senLists.add(new KESentence(content));
			return senLists;
		}
		Iterable<String> sents = Splitter.on(NLPConfig.SENTENCE_SPLIT).split(content);
		for (String sent : sents) {
			sent = StringUtils.trimToEmpty(sent);
			if (sent.length() >= NLPConfig.MAX_SENTENCE_LENGTH) {
				Iterable<String> smallSents = Splitter.on(NLPConfig.SMALL_SENTENCE_SPLIT).split(content);
				for (String smallSent : smallSents) {
					senLists.add(new KESentence(smallSent));
				}
			} else if (sent.length() > 0) {
				senLists.add(new KESentence(sent));
			}
		}
		logger.debug("文章:{},分句:{}", new Object[] { content, senLists });
		return senLists;
	}

	@Override
	public List<KESentence> splitSent(String content) {
		content = StringUtils.trimToEmpty(content);
		List<KESentence> senLists = Lists.newArrayList();
		if (content.length() <= NLPConfig.MIN_SPLIT_LENGTH) {
			senLists.add(new KESentence(content));
			return senLists;
		}
		Iterable<String> sents = Splitter.on(NLPConfig.SENTENCE_SPLIT).split(content);
		for (String sent : sents) {
			sent = StringUtils.trimToEmpty(sent);
			if (sent.length() > 10) {
				senLists.add(new KESentence(sent));
			}
		}
		logger.debug("文章:{},分句:{}", new Object[] { content, senLists });
		return senLists;
	}

	@Override
	public List<KEWord> segment(KESentence sentence) {
		return null;
	}

	@Override
	public List<KEWord> segPos(KESentence sentence) {
		return null;
	}

	@Override
	public List<KEWord> parse(KESentence sentence) {
		return null;
	}

	public boolean isNoun(KEWord word) {
		return false;
	}

	public boolean isAdj(KEWord word) {
		return false;
	}

}
