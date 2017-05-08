package com.swust.kelab.mongo.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.xpath.operations.Bool;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengdan on 2017/5/5.
 */
public class CollectionUtil {
    public static List emptyList() {
        return Lists.newArrayList();
    }

    public static Map emptyMap() {
        return Maps.newHashMap();
    }

    public static Boolean isEmpty(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    public static Boolean isEmpty(Map map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }
}
