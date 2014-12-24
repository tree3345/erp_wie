package com.wie.erp.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wie.erp.biz.ICategoryService;
import com.wie.erp.biz.IProductClassService;
import com.wie.erp.model.Category;
import com.wie.erp.model.CloneProductClass;
import com.wie.erp.model.ProductClass;
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.permissions.tree.Utils;
import com.wie.tree.Node;
import com.wie.tree.UncodeException;
import com.wie.tree.UserDataUncoder;
import com.wie.tree.support.AbstractWebTreeModelCreator;
import com.wie.tree.support.WebTreeNode;


/** 
  * @ClassName: GroupTg 
  * @Description: 组信息控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("categoryControl")
public class CategoryTg extends BaseTg {
	/** 
	  * @Fields groupService : IGroupService接口spring注入 
	  */ 
	
	@Autowired
	private ICategoryService    categoryService;
	
	/** 
	  * @Fields group : struts2.0自动接受
	  */ 
	private Category category;
	
	/** 
	  * @Fields id : 要删除的id
	  */ 
	private String id;
	/** 
	  * @Fields userIds : 需要加到组里面的用户id
	  */ 
	private String treeId;
	private String categoryId;
	private String parentId;
	public void test()
	{
		String id=request.getParameter("id");
		String parentId=request.getParameter("parentId");
		String text=request.getParameter("text");
		String targetId=request.getParameter("targetId");
		String point=request.getParameter("point");
		
		System.err.println("id:"+id);
		System.err.println("parentId:"+parentId);
		System.err.println("text:"+text);
		System.err.println("targetId:"+targetId);
		System.err.println("point:"+point);
		 
	}
	
	public void getTree(){
         List<Map<String,Object>> nodes = new ArrayList<Map<String,Object>>();
         
		// 获取一级节点
		List<Category> types = categoryService.findList("from Category");
		//.findAll(GoodType.class, "parentId=0", null, "sort");
		for(Category type: types){
			Map<String,Object> node = new HashMap<String,Object>();
			node.put("id", type.getCategoryId());
			node.put("text", type.getCategoryName());
			nodes.add(node);
		}
		
		returnJsion(JSONArray.fromObject(nodes).toString(),response);
		/*
		try {
		    Category tem = 	this.categoryService.findById(categoryId);
			List tree = this.productClassService.getTree4MsSQL(tem,id+"");
			outJsonPlainString(response, JSONArray.fromObject(tree).toString());
		} catch (Exception e) {
			e.printStackTrace();
			outJsonPlainString(response, "[]");
		}*/
	}
	
	public void update(){
//		if(this.productClassService.alter(productClass)){
//			returnJsion("{\"success\":\"true\"}",response);
//		}else{
//			returnJsion("{\"error\":\"true\"}",response);
//		}
	}
	
	public void del(){
		if(this.categoryService.deleteById(id)){
			returnJsion("{\"success\":\"true\"}",response);
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}
	}
	
	public void save(){
		/*if(this.productClassService.save(productClass)){
			returnJsion("{\"success\":\"true\"}",response);
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}*/
	}
	
	/** 
	  * @Title: list 
	  * @Description: 获取所有组，并且用tree来显示
	  * @param @return
	  * @return String 一段<html>代码
	  * @throws 
	  */

	/** 
	  * @Title: index 
	  * @Description: 组的首页信息
	  * @param @return
	  * @return String
	  * @throws 
	  */
	public String index(){
		/*try {
			request.setAttribute("allTrees",this.productClassService.getAllHTMLTrees4MsSQL(request));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outJsonPlainString(response,"");
		}*/
		return SUCCESS;
	}

	/** 
	  * @Title: returnJsion 
	  * @Description: 解决json问题
	  * @param @param baseStr
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
	  * @Title: doOther 
	  * @Description: 解决struts2.0domal接受参数的方法
	  * @param @param list 装Groups的具体信息。
	  * @param @return
	  * @return List
	  * @throws 
	  */
	private List doOther(List<ProductClass> list) {
		List lists = new ArrayList();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				ProductClass productClass = list.get(i);
				map.put("name", productClass.getClassName());
				map.put("id", productClass.getClassId() + "");
				lists.add(map);
			}
		}else{
			Map<String,String> map = new HashMap<String, String>();
			map.put("error", "error");
			lists.add(map);
		}
		return lists;
	}

	/** 
	  * @Title: doRole 
	  * @Description: 解决struts2.0domal接受参数的方法
	  * @param @param list 装Role的具体信息。
	  * @param @return
	  * @return List
	  * @throws 
	  */
	
   

	public String getId() {
		return id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getTreeId() {
		return treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public String getCategoryId() {
		return categoryId;
	}





	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

}
