package com.swust.kelab.mongo.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.swust.kelab.mongo.dao.AuthorDaoTemp;
import com.swust.kelab.mongo.dao.base.PageResult;
import com.swust.kelab.mongo.domain.TempAuthor;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.dao.query.BaseQuery;
import com.swust.kelab.mongo.utils.CollectionUtil;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.QueryData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service(value = "authorService")
public class AuthorServiceTemp {
	@Resource
	private AuthorDaoTemp authorDao;

	/**
	 * 查询前topNum的作者
	 */
	public List<TempAuthor> selectHotTopAuthor(Integer siteId, Integer sortField, Integer descOrAsc, Integer topNum) {
		Integer tag = 1;
		if(descOrAsc == 0){
			tag = 1;
		}else{
			tag = -1;
		}
		List<TempAuthor> authorList = authorDao.selectHotTopAuthor(siteId, sortField, tag, topNum);
		return  authorList;
	}

	public QueryData viewAuthorByPage(EPOQuery iQuery, Integer siteId, Integer sortField, Integer descOrAsc, String searchArea){
		Integer tag = 1;
		if(descOrAsc == 0){
			tag = 1;
		}else{
			tag = -1;
		}
		QueryData queryData = authorDao.viewAuthorByPage(iQuery, siteId, sortField, tag, searchArea);
		return queryData;
	}

    public Map<String, Object> countInfoNumAll(String hitsRange, String commentsRange, String recomsRange, String worksRange, Integer siteId, Integer descOrAsc)
            throws Exception {
        //获取所有，不排序
        List<TempAuthor> allAuthorList = authorDao.selectHotTopAuthor(siteId, 0, descOrAsc, Integer.MAX_VALUE);
        if(allAuthorList.isEmpty()){
            return CollectionUtil.emptyMap();
        }
        List<TempAuthor> sortedAuthorByHitsNum = allAuthorList.stream().sorted((o1, o2) -> o1.getAuthWorksHitsNum().compareTo(o2.getAuthWorksHitsNum())).collect(Collectors.toList());
        List<TempAuthor> sortedAuthorByCommentsNum = allAuthorList.stream().sorted((o1, o2) -> o1.getAuthWorksCommentsNum().compareTo(o2.getAuthWorksCommentsNum())).collect(Collectors.toList());
        List<TempAuthor> sortedAuthorByRecomsNum = allAuthorList.stream().sorted((o1, o2) -> o1.getAuthWorksRecomsNum().compareTo(o2.getAuthWorksRecomsNum())).collect(Collectors.toList());
        List<TempAuthor> sortedAuthorByWorksNum = allAuthorList.stream().sorted((o1, o2) -> o1.getAuthWorksNum().compareTo(o2.getAuthWorksNum())).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("totalHits", this.getHitsRangeWithAuthorCount(1, hitsRange, sortedAuthorByHitsNum));
        map.put("commentsNum", this.getHitsRangeWithAuthorCount(2, commentsRange, sortedAuthorByCommentsNum));
        map.put("totalRecoms", this.getHitsRangeWithAuthorCount(3, recomsRange, sortedAuthorByRecomsNum));
        map.put("worksCount", this.getHitsRangeWithAuthorCount(4, worksRange, sortedAuthorByWorksNum));
        return map;
    }

    private List<Area> getHitsRangeWithAuthorCount(Integer field, String rangeStr, List<TempAuthor> sortAuthorList){
        List<Long> ranges = this.getRanges(rangeStr);
        List<Area> list = Lists.newArrayList();
        for(Long range:ranges){
            List<TempAuthor> rangeAuthorList = Lists.newArrayList();
            Integer num = 0;
            for(int i=0; i<sortAuthorList.size(); i++){
                if(field == 1){
                    num = sortAuthorList.get(i).getAuthWorksHitsNum();
                }else if(field == 2){
                    num = sortAuthorList.get(i).getAuthWorksCommentsNum();
                }else if(field == 3){
                    num = sortAuthorList.get(i).getAuthWorksRecomsNum();
                }else if(field == 4){
                    num = sortAuthorList.get(i).getAuthWorksNum();
                }
                if(num>range){
                    rangeAuthorList = sortAuthorList.subList(0, i);
                    sortAuthorList = sortAuthorList.subList(i, sortAuthorList.size());
                    break;
                }
            }
            list.add(new Area(String.valueOf(range), Long.valueOf(rangeAuthorList.stream().count()).intValue()));
        }
        return list;
    }

	private List<Long> getRanges(String rangeStr) {
		String[] strs = rangeStr.split(",");
        List<Long> ranges = Arrays.stream(strs).map(range -> Long.parseLong(range)).collect(Collectors.toList());
        return ranges;
	}

	/**
	 * 分页查询作者信息
	 * 
	 * @throws Exception
	 */
	public PageResult<TempAuthor> viewAllAuthor(BaseQuery query) {
		PageResult<TempAuthor> result = authorDao.query(query);
		return result;
	}

	/**
	 * 根据authId查询作者信息
	 */
	public TempAuthor viewAuthor(Integer authId) {
		TempAuthor author = authorDao.findById(authId);
		return author;
	}

