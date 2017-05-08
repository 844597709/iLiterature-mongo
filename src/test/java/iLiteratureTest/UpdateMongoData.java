package iLiteratureTest;

/**
 * Created by zengdan on 2017/2/15.
 */

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mongodb.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.swust.kelab.mongo.dao.AuthorDaoTemp;
import com.swust.kelab.mongo.dao.base.PageInfo;
import com.swust.kelab.mongo.domain.TempAuthor;
import com.swust.kelab.mongo.domain.TempWorks;
import com.mongodb.bulk.BulkWriteResult;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:mongodb/mongodb-config.xml"})
public class UpdateMongoData {
    @Resource
    private AuthorDaoTemp authorDao;
    @Resource
    private MongoTemplate mongoTemplate;
    private String collectionName = "author";

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    /**
     * 2个collection联合查询，速度超级慢，放弃
     */
    @Test
    public void updateData() {
        DBCollection collection = mongoTemplate.getCollection(collectionName);
        System.out.println("获得连接 collection");
        System.out.println(collectionName + "共有" + collection.getCount() + "数据");
        long timea = System.currentTimeMillis();
        DBObject project = new BasicDBObject("$project", new BasicDBObject("authId", 1));
        DBObject fields = new BasicDBObject();
        fields.put("from", "works");
        fields.put("localField", "authId");
        fields.put("foreignField", "workAuthId");
        fields.put("as", "works");
        DBObject lookup = new BasicDBObject("$lookup", fields);
        int pageSize = 1000;
        int total = (int) collection.getCount();
        int count = total / pageSize + 1;
        PageInfo page = new PageInfo(1, pageSize);
        page.setTotal(total);
        while (count > 0) {
            long timeaa = System.currentTimeMillis();
            DBObject skip = new BasicDBObject("$skip", page.getCurrentPage() > 1 ? (page.getCurrentPage() - 1) * page.getPageSize() : 0);
            DBObject limit = new BasicDBObject("$limit", page.getPageSize());
            /*List list = new ArrayList();
            list.add(project);
            list.add(lookup);
            list.add(skip);
            list.add(limit);*/
//          AggregationOutput output = collection.aggregate(list);
            AggregationOutput output = collection.aggregate(project, lookup, skip, limit);
            long timep = System.currentTimeMillis();
            System.out.println("查询第" + page.getCurrentPage() + "次消耗" + (timep - timeaa) / 1000 + "s");
//            System.out.println(output.results().toString());
            Iterator<DBObject> iterator = output.results().iterator();
//            System.out.println("开始更新。。。");
            while (iterator.hasNext()) {
                DBObject obj = iterator.next();
                String json = JSON.toJSONString(obj);
//                System.out.println("json:"+json);
                AuthWork authWork = JSON.parseObject(json, AuthWork.class);
                List<TempWorks> workList = authWork.getWorks();
                int works = workList.size();
                int hits = 0;
                int comments = 0;
                int recoms = 0;
                for (TempWorks w : workList) {//获取作者作品总点击数，评论数，推荐数
                    hits += w.getWorkTotalHits();
                    comments += w.getWorkCommentsNum();
                    recoms += w.getWorkTotalRecoms();
                }
                DBObject auth = new BasicDBObject();
                auth.put("authWorksNum", works);
                if (hits < 0)
                    hits = -1;
                if (comments < 0)
                    comments = -1;
                if (recoms < 0)
                    recoms = -1;
                auth.put("authWorksHitsNum", hits);
                auth.put("authWorksCommentsNum", comments);
                auth.put("authWorksRecomsNum", recoms);
                DBObject update = new BasicDBObject("$set", auth);
                DBObject query = new BasicDBObject("authId", authWork.getAuthId());
                collection.update(query, update);//更新作者信息
            }
            long timebb = System.currentTimeMillis();
            System.out.println("已更新" + page.getCurrentPage() * page.getPageSize() + "数据, 消耗" + (timebb - timeaa) / 1000 + "s");
            page.setCurrentPage(page.getCurrentPage() + 1);
            count--;
        }
        long timeb = System.currentTimeMillis();
        System.out.println("查询完成, 共消耗" + (timeb - timea) / 1000 + "s");
        System.out.println("全部更新完毕");
    }

