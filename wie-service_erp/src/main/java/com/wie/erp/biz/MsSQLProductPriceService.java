package com.wie.erp.biz;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wie.erp.dao.ProductDao;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.ProductPriceDao;
import com.wie.erp.model.ProductPrice;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;


@Service
public class MsSQLProductPriceService extends BaseService<ProductPrice> implements IProductPriceService {
	@Autowired
	private ProductPriceDao productPriceDao;
	@Autowired
	private ProductDao productDao;
	public Pagination getPageList(ProductPrice productPrice, int page, int rows,String sort,String order) {
		return productPriceDao.getPageList(productPrice, page, rows,sort,order);
	}

	@Override
	protected DAOInterface<ProductPrice> getDAO() {
		return productPriceDao;
	}

	
	

	public ProductPriceDao getProductPriceDao() {
		return productPriceDao;
	}

	public void setProductPriceDao(ProductPriceDao productPriceDao) {
		this.productPriceDao = productPriceDao;
	}

	public List<ProductPrice> getAll() {
		return productPriceDao.getAll();
	}
	//获得商品
	public List getProductJSON(String vegeName)
	{
//		String sql="select dbo.JN_Product.ProductID,dbo.JN_Product.ProductName,dbo.JN_ProductPrice.OrderPrice,dbo.JN_ProductPrice.PurchasePrice,dbo.JN_ProductPrice.CostPrice,dbo.JN_ProductPrice.SalesPrice,dbo.JN_Product.Unit,dbo.JN_Product.ProductNO,dbo.JN_Product.Spelling,dbo.JN_Class.ClassName from (dbo.JN_Product inner join dbo.JN_ProductPrice on dbo.JN_Product.ProductID = dbo.JN_ProductPrice.ProductID) inner join dbo.JN_Class on dbo.JN_Product.ClassID = dbo.JN_Class.ClassID order by dbo.JN_Class.ClassName";
		String sqlString = "";

		String chinese = "";

		if (vegeName == null || vegeName.equals("")) {
			sqlString = "select dbo.JN_Product.ProductID,dbo.JN_Product.ProductName,dbo.JN_ProductPrice.OrderPrice,dbo.JN_ProductPrice.PurchasePrice,dbo.JN_ProductPrice.CostPrice,dbo.JN_ProductPrice.SalesPrice,dbo.JN_Product.Unit,dbo.JN_Product.ProductNO,dbo.JN_Product.Spelling,dbo.JN_Class.ClassName from (dbo.JN_Product inner join dbo.JN_ProductPrice on dbo.JN_Product.ProductID = dbo.JN_ProductPrice.ProductID) inner join dbo.JN_Class on dbo.JN_Product.ClassID = dbo.JN_Class.ClassID order by dbo.JN_Class.ClassName ";
		} else {

			Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");

			Matcher matcher = pattern.matcher(vegeName);

			if (matcher.find()) {

				for (int i = 0; i < vegeName.length(); i++) {

					matcher = pattern.matcher(vegeName.substring(i, i + 1));

					if (matcher.matches()) {
						chinese += "%" + vegeName.substring(i, i + 1);
					}
				}

				chinese += "%";

				sqlString = "select dbo.JN_Product.ProductID,dbo.JN_Product.ProductName,dbo.JN_ProductPrice.OrderPrice,dbo.JN_ProductPrice.PurchasePrice,dbo.JN_ProductPrice.CostPrice,dbo.JN_ProductPrice.SalesPrice,dbo.JN_Product.Unit,dbo.JN_Product.ProductNO,dbo.JN_Product.Spelling,dbo.JN_Class.ClassName from (dbo.JN_Product inner join dbo.JN_ProductPrice on dbo.JN_Product.ProductID = dbo.JN_ProductPrice.ProductID) inner join dbo.JN_Class on dbo.JN_Product.ClassID = dbo.JN_Class.ClassID where dbo.JN_Product.ProductName like '"
						+ chinese + "' order by dbo.JN_Class.ClassName";
			} else {

				Pattern patternName = Pattern.compile("[0-9]+");

				if (patternName.matcher(vegeName).matches()) {

					sqlString = "select dbo.JN_Product.ProductID,dbo.JN_Product.ProductName,dbo.JN_ProductPrice.OrderPrice,dbo.JN_ProductPrice.PurchasePrice,dbo.JN_ProductPrice.CostPrice,dbo.JN_ProductPrice.SalesPrice,dbo.JN_Product.Unit,dbo.JN_Product.ProductNO,dbo.JN_Product.Spelling,dbo.JN_Class.ClassName from (dbo.JN_Product inner join dbo.JN_ProductPrice on dbo.JN_Product.ProductID = dbo.JN_ProductPrice.ProductID) inner join dbo.JN_Class on dbo.JN_Product.ClassID = dbo.JN_Class.ClassID where dbo.JN_Product.ProductName like '%"
							+ vegeName + "%' order by dbo.JN_Class.ClassName";

				} else {

					sqlString = "select dbo.JN_Product.ProductID,dbo.JN_Product.ProductName,dbo.JN_ProductPrice.OrderPrice,dbo.JN_ProductPrice.PurchasePrice,dbo.JN_ProductPrice.CostPrice,dbo.JN_ProductPrice.SalesPrice,dbo.JN_Product.Unit,dbo.JN_Product.ProductNO,dbo.JN_Product.Spelling,dbo.JN_Class.ClassName from (dbo.JN_Product inner join dbo.JN_ProductPrice on dbo.JN_Product.ProductID = dbo.JN_ProductPrice.ProductID) inner join dbo.JN_Class on dbo.JN_Product.ClassID = dbo.JN_Class.ClassID where dbo.JN_Product.Spelling like '%"
							+ vegeName + "%' order by dbo.JN_Class.ClassName";
				}
			}
		}
		return productPriceDao.executebySql(sqlString);
	}
	//获得库存
	public List getWarehouse(String vegeName, String storeId)
	{
		String sqlString = "";

		String chinese = "";

		if (vegeName == null || vegeName.equals("")) {
			sqlString = "select dbo.JN_WarehouseDetail.ID,dbo.JN_Product.ProductName,dbo.JN_WarehouseDetail.Quantity,dbo.JN_Product.Unit from dbo.JN_Store,dbo.JN_Product,dbo.JN_Warehouse,dbo.JN_WarehouseDetail where dbo.JN_Product.ProductID = dbo.JN_WarehouseDetail.ProductID and dbo.JN_Store.StoreID = dbo.JN_Warehouse.StoreID and dbo.JN_Warehouse.WarehouseID = dbo.JN_WarehouseDetail.WarehouseID and dbo.JN_Warehouse.StoreID = '"
					+ storeId + "' order by dbo.JN_Store.StoreName ";
		} else {

			Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");

			Matcher matcher = pattern.matcher(vegeName);

			if (matcher.find()) {

				for (int i = 0; i < vegeName.length(); i++) {

					matcher = pattern.matcher(vegeName.substring(i, i + 1));

					if (matcher.matches()) {
						chinese += "%" + vegeName.substring(i, i + 1);
					}
				}

				chinese += "%";

				sqlString = "select dbo.JN_WarehouseDetail.ID,dbo.JN_Product.ProductName,dbo.JN_WarehouseDetail.Quantity,dbo.JN_Product.Unit from dbo.JN_Store,dbo.JN_Product,dbo.JN_Warehouse,dbo.JN_WarehouseDetail where dbo.JN_Product.ProductID = dbo.JN_WarehouseDetail.ProductID and dbo.JN_Store.StoreID = dbo.JN_Warehouse.StoreID and dbo.JN_Warehouse.WarehouseID = dbo.JN_WarehouseDetail.WarehouseID and dbo.JN_Product.ProductName like '"
						+ chinese
						+ "' and dbo.JN_Warehouse.StoreID = '"
						+ storeId
						+ "' order by dbo.JN_Store.StoreName";
			} else {
				sqlString = "select dbo.JN_WarehouseDetail.ID,dbo.JN_Product.ProductName,dbo.JN_WarehouseDetail.Quantity,dbo.JN_Product.Unit from dbo.JN_Store,dbo.JN_Product,dbo.JN_Warehouse,dbo.JN_WarehouseDetail where dbo.JN_Product.ProductID = dbo.JN_WarehouseDetail.ProductID and dbo.JN_Store.StoreID = dbo.JN_Warehouse.StoreID and dbo.JN_Warehouse.WarehouseID = dbo.JN_WarehouseDetail.WarehouseID and dbo.JN_Product.Spelling like '%"
						+ vegeName
						+ "%' and dbo.JN_Warehouse.StoreID = '"
						+ storeId
						+ "' order by dbo.JN_Store.StoreName";
			}
		}
		return productPriceDao.executebySql(sqlString);
	}
	
	//获得库存详情
	public List getWarehouseInfo(String warehouseID) {

		String sqlString = "select dbo.JN_Product.ProductID,dbo.JN_Product.ProductName,dbo.JN_WarehouseDetail.Quantity,dbo.JN_Product.Unit from dbo.JN_Product,dbo.JN_Store,dbo.JN_Warehouse,dbo.JN_WarehouseDetail where dbo.JN_Product.ProductID = dbo.JN_WarehouseDetail.ProductID and dbo.JN_Store.StoreID = dbo.JN_Warehouse.StoreID and dbo.JN_Warehouse.WarehouseID = dbo.JN_WarehouseDetail.WarehouseID and dbo.JN_WarehouseDetail.ID = '"
				+ warehouseID + "'";
	
		return productPriceDao.executebySql(sqlString);
	}
  //获得销售记录
	public List getSaleRecord(String saleperson,String mumberNo,String startTime,String endTime) {
		
		String sqlString = "select dbo.JN_Bill.BillID,dbo.JN_Bill.MemberID,dbo.JN_Bill.BillNO,dbo.JN_Bill.SumMoney,dbo.JN_Bill.SaleMoney,dbo.JN_Employee.UserName,dbo.JN_Bill.BillDateTime,dbo.JN_Bill.Remark from dbo.JN_Bill inner join dbo.JN_Employee on dbo.JN_Employee.UserID = dbo.JN_Bill.CreateBy "
				+ "where 1=1 ";
		if(saleperson!=null)
			sqlString+=" and dbo.JN_Employee.UserName='"+saleperson+"'";
		if(mumberNo!=null)
			sqlString+=" and dbo.JN_Bill.MemberID='"+mumberNo+"'";
			
		String ordersql=" order by dbo.JN_Bill.BillDateTime";
		sqlString+=ordersql;
		return productPriceDao.executebySql(sqlString);
	}
	
