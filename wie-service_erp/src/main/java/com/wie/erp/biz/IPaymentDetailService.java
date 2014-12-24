package com.wie.erp.biz;



import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.OrderDetail;
import com.wie.erp.model.PaymentDetail;
import com.wie.framework.service.ServiceInterface;

public interface IPaymentDetailService extends ServiceInterface<PaymentDetail>{

	public void test();


	public Pagination getPageList(PaymentDetail paymentDetail, Integer page, Integer rows,
			String sort, String order);

}
