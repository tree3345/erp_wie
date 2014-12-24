package com.wie.permissions.biz;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wie.common.tools.menu.MenuModel;
import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.permissions.dao.UsersDao;
import com.wie.permissions.model.Users;
@Service
public class UserService extends BaseService<Users> implements IUserService {
	@Autowired
	private UsersDao userDao;
	
	@Override
	protected DAOInterface<Users> getDAO() {
		return userDao;
	}
	
	public String returnRoleName(String userId){
		return userDao.returnRoleName(userId);
	}

	public List<Users> getAll() {
		return userDao.getAll();
	}

	public UsersDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UsersDao userDao) {
		this.userDao = userDao;
	}

	public List<Users> getGroupAll(String groupId) {
		return this.userDao.getGroupAll(groupId);
	}

	public Pagination getPageList(Users sys, int page, int rows,String sort,String order) {
		return this.userDao.getPageList(sys, page, rows,sort,order);
	}

	public Boolean saveToGroup(String groupId, Users user) {
		return this.userDao.saveToGroup(groupId, user);
	}

	public Users login(String userName, String password) {
		return this.userDao.login(userName, password);
	}

	public boolean alterToGroup(Users user, String groupId) {
		return this.userDao.alterToGroup(user,groupId);
	}
	public List<Users> findByLoginIds(String ids){
		return this.userDao.findByLoginIds(ids);
	}

	@Override
	public MenuModel findMenuList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addStoresToUser(String storeIds, String id) {
		this.userDao.addStoresToUser(storeIds,id);
	}

	@Override
	public void delStoresFromUser(String id, String storeIds) {
		this.userDao.delStoresFromUser(id,storeIds);
	}


}
