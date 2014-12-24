package com.wie.erp.biz;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.BillDetailDao;
import com.wie.erp.dao.ProfitDao;
import com.wie.erp.dao.PurchaseDetailDao;
import com.wie.erp.model.BillDetail;
import com.wie.erp.model.Profit;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;

@Service
public class BillDetailService extends BaseService<BillDetail> implements
		IBillDetailService {

	@Autowired
	private BillDetailDao billDetailDao;
	@Autowired
	private PurchaseDetailDao purchaseDetailDao;
	@Autowired
	private ProfitDao profitDao;

	public Pagination getPageList(BillDetail billDetail, int page, int rows,
			String sort, String order) {
		return billDetailDao.getPageList(billDetail, page, rows, sort, order);
	}

	public List<BillDetail> getAll() {
		return billDetailDao.getAll();
	}

	// 销售统计
	public List getStatisticsPageList(BillDetail billDetail, int page,
			int rows, String sort, String order) {
		return billDetailDao.getStatisticsPageList(billDetail, page, rows,
				sort, order);
	}

	// 销售统计分页
	public int findCountBySql(BillDetail billDetail) {
		return billDetailDao.findCountBySql(billDetail);
	}

	// 生成销售利润
	public void updatePurchasePrice(String storeId) {
		String sql="select bd.productId,bd.price,b.BillDateTime,bd.billEntry from jn_billDetail bd left join jn_bill b on bd.billId=b.billId where bd.billEntry not in(select pf.billEntry from jn_profit pf) and bd.purchasePrice is  null and b.storeId in("+storeId+")";
		List<Object[]> details=billDetailDao.executebySql(sql);
		
		for(Object[] detail:details)
		{
			List<String> purchasePrices=billDetailDao.executebySql("select top 1 pd.price from jn_purchaseDetail pd left join jn_purchase p on p.purchaseId=pd.purchaseId "
					+ "where pd.productId='"+detail[0]+"' and p.checkDate<'"+detail[2]+"' order by p.checkDate desc");
			BigDecimal salePrice=new BigDecimal(detail[1]+"");
			BigDecimal purchasePrice=new BigDecimal(0);
			if(purchasePrices.size()==1)
				purchasePrice=new BigDecimal(purchasePrices.get(0));
			//System.err.println("入库价："+purchasePrice+" 利润："+salePrice.subtract(purchasePrice));
		  Profit profit=new Profit();
		  profit.setBillEntry(detail[3]+"");
		  profit.setPurchasePrice(purchasePrice);
		  profit.setProfitPrice(salePrice.subtract(purchasePrice));
		  profitDao.save(profit);
		 // billDetailDao.alter(billDetail);
		}
		
		}

	@Override
	protected DAOInterface<BillDetail> getDAO() {
		return billDetailDao;
	}

}
