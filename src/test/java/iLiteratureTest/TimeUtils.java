package iLiteratureTest;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.swust.kelab.mongo.domain.TempWorks;
import com.swust.kelab.mongo.domain.TempWorksUpdate;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zengdam on 2017/2/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:mongodb/mongodb-config.xml"})
public class TimeUtils {
    @Resource
    private MongoTemplate mongoTemplate;
    private String collectionName="worksupdate";

    @Test
    public void updateTime(){
        /*db.getCollection('worksupdate').find({
                $and:[{"woupUpdateTime":{$exists:true},
            "woupUpdateTime":"0000-00-00 00:00:00"}]})*/
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("iLiterature");
        MongoCollection col = db.getCollection("worksupdate");

        List<DBObject> list = new ArrayList<DBObject>();
        long timec = System.currentTimeMillis();
//        MongoCursor cursor = col.find().iterator();
        AggregateIterable iterable = col.aggregate(list).allowDiskUse(true);
        long timecc = System.currentTimeMillis();
        Iterator<Document> iterator = iterable.iterator();
        System.out.println("...查询完成, 共有"+col.count()+"数据, 消耗"+(timecc-timec)+"ms");
        int num=1;
        int number=0;
        while(iterator.hasNext()){
            long timep = System.currentTimeMillis();
            Document obj = iterator.next();
            String json = JSON.toJSONString(obj);
            TempWorksUpdate worksUpdate = JSON.parseObject(json, TempWorksUpdate.class);

            DBObject update = new BasicDBObject("$set", new BasicDBObject("woupTime", worksUpdate.getWoupTime().replace(".0", "")));
            DBObject query = new BasicDBObject("woupId", worksUpdate.getWoupId());
            mongoTemplate.getCollection(collectionName).update(query, update);//更新信息
            /*Document dupdate = null;
            if(worksUpdate.getWoupTime().contains(".")){
                String[] strs = worksUpdate.getWoupTime().split(".0");
                dupdate = new Document("$set", new Document("woupTime", worksUpdate.getWoupTime().replace(worksUpdate.getWoupTime(), strs[0]+".0")));
            }else{
                dupdate = new Document("$set", new Document("woupTime", worksUpdate.getWoupTime().replace(worksUpdate.getWoupTime(), worksUpdate.getWoupTime()+".0")));
            }*/
//            Document dupdate = new Document("$set", new Document("woupTime", worksUpdate.getWoupTime().replace(".0.0", "")));
//            Document dquery = new Document("woupId", worksUpdate.getWoupId());
//            col.updateOne(dquery, dupdate);
            long timepp = System.currentTimeMillis();
            if((timepp-timep)>50){
                number++;
                System.out.println(number+": 第"+num+"次更新超时, woupId="+worksUpdate.getWoupId()+", 消耗"+(timepp-timep)+"ms...");
            }
            num++;
        }
        long timeb = System.currentTimeMillis();
        System.out.println("sum:"+num+", 更新完成, "+collectionName+":"+mongoTemplate.getCollection(collectionName).getCount()+", 消耗"+(timeb-timecc)+"ms");
        System.out.println("所有完成...消耗"+(timeb-timec)+"ms");
    }
}
