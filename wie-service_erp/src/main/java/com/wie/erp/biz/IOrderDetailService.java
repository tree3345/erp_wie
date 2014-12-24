package com.wie.erp.biz;



import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Order;
import com.wie.erp.model.OrderDetail;
import com.wie.framework.service.ServiceInterface;

public interface IOrderDetailService extends ServiceInterface<OrderDetail>{

	public void test();


	public Pagination getPageList(OrderDetail orderDetail, Integer page, Integer rows,
			String sort, String order);

}
