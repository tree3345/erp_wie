package com.wie.erp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Category;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;
import com.wie.permissions.model.Systems;

/** 
  * @ClassName: CategoryDao 
  * @Description: 大类Dao层
  *  
  */
@Repository
public class CategoryDao extends BaseDAO<Category> {
	
	/** 
	  * @Title: getPageList 
	  * @Description: 获取具体某页的信息
	  * @param @param sys 一条具体Systems对象的信息
	  * @param @param page 当前页
	  * @param @param rows 一页显示多少记录
	  * @param @return
	  * @return Pagination
	  * @throws 
	  */
	public Pagination getPageList(Category category,int page, int rows,String sort,String order) {
		StringBuffer sb = new StringBuffer("from Category where 1=1");
		if(null != category){
			if(null != category.getCategoryName() && !"".equals(category.getCategoryName())){
				sb.append(" and Category like'%" + category.getCategoryName() + "%'");
			}
		}
		if(sort!=null && !"".equals(sort)){
			sb.append(" order by "+sort);
			if(order!=null && !"".equals(order)){
				sb.append(" "+order);
			}else{
				sb.append(" desc");
			}
		}
		return super.getHandler().getPageList(Finder.create(sb.toString()), page, rows);
	}
	
	/** 
	  * @Title: getAll 
	  * @Description: 获取所有的系统信息
	  * @param @return
	  * @return List<Systems>
	  * @throws 
	  */
	public List<Category> getAll(){
		StringBuffer sb = new StringBuffer("from Category");
		
		return this.getHandler().findListOfObj(sb.toString());
	}
	
}