    /**
     * 2个collection联合查询（另一种方式），超级慢，放弃
     */
    @Test
    public void updateData1() {
        DBCollection collection = mongoTemplate.getCollection(collectionName);
        System.out.println("获得连接 collection");
        System.out.println(collectionName + "共有" + collection.getCount() + "数据");
        long timea = System.currentTimeMillis();
        int pageSize = 1000;
        PageInfo page = new PageInfo(1, pageSize);
        page.setTotal((int) collection.getCount());
        int count = (int) collection.getCount() / pageSize + 1;
        while (count > 0) {
            long timeaa = System.currentTimeMillis();
            TypedAggregation<AuthWork> a = Aggregation.newAggregation(AuthWork.class,
                    Aggregation.project("authId"),
                    Aggregation.lookup("works", "authId", "workAuthId", "works"),
                    Aggregation.skip((long) page.getCurrentPage() > 1 ? (page.getCurrentPage() - 1) * page.getPageSize() : (long) 0),
                    Aggregation.limit(page.getPageSize()));
            AggregationResults results = mongoTemplate.aggregate(a, collectionName, AuthWork.class);//mongoTemplate需要制定collection
            long timep = System.currentTimeMillis();
            System.out.println("查询第" + page.getCurrentPage() + "次消耗" + (timep - timeaa) / 1000 + "s");
            //打印查询语句
//            System.out.println("SQL:"+a.toString());
            //打印结果
//            System.out.println("RESULT:"+results.getMappedResults());
            System.out.println("...");
            List<AuthWork> authWorkList = results.getMappedResults();
            for (AuthWork work : authWorkList) {//获取作者作品总点击数，评论数，推荐数
                int works = work.getWorks().size();
                int hits = 0;
                int comments = 0;
                int recoms = 0;
                for (TempWorks w : work.getWorks()) {
                    hits += w.getWorkTotalHits();
                    comments += w.getWorkCommentsNum();
                    recoms += w.getWorkTotalRecoms();
                }
                DBObject auth = new BasicDBObject();
                auth.put("authWorksNum", works);
                if (hits < 0)
                    hits = -1;
                if (comments < 0)
                    comments = -1;
                if (recoms < 0)
                    recoms = -1;
                auth.put("authWorksHitsNum", hits);
                auth.put("authWorksCommentsNum", comments);
                auth.put("authWorksRecomsNum", recoms);
                DBObject update = new BasicDBObject("$set", auth);
                DBObject query = new BasicDBObject("authId", work.getAuthId());
                collection.update(query, update);//更新作者信息
            }
            long timebb = System.currentTimeMillis();
            System.out.println("已更新" + page.getCurrentPage() * page.getPageSize() + "数据, 消耗" + (timebb - timeaa) / 1000 + "s");
            page.setCurrentPage(page.getCurrentPage() + 1);
            count--;
//            long timebb = System.currentTimeMillis();
//            System.out.println("第"+page.getCurrentPage()+"页更新消耗"+(timebb-timeaa)/1000+"s");
        }
        long timeb = System.currentTimeMillis();
        System.out.println("查询完成, 共消耗" + (timeb - timea) / 1000 + "s");
        System.out.println("全部更新完毕");
    }

