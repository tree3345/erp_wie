package com.wie.erp.biz;


import java.util.List;
import java.util.Map;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Order;
import com.wie.erp.model.OrderDetail;
import com.wie.framework.service.ServiceInterface;

public interface IOrderService extends ServiceInterface<Order>{

	public void test();

	public Map<String,Object> saveItem(Order orderIn, Map<String, String> detailmap, boolean flag);

	public Pagination getPageList(Order orderIn, Integer page, Integer rows,
			String sort, String order);

	public String checkOrder(Order order);

	public void deleteOrderById(String id,List<OrderDetail> orderDetails);

}
