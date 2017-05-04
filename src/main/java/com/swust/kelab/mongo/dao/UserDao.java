package com.swust.kelab.mongo.dao;


import com.mongodb.BasicDBObject;
import com.swust.kelab.mongo.domain.model.User;
import com.swust.kelab.mongo.dao.query.BaseDao;

/**
 * 用户
 */
public class UserDao extends BaseDao<User>{
    @Override
    public void init() {
        super.collection = "user";
    }

    @Override
    public boolean updateOrSave(User entity) {
        if(entity.getUserId()!=null){//更新
            BasicDBObject query = new BasicDBObject("userId", entity.getUserId());
            BasicDBObject update = new BasicDBObject("$set", entity);
            super.getDBCollection().update(query, update);
        }else{//新增
            //之后再写,涉及id自增
        }
        return true;
    }

    /*@Override
    public boolean delete(Integer id) {
        BasicDBObject query = new BasicDBObject("userId", id);
        super.getDBCollection().findAndRemove(query);
        return true;
    }*/

    @Override
    public User findById(Integer id) {
        BasicDBObject query = new BasicDBObject("userId", id);
        User user = decode(super.getDBCollection().findOne(query), User.class);
        return user;
    }

    /*@Override
    public List<User> getBusinessesByIds(List<Integer> ids) {
        BasicDBList values = new BasicDBList();
        values.addAll(ids);
        BasicDBObject query = new BasicDBObject("$in", values);
        List<User> list = decode(super.getDBCollection().find(query), User.class);
        return list;
    }*/
}
