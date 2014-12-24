package com.wie.panelClient.dao;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;
import com.wie.panelClient.model.Employee;
import com.wie.panelClient.model.Erole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/** 
  * @ClassName: EmployeeDao
  * @Description: 电子称操作员DAO
  *  
  */
@Repository(value="employeeDao")
public class EmployeeDao extends BaseDAO<Employee> {

	public Pagination getPageList(Employee employee, int page, int rows, String sort,
			String order) {
		StringBuffer sb = new StringBuffer("from Employee where 1=1 ");
		if(null != employee){
			if(null != employee.getUserName() && !"".equals(employee.getUserName())){
				sb.append(" and userName like '%" +employee.getUserName() + "%' ");
			}
			if(null != employee.getEmployeeName() && !"".equals(employee.getEmployeeName())){
				sb.append(" and employeeName like '%" +employee.getEmployeeName() + "%' ");
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

	public boolean saveRolesfrom(Employee employee, List<Erole> insertList) {
		boolean flag=false;
		Set<Erole> empRoles = employee.getEroles();
		for(Erole erole:insertList){
			flag=empRoles.add(erole);
		}
		employee.setEroles(empRoles);
		return flag;
	}

	public boolean delRolesFrom(Employee employee, List<Erole> deleteList) {
		boolean flag=false;
		Set<Erole> empRoles = employee.getEroles();
		for(Erole erole:deleteList){
			Erole delErole = (Erole)this.getHandler().findObjById(Erole.class, erole.getRoleId());
			flag=empRoles.remove(delErole);
		}
		employee.setEroles(empRoles);
		return flag;
	}
}
