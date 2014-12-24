package com.wie.erp.controls;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IErrorsService;
import com.wie.erp.model.Errors;
import com.wie.framework.controls.struts2.BaseTg;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


/** 
  * @ClassName: DictionaryTg 
  * @Description: 系统字典控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("errorsControl")
public class ErrorsTg extends BaseTg {
	/** 
	  * @Fields dictionaryService : IDictionaryService业务层接口注入
	  */ 
	@Autowired
	private IErrorsService errorsService;
	
	
	/** 
	  * @Fields dic : struts2.0接受前台信息（domain接受）
	  */ 
	private Errors errorsw;

	
	/** 
	  * @Fields id : 删除，更新所需要的id，struts2.0接受
	  */ 
	private String id;

	/** 
	  * @Title: index 
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
		if(errorsw==null)errorsw=new Errors();
		String errorContent=request.getParameter("errorContent");
        String macAddress=request.getParameter("macAddredd");
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        errorsw.setErrorContent(errorContent);
        errorsw.setMacAddress(macAddress);
        errorsw.setStartTime(startTime);
        errorsw.setEndTime(endTime);
		Pagination pagination = errorsService.getPageList(errorsw, page, rows,sort,order);
		List<Errors> list = pagination.getList();
		List lists = doList(list);	
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + pagination.getTotalCount() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr,response);
	}

    public void find(){
        JSONObject obj= JSONObject.fromObject(errorsService.findById(id));
        returnJsion(obj.toString(),response);
        //errorsService.findById(id);
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
	private List doList(List<Errors> list) {
		List lists = new ArrayList();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				Errors error = list.get(i);
				map.put("id", error.getId());
				map.put("errorContent", error.getErrorContent());
                map.put("macAddress",error.getMacAddress());
                map.put("errorDateTime",error.getErrorDateTime()+"");
				lists.add(map);
			}
		}
		return lists;
	}

    public Errors getErrorsw() {
        return errorsw;
    }
    public void setErrorsw(Errors errorsw) {
        this.errorsw = errorsw;
    }
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
