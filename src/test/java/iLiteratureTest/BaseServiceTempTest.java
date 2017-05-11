package iLiteratureTest;

import com.swust.kelab.mongo.service.BaseServiceTemp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zengdan on 2017/5/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml",  "classpath:spring/dao.xml", "classpath:mongodb/mongodb-config.xml"})
public class BaseServiceTempTest {
    @Resource
    private BaseServiceTemp baseService;

    @Test
    public void countInfoTest(){
        Map<String, Long> map = baseService.countInfo();
        System.out.println(map);
    }
}
