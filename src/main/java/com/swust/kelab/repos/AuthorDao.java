package com.swust.kelab.repos;

//@Repository(value = "authorDao")
/*public class AuthorDao {
	@Resource
	private SqlSession sqlSession;
//	@Resource
//	HttpServletRequest request;

	public int selectCount(ListQuery query) throws Exception {
		return sqlSession.selectOne("author.selectCount", query);
	}

	public int insert(Author author) throws Exception {
		return sqlSession.insert("author.insert", author);
	}

	public int update(Author author) throws Exception {
		return sqlSession.update("author.update", author);
	}

	public int delete(int authorId) throws Exception {
		return sqlSession.delete("author.delete", authorId);
	}

	public List<Author> selectList(ListQuery query) throws Exception {
		return sqlSession.selectList("author.select", query);
	}

	public List<AuthorUpdate> selectAuthorUpdate(ListQuery query) throws Exception {
		return sqlSession.selectList("author.selectAuthorUpdate", query);
	}

	public Author selectOne(ListQuery query) throws Exception {
		return sqlSession.selectOne("author.selectById", query);
	}

	public long countAuthor(int siteId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteId", siteId);
		return sqlSession.selectOne("author.countAuthor", map);
	}

	public int countA() throws Exception {
		return sqlSession.selectOne("author.countAuthor");
	}

	// lj
	public Long countAuthorGender(String gender, int siteId) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteId", siteId);
		map.put("gender", gender);
		return sqlSession.selectOne("author.countAuthorGender", map);
	}

	// lj
	public int countAuthorNum(int type, long minNum, long maxNum) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("minNum", minNum);
		map.put("maxNum", maxNum);
		map.put("type", type);
		return sqlSession.selectOne("author.selectCountOfNum", map);
	}
	//cdk 2016/5/12
	public List<Area>  countAreaInfo(Integer siteId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteId", siteId);
		return sqlSession.selectList("author.countAreaInfo",map);
	}
	//cdk
	public Area countAreaMaxPeople(Integer siteId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteId", siteId);
		return sqlSession.selectOne("author.countAreaMaxPeople",map);
	}
	//cdk
	public int countAreaSumPeople(Integer siteId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteId", siteId);
		return sqlSession.selectOne("author.countAreaSumPeople",map);
	}
	

}*/