    /**
     * 使用java自身Mongo方法分页查询数据，再使用mongoTemplate逐条更新（更新5w，耗时平均7000ms）
     */
    @Test
    public void updateAuthorData() {
        /*
        db.getCollection('works').aggregate([
            {$project:{"workAuthId":1, "workTotalHits":1, "workCommentsNum":1, "workTotalRecoms":1}},
            {$group:{_id:"$workAuthId", "authId":{$first:"$workAuthId"}, "works":{$push: "$$ROOT"}}}
            ], {allowDiskUse : true})
         */

        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("iLiterature");
        MongoCollection col = db.getCollection("works");

        this.setCollectionName("works");
        DBCollection collection = mongoTemplate.getCollection(collectionName);
        DBObject fields = new BasicDBObject();
        fields.put("workAuthId", 1);
        fields.put("workTotalHits", 1);
        fields.put("workCommentsNum", 1);
        fields.put("workTotalRecoms", 1);
        DBObject groupFields = new BasicDBObject();
        groupFields.put("_id", "$workAuthId");
//        groupFields.put("number", new BasicDBObject("$sum", 1));
        groupFields.put("authId", new BasicDBObject("$first", "$workAuthId"));
        groupFields.put("works", new BasicDBObject("$push", "$$ROOT"));
        DBObject project = new BasicDBObject("$project", fields);
        DBObject group = new BasicDBObject("$group", groupFields);
        long timea = System.currentTimeMillis();
        int pageSize = 50000;
        PageInfo page = new PageInfo(1, pageSize);
        page.setTotal((int) collection.getCount());
        int count = (int) collection.getCount() / pageSize + 1;
        DBObject limit = new BasicDBObject("$limit", page.getPageSize());
        while (count > 0) {
            DBObject skip = new BasicDBObject("$skip", page.getCurrentPage() > 1 ? (page.getCurrentPage() - 1) * page.getPageSize() : 0);
            List<DBObject> list = new ArrayList<DBObject>();
            list.add(project);
            list.add(group);
            list.add(skip);
            list.add(limit);
//            this.setCollectionName("works");

            long timec = System.currentTimeMillis();
            AggregateIterable iterable = col.aggregate(list).allowDiskUse(true);
            long timecc = System.currentTimeMillis();
            Iterator<Document> iterator = iterable.iterator();
            System.out.println("...查询完成, 共有" + page.getCurrentPage() * page.getPageSize() + "数据, 消耗" + (timecc - timec) + "ms");

            //后者查询速度很慢，2000+ms
            /*long timeaa = System.currentTimeMillis();
            Cursor cursor = collection.aggregate(list, AggregationOptions.builder().allowDiskUse(true).build());
//        AggregationOutput output = mongoTemplate.getCollection(collectionName).aggregate(list);
            long timebb = System.currentTimeMillis();
            System.out.println(page.getCurrentPage()+"查询完成, 共有"+page.getCurrentPage()*page.getPageSize()+"数据, 消耗"+(timebb-timeaa)+"ms");
            */
//        Iterator<DBObject> iterator = output.results().iterator();
            this.setCollectionName("author");
//        while(iterator.hasNext()){
//            DBObject obj = iterator.next();
            int num = 1;
            int number = 0;
            while (iterator.hasNext()) {
                long timep = System.currentTimeMillis();
                Document obj = iterator.next();
                String json = JSON.toJSONString(obj);
                AuthWork authWork = JSON.parseObject(json, AuthWork.class);
                List<TempWorks> workList = authWork.getWorks();
                int works = workList.size();
                int hits = 0;
                int comments = 0;
                int recoms = 0;
                for (TempWorks w : workList) {//获取作者作品总点击数，评论数，推荐数
                    hits += w.getWorkTotalHits();
                    comments += w.getWorkCommentsNum();
                    recoms += w.getWorkTotalRecoms();
                }
                DBObject auth = new BasicDBObject();
                auth.put("authWorksNum", works);
                if (hits < 0)
                    hits = -1;
                if (comments < 0)
                    comments = -1;
                if (recoms < 0)
                    recoms = -1;
                auth.put("authWorksHitsNum", hits);
                auth.put("authWorksCommentsNum", comments);
                auth.put("authWorksRecomsNum", recoms);
                DBObject update = new BasicDBObject("$set", auth);
                DBObject query = new BasicDBObject("authId", authWork.getAuthId());
                mongoTemplate.getCollection(collectionName).update(query, update);//更新作者信息
                long timepp = System.currentTimeMillis();
                if ((timepp - timep) > 10) {
                    number++;
                    System.out.println(number + ": 第" + num + "次更新超时, authId=" + authWork.getAuthId() + ", 消耗" + (timepp - timep) + "ms...");
                }
                num++;
            }
            /*while(cursor.hasNext()){
                long timep = System.currentTimeMillis();
                DBObject obj = cursor.next();
                String json = JSON.toJSONString(obj);
                AuthWork authWork = JSON.parseObject(json, AuthWork.class);
                List<TempWorks> workList = authWork.getWorks();
                int works = workList.size();
                int hits = 0;
                int comments = 0;
                int recoms = 0;
                for(TempWorks w:workList){//获取作者作品总点击数，评论数，推荐数
                    hits+=w.getWorkTotalHits();
                    comments+=w.getWorkCommentsNum();
                    recoms+=w.getWorkTotalRecoms();
                }
                DBObject auth = new BasicDBObject();
                auth.put("authWorksNum", works);
                if(hits<0)
                    hits=-1;
                if(comments<0)
                    comments=-1;
                if(recoms<0)
                    recoms=-1;
                auth.put("authWorksHitsNum", hits);
                auth.put("authWorksCommentsNum", comments);
                auth.put("authWorksRecomsNum", recoms);
                DBObject update = new BasicDBObject("$set", auth);
                DBObject query = new BasicDBObject("authId", authWork.getAuthId());
                mongoTemplate.getCollection(collectionName).update(query, update);//更新作者信息
                long timepp = System.currentTimeMillis();
                if((timepp-timep)>10){
                    number++;
                    System.out.println(number+": 第"+num+"次更新超时, authId="+authWork.getAuthId()+", 消耗"+(timepp-timep)+"ms...");
                }
                num++;
            }*/
            long timeb = System.currentTimeMillis();
//        System.out.println("已更新"+page.getCurrentPage()*page.getPageSize()+"数据, 消耗"+(timebb-timeaa)/1000+"s");
            System.out.println(page.getCurrentPage() + "更新完成, 共有" + page.getCurrentPage() * page.getPageSize() + "数据, 消耗" + (timeb - timecc) + "ms");
            page.setCurrentPage(page.getCurrentPage() + 1);
            count--;
        }
        long time = System.currentTimeMillis();
        System.out.println("所有完成...消耗" + (time - timea) + "ms");
    }

