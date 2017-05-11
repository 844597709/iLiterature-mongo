package com.swust.kelab.service.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.swust.kelab.mongo.dao.AuthorUpdateDaoTemp;
import com.swust.kelab.mongo.dao.WorksUpdateDaoTemp;
import com.swust.kelab.mongo.domain.model.Area;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.Site;
import com.swust.kelab.repos.SiteDao;
import com.swust.kelab.repos.bean.GenericQuery;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.PageData;
import com.swust.kelab.web.model.QueryData;

@Service(value = "siteService")
public class SiteService {
	@Resource
	private SiteDao siteDao;
//	@Resource
//	HttpServletRequest request;
	@Resource
	private WorksUpdateDaoTemp worksUpdateDao;
	@Resource
	private AuthorUpdateDaoTemp authorUpdateDao;

	// --zd--
	public List<Site> selectSite() throws Exception {
		List<Site> list = siteDao.selectSite();
		List<Site> sites = new ArrayList<Site>();
		for(int i=0; i<list.size(); i++){
			if(!(list.get(i).getTotalAuthors() == 0 && list.get(i).getTotalWorks() == 0)){
				sites.add(list.get(i));
			}
		}
		return sites;
	}

	// --lj--
	/*public Site selectSiteById(int siteId) throws Exception {
		return siteDao.selectSiteById(siteId);
	}*/

	@Deprecated
	public QueryData viewAuthorAndWorkNum(EPOQuery iQuery) throws Exception {
		QueryData queryData = new QueryData();
		// 构造查询条件
		ListQuery query = new GenericQuery();
		int[] pageArray = iQuery.getPageArray();
		int recordPerPage = iQuery.getRecordPerPage();
		int totalCount=-1;
        if(iQuery.getSearchWord() == null){
        	totalCount = siteDao.selectCount(new GenericQuery());
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
			query.fill("searchWord", iQuery.getSearchWord());
			List<Site> list = siteDao.selectLastestUpdateTime();
			List<Site> authorWorkList = siteDao.selectAuthorWorkNum(query);
			for(int j=0; j<authorWorkList.size();j++){
				int flag=0;
				for(int k=0; k<list.size(); k++){
					if(list.get(k).getSiteId() == authorWorkList.get(j).getSiteId()){
						authorWorkList.get(j).setAuthorUpdate(list.get(k).getAuthorUpdate());
						flag=1;
						break;
					}
				}
				if(flag == 0){
					authorWorkList.get(j).setAuthorUpdate("0");
				}
			}
			if(iQuery.getSearchWord() != null){
            	totalCount = authorWorkList.size();
            	queryData.setTotalCount(totalCount);
            }
			pageDataList.add(new PageData(page, authorWorkList));
		}
		// 装载返回结果
		queryData.setPageData(pageDataList);
		return queryData;
	}

	/**
	 * 慢的很
	 */
	/*public QueryData viewAuthorAndWorkNum(EPOQuery iQuery) {
		QueryData queryData = new QueryData();
		int[] pageArray = iQuery.getPageArray();
		String searchWord = iQuery.getSearchWord();
		Integer perPageCount = iQuery.getRecordPerPage();
		if (perPageCount <= 0) {
			perPageCount = 10;
		}
		int totalCount = siteDao.selectCount(new GenericQuery());
		queryData.setTotalCount(totalCount);
		if (totalCount == 0) {
			return queryData;
		}
		queryData.setTotalCount(totalCount);
		int totalPage = QueryData.computeTotalPage(totalCount, perPageCount);
		queryData.setTotalPage(totalPage);
		// 未指定页数，则只读取前三页数据
		if (pageArray == null) {
			pageArray = new int[]{1, 2, 3};
		}
		List<PageData> pageDataList = Lists.newArrayList();
		// 分别获取每页的数据
		Long timea = System.currentTimeMillis();
		for (int i = 0; i < pageArray.length; i++) {
			int page = pageArray[i];
			if (page <= 0 || page > totalPage) {
				continue;
			}
			ListQuery query = new GenericQuery(i*perPageCount, perPageCount);
			query.put("searchWord", searchWord);
			List<Site> sites = siteDao.selectList(query);
			sites.forEach(site->{
				Area lastTimeWithWorkCount = worksUpdateDao.selectLastTimeWithWorkCount(site.getSiteId());
				Area lastTimeWithAuthorCount = authorUpdateDao.selectLastTimeWithAuthorCount(site.getSiteId());
				String lastTime = lastTimeWithWorkCount.getName();
				if(StringUtils.isEmpty(lastTime)){
					lastTime = lastTimeWithAuthorCount.getName();
				}
				site.setAuthorUpdate(lastTime);
				site.setTotalAuthors(lastTimeWithAuthorCount.getValue());
				site.setTotalWorks(lastTimeWithWorkCount.getValue());
			});
			pageDataList.add(new PageData(page, sites));
		}
		Long timeaa = System.currentTimeMillis();
		System.out.println("site-count:"+(timeaa-timea));
		// 装载返回结果
		queryData.setPageData(pageDataList);
		return queryData;
	}*/

	// --至此--

	/**
	 * 分页查询爬取网站
	 * 
	 * @param pageArray
	 * @param recordPerPage
	 * @return
	 * @throws Exception
	 */

	public QueryData viewAllSite(int[] pageArray, int recordPerPage) throws Exception {
		QueryData queryData = new QueryData();
		// 构造查询条件
		ListQuery query = new GenericQuery();
		int totalCount = siteDao.selectCount(new GenericQuery());
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
			List<Site> caList = siteDao.selectList(query);
			pageDataList.add(new PageData(page, caList));
		}
		// 装载返回结果
		queryData.setPageData(pageDataList);
		return queryData;
	}

	public Site viewMySite(int siteId) throws Exception {
		Site site = siteDao.selectList(new GenericQuery().fill("siteId", siteId)).get(0);
		return site;
	}

	/**
	 * 添加爬取网站
	 * 
	 * @throws Exception
	 */
	public String addSite(Site site) throws Exception {
		String result = "fail";
		List<Site> dataList = siteDao.selectList(new GenericQuery().fill("siteName", site.getSiteName()));
		if (dataList != null && dataList.size() > 0) {
			result = "isExist";
		} else {
			siteDao.insert(site);
			result = "success";
		}
		return result;
	}

	/**
	 * 编辑爬取网站
	 * 
	 * @param site
	 * @return
	 * @throws Exception
	 */

	public String editSite(Site site) throws Exception {
		String result = "fail";
		siteDao.update(site);
		result = "success";
		return result;
	}

	/**
	 * 删除爬取网站
	 * 
	 * @param siteId
	 * @return
	 * @throws Exception
	 */

	public String delSite(int siteId) throws Exception {
		String result = "fail";
		siteDao.delete(siteId);
		result = "success";
		return result;
	}
}
