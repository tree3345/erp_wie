package com.wie.erp.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wie.erp.biz.IIntercourseService;
import com.wie.erp.biz.IIntercourseTypeService;
import com.wie.erp.model.IntercourseType;
import com.wie.erp.model.ProductClass;
import com.wie.framework.controls.struts2.BaseTg;


/** 
  * @ClassName: GroupTg 
  * @Description: 组信息控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("intercourseTypeControl")
public class IntercourseTypeTg extends BaseTg {
	/** 
	  * @Fields groupService : IGroupService接口spring注入 
	  */ 
	@Autowired
	private IIntercourseTypeService intercourseTypeService;
	@Autowired
	private IIntercourseService intercourseService;
	private IntercourseType intercourseType;
	
	
	public IntercourseType getIntercourseType() {
		return intercourseType;
	}
	public void setIntercourseType(IntercourseType intercourseType) {
		this.intercourseType = intercourseType;
	}

	/** 
	  * @Fields id : 要删除的id
	  */ 
	private String id;
	private String parentId;
	
	public void create()
	{
		String parentId=request.getParameter("parentId");
		if(intercourseType==null)
			intercourseType=new IntercourseType();
		if(!"0".equals(parentId))
			intercourseType.setParentId(parentId);
		intercourseType.setName("新分类");
		intercourseType.setSort(0);
		intercourseTypeService.save(intercourseType);
		String id=intercourseType.getId();
		String text=intercourseType.getName();
		returnJsion("{\"id\":\""+id+"\",\"text\":\""+text+"\"}",response);
	}
	public void update()
	{
		String id=request.getParameter("id");
		String text=request.getParameter("text");
		if(intercourseType==null)intercourseType=new IntercourseType();
		intercourseType=intercourseTypeService.findById(id);
		if(!text.equals(intercourseType.getName()))
		{
			intercourseType.setName(text);
			intercourseTypeService.alter(intercourseType);
		}
	}
	
	public void destroy()
	{
		String id=request.getParameter("id");
		if(intercourseTypeService.deleteById(id))
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
			int size=intercourseService.findCountBySql("from Intercourse i where i.intercourseTypeId='"+id+"'");
			if(size>0){
				returnJsion("{\"error\":\"true\",\"message\":\"该分类下存在单位，不能删除！\"}",response);
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
		if(intercourseType==null)
			intercourseType=new IntercourseType();
		
		intercourseType=intercourseTypeService.findById(id);
		IntercourseType targetType=intercourseTypeService.findById(targetId);
		
		if("append".equals(point))
			intercourseType.setParentId(targetId);
		
		if("top".equals(point)){
			intercourseType.setParentId(targetType.getParentId());
		}
			
		if("bottom".equals(point))
		{
			intercourseType.setParentId(targetType.getParentId());
		}
			
		intercourseTypeService.alter(intercourseType);
			
	}
	
	public void getItems(){
		String cond = "";
		if (parentId != null){
			cond = " and (id='"+parentId+"' or parentId='" + parentId+"')";
		}
		List<IntercourseType> list=intercourseTypeService.findList("from IntercourseType i where 1=1"+cond);
//		return new JsonView(IntercourseType.findAll(IntercourseType.class, cond, null, "sort"));
		returnJsion(JSONArray.fromObject(list).toString(),response);
	}
	
	
	public void getTree(){
		String hql="from IntercourseType i where i.parentId is null or i.parentId ='' order by sort";
        List<Map<String,Object>> nodes = new ArrayList<Map<String,Object>>();
         
		// 获取一级节点
		List<IntercourseType> types = intercourseTypeService.findList(hql);
		//.findAll(GoodType.class, "parentId=0", null, "sort");
		for(IntercourseType type: types){
			Map<String,Object> node = new HashMap<String,Object>();
			node.put("id", type.getId());
			node.put("text", type.getName());
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
				types = intercourseTypeService.findList("from IntercourseType p where p.parentId='"+obj+"' order by sort");
//(GoodType.class, "parentId=?", new Object[]{item.get("id")}, "sort");
				if (types.isEmpty()) continue;
				
				List<Object> children = new ArrayList<Object>();
				for(IntercourseType type: types){
					Map<String,Object> node = new HashMap<String,Object>();
					node.put("id", type.getId());
					node.put("text", type.getName());
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
	  * @Title: all 
	  * @Description: 获得所有的组
	  * @param 
	  * @return void
	  * @throws 
	  */
	/*public void all(){
		List<IntercourseType> list = intercourseTypeService.getAll();
		JSONArray jsonArray = JSONArray.fromObject(list);
		returnJsion(jsonArray.toString(),response);
	}*/
	public void getAll(){
		try{
			List<IntercourseType> intypes = this.intercourseTypeService.getAll();
			List<Map<String,String>> maps = new ArrayList<Map<String,String>>();
			for(Iterator<IntercourseType> iter=intypes.iterator();iter.hasNext();){
				Map<String,String> map = new HashMap<String,String>();
				IntercourseType intype = iter.next();
				map.put("id", intype.getId()+"");
				map.put("name", intype.getName());
				maps.add(map);
			}
			JSONArray json = JSONArray.fromObject(maps);
			outJsonUTFString(response, json.toString());
		}catch(Exception e){
			e.printStackTrace();
			outJsonUTFString(response, "[]");
		}
	}
	
	/** 
	  * @Title: find 
	  * @Description: 根据id查找组的信息
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void find(){
		/*ProductClass productClass = this.intercourseTypeService.findById(id);
		List<ProductClass> list = new ArrayList<ProductClass>();
		if(null != productClass){
			list.add(productClass);
		}
		JSONArray jsonArray = JSONArray.fromObject(doOther(list));
		
		if(null != productClass.getParentId()&&!productClass.getParentId().equals("")){
			ProductClass temp = this.productClassService.findById(productClass.getParentId());
			returnJsion("{\"rows\":" + jsonArray.toString() + ",\"parentid\":\"" + temp.getClassId() + "\",\"parentName\":\"" + temp.getClassName() + "\"}",response);
		}else{
			returnJsion("{\"rows\":" + jsonArray.toString() + ",\"parentid\":\"\"}",response);
		}*/
	}
	
	/** 
	  * @Title: del 
	  * @Description: 根据id删除组的信息
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void del(){
		/*if(this.productClassService.deleteById(id)){
			returnJsion("{\"success\":\"true\"}",response);
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}*/
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
		if(this.intercourseTypeService.saveAll(updated)){
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
		/*List<ProductClass> list = productClassService.getAll();
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
		outHtmlUTFString(response,temp);*/
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

	/** 
	  * @Title: returnJsion 
	  * @Description: 解决json问题
	  * @param @param baseStr
	  * @param @param response
	  * @return void
	  * @throws 
	  */
	private List<Map<String, String>> doList(List<IntercourseType> list) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		//dic.setCreateTime(format.format(new Date()));
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				IntercourseType in = list.get(i);
				map.put("id", in.getId());
				map.put("name", in.getName());
				lists.add(map);
			}
		}
		return lists;
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}
