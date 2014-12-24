package com.wie.erp.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.common.tools.page.Pager;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.ProductDao;
import com.wie.erp.dao.ProductPriceDao;
import com.wie.erp.dao.ProductStoreDao;
import com.wie.erp.model.Product;
import com.wie.erp.model.ProductStore;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;


@Service
public class ProductStoreService extends BaseService<ProductStore> implements IProductStoreService {
	@Autowired
	private ProductStoreDao productStoreDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductPriceDao productPriceDao;
	public Pagination getPageList(ProductStore product, int page, int rows,String sort,String order) {
		return productStoreDao.getPageList(product, page, rows,sort,order);
	}

	@Override
	protected DAOInterface<ProductStore> getDAO() {
		return productStoreDao;
	}

	
	
	public ProductStoreDao getProductStoreDao() {
		return productStoreDao;
	}

	public void setProductStoreDao(ProductStoreDao productStoreDao) {
		this.productStoreDao = productStoreDao;
	}

	public List<ProductStore> getAllProduct() {
		return productStoreDao.getAllProduct();
	}
	
	public List executebysql(String sql)
	{
		return productStoreDao.executebySql(sql);
	}


	public Pager<Object[]> findproduct() {
		return null;
	}


	public List<ProductStore> getAll() {
		return this.productStoreDao.getAll();
	}
	public List<ProductStore> getAll4MsSQL() {
	/*	String sql="select  t.productname,c.salesprice,t.unit,t.productno,t.spelling,t.classId,t.productid from JN_product t left join JN_productprice c on t.productid=c.productid ";
		return this.productStoreDao.executebySql(sql);*/
		return this.productStoreDao.getAll();
	}
	public List<ProductStore> getPur4MsSQL() {
		String sql="select  t.productid,t.productname from JN_product t  ";
		return this.productStoreDao.executebySql(sql);
	}


	public boolean alterToClass(ProductStore product, String classId) {
		return this.productStoreDao.alterToClass(product,classId);
	}

	public Pagination getPageList4MsSQL(ProductStore product, int page, int rows,
			String sort, String order) {
		return productStoreDao.getPageList(product, page, rows,sort,order);
	}

	@SuppressWarnings({ "unchecked", "deprecation"})
	public boolean saveAllMsSQL(Map<String,String> datamap) {
		String inserted=datamap.get("inserted");
		String updated=datamap.get("updated");
		String deleted=datamap.get("deleted");
		boolean flag=false;
		JsonConfig jsonConfig = new JsonConfig();  //建立配置文件
		jsonConfig.setIgnoreDefaultExcludes(false);  //设置默认忽略
		jsonConfig.setExcludes(new String[]
		{"salesPrice","status","editing"});  //将所需忽略字段加到数组中，如“productNo”，“productName”.
		
		//批量插入
		if(inserted != null){
			JSONArray jsonArr = JSONArray.fromObject(inserted, jsonConfig);
			List<ProductStore> inslist = JSONArray.toList(jsonArr, ProductStore.class);
			for(int i=0;i<inslist.size();i++)
			{
				ProductStore pd=inslist.get(i);
				pd.setStatus(1);
				flag=productStoreDao.save(pd);
			}
		}
		
		//批量更新
		if(updated != null){
			JSONArray jsonArr = JSONArray.fromObject(updated, jsonConfig);
			List<ProductStore> updatelist = JSONArray.toList(jsonArr, ProductStore.class);
			for(int i=0;i<updatelist.size();i++)
			{
				ProductStore pd=updatelist.get(i);
				pd.setStatus(1);
				flag=productStoreDao.alter(pd);
			}
		}
		
			//批量删除 商品价格级联删除
		   if(deleted != null){
				JSONArray jsonArrd = JSONArray.fromObject(deleted, jsonConfig);
				List<ProductStore> dellist = JSONArray.toList(jsonArrd, ProductStore.class);
				for(int i=0;i<dellist.size();i++)
				{
					ProductStore pd=dellist.get(i);
					pd.setStatus(0);
					flag=productStoreDao.alter(pd);
				}
				/*
				 * JSONArray ja =JSONArray.fromObject(deleted);
				for (int i = 0; i < ja.size(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					String id=jo.get("productId").toString();
					flag=productStoreDao.deleteById(id);
				}*/
			}

		return flag;
	}

	public boolean save4MsSQL(ProductStore product) {
		return productStoreDao.save(product);
	}
	public int saveMsSQL(ProductStore product) {
		
		String sql="insert JN_ProductStore"
				+ "(classId, descr, goodNumber, Icon, image, imageName, mouseImage, productNO, productName, sort, spelling, unit, weightlower, productId) "
				+ "values('"+product.getClassId()+"','"+product.getDescr()+"','"+product.getGoodNumber()+"','"+product.getIcon()+"','"+product.getImage()+"','"
				+product.getImageName()+"','"+product.getMouseImage()+"','"+product.getProductNo()+"','"+product.getProductName()+"',"+product.getSort()+",'"+product.getSpelling()+"','"
				+product.getUnit()+"',"+product.getWeightlower()+",'"+UUID.randomUUID().toString()+"')";
		System.err.println(sql);
		return productStoreDao.executeupdateBysql(sql);
	}

	public boolean deleteByIdMsSQL(String id) {
		return productStoreDao.deleteById(id);
	}

	public ProductStore findMsSQL(String id) {
		return productStoreDao.findById(id);
	}

	public boolean alterMsSQL(ProductStore product) {
		return productStoreDao.alter(product);
	}

	public boolean impgoods(String productIds,String storeId) {
		List<Product> pList=new ArrayList<Product>();
		if(productIds.equals("all")){
			pList=productDao.getAll();
		}
		else{
		 pList=productDao.findByIds(productIds);
		}
		for(Product pt:pList){
			ProductStore pst=new ProductStore();
			pst.setStoreId(storeId);
			pst.setProductId(pt.getProductId());
			pst.setClassId(pt.getProductClass().getClassId());
			pst.setGoodNumber(pt.getGoodNumber());
			pst.setIcon(pt.getIcon());
			pst.setImage(pt.getImage());
			pst.setImageName(pt.getImageName());
			pst.setMouseImage(pt.getMouseImage());
			pst.setProductName(pt.getProductName());
			pst.setProductNo(pt.getProductNo());
//			pst.setProductPrice(pt.getProductPrice()==null?new BigDecimal(0):pt.getProductPrice().getSalesPrice());
			pst.setSalesPrice(pt.getProductPrice()==null?new BigDecimal(0):pt.getProductPrice().getSalesPrice());
			pst.setSpelling(pt.getSpelling());
			pst.setSort(pt.getSort());
			pst.setStatus(pt.getStatus());
			pst.setWeightlower(pt.getWeightlower());
			pst.setUnit(pt.getUnit());
			productStoreDao.save(pst);
		}
		return true;
	}

	public boolean isExist(String storeId) {
		 boolean  flag=false;
		 int counts=productStoreDao.findCountBySql("from ProductStore p where p.storeId='"+storeId+"'");
		 if(counts>0)
		  flag=true;
		 return flag;
	}
	
}
