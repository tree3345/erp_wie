/**   
  * @Title: RoleService.java 
  * @Package com.tgyt.permissions.biz 
  * @Description: 
  * @author WangMing wang1988ming@qq.com 
  * @date 2011-9-23 下午2:47:44 
  * @version V1.0   
  */

package com.wie.permissions.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.permissions.dao.ActionsDAO;
import com.wie.permissions.dao.ResourcesDAO;
import com.wie.permissions.dao.RoleAuthDao;
import com.wie.permissions.dao.RoleDao;
import com.wie.permissions.dao.SystemDao;
import com.wie.permissions.model.Actions;
import com.wie.permissions.model.Resources;
import com.wie.permissions.model.Role;
import com.wie.permissions.model.RoleAuth;
import com.wie.permissions.model.Systems;

/** 
 * @ClassName: RoleService 
 * @Description: 角色操作服务层 
 *  
 */
@Service(value="roleService")
public class RoleService extends BaseService<Role> implements IRoleService {
	
	@Resource(name="roleDao")
	private RoleDao roleDao;
	
	@Resource(name="actionsDAO")
	private ActionsDAO actionsDAO;
	
	@Resource(name="roleAuthDao")
	private RoleAuthDao roleAuthDao;
	
	/** 
	  * @Fields resourcesDAO : 资源服务DAO 
	  */ 
	@Resource(name="resourcesDAO")
	private ResourcesDAO resourcesDAO;
	
	/** 
	  * @Fields systemDao : 系统操作DAO 
	  */ 
	@Autowired
	private SystemDao systemDao;
	public boolean addRole(Role role) {
		if(role.getOrderid() == null){
			role.setOrderid(this.roleDao.getMaxOrderId()+1);
		}
		return this.save(role);
	}

	public Pagination getPageList(Role role, int pageNo, int pageSize,String sort,String order) {
		return this.roleDao.getPageList(role, pageNo, pageSize,sort,order);
	}


	public RoleDao getRoleDao() {
		return roleDao;
	}


	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	protected DAOInterface<Role> getDAO() {
		return this.roleDao;
	}
	
	public List<Map<String,Object>> getAllActions(){
		List<Map<String,Object>> actions = new ArrayList<Map<String,Object>>();
		List<Actions> list = this.actionsDAO.getAll();
		Map<String,Object> item = new HashMap<String,Object>();
		item.put("headerText","资源名称");
		item.put("dataField","text");
		item.put("headerAlign","center");
		item.put("dataAlign","center");
		item.put("width",100);
		item.put("handler","customResName");
		item.put("checkbox",true);
//		item.put("width",120);
		actions.add(item);
		for(Iterator<Actions> iter=list.iterator();iter.hasNext();){
			item = new HashMap<String,Object>();
			Actions ac = iter.next();
			item.put("headerText",ac.getName());
			item.put("dataField",ac.getEnname());
			item.put("width",100);
			item.put("headerAlign","center");
			item.put("dataAlign","center");
			item.put("handler","checkbox");
			item.put("checkbox",true);
//			item.put("width",120);
			actions.add(item);
		}
		return actions;
	}

	public void saveAuthorizate(String roldId,String resourcesIds){
		Role role = this.findById(roldId);
		role.getResources().clear();
		if(resourcesIds!=null &&!"".equals(resourcesIds)){
			String[] rids = resourcesIds.split(",");
			for(int i=0;i<rids.length;i++){
				if(rids[i].indexOf(ISystemService.TREE_NAME)!=-1 ||rids[i].indexOf("TREES")!=-1){
					continue;
				}
				Resources rs = this.resourcesDAO.findById(rids[i]);
				role.getResources().add(rs);
			}
		}
		this.alter(role);
	}
	
public List<Map<String,Object>> getAllListTrees(String roleId) {
		Role role = this.roleDao.findById(roleId);
		Map<String,Object> maps = new HashMap<String,Object>();
		if(role!=null){
			for(Iterator<Resources> iter=role.getResources().iterator();iter.hasNext();){
				Resources rs = iter.next();
				maps.put(rs.getId(), rs.getName());
			}
		}
		List<Map<String,Object>> nodes = new ArrayList<Map<String,Object>>();
		Map<String,Object> root = new HashMap<String,Object>();
		root.put("id", "TREES");
		root.put("text", "系统资源");
		nodes.add(root);
		
		List<Systems> systems = this.systemDao.findList("from Systems");
		List<Object> all = new ArrayList<Object>();
		for(Iterator<Systems> iter=systems.iterator();iter.hasNext();){
			all.add(this.getTree(maps,iter.next()).get(0));
		}
		
		root.put("children", all);
		return nodes;
	}
	
