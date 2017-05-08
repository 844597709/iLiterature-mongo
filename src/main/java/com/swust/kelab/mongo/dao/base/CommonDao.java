package com.swust.kelab.mongo.dao.base;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.swust.kelab.mongo.domain.model.Area;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zengdan on 2017/5/6.
 */
@Repository(value = "commonDao")
public class CommonDao {
    public List<Area> queryByCondition(DBCollection collection, List<DBObject> queryList){
        Iterable<DBObject> results = collection.aggregate(queryList).results();
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
}
