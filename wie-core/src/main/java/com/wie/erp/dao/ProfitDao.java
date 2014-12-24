package com.wie.erp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Profit;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: CategoryDao 
  * @Description: 大类Dao层
  *  
  */
@Repository
public class ProfitDao extends BaseDAO<Profit> {
	
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
	public Pagination getPageList(Profit profit,int page, int rows,String sort,String order) {
		StringBuffer sb = new StringBuffer("from Profit bd where 1=1");
		/*if(null != profit){
			if(null != billDetail.getBill()){
				sb.append(" and bd.bill.billId ='" + billDetail.getBill().getBillId() + "'");
			}
		}*/
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
	
	public List getStatisticsPageList(Profit profit,int page, int rows,String sort,String order) {
		StringBuffer sb = new StringBuffer("select product.productId,product.productName,product.unit,sum(quantity) as dq ,sum(quantity*price) as account from BillDetail  where 1=1  ");
		
		/*if (billDetail != null) {
			
			if (billDetail.getProduct() != null)
				if (billDetail.getProduct().getProductName() != null
						&& !"".equals(billDetail.getProduct().getProductName())) {
					sb.append(" and product.productName like '%"+ billDetail.getProduct().getProductName()+ "%'");
				}
            
			if (billDetail.getBill() != null)
				if ((billDetail.getBill().getStartTime() != null && !""
						.equals(billDetail.getBill().getStartTime()))) {
					sb.append(" and bill.billDateTime>'"+ billDetail.getBill().getStartTime()+ "'");
				}
				if ((billDetail.getBill().getEndTime() != null && !""
						.equals(billDetail.getBill().getEndTime()))) {
					sb.append(" and bill.billDateTime<'"+ billDetail.getBill().getEndTime() + "'");
				}
	
				if (billDetail.getBill().getStoreId() != null
						&& !"".equals(billDetail.getBill().getStoreId())) {
					sb.append(" and bill.storeId='"+ billDetail.getBill().getStoreId() + "'");
				}
		}*/
		//分组、排序
		sb.append(" group by product.productId,product.productName,product.unit");
		if (sort != null && !"".equals(sort)) {
			sb.append(" order by " + sort);
			if (order != null && !"".equals(order)) {
				sb.append(" " + order);
			} else {
				sb.append(" desc");
			}
			
		}
		return  super.getHandler().findListOfObj(sb.toString(), page, rows);/*(Finder.create(sb.toString()), page, rows);*/
	}
	
	public int findCountBySql(Profit profit) {
        StringBuffer sb = new StringBuffer("select product.productId,product.productName,sum(quantity) as dq "
        		+ "from BillDetail  where 1=1");
		
		/*if (profit != null) {
			
			if (profit.getProduct() != null)
				if (profit.getProduct().getProductName() != null
						&& !"".equals(profit.getProduct().getProductName())) {
					sb.append(" and product.productName like '%"+ profit.getProduct().getProductName()+ "%'");
				}
            
			if (billDetail.getBill() != null)
				if ((billDetail.getBill().getStartTime() != null && !""
						.equals(billDetail.getBill().getStartTime()))) {
					sb.append(" and bill.billDateTime>'"+ billDetail.getBill().getStartTime()+ "'");
				}
				if ((billDetail.getBill().getEndTime() != null && !""
						.equals(billDetail.getBill().getEndTime()))) {
					sb.append(" and bill.billDateTime<'"+ billDetail.getBill().getEndTime() + "'");
				}
	
				if (billDetail.getBill().getStoreId() != null
						&& !"".equals(billDetail.getBill().getStoreId())) {
					sb.append(" and bill.storeId='"+ billDetail.getBill().getStoreId() + "'");
				}
		}
		//分组、排序
		sb.append(" group by product.productId,product.productName");*/
		return this.findList(sb.toString()).size();
	}
	
}
