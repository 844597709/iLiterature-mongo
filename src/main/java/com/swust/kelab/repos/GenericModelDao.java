package com.swust.kelab.repos;

import com.swust.kelab.domain.User;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.repos.bean.ListResult;
import com.swust.kelab.repos.bean.Query;
import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenericModelDao<T> /*extends AbstractModelDao*/ implements ModelDao<T> {

    // SQL_ID列表
    protected final String SQL_SELECT;
    protected final String SQL_SELECT_COUNT;
    protected final String SQL_INSERT;
    protected final String SQL_UDPATE;
    protected final String SQL_DELETE;

    // 配置
    private int startIndex = 0;
    private int maxCount = 1024;

    public GenericModelDao(String model) {
        SQL_SELECT = model + ".select";
        SQL_SELECT_COUNT = model + ".selectCount";
        SQL_INSERT = model + ".insert";
        SQL_UDPATE = model + ".update";
        SQL_DELETE = model + ".delete";
    }

    //--zd--
    @Resource
    private SqlSession sqlSession;

    @Override
    public List<T> select(ListQuery query) {
        return select(SQL_SELECT, query);
    }

    protected List<T> select(String sqlId, ListQuery query) {

        Integer startIndex = query.getStartIndex();
        Integer maxCount = query.getMaxCount();

        if (startIndex == null && maxCount == null) {
            return sqlSession.selectList(sqlId, query);
//            return queryDAO.executeForObjectList(sqlId, query);
        }

        startIndex = (startIndex == null || startIndex < this.startIndex) ? this.startIndex : startIndex.intValue();
        maxCount = (maxCount == null || maxCount > this.maxCount || maxCount <= 0) ? this.maxCount : maxCount
                .intValue();

        query.setStartIndex(startIndex);
        query.setMaxCount(maxCount);
//        return queryDAO.executeForObjectList(sqlId, query, startIndex, maxCount);
        return sqlSession.selectOne(sqlId, query);
    }

    @Override
    public int selectCount(Query query) {
        return sqlSession.selectOne(SQL_SELECT_COUNT, query);
    }

    @Override
    public List<User> selectIsLegal(HashMap<String, Object> map) {
        return sqlSession.selectList("user.selectIsLegal", map);
    }
    //--至此--
    
    @Override
    public ListResult<T> list(ListQuery query) {

        int totalCount = selectCount(query);
        if (totalCount == 0) {
            return new ListResult<T>(new ArrayList<T>(0));
        }

        List<T> list = select(query); // queryDAO.executeForObjectList(SQL_SELECT, query, query.getStartIndex(),
                                      // query.getMaxCount());

        return new ListResult<T>(list, totalCount);
    }

    @Override
    public int insert(T object) {
        if (object == null) {
            return 0;
        }
        return sqlSession.insert(SQL_INSERT, object);
    }

    @Override
    @Transactional
    public int insert(T[] list) {

        if (list == null || list.length == 0) {
            return 0;
        }

        // batchInsert or loop (when database not supported)
        int ret = 0;
        for (T obj : list) {
            ret = ret + insert(obj);
        }

        return ret;
    }

    @Override
    public int update(T object) {

        return sqlSession.update(SQL_UDPATE, object);
    }

    @Override
    public int delete(int id) {
        return sqlSession.delete(SQL_DELETE, id);
    }

}
