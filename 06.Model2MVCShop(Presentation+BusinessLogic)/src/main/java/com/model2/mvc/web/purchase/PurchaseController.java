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
import org.springframework.web.bind.annotation.RequestMethod;
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
@RequestMapping("/purchase/*")
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
	
	
	///구매전화면 
	//@RequestMapping("/addPurchaseView.do")
	@RequestMapping(value="addPurchase",  method=RequestMethod.GET )
	public ModelAndView addPurchase(	
																							HttpServletRequest request,
																							@ModelAttribute("product") Product product,
																							ModelAndView modelAndView) throws Exception {
		
		System.out.println("/purchase/addPurchase : GET ::구매전화면 ");
		int prodNo =Integer.parseInt(request.getParameter("prod_no"));
		 product= productService.getProduct(prodNo);
		 
		// Model 과 View 연결
		modelAndView.setViewName("forward:/purchase/addPurchaseView.jsp");
		modelAndView.addObject("product",product);
		return modelAndView; 
		}
	
	
	///구매화면
	//@RequestMapping("/addPurchase.do")
	@RequestMapping(value="addPurchase",  method=RequestMethod.POST )
	public ModelAndView addPurchase( @ModelAttribute("purchase") Purchase purchase,
																				Product product,
																				HttpSession session,
																				HttpServletRequest request,
																				ModelAndView modelAndView) throws Exception {

		System.out.println("/purchase/addPurchase : POST ");
		//Business Logic
		//펄체이스 필드에 담는과정 (유저는 세션에서 뽑아서 객체 통으로 ,프로덕트는 객체 생성해서 상품번호 심은다음 넣어줌, 날짜는 - 빼줌), 트랜코드 1 심어주는건 쿼리로 넘김mapper.xml로, 
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
		
		// 만약에 구매개수가 상품수보다 크다면 
		if(quantity>product.getQuantity()) {
			modelAndView.setViewName("forward:/purchase/noProd.jsp");
			modelAndView.addObject("product",product);
		
		}else { //구매개수 <= 상품수 
			modelAndView.setViewName("forward:/purchase/addPurchaseAfter.jsp");
			quantity = product.getQuantity() - quantity;  // 기존개수에서 - 구매개수 
			product.setQuantity(quantity);  // 다시 프로덕트 도메인데 담아서 프로덕트 테이블 update
			productService.updateProduct(product);
		}	
		
		// Model 과 View 연결
		modelAndView.addObject("purchase",purchase);
		return modelAndView; 
	}
	
	
	///구매 상세 조회
	//@RequestMapping("/getPurchase.do")
	@RequestMapping(value="getPurchase",  method=RequestMethod.GET)
	public ModelAndView getPurchase( @RequestParam("tranNo") String tranNo , 
																				Purchase purchase,
																				ModelAndView modelAndView) throws Exception {
		
		System.out.println("purchase/getPurchase:: GET");
		//Business Logic
		purchase= purchaseService.getPurchase(Integer.parseInt(tranNo));
		int prodNO =purchase.getPurchaseProd().getProdNo(); //상품명 가져오기 위해서
		Product product = productService.getProduct(prodNO);
		
		// Model 과 View 연결
		modelAndView.setViewName("forward:/purchase/getPurchase.jsp");
		modelAndView.addObject("purchase",purchase);
		modelAndView.addObject("product", product);
		return modelAndView;
	}
	
	///구매정보 수정 전 화면
	//@RequestMapping("/updatePurchaseView.do")
	@RequestMapping(value="updatePurchase",method=RequestMethod.GET)
	public ModelAndView updatePurchase( @RequestParam("tranNo") String tranNo,
																												ModelAndView modelAndView) throws Exception{

		System.out.println("/purchase/updatePurchase ::GET");
		//Business Logic
		Purchase purchase= purchaseService.getPurchase(Integer.parseInt(tranNo));
		
		// Model 과 View 연결
		modelAndView.setViewName("forward:/purchase/updatePurchaseView.jsp");
		modelAndView.addObject("purchase", purchase);
		return modelAndView ;
	}
	
	///구매 정보수정
	//@RequestMapping("/updatePurchase.do")
	@RequestMapping(value="updatePurchase",method=RequestMethod.POST)
	public ModelAndView updatePurchase( @RequestParam("tranNo") String tranNo,
																								@ModelAttribute("purchase") Purchase purchase ,
																								ModelAndView modelAndView) throws Exception{

		System.out.println("/purchase/updatePurchase ::POST");
		//Business Logic
		purchase.setTranNo(Integer.parseInt(tranNo)); 
		purchase.setDivyDate(purchase.getDivyDate().replaceAll("-",""));
		purchaseService.updatePurchase(purchase);
		
		// Model 과 View 연결
		modelAndView.setViewName( "redirect:/purchase/getPurchase?tranNo="+tranNo);
		return modelAndView;
	}
	
	///유저로그인, 구매목록조회에서 물건도착 눌렀을때 , 반품신청할때(반품신청은 단순 코드변경임) 
	//@RequestMapping("/updateTranCode.do")
	@RequestMapping(value="updateTranCode" ,method=RequestMethod.GET)
	public ModelAndView updateTranCode(@Param("tranNo")String tranNo,
																								@Param("tranCode")String tranCode,
																								Purchase purchase,
																								ModelAndView modelAndView) throws Exception{
		
		System.out.println("/purchase/updateTranCode ::GET ::유저 물건도착 누름");
		//Business Logic
		purchase.setTranCode(tranCode);
		purchase.setTranNo(Integer.parseInt(tranNo));
		purchaseService.updateTranCode(purchase);
		
		// Model 과 View 연결
		modelAndView.setViewName("forward:/purchase/listPurchase.do");
		modelAndView.addObject("purchase", purchase);
		return modelAndView;
	}
	
	///어드민 로그인, 상품관리에서 배송하기 눌렀을때 
	//@RequestMapping("/updateTranCodeByProd.do")
	@RequestMapping(value="updateTranCodeByProd" ,method=RequestMethod.GET)
	public ModelAndView updateTranCodeByProd(@Param("tranNo")String tranNo,
																													@Param("tranCode")String tranCode,
																													Purchase purchase,
																													Product product,
																													ModelAndView modelAndView) throws Exception{
		
		System.out.println("/purchase/updateTranCodeByProd ::GET ::어드민 배송하기 누름");
		//Business Logic
		purchase.setTranCode(tranCode);
		purchase.setTranNo(Integer.parseInt(tranNo));
		purchaseService.updateTranCodeByProd(purchase);
		// Model 과 View 연결
		modelAndView.setViewName("forward:/purchase/listDeliveryManage");
		return modelAndView;
	}
	
	
	///유저로그인, 구매목록조회 (바이어아이디로 구매테이블 검색)
	//@RequestMapping("/listPurchase.do")
	@RequestMapping(value="listPurchase" )
	public ModelAndView listPurchase( @ModelAttribute("search") Search search ,
												 		HttpServletRequest request,
												 		HttpSession session,
														ModelAndView modelAndView) throws Exception{
		
		System.out.println("/purchase/listPurchase ::GET,POST");
		
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
	
	//////추가기능
	///어드민 로그인. 배송, 구매취소, 반품 관리 목록 조회(전체 구매테이블 검색)
		//@RequestMapping("/listDeliveryManage.do")
		@RequestMapping(value="listDeliveryManage" )
		public ModelAndView listDeliveryManage( @ModelAttribute("search") Search search ,
													 		HttpServletRequest request,
													 		HttpSession session,
															ModelAndView modelAndView) throws Exception{
			
			System.out.println("/purchase/listDeliveryManage::GET,POST");
			
			//서치
			if(search.getCurrentPage() ==0 ){
				search.setCurrentPage(1);
			}
			search.setPageSize(pageSize);
			
			String buyerId=null;
			// Business logic 수행
			Map<String , Object> map= purchaseService.getPurchaseList(search, buyerId);
			Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
			System.out.println(resultPage);
			
			// Model 과 View 연결
			modelAndView.setViewName("forward:/purchase/listDeliveryManage.jsp");
			modelAndView.addObject("list", map.get("list"));
			modelAndView.addObject("resultPage", resultPage);
			modelAndView.addObject("search", search);
			return modelAndView;
		}
		
		///주문취소, 반품 수락 후 상품개수 살리기
		//@RequestMapping("/cancleProd.do")
		@RequestMapping(value="cancleProd" ,method=RequestMethod.GET)
		public ModelAndView cancleProd(@Param("tranNo")String tranNo,
																						@Param("tranCode")String tranCode,
																						Purchase purchase,
																						Product product,
																						HttpSession session,
																						ModelAndView modelAndView) throws Exception{
			
			System.out.println("/purchase/cancleProd::GET");
			//Business Logic
			//1.구매테이블에서 배송코드 바꾸기, 구매테이블에서 구매수량, 상품번호 들고 오기
			purchase.setTranCode(tranCode);
			int tranNoInt = Integer.parseInt(tranNo);
			purchase.setTranNo(tranNoInt);
			purchaseService.updateTranCodeByProd(purchase); // 겟파라메터한 트랜코드와 트렌넘버 펄체이스에 넣어서 구매테이블에 코드변경시킴
			
			purchase = purchaseService.getPurchase(tranNoInt);   //트랜넘버 기준으로 펄체이스 가져와서 (상품번호, 구매수량)
			int prodNo = purchase.getPurchaseProd().getProdNo();  //그안에 상품번호를 겟함 
			int quantityTran = purchase.getQuantity(); //구매했을때 구매수량
			
			//2. 상품테이블가서 상품수량 늘리기 (현재 상품 체크 후 +시키기)
			product = productService.getProduct(prodNo);  // 다시 더해줄 상품의 현재 개수 알아야함, 위에서 꺼낸 상품번호 기준으로 상품 찾아옴
			int quantityProd = product.getQuantity();  //상품번호로 가져온 상품의  현재수량
			quantityProd = quantityProd+quantityTran; // 상품 현재수량에 + 구매수량(반품했거나 주문취소) 
			product.setQuantity(quantityProd);  // 계산해서 다시 상품도메인에 넣고 수정쿼리 날림
			productService.updateProduct(product);
			
			// Model 과 View 연결
			User user =(User)session.getAttribute("user");        // 주문취소는 유저가 한것이고, 반품수락은 어드민이 한것이므로 세션에서 꺼내서 확인 후 해당페이지로 포워드
			
			if ( user.getRole().equals("admin") ){
				modelAndView.setViewName("forward:/purchase/listDeliveryManage");
			}else {
				modelAndView.setViewName("forward:/purchase/listPurchase");
			}
			return modelAndView;
			
		}
}//end of class