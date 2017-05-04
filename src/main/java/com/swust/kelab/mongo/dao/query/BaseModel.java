package com.swust.kelab.mongo.dao.query;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础模型，包含修改时间和创建时间，变更集方法用来支持增量修改
 */
public abstract class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String _id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

    private String operatorName;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    @Override
    public String toString() {
        return "CompatibleBaseModel [_id=" + _id + ", createTime=" + createTime + ", updateTime=" + updateTime
                + ", operatorName=" + operatorName + "]";
    }
}
