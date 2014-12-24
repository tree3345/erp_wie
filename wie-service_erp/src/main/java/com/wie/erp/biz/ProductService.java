package com.wie.erp.biz;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wie.common.tools.page.Pager;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.ProductDao;
import com.wie.erp.dao.ProductPriceDao;
import com.wie.erp.dao.ProductPriceHistoryDao;
import com.wie.erp.model.Product;
import com.wie.erp.model.ProductClass;
import com.wie.erp.model.ProductPrice;
import com.wie.erp.model.ProductPriceHistory;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;


@Service("productService")
public class ProductService extends BaseService<Product> implements IProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductPriceDao productPriceDao;
	@Autowired
	private ProductPriceHistoryDao productPriceHistoryDao;
	public Pagination getPageList(Product product, int page, int rows,String sort,String order) {
		return productDao.getPageList(product, page, rows,sort,order);
	}

	@Override
	protected DAOInterface<Product> getDAO() {
		return productDao;
	}

	
	
	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public List<Product> getAllProduct() {
		return productDao.getAllProduct();
	}
	
	public List executebysql(String sql)
	{
		return productDao.executebySql(sql);
	}


	public Pager<Object[]> findproduct() {
		return null;
	}


	public List<Product> getAll() {
		return this.productDao.getAll();
	}
	public List<Product> getAll4MsSQL() {
	/*	String sql="select  t.productname,c.salesprice,t.unit,t.productno,t.spelling,t.classId,t.productid from JN_product t left join JN_productprice c on t.productid=c.productid ";
		return this.productDao.executebySql(sql);*/
		return this.productDao.getAll();
	}
	public List<Product> getPur4MsSQL() {
		String sql="select  t.productid,t.productname from JN_product t  ";
		return this.productDao.executebySql(sql);
	}


	public boolean alterToClass(Product product, String classId) {
		return this.productDao.alterToClass(product,classId);
	}

	public Pagination getPageList4MsSQL(Product product, int page, int rows,
			String sort, String order) {
		return productDao.getPageList(product, page, rows,sort,order);
	}

	@SuppressWarnings({ "unchecked", "deprecation"})
	public boolean saveAllMsSQL(Map<String,String> datamap) {
		String inserted=datamap.get("inserted");
		String updated=datamap.get("updated");
		String deleted=datamap.get("deleted");
		String storeId=datamap.get("storeId");
		boolean flag=false;
		//批量插入
		if(inserted != null){
			JSONArray ji =JSONArray.fromObject(inserted);
			for (int i = 0; i < ji.size(); i++) {
				JSONObject jo = (JSONObject) ji.get(i);
				Product pt=new Product();
				pt.setProductName(jo.get("productName").toString());
				ProductClass productClass=new ProductClass();
				productClass.setClassId(jo.get("classId").toString());
				pt.setProductClass(productClass);
				pt.setProductNo(jo.get("productNo").toString());
				pt.setUnit(jo.get("unit").toString());
				pt.setSpelling(jo.get("spelling").toString());
				pt.setStatus(1);
				if(storeId!=null&&!"".equals(storeId))
					pt.setStoreId(storeId);
				
				//价格
				ProductPrice ptc=new ProductPrice();
				ptc.setProductId(pt.getProductId());
				String salesPrice=jo.get("salesPrice").toString().equals("")?"0":jo.get("salesPrice").toString();
				ptc.setSalesPrice(new BigDecimal(salesPrice));
				
				//历史价格
				ProductPriceHistory pph=new ProductPriceHistory();
				pph.setProductId(pt.getProductId());
				pph.setUnitPrice(ptc.getSalesPrice());
				pph.setActiveDT(new java.sql.Timestamp(System.currentTimeMillis()));
				pph.setStatus(0);
				pph.setStoreId(storeId);
				flag=productDao.save(pt);
				flag=productPriceDao.save(ptc);
				flag=productPriceHistoryDao.save(pph);
				
			}
		}
		
		//批量更新
		if(updated != null){
			JSONArray ji =JSONArray.fromObject(updated);
			for (int i = 0; i < ji.size(); i++) {
				JSONObject jo = (JSONObject) ji.get(i);
				Product pt=new Product();
				pt.setProductId(jo.get("productId").toString());
				pt.setProductName(jo.get("productName").toString());
				ProductClass productClass=new ProductClass();
				productClass.setClassId(jo.get("classId").toString());
				pt.setProductClass(productClass);
				pt.setProductNo(jo.get("productNo").toString());
				pt.setUnit(jo.get("unit").toString());
				pt.setSpelling(jo.get("spelling").toString());
				pt.setStatus(1);
				if(storeId!=null&&!"".equals(storeId))
					pt.setStoreId(storeId);
				flag=productDao.alter(pt);
				//价格
				ProductPrice ptc=new ProductPrice();
				ptc.setProductId(pt.getProductId());
				String salesPrice=jo.get("salesPrice").toString().equals("")?"0":jo.get("salesPrice").toString();
				ptc.setSalesPrice(new BigDecimal(salesPrice));
				   if(jo.get("priceStatus").toString().equals("0"))
					flag=productPriceDao.save(ptc);
					if(jo.get("priceStatus").toString().equals("1"))
					{
					flag=productPriceDao.alter(ptc);
					}
				//历史价格
				ProductPriceHistory pph=new ProductPriceHistory();
				pph.setProductId(pt.getProductId());
				pph.setUnitPrice(ptc.getSalesPrice());
				pph.setActiveDT(new java.sql.Timestamp(System.currentTimeMillis()));
				pph.setStatus(0);
				pph.setStoreId(storeId);
				flag=productPriceHistoryDao.save(pph);
			}
			
			
		}
		
			//批量删除 商品价格级联删除
		   if(deleted != null){
			   JsonConfig jsonConfig = new JsonConfig();  //建立配置文件
				jsonConfig.setIgnoreDefaultExcludes(false);  //设置默认忽略
				
				jsonConfig.setExcludes(new String[]{"salesPrice", "costPrice","orderPrice","purchasePrice","priceStatus","status","editing"});  //将所需忽略字段加到数组中，如“productNo”，“productName”.
				JSONArray jsonArrd = JSONArray.fromObject(deleted, jsonConfig);
				List<Product> dellist = JSONArray.toList(jsonArrd, Product.class);
				for(int i=0;i<dellist.size();i++)
				{
					Product pd=dellist.get(i);
					pd.setStatus(0);
					flag=productDao.alter(pd);
				}
				
			}

		return flag;
	}

	public boolean save4MsSQL(Product product) {
		return productDao.save(product);
	}
	public int saveMsSQL(Product product) {
		
		String sql="insert JN_Product"
				+ "(classId, descr, goodNumber, Icon, image, imageName, mouseImage, productNO, productName, sort, spelling, unit, weightlower, productId) "
				+ "values('"+product.getProductClass().getClassId()+"','"+product.getDescr()+"','"+product.getGoodNumber()+"','"+product.getIcon()+"','"+product.getImage()+"','"
				+product.getImageName()+"','"+product.getMouseImage()+"','"+product.getProductNo()+"','"+product.getProductName()+"',"+product.getSort()+",'"+product.getSpelling()+"','"
				+product.getUnit()+"',"+product.getWeightlower()+",'"+UUID.randomUUID().toString()+"')";
		System.err.println(sql);
		return productDao.executeupdateBysql(sql);
	}

	public boolean deleteByIdMsSQL(String id) {
		return productDao.deleteById(id);
	}

	public Product findMsSQL(String id) {
		return productDao.findById(id);
	}

	public boolean alterMsSQL(Product product) {
		return productDao.alter(product);
	}

	public String isExitstData(String productId) {
		 String baseStr="{\"success\":\"true\"}";
		 List list=productDao.ExistCountList(productId);
		 for(int i=0;i<list.size();i++)
	     {
	    	 Integer count=Integer.parseInt(list.get(i).toString());
	    	 if(count>0){
	    		 baseStr ="{\"error\":\"true\"}";
	    		 break;
	    	 }
	     }
		return baseStr;
	}

	@Override
	public boolean saveProductandPrice(Product product) {
		boolean flag=false;
		//System.out.println(product.getProductId());
		ProductPrice price=new ProductPrice();
		price.setProductId(product.getProductId());
		price.setSalesPrice(product.getProductPrice().getSalesPrice());
		product.setProductPrice(null);
		flag=productDao.save(product)&&productPriceDao.save(price);
		return flag;
	}

	@Override
	public boolean alteProductandPrice(Product product) {
		boolean flag=false;
		//System.out.println(product.getProductId());
		ProductPrice price=new ProductPrice();
		price.setProductId(product.getProductId());
		price.setSalesPrice(product.getProductPrice().getSalesPrice());
		product.setProductPrice(null);
		productDao.alter(product);
		if(productPriceDao.findById(product.getProductId())==null)
			flag=productPriceDao.save(price);
		else
			flag=productPriceDao.alter(price);
		return flag;
	}

}
