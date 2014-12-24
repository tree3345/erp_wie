package com.wie.erp.dao;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

import com.wie.basic.datasource.DBHelper;
import com.wie.common.tools.encode.AES;
import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Product;
import com.wie.erp.model.ProductClass;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;
import com.wie.permissions.model.Groups;
import com.wie.permissions.model.Users;

/** 
  * @ClassName: ActionsDAO 
  * @Description: 操作管理DAO 
  *  
  */
@Repository(value="productDao")
public class ProductDao extends BaseDAO<Product> {


	
	/** 
	  * @Title: getAll 
	  * @Description: 获得所有记录 
	  * @param @return
	  * @return List<Actions>
	  * @throws 
	  */

	public Pagination getPageList(Product product, int page, int rows,
			String sort, String order) {
		StringBuffer sb = new StringBuffer("from Product where status=1 ");
		if(null != product){
			if(null != product.getProductName() && !"".equals(product.getProductName())){
				sb.append("and (productName like '%" + product.getProductName() + "%' ");
				sb.append("or spelling like '%" + product.getProductName() + "%') ");
			}
		}
		
		if(null != product){
			if(null != product.getFlag()){
				Integer flag=product.getFlag();
				if(flag==0)
				  sb.append(" and (storeId in ("+product.getStoreId()+") or storeId is null) ");
				if(flag==1)
				  sb.append(" and storeId in ("+product.getStoreId()+")");
				if(flag==2)
				  sb.append(" and storeId is null ");
			}
		}
		
		if(null!=product){
			if(null!=product.getStockflag())
			{
				Integer stockFlag=product.getStockflag();
				if(stockFlag==1)
				{
					sb.append(" and productId in (SELECT w.product.productId FROM WarehouseDetail w where w.warehouse.storeId in("+product.getStoreId()+") )");
				}
			}
		}
		if(null != product){
			if(null != product.getProductClass()&& !"".equals(product.getProductClass())
					&& !"null".equals(product.getProductClass())) {
			  if(null != product.getProductClass().getClassId()&&!"".equals(product.getProductClass().getClassId())){	
				List<String> strlist = this.executebySql(
				"select c.classId from jn_class c where c.parentid='"+ product.getProductClass().getClassId() + "' or c.classId='"+product.getProductClass().getClassId()+"'"
				                                         );
				//System.err.println("classId:"+product.getProductClass().getClassId());
				List<String> formatterlist = new ArrayList<String>();
				for (int i = 0; i < strlist.size(); i++) {
					formatterlist.add("'" + strlist.get(i) + "'");
				}
				String ids = formatterlist.toString().replace("[", "(").replace("]", ")");
				if (strlist.size() > 0) {
					sb.append(" and productClass.classId in " + ids);
				} else {
					sb.append(" and productClass.classId = '" + product.getProductClass().getClassId() + "' ");
				}
			  }
			}
		}
		

		if(sort!=null && !"".equals(sort)){
			sb.append(" order by "+sort);
			if(order!=null && !"".equals(order)){
				sb.append(" "+order);
			}else{
				sb.append(" desc");
			}
		}
		return this.getHandler().getPageList(Finder.create(sb.toString()), page, rows);
	}

	public List<Product> getAllProduct() {
		return this.findList("from Product");
	}

	public List<Product> getAll(){
		StringBuffer sb = new StringBuffer("from Product");
		return this.getHandler().findListOfObj(sb.toString());
	}


	public boolean alterToClass(Product product, String classId) {

		/*Set<ProductClass> set = new HashSet<ProductClass>(); 
		set.add((ProductClass)this.getHandler().findObj("from ProductClass where classId='" + classId + "'"));
		if(this.getHandler().alterObj(product)){
			product.setProductClass(set);
			return true;
		}
		*/
		return false;
	}

	public List findByIds(String productIds) {
		String[] inIds=productIds.split(",");
		List<String> idlist=new ArrayList<String>();
		for(int i=0;i<inIds.length;i++){
			idlist.add("'"+inIds[i]+"'");
		}
		productIds=idlist.toString().replace("[", "(").replace("]", ")");	
		String hql="from Product p where p.productId in" +productIds;
		//System.err.println("hql:"+hql);
		return this.findList(hql);
	}

	public List ExistCountList(String productId) {
		String sql="select count(*) from JN_purchaseDetail p where p.productId='"+productId+"'"
				+ " union select count(*) from JN_orderDetail o where o.productId='"+productId+"'"
				+ " union select count(*) from JN_WarehouseDetail w where w.productId='"+productId+"'"
				+ " union select count(*) from JN_billDetail b where b.productId='"+productId+"'";
		return this.executebySql(sql);
	}

