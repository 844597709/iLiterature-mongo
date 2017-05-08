package com.swust.kelab.mongo.utils.impl;

import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.swust.kelab.mongo.utils.MongoService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("mongoService")
public class MongoServiceImpl implements MongoService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public DBCollection getCollection(String collectionName) {
        return mongoTemplate.getCollection(collectionName);
    }

    @Override
    public CommandResult command(DBObject command) {
        return mongoTemplate.executeCommand(command);
    }

    @Override
    public DBObject remove(String collectionName) {
        return remove(collectionName, null, null);
    }

    @Override
    public DBObject remove(String collectionName, DBObject query) {
        return remove(collectionName, query, null);
    }

    @Override
    public DBObject remove(String collectionName, DBObject query, DBObject fields) {
        DBCollection dbc = getCollection(collectionName);
        DBObject dbObj = dbc.findAndModify(query, fields, null, true, null, true, false);
        return dbObj;
    }

}
