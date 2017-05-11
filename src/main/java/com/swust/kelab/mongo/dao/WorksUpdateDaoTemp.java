package com.swust.kelab.mongo.dao;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.swust.kelab.mongo.dao.base.CommonDao;
import com.swust.kelab.mongo.dao.base.PageResult;
import com.swust.kelab.mongo.dao.query.BaseDao;
import com.swust.kelab.mongo.dao.query.BaseQuery;
import com.swust.kelab.mongo.domain.TempWorksUpdate;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.utils.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zengdan on 2017/5/5.
 */
@Repository(value = "worksUpdateDao")
public class WorksUpdateDaoTemp extends BaseDao<TempWorksUpdate> {
    @Resource
    private CommonDao commonDao;
    @Resource
    private WorksInfoDaoTemp worksInfoDao;

    @Override
    public void init() {
//        super.collection = "test";
        super.collection = "worksupdate";
    }

    @Deprecated
    public List<TempWorksUpdate> selectSomeWorkUpdate(Integer limitNum) {
        DBCursor cursor = super.getDBCollection().find().limit(limitNum);
        List<TempWorksUpdate> list = Lists.newArrayList();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            sdf.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) obj.get("woupTime"));
            String woupTime = sdf.format(calendar.getTime());*/
//            obj.get("woupTime")
            TempWorksUpdate worksUpdate = decode(obj, TempWorksUpdate.class);
            list.add(worksUpdate);
        }
        return list;
    }

    /**
     * 慢
     */
    @Deprecated
    public List<Area> selectRecentWorkUpdate(Integer dayNum) {
        DBObject match = new BasicDBObject("$match", new BasicDBObject("woupTime", new BasicDBObject("$ne", "")));
        DBObject dateToStringFileds = new BasicDBObject();
        dateToStringFileds.put("format", "%Y-%m-%d");
//        dateToStringFileds.put("date", "$woupUpdateTime");
        dateToStringFileds.put("date", "$woupTime"); // 暂时使用采集时间
        DBObject dateToString = new BasicDBObject("$dateToString", dateToStringFileds);
        DBObject project = new BasicDBObject("$project", new BasicDBObject("time", dateToString));
        DBObject groupFileds = new BasicDBObject();
        groupFileds.put("_id", "$time");
        groupFileds.put("value", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFileds);
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("_id", 1));
        DBObject limit = new BasicDBObject("$limit", dayNum);
        List<DBObject> queryList = Lists.newArrayList();
        queryList.add(match);
        queryList.add(project);
        queryList.add(group);
        queryList.add(sort);
        queryList.add(limit);
        List<Area> results = commonDao.queryByCondition(super.getDBCollection(), queryList);
        return results;
    }

    @Deprecated
    public List<Area> selectRecentWorkUpdate2(Integer dayNum) {
//        DBObject match = new BasicDBObject("$match", new BasicDBObject("woupTime", new BasicDBObject("$ne", "")));
        Long timea = System.currentTimeMillis();
        DBCursor cursor = super.getDBCollection().find(new BasicDBObject("woupTime", new BasicDBObject("$ne", "")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Area> allWorksUpdateList = Lists.newArrayList();
        while (cursor.hasNext()) {
            TempWorksUpdate worksUpdate = decode(cursor.next(), TempWorksUpdate.class);
            String time = sdf.format(worksUpdate.getWoupTime());
            allWorksUpdateList.add(new Area(time, 1));
        }
        Long timeaa = System.currentTimeMillis();
        System.out.println("查询所有花费时间--" + (timeaa - timea));
        Map<String, Long> timeWithCountMap = allWorksUpdateList.stream().collect(Collectors.groupingBy(Area::getName, Collectors.counting()));
        Long timeb = System.currentTimeMillis();
        System.out.println("分组花费时间--" + (timeb - timeaa));
        List<Area> countByTimeList = Lists.newArrayList();
        timeWithCountMap.forEach((time, count) -> {
            countByTimeList.add(new Area(time, count.intValue()));
        });
        countByTimeList.stream().sorted((o1, o2) -> o1.getName().compareTo(o2.getName()));
        Long timebb = System.currentTimeMillis();
        System.out.println("排序花费时间--" + (timebb - timeb));
        List<Area> result = countByTimeList.subList(0, dayNum);
        Long time = System.currentTimeMillis();
        System.out.println("work-time:" + (time - timea));
        return result;
    }

    /**
     * @param descOrAsc 1升序，-1降序
     * @param startTime 开始时间
     * @param dayNum    几天
     * @return
     */
    public List<Area> selectWorkUpdateByTime(Integer descOrAsc, String startTime, Integer dayNum) throws ParseException {
        /*db.worksupdate.aggregate([
        {"$match":{"woupRoughTime":{"$gte":startTime, "lt":endTime}}},
        {"$group":{"_id":"$woupRoughTime", "value":{$sum:1}}},
        {"$sort":{"woupRoughTime":-1}},  {"$limit":10} ]);*/
        Long timea = System.currentTimeMillis();
        if (StringUtils.isEmpty(startTime)) {
            return CollectionUtil.emptyList();
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(sdf.parse(startTime));
        calendar.add(Calendar.DATE, dayNum);
        String endTime = sdf.format(calendar.getTime());
//            System.out.println("calendar-after-time:" + afterTime);

        DBObject matchField = new BasicDBObject("woupRoughTime", new BasicDBObject().append("$gte", startTime).append("$lt", endTime));
        DBObject match = new BasicDBObject("$match", matchField);
        DBObject groupFields = new BasicDBObject().append("_id", "$woupRoughTime").append("value", new BasicDBObject("$sum", 1));
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
        System.out.println("work-updatetime:" + (timeaa - timea));
        return results;
    }

    /**
     * 查询最近更新日期及作品数量
     */
    public Area selectLastTimeWithWorkCount(Integer siteId){
        /**
         db.works.find({"workWebsiteId":siteId}, {"workId":1});
         db.worksupdate.find().sort({"woupTime":-1}).limit(1);   //insert的时候排重了的
         */
        DBObject site = new BasicDBObject("workWebsiteId", siteId);
        DBObject field = new BasicDBObject("workId", 1);
        DBCursor idCursor = worksInfoDao.getDBCollection().find(site, field);
        List<Integer> workIdList = Lists.newArrayList();
        while(idCursor.hasNext()){
            workIdList.add(Integer.parseInt(idCursor.next().get("workId").toString()));
        }
        DBObject query = new BasicDBObject("woupWorkId", new BasicDBObject("$in", workIdList));
        DBObject workupdateField = new BasicDBObject("woupTime", 1);
        DBObject sort = new BasicDBObject("woupTime", -1);
        DBCursor cursor = super.getDBCollection().find(query, workupdateField).sort(sort).limit(1);
        Area lastTimeWithCount = new Area();
        if(cursor.hasNext()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            lastTimeWithCount.setName(sdf.format(cursor.next().get("woupTime")));
        }
        lastTimeWithCount.setValue(workIdList.size());
        return lastTimeWithCount;
    }

    @Override
    public boolean updateOrSave(TempWorksUpdate entity) {
        return false;
    }

    @Override
    public PageResult<TempWorksUpdate> query(BaseQuery query) {
        return super.query(query);
    }

    @Override
    public TempWorksUpdate findById(Integer id) {
        return null;
    }
}
