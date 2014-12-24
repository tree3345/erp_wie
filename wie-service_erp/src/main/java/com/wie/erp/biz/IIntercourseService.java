package com.wie.erp.biz;

import java.util.List;

import com.wie.common.tools.page.Pager;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Intercourse;
import com.wie.erp.model.Product;
import com.wie.framework.service.ServiceInterface;

/** 
  * @ClassName: IDictionaryService 
  * @Description: 系统字典业务层接口
  *  
  */
public interface IIntercourseService extends ServiceInterface<Intercourse> {
	/** 
	  * @Title: findMax 
	  * @Description: 获取昵称后的值
	  * @param @param nickName 昵称  生成的代码
	  * @param @return
	  * @return String
	  * @throws 
	  */
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
	public Pagination getPageList(Intercourse intercourse,int page,int rows,String sort,String order);
	
	
	/** 
	  * @Title: getAllDictionary 
	  * @Description: 获取所有的系统字典的记录
	  * @param @return
	  * @return List<Dictionarys>
	  * @throws 
	  */
	
	public List executebysql(String sql);


	public boolean saveAll(String inserted, String deleted, String updated);
	

}
