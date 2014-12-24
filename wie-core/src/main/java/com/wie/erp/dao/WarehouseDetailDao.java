package com.wie.erp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Product;
import com.wie.erp.model.WarehouseDetail;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: ActionsDAO 
  * @Description: 操作管理DAO 
  *  
  */
@Repository(value="warehouseDetailDao")
public class WarehouseDetailDao extends BaseDAO<WarehouseDetail> {

	
	
	/** 
	  * @Title: getAll 
	  * @Description: 获得所有记录 
	  * @param @return
	  * @return List<Actions>
	  * @throws 
	  */
	public List getAll(){
		String sql="select p.productName,d.quantity,s.storename from jn_warehousedetail d "
				+ "left join jn_product p on d.productId=p.productId "
				+ "left join jn_warehouse w on w.warehouseid=d.warehouseid "
				+ "left join jn_store s on w.storeid=s.storeid";
		return this.executebySql(sql);
	}

	public Pagination getPageList(WarehouseDetail warehouseDetail, int page,
			int rows, String sort, String order) {
		StringBuffer sb = new StringBuffer("from WarehouseDetail b where 1=1 ");
		
		if(null != warehouseDetail){
			if(null!=warehouseDetail.getWarehouse())
			{
				sb.append(" and b.warehouse.storeId in ("+warehouseDetail.getWarehouse().getStoreId()+") ");
			}
		}
		if(null != warehouseDetail){
			if(null != warehouseDetail.getProduct()){
				Product product=warehouseDetail.getProduct();
				if(null!=product.getProductName()&&!"".equals(product.getProductName())){
					sb.append(" and b.product.productName like'%" + warehouseDetail.getProduct().getProductName() + "%' ");
				}
				if(null!=product.getProductClass()&&!"".equals(product.getProductClass()))
				sb.append(" and b.product.productClass.classId ='" + warehouseDetail.getProduct().getProductClass().getClassId() + "' ");
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

	public List<WarehouseDetail> getAllWarehouse() {
		// TODO Auto-generated method stub
		return null;
	}
}
