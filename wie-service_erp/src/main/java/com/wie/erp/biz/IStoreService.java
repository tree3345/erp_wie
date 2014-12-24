package com.wie.erp.biz;

import java.util.List;
import java.util.Map;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Store;
import com.wie.framework.service.ServiceInterface;
import com.wie.permissions.model.Dictionarys;

/** 
  * @ClassName: IDictionaryService 
  * @Description: 系统字典业务层接口
  *  
  */
public interface IStoreService extends ServiceInterface<Store> {
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
	public Pagination getPageList(Store store,int page,int rows,String sort,String order);
	
	/** 
	  * @Title: getAllDictionary 
	  * @Description: 获取所有的系统字典的记录
	  * @param @return
	  * @return List<Dictionarys>
	  * @throws 
	  */
	public List<Store> getAllStore();

	public boolean saveAll(Map<String, String> datamap);

	String storeIdsByUserId(String userid);

	List getStores(String storeIds);

	String getUserStores(String userId, String storeId);
}
