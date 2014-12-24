package com.wie.erp.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wie.erp.model.Order;
import com.wie.erp.model.OrderDetail;
import com.wie.erp.model.Product;
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.permissions.model.Users;


/** 
  * @ClassName: DictionaryTg 
  * @Description: 系统字典控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("orderInControl")
public class OrderInTg extends BaseTg {
	/** 
	  * @Fields dictionaryService : IDictionaryService业务层接口注入
	  */ 
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderDetailService orderDetailService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IIntercourseService intercourseService;
	@Autowired
	private IStoreService storeService;
	
	/** 
	  * @Fields dic : struts2.0接受前台信息（domain接受）
	  */ 
	private Order orderIn;

	
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
		String orderNumber=request.getParameter("code");
		orderIn.setOrderNumber(orderNumber);
		String storeId =request.getParameter("storeId");
		String userId=(String)request.getSession().getAttribute("userId");
		String storeIds=storeService.getUserStores(userId,storeId);
		orderIn.setStoreId(storeIds);
		Pagination pagination=orderService.getPageList(orderIn, page, rows, sort,order);
		List<Order> list = pagination.getList();
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
		if(orderIn==null)orderIn=new Order();
		String orderId=request.getParameter("orderId");
		orderIn = orderService.findById(orderId);
		List<OrderDetail> detailist=orderDetailService.findList("from OrderDetail d where d.orderId='"+orderId+"'");
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		for(OrderDetail orderDetail: detailist){
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("productId", orderDetail.getProductId());
			Product product = productService.findById(orderDetail.getProductId());
			if (product != null){
				item.put("productNO", product.getGoodNumber());
				item.put("productName", product.getProductName());
				item.put("icon", product.getIcon());
				item.put("storeId", product.getStoreId());
				item.put("productNo", product.getProductNo());
				item.put("spelling", product.getSpelling());
				item.put("unit", product.getUnit());
			}
			item.put("totalCount", orderDetail.getQuantity());
			item.put("totalPrice", orderDetail.getPrice());
			item.put("billCost",orderDetail.getPrice().multiply(orderDetail.getQuantity()));
			item.put("detailId", orderDetail.getId());
			items.add(item);
		}
		
		List<Map<String,Object>> footer = new ArrayList<Map<String,Object>>();
		Map<String,Object> fitem = new HashMap<String,Object>();
		fitem.put("productNo", "合计");
		fitem.put("billCount", orderIn.getTotalCount());
		fitem.put("billCost", orderIn.getTotalPrice());
		footer.add(fitem);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total", items.size());
		result.put("rows", items);
		result.put("footer", footer);
		returnJsion(JSONObject.fromObject(result).toString(), response);
	}
	
	
	public String create() {
		if(orderIn==null)orderIn=new Order();
		orderIn.setOrderNumber("单据编号由系统自动生成");
		orderIn.setCreatedt(new java.sql.Date(System.currentTimeMillis()));
		request.setAttribute("editable", true);
		request.setAttribute("checkable", false);
		request.setAttribute("orderIn", orderIn);
		request.setAttribute("detailUrl", "/orderIn/getInitDetailsOrderIn.tg");
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
		orderIn.setOrderNumber("JNDD"+sdf.format(date));
		orderIn.setCreateBy(createBy);
		orderIn.setCreatedt(new java.sql.Timestamp(System.currentTimeMillis()));
		orderIn.setStatus(0);
		boolean flag=true;
		Map<String,Object> result=orderService.saveItem(orderIn,detailmap,flag);
		String jsonstr=JSONObject.fromObject(result).toString();
		response.setContentType("text/plain;charset=utf-8");
		returnJsion(jsonstr, response);
	}
	
	public String edit(){
		Subject subject=SecurityUtils.getSubject();
		String userId= (String) subject.getSession().getAttribute("userId");
		Order orderIn = orderService.findById(id);
		if (orderIn.getStatus() == 0 && orderIn.getCreateBy().getId().equals(userId)){
			request.setAttribute("editable", true);
		}
		if (orderIn.getStatus() == 0){
			request.setAttribute("checkable", true);
		}
		request.setAttribute("orderIn", orderIn);
		request.setAttribute("detailUrl", "/orderIn/getBillDetailsOrderIn.tg?orderId="+orderIn.getOrderId().toUpperCase());
//		System.err.println("/orderIn/getBillDetailsOrderIn.tg?orderId="+orderIn.getOrderId());
		return "edit";
	}
	
	public void check() {
		orderIn=orderService.findById(id);
		Subject subject=SecurityUtils.getSubject();
		String userId= (String) subject.getSession().getAttribute("userId");
		Users checkBy=new Users();
		checkBy.setId(userId);
		orderIn.setCheckBy(checkBy);
		orderIn.setCheckDate(new java.sql.Timestamp(System.currentTimeMillis()));
		String ok = orderService.checkOrder(orderIn);
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
//		user = (User)rbac.getCurrentUser();
		
		Map<String,Object> result = new HashMap<String,Object>();
		orderIn=orderService.findById(id);
//		Bill item = Bill.find(Bill.class, id);
		if (orderIn.getStatus() == 0){
			if(orderIn.getCreateBy()!=null) {
				if (!orderIn.getCreateBy().getId().equals(userId)) {
					result.put("failure", true);
					result.put("msg", "对不起，你没有权限删除该单据。");
				} else {
					List<OrderDetail> orderDetails = orderDetailService.findList("from OrderDetail od where od.orderId='" + id + "'");
					orderService.deleteOrderById(id, orderDetails);
					result.put("success", true);
				}
			}else{
				List<OrderDetail> orderDetails = orderDetailService.findList("from OrderDetail od where od.orderId='" + id + "'");
				orderService.deleteOrderById(id, orderDetails);
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
		orderIn.setOrderId(id);
//		orderIn = Order.updateModel(orderIn, params);
		Users user=new Users();
		user.setId(userId);
		orderIn.setCreateBy(user);
		orderIn.setCreatedt(new java.sql.Timestamp(System.currentTimeMillis()));
		boolean flag=false;
		Map<String,Object> result = orderService.saveItem(orderIn, detailmap,flag);
		response.setContentType("text/plain;charset=utf-8");
		returnJsion(JSONObject.fromObject(result).toString(), response);
	}
	
	public void test()
	{
		orderService.test();
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
	private List<Map<String, String>> doList(List<Order> list) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		JsonConfig config = new JsonConfig();  
		 config.setExcludes(new String[]{"groups","password","address","age","birthday","education","email","employeddate","phone","position",
				 "registerdate","sex","status","storeId","usertype","jobmember","lastlogoffdate","lastlogondate","lastlogonip","store"});
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				Order orderIn = list.get(i);
//				Intercourse ics=intercourseService.findById(orderIn.getIntercourseId());
				map.put("orderNumber", orderIn.getOrderNumber());
				System.err.println("json:"+JSONObject.fromObject(orderIn.getCreateBy(),config)+"");
				map.put("createBy", JSONObject.fromObject(orderIn.getCreateBy(),config)+"");
				map.put("createdt", orderIn.getCreatedt()+"");
				map.put("checkBy", JSONObject.fromObject(orderIn.getCheckBy(),config)+"");
				map.put("checkDate", orderIn.getCheckDate()+"");
				map.put("storeId", orderIn.getStoreId());
				map.put("remark", orderIn.getRemark());
				map.put("intercourseName", orderIn.getIntercourse()==null?"":orderIn.getIntercourse().getShortName());
				map.put("totalCount", orderIn.getTotalCount()+"");
				map.put("totalPrice", orderIn.getTotalPrice()+"");
				map.put("status", orderIn.getStatus()+"");
				map.put("orderId", orderIn.getOrderId());
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


	public Order getOrderIn() {
		return orderIn;
	}

	public void setOrderIn(Order orderIn) {
		this.orderIn = orderIn;
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
