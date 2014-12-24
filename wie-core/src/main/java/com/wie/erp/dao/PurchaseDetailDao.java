package com.wie.erp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.InventoryDetail;
import com.wie.erp.model.OrderDetail;
import com.wie.erp.model.PurchaseDetail;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: CategoryDao 
  * @Description: 大类Dao层
  *  
  */
@Repository
public class PurchaseDetailDao extends BaseDAO<PurchaseDetail> {
	
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
	public List getPageList(PurchaseDetail purchaseDetail,int page, int rows,String sort,String order) {
		StringBuffer sb = new StringBuffer("select product.productId,product.productName,product.unit,sum(quantity) as dq,sum(quantity*price) as account from PurchaseDetail  where 1=1 and purchase.status=1 ");
		
		if (purchaseDetail != null) {
			
			if (purchaseDetail.getProduct() != null)
				if (purchaseDetail.getProduct().getProductName() != null
						&& !"".equals(purchaseDetail.getProduct().getProductName())) {
					sb.append(" and product.productName like '%"+ purchaseDetail.getProduct().getProductName()+ "%'");
				}
            
			if (purchaseDetail.getPurchase() != null) {
                if(purchaseDetail.getPurchase().getInby()!=null){
                    sb.append(" and purchase.inby.name like '%"+purchaseDetail.getPurchase().getInby().getName()+"%'");
                }
                if ((purchaseDetail.getPurchase().getStartTime() != null && !""
                        .equals(purchaseDetail.getPurchase().getStartTime()))) {
                    sb.append(" and purchase.checkDate>'" + purchaseDetail.getPurchase().getStartTime() + "'");
                }
                if ((purchaseDetail.getPurchase().getEndTime() != null && !""
                        .equals(purchaseDetail.getPurchase().getEndTime()))) {
                    sb.append(" and purchase.checkDate<'" + purchaseDetail.getPurchase().getEndTime() + "'");
                }

                if (purchaseDetail.getPurchase().getStoreId() != null
                        && !"".equals(purchaseDetail.getPurchase().getStoreId())) {
                    sb.append(" and purchase.storeId in(" + purchaseDetail.getPurchase().getStoreId() + ")");
                }
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
	
	public int findCountBySql(PurchaseDetail purchaseDetail) {
        StringBuffer sb = new StringBuffer("select product.productId,product.productName,sum(quantity) as dq "
        		+ "from PurchaseDetail  where 1=1 and purchase.status=1");
		
		if (purchaseDetail != null) {
			
			if (purchaseDetail.getProduct() != null)
				if (purchaseDetail.getProduct().getProductName() != null
						&& !"".equals(purchaseDetail.getProduct().getProductName())) {
					sb.append(" and product.productName like '%"+ purchaseDetail.getProduct().getProductName()+ "%'");
				}
            
			if (purchaseDetail.getPurchase() != null) {
                if(purchaseDetail.getPurchase().getInby()!=null){
                    sb.append(" and purchase.inby.name like '%"+purchaseDetail.getPurchase().getInby().getName()+"%'");
                }
                if ((purchaseDetail.getPurchase().getStartTime() != null && !""
                        .equals(purchaseDetail.getPurchase().getStartTime()))) {
                    sb.append(" and purchase.checkDate>'" + purchaseDetail.getPurchase().getStartTime() + "'");
                }
                if ((purchaseDetail.getPurchase().getEndTime() != null && !""
                        .equals(purchaseDetail.getPurchase().getEndTime()))) {
                    sb.append(" and purchase.checkDate<'" + purchaseDetail.getPurchase().getEndTime() + "'");
                }

                if (purchaseDetail.getPurchase().getStoreId() != null
                        && !"".equals(purchaseDetail.getPurchase().getStoreId())) {
                    sb.append(" and purchase.storeId in(" + purchaseDetail.getPurchase().getStoreId() + ")");
                }
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
	public List<PurchaseDetail> getAll(){
		StringBuffer sb = new StringBuffer("from PurchaseDetail where 1=1");
		return this.getHandler().findListOfObj(sb.toString());
	}

    public Pagination getPageDetailList(PurchaseDetail purchaseDetail, Integer page, Integer rows, String sort, String order) {
        StringBuffer sb = new StringBuffer("from PurchaseDetail d where d.purchase.status=1 ");
        if(null != purchaseDetail){
            if(null != purchaseDetail.getProduct()){
                sb.append(" and d.product.productId ='" + purchaseDetail.getProduct().getProductId() + "' ");
            }

            if (purchaseDetail.getPurchase() != null) {
                if(purchaseDetail.getPurchase().getInby()!=null){
                    sb.append(" and purchase.inby.name like '%"+purchaseDetail.getPurchase().getInby().getName()+"%'");
                }
                if ((purchaseDetail.getPurchase().getStartTime() != null && !""
                        .equals(purchaseDetail.getPurchase().getStartTime()))) {
                    sb.append(" and purchase.checkDate>'" + purchaseDetail.getPurchase().getStartTime() + "'");
                }
                if ((purchaseDetail.getPurchase().getEndTime() != null && !""
                        .equals(purchaseDetail.getPurchase().getEndTime()))) {
                    sb.append(" and purchase.checkDate<'" + purchaseDetail.getPurchase().getEndTime() + "'");
                }

                if (purchaseDetail.getPurchase().getStoreId() != null
                        && !"".equals(purchaseDetail.getPurchase().getStoreId())) {
                    sb.append(" and purchase.storeId in(" + purchaseDetail.getPurchase().getStoreId() + ")");
                }
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
}
