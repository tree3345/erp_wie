package com.wie.erp.biz;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.service.ServiceInterface;
import com.wie.panelClient.model.Module;

import java.util.List;


/** 
  * @ClassName: IDictionaryService 
  * @Description: 系统字典业务层接口
  *  
  */
public interface IModuleService extends ServiceInterface<Module> {
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
	public Pagination getPageList(Module module, int page, int rows, String sort, String order);

	Integer getMaxId();

	List<Module> getModulesByErole(String id);

	void saveModuleFuncions();

}
