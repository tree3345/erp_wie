package com.wie.erp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Inventory;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: CategoryDao 
  * @Description: 大类Dao层
  *  
  */
@Repository
public class InventoryDao extends BaseDAO<Inventory> {
	
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
	public Pagination getPageList(Inventory inventory,int page, int rows,String sort,String order) {
		StringBuffer sb = new StringBuffer("from Inventory b where 1=1 ");
		if(null != inventory){
			if(null != inventory.getCode() && !"".equals(inventory.getCode())){
				sb.append(" and b.code like'%" + inventory.getCode() + "%' ");
			}
		}
		
		if(null != inventory){
			if(null != inventory.getStoreId() && !"".equals(inventory.getStoreId())){
				sb.append(" and b.storeId in(" + inventory.getStoreId() + ") ");
			}
		}
		//排序
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
	public List<Inventory> getAll(){
		StringBuffer sb = new StringBuffer("from Inventory");
		return this.getHandler().findListOfObj(sb.toString());
	}
}
