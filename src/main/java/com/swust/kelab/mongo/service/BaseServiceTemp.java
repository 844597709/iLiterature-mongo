package com.swust.kelab.mongo.service;

import com.google.common.collect.Maps;
import com.swust.kelab.mongo.dao.AuthorDaoTemp;
import com.swust.kelab.mongo.dao.SiteDaoTemp;
import com.swust.kelab.mongo.dao.WorksCommentDaoTemp;
import com.swust.kelab.mongo.dao.WorksInfoDaoTemp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zengdan on 2017/5/7.
 */
@Service(value = "baseService")
public class BaseServiceTemp {
    @Resource
    private AuthorDaoTemp authorDao;
    @Resource
    private WorksInfoDaoTemp worksInfoDao;
    @Resource
    private WorksCommentDaoTemp worksCommentDaoTemp;
    @Resource
    private SiteDaoTemp siteDaoTemp;

    public Map<String, Long> countInfo() {
        Map<String, Long> map = Maps.newHashMap();
        long author = authorDao.getCount();
        long site = siteDaoTemp.getCount();
        long works = worksInfoDao.getCount();
        long worksComments = worksCommentDaoTemp.getCount();
        map.put("author", author);
        map.put("site", site);
        map.put("works", works);
        map.put("worksComments", worksComments);
        return map;
    }
}
