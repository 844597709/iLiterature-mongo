package com.swust.kelab.mongo.dao;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.swust.kelab.mongo.dao.base.CommonDao;
import com.swust.kelab.mongo.dao.query.BaseDao;
import com.swust.kelab.mongo.domain.TempAuthorUpdate;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.utils.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zengdan on 2017/5/5.
 */
@Repository(value = "authorUpdateDao")
public class AuthorUpdateDaoTemp extends BaseDao<TempAuthorUpdate> {
    @Resource
    private CommonDao commonDao;
    @Resource
    private AuthorDaoTemp authorDao;

    @Override
    public void init() {
        super.collection = "authorupdate";
    }

    @Deprecated
    public List<TempAuthorUpdate> selectSomeAuthorUpdate(Integer limitNum) {
        DBCursor cursor = super.getDBCollection().find().limit(limitNum);
        List<TempAuthorUpdate> list = Lists.newArrayList();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            sdf.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) obj.get("woupTime"));
            String woupTime = sdf.format(calendar.getTime());*/
//            obj.get("woupTime")
            TempAuthorUpdate authorUpdate = decode(obj, TempAuthorUpdate.class);
            list.add(authorUpdate);
        }
        return list;
    }

    @Deprecated
    public List<Area> selectRecentAuthorUpdate(Integer dayNum) {
//        DBObject match = new BasicDBObject("$match", new BasicDBObject("$auupTime", new BasicDBObject("$ne", "")));
        Long timea = System.currentTimeMillis();
        DBObject dateToStringFileds = new BasicDBObject();
        dateToStringFileds.put("format", "%Y-%m-%d");
        dateToStringFileds.put("date", "$auupTime"); //只有采集时间
        DBObject dateToString = new BasicDBObject("$dateToString", dateToStringFileds);
        DBObject project = new BasicDBObject("$project", new BasicDBObject("time", dateToString));
        DBObject groupFileds = new BasicDBObject();
        groupFileds.put("_id", "$time");
        groupFileds.put("value", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFileds);
//        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("value", -1));
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("_id", 1));
        DBObject limit = new BasicDBObject("$limit", dayNum);
        List<DBObject> queryList = Lists.newArrayList();
//        queryList.add(match);
        queryList.add(project);
        queryList.add(group);
        queryList.add(sort);
        queryList.add(limit);
        List<Area> results = commonDao.queryByCondition(super.getDBCollection(), queryList);
        Long timeaa = System.currentTimeMillis();
        System.out.println("author-time:"+(timeaa-timea));
        return results;
    }

    public List<Area> selectAuthorUpdateByTime(Integer descOrAsc, String startTime, Integer dayNum) throws ParseException {
        /*db.authorupdate.aggregate([
        {$group:{"_id":"$auupRoughTime", "value":{$sum:1}}},
        {"$sort":{"auupRoughTime":-1}},  {"$limit":10} ]);*/
        Long timea = System.currentTimeMillis();
        if (StringUtils.isEmpty(startTime)) {
            return CollectionUtil.emptyList();
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(sdf.parse(startTime));
        calendar.add(Calendar.DATE, dayNum);
        String endTime = sdf.format(calendar.getTime());

        DBObject matchField = new BasicDBObject("auupRoughTime", new BasicDBObject().append("$gte", startTime).append("$lt", endTime));
        DBObject match = new BasicDBObject("$match", matchField);
        DBObject groupFields = new BasicDBObject().append("_id", "$auupRoughTime").append("value", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("_id", descOrAsc));
        DBObject limit = new BasicDBObject("$limit", dayNum);
        List<DBObject> queryList = Lists.newArrayList();
        queryList.add(match);
        queryList.add(group);
        queryList.add(sort);
        queryList.add(limit);
        List<Area> results = commonDao.queryByCondition(super.getDBCollection(), queryList);
        Long timeaa = System.currentTimeMillis();
        System.out.println("author-updatetime:"+(timeaa-timea));
        return results;
    }

    /**
     * 查询最近更新日期及作者数量
     */
    public Area selectLastTimeWithAuthorCount(Integer siteId){
        /**
         db.authorupdate.find().sort({"auupTime":-1}).limit(1);   //insert的时候排重了的
         */
        /**
         * 这个语句，太慢了
         db.author.explain().aggregate([
         {"$match":{"authWebsiteId":1}},
         {"$project":{authId:1}},
         {"$lookup":{
             from: "authorupdate",
             localField: "authId",
             foreignField: "auupAuthId",
             as: "authorupdateList"
         }},
         {"$sort":{"authorupdateList.auupTime":-1}},
         {"$project":{"authorupdateList.auupTime":1}},
         {"$limit":1}
         ]);
         */
        DBObject site = new BasicDBObject("authWebsiteId", siteId);
        DBObject authorField = new BasicDBObject("authId", 1);
        DBCursor idCursor = authorDao.getDBCollection().find(site, authorField);
        List<Integer> authorIdList = Lists.newArrayList();
        while(idCursor.hasNext()){
            authorIdList.add((Integer) idCursor.next().get("authId"));
        }
        /*
        db.authorupdate.find({"auupAuthorId":{"$in":{97056,97057,97061,97067,97072,97074,97079,97081,97084,97089,97092,97098,97103,97105,97108}}},
        {auupTime:1}).sort({auupTime:-1}).limit(1);
        */
        DBObject query = new BasicDBObject("auupAuthId", new BasicDBObject("$in", authorIdList));
        DBObject authorupdateField = new BasicDBObject("auupTime", 1);
        DBObject sort = new BasicDBObject("auupTime", -1);
        DBCursor cursor = super.getDBCollection().find(query, authorupdateField).sort(sort).limit(1);
        Area lastTimeWithCount = new Area();
        if(cursor.hasNext()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            lastTimeWithCount.setName(sdf.format(cursor.next().get("auupTime")));
        }
        lastTimeWithCount.setValue(authorIdList.size());
        return lastTimeWithCount;
    }

    /**
     * 根据作者id查询作者最近一次更新状况
     */
    public TempAuthorUpdate selectAuthorUpdateByAuthId(Integer authId){
        DBObject query = new BasicDBObject("auupAuthId", authId);
        DBObject sort = new BasicDBObject("auupTime", -1);
        DBCursor cursor = super.getDBCollection().find(query).sort(sort).limit(1);
        TempAuthorUpdate authorUpdate = null;
        while(cursor.hasNext()){
            authorUpdate = decode(cursor.next(), TempAuthorUpdate.class);
        }
        return authorUpdate;
    }

    @Override
    public boolean updateOrSave(TempAuthorUpdate entity) {
        return false;
    }

    @Override
    public TempAuthorUpdate findById(Integer id) {
        return null;
    }
}
