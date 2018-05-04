package com.model2.mvc.service.purchase;

import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;

import com.model2.mvc.service.domain.Purchase;

public interface PurchaseDao {

	// INSERT 상품구매
	public void addPurchase(Purchase  purchase) throws Exception ;

	// SELECT ONE 구매 상세조회 
	public Purchase getPurchase(int tranNo) throws Exception ;

	// SELECT LIST 구매목록조회
	public  List<Purchase> getPurchaseList(Search search,String buyerId) throws Exception ;

	// UPDATE 구매정보 수정
	public void updatePurchase(Purchase purchase) throws Exception ;
	
	// 게시판 Page 처리를 위한 전체Row(totalCount)  return
	public int getTotalCount(String buyerId) throws Exception ;

	//배송코드 변경 2->3 구매이력조회에서 물건도착 눌렀을때 (유저로그인) 
	public void updateTranCode(Purchase purchase) throws Exception ;

	//배송코드 변경 1->2 상품관리에서 배송하기 눌렀을때 (어드민로그인) 
	public void updateTranCodeByProd(Purchase purchase) throws Exception ;
	
}
