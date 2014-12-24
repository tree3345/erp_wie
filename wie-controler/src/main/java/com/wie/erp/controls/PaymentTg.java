package com.wie.erp.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wie.erp.biz.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Action;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Intercourse;
import com.wie.erp.model.Payment;
import com.wie.erp.model.PaymentDetail;
import com.wie.erp.model.Product;
import com.wie.erp.model.Purchase;
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.permissions.model.Users;


/** 
  * @ClassName: DictionaryTg 
  * @Description: 系统字典控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("paymentControl")
public class PaymentTg extends BaseTg {
	/** 
	  * @Fields dictionaryService : IDictionaryService业务层接口注入
	  */ 
	@Autowired
	private IPaymentService paymentService;
	@Autowired
	private IPaymentDetailService paymentDetailService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IIntercourseService intercourseService;
	@Autowired
	private IPurchaseService purchaseService;
	@Autowired
	private IWarehouseDetailService warehouseDetailService;
	@Autowired
	private IStoreService storeService;
	/** 
	  * @Fields dic : struts2.0接受前台信息（domain接受）
	  */ 
	private Payment payment;

	
	/** 
	  * @Fields id : 删除，更新所需要的id，struts2.0接受
	  */ 
	private String id;

	/** 
	  * @Title: index 
	  * @param @return
	  * @return String
	  * @throws 
	  */
	public String index(){
		String flag="0";
		if(request.getParameter("flag")!=null)
		flag=request.getParameter("flag");
		
		request.setAttribute("flag", flag);
		return Action.SUCCESS;
	}
	public void getItems(){
		if(payment==null)payment=new Payment();
		String orderNumber=request.getParameter("code");
		payment.setCode(orderNumber);
		String storeId=request.getParameter("storeId");
		String userId=(String)request.getSession().getAttribute("userId");
		String storeIds=storeService.getUserStores(userId,storeId);
		payment.setStoreId(storeIds);
		Pagination pagination=paymentService.getPageList(payment, page, rows, sort,order);
		List<Payment> list = pagination.getList();
		List<Map<String, String>> lists = doList(list);	
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + pagination.getTotalCount() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr,response);
	}
	public void getInitDetails(){
		List<Map<String,Object>> footer = new ArrayList<Map<String,Object>>();
		Map<String,Object> fitem = new HashMap<String,Object>();
		fitem.put("name", "合计");
		fitem.put("billCount", 0);
		fitem.put("billCost", 0);
		footer.add(fitem);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total", 0);
		result.put("rows", new Object[]{});
		result.put("footer", footer);
		returnJsion(JSONObject.fromObject(result).toString(), response);
	}
	public void getBillDetails(){
		if(payment==null)payment=new Payment();
		String paymentId=request.getParameter("paymentId");
		System.err.println(paymentId);
		payment = paymentService.findById(paymentId);
		List<PaymentDetail> detailist=paymentDetailService.findList("from PaymentDetail d where d.paymentId='"+paymentId+"'");
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		for(PaymentDetail paymentDetail: detailist){
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("productId", paymentDetail.getProductId());
			Product product = productService.findById(paymentDetail.getProductId());
			if (product != null){
				item.put("productNO", product.getGoodNumber());
				item.put("productName", product.getProductName());
				item.put("icon", product.getIcon());
				item.put("productNo", product.getProductNo());
				item.put("spelling", product.getSpelling());
				item.put("unit", product.getUnit());
			}
			item.put("totalCount", paymentDetail.getQuantity());
			item.put("totalPrice", paymentDetail.getPrice());
			item.put("paid", paymentDetail.getPaid());
			item.put("unpaid", paymentDetail.getUnpaid());
			item.put("billCost",paymentDetail.getPrice().multiply(paymentDetail.getQuantity()) );
			item.put("detailId", paymentDetail.getId());
			items.add(item);
		}
		
		List<Map<String,Object>> footer = new ArrayList<Map<String,Object>>();
		Map<String,Object> fitem = new HashMap<String,Object>();
		fitem.put("name", "合计");
		fitem.put("billCount", payment.getTotalCount());
		fitem.put("billCost", payment.getTotalPrice());
		fitem.put("paid", payment.getPaid());
		fitem.put("unpaid", payment.getUnpaid());
		footer.add(fitem);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total", items.size());
		result.put("rows", items);
		result.put("footer", footer);
		returnJsion(JSONObject.fromObject(result).toString(), response);
	}
	
	
	public String create() {
		if(payment==null)payment=new Payment();
		payment.setCode("单据编号由系统自动生成");
		payment.setIndt(new java.sql.Date(System.currentTimeMillis()));
		request.setAttribute("editable", true);
		request.setAttribute("checkable", false);
		request.setAttribute("payment", payment);
		request.setAttribute("detailUrl", "/payment/getInitDetailsPayment.tg");
		return "create";
	}
	public void save(){
		String inserted=request.getParameter("inserted");
		String deleted=request.getParameter("deleted");
		String updated=request.getParameter("updated");
		Map<String,String> detailmap=new HashMap<String,String>();
		detailmap.put("inserted", inserted);
		detailmap.put("deleted", deleted);
		detailmap.put("updated", updated);
		Subject subject=SecurityUtils.getSubject();
		String userId= (String) subject.getSession().getAttribute("userId");
		Users createBy=new Users();
		createBy.setId(userId);
		Date date=new Date();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMddHHmmss" );
		payment.setCode("JNRK"+sdf.format(date));
		payment.setInby(createBy);
		payment.setIndt(new java.sql.Timestamp(System.currentTimeMillis()));
		payment.setStatus(0);
		if(payment.getPurchaseId().equals(""))
			payment.setPurchaseId(null);
		boolean flag=true;
		Map<String,Object> result=paymentService.saveItem(payment,detailmap,flag);
		String jsonstr=JSONObject.fromObject(result).toString();
		response.setContentType("text/plain;charset=utf-8");
		returnJsion(jsonstr, response);
	}
	
	public String edit(){
		Subject subject=SecurityUtils.getSubject();
		String userId= (String) subject.getSession().getAttribute("userId");
		Payment payment = paymentService.findById(id);
		if(payment.getInby()!=null){
			if (payment.getStatus() == 0 && payment.getInby().getId().equals(userId)){
				request.setAttribute("editable", true);
			}
		}
		if (payment.getStatus() == 0){
			request.setAttribute("checkable", true);
		}
		request.setAttribute("payment", payment);
		request.setAttribute("detailUrl", "/payment/getBillDetailsPayment.tg?paymentId="+payment.getPaymentId().toUpperCase());
//		System.err.println("/payment/getBillDetailspayment.tg?orderId="+payment.getOrderId());
		return "edit";
	}
	
	public void check() {
		payment=paymentService.findById(id);
		Subject subject=SecurityUtils.getSubject();
		String userId= (String) subject.getSession().getAttribute("userId");
		Users checkBy=new Users();
		checkBy.setId(userId);
		payment.setCheckBy(checkBy);
		payment.setCheckDate(new java.sql.Timestamp(System.currentTimeMillis()));
		String ok = paymentService.checkPayment(payment);
		Map<String,Object> result = new HashMap<String,Object>();
		if (ok.equals("ok")){
			result.put("success", true);
			result.put("id", id);
		} else {
			result.put("failure", true);
			result.put("msg", ok);
		}
		
		JSONObject jsonstr=JSONObject.fromObject(result);
		returnJsion(jsonstr.toString(), response);
	}
	
	public void destroy(){
		String userId=(String)request.getSession().getAttribute("userId"); 
		
		Map<String,Object> result = new HashMap<String,Object>();
		payment=paymentService.findById(id);
		if (payment.getStatus() == 0){
			if (payment.getInby().equals(userId)){
				result.put("failure", true);
				result.put("msg", "对不起，你没有权限删除该单据。");
			} else {
				List<PaymentDetail> paymentDetails=paymentDetailService.findList("from PaymentDetail od where od.paymentId='"+id+"'");
				paymentService.deletePaymentById(id,paymentDetails);
				result.put("success", true);
			}
		} else {
			result.put("failure", true);
			result.put("msg", "该单据已经审核，不能删除。");
		}
		JSONObject jsonstr=JSONObject.fromObject(result);
		returnJsion(jsonstr.toString(), response);
	}
	
	public void update(){
		String inserted=request.getParameter("inserted");
		String deleted=request.getParameter("deleted");
		String updated=request.getParameter("updated");
		Map<String,String> detailmap=new HashMap<String,String>();
		detailmap.put("inserted", inserted);
		detailmap.put("deleted", deleted);
		detailmap.put("updated", updated);
		Subject subject=SecurityUtils.getSubject();
		String userId= (String) subject.getSession().getAttribute("userId");
		payment.setPaymentId(id);
//		payment = Order.updateModel(payment, params);
		Users user=new Users();
		user.setId(userId);
		payment.setInby(user);
		payment.setIndt(new java.sql.Timestamp(System.currentTimeMillis()));
		boolean flag=false;
		Map<String,Object> result = paymentService.saveItem(payment, detailmap,flag);
		response.setContentType("text/plain;charset=utf-8");
		returnJsion(JSONObject.fromObject(result).toString(), response);
	}

	/**
	 * 财务盈亏
	 */
	public String plIndex(){
		return "pl";
	}

	public void profitLoss(){
		    Map<String,String> paramsMap=requestMap(request);
			Map<String,Object> map=paymentService.getProfitLoss(paramsMap,page,rows,sort,order);
			List<Object[]> list= (List<Object[]>) map.get("list");
			Integer totalCount = (Integer) map.get("totalCount");
			String[] params = new String[]{"productId", "productName", "quantity_b", "quantity_p","quantity_i","quantity_w","acount_b", "acount_p","acount_i","acount_w"};
			List<Map<String, String>> lists = doListObj(list, params);
			JSONArray jsonArray = JSONArray.fromObject(lists);
			String baseStr = "{\"total\":" + totalCount + ",\"rows\":";
			baseStr = baseStr + jsonArray.toString() + "}";
			returnJsion(baseStr, response);
	}
	private Map<String,String> requestMap(HttpServletRequest request){
		String storeId=request.getParameter("storeId");
		String userId=(String)request.getSession().getAttribute("userId");
		String storeIds=storeService.getUserStores(userId,storeId);
		String classId=request.getParameter("classId");
		String productName=request.getParameter("productName");
		String startTime=request.getParameter("startTime");
		String isBusiness=request.getParameter("isBusiness");
		Map<String,String> paramsMap=new HashMap<String, String>();
		paramsMap.put("storeId",storeIds);
		if(classId!=null)paramsMap.put("classId",classId);
		if(productName!=null)paramsMap.put("productName", productName);
		if(startTime!=null)paramsMap.put("startTime",startTime);
		if(isBusiness!=null)paramsMap.put("isBusiness",isBusiness);
		return paramsMap;
	}
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
	private List<Map<String, String>> doList(List<Payment> list) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		JsonConfig config = new JsonConfig();  
		 config.setExcludes(new String[]{"groups","password","address","age","birthday","education","email","employeddate","phone","position",
				 "registerdate","sex","status","storeId","usertype","jobmember","lastlogoffdate","lastlogondate","lastlogonip","store"});
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				Payment payment = list.get(i);
				Intercourse ics=intercourseService.findById(payment.getIntercourseId());
				Purchase purchase =purchaseService.findById(payment.getPurchaseId());
				map.put("code", payment.getCode());
				map.put("inby", JSONObject.fromObject(payment.getInby(),config)+"");
				map.put("indt", payment.getIndt()+"");
				map.put("checkBy", JSONObject.fromObject(payment.getCheckBy(),config)+"");
				map.put("checkDate", payment.getCheckDate()+"");
				map.put("storeId", payment.getStoreId());
				map.put("remark", payment.getRemark());
				map.put("intercourseName", ics==null?"":ics.getShortName());
				map.put("totalCount", payment.getTotalCount()+"");
				map.put("totalPrice", payment.getTotalPrice()+"");
				map.put("paid", payment.getPaid()+"");
				map.put("unpaid", payment.getUnpaid()+"");
				map.put("status", payment.getStatus()+"");
				map.put("orderNumber", purchase==null?"":purchase.getCode());
				map.put("paymentId", payment.getPaymentId());
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


	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
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

	
}
