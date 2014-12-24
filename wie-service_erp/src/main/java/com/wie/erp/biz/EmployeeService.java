package com.wie.erp.biz;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.panelClient.dao.EmployeeDao;
import com.wie.panelClient.model.Employee;
import com.wie.panelClient.model.Erole;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2014/12/12.
 */
@Service
public class EmployeeService extends BaseService<Employee> implements IEmployeeService {
    @Autowired
    private EmployeeDao employeeDao;
    @Override
    public Pagination getPageList(Employee employee, int page, int rows, String sort, String order) {
        return employeeDao.getPageList(employee,page,rows,sort,order);
    }

    @Override
    public boolean saveRoleTo(Map<String, Object> map,String id) {
        boolean flag=false;
        JSONArray inserts=(JSONArray)map.get("insert");
        JSONArray deletes=(JSONArray)map.get("delete");
        Employee employee = this.employeeDao.findById(id);

        if(inserts.size()>0) {
            List<Erole> insertList = JSONArray.toList(inserts, Erole.class);
                flag= this.employeeDao.saveRolesfrom(employee, insertList);
        }

        if(deletes.size()>0) {
            List<Erole> deleteList = JSONArray.toList(deletes, Erole.class);
            flag=this.employeeDao.delRolesFrom(employee,deleteList);
        }
        return flag;
    }

    @Override
    protected DAOInterface<Employee> getDAO() {
        return employeeDao;
    }
}
