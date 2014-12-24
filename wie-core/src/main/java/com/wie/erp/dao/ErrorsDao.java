package com.wie.erp.dao;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Errors;
import com.wie.erp.model.Store;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

/** 
  * @ClassName: ActionsDAO 
  * @Description: 操作管理DAO 
  *  
  */
@Repository(value="errorsDao")
public class ErrorsDao extends BaseDAO<Errors> {



	public Pagination getPageList(Errors errors, int page, int rows, String sort,
			String order) {
		StringBuffer sb = new StringBuffer("from Errors where 1=1 ");
		if(null != errors){
			if(null != errors.getErrorContent() && !"".equals(errors.getErrorContent())){
				sb.append("and errorContent like '%" +errors.getErrorContent() + "%' ");
			}
            if(null!=errors.getStartTime()&&!"".equals(errors.getStartTime()))
            {
                sb.append(" and errorDateTime>'"+errors.getStartTime()+"'");
            }
            if(null!=errors.getEndTime()&&!"".equals(errors.getEndTime()))
            {
                sb.append(" and errorDateTime>'"+errors.getEndTime()+"'");
            }
            if(null!=errors.getMacAddress()&&!"".equals(errors.getMacAddress())){
                sb.append(" and marAddress like '%"+errors.getMacAddress()+"%'");
            }
		}
		if(sort!=null && !"".equals(sort)){
			sb.append(" order by "+sort);
			if(order!=null && !"".equals(order)){
				sb.append(" "+order);
			}else{
				sb.append(" desc");
			}
		}
		return this.getHandler().getPageList(Finder.create(sb.toString()), page, rows);
	}
}
