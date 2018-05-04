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


//==> ���Ű��� Controller
@Controller
public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method ���� ����
		
	public PurchaseController(){
		System.out.println("��ü�̽� ��Ʈ�ѷ�������"+this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml ���� �Ұ�
	//==> �Ʒ��� �ΰ��� �ּ��� Ǯ�� �ǹ̸� Ȯ�� �Ұ�
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	//������ȭ�� 
	@RequestMapping("/addPurchaseView.do")
	public ModelAndView addUserView(	
																							HttpServletRequest request,
																							@ModelAttribute("product") Product product,
																							ModelAndView modelAndView) throws Exception {
		System.out.println("/addPurchaseView.do");
		int prodNo =Integer.parseInt(request.getParameter("prod_no"));
		
		 product= productService.getProduct(prodNo);

		// Model �� View ����
		modelAndView.setViewName("forward:/purchase/addPurchaseView.jsp");
		modelAndView.addObject("product",product);
		
		return modelAndView; 
		}
	
	//����ȭ��
	@RequestMapping("/addPurchase.do")
	public ModelAndView addUser( @ModelAttribute("purchase") Purchase purchase,
																				HttpSession session,
																				HttpServletRequest request,
																					ModelAndView modelAndView) throws Exception {

		System.out.println("/addPurchase.do");
		//Business Logic
		/// ��ü�̽� �ʵ忡 ��°��� (������ ���ǿ��� �̾Ƽ� ��ü ������ ,���δ�Ʈ�� ��ü �����ؼ� ��ǰ��ȣ �������� �־���, ��¥�� - ����), Ʈ���ڵ� 1 �ɾ��ִ°� ������ �ѱ�mapper.xml��, 
		User user =(User)session.getAttribute("user");
		purchase.setBuyer(user);
		String prodNo = request.getParameter("prodNo");
		Product product = new Product();
		product.setProdNo(Integer.parseInt(prodNo));
		purchase.setPurchaseProd(product);
		
		purchase.setDivyDate(purchase.getDivyDate().replaceAll("-", ""));
		
		purchaseService.addPurchase(purchase);
		
		// Model �� View ����
		modelAndView.setViewName("forward:/purchase/addPurchaseAfter.jsp");
		modelAndView.addObject("purchase",purchase);
		
		return modelAndView; 
	}
	
	//���� �� ��ȸ
	@RequestMapping("/getPurchase.do")
	public ModelAndView getUser( @RequestParam("tranNo") String tranNo , 
														Purchase purchase,
															ModelAndView modelAndView) throws Exception {
		
		System.out.println("/getPurchase.do");
		//Business Logic
		purchase= purchaseService.getPurchase(Integer.parseInt(tranNo));
		// Model �� View ����
		modelAndView.setViewName("forward:/purchase/getPurchase.jsp");
		modelAndView.addObject("purchase",purchase);
		
		return modelAndView;
	}
	
	//�������� ���� �� ȭ��
	@RequestMapping("/updatePurchaseView.do")
	public ModelAndView updateUserView( @RequestParam("tranNo") String tranNo,
																				ModelAndView modelAndView,
																				Purchase purchase
																				) throws Exception{

		System.out.println("/updatePurchaseView.do");
		//Business Logic
		purchase= purchaseService.getPurchase(Integer.parseInt("tranNo"));
		// Model �� View ����
		modelAndView.setViewName("forward:/user/updateUser.jsp");
		modelAndView.addObject("purchase", purchase);
		
		return modelAndView ;
	}
	
	//���� ��������
	@RequestMapping("/updatePurchase.do")
	public ModelAndView updateUser( @ModelAttribute("purchase") Purchase purchase ,
																	@Param("tranNo")String tranNo,
																	@Param("buyerId")String buyerId,
																	User user,
																	ModelAndView modelAndView) throws Exception{

		System.out.println("/updatePurchase.do");
		//Business Logic
		//���̵�(������ ���������� �־���ؼ�), Ʈ���ѹ�(��Ʈ���� ��Ʈ�� �ٲ㼭)�� ���Ķ���� �޾Ƽ� ��ü�̽��� �־��� 
		user.setUserId(buyerId);
		purchase.setBuyer(user);
		purchase.setTranNo(Integer.parseInt("tranNo")); 
		purchaseService.updatePurchase(purchase);

		// Model �� View ����
		modelAndView.setViewName( "redirect:/getPurchase.do?tranNo="+tranNo);
		modelAndView.addObject("purchase", purchase);
		return modelAndView;
	}
	
	//�����α���, ���Ÿ����ȸ���� ���ǵ��� ��������
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
		
		// Model �� View ����
		modelAndView.setViewName("forward:/listPurchase.do");
		modelAndView.addObject("purchase", purchase);
		return modelAndView;
	}
	
	//���� �α���, ��ǰ�������� ����ϱ� �������� 
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

		modelAndView.setViewName("forward:/listProduct.do");
	
		return modelAndView;
	}
	
	//���Ÿ����ȸ
	@RequestMapping("/listPurchase.do")
	public ModelAndView listUser( @ModelAttribute("search") Search search ,
												 		HttpServletRequest request,
												 		HttpSession session,
														ModelAndView modelAndView
			) throws Exception{
		
		System.out.println("/listPurchase.do");
		
		//��ġ
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		//���ǿ��� ���� ���̵� �̱�(���Ÿ����ȸ �������� �������̵� �ʿ�)
		User user=(User)session.getAttribute("user");
		String buyerId = user.getUserId();
		
		// Business logic ����
		Map<String , Object> map= purchaseService.getPurchaseList(search, buyerId);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model �� View ����
		modelAndView.setViewName("forward:/purchase/listPurchase.jsp");
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		
		List<Purchase> list = (List<Purchase>)map.get("list");
		System.out.println("Ʈ���ڵ� Ȯ��"+list.get(0).getTranCode());
		
		
		return modelAndView;
	}
}//end of class