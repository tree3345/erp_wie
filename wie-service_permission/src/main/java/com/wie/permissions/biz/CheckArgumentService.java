package com.wie.permissions.biz;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.StoreDao;
import com.wie.erp.model.Store;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.permissions.dao.CheckArgumentDAO;
import com.wie.permissions.model.Arguments;
import com.wie.permissions.model.CheckArgument;


/** 
  * @ClassName: ArgumentsService 
  * @Description: 全局参数管理服务类 
  *  
  */
@Service
public class CheckArgumentService extends BaseService<CheckArgument> implements
		ICheckArgumentService {

	@Resource(name="checkArgumentDAO")
	private CheckArgumentDAO checkArgumentDAO;
	
	protected DAOInterface<CheckArgument> getDAO() {
		return checkArgumentDAO;
	}

	public Pagination getPageList(CheckArgument checkArgument, int page, int rows,String sort,String order) {
		return checkArgumentDAO.getPageList(checkArgument, page, rows,sort,order);
	}

	@Override
	public boolean saveAll(Map<String, String> datamap) {
		String inserted=datamap.get("inserted");
		String updated=datamap.get("updated");
		String deleted=datamap.get("deleted");
		boolean flag=false;
		JsonConfig jsonConfig = new JsonConfig();  //建立配置文件
		jsonConfig.setIgnoreDefaultExcludes(false);  //设置默认忽略
		jsonConfig.setExcludes(new String[]{"status","editing"});  
		//批量保存
		if(inserted != null){
			JSONArray jsonArr = JSONArray.fromObject(inserted, jsonConfig);
			List<CheckArgument> insertedlist = JSONArray.toList(jsonArr, CheckArgument.class);
			for(int i=0;i<insertedlist.size();i++)
			{
				CheckArgument pd=insertedlist.get(i);
				flag=checkArgumentDAO.save(pd);
			}
		}
		//批量更新
		if(updated != null){
			JSONArray jsonArru = JSONArray.fromObject(updated, jsonConfig);
			List<CheckArgument> updatelist = JSONArray.toList(jsonArru, CheckArgument.class);
			for(int i=0;i<updatelist.size();i++)
			{
				CheckArgument pd=updatelist.get(i);
				flag=checkArgumentDAO.alter(pd);
			}
		}
        
		if(deleted != null){
			JSONArray jsonArrd = JSONArray.fromObject(deleted, jsonConfig);
			List<CheckArgument> deletedlist = JSONArray.toList(jsonArrd, CheckArgument.class);
			for(int i=0;i<deletedlist.size();i++)
			{
				CheckArgument pd=deletedlist.get(i);
				flag=checkArgumentDAO.delete(pd);
			}
		}
		return flag;
	}

	@Override
	public List getStores() {
		String sqlStr = "select storeId,storeName from dbo.JN_store";
		return checkArgumentDAO.executebySql(sqlStr);
	}

}
