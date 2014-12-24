package com.wie.erp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wie.common.tools.util.ObjectUtils;
import com.wie.erp.model.Product;
import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Order;
import com.wie.erp.model.Payment;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: CategoryDao 
  * @Description: 大类Dao层
  *  
  */
@Repository
public class PaymentDao extends BaseDAO<Payment> {
	
	/** 
	  * @Title: getPageList 
	  * @Description: 获取具体某页的信息
	  * @param @param sys 一条具体Systems对象的信息
	  * @param @param page 当前页
	  * @param @param rows 一页显示多少记录
	  * @param @return
	  * @return Pagination
	  * @throws 
	  */
	public Pagination getPageList(Payment payment,int page, int rows,String sort,String order) {
		StringBuffer sb = new StringBuffer("from Payment b where 1=1 ");
		if(null != payment){
			if(null != payment.getCode() && !"".equals(payment.getCode())){
				sb.append(" and b.code like'%" + payment.getCode() + "%' ");
			}
		}
		
		if(null != payment){
			if(null != payment.getStoreId() && !"".equals(payment.getStoreId())){
				sb.append(" and b.storeId in(" + payment.getStoreId() + ") ");
			}
		}
		//排序
		if(sort!=null && !"".equals(sort)){
			sb.append(" order by "+sort);
			if(order!=null && !"".equals(order)){
				sb.append(" "+order);
			}else{
				sb.append(" desc");
			}
		}
		return super.getHandler().getPageList(Finder.create(sb.toString()), page, rows);
	}
	
	/** 
	  * @Title: getAll 
	  * @Description: 获取所有的系统信息
	  * @param @return
	  * @return List<Systems>
	  * @throws 
	  */
	public List<Order> getAll(){
		StringBuffer sb = new StringBuffer("from Order");
		return this.getHandler().findListOfObj(sb.toString());

	}

	public Map<String,Object> getProfitLoss(Map<String,String> paramsMap,int page,int rows,String sort,String order) {
		String storeId=paramsMap.get("storeId");
		String startTime=paramsMap.get("startTime");
		String productName=paramsMap.get("productName");
		String classId=paramsMap.get("classId");
		String isBusiness=paramsMap.get("isBusiness");
        if(startTime==null||"".equals(startTime))startTime="2000-01-01";
		String totalsql="select count(*) from JN_Product p ";
		String sql="SELECT  "+
				" p.ProductId as productId, p.ProductName as productName, bb.Quantity as quantity_b, pp.Quantity as quantity_p,ii.quantity as quantity_i, ww.Quantity as quantity_w," +
				" CONVERT(DECIMAL(18,2),bb.acount) as acount_b,CONVERT(DECIMAL(18,2),pp.acount) as acount_p, CONVERT(DECIMAL(18,2),ii.acount) as acount_i, CONVERT(DECIMAL(18,2),ww.acount) as acount_w " +
				"FROM JN_Product p " ;
		String commsql=
				"LEFT JOIN (" +
				" SELECT" +
				"  bl.productid AS ProductID," +
				"  SUM (bl.quantity) AS Quantity," +
				"  SUM (" +
				"   bl.quantity * bl.price * (bl.discount / 100)" +
				"  ) AS acount" +
				" FROM" +
				"  JN_BillDetail bl LEFT JOIN JN_Bill b ON bl.BillID=b.BillID where 1=1" +
				"    and b.BillDateTime>'"+startTime+"'" +
				"    and b.StoreID in("+storeId+")" +
				" GROUP BY" +
				"  bl.productId" +
				") AS bb ON bb.ProductID = p.ProductID " +
				"LEFT JOIN (" +
				" SELECT" +
				"  pl.ProductID AS ProductID," +
				"  SUM (pl.Quantity) AS Quantity," +
				"  SUM (pl.quantity * pl.price) AS acount" +
				" FROM" +
				"  JN_PurchaseDetail pl LEFT JOIN JN_Purchase pc ON pl.PurchaseID=pc.PurchaseID where pc.status=1 " +
				"    and pc.checkDate>'"+startTime+"'" +
				"    and pc.StoreID in("+storeId+")" +
				" GROUP BY" +
				"  pl.ProductID" +
				") AS pp ON pp.ProductID = p.ProductID " +
				"LEFT JOIN (" +
				" SELECT" +
				"  il.productId AS productId," +
				"  SUM (il.damageQuantity) AS quantity," +
				"  SUM (il.price * il.damagequantity) AS acount" +
				" FROM" +
				"  jn_inventorydetail il" +
				" LEFT JOIN jn_inventory i ON i.inventoryId = il.inventoryId where i.status=1 " +
				"  and i.checkDate>'"+startTime+"'" +
				"  and i.storeId in("+storeId+")" +
				" GROUP BY" +
				"  il.productId" +
				")  as ii ON ii.productId=p.ProductID " +
				" LEFT JOIN (" +
				" SELECT" +
				"  wl.ProductID AS ProductID," +
				"  SUM (wl.Quantity) AS Quantity," +
				"  SUM (wl.Quantity*c.purchasePrice) AS acount" +
				" FROM" +
				"  JN_WarehouseDetail wl" +
				"  LEFT JOIN JN_Warehouse w ON wl.WarehouseID = w.WarehouseID " +
				"  LEFT JOIN JN_ProductPrice c ON wl.ProductID=c.ProductID " +
				"  where w.StoreID in("+storeId+")" +
				" GROUP BY" +
				"  wl.ProductID" +
				") AS ww ON ww.ProductID = p.ProductID where 1=1 ";
		StringBuffer sb = new StringBuffer(commsql);
		if(productName!=null&&!"".equals(productName)){
			sb.append(" and p.productName like '%"+productName+"%' ");
		}
		if(classId!=null&&!"".equals(classId)){
			sb.append(" and (p.classId='"+classId+"' or p.classId in (select c.classId from jn_class c where c.parentId='"+classId+"'))");
		}
//		if(isBusiness!=null&&!"".equals(isBusiness)){
		   sb.append("  and (p.productId in (select bd.productId from JN_BillDetail bd left join JN_Bill jl on jl.billId=bd.billId where jl.storeId in("+storeId+") ) " +
				   "                           or p.productId in (select pd.productId from JN_PurchaseDetail pd left join JN_Purchase jp on jp.purchaseId=pd.purchaseId where jp.storeId in("+storeId+")) " +
				   "                           or p.productId in (select vd.productId FROM JN_InventoryDetail vd left join JN_Inventory jv on jv.inventoryId=vd.inventoryId where jv.storeId in("+storeId+") ) " +
				   "                           or p.productId in (select productId from JN_WarehouseDetail wd left join JN_Warehouse jw on jw.warehouseId=wd.warehouseId where jw.storeId in("+storeId+")))");
//		}
		//总记录数
		Integer totalCount=Integer.parseInt(this.executebySql(totalsql+sb.toString()).get(0).toString());
		//排序
		if(sort!=null && !"".equals(sort)){
			sb.append(" order by "+sort);
			if(order!=null && !"".equals(order)){
				sb.append(" "+order);
			}else{
				sb.append(" desc");
			}
		}
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("totalCount",totalCount);
		map.put("list",super.getHandler().findListOfObjBySql(sql+sb.toString(), page, rows));
		return  map;
	}
}
