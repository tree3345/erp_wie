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
import com.wie.erp.model.Inventory;
import com.wie.erp.model.InventoryDetail;
import com.wie.erp.model.Product;
import com.wie.erp.model.WarehouseDetail;
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.permissions.model.Users;


/** 
  * @ClassName: InventoryTg 
  * @Description: 
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("inventoryControl")
public class InventoryTg extends BaseTg {
	/** 
	  * @Fields dictionaryService : IDictionaryService业务层接口注入
	  */ 
	@Autowired
	private IInventoryService inventoryService;
	@Autowired
	private IInventoryDetailService inventoryDetailService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IWarehouseDetailService warehouseDetailService;
	@Autowired
	private IStoreService storeService;
	
	/** 
	  * @Fields dic : struts2.0接受前台信息（domain接受）
	  */ 
	private Inventory inventory;

	
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
		return Action.SUCCESS;
	}
	
	public void getItems(){
		if(inventory==null)inventory=new Inventory();
		String code=request.getParameter("code");
		inventory.setCode(code);
		String userId=(String)request.getSession().getAttribute("userId");
		String storeId=request.getParameter("storeId");
		String storeIds=storeService.getUserStores(userId,storeId);
		inventory.setStoreId(storeIds);
		Pagination pagination=inventoryService.getPageList(inventory, page, rows, sort,order);
		List<Inventory> list = pagination.getList();
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
		if(inventory==null)inventory=new Inventory();
		String inventoryId=request.getParameter("inventoryId");
		System.err.println(inventoryId);
		inventory = inventoryService.findById(inventoryId);
		List<InventoryDetail> detailist=inventoryDetailService.findList("from InventoryDetail d where d.inventory.inventoryId='"+inventoryId+"'");
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		for(InventoryDetail inventoryDetail: detailist){
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("productId", inventoryDetail.getProduct().getProductId());
			Product product = inventoryDetail.getProduct();
			if (product != null){
				item.put("productNo", product.getGoodNumber());
				item.put("productName", product.getProductName());
				item.put("storeId", product.getStoreId());
				item.put("icon", product.getIcon());
				item.put("productNo", product.getProductNo());
				item.put("spelling", product.getSpelling());
				//item.put("purchasePrice", product.getProductPrice()==null?"0":product.getProductPrice().getPurchasePrice());
				item.put("unit", product.getUnit());
			}
			item.put("inventoryQuantity", inventoryDetail.getInventoryQuantity());
			item.put("warehouseQuantity", inventoryDetail.getWarehouseQuantity());
			item.put("billCost",inventoryDetail.getDamageQuantity());
            item.put("purchasePrice",inventoryDetail.getPrice()==null?"0":inventoryDetail.getPrice());
			item.put("damageSum", inventoryDetail.getDamageSum());
			item.put("detailId", inventoryDetail.getId());
			items.add(item);
		}
		
		List<Map<String,Object>> footer = new ArrayList<Map<String,Object>>();
		Map<String,Object> fitem = new HashMap<String,Object>();
		fitem.put("productNo", "合计");
		fitem.put("damageSum", inventory.getTotalSum());
		fitem.put("billCost", inventory.getTotalInventory());
		footer.add(fitem);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total", items.size());
		result.put("rows", items);
		result.put("footer", footer);
		returnJsion(JSONObject.fromObject(result).toString(), response);
	}
	
	
	public String create() {
		if(inventory==null)inventory=new Inventory();
		inventory.setCode("单据编号由系统自动生成");
		inventory.setCreatedt(new java.sql.Date(System.currentTimeMillis()));
		request.setAttribute("editable", true);
		request.setAttribute("checkable", false);
		request.setAttribute("inventory", inventory);
		request.setAttribute("detailUrl", "/inventory/getInitDetailsInventory.tg");
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
		inventory.setCode("JNPK"+sdf.format(date));
		inventory.setCreateBy(createBy);
		inventory.setCreatedt(new java.sql.Timestamp(System.currentTimeMillis()));
		inventory.setStatus(0);
		boolean flag=true;
		Map<String,Object> result=inventoryService.saveItem(inventory,detailmap,flag);
		String jsonstr=JSONObject.fromObject(result).toString();
		response.setContentType("text/plain;charset=utf-8");
		returnJsion(jsonstr, response);
	}
	
	public String edit(){
		Subject subject=SecurityUtils.getSubject();
		String userId= (String) subject.getSession().getAttribute("userId");
		Inventory inventory = inventoryService.findById(id);
		if(inventory.getCreateBy()!=null){
			if (inventory.getStatus() == 0 && inventory.getCreateBy().getId().equals(userId)){
				request.setAttribute("editable", true);
			}
		}
		if (inventory.getStatus() == 0){
			request.setAttribute("checkable", true);
		}
		request.setAttribute("inventory", inventory);
		request.setAttribute("detailUrl", "/inventory/getBillDetailsInventory.tg?inventoryId="+inventory.getInventoryId().toUpperCase());
//		System.err.println("/inventory/getBillDetailsinventory.tg?orderId="+inventory.getOrderId());
		return "edit";
	}
	
	public void check() {
		inventory=inventoryService.findById(id);
		Subject subject=SecurityUtils.getSubject();
		String userId= (String) subject.getSession().getAttribute("userId");
		Users checkBy=new Users();
		checkBy.setId(userId);
		inventory.setCheckBy(checkBy);
		inventory.setCheckDate(new java.sql.Timestamp(System.currentTimeMillis()));
		Map<String,WarehouseDetail> warehouseDetails=warehouseDetailService.getStock4Now(inventory);
		String ok = inventoryService.checkInventory(inventory,warehouseDetails);
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
		inventory=inventoryService.findById(id);
		if (inventory.getStatus() == 0){
			if(inventory.getCreateBy()!=null) {
				if (!inventory.getCreateBy().getId().equals(userId)) {
					result.put("failure", true);
					result.put("msg", "对不起，你没有权限删除该单据。");
				} else {
					List<InventoryDetail> inventoryDetails = inventoryDetailService.findList("from InventoryDetail od where od.inventory.inventoryId='" + id + "'");
					inventoryService.deleteInventoryById(id, inventoryDetails);
					result.put("success", true);
				}
			}else{
				List<InventoryDetail> inventoryDetails = inventoryDetailService.findList("from InventoryDetail od where od.inventory.inventoryId='" + id + "'");
				inventoryService.deleteInventoryById(id, inventoryDetails);
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
		inventory.setInventoryId(id);
//		inventory = Order.updateModel(inventory, params);
		Users user=new Users();
		user.setId(userId);
		inventory.setCreateBy(user);
		inventory.setCreatedt(new java.sql.Timestamp(System.currentTimeMillis()));
		boolean flag=false;
		Map<String,Object> result = inventoryService.saveItem(inventory, detailmap,flag);
		response.setContentType("text/plain;charset=utf-8");
		returnJsion(JSONObject.fromObject(result).toString(), response);
	}
	
	//货损统计
	public String statistics(){
		return "damageGoods";
	}
	
	public void getItemsStatistics()
	{
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if((!"".equals(startTime)&&startTime!=null)&&(!"".equals(endTime)&&endTime!=null)) {
			InventoryDetail inventoryDetail = new InventoryDetail();
			String productName = request.getParameter("productName");
			String storeId=request.getParameter("storeId");
			String userId=(String)request.getSession().getAttribute("userId");
			String storeIds=storeService.getUserStores(userId,storeId);
			Product productCon = new Product();
			productCon.setProductName(productName);
			inventoryDetail.setProduct(productCon);
			Inventory inv = new Inventory();
			inv.setStoreId(storeIds);
			inv.setStartTime(startTime);
			inv.setEndTime(endTime);
			inventoryDetail.setInventory(inv);
			List<Object[]> list = inventoryDetailService.getPageList(inventoryDetail, page, rows, sort, order);
			int totalcount = inventoryDetailService.findCountBySql(inventoryDetail);
			String[] params = new String[]
					{"productId", "productName", "unit", "dq", "dqsum"};
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
		String hql="from InventoryDetail ind where ind.inventory.storeId in("+storeIds+")  and ind.product.productId='"+productId+"' and ind.inventory.status=1 ";
		if(!"".equals(startTime)&&startTime!=null)	
			hql+=" and ind.inventory.checkDate > '"+startTime.replace("_", " ")+"' ";
		if(!"".equals(endTime)&&endTime!=null)	
			hql+=" and ind.inventory.checkDate < '"+endTime.replace("_", " ")+"' ";
		hql	+=" order by ind.inventory.checkDate asc";
		List<InventoryDetail> list=inventoryDetailService.findList(hql);
		System.out.println(hql);
		String data="";
		for(InventoryDetail ind:list)
		{
			Date date=ind.getInventory().getCheckDate();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmm");
			String dateStr=format.format(date);
			int year=Integer.parseInt(dateStr.substring(0, 4));
			int month=Integer.parseInt(dateStr.substring(4, 6));
			int day=Integer.parseInt(dateStr.substring(6, 8));
			int hour=Integer.parseInt(dateStr.substring(9, 11));
			int min=Integer.parseInt(dateStr.substring(11, 13));
			data+="[Date.UTC("+year+", "+(month-1)+", "+(day)+","+(hour)+","+(min)+"), "+ind.getDamageQuantity()+"],";
		}
		if(list.size()>0)
		  data="["+data.substring(0, data.length()-1)+"]";
		System.err.println("数据"+data);
		
		request.setAttribute("data", data);
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
	private List<Map<String, String>> doList(List<Inventory> list) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		JsonConfig config = new JsonConfig();  
		 config.setExcludes(new String[]{"groups","password","address","age","birthday","education","email","employeddate","phone","position",
				 "registerdate","sex","status","storeId","usertype","jobmember","lastlogoffdate","lastlogondate","lastlogonip","store"});
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				Inventory inventory = list.get(i);
				map.put("code", inventory.getCode());
				map.put("createBy", JSONObject.fromObject(inventory.getCreateBy(),config)+"");
				map.put("createdt", inventory.getCreatedt()+"");
				map.put("checkBy", JSONObject.fromObject(inventory.getCheckBy(),config)+"");
				map.put("checkDate", inventory.getCheckDate()+"");
				map.put("storeId", inventory.getStoreId());
				map.put("remark", inventory.getMemo());
				map.put("totalInventory", inventory.getTotalInventory()+"");
				map.put("totalSum", inventory.getTotalSum()+"");
				map.put("status", inventory.getStatus()+"");
				map.put("inventoryId", inventory.getInventoryId());
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

	public Inventory getInventory() {
		return inventory;
	}
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
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
