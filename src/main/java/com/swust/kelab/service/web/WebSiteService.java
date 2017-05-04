package com.swust.kelab.service.web;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.WebSite;
import com.swust.kelab.repos.WebSiteDao;
import com.swust.kelab.repos.bean.GenericQuery;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.web.model.PageData;
import com.swust.kelab.web.model.QueryData;

@Service(value = "webSiteService")
public class WebSiteService {
    @Resource
    private WebSiteDao webSiteDao;
    @Resource
    HttpServletRequest request;

    /**
     * 分页查询主题分类
     * 
     * @throws Exception
     */
    public QueryData viewAllSite(int[] pageArray, int recordPerPage) throws Exception {
        QueryData queryData = new QueryData();
        // 构造查询条件
        ListQuery query = new GenericQuery();
        int totalCount = webSiteDao.selectCount(new GenericQuery());
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
            List<WebSite> caList = webSiteDao.selectList(query);
            pageDataList.add(new PageData(page, caList));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;
    }

    /**
     * 查詢全部主題
     * 
     * @throws Exception
     */
    public List<WebSite> viewAllTopicCate() throws Exception {
        List<WebSite> caList = webSiteDao.selectList(new GenericQuery());
        return caList;
    }

    /**
     * 添加主题分类
     * 
     * @throws Exception
     */
    public String addSite(WebSite webSite) throws Exception {
        String result = "fail";
        List<WebSite> caList = webSiteDao.selectList(new GenericQuery().fill("siteName", webSite.getSiteName()));
        if (caList != null && caList.size() > 0) {
            result = "isExist";
        } else {
            webSiteDao.insert(webSite);
            result = "success";
        }
        return result;
    }

    /*
     * 编辑主题分类 ，安装主题分类名称进行update
     */
    public String editSite(WebSite webSite) throws Exception {
        String result = "fail";
        int resultInt = webSiteDao.update(webSite);
        if (resultInt > 0) {
            result = "success";
        }
        return result;
    }

    public String delSite(int siteId) throws Exception {
        String result = "fail";
        webSiteDao.delete(siteId);
        result = "success";
        return result;
    }
}
