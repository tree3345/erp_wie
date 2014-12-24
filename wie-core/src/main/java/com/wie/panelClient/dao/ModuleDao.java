package com.wie.panelClient.dao;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;
import com.wie.panelClient.model.Module;
import org.springframework.stereotype.Repository;

import java.util.List;


/** 
  * @ClassName: moduleDao
  * @Description: 电子称角色DAO
  *  
  */
@Repository(value="moduleDao")
public class ModuleDao extends BaseDAO<Module> {
	public Pagination getPageList(Module module, int page, int rows, String sort,
			String order) {
		StringBuffer sb = new StringBuffer("from Module where 1=1 ");
		if(null != module){
			if(null != module.getModuleName() && !"".equals(module.getModuleName())){
				sb.append(" and moduleName like '%" +module.getModuleName() + "%' ");
			}
			if(null != module.getViewName() && !"".equals(module.getViewName())){
				sb.append(" and viewName like '%" +module.getViewName() + "%' ");
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

	public Integer getMaxId() {
		return Integer.parseInt(this.executebySql("select max(moduleId) from jn_Module").get(0).toString());
	}

	public List<Module> getModulesByErole(String id) {
		String hql="select module from Module as module left join module.eroles as role  where role.roleId='"+id+"'";
		return this.findList(hql);
	}
}
