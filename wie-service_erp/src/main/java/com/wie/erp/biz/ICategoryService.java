package com.wie.erp.biz;
import java.util.List;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Category;
import com.wie.framework.service.ServiceInterface;

/** 
  * @ClassName: ISystemService 
  * @Description: 系统信息业务层接口
  *  
  */
public interface ICategoryService extends ServiceInterface<Category> {
	/** 
	  * @Fields TREE_NAME : 在构造树的时候用于标识当前节点的类型 
	  */
	String TREE_NAME = "CATEGORY";
	/** 
	  * @Title: getPageList 
	  * @Description:  获取具体某页的记录
	  * @param @param sys 一条具体Systems对象的信息
	  * @param @param page 当前页
	  * @param @param rows 一页显示的多少记录
	  * @param @return
	  * @return Pagination
	  * @throws 
	  */
	public Pagination getPageList(Category category,int page,int rows,String sort,String order);
	/** 
	  * @Title: getAll 
	  * @Description: 获取所有的系统信息
	  * @param @return
	  * @return List<Systems>
	  * @throws 
	  */
	public List<Category> getAll();
}
