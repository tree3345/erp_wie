package com.wie.permissions.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.framework.dao.hspring.BaseDAO;
import com.wie.framework.dao.hspring.handler.IHandler;
import com.wie.permissions.model.Actions;

/** 
  * @ClassName: ActionsDAO 
  * @Description: 操作管理DAO 
  *  
  */
@Repository(value="actionsDAO")
public class ActionsDAO extends BaseDAO<Actions> {

	/** 
	  * @Title: getMaxOrder 
	  * @Description:获取操作信息表中的排序最大值 
	  * @param @return
	  * @return int
	  * @throws 
	  */
	public int getMaxOrder(){
		IHandler handler = this.getHandler();
		Object result = handler.findObj("select max(orderid) from Actions ");
		if(result!=null){
			return (Integer)result;
		}else{
			return 0;
		}
		
	}
	
	/** 
	  * @Title: getAll 
	  * @Description: 获得所有记录 
	  * @param @return
	  * @return List<Actions>
	  * @throws 
	  */
	public List<Actions> getAll(){
		return this.findList("from Actions");
	}
}
