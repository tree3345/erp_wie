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
import com.wie.erp.dao.ProductClassDao;
import com.wie.erp.model.Category;
import com.wie.erp.model.CloneProductClass;
import com.wie.erp.model.IntercourseType;
import com.wie.erp.model.ProductClass;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.DAOInterface;
import com.wie.framework.service.BaseService;
import com.wie.permissions.model.CloneResources;
import com.wie.permissions.model.Resources;
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
public class ProductClassService extends BaseService<ProductClass> implements IProductClassService {
	@Autowired
	private ProductClassDao productClassDao;
	@Autowired
	private CategoryDao categoryDao;
	public Pagination getPageList(ProductClass productClass, int page, int rows,String sort,String order) {
		return productClassDao.getPageList(productClass, page, rows,sort,order);
	}

	@Override
	protected DAOInterface<ProductClass> getDAO() {
		return productClassDao;
	}

	public ProductClassDao getProductClassDao() {
		return productClassDao;
	}

	public void setProductClassDao(ProductClassDao productClassDao) {
		this.productClassDao = productClassDao;
	}

	public List<ProductClass> getAll() {
		return productClassDao.getAllProductClass();
	}
	
	public List<ProductClass> getAll4MsSQL() {
		return productClassDao.getAllProductClass();
	}

	public List<Map<String,Object>> getTree(String id) {
		return this.productClassDao.getTree(id);
	}

	public List<Map<String,Object>> getTree4MsSQL(String id) {
		return this.productClassDao.getTree(id);
	}
	public void delProductFromClass(String groupid, String id) {
		// TODO Auto-generated method stub
		
	}

	public void addProductToClass(String userIds, String groupid) {
		// TODO Auto-generated method stub
		
	}

	

	public List<Map<String, Object>> getTree4MsSQL(Category category, String id) {

		// 存放所有节点每一个节点信息都保存到一个Map<String,Object>中
		List<Map<String,Object>> nodes = new ArrayList<Map<String,Object>>();
		if(category==null){
			return new ArrayList<Map<String,Object>>();
		}
		// 获取系统一级节点
		Map<String,Object> root = new HashMap<String,Object>();
		root.put("id", ICategoryService.TREE_NAME+category.getCategoryId());
		root.put("text", category.getCategoryName());
		nodes.add(root);//将系统节点放入
		
		// 存放每层资源节点
		List<Map<String,Object>> doing = new ArrayList<Map<String,Object>>();
		doing.addAll(nodes);
		
		List<ProductClass> types ;
		//存放每层资源节点的临时变量
		List<Map<String,Object>> temp = new ArrayList<Map<String,Object>>();
		//一次循环系统节点，获取每个系统的第一层资源节点
		for(Map<String,Object> item: doing){
			//获取当前系统的第一级资源的集合
			types =  this.productClassDao.findList("from ProductClass where parent.classId is null and category.categoryId='"+category.getCategoryId()+"' order by sort");
			//如果当前系统不存在资源节点，那么执行下一次循环
			if (types.isEmpty()) continue;
			//存放当前系统的第一级资源
			List<Object> children = new ArrayList<Object>();
			for(ProductClass type: types){
				if(id!=null&&type.getClassId()==id){
					continue;
				}
				Map<String,Object> node = new HashMap<String,Object>();
				node.put("id",  type.getClassId()+"");
				node.put("text", type.getClassName());
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
				if(pid.indexOf(ICategoryService.TREE_NAME)!=-1){
					pid = pid.substring(ICategoryService.TREE_NAME.length());
				}
				types = this.productClassDao.findList("from ProductClass where parent.classId='"+pid+"' and category.categoryId='"+category.getCategoryId()+"' order by sort");
				if (types.isEmpty()) continue;
				
				List<Object> children = new ArrayList<Object>();
				for(ProductClass type: types){
					if(id!=null&&type.getClassId()==id){
						continue;
					}
					Map<String,Object> node = new HashMap<String,Object>();
					node.put("id", type.getClassId()+"");
					node.put("text", type.getClassName());
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
			
			List<ProductClass> updatelist = JSONArray.toList(jsonArr, ProductClass.class);
			System.err.println(updatelist.size());
			for(int i=0;i<updatelist.size();i++)
			{
				ProductClass pd=updatelist.get(i);
					flag=productClassDao.alter(pd);
			}
		}
		return flag;
	}
}
