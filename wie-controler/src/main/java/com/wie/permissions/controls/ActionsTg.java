package com.wie.permissions.controls;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wie.common.tools.json.FormatJSON;
import com.wie.common.tools.page.Pagination;
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.permissions.biz.IActionsService;
import com.wie.permissions.model.Actions;


/** 
  * @ClassName: ActionsTg 
  * @Description: 操作管理控制类 
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller(value="permissions.actionsTgControl")
public class ActionsTg extends BaseTg {

	/** 
	  * @Fields actionsService :操作信息的服务接口 
	  */ 
	@Autowired
	private IActionsService actionsService;
	
	/** 
	  * @Title: index 
	  * @Description: 索引进入操作信息页面  
	  * @param @return
	  * @return String
	  * @throws 
	  */
	public String index(){
		return SUCCESS;
	}
	
	/** 
	  * @Title: getItems 
	  * @Description: 根据action的条件获得指定列表 
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void getItems(){
		Pagination pagination = this.actionsService.getActionsList(this.getAction(), page, rows,sort,order);
		List list = pagination.getList();
		String  str="";
		JsonConfig config = new JsonConfig();   
		 config.setIgnoreDefaultExcludes(false);      
		 config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);    
		// config.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor("yyyy-MM-dd")); //date processor register   
		 config.setExcludes(new String[]{//只要设置这个数组，指定过滤哪些字段。   
		   "resource"
		 }); 
		 str = JSONSerializer.toJSON(list,config).toString();
		System.out.println(str);
		try {
			outJsonString(response,"{\"total\":" + pagination.getTotalCount() + ",\"rows\":"+str + "}");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getItem(){
		Pagination pagination = this.actionsService.getActionsList(this.getAction(), page, rows,sort,order);
		List list = pagination.getList();
		String  str="";
		JsonConfig config = new JsonConfig();   
		 config.setIgnoreDefaultExcludes(false);      
		 config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);    
		// config.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor("yyyy-MM-dd")); //date processor register   
		 config.setExcludes(new String[]{//只要设置这个数组，指定过滤哪些字段。   
		   "resource"
		 }); 
		 str = JSONSerializer.toJSON(list,config).toString();
		System.out.println(str);
		try {
			outJsonString(response,str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getAllItem(){
		List<Actions> actions = this.actionsService.getAll();
		String  str="";
		JsonConfig config = new JsonConfig();   
		config.setIgnoreDefaultExcludes(false);      
		config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);    
		// config.registerJsonValueProcessor(Date.class,new DateJsonValueProcessor("yyyy-MM-dd")); //date processor register   
		config.setExcludes(new String[]{//只要设置这个数组，指定过滤哪些字段。   
				"resource"
		}); 
		str = JSONSerializer.toJSON(actions,config).toString();
		System.out.println(str);
		try {
			outJsonString(response,str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** 
	  * @Title: save 
	  * @Description:保存一条操作信息记录 
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void save(){
		try{
			if(action.getOrderid()==null){
				action.setOrderid(this.actionsService.getMaxOrder()+1);
			}
			this.actionsService.save(action);
			outJsonPlainString(response, "{\"success\":true}");
		}catch(Exception e){
			e.printStackTrace();
			outJsonPlainString(response, "{\"error\":true}");
			
		}
	}
	/** 
	  * @Title: update 
	  * @Description: 修改操作信息记录
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void update(){
		try{
			this.actionsService.alter(action);
			outJsonPlainString(response, "{\"success\":true}");
		}catch(Exception e){
			e.printStackTrace();
			outJsonPlainString(response, "{\"error\":true}");
		}
	}
	/** 
	  * @Title: delete 
	  * @Description: 删除指定ID的操作信息记录
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void delete(){
		try {
			this.actionsService.deleteById(id);
			outJsonPlainString(response, "{\"success\":true}");
		}catch(Exception e){
			e.printStackTrace();
			outJsonPlainString(response, "{\"error\":true}");
		}
	}
	/** 
	  * @Title: getAllTrees 
	  * @Description:根据操作ID获得获得所有资源树，其中角色有权限的资源checkbox为true  
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void getAllTrees(){
		try {
			JSONArray json = JSONArray.fromObject(this.actionsService.getAllListTrees(id));
			outJsonString(response,json.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outJsonPlainString(response,"[]");
		}
	}
	/** 
	  * @Title: saveAuthorizate 
	  * @Description: 给指定ID的角色分配资源 
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void saveAuthorizate(){
		try{
			this.actionsService.saveAuthorizate(id,rids);
			outJsonPlainString(response, "{\"success\":true}");
		}catch(Exception e){
			
			outJsonPlainString(response, "{\"error\":true}");
		}
	}
	
	private String id;
	private Actions action;
	private String rids;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Actions getAction() {
		return action;
	}
	public void setAction(Actions action) {
		this.action = action;
	}
	private String actions;

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}
	public String getRids() {
		return rids;
	}

	/**
	 * @param rids the rids to set
	 */
	public void setRids(String rids) {
		this.rids = rids;
	}
}