    /**
     * 采用！！！应该采用spring定时器，next time done
     * <p>
     * java自带Mongo方法，不分页查询（更新20+w，耗时平均30s以内）
     */
    @Test
    public void update() {
        /*
        db.getCollection('works').aggregate([
            {$project:{"workAuthId":1, "workTotalHits":1, "workCommentsNum":1, "workTotalRecoms":1}},
            {$group:{_id:"$workAuthId", "authId":{$first:"$workAuthId"}, "works":{$push: "$$ROOT"}}}
            ], {allowDiskUse : true})
         */

        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("iLiterature");
        MongoCollection col = db.getCollection("works");
        System.out.println("已连接...");
        List<DBObject> list = this.getWorksByAggregateGroup();
        this.setCollectionName("works");

        long timec = System.currentTimeMillis();
        AggregateIterable iterable = col.aggregate(list).allowDiskUse(true);
        long timecc = System.currentTimeMillis();
        Iterator<Document> iterator = iterable.iterator();
        System.out.println("...查询完成, 共有" + col.count() + "数据, 消耗" + (timecc - timec) + "ms");

        this.setCollectionName("author");
        int num = 1;
        int number = 0;
        //MongoCollection.update()，较后者慢太多，每条更新均200+ms
        /*while(iterator.hasNext()){
            long timep = System.currentTimeMillis();
            Document obj = iterator.next();
            String json = JSON.toJSONString(obj);
            AuthWork authWork = JSON.parseObject(json, AuthWork.class);
            List<TempWorks> workList = authWork.getWorks();
            int works = workList.size();
            int hits = 0;
            int comments = 0;
            int recoms = 0;
            for(TempWorks w:workList){//获取作者作品总点击数，评论数，推荐数
                hits+=w.getWorkTotalHits();
                comments+=w.getWorkCommentsNum();
                recoms+=w.getWorkTotalRecoms();
            }
            Document auth = new Document();
            auth.put("authWorksNum", works);
            if(hits<0)
                hits=-1;
            if(comments<0)
                comments=-1;
            if(recoms<0)
                recoms=-1;
            auth.put("authWorksHitsNum", hits);
            auth.put("authWorksCommentsNum", comments);
            auth.put("authWorksRecomsNum", recoms);
            Document update = new Document("$set", auth);
            Document query = new Document("authId", authWork.getAuthId());
            col.updateOne(query, update);
            long timepp = System.currentTimeMillis();
            if((timepp-timep)>10){
                number++;
                System.out.println(number+": 第"+num+"次更新超时, authId="+authWork.getAuthId()+", 消耗"+(timepp-timep)+"ms...");
            }
            num++;
        }*/
        //mongoTemplate.update()
        while (iterator.hasNext()) {
            long timep = System.currentTimeMillis();
            Document obj = iterator.next();
            String json = JSON.toJSONString(obj);
            AuthWork authWork = JSON.parseObject(json, AuthWork.class);
            List<TempWorks> workList = authWork.getWorks();
            int works = workList.size();
            int hits = 0;
            int comments = 0;
            int recoms = 0;
            for (TempWorks w : workList) {//获取作者作品总点击数，评论数，推荐数
                hits += w.getWorkTotalHits();
                comments += w.getWorkCommentsNum();
                recoms += w.getWorkTotalRecoms();
            }
            DBObject auth = new BasicDBObject();
            auth.put("authWorksNum", works);
            if (hits < 0)
                hits = -1;
            if (comments < 0)
                comments = -1;
            if (recoms < 0)
                recoms = -1;
            auth.put("authWorksHitsNum", hits);
            auth.put("authWorksCommentsNum", comments);
            auth.put("authWorksRecomsNum", recoms);
            DBObject update = new BasicDBObject("$set", auth);
            DBObject query = new BasicDBObject("authId", authWork.getAuthId());
            mongoTemplate.getCollection(collectionName).update(query, update);//更新作者信息
            long timepp = System.currentTimeMillis();
            if ((timepp - timep) > 10) {
                number++;
                System.out.println(number + ": 第" + num + "次更新超时, authId=" + authWork.getAuthId() + ", 消耗" + (timepp - timep) + "ms...");
            }
            num++;
        }
        long timeb = System.currentTimeMillis();
        System.out.println("sum:" + num + ", 更新完成, " + collectionName + ":" + mongoTemplate.getCollection(collectionName).getCount() + ", 消耗" + (timeb - timecc) + "ms");
        long time = System.currentTimeMillis();
        System.out.println("所有完成...消耗" + (time - timec) + "ms");
    }

