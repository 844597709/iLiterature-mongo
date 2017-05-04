package com.swust.kelab.repos;

import java.util.HashMap;
import java.util.List;

import com.swust.kelab.domain.User;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.repos.bean.ListResult;
import com.swust.kelab.repos.bean.Query;

/**
 * 通用数据访问接口
 * 
 * @author zhongyuan.zhang
 * 
 * @param <T> 结果数据类型
 */
public interface ModelDao<T> {

    /**
     * 查询符合条件的数据列表
     * 
     * @param query 查询条件
     * @return
     */
    List<T> select(ListQuery query);

    /**
     * 查询符合条件的数据总量
     * 
     * @param query 查询条件
     * @return
     */
    int selectCount(Query query);
    
    //--zd--
    /**
     * 查询用户是否认证及是否已经过期
     * 
     * @param map 查询条件
     * @return
     */
    List<User> selectIsLegal(HashMap<String, Object> map);
    //--至此--

    /**
     * 查询符合条件的数据列表和数据总量
     * 
     * @param query 查询条件
     * @return
     */
    ListResult<T> list(ListQuery query);

    /**
     * 插入单条数据
     * 
     * @param object 单条数据
     * @return 插入数据量
     */
    int insert(T object);

    /**
     * 批量插入多条数据
     * 
     * @param list 数据列表
     * @return 插入数据量
     */
    int insert(T[] list);

    /**
     * 更新单个数据
     * 
     * @param query 更新数据
     * @return
     */
    int update(T object);

    /**
     * 删除单个数据
     * 
     * @param id 数据ID
     * @return
     */
    int delete(int id);
}
