package com.wie.erp.biz;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Errors;
import com.wie.framework.service.ServiceInterface;

import java.util.List;
import java.util.Map;

/** 
  * @ClassName: IDictionaryService 
  * @Description: 系统字典业务层接口
  *  
  */
public interface IErrorsService extends ServiceInterface<Errors> {
	/** 
	  * @Title: getPageList 
	  * @Description: 获取具体某页的记录
	  * @param @param dic 一条具体Dictionarys对象信息
	  * @param @param page 当前页
	  * @param @param rows 一页显示的内容
	  * @param @return
	  * @return Pagination
	  * @throws 
	  */
	public Pagination getPageList(Errors Errors, int page, int rows, String sort, String order);
	
}
