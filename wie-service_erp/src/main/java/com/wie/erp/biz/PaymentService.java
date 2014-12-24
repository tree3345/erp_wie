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
import com.wie.erp.dao.WarehouseDao;
import com.wie.erp.dao.WarehouseDetailDao;
import com.wie.erp.model.Order;
import com.wie.erp.model.OrderDetail;
import com.wie.erp.model.Payment;
import com.wie.erp.model.PaymentDetail;
import com.wie.erp.model.Product;
import com.wie.erp.model.Warehouse;
import com.wie.erp.model.WarehouseDetail;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.permissions.model.Users;

@Service
public class PaymentService  extends BaseService<Payment> implements IPaymentService {

	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private PaymentDetailDao paymentDetailDao;
	
	@Autowired
	private WarehouseDao warehouseDao;
	
	@Autowired
	private WarehouseDetailDao warehouseDetailDao;
	
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderDetailDao orderDetailDao;

	@Override
	protected DAOInterface<Payment> getDAO() {
		return paymentDao;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public Map<String,Object> saveItem(Payment payment, Map<String, String> detailmap,boolean flag) {
		Map<String,Object> result = new HashMap<String,Object>();
		if(flag)
		{
			paymentDao.save(payment);
		}
		else{
			paymentDao.alter(payment);
		}
		
		String inserted=detailmap.get("inserted");
		String deleted=detailmap.get("deleted");
		String updated=detailmap.get("updated");
		List<PaymentDetail> insertlist=new ArrayList<PaymentDetail>();
		if(inserted != null&&!"".equals(inserted)){
			JSONArray ja =JSONArray.fromObject(inserted);
			for (int i = 0; i < ja.size(); i++) {
				PaymentDetail od=new PaymentDetail();
				JSONObject jo = (JSONObject) ja.get(i);
				String productId=jo.get("productId").toString();
				od.setProductId(productId);
				od.setPaymentId(payment.getPaymentId());
				String totalCount=jo.get("totalCount")+"";
				if(totalCount.equals("")||totalCount.equals("null"))
					totalCount="0";
				od.setQuantity(new BigDecimal(totalCount));
				String totalPrice=jo.get("totalPrice")+"";
				if(totalPrice.equals("")||totalPrice.equals("null"))
					totalPrice="0";
				od.setPrice(new BigDecimal(totalPrice));
				String paid=jo.get("paid")+"";
				if(paid.equals("")||paid.equals("null"))
					paid="0";
				od.setPaid(new BigDecimal(paid));
				String unpaid=jo.get("unpaid")+"";
				if(unpaid.equals("")||unpaid.equals("null"))
					unpaid="0";
				od.setUnpaid(new BigDecimal(unpaid));
				paymentDetailDao.save(od);
				insertlist.add(od);
				}
		}
		
		if(updated != null&&!"".equals(updated)){
			JSONArray ja =JSONArray.fromObject(updated);
			for (int i = 0; i < ja.size(); i++) {
				PaymentDetail od=new PaymentDetail();
				JSONObject jo = (JSONObject) ja.get(i);
				String id=jo.get("detailId").toString();
				od.setId(id);
				od.setPaymentId(payment.getPaymentId());
				String productId=jo.get("productId").toString();
				od.setProductId(productId);
				
				String totalCount=jo.get("totalCount")+"";
				if(totalCount.equals(""))
					totalCount="0";
				od.setQuantity(new BigDecimal(totalCount));
				String totalPrice=jo.get("totalPrice")+"";
				if(totalPrice.equals(""))
					totalPrice="0";
				od.setPrice(new BigDecimal(totalPrice));
				String paid=jo.get("paid")+"";
				if(paid.equals("")||paid.equals("null"))
					paid="0";
				od.setPaid(new BigDecimal(paid));
				String unpaid=jo.get("unpaid")+"";
				if(unpaid.equals("")||unpaid.equals("null"))
					unpaid="0";
				od.setUnpaid(new BigDecimal(unpaid));
				paymentDetailDao.alter(od);
				}
		}
		
		if(deleted != null&&!"".equals(deleted)){
			JSONArray ja =JSONArray.fromObject(deleted);
			for (int i = 0; i < ja.size(); i++) {
				JSONObject jo = (JSONObject) ja.get(i);
				String id=jo.get("detailId").toString();
				paymentDetailDao.deleteById(id);
				}
		}
		
		result.put("success", true);
		result.put("id", payment.getPaymentId());
		return result;
	}

	public Pagination getPageList(Payment payment, Integer page, Integer rows,
			String sort, String Payment) {
		return paymentDao.getPageList(payment, page, rows,sort,Payment);
	}

	public PaymentDao getPaymentDao() {
		return paymentDao;
	}

	public void setPaymentDao(PaymentDao PaymentDao) {
		this.paymentDao = PaymentDao;
	}

	public PaymentDetailDao getPaymentDetailDao() {
		return paymentDetailDao;
	}

	public void setPaymentDetailDao(PaymentDetailDao paymentDetailDao) {
		this.paymentDetailDao = paymentDetailDao;
	}

	
	public  String checkPayment(Payment payment){
		//String warehouseId=warehouseDao.find(payment.getStoreId()).getWarehouseId();
		List<Warehouse> warehouseList=warehouseDao.findList("from Warehouse w where w.storeId='"+payment.getStoreId()+"'");
		if(warehouseList.size()>1){
			System.err.println("warehouse 与  store 数据不一致");
		     return null;
		}
		String warehouseId=warehouseList.get(0).getWarehouseId();
		if (payment.getStatus() == 1){
			return "该单据已经审核过，不能再审核。";
		}
		List<PaymentDetail> paymentDetails=paymentDetailDao.findList("from PaymentDetail pd where pd.paymentId='"+payment.getPaymentId()+"'");
	/*	for(PaymentDetail pd:paymentDetails){
			//保存到实际付款表
		}*/
		payment.setStatus(1);
		paymentDao.alter(payment);
		return "ok";
	}

	public void deletePaymentById(String id,List<PaymentDetail> paymentDetails) {
		for(PaymentDetail pd:paymentDetails)
		{
			paymentDetailDao.delete(pd);
		}
		paymentDao.deleteById(id);
	}

	@Override
	public Map<String,Object> getProfitLoss(Map<String,String> paramsMap,int page,int rows,String sort,String order) {
		return paymentDao.getProfitLoss(paramsMap, page, rows,sort,order);
	}

}
