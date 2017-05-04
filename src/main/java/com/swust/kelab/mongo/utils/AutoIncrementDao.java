/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2015 All Rights Reserved.
 */
package com.swust.kelab.mongo.utils;

/**
 *
 */
public interface AutoIncrementDao {
    Integer getAutoIncrement(String collectionName, String fieldName);
    Integer getAutoIncrement(String collectionName, String fieldName, int init);
}
