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


//==> ���Ű��� Controller
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
	
	
	///������ȭ�� 
	//@RequestMapping("/addPurchaseView.do")
	@RequestMapping(value="addPurchase",  method=RequestMethod.GET )
	public ModelAndView addPurchase(	
																							HttpServletRequest request,
																							@ModelAttribute("product") Product product,
																							ModelAndView modelAndView) throws Exception {
		
		System.out.println("/purchase/addPurchase : GET ::������ȭ�� ");
		int prodNo =Integer.parseInt(request.getParameter("prod_no"));
		 product= productService.getProduct(prodNo);
		 
		// Model �� View ����
		modelAndView.setViewName("forward:/purchase/addPurchaseView.jsp");
		modelAndView.addObject("product",product);
		return modelAndView; 
		}
	
	
	///����ȭ��
	//@RequestMapping("/addPurchase.do")
	@RequestMapping(value="addPurchase",  method=RequestMethod.POST )
	public ModelAndView addPurchase( @ModelAttribute("purchase") Purchase purchase,
																				Product product,
																				HttpSession session,
																				HttpServletRequest request,
																				ModelAndView modelAndView) throws Exception {

		System.out.println("/purchase/addPurchase : POST ");
		//Business Logic
		//��ü�̽� �ʵ忡 ��°��� (������ ���ǿ��� �̾Ƽ� ��ü ������ ,���δ�Ʈ�� ��ü �����ؼ� ��ǰ��ȣ �������� �־���, ��¥�� - ����), Ʈ���ڵ� 1 �ɾ��ִ°� ������ �ѱ�mapper.xml��, 
		User user =(User)session.getAttribute("user");
		purchase.setBuyer(user);
		
		String prodNo = request.getParameter("prodNo");
		product = productService.getProduct(Integer.parseInt(prodNo));
		
		//product.setProdNo(Integer.parseInt(prodNo));
		purchase.setPurchaseProd(product);
		
		purchase.setDivyDate(purchase.getDivyDate().replaceAll("-", ""));
		purchaseService.addPurchase(purchase);
		
		// ���� ���Ž� ���δ�Ʈ ���̺��� ���� ���� ,  ��ǰ��ȣ�� ������ ��ǰ���̺��
		int quantity =Integer.parseInt( request.getParameter("quantity"));
		
		// ���࿡ ���Ű����� ��ǰ������ ũ�ٸ� 
		if(quantity>product.getQuantity()) {
			modelAndView.setViewName("forward:/purchase/noProd.jsp");
			modelAndView.addObject("product",product);
		
		}else { //���Ű��� <= ��ǰ�� 
			modelAndView.setViewName("forward:/purchase/addPurchaseAfter.jsp");
			quantity = product.getQuantity() - quantity;  // ������������ - ���Ű��� 
			product.setQuantity(quantity);  // �ٽ� ���δ�Ʈ �����ε� ��Ƽ� ���δ�Ʈ ���̺� update
			productService.updateProduct(product);
		}	
		
		// Model �� View ����
		modelAndView.addObject("purchase",purchase);
		return modelAndView; 
	}
	
	
	///���� �� ��ȸ
	//@RequestMapping("/getPurchase.do")
	@RequestMapping(value="getPurchase",  method=RequestMethod.GET)
	public ModelAndView getPurchase( @RequestParam("tranNo") String tranNo , 
																				Purchase purchase,
																				ModelAndView modelAndView) throws Exception {
		
		System.out.println("purchase/getPurchase:: GET");
		//Business Logic
		purchase= purchaseService.getPurchase(Integer.parseInt(tranNo));
		int prodNO =purchase.getPurchaseProd().getProdNo(); //��ǰ�� �������� ���ؼ�
		Product product = productService.getProduct(prodNO);
		
		// Model �� View ����
		modelAndView.setViewName("forward:/purchase/getPurchase.jsp");
		modelAndView.addObject("purchase",purchase);
		modelAndView.addObject("product", product);
		return modelAndView;
	}
	
	///�������� ���� �� ȭ��
	//@RequestMapping("/updatePurchaseView.do")
	@RequestMapping(value="updatePurchase",method=RequestMethod.GET)
	public ModelAndView updatePurchase( @RequestParam("tranNo") String tranNo,
																												ModelAndView modelAndView) throws Exception{

		System.out.println("/purchase/updatePurchase ::GET");
		//Business Logic
		Purchase purchase= purchaseService.getPurchase(Integer.parseInt(tranNo));
		
		// Model �� View ����
		modelAndView.setViewName("forward:/purchase/updatePurchaseView.jsp");
		modelAndView.addObject("purchase", purchase);
		return modelAndView ;
	}
	
	///���� ��������
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
		
		// Model �� View ����
		modelAndView.setViewName( "redirect:/purchase/getPurchase?tranNo="+tranNo);
		return modelAndView;
	}
	
	///�����α���, ���Ÿ����ȸ���� ���ǵ��� �������� , ��ǰ��û�Ҷ�(��ǰ��û�� �ܼ� �ڵ庯����) 
	//@RequestMapping("/updateTranCode.do")
	@RequestMapping(value="updateTranCode" ,method=RequestMethod.GET)
	public ModelAndView updateTranCode(@Param("tranNo")String tranNo,
																								@Param("tranCode")String tranCode,
																								Purchase purchase,
																								ModelAndView modelAndView) throws Exception{
		
		System.out.println("/purchase/updateTranCode ::GET ::���� ���ǵ��� ����");
		//Business Logic
		purchase.setTranCode(tranCode);
		purchase.setTranNo(Integer.parseInt(tranNo));
		purchaseService.updateTranCode(purchase);
		
		// Model �� View ����
		modelAndView.setViewName("forward:/purchase/listPurchase.do");
		modelAndView.addObject("purchase", purchase);
		return modelAndView;
	}
	
	///���� �α���, ��ǰ�������� ����ϱ� �������� 
	//@RequestMapping("/updateTranCodeByProd.do")
	@RequestMapping(value="updateTranCodeByProd" ,method=RequestMethod.GET)
	public ModelAndView updateTranCodeByProd(@Param("tranNo")String tranNo,
																													@Param("tranCode")String tranCode,
																													Purchase purchase,
																													Product product,
																													ModelAndView modelAndView) throws Exception{
		
		System.out.println("/purchase/updateTranCodeByProd ::GET ::���� ����ϱ� ����");
		//Business Logic
		purchase.setTranCode(tranCode);
		purchase.setTranNo(Integer.parseInt(tranNo));
		purchaseService.updateTranCodeByProd(purchase);
		// Model �� View ����
		modelAndView.setViewName("forward:/purchase/listDeliveryManage");
		return modelAndView;
	}
	
	
	///�����α���, ���Ÿ����ȸ (���̾���̵�� �������̺� �˻�)
	//@RequestMapping("/listPurchase.do")
	@RequestMapping(value="listPurchase" )
	public ModelAndView listPurchase( @ModelAttribute("search") Search search ,
												 		HttpServletRequest request,
												 		HttpSession session,
														ModelAndView modelAndView) throws Exception{
		
		System.out.println("/purchase/listPurchase ::GET,POST");
		
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
		return modelAndView;
	}
	
	//////�߰����
	///���� �α���. ���, �������, ��ǰ ���� ��� ��ȸ(��ü �������̺� �˻�)
		//@RequestMapping("/listDeliveryManage.do")
		@RequestMapping(value="listDeliveryManage" )
		public ModelAndView listDeliveryManage( @ModelAttribute("search") Search search ,
													 		HttpServletRequest request,
													 		HttpSession session,
															ModelAndView modelAndView) throws Exception{
			
			System.out.println("/purchase/listDeliveryManage::GET,POST");
			
			//��ġ
			if(search.getCurrentPage() ==0 ){
				search.setCurrentPage(1);
			}
			search.setPageSize(pageSize);
			
			String buyerId=null;
			// Business logic ����
			Map<String , Object> map= purchaseService.getPurchaseList(search, buyerId);
			Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
			System.out.println(resultPage);
			
			// Model �� View ����
			modelAndView.setViewName("forward:/purchase/listDeliveryManage.jsp");
			modelAndView.addObject("list", map.get("list"));
			modelAndView.addObject("resultPage", resultPage);
			modelAndView.addObject("search", search);
			return modelAndView;
		}
		
		///�ֹ����, ��ǰ ���� �� ��ǰ���� �츮��
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
			//1.�������̺��� ����ڵ� �ٲٱ�, �������̺��� ���ż���, ��ǰ��ȣ ��� ����
			purchase.setTranCode(tranCode);
			int tranNoInt = Integer.parseInt(tranNo);
			purchase.setTranNo(tranNoInt);
			purchaseService.updateTranCodeByProd(purchase); // ���Ķ������ Ʈ���ڵ�� Ʈ���ѹ� ��ü�̽��� �־ �������̺� �ڵ庯���Ŵ
			
			purchase = purchaseService.getPurchase(tranNoInt);   //Ʈ���ѹ� �������� ��ü�̽� �����ͼ� (��ǰ��ȣ, ���ż���)
			int prodNo = purchase.getPurchaseProd().getProdNo();  //�׾ȿ� ��ǰ��ȣ�� ���� 
			int quantityTran = purchase.getQuantity(); //���������� ���ż���
			
			//2. ��ǰ���̺��� ��ǰ���� �ø��� (���� ��ǰ üũ �� +��Ű��)
			product = productService.getProduct(prodNo);  // �ٽ� ������ ��ǰ�� ���� ���� �˾ƾ���, ������ ���� ��ǰ��ȣ �������� ��ǰ ã�ƿ�
			int quantityProd = product.getQuantity();  //��ǰ��ȣ�� ������ ��ǰ��  �������
			quantityProd = quantityProd+quantityTran; // ��ǰ ��������� + ���ż���(��ǰ�߰ų� �ֹ����) 
			product.setQuantity(quantityProd);  // ����ؼ� �ٽ� ��ǰ�����ο� �ְ� �������� ����
			productService.updateProduct(product);
			
			// Model �� View ����
			User user =(User)session.getAttribute("user");        // �ֹ���Ҵ� ������ �Ѱ��̰�, ��ǰ������ ������ �Ѱ��̹Ƿ� ���ǿ��� ������ Ȯ�� �� �ش��������� ������
			
			if ( user.getRole().equals("admin") ){
				modelAndView.setViewName("forward:/purchase/listDeliveryManage");
			}else {
				modelAndView.setViewName("forward:/purchase/listPurchase");
			}
			return modelAndView;
			
		}
}//end of class