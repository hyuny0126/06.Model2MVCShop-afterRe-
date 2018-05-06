package com.model2.mvc.web.purchase;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;


//==> 구매관리 Controller
@Controller
public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public PurchaseController(){
		System.out.println("펄체이스 컨트롤러생성자"+this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	//구매전화면 
	@RequestMapping("/addPurchaseView.do")
	public ModelAndView addUserView(	
																							HttpServletRequest request,
																							@ModelAttribute("product") Product product,
																							ModelAndView modelAndView) throws Exception {
		
		System.out.println("/addPurchaseView.do");
		int prodNo =Integer.parseInt(request.getParameter("prod_no"));
		 product= productService.getProduct(prodNo);
		 
		// Model 과 View 연결
		modelAndView.setViewName("forward:/purchase/addPurchaseView.jsp");
		modelAndView.addObject("product",product);
		return modelAndView; 
		}
	
	//구매화면
	@RequestMapping("/addPurchase.do")
	public ModelAndView addUser( @ModelAttribute("purchase") Purchase purchase,
																				Product product,
																				HttpSession session,
																				HttpServletRequest request,
																				ModelAndView modelAndView) throws Exception {

		System.out.println("/addPurchase.do");
		//Business Logic
		/// 펄체이스 필드에 담는과정 (유저는 세션에서 뽑아서 객체 통으로 ,프로덕트는 객체 생성해서 상품번호 심은다음 넣어줌, 날짜는 - 빼줌), 트랜코드 1 심어주는건 쿼리로 넘김mapper.xml로, 
		User user =(User)session.getAttribute("user");
		purchase.setBuyer(user);
		
		String prodNo = request.getParameter("prodNo");
		product = productService.getProduct(Integer.parseInt(prodNo));
		
		//product.setProdNo(Integer.parseInt(prodNo));
		purchase.setPurchaseProd(product);
		
		purchase.setDivyDate(purchase.getDivyDate().replaceAll("-", ""));
		purchaseService.addPurchase(purchase);
		
		// 수량 구매시 프로덕트 테이블에서 수량 감소 ,  상품번호랑 수량을 상품테이블로
		int quantity =Integer.parseInt( request.getParameter("quantity"));
		quantity = product.getQuantity() - quantity;  // 기존개수에서 - 구매개수 
		
		System.out.println("구매수량"+quantity);

		product.setQuantity(quantity);  // 다시 프로덕트 도메인데 담아서 프로덕트 테이블 update
		System.out.println("프로덕트 가기전"+product);
		productService.updateProduct(product);
		
		// Model 과 View 연결
		modelAndView.setViewName("forward:/purchase/addPurchaseAfter.jsp");
		modelAndView.addObject("purchase",purchase);
		return modelAndView; 
	}
	
	//구매 상세 조회
	@RequestMapping("/getPurchase.do")
	public ModelAndView getUser( @RequestParam("tranNo") String tranNo , 
																				Purchase purchase,
																				ModelAndView modelAndView) throws Exception {
		
		System.out.println("/getPurchase.do");
		//Business Logic
		purchase= purchaseService.getPurchase(Integer.parseInt(tranNo));
		
		// Model 과 View 연결
		modelAndView.setViewName("forward:/purchase/getPurchase.jsp");
		modelAndView.addObject("purchase",purchase);
		return modelAndView;
	}
	
	//구매정보 수정 전 화면
	@RequestMapping("/updatePurchaseView.do")
	public ModelAndView updatePurchaseView( @RequestParam("tranNo") String tranNo,
																												ModelAndView modelAndView) throws Exception{

		System.out.println("/updatePurchaseView.do");
		//Business Logic
		Purchase purchase= purchaseService.getPurchase(Integer.parseInt(tranNo));
		
		// Model 과 View 연결
		modelAndView.setViewName("forward:/purchase/updatePurchaseView.jsp");
		modelAndView.addObject("purchase", purchase);
		return modelAndView ;
	}
	
	//구매 정보수정
	@RequestMapping("/updatePurchase.do")
	public ModelAndView updatePurchase( @RequestParam("tranNo") String tranNo,
																								@ModelAttribute("purchase") Purchase purchase ,
																								ModelAndView modelAndView) throws Exception{

		System.out.println("/updatePurchase.do");
		//Business Logic
		purchase.setTranNo(Integer.parseInt(tranNo)); 
		purchase.setDivyDate(purchase.getDivyDate().replaceAll("-",""));
		purchaseService.updatePurchase(purchase);
		
		// Model 과 View 연결
		modelAndView.setViewName( "redirect:/getPurchase.do?tranNo="+tranNo);
		return modelAndView;
	}
	
	//유저로그인, 구매목록조회에서 물건도착 눌렀을때
	@RequestMapping("/updateTranCode.do")
	public ModelAndView updateTranCode(@Param("tranNo")String tranNo,
																								@Param("tranCode")String tranCode,
																								Purchase purchase,
																								ModelAndView modelAndView) throws Exception{
		
		System.out.println("/updateTranCode.do");
		//Business Logic
		purchase.setTranCode(tranCode);
		purchase.setTranNo(Integer.parseInt(tranNo));
		purchaseService.updateTranCode(purchase);
		
		// Model 과 View 연결
		modelAndView.setViewName("forward:/listPurchase.do");
		modelAndView.addObject("purchase", purchase);
		return modelAndView;
	}
	
	//어드민 로그인, 상품관리에서 배송하기 눌렀을때 
	@RequestMapping("/updateTranCodeByProd.do")
	public ModelAndView updateTranCodeByProd(@Param("prodNo")String prodNo,
																													@Param("tranCode")String tranCode,
																													Purchase purchase,
																													Product product,
																													ModelAndView modelAndView) throws Exception{
		
		System.out.println("/updateTranCodeByProd.do");
		//Business Logic
		purchase.setTranCode(tranCode);
		product.setProdNo(Integer.parseInt(prodNo)); 
		purchase.setPurchaseProd(product);
		purchaseService.updateTranCodeByProd(purchase);
		// Model 과 View 연결
		modelAndView.setViewName("forward:/listProduct.do");
		return modelAndView;
	}
	
	//구매목록조회
	@RequestMapping("/listPurchase.do")
	public ModelAndView listUser( @ModelAttribute("search") Search search ,
												 		HttpServletRequest request,
												 		HttpSession session,
														ModelAndView modelAndView) throws Exception{
		
		System.out.println("/listPurchase.do");
		
		//서치
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		//세션에서 유저 아이디 뽑기(구매목록조회 쿼리에는 유저아이디 필요)
		User user=(User)session.getAttribute("user");
		String buyerId = user.getUserId();
		
		// Business logic 수행
		Map<String , Object> map= purchaseService.getPurchaseList(search, buyerId);
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		modelAndView.setViewName("forward:/purchase/listPurchase.jsp");
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		return modelAndView;
	}
}//end of class