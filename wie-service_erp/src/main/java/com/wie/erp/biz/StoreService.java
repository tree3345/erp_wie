package com.wie.erp.biz;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.wie.erp.dao.WarehouseDao;
import com.wie.erp.model.Warehouse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.StoreDao;
import com.wie.erp.model.Product;
import com.wie.erp.model.ProductPrice;
import com.wie.erp.model.Store;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.permissions.dao.DictionaryDao;
import com.wie.permissions.model.Dictionarys;

@Service
@Transactional
public class StoreService extends BaseService<Store> implements IStoreService {
	@Autowired
	private StoreDao storeDao;
	private WarehouseDao warehouseDao;
	public Pagination getPageList(Store store, int page, int rows,String sort,String order) {
		return storeDao.getPageList(store, page, rows,sort,order);
	}

	@Override
	protected DAOInterface<Store> getDAO() {
		return storeDao;
	}

	
	
	public StoreDao getStoreDao() {
		return storeDao;
	}

	public void setStoreDao(StoreDao storeDao) {
		this.storeDao = storeDao;
	}

	public List<Store> getAllStore() {
		return storeDao.getAllStore();
	}

	public boolean saveAll(Map<String, String> datamap) {
		String inserted=datamap.get("inserted");
		String updated=datamap.get("updated");
		String deleted=datamap.get("deleted");
		boolean flag=false;
		JsonConfig jsonConfig = new JsonConfig();  //建立配置文件
		jsonConfig.setIgnoreDefaultExcludes(false);  //设置默认忽略
		jsonConfig.setExcludes(new String[]{"status","editing"});  
		//批量保存
		if(inserted != null){
			JSONArray jsonArr = JSONArray.fromObject(inserted, jsonConfig);
			List<Store> insertedlist = JSONArray.toList(jsonArr, Store.class);
			for(int i=0;i<insertedlist.size();i++)
			{
				Store pd=insertedlist.get(i);
				Warehouse warehouse=new Warehouse();
				warehouse.setStoreId(pd.getStoreId());
				flag=storeDao.save(pd)&&warehouseDao.save(warehouse);
			}
		}
		//批量更新
		if(updated != null){
			JSONArray jsonArru = JSONArray.fromObject(updated, jsonConfig);
			List<Store> updatelist = JSONArray.toList(jsonArru, Store.class);
			for(int i=0;i<updatelist.size();i++)
			{
				Store pd=updatelist.get(i);
				flag=storeDao.alter(pd);
			}
		}
        
		if(deleted != null){
			JSONArray jsonArrd = JSONArray.fromObject(deleted, jsonConfig);
			List<Store> deletedlist = JSONArray.toList(jsonArrd, Store.class);
			for(int i=0;i<deletedlist.size();i++)
			{
				Store pd=deletedlist.get(i);
				pd.setStatus(0);
				flag=storeDao.alter(pd);
			}
		}
		return flag;
	}

	@Override
	public String storeIdsByUserId(String userid) {
		return storeDao.storeIdsByUserId(userid);
	}

	@Override
	public List getStores(String storeIds) {
		return storeDao.getStores(storeIds);
	}

	@Override
	public String getUserStores(String userId, String storeId) {
		return storeDao.getUserStores(userId,storeId);
	}

}
