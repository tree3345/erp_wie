package com.wie.erp.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.OrderDao;
import com.wie.erp.dao.OrderDetailDao;
import com.wie.erp.model.Order;
import com.wie.erp.model.OrderDetail;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;

@Service
public class OrderDetailService  extends BaseService<OrderDetail> implements IOrderDetailService {

	
	@Autowired
	private OrderDetailDao orderDetailDao;

	public void test() {
		
	}

	@Override
	protected DAOInterface<OrderDetail> getDAO() {
		return orderDetailDao;
	}

	

	public Pagination getPageList(OrderDetail orderDetail, Integer page, Integer rows,
			String sort, String order) {
		return orderDetailDao.getPageList(orderDetail, page, rows,sort,order);
	}
}
