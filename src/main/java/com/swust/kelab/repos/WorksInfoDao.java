package com.swust.kelab.repos;

import com.swust.kelab.domain.*;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.web.model.AuthorWorkUpdate;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository(value = "worksInfoDaoOld")
public class WorksInfoDao {
    @Resource
    private SqlSession sqlSession;
//    @Resource
//    HttpServletRequest request;
    
    
    //--ljf--
	/*public List<WorkUpdate> selectWorkUpdateByID(ListQuery query)
			throws Exception {
		return sqlSession.selectList("worksInfo.selectWorkUpdateByID", query);
	}*/
    public String selectWorkUpdateByWebsiteId(ListQuery query) throws Exception{
    	return sqlSession.selectOne("worksInfo.selectWorkUpdateByWebsiteId",query);
    }
    
    
    public List<WorksUpdate> selectWorksUpdateById(ListQuery query) throws Exception{
    	return sqlSession.selectList("worksInfo.selectWorksUpdateById",query);
    }
    
 
    //--至此--
    
    //--zd--
    public List<Area> selectWorkType(Integer siteId) throws Exception {
    	HashMap<String,Integer> map=new HashMap<String,Integer>();
    	map.put("siteId", siteId);
		List<Area> resultList = sqlSession.selectList("worksInfo.selectWorkType", map);
    	if(resultList.size()== 0){
    		resultList.add(new Area("无",0));
    		return resultList;
    	}else{
    		return resultList;
    	}
    }
    public long selectWorkSome(int siteId) throws Exception {
    	Long work=sqlSession.selectOne("worksInfo.selectWorkSome", siteId);
    	if(work==null){
    		return 0;
    	}
    	return work;
    	
    }
    
    public List<WorkDetail> selectHotWork(int ActionType) throws Exception {
        return sqlSession.selectList("worksInfo.selectHotWork", ActionType);
    }
    
    //最近每日更新作者数、作品数
    public List<AuthorWorkUpdate> selectUpdate() throws Exception {
        return sqlSession.selectList("worksInfo.selectUpdate");
    }
    
    //每个阶段点击量的作品数
    public List<WorkDetail> selectWorkHit(long maxBound, long minBound, int siteId) throws Exception {
    	Map<String, Long> map = new HashMap<String, Long>();
    	map.put("maxBound", maxBound);
    	map.put("minBound", minBound);
    	map.put("siteId", (long) siteId);
        return sqlSession.selectList("worksInfo.selectWorkHit", map);
    }
    
    //每个阶段评论数的作品数
    public List<WorkDetail> selectWorkComment(long maxBound, long minBound, int siteId) throws Exception {
    	Map<String, Long> map = new HashMap<String, Long>();
    	map.put("maxBound", maxBound);
    	map.put("minBound", minBound);
    	map.put("siteId", (long) siteId);
        return sqlSession.selectList("worksInfo.selectWorkComment", map);
    }

    //每个阶段推荐数的作品数
    public List<WorkDetail> selectWorkRecom(long maxBound, long minBound, int siteId) throws Exception {
    	Map<String, Long> map = new HashMap<String, Long>();
    	map.put("maxBound", maxBound);
    	map.put("minBound", minBound);
    	map.put("siteId", (long) siteId);
        return sqlSession.selectList("worksInfo.selectWorkRecom", map);
    }
    
    public int selectCount(HashMap<String, Integer> map) throws Exception {
    	Integer work=sqlSession.selectOne("worksInfo.selectCount", map);
    	if(work== null){
    		return 0;
    	}
    	return work;
    }
    //--至此--
    
    public int insert(Author author) throws Exception {
        return sqlSession.insert("worksInfo.insert", author);
    }

    public int update(Author author) throws Exception {
        return sqlSession.update("worksInfo.update", author);
    }

    public int delete(int authorId) throws Exception {
        return sqlSession.delete("worksInfo.delete", authorId);
    }
    
    public List<WorksInfo> selectList(ListQuery query) throws Exception {
        return sqlSession.selectList("worksInfo.select", query);
    }

    public List<WorksInfo> selectTop(int ActionType) throws Exception {
        return sqlSession.selectList("worksInfo.selectTop", ActionType);
    }

    public List<WorksInfo> selectByAuthor(ListQuery query) throws Exception {
        return sqlSession.selectList("worksInfo.selectByAuthor", query);
    }
    //作品简介词云
    public List<WorkDescription> selectByAuthor_description(ListQuery query) throws Exception {
        return sqlSession.selectList("worksInfo.selectByAuthor_description", query);
    }

    public long countWorks() throws Exception {
        return sqlSession.selectOne("worksInfo.countWorks");
    }

    //--zd--
    public long countWorksComments() throws Exception {
        return sqlSession.selectOne("worksInfo.countWorksComments");
    }
    //--至此--
    
    public WorksInfo selectById(ListQuery query) throws Exception {
        return sqlSession.selectOne("worksInfo.select", query);
    }

    public List<WorkDetail> selectByWork(ListQuery query) throws Exception {
        return sqlSession.selectList("worksInfo.selectByWork", query);
    }

    public List<Comment> commentsByWork(ListQuery query) throws Exception {
        return sqlSession.selectList("worksInfo.commentsByWork", query);
    }
}
