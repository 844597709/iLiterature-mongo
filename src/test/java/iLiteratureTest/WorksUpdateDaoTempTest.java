package iLiteratureTest;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.swust.kelab.mongo.dao.WorksInfoDaoTemp;
import com.swust.kelab.mongo.dao.WorksUpdateDaoTemp;
import com.swust.kelab.mongo.domain.TempWorksUpdate;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.utils.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zengdan on 2017/5/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/dao.xml", "classpath:mongodb/mongodb-config.xml"})
public class WorksUpdateDaoTempTest {
    @Resource
    private WorksUpdateDaoTemp worksUpdateDao;
    @Resource
    private WorksInfoDaoTemp worksInfoDao;

    @Test
    public void selectSomeWorkUpdateTest() {
        Integer limitNum = 3;
        List<TempWorksUpdate> worksUpdateList = worksUpdateDao.selectSomeWorkUpdate(limitNum);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        worksUpdateList.forEach(worksUpdate -> {
            try {
                worksUpdate.setWoupUpdateTime(sdf.parse(sdf.format(worksUpdate.getWoupUpdateTime())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println(worksUpdateList);
    }

    @Test
    public void selectRecentWorkUpdateTest() {
        Integer dayNum = 10;
        // worksupdate存储的时间需要修改
        List<Area> dayOfWorksUpdateCountList = worksUpdateDao.selectRecentWorkUpdate(dayNum);
        System.out.println(dayOfWorksUpdateCountList);
    }

    @Test
    public void selectRecentWorkUpdate2Test() {
        Integer dayNum = 10;
        List<Area> areaList = worksUpdateDao.selectRecentWorkUpdate2(dayNum);
        System.out.println(areaList);
    }

    @Test
    public void selectWorkUpdateByTimeTest() {
        String startTime = "2016-12-03";
        Integer dayNum = 10;
        if (StringUtils.isEmpty(startTime)) {
            return;
        }
        try {
            List<Integer> times = Arrays.stream(startTime.split("-")).map(time -> Integer.parseInt(time)).collect(Collectors.toList());
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime(sdf.parse(startTime));
            String beforeTime = sdf.format(calendar.getTime());
            System.out.println("calendar-before-time:" + beforeTime);
            calendar.add(Calendar.DATE, dayNum);
            String afterTime = sdf.format(calendar.getTime());
            System.out.println("calendar-after-time:" + afterTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        DBObject site = new BasicDBObject("workWebsiteId", 1);
        DBObject field = new BasicDBObject("workId", 1);
        DBCursor cursor = worksInfoDao.getDBCollection().find(site, field);
        List<Integer> list = Lists.newArrayList();
        while(cursor.hasNext()){
            list.add((Integer) cursor.next().get("workId"));
        }
        System.out.println(list);
    }
}
