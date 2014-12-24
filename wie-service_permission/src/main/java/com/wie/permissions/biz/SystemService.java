package com.wie.permissions.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.permissions.dao.SystemDao;
import com.wie.permissions.model.Systems;

@Service
@Transactional
public class SystemService extends BaseService<Systems> implements ISystemService {

	@Autowired
	private SystemDao systemDao;
	
	public Pagination getPageList(Systems sys,int page, int rows,String sort,String order) {
		return systemDao.getPageList(sys,page, rows,sort,order);
	}
	public List<Systems> getAll(){
		return systemDao.getAll();
	}

	@Override
	protected DAOInterface<Systems> getDAO() {
		return systemDao;
	}
}
