package com.wie.erp.biz;

import java.util.List;
import java.util.Map;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Inventory;
import com.wie.erp.model.InventoryDetail;
import com.wie.erp.model.Purchase;
import com.wie.erp.model.WarehouseDetail;
import com.wie.framework.service.ServiceInterface;

/** 
  * @ClassName: IDictionaryService 
  * @Description: 系统字典业务层接口
  *  
  */
public interface IWarehouseDetailService extends ServiceInterface<WarehouseDetail> {
	/** 
	  * @Title: getPageList 
	  * @Description: 获取具体某页的记录
	  * @param @param dic 一条具体Dictionarys对象信息
	  * @param @param page 当前页
	  * @param @param rows 一页显示的内容
	  * @param @return
	  * @return Pagination
	  * @throws 
	  */
	public Pagination getPageList(WarehouseDetail warehouseDetail,int page,int rows,String sort,String order);
	
	/** 
	  * @Title: getAllDictionary 
	  * @Description: 获取所有的系统字典的记录
	  * @param @return
	  * @return List<Dictionarys>
	  * @throws 
	  */
	public List<WarehouseDetail> getAllWarehouseDetail();

	public List getAll4MsSQL();

	public Map<String,WarehouseDetail> getStock4Now(Purchase purchase);

	public Map<String, WarehouseDetail> getStock4Now(Inventory inventory);


	public void saveList(List<WarehouseDetail> data, Inventory inventory,
			List<InventoryDetail> inventoryDetailList);
}
