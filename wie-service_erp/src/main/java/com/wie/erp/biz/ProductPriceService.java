package com.wie.erp.biz;

import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wie.common.tools.page.Pager;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.ProductDao;
import com.wie.erp.dao.ProductPriceDao;
import com.wie.erp.model.Product;
import com.wie.erp.model.ProductPrice;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;


@Service
public class ProductPriceService extends BaseService<ProductPrice> implements IProductPriceService {
	@Autowired
	private ProductPriceDao productPriceDao;
	public Pagination getPageList(ProductPrice productPrice, int page, int rows,String sort,String order) {
		return productPriceDao.getPageList(productPrice, page, rows,sort,order);
	}

	@Override
	protected DAOInterface<ProductPrice> getDAO() {
		return productPriceDao;
	}

	public List<ProductPrice> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getPrices() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getProductJSON(String VegeName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getSaleRecord(String saleperson, String mumberNo,
			String startTime, String endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getDetailSaleRecord(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getProductClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getPclass() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getProductClassByCategoryName(String categoryName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getProductNameByClassName(String className) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getProductInfo(String productName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getSalesRecord(String[] vegesName, String beginTime,
			String endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getStatistics(String vegeName, String beginTime, String endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getWarehouse(String vegeName, String storeId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getWarehouseInfo(String warehouseID) {
		// TODO Auto-generated method stub
		return null;
	}

	public List loginServer(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	public String operateDB(String sqlStr) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean saveAll(String updated, List listProductIds) {
		// TODO Auto-generated method stub
		return false;
	}

	public List getProductIds() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
