package com.swust.kelab.mongo.dao;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.swust.kelab.mongo.domain.TempAuthor;
import com.swust.kelab.mongo.dao.query.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by zengdan on 2017/2/9.
 */
@Repository(value = "authorDao")
public class AuthorDaoTemp extends BaseDao<TempAuthor> {
    @Override
    public void init() {
        super.collection = "author";
    }

    @Override
    public boolean updateOrSave(TempAuthor entity) {
        if(entity.getAuthId()!=null){//更新
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

    public Integer getNum(Integer websiteId, String field, Integer start, Integer end){
        DBObject obj = new BasicDBObject();
        obj.put("$gt", start);
        obj.put("lte", end);
        DBObject query = new BasicDBObject(field, obj);
        if(websiteId!=null){
            DBObject siteId = new BasicDBObject("authWebsiteId", websiteId);
            BasicDBList and = new BasicDBList();
            and.add(siteId);
            and.add(query);
            query = new BasicDBObject("$and", and);
        }
        return super.getDBCollection().find(query).count();
    }
}
