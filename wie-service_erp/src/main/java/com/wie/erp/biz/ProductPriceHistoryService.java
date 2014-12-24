package com.wie.erp.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.ProductPriceHistoryDao;
import com.wie.erp.model.ProductPriceHistory;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;


@Service
public class ProductPriceHistoryService extends BaseService<ProductPriceHistory> implements IProductPriceHistoryService {
	@Autowired
	private ProductPriceHistoryDao productPriceHistoryDao;
	public Pagination getPageList(ProductPriceHistory productPriceHistory, int page, int rows,String sort,String order) {
		return productPriceHistoryDao.getPageList(productPriceHistory, page, rows,sort,order);
	}

	@Override
	protected DAOInterface<ProductPriceHistory> getDAO() {
		return productPriceHistoryDao;
	}
}
