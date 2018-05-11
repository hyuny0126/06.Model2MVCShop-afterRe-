package com.model2.mvc.web.product;

import java.io.File;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.CookieGenerator;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


//==> ��ǰ���� Controller
@Controller
@RequestMapping("/product/*")
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
	//@RequestMapping("/addProduct.do")
	@RequestMapping( value="addProduct", method=RequestMethod.POST )
	public String addProduct( @ModelAttribute("product") Product product, 
																HttpServletRequest request, 
																HttpServletResponse response,
																@RequestParam("file")MultipartFile file) throws Exception {

		System.out.println("/product/addProduct :POST");
		product.setManuDate(product.getManuDate().replaceAll("-", ""));
		
		//���� ���ε� �ϱ� ���ؼ� �ʿ��Ѱ� 
		File f= new File("C:\\Users\\Bit\\git\\06MVCShop(afterRe)\\06.Model2MVCShop(Presentation+BusinessLogic)\\WebContent\\images\\uploadFiles\\"+file.getOriginalFilename());
		file.transferTo(f);
		product.setFileName(file.getOriginalFilename());
		
		productService.addProduct(product);
		//Business Logic
		
		/*
		if(FileUpload.isMultipartContent(request)) {
			String temDir ="C:\\workspace\\07.Model2MVCShop(URI)fileTest\\WebContent\\images\\uploadFiles\\";
			
			DiskFileUpload fileUpload = new DiskFileUpload(); //���� ���ε� �ڵ鷯 ����
			fileUpload.setRepositoryPath(temDir);
			fileUpload.setSizeMax(1024*1024*10);
			fileUpload.setSizeThreshold(124*100);
			
			if(request.getContentLength()<fileUpload.getSizeMax()) {
				
				StringTokenizer token =null;
				
				List fileItemList = fileUpload.parseRequest(request);
				
				int size = fileItemList.size();
				
				for (int i = 0; i < size; i++) {
					FileItem fileItem = (FileItem)fileItemList.get(i);
					if(fileItem.isFormField()) {
						if(fileItem.getFieldName().equals("manuDate")) {
							token = new StringTokenizer(fileItem.getString("euc-kr"),"-");
							String manuDate = token.nextToken() + token.nextToken() + token.nextToken();
							product.setManuDate(manuDate);
						}
						else if(fileItem.getFieldName().equals("prodName")) {
							product.setProdName(fileItem.getString("euc-kr"));
						}
						else if(fileItem.getFieldName().equals("prodDetail")) {
							product.setProdDetail(fileItem.getString("euc-kr"));
						}
						else if(fileItem.getFieldName().equals("price")) {
							product.setPrice(Integer.parseInt(fileItem.getString("euc-kr")));
						}
						else if(fileItem.getFieldName().equals("quantity")) {
							product.setQuantity(Integer.parseInt(fileItem.getString("euc-kr")));
						}
					}else { //���������̸�
						if(fileItem.getSize()>0){
							int idx = fileItem.getName().lastIndexOf("\\");
							if(idx ==-1) {
								idx = fileItem.getName().lastIndexOf("/");
							}
							String fileName = fileItem.getName().substring(idx+1);
							product.setFileName(fileName);
							try {
								File uploadedFile = new File(temDir,fileName);
								fileItem.write(uploadedFile);
							}catch (IOException e) {
								System.out.println(e);
							}
									
						} else{
									product.setFileName("../../images/empty.GIF");
						}//���� ���ε� �ȿ��� 
					}		
				} // ���Ͼ��ε� ����
				
					String menuDate = request.getParameter("menuDate")	;
					product.setManuDate(product.getManuDate().replaceAll("-", ""));
					
					productService.addProduct(product);
					request.setAttribute("product", product);
					
			}	else {
						int overSize =(request.getContentLength()/1000000);
						System.out.println("<script>alert('������ ũ��� 1MB �����Դϴ�. �ø��� ���� �뷮��"+overSize +"MB�Դϴ�");
						System.out.println("history.back();</script>");
					}
					
				
			}else {
				System.out.println("���ڵ� ������ Ÿ���� multipart/form-data�� �ƴմϴ�");
			}

			*/
			return "forward:/product/addProductAfter.jsp";
		
		
	
	} // ��ǰ��� �� 
	
	//��ǰ��ȸ
	//@RequestMapping("/getProduct.do")
	@RequestMapping(value="getProduct")
	public String getProduct( @RequestParam("prodNo") String prodNo ,
														Model model,
														HttpServletRequest request,
														HttpServletResponse response) throws Exception {
		
		System.out.println("/product/getProduct : GET / POST");
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
					System.out.println("��Ű Ȯ���� ����333 "+sumProNo);
				}//end of if
			}//end of for
		}//end of if
		prodNo = sumProNo+prodNo+",";
		//��Ű ����  
		
		/*
		 cookie= new Cookie("history",prodNo);
		response.addCookie(cookie);
		
		*/
		
		CookieGenerator cg = new CookieGenerator();

		cg.setCookieName("history");
		cg.addCookie(response, prodNo);

		return "forward:/product/getProduct.jsp";
	}
	
	///��ǰ���� �� ȭ�� ��û
	//@RequestMapping("/updateProductView.do")
	@RequestMapping(value="updateProduct" ,method=RequestMethod.GET)
	public String updateProduct( @RequestParam("prodNo") String prodNo , Model model ) throws Exception{

		System.out.println("���� : /product/updateProduct : GET ����/updateProductView.do");
		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model �� View ����
		model.addAttribute("product", product);
		
		return "forward:/product/updateProductView.jsp";
	}
	
	///��ǰ���� ��û
	//@RequestMapping("/updateProduct.do")
	@RequestMapping(value="updateProduct" ,method=RequestMethod.POST)
	public String updateProduct( @ModelAttribute("product") Product product , 
															Model model , 
															HttpSession session,
															@RequestParam("file")MultipartFile file) throws Exception{

		System.out.println("���� : /product/updateProduct : POST ����/updateProduct.do");
		//���� ���ε� �ϱ� ���ؼ� �ʿ��Ѱ� 
		File f= new File("C:\\Users\\Bit\\git\\06MVCShop(afterRe)\\06.Model2MVCShop(Presentation+BusinessLogic)\\WebContent\\images\\uploadFiles\\"+file.getOriginalFilename());
		file.transferTo(f);
		product.setFileName(file.getOriginalFilename());
		
		//Business Logic
		 productService.updateProduct(product);
		
		return "redirect:/product/getProduct?prodNo="+product.getProdNo()+"&menu=manage";
	}
	
	// ��ǰ����Ʈ
	//@RequestMapping("/listProduct.do")
	@RequestMapping( value="listProduct")
	public String listProduct( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/product/listProduct : GET / POST");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic ����
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model �� View ����
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