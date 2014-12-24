package com.wie.erp.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.WarehouseDao;
import com.wie.erp.model.Warehouse;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.permissions.model.Dictionarys;

@Service
@Transactional
public class WarehouseService extends BaseService<Warehouse> implements IWarehouseService {
	@Autowired
	private WarehouseDao warehouseDao;
	public Pagination getPageList(Warehouse warehouse, int page, int rows,String sort,String order) {
		return warehouseDao.getPageList(warehouse, page, rows,sort,order);
	}

	@Override
	protected DAOInterface<Warehouse> getDAO() {
		return warehouseDao;
	}

	
	public List<Warehouse> getAllWarehouse() {
		return warehouseDao.getAllWarehouse();
	}
	
 
}
