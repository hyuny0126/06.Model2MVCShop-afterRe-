package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;


//==> 상품관리 Controller
@Controller
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public ProductController(){
		System.out.println("프로덕트 컨트롤러 생성자"+this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	//상품등록
	@RequestMapping("/addProduct.do")
	public String addUser( @ModelAttribute("product") Product product) throws Exception {

		System.out.println("/addProduct.do");
		//Business Logic
		product.setManuDate(product.getManuDate().replaceAll("-", ""));
		productService.addProduct(product);
		
		return "forward:/product/addProductAfter.jsp";
	}
	
	//상품조회
	@RequestMapping("/getProduct.do")
	public String getUser( @RequestParam("prodNo") String prodNo ,
														Model model,
														HttpServletRequest request,
														HttpServletResponse response) throws Exception {
		
		System.out.println("/getProduct.do");
		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		//쿠키에 담을 상품 번호
		prodNo = ""+product.getProdNo();
		//cookie "history"키값에 더해져서 들어가야함  // 계속해서 어펜드 // 넣을 공간 필요 sum +=a, 과자, + 필통, 
		String sumProNo ="";
		Cookie[] cookies = request.getCookies(); //쿠키값 가져오는거임
		//스트링, 스트링, 더해주기 위해
		Cookie cookie ;
		if (cookies!=null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				 cookie = cookies[i];
				if (cookie.getName().equals("history")) {
					sumProNo = cookie.getValue(); 
				}//end of if
			}//end of for
		}//end of if
		prodNo = sumProNo+prodNo+",";
		//쿠키 생성  
		
		cookie= new Cookie("history",prodNo);
		response.addCookie(cookie);

		return "forward:/product/getProduct.jsp";
	}
	
	///상품수정 전 화면 요청
	@RequestMapping("/updateProductView.do")
	public String updateUserView( @RequestParam("prodNo") String prodNo , Model model ) throws Exception{

		System.out.println("/updateProductView.do");
		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/updateProductView.jsp";
	}
	
	///상품수정 요청
	@RequestMapping("/updateProduct.do")
	public String updateUser( @ModelAttribute("product") Product product , 
															Model model , 
															HttpSession session) throws Exception{

		System.out.println("/updateProduct.do");
		//Business Logic
		 productService.updateProduct(product);
		
		return "redirect:/getProduct.do?prodNo="+product.getProdNo()+"&menu=manage";
	}
	
	// 상품리스트
	@RequestMapping("/listProduct.do")
	public String listUser( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/listProduct.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		String result = "";
		if (request.getParameter("menu").equals("search")) {
			result ="forward:/product/listProductSerch.jsp";
		}else {
			result ="forward:/product/listProductManage.jsp";
		}
		
		return result;
	}
}