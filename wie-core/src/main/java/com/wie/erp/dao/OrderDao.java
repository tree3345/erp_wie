package com.wie.erp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Bill;
import com.wie.erp.model.Order;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: CategoryDao 
  * @Description: 大类Dao层
  *  
  */
@Repository
public class OrderDao extends BaseDAO<Order> {
	
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
	public Pagination getPageList(Order orderIn,int page, int rows,String sort,String order) {
		StringBuffer sb = new StringBuffer("from Order b where 1=1 ");
		if(null != orderIn){
			if(null != orderIn.getOrderNumber() && !"".equals(orderIn.getOrderNumber())){
				sb.append(" and b.orderNumber like'%" + orderIn.getOrderNumber() + "%' ");
			}
		}
		if(null != orderIn){
			if(null != orderIn.getStoreId() && !"".equals(orderIn.getStoreId())){
				sb.append(" and b.storeId in (" + orderIn.getStoreId() + ") ");
			}
		}
		if(null != orderIn){
			if(null!=orderIn.getFlag()&&!"0".equals(orderIn.getFlag())){
				sb.append(" and b.status=1 ");
				sb.append(" and b.orderId not in (SELECT order.orderId FROM Purchase WHERE order.orderId is not null)");
//				List<String> list=this.executebySql("select jp.orderid from jn_purchase jp where jp.orderid is not null");
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
	public List<Order> getAll(){
		StringBuffer sb = new StringBuffer("from Order");
		return this.getHandler().findListOfObj(sb.toString());
	}
}
