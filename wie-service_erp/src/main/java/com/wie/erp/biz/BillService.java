package com.wie.erp.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.BillDao;
import com.wie.erp.model.Bill;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;

@Service
public class BillService extends BaseService<Bill> implements IBillService {

	@Autowired
	private BillDao billDao;
	
	public Pagination getPageList(Bill bill,int page, int rows,String sort,String order) {
		return billDao.getPageList(bill,page, rows,sort,order);
	}
	public List<Bill> getAll(){
		return billDao.getAll();
	}

	@Override
	protected DAOInterface<Bill> getDAO() {
		return billDao;
	}
}
