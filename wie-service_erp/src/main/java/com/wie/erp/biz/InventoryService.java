package com.wie.erp.biz;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.InventoryDao;
import com.wie.erp.dao.InventoryDetailDao;
import com.wie.erp.dao.WarehouseDao;
import com.wie.erp.dao.WarehouseDetailDao;
import com.wie.erp.model.Inventory;
import com.wie.erp.model.InventoryDetail;
import com.wie.erp.model.OrderDetail;
import com.wie.erp.model.Product;
import com.wie.erp.model.PurchaseDetail;
import com.wie.erp.model.Warehouse;
import com.wie.erp.model.WarehouseDetail;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;

@Service
public class InventoryService  extends BaseService<Inventory> implements IInventoryService {

	@Autowired
	private InventoryDao inventoryDao;
	@Autowired
	private InventoryDetailDao inventoryDetailDao;
	@Autowired
	private WarehouseDao warehouseDao;
	@Autowired
	private WarehouseDetailDao warehouseDetailDao;

	@Override
	protected DAOInterface<Inventory> getDAO() {
		return inventoryDao;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public Map<String,Object> saveItem(Inventory inventory, Map<String, String> detailmap,boolean flag) {
		Map<String,Object> result = new HashMap<String,Object>();
		if(flag)
		{
			inventoryDao.save(inventory);
		}
		else{
			inventoryDao.alter(inventory);
		}
		
		String inserted=detailmap.get("inserted");
		String deleted=detailmap.get("deleted");
		String updated=detailmap.get("updated");
		
		if(inserted != null&&!"".equals(inserted)){
			JSONArray ja =JSONArray.fromObject(inserted);
			for (int i = 0; i < ja.size(); i++) {
				InventoryDetail od=new InventoryDetail();
				JSONObject jo = (JSONObject) ja.get(i);
				String productId=jo.get("productId").toString();
				Product product=new Product();
				product.setProductId(productId);
				od.setProduct(product);
				
				od.setInventory(inventory);
				
				String inventoryQuantity=jo.get("inventoryQuantity")+"";
				if(inventoryQuantity.equals("")||inventoryQuantity.equals("null"))
					inventoryQuantity="0";
				od.setInventoryQuantity(new BigDecimal(inventoryQuantity));
				
				String warehouseQuantity=jo.get("warehouseQuantity")+"";
				if(warehouseQuantity.equals("")||warehouseQuantity.equals("null"))
					warehouseQuantity="0";
				od.setWarehouseQuantity(new BigDecimal(warehouseQuantity));
				
				od.setDamageQuantity(od.getInventoryQuantity().subtract(od.getWarehouseQuantity()));
				String purchasePrice=jo.get("purchasePrice")+"";
				if(purchasePrice.equals("")||purchasePrice.equals("null"))
					purchasePrice="0";
                od.setPrice(new BigDecimal(purchasePrice));
				od.setDamageSum(od.getDamageQuantity().multiply(new BigDecimal(purchasePrice)));
				inventoryDetailDao.save(od);
				}
		}
		
		if(updated != null&&!"".equals(updated)){
			JSONArray ja =JSONArray.fromObject(updated);
			for (int i = 0; i < ja.size(); i++) {
				InventoryDetail od=new InventoryDetail();
				JSONObject jo = (JSONObject) ja.get(i);
				String id=jo.get("detailId").toString();
				od.setId(id);
				od.setInventory(inventory);
				String productId=jo.get("productId").toString();
				Product product=new Product();
				product.setProductId(productId);
				od.setProduct(product);
				
				String inventoryQuantity=jo.get("inventoryQuantity")+"";
				if(inventoryQuantity.equals("")||inventoryQuantity.equals("null"))
					inventoryQuantity="0";
				od.setInventoryQuantity(new BigDecimal(inventoryQuantity));
				
				String warehouseQuantity=jo.get("warehouseQuantity")+"";
				if(warehouseQuantity.equals("")||warehouseQuantity.equals("null"))
					warehouseQuantity="0";
				od.setWarehouseQuantity(new BigDecimal(warehouseQuantity));
				od.setDamageQuantity(od.getInventoryQuantity().subtract(od.getWarehouseQuantity()));
				String purchasePrice=jo.get("purchasePrice")+"";
				if(purchasePrice.equals("")||purchasePrice.equals("null"))
					purchasePrice="0";
                od.setPrice(new BigDecimal(purchasePrice));
				od.setDamageSum(od.getDamageQuantity().multiply(new BigDecimal(purchasePrice)));
				inventoryDetailDao.alter(od);
				}
		}
		
		if(deleted != null&&!"".equals(deleted)){
			JSONArray ja =JSONArray.fromObject(deleted);
			for (int i = 0; i < ja.size(); i++) {
				JSONObject jo = (JSONObject) ja.get(i);
				String id=jo.get("detailId").toString();
				inventoryDetailDao.deleteById(id);
				}
		}
		
		result.put("success", true);
		result.put("id", inventory.getInventoryId());
		return result;
	}

	public Pagination getPageList(Inventory inventory, Integer page, Integer rows,
			String sort, String Purchase) {
		return inventoryDao.getPageList(inventory, page, rows,sort,Purchase);
	}


	
	public  String checkInventory(Inventory inventory,Map<String,WarehouseDetail> warehouseDetailMap){
		//String warehouseId=warehouseDao.find(purchase.getStoreId()).getWarehouseId();
		List<Warehouse> warehouseList=warehouseDao.findList("from Warehouse w where w.storeId='"+inventory.getStoreId()+"'");
		if(warehouseList.size()>1){
			System.err.println("warehouse 与  store 数据不一致");
		     return null;
		}
		String warehouseId=warehouseList.get(0).getWarehouseId();
		if (inventory.getStatus() == 1){
			return "该单据已经审核过，不能再审核。";
		}
		List<InventoryDetail> inventoryDetails=inventoryDetailDao.findList("from InventoryDetail pd where pd.inventory.inventoryId='"+inventory.getInventoryId()+"'");
		for(InventoryDetail pd:inventoryDetails){
			WarehouseDetail wd=new WarehouseDetail();
			if(warehouseDetailMap.get(pd.getProduct())!=null)
			{
				wd=warehouseDetailMap.get(pd.getProduct().getProductId());
				wd.setQuantity(wd.getQuantity().add(pd.getDamageQuantity()));
				warehouseDetailDao.alter(wd);
			}/*else{
				Product product=new Product();
				product.setProductId(pd.getProductId());
				wd.setProduct(product);
				Warehouse warehouse = new Warehouse();
				warehouse.setWarehouseId(warehouseId);
				wd.setWarehouse(warehouse);
				wd.setQuantity(pd.getQuantity());
				warehouseDetailDao.save(wd);
			}*/
		}
		inventory.setStatus(1);
		inventoryDao.alter(inventory);
		return "ok";
	}

	public void deleteInventoryById(String id,
			List<InventoryDetail> inventoryDetails) {
		for(InventoryDetail nd:inventoryDetails)
		{
			inventoryDetailDao.delete(nd);
		}
		inventoryDao.deleteById(id);
		
	}
	
}
