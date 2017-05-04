package com.swust.kelab.repos;

import com.swust.kelab.repos.bean.ListQuery;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zengdan on 2017/1/6.
 */
@Repository(value = "mysqlDao")
public class MysqlDao{
    @Resource
    private SqlSession sqlSession;
    //	临时
    public List<Object> selectAllData(String space, ListQuery query) throws Exception {
        return sqlSession.selectList(space+".selectAllData", query);
    }
    public int selectAllCount(String space, ListQuery query) throws Exception {
        return sqlSession.selectOne(space+".selectAllCount", query);
    }
//截止
}