	//获得销售记录详情
	public List getDetailSaleRecord(String id) {

		String sqlString = "select dbo.JN_Product.ProductNO,dbo.JN_Bill.BillNO,dbo.JN_Product.ProductName,dbo.JN_BillDetail.Quantity,dbo.JN_BillDetail.Price,dbo.JN_BillDetail.Discount,dbo.JN_BillDetail.Remark from dbo.JN_Bill, dbo.JN_BillDetail ,dbo.JN_Product where dbo.JN_Bill.BillID = dbo.JN_BillDetail.BillID and dbo.JN_BillDetail.ProductID = dbo.JN_Product.ProductID and dbo.JN_Bill.BillID = '"
				+ id + "'";
		return productPriceDao.executebySql(sqlString);
	}
	

	public List getCategory() {
		String sqlStr = "select Category from dbo.JN_Category";
		return productPriceDao.executebySql(sqlStr);
	}

	public List getProductClass() {
		String sqlStr = "select ClassName from dbo.JN_Class";
		return productPriceDao.executebySql(sqlStr);
	}
	
	public List getPclass() {
		String sqlStr = "select classId,ClassName from dbo.JN_Class";
		return productPriceDao.executebySql(sqlStr);
	}

	public List getProductClassByCategoryName(String categoryName) {
		    String categoryID = null;
			String sqlStr = "select CategoryID from dbo.JN_Category where Category = '" + categoryName + "'";

			List list=productPriceDao.executebySql(sqlStr);

			if (list.size()>0) {
				categoryID = (String) list.get(0);
			}

			sqlStr = "select ClassName from dbo.JN_Class where CategoryID = '" + categoryID + "'";
			return productPriceDao.executebySql(sqlStr);
	}

	public List getProductNameByClassName(String className) {
		String classID = null;

			String sqlStr = "select ClassID from dbo.JN_Class where ClassName = '" + className + "'";

			 List list=productPriceDao.executebySql(sqlStr);

			if (list.size()>0) {
				classID= (String) list.get(0);
			}


			sqlStr = "select ProductName from dbo.JN_Product where ClassID = '" + classID + "'";

			return productPriceDao.executebySql(sqlStr);
	}

