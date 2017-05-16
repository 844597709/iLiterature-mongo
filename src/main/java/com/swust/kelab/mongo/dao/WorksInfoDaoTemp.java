package com.swust.kelab.mongo.dao;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mongodb.*;
import com.swust.kelab.domain.WorkDetail;
import com.swust.kelab.mongo.dao.base.CommonDao;
import com.swust.kelab.mongo.dao.base.PageResult;
import com.swust.kelab.mongo.dao.query.BaseDao;
import com.swust.kelab.mongo.dao.query.BaseQuery;
import com.swust.kelab.mongo.domain.TempAuthor;
import com.swust.kelab.mongo.domain.TempWorks;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.domain.vo.TempWorksVo;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.PageData;
import com.swust.kelab.web.model.QueryData;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Repository(value = "worksInfoDao")
public class WorksInfoDaoTemp extends BaseDao<TempWorks> {
    @Resource
    private CommonDao commonDao;
    @Resource
    private WorksUpdateDaoTemp worksUpdateDao;

    @Override
    public void init() {
        super.collection = "works";
    }

    /**
     * 查询所有有效书籍数量
     */
    public Integer selectValidWorkCount(Integer siteId) {
        DBObject filterQuery = new BasicDBObject("workType", new BasicDBObject("$ne", ""));
        if (siteId != 0) {
            filterQuery.put("workWebsiteId", siteId);
        }
        int validWorkCount = super.getDBCollection().find(filterQuery).count();
        return validWorkCount;
    }

    /**
     * 查询排名前topNum的类型即对应书籍数量
     */
    public List<Area> selectWorkType(Integer siteId, Integer topNum) {
        /*db.works.aggregate([
        {$match:{"workType":{$ne:""}}},
        {$group:{"_id":"$workType", "value":{$sum:1}}},
        {$sort:{value:-1}},
        {$limit:9}
        ])*/
        List<DBObject> queryList = new ArrayList<DBObject>();
        DBObject queryFields = new BasicDBObject("workType", new BasicDBObject("$ne", ""));
        DBObject match = new BasicDBObject("$match", queryFields);
        if (siteId != 0) {
            queryFields.put("workWebsiteId", siteId);
        }
        DBObject groupFields = new BasicDBObject();
        groupFields.put("_id", "$workType");
        groupFields.put("value", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("value", -1));
        DBObject limit = new BasicDBObject("$limit", topNum);
        queryList.add(match);
        queryList.add(group);
        queryList.add(sort);
        queryList.add(limit);
        List<Area> list = commonDao.queryByCondition(super.getDBCollection(), queryList);
        return list;
    }

    /**
     * 查询前topNum书籍
     */
    public List<TempWorksVo> selectHotTopWork(Integer siteId, Integer field, Integer descOrAsc, Integer topNum) {
        DBObject sort = new BasicDBObject();
        this.sortFields(field, descOrAsc, sort);
        DBObject site = new BasicDBObject();
        if (siteId != 0) {
            site.put("workWebsiteId", siteId);
        }
        DBObject match = new BasicDBObject("$match", site);
        DBObject sortFields = new BasicDBObject("$sort", sort);
        DBObject limit = new BasicDBObject("$limit", topNum);
        List<DBObject> queryList = Lists.newArrayList();
        queryList.add(match);
        queryList.add(sortFields);
        queryList.add(limit);
        List<TempWorksVo> workList = this.getTempWorksVos(queryList);
        return workList;
    }

    /**
     * 分页查询作品信息，默认3页
     */
    public QueryData viewWorkByPage(EPOQuery iQuery, Integer siteId, Integer sortField, Integer descOrAsc) {
        QueryData queryData = new QueryData();
        int[] pageArray = iQuery.getPageArray();
        String searchWord = iQuery.getSearchWord();
        Integer perPageCount = iQuery.getRecordPerPage();
        if (perPageCount <= 0) {
            perPageCount = 10;
        }
        int workCount = Long.valueOf(super.getCount()).intValue();
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
            if (searchWord != null) {
//                site.put("workTitle", Pattern.compile("^.*"+searchWord+".*$"));
                queryFields.put("workTitle", new BasicDBObject("$regex", searchWord));
            }
            /*DBCursor cursor = super.getDBCollection().find(site).sort(sort).skip(i * perPageCount).limit(perPageCount);
            List<TempWorks> workList = Lists.newArrayList();
            while (cursor.hasNext()) {
                workList.add(decode(cursor.next(), TempWorks.class));
            }*/

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
            DBObject skip = new BasicDBObject("$skip", i * perPageCount);
            DBObject limit = new BasicDBObject("$limit", perPageCount);
            DBObject lookupFields = new BasicDBObject("from", "crawlwebsite").append("localField", "workWebsiteId")
                    .append("foreignField", "crwsId").append("as", "crawlWebsiteList");
            DBObject lookup = new BasicDBObject("$lookup", lookupFields);
            List<DBObject> queryList = Lists.newArrayList();
            queryList.add(match);
            queryList.add(sortFields);
            queryList.add(skip);
            queryList.add(limit);
            queryList.add(lookup);
            List<TempWorksVo> workList = this.getTempWorksVos(queryList);
            pageDataList.add(new PageData(page, workList));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;
    }

