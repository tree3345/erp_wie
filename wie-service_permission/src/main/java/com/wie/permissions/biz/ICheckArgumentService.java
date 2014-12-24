package com.wie.permissions.biz;


import java.util.List;
import java.util.Map;

import com.wie.common.tools.page.Pagination;
import com.wie.framework.service.ServiceInterface;
import com.wie.permissions.model.CheckArgument;


/** 
  * @ClassName: IArgumentsService 
  * @Description: 全局参数管理接口 
  *  
  */
public interface ICheckArgumentService extends ServiceInterface<CheckArgument> {

	public Pagination getPageList(CheckArgument checkArgument,int page,int rows,String sort,String order);

	public boolean saveAll(Map<String, String> datamap);

	public List getStores();
}