    @Test
    public void update2() {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("iLiterature");
        MongoCollection col = db.getCollection("works");
        System.out.println("已连接...");

        DBObject fields = new BasicDBObject();
        fields.put("workAuthId", 1);
        fields.put("workTotalHits", 1);
        fields.put("workCommentsNum", 1);
        fields.put("workTotalRecoms", 1);
        DBObject project = new BasicDBObject("$project", fields);
        Long timec = System.currentTimeMillis();
        MongoCursor cursor = col.aggregate(Lists.newArrayList(project)).iterator();
        Long timecc = System.currentTimeMillis();
        System.out.println("查询消耗：" + (timecc - timec));
        List<TempWorks> list = Lists.newArrayList();
        while (cursor.hasNext()) {
            String json = JSON.toJSONString(cursor.next());
            TempWorks work = JSON.parseObject(json, TempWorks.class);
            list.add(work);
        }
        Map<Integer, List<TempWorks>> authIdWithWorks = list.stream().collect(Collectors.groupingBy(TempWorks::getWorkAuthId));
        Long timea = System.currentTimeMillis();
        System.out.println("group消耗："+(timea-timecc));
        int number = 0;
        int size = 10000;
//        authIdWithWorks.forEach((authId, works)->{
        DBObject queryFields = new BasicDBObject("authWorksHitsNum", new BasicDBObject("$exists", false));
        DBCursor authorCursor = authorDao.getDBCollection().find(queryFields);
//        DBCursor authorCursor = mongoTemplate.getCollection(collectionName).find(queryFields);
        Long timeaa = System.currentTimeMillis();
        System.out.println("查询author消耗："+(timeaa-timea));
        List<TempAuthor> authorList = Lists.newArrayList();
        while(authorCursor.hasNext()){
            String json = JSON.toJSONString(authorCursor.next());
            TempAuthor author = JSON.parseObject(json, TempAuthor.class);
            authorList.add(author);
        }
        System.out.println("剩余author--"+authorList.size());
        for(TempAuthor author:authorList){
            if(authIdWithWorks.containsKey(author.getAuthId())){
                List<TempWorks> works = authIdWithWorks.get(author.getAuthId());
                DBObject auth = new BasicDBObject();
                auth.put("authWorksNum", works.size());
                Long timeb = System.currentTimeMillis();
                List<TempWorks> filterHitWorks = works.stream().filter(work -> work.getWorkTotalHits() > 0).collect(Collectors.toList());
                List<TempWorks> filterCommentWorks = works.stream().filter(work -> work.getWorkCommentsNum() > 0).collect(Collectors.toList());
                List<TempWorks> filterRecomWorks = works.stream().filter(work -> work.getWorkTotalRecoms() > 0).collect(Collectors.toList());
                Long hits = -1L;
                Long comments = -1L;
                Long recoms = -1L;
                if (!filterHitWorks.isEmpty()) {
                    hits = filterHitWorks.stream().collect(Collectors.summarizingInt(TempWorks::getWorkTotalHits)).getSum();
                }
                if (!filterCommentWorks.isEmpty()) {
                    comments = filterCommentWorks.stream().collect(Collectors.summarizingInt(TempWorks::getWorkCommentsNum)).getSum();
                }
                if (!filterRecomWorks.isEmpty()) {
                    recoms = filterRecomWorks.stream().collect(Collectors.summarizingInt(TempWorks::getWorkTotalRecoms)).getSum();
                }
                Long timebb = System.currentTimeMillis();
                auth.put("authWorksHitsNum", hits);
                auth.put("authWorksCommentsNum", comments);
                auth.put("authWorksRecomsNum", recoms);
                DBObject update = new BasicDBObject("$set", auth);

                //更新条件
                DBObject query = new BasicDBObject("authId", author.getAuthId());
                authorDao.getDBCollection().update(query, update);//更新作者信息
//            mongoTemplate.getCollection(collectionName).update(query, update);//更新作者信息
                long timepp = System.currentTimeMillis();
                if ((timepp - timebb) > 200) {
                    System.out.println("计算：authId=" + author.getAuthId() + ", 消耗" + (timebb - timeb) + "ms...");
                    System.out.println("更新时间：authId=" + author.getAuthId() + ", 消耗" + (timepp - timebb) + "ms...");
                }
                number++;
                if (number >= size) {
                    try {
                        System.out.println("休息一下..."+number);
                        number = 0;
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        System.out.println("休息完毕");
                    }
                }
            }else{
                System.out.println("无作品authorId:"+author.getAuthId());
                DBObject auth = new BasicDBObject();
                auth.put("authWorksNum", 0);
                auth.put("authWorksHitsNum", -1);
                auth.put("authWorksCommentsNum", -1);
                auth.put("authWorksRecomsNum", -1);
                DBObject update = new BasicDBObject("$set", auth);
                DBObject query = new BasicDBObject("authId", author.getAuthId());
                authorDao.getDBCollection().update(query, update);//更新作者信息
            }
        }
        Long time = System.currentTimeMillis();
        System.out.println("所有完成，消耗" + (time - timec) / 1000 + "s");
    }

