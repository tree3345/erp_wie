package com.wie.erp.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.InventoryDao;
import com.wie.erp.dao.InventoryDetailDao;
import com.wie.erp.dao.PurchaseDetailDao;
import com.wie.erp.dao.WarehouseDetailDao;
import com.wie.erp.model.Inventory;
import com.wie.erp.model.InventoryDetail;
import com.wie.erp.model.Purchase;
import com.wie.erp.model.PurchaseDetail;
import com.wie.erp.model.WarehouseDetail;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;

@Service
public class WarehouseDetailService extends BaseService<WarehouseDetail> implements IWarehouseDetailService {
	@Autowired
	private WarehouseDetailDao warehouseDetailDao;
	@Autowired
	private PurchaseDetailDao purchaseDetailDao;
	@Autowired
	private InventoryDetailDao inventoryDetailDao;
	@Autowired
	private InventoryDao inventoryDao;
	public Pagination getPageList(WarehouseDetail warehouseDetail, int page, int rows,String sort,String order) {
		return warehouseDetailDao.getPageList(warehouseDetail, page, rows,sort,order);
	}

	@Override
	protected DAOInterface<WarehouseDetail> getDAO() {
		return warehouseDetailDao;
	}

	
	

	public WarehouseDetailDao getWarehouseDetailDao() {
		return warehouseDetailDao;
	}

	public void setWarehouseDetailDao(WarehouseDetailDao warehouseDetailDao) {
		this.warehouseDetailDao = warehouseDetailDao;
	}


	public List<WarehouseDetail> getAllWarehouseDetail() {
		return warehouseDetailDao.getAllWarehouse();
	}

	public List getAll4MsSQL() {
		return  this.warehouseDetailDao.getAll();
	}

	public Map<String,WarehouseDetail> getStock4Now(Purchase purchase) {
		Map<String,WarehouseDetail> warehouseDetailMap=new HashMap<String, WarehouseDetail>();
		List<PurchaseDetail>  purchaseDetails=purchaseDetailDao.findList("from PurchaseDetail pd where pd.purchase.purchaseId='"+purchase.getPurchaseId()+"'");
		String inIds="";
		for(PurchaseDetail pd:purchaseDetails)
		{
			inIds+="'"+pd.getProduct().getProductId()+"',";
		}
		
		String hql="from WarehouseDetail wd where wd.warehouse.storeId='"+purchase.getStoreId()+"' ";
		if(!"".equals(inIds)){
			inIds=inIds.substring(0, inIds.length()-1);
			inIds=" and wd.product.productId in("+inIds+") ";
			hql+=inIds;
		}
		List<WarehouseDetail> warehouseDetails=warehouseDetailDao.findList(hql);
		for(WarehouseDetail wd:warehouseDetails)
		{
			warehouseDetailMap.put(wd.getProduct().getProductId(), wd);
		}
		return warehouseDetailMap;
	}

	public Map<String, WarehouseDetail> getStock4Now(Inventory inventory) {
		Map<String,WarehouseDetail> warehouseDetailMap=new HashMap<String, WarehouseDetail>();
		List<InventoryDetail>  inventoryDetails=inventoryDetailDao.findList("from InventoryDetail pd where pd.inventory.inventoryId='"+inventory.getInventoryId()+"'");
		String inIds="";
		for(InventoryDetail pd:inventoryDetails)
		{
			inIds+="'"+pd.getProduct().getProductId()+"',";
		}
		
		String hql="from WarehouseDetail wd where wd.warehouse.storeId='"+inventory.getStoreId()+"' ";
		if(!"".equals(inIds)){
			inIds=inIds.substring(0, inIds.length()-1);
			inIds=" and wd.product.productId in("+inIds+") ";
			hql+=inIds;
		}
		List<WarehouseDetail> warehouseDetails=warehouseDetailDao.findList(hql);
		for(WarehouseDetail wd:warehouseDetails)
		{
			warehouseDetailMap.put(wd.getProduct().getProductId(), wd);
		}
		return warehouseDetailMap;
	}

	public void saveList(List<WarehouseDetail> data, Inventory inventory,
			List<InventoryDetail> inventoryDetailList) {
		   for(WarehouseDetail wd:data)
		   {
			   warehouseDetailDao.alter(wd);
		   }
		   //货损
		   inventoryDao.save(inventory);
		   for(InventoryDetail vd:inventoryDetailList){
			   inventoryDetailDao.save(vd);
		   }
	}
	
 
}
