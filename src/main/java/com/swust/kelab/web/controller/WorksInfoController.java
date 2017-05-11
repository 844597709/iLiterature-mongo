package com.swust.kelab.web.controller;

import com.swust.kelab.domain.*;
import com.swust.kelab.service.web.WorksInfoService;
import com.swust.kelab.web.json.JsonAndView;
import com.swust.kelab.web.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("worksInfoOld")
public class WorksInfoController {
    /*@Resource
    private WorksInfoService worksInfoService;

    //cdk 2016/5/12
    @RequestMapping(value = "selectWorkType", method = RequestMethod.POST)
    public JsonAndView selectWorkType(Integer siteId) throws Exception {
        JsonAndView jv = new JsonAndView();
        List<Area> resultList = worksInfoService.selectWorkType(siteId);
        long otherNumber=worksInfoService.selectWorkRest(siteId);
        if(otherNumber!= 0){
        	Area nameValue = new Area("其他类型", (int) otherNumber);
        	resultList.add(nameValue);
        }
        jv.addData("data", resultList);
        return jv;
    }
    //cdk 2016/5/12
    @RequestMapping(value = "selectHotWork", method = RequestMethod.POST)
    public JsonAndView selectHotWork(Integer ActionType) throws Exception {
        JsonAndView jv = new JsonAndView();
        List<WorkDetail> resultList = worksInfoService.selectHotWork(ActionType);
        jv.addData("data", resultList);
        return jv;
    }
    
    @RequestMapping(value = "selectNovelUpdateByTime", method = RequestMethod.POST)
    public JsonAndView selectNovelUpdateByTime() throws Exception {
        JsonAndView jv = new JsonAndView();
        List<AuthorWorkUpdate> resultList = worksInfoService.selectUpdate();
        jv.addData("data", resultList);
        return jv;
    }
    
    //cdk 作品点击图
    @RequestMapping(value = "selectWorkHit", method = RequestMethod.POST)
    public JsonAndView selectWorkHit(Integer siteId) throws Exception {
        JsonAndView jv = new JsonAndView();
        List<NameValuePair> resultList = worksInfoService.selectWorkHit(siteId);
        jv.addData("data", resultList);
        return jv;
    }
    
    //cdk 评论数图
    @RequestMapping(value = "selectWorkComment", method = RequestMethod.POST)
    public JsonAndView selectWorkComment(Integer siteId) throws Exception {
        JsonAndView jv = new JsonAndView();
        List<NameValuePair> resultList = worksInfoService.selectWorkComment(siteId);
        jv.addData("data", resultList);
        return jv;
    }
    
    //cdk 推荐数图
    @RequestMapping(value = "selectWorkRecom", method = RequestMethod.POST)
    public JsonAndView selectWorkRecom(Integer siteId) throws Exception {
        JsonAndView jv = new JsonAndView();
        List<NameValuePair> resultList = worksInfoService.selectWorkRecom(siteId);
        jv.addData("data", resultList);
        return jv;
    }
    //--至此--
    //cdk 所有作品信息
    @RequestMapping(value = "/viewAllWorksInfo", method = RequestMethod.POST)
    public JsonAndView viewAllWorksInfo(EPOQuery query, Integer parameter, Integer orderDesc, Integer siteId) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (query == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        QueryData queryData = worksInfoService.viewAllWorksInfo(query, parameter, orderDesc, siteId);
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

    @RequestMapping(value = "viewTop", method = RequestMethod.POST)
    public JsonAndView viewTop(Integer ActionType) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (ActionType <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
   
        jv.addData("test", "ah");
        return jv;
    }

    @RequestMapping(value = "selectByAuthor", method = RequestMethod.POST)
    public JsonAndView selectByAuthor(Integer authorId) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (authorId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        List<WorksInfo> resultList = worksInfoService.selectByAuthor(authorId);
        jv.addData("data", resultList);
        return jv;
    }

    //作品简介词云
    @RequestMapping(value = "selectByAuthor_description", method = RequestMethod.POST)
    public JsonAndView selectByAuthor_description(Integer authorId) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (authorId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        List<WorkDescription> resultList = worksInfoService.selectByAuthor_description(authorId);
        jv.addData("data", resultList);
        return jv;
    }

    @RequestMapping(value = "viewWork", method = RequestMethod.POST)
    public JsonAndView viewWork(Integer workId) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (workId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        WorksInfo result = worksInfoService.viewWork(workId);
        jv.addData("data", result);
        return jv;
    }

    @RequestMapping(value = "selectByWork", method = RequestMethod.POST)
    public JsonAndView selectByWork(Integer workId) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (workId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        List<WorkDetail> result = worksInfoService.selectByWork(workId);
        Map<String, Object> data = CollectionInfo.totalAttr(result);
        jv.addData("data", data);
        return jv;
    }
    
    /*//*****************************************************
    @RequestMapping(value = "selectWorksUpdateById", method = RequestMethod.POST)
    public JsonAndView selectWorksUpdateById(Integer workId) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (workId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        List<WorksUpdate> result = worksInfoService.selectWorksUpdateById(workId);
		String[] paraName = worksInfoService.selectWorkUpdateByWebsiteId(result.get(0).websiteId).split(" ");
		Map<String, Object> data = CollectionInfo.collectionInfo(result, paraName);


        jv.addData("data", data);
        return jv;
    }
    /*//******************************************************

    @SuppressWarnings("null")
	@RequestMapping(value = "commentsByWork", method = RequestMethod.POST)
    public JsonAndView commentsByWork(Integer workId) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (workId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        List<Comment> result = worksInfoService.commentsByWork(workId);
        jv.addData("data", result);
        return jv;
    }*/
}
