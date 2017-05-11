package com.swust.kelab.mongo.dao;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mongodb.*;
import com.swust.kelab.mongo.dao.base.CommonDao;
import com.swust.kelab.mongo.dao.query.BaseDao;
import com.swust.kelab.mongo.domain.TempAuthor;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.domain.vo.TempAuthorVo;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.PageData;
import com.swust.kelab.web.model.QueryData;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zengdan on 2017/2/9.
 */
@Repository(value = "authorDao")
public class AuthorDaoTemp extends BaseDao<TempAuthor> {
    @Resource
    private CommonDao commonDao;

    @Override
    public void init() {
        super.collection = "author";
    }

    public List<TempAuthor> selectHotTopAuthor(Integer siteId, Integer sortField, Integer descOrAsc, Integer topNum) {
        /*
        db.author.find().sorted({"authWorksHitsNum":-1}).explain();
        */
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
        int allAuthorCount = Long.valueOf(super.getCount()).intValue();
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
            if (searchWord != null) {
//                queryFields.put("authName", Pattern.compile("^.*"+searchWord+".*$"));
                queryFields.put("authName", new BasicDBObject("$regex", searchWord));
            }
            if (searchArea != null) {
                queryFields.put("authArea", new BasicDBObject("$regex", searchArea));
            }
            /*db.works.aggregate([
            {"$match":{"workWebsiteId": 1}},
            {"$sort":{"workTotalHits":-1}},
            {"$skip":0},
            {"$limit":20},
            {"$lookup":{
                from: "crawlwebsite",
                localField: "workWebsiteId",
                foreignField: "crwsId",
                as: "crawlWebsiteList"
            }}]);*/
            DBObject match = new BasicDBObject("$match", queryFields);
            DBObject sortFields = new BasicDBObject("$sort", sort);
            DBObject skip = new BasicDBObject("$skip", i*perPageCount);
            DBObject limit = new BasicDBObject("$limit", perPageCount);
            DBObject lookupFields = new BasicDBObject("from", "crawlwebsite").append("localField", "authWebsiteId")
                    .append("foreignField", "crwsId").append("as", "crawlWebsiteList");
            DBObject lookup = new BasicDBObject("$lookup", lookupFields);
            List<DBObject> queryList = Lists.newArrayList();
            queryList.add(match);
            queryList.add(sortFields);
            queryList.add(skip);
            queryList.add(limit);
            queryList.add(lookup);
            Iterator<DBObject> iterator = super.getDBCollection().aggregate(queryList).results().iterator();
            List<TempAuthorVo> authorList = Lists.newArrayList();
            while(iterator.hasNext()){
                String json = JSON.toJSONString(iterator.next());
                TempAuthorVo authorVo = JSON.parseObject(json, TempAuthorVo.class);
                authorList.add(authorVo);
            }
            pageDataList.add(new PageData(page, authorList));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;
    }

    public TempAuthorVo selectAuthorById(Integer authId){
        DBObject query = new BasicDBObject("authId", authId);
        DBObject match = new BasicDBObject("$match", query);
        DBObject lookupFields = new BasicDBObject("from", "crawlwebsite").append("localField", "authWebsiteId")
                .append("foreignField", "crwsId").append("as", "crawlWebsiteList");
        DBObject lookup = new BasicDBObject("$lookup", lookupFields);
        List<DBObject> queryList = Lists.newArrayList();
        queryList.add(match);
        queryList.add(lookup);
        Iterator<DBObject> iterator = super.getDBCollection().aggregate(queryList).results().iterator();
        TempAuthorVo author = JSON.parseObject(JSON.toJSONString(iterator.next()), TempAuthorVo.class);
        return author;
    }

    /**
     * 所有作者统计信息（点击量，评论量，推荐量）
     */
    @Deprecated
    public List<TempAuthor> selectSortedAuthors(Integer siteId, Integer sortField, Integer descOrAsc) {
        /*db.author.aggregate([{"$match":{"authWebsiteId":siteId},{"$sort":{"authWorksHitsNum":-1}}}])*/
        DBObject site = new BasicDBObject();
        if (siteId != 0) {
            site.put("authWebsiteId", siteId);
        }
        DBObject sort = new BasicDBObject();
        this.sortFields(sortField, descOrAsc, sort);
        List<DBObject> queryList = Lists.newArrayList();
        queryList.add(new BasicDBObject("$match", site));
        queryList.add(new BasicDBObject("$sort", sort));
        Cursor cursor = super.getDBCollection().aggregate(queryList, AggregationOptions.builder().allowDiskUse(true).build());
        List<TempAuthor> authorList = Lists.newArrayList();
        while (cursor.hasNext()) {
            TempAuthor author = decode(cursor.next(), TempAuthor.class);
            authorList.add(author);
        }
        return authorList;
    }

    public Area selectRangeWithAuthorCount(Integer siteId, Integer sortField, Integer min, Integer max) {
        /*db.author.find({{"authWebsiteId":siteId},{"authWorksHitsNum":{"$gte":5000,"$lte":10000}}});*/
        String fieldStr = "";
        if (sortField == 1) {
            fieldStr = "authWorksHitsNum";
        } else if (sortField == 2) {
            fieldStr = "authWorksCommentsNum";
        } else if (sortField == 3) {
            fieldStr = "authWorksRecomsNum";
        } else if (sortField == 4) {
            fieldStr = "authWorksNum";
        }
        DBObject query = new BasicDBObject();
        if (siteId != 0) {
            query.put("authWebsiteId", siteId);
        }
        query.put(fieldStr, new BasicDBObject().append("$gte", min).append("$lt", max));
        int authorCount = super.getDBCollection().find(query).count();
        Area area = new Area(min.toString(), authorCount);
        return area;
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
        List<Area> genderList = commonDao.queryByCondition(super.getDBCollection(), list);
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
        List<Area> areaList = commonDao.queryByCondition(super.getDBCollection(), list);
        return areaList;
    }

    @Deprecated
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