	public String getResult(String sqlStr) {

		String result = "";

		String[] temp = sqlStr.split("#");


		if (temp[0].equals("login")) {// 登陆

			sqlStr = "select JN_Employee.UserID,JN_Employee.StoreID,JN_Employee.EmployeeName,JN_Store.StoreName,dbo.JN_Emp_Role.RoleID from (JN_Employee inner join JN_Store on JN_Employee.StoreID = JN_Store.StoreID) inner join dbo.JN_Emp_Role on dbo.JN_Emp_Role.UserID = JN_Employee.UserID where UserName = '"
					+ temp[1] + "' AND Password = '" + AES.Encrypt(temp[2]) + "'";

			List list = this.executebySql(sqlStr);

			if (list.size()>0) {

				for(int i=0;i<list.size();i++){
					Object[] obj = (Object[])list.get(i);
					result += obj[0] + "#" + obj[1] + "#" + obj[2]
							+ "#" + obj[3] + "#" + ((String) obj[4]).toLowerCase(Locale.CHINA);
					result += "|";
				}

				if (!result.equals("")) {

					result = result.substring(0, result.lastIndexOf("|"));
				}


			} else {
				result = "null";
			}

		}else if(temp[0].equals("logout")){
			sqlStr = "select JN_Employee.UserID,JN_Employee.StoreID,JN_Employee.EmployeeName,JN_Store.StoreName,dbo.JN_Emp_Role.RoleID from (JN_Employee inner join JN_Store on JN_Employee.StoreID = JN_Store.StoreID) inner join dbo.JN_Emp_Role on dbo.JN_Emp_Role.UserID = JN_Employee.UserID where UserName = '"
					+ temp[1] + "' AND Password = '" + AES.Encrypt(temp[2]) + "'";

			List list = this.executebySql(sqlStr);

			if (list.size()>0) {

				for(int i=0;i<list.size();i++){
					Object[] obj = (Object[])list.get(i);
					result += obj[0] + "#" + obj[1] + "#" + obj[2]
							+ "#" + obj[3] + "#" + ((String) obj[4]).toLowerCase(Locale.CHINA)+"#logout";
					result += "|";
				}

				if (!result.equals("")) {

					result = result.substring(0, result.lastIndexOf("|"));
				}


			} else {
				result = "null";
			}
		}
		else if (temp[0].equals("select Warehouse")) {// 查询库存

			String sqlString = "select JN_Product.ProductName,JN_Product.Unit,JN_Product.ImageName,sum(JN_WarehouseDetail.Quantity) as Quantity from JN_Product inner join JN_WarehouseDetail on JN_Product.ProductID = JN_WarehouseDetail.ProductID where JN_WarehouseDetail.WarehouseID in (select WarehouseID from JN_Warehouse where StoreID = '"
					+ temp[1]
					+ "' and JN_Product.ClassID like '"
					+ temp[2]
					+ "') and "
					+ temp[3]
					+ " group by JN_Product.ProductName,JN_Product.Unit,JN_Product.ImageName";

			List list = this.executebySql(sqlString);

			if (list.size()>0) {

				for(int i=0;i<list.size();i++) {
					Object[] obj= (Object[])list.get(i);
					result += obj[0] + "#" + obj[1] + "#" + obj[2]
							+ "#" + obj[3];
					result += "|";
				}

				if (!result.equals("")) {

					result.lastIndexOf("|");

					result = result.substring(0, result.lastIndexOf("|"));
				}

			} else {
				result = "null";
			}

		} else if (temp[0].equals("updateWarehouse")) {// 更新库存

			int returnValue = 0;

			for (int i = 1; i < temp.length; i++) {

				String[] vegeMsg = temp[i].split("\\|");

				sqlStr = "update JN_WarehouseDetail set Quantity = Quantity - " + vegeMsg[0]
						+ " where WarehouseID = (select WarehouseID from dbo.JN_Warehouse where StoreID = '"
						+ vegeMsg[1] + "' and ProductID = '" + vegeMsg[2] + "')";

				returnValue += this.executeupdateBysql(sqlStr);
			}

			if (returnValue == 0) {
				result = "null";
			} else {
				result = "1";
			}

		} else if (temp[0].equals("saleRecord")) {//上传销售记录
			//System.out.println(111);


			ArrayList<String> Bill = new ArrayList<String>();
			ArrayList<String> BillDetail = new ArrayList<String>();
			for (int i = 1; i < temp.length; i++) {
				String[] vegeMsg = new String[11];
				String[] tempchild = temp[i].split("\\*");
				double number = 0;
				for (int j = 0; j < tempchild.length; j++) {
					vegeMsg = tempchild[j].split("\\|");

					number += Double.parseDouble(new DecimalFormat("0.00").format((Double.parseDouble(vegeMsg[3])
							* Double.parseDouble(vegeMsg[4]) * (Double.parseDouble(vegeMsg[7]) * 0.01))));

					BillDetail
							.add("INSERT INTO JN_BillDetail([BillEntry],[BillID],[ProductID],[Quantity],[Price],[Discount]) VALUES ('"
									+ UUID.randomUUID().toString()
									+ "','"
									+ vegeMsg[0]
									+ "','"
									+ vegeMsg[9]
									+ "','"
									+ Double.parseDouble(vegeMsg[3])
									+ "','"
									+ Double.parseDouble(vegeMsg[4])
									+ "','"
									+ Double.parseDouble(vegeMsg[7])
									+ "')");
				}
				String tempNumber = new DecimalFormat(".00").format(number);

				String sumMoney;

				if (number > 0) {
					sumMoney = ((tempNumber + "").substring(0, tempNumber.length() - 1)) + "0";
				} else {
					sumMoney = "0";
				}

				Bill.add("INSERT INTO JN_Bill ([BillID],[BillNO],[BillDateTime],[MemberID],[SumMoney],[StoreID],[CreateBy],[IsUpLoad],[SaleMoney]) VALUES('"
						+ vegeMsg[0]
						+ "','"
						+ vegeMsg[1]
						+ "','"
						+ vegeMsg[5]
						+ "','"
						+ ((vegeMsg.length == 10) ? "散客" : vegeMsg[10])
						+ "','"
						+ Double.parseDouble(tempNumber)
						+ "','" + vegeMsg[8] + "','" + vegeMsg[6] + "','Y','" + Double.parseDouble(sumMoney) + "')");
				// vegeMsg=null;
			}
			DBHelper dbHelper=new DBHelper();
			result=dbHelper.uploadSale(Bill, BillDetail);

		} else if (temp[0].equals("select veges")) {// 获得菜品缩写

			sqlStr = "select JN_Product.Spelling from JN_Product inner join JN_ProductPrice on JN_Product.ProductID = JN_ProductPrice.ProductID WHERE "
					+ " ProductName like " + temp[2];

			List list1 = this.executebySql(sqlStr);

			if (list1.size()>0) {

				for(int i=0;i<list1.size();i++) {
					result += list1.get(i);
					result += "|";
				}

				if (!result.equals("")) {

					result.lastIndexOf("|");

					result = result.substring(0, result.lastIndexOf("|"));
				}

			} else {
				result = "null";
			}
		} else if (temp[0].equals("select vegep")) {// 更新菜品价格

			sqlStr = "update JN_ProductPrice set SalesPrice = " + temp[1] + " where ProductID = " + temp[2];

			this.executeupdateBysql(sqlStr);

		} else if (temp[0].equals("select vegese")) {// 获得菜品

			sqlStr = "select JN_Product.ProductName,JN_Product.ImageName,JN_Product.ProductID,JN_ProductPrice.SalesPrice from JN_Product inner join JN_ProductPrice on JN_Product.ProductID = JN_ProductPrice.ProductID WHERE ClassID like "
					+ temp[1] + temp[2];

			List list = this.executebySql(sqlStr);

			if (list.size()>0) {

				for(int i=0;i<list.size();i++) {
					Object[] obj = (Object[])list.get(i);
					result += obj[0] + "#" + obj[1] + "#" + obj[2]
							+ "#" + obj[3];
					result += "|";
				}

				if (!result.equals("")) {

					result.lastIndexOf("|");

					result = result.substring(0, result.lastIndexOf("|"));
				}

			} else {
				result = "null";
			}
		} else if (temp[0].equals("select vegess")) {// 获得菜品

			sqlStr = "select JN_Product.ProductName,JN_Product.ImageName,JN_Product.ProductID,JN_Product.ProductNO,JN_ProductPrice.SalesPrice,JN_Product.Spelling,JN_Product.ClassID from JN_Product inner join JN_ProductPrice on JN_Product.ProductID = JN_ProductPrice.ProductID WHERE ClassID like "
					+ temp[1] + " or Spelling like " + temp[2];

			List  list = this.executebySql(sqlStr);

			if (list.size()>0) {

				for(int i=0;i<list.size();i++) {
					Object[] obj = (Object[])list.get(i);
					result += obj[0] + "#" + obj[1] + "#" + obj[2]
							+ "#" + obj[3] + "#" + obj[4] + "#"
							+ obj[5] + "#" + obj[6];
					result += "|";
				}

				if (!result.equals("")) {

					result.lastIndexOf("|");

					result = result.substring(0, result.lastIndexOf("|"));
				}

			} else {
				result = "null";
			}
		} else if (temp[0].equals("select vege")) {// 获得菜品

			sqlStr = "select JN_Product.ProductName,JN_Product.ImageName,JN_Product.ProductID,JN_ProductPrice.SalesPrice from JN_Product inner join JN_ProductPrice on JN_Product.ProductID = JN_ProductPrice.ProductID WHERE ClassID like "
					+ temp[1] + " or Spelling like " + temp[2];

			List list = this.executebySql(sqlStr);

			if (list.size()>0) {

				for(int i=0;i<list.size();i++) {
					Object[] obj = (Object[])list.get(i);
					result += obj[0] + "#" + obj[1] + "#" + obj[2]
							+ "#" + obj[3];
					result += "|";
				}

				if (!result.equals("")) {

					result.lastIndexOf("|");

					result = result.substring(0, result.lastIndexOf("|"));
				}

			} else {
				result = "null";
			}
		} else if (temp[0].equals("select price")) {// 获得菜品价格

			sqlStr = "select ProductID,SalesPrice from JN_ProductPrice";

			List list = this.executebySql(sqlStr);

			if (list.size()>0) {

				for(int i=0;i<list.size();i++) {
					Object[] obj = (Object[])list.get(i);
					result += obj[0] + "#" + obj[1];
					result += "|";
				}

				if (!result.equals("")) {

					result = result.substring(0, result.lastIndexOf("|"));
				}


			} else {
				result = "null";
			}
		} else if (temp[0].equals("select category")) {// 获得小类

			// macrobin 最新修改 获得小类
			sqlStr = "select ClassID,ClassName from JN_Class order by Sort";

			List list = this.executebySql(sqlStr);

			if (list.size() >0) {

				for (int i=0 ;i<list.size();i++) {
					Object[] obj = (Object[])list.get(i);
					result += obj[0] + "#" + obj[1];
					result += "|";
				}

				if (!result.equals("")) {

					result = result.substring(0, result.lastIndexOf("|"));
				}


			} else {
				result = "null";
			}
		}else if (temp[0].equals("clientError")) {//插入错误
			String sql= "insert into JN_Error(MacAdress,ErrorDateTime,ErrorContent) " +
					"values('"+temp[1]+"','"+temp[2]+"','"+temp[3]+"')";
			boolean rsflag=this.executeSql(sql);
			result=rsflag+"";

		}else if (temp[0].equals("getVersion")) {

			File file = null;

			if (temp[1].equals("high")) {

				file = new File("C:\\EscaleServer\\Client\\High\\");

			} else if (temp[1].equals("low")) {

				file = new File("C:\\EscaleServer\\Client\\Low\\");
			}

			if (file != null && file.exists()) {

				File[] client = file.listFiles();

				if (client.length > 0) {

					result = client[0].getName().substring(client[0].getName().lastIndexOf("_") + 1,
							client[0].getName().lastIndexOf(".apk"));

					try {
						Double.parseDouble(result);
					} catch (Exception e) {
						// TODO: handle exception
						result = "";
					}

				} else {
					result = "";
				}

			} else {
				result = "";
			}
		} else if (temp[0].equals("getPicture")) {
			float version = Float.parseFloat(temp[1]);
			File file = new File("C:\\EscaleServer\\Picture\\");
			String tempResult = "";
			if (file.exists()) {
				for (File tempFile : file.listFiles()) {
					String path = tempFile.getAbsolutePath();
					try {
						float pictureNum = Float.parseFloat(path.substring(path.lastIndexOf("_") + 1,
								path.lastIndexOf(".")));

						if (pictureNum > version) {

							tempResult += path;

							tempResult += "#";
						}
					} catch (Exception e) {
						// TODO: handle exception
						result = "";
					}
				}
				if (tempResult.length() > 0) {
					tempResult = tempResult.substring(0, tempResult.lastIndexOf("#"));
				}

				result = tempResult;

			} else {
				result = "";
			}

		}
		return result;
	}
}
