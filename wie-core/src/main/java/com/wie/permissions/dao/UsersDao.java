package com.wie.permissions.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.wie.erp.model.Store;
import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;
import com.wie.permissions.model.Groups;
import com.wie.permissions.model.Role;
import com.wie.permissions.model.Systems;
import com.wie.permissions.model.Users;

/** 
  * @ClassName: UsersDao 
  * @Description: 用户Dao层
  * @author ligangying ligangying1987@163.com 
  * @date 2011-9-26 上午9:58:14 
  *  
  */
@Repository
public class UsersDao extends BaseDAO<Users> {
	/**
	  * @Title: getAll 
	  * @Description: 获取所有的用户
	  * @param @return
	  * @return List<Users>
	  * @throws 
	  */
	public List<Users> getAll(){
		StringBuffer sb = new StringBuffer("from Users");
		return this.getHandler().findListOfObj(sb.toString());
	}

	/** 
	  * @Title: getGroupAll 
	  * @Description: 获取某组下的所有用户
	  * @param @param groupId
	  * @param @return
	  * @return List<Users>
	  * @throws 
	  */
	public List<Users> getGroupAll(String groupId) {
		List<Users> result = null;
		String hql="from Groups ";
		if(groupId==null)
			hql+=" where id is null or parentId is null";
			else{
		   hql+="where id='" + groupId + "' or parentId='" + groupId+"'";
			}
		System.out.println("hql---"+hql);
		StringBuffer sb = new StringBuffer(hql);		
		List<Groups> groups = this.getHandler().findListOfObj(sb.toString());
		
		Set<Users> users = new HashSet<Users>();
		
		if(null != groups  && groups.size() > 0){
			result = new ArrayList<Users>();
			for(int i=0;i<groups.size();i++){
				Groups group = groups.get(i);
				Set<Users> set = group.getUsers();
				Iterator<Users> it = set.iterator();
				while(it.hasNext()){
					users.add(it.next());
				}
			}
		}
		
		Iterator<Users> it= users.iterator();
		while(it.hasNext()){
			result.add(it.next());
		}
		
		return result;
	}
	
	/** 
	  * @Title: getPageList 
	  * @Description: 具体某页显示多少条记录
	  * @param @param user 一条具体的用户信息
	  * @param @param page 当前页
	  * @param @param rows 一页显示多少条记录
	  * @param @return
	  * @return Pagination
	  * @throws 
	  */
	public Pagination getPageList(Users user, int page, int rows,String sort,String order) {
		StringBuffer sb = new StringBuffer("from Users where 1=1");
		if(null != user){
			if(null !=user.getLogonid() && !"".equals(user.getLogonid())){
				sb.append(" and logonid like'%" + user.getLogonid() + "%'");
			}
			if(null != user.getSex() && !"".equals(user.getSex())){
				sb.append(" and sex like'%" + user.getSex() + "%'");
			}
			if(null != user.getBirthday() && !"".equals(user.getBirthday())){
				sb.append(" and birthday like'%" + user.getBirthday() + "%'");
			}
		}
		sb.append(" order by registerdate desc");
		
		if(sort!=null && !"".equals(sort)){
			sb.append(" "+sort);
			if(order!=null && !"".equals(order)){
				sb.append(" "+order);
			}else{
				sb.append(" desc");
			}
		}
		return super.getHandler().getPageList(Finder.create(sb.toString()), page, rows);
	}
	
	/** 
	  * @Title: saveToGroup 
	  * @Description: 往组里面添加用户
	  * @param @param groupId 组id
	  * @param @param user 具体用户信息
	  * @param @return
	  * @return Boolean
	  * @throws 
	  */
	public Boolean saveToGroup(String groupId, Users user){
		Boolean flag = false;
		try {
			Groups group = (Groups)this.getHandler().findObjById(Groups.class, groupId);
			Set<Users> set = group.getUsers();
			this.getHandler().saveObj(user);
			set.add(user);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}

	/** 
	  * @Title: login 
	  * @Description: 验证用户名和密码 
	  * @param  userName
	  * @param  password
	  * @return Users 验证成功返回Users对象，失败返回null
	  * @throws 
	  */
	public Users login(String userName,String password){
		return (Users) this.getHandler().findObj("from Users where logonid ="+"'"+userName+"'"+" and password="+"'"+password+"'");
	}

	/** 
	  * @Title: returnGroupName 
	  * @Description: TODO(这里用一句话描述这个方法的作用) 
	  * @param @param userId
	  * @param @return
	  * @return String
	  * @throws 
	  */
	public String returnRoleName(String userId) {
		Users user = (Users)this.getHandler().findObj("from Users where logonid ='" + userId + "'");
		Set<Groups> sets = user.getGroups();
		StringBuffer sb = new StringBuffer("");
		/*
		 * 这是按一个用户只能有一个组,来设计的
		 */
		Iterator<Groups> it = sets.iterator();
		while(it.hasNext()){
			Groups group = it.next();
			
			Set<Role> roles = group.getRoles();
			Iterator<Role> temp = roles.iterator();
			while(temp.hasNext()){
				sb.append("," + temp.next().getEnname());
			}
		}
		
		return sb.toString();
	}

	/** 
	  * @Title: alterToGroup 
	  * @Description: TODO(这里用一句话描述这个方法的作用) 
	  * @param @param user
	  * @param @param groupId
	  * @param @return
	  * @return boolean
	  * @throws 
	  */
	public boolean alterToGroup(Users user, String groupId) {
		Set<Groups> set = new HashSet<Groups>(); 
		set.add((Groups)this.getHandler().findObj("from Groups where id='" + groupId + "'"));
		if(this.getHandler().alterObj(user)){
			user.setGroups(set);
			return true;
		}
		
		return false;
	}
	public List<Users> findByLoginIds(String ids){
		String[] strs=ids.split(",");
		List<Users> list=new ArrayList<Users>();
		for(int i=0;i<strs.length;i++){
			Users user=new Users();
			user=(Users) this.getHandler().findObj("from Users where logonid ='" + strs[i] + "'");
			list.add(user);
		}
		return list;
	}


	public void addStoresToUser(String storeIds, String id) {
		Users user = (Users) this.getHandler().findObjById(Users.class, id);
		Set<Store> userStores = user.getStore();
		storeIds = storeIds.substring(0,storeIds.lastIndexOf(","));
		String[] ids = storeIds.split(",");
		for(int i=0;i<ids.length;i++){
			Store temp = (Store)this.getHandler().findObjById(Store.class, ids[i].trim());
			userStores.add(temp);
		}
		user.setStore(userStores);
	}

	public void delStoresFromUser(String id, String storeIds) {

		Users user = (Users) this.getHandler().findObjById(Users.class, id);
		Set<Store> stores = user.getStore();
		storeIds = storeIds.substring(0,storeIds.lastIndexOf(","));
		String[] tem = storeIds.split(",");
		for(int i=0;i<tem.length;i++){
			Store store = (Store)this.getHandler().findObjById(Store.class, tem[i].trim());
			stores.remove(store);
		}
		user.setStore(stores);
	}
}
