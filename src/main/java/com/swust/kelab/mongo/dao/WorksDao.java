package com.swust.kelab.mongo.dao;

import com.mongodb.BasicDBObject;
import com.swust.kelab.mongo.domain.TempWorks;
import com.swust.kelab.mongo.dao.query.BaseDao;
import org.springframework.stereotype.Component;

/**
 * Created by zengdan on 2017/2/9.
 */
@Component("worksDao")
public class WorksDao extends BaseDao<TempWorks> {
    @Override
    public void init() {
        super.collection = "works";
    }

    @Override
    public boolean updateOrSave(TempWorks entity) {
        if(entity.getWorkId()!=null){//更新
            BasicDBObject query = new BasicDBObject("workId", entity.getWorkId());
            BasicDBObject update = new BasicDBObject("$set", entity);
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
    public TempWorks findById(Integer id) {
        BasicDBObject query = new BasicDBObject("workId", id);
        TempWorks work = decode(super.getDBCollection().findOne(query), TempWorks.class);
        return work;
    }

    /*@Override
    public List<Author> getBusinessesByIds(List<Integer> ids) {
        BasicDBList values = new BasicDBList();
        values.addAll(ids);
        BasicDBObject query = new BasicDBObject("$in", values);
        List<Author> list = decode(super.getDBCollection().find(query), Author.class);
        return list;
    }*/

}
