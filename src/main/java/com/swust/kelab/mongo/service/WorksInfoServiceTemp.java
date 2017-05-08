package com.swust.kelab.mongo.service;

import com.google.common.collect.Lists;
import com.swust.kelab.mongo.dao.AuthorUpdateDaoTemp;
import com.swust.kelab.mongo.dao.WorksInfoDaoTemp;
import com.swust.kelab.mongo.dao.WorksUpdateDaoTemp;
import com.swust.kelab.mongo.dao.base.PageResult;
import com.swust.kelab.mongo.dao.query.BaseQuery;
import com.swust.kelab.mongo.domain.TempAuthor;
import com.swust.kelab.mongo.domain.TempWorks;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.utils.CollectionUtil;
import com.swust.kelab.web.model.AuthorWorkUpdate;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.QueryData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(value = "worksInfoService")
public class WorksInfoServiceTemp {
    @Resource
    private WorksInfoDaoTemp worksInfoDao;
    @Resource
    private WorksUpdateDaoTemp worksUpdateDao;
    @Resource
    private AuthorUpdateDaoTemp authorUpdateDao;

    public List<Area> selectWorkType(Integer siteId, Integer topNum) {
        Integer allValidWorkCount = worksInfoDao.selectValidWorkCount(siteId);
        List<Area> results = worksInfoDao.selectWorkType(siteId, topNum);
        Integer topWorkCount = results.stream().collect(Collectors.summingInt(Area::getValue));
        results.add(new Area("其他类型", allValidWorkCount - topWorkCount));
        return results;
    }

    public List<TempWorks> selectHotTopWork(Integer siteId, Integer field, Integer descOrAsc, Integer topNum) {
        List<TempWorks> workList = worksInfoDao.selectHotTopWork(siteId, field, descOrAsc, topNum);
        return workList;
    }

    public List<AuthorWorkUpdate> selectWorkUpdateByTime(Integer dayNum) {
        List<Area> workUpdateList = worksUpdateDao.selectRecentWorkUpdate(dayNum);
        List<Area> authorUpdateList = authorUpdateDao.selectRecentAuthorUpdate(dayNum);
        List<AuthorWorkUpdate> list = Lists.newArrayList();
        Map<String, Integer> workUpdateMap = workUpdateList.stream().collect(Collectors.toMap(Area::getName, Area::getValue));
        Map<String, Integer> authorUpdateMap = authorUpdateList.stream().collect(Collectors.toMap(Area::getName, Area::getValue));
        workUpdateMap.forEach((time, count) -> {
            AuthorWorkUpdate authorWorkUpdate = new AuthorWorkUpdate(authorUpdateMap.get(time), count, time);
            list.add(authorWorkUpdate);
        });
        return list;
    }

    /**
     * 分页查询作品信息
     *
     * @throws Exception
     */
    public QueryData viewWorkByPage(EPOQuery iQuery, Integer siteId, Integer sortField, Integer descOrAsc) {
        Integer tag = 1;
        if (descOrAsc == 0) {
            tag = 1;
        } else {
            tag = -1;
        }
        QueryData queryData = worksInfoDao.viewWorkByPage(iQuery, siteId, sortField, tag);
        return queryData;
    }

    public Map<String, Object> countInfoNumAll(String hitsRange, String commentsRange, String recomsRange, Integer siteId, Integer descOrAsc)
            throws Exception {
        //获取所有，不排序
        List<TempWorks> allWorkList = worksInfoDao.selectHotTopWork(siteId, 0, descOrAsc, Integer.MAX_VALUE);
        if(allWorkList.isEmpty()){
            return CollectionUtil.emptyMap();
        }
        List<TempWorks> sortedWorkByHitsNum = allWorkList.stream().sorted((o1, o2) -> o1.getWorkTotalHits().compareTo(o2.getWorkTotalHits())).collect(Collectors.toList());
        List<TempWorks> sortedWorkByCommentsNum = allWorkList.stream().sorted((o1, o2) -> o1.getWorkCommentsNum().compareTo(o2.getWorkCommentsNum())).collect(Collectors.toList());
        List<TempWorks> sortedWorkByRecomsNum = allWorkList.stream().sorted((o1, o2) -> o1.getWorkTotalRecoms().compareTo(o2.getWorkTotalRecoms())).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("totalHits", this.getHitsRangeWithAuthorCount(1, hitsRange, sortedWorkByHitsNum));
        map.put("commentsNum", this.getHitsRangeWithAuthorCount(2, commentsRange, sortedWorkByCommentsNum));
        map.put("totalRecoms", this.getHitsRangeWithAuthorCount(3, recomsRange, sortedWorkByRecomsNum));
        return map;
    }

    private List<Area> getHitsRangeWithAuthorCount(Integer field, String rangeStr, List<TempWorks> sortWorkList){
        List<Long> ranges = this.getRanges(rangeStr);
        List<Area> list = Lists.newArrayList();
        for(Long range:ranges){
            List<TempWorks> rangeWorkList = Lists.newArrayList();
            Integer num = 0;
            for(int i=0; i<sortWorkList.size(); i++){
                if(field == 1){
                    num = sortWorkList.get(i).getWorkTotalHits();
                }else if(field == 2){
                    num = sortWorkList.get(i).getWorkCommentsNum();
                }else if(field == 3){
                    num = sortWorkList.get(i).getWorkTotalRecoms();
                }
                if(num>range){
                    rangeWorkList = sortWorkList.subList(0, i);
                    sortWorkList = sortWorkList.subList(i, sortWorkList.size());
                    break;
                }
            }
            list.add(new Area(String.valueOf(range), Long.valueOf(rangeWorkList.stream().count()).intValue()));
        }
        return list;
    }

    private List<Long> getRanges(String rangeStr) {
        String[] strs = rangeStr.split(",");
        List<Long> ranges = Arrays.stream(strs).map(range -> Long.parseLong(range)).collect(Collectors.toList());
        return ranges;
    }
}
