package com.wie.erp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Warehouse;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: ActionsDAO 
  * @Description: 操作管理DAO 
  *  
  */
@Repository(value="warehouseDao")
public class WarehouseDao extends BaseDAO<Warehouse> {

	
	
	/** 
	  * @Title: getAll 
	  * @Description: 获得所有记录 
	  * @param @return
	  * @return List<Actions>
	  * @throws 
	  */
	public List<Warehouse> getAll(){
		return this.findList("from Warehouse");
	}

	public List<Warehouse> getAllWarehouse() {
		// TODO Auto-generated method stub
		return null;
	}

	public Pagination getPageList(Warehouse warehouse, int page, int rows,String sort, String order) {
		StringBuffer sb = new StringBuffer("from Warehouse w where 1=1");
		
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
}
