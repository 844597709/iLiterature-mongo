package com.swust.kelab.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.swust.kelab.domain.Site;
import com.swust.kelab.service.web.SiteService;
import com.swust.kelab.web.json.JsonAndView;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.QueryData;

@Controller
@RequestMapping("site")
public class SiteController {
    @Resource
    private SiteService siteService;

    //--zd--
    @RequestMapping(value = "/selectSite", method = RequestMethod.POST)
    public JsonAndView selectSite() throws Exception {
        JsonAndView jv = new JsonAndView();
        List<Site> site = siteService.selectSite();
        jv.addData("result", site);
        return jv;
    }
    
    @RequestMapping(value = "/viewAuthorAndWorkNum", method = RequestMethod.POST)
    public JsonAndView viewAuthorAndWorkNum(EPOQuery query) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (query == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        QueryData queryData = siteService.viewAuthorAndWorkNum(query);
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }
    //--至此--
    
    @RequestMapping(value = "/viewAllSite", method = RequestMethod.POST)
    public JsonAndView viewAllSite(EPOQuery query) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (query == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        QueryData queryData = siteService.viewAllSite(query.getPageArray(), query.getRecordPerPage());
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

    @RequestMapping(value = "/viewMySite", method = RequestMethod.POST)
    public JsonAndView viewMySite(Integer siteId) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (siteId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        Site site = siteService.viewMySite(siteId);
        jv.addData("iSite", site);
        return jv;
    }

    @RequestMapping(value = "/addSite", method = RequestMethod.POST)
    public JsonAndView addSite(Site site) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (site == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        String result = siteService.addSite(site);
        jv.addData("result", result);
        return jv;
    }

    @RequestMapping(value = "/editSite", method = RequestMethod.POST)
    public JsonAndView editSite(Site site) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (site == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        String result = siteService.editSite(site);
        jv.addData("result", result);
        return jv;
    }

    @RequestMapping(value = "/delSite", method = RequestMethod.POST)
    public JsonAndView delSite(Integer siteId) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (siteId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        String result = siteService.delSite(siteId);
        jv.addData("result", result);
        return jv;
    }
}
