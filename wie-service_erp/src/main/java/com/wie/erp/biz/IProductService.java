package com.wie.erp.biz;

import java.util.List;
import java.util.Map;

import com.wie.common.tools.page.Pager;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Product;
import com.wie.framework.service.ServiceInterface;

/** 
  * @ClassName: IDictionaryService 
  * @Description: 系统字典业务层接口
  *  
  */
public interface IProductService extends ServiceInterface<Product> {
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
	public Pagination getPageList(Product product,int page,int rows,String sort,String order);
	
	
	/** 
	  * @Title: getAllDictionary 
	  * @Description: 获取所有的系统字典的记录
	  * @param @return
	  * @return List<Dictionarys>
	  * @throws 
	  */
	public List<Product> getAllProduct();
	
	public List executebysql(String sql);
	
	public Pager<Object[]> findproduct();
	

	public List<Product> getAll();
	public List<Product> getAll4MsSQL();
	public List<Product> getPur4MsSQL();
	public boolean saveAllMsSQL(Map<String,String> datamap);

	public boolean save4MsSQL(Product product);
	public int saveMsSQL(Product product);
	public boolean deleteByIdMsSQL(String id);
	public Product findMsSQL(String id);
	public boolean alterMsSQL(Product product);

	public boolean alterToClass(Product product, String classId);


	public String isExitstData(String productId);

	boolean saveProductandPrice(Product product);

	boolean alteProductandPrice(Product product);
}
