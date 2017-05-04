package com.swust.kelab.repos;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import com.google.common.collect.Maps;
import com.swust.kelab.domain.SystemParameter;

@Repository(value = "systemParamDao")
public class SystemParamDao {
	@Resource
	private SqlSession sqlSession;
	// querySystemParam 返回全部
	public List<SystemParameter> querySystemParam() {
		return sqlSession.selectList("systemparameter.select");
	}

	// modSystemParameter 修改参数值
	public int modSystemParameter(int sypaId1,int sypaId2,String sypaValue1,String sypaValue2) {
		int flag = 0;
		if (sypaId1 >0 && sypaValue1 != null ) {
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("sypaId", sypaId1);
			parameters.put("sypaValue", sypaValue1);
			sqlSession.update("systemparameter.updateSystemparam", parameters);
			flag = 1;
		}
		else{
			flag = 0;
		}
		if (sypaId2 >0&&sypaValue2!= null ) {
			Map<String, Object> parameters = Maps.newHashMap();
			parameters.put("sypaId", sypaId2);
			parameters.put("sypaValue", sypaValue2);
			sqlSession.update("systemparameter.updateSystemparam", parameters);
			flag = 1;		
		}
		else{
			flag = 0;
		}
		return flag;
	}

}
