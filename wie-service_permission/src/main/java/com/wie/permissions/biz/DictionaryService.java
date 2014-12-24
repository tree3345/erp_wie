package com.wie.permissions.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.permissions.dao.DictionaryDao;
import com.wie.permissions.model.Dictionarys;

@Service
@Transactional
public class DictionaryService extends BaseService<Dictionarys> implements IDictionaryService {
	@Autowired
	private DictionaryDao dictionaryDao;
	public Pagination getPageList(Dictionarys dic, int page, int rows,String sort,String order) {
		return dictionaryDao.getPageList(dic, page, rows,sort,order);
	}

	@Override
	protected DAOInterface<Dictionarys> getDAO() {
		return dictionaryDao;
	}

	public DictionaryDao getDictionaryDao() {
		return dictionaryDao;
	}

	public void setDictionaryDao(DictionaryDao dictionaryDao) {
		this.dictionaryDao = dictionaryDao;
	}
	
	public List<Dictionarys> getAllDictionary() {
		return dictionaryDao.getAllDictionary();
	}
	
	public String findMax(String nickName) {
		return dictionaryDao.findMax(nickName);
	}
 
}
