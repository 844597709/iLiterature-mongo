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

import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.OrderFieldType;

public class EPOQueryMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (EPOQuery.class.isAssignableFrom(parameter.getParameterType())) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 从request中解析参数放入EPOQuery中
        EPOQuery query = new EPOQuery();
        // 情感类别
        String sentiment = webRequest.getParameter("sentiment");
        if (StringUtils.isNotBlank(sentiment)) {
            query.setSentiment(NumberUtils.toInt(sentiment, -2));
        }
        // 主题
        String topic = webRequest.getParameter("topic");
        if (StringUtils.isNotBlank(topic)) {
            query.setTopic(NumberUtils.toInt(topic));
        }
        // 搜索引擎
        String metaSearch = webRequest.getParameter("metaSearch");
        if (StringUtils.isNotBlank(metaSearch)) {
            query.setMetaSearch(NumberUtils.toInt(metaSearch));
        }
        // 关注级别
        String attentionLevel = webRequest.getParameter("attentionLevel");
        if (StringUtils.isNotBlank(attentionLevel)) {
            query.setAttentionLevel(NumberUtils.toInt(attentionLevel));
        }
        // 预警
        String warn = webRequest.getParameter("warn");
        if (StringUtils.isNotBlank(warn)) {
            query.setWarn(NumberUtils.toInt(warn));
        }
        // 查询字段
        String searchType = webRequest.getParameter("searchType");
        if (StringUtils.isNotBlank(searchType)) {
            query.setSearchType(NumberUtils.toInt(searchType));
        }
        // 查询内容
        String searchWord = webRequest.getParameter("searchWord");
        if (StringUtils.isNotBlank(searchWord)) {
            query.setSearchWord(searchWord);
        }
        // 查询收藏时间
        String collectDate = webRequest.getParameter("collectDate");
        if (StringUtils.isNotBlank(collectDate)) {
            query.setCollectDate(DateUtils.parseDate(collectDate, "yyyy-MM-dd hh:mm:ss"));
        }
        // 查询编辑与否（收藏模块）
        String chooseEdit = webRequest.getParameter("chooseEdit");
        if (StringUtils.isNotBlank(chooseEdit)) {
            query.setChooseEdit(NumberUtils.toInt(chooseEdit));
        }
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
            query.setStartTime(startTime);
            query.setEndTime(endTime);
        } else {
            // 开始时间
            String startTime = webRequest.getParameter("startTime");
            try {
                if (StringUtils.isNotBlank(startTime)) {
                    query.setStartTime(DateUtils.parseDate(startTime, "yyyy-MM-dd hh:mm:ss"));
                }
                // 结束时间
                String endTime = webRequest.getParameter("endTime");
                if (StringUtils.isNotBlank(endTime)) {
                    query.setEndTime(DateUtils.parseDate(endTime, "yyyy-MM-dd hh:mm:ss"));
                }
            } catch (ParseException e) {
                return null;
            }
        }
        // 排序字段
        String orderFieldStr = webRequest.getParameter("orderField");
        int orderFieldNo;
        OrderFieldType orderField = OrderFieldType.RELATION;
        if (StringUtils.isNotBlank(orderFieldStr) && ((orderFieldNo = NumberUtils.toInt(orderFieldStr)) > 0)) {
            orderField = OrderFieldType.codeOf(orderFieldNo);
        }
        query.setOrderField(orderField);
        // 查询页码
        String pageArrays = webRequest.getParameter("pageArray");
        if (pageArrays != null) {
            String[] pages = StringUtils.split(pageArrays, ",");
            int[] pageArray = new int[pages.length];
            if (pages.length == 0) {
                pageArray[0] = 1;
            } else {
                for (int i = 0; i < pages.length; i++) {
                    pageArray[i] = NumberUtils.toInt(pages[i]);
                }
            }
            query.setPageArray(pageArray);
        }
        // 每页记录条数
        String strRecordPerPage = webRequest.getParameter("recordPerPage");
        if (strRecordPerPage != null) {
            query.setRecordPerPage(NumberUtils.toInt(strRecordPerPage, 10));
        }
        return query;
    }
}
