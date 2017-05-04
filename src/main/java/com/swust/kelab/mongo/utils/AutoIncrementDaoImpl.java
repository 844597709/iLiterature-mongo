package com.swust.kelab.mongo.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.swust.kelab.mongo.dao.query.MongoTemplateDao;
import org.springframework.stereotype.Component;

/**
 * Created by libo on 16/8/22.
 */
@Component("autoIncrementDao")
public class AutoIncrementDaoImpl extends MongoTemplateDao<AutoIncrement> implements AutoIncrementDao {

    @Override
    public void init() {
        super.collection = "autoincrement";
    }

    @Override
    public Integer getAutoIncrement(String collectionName, String fieldName) {
        return getAutoIncrement(collectionName, fieldName, 0);
    }

    @Override
    public Integer getAutoIncrement(String collectionName, String fieldName, int init) {
        BasicDBObject dbObject = new BasicDBObject("collectionName", collectionName);
        dbObject.put("fieldName", fieldName);
        DBObject result = super.getDBCollection().findOne(dbObject);
        if (result == null) {
            dbObject.put("incrementId", init);
            super.getDBCollection().save(dbObject);
            return init;
        }
        DBObject inc = new BasicDBObject("$inc", new BasicDBObject("incrementId", 1));
        super.getDBCollection().update(dbObject, inc);
        result = super.getDBCollection().findOne(dbObject);
        if (result == null) {
            return null;
        }
        return (Integer) result.get("incrementId");
    }
}
