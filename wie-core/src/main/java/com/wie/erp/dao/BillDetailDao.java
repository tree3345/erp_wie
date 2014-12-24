package com.wie.erp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.BillDetail;
import com.wie.erp.model.Category;
import com.wie.erp.model.PurchaseDetail;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: CategoryDao 
  * @Description: 大类Dao层
  *  
  */
@Repository
public class BillDetailDao extends BaseDAO<BillDetail> {
	
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
	public Pagination getPageList(BillDetail billDetail,int page, int rows,String sort,String order) {
		StringBuffer sb = new StringBuffer("from BillDetail bd where 1=1 ");
		if(null != billDetail){
			if(null != billDetail.getBill()){
                if(null!=billDetail.getBill().getBillId())
				  sb.append(" and bd.bill.billId ='" + billDetail.getBill().getBillId() + "'");
                if(null!=billDetail.getBill().getStoreId()&&!"".equals(billDetail.getBill().getStoreId()))
                    sb.append(" and bd.bill.storeId in("+billDetail.getBill().getStoreId()+")");
                if ((billDetail.getBill().getStartTime() != null && !""
                        .equals(billDetail.getBill().getStartTime()))) {
                    sb.append(" and bd.bill.billDateTime>'"+ billDetail.getBill().getStartTime()+ "'");
                }
                if ((billDetail.getBill().getEndTime() != null && !""
                        .equals(billDetail.getBill().getEndTime()))) {
                    sb.append(" and bd.bill.billDateTime<'"+ billDetail.getBill().getEndTime() + "'");
                }
                if(billDetail.getBill().getCreateBy()!=null){
                    sb.append(" and bd.bill.createBy.userName like '%"+billDetail.getBill().getCreateBy().getUserName()+"%'");
                }
			}
            if(null!=billDetail.getProduct()){
                sb.append(" and bd.product.productId='"+billDetail.getProduct().getProductId()+"'");
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
	
	public List getStatisticsPageList(BillDetail billDetail,int page, int rows,String sort,String order) {
		StringBuffer sb = new StringBuffer("select product.productId,product.productName,product.unit,sum(quantity) as dq ,sum(quantity*price) as account ,sum(quantity*profit.profitPrice) as profits from BillDetail  where 1=1  ");
		
		if (billDetail != null) {
			
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
					sb.append(" and bill.storeId in("+ billDetail.getBill().getStoreId() + ")");
				}
            if(billDetail.getBill().getCreateBy()!=null){
                sb.append(" and bill.createBy.userName like '%"+billDetail.getBill().getCreateBy().getUserName()+"%'");
            }
		}
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
	
	public int findCountBySql(BillDetail billDetail) {
        StringBuffer sb = new StringBuffer("select product.productId,product.productName,sum(quantity) as dq "
        		+ "from BillDetail  where 1=1");
		
		if (billDetail != null) {
			
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
					sb.append(" and bill.storeId in("+ billDetail.getBill().getStoreId() + ")");
				}
                if(billDetail.getBill().getCreateBy()!=null){
                    sb.append(" and bill.createBy.userName like '%"+billDetail.getBill().getCreateBy().getUserName()+"%'");
                }
		}
		//分组、排序
		sb.append(" group by product.productId,product.productName");
		return this.findList(sb.toString()).size();
	}
	/** 
	  * @Title: getAll 
	  * @Description: 获取所有的系统信息
	  * @param @return
	  * @return List<Systems>
	  * @throws 
	  */
	public List<BillDetail> getAll(){
		StringBuffer sb = new StringBuffer("from BillDetail where 1=1");
		return this.getHandler().findListOfObj(sb.toString());
	}
	
}
