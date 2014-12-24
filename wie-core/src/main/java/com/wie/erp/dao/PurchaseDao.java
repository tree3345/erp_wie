package com.wie.erp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Order;
import com.wie.erp.model.Purchase;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: CategoryDao 
  * @Description: 大类Dao层
  *  
  */
@Repository
public class PurchaseDao extends BaseDAO<Purchase> {
	
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
	public Pagination getPageList(Purchase purchase,int page, int rows,String sort,String order) {
		StringBuffer sb = new StringBuffer("from Purchase b where 1=1 ");
		if(null != purchase){
			if(null != purchase.getCode() && !"".equals(purchase.getCode())){
				sb.append(" and b.code like'%" + purchase.getCode() + "%' ");
			}
		}
		
		if(null != purchase){
			if(null != purchase.getStoreId() && !"".equals(purchase.getStoreId())){
				sb.append(" and b.storeId in (" + purchase.getStoreId() + ") ");
			}
		}
		
		if(null!=purchase)
		   if(null!=purchase.getOrder()){
			   if(null!=purchase.getOrder().getOrderNumber()&&!"".equals(purchase.getOrder().getOrderNumber()))
			   {
				   sb.append(" and b.order.orderNumber like '%"+purchase.getOrder().getOrderNumber()+"%'");
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
		}else{sb.append(" order by indt desc");}
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
