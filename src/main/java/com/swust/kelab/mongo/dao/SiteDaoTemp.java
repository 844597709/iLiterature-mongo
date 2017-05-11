package com.swust.kelab.mongo.dao;

import com.swust.kelab.mongo.dao.base.PageResult;
import com.swust.kelab.mongo.dao.query.BaseDao;
import com.swust.kelab.mongo.dao.query.BaseQuery;
import com.swust.kelab.mongo.domain.TempCrawlWebsite;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "siteDaoTemp")
public class SiteDaoTemp extends BaseDao<TempCrawlWebsite> {
	@Override
	public void init() {
		super.collection = "crawlwebsite";
	}

	@Override
	public boolean updateOrSave(TempCrawlWebsite entity) {
		return false;
	}

	@Override
	public PageResult<TempCrawlWebsite> query(BaseQuery query) {
		return super.query(query);
	}

	@Override
	public TempCrawlWebsite findById(Integer id) {
		return null;
	}
}
