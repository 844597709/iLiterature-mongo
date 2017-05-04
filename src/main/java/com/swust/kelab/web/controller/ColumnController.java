package com.swust.kelab.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.swust.kelab.domain.Column;
import com.swust.kelab.service.web.ColumnService;
import com.swust.kelab.web.json.JsonAndView;

@Controller
@RequestMapping("/column")
public class ColumnController {
    @Resource
    private ColumnService columnService;
    @Resource
    HttpServletRequest request;

    @RequestMapping(value = "/viewAll", method = RequestMethod.POST)
    public JsonAndView viewAllColumn() {
        JsonAndView jsonAndView = new JsonAndView();
        List<?> list = columnService.getColumnsTree();
        jsonAndView.addData("list", list);
        return jsonAndView;
    }

    @RequestMapping(value = "/viewFirstMenu", method = RequestMethod.POST)
    public JsonAndView viewFirstMenu() {
        JsonAndView jsonAndView = new JsonAndView();
        List<Column> list = columnService.getFirstMenu();
        jsonAndView.addData("column", list);
        return jsonAndView;
    }

    @RequestMapping(value = "/viewMenuById", method = RequestMethod.POST)
    public JsonAndView viewMenuById(Integer parent) {
    	
    	// 权限判断
    	 HttpSession session = request.getSession();
    	int role = (Integer) session.getAttribute("role");
    	JsonAndView jsonAndView = new JsonAndView();
    	if(role==0 && (parent==4 || parent==6)){
    		  jsonAndView.addData("column", null);
    		  jsonAndView.setRet(false);
    		 return jsonAndView;
    	}
        List<Column> list = columnService.getMenuById(parent);
        jsonAndView.addData("column", list);
        return jsonAndView;
    }

    @RequestMapping(value = "/viewAllMetaserach", method = RequestMethod.POST)
    public JsonAndView viewAllMetasearch() {
        JsonAndView jsonAndView = new JsonAndView();
        Map<String, List<Map<String, Object>>> map = columnService.getMetasearchInformation();
        jsonAndView.addData("metasearchData", map);
        return jsonAndView;
    }
}