    public TempWorksVo selectWorkById(Integer workId) {
        DBObject query = new BasicDBObject("workId", workId);
        DBCursor cursor = super.getDBCollection().find(query);
        TempWorksVo worksVo = JSON.parseObject(JSON.toJSONString(cursor.next()), TempWorksVo.class);
        DBObject queryField = new BasicDBObject("woupWorkId", workId);
        DBObject fields = new BasicDBObject("woupTime", 1);
        DBObject sortTime = new BasicDBObject("woupTime", -1);
        DBCursor workUpdateCursor = worksUpdateDao.getDBCollection().find(queryField, fields).sort(sortTime).limit(1);
        while (workUpdateCursor.hasNext()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            worksVo.setWorkLastUpdateTime(sdf.format(workUpdateCursor.next().get("woupTime")));
        }
        return worksVo;
    }

    public List<TempWorks> selectWorksByAuthId(Integer authId) {
        DBObject query = new BasicDBObject("workAuthId", authId);
        DBCursor cursor = super.getDBCollection().find(query);
        List<TempWorks> workList = Lists.newArrayList();
        while (cursor.hasNext()) {
            TempWorks work = decode(cursor.next(), TempWorks.class);
            workList.add(work);
        }
        return workList;
    }

    private List<TempWorksVo> getTempWorksVos(List<DBObject> queryList) {
        Iterator<DBObject> iterator = super.getDBCollection().aggregate(queryList).results().iterator();
        List<TempWorksVo> workList = Lists.newArrayList();
        while (iterator.hasNext()) {
            String json = JSON.toJSONString(iterator.next());
            TempWorksVo worksVo = JSON.parseObject(json, TempWorksVo.class);
            DBObject queryField = new BasicDBObject("woupWorkId", worksVo.getWorkId());
            DBObject fields = new BasicDBObject("woupTime", 1);
            DBObject sortTime = new BasicDBObject("woupTime", -1);
            DBCursor cursor = worksUpdateDao.getDBCollection().find(queryField, fields).sort(sortTime).limit(1);
            while (cursor.hasNext()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                worksVo.setWorkLastUpdateTime(sdf.format(cursor.next().get("woupTime")));
            }
            workList.add(worksVo);
        }
        return workList;
    }

    /**
     * 所有作品统计信息（点击量，评论量，推荐量），一次性要超16M，多次速度慢
     */
    @Deprecated
    public List<TempWorks> selectSortedWorks(Integer siteId, Integer sortField, Integer descOrAsc, Integer pageSize) {
        /*db.works.aggregate([{"$match":{"workWebsiteId":siteId},{"$sort":{"workTotalHits":-1}}}],{allowDiskUse:true})*/
        Long timea = System.currentTimeMillis();
        List<TempWorks> workList = Lists.newArrayList();
        int allCount = Long.valueOf(super.getCount()).intValue();
        int pageCount = allCount / pageSize;
        System.out.println("allCount:" + allCount + ", pageCount:" + pageCount);
        for (int i = 0; i < pageCount; i++) {
            Long timeb = System.currentTimeMillis();
            DBObject site = new BasicDBObject();
            if (siteId != 0) {
                site.put("workWebsiteId", siteId);
            }
            DBObject sort = new BasicDBObject();
            this.sortFields(sortField, descOrAsc, sort);
            List<DBObject> queryList = Lists.newArrayList();
            queryList.add(new BasicDBObject("$match", site));
            queryList.add(new BasicDBObject("$sort", sort));
            queryList.add(new BasicDBObject("$skip", i * pageSize));
            queryList.add(new BasicDBObject("$limit", pageSize));
            Cursor cursor = super.getDBCollection().aggregate(queryList, AggregationOptions.builder().allowDiskUse(true).build());
            while (cursor.hasNext()) {
                TempWorks work = decode(cursor.next(), TempWorks.class);
                workList.add(work);
            }
            Long timebb = System.currentTimeMillis();
            System.out.println("第" + i + "次查询+排序：" + (timebb - timeb));
        }
        Long timeaa = System.currentTimeMillis();
        System.out.println("所有消耗：" + (timeaa - timea));
        return workList;
    }

    /**
     * 根据点击量/评论量/推荐量的取值范围获取对应作品数量
     */
    public Area selectRangeWithWorkCount(Integer siteId, Integer sortField, Integer min, Integer max) {
        /*db.works.find({"workWebsiteId":siteId,"workTotalHits":{"$gte":5000,"$lte":10000}});*/
        String fieldStr = "";
        if (sortField == 1) {
            fieldStr = "workTotalHits";
        } else if (sortField == 2) {
            fieldStr = "workCommentsNum";
        } else if (sortField == 3) {
            fieldStr = "workTotalRecoms";
        }
        DBObject query = new BasicDBObject();
        if (siteId != 0) {
            query.put("workWebsiteId", siteId);
        }
        query.put(fieldStr, new BasicDBObject().append("$gte", min).append("$lt", max));
        int workCount = super.getDBCollection().find(query).count();
        Area area = new Area(min.toString(), workCount);
        return area;
    }

    private void sortFields(Integer sortField, Integer descOrAsc, DBObject sort) {
        if (sortField == 1) { //hits
            sort.put("workTotalHits", descOrAsc);
        } else if (sortField == 2) {
            sort.put("workCommentsNum", descOrAsc);
        } else if (sortField == 3) {
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
}
