package iLiteratureTest;

import com.google.common.collect.Lists;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.service.AuthorServiceTemp;
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
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:mongodb/mongodb-config.xml"})
public class AuthorControllerTest {
    @Resource
    private AuthorServiceTemp authorService;
    @Test
    public void countAuthorGender(){
        Map<String, Integer> result = authorService.countInfoGender(null);
        System.out.println(result);
    }

    @Test
    public void countAuthorArea(){
        Map<String, Integer> map = authorService.countInfoArea(null);
        List<Area> list = Lists.newArrayList();
        for(Map.Entry<String, Integer> entry:map.entrySet()){
            Area area = new Area(entry.getKey(), entry.getValue());
            list.add(area);
        }
        System.out.println(list);
    }
}
