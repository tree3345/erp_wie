package com.wie.permissions.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.permissions.biz.ICheckArgumentService;
import com.wie.permissions.model.CheckArgument;


/** 
  * @ClassName: ArgumentsTg 
  * @Description:全局参数管理业务类
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("permissions.checkArgumentControl")
public class CheckArgumentTg extends BaseTg {

	/** 
	  * @Fields argumentsService :参数信息服务接口 
	  */ 
	@Autowired
	private ICheckArgumentService checkArgumentService;
	
	private CheckArgument checkArgument;
	
	private String id;
	
	
	/** 
	  * @Title: index 
	  * @Description:用于索引全局参数信息页面 
	  * @param @return
	  * @return String
	  * @throws 
	  */
	public String index() {
		return SUCCESS;
	}
    public void findByName(){
    	String param=request.getParameter("param");
    	String storeId=(String)request.getSession().getAttribute("storeId");
    	List<CheckArgument> checklist=checkArgumentService.findList("from CheckArgument where  name='"+param+"' and storeId='"+storeId+"'");
    	String isaudit="false";
    	if(checklist.size()>0){
    		isaudit=checklist.get(0).getValue();
    	}
    	returnJsion("{\"isaudit\":"+isaudit+"}", response);
    
    }
	public void save(){
		String inserted = request.getParameter("inserted");
		String deleted = request.getParameter("deleted");
		String updated = request.getParameter("updated");
		Map<String,String> datamap = new HashMap<String, String>();
		datamap.put("inserted", inserted);
		datamap.put("deleted", deleted);
		datamap.put("updated", updated);
		if (this.checkArgumentService.saveAll(datamap)) {
			returnJsion("{\"success\":\"true\"}", response);
		} else {
			returnJsion("{\"error\":\"true\"}", response);
		}
	}
	
	/** 
	  * @Title: getItems 
	  * @Description:根据argument查询符合条件的记录 
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void getItems(){
		Pagination pagination = this.checkArgumentService.getPageList(checkArgument,page, rows,sort,order);
		List list = pagination.getList();
		JSONArray json = JSONArray.fromObject(list);
		try {
			outJsonString(response,"{\"total\":" + pagination.getTotalCount() + ",\"rows\":"+json.toString() + "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	
	public CheckArgument getCheckArgument() {
		return checkArgument;
	}

	public void setCheckArgument(CheckArgument checkArgument) {
		this.checkArgument = checkArgument;
	}

}
