package com.wie.permissions.biz;

import java.util.List;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.service.ServiceInterface;
import com.wie.permissions.model.Arguments;


/** 
  * @ClassName: IArgumentsService 
  * @Description: 全局参数管理接口 
  *  
  */
public interface IArgumentsService extends ServiceInterface<Arguments> {

	/** 
	  * @Title: getArgumentsList 
	  * @Description: TODO(这里用一句话描述这个方法的作用) 
	  * @param @param argument
	  * @param @param pageNo
	  * @param @param pageSize
	  * @param @param sort 排序字段
	  * @param @param order 排序顺序
	  * @param @return
	  * @return Pagination
	  * @throws 
	  */
	public Pagination getArgumentsList(Arguments argument,int pageNo,int pageSize,String sort,String order);
	
	/** 
	  * @Title: getMaxOrder 
	  * @Description: 获得数据库中的最大排序值 
	  * @param @return
	  * @return int
	  * @throws 
	  */
	public int getMaxOrder();
	
	/** 
	  * @Title: getAllArguments 
	  * @Description: 获取所有全局参数
	  * @param @return
	  * @return List<Arguments>
	  * @throws 
	  */
	public List<Arguments> getAllArguments();
}