    private List<DBObject> getWorksByAggregateGroup() {
        DBObject fields = new BasicDBObject();
        fields.put("workAuthId", 1);
        fields.put("workTotalHits", 1);
        fields.put("workCommentsNum", 1);
        fields.put("workTotalRecoms", 1);
        DBObject groupFields = new BasicDBObject();
        groupFields.put("_id", "$workAuthId");
        groupFields.put("authId", new BasicDBObject("$first", "$workAuthId"));
        groupFields.put("works", new BasicDBObject("$push", "$$ROOT"));
        DBObject project = new BasicDBObject("$project", fields);
        DBObject group = new BasicDBObject("$group", groupFields);
        List<DBObject> list = new ArrayList<DBObject>();
        list.add(project);
        list.add(group);
        return list;
    }


    /**
     * 比较java自带Mongo与Spring-data-Mongo方法比较，前者查询速度快！！！！
     * 使用java自身Mongo方法分页查询数据，再使用自身bulkWrite方法批量更新（超级慢，放弃）
     */
    @Test
    public void updateMany() {
        /*
        db.getCollection('works').aggregate([
            {$project:{"workAuthId":1, "workTotalHits":1, "workCommentsNum":1, "workTotalRecoms":1}},
            {$group:{_id:"$workAuthId", "authId":{$first:"$workAuthId"}, "works":{$push: "$$ROOT"}}}
            ], {allowDiskUse : true})
         */


        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("iLiterature");
        MongoCollection col = db.getCollection("works");

        this.setCollectionName("works");
        DBCollection collection = mongoTemplate.getCollection(collectionName);
        DBObject fields = new BasicDBObject();
        fields.put("workAuthId", 1);
        fields.put("workTotalHits", 1);
        fields.put("workCommentsNum", 1);
        fields.put("workTotalRecoms", 1);
        DBObject groupFields = new BasicDBObject();
        groupFields.put("_id", "$workAuthId");
//        groupFields.put("number", new BasicDBObject("$sum", 1));
        groupFields.put("authId", new BasicDBObject("$first", "$workAuthId"));
        groupFields.put("works", new BasicDBObject("$push", "$$ROOT"));
        DBObject project = new BasicDBObject("$project", fields);
        DBObject group = new BasicDBObject("$group", groupFields);
        long timea = System.currentTimeMillis();
        int pageSize = 50000;
        PageInfo page = new PageInfo(1, pageSize);
        page.setTotal((int) collection.getCount());
        int count = (int) collection.getCount() / pageSize + 1;
        DBObject limit = new BasicDBObject("$limit", page.getPageSize());
        while (count > 0) {
            DBObject skip = new BasicDBObject("$skip", page.getCurrentPage() > 1 ? (page.getCurrentPage() - 1) * page.getPageSize() : 0);
            List<DBObject> list = new ArrayList<DBObject>();
            list.add(project);
            list.add(group);
            list.add(skip);
            list.add(limit);
            this.setCollectionName("works");

            long timec = System.currentTimeMillis();
            AggregateIterable iterable = col.aggregate(list).allowDiskUse(true);
            long timecc = System.currentTimeMillis();
            Iterator<Document> iterator = iterable.iterator();
            System.out.println("...查询完成, 共有" + page.getCurrentPage() * page.getPageSize() + "数据, 消耗" + (timecc - timec) / 1000 + "s");
            List<AuthWork> authWorkList = new ArrayList<AuthWork>();
            while (iterator.hasNext()) {
                Document obj = iterator.next();
                String objStr = JSON.toJSONString(obj);
                AuthWork authWork = JSON.parseObject(objStr, AuthWork.class);
                authWorkList.add(authWork);
            }
            long timed = System.currentTimeMillis();
            bulkWriteUpdate(col, authWorkList);
            long timedd = System.currentTimeMillis();
            System.out.println("已更新" + page.getCurrentPage() * page.getPageSize() + "数据, 消耗" + (timedd - timed) / 1000 + "s");

            long timeb = System.currentTimeMillis();
            Cursor cursor = collection.aggregate(list, AggregationOptions.builder().allowDiskUse(true).build());
//        AggregationOutput output = mongoTemplate.getCollection(collectionName).aggregate(list);
            long timebb = System.currentTimeMillis();
            System.out.println(page.getCurrentPage() + "查询完成, 共有" + page.getCurrentPage() * page.getPageSize() + "数据, 消耗" + (timebb - timeb) / 1000 + "s");
            System.out.println();
            /*List<AuthWork> authWorkList = new ArrayList<AuthWork>();
            while (cursor.hasNext()){
                DBObject obj = cursor.next();
                String objStr = JSON.toJSONString(obj);
                AuthWork authWork = JSON.parseObject(objStr, AuthWork.class);
                authWorkList.add(authWork);
            }
            bulkWriteUpdate(col, authWorkList);
            */
            page.setCurrentPage(page.getCurrentPage() + 1);
            count--;
        }
        long timeaa = System.currentTimeMillis();
        System.out.println("所有完成...消耗" + (timeaa - timea) / 1000 + "s");
    }

