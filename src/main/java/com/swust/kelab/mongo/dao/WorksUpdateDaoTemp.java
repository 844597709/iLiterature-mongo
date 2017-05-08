package com.swust.kelab.mongo.dao;

import com.google.common.collect.Lists;
import com.mongodb.AggregationOutput;
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
import com.swust.kelab.web.model.AuthorWorkUpdate;
import org.springframework.core.SimpleAliasRegistry;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zengdan on 2017/5/5.
 */
@Repository(value = "worksUpdateDao")
public class WorksUpdateDaoTemp extends BaseDao<TempWorksUpdate> {
    @Resource
    private CommonDao commonDao;
    @Override
    public void init() {
//        super.collection = "test";
        super.collection = "worksupdate";
    }

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

    public List<Area> selectRecentWorkUpdate(Integer dayNum) {
//        DBObject match = new BasicDBObject("$match", new BasicDBObject("woupTime", new BasicDBObject("$ne", "")));
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
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("_id", -1));
        DBObject limit = new BasicDBObject("$limit", dayNum);
        List<DBObject> queryList = Lists.newArrayList();
//        queryList.add(match);
        queryList.add(project);
        queryList.add(group);
        queryList.add(sort);
        queryList.add(limit);
        List<Area> results = commonDao.queryByCondition(super.getDBCollection(), queryList);
        return results;
    }

    public List<Area> selectRecentWorkUpdate2(Integer dayNum) {
        DBObject match = new BasicDBObject("$match", new BasicDBObject("woupTime", new BasicDBObject("$ne", "")));
        DBCursor cursor = super.getDBCollection().find();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Area> allWorksUpdateList = Lists.newArrayList();
        Long timea = System.currentTimeMillis();
        while(cursor.hasNext()){
            TempWorksUpdate worksUpdate = decode(cursor.next(), TempWorksUpdate.class);
            String time = sdf.format(worksUpdate.getWoupTime());
            allWorksUpdateList.add(new Area(time, 1));
        }
        Long timeaa = System.currentTimeMillis();
        System.out.println("查询所有花费时间--"+(timeaa-timea));
        Map<String, Long> timeWithCountMap = allWorksUpdateList.stream().collect(Collectors.groupingBy(Area::getName, Collectors.counting()));
        Long timeb = System.currentTimeMillis();
        System.out.println("分组花费时间--"+(timeb-timeaa));
        List<Area> countByTimeList = Lists.newArrayList();
        timeWithCountMap.forEach((time, count)->{
            countByTimeList.add(new Area(time, count.intValue()));
        });
        countByTimeList.stream().sorted((o1, o2) -> o1.getName().compareTo(o2.getName()));
        Long timebb = System.currentTimeMillis();
        System.out.println("排序花费时间--"+(timebb-timeb));
        return countByTimeList.subList(0, dayNum);
    }

    public List<AuthorWorkUpdate> selectWorkUpdateByTime(Integer dayNum) {
        /*db.test.aggregate([
        {$project: {yearMonthDay:{$dateToString:{format:"%Y-%m-%d", date:"$woupUpdateTime"}}}},
        {$group:{"_id":"$yearMonthDay", "value":{$sum:1}}},
        {$sort:{"value":-1}},
        {$limit:10}
        ],{allowDiskUse:true})*/

        return CollectionUtil.emptyList();
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

    @Override
    public Long getCount(BaseQuery query) {
        return super.getCount(query);
    }
}
