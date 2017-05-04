package com.swust.kelab.service.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import com.swust.kelab.domain.Column;
import com.swust.kelab.domain.MetaSearch;
import com.swust.kelab.repos.ModelDao;
import com.swust.kelab.repos.bean.GenericQuery;

@Service
public class ColumnService {
    @Resource
    private ModelDao<Column> columnDao;
    public List<Column> getFirstMenu() {
        List<Column> menuList = new ArrayList<Column>();
        menuList = columnDao.select(new GenericQuery().fill("type", 1));
        return menuList;
    }

    public List<Column> getMenuById(int parentId) {
        List<Column> menuList = new ArrayList<Column>();
        menuList = columnDao.select(new GenericQuery().fill("parent", parentId));
        return menuList;
    }

    public List<Map<String, Object>> getColumnsTree() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Column> column = columnDao.select(new GenericQuery());
        for (int i = 0; i < column.size(); i++) {
            Column firstCol = column.get(i);
            if (firstCol.getId() == firstCol.getType()) {
                Map<String, Object> map = new HashMap<String, Object>();
                List<Map<String, Object>> subcols1 = new ArrayList<Map<String, Object>>();
                map.put("colid", firstCol.getId());
                map.put("colname", firstCol.getName());
                map.put("colurl", firstCol.getUrl());
                map.put("coltype", firstCol.getType());
                map.put("colorder", firstCol.getSequence());
                List<Column> secondColumns = columnDao.select(new GenericQuery().fill("type", firstCol.getId()));
                for (int j = 0; j < secondColumns.size(); j++) {
                    Column secondCol = secondColumns.get(j);
                    Map<String, Object> secondMap = new HashMap<String, Object>();
                    List<Map<String, Object>> subcols2 = new ArrayList<Map<String, Object>>();
                    secondMap.put("colid", secondCol.getId());
                    secondMap.put("colname", secondCol.getName());
                    secondMap.put("colurl", secondCol.getUrl());
                    secondMap.put("coltype", secondCol.getType());
                    secondMap.put("colorder", secondCol.getSequence());
                    subcols1.add(secondMap);
                    List<Column> thirdColumns = columnDao.select(new GenericQuery().fill("type", secondCol.getId()));
                    for (int k = 0; k < thirdColumns.size(); k++) {
                        Column thirdColumn = thirdColumns.get(k);
                        Map<String, Object> thirdMap = new HashMap<String, Object>();
                        thirdMap.put("colid", thirdColumn.getId());
                        thirdMap.put("colname", thirdColumn.getName());
                        thirdMap.put("colurl", thirdColumn.getUrl());
                        thirdMap.put("coltype", thirdColumn.getType());
                        thirdMap.put("colorder", thirdColumn.getSequence());
                        subcols2.add(thirdMap);
                    }
                    secondMap.put("subcols", subcols2);
                }
                map.put("subcols", subcols1);
                list.add(map);
            }
        }
        return list;
    }

    /***
     * 功能：按照搜索引擎类型 返回给前台
     * 
     * @return
     */
    public Map<String, List<Map<String, Object>>> getMetasearchInformation() {
        return null;
    }

    public String chineseName(String name) {
        String returnName = "";
        if (name.contains("Yahoo")) {
            returnName = "雅虎";
        } else if (name.contains("Sogou")) {
            returnName = "搜狗";
        } else if (name.contains("Bing")) {
            returnName = "必应";
        } else if (name.contains("Baidu")) {
            returnName = "百度";
        } else if (name.contains("Qihu")) {
            returnName = "奇虎";
        }
        return returnName;
    }

}
