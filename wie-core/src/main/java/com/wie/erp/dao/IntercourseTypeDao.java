package com.wie.erp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.IntercourseType;
import com.wie.erp.model.ProductClass;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: ActionsDAO 
  * @Description: 操作管理DAO 
  *  
  */
@Repository(value="intercourseTypeDao")
public class IntercourseTypeDao extends BaseDAO<IntercourseType> {


	
	/** 
	  * @Title: getAll 
	  * @Description: 获得所有记录 
	  * @param @return
	  * @return List<Actions>
	  * @throws 
	  */
	public List<IntercourseType> getAll(){
		return this.findList("from IntercourseType");
	}

	public Pagination getPageList(IntercourseType intercourseType, int page, int rows,
			String sort, String order) {
		StringBuffer sb = new StringBuffer("from IntercourseType where 1=1 ");
		if(null != intercourseType){
			if(null != intercourseType.getName()&& !"".equals(intercourseType.getName())){
				sb.append("and name like '%" + intercourseType.getName() + "%' ");
			}
			
		}
		/*if(sort!=null && !"".equals(sort)){
			sb.append(" order by "+sort);
			if(order!=null && !"".equals(order)){
				sb.append(" "+order);
			}else{
				sb.append(" desc");
			}
		}*/
		return this.getHandler().getPageList(Finder.create(sb.toString()), page, rows);
	}

	/*public Pagination getPageList(Object[] obj, int page, int rows,
			String sort, String order) {
		String sql="select t.productid,t.productname,c.salesprice,t.unit,t.productno,t.spelling,"
				+ "s.classname from JN_product t left join JN_productprice c on t.productid=c.productid "
				+ "left join JN_class s on s.classid=t.classid";
		StringBuffer sb = new StringBuffer(sql);
			
		return this.getHandler().getPageList(Finder.create(sb.toString()), page, rows);
	}*/
	public List<ProductClass> getAllProductClass() {
		StringBuffer sb = new StringBuffer("from ProductClass");
		return this.getHandler().findListOfObj(sb.toString());
	}

	public List<Map<String, Object>> getTree(String id) {

		List<Map<String,Object>> nodes = new ArrayList<Map<String,Object>>();
		//存放每层资源的临时变量
		List<Map<String,Object>> temp = new ArrayList<Map<String,Object>>();
		//第一级资源
		List<ProductClass> types  =  this.getHandler().findListOfObj("from ProductClass where parentid is null");
		if(null == types || types.size() == 0){
			return nodes;
		}
		for(ProductClass type: types){
				Map<String,Object> node = new HashMap<String,Object>();
				node.put("id",  type.getClassId()+"");
				node.put("text", type.getClassName());
				nodes.add(node);
				temp.add(node);
		}
		//临时
		List<Map<String,Object>> doing = new ArrayList<Map<String,Object>>();
		doing.addAll(nodes);

		while(!doing.isEmpty()){
			temp = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> item: doing){
				List<Object> children = new ArrayList<Object>();
				String pid = (String)item.get("id");
				List<ProductClass> tempTypes  =  this.getHandler().findListOfObj("from ProductClass where parentid='" + pid + "'");
				for(ProductClass type: tempTypes){
					Map<String,Object> node = new HashMap<String,Object>();
					node.put("id",  type.getClassId()+"");
					node.put("text", type.getClassName());
					temp.add(node);
					children.add(node);
				}
				item.put("children", children.toArray(new Object[children.size()]));
			}
			doing = temp;
		}
		return nodes;
	
	}
}
