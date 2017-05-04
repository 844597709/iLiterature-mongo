package com.swust.kelab.mongo.utils;


import com.swust.kelab.mongo.dao.query.BaseModel;

/**
 * Created by libo on 16/8/22.
 */
public class AutoIncrement extends BaseModel {

    private String collectionName;

    private String fieldName;

    private Integer incrementId;

    public Integer getIncrementId() {
        return incrementId;
    }

    public void setIncrementId(Integer incrementId) {
        this.incrementId = incrementId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
