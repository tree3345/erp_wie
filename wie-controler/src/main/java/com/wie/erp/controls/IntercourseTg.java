package com.wie.erp.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IIntercourseService;
import com.wie.erp.biz.IIntercourseTypeService;
import com.wie.erp.model.Intercourse;
import com.wie.erp.model.IntercourseType;
import com.wie.erp.model.Product;
import com.wie.framework.controls.struts2.BaseTg;


/** 
  * @ClassName: DictionaryTg 
  * @Description: 系统字典控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("intercourseControl")
public class IntercourseTg extends BaseTg {
	/** 
	  * @Fields dictionaryService : IDictionaryService业务层接口注入
	  */ 
	@Autowired
	private IIntercourseService intercourseService;
	
	@Autowired
	private IIntercourseTypeService intercourseTypeService;
	
	/** 
	  * @Fields dic : struts2.0接受前台信息（domain接受）
	  */ 
	private Intercourse intercourse;

	
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
	public String index(){
		String[] params={"id","name"};
		List list=intercourseTypeService.getAll();
		List lists=doListObj(list,params);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		request.setAttribute("intercourseType", jsonArray.toString());
		return this.SUCCESS;
	}
	
	public void getJson(){
		List list=intercourseService.findList("select id,shortName from Intercourse");
		String[] params={"id","shortName"};
		List lists=doListObj(list,params);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		returnJsion(jsonArray+"", response);
	}
	/** 
	  * @Title: getItems 
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void getItems(){
		if(intercourse==null)intercourse=new Intercourse();
		String params=request.getParameter("q");
		intercourse.setShortName(params);
		Pagination pagination=intercourseService.getPageList(intercourse, page, rows, sort,order);
		List<Intercourse> list = pagination.getList();
		List<Map<String, String>> lists = doList(list);	
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + pagination.getTotalCount() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr,response);
	}

	public void isExistData()
	{
		String intercourseId=request.getParameter("intercourseId") ;
		List list=intercourseService.executebysql("select count(*) from JN_purchase p where p.intercourseId='"+intercourseId+"' union select count(*) from JN_order o where o.intercourseId='"+intercourseId+"'");
	     String baseStr="{\"success\":\"true\"}";
		 for(int i=0;i<list.size();i++)
	     {
	    	 Integer count=Integer.parseInt(list.get(i).toString());
	    	 if(count>0){
	    		 baseStr ="{\"error\":\"true\"}";
	    		 break;
	    	 }
	     }
	     returnJsion(baseStr,response);
	}
	/** 
	  * @Title: save 
	  * @Description: 保存一条系统字典表中的常量信息
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void save(){
		
		String inserted=request.getParameter("inserted");
		String deleted=request.getParameter("deleted");
		String updated=request.getParameter("updated");
		if(this.intercourseService.saveAll(inserted, deleted, updated)){
				returnJsion("{\"success\":\"true\"}",response);
			}else{
				returnJsion("{\"error\":\"true\"}",response);
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
		Intercourse intercourse = this.intercourseService.findById(id);
		List<Intercourse> list = new ArrayList<Intercourse>();
		if(null != intercourse){
			list.add(intercourse);
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
		
			if(this.intercourseService.alter(intercourse)){
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
		/*if(this.productService.deleteByIdMsSQL(id)){
			returnJsion("{\"success\":\"true\"}",response);	
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}*/
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
	private List doList(List<Intercourse> list) {
		List lists = new ArrayList();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				Intercourse intercourse = list.get(i);
				IntercourseType tp=intercourseTypeService.findById(intercourse.getIntercourseTypeId());
				map.put("addr", intercourse.getAddr());
				map.put("code", intercourse.getCode());
				map.put("contactMan", intercourse.getContactMan());
				map.put("email", intercourse.getEmail());
				map.put("fax", intercourse.getFax());
				map.put("fullName", intercourse.getFullName());
				map.put("id", intercourse.getId());
				map.put("intercourseTypeId", intercourse.getIntercourseTypeId());
				map.put("intercourseTypeName", tp==null?"":tp.getName());
				map.put("phone", intercourse.getPhone());
				map.put("postcode", intercourse.getPostcode());
				map.put("remark", intercourse.getRemark());
				map.put("shortName", intercourse.getShortName());
				map.put("www", intercourse.getWww());
				lists.add(map);
				
			}
		}
		return lists;
	}

	private List doOther(List<Product> list) {
		List lists = new ArrayList();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				Product product = list.get(i);
				map.put("productName", product.getProductName());
				map.put("spelling", product.getSpelling());
				map.put("id", product.getProductId() + "");
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
	private List doListStr(List<String> list,String params) {
		List lists = new ArrayList();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				String str =(String) list.get(i);
					map.put(params, str);
				lists.add(map);
			}
		}
		return lists;
	}


	public Intercourse getIntercourse() {
		return intercourse;
	}

	public void setIntercourse(Intercourse intercourse) {
		this.intercourse = intercourse;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
