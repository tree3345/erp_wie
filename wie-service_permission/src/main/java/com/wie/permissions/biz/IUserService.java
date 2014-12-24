package com.wie.permissions.biz;

import java.util.List;

import com.wie.common.tools.menu.MenuModel;
import com.wie.common.tools.page.Pagination;
import com.wie.framework.service.ServiceInterface;
import com.wie.permissions.model.Systems;
import com.wie.permissions.model.Users;

/** 
  * @ClassName: IUserService 
  * @Description: 用户业务层接口
  *  
  */
public interface IUserService extends ServiceInterface<Users> {
	
	public boolean alterToGroup(Users user,String groupId);
	/** 
	  * @Title: getPageList 
	  * @Description: 获取具体某页的用户信息
	  * @param @param sys 一条具体用户Users对象的信息
	  * @param @param page 当前页
	  * @param @param rows 一页显示的记录
	  * @param @return
	  * @return Pagination
	  * @throws 
	  */
	public Pagination getPageList(Users sys,int page,int rows,String sort,String order);
	/** 
	  * @Title: getAll 
	  * @Description: 获取所有的用户信息
	  * @param @return
	  * @return List<Users>
	  * @throws 
	  */
	public List<Users> getAll();
	/** 
	  * @Title: getGroupAll 
	  * @Description: 获取某组下的所有用户信息
	  * @param @param groupId 组id
	  * @param @return
	  * @return List<Users>
	  * @throws 
	  */
	public List<Users> getGroupAll(String groupId);
	/** 
	  * @Title: saveToGroup 
	  * @Description: 往组里面添加用户
	  * @param @param groupId  组id
	  * @param @param user 具体用户对象
	  * @param @return
	  * @return Boolean
	  * @throws 
	  */
	public Boolean saveToGroup(String groupId,Users user);
	public Users login(String userName,String password);
	public List<Users> findByLoginIds(String ids);
	public String returnRoleName(String userId);
	public MenuModel findMenuList();

	void addStoresToUser(String storeIds, String id);

	void delStoresFromUser(String id, String storeIds);
}
