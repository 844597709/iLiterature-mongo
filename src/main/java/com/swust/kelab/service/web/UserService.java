package com.swust.kelab.service.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.swust.kelab.domain.User;
import com.swust.kelab.repos.ModelDao;
import com.swust.kelab.repos.bean.GenericQuery;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.web.model.PageData;
import com.swust.kelab.web.model.QueryData;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "userService")
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Resource
    private ModelDao<User> userDao;
    @Resource
    private SqlSession sqlSession;
    @Resource
    HttpServletRequest request;

    @Transactional
    public QueryData viewAllUser(String userName, int[] pageArray, int recordPerPage) {
        QueryData queryData = new QueryData();
        // 构造查询条件
        ListQuery query = new GenericQuery();
        if (userName != null && userName.length() > 0) {
            query.fill("userName", userName);
        }
        int totalCount = userDao.selectCount(query);
        queryData.setTotalCount(totalCount);
        if (totalCount == 0) {
            return queryData;
        }
        if (recordPerPage <= 0) {
            recordPerPage = 10;
        }
        query.fill("maxCount", recordPerPage);
        int totalPage = QueryData.computeTotalPage(totalCount, recordPerPage);
        queryData.setTotalPage(totalPage);
        List<PageData> pageDataList = Lists.newArrayList();
        // 未指定页数，则只读取前三页数据
        if (pageArray == null) {
            pageArray = new int[] { 1, 2, 3 };
        }
        // 分别获取每页的数据
        for (int i = 0; i < pageArray.length; i++) {
            int page = pageArray[i];
            if (page <= 0 || page > totalPage) {
                continue;
            }
            query.fill("startIndex", QueryData.computeStartIndex(page, recordPerPage));
            List<User> userLists = sqlSession.selectList("user.select", query);
            pageDataList.add(new PageData(page, userLists));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;
    }

    public User viewUser(int userId) {
        User user = new User();
        List<User> users = userDao.select(new GenericQuery().fill("userId", user.getUserId()));
        user = users.get(0);
        return user;
    }

    public Map<String, Object> login(User user) {
        Map<String, Object> map = Maps.newHashMap();
        String result = "fail";
        List<User> users = userDao.select(new GenericQuery().fill("userName", user.getUserName()));
        if (!users.isEmpty()) {
            User findUser = users.get(0);
            if (findUser.getPassword().equals(user.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("userInfo", findUser);
                session.setAttribute("userId", findUser.getUserId());
                session.setAttribute("role", findUser.getRole());
                result = "success";
                
                //--zd--
                if(findUser.getRole() != 1){
                	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
                    Date date = new Date(System.currentTimeMillis());
                    String time = sdf.format(date);
                    HashMap<String, Object> map1 = new HashMap<String, Object>();
                    map1.put("userId", findUser.getUserId());
                    map1.put("dateTime", time);
                    System.out.println("userDao.selectIsilegal(map1)--"+userDao.selectIsLegal(map1));
                    if(userDao.selectIsLegal(map1).get(0).getVerify()==0){
                    	result = "没有认证";
                    }else{
                    	if(userDao.selectIsLegal(map1).get(0).getIsLegal()==0){
                        	result = "时间过期";
                        }
                    }
                }
                //--至此--
                map.put("user", findUser);
            } else
                result = "passwordError";
        } else
            result = "null";
        map.put("result", result);
        return map;
    }
    
    //--zd--
    @Transactional
    public QueryData selectVerifyDateTimeUser(User user, int[] pageArray, int recordPerPage) {
    	HashMap<String, Object> map = new HashMap<String, Object>();
    	QueryData queryData = new QueryData();
        // 构造查询条件
        ListQuery query = new GenericQuery();
        int totalCount=-1;
        if(user.getIsLegal() == 0 && user.getVerify() == 0){
        	totalCount = userDao.selectCount(query);
        	queryData.setTotalCount(totalCount);
        }
        if (totalCount == 0) {
            return queryData;
        }
        if (recordPerPage <= 0) {
            recordPerPage = 10;
        }
        map.put("maxCount", recordPerPage);
        int totalPage = QueryData.computeTotalPage(totalCount, recordPerPage);
        queryData.setTotalPage(totalPage);
        List<PageData> pageDataList = Lists.newArrayList();
        // 未指定页数，则只读取前三页数据
        if (pageArray == null) {
            pageArray = new int[] { 1, 2, 3 };
        }
        // 分别获取每页的数据
        for (int i = 0; i < pageArray.length; i++) {
            int page = pageArray[i];
            if (page <= 0 || page > totalPage) {
                continue;
            }
            map.put("startIndex", QueryData.computeStartIndex(page, recordPerPage));
            map.put("isLegal", user.getIsLegal());
            map.put("verify", user.getVerify());
            List<User> userLists = sqlSession.selectList("user.selectVerifyDateTimeUser", map);
            if(user.getIsLegal() != 0 || user.getVerify() != 0){
            	totalCount = userLists.size();
            	queryData.setTotalCount(totalCount);
            }
            pageDataList.add(new PageData(page, userLists));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;
    }
    //--至此--

    public String delUser(int userId) {
        String result = "fail";
        userDao.delete(userId);
        result = "success";
        return result;
    }

    public String addUser(User user) {
        String result = "fail";
        List<User> users = userDao.select(new GenericQuery().fill("userName", user.getUserName()));
        if (!users.isEmpty()) {
            result = "isExist";
        } else {
        	user.setDeadTime(user.getTime());
        	System.out.println("user.getTime()--"+user.getTime());
            userDao.insert(user);
            result = "success";
        }
        return result;
    }

    public User viewUserInfo() {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userInfo");
        return user;
    }

    public Map<String, Object> editUser(User user) {
        Map<String, Object> map = Maps.newHashMap();
        String result = "fail";
        System.out.println("user.getDeadTime()--"+user.getDeadTime());
        System.out.println("user.getUserId()--"+user.getUserId());
        userDao.update(user);
        List<User> users = userDao.select(new GenericQuery().fill("userId", user.getUserId()));
        System.out.println("user.getUserId()--"+user.getUserId());
        if (!users.isEmpty()) {
            User findUser = users.get(0);
            System.out.println("findUser.getDeadTime()--"+findUser.getDeadTime());
            result = "success";
            map.put("user", findUser);
        }
        map.put("result", result);
        return map;
    }

    public boolean logout() {
        HttpSession session = request.getSession();
        session.setAttribute("userId", 0);
        session.invalidate();
        return true;
    }
}
