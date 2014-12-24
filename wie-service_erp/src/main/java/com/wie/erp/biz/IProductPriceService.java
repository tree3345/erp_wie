package com.wie.erp.biz;

import java.util.List;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Product;
import com.wie.erp.model.ProductPrice;
import com.wie.framework.service.ServiceInterface;

/** 
  * @ClassName: IDictionaryService 
  * @Description: 系统字典业务层接口
  *  
  */
public interface IProductPriceService extends ServiceInterface<ProductPrice> {
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
	public Pagination getPageList(ProductPrice product,int page,int rows,String sort,String order);
	
	/** 
	  * @Title: getAllDictionary 
	  * @Description: 获取所有的系统字典的记录
	  * @param @return
	  * @return List<Dictionarys>
	  * @throws 
	  */
	/**
	 * 获得菜品
	 * @return
	 */
	public List<ProductPrice> getAll();
	
	public List getPrices();
	/**
	 * 按菜品名获得菜品
	 * @param VegeName
	 * @return
	 */
	public List getProductJSON(String VegeName);
	
	/**
	 * 获得销售记录
	 * @param condition
	 * @return
	 */
	public List getSaleRecord(String saleperson,String mumberNo,String startTime,String endTime) ;
	
	/**
	 * 获得销售记录详情
	 * @param id
	 * @return
	 */
	public List getDetailSaleRecord(String id);
	
	/**
	 * 获得所有小类
	 * @return
	 */
	public List getCategory();
	
	/**
	 * 获得所有中类
	 * @return
	 */
	public List getProductClass();
	
	public List getPclass();

	/**
	 * 根据小类获得中类
	 * @param categoryName
	 * @return
	 */
	public List getProductClassByCategoryName(String categoryName);
	
	/**
	 * 通过中类名称获得菜品名称
	 * @param className
	 * @return
	 */
	public List getProductNameByClassName(String className);
	
	/**
	 * 获得菜品详情
	 * @param productName
	 * @return
	 */
	public List getProductInfo(String productName);
	
	/**
	 * 获得销售记录
	 * @param vegesName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List getSalesRecord(String[] vegesName, String beginTime, String endTime);
	
	
	public List getStatistics(String vegeName, String beginTime, String endTime);
	
	/**
	 * 按菜品名和商店id获取菜品库存
	 * @param vegeName
	 * @param storeId
	 * @return
	 */
	public List getWarehouse(String vegeName, String storeId);
	
	/**
	 * 根据仓库id获得库存详情
	 * @param warehouseID
	 * @return
	 */
	public List getWarehouseInfo(String warehouseID);

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
	public List loginServer(String username, String password) ;
	
	/**
	 * @author Administrator
	 * 客户端方法
	 */
	public String operateDB(String sqlStr);

	public boolean saveAll(String updated,List listProductIds);
	
	public List getProductIds();
}
