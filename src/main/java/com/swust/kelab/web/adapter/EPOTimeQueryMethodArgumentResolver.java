package com.swust.kelab.web.adapter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.swust.kelab.web.model.EPOTimeQuery;

public class EPOTimeQueryMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (EPOTimeQuery.class.isAssignableFrom(parameter.getParameterType())) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 从request中解析时间参数放入EPOTimeRange中
        EPOTimeQuery timeQuery = new EPOTimeQuery();
        // topNum
        int topNum = 10;
        String topNumStr = webRequest.getParameter("topNum");
        if (StringUtils.isBlank(topNumStr) || (topNum = NumberUtils.toInt(topNumStr)) <= 0) {
            topNum = 10;
        }
        timeQuery.setTopNum(topNum);
        // 最近天数
        String recentDaysStr = webRequest.getParameter("recentDays");
        int recentDays = 0;
        if (StringUtils.isNotBlank(recentDaysStr) && (recentDays = NumberUtils.toInt(recentDaysStr)) > 0) {
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            Date endTime = DateUtils.addDays(today.getTime(), 1);
            Date startTime = DateUtils.addDays(endTime, -recentDays);
            timeQuery.setStartTime(startTime);
            timeQuery.setEndTime(endTime);
        } else {
            // 开始时间
            String startTime = webRequest.getParameter("startTime");
            try {
                if (StringUtils.isNotBlank(startTime)) {
                    timeQuery.setStartTime(DateUtils.parseDate(startTime, "yyyy-MM-dd hh:mm:ss"));
                }
                // 结束时间
                String endTime = webRequest.getParameter("endTime");
                if (StringUtils.isNotBlank(endTime)) {
                    timeQuery.setEndTime(DateUtils.parseDate(endTime, "yyyy-MM-dd hh:mm:ss"));
                }
            } catch (ParseException e) {
                return null;
            }
        }
        return timeQuery;
    }
}
