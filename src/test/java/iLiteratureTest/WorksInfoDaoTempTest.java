package iLiteratureTest;

import com.swust.kelab.mongo.dao.WorksInfoDaoTemp;
import com.swust.kelab.mongo.domain.TempWorks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zengdan on 2017/5/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/dao.xml", "classpath:mongodb/mongodb-config.xml"})
public class WorksInfoDaoTempTest {
    @Resource
    private WorksInfoDaoTemp worksInfoDao;

    @Test
    public void selectSortedWorksTest(){
        List<TempWorks> workList = worksInfoDao.selectSortedWorks(0, 1, -1, 10000);
        System.out.println(workList);
    }
}
