package com.wie.erp.biz;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.ErrorsDao;
import com.wie.erp.model.Errors;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorsService extends BaseService<Errors> implements IErrorsService {
    @Autowired
    private ErrorsDao errorsDao;
    public Pagination getPageList(Errors Errors, int page, int rows,String sort,String order) {
        return errorsDao.getPageList(Errors, page, rows,sort,order);
    }

    @Override
    protected DAOInterface<Errors> getDAO() {
        return errorsDao;
    }

    public ErrorsDao getErrorsDao() {
        return errorsDao;
    }

    public void setErrorsDao(ErrorsDao errorsDao) {
        this.errorsDao = errorsDao;
    }
}
