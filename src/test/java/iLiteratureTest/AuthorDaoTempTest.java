package iLiteratureTest;

import com.swust.kelab.mongo.dao.AuthorDaoTemp;
import com.swust.kelab.web.model.EPOQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by zengdan on 2017/5/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:mongodb/mongodb-config.xml"})
public class AuthorDaoTempTest {
    @Resource
    private AuthorDaoTemp authorDao;

    @Test
    public void viewAllAuthorTest(){
        EPOQuery query = new EPOQuery();
        query.setSearchWord("李");
        authorDao.viewAuthorByPage(query, 0, 1, 1, null);
    }
}
