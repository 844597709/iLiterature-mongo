package com.swust.kelab.web.controller;

import com.swust.kelab.mongo.domain.TempWorks;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.service.WorksInfoServiceTemp;
import com.swust.kelab.web.json.JsonAndView;
import com.swust.kelab.web.model.AuthorWorkUpdate;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.QueryData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("worksInfo")
public class WorksInfoControllerTemp {
    private static final Integer DESCORASC = -1; // 默认降序
    private static final Integer TOPNUM = 10; // top值
    private static final Integer DAYNUM = 10; // 展示近期天数

    @Resource
    private WorksInfoServiceTemp worksInfoService;

    @RequestMapping(value = "selectWorkType", method = RequestMethod.POST)
    public JsonAndView selectWorkType(Integer siteId) {
        JsonAndView jv = new JsonAndView();
        List<Area> resultList = worksInfoService.selectWorkType(siteId, TOPNUM);
        jv.addData("data", resultList);
        return jv;
    }

    @RequestMapping(value = "selectHotTopWork", method = RequestMethod.POST)
    public JsonAndView selectHotTopWork(Integer siteId, Integer field) {
        JsonAndView jv = new JsonAndView();
        List<TempWorks> resultList = worksInfoService.selectHotTopWork(siteId, field, DESCORASC, TOPNUM);
        jv.addData("data", resultList);
        return jv;
    }

    @RequestMapping(value = "selectWorkUpdateByTime", method = RequestMethod.POST)
    public JsonAndView selectWorkUpdateByTime() {
        JsonAndView jv = new JsonAndView();
        // TODO 后期有时间再修改，加入开始时间
        List<AuthorWorkUpdate> resultList = worksInfoService.selectWorkUpdateByTime(DAYNUM);
        jv.addData("data", resultList);
        return jv;
    }

    @RequestMapping(value = "/viewAllWorksInfo", method = RequestMethod.POST)
    public JsonAndView viewAllWorksInfo(EPOQuery query, Integer field, Integer orderDesc, Integer siteId) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (query == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        QueryData queryData = worksInfoService.viewWorkByPage(query, siteId, field, orderDesc);
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

    // 一次全部查出作者统计：range1~4：1点击量、2评论数、3推荐数、4作品数
    @RequestMapping(value = "/countWorkInfoNumAll", method = RequestMethod.POST)
    public JsonAndView countWorkNumAll(String hitsRange, String commentsRange, String recomsRange, Integer siteId)
            throws Exception {
        System.out.println(hitsRange+" "+commentsRange+" "+recomsRange+" "+siteId);
        JsonAndView jv = new JsonAndView();
        Map<String, Object> map = worksInfoService.countInfoNumAll(hitsRange, commentsRange, recomsRange, siteId, DESCORASC);
        if (map == null || map.size() == 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("没有符合条件的作者");
            return jv;
        }
        jv.addData("result", map);
        return jv;
    }
}
