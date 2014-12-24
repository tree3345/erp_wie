package com.wie.permissions.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;
import com.wie.permissions.model.CheckArgument;


@Repository(value="checkArgumentDAO")
public class CheckArgumentDAO extends BaseDAO<CheckArgument> {

	public List<CheckArgument> getAllcheckArgument(){
		return this.findList("from CheckArgument");
	}

	public Pagination getPageList(CheckArgument checkArgument, int page, int rows, String sort,
			String order) {
		StringBuffer sb = new StringBuffer("from CheckArgument where 1=1 ");
		if(null != checkArgument){
			if(null != checkArgument.getName() && !"".equals(checkArgument.getName())){
				sb.append("and name like '%" +checkArgument.getName() + "%' ");
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
