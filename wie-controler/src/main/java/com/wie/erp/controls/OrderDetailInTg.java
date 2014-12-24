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

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Action;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IBillDetailService;
import com.wie.erp.biz.IProductService;
import com.wie.erp.biz.IPurchaseService;
import com.wie.erp.model.Bill;
import com.wie.erp.model.BillDetail;
import com.wie.erp.model.Purchase;
import com.wie.erp.model.PurchaseDetail;
import com.wie.framework.controls.struts2.BaseTg;


/** 
  * @ClassName: DictionaryTg 
  * @Description: 系统字典控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("orderDetailInControl")
public class OrderDetailInTg extends BaseTg {
	/** 
	  * @Fields dictionaryService : IDictionaryService业务层接口注入
	  */ 
	@Autowired
	private IBillDetailService billDetailService;
	
	@Autowired
	private IProductService productService;
	
	/** 
	  * @Fields dic : struts2.0接受前台信息（domain接受）
	  */ 
	private BillDetail billDetail;

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
		List list=productService.getPur4MsSQL();
		String[] params={"productId","productName"};
		List lists = doListObj(list,params);	
		JSONArray jsonArray = JSONArray.fromObject(lists);
		request.setAttribute("products", jsonArray.toString());
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
		if(billDetail==null)
			billDetail=new BillDetail();
        String billId=request.getParameter("billId");
        Bill bill=new Bill();
        bill.setBillId(billId);
        billDetail.setBill(bill);
		Pagination pagination = billDetailService.getPageList(billDetail, page, rows,sort,order);
		List<BillDetail> list = pagination.getList();
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
		BillDetail billDetail = this.billDetailService.findById(id);
		List<BillDetail> list = new ArrayList<BillDetail>();
		if(null != billDetail){
			list.add(billDetail);
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
	private List<Map<String, String>> doList(List<BillDetail> list) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		//dic.setCreateTime(format.format(new Date()));
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				BillDetail billDetail = list.get(i);
				map.put("billNo", billDetail.getBill().getBillNo());
				map.put("productName", billDetail.getProduct().getProductName());
				map.put("quantity", billDetail.getQuantity()+"");
				map.put("price", billDetail.getPrice()+"");
				map.put("discount", billDetail.getDiscount()+"");
				map.put("billEntry", billDetail.getBillEntry());
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

	
	
	public BillDetail getBillDetail() {
		return billDetail;
	}

	public void setBillDetail(BillDetail billDetail) {
		this.billDetail = billDetail;
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
