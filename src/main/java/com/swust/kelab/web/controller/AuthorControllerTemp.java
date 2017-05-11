package com.swust.kelab.web.controller;

import com.google.common.collect.Lists;
import com.swust.kelab.mongo.domain.TempAuthor;
import com.swust.kelab.mongo.domain.TempAuthorUpdate;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.domain.vo.TempAuthorVo;
import com.swust.kelab.mongo.service.AuthorServiceTemp;
import com.swust.kelab.mongo.utils.CollectionUtil;
import com.swust.kelab.web.json.JsonAndView;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.QueryData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("author")
public class AuthorControllerTemp {
    private static final Integer DESCORASC = -1; // 默认降序
    private static final Integer TOPNUM = 10; // top值

    @Resource
    private AuthorServiceTemp authorService;

    @RequestMapping(value = "/selectHotTopAuthor", method = RequestMethod.POST)
    public JsonAndView selectHotTopAuthor(Integer siteId, Integer field, Integer desc) {
        JsonAndView jv = new JsonAndView();
        List<TempAuthor> result = authorService.selectHotTopAuthor(siteId, field, desc, TOPNUM);
        jv.addData("result", result);
        return jv;
    }

    /*@RequestMapping(value = "/viewAllAuthor", method = RequestMethod.POST)
    public JsonAndView viewAllAuthor(AuthorQuery query) {
        JsonAndView jv = new JsonAndView();
        PageResult<TempAuthor> result = authorService.viewAllAuthor(query);
        jv.addData("result", result);
        return jv;
    }*/

    @RequestMapping(value = "/viewAllAuthor", method = RequestMethod.POST)
    public JsonAndView viewAllAuthor(EPOQuery query, Integer siteId, Integer field, Integer desc, String Autharea) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (query == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        if (Autharea != null && Autharea.length() == 0) {
            Autharea = null;
        }
        QueryData queryData = authorService.viewAuthorByPage(query, siteId, field, desc, Autharea);
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

    @RequestMapping(value = "/viewAuthorById", method = RequestMethod.POST)
    public JsonAndView viewAuthorById(Integer authorId) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (authorId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        TempAuthorVo author = authorService.selectAuthorById(authorId);
        jv.addData("result", author);
        return jv;
    }

    // 一次全部查出作者统计：range1~4：1点击量、2评论数、3推荐数、4作品数
    @RequestMapping(value = "/countAuthorInfoNumAll", method = RequestMethod.POST)
    public JsonAndView countAuthorNumAll(String hitsRange, String commentsRange, String recomsRange, String worksRange, Integer siteId) {
        System.out.println(hitsRange+" "+commentsRange+" "+recomsRange+" "+worksRange+" "+siteId);
        JsonAndView jv = new JsonAndView();
//        Map<String, Object> map = authorService.countInfoNumAll(hitsRange, commentsRange, recomsRange, worksRange, siteId, DESCORASC);
        Map<String, List<Area>> map = authorService.countAuthorInfoNum(hitsRange, commentsRange, recomsRange, worksRange, siteId);
        if (map == null || map.size() == 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("没有符合条件的作者");
            return jv;
        }
        jv.addData("result", map);
        return jv;
    }

    @RequestMapping(value = "/viewAuthorUpdate", method = RequestMethod.POST)
    public JsonAndView viewAuthorUpdate(Integer auupAuthId) throws Exception {
		JsonAndView jv = new JsonAndView();
		// 查询条件格式验证
		if (auupAuthId <= 0) {
			jv.setRet(false);
			jv.setErrcode(601);
			jv.setErrmsg("数据格式错误");
			return jv;
		}
		List<TempAuthorUpdate> result = authorService.selectAuthorUpdateByAuthId(auupAuthId);
		jv.addData("result", result);
		return jv;
	}

	/*@RequestMapping(value = "/viewAuthor", method = RequestMethod.POST)
	public JsonAndView viewAuthor(Integer authorId) throws Exception {
		JsonAndView jv = new JsonAndView();
		// 查询条件格式验证
		if (authorId <= 0) {
			jv.setRet(false);
			jv.setErrcode(601);
			jv.setErrmsg("数据格式错误");
			return jv;
		}
		Author result = authorService.viewAuthor(authorId);
		jv.addData("result", result);
		return jv;
	}

	@RequestMapping(value = "/countInfo", method = RequestMethod.POST)
	public JsonAndView countAuthor() throws Exception {
		JsonAndView jv = new JsonAndView();
		Map<String, Long> result = authorService.countInfo();
		jv.addData("result", result);
		return jv;
	}*/

    // liujie siteId=0 查找全部
    @RequestMapping(value = "/countInfoGender", method = RequestMethod.POST)
    public JsonAndView countAuthorGender(Integer siteId) throws Exception {
        JsonAndView jv = new JsonAndView();
        Map<String, Integer> result = authorService.countInfoGender(siteId);
        jv.addData("result", result);
        return jv;
    }

    @RequestMapping(value = "/countInfoArea", method = RequestMethod.POST)
    public JsonAndView countAuthorArea(Integer siteId) {
        JsonAndView jv = new JsonAndView();
        Map<String, Integer> areaMap = authorService.countInfoArea(siteId);
        List<Area> list = Lists.newArrayList();
        areaMap.forEach((areaName, count) -> {
            if (!("").equals(areaName.trim())) {
                Area area = new Area(areaName, count);
                list.add(area);
            }
        });
        List<Area> sortedByAreaCountList = list.stream().sorted((o1, o2) -> o1.compareTo(o2)).collect(Collectors.toList());
        if (!CollectionUtil.isEmpty(sortedByAreaCountList)) {
            jv.addData("allAuthorNum", areaMap.values().stream().reduce(0, Integer::sum));
            jv.addData("maxProvinceName", sortedByAreaCountList.get(0).getName());
            jv.addData("maxProvinceNum", sortedByAreaCountList.get(0).getValue());
        }
        jv.addData("result", sortedByAreaCountList);
        return jv;
    }
    // liujie siteId=0 查找全部
	/*@RequestMapping(value = "/countInfoArea", method = RequestMethod.POST)
	public JsonAndView countAuthorArea(Integer siteId) throws Exception {
		JsonAndView jv = new JsonAndView();
		List<Area> list=	authorService.countAreaInfo( siteId);
		Area area=authorService.countAreaMaxPeople(siteId);
		int allAuthorNum=authorService.countAreaSumPeople( siteId);
		jv.addData("allAuthorNum",allAuthorNum);
		jv.addData("maxProvinceName", area.getName());
		jv.addData("maxProvinceNum", area.getValue());
		jv.addData("result", list);
		return jv;
	}*/

    //一次全部查出作者统计：range1~4：1作品数、2点击量、3评论数、4推荐数
    @RequestMapping(value = "/countInfoNumAll", method = RequestMethod.POST)
    public JsonAndView countAuthorNumAll(Integer websiteId, String worksR, String hitsR, String commentsR, String recomsR)
            throws Exception {
        JsonAndView jv = new JsonAndView();
        Map<String, Object> map = authorService.countAuthorInfo(websiteId, worksR, hitsR, commentsR, recomsR);
        if (map == null || map.size() == 0) {
            jv.setRet(false);
            jv.setErrcode(601);// 随便写的哈
            jv.setErrmsg("没有符合条件的作者");
            return jv;
        }
        jv.addData("result", map);
        return jv;
    }

}
