package com.swust.kelab.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.swust.kelab.service.web.SystemService;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 
 * @author
 * 
 */
@Controller
@RequestMapping("/system")
public class SystemController {
    @Resource
    private SystemService systemService;
    @Resource
    HttpServletRequest request;

    //
    @RequestMapping(value = "/viewServerInfo", method = RequestMethod.POST)
    public JsonAndView viewServerInfo() throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        Map<String, Object> map = new HashMap<String, Object>();
        map = systemService.viewServerInfo();
        jv.addData("data", map);
        return jv;
    }

    //
    @RequestMapping(value = "/viewServerStatus", method = RequestMethod.POST)
    public JsonAndView viewServerStatus() throws Exception {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        Map<String, String> map = new HashMap<String, String>();
        map = systemService.viewServerStatus();
        jv.addData("data", map);
        return jv;
    }
}