    public void bulkWriteUpdate(MongoCollection col, List<AuthWork> authWorkList) {
        List<WriteModel<Document>> requests = new ArrayList<WriteModel<Document>>();
        for (AuthWork authWork : authWorkList) {
            //更新条件
            Document queryDocument = new Document("authId", authWork.getAuthId());
            int works = authWork.getWorks().size();
            int hits = 0;
            int comments = 0;
            int recoms = 0;
            for (TempWorks w : authWork.getWorks()) {
                hits += w.getWorkTotalHits();
                comments += w.getWorkCommentsNum();
                recoms += w.getWorkTotalRecoms();
            }
            Document auth = new Document();
            auth.put("authWorksNum", works);
            if (hits < 0)
                hits = -1;
            if (comments < 0)
                comments = -1;
            if (recoms < 0)
                recoms = -1;
            auth.put("authWorksHitsNum", hits);
            auth.put("authWorksCommentsNum", comments);
            auth.put("authWorksRecomsNum", recoms);
            Document updateDocument = new Document("$set", auth);
            //构造更新单个文档的操作模型
            UpdateOneModel<Document> uom = new UpdateOneModel<Document>(queryDocument, updateDocument, new UpdateOptions().upsert(false));
            //UpdateOptions代表批量更新操作未匹配到查询条件时的动作，默认false，什么都不干，true时表示将一个新的Document插入数据库，他是查询部分和更新部分的结合
            requests.add(uom);
        }
        System.out.println("add完成。。。");
        BulkWriteResult bulkWriteResult = col.bulkWrite(requests);
        System.out.println(bulkWriteResult.toString());
    }