	/**
	 * 统计性别
	 */
	public Map<String, Integer> countInfoGender(Integer wesiId) {
		List<Area> genderList = authorDao.countInfoGender(wesiId);
		Map<String, Integer> genderMap = Maps.newHashMap();
		genderList.forEach(area->{
			if("男".equals(area.getName())){
				genderMap.put("authorMan", area.getValue());
			}else if("女".equals(area.getName())){
				genderMap.put("authorWoman", area.getValue());
			}else{
				if(genderMap.containsKey("authorOther")){
					genderMap.put("authorOther", genderMap.get("authorOther")+area.getValue());
				}else{
					genderMap.put("authorOther", area.getValue());
				}
			}
		});
		return genderMap;
	}

	/**
	 * 统计省份作者数
	 */
	public Map<String, Integer> countInfoArea(Integer wesiId) {
		List<Area> areaList = authorDao.countInfoArea(wesiId);
		Map<String, Integer> areaMap = Maps.newHashMap();
		areaList.forEach(area->{
			if(area.getName()!="" && area.getName()!=null){
				String name = area.getName().split("-")[0].trim();//获取省份名称
				if(areaMap.containsKey(name)){
					areaMap.put(name, areaMap.get(name)+area.getValue());
				}else{
					areaMap.put(name, area.getValue());
				}
			}
		});
		return areaMap;
	}

	public Map<String, Object> countAuthorInfo(Integer websiteId, String worksR, String hitsR, String commentsR, String recomsR){
		String[] works = worksR.split(",");
		String[] hits = hitsR.split(",");
		String[] comments = commentsR.split(",");
		String[] recoms = recomsR.split(",");
		long timea = System.currentTimeMillis();
		Map<String, Object> map = Maps.newHashMap();
		map.put("authWorksNum", getNum(websiteId, "authWorksNum", works));
		map.put("authWorksHitsNum", getNum(websiteId, "authWorksHitsNum", hits));
		map.put("authWorksCommentsNum", getNum(websiteId, "authWorksCommentsNum", comments));
		map.put("authWorksRecomsNum", getNum(websiteId, "authWorksRecomsNum", recoms));
		long timeaa = System.currentTimeMillis();
		System.out.println("service: countAuthorInfo--消耗"+(timeaa-timea)+"ms");
		return map;
	}

	private List<Integer> getNum(Integer websiteId, String field, String[] ranges){
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0; i<ranges.length-1; i++){
			Integer start = Integer.parseInt(ranges[i]);
			Integer end = Integer.parseInt(ranges[i+1]);
			list.add(authorDao.getNum(websiteId, field, start, end));
		}
		return list;
	}
	// lj
	/*private String getNumStr(int type, long a, long b) {
		if (type == 4) {
			if (a + 1 == b)
				return String.valueOf(a) + "部";
			return String.valueOf(a) + "~" + String.valueOf(b) + "部";
		} else
			return String.valueOf(a) + "~" + String.valueOf(b);
	}

	// lj
	private String getNumStr(int type, long a) {
		if (type == 4)
			return String.valueOf(a) + "部及以上";
		else
			return String.valueOf(a) + "以上";
	}

	// lj
	public List<NameValuePair> countInfoNumByJava(int type, String range, List<Author> authors) throws Exception {
		if (authors == null || authors.size() == 0)
			authors = authorDao.selectList(new GenericQuery());// 所有作者,没加siteId哦
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		long[] r = getRange(range);
		long[] n = new long[r.length];
		for (int i = 0; i < n.length; i++)
			n[i] = 0;
		for (int i = 0; i < authors.size(); i++) {
			long num = authors.get(i).getNum(type);
			if (num < 0 || num < r[0])
				continue;
			if (num >= r[r.length - 1]) {
				n[n.length - 1]++;
				continue;
			}
			for (int j = 1; j < r.length; j++) {
				if (num < r[j]) {
					n[j - 1]++;
					break;
				}
			}
		}
		for (int i = 0; i < n.length - 1; i++)
			list.add(new NameValuePair(getNumStr(type, r[i], r[i + 1]), String.valueOf(n[i])));
		list.add(new NameValuePair(getNumStr(type, r[r.length - 1]), String.valueOf(n[n.length - 1])));
		return list;
	}

	// lj
	private long[] getRange(String rangeStr) {
		String str[] = rangeStr.split(",");
		long[] range = new long[str.length];
		for (int i = 0; i < str.length; i++) {
			range[i] = Long.parseLong(str[i]);
		}
		return range;
	}

	// lj
	public Map<String, Object> countInfoNumAll(String range1, String range2, String range3, String range4, int siteId)
			throws Exception {
		GenericQuery gq = new GenericQuery();
		gq.put("siteId", siteId);
		List<Author> authors = authorDao.selectList(gq);
		if (authors == null || authors.size() == 0)
			return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalHits", countInfoNumByJava(1, range1, authors));
		map.put("commentsNum", countInfoNumByJava(2, range2, authors));
		map.put("totalRecoms", countInfoNumByJava(3, range3, authors));
		map.put("worksCount", countInfoNumByJava(4, range4, authors));
		return map;
	}*/
	//cdk
	/*public List<Area> countAreaInfo(Integer siteId){
		return authorDao.countAreaInfo(siteId);
	}
	
	//cdk
	public Area countAreaMaxPeople(Integer siteId){
		return authorDao.countAreaMaxPeople(siteId);
	}
	//cdk
	public int countAreaSumPeople(Integer siteId){
		return authorDao.countAreaSumPeople(siteId);
	}*/
}
