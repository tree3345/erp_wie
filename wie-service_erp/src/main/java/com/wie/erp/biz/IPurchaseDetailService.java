package com.wie.erp.biz;



import java.util.List;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.PurchaseDetail;
import com.wie.framework.service.ServiceInterface;

public interface IPurchaseDetailService extends ServiceInterface<PurchaseDetail>{

	public void test();


	public List getPageList(PurchaseDetail purchaseDetail, Integer page, Integer rows,
			String sort, String order);


	public int findCountBySql(PurchaseDetail purchaseDetail);


    public Pagination getPageDetailList(PurchaseDetail purchaseDetail, Integer page, Integer rows, String sort, String order);
}
