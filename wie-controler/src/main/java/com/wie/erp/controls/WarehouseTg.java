package com.wie.erp.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wie.erp.biz.IStoreService;
import com.wie.erp.model.Store;
import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IWarehouseService;
import com.wie.erp.model.Warehouse;
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.permissions.model.Dictionarys;


/** 
  * @ClassName: DictionaryTg 
  * @Description: 系统字典控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("warehouseControl")
public class WarehouseTg extends BaseTg {
	/** 
	  * @Fields dictionaryService : IDictionaryService业务层接口注入
	  */ 
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired
	private IStoreService storeService;
	
	/** 
	  * @Fields dic : struts2.0接受前台信息（domain接受）
	  */ 
	private Warehouse warehouse;

	
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
		return this.SUCCESS;
	}
	
	/** 
	  * @Title: getItems 
	  * @Description: 系统字典表所有常量信息
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void getItems(){
		Pagination pagination = warehouseService.getPageList(warehouse, page, rows,sort,order);
		List<Warehouse> list = pagination.getList();
		List lists = doList(list);	
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + pagination.getTotalCount() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr,response);
	}

	public void getAll()
	{
		List<Warehouse> list=warehouseService.findList("from Warehouse");
		JSONArray jsonArray = JSONArray.fromObject(list);
		returnJsion(jsonArray.toString(),response);
	}
	/** 
	  * @Title: save 
	  * @Description: 保存一条系统字典表中的常量信息
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void save(){
		
		if(this.warehouseService.save(this.warehouse)){
			returnJsion("{\"success\":\"true\"}",response);
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}
	}

	public void saveTo(){
		List<Store> stores=storeService.findList("from Store w where w.storeId not in(select storeId from Warehouse)");
		System.out.println(stores.size());
        for(Store store:stores)
		{
			warehouse=new Warehouse();
			warehouse.setStoreId(store.getStoreId());
			warehouseService.save(warehouse);
		}
	}
	/** 
	  * @Title: find 
	  * @Description: 查找一常量
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void find(){
		Warehouse warehouse = this.warehouseService.findById(id);
		List<Warehouse> list = new ArrayList<Warehouse>();
		if(null != warehouse){
			list.add(warehouse);
		}
		JSONArray json = JSONArray.fromObject(list);
		returnJsion("{\"rows\":" +  json.toString() +"}", response);
	}

	/** 
	  * @Title: update 
	  * @Description:  更新常量
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void update(){
		if(this.warehouseService.alter(this.warehouse)){
			returnJsion("{\"success\":\"true\"}",response);
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}
	}

	/** 
	  * @Title: del 
	  * @Description: 删除常量
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void del(){
		if(this.warehouseService.deleteById(id)){
			returnJsion("{\"success\":\"true\"}",response);	
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}
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
	private List doList(List<Warehouse> list) {
		List lists = new ArrayList();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				Warehouse warehouse = list.get(i);
				map.put("warehouseId", warehouse.getWarehouseId());
				map.put("storeId", warehouse.getStoreId());
				lists.add(map);
			}
		}
		return lists;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

}
