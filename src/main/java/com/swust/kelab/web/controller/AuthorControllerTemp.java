package com.swust.kelab.web.controller;

import com.google.common.collect.Lists;
import com.swust.kelab.mongo.dao.base.PageResult;
import com.swust.kelab.mongo.domain.TempAuthor;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.dao.query.AuthorQuery;
import com.swust.kelab.mongo.service.AuthorServiceTemp;
import com.swust.kelab.web.json.JsonAndView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("author")
public class AuthorControllerTemp {
	@Resource
	private AuthorServiceTemp authorService;

	@RequestMapping(value = "/viewAllAuthor", method = RequestMethod.POST)
	public JsonAndView viewAllAuthor(AuthorQuery query) {
		JsonAndView jv = new JsonAndView();
		PageResult<TempAuthor> result = authorService.viewAllAuthor(query);
		jv.addData("result", result);
		return jv;
	}

	/*@RequestMapping(value = "/viewAuthorUpdate", method = RequestMethod.POST)
	public JsonAndView viewAuthorUpdate(Integer auupAuthId) throws Exception {
		JsonAndView jv = new JsonAndView();
		// 查询条件格式验证
		if (auupAuthId <= 0) {
			jv.setRet(false);
			jv.setErrcode(601);
			jv.setErrmsg("数据格式错误");
			return jv;
		}
		List<AuthorUpdate> result = authorService.viewAuthorUpdate(auupAuthId);
		jv.addData("result", result);
		return jv;

	}

	@RequestMapping(value = "/viewAuthor", method = RequestMethod.POST)
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
		Map<String, Integer> map = authorService.countInfoArea(siteId);
		List<Area> list = Lists.newArrayList();
		for(Map.Entry<String, Integer> entry:map.entrySet()){
			Area area = new Area(entry.getKey(), entry.getValue());
			list.add(area);
		}
		jv.addData("result", list);
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
