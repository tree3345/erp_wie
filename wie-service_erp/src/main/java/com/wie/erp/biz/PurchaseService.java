package com.wie.erp.biz;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.OrderDao;
import com.wie.erp.dao.OrderDetailDao;
import com.wie.erp.dao.PaymentDao;
import com.wie.erp.dao.PaymentDetailDao;
import com.wie.erp.dao.ProductPriceDao;
import com.wie.erp.dao.ProductPriceHistoryDao;
import com.wie.erp.dao.PurchaseDao;
import com.wie.erp.dao.PurchaseDetailDao;
import com.wie.erp.dao.WarehouseDao;
import com.wie.erp.dao.WarehouseDetailDao;
import com.wie.erp.model.Order;
import com.wie.erp.model.OrderDetail;
import com.wie.erp.model.Payment;
import com.wie.erp.model.PaymentDetail;
import com.wie.erp.model.Product;
import com.wie.erp.model.ProductPrice;
import com.wie.erp.model.ProductPriceHistory;
import com.wie.erp.model.Purchase;
import com.wie.erp.model.PurchaseDetail;
import com.wie.erp.model.Warehouse;
import com.wie.erp.model.WarehouseDetail;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.permissions.model.Users;

@Service
public class PurchaseService  extends BaseService<Purchase> implements IPurchaseService {

	@Autowired
	private ProductPriceDao productPriceDao;
	@Autowired
	private PurchaseDao purchaseDao;
	
	@Autowired
	private PurchaseDetailDao purchaseDetailDao;
	
	@Autowired
	private WarehouseDao warehouseDao;
	
	@Autowired
	private WarehouseDetailDao warehouseDetailDao;
	
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderDetailDao orderDetailDao;
	
	@Autowired
	private PaymentDao paymentDao;
	@Autowired 
	private PaymentDetailDao paymentDetailDao;
	
	@Autowired
	private ProductPriceHistoryDao productPriceHistoryDao;

	public void test() {
		/*Purchase Purchase=new Purchase();
		Purchase.setPurchaseNumber("JN12312312313");
		PurchaseDetail od=new PurchaseDetail();
		
		od.setQuantity(12211.0);
		od.setPrice(new BigDecimal(1.0));
		od.setProductId("7745A3F5-94ED-4FD4-B337-00CF9924579F");
		od.setPurchaseId(Purchase.getPurchaseId());
		
		
		Users createBy=new Users();
		createBy.setId("4");
		Purchase.setCreateBy(createBy);
		Purchase.setCreatedt(new Date());
//		PurchaseDetailDao.alter(od);
		PurchaseDao.save(Purchase);*/
	}

