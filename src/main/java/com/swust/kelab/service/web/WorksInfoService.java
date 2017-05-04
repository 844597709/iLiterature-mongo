package com.swust.kelab.service.web;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.*;
import com.swust.kelab.repos.WorksInfoDao;
import com.swust.kelab.repos.bean.GenericQuery;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.web.model.*;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(value = "worksInfoService")
public class WorksInfoService {
    /*@Resource
    private AuthorDao authorDao;*/
    @Resource
    private WorksInfoDao worksInfoDao;
    @Resource
    HttpServletRequest request;
    public String[] allCommentWords; 

    //--zd-- 
    /**
     * @author zengdan
     * 小说前9种类别统计， 按类型点击量排名
     */
    public List<Area> selectWorkType(Integer siteId) throws Exception {
        List<Area> caList = worksInfoDao.selectWorkType(siteId);
        return caList;
    }
    
    /**
     * @author zengdan
     * 小说剩余所有类别统计
     */
    public long selectWorkRest(int siteId) throws Exception {
    	HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("siteId", siteId);
    	long workSome = worksInfoDao.selectWorkSome(siteId);
    	long workCount = worksInfoDao.selectCount(map);
        long workRest = workCount - workSome;
        return workRest;
    }
    
    /**
     * @author zengdan
     * 最热10部
     */
    public List<WorkDetail> selectHotWork(int ActionType) throws Exception {
        List<WorkDetail> caList = worksInfoDao.selectHotWork(ActionType);
        return caList;
    }
    
    /**
     * @author zengdan
     * 最近每日更新作者数、作品数
     */
    public List<AuthorWorkUpdate> selectUpdate() throws Exception {
    	int[] time = {1,2,3,4,5,6,7};
    	List<AuthorWorkUpdate> list = worksInfoDao.selectUpdate();
    	List<AuthorWorkUpdate> alist = new ArrayList<AuthorWorkUpdate>();
    	for(int k=0; k<time.length; k++){
    		
    		Date dNow = new Date();   //当前时间
    		Date dBefore = new Date();
    		Calendar calendar = Calendar.getInstance(); //得到日历
    		calendar.setTime(dNow);//把当前时间赋给日历
    		calendar.add(Calendar.DAY_OF_MONTH, -time[k]);  //设置为之前的时间
    		dBefore = calendar.getTime();   //得到之前的时间
    		SimpleDateFormat sdf=new SimpleDateFormat("MM-dd"); //设置时间格式
    		String defaultStartDate = sdf.format(dBefore);    //格式化之前的时间
    		int flag=0;
    		for(int i=0; i<list.size(); i++){
    			if(defaultStartDate.equals(list.get(i).getTime())){
        			AuthorWorkUpdate authorWork = new AuthorWorkUpdate(list.get(i).getTotalUpdateAuthors(), list.get(i).getTotalUpdateWorks(), defaultStartDate);
        			alist.add(authorWork);
        			flag = 1;
        			break;
        		}
    		}
    		if(flag == 0){
    			AuthorWorkUpdate authorWork = new AuthorWorkUpdate(0, 0, defaultStartDate);
    			alist.add(authorWork);
    		}
    	}
    	List<AuthorWorkUpdate> list1 = new ArrayList<AuthorWorkUpdate>();
    	for(int i=alist.size()-1; i>=0; i--){
    		list1.add(alist.get(i));
    	}
        return list1;
    }
    
    //点击量图
    public List<NameValuePair> selectWorkHit(int siteId) throws Exception {
		List<NameValuePair> hitAndWorkNum = new ArrayList<NameValuePair>();
		long[] each = {0,100000,500000,1000000,2000000,3000000,5000000,7000000,9000000,10000000,-1};
		for(int i=0; i<10; i++){
			if(i==0){
				//包括等于-1的（主要处理-1）
				List<WorkDetail> caList = worksInfoDao.selectWorkHit(each[i+1], each[i], siteId);
				NameValuePair authorWork = new NameValuePair(String.valueOf(each[i+1]), String.valueOf(caList.get(0).getTotalWorks()));
				hitAndWorkNum.add(authorWork);
			}else{
				List<WorkDetail> caList = worksInfoDao.selectWorkHit(each[i+1], each[i], siteId);
				NameValuePair authorWork = new NameValuePair(String.valueOf(each[i+1]), String.valueOf(caList.get(0).getTotalWorks()));
				hitAndWorkNum.add(authorWork);
			}
		}
		return hitAndWorkNum;
    }
    
    //评论数图
    public List<NameValuePair> selectWorkComment(int siteId) throws Exception {
		List<NameValuePair> hitAndWorkNum = new ArrayList<NameValuePair>();
		long[] each = {0,5,10,20,30,40,50,100,200,500,-1};
		for(int i=0; i<10; i++){
			if(i==0){
				//包括等于-1的（主要处理-1）
				List<WorkDetail> caList = worksInfoDao.selectWorkComment(each[i+1], each[i], siteId);
				NameValuePair authorWork = new NameValuePair(String.valueOf(each[i+1]), String.valueOf(caList.get(0).getTotalWorks()));
				hitAndWorkNum.add(authorWork);
			}else{
				List<WorkDetail> caList = worksInfoDao.selectWorkComment(each[i+1], each[i], siteId);
				NameValuePair authorWork = new NameValuePair(String.valueOf(each[i+1]), String.valueOf(caList.get(0).getTotalWorks()));
				hitAndWorkNum.add(authorWork);
			}
		}
		return hitAndWorkNum;
    }
    
