package iLiteratureTest;

import com.google.common.collect.Lists;
import com.swust.kelab.mongo.dao.base.PageResult;
import com.swust.kelab.mongo.dao.query.AuthorQuery;
import com.swust.kelab.mongo.domain.TempAuthor;
import com.swust.kelab.mongo.domain.TempAuthorUpdate;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.domain.vo.TempAuthorVo;
import com.swust.kelab.mongo.service.AuthorServiceTemp;
import com.swust.kelab.web.model.EPOQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by zengdan on 2017/2/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/service.xml", "classpath:spring/dao.xml", "classpath:mongodb/mongodb-config.xml"})
public class AuthorControllerTest {
    @Resource
    private AuthorServiceTemp authorService;

    @Test
    public void selectHotTopAuthorTest(){
        List<TempAuthor> result = authorService.selectHotTopAuthor(0, 1, -1, 10);
        System.out.println(result);
    }

    @Test
    public void viewAuthorByPageTest(){
        authorService.viewAuthorByPage(new EPOQuery(), 0, 1, -1, null);
    }

    @Test
    public void viewAuthorByIdTest(){
        Integer authorId = 97056;
        TempAuthorVo author = authorService.selectAuthorById(authorId);
        System.out.println(author);
    }

    @Test
    public void countAuthorInfoNumAllTest(){
        String hitsRange = "0,100000,500000,1000000,2000000,3000000,5000000,7000000,9000000,10000000";
        String commentsRange = "1,5,10,20,30,40,50,100,200,500";
        String recomsRange = "1,1000,5000,10000,20000,30000,50000,70000,90000,100000";
        String worksRange = "0,1,2,3,4,5,6";
        Map<String, List<Area>> map = authorService.countAuthorInfoNum(hitsRange, commentsRange, recomsRange, worksRange, 0);
        System.out.println(map);
    }

    @Test
    public void viewAuthorUpdateTest(){
        Integer authId = 97056;
        TempAuthorUpdate result = authorService.selectAuthorUpdateByAuthId(authId);
        System.out.println(result);
    }

    @Test
    public void countAuthorGender(){
        Map<String, Integer> result = authorService.countInfoGender(0);
        System.out.println(result);
    }

    @Test
    public void countAuthorArea(){
        Map<String, Integer> map = authorService.countInfoArea(0);
        System.out.println(map);
    }
}
