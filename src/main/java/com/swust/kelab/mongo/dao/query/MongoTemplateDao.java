package com.swust.kelab.mongo.dao.query;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mongodb.*;
import com.swust.kelab.mongo.utils.MongoService;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

@Component("MongoTemplateDao")
public abstract class MongoTemplateDao<T extends BaseModel> implements InitializingBean {
    /*@Value("${mongo.databaseName}")
    protected String       db         = "iLiterature"; //db name默认为iLiterature*/
    protected String       collection = null;
    @Resource
    private MongoService mongoService;

    public abstract void init();

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    public DBCollection getDBCollection() {
        Assert.notNull(collection);
        DBCollection dbCollection = mongoService.getCollection(collection);
        dbCollection.setWriteConcern(WriteConcern.SAFE);
        dbCollection.setReadPreference(ReadPreference.primary());
        return dbCollection;
    }

    public DBObject encode(T obj) {
        String id = obj.get_id();
        obj.set_id(null);
        String str = JSON.toJSONString(obj);
        DBObject result = (DBObject) JSON.parse(str);
        if (StringUtils.isNotEmpty(id)) {
            result.put("_id", new ObjectId(id));
            obj.set_id(id);
        }
        return result;
    }

    public T decode(DBObject obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        String json = JSON.toJSONString(obj);
        T result = JSON.parseObject(json, clazz);
        Object oid = obj.get("_id");
        if (oid != null) {
            result.set_id(oid.toString());
        }
        return result;
    }

    public List<T> decode(DBCursor cursor, Class<T> clazz) {
        if (cursor == null) {
            return null;
        }
        List<T> list = Lists.newArrayList();
        while(cursor.hasNext()){
            DBObject obj = cursor.next();
            String json = JSON.toJSONString(obj);
            T result = JSON.parseObject(json, clazz);
            Object oid = obj.get("_id");
            if (oid != null) {
                result.set_id(oid.toString());
            }
            list.add(result);
        }
        return list;
    }
}
