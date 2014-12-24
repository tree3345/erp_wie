package com.wie.erp.biz;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.service.ServiceInterface;
import com.wie.panelClient.model.Erole;

import java.util.List;
import java.util.Map;


/** 
  * @ClassName: IDictionaryService 
  * @Description: 系统字典业务层接口
  *  
  */
public interface IEroleService extends ServiceInterface<Erole> {
	/** 
	  * @Title: getPageList 
	  * @Description: 获取具体某页的记录
	  * @param @param dic Employee
	  * @param @param page 当前页
	  * @param @param rows 一页显示的内容
	  * @param @return
	  * @return Pagination
	  * @throws 
	  */
	public Pagination getPageList(Erole erole, int page, int rows, String sort, String order);

	List<Erole> getEroleByEmp(String  empId);

	boolean saveModulesTo(Map<String, Object> map, String id);

	boolean saveFunctionsTo(Map<String, Object> map, String id);
}
