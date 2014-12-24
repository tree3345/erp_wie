package com.wie.erp.biz;

import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wie.common.tools.page.Pager;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.IntercourseDao;
import com.wie.erp.model.Intercourse;
import com.wie.erp.model.Product;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;


@Service
@Transactional
public class IntercourseService extends BaseService<Intercourse> implements IIntercourseService {
	@Autowired
	private IntercourseDao intercourseDao;
	public Pagination getPageList(Intercourse intercourse, int page, int rows,String sort,String order) {
		return intercourseDao.getPageList(intercourse, page, rows,sort,order);
	}

	@Override
	protected DAOInterface<Intercourse> getDAO() {
		return intercourseDao;
	}
	public IntercourseDao getIntercourseDao() {
		return intercourseDao;
	}

	public void setIntercourseDao(IntercourseDao intercourseDao) {
		this.intercourseDao = intercourseDao;
	}
	public List executebysql(String sql)
	{
		return intercourseDao.executebySql(sql);
	}

	public boolean saveAll(String inserted, String deleted, String updated) {
		boolean flag=false;
		//批量插入
		if(inserted != null){
			System. err.println("inserted "+inserted);
			
			JsonConfig jsonConfig = new JsonConfig();  //建立配置文件
			jsonConfig.setIgnoreDefaultExcludes(false);  //设置默认忽略
			//将所需忽略字段加到数组中，如“productNo”，“productName”.
			jsonConfig.setExcludes(new String[]{"status","editing"}); 
			JSONArray jsonArr = JSONArray.fromObject(inserted, jsonConfig);
			
			List<Intercourse> insertlist = JSONArray.toList(jsonArr, Intercourse.class);
		    for(int i=0;i<insertlist.size();i++)
			{
		    	Intercourse ic=insertlist.get(i);
				flag=intercourseDao.save(ic);
			}
		}
		//批量更新
		if(updated != null){
			System. err.println("updated "+updated);
			
			JsonConfig jsonConfig = new JsonConfig();  //建立配置文件
			jsonConfig.setIgnoreDefaultExcludes(false);  //设置默认忽略
			//将所需忽略字段加到数组中，如“productNo”，“productName”.
			jsonConfig.setExcludes(new String[]{"editing"}); 
			JSONArray jsonArr = JSONArray.fromObject(updated, jsonConfig);
			
			List<Intercourse> updatelist = JSONArray.toList(jsonArr, Intercourse.class);
			
			for(int i=0;i<updatelist.size();i++)
			{
				Intercourse ic=updatelist.get(i);
				flag=intercourseDao.alter(ic);
			}
		}
		
			//批量删除
		   if(deleted != null){
				JSONArray ja =JSONArray.fromObject(deleted);
				for (int i = 0; i < ja.size(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					String id=jo.get("id").toString();
					flag=intercourseDao.deleteById(id);
					System.out.println(jo.get("productId"));
					}
			}

		return flag;
	}

}
