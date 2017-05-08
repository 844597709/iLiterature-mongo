package com.swust.kelab.mongo.service;

import com.google.common.collect.Maps;
import com.swust.kelab.mongo.dao.AuthorDaoTemp;
import com.swust.kelab.mongo.dao.WorksCommentDao;
import com.swust.kelab.mongo.dao.WorksInfoDaoTemp;
import com.swust.kelab.repos.SiteDao;
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
    private WorksCommentDao worksCommentDao;
    @Resource
    private SiteDao siteDao;

    public Map<String, Long> countInfo() {
        Map<String, Long> map = Maps.newHashMap();
       /* long author = authorDao.getCount(null);
        long site = siteDao.countSite();
        long works = worksInfoDao.getCount(null);
        // TODO comments表还未导数据
        long worksComments = worksCommentDao.getCount(null);*/
        long author = authorDao.getDBCollection().find().count();
        long site = siteDao.countSite();
        long works = worksInfoDao.getDBCollection().find().count();
//        long worksComments = worksCommentDao.getDBCollection().find().count();
        map.put("author", author);
        map.put("site", site);
        map.put("works", works);
//        map.put("worksComments", worksComments);
        map.put("worksComments", 0L);
        return map;
    }
}
