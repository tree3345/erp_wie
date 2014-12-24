package com.wie.erp.biz;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Category;
import com.wie.erp.model.IntercourseType;
import com.wie.framework.service.ServiceInterface;


/** 
  * @ClassName: IDictionaryService 
  * @Description: 系统字典业务层接口
  *  
  */
public interface IIntercourseTypeService extends ServiceInterface<IntercourseType> {
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
	public Pagination getPageList(IntercourseType intercourseType,int page,int rows,String sort,String order);

	public boolean saveAll(String updated);

	public List getAll();
	

}