    //推荐数图
    public List<NameValuePair> selectWorkRecom(int siteId) throws Exception {
		List<NameValuePair> hitAndWorkNum = new ArrayList<NameValuePair>();
		long[] each = {0,1000,5000,10000,20000,30000,50000,70000,90000,100000,-1};
		for(int i=0; i<10; i++){
			if(i==0){
				//包括等于-1的（主要处理-1）
				List<WorkDetail> caList = worksInfoDao.selectWorkRecom(each[i+1], each[i], siteId);
				NameValuePair authorWork = new NameValuePair(String.valueOf(each[i+1]), String.valueOf(caList.get(0).getTotalWorks()));
				hitAndWorkNum.add(authorWork);
			}else{
				List<WorkDetail> caList = worksInfoDao.selectWorkRecom(each[i+1], each[i], siteId);
				NameValuePair authorWork = new NameValuePair(String.valueOf(each[i+1]), String.valueOf(caList.get(0).getTotalWorks()));
				hitAndWorkNum.add(authorWork);
			}
		}
		return hitAndWorkNum;
    }
    //--至此--
    
    //--ljf--****************
   /* public List<WorkUpdate> selectWorkUpdateByID(int workId) throws Exception {
    	GenericQuery query = new GenericQuery();
        List<WorkUpdate> caList = worksInfoDao.selectWorkUpdateByID(query.fill("workId", workId));
        return caList;
    }*/
    public String selectWorkUpdateByWebsiteId(int websiteId) throws Exception {
    	GenericQuery query = new GenericQuery();
    	String caList = worksInfoDao.selectWorkUpdateByWebsiteId(query.fill("websiteId", websiteId));
        return caList;
    }
    
    public List<WorksUpdate> selectWorksUpdateById(int workId) throws Exception {
    	GenericQuery query = new GenericQuery();
        List<WorksUpdate> caList = worksInfoDao.selectWorksUpdateById(query.fill("workId", workId));
        return caList;
    }
    //--至此--***************
    
    /**
     * 查詢全部主題
     * 
     * @throws Exception
     */
    public List<WorksInfo> viewTop(int ActionType) throws Exception {
        List<WorksInfo> caList = worksInfoDao.selectTop(ActionType);
        return caList;
    }

    public List<WorksInfo> selectByAuthor(int authorId) throws Exception {
        GenericQuery query = new GenericQuery();
        List<WorksInfo> result = worksInfoDao.selectByAuthor(query.fill("authorId", authorId));
        return result;
    }

    /**
     * 分页查询主题分类
     * @author zengdan
     * @throws Exception
     */
    public QueryData viewAllWorksInfo(EPOQuery iQuery, int parameter, int orderDesc, int siteId) throws Exception {
        QueryData queryData = new QueryData();
        // 构造查询条件
        ListQuery query = iQuery.format();
        int[] pageArray = iQuery.getPageArray();
        int recordPerPage = iQuery.getRecordPerPage();
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("siteId", siteId);
        int totalCount=-1;
        if(siteId == 0){
        	totalCount = worksInfoDao.selectCount(map);
        	queryData.setTotalCount(totalCount);
        }
        if (totalCount == 0) {
            return queryData;
        }
        if (recordPerPage <= 0) {
            recordPerPage = 10;
        }
        query.fill("maxCount", recordPerPage);
        int totalPage = QueryData.computeTotalPage(totalCount, recordPerPage);
        queryData.setTotalPage(totalPage);
        List<PageData> pageDataList = Lists.newArrayList();
        // 未指定页数，则只读取前三页数据
        if (pageArray == null) {
            pageArray = new int[] { 1, 2, 3 };
        }
        // 分别获取每页的数据
        for (int i = 0; i < pageArray.length; i++) {
            int page = pageArray[i];
            if (page <= 0 || page > totalPage) {
                continue;
            }
            query.fill("startIndex", QueryData.computeStartIndex(page, recordPerPage));
            query.fill("parameter", parameter);
            query.fill("orderDesc", orderDesc);
            query.fill("siteId", siteId);
            List<WorksInfo> caList = worksInfoDao.selectList(query);
            if(siteId != 0){
            	totalCount = caList.size();
            	queryData.setTotalCount(totalCount);
            }
            pageDataList.add(new PageData(page, caList));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;
    }

    public WorksInfo viewWork(int workId) throws Exception {
        GenericQuery query = new GenericQuery();
        WorksInfo result = worksInfoDao.selectById(query.fill("workId", workId));
        return result;
    }

    public List<WorkDetail> selectByWork(int workId) throws Exception {
        GenericQuery query = new GenericQuery();
        List<WorkDetail> result = worksInfoDao.selectByWork(query.fill("workId", workId));
        return result;
    }

    /**
     * @author zhuhai
     * 评论词云
     */
    public List<Comment> commentsByWork(int workId) throws Exception {
        GenericQuery query = new GenericQuery();
        List<Comment> result = worksInfoDao.commentsByWork(query.fill("workId", workId));
        this.allCommentWords=null;
        for(int i=0;i<result.size();i++){
        	this.allCommentWords=(String[]) ArrayUtils.addAll(this.allCommentWords, result.get(i).rawwords);
        }
        CommentFreqs freqs=new CommentFreqs();
        List<Comment> wordcloud=freqs.getHighFreqWords(this.allCommentWords);
        return wordcloud;
    }
    public List<WorkDescription> selectByAuthor_description(int authorId) throws Exception {
        GenericQuery query = new GenericQuery();
        List<WorkDescription> result = worksInfoDao.selectByAuthor_description(query.fill("authorId", authorId));
        this.allCommentWords=null;
        for(int i=0;i<result.size();i++){
        	this.allCommentWords=(String[]) ArrayUtils.addAll(this.allCommentWords, result.get(i).Words);
        }
        DescriptionFreqs freqs=new DescriptionFreqs();
        List<WorkDescription> wordcloud=freqs.getHighFreqWords(this.allCommentWords);
        return wordcloud;
    }
}
