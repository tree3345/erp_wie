package com.wie.permissions.biz;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.permissions.dao.RoleAuthDao;
import com.wie.permissions.model.RoleAuth;

@Service(value="roleAuthService")
public class RoleAuthService extends BaseService<RoleAuth> implements
		IRoleAuthService {
	@Resource(name="roleAuthDao")
	private RoleAuthDao roleAuthDao;

	@Override
	protected DAOInterface<RoleAuth> getDAO() {
		return this.roleAuthDao;
	}

	public RoleAuthDao getRoleAuthDao() {
		return roleAuthDao;
	}

	public void setRoleAuthDao(RoleAuthDao roleAuthDao) {
		this.roleAuthDao = roleAuthDao;
	}

}