    /**
     * spring-data-mongo； Aggregation
     */
    @Test
    public void updateAuthorData1() {
        /*
        db.getCollection('works').aggregate([
            {$project:{"workAuthId":1, "workTotalHits":1, "workCommentsNum":1, "workTotalRecoms":1}},
            {$group:{_id:"$workAuthId", "authId":{$first:"$workAuthId"}, "works":{$push: "$$ROOT"}}}
            ], {allowDiskUse : true})
         */
        TypedAggregation aggregation = Aggregation.newAggregation(AuthWork.class,
                Aggregation.project("workAuthId", "workTotalHits", "workCommentsNum", "workTotalRecoms"),
                Aggregation.group("workAuthId").first("workAuthId").as("authId").push("$$ROOT").as("works")

        );
        aggregation.newAggregationOptions().allowDiskUse(true);

        System.out.println("SQL:" + aggregation.toString());
        long timea = System.currentTimeMillis();
        this.setCollectionName("works");
        AggregationResults results = mongoTemplate.aggregate(aggregation, collectionName, AuthWork.class);
//        AggregationOutput output = mongoTemplate.getCollection(collectionName).aggregate(list);
        long timeb = System.currentTimeMillis();
        System.out.println("查询完成, 共有" + mongoTemplate.getCollection(collectionName).getCount() + "数据, 消耗" + (timeb - timea) / 1000 + "s");
        List<AuthWork> authWorkList = results.getMappedResults();

        this.setCollectionName("author");
        for (AuthWork work : authWorkList) {//获取作者作品总点击数，评论数，推荐数
            int works = work.getWorks().size();
            int hits = 0;
            int comments = 0;
            int recoms = 0;
            for (TempWorks w : work.getWorks()) {
                hits += w.getWorkTotalHits();
                comments += w.getWorkCommentsNum();
                recoms += w.getWorkTotalRecoms();
            }
            DBObject auth = new BasicDBObject();
            auth.put("authWorksNum", works);
            if (hits < 0)
                hits = -1;
            if (comments < 0)
                comments = -1;
            if (recoms < 0)
                recoms = -1;
            auth.put("authWorksHitsNum", hits);
            auth.put("authWorksCommentsNum", comments);
            auth.put("authWorksRecomsNum", recoms);
            DBObject update = new BasicDBObject("$set", auth);
            DBObject query = new BasicDBObject("authId", work.getAuthId());
            mongoTemplate.getCollection(collectionName).update(query, update);//更新作者信息
        }
        long timebb = System.currentTimeMillis();
//        System.out.println("已更新"+page.getCurrentPage()*page.getPageSize()+"数据, 消耗"+(timebb-timeaa)/1000+"s");
        System.out.println("已更新完成, 消耗" + (timebb - timeb) / 1000 + "s");
    }
}
