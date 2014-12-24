package com.wie.erp.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.wie.erp.biz.*;
import com.wie.erp.model.*;
import com.wie.permissions.controls.SystemTg;
import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wie.common.tools.page.Pagination;
import com.wie.common.tools.util.IpAddr;
import com.wie.framework.controls.struts2.BaseTg;

/**
 * @ClassName: DictionaryTg
 * @Description: 系统字典控制层
 * 
 */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("productControl")
public class ProductTg extends BaseTg {
	/**
	 * @Fields dictionaryService : IDictionaryService业务层接口注入
	 */
	@Autowired
	private IProductService productService;
	@Autowired
	private IWarehouseDetailService warehouseDetailService;
	@Autowired
	private IProductPriceService msSQLProductPriceService;
	@Autowired
	private IMsSQLErpService msSQLErpService;
	@Autowired
	private IStoreService storeService;

	private static List<Map> loginList = new ArrayList<Map>();
	/**
	 * @Fields dic : struts2.0接受前台信息（domain接受）
	 */
	private Product product;

	/**
	 * @Fields id : 删除，更新所需要的id，struts2.0接受
	 */

	private String classId;
	private String id;

	/**
	 * @Title: index
	 * @Description: 系统字典首页
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String index() {
		String flag=request.getParameter("flag");
		request.setAttribute("flag", flag);
		return this.SUCCESS;
	}

	/**
	 * @Description 商品管理
	 */
	public String mgr(){
		return "mgr";
	}
	/**
	 * @Title: getItems
	 * @Description: 系统字典表所有常量信息
	 * @param
	 * @return void
	 * @throws
	 */
	public void getItems() {
		if (product == null)
			product = new Product();
		String classId = request.getParameter("classId");
		String params = request.getParameter("productName");
		String storeId=request.getParameter("storeId");
		if (!"null".equals(params) && params != null)
			product.setProductName(params);
		if (classId != null){
			ProductClass productClass=new ProductClass();
			productClass.setClassId(classId);
			product.setProductClass(productClass);
		}
		String userId=(String)request.getSession().getAttribute("userId");
		String storeIds=storeService.getUserStores(userId,storeId);
		product.setStoreId(storeIds);
		Pagination pagination = productService.getPageList(product, page, rows,sort, order);
		List<Product> list = pagination.getList();
		Integer stockflag=product.getStockflag()==null?0:product.getStockflag();	
		List<Map<String, String>> lists = doList(list,stockflag,storeId);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + pagination.getTotalCount()
				+ ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr, response);
	}

	public String queryGood(){
        String storeId=request.getParameter("storeId");
		String stockflag=request.getParameter("stockflag");
		request.setAttribute("stockflag",stockflag);
		request.setAttribute("storeId",storeId);
		return "querygood";
	}
     /**
      * 商品删除验证
      */
	public void isExistData()
	{
		String baseStr=productService.isExitstData(id);
	    returnJsion(baseStr,response);
	}
	/**
	 * 商品价格
	 */
	public void getPtjson() {
		String vegeName = request.getParameter("vegename");
		System.out.println("乱码" + vegeName);
		String[] params = { "f1", "f2", "f3", "f4" };
		List list = this.msSQLProductPriceService.getProductJSON(vegeName);
		List lists = doListObj(list, params);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + list.size() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr, response);
	}

	/**
	 * 获得库存
	 */
	public void getWarehouse() {
		String vegeName = request.getParameter("vegename");
		String storeId = request.getParameter("storeid");
		String[] params = { "f1", "f2", "f3", "f4" };
		if (storeId != null && !storeId.equals("")) {
			List list = this.msSQLProductPriceService.getWarehouse(vegeName,
					storeId);
			List lists = doListObj(list, params);
			JSONArray jsonArray = JSONArray.fromObject(lists);
			String baseStr = "{\"total\":" + list.size() + ",\"rows\":";
			baseStr = baseStr + jsonArray.toString() + "}";
			returnJsion(baseStr, response);
		} else {
			returnJsion("无对应商店，数据为空", response);
		}
	}

	/**
	 * 获得库存详情
	 */
	public void getWarehouseInfo() {
		String warehouseID = request.getParameter("warehouseID");
		String[] params = { "f1", "f2", "f3", "f4" };
		List list = this.msSQLProductPriceService.getWarehouseInfo(warehouseID);
		List lists = doListObj(list, params);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + list.size() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr, response);
	}

	/**
	 * 获得销售记录
	 */
	public void getSaleRecord() {
		String saleperson = request.getParameter("saleperson");
		String mumberNo = request.getParameter("memberNo");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String[] params = { "f1", "f2", "f3", "f4" };
		List list = this.msSQLProductPriceService.getSaleRecord(saleperson,
				mumberNo, startTime, endTime);
		List lists = doListObj(list, params);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + list.size() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr, response);
	}

	/**
	 * 获得销售记录详情
	 */
	public void getDetailSaleRecord() {
		String id = request.getParameter("id");
		String[] params = { "f1", "f2", "f3", "f4" };
		List list = this.msSQLProductPriceService.getDetailSaleRecord(id);
		List lists = doListObj(list, params);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + list.size() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr, response);
	}

	/**
	 * 获得小类
	 */
	public void getCategory() {
		String params = "f1";
		List list = this.msSQLProductPriceService.getCategory();
		List lists = doListStr(list, params);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + list.size() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr, response);
	}

	/**
	 * 获得中类
	 */
	public void getPtClass() {
		String params = "f1";
		List list = this.msSQLProductPriceService.getProductClass();
		List lists = doListStr(list, params);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + list.size() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr, response);
	}

	// ajax调用分类
	public void getPclass() {
		String[] params = { "classId", "className" };
		List list = this.msSQLProductPriceService.getPclass();
		List lists = doListObj(list, params);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		returnJsion(jsonArray.toString(), response);
	}

	/**
	 * 根据小类获得中类
	 * 
	 * @return
	 */
	public void getPtClassByCategoryName() {
		String categoryName = request.getParameter("categoryName");
		String params = "f1";
		List list = this.msSQLProductPriceService
				.getProductClassByCategoryName(categoryName);
		List lists = doListStr(list, params);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + list.size() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr, response);
	}

	public void getPtNameByClassName() {
		String className = request.getParameter("className");
		String params = "f1";
		List list = this.msSQLProductPriceService
				.getProductNameByClassName(className);
		List lists = doListStr(list, params);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + list.size() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr, response);
	}


	/**
	 * 客户端调用方法
	 */
	public void clientfn() {
		String sqlStr = request.getParameter("sqlStr");
		System.out.println(sqlStr);
		String result="";
		try {
			result = this.msSQLProductPriceService.operateDB(sqlStr);
		} catch (Exception e) {
			e.printStackTrace();
			result=e.toString();
			returnJsion(result, response);
			return;
		}
		String[] temp = result.split("#");
		request.getSession().setAttribute("userid", temp[0]);
		System.err.println("user:" + result);
		// 登录信息写入内存
		if ((sqlStr.contains("login")||sqlStr.contains("logout")) && result != null) {
			IpAddr ipaddr = new IpAddr();
			String ip = ipaddr.getIpAddr(request);
			Map<String, String> loginMap = new HashMap<String, String>();
			if (loginList != null) {
				for (int i = 0; i < loginList.size(); i++) {
					Map<String, String> oldloginMap = loginList.get(i);
					Set<String> ipkey = oldloginMap.keySet();// 如果存在删除原有ip，重新赋值
					for (Iterator it = ipkey.iterator(); it.hasNext();) {
						String s = (String) it.next();
						if (s.equals(ip)) {
							oldloginMap.remove(s);
							loginList.remove(i);
							break;
						}
					}
				}
			}
			if(sqlStr.contains("login"))
			 loginMap.put(ip, result + "#" + ip);
			loginList.add(loginMap);
		}
		returnJsion(result, response);
	}
	public void online() {
		List lists = new ArrayList();
		for (int i = 0; i < loginList.size(); i++) {
			Map loginMap = loginList.get(i);
			Map<String, String> map = new HashMap<String, String>();
			Set<String> ipkey = loginMap.keySet();
			String ip = "";
			String info = "";
			for (Iterator it = ipkey.iterator(); it.hasNext();) {
				String s = (String) it.next();
				ip += s;
				info += loginMap.get(s);
				System.out.println("key:" + s + "   value:" + loginMap.get(s));
			}
			String[] infosp = info.split("#");

			if (!info.equals("") && info != null) {
				map.put("ip", ip);
				map.put("username", infosp[2]);
				map.put("store", infosp[3]);
				lists.add(map);
			}
		}
		JSONArray jsonArray = JSONArray.fromObject(lists);
		returnJsion(jsonArray.toString(), response);
	}

	public String onlineIndex() {
		return "online";
	}

	/**
	 * @Title: save
	 * @Description: 保存一条系统字典表中的常量信息
	 * @param
	 * @return void
	 * @throws
	 */
	public void save(){
		product.setProductId(UUID.randomUUID().toString());
		if(this.productService.saveProductandPrice(product)){
			returnJsion("{\"success\":\"true\"}",response);
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}
	}
	/**
	 * @Title: update
	 * @Description: 更新常量
	 * @param
	 * @return void
	 * @throws
	 */
	public void update() {

		if (this.productService.alteProductandPrice(product)) {
			returnJsion("{\"success\":\"true\"}", response);
		} else {
			returnJsion("{\"error\":\"true\"}", response);
		}
	}
	public void save_bak() {

		String purchaseId = request.getParameter("purchaseId");
		String flag = request.getParameter("flag");
		String inserted = request.getParameter("inserted");
		String deleted = request.getParameter("deleted");
		String updated = request.getParameter("updated");
		String storeId = (String) request.getSession().getAttribute("storeId");
		Map<String,String> datamap = new HashMap<String, String>();
		datamap.put("inserted", inserted);
		datamap.put("deleted", deleted);
		datamap.put("updated", updated);
		if(flag.equals("1"))
		datamap.put("storeId", storeId);
		if (this.productService.saveAllMsSQL(datamap)) {
			returnJsion("{\"success\":\"true\"}", response);
		} else {
			returnJsion("{\"error\":\"true\"}", response);
		}

	}


	/**
	 * @Title: del
	 * @Description: 删除常量
	 * @param
	 * @return void
	 * @throws
	 */
	public void del() {
		product=productService.findById(id);
		product.setStatus(0);
		if (this.productService.alter(product)) {
			returnJsion("{\"success\":\"true\"}", response);
		} else {
			returnJsion("{\"error\":\"true\"}", response);
		}
	}

	/**
	 * @Title: returnJsion
	 * @Description: 解决json问题
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
		} finally {
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
	private List doList(List<Product> list,Integer stockflag,String storeId) {
		List lists = new ArrayList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				Product product = list.get(i);
				map.put("productId", product.getProductId());
				map.put("productNo", product.getProductNo());
				map.put("productName", product.getProductName());
				map.put("classId", product.getProductClass()==null?"":product.getProductClass().getClassId());
				map.put("className", product.getProductClass()==null?"":product.getProductClass().getClassName());
				ProductPrice prices=product.getProductPrice();
				map.put("salesPrice", prices==null?"0":prices.getSalesPrice()+"");
				map.put("costPrice", prices==null?"0":prices.getCostPrice()+"");
				map.put("orderPrice", prices==null?"0":prices.getOrderPrice()+"");
				map.put("purchasePrice", prices==null?"0":(prices.getPurchasePrice()==null?"0":prices.getPurchasePrice()+""));
				map.put("priceStatus", prices==null?"0":"1");
				map.put("status", product.getStatus()+"");
				map.put("storeId", product.getStoreId());
				map.put("goodNumber", product.getGoodNumber());
				map.put("spelling", product.getSpelling());
				map.put("unit", product.getUnit());
				if(stockflag!=null&stockflag==1){
				List<WarehouseDetail> stockquantity=warehouseDetailService.findList("from WarehouseDetail w where w.product.productId='"+product.getProductId()+"' and w.warehouse.storeId='"+storeId+"'");
				map.put("warehouseQuantity", stockquantity.size()==0?"":stockquantity.get(0).getQuantity()+"");
				}
				lists.add(map);
			}
		}
		return lists;
	}

	private List doOther(List<Product> list) {
		List lists = new ArrayList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				Product product = list.get(i);
				map.put("productName", product.getProductName());
				map.put("spelling", product.getSpelling());
				map.put("id", product.getProductId() + "");
				lists.add(map);
			}
		}
		return lists;
	}

	private List doListObj(List<Object[]> list, String[] params) {
		List lists = new ArrayList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				Object[] obj = (Object[]) list.get(i);
				for (int j = 0; j < params.length; j++)
					map.put(params[j], obj[j] + "");
				lists.add(map);
			}
		}
		return lists;
	}

	private List doListStr(List<String> list, String params) {
		List lists = new ArrayList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				String str = (String) list.get(i);
				map.put(params, str);
				lists.add(map);
			}
		}
		return lists;
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public IProductPriceService getMsSQLProductPriceService() {
		return msSQLProductPriceService;
	}

	public void setMsSQLProductPriceService(
			IProductPriceService msSQLProductPriceService) {
		this.msSQLProductPriceService = msSQLProductPriceService;
	}


	public IMsSQLErpService getMsSQLErpService() {
		return msSQLErpService;
	}

	public void setMsSQLErpService(IMsSQLErpService msSQLErpService) {
		this.msSQLErpService = msSQLErpService;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

}
