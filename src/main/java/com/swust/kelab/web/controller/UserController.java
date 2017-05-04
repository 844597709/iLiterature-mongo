package com.swust.kelab.web.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Maps;
import com.swust.kelab.domain.User;
import com.swust.kelab.service.web.UserService;
import com.swust.kelab.web.json.JsonAndView;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.QueryData;

/**
 * 同义词
 * 
 * @author
 * 
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    HttpServletRequest request;

    //--zd--
    @RequestMapping(value = "/selectVerifyDateTimeUser", method = RequestMethod.POST)
    public JsonAndView selectVerifyDateTimeUser(User user, EPOQuery query) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (query == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        QueryData queryData = userService.selectVerifyDateTimeUser(user, query.getPageArray(), query.getRecordPerPage());
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }
    //--至此--
    
    // 根据查询条件检索同义词
    @RequestMapping(value = "/viewAllUser", method = RequestMethod.POST)
    public JsonAndView viewAllUser(String userName, EPOQuery query) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (query == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        QueryData queryData = userService.viewAllUser(userName, query.getPageArray(), query.getRecordPerPage());
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

    // 根据查询条件检索单个同义词信息
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonAndView login(User user, String authcode, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String checkCode = (String) session.getAttribute("checkCode");
        if (!checkCode.equals(authcode)) {
            JsonAndView jv = new JsonAndView();
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("验证码有误,请检查");
            return jv;
        }
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (user == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        Map<String, Object> map = Maps.newHashMap();
        map = userService.login(user);
        jv.addData("result", map);
        return jv;
    }

    // 退出
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public JsonAndView logout() {
        JsonAndView jv = new JsonAndView();
        boolean result = userService.logout();
        jv.addData("result", result);
        return jv;
    }

    @SuppressWarnings("unused")
    private void errorData(JsonAndView jv) {
        jv.setRet(false);
        jv.setErrcode(601);
        jv.setErrmsg("数据格式错误");
    }

    // 删除用户
    @RequestMapping(value = "/delUser", method = RequestMethod.POST)
    public JsonAndView delUser(Integer userId) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (userId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        String result = userService.delUser(userId);
        jv.addData("result", result);
        return jv;
    }

    // 添加用户
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public JsonAndView addUser(User user) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (user == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        String result = userService.addUser(user);
        jv.addData("result", result);
        return jv;
    }

    // 显示用户信息
    @RequestMapping(value = "/viewUserInfo", method = RequestMethod.POST)
    public JsonAndView viewUserInfo() {
        JsonAndView jv = new JsonAndView();
        User user = userService.viewUserInfo();
        jv.addData("user", user);
        return jv;
    }

    @RequestMapping(value = "/editUser", method = RequestMethod.POST)
    public JsonAndView editUser(User user) {
        JsonAndView jv = new JsonAndView();
        if (user == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        //System.out.println("editUser user.getTime()--"+user.getTime());
        user.setDeadTime(user.getTime());
        //System.out.println("editUser user.getDeadTime()--"+user.getDeadTime());
        Map<String, Object> map = Maps.newHashMap();
        map = userService.editUser(user);
        jv.addData("data", map);
        return jv;
    }
}
