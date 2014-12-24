package com.wie.erp.biz;

import java.util.List;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.ProductPrice;
import com.wie.erp.model.ProductPriceHistory;
import com.wie.framework.service.ServiceInterface;

/** 
  * @ClassName: IProductPriceHistoryService 
  * @Description: 
  *  
  */
public interface IProductPriceHistoryService extends ServiceInterface<ProductPriceHistory> {
	/** 
	  * @Title: getPageList 
	  * @Description: 获取具体某页的记录
	  * @param @param page 当前页
	  * @param @param rows 一页显示的内容
	  * @param @return
	  * @return Pagination
	  * @throws 
	  */
	public Pagination getPageList(ProductPriceHistory product,int page,int rows,String sort,String order);
	
}
