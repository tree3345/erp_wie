package com.wie.erp.biz;

import java.util.List;
import java.util.Map;

import com.wie.common.tools.page.Pager;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Product;
import com.wie.erp.model.ProductStore;
import com.wie.framework.service.ServiceInterface;

/** 
  * @ClassName: IDictionaryService 
  * @Description: 系统字典业务层接口
  *  
  */
public interface IProductStoreService extends ServiceInterface<ProductStore> {
	/** 
	  * @Title: findMax 
	  * @Description: 获取昵称后的值
	  * @param @param nickName 昵称  生成的代码
	  * @param @return
	  * @return String
	  * @throws 
	  */
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
	public Pagination getPageList(ProductStore productStore,int page,int rows,String sort,String order);
	
	
	/** 
	  * @Title: getAllDictionary 
	  * @Description: 获取所有的系统字典的记录
	  * @param @return
	  * @return List<Dictionarys>
	  * @throws 
	  */
	public List<ProductStore> getAllProduct();
	
	public List executebysql(String sql);
	
	public Pager<Object[]> findproduct();
	

	public List<ProductStore> getAll();
	public List<ProductStore> getAll4MsSQL();
	public List<ProductStore> getPur4MsSQL();
	public boolean saveAllMsSQL(Map<String,String> datamap);

	public boolean save4MsSQL(ProductStore product);
	public int saveMsSQL(ProductStore product);
	public boolean deleteByIdMsSQL(String id);
	public ProductStore findMsSQL(String id);
	public boolean alterMsSQL(ProductStore product);

	public boolean alterToClass(ProductStore product, String classId);


	public boolean impgoods(String productIds,String storeId);


	public boolean isExist(String storeId);
}
