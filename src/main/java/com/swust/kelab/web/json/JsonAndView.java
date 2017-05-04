/*
 * $Id: JsonAndView.java 5075 2012-10-18 07:11:36Z you.zhou $ Copyright (c) 2012 Qunar.com. All Rights Reserved.
 */
package com.swust.kelab.web.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// --------------------- Change Logs----------------------
// <p>@author zhou Initial Created at 2012-12-10<p>
// -------------------------------------------------------
public class JsonAndView implements Serializable {

    private static final long serialVersionUID = -8690687070450659428L;

    /**
     * 执行是否成功，true/false
     */
    private boolean ret = true;

    private int errcode = 0;

    private String errmsg = "";

    private int ver = 1;

    private Map<String, Object> data;

    /**
     * 创建默认JsonAndView
     */
    public JsonAndView() {
    }

    /**
     * 使用指定的ResultCode和message创建
     * 
     * @param errCode
     * @param errmsg
     */
    public JsonAndView(int resultCode, String message) {
        if (resultCode != 0) {
            ret = false;
        }
        this.errcode = resultCode;
        this.errmsg = message;
    }

    /**
     * 
     * @param resultCode
     */
    public JsonAndView(int resultCode) {
        if (resultCode != 0) {
            ret = false;
        }
        this.errcode = resultCode;
    }

    public boolean isRet() {
        return ret;
    }

    public JsonAndView setRet(boolean ret) {
        this.ret = ret;
        return this;
    }

    public int getErrcode() {
        return errcode;
    }

    public JsonAndView setErrcode(int errcode) {
        this.errcode = errcode;
        return this;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public JsonAndView setErrmsg(String errmsg) {
        this.errmsg = errmsg;
        return this;
    }

    /**
     * 设置数据 替换原有数据内容
     * 
     * @param data
     */
    public JsonAndView setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public Object getData() {
        return data;
    }

    /**
     * 添加数据 要求已持有对象必须为空或者类型为Map
     * 
     * @param key
     * @param data
     */
    public JsonAndView addData(String key, Object data) {
        if (this.data == null) {
            this.data = new HashMap<String, Object>();
        }
        this.data.put(key, data);
        return this;
    }

    /**
     * 批量插入数据 要求当传入对象为Map时，持有对象必须为Map或者为null 当传入对象为List时，如果持有对象为List，则追加，否则新建List，将原持有对象和传入List中的每一个元素加入到List中 否则插入失败
     * 
     * @param data
     */
    public JsonAndView addAllData(Map<String, Object> data) {
        if (this.data == null) {
            this.data = new HashMap<String, Object>();
        }
        this.data.putAll(data);
        return this;
    }

    public int getVer() {
        return ver;
    }

    public JsonAndView setVer(int ver) {
        this.ver = ver;
        return this;
    }

}
