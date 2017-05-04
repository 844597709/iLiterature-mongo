package com.swust.kelab.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.swust.kelab.domain.WebSite;
import com.swust.kelab.service.web.WebSiteService;
import com.swust.kelab.web.json.JsonAndView;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.QueryData;

@Controller
@RequestMapping("webSite")
public class WebSiteController {
    @Resource
    private WebSiteService webSiteService;

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
        QueryData queryData = webSiteService.viewAllSite(query.getPageArray(), query.getRecordPerPage());
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

    @RequestMapping(value = "/addSite", method = RequestMethod.POST)
    public JsonAndView addSite(WebSite webSite) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (webSite == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        String result = webSiteService.addSite(webSite);
        jv.addData("result", result);
        return jv;
    }

    @RequestMapping(value = "/editSite", method = RequestMethod.POST)
    public JsonAndView editSite(WebSite webSite) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (webSite == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        String result = webSiteService.editSite(webSite);
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
        String result = webSiteService.delSite(siteId);
        jv.addData("result", result);
        return jv;
    }
}
