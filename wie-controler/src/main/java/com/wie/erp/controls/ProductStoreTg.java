package com.wie.erp.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IMsSQLErpService;
import com.wie.erp.biz.IProductClassService;
import com.wie.erp.biz.IProductPriceService;
import com.wie.erp.biz.IProductService;
import com.wie.erp.model.Product;
import com.wie.erp.model.ProductClass;
import com.wie.erp.model.ProductStore;
import com.wie.framework.controls.struts2.BaseTg;

/**
 * @ClassName: DictionaryTg
 * @Description: 系统字典控制层
 * 
 */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("productStoreControl")
public class ProductStoreTg extends BaseTg {
	/**
	 * @Fields dictionaryService : IDictionaryService业务层接口注入
	 */
	@Autowired
	private IProductService productService;
	
	@Autowired
	private IProductClassService productClassService;
	@Autowired
	private IProductPriceService msSQLProductPriceService;

	@Autowired
	private IMsSQLErpService msSQLErpService;

	private static List<Map> loginList = new ArrayList<Map>();
	/**
	 * @Fields dic : struts2.0接受前台信息（domain接受）
	 */
	private Product product;

	/**
	 * @Fields id : 删除，更新所需要的id，struts2.0接受
	 */
	private String id;

	private String classId;

	/**
	 * @Title: index
	 * @Description: 系统字典首页
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String index() {
		String[] params = { "classId", "className" };
		List list = this.msSQLProductPriceService.getPclass();
		List lists = doListObj(list, params);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		request.setAttribute("pclass", jsonArray.toString());
		return this.SUCCESS;
	}

	/**
	 * @Title: getItems
	 * @Description: 系统字典表所有常量信息
	 * @param
	 * @return void
	 * @throws
	 */
	public void getItems() {
		String storeId=(String)request.getSession().getAttribute("storeId");
		if (product == null)
			product = new Product();
		product.setStoreId(storeId);
		String classId = request.getParameter("classId");
		String params = request.getParameter("productName");
		String flag= request.getParameter("flag");
		if (!"null".equals(params) && params != null)
			product.setProductName(params);
		if (classId != null){
			ProductClass productClass=new ProductClass();
			productClass.setClassId(classId);
			product.setProductClass(productClass);
		}
		Pagination pagination = productService.getPageList(product, page, rows,
				sort, order);
		List<Product> list = pagination.getList();
		List<Map<String, String>> lists = doList(list);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + pagination.getTotalCount()+ ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr, response);
	}

	/*public void impgoods(){
		String productIds=request.getParameter("productIds");
		String storeId=(String) request.getSession().getAttribute("storeId");
		
		if(this.productService.impgoods(productIds,storeId)){
			returnJsion("{\"success\":\"true\"}",response);
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}
	}*/
	
	/*public void isExist()
	{
		String storeId=(String) request.getParameter("storeId");
		if(this.productStoreService.isExist(storeId)){
			returnJsion("{\"success\":\"true\"}",response);
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}
	}*/
	

	

	/**
	 * @Title: save
	 * @Description: 保存一条系统字典表中的常量信息
	 * @param
	 * @return void
	 * @throws
	 */
	public void save() {

		String purchaseId = request.getParameter("purchaseId");
		String inserted = request.getParameter("inserted");
		String deleted = request.getParameter("deleted");
		String updated = request.getParameter("updated");
		Map<String,String> datamap = new HashMap<String, String>();
		datamap.put("inserted", inserted);
		datamap.put("deleted", deleted);
		datamap.put("updated", updated);
		if (this.productService.saveAllMsSQL(datamap)) {
			returnJsion("{\"success\":\"true\"}", response);
		} else {
			returnJsion("{\"error\":\"true\"}", response);
		}

	}

	public void testbackroll() throws Exception {
		List list = this.productService.executebysql("");
		System.out.println("测试记录数：" + list.size());

		/*
		 * this.multipleDataSourceService.execute4MySQL();
		 * this.multipleDataSourceService.execute4MsSQL();
		 * this.multipleDataSourceService.execute4Oracle();
		 */
	}

	/**
	 * @Title: find
	 * @Description: 查找一常量
	 * @param
	 * @return void
	 * @throws
	 */
	public void find() {
		Product product = this.productService.findMsSQL(id);
		List<Product> list = new ArrayList<Product>();
		if (null != product) {
			list.add(product);
		}
		JSONArray jsonArray = JSONArray.fromObject(doOther(list));
		// 现在一个用户只能有一个组
		/*
		 * Iterator<ProductClass> it = product.getProductClass().iterator();
		 * 
		 * 
		 * while(it.hasNext()){ ProductClass temp = it.next();
		 * returnJsion("{\"rows\":" + jsonArray.toString() + ",\"parentid\":\""
		 * + temp.getClassId() + "\",\"parentName\":\"" + temp.getClassName() +
		 * "\"}",response); return; }
		 */
		returnJsion("{\"rows\":" + jsonArray.toString()
				+ ",\"parentid\":\"\",\"parentName\":\"\"}", response);

	}

	/**
	 * @Title: update
	 * @Description: 更新常量
	 * @param
	 * @return void
	 * @throws
	 */
	public void update() {

		if (this.productService.alterMsSQL(product)) {
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
		if (this.productService.deleteByIdMsSQL(id)) {
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
	private List doList(List<Product> list) {
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
				map.put("salesPrice", product.getProductPrice()==null?"0":product.getProductPrice().getSalesPrice()+"");
				map.put("costPrice", product.getProductPrice()==null?"0":product.getProductPrice().getCostPrice()+"");
				map.put("orderPrice", product.getProductPrice()==null?"0":product.getProductPrice().getOrderPrice()+"");
				map.put("purchasePrice", product.getProductPrice()==null?"0":product.getProductPrice().getPurchasePrice()+"");
				map.put("priceStatus", product.getProductPrice()==null?"0":"1");
				map.put("spelling", product.getSpelling());
				map.put("unit", product.getUnit());
				lists.add(map);
				/*
				 * Object[] obj =(Object[]) list.get(i); map.put("id",
				 * obj[0]+""); map.put("productName", obj[1]+"");
				 * map.put("price",obj[2]+""); map.put("unit", obj[3]+"");
				 * map.put("productNo", obj[4]+""); map.put("spelling",
				 * obj[5]+""); map.put("className", obj[6]+""); lists.add(map);
				 */
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
