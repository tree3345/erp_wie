package com.wie.erp.biz;

import java.util.List;

import com.wie.common.tools.page.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.erp.dao.PurchaseDetailDao;
import com.wie.erp.model.PurchaseDetail;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;

@Service
public class PurchaseDetailService  extends BaseService<PurchaseDetail> implements IPurchaseDetailService {

	@Autowired
	private PurchaseDetailDao purchaseDetailDao;

	public void test() {
		
	}

	@Override
	protected DAOInterface<PurchaseDetail> getDAO() {
		return purchaseDetailDao;
	}

	public List getPageList(PurchaseDetail purchaseDetail, Integer page, Integer rows,
			String sort, String order) {
		return purchaseDetailDao.getPageList(purchaseDetail, page, rows,sort,order);
	}

	public int findCountBySql(PurchaseDetail purchaseDetail) {
		return purchaseDetailDao.findCountBySql(purchaseDetail);
	}

    @Override
    public Pagination getPageDetailList(PurchaseDetail purchaseDetail, Integer page, Integer rows, String sort, String order) {
        return purchaseDetailDao.getPageDetailList(purchaseDetail, page, rows,sort,order);
    }
}
