package iLiteratureTest;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.swust.kelab.mongo.domain.TempWorksUpdate;
import com.swust.kelab.repos.MysqlDao;
import com.swust.kelab.repos.bean.GenericQuery;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by zd on 16/10/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/dao.xml", "classpath:mongodb/mongodb-config.xml"})
public class MysqlToMongo {
    @Resource
    private MysqlDao mysqlDao;
//    private static RiskBusinessDao riskBusinessDao = new RiskBusinessDaoImpl();
    public static final String url = "jdbc:mysql://127.0.0.1:3306/iliterature_new";
    public static final String name = "com.mysql.cj.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "150801";

    public static java.sql.Connection mysqlConn = null;
    public static java.sql.PreparedStatement pst = null;

    public List resultSetToList(ResultSet rs){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try{
            if (rs == null)
                return Collections.EMPTY_LIST;
            ResultSetMetaData md = rs.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等
            int columnCount = md.getColumnCount(); //返回此 ResultSet 对象中的列数
            Map<String, Object> rowData = new HashMap<String, Object>();
            while (rs.next()) {
                rowData = new HashMap<String, Object>(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), rs.getObject(i));
                }
                list.add(rowData);
//            System.out.println("list:" + list.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    private void save(MongoCollection collection, ResultSet result){
        List<Map<String, Object>> ls = resultSetToList(result);
        Iterator it = ls.iterator();
        while(it.hasNext()) {
            Map<String, Object> hm = (HashMap<String, Object>)it.next();
//            DBObject object = new BasicDBObject();
            Document document = new Document();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for(String key:hm.keySet()){
                Object value = hm.get(key);
                if (value!=null && !"".equals(value) && ("auth_inTime".equals(key) || "auup_time".equals(key) || "user_loginTime".equals(key)
                        || "work_inTime".equals(key) || "woup_time".equals(key)
                        || "woup_updateTime".equals(key) || "woco_Time".equals(key))) {
                    value = simpleDateFormat.format(value);
                }
//                object.put(key, value);
                document.put(key, value);
            }
            collection.insertOne(document);
        }
    }

    //获取mongoConn
    private MongoDatabase getMongoConn(String databaseName){
        MongoDatabase mongoConn = null;
        MongoClient mongoClient=null;
        if(mongoClient == null){
            mongoClient = new MongoClient("127.0.0.1", 27017 );
        }
        mongoConn = mongoClient.getDatabase(databaseName);
        return mongoConn;
//        MongoCollection collection = mongoConn.getCollection("authorupdate");
    }

