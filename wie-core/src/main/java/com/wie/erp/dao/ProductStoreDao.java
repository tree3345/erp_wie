package com.wie.erp.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Product;
import com.wie.erp.model.ProductStore;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: ActionsDAO 
  * @Description: 操作管理DAO 
  *  
  */
@Repository(value="productStoreDao")
public class ProductStoreDao extends BaseDAO<ProductStore> {


	
	/** 
	  * @Title: getAll 
	  * @Description: 获得所有记录 
	  * @param @return
	  * @return List<Actions>
	  * @throws 
	  */

	public Pagination getPageList(ProductStore product, int page, int rows,
			String sort, String order) {
		StringBuffer sb = new StringBuffer("from ProductStore where status=1 ");
		if(null != product){
			if(null != product.getProductName() && !"".equals(product.getProductName())){
				sb.append("and productName like '%" + product.getProductName() + "%' ");
				sb.append("or spelling like '%" + product.getProductName() + "%' ");
			}
		}
		if(null != product){
			if(null != product.getStoreId() && !"".equals(product.getStoreId())){
				sb.append("and storeId = '"+product.getStoreId()+"' ");
			}
		}
		List<String> strlist=this.executebySql("select c.classId from jn_class c where c.parentid='"+product.getClassId()+"'");
		List<String> formatterlist=new ArrayList<String>();
		for(int i=0;i<strlist.size();i++)
		{
			formatterlist.add("'"+strlist.get(i)+"'");
		}
		String ids=formatterlist.toString().replace("[", "(").replace("]", ")");
		System.err.println(ids);
		
		if(null != product){
			if(null != product.getClassId()&& !"".equals(product.getClassId())&&!"null".equals(product.getClassId())){
				if(strlist.size()>0){
					sb.append("and classId in " + ids);
				}
				else{
				sb.append("and classId = '" + product.getClassId()+ "' ");
				}
			}
		}
		
		if(sort!=null && !"".equals(sort)){
			sb.append(" order by "+sort);
			if(order!=null && !"".equals(order)){
				sb.append(" "+order);
			}else{
				sb.append(" desc");
			}
		}
		return this.getHandler().getPageList(Finder.create(sb.toString()), page, rows);
	}

	public List<ProductStore> getAllProduct() {
		return this.findList("from ProductStore");
	}

	public List<ProductStore> getAll(){
		StringBuffer sb = new StringBuffer("from ProductStore");
		return this.getHandler().findListOfObj(sb.toString());
	}


	public boolean alterToClass(ProductStore product, String classId) {

		/*Set<ProductClass> set = new HashSet<ProductClass>(); 
		set.add((ProductClass)this.getHandler().findObj("from ProductClass where classId='" + classId + "'"));
		if(this.getHandler().alterObj(product)){
			product.setProductClass(set);
			return true;
		}
		*/
		return false;
	}
   
}
