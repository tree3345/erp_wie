package com.wie.erp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.ProductPriceHistory;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: ActionsDAO 
  * @Description: 操作管理DAO 
  *  
  */
@Repository(value="productPriceHistoryDao")
public class ProductPriceHistoryDao extends BaseDAO<ProductPriceHistory> {


	
	/** 
	  * @Title: getAll 
	  * @Description: 获得所有记录 
	  * @param @return
	  * @return List<Actions>
	  * @throws 
	  */
	public List<ProductPriceHistory> getAll(){
		return this.findList("from ProductPrice");
	}

	public Pagination getPageList(ProductPriceHistory productPriceHistory, int page, int rows,
			String sort, String order) {
		StringBuffer sb = new StringBuffer("from ProductPriceHistory where 1=1 ");
		if(productPriceHistory!=null){
			if(productPriceHistory.getProductId()!=null&&!"".equals(productPriceHistory.getProductId()))
			{
				sb.append(" and productId ='"+productPriceHistory.getProductId()+"'");
			}
		    
			if(productPriceHistory.getStatus()!=null)
			{
				sb.append(" and status="+productPriceHistory.getStatus());
			}
			if(productPriceHistory.getStoreId()!=null)
			{
				sb.append(" and (storeId in("+productPriceHistory.getStoreId()+") or storeId is null)");
			}
			if(productPriceHistory.getStartTime()!=null&&!"".equals(productPriceHistory.getStartTime()))
			{
				sb.append(" and activeDT >'"+productPriceHistory.getStartTime()+"'");
			}
			if(productPriceHistory.getEndTime()!=null&&!"".equals(productPriceHistory.getEndTime()))
			{
				sb.append(" and activeDT <'"+productPriceHistory.getEndTime()+"'");
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
		return this.getHandler().getPageList(Finder.create(sb.toString()), page, rows);
	}

}
