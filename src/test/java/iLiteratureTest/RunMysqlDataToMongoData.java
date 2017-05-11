package iLiteratureTest;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.swust.kelab.repos.bean.GenericQuery;
import org.bson.Document;

import java.sql.*;
import java.util.List;

/**
 * Created by zengdan on 2017/5/8.
 */
public class RunMysqlDataToMongoData {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/iliterature";
    private static final String NAME = "com.mysql.cj.jdbc.Driver";
    private static final String USER = "root";
    private static final String PASSWORD = "cs.swust";
    private static Connection mysqlConn = null;
    private static PreparedStatement pst = null;

    private static final String DATABASENAME = "iLiterature";
    private static MongoCollection collection = null;
    private static String tableName;
    private static String collectionName;

    private static MongoCollection getMongoConn(String databaseName) {
        MongoClient mongoClient = null;
        if (mongoClient == null) {
            mongoClient = new MongoClient("127.0.0.1", 27017);
        }
        MongoDatabase mongoConn = mongoClient.getDatabase(databaseName);
        collection = mongoConn.getCollection(RunMysqlDataToMongoData.collectionName);
        return collection;
    }

    //获取mysql连接
    private static Connection getMysqlConn() {
        try {
            Class.forName(NAME);//指定连接类型
            if (mysqlConn == null) {
                mysqlConn = DriverManager.getConnection(URL, USER, PASSWORD);//获取连接
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mysqlConn;
    }

    public void close() {
        try {
            this.mysqlConn.close();
            this.pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setInfo(String tableName, String collectionName) {
        this.tableName = tableName;
        this.collectionName = collectionName;
    }

    private static void init(){
        RunMysqlDataToMongoData.getMongoConn(DATABASENAME);
        RunMysqlDataToMongoData.getMysqlConn();
    }

    public static void main(String[] args) {
        RunMysqlDataToMongoData mysqlDataToMongoData = new RunMysqlDataToMongoData();
        mysqlDataToMongoData.setInfo(args[0], args[1]);
        RunMysqlDataToMongoData.init(); //初始化数据库连接
        try {
            // 查询mysql数据
            String countSql = "select count(1) from "+RunMysqlDataToMongoData.tableName;
            RunMysqlDataToMongoData.pst = RunMysqlDataToMongoData.mysqlConn.prepareStatement(countSql);
            // 分页查询
            int pageSize = 100000;
            ResultSet resultSet = RunMysqlDataToMongoData.pst.executeQuery();
            int rows = resultSet.getRow();
            int count = rows / pageSize;
            System.out.println("rows:" + rows + ", count:" + count);
            if (count > 0) {
                int startIndex = 0;
                int i = 0;
                while (i <= count) {
                    long timea = System.currentTimeMillis();
                    startIndex = (rows - startIndex) > pageSize ? i * pageSize : rows;
                    //分段查询
                    String dataSql = "select * from "+RunMysqlDataToMongoData.tableName+" limit "+startIndex+","+pageSize;
                    ResultSet datasSet = RunMysqlDataToMongoData.mysqlConn.prepareStatement(dataSql).executeQuery();
                    // TODO 未完成
                }
            }
        } catch (SQLException e) {
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
    /*public void mysql2Mongo() {
        try {
//            this.setInfo("author", "tempAuthor");
//            this.setInfo("authorupdate", "tempAuthorUpdate");
//            this.setInfo("works", "tempWorks");
//            this.setInfo("worksupdate", "tempWorksUpdate");
            this.setInfo("workscomment", "tempWorksComment");
//            this.setInfo("column", "tempColumn");
//            this.setInfo("crawlwebsite", "tempCrawlWebsite");
//            this.setInfo("systemparameter", "tempSystemParameter");
//            this.setInfo("user", "tempUser");
//            this.setInfo("website", "tempWebsite");

            //获取mongodb连接
            MongoDatabase database = getMongoConn(DATABASENAME);
            MongoCollection collection = database.getCollection(collectionName);

            //mybatis查询mysql数据
            int pageSize = 100000;
            //获取记录条数
            GenericQuery query = new GenericQuery();
            int rows = mysqlDao.selectAllCount(space, query);
            int count = rows / pageSize;
            System.out.println("rows:" + rows + ", count:" + count);
            if (count > 0) {
                int startIndex = 0;
                int i = 0;
                while (i <= count) {
                    long timea = System.currentTimeMillis();
                    startIndex = (rows - startIndex) > pageSize ? i * pageSize : rows;
                    //分段查询
                    query = new GenericQuery();
                    query.put("startIndex", startIndex);
                    query.put("pageSize", pageSize);
                    List list = mysqlDao.selectAllData(space, query);
                    long timeaa = System.currentTimeMillis();
                    System.out.println("查询消耗：" + (timeaa - timea) + "ms");
                    //存入数据到mongo
                    collection.insertMany(saveTemp(list));
                    System.out.println("第 " + i + " 次save success");
                    i++;
                    long timeb = System.currentTimeMillis();
                    System.out.println("第 " + (i - 1) + " 次save cost time=" + (timeb - timeaa) + "ms");
                }
            } else {
                List list = mysqlDao.selectAllData(space, query);
                collection.insertMany(saveTemp(list));
                System.out.println("save success");
            }
            System.out.println("all success...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
