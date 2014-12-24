package com.wie.erp.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.CategoryDao;
import com.wie.erp.model.Category;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.permissions.model.Systems;

@Service(value="msSQLCategoryService")
@Transactional
public class MsSQLCategoryService extends BaseService<Category> implements ICategoryService {

	@Autowired
	private CategoryDao categoryDao;
	
	public Pagination getPageList(Category category,int page, int rows,String sort,String order) {
		return categoryDao.getPageList(category,page, rows,sort,order);
	}
	public List<Category> getAll(){
		return categoryDao.getAll();
	}

	@Override
	protected DAOInterface<Category> getDAO() {
		return categoryDao;
	}
}
