package com.swust.kelab.mongo.dao;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.swust.kelab.domain.WorkDetail;
import com.swust.kelab.mongo.dao.base.PageResult;
import com.swust.kelab.mongo.dao.query.BaseDao;
import com.swust.kelab.mongo.dao.query.BaseQuery;
import com.swust.kelab.mongo.domain.TempAuthor;
import com.swust.kelab.mongo.domain.TempWorks;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.PageData;
import com.swust.kelab.web.model.QueryData;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Repository(value = "worksInfoDao")
public class WorksInfoDaoTemp extends BaseDao<TempWorks> {

    @Override
    public void init() {
        super.collection = "works";
    }

    /**
     * 查询所有有效书籍数量
     */
    public Integer selectValidWorkCount(Integer siteId){
        DBObject filterQuery = new BasicDBObject("workType", new BasicDBObject("$ne", ""));
        if (siteId != 0) {
            filterQuery.put("workWebsiteId", siteId);
        }
        int validWorkCount = super.getDBCollection().find(filterQuery).count();
        return validWorkCount;
    }

    /**
     * 查询排名前九的类型即对应书籍数量
     */
    public List<Area> selectWorkType(Integer siteId, Integer topNum) {
        /*db.works.aggregate([
        {$match:{$and: [{"workType":{$ne:""}}, {"workWebsiteId":1}]}},
        // {$match:{"workWebsiteId":1}},
        {$group:{"_id":"$workType", "value":{$sum:1}}},
        {$sort:{value:-1}},
        {$limit:9}
        ])*/
        List<DBObject> queryList = new ArrayList<DBObject>();
        DBObject filterType = new BasicDBObject("$match", new BasicDBObject("workType", new BasicDBObject("$ne", "")));
        queryList.add(filterType);
        if (siteId != 0) {
            DBObject site = new BasicDBObject("$match", new BasicDBObject("workWebsiteId", siteId));
            queryList.add(site);
        }
        DBObject groupFields = new BasicDBObject();
        groupFields.put("_id", "$workType");
        groupFields.put("value", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("value", -1));
        DBObject limit = new BasicDBObject("$limit", topNum);
        queryList.add(group);
        queryList.add(sort);
        queryList.add(limit);
        Iterable<DBObject> results = super.getDBCollection().aggregate(queryList).results();
        Iterator<DBObject> iterator = results.iterator();
        List<Area> list = Lists.newArrayList();
        while (iterator.hasNext()) {
            String json = JSON.toJSONString(iterator.next());
            Area typeWithCount = JSON.parseObject(json, Area.class);
            typeWithCount.setName(typeWithCount.get_id());
            list.add(typeWithCount);
        }
        return list;
    }

    /**
     * 查询前topNum书籍
     */
    public List<TempWorks> selectHotTopWork(Integer siteId, Integer field, Integer descOrAsc, Integer topNum){
        DBObject sort = new BasicDBObject();
        this.sortFields(field, descOrAsc, sort);
        DBObject site = new BasicDBObject();
        if (siteId != 0) {
            site.put("authWebsiteId", siteId);
        }
        DBCursor cursor = super.getDBCollection().find(site).sort(sort).limit(topNum);
        List<TempWorks> workList = Lists.newArrayList();
        while(cursor.hasNext()){
            TempWorks work = decode(cursor.next(), TempWorks.class);
            workList.add(work);
        }
        return workList;
    }

    public QueryData viewWorkByPage(EPOQuery iQuery, Integer siteId, Integer sortField, Integer descOrAsc) {
        QueryData queryData = new QueryData();
        int[] pageArray = iQuery.getPageArray();
        String searchWord = iQuery.getSearchWord();
        Integer perPageCount = iQuery.getRecordPerPage();
        if (perPageCount <= 0) {
            perPageCount = 10;
        }
        int workCount = super.getDBCollection().find().count();
        if (workCount == 0) {
            return queryData;
        }
        queryData.setTotalCount(workCount);
        int totalPage = QueryData.computeTotalPage(workCount, perPageCount);
        queryData.setTotalPage(totalPage);

        DBObject queryFields = new BasicDBObject();
        if (siteId != 0) {
            queryFields.put("workWebsiteId", siteId);
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
                queryFields.put("workTitle", Pattern.compile("^.*"+searchWord+".*$"));
            }
            DBCursor cursor = super.getDBCollection().find(queryFields).sort(sort).skip(i * perPageCount).limit(perPageCount);
            List<TempWorks> workList = Lists.newArrayList();
            while (cursor.hasNext()) {
                workList.add(decode(cursor.next(), TempWorks.class));
            }
            pageDataList.add(new PageData(page, workList));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;
    }

    private void sortFields(Integer sortField, Integer descOrAsc, DBObject sort) {
        if(sortField == 1){ //hits
            sort.put("workTotalHits", descOrAsc);
        }else if(sortField == 2){
            sort.put("workCommentsNum", descOrAsc);
        }else if(sortField == 3){
            sort.put("workTotalRecoms", descOrAsc);
        }
    }

    @Override
    public boolean updateOrSave(TempWorks entity) {
        return false;
    }

    @Override
    public PageResult<TempWorks> query(BaseQuery query) {
        return super.query(query);
    }

    @Override
    public TempWorks findById(Integer id) {
        return null;
    }

    @Override
    public Long getCount(BaseQuery query) {
        return super.getCount(query);
    }
}
