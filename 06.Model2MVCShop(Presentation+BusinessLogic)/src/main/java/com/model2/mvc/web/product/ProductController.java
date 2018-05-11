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


//==> 상품관리 Controller
@Controller
@RequestMapping("/product/*")
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
	//@RequestMapping("/addProduct.do")
	@RequestMapping( value="addProduct", method=RequestMethod.POST )
	public String addProduct( @ModelAttribute("product") Product product, 
																HttpServletRequest request, 
																HttpServletResponse response,
																@RequestParam("file")MultipartFile file) throws Exception {

		System.out.println("/product/addProduct :POST");
		product.setManuDate(product.getManuDate().replaceAll("-", ""));
		
		//파일 업로드 하기 위해서 필요한것 
		File f= new File("C:\\Users\\Bit\\git\\06MVCShop(afterRe)\\06.Model2MVCShop(Presentation+BusinessLogic)\\WebContent\\images\\uploadFiles\\"+file.getOriginalFilename());
		file.transferTo(f);
		product.setFileName(file.getOriginalFilename());
		
		productService.addProduct(product);
		//Business Logic
		
		/*
		if(FileUpload.isMultipartContent(request)) {
			String temDir ="C:\\workspace\\07.Model2MVCShop(URI)fileTest\\WebContent\\images\\uploadFiles\\";
			
			DiskFileUpload fileUpload = new DiskFileUpload(); //파일 업로드 핸들러 생성
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
					}else { //파일형식이면
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
						}//파일 업로드 안에서 
					}		
				} // 파일업로드 시작
				
					String menuDate = request.getParameter("menuDate")	;
					product.setManuDate(product.getManuDate().replaceAll("-", ""));
					
					productService.addProduct(product);
					request.setAttribute("product", product);
					
			}	else {
						int overSize =(request.getContentLength()/1000000);
						System.out.println("<script>alert('파일의 크기는 1MB 까지입니다. 올리신 파일 용량은"+overSize +"MB입니다");
						System.out.println("history.back();</script>");
					}
					
				
			}else {
				System.out.println("인코딩 데이터 타입이 multipart/form-data가 아닙니다");
			}

			*/
			return "forward:/product/addProductAfter.jsp";
		
		
	
	} // 상품등록 끝 
	
	//상품조회
	//@RequestMapping("/getProduct.do")
	@RequestMapping(value="getProduct")
	public String getProduct( @RequestParam("prodNo") String prodNo ,
														Model model,
														HttpServletRequest request,
														HttpServletResponse response) throws Exception {
		
		System.out.println("/product/getProduct : GET / POST");
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
					System.out.println("쿠키 확인을 위해333 "+sumProNo);
				}//end of if
			}//end of for
		}//end of if
		prodNo = sumProNo+prodNo+",";
		//쿠키 생성  
		
		/*
		 cookie= new Cookie("history",prodNo);
		response.addCookie(cookie);
		
		*/
		
		CookieGenerator cg = new CookieGenerator();

		cg.setCookieName("history");
		cg.addCookie(response, prodNo);

		return "forward:/product/getProduct.jsp";
	}
	
	///상품수정 전 화면 요청
	//@RequestMapping("/updateProductView.do")
	@RequestMapping(value="updateProduct" ,method=RequestMethod.GET)
	public String updateProduct( @RequestParam("prodNo") String prodNo , Model model ) throws Exception{

		System.out.println("지금 : /product/updateProduct : GET 예전/updateProductView.do");
		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/updateProductView.jsp";
	}
	
	///상품수정 요청
	//@RequestMapping("/updateProduct.do")
	@RequestMapping(value="updateProduct" ,method=RequestMethod.POST)
	public String updateProduct( @ModelAttribute("product") Product product , 
															Model model , 
															HttpSession session,
															@RequestParam("file")MultipartFile file) throws Exception{

		System.out.println("지금 : /product/updateProduct : POST 예전/updateProduct.do");
		//파일 업로드 하기 위해서 필요한것 
		File f= new File("C:\\Users\\Bit\\git\\06MVCShop(afterRe)\\06.Model2MVCShop(Presentation+BusinessLogic)\\WebContent\\images\\uploadFiles\\"+file.getOriginalFilename());
		file.transferTo(f);
		product.setFileName(file.getOriginalFilename());
		
		//Business Logic
		 productService.updateProduct(product);
		
		return "redirect:/product/getProduct?prodNo="+product.getProdNo()+"&menu=manage";
	}
	
	// 상품리스트
	//@RequestMapping("/listProduct.do")
	@RequestMapping( value="listProduct")
	public String listProduct( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/product/listProduct : GET / POST");
		
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