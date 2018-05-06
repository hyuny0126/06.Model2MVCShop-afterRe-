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


//==> ��ǰ���� Controller
@Controller
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method ���� ����
		
	public ProductController(){
		System.out.println("���δ�Ʈ ��Ʈ�ѷ� ������"+this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml ���� �Ұ�
	//==> �Ʒ��� �ΰ��� �ּ��� Ǯ�� �ǹ̸� Ȯ�� �Ұ�
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	//��ǰ���
	@RequestMapping("/addProduct.do")
	public String addProduct( @ModelAttribute("product") Product product) throws Exception {

		System.out.println("/addProduct.do");
		//Business Logic
		product.setManuDate(product.getManuDate().replaceAll("-", ""));
		productService.addProduct(product);
		
		return "forward:/product/addProductAfter.jsp";
	}
	
	//��ǰ��ȸ
	@RequestMapping("/getProduct.do")
	public String getProduct( @RequestParam("prodNo") String prodNo ,
														Model model,
														HttpServletRequest request,
														HttpServletResponse response) throws Exception {
		
		System.out.println("/getProduct.do");
		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model �� View ����
		model.addAttribute("product", product);
		
		//��Ű�� ���� ��ǰ ��ȣ
		prodNo = ""+product.getProdNo();
		//cookie "history"Ű���� �������� ������  // ����ؼ� ����� // ���� ���� �ʿ� sum +=a, ����, + ����, 
		String sumProNo ="";
		Cookie[] cookies = request.getCookies(); //��Ű�� �������°���
		//��Ʈ��, ��Ʈ��, �����ֱ� ����
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
		//��Ű ����  
		
		cookie= new Cookie("history",prodNo);
		response.addCookie(cookie);

		return "forward:/product/getProduct.jsp";
	}
	
	///��ǰ���� �� ȭ�� ��û
	@RequestMapping("/updateProductView.do")
	public String updateProductView( @RequestParam("prodNo") String prodNo , Model model ) throws Exception{

		System.out.println("/updateProductView.do");
		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model �� View ����
		model.addAttribute("product", product);
		
		return "forward:/product/updateProductView.jsp";
	}
	
	///��ǰ���� ��û
	@RequestMapping("/updateProduct.do")
	public String updateProduct( @ModelAttribute("product") Product product , 
															Model model , 
															HttpSession session) throws Exception{

		System.out.println("/updateProduct.do");
		//Business Logic
		 productService.updateProduct(product);
		
		return "redirect:/getProduct.do?prodNo="+product.getProdNo()+"&menu=manage";
	}
	
	// ��ǰ����Ʈ
	@RequestMapping("/listProduct.do")
	public String listProduct( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/listProduct.do");
		
		System.out.println("��ġ Ȯ��"+search.getListOrderby());
		System.out.println("11111"+search);
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		System.out.println("22222"+search);
		// Business logic ����
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		System.out.println("�� �����Գ�");
		// Model �� View ����
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		String result = "";
		System.out.println("�޴�üũ"+request.getParameter("menu"));
		if (request.getParameter("menu").equals("search")) {
			result ="forward:/product/listProductSerch.jsp";
		}else {
			result ="forward:/product/listProductManage.jsp";
		}
		System.out.println("111111111111"+result);
		return result;
	}
}