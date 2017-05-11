package iLiteratureTest;

import com.swust.kelab.domain.WorkDescription;
import com.swust.kelab.mongo.service.WorksInfoServiceTemp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zengdan on 2017/5/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/dao.xml", "classpath:mongodb/mongodb-config.xml"})
public class WorksInfoServiceTempTest {
    @Resource
    private WorksInfoServiceTemp worksInfoService;

    @Test
    public void selectWorksDescByAuthIdTest(){
        // 查看消耗时间比例
        List<WorkDescription> list1 = worksInfoService.selectWorksDescByAuthId(98676);
        System.out.println("===================");
        List<WorkDescription> list2 = worksInfoService.selectWorksDescByAuthId(97232);
    }
}