	public List<Map<String,Object>> getTree(Map<String,Object> maps,Systems system) {
		// 存放所有节点每一个节点信息都保存到一个Map<String,Object>中
		List<Map<String,Object>> nodes = new ArrayList<Map<String,Object>>();
		if(system==null){
			return new ArrayList<Map<String,Object>>();
		}
		
		// 获取系统一级节点
		Map<String,Object> root = new HashMap<String,Object>();
		root.put("id", ISystemService.TREE_NAME+system.getId());
		root.put("text", system.getName());
		nodes.add(root);//将系统节点放入
		
		// 存放每层资源节点
		List<Map<String,Object>> doing = new ArrayList<Map<String,Object>>();
		doing.addAll(nodes);
		
		List<Resources> types ;
		//存放每层资源节点的临时变量
		List<Map<String,Object>> temp = new ArrayList<Map<String,Object>>();
		//一次循环系统节点，获取每个系统的第一层资源节点
		for(Map<String,Object> item: doing){
			//获取当前系统的第一级资源的集合
			types =  this.resourcesDAO.findList("from Resources where parent.id is null and system.id='"+system.getId()+"'");
			//如果当前系统不存在资源节点，那么执行下一次循环
			if (types.isEmpty()) continue;
			//存放当前系统的第一级资源
			List<Object> children = new ArrayList<Object>();
			for(Resources type: types){
				Map<String,Object> node = new HashMap<String,Object>();
				node.put("id",  type.getId()+"");
				node.put("text", type.getName());
				if(maps.containsKey(type.getId())){
					node.put("checked", true);
					
				}
				children.add(node);
				//在临时变量中存放当前节点
				temp.add(node);
			}
			//将获取的第一层资源作为系统根节点的孩子
			item.put("children", children.toArray(new Object[children.size()]));
		}
		//在doing中存放当前层的所有资源节点
		doing = temp;
		//循环获取每层资源节点
		while(!doing.isEmpty()){
			//每次循环将临时变量清空，防止doing=temp死循环
			temp = new ArrayList<Map<String,Object>>();
			//循环获取当前层的下一级子节点，同上面的系统节点获取子节点
			for(Map<String,Object> item: doing){
				String pid = (String)item.get("id");
				if(pid.indexOf(ISystemService.TREE_NAME)!=-1){
					pid = pid.substring(ISystemService.TREE_NAME.length());
				}
				types = this.resourcesDAO.findList("from Resources where parent.id='"+pid+"' and system.id='"+system.getId()+"'");
				if (types.isEmpty()) continue;
				
				List<Object> children = new ArrayList<Object>();
				for(Resources type: types){
					Map<String,Object> node = new HashMap<String,Object>();
					node.put("id", type.getId()+"");
					node.put("text", type.getName());
					if(maps.containsKey(type.getId())){
						node.put("checked", true);
						
					}
					children.add(node);
					
					temp.add(node);
				}
				item.put("children", children.toArray(new Object[children.size()]));
			}
			//在doing中存放当前层的所有资源节点
			doing = temp;
		}
		return nodes;
	}
	
	public void saveResActions(String roleId,String resActions){
		List<RoleAuth> roleAuth = this.roleAuthDao.findList("from RoleAuth where roleId ='"+roleId+"'");
		if(roleAuth!=null && roleAuth.size()>0){
			for(Iterator<RoleAuth> iter=roleAuth.iterator();iter.hasNext();){
				RoleAuth temp = iter.next();
				temp.setActions("");
				this.roleAuthDao.alter(temp);
			}
		}
		if(resActions!=null && !"".equals(resActions)){
			String actions[] = resActions.split(";");
			for(int i=0;i<actions.length;i++){
				String resourceId = actions[i].substring(0,actions[i].indexOf(":"));
				Resources resource = this.resourcesDAO.findById(resourceId);
				String temp = actions[i].substring(0, actions[i].length()).replaceAll(resourceId, resource.getEnname());
				RoleAuth auth = this.roleAuthDao.find("from RoleAuth where roleid='"+roleId+"' and resourceid='"+resourceId+"'");
				if(auth==null){
					auth = new RoleAuth();
					auth.setResourceId(resourceId);
					auth.setRoleId(roleId);
					auth.setActions(temp);
					this.roleAuthDao.save(auth);
				}else{
					auth.setActions(temp);
					this.roleAuthDao.alter(auth);
				}
//				this.executeSql("update c_roleauth set actions='"+temp+
//						"' where roleid="+roleId+" and resourceid="+resourceId);
			}
		}
	}

	@Override
	public void saveResActionsTree(List<RoleAuth> list) {
		for(RoleAuth rh:list){
			this.roleAuthDao.alter(rh);
		}
	}


}
