package iLiteratureTest;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.model.DBCollectionUpdateOptions;
import com.swust.kelab.domain.Site;
import com.swust.kelab.mongo.dao.AuthorDaoTemp;
import com.swust.kelab.mongo.dao.SiteDaoTemp;
import com.swust.kelab.mongo.dao.WorksInfoDaoTemp;
import com.swust.kelab.mongo.service.SiteServiceTemp;
import com.swust.kelab.service.web.SiteService;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.PageData;
import com.swust.kelab.web.model.QueryData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zengdan on 2017/5/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/dao.xml", "classpath:mongodb/mongodb-config.xml"})
public class SiteServiceTempTest {
    @Resource
    private SiteServiceTemp siteServiceTemp;
    @Resource
    private SiteDaoTemp siteDaoTemp;
    @Resource
    private AuthorDaoTemp authorDao;
    @Resource
    private WorksInfoDaoTemp worksInfoDao;

    @Test
    public void viewAuthorAndWorkNumTest() {
        EPOQuery iQuery = new EPOQuery();
        iQuery.setRecordPerPage(20);
        QueryData queryData = siteServiceTemp.viewSiteWithAuthorAndWorkCountByPage(iQuery);

        System.out.println(queryData);
    }

    @Test
    public void update(){
        EPOQuery iQuery = new EPOQuery();
        iQuery.setRecordPerPage(20);
        QueryData queryData = siteServiceTemp.viewAuthorAndWorkNum(iQuery);

        System.out.println("开始更新....");
        // crawlwebsite添加字段
        List<PageData> pageData = queryData.getPageData();
        pageData.forEach(page -> {
            List<Site> sites = (List<Site>) page.getData();
            sites.forEach(site -> {
                DBObject queryField = new BasicDBObject("crwsId", site.getSiteId());
                // 更新时间
                String lastTime = site.getAuthorUpdate();
                DBObject update = new BasicDBObject("$set", new BasicDBObject("crwsAuthorOrWorkUpdateTime", lastTime));

                // 更新作者作品数量
                /*DBObject authQuery = new BasicDBObject("authWebsiteId", site.getSiteId());
                int authorCount = authorDao.getDBCollection().find(authQuery).count();
                DBObject workQuery = new BasicDBObject("workWebsiteId", site.getSiteId());
                int workCount = worksInfoDao.getDBCollection().find(workQuery).count();
                DBObject update = new BasicDBObject("$set", new BasicDBObject("crwsTotalAuthorNum", authorCount).append("crwsTotalWorkNum", workCount));
                */
                siteDaoTemp.getDBCollection().update(queryField, update, new DBCollectionUpdateOptions().upsert(true));
            });
        });
        System.out.println(queryData);
    }
}
