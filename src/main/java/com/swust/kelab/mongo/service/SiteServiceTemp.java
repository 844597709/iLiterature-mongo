package com.swust.kelab.mongo.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.swust.kelab.domain.Site;
import com.swust.kelab.mongo.dao.*;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.domain.vo.TempCrawlWebsiteVo;
import com.swust.kelab.repos.SiteDao;
import com.swust.kelab.repos.bean.GenericQuery;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.PageData;
import com.swust.kelab.web.model.QueryData;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zengdan on 2017/5/10.
 */
@Service(value = "siteServiceTemp")
public class SiteServiceTemp {
    @Resource
    private SiteDao siteDao;
    @Resource
    private WorksUpdateDaoTemp worksUpdateDao;
    @Resource
    private AuthorUpdateDaoTemp authorUpdateDao;
    @Resource
    private SiteDaoTemp siteDaoTemp;

    public QueryData viewSiteWithAuthorAndWorkCountByPage(EPOQuery iQuery) {
        QueryData queryData = new QueryData();
        int[] pageArray = iQuery.getPageArray();
        String searchWord = iQuery.getSearchWord();
        Integer perPageCount = iQuery.getRecordPerPage();
        if (perPageCount <= 0) {
            perPageCount = 10;
        }
        int totalCount = Long.valueOf(siteDaoTemp.getCount()).intValue();
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
            DBObject queryFields = new BasicDBObject();
            if (searchWord != null) {
//                queryFields.put("authName", Pattern.compile("^.*"+searchWord+".*$"));
                queryFields.put("crwsSiteName", new BasicDBObject("$regex", searchWord));
            }
            DBCursor cursor = siteDaoTemp.getDBCollection().find(queryFields).skip(i * perPageCount).limit(perPageCount);
            List<TempCrawlWebsiteVo> crawlWebsiteVos = Lists.newArrayList();
            while (cursor.hasNext()) {
                String json = JSON.toJSONString(cursor.next());
                TempCrawlWebsiteVo crawlWebsiteVo = JSON.parseObject(json, TempCrawlWebsiteVo.class);
                crawlWebsiteVos.add(crawlWebsiteVo);
            }
            pageDataList.add(new PageData(page, crawlWebsiteVos));
        }
        Long timeaa = System.currentTimeMillis();
        System.out.println("site-count:" + (timeaa - timea));
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;
    }

    @Deprecated
    public QueryData viewAuthorAndWorkNum(EPOQuery iQuery) {
        QueryData queryData = new QueryData();
        int[] pageArray = iQuery.getPageArray();
        String searchWord = iQuery.getSearchWord();
        Integer perPageCount = iQuery.getRecordPerPage();
        if (perPageCount <= 0) {
            perPageCount = 10;
        }
        int totalCount = siteDao.selectCount(new GenericQuery());
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
        for (int i = 0; i < pageArray.length; i++) {
            int page = pageArray[i];
            if (page <= 0 || page > totalPage) {
                continue;
            }
            ListQuery query = new GenericQuery();
            query.put("startIndex", i*perPageCount);
            query.put("maxCount", perPageCount);
            query.put("searchWord", searchWord);
            List<Site> sites = siteDao.selectList(query);
            if(sites.isEmpty()){
                continue;
            }
            for(Site site:sites) {
                Area lastTimeWithWorkCount = worksUpdateDao.selectLastTimeWithWorkCount(site.getSiteId());
                Area lastTimeWithAuthorCount = authorUpdateDao.selectLastTimeWithAuthorCount(site.getSiteId());

                String lastTime = lastTimeWithWorkCount.getName();
                if (StringUtils.isEmpty(lastTime)) {
                    lastTime = lastTimeWithAuthorCount.getName();
                }
                site.setAuthorUpdate(lastTime);
                site.setTotalAuthors(lastTimeWithAuthorCount.getValue());
                site.setTotalWorks(lastTimeWithWorkCount.getValue());
            }
            pageDataList.add(new PageData(page, sites));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;
    }
}
