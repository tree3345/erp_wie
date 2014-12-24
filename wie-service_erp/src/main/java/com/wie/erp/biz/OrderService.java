package com.wie.erp.biz;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.OrderDao;
import com.wie.erp.dao.OrderDetailDao;
import com.wie.erp.model.Intercourse;
import com.wie.erp.model.Order;
import com.wie.erp.model.OrderDetail;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.permissions.model.Users;

@Service
public class OrderService  extends BaseService<Order> implements IOrderService {

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private OrderDetailDao orderDetailDao;

	public void test() {
		Order order=new Order();
		order.setOrderNumber("JN12312312313");
		OrderDetail od=new OrderDetail();
		
		od.setQuantity(new BigDecimal(12211.0));
		od.setPrice(new BigDecimal(1.0));
		od.setProductId("7745A3F5-94ED-4FD4-B337-00CF9924579F");
		od.setOrderId(order.getOrderId());
		
		
		Users createBy=new Users();
		createBy.setId("4");
		order.setCreateBy(createBy);
		order.setCreatedt(new Date());
//		orderDetailDao.alter(od);
		orderDao.save(order);
	}

	@Override
	protected DAOInterface<Order> getDAO() {
		return orderDao;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public Map<String,Object> saveItem(Order orderIn, Map<String, String> detailmap,boolean flag) {
		Map<String,Object> result = new HashMap<String,Object>();
		if(flag)
		{
			orderDao.save(orderIn);
		}
		else{
			orderDao.alter(orderIn);
		}
		
		String inserted=detailmap.get("inserted");
		String deleted=detailmap.get("deleted");
		String updated=detailmap.get("updated");
		List<OrderDetail> insertlist=new ArrayList<OrderDetail>();
		if(inserted != null&&!"".equals(inserted)){
			JSONArray ja =JSONArray.fromObject(inserted);
			for (int i = 0; i < ja.size(); i++) {
				OrderDetail od=new OrderDetail();
				JSONObject jo = (JSONObject) ja.get(i);
				String productId=jo.get("productId").toString();
				od.setProductId(productId);
				od.setOrderId(orderIn.getOrderId());
				String totalCount=jo.get("totalCount")+"";
				if(totalCount.equals("")||totalCount.equals("null"))
					totalCount="0";
				od.setQuantity(new BigDecimal(totalCount));
				String totalPrice=jo.get("totalPrice")+"";
				if(totalPrice.equals("")||totalPrice.equals("null"))
					totalPrice="0";
				od.setPrice(new BigDecimal(totalPrice));
				orderDetailDao.save(od);
				insertlist.add(od);
				}
		}
		
		if(updated != null&&!"".equals(updated)){
			JSONArray ja =JSONArray.fromObject(updated);
			for (int i = 0; i < ja.size(); i++) {
				OrderDetail od=new OrderDetail();
				JSONObject jo = (JSONObject) ja.get(i);
				String id=jo.get("detailId").toString();
				od.setId(id);
				od.setOrderId(orderIn.getOrderId());
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
				orderDetailDao.alter(od);
				}
		}
		
		if(deleted != null&&!"".equals(deleted)){
			JSONArray ja =JSONArray.fromObject(deleted);
			for (int i = 0; i < ja.size(); i++) {
				JSONObject jo = (JSONObject) ja.get(i);
				String id=jo.get("detailId").toString();
				orderDetailDao.deleteById(id);
				}
		}
		
		result.put("success", true);
		result.put("id", orderIn.getOrderId());
		return result;
	}

	public Pagination getPageList(Order orderIn, Integer page, Integer rows,
			String sort, String order) {
		return orderDao.getPageList(orderIn, page, rows,sort,order);
	}

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public OrderDetailDao getOrderDetailDao() {
		return orderDetailDao;
	}

	public void setOrderDetailDao(OrderDetailDao orderDetailDao) {
		this.orderDetailDao = orderDetailDao;
	}

	
	public  String checkOrder(Order order){
		
		if (order.getStatus() == 1){
			return "该单据已经审核过，不能再审核。";
		}
		order.setStatus(1);
		orderDao.alter(order);
		return "ok";
	}
    @Override
    public void deleteOrderById(String id, List<OrderDetail> orderDetails) {
		for(OrderDetail od:orderDetails)
		{
			orderDetailDao.delete(od);
		}
		orderDao.deleteById(id);
	}
	
}