	@Override
	protected DAOInterface<Purchase> getDAO() {
		return purchaseDao;
	}
   //入库保存
	@SuppressWarnings({ "deprecation", "unchecked" })
	public Map<String,Object> saveItem(Purchase purchase, Map<String, String> detailmap,boolean flag) {
		Map<String,Object> result = new HashMap<String,Object>();
		if(flag)
		{
			purchaseDao.save(purchase);
		}
		else{
			purchaseDao.alter(purchase);
		}
		
		String inserted=detailmap.get("inserted");
		String deleted=detailmap.get("deleted");
		String updated=detailmap.get("updated");
		//List<PurchaseDetail> insertlist=new ArrayList<PurchaseDetail>();
		if(inserted != null&&!"".equals(inserted)){
			JSONArray ja =JSONArray.fromObject(inserted);
			for (int i = 0; i < ja.size(); i++) {
				PurchaseDetail od=new PurchaseDetail();
				JSONObject jo = (JSONObject) ja.get(i);
				String productId=jo.get("productId").toString();
				Product good=new Product();
				good.setProductId(productId);
				od.setProduct(good);
				od.setPurchase(purchase);
				String totalCount=jo.get("totalCount")+"";
				if(totalCount.equals("")||totalCount.equals("null"))
					totalCount="0";
				od.setQuantity(new BigDecimal(totalCount));
				String totalPrice=jo.get("totalPrice")+"";
				if(totalPrice.equals("")||totalPrice.equals("null"))
					totalPrice="0";
				od.setPrice(new BigDecimal(totalPrice));
				purchaseDetailDao.save(od);
				//insertlist.add(od);
				}
		}
		
		if(updated != null&&!"".equals(updated)){
			JSONArray ja =JSONArray.fromObject(updated);
			for (int i = 0; i < ja.size(); i++) {
				PurchaseDetail od=new PurchaseDetail();
				JSONObject jo = (JSONObject) ja.get(i);
				String id=jo.get("detailId").toString();
				od.setId(id);
				od.setPurchase(purchase);
				String productId=jo.get("productId").toString();
				Product pro=new Product();
				pro.setProductId(productId);
				od.setProduct(pro);
				
				String totalCount=jo.get("totalCount")+"";
				if(totalCount.equals(""))
					totalCount="0";
				od.setQuantity(new BigDecimal(totalCount));
				String totalPrice=jo.get("totalPrice")+"";
				if(totalPrice.equals(""))
					totalPrice="0";
				od.setPrice(new BigDecimal(totalPrice));
				purchaseDetailDao.alter(od);
				
				}
		}
		
		if(deleted != null&&!"".equals(deleted)){
			JSONArray ja =JSONArray.fromObject(deleted);
			for (int i = 0; i < ja.size(); i++) {
				JSONObject jo = (JSONObject) ja.get(i);
				String id=jo.get("detailId").toString();
				purchaseDetailDao.deleteById(id);
				}
		}
		
		result.put("success", true);
		result.put("id", purchase.getPurchaseId());
		return result;
	}

	public Pagination getPageList(Purchase purchase, Integer page, Integer rows,
			String sort, String Purchase) {
		return purchaseDao.getPageList(purchase, page, rows,sort,Purchase);
	}

	public PurchaseDao getPurchaseDao() {
		return purchaseDao;
	}

	public void setPurchaseDao(PurchaseDao PurchaseDao) {
		this.purchaseDao = PurchaseDao;
	}

	public PurchaseDetailDao getPurchaseDetailDao() {
		return purchaseDetailDao;
	}

