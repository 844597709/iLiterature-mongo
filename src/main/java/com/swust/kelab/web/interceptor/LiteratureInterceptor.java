package com.swust.kelab.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LiteratureInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getHeader("referer");
        if (url.contains("login.html"))
            return true;
        HttpSession session = request.getSession();
        if (session.getAttribute("userId") != null) {
            int userId = 0;
            userId = (Integer) session.getAttribute("userId");
            if (userId > 0)
                return true;
            else {
                response.setStatus(602);// 未登陆状态
                return false;
            }
        } else {
            response.setStatus(602);// 未登陆状态
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    /**
     * 判断请求是否来自ajax
     * 
     * @author
     * @param request
     * @return
     */
    @SuppressWarnings("unused")
    private boolean isAjaxRequest(HttpServletRequest request) {
        boolean flag = false;
        if (request.getHeader("X-Requested-With") != null
                && request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
            flag = true;
        }
        return flag;
    }
}
