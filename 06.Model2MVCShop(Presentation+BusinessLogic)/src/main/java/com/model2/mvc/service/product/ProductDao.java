package com.model2.mvc.service.product;

import java.util.List;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;

public interface ProductDao{
	
	// INSERT 상품등록
	public void addProduct(Product  product) throws Exception ;

	// SELECT ONE 상품조회 
	public Product  getProduc(int prodNo) throws Exception ;

	// SELECT LIST 상품목록조회
	public List<Product> getProductList(Search search) throws Exception ;

	// UPDATE 상품수정
	public void updateProduct(Product product) throws Exception ;
	
	// 게시판 Page 처리를 위한 전체Row(totalCount)  return
	public int getTotalCount(Search search) throws Exception ;

}//end of class


