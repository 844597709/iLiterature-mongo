package com.swust.kelab.mongo.utils;

import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * mongodb服务接口
 *
 */
public interface MongoService {
    /**
     * 获取mongodb collection
     * 
     * @param collectionName
     * @return
     */
    DBCollection getCollection(String collectionName);

    /**
     * 执行一条数据库命令并返回结果
     *
     * @param command
     *            数据库命令对象
     * @return 命令执行结果
     */
    CommandResult command(DBObject command);

    /**
     * 从一个集合里删除一条记录,并返回被删的记录
     * 
     * @param collectionName
     *            集合名
     * @return 被删除的记录
     */
    DBObject remove(String collectionName);

    /**
     * 从一个集合里删除一个记录，并返回被删除的记录
     * 
     * @param collectionName
     *            集合名
     * @param query
     *            删除条件,若集合里有多个记录符合条件,则只会删除并返回第一个记录
     * @return 取回的记录
     */
    DBObject remove(String collectionName, DBObject query);

    /**
     * 从一个集合里删除一个记录，并返回被删除的记录
     * 
     * @param collectionName
     *            集合名
     * @param query
     *            删除条件,若集合里有多个记录符合条件,则只会删除并返回第一个记录
     * @param fields
     *            要取回的字段
     * @return 取回的记录
     */
    DBObject remove(String collectionName, DBObject query, DBObject fields);
}
