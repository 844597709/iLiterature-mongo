package com.swust.kelab.mongo.dao;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mongodb.*;
import com.swust.kelab.mongo.domain.TempAuthor;
import com.swust.kelab.mongo.dao.query.BaseDao;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.PageData;
import com.swust.kelab.web.model.QueryData;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by zengdan on 2017/2/9.
 */
@Repository(value = "authorDao")
public class AuthorDaoTemp extends BaseDao<TempAuthor> {
    @Override
    public void init() {
        super.collection = "author";
    }

    public List<TempAuthor> selectHotTopAuthor(Integer siteId, Integer sortField, Integer descOrAsc, Integer topNum) {
        DBObject sort = new BasicDBObject();
        this.sortFields(sortField, descOrAsc, sort);
        DBObject site = new BasicDBObject();
        if (siteId != 0) {
            site.put("authWebsiteId", siteId);
        }
        DBCursor cursor = super.getDBCollection().find(site).sort(sort).limit(topNum);
        List<TempAuthor> authorList = Lists.newArrayList();
        while (cursor.hasNext()) {
            TempAuthor author = decode(cursor.next(), TempAuthor.class);
            authorList.add(author);
        }
        return authorList;
    }

    public QueryData viewAuthorByPage(EPOQuery iQuery, Integer siteId, Integer sortField, Integer descOrAsc, String searchArea) {
        QueryData queryData = new QueryData();
        int[] pageArray = iQuery.getPageArray();
        String searchWord = iQuery.getSearchWord();
        Integer perPageCount = iQuery.getRecordPerPage();
        if (perPageCount <= 0) {
            perPageCount = 10;
        }
        int allAuthorCount = super.getDBCollection().find().count();
        if (allAuthorCount == 0) {
            return queryData;
        }
        queryData.setTotalCount(allAuthorCount);
        int totalPage = QueryData.computeTotalPage(allAuthorCount, perPageCount);
        queryData.setTotalPage(totalPage);

        DBObject queryFields = new BasicDBObject();
        if (siteId != 0) {
            queryFields.put("authWebsiteId", siteId);
        }
        DBObject sort = new BasicDBObject();
        this.sortFields(sortField, descOrAsc, sort);

        List<PageData> pageDataList = Lists.newArrayList();
        // 未指定页数，则只读取前三页数据
        if (pageArray == null) {
            pageArray = new int[]{1, 2, 3};
        }
        // 分别获取每页的数据
        for (int i = 0; i < pageArray.length; i++) {
            int page = pageArray[i];
            if (page <= 0 || page > totalPage) {
                continue;
            }
            if(searchWord!=null){
                queryFields.put("authName", Pattern.compile("^.*"+searchWord+".*$"));
            }
            if(searchArea!=null){
                queryFields.put("authArea", Pattern.compile("^.*"+searchArea+".*$"));
            }
            DBCursor cursor = super.getDBCollection().find(queryFields).sort(sort).skip(i * perPageCount).limit(perPageCount);
            List<TempAuthor> authorList = Lists.newArrayList();
            while (cursor.hasNext()) {
                authorList.add(decode(cursor.next(), TempAuthor.class));
            }
            pageDataList.add(new PageData(page, authorList));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;
    }

    private void sortFields(Integer sortField, Integer descOrAsc, DBObject sort) {
        if (sortField == 1) {
            sort.put("authWorksHitsNum", descOrAsc);
        } else if (sortField == 2) {
            sort.put("authWorksCommentsNum", descOrAsc);
        } else if (sortField == 3) {
            sort.put("authWorksRecomsNum", descOrAsc);
        } else if (sortField == 4) {
            sort.put("authWorksNum", descOrAsc);
        }
    }

    @Override
    public boolean updateOrSave(TempAuthor entity) {
        if (entity.getAuthId() != null) {//更新
            DBObject query = new BasicDBObject("authId", entity.getAuthId());
            DBObject update = new BasicDBObject("$set", entity);
            super.getDBCollection().update(query, update);
            return true;
        }
        return false;
    }

    /*@Override
    public boolean delete(Integer id) {
        BasicDBObject query = new BasicDBObject("authId", id);
        super.getDBCollection().findAndRemove(query);
        return true;
    }*/

    @Override
    public TempAuthor findById(Integer id) {
        DBObject query = new BasicDBObject("authId", id);
        TempAuthor author = decode(super.getDBCollection().findOne(query), TempAuthor.class);
        return author;
    }

    /*@Override
    public List<Author> getBusinessesByIds(List<Integer> ids) {
        BasicDBList values = new BasicDBList();
        values.addAll(ids);
        BasicDBObject query = new BasicDBObject("$in", values);
        List<Author> list = decode(super.getDBCollection().find(query), Author.class);
        return list;
    }*/

    /**
     * 统计性别
     */
    public List<Area> countInfoGender(Integer wesiId) {
        DBObject queryFields = new BasicDBObject();
        queryFields.put("_id", "$authGender");
        queryFields.put("value", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", queryFields);
        List<DBObject> list = Lists.newArrayList();
        if (wesiId != 0) {
            DBObject match = new BasicDBObject("$match", new BasicDBObject("authWebsiteId", wesiId));
            list.add(match);
        }
        list.add(group);
        AggregationOutput output = super.getDBCollection().aggregate(list);
        Iterator<DBObject> iter = output.results().iterator();
        List<Area> genderList = Lists.newArrayList();
        while (iter.hasNext()) {
            DBObject obj = iter.next();
            String json = JSON.toJSONString(obj);
            Area area = JSON.parseObject(json, Area.class);
            area.setName(area.get_id()); // 获取性别
            genderList.add(area);
        }
        return genderList;
    }

    /**
     * 统计省份作者数
     */
    public List<Area> countInfoArea(Integer wesiId) {
        DBObject queryFields = new BasicDBObject();
        queryFields.put("_id", "$authArea");
        queryFields.put("value", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", queryFields);
        List<DBObject> list = Lists.newArrayList();
        if (wesiId != 0) {
            DBObject match = new BasicDBObject("$match", new BasicDBObject("authWebsiteId", wesiId));
            list.add(match);
        }
        list.add(group);
        AggregationOutput output = super.getDBCollection().aggregate(list);
        Iterator<DBObject> iter = output.results().iterator();
        List<Area> areaList = Lists.newArrayList();
        while (iter.hasNext()) {
            DBObject obj = iter.next();
            String json = JSON.toJSONString(obj);
            Area area = JSON.parseObject(json, Area.class);
            area.setName(area.get_id());
            areaList.add(area);
        }
        return areaList;
    }

    public Integer getNum(Integer websiteId, String field, Integer start, Integer end) {
        DBObject obj = new BasicDBObject();
        obj.put("$gt", start);
        obj.put("lte", end);
        DBObject query = new BasicDBObject(field, obj);
        if (websiteId != null) {
            DBObject siteId = new BasicDBObject("authWebsiteId", websiteId);
            BasicDBList and = new BasicDBList();
            and.add(siteId);
            and.add(query);
            query = new BasicDBObject("$and", and);
        }
        return super.getDBCollection().find(query).count();
    }
}