	public List getProductInfo(String productName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getSalesRecord(String[] vegesName, String beginTime,
			String endTime) {
		    String[][] result;
		    List reslist=null;
			String temp = "";

			for (int i = 0; i < vegesName.length; i++) {

				String sqlStr = "select dbo.JN_Product.ProductName, sum(dbo.JN_BillDetail.Quantity) from (dbo.JN_BillDetail inner join dbo.JN_Product on dbo.JN_BillDetail.ProductID = dbo.JN_Product.ProductID) inner join dbo.JN_Bill on dbo.JN_Bill.BillID = dbo.JN_BillDetail.BillID where dbo.JN_Bill.BillDateTime between '"
						+ beginTime
						+ "' and '"
						+ endTime
						+ "' and dbo.JN_BillDetail.ProductID in(select ProductID from dbo.JN_Product where ProductName = '"
						+ vegesName[i] + "') group by dbo.JN_Product.ProductName";

				List list=productPriceDao.executebySql(sqlStr);

				if (list.size()>0) {

					Object[] obj = (Object[]) list.get(0);
					temp += obj[0] + "#" + obj[1];

					temp += "|";

				} else {

					temp += vegesName[i] + "#0";

					temp += "|";
				}
			}

			String[] tempVege = temp.split("\\|");

			result = new String[tempVege.length][];

			for (int i = 0; i < result.length; i++) {
				result[i] = tempVege[i].split("#");
				reslist.add(result[i]);
			}

			return reslist;
	}

	public List getStatistics(String vegeName, String beginTime, String endTime) {
		String[][] result;
        List reslist = null;
		String sqlString;

		String temp = "";



			sqlString = "select convert(varchar(10),dbo.JN_Bill.BillDateTime,120) as BillDateTime,dbo.JN_BillDetail.Quantity,dbo.JN_BillDetail.Price into #StatisticsTable from dbo.JN_Bill inner join dbo.JN_BillDetail on dbo.JN_Bill.BillID = dbo.JN_BillDetail.BillID where ProductID = (select ProductID from dbo.JN_Product where ProductName = '"
					+ vegeName
					+ "') and dbo.JN_Bill.BillDateTime between '"
					+ beginTime
					+ " 00:00:00' and '"
					+ endTime
					+ " 23:59:59' order by dbo.JN_Bill.BillDateTime asc";

			productPriceDao.executebySql(sqlString);

			sqlString = "select BillDateTime,sum(Quantity) as Quantity ,avg(Price) as Price,sum(Quantity*Price) as total from #StatisticsTable group by BillDateTime having count(BillDateTime) >= 1";

			List list=productPriceDao.executebySql(sqlString);

			while (list.size()>0) {
                Object[] obj = (Object[]) list.get(0);
				temp += obj[0] + "#" + obj[1] + "#"
						+ new DecimalFormat("0.00").format(obj[2]) + "#"
						+ new DecimalFormat("0.00").format(obj[3]);

				temp += "|";
			}

			productPriceDao.executebySql("drop table #StatisticsTable");

			String[] tempVege = temp.split("\\|");

			result = new String[tempVege.length][];

			for (int i = 0; i < result.length; i++) {
				result[i] = tempVege[i].split("#");
				reslist.add(result[i]);
			}

			if (result[0].length == 1) {
				return null;
			}

			return reslist;

	}

	public List loginServer(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	public String operateDB(String sqlStr) {
		return this.productDao.getResult(sqlStr);
	}

	public List getPrices() {
		String sql="select  t.productid,c.orderprice,c.purchaseprice,c.costprice,c.salesprice,t.productname,t.unit,t.productno,t.spelling,t.classId from JN_product t left join JN_productprice c on t.productid=c.productid ";
		return this.productPriceDao.executebySql(sql);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public boolean saveAll(String updated,List listProductIds) {
		boolean flag=false;
		
		//批量更新
		if(updated != null){
			System. err.println("updated "+updated);
			JsonConfig jsonConfig = new JsonConfig();  //建立配置文件
			jsonConfig.setIgnoreDefaultExcludes(false);  //设置默认忽略
			jsonConfig.setExcludes(new String[]{"productNo", "unit","spelling","classId","productName","editing"});  //将所需忽略字段加到数组中，如“productNo”，“productName”.
			JSONArray jsonArr = JSONArray.fromObject(updated, jsonConfig);
			System. err.println("filter "+jsonArr);
			
			List<ProductPrice> updatelist = JSONArray.toList(jsonArr, ProductPrice.class);
			System.err.println(updatelist.size());
			for(int i=0;i<updatelist.size();i++)
			{
				ProductPrice pd=updatelist.get(i);
				if(listProductIds.contains(pd.getProductId()))
					flag=productPriceDao.alter(pd);
				else
					flag=productPriceDao.save(pd);
			}
		}
		return flag;
	}
	public List getProductIds() {
		String sql="select  productid from  JN_productprice";
		return this.productPriceDao.executebySql(sql);
	}
	
}