    //获取mysql连接
    private Connection getMysqlConn(){
        try{
            Class.forName(name);//指定连接类型
            if(mysqlConn==null){
                mysqlConn = DriverManager.getConnection(url, user, password);//获取连接
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  mysqlConn;
    }

    //获取mysql所有数据
    private ResultSet getMysqlAllData(Connection mysqlConn, String tableName, int pageSize){
        ResultSet result=null;
        try{
            /*Class.forName(name);//指定连接类型
            mysqlConn = DriverManager.getConnection(url, user, password);//获取连接
            System.out.println("mysql成功连接...");*/
            //不分页查询
            String sql = "select * from "+tableName;
            pst = mysqlConn.prepareStatement(sql);//准备执行语句
            result = pst.executeQuery();

            /*result.last();//移到最后一行
            int rows=result.getRow();*/
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //分页获取mysql数据
    private ResultSet getMysqlSomeData(Connection mysqlConn, String tableName, int start, int pageSize){
        ResultSet result=null;
        try{
            //分页查询
            String sql="select * from authorupdate limit "+start+", "+pageSize;
            pst = mysqlConn.prepareStatement(sql);//准备执行语句
            result = pst.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //数据太多了不适合！会报java heap或者GC overhead错误！！！
    private void table2table(){
        try{
            String databaseName="iLiterature";
            String collectionName="authorupdate";
            //获取mongodb连接
            MongoDatabase database = getMongoConn(databaseName);
            MongoCollection collection = database.getCollection(collectionName);

            //获取mysql连接
            Connection mysqlConn = getMysqlConn();
            String tableName=collectionName;
            int pageSize=100000;
            ResultSet resultSet = getMysqlAllData(mysqlConn, tableName, pageSize);
            resultSet.last();//移到最后一行
            int rows=resultSet.getRow();
            int count=rows/pageSize;
            resultSet.beforeFirst();//移到第一行
            if(count>0){
                int start=0;
                int i=0;
                while(i<=count){
                    long timea = System.currentTimeMillis();
                    start = (rows-start)>pageSize?i*pageSize:rows;
                    resultSet = getMysqlSomeData(mysqlConn, tableName, start, pageSize);
                    save(collection, resultSet);
                    System.out.println("第 "+i+" 次save success");
                    i++;
                    if(i!=0 && i%5==0){
                        System.out.println("休息一下...");
                        Thread.sleep(5000);
                        System.out.println("继续工作...");
                    }
                    long timeb = System.currentTimeMillis();
                    System.out.println("第 "+(i-1)+" 次cost time="+(timeb-timea)/1000+"s");
                }
            }else{
                save(collection, resultSet);
                System.out.println("save success");
            }
            System.out.println("all success...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private List<Document> saveTemp(List<Object> list){
        List<Document> documents = Lists.newArrayList();
        long timea = System.currentTimeMillis();
        for(Object obj:list){
            Gson gson = new Gson();
            String auupStr = gson.toJson(obj);
//            String auupStr = JSON.toJSONString(obj);//JSON--alibaba
            Document document = Document.parse(auupStr);
            documents.add(document);
        }
        long timeaa = System.currentTimeMillis();
        System.out.println("getList 耗时："+(timeaa-timea)+"ms");
        return documents;
    }

    @Test
    public void time() throws ParseException {
        long time = 1440596548000L;
//        Calendar calendar = Calendar.getInstance();
        Date date = new Date(time);
//        calendar.setTime(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        System.out.println("date:"+dateStr);

        String str = "2015-08-26 13:42:29";
        date = simpleDateFormat.parse(str);
        System.out.println("time:"+date.getTime());
    }

    @Test
    public void mysql2Mongo(){
        try{
            String databaseName="iLiterature";

//            String collectionName="author";
//            String space="tempAuthor";

//            String collectionName="authorupdate";
//            String space="tempAuthorUpdate";

//            String collectionName="works";
//            String space="tempWorks";

//            String collectionName="worksupdate";
            String collectionName="worksupdatetemp";
            String space="tempWorksUpdate";

//            String collectionName="workscomment";
//            String space="tempWorksComment";

//            String collectionName="column";
//            String space="tempColumn";

//            String collectionName="crawlwebsite";
//            String space="tempCrawlWebsite";

//            String collectionName="systemparameter";
//            String space="tempSystemParameter";

//            String collectionName="user";
//            String space="tempUser";

//            String collectionName="website";
//            String space="tempWebsite";
            //获取mongodb连接
            MongoDatabase database = getMongoConn(databaseName);
            MongoCollection collection = database.getCollection(collectionName);

            //mybatis查询mysql数据
            int pageSize=100000;
            //获取记录条数
            GenericQuery query = new GenericQuery();
            int rows = mysqlDao.selectAllCount(space, query);
            int count = rows/pageSize;
            System.out.println("rows:"+rows+", count:"+count);
            if(count>0){
                int startIndex=0;
                int i=0;
                while(i<=count){
                    long timea = System.currentTimeMillis();
                    startIndex = (rows-startIndex)>pageSize?i*pageSize:rows;
                    //分段查询
                    query = new GenericQuery();
                    query.put("startIndex", startIndex);
                    query.put("pageSize", pageSize);
                    List list = mysqlDao.selectAllData(space, query);
                    long timeaa = System.currentTimeMillis();
                    System.out.println("查询消耗："+(timeaa-timea)+"ms");
                    //存入数据到mongo
                    collection.insertMany(saveTemp(list));
                    System.out.println("第 "+i+" 次save success");
                    i++;
                    long timeb = System.currentTimeMillis();
                    System.out.println("第 "+(i-1)+" 次save cost time="+(timeb-timeaa)+"ms");
                }
            }else{
                List list = mysqlDao.selectAllData(space, query);
                collection.insertMany(saveTemp(list));
                System.out.println("save success");
            }
            System.out.println("all success...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Resource
    private MongoTemplate mongoTemplate;
    private String collectionName="worksupdate";
    private String space="tempWorksUpdate";

    @Test
    public void saveToMongo() throws Exception{
        //mybatis查询mysql数据
//        int pageSize=1000;
        int pageSize=100000;
        //获取记录条数
        GenericQuery query = new GenericQuery();
        int rows = mysqlDao.selectAllCount(space, query);
//        int pageSize = rows;
        int count = rows/pageSize;
        System.out.println("rows:"+rows+", count:"+count);
        if(count>0){
            int startIndex=0;
            int i=0;
//            if(i<=count){
            while(i<=count){
                long  timea = System.currentTimeMillis();
                startIndex = (rows-startIndex)>pageSize?i*pageSize:rows;
                //分段查询
                query = new GenericQuery();
                query.put("startIndex", startIndex);
                query.put("pageSize", pageSize);
                List list = mysqlDao.selectAllData(space, query);
                long timeaa = System.currentTimeMillis();
                System.out.println("查询消耗："+(timeaa-timea)+"ms");
                //存入数据到mongo
                mongoTemplate.getCollection (collectionName).insert(getList(list));
                System.out.println("第 "+i+" 次save success");
                i++;
                long timeb = System.currentTimeMillis();
                System.out.println("第 "+(i-1)+" 次save cost time="+(timeb-timeaa)+"ms");
            }
        }else{
            long timea = System.currentTimeMillis();
            List list = mysqlDao.selectAllData(space, query);
            long timeaa = System.currentTimeMillis();
            System.out.println("查询消耗："+(timeaa-timea)+"ms");
            mongoTemplate.getCollection(collectionName).insert(getList(list));
            long timeb = System.currentTimeMillis();
            System.out.println("save消耗："+(timeb-timeaa)+"ms");
            System.out.println("save success");
        }
        System.out.println("all success...");
    }

    private List<DBObject> getList(List<Object> list){
        long timea = System.currentTimeMillis();
        List dbList = Lists.newArrayList();
        for(Object obj:list){
            Gson gson=new Gson();
            DBObject dbObject = (DBObject)JSON.parse(gson.toJson(obj));//JSON---mongodb.util
            dbList.add(dbObject);
        }
        long timeaa = System.currentTimeMillis();
        System.out.println("getList 耗时："+(timeaa-timea)+"ms");
        return dbList;
    }

    public static void main(String[] args) {
        List list = Lists.newArrayList();
        TempWorksUpdate worksUpdate = new TempWorksUpdate();
        worksUpdate.setWoupId(1);
        worksUpdate.setWoupTotalHits(1000);
        worksUpdate.setWoupTotalRecoms(500);
        list.add(worksUpdate);
        List dbList= new MysqlToMongo().getList(list);
        System.out.println(dbList);
    }
}
