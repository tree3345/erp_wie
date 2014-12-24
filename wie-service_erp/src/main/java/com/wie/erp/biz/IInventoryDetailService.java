package com.wie.erp.biz;



import java.math.BigDecimal;
import java.util.List;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.InventoryDetail;
import com.wie.framework.service.ServiceInterface;

public interface IInventoryDetailService extends ServiceInterface<InventoryDetail>{



	public List getPageList(InventoryDetail inventoryDetail, Integer page, Integer rows,
			String sort, String order);

	public int findCountBySql(InventoryDetail inventoryDetail);

    public Pagination getPageDetailList(InventoryDetail inventoryDetail, Integer page, Integer rows, String sort, String order);

    public BigDecimal findInventoryByProductId(String productId ,String startTime,String endTime,String storeId);
}
