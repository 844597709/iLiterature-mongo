package com.swust.kelab.mongo.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.swust.kelab.domain.Comment;
import com.swust.kelab.domain.CommentFreqs;
import com.swust.kelab.domain.DescriptionFreqs;
import com.swust.kelab.domain.WorkDescription;
import com.swust.kelab.mongo.dao.AuthorUpdateDaoTemp;
import com.swust.kelab.mongo.dao.WorksCommentDao;
import com.swust.kelab.mongo.dao.WorksInfoDaoTemp;
import com.swust.kelab.mongo.dao.WorksUpdateDaoTemp;
import com.swust.kelab.mongo.domain.TempWorks;
import com.swust.kelab.mongo.domain.TempWorksComment;
import com.swust.kelab.mongo.domain.model.Area;
import com.swust.kelab.mongo.domain.vo.TempWorksVo;
import com.swust.kelab.mongo.utils.CollectionUtil;
import com.swust.kelab.web.model.AuthorWorkUpdate;
import com.swust.kelab.web.model.EPOQuery;
import com.swust.kelab.web.model.QueryData;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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
    @Resource
    private WorksCommentDao worksCommentDao;

    public List<Area> selectWorkType(Integer siteId, Integer topNum) {
        Integer allValidWorkCount = worksInfoDao.selectValidWorkCount(siteId);
        List<Area> results = worksInfoDao.selectWorkType(siteId, topNum);
        Integer topWorkCount = results.stream().collect(Collectors.summingInt(Area::getValue));
        results.add(new Area("其他类型", allValidWorkCount - topWorkCount));
        return results;
    }

    public List<TempWorksVo> selectHotTopWork(Integer siteId, Integer field, Integer descOrAsc, Integer topNum) {
        List<TempWorksVo> workList = worksInfoDao.selectHotTopWork(siteId, field, descOrAsc, topNum);
        return workList;
    }

    public List<AuthorWorkUpdate> selectWorkUpdateByTime(Integer descOrAsc, String startTime, Integer dayNum) {
        List<AuthorWorkUpdate> list = Lists.newArrayList();
        try {
            List<Area> workUpdateList = worksUpdateDao.selectWorkUpdateByTime(descOrAsc, startTime, dayNum);
            List<Area> authorUpdateList = authorUpdateDao.selectAuthorUpdateByTime(descOrAsc, startTime, dayNum);

            Map<String, Integer> workUpdateMap = workUpdateList.stream().collect(Collectors.toMap(Area::getName, Area::getValue));
            Map<String, Integer> authorUpdateMap = authorUpdateList.stream().collect(Collectors.toMap(Area::getName, Area::getValue));
            /*workUpdateMap.forEach((time, count) -> {
                AuthorWorkUpdate authorWorkUpdate = new AuthorWorkUpdate(authorUpdateMap.get(time), count, time);
                list.add(authorWorkUpdate);
            });*/
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime(sdf.parse(startTime));
            for (int i = 0; i < dayNum; i++) {
                String time = sdf.format(calendar.getTime());
                Integer authorUpdateCount = authorUpdateMap.get(time) == null ? 0 : authorUpdateMap.get(time);
                Integer workUpdateCount = workUpdateMap.get(time) == null ? 0 : workUpdateMap.get(time);
                AuthorWorkUpdate authorWorkUpdate = new AuthorWorkUpdate(authorUpdateCount, workUpdateCount, time);
                calendar.add(Calendar.DATE, 1);
                list.add(authorWorkUpdate);
            }
            /*workUpdateList.forEach(workupdate -> {
                AuthorWorkUpdate authorWorkUpdate = new AuthorWorkUpdate(authorUpdateMap.get(workupdate.getName()), workupdate.getValue(), workupdate.getName());
                list.add(authorWorkUpdate);
            });*/
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    /**
     * 根据作品id查询作品信息
     */
    public TempWorksVo selectWorkById(Integer workId) {
        return worksInfoDao.selectWorkById(workId);
    }

    /**
     * 根据作者id查询作品信息
     */

    public List<TempWorks> selectWorksByAuthId(Integer authId) {
        return worksInfoDao.selectWorksByAuthId(authId);
    }

    /**
     * 根据作者id针对作品评论分词
     */
    // TODO 后期修改
    public List<WorkDescription> selectWorksDescByAuthId(Integer authId) {
        List<TempWorks> worksList = worksInfoDao.selectWorksByAuthId(authId);
        List<WorkDescription> workDescList = Lists.newArrayList();
        worksList.forEach(work -> {
            WorkDescription workDesc = new WorkDescription();
            workDesc.setWorkId(work.getWorkId());
            workDesc.setDescription(work.getWorkDesc());
            workDescList.add(workDesc);
        });

        String[] allCommentWords = null;
        for (int i = 0; i < workDescList.size(); i++) {
            allCommentWords = (String[]) ArrayUtils.addAll(allCommentWords, workDescList.get(i).Words);
        }
        DescriptionFreqs freqs = new DescriptionFreqs();
        List<WorkDescription> wordcloud = freqs.getHighFreqWords(allCommentWords);
        return wordcloud;
    }

    /**
     * 评论词云
     */
    public List<Comment> selectWorkCommentById(int workId) {
        List<TempWorksComment> workCommentList = worksCommentDao.selectWorkCommentById(workId);
        List<Comment> commentList = Lists.newArrayList();
        workCommentList.forEach(workComment->{
            Comment workDesc = new Comment();
            workDesc.setWorkId(workComment.getWocoWorkId());
            workDesc.setContent(workComment.getWocoContent());
            commentList.add(workDesc);
        });

        String[] allCommentWords = null;
        for (int i = 0; i < commentList.size(); i++) {
            allCommentWords = (String[]) ArrayUtils.addAll(allCommentWords, commentList.get(i).rawwords);
        }
        CommentFreqs freqs = new CommentFreqs();
        List<Comment> wordcloud = freqs.getHighFreqWords(allCommentWords);
        return wordcloud;
    }

    @Deprecated
    public Map<String, Object> countInfoNumAll(String hitsRange, String commentsRange, String recomsRange, Integer siteId, Integer descOrAsc, Integer pageSize) {
        Long timea = System.currentTimeMillis();
        // 方法1：
        /*int documentCount = Long.valueOf(worksInfoDao.getCount()).intValue();
        System.out.println(documentCount);
        List<TempWorks> sortedWorkByHitsNum = worksInfoDao.selectHotTopWork(siteId, 1, descOrAsc, documentCount);
        List<TempWorks> sortedWorkByCommentsNum = worksInfoDao.selectHotTopWork(siteId, 2, descOrAsc, documentCount);
        List<TempWorks> sortedWorkByRecomsNum = worksInfoDao.selectHotTopWork(siteId, 3, descOrAsc, documentCount);
        */

        // 方法2：
        //获取所有，不排序
        List<TempWorksVo> allWorkList = worksInfoDao.selectHotTopWork(siteId, 0, descOrAsc, Integer.MAX_VALUE);
        if (allWorkList.isEmpty()) {
            return CollectionUtil.emptyMap();
        }
        List<TempWorksVo> sortedWorkByHitsNum = allWorkList.stream().sorted((o1, o2) -> o1.getWorkTotalHits().compareTo(o2.getWorkTotalHits())).collect(Collectors.toList());
        List<TempWorksVo> sortedWorkByCommentsNum = allWorkList.stream().filter(work -> work.getWorkCommentsNum() > 0).sorted((o1, o2) -> o1.getWorkCommentsNum().compareTo(o2.getWorkCommentsNum())).collect(Collectors.toList());
        List<TempWorksVo> sortedWorkByRecomsNum = allWorkList.stream().filter(work -> work.getWorkCommentsNum() > 0).sorted((o1, o2) -> o1.getWorkTotalRecoms().compareTo(o2.getWorkTotalRecoms())).collect(Collectors.toList());

        // 方法3：
//        List<TempWorks> sortedWorkByHitsNum = worksInfoDao.selectSortedWorks(siteId, 1, descOrAsc, pageSize);

        Map<String, Object> map = Maps.newHashMap();
        map.put("totalHits", this.getHitsRangeWithAuthorCount(1, hitsRange, sortedWorkByHitsNum));
        map.put("commentsNum", this.getHitsRangeWithAuthorCount(2, commentsRange, sortedWorkByCommentsNum));
//        map.put("commentsNum", CollectionUtil.emptyList());
        map.put("totalRecoms", this.getHitsRangeWithAuthorCount(3, recomsRange, sortedWorkByRecomsNum));
//        map.put("totalRecoms", CollectionUtil.emptyList());
        Long timeaa = System.currentTimeMillis();
        System.out.println("work-countInfoNum:" + (timeaa - timea));
        return map;
    }

    @Deprecated
    private List<Area> getHitsRangeWithAuthorCount(Integer field, String rangeStr, List<TempWorksVo> sortWorkList) {
        List<Integer> ranges = this.getRanges(rangeStr);
        List<Area> list = Lists.newArrayList();
        for (Integer range : ranges) {
            List<TempWorksVo> rangeWorkList = Lists.newArrayList();
            Integer num = 0;
            for (int i = 0; i < sortWorkList.size(); i++) {
                if (field == 1) {
                    num = sortWorkList.get(i).getWorkTotalHits();
                } else if (field == 2) {
                    num = sortWorkList.get(i).getWorkCommentsNum();
                } else if (field == 3) {
                    num = sortWorkList.get(i).getWorkTotalRecoms();
                }
                if (num > range) {
                    rangeWorkList = sortWorkList.subList(0, i);
                    sortWorkList = sortWorkList.subList(i, sortWorkList.size());
                    break;
                }
            }
            list.add(new Area(String.valueOf(range), Long.valueOf(rangeWorkList.stream().count()).intValue()));
        }
        return list;
    }

    public Map<String, List<Area>> countWorkInfoNum(String hitsRange, String commentsRange, String recomsRange, Integer siteId) {
        List<Integer> hitsList = this.getRanges(hitsRange);
        List<Integer> commentsList = this.getRanges(commentsRange);
        List<Integer> recomsList = this.getRanges(recomsRange);
        if (hitsList.size() <= 1) {
            return CollectionUtil.emptyMap();
        }
        if (commentsList.size() <= 1) {
            return CollectionUtil.emptyMap();
        }
        if (recomsList.size() <= 1) {
            return CollectionUtil.emptyMap();
        }

        Long timea = System.currentTimeMillis();
        List<Area> rangeWithHitsCount = Lists.newArrayList();
        for (int i = 1; i < hitsList.size(); i++) {
            Area hits = worksInfoDao.selectRangeWithWorkCount(siteId, 1, hitsList.get(i - 1), hitsList.get(i));
            rangeWithHitsCount.add(hits);
        }
        List<Area> rangeWithCommentsCount = Lists.newArrayList();
        for (int i = 1; i < commentsList.size(); i++) {
            Area comments = worksInfoDao.selectRangeWithWorkCount(siteId, 2, commentsList.get(i - 1), commentsList.get(i));
            rangeWithCommentsCount.add(comments);
        }
        List<Area> rangeWithRecomsCount = Lists.newArrayList();
        for (int i = 1; i < recomsList.size(); i++) {
            Area recoms = worksInfoDao.selectRangeWithWorkCount(siteId, 3, recomsList.get(i - 1), recomsList.get(i));
            rangeWithRecomsCount.add(recoms);
        }
        Long timeaa = System.currentTimeMillis();
        System.out.println("work-消耗时间：" + (timeaa - timea));

        Map<String, List<Area>> map = Maps.newHashMap();
        map.put("totalHits", rangeWithHitsCount);
        map.put("commentsNum", rangeWithCommentsCount);
        map.put("totalRecoms", rangeWithRecomsCount);
        return map;
    }

    private List<Integer> getRanges(String rangeStr) {
        String[] strs = rangeStr.split(",");
        List<Integer> ranges = Arrays.stream(strs).map(range -> Integer.parseInt(range)).collect(Collectors.toList());
        return ranges;
    }
}
