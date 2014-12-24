/**   
  * @Title: ResourcesDAO.java 
  * @Description:
  * @version V1.0
  */

package com.wie.permissions.dao;

import java.util.List;


import org.springframework.stereotype.Repository;

import com.wie.framework.dao.hspring.BaseDAO;
import com.wie.framework.dao.hspring.handler.IHandler;
import com.wie.permissions.model.Resources;

/** 
 * @ClassName: ResourcesDAO 
 * @Description: 处理资源信息模块的DAO 
 */
@Repository(value="resourcesDAO")
public class ResourcesDAO extends BaseDAO<Resources> {

	/** 
	  * @Title: getMaxOrder 
	  * @Description:获取资源信息表中的排序最大值 
	  * @param @return
	  * @return int
	  * @throws 
	  */
	public int getMaxOrder(){
		IHandler handler = this.getHandler();
		Object result = handler.findObj("select max(orderid) from Resources ");
		if(result!=null){
			return (Integer)result;
		}else{
			return 0;
		}
		
	}
	
	
	
	/** 
	  * @Title: getTree 
	  * @Description: 获得单个系统的树HTML代码 
	  * @param @param request
	  * @param @param system
	  * @param @return
	  * @return String
	  * @throws 
	  */
	
	
	/** 
	  * @Title: getAll 
	  * @Description: 获得所有资源记录集合
	  * @param @return
	  * @return List<Resources>
	  * @throws 
	  */
	public List<Resources> getAll(){
		return this.findList("from Resources");
	}
}
