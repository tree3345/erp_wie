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

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IStoreService;
import com.wie.erp.model.Store;
import com.wie.framework.controls.struts2.BaseTg;


/** 
  * @ClassName: DictionaryTg 
  * @Description: 系统字典控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("storeControl")
public class StoreTg extends BaseTg {
	@Autowired
	private IStoreService storeService;
	private Store store;
	private String id;

	/** 
	  * @Title: index 
	  * @throws
	  */
	public String index(){
		return this.SUCCESS;
	}
	
	/** 
	  * @Title: getItems 
	  * @Description: 系统字典表所有常量信息
	  * @return void
	  * @throws 
	  */
	public void getItems(){
		if(store==null)store=new Store();
		String storeName=request.getParameter("storeName");
		if(storeName!=null)
			store.setStoreName(storeName);
		Pagination pagination = storeService.getPageList(store, page, rows,sort,order);
		List<Store> list = pagination.getList();
		List lists = doList(list);	
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + pagination.getTotalCount() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr,response);
	}

	public void getAll()
	{
        String storeIds=request.getParameter("storeIds");
		List list=storeService.getStores(storeIds);
		List lists = doList(list);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		returnJsion(jsonArray.toString(),response);
	}
	
	public void getJSONAll()
	{

		List list=storeService.findList("from Store");
		List lists = doList(list);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		returnJsion(jsonArray.toString(),response);
	}

	//获得userstore json
	public void users_stores(){
		String userid=request.getParameter("id");
		List list=storeService.findList("select s from Store as s left join s.users as u where u.id='"+userid+"'");
		List lists = doList(list);
		JSONArray jsonArray = JSONArray.fromObject(lists);
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
		String inserted = request.getParameter("inserted");
		String deleted = request.getParameter("deleted");
		String updated = request.getParameter("updated");
		Map<String,String> datamap = new HashMap<String, String>();
		datamap.put("inserted", inserted);
		datamap.put("deleted", deleted);
		datamap.put("updated", updated);
		if (this.storeService.saveAll(datamap)) {
			returnJsion("{\"success\":\"true\"}", response);
		} else {
			returnJsion("{\"error\":\"true\"}", response);
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
	private List doList(List<Store> list) {
		List lists = new ArrayList();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				Store store = list.get(i);
				map.put("storeId", store.getStoreId());
				map.put("storeName", store.getStoreName());
				lists.add(map);
			}
		}
		return lists;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
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
