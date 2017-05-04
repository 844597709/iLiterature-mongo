package com.swust.kelab.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.swust.kelab.service.web.SystemParamService;
import com.swust.kelab.web.json.JsonAndView;

@Controller
@RequestMapping("/systemparameter")
public class SystemParameterController {
	@Resource
	private SystemParamService systemParamService;
	@RequestMapping(value = "/querySysParam", method = RequestMethod.POST)
	public JsonAndView querySysParam(
			@RequestParam(value = "topNum", required = false, defaultValue = "10") Integer topNum,
			HttpServletRequest request) {
		JsonAndView jv = new JsonAndView();
		jv.addData("data", systemParamService.querySystemParam());
		return jv;
	}
	@RequestMapping(value = "/modSysParam", method = RequestMethod.POST)
	public JsonAndView modSysParam(Integer sypaId1, Integer sypaId2,String sypaValue1,String sypaValue2) {
		JsonAndView jv = new JsonAndView();
		jv.addData("data", systemParamService.modSystemParam(sypaId1,sypaId2, sypaValue1,sypaValue2));
		return jv;
	}
}
