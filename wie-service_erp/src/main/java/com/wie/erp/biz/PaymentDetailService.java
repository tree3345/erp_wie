package com.wie.erp.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.PaymentDetailDao;
import com.wie.erp.model.PaymentDetail;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;

@Service
public class PaymentDetailService  extends BaseService<PaymentDetail> implements IPaymentDetailService {

	
	@Autowired
	private PaymentDetailDao paymentDetailDao;

	public void test() {
		
	}

	@Override
	protected DAOInterface<PaymentDetail> getDAO() {
		return paymentDetailDao;
	}

	

	public Pagination getPageList(PaymentDetail paymentDetail, Integer page, Integer rows,
			String sort, String order) {
		return paymentDetailDao.getPageList(paymentDetail, page, rows,sort,order);
	}
}
