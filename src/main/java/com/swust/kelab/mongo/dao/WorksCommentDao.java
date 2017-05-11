package com.swust.kelab.mongo.dao;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.swust.kelab.mongo.domain.TempWorksComment;
import com.swust.kelab.mongo.dao.query.BaseDao;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zengdan on 2017/2/9.
 */
@Component("worksCommentDao")
public class WorksCommentDao extends BaseDao<TempWorksComment> {
    @Override
    public void init() {
        super.collection = "workscomment";
    }

    public List<TempWorksComment> selectWorkCommentById(Integer workId){
        // TODO 记得加索引
        DBObject query = new BasicDBObject("wocoWorkId", workId);
        DBCursor cursor = super.getDBCollection().find(query);
        List<TempWorksComment> workCommentList = Lists.newArrayList();
        while(cursor.hasNext()){
            TempWorksComment worksComment = decode(cursor.next(), TempWorksComment.class);
            workCommentList.add(worksComment);
        }
        return workCommentList;
    }

    @Override
    public boolean updateOrSave(TempWorksComment entity) {
        if(entity.getWocoId()!=null){//更新
            BasicDBObject query = new BasicDBObject("wocoId", entity.getWocoId());
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
    public TempWorksComment findById(Integer id) {
        BasicDBObject query = new BasicDBObject("wocoId", id);
        TempWorksComment worksComment = decode(super.getDBCollection().findOne(query), TempWorksComment.class);
        return worksComment;
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
