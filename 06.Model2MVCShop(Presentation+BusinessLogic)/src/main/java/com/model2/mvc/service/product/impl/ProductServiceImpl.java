package com.model2.mvc.service.product.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.product.ProductService;

//==> 상품관리 서비스 구현
@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService  {
	
	///Field
	@Autowired
	@Qualifier("productDaoImpl")
	
	ProductDao productDao;
	
	public void setProductDAO(ProductDao productDao){
		this.productDao=productDao;
	}
	
	///Constructor
	public ProductServiceImpl() {
		System.out.println("ProductServiceImpl 생성자 호출");
	}
	
	///Method
	public void addProduct(Product product) throws Exception{
		productDao.addProduct(product);
	}

	public Product getProduct(int prodNo) throws Exception{      
		
		return productDao.getProduc(prodNo); //상품조회  getProduct(int prodNo) 는 리턴값이 protuctVO
	}

	public Map<String, Object> getProductList(Search search) throws Exception{  
		
		List<Product> list = productDao.getProductList(search);
		int totalCount = productDao.getTotalCount(search);
		//return productDao.getProductList(search); //상품목록은 리턴값이맵 //기존의 서비스 임플에서 바로 dao.~~() 호출
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list );
		map.put("totalCount", new Integer(totalCount));
		
		return map;
		
	}

	public void updateProduct(Product product) throws Exception{
		productDao.updateProduct(product);
	}

}//end of class
