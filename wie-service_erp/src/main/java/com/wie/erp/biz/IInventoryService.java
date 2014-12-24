package com.wie.erp.biz;


import java.util.List;
import java.util.Map;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Inventory;
import com.wie.erp.model.InventoryDetail;
import com.wie.erp.model.Purchase;
import com.wie.erp.model.WarehouseDetail;
import com.wie.framework.service.ServiceInterface;

public interface IInventoryService extends ServiceInterface<Inventory>{


	public Map<String,Object> saveItem(Inventory inventory, Map<String, String> detailmap, boolean flag);

	public Pagination getPageList(Inventory inventory, Integer page, Integer rows,
								  String sort, String order);

	public String checkInventory(Inventory inventory,Map<String,WarehouseDetail> warehouseDetailMap);

	public void deleteInventoryById(String id,List<InventoryDetail> inventoryDetails);

}
