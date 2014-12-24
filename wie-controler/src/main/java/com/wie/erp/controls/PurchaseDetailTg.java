package com.wie.erp.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.wie.erp.biz.IPurchaseDetailService;
import com.wie.erp.biz.IStoreService;
import com.wie.erp.model.Product;
import com.wie.erp.model.Purchase;
import com.wie.permissions.model.Users;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.Action;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IProductService;
import com.wie.erp.model.PurchaseDetail;
import com.wie.framework.controls.struts2.BaseTg;


/** 
  * @ClassName: DictionaryTg 
  * @Description: 系统字典控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("purchaseDetailControl")
public class PurchaseDetailTg extends BaseTg {
	/** 
	  * @Fields dictionaryService : IDictionaryService业务层接口注入
	  */ 
	@Autowired
	private IPurchaseDetailService purchaseDetailService;
	
	@Autowired
	private IProductService productService;
	@Autowired
	private IStoreService storeService;
	
	/** 
	  * @Fields dic : struts2.0接受前台信息（domain接受）
	  */ 
	private PurchaseDetail purchaseDetail;

	/** 
	  * @Fields nickName : 昵称
	  */ 
	private String nickName;
	
	/** 
	  * @Fields id : 删除，更新所需要的id，struts2.0接受
	  */ 
	private String id;

	/** 
	  * @Title: index 
	  * @Description: 系统字典首页
	  * @param @return
	  * @return String
	  * @throws 
	  */
	public String index(){
		/*List list=productService.getPur4MsSQL();
		String[] params={"productId","productName"};
		List lists = doListObj(list,params);	
		JSONArray jsonArray = JSONArray.fromObject(lists);
		request.setAttribute("products", jsonArray.toString());*/
		return Action.SUCCESS;
	}
	
	/** 
	  * @Title: getItems 
	  * @Description: 系统字典表所有常量信息
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void getItems(){
		if(purchaseDetail==null)
			purchaseDetail=new PurchaseDetail();
        String productId=request.getParameter("productId");
        String startTime=request.getParameter("startTime");
        String endTime =request.getParameter("endTime");
        String inby = request.getParameter("inby");
		String storeId=request.getParameter("storeId");
		String userId=(String)request.getSession().getAttribute("userId");
		String storeIds=storeService.getUserStores(userId,storeId);
        Product product = new Product();
        product.setProductId(productId);
        purchaseDetail.setProduct(product);
        Purchase purchase = new Purchase();
        purchase.setStoreId(storeIds);
        if(startTime!=null)purchase.setStartTime(startTime);
        if(endTime!=null) purchase.setEndTime(endTime);
        if(inby!=null&&!"".equals(inby)){
            Users inbyUser = new Users();
            inbyUser.setName(inby);
            purchase.setInby(inbyUser);
        }
        purchaseDetail.setPurchase(purchase);

		Pagination pagination = purchaseDetailService.getPageDetailList(purchaseDetail, page, rows, sort, order);
		List<PurchaseDetail> list = pagination.getList();
		List lists = doList(list);	
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + pagination.getTotalCount() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr,response);
	}

	
	

	/** 
	  * @Title: find 
	  * @Description: 查找一常量
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void find(){
		PurchaseDetail purchaseDetail = this.purchaseDetailService.findById(id);
		List<PurchaseDetail> list = new ArrayList<PurchaseDetail>();
		if(null != purchaseDetail){
			list.add(purchaseDetail);
		}
		JSONArray json = JSONArray.fromObject(list);
		returnJsion("{\"rows\":" +  json.toString() +"}", response);
	}

	
	/** 
	  * @Title: returnJsion 
	  * @Description:  解决json问题
	  * @param @param baseStr 拼好的jsion串
	  * @param @param response 
	  * @return void
	  * @throws 
	  */
	private void returnJsion(String baseStr, HttpServletResponse response) {
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(baseStr);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (out != null) {
				out.close();
			}
		}
	}

	/** 
	  * @Title: doList 
	  * @Description: 解决struts2.0domal接受参数的方法
	  * @param @param list 装Dictionarys的具体信息。
	  * @param @return
	  * @return List
	  * @throws 
	  */
	private List<Map<String, String>> doList(List<PurchaseDetail> list) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//dic.setCreateTime(format.format(new Date()));
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				PurchaseDetail purchaseDetail = list.get(i);
				map.put("purchase.inby.name", purchaseDetail.getPurchase().getInby().getName());
                map.put("price",purchaseDetail.getPrice()+"");
                map.put("quantity",purchaseDetail.getQuantity()+"");
				map.put("purchase.checkDate", format.format(purchaseDetail.getPurchase().getCheckDate()));
                map.put("purchase.code",purchaseDetail.getPurchase().getCode());
				lists.add(map);
			}
		}
		return lists;
	}

	private List doListObj(List<Object[]> list,String[] params) {
		List lists = new ArrayList();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				Object[] obj =(Object[]) list.get(i);
				for(int j=0;j<params.length;j++)
					map.put(params[j], obj[j]+"");
				lists.add(map);
			}
		}
		return lists;
	}

    public PurchaseDetail getPurchaseDetail() {
        return purchaseDetail;
    }

    public void setPurchaseDetail(PurchaseDetail purchaseDetail) {
        this.purchaseDetail = purchaseDetail;
    }

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}


	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}
	
}
