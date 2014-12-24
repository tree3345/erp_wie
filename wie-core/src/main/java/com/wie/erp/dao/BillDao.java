package com.wie.erp.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Bill;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: CategoryDao 
  * @Description: 大类Dao层
  *  
  */
@Repository
public class BillDao extends BaseDAO<Bill> {
	
	/** 
	  * @Title: getPageList 
	  * @Description: 获取具体某页的信息
	  * @param @param sys 一条具体Systems对象的信息
	  * @param @param page 当前页
	  * @param @param rows 一页显示多少记录
	  * @param @return
	  * @return Pagination
	  * @throws 
	  */
	public Pagination getPageList(Bill bill,int page, int rows,String sort,String order) {
		StringBuffer sb = new StringBuffer("from Bill b where 1=1");
		if(null != bill){
			if(null != bill.getMemberId() && !"".equals(bill.getMemberId())){
				sb.append(" and b.memberId like'%" + bill.getMemberId() + "%'");
			}
		}
		if(null != bill){
			if(null != bill.getCreateBy()){
				sb.append(" and (b.createBy.userName like'%" + bill.getCreateBy().getUserName() + "%' "
						+ " or b.createBy.employeeName like'%" + bill.getCreateBy().getEmployeeName() + "%')");
			}
            //按日期检索
            if(null!=bill.getStartTime()&&null!=bill.getEndTime())
                sb.append(" and (b.billDateTime between '" + bill.getStartTime()+"' and '"+bill.getEndTime()+"')" );

            if(null!=bill.getStoreId()&&!"".equals(bill.getStoreId()))
                sb.append(" and b.storeId in("+bill.getStoreId()+")");
		}



			 
		/*if(null != bill){
			if(null != bill()){
				sb.append(" and b.createBy.employeeName like'%" + bill.getCreateBy().getEmployeeName() + "%'");
			}
		}*/
		//排序
		if(sort!=null && !"".equals(sort)){
			sb.append(" order by "+sort);
			if(order!=null && !"".equals(order)){
				sb.append(" "+order);
			}else{
				sb.append(" desc");
			}
		}
		return super.getHandler().getPageList(Finder.create(sb.toString()), page, rows);
	}
	
	/** 
	  * @Title: getAll 
	  * @Description: 获取所有的系统信息
	  * @param @return
	  * @return List<Systems>
	  * @throws 
	  */
	public List<Bill> getAll(){
		StringBuffer sb = new StringBuffer("from Bill");
		return this.getHandler().findListOfObj(sb.toString());
	}
	
}
