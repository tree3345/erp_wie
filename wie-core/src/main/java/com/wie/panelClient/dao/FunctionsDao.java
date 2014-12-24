package com.wie.panelClient.dao;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;
import com.wie.panelClient.model.Functions;
import com.wie.panelClient.model.Functions;
import org.springframework.stereotype.Repository;

import java.util.List;


/** 
  * @ClassName: FunctionsDao
  * @Description: 电子称角色DAO
  *  
  */
@Repository(value="functionsDao")
public class FunctionsDao extends BaseDAO<Functions> {
	public Pagination getPageList(Functions functions, int page, int rows, String sort,
			String order) {
		StringBuffer sb = new StringBuffer("from Functions where 1=1 ");
		if(null != functions){
			if(null != functions.getFunctionName() && !"".equals(functions.getFunctionName())){
				sb.append(" and functionName like '%" +functions.getFunctionName() + "%' ");
			}
			if(null != functions.getControlName() && !"".equals(functions.getControlName())){
				sb.append(" and controlName like '%" +functions.getControlName() + "%' ");
			}
			if(null!=functions.getModule())
					sb.append(" and module.moduleId='"+functions.getModule().getModuleId()+"'");
		}
		if(sort!=null && !"".equals(sort)){
			sb.append(" order by "+sort);
			if(order!=null && !"".equals(order)){
				sb.append(" "+order);
			}else{
				sb.append(" desc");
			}
		}else{
			sb.append(" order by module.moduleId asc,functionId asc");
		}
		return this.getHandler().getPageList(Finder.create(sb.toString()), page, rows);
	}

	public int getMaxId() {
		Object obj=this.executebySql("select max(functionId) from jn_ModuleFunction").get(0);
		if(obj==null)
			obj=0;
		return Integer.parseInt(obj.toString());
	}

	public List<Functions> getFunctionsByErole(String roleId) {
		String hql="select functions from Functions as functions left join functions.eroles as role  where role.roleId='"+roleId+"' order by functions.module.moduleId asc,functions.functionId asc";
		return this.findList(hql);
	}
}
