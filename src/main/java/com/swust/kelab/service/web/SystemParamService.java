package com.swust.kelab.service.web;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.swust.kelab.domain.SystemParameter;
import com.swust.kelab.repos.SystemParamDao;

@Service(value = "systemParamService")
public class SystemParamService {
	@Resource
	private SystemParamDao systemParamDao;
	/**
	 * 查询系统参数表中所有信息
	 * @return
	 */
	public List<SystemParameter> querySystemParam(){
		return systemParamDao.querySystemParam();
	}
	/**
	 * 修改系统参数表中参数值部分
	 * * @return
	 */
	public int modSystemParam(int sypaId1,int sypaId2,String sypaValue1, String sypaValue2) {
		return systemParamDao.modSystemParameter(sypaId1,sypaId2, sypaValue1,sypaValue2);
	}

}
