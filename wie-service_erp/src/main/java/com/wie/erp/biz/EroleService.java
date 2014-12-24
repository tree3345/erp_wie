package com.wie.erp.biz;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.panelClient.dao.EroleDao;
import com.wie.panelClient.model.Erole;
import com.wie.panelClient.model.Functions;
import com.wie.panelClient.model.Module;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/12/12.
 */
@Service
public class EroleService extends BaseService<Erole> implements IEroleService {
    @Autowired
    private EroleDao eroleDao;
    @Override
    public Pagination getPageList(Erole erole, int page, int rows, String sort, String order) {
        return eroleDao.getPageList(erole,page,rows,sort,order);
    }

    @Override
    public List<Erole> getEroleByEmp(String empId) {
        return eroleDao.getEroleByEmp(empId);
    }

    @Override
    public boolean saveModulesTo(Map<String, Object> map, String id) {
        boolean flag=false;
        JSONArray inserts=(JSONArray)map.get("insert");
        JSONArray deletes=(JSONArray)map.get("delete");
        Erole erole = this.eroleDao.findById(id);

        if(inserts.size()>0) {
            List<Module> insertList = JSONArray.toList(inserts, Module.class);
            flag= this.eroleDao.saveModulesTo(erole, insertList);
        }

        if(deletes.size()>0) {
            List<Module> deleteList = JSONArray.toList(deletes, Module.class);
            flag=this.eroleDao.delModulessFrom(erole,deleteList);
        }
        return flag;
    }

    @Override
    public boolean saveFunctionsTo(Map<String, Object> map, String id) {
        boolean flag=false;
        JSONArray inserts=(JSONArray)map.get("insert");
        JSONArray deletes=(JSONArray)map.get("delete");
        Erole erole = this.eroleDao.findById(id);

        if(inserts.size()>0) {
            List<Functions> insertList = JSONArray.toList(inserts, Functions.class);
            flag= this.eroleDao.saveFunctionsTo(erole, insertList);
        }

        if(deletes.size()>0) {
            List<Functions> deleteList = JSONArray.toList(deletes, Functions.class);
            flag=this.eroleDao.delFunctionsFrom(erole,deleteList);
        }
        return flag;
    }

    @Override
    protected DAOInterface<Erole> getDAO() {
        return eroleDao;
    }
}
