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
import com.wie.erp.biz.IProductService;
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
@Controller("classControl")
public class ClassTg extends BaseTg {
	/** 
	  * @Fields groupService : IGroupService接口spring注入 
	  */ 
	@Autowired
	private IProductClassService productClassService;
	
	@Autowired
	private IProductService productService;
	
	@Autowired
	private ICategoryService    categoryService;
	
	/** 
	  * @Fields group : struts2.0自动接受
	  */ 
	private ProductClass productClass;
	
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
	public void create()
	{
		String parentId=request.getParameter("parentId");
		if(productClass==null)
			productClass=new ProductClass();
		if(!"0".equals(parentId))
			productClass.setParentId(parentId);
		productClass.setClassName("新分类");
		productClass.setSort(0);
		productClassService.save(productClass);
		String id=productClass.getClassId();
		String text=productClass.getClassName();
		returnJsion("{\"id\":\""+id+"\",\"text\":\""+text+"\"}",response);
	}
	public void update()
	{
		String id=request.getParameter("id");
		String text=request.getParameter("text");
		if(productClass==null)productClass=new ProductClass();
		productClass=productClassService.findById(id);
		if(!text.equals(productClass.getClassName()))
		{
			productClass.setClassName(text);
			productClassService.alter(productClass);
		}
	}
	
	public void destroy(){
		if(productClassService.deleteById(id))
		{
			returnJsion("{\"success\":true}",response);
		}else
		{
			returnJsion("{\"error\":\"true\"}",response);
		}
	}
	public void destroyJudge()
	{
		String id=request.getParameter("id");
			int size=productService.findCountBySql("from Product p where p.productClass.classId='"+id+"'");
			if(size>0){
				returnJsion("{\"error\":\"true\",\"message\":\"该分类下存在商品，不能删除！\"}",response);
			}
			else{
				returnJsion("{\"success\":\"true\"}",response);
			}
	}
	public void dnd()
	{
		String id=request.getParameter("id");
		String targetId=request.getParameter("targetId");
		String point=request.getParameter("point");
		System.err.println(id);
		System.err.println(targetId);
		System.err.println(point);
		if(productClass==null)
			productClass=new ProductClass();
		
		productClass=productClassService.findById(id);
		ProductClass targetType=productClassService.findById(targetId);
		
		if("append".equals(point))
			productClass.setParentId(targetId);
		
		if("top".equals(point)){
			productClass.setParentId(targetType.getParentId());
		}
			
		if("bottom".equals(point))
		{
			productClass.setParentId(targetType.getParentId());
		}
			
		productClassService.alter(productClass);
			
	}
	
	public void getItems(){
		String cond = "";
		String parentId=request.getParameter("parentId");
		if (parentId != null){
			cond = " and (id='"+parentId+"' or parentId='" + parentId+"')";
		}
		List<ProductClass> list=productClassService.findList("from ProductClass i where 1=1"+cond);
//		return new JsonView(productClass.findAll(productClass.class, cond, null, "sort"));
		returnJsion(JSONArray.fromObject(list).toString(),response);
	}
	
	
	public void getTree(){
		String hql="from ProductClass i where i.parentId is null or i.parentId ='' order by sort";
        List<Map<String,Object>> nodes = new ArrayList<Map<String,Object>>();
         
		// 获取一级节点
		List<ProductClass> types = productClassService.findList(hql);
		//.findAll(GoodType.class, "parentId=0", null, "sort");
		for(ProductClass type: types){
			Map<String,Object> node = new HashMap<String,Object>();
			node.put("id", type.getClassId());
			node.put("text", type.getClassName());
			if(productClassService.findCountBySql("from ProductClass p where p.parentId='"+type.getClassId()+"'")>0)
			node.put("state", "closed");
			nodes.add(node);
		}
		
		// 循环获取全部子节点
		List<Map<String,Object>> doing = new ArrayList<Map<String,Object>>();
		doing.addAll(nodes);
		while(!doing.isEmpty()){
			List<Map<String,Object>> todo = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> item: doing){
				String obj=(String) item.get("id");
				System.err.println(obj);
				types = productClassService.findList("from ProductClass p where p.parentId='"+obj+"' order by sort");
//(GoodType.class, "parentId=?", new Object[]{item.get("id")}, "sort");
				if (types.isEmpty()) continue;
				
				List<Object> children = new ArrayList<Object>();
				for(ProductClass type: types){
					Map<String,Object> node = new HashMap<String,Object>();
					node.put("id", type.getClassId());
					node.put("text", type.getClassName());
					children.add(node);
					
					todo.add(node);
				}
				item.put("children", children.toArray(new Object[children.size()]));
			}
			doing = todo;
		}
		returnJsion(JSONArray.fromObject(nodes).toString(),response);
	}
	/**
	  * @Title: save 
	  * @Description: 保存一条组的记录
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void save(){
		String updated=request.getParameter("updated");
		if(this.productClassService.saveAll(updated)){
			returnJsion("{\"success\":\"true\"}",response);
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}
	}
	
	/** 
	  * @Title: list 
	  * @Description: 获取所有组，并且用tree来显示
	  * @param @return
	  * @return String 一段<html>代码
	  * @throws 
	  */
	public void list(){
		List<ProductClass> list = productClassService.getAll();
		// 业务数据解码器，从业务数据中分解出id和parentid
		UserDataUncoder orgUncoder = new UserDataUncoder() {
			public Object getID(Object pUserData) throws UncodeException {
				ProductClass productClass = (ProductClass) pUserData;
				return productClass.getClassId();
			}

			public Object getParentID(Object pUserData) throws UncodeException {
				ProductClass productClass = (ProductClass) pUserData;
				return productClass.getParentId();
			}
		};

		// Tree模型构造器，用于生成树模型
		AbstractWebTreeModelCreator treeModelCreator = new AbstractWebTreeModelCreator() {
			// 该方法负责将业务数据映射到树型节点
			protected Node createNode(Object pUserData, UserDataUncoder pUncoder) {
				ProductClass productClass = (ProductClass) pUserData;
				WebTreeNode result = new WebTreeNode(productClass.getClassName(), "node"+productClass.getClassId(),productClass);

				return result;
			}
		};
		String temp = Utils.showeasyuiTree(orgUncoder, treeModelCreator, list, request);
		outHtmlUTFString(response,temp);
	}

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
    public void getAll()
    {
    	List list=productClassService.findList("from ProductClass");
		JSONArray jsonArray = JSONArray.fromObject(list);
		returnJsion(jsonArray.toString(),response);
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


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public ProductClass getProductClass() {
		return productClass;
	}
	public void setProductClass(ProductClass productClass) {
		this.productClass = productClass;
	}

	
}
