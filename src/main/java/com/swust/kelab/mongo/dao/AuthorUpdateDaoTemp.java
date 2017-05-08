package com.swust.kelab.mongo.dao;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.swust.kelab.mongo.dao.base.CommonDao;
import com.swust.kelab.mongo.dao.query.BaseDao;
import com.swust.kelab.mongo.dao.query.BaseQuery;
import com.swust.kelab.mongo.domain.TempAuthorUpdate;
import com.swust.kelab.mongo.domain.model.Area;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zengdan on 2017/5/5.
 */
@Repository(value = "authorUpdateDao")
public class AuthorUpdateDaoTemp extends BaseDao<TempAuthorUpdate> {
    @Resource
    private CommonDao commonDao;
    @Override
    public void init() {
        super.collection = "authorupdate";
    }

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

    public List<Area> selectRecentAuthorUpdate(Integer dayNum) {
//        DBObject match = new BasicDBObject("$match", new BasicDBObject("$auupTime", new BasicDBObject("$ne", "")));
        DBObject dateToStringFileds = new BasicDBObject();
        dateToStringFileds.put("format", "%Y-%m-%d");
        dateToStringFileds.put("date", "$auupTime"); //只有采集时间
        DBObject dateToString = new BasicDBObject("$dateToString", dateToStringFileds);
        DBObject project = new BasicDBObject("$project", new BasicDBObject("time", dateToString));
        DBObject groupFileds = new BasicDBObject();
        groupFileds.put("_id", "$time");
        groupFileds.put("value", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFileds);
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("value", -1));
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

    @Override
    public boolean updateOrSave(TempAuthorUpdate entity) {
        return false;
    }

    @Override
    public TempAuthorUpdate findById(Integer id) {
        return null;
    }

    @Override
    public Long getCount(BaseQuery query) {
        return super.getCount(query);
    }
}
