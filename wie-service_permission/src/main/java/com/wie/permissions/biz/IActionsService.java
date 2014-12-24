package com.wie.permissions.biz;

import java.util.List;
import java.util.Map;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.service.ServiceInterface;
import com.wie.permissions.model.Actions;


/** 
  * @ClassName: IActionsService 
  * @Description: 操作管理服务接口 
  *  
  */
public interface IActionsService extends ServiceInterface<Actions> {

	/** 
	  * @Title: getActionsList 
	  * @Description: TODO(这里用一句话描述这个方法的作用) 
	  * @param @param actions
	  * @param @param pageNo
	  * @param @param pageSize
	  * @param @param sort 排序字段
	  * @param @param order 排序顺序
	  * @param @return
	  * @return Pagination
	  * @throws 
	  */
	public Pagination getActionsList(Actions actions,int pageNo,int pageSize,String sort,String order);
	
	/** 
	  * @Title: getMaxOrder 
	  * @Description: 获得数据库中记录最大排序值
	  * @param @return
	  * @return int
	  * @throws 
	  */
	public int getMaxOrder();
	
	/** 
	  * @Title: getAll 
	  * @Description: 获得操作信息的所有记录
	  * @param @return
	  * @return List<Actions>
	  * @throws 
	  */
	public List<Actions> getAll();
	
	/** 
	  * @Title: getAllListTrees 
	  * @Description:获得所有树节点。
	  * @param @param actionId
	  * @param @return
	  * @return List<Map<String,Object>>
	  * @throws 
	  */
	public List<Map<String,Object>> getAllListTrees(String actionId);
	/** 
	  * @Title: saveAuthorizate 
	  * @Description: 保存节点
	  * @param @param actionId
	  * @param @param resourcesIds
	  * @return void
	  * @throws 
	  */
	public void saveAuthorizate(String actionId,String resourcesIds);
}
