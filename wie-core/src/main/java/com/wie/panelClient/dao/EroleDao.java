package com.wie.panelClient.dao;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;
import com.wie.panelClient.model.Employee;
import com.wie.panelClient.model.Erole;
import com.wie.panelClient.model.Functions;
import com.wie.panelClient.model.Module;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/** 
  * @ClassName: EroleDao
  * @Description: 电子称角色DAO
  *  
  */
@Repository(value="eroleDao")
public class EroleDao extends BaseDAO<Erole> {
	public Pagination getPageList(Erole erole, int page, int rows, String sort,
			String order) {
		StringBuffer sb = new StringBuffer("from Erole where 1=1 ");
		if(null != erole){
			if(null != erole.getRoleName() && !"".equals(erole.getRoleName())){
				sb.append(" and roleName like '%" +erole.getRoleName() + "%' ");
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

	public List<Erole> getEroleByEmp(String empId) {
		String hql="select role from Erole as role left join role.employees as emp where  emp.userId='"+empId+"'" ;
		return this.findList(hql);
	}

	public boolean saveModulesTo(Erole erole, List<Module> insertList) {
		boolean flag=false;
		Set<Module> eroleModules = erole.getModules();
		for(Module module:insertList){
			flag=eroleModules.add(module);
		}
		erole.setModules(eroleModules);
		return flag;
	}

	public boolean delModulessFrom(Erole erole, List<Module> deleteList) {
		boolean flag=false;
		Set<Module> eroleModules = erole.getModules();
		for(Module module:deleteList){
			Module delModule = (Module)this.getHandler().findObjById(Module.class, module.getModuleId());
			flag=eroleModules.remove(delModule);
		}
		erole.setModules(eroleModules);
		return flag;
	}

	public boolean saveFunctionsTo(Erole erole, List<Functions> insertList) {
		boolean flag=false;
		Set<Functions> eroleFunctions = erole.getFunctions();
		for(Functions functions:insertList){
			flag=eroleFunctions.add(functions);
		}
		erole.setFunctions(eroleFunctions);
		return flag;
	}

	public boolean delFunctionsFrom(Erole erole, List<Functions> deleteList) {
		boolean flag=false;
		Set<Functions> eroleFunctions = erole.getFunctions();
		for(Functions functions:deleteList){
			Functions delFunctions = (Functions)this.getHandler().findObjById(Functions.class, functions.getFunctionId());
			flag=eroleFunctions.remove(delFunctions);
		}
		erole.setFunctions(eroleFunctions);
		return flag;
	}
}
