package com.wie.erp.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.dao.CategoryDao;
import com.wie.erp.dao.IntercourseTypeDao;
import com.wie.erp.dao.ProductClassDao;
import com.wie.erp.model.Category;
import com.wie.erp.model.CloneProductClass;
import com.wie.erp.model.IntercourseType;
import com.wie.erp.model.ProductClass;
import com.wie.erp.model.ProductPrice;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.tree.Node;
import com.wie.tree.TreeDirector;
import com.wie.tree.TreeModel;
import com.wie.tree.UncodeException;
import com.wie.tree.UserDataUncoder;
import com.wie.tree.easyui.EasyuiTreeBuilder;
import com.wie.tree.support.AbstractWebTreeModelCreator;
import com.wie.tree.support.DefaultNodeComparator;
import com.wie.tree.support.DefaultTreeDirector;
import com.wie.tree.support.DefaultTreeModel;
import com.wie.tree.support.ReverseComparator;
import com.wie.tree.support.WebTreeNode;


@Service
@Transactional
public class IntercourseTypeService extends BaseService<IntercourseType> implements IIntercourseTypeService {
	@Autowired
	private IntercourseTypeDao intercourseTypeDao;
	public Pagination getPageList(IntercourseType intercourseType, int page, int rows,String sort,String order) {
		return intercourseTypeDao.getPageList(intercourseType, page, rows,sort,order);
	}

	@Override
	protected DAOInterface<IntercourseType> getDAO() {
		return intercourseTypeDao;
	}

	

	public IntercourseTypeDao getIntercourseTypeDao() {
		return intercourseTypeDao;
	}

	public void setIntercourseTypeDao(IntercourseTypeDao intercourseTypeDao) {
		this.intercourseTypeDao = intercourseTypeDao;
	}

	

	public boolean saveAll(String updated) {
		boolean flag=false;
		if(updated != null){
			System. err.println("updated "+updated);
			
			JsonConfig jsonConfig = new JsonConfig();  //建立配置文件
			jsonConfig.setIgnoreDefaultExcludes(false);  //设置默认忽略
			//将所需忽略字段加到数组中，如“productNo”，“productName”.
			jsonConfig.setExcludes(new String[]{"editing"}); 
			
			JSONArray jsonArr = JSONArray.fromObject(updated, jsonConfig);
			System. err.println("filter "+jsonArr);
			
			List<IntercourseType> updatelist = JSONArray.toList(jsonArr, IntercourseType.class);
			System.err.println(updatelist.size());
			for(int i=0;i<updatelist.size();i++)
			{
				IntercourseType pd=updatelist.get(i);
					flag=intercourseTypeDao.alter(pd);
			}
		}
		return flag;
	}

	public List getAll() {
		String sql="select id,name from jn_intercourseType";
		return intercourseTypeDao.executebySql(sql);
	}
}
