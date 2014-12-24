package com.wie.erp.biz;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.panelClient.dao.ModuleDao;
import com.wie.panelClient.model.Functions;
import com.wie.panelClient.model.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2014/12/15.
 */
@Service
public class ModuleService extends BaseService<Module> implements IModuleService {
    @Autowired
    private ModuleDao moduleDao;
    @Override
    public Pagination getPageList(Module module, int page, int rows, String sort, String order) {
        return moduleDao.getPageList(module,page,rows,sort,order);
    }

    @Override
    public Integer getMaxId() {
        return moduleDao.getMaxId();
    }

    @Override
    public List<Module> getModulesByErole(String id) {
        return this.moduleDao.getModulesByErole(id);
    }

    @Override
    public void saveModuleFuncions() {
    }

    @Override
    protected DAOInterface<Module> getDAO() {
        return moduleDao;
    }
}
