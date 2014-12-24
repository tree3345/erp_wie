package com.wie.erp.biz;


import java.util.List;
import java.util.Map;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Payment;
import com.wie.erp.model.PaymentDetail;
import com.wie.erp.model.WarehouseDetail;
import com.wie.framework.service.ServiceInterface;

public interface IPaymentService extends ServiceInterface<Payment>{


	public Map<String,Object> saveItem(Payment payment, Map<String, String> detailmap, boolean flag);

	public Pagination getPageList(Payment payment, Integer page, Integer rows,
			String sort, String order);

	public String checkPayment(Payment payment);

	public void deletePaymentById(String id,List<PaymentDetail> paymentDetails);

	public Map<String,Object> getProfitLoss(Map<String,String> paramsMap,int page,int rows,String sort,String order);
}
