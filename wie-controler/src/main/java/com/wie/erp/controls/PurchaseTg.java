package com.wie.erp.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wie.erp.biz.*;
import com.wie.erp.model.*;
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
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.permissions.model.Users;


/** 
  * @ClassName: DictionaryTg 
  * @Description: 系统字典控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("purchaseControl")
public class PurchaseTg extends BaseTg {
	/** 
	  * @Fields dictionaryService : IDictionaryService业务层接口注入
	  */ 
	@Autowired
	private IPurchaseService purchaseService;
	@Autowired
	private IPurchaseDetailService purchaseDetailService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IIntercourseService intercourseService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IWarehouseDetailService warehouseDetailService;
	@Autowired
	private IStoreService storeService;
	/** 
	  * @Fields dic : struts2.0接受前台信息（domain接受）
	  */ 
	private Purchase purchase;

	
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
		String flag="0";
		if(request.getParameter("flag")!=null)
		flag=request.getParameter("flag");
		
		request.setAttribute("flag", flag);
		return Action.SUCCESS;
	}
	public void getItems(){
		if(purchase==null)purchase=new Purchase();
		String code=request.getParameter("code");
		purchase.setCode(code);
		String ordercode=request.getParameter("ordercode");
		Order orderimp=new Order();
		orderimp.setOrderNumber(ordercode);
		purchase.setOrder(orderimp);
		String storeId=request.getParameter("storeId");
		String userId=(String)request.getSession().getAttribute("userId");
		String storeIds=storeService.getUserStores(userId,storeId);
		purchase.setStoreId(storeIds);
		Pagination pagination=purchaseService.getPageList(purchase, page, rows, sort,order);
		List<Purchase> list = pagination.getList();
		List<Map<String, String>> lists = doList(list);	
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + pagination.getTotalCount() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr,response);
	}
	public void getInitDetails(){
		List<Map<String,Object>> footer = new ArrayList<Map<String,Object>>();
		Map<String,Object> fitem = new HashMap<String,Object>();
		fitem.put("productNo", "合计");
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
		if(purchase==null)purchase=new Purchase();
		String purchaseId=request.getParameter("purchaseId");
		//System.err.println(purchaseId);
		purchase = purchaseService.findById(purchaseId);
		List<PurchaseDetail> detailist=purchaseDetailService.findList("from PurchaseDetail d where d.purchase.purchaseId='"+purchaseId+"'");
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		for(PurchaseDetail purchaseDetail: detailist){
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("productId", purchaseDetail.getProduct().getProductId());
			Product product = purchaseDetail.getProduct();
			if (product != null){
				item.put("productNO", product.getGoodNumber());
				item.put("productName", product.getProductName());
				item.put("icon", product.getIcon());
				item.put("storeId", product.getStoreId());
				item.put("productNo", product.getProductNo());
				item.put("spelling", product.getSpelling());
				item.put("unit", product.getUnit());
			}
			item.put("totalCount", purchaseDetail.getQuantity());
			item.put("totalPrice", purchaseDetail.getPrice());
			item.put("billCost",purchaseDetail.getPrice().multiply(purchaseDetail.getQuantity()) );
			item.put("detailId", purchaseDetail.getId());
			items.add(item);
		}
		
		List<Map<String,Object>> footer = new ArrayList<Map<String,Object>>();
		Map<String,Object> fitem = new HashMap<String,Object>();
		fitem.put("productNo", "合计");
		fitem.put("billCount", purchase.getTotalCount());
		fitem.put("billCost", purchase.getTotalPrice());
		footer.add(fitem);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total", items.size());
		result.put("rows", items);
		result.put("footer", footer);
		returnJsion(JSONObject.fromObject(result).toString(), response);
	}
	
	
	public String create() {
		if(purchase==null)purchase=new Purchase();
		purchase.setCode("单据编号由系统自动生成");
		purchase.setIndt(new java.sql.Date(System.currentTimeMillis()));
		request.setAttribute("editable", true);
		request.setAttribute("checkable", false);
		request.setAttribute("purchase", purchase);
		request.setAttribute("detailUrl", "/purchase/getInitDetailsPurchase.tg");
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
		purchase.setCode("JNRK"+sdf.format(date));
		purchase.setInby(createBy);
		purchase.setIndt(new java.sql.Timestamp(System.currentTimeMillis()));
		purchase.setStatus(0);
		if(purchase.getOrder().getOrderId().equals("")){
			purchase.setOrder(null);
		}
		boolean flag=true;
		Map<String,Object> result=purchaseService.saveItem(purchase,detailmap,flag);
//		result.put("",)
		String jsonstr=JSONObject.fromObject(result).toString();
		response.setContentType("text/plain;charset=utf-8");
		returnJsion(jsonstr, response);
	}
	
	public String edit(){
		Subject subject=SecurityUtils.getSubject();
		String userId= (String) subject.getSession().getAttribute("userId");
		Purchase purchase = purchaseService.findById(id);
		if(purchase.getInby()!=null){
			if (purchase.getStatus() == 0 && purchase.getInby().getId().equals(userId)){
				request.setAttribute("editable", true);
			}
		}
		if (purchase.getStatus() == 0){
			request.setAttribute("checkable", true);
		}
		request.setAttribute("purchase", purchase);
		request.setAttribute("detailUrl", "/purchase/getBillDetailsPurchase.tg?purchaseId="+purchase.getPurchaseId().toUpperCase());
//		System.err.println("/purchase/getBillDetailspurchase.tg?orderId="+purchase.getOrderId());
		return "edit";
	}
	
	public void check() {
		purchase=purchaseService.findById(id);
		Subject subject=SecurityUtils.getSubject();
		String userId= (String) subject.getSession().getAttribute("userId");
		Users checkBy=new Users();
		checkBy.setId(userId);
		purchase.setCheckBy(checkBy);
		purchase.setCheckDate(new java.sql.Timestamp(System.currentTimeMillis()));
		Map<String,WarehouseDetail> warehouseDetails=warehouseDetailService.getStock4Now(purchase);
		String ok = purchaseService.checkPurchase(purchase,warehouseDetails);
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
		purchase=purchaseService.findById(id);
		if (purchase.getStatus() == 0){
			if(purchase.getInby()!=null) {
				if (!purchase.getInby().getId().equals(userId)) {
					result.put("failure", true);
					result.put("msg", "对不起，你没有权限删除该单据。");
				} else {
					List<PurchaseDetail> purchaseDetails = purchaseDetailService.findList("from PurchaseDetail od where od.purchase.purchaseId='" + id + "'");
					purchaseService.deletePurchaseById(id, purchaseDetails);
					result.put("success", true);
				}
			}else {
				List<PurchaseDetail> purchaseDetails = purchaseDetailService.findList("from PurchaseDetail od where od.purchase.purchaseId='" + id + "'");
				purchaseService.deletePurchaseById(id, purchaseDetails);
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
		purchase.setPurchaseId(id);
//		purchase = Order.updateModel(purchase, params);
		Users user=new Users();
		user.setId(userId);
		purchase.setInby(user);
		if(purchase.getOrder().getOrderId().equals(""))
			purchase.setOrder(null);
		purchase.setIndt(new java.sql.Timestamp(System.currentTimeMillis()));
		boolean flag=false;
		Map<String,Object> result = purchaseService.saveItem(purchase, detailmap,flag);
		response.setContentType("text/plain;charset=utf-8");
		returnJsion(JSONObject.fromObject(result).toString(), response);
	}
	
	 public void impOrder()
	   {
		   String orderId=request.getParameter("orderId");
		   String userId = (String) request.getSession().getAttribute("userId");
		   System.err.println(orderId);
		   if(this.purchaseService.saveFromOrder(orderId,userId)){
				returnJsion("{\"success\":\"true\"}",response);
			}else{
				returnJsion("{\"error\":\"true\"}",response);
			}
	   }
	//统计
		public String statistics(){
			return "statistics";
		}
		
		public void getItemsStatistics()
		{
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			if((!"".equals(startTime)&&startTime!=null)&&(!"".equals(endTime)&&endTime!=null)) {
				PurchaseDetail purchaseDetail = new PurchaseDetail();
				String productName = request.getParameter("productName");
				String inby = request.getParameter("inby");
				String storeId=request.getParameter("storeId");
				String userId=(String)request.getSession().getAttribute("userId");
				String storeIds=storeService.getUserStores(userId,storeId);
				//String storeId = (String) request.getSession().getAttribute("storeId");

				Product productCon = new Product();
				productCon.setProductName(productName);
				purchaseDetail.setProduct(productCon);
				Purchase pur = new Purchase();
				if (inby != null && !"".equals(inby)) {
					Users inbyUser = new Users();
					inbyUser.setName(inby);
					pur.setInby(inbyUser);
				}
				pur.setStoreId(storeIds);
				pur.setStartTime(startTime);
				pur.setEndTime(endTime);
				purchaseDetail.setPurchase(pur);
				List<Object[]> list = purchaseDetailService.getPageList(purchaseDetail, page, rows, sort, order);
				int totalcount = purchaseDetailService.findCountBySql(purchaseDetail);
				String[] params = new String[]
						{"productId", "productName", "unit", "dq", "account"};
				List<Map<String, String>> lists = doListObj(list, params);
				JSONArray jsonArray = JSONArray.fromObject(lists);
				String baseStr = "{\"total\":" + totalcount + ",\"rows\":";
				baseStr = baseStr + jsonArray.toString() + "}";
				returnJsion(baseStr, response);
			}
		}
		
		public String showCharts(){
			String productId=request.getParameter("productId");
			String startTime=request.getParameter("startTime");
			String endTime=request.getParameter("endTime");

			String storeId=request.getParameter("storeId");
			String userId=(String)request.getSession().getAttribute("userId");
			String storeIds=storeService.getUserStores(userId,storeId);
			String hql="from PurchaseDetail ind where ind.purchase.storeId in("+storeIds+")  and ind.product.productId='"+productId+"' and ind.purchase.status=1 ";
			if(!"".equals(startTime)&&startTime!=null)	
				hql+=" and ind.purchase.checkDate > '"+startTime.replace("_", " ")+"' ";
			if(!"".equals(endTime)&&endTime!=null)	
				hql+=" and ind.purchase.checkDate < '"+endTime.replace("_", " ")+"' ";
			hql	+=" order by ind.purchase.checkDate asc";
			List<PurchaseDetail> list=purchaseDetailService.findList(hql);
			String data="", data2="",productName="",unit="";
        
			for(PurchaseDetail psd:list)
			{
				Date date=psd.getPurchase().getCheckDate();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmm");
				String dateStr=format.format(date);
				int year=Integer.parseInt(dateStr.substring(0, 4));
				int month=Integer.parseInt(dateStr.substring(4, 6));
				int day=Integer.parseInt(dateStr.substring(6, 8));
				int hour=Integer.parseInt(dateStr.substring(9, 11));
				int min=Integer.parseInt(dateStr.substring(11, 13));
				data+="[Date.UTC("+year+", "+(month-1)+", "+(day)+","+(hour)+","+(min)+"), "+psd.getQuantity()+"],";
				data2+="[Date.UTC("+year+", "+(month-1)+", "+(day)+","+(hour)+","+(min)+"), "+psd.getPrice()+"],";
				productName=psd.getProduct().getProductName();
				unit=psd.getProduct().getUnit();
			}
			if(list.size()>0){
			  data="["+data.substring(0, data.length()-1)+"]";
			  data2="["+data2.substring(0, data2.length()-1)+"]";
			}
			
			request.setAttribute("data", data);
			request.setAttribute("data2", data2);
			request.setAttribute("unit", unit);
			request.setAttribute("productName", productName);
			return "showChart";
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
	private List<Map<String, String>> doList(List<Purchase> list) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		JsonConfig config = new JsonConfig();  
		 config.setExcludes(new String[]{"groups","password","address","age","birthday","education","email","employeddate","phone","position",
				 "registerdate","sex","status","storeId","usertype","jobmember","lastlogoffdate","lastlogondate","lastlogonip","store"});
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				Purchase purchase = list.get(i);
				Intercourse ics=intercourseService.findById(purchase.getIntercourseId());
				Order order =purchase.getOrder();
				map.put("code", purchase.getCode());
				map.put("inby", JSONObject.fromObject(purchase.getInby(),config)+"");
				map.put("indt", purchase.getIndt()+"");
				map.put("checkBy", JSONObject.fromObject(purchase.getCheckBy(),config)+"");
				map.put("checkDate", purchase.getCheckDate()+"");
				map.put("storeId", purchase.getStoreId());
				map.put("remark", purchase.getRemark());
				map.put("intercourseName", ics==null?"":ics.getShortName());
				map.put("totalCount", purchase.getTotalCount()+"");
				map.put("totalPrice", purchase.getTotalPrice()+"");
				map.put("status", purchase.getStatus()+"");
				map.put("order.orderNumber", order==null?"":order.getOrderNumber());
				map.put("purchaseId", purchase.getPurchaseId());
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


	public Purchase getPurchase() {
		return purchase;
	}
	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
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
