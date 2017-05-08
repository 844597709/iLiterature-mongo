package com.swust.kelab.web.controller;

import com.swust.kelab.mongo.service.BaseServiceTemp;
import com.swust.kelab.web.json.JsonAndView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zengdan on 2017/5/7.
 */
@Controller
@RequestMapping("base")
public class BaseControllerTemp {
    @Resource
    private BaseServiceTemp baseService;

    @RequestMapping(value = "/countInfo", method = RequestMethod.POST)
    public JsonAndView countInfo() {
        JsonAndView jv = new JsonAndView();
        Map<String, Long> result = baseService.countInfo();
        jv.addData("result", result);
        return jv;
    }
}
