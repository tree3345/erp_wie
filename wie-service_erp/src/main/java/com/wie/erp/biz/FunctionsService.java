package com.wie.erp.biz;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.panelClient.dao.FunctionsDao;
import com.wie.panelClient.dao.ModuleDao;
import com.wie.panelClient.model.Functions;
import com.wie.panelClient.model.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2014/12/15.
 */
@Service
public class FunctionsService extends BaseService<Functions> implements IFunctionsService {
    @Autowired
    private FunctionsDao functionsDao;
    @Override
    public Pagination getPageList(Functions functions, int page, int rows, String sort, String order) {
        return functionsDao.getPageList(functions,page,rows,sort,order);
    }

    @Override
    public int getMaxId() {
        return functionsDao.getMaxId();
    }

    @Override
    public List<Functions> getFunctionsByErole(String roleId) {
        return functionsDao.getFunctionsByErole(roleId);
    }


    @Override
    protected DAOInterface<Functions> getDAO() {
        return functionsDao;
    }
}
