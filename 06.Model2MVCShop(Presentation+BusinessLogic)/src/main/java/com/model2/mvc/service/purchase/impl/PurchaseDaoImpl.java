package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;

import com.model2.mvc.service.purchase.PurchaseDao;

//==> 구매관리 DAO CRUD 구현
@Repository("purchaseDaoImpl")
public class PurchaseDaoImpl implements PurchaseDao {

	///Field
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	///Constructor
	public PurchaseDaoImpl() {
		System.out.println("purchase dao impl생성자 호출");
	}
	
	///Method
	@Override
	public void addPurchase(Purchase purchase) throws Exception {
		System.out.println("펄체이스 다오 시작");
		sqlSession.insert("PurchaseMapper.addPurchase", purchase);
	}

	@Override
	public Purchase getPurchase(int tranNo) throws Exception {
		return sqlSession.selectOne("PurchaseMapper.getPurchase",tranNo);
	}

	@Override
	public List<Purchase> getPurchaseList(Search search, String buyerId) throws Exception {
		
		//구매목록 리스트 서치랑 아이디 필요함
		Map<String , Object>  map = new HashMap<String, Object>();
		map.put("search", search);
		map.put("buyerId", buyerId);
		
		return sqlSession.selectList("PurchaseMapper.getpurchaseList",map );
		
		}

	@Override
	public int getTotalCount(String buyerId) throws Exception {
		return sqlSession.selectOne("PurchaseMapper.getTotalCount", buyerId);
	}
	
	@Override
	public void updatePurchase(Purchase purchase) throws Exception {
		sqlSession.update("PurchaseMapper.updatePurchase", purchase);
	}

	@Override
	public void updateTranCode(Purchase purchase) throws Exception {
		sqlSession.update("PurchaseMapper.updateTranCode", purchase);
	}

	@Override
	public void updateTranCodeByProd(Purchase purchase) throws Exception {
		sqlSession.update("PurchaseMapper.updateTranCodeByProd", purchase);
	}
	
}//end of class

