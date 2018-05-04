package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseDao;
import com.model2.mvc.service.purchase.PurchaseService;


//==> 구매관리 서비스 구현
@Service("purchaseServiceImpl")

public class PurchaseServiceImpl implements PurchaseService  {
		
	///Field
		@Autowired
		@Qualifier("purchaseDaoImpl")
		 PurchaseDao  purchaseDao;
		
		public void setPruchaseDAO(PurchaseDao purchaseDao){
			this.purchaseDao=purchaseDao;
		}

		///Constructor
		public PurchaseServiceImpl() {
			System.out.println(" purchase service impl 생성자 호출");
		}
		
		///Method
		//구매요청
		public void addPurchase(Purchase purchase) throws Exception{
			System.out.println("========================== purchase service impl 구매요청");
			purchaseDao.addPurchase(purchase);
		}
		
		//구매목록조회
		public Map<String,Object> getPurchaseList(Search search, String buyerId) throws Exception{  
			
			List<Purchase>list = purchaseDao.getPurchaseList(search,buyerId);
			int totalCount = purchaseDao.getTotalCount(buyerId);
			//return productDao.getProductList(search); //상품목록은 리턴값이맵 //기존의 서비스 임플에서 바로 dao.~~() 호출
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", list );
			map.put("totalCount", new Integer(totalCount));
			
			return map;
		}
		
		//구매 상세조회
		public Purchase getPurchase(int tranNo) throws Exception{
			return purchaseDao.getPurchase(tranNo);
		}
		
		//구매수정조회
		public void updatePurchase(Purchase purchase) throws Exception{
			purchaseDao.updatePurchase(purchase);
		}
		
		//구매이력 조회 물건도착 눌렀을때
		public void updateTranCode(Purchase purchaseVO) throws Exception{
			purchaseDao.updateTranCode(purchaseVO);
		}
		
		//Admin 상품관리에서 배송하기를 눌렀을때
		public void updateTranCodeByProd(Purchase purchase) throws Exception{
			purchaseDao.updateTranCodeByProd(purchase);
		}
		
}//end of class
