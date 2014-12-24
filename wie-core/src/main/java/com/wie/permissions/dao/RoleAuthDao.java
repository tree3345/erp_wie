package com.wie.permissions.dao;

import org.springframework.stereotype.Repository;

import com.wie.framework.dao.hspring.BaseDAO;
import com.wie.permissions.model.RoleAuth;

@Repository(value="roleAuthDao")
public class RoleAuthDao extends BaseDAO<RoleAuth> {
}
