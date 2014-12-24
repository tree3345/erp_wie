package com.wie.erp.biz;


import java.util.List;
import java.util.Map;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Purchase;
import com.wie.erp.model.PurchaseDetail;
import com.wie.erp.model.WarehouseDetail;
import com.wie.framework.service.ServiceInterface;

public interface IPurchaseService extends ServiceInterface<Purchase>{

	public void test();

	public Map<String,Object> saveItem(Purchase purchase, Map<String, String> detailmap, boolean flag);

	public Pagination getPageList(Purchase purchase, Integer page, Integer rows,
			String sort, String order);

	public String checkPurchase(Purchase purchase,Map<String,WarehouseDetail> warehouseDetailMap);

	public boolean saveFromOrder(String orderId, String userId);

	public void deletePurchaseById(String id,List<PurchaseDetail> purchaseDetails);

}
