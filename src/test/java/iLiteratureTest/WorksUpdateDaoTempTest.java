package iLiteratureTest;

import com.swust.kelab.mongo.dao.WorksUpdateDaoTemp;
import com.swust.kelab.mongo.domain.TempWorksUpdate;
import com.swust.kelab.mongo.domain.model.Area;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zengdan on 2017/5/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:mongodb/mongodb-config.xml"})
public class WorksUpdateDaoTempTest {
    @Resource
    private WorksUpdateDaoTemp worksUpdateDao;

    @Test
    public void selectSomeWorkUpdateTest(){
        Integer limitNum=3;
        List<TempWorksUpdate> worksUpdateList = worksUpdateDao.selectSomeWorkUpdate(limitNum);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        worksUpdateList.forEach(worksUpdate->{
            try {
                worksUpdate.setWoupUpdateTime(sdf.parse(sdf.format(worksUpdate.getWoupUpdateTime())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println(worksUpdateList);
    }

    @Test
    public void selectRecentWorkUpdateTest(){
        Integer dayNum = 5;
        // worksupdate存储的时间需要修改
        List<Area> dayOfWorksUpdateCountList = worksUpdateDao.selectRecentWorkUpdate(dayNum);
        System.out.println(dayOfWorksUpdateCountList);
    }

    @Test
    public void selectRecentWorkUpdate2Test(){
        Integer dayNum = 7;
        List<Area> areaList = worksUpdateDao.selectRecentWorkUpdate2(dayNum);
        System.out.println(areaList);
    }
}