	public void setPurchaseDetailDao(PurchaseDetailDao purchaseDetailDao) {
		this.purchaseDetailDao = purchaseDetailDao;
	}

	
	public  String checkPurchase(Purchase purchase,Map<String,WarehouseDetail> warehouseDetailMap){
		//String warehouseId=warehouseDao.find(purchase.getStoreId()).getWarehouseId();
		List<Warehouse> warehouseList=warehouseDao.findList("from Warehouse w where w.storeId='"+purchase.getStoreId()+"'");
		if(warehouseList.size()>1){
			System.err.println("warehouse 与  store 数据不一致");
		     return null;
		}
		String warehouseId=warehouseList.get(0).getWarehouseId();
		if (purchase.getStatus() == 1){
			return "该单据已经审核过，不能再审核。";
		}
		List<PurchaseDetail> purchaseDetails=purchaseDetailDao.findList("from PurchaseDetail pd where pd.purchase.purchaseId='"+purchase.getPurchaseId()+"'");
		Payment payment = this.converter(purchase);
		paymentDao.save(payment);
		for(PurchaseDetail pd:purchaseDetails){
			PaymentDetail paymentDetail = this.converter(pd);
			paymentDetail.setPaymentId(payment.getPaymentId());
			paymentDetailDao.save(paymentDetail);
            //更新价格表
			ProductPrice productPrice=productPriceDao.findById(pd.getProduct().getProductId());
			if(productPrice==null){
				productPrice=new ProductPrice();
				productPrice.setProductId(pd.getProduct().getProductId());
				productPrice.setPurchasePrice(pd.getPrice());
				productPriceDao.save(productPrice);
			}else{
			productPrice.setPurchasePrice(pd.getPrice());
			productPriceDao.alter(productPrice);
			}
			//产生历史价格
			ProductPriceHistory pph=new ProductPriceHistory();
			pph.setProductId(pd.getProduct().getProductId());
			pph.setUnitPrice(pd.getPrice());
			pph.setActiveDT(purchase.getCheckDate());
			pph.setStoreId(purchase.getStoreId());
			pph.setStatus(1);
			productPriceHistoryDao.save(pph);
			WarehouseDetail wd=new WarehouseDetail();
			if(warehouseDetailMap.get(pd.getProduct().getProductId())!=null)
			{
				wd=warehouseDetailMap.get(pd.getProduct().getProductId());
				wd.setQuantity(wd.getQuantity().add(pd.getQuantity()));
				warehouseDetailDao.alter(wd);
			}else{
				Product product=new Product();
				product.setProductId(pd.getProduct().getProductId());
				wd.setProduct(product);
				Warehouse warehouse = new Warehouse();
				warehouse.setWarehouseId(warehouseId);
				wd.setWarehouse(warehouse);
				wd.setQuantity(pd.getQuantity());
				warehouseDetailDao.save(wd);
			}
			
		}
		purchase.setStatus(1);
		purchaseDao.alter(purchase);
		
		return "ok";
	}
	public boolean saveFromOrder(String orderId,String userId) {
		Order order=orderDao.findById(orderId);
		Purchase purchase=new Purchase();
		Users Inby=new Users();
		Inby.setId(userId);
		Date date=new Date();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMddHHmmss" );
		
		purchase.setCode("JNRK"+sdf.format(date));
		Order orderimp = new Order();
		orderimp.setOrderId(orderId);
		purchase.setOrder(orderimp);
		purchase.setInby(Inby);
		purchase.setIndt(new java.sql.Timestamp(System.currentTimeMillis()));
		purchase.setStoreId(order.getStoreId());
		purchase.setTotalPrice(order.getTotalPrice());
		purchase.setIntercourseId(order.getIntercourse().getId());
		purchase.setStatus(0);
		purchase.setTotalCount(order.getTotalCount());
		purchaseDao.save(purchase);
		
		List<OrderDetail> orderDetailList=orderDetailDao.findList("from OrderDetail where orderId='"+orderId+"'");
		for(OrderDetail orderDetail:orderDetailList)
		{
			PurchaseDetail purchaseDetail=new PurchaseDetail();
			purchaseDetail.setPurchase(purchase);
			Product pro = new Product();
			pro.setProductId(orderDetail.getProductId());
			purchaseDetail.setProduct(pro);
			purchaseDetail.setPrice(orderDetail.getPrice());
			purchaseDetail.setQuantity(orderDetail.getQuantity());
			purchaseDetailDao.save(purchaseDetail);
		}
		return true;
	}

	public void deletePurchaseById(String id,List<PurchaseDetail> purchaseDetails) {
		for(PurchaseDetail pd:purchaseDetails)
		{
			purchaseDetailDao.delete(pd);
		}
		purchaseDao.deleteById(id);
	}
	
	private Payment converter(Purchase purchase)
	{
		Payment payment = new Payment();
		Date date=new Date();
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMddHHmmss" );
		payment.setCode("yf"+sdf.format(date));
		payment.setInby(purchase.getInby());
		payment.setIndt(purchase.getIndt());
		payment.setIntercourseId(purchase.getIntercourseId());
		payment.setPurchaseId(purchase.getPurchaseId());
		payment.setRemark(purchase.getRemark());
		payment.setStatus(purchase.getStatus());
		payment.setStoreId(purchase.getStoreId());
		payment.setTotalCount(purchase.getTotalCount());
		payment.setTotalPrice(purchase.getTotalPrice());
		return payment;
	}
	private PaymentDetail converter(PurchaseDetail purchaseDetail)
	{
		PaymentDetail paymentDetail = new PaymentDetail();
		paymentDetail.setProductId(purchaseDetail.getProduct().getProductId());
		paymentDetail.setPrice(purchaseDetail.getPrice());
		paymentDetail.setQuantity(purchaseDetail.getQuantity());
		return paymentDetail;
	}
}
