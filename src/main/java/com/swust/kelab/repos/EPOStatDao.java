package com.swust.kelab.repos;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.swust.kelab.domain.EPOStatResult;
import com.swust.kelab.domain.MetaSearch;
import com.swust.kelab.repos.bean.Query;
import com.swust.kelab.web.model.EPOTimeStatistic;
import com.swust.kelab.web.model.TimeUnit;

@Repository(value = "epoStatDao")
public class EPOStatDao {
	@Resource
	private SqlSession sqlSession;

	public List<EPOStatResult> statByTopic(Date startTime, Date endTime, int userId) {
		Map<String, Object> parameters = Maps.newHashMap();
		if (userId > 0) {
			parameters.put("userId", userId);
		}
		if (startTime != null) {
			parameters.put("startTime", startTime);
		}
		if (endTime != null) {
			parameters.put("endTime", endTime);
		}
		return sqlSession.selectList("epostat.statByTopic", parameters);
	}

	public List<EPOStatResult> statByMetaSearch(Date startTime, Date endTime, int userId) {
		Map<String, Object> parameters = Maps.newHashMap();
		if (userId > 0) {
			parameters.put("userId", userId);
		}
		if (startTime != null) {
			parameters.put("startTime", startTime);
		}
		if (endTime != null) {
			parameters.put("endTime", endTime);
		}
		return sqlSession.selectList("epostat.statByMetaSearch", parameters);
	}

	// queryMate 返回单个查询条目
	public MetaSearch queryOneMetaSearch(int metaId) {
		return sqlSession.selectOne("metasearch.selectMeta", metaId);
	}

	// queryMate 返回全部
	public List<MetaSearch> queryMetaSearch() {
		return sqlSession.selectList("metasearch.select");
	}
	// queryNewtime 一键返回所有最新引擎发布时间
	public Map queNewTimeMetaSearch(String ids,String webTypes) {
		String allId[] = ids.split(";");
		String allType[] = webTypes.split(";");
		Map<Integer, Object> parameters = Maps.newHashMap();
		for(int i=0;i<allId.length;i++){
			if(allType[i].equals("Web")||allType[i].equals("News")){
				Map<String, Object> parameterscall = Maps.newHashMap();
				parameterscall.put("metaId", Integer.parseInt(allId[i]));
				String time = sqlSession.selectOne("metasearch.selectNewTimeWeb",parameterscall);
				if(time==null||time==""){
					time = "暂未启动引擎";
				}
				parameters.put(i, time);
			}
			if(allType[i].equals("Weibo")){
				if(Integer.parseInt(allId[i])==14){
					Map<String, Object> parameterscall = Maps.newHashMap();
					parameterscall.put("weiboName","新浪微博");
					String time = sqlSession.selectOne("metasearch.selectNewTimeWeibo",parameterscall);
					if(time==null||time==""){
						time = "暂未启动引擎";
					}
					parameters.put(i, time);
				}
                if(Integer.parseInt(allId[i])==15){
                	Map<String, Object> parameterscall = Maps.newHashMap();
    				parameterscall.put("weiboName","腾讯微博");
                	String time = sqlSession.selectOne("metasearch.selectNewTimeWeibo",parameterscall);
    				if(time==null||time==""){
    					time = "暂未启动引擎";
    				}
    				parameters.put(i, time);
				}
				/*Map<String, Object> parameterscall = Maps.newHashMap();
				parameterscall.put("metaId", Integer.parseInt(allId[i]));*/
			}
		}
		return parameters;
	}
	// modmetaSearch
	public int modMetaSearch(int metaId,String metaCate, String metaName,String metaPrefix,String metaPostfix,String metaCodeFormate,String metaDivTag,String metaUrlTag,String metaTitleTag,String metaSummTag,String metaDateTag,int metaSleep,int metaThread,int metaHighSearch,String metaSiteNameTag) {
		Map<String, Object> parameters = Maps.newHashMap();
		if (metaId > 0) {
			parameters.put("metaId", metaId);
		}
		if (metaCate != null) {
			parameters.put("metaCate", metaCate);
		}
		if (metaName != null) {
			parameters.put("metaName", metaName);
		}
		if (metaPrefix != null) {
			parameters.put("metaPrefix", metaPrefix);
		}
		parameters.put("metaPostfix", metaPostfix);
		if (metaCodeFormate != null) {
			parameters.put("metaCodeFormate", metaCodeFormate);
		}
		if (metaDivTag != null) {
			parameters.put("metaDivTag", metaDivTag);
		}
		if (metaUrlTag != null) {
			parameters.put("metaUrlTag", metaUrlTag);
		}
		if (metaTitleTag != null) {
			parameters.put("metaTitleTag", metaTitleTag);
		}
		if (metaSummTag != null) {
			parameters.put("metaSummTag", metaSummTag); 
		}
		parameters.put("metaDateTag", metaDateTag);
		parameters.put("metaSleep", metaSleep);
		parameters.put("metaThread", metaThread);
		if (metaHighSearch > 0) {
			parameters.put("metaHighSearch", metaHighSearch);
		}
		parameters.put("metaSiteNameTag", metaSiteNameTag);
		return sqlSession.update("metasearch.updateMetasearch", parameters);
	}

	public int statByTimeRange(Query query) {
		return (Integer) sqlSession.selectOne("epostat.statByTimeRange", query);
	}

	public List<EPOTimeStatistic> simpleStatTimeStatistic(Query query, TimeUnit unit) {
		String dateFormat = "%Y";
		switch (unit) {
		case MONTH:
			dateFormat = "%Y-%m";
			break;
		case WEEK:
			dateFormat = "%X %V";
			break;
		case DAY:
			dateFormat = "%Y-%m-%d";
			break;
		case HOUR:
			dateFormat = "%Y-%m-%d %H";
			break;
		}
		query.fill("dateFormat", dateFormat);
		return sqlSession.selectList("epostat.simpleStatByTimeRange", query);
	}
	public List<EPOTimeStatistic> sentimentCount(Query query, TimeUnit unit) {
	    String dateFormat = "%Y";
	    switch (unit) {
	    case MONTH:
	        dateFormat = "%Y-%m";
	        break;
	    case WEEK:
	        dateFormat = "%X %V";
	        break;
	    case DAY:
	        dateFormat = "%Y-%m-%d";
	        break;
	    case HOUR:
	        dateFormat = "%Y-%m-%d %H";
	        break;
	    }
	    query.fill("dateFormat", dateFormat);
	    return sqlSession.selectList("epostat.sentimentCount", query);
	}
	public List<EPOTimeStatistic> weiboTimeRange(Query query, TimeUnit unit) {
		String dateFormat = "%Y";
		switch (unit) {
		case MONTH:
			dateFormat = "%Y-%m";
			break;
		case WEEK:
			dateFormat = "%X %V";
			break;
		case DAY:
			dateFormat = "%Y-%m-%d";
			break;
		case HOUR:
			dateFormat = "%Y-%m-%d %H";
			break;
		}
		query.fill("dateFormat", dateFormat);
		return sqlSession.selectList("epostat.weiboTimeRange", query);
	}

	public int statUpdateMeta(Query query) {
		return sqlSession.selectList("epostat.statUpdateMetaSearch", query).size();
	}

	public int statWarningEPO(Query query) {
		return (Integer) sqlSession.selectOne("epostat.statWarningEPO", query);
	}
}
