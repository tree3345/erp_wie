package com.wie.erp.biz;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.InventoryDetailDao;
import com.wie.erp.model.InventoryDetail;
import com.wie.erp.model.PurchaseDetail;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;

@Service
public class InventoryDetailService  extends BaseService<InventoryDetail> implements IInventoryDetailService {

	
	@Autowired
	private InventoryDetailDao inventoryDetailDao;

	public void test() {
		
	}

	@Override
	protected DAOInterface<InventoryDetail> getDAO() {
		return inventoryDetailDao;
	}

	

	public List getPageList(InventoryDetail inventoryDetail, Integer page, Integer rows,
			String sort, String order) {
		return inventoryDetailDao.getPageList(inventoryDetail, page, rows,sort,order);
	}

	public int findCountBySql(InventoryDetail inventoryDetail) {
		return inventoryDetailDao.findCountBySql(inventoryDetail);
	}

    @Override
    public Pagination getPageDetailList(InventoryDetail inventoryDetail, Integer page, Integer rows, String sort, String order) {
        return inventoryDetailDao.getPageDetailList(inventoryDetail, page, rows,sort,order);
    }

    @Override
    public BigDecimal findInventoryByProductId(String productId ,String startTime,String endTime,String storeId) {
        return inventoryDetailDao.findInventoryByProductId(productId,startTime,endTime,storeId);
    }
}
