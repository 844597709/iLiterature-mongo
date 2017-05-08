package com.swust.kelab.repos;

import com.swust.kelab.domain.Site;
import com.swust.kelab.repos.bean.ListQuery;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository(value = "siteDao")
public class SiteDao {
	@Resource
	private SqlSession sqlSession;
//	@Resource
//	HttpServletRequest request;

	// --zd--
	public List<Site> selectSite() throws Exception {
		return sqlSession.selectList("site.selectSite");
	}

	// lj
	/*public Site selectSiteById(int siteId) throws Exception {
		return sqlSession.selectOne("site.selectSiteById", siteId);
	}*/

	public List<Site> selectAuthorWorkNum(ListQuery query) throws Exception {
		return sqlSession.selectList("site.selectAuthorWorkNum", query);
	}

	public List<Site> selectLastestUpdateTime() throws Exception {
		return sqlSession.selectList("site.selectLastestUpdateTime");
	}
	
	public List<Site> selectAuthorNum(ListQuery query) throws Exception {
		return sqlSession.selectList("site.selectAuthorNum", query);
	}

	public long selectWorkNum(int crwsId) throws Exception {
		return sqlSession.selectOne("site.selectWorkNum", crwsId);
	}

	public String selectLastestUpdate(int crwsId) throws Exception {
		return sqlSession.selectOne("site.selectLastestUpdate", crwsId);
	}
	// --至此--

	public int selectCount(ListQuery query) throws Exception {
		return sqlSession.selectOne("site.selectCount", query);
	}

	public int insert(Site site) throws Exception {
		return sqlSession.insert("site.insert", site);
	}

	public int update(Site site) throws Exception {
		return sqlSession.update("site.update", site);
	}

	public int delete(int siteId) throws Exception {
		return sqlSession.delete("site.delete", siteId);
	}

	public List<Site> selectList(ListQuery query) throws Exception {
		return sqlSession.selectList("site.select", query);
	}

	public long countSite() {
		return sqlSession.selectOne("site.countSite");
	}
}
