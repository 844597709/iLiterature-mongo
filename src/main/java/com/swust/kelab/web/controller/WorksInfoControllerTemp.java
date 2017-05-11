package com.swust.kelab.web.controller;

import com.swust.kelab.domain.Comment;
import com.swust.kelab.domain.WorkDescription;
import com.swust.kelab.mongo.domain.TempWorks;
import com.swust.kelab.mongo.domain.TempWorksUpdate;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.domain.vo.TempWorksVo;
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
    private static final String STARTTIME = "2016-12-10"; // 暂时写死
    private static final Integer DAYNUM = 10; // 展示近期天数
    private static final Integer WEEKNUM = 7; // 往回退的天数

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
        List<TempWorksVo> resultList = worksInfoService.selectHotTopWork(siteId, field, DESCORASC, TOPNUM);
        jv.addData("data", resultList);
        return jv;
    }

    @RequestMapping(value = "selectWorkUpdateByTime", method = RequestMethod.POST)
    public JsonAndView selectWorkUpdateByTime() {
        JsonAndView jv = new JsonAndView();
        List<AuthorWorkUpdate> resultList = worksInfoService.selectWorkUpdateByTime(DESCORASC, STARTTIME, DAYNUM);
        jv.addData("data", resultList);
        return jv;
    }

    @RequestMapping(value = "/viewAllWorksInfo", method = RequestMethod.POST)
    public JsonAndView viewAllWorksInfo(EPOQuery query, Integer field, Integer orderDesc, Integer siteId) {
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
    public JsonAndView countWorkNumAll(String hitsRange, String commentsRange, String recomsRange, Integer siteId) {
        System.out.println(hitsRange+" "+commentsRange+" "+recomsRange+" "+siteId);
        JsonAndView jv = new JsonAndView();
//        Map<String, Object> map = worksInfoService.countInfoNumAll(hitsRange, commentsRange, recomsRange, siteId, DESCORASC, PAGESIZE);
        Map<String, List<Area>> map = worksInfoService.countWorkInfoNum(hitsRange, commentsRange, recomsRange, siteId);
        if (map == null || map.size() == 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("没有符合条件的作者");
            return jv;
        }
        jv.addData("result", map);
        return jv;
    }

    //作品简介词云
    // TODO 首次太慢 15s，第二次就很快
    @RequestMapping(value = "selectWorksByAuthId", method = RequestMethod.POST)
    public JsonAndView selectWorksByAuthId(Integer authorId) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (authorId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        List<WorkDescription> resultList = worksInfoService.selectWorksDescByAuthId(authorId);
        jv.addData("data", resultList);
        return jv;
    }

    @RequestMapping(value = "selectByAuthor", method = RequestMethod.POST)
    public JsonAndView selectByAuthor(Integer authorId) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (authorId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        List<TempWorks> resultList = worksInfoService.selectWorksByAuthId(authorId);
        jv.addData("data", resultList);
        return jv;
    }

    @RequestMapping(value = "selectWorkById", method = RequestMethod.POST)
    public JsonAndView selectWorkById(Integer workId) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (workId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        TempWorksVo result = worksInfoService.selectWorkById(workId);
        jv.addData("data", result);
        return jv;
    }

    @RequestMapping(value = "selectCommentsByWorkId", method = RequestMethod.POST)
    public JsonAndView selectCommentsByWorkId(Integer workId) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (workId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        List<Comment> result = worksInfoService.selectCommentsByWorkId(workId);
        jv.addData("data", result);
        return jv;
    }

    /**
     * 今日往前推一周作品的点击量，评论量，推荐量
     */
    @RequestMapping(value = "selectWeekOfWorkInfo", method = RequestMethod.POST)
    public JsonAndView selectWeekOfWorkInfo(Integer workId, String endTime){
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (workId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        List<TempWorksUpdate> worksUpdateList = worksInfoService.selectWeekOfWorkInfo(workId, endTime, WEEKNUM);
        jv.addData("data", worksUpdateList);
        return jv;
    }
}
