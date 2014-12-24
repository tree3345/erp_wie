package com.wie.erp.biz;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Category;
import com.wie.erp.model.CloneProductClass;
import com.wie.erp.model.ProductClass;
import com.wie.framework.service.ServiceInterface;
import com.wie.permissions.model.CloneResources;
import com.wie.permissions.model.Resources;


/** 
  * @ClassName: IDictionaryService 
  * @Description: 系统字典业务层接口
  *  
  */
public interface IProductClassService extends ServiceInterface<ProductClass> {
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
	public Pagination getPageList(ProductClass productClass,int page,int rows,String sort,String order);
	
	/** 
	  * @Title: getAllDictionary 
	  * @Description: 获取所有的系统字典的记录
	  * @param @return
	  * @return List<Dictionarys>
	  * @throws 
	  */
	
	public List<ProductClass> getAll();
	
	
	public List<Map<String,Object>> getTree(String id);
	

	public void delProductFromClass(String groupid, String id);

	public void addProductToClass(String userIds, String groupid);

	public boolean saveAll(String updated);

}
