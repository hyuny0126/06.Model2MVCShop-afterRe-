package com.model2.mvc.service.product.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductDao;


//==> 惑前包府 DAO CRUD 备泅
@Repository("productDaoImpl")
public class ProductDaoImpl implements ProductDao {
	
	///Field
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession; 
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public ProductDaoImpl(){
		System.out.println("product dao impl积己磊 龋免");
	}

	///Method
	@Override
	public void addProduct(Product product) throws Exception {
		sqlSession.insert("ProductMapper.addProduct", product);
	}

	@Override
	public Product getProduc(int prodNo) throws Exception {
		return sqlSession.selectOne("ProductMapper.getProduct",prodNo);
	}

	@Override
	public List<Product> getProductList(Search search) throws Exception {
		return sqlSession.selectList("ProductMapper.getProductList",search );
	}

	@Override
	public void updateProduct(Product product) throws Exception {
		sqlSession.update("ProductMapper.updateProduct", product);
	}
	

	@Override
	public int getTotalCount(Search search) throws Exception {
		return sqlSession.selectOne("ProductMapper.getTotalCount", search);
	}
	
}//end of class
