package com.wie.permissions.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.permissions.dao.LogDao;
import com.wie.permissions.model.Logs;

@Service
@Transactional
public class LogService extends BaseService<Logs> implements ILogService{
	
	@Autowired
	private LogDao logDao;
	
	public Pagination getPageList(Logs log,int page,int rows,String sort,String order){
		return logDao.getPageList(log, page, rows,sort,order);
	}
	
	@Override
	protected DAOInterface<Logs> getDAO() {
		return logDao;
	}
}
