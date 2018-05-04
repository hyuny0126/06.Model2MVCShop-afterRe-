package com.model2.mvc.service.purchase;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;

public interface PurchaseService {

	public void addPurchase(Purchase purchase) throws Exception;

	public Map<String,Object> getPurchaseList(Search search, String buyerId) throws Exception;
	
	//구매 목록 조회에서 no링크타고 내가 구매한 상품 보는것
	public Purchase getPurchase(int tranNo) throws Exception;
	
	//구매요청을 수정
	public void updatePurchase(Purchase purchase) throws Exception;
	
	/*public PurchaseVO getPurchase2(int ProdNo) throws Exception;
	
	public HashMap<String,Object> getSaleList(SearchVO searchVO) throws Exception;*/
	
	//구매이력조회에서 물건도착 눌렀을때 
	public void updateTranCode(Purchase purchase) throws Exception;
	
	//상품관리에서 배송하기를 눌렀을때
	public void updateTranCodeByProd(Purchase purchase) throws Exception;
	
}