package com.swust.kelab.service.web;

/*import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.Area;
import com.swust.kelab.domain.Author;
import com.swust.kelab.domain.AuthorUpdate;
import com.swust.kelab.repos.AuthorDao;
import com.swust.kelab.repos.SiteDao;
import com.swust.kelab.repos.WorksInfoDao;
import com.swust.kelab.repos.bean.GenericQuery;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.NameValuePair;
import com.swust.kelab.web.model.PageData;
import com.swust.kelab.web.model.QueryData;

@Service(value = "authorService")
public class AuthorService {
	@Resource
	private AuthorDao authorDao;
	@Resource
	private SiteDao siteDao;
	@Resource
	private WorksInfoDao worksInfoDao;
	@Resource
	HttpServletRequest request;*/

	/**
	 * 分页查询主题分类
	 * 
	 * @throws Exception
	 */
	/*public QueryData viewAllAuthor(EPOQuery iQuery, int siteId, int field, int desc, String searchArea)
			throws Exception {
		QueryData queryData = new QueryData();
		// 构造查询条件
		// 构造查询条件
		ListQuery query = iQuery.format();
		int[] pageArray = iQuery.getPageArray();
		int recordPerPage = iQuery.getRecordPerPage();
		query.fill("orderField", field);
		query.fill("orderDesc", desc);
		query.fill("searchArea", searchArea);
		query.fill("siteId", siteId);
		int totalCount = authorDao.selectCount(query);
		queryData.setTotalCount(totalCount);
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
			List<Author> caList = authorDao.selectList(query);
			// System.out.println("第三个："+caList.get(2).authorName);
			pageDataList.add(new PageData(page, caList));
		}
		// 装载返回结果
		queryData.setPageData(pageDataList);
		return queryData;
	}*/

	/**
	 * 查詢全部主題
	 * 
	 * @throws Exception
	 */
	/*public List<Author> viewAllTopicCate() throws Exception {
		List<Author> caList = authorDao.selectList(new GenericQuery());
		return caList;
	}*/

	/**
	 * 添加主题分类
	 * 
	 * @throws Exception
	 */
	/*public String addAuthor(Author author) throws Exception {
		String result = "fail";
		List<Author> dataList = authorDao.selectList(new GenericQuery().fill("authorName", author.getAuthorName()));
		if (dataList != null && dataList.size() > 0) {
			result = "isExist";
		} else {
			authorDao.insert(author);
			result = "success";
		}
		return result;
	}*/

	/*
	 * 编辑主题分类 ，安装主题分类名称进行update
	 */
	/*public String editAuthor(Author author) throws Exception {
		String result = "fail";
		List<Author> dataList = authorDao.selectList(new GenericQuery().fill("authorName", author.getAuthorName()));
		if (dataList != null && dataList.size() > 1) {
			result = "isExist";
		} else {
			int resultInt = authorDao.update(author);
			if (resultInt > 0) {
				result = "success";
			}
		}
		return result;
	}

	public String delAuthor(int authorId) throws Exception {
		String result = "fail";
		authorDao.delete(authorId);
		result = "success";
		return result;
	}

	public Author viewAuthor(int authorId) throws Exception {
		GenericQuery query = new GenericQuery();
		Author result = authorDao.selectOne(query.fill("authorId", authorId));
		String audescription = result.getDescription();
		if (audescription != null) {
			String[] descriptionArray = audescription.split("，");
			if (descriptionArray.length >= 2) {
				if (descriptionArray[0] == "男" || descriptionArray[0] == "女")
					result.setGender(descriptionArray[0]);
				if (descriptionArray[1].indexOf("-") != -1)
					result.setArea(descriptionArray[1]);
			} else {
				if (descriptionArray[0] == "男" || descriptionArray[0] == "女")
					result.setGender(descriptionArray[0]);
			}

		}
		return result;
	}

	public List<AuthorUpdate> viewAuthorUpdate(int auupAuthId) throws Exception {
		GenericQuery query = new GenericQuery();
		List<AuthorUpdate> auupList = authorDao.selectAuthorUpdate(query.fill("auupAuthId", auupAuthId));
		return auupList;
	}*/

	/*public Map<String, Long> countInfo() throws Exception {
		Map<String, Long> map = new HashMap<String, Long>();
		long author = authorDao.countAuthor(0);
		long site = siteDao.countSite();
		long works = worksInfoDao.countWorks();
		long worksComments = worksInfoDao.countWorksComments();
		map.put("author", author);
		map.put("site", site);
		map.put("works", works);
		map.put("worksComments", worksComments);
		return map;
	}*/

	// lj
	/*public Map<String, Long> countInfoGender(int siteId) throws Exception {
		Map<String, Long> map = new HashMap<String, Long>();
		long authorAll = authorDao.countAuthor(siteId);
		long authorMan = authorDao.countAuthorGender("男", siteId);
		long authorWoman = authorDao.countAuthorGender("女", siteId);
		long authorOther = authorAll - authorMan - authorWoman;
		map.put("authorAll", authorAll);
		map.put("authorMan", authorMan);
		map.put("authorWoman", authorWoman);
		map.put("authorOther", authorOther);
		return map;
	}*/

	// lj
	/*public Map<String, Integer> countInfoArea(int siteId) throws Exception {
		Map<String, Integer> map = getProvinceMap();
		ListQuery lq = new GenericQuery();
		lq.put("siteId", siteId);
		List<Author> authors = authorDao.selectList(lq);
		for (int i = 0; i < authors.size(); i++) {
			String province = authors.get(i).getAreaProvince();
			Integer num = map.get(province);
			if (num == null)
				continue;
			map.put(province, num.intValue() + 1);
		}
		return map;
	}*/

	// lj
	/*private Map<String, Integer> getProvinceMap() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("河北", 0);
		map.put("山西", 0);
		map.put("辽宁", 0);
		map.put("吉林", 0);
		map.put("黑龙江", 0);
		map.put("江苏", 0);
		map.put("浙江", 0);
		map.put("安徽", 0);
		map.put("福建", 0);
		map.put("江西", 0);
		map.put("山东", 0);
		map.put("河南", 0);
		map.put("湖北", 0);
		map.put("湖南", 0);
		map.put("广东", 0);
		map.put("海南", 0);
		map.put("四川", 0);
		map.put("贵州", 0);
		map.put("云南", 0);
		map.put("陕西", 0);
		map.put("甘肃", 0);
		map.put("青海", 0);
		map.put("台湾", 0);
		map.put("内蒙古", 0);
		map.put("广西", 0);
		map.put("西藏", 0);
		map.put("宁夏", 0);
		map.put("新疆", 0);
		map.put("香港", 0);
		map.put("澳门", 0);
		map.put("北京", 0);
		map.put("重庆", 0);
		map.put("天津", 0);
		map.put("上海", 0);
		return map;
	}*/

	// lj
	/*public List<NameValuePair> countInfoNum(int type, String range) throws Exception {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		long minNum = -1, maxNum = -1;
		String rang1[] = range.split(",");
		for (int i = 0; i < rang1.length - 1; i++) {
			minNum = Long.parseLong(rang1[i]);
			maxNum = Long.parseLong(rang1[i + 1]);
			list.add(new NameValuePair(getNumStr(type, minNum, maxNum),
					String.valueOf(authorDao.countAuthorNum(type, minNum, maxNum))));
		}
		maxNum = Long.parseLong(rang1[rang1.length - 1]);
		list.add(
				new NameValuePair(getNumStr(type, maxNum), String.valueOf(authorDao.countAuthorNum(type, maxNum, -1))));
		return list;
	}*/

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
	}*/

	// lj
	/*private long[] getRange(String rangeStr) {
		String str[] = rangeStr.split(",");
		long[] range = new long[str.length];
		for (int i = 0; i < str.length; i++) {
			range[i] = Long.parseLong(str[i]);
		}
		return range;
	}*/

	// lj
	/*public Map<String, Object> countInfoNumAll(String range1, String range2, String range3, String range4, int siteId)
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
//	public List<Area> countAreaInfo(Integer siteId){
//		return authorDao.countAreaInfo(siteId);
//	}
	
	//cdk
//	public Area countAreaMaxPeople(Integer siteId){
//		return authorDao.countAreaMaxPeople(siteId);
//	}
	//cdk
//	public int countAreaSumPeople(Integer siteId){
//		return authorDao.countAreaSumPeople(siteId);
//	}
//}
