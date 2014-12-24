package com.wie.erp.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Intercourse;
import com.wie.erp.model.Product;
import com.wie.erp.model.ProductClass;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: ActionsDAO 
  * @Description: 操作管理DAO 
  *  
  */
@Repository(value="intercourseDao")
public class IntercourseDao extends BaseDAO<Intercourse> {


	
	/** 
	  * @Title: getAll 
	  * @Description: 获得所有记录 
	  * @param @return
	  * @return List<Actions>
	  * @throws 
	  */

	public Pagination getPageList(Intercourse intercourse, int page, int rows,
			String sort, String order) {
		StringBuffer sb = new StringBuffer("from Intercourse where 1=1 ");
		if(null != intercourse){
			if(null != intercourse.getShortName() && !"".equals(intercourse.getShortName())){
				sb.append("and (shortName like '%" + intercourse.getShortName() + "%' "
						      + "or code like '%" + intercourse.getCode() + "%' "
						      + "or fullName like '%" + intercourse.getFullName() + "%') ");
			}
		}
		if(null != intercourse){
			if(null != intercourse.getIntercourseTypeId() && !"".equals(intercourse.getIntercourseTypeId())){
				sb.append("and (intercourseTypeId = '" + intercourse.getIntercourseTypeId() + "'"
						+ " or  intercourseTypeId in (select id from IntercourseType where parentId='"+intercourse.getIntercourseTypeId()+"') ) ");
			}
		}
//		if(null != intercourse){
//			if(null != intercourse.getIntercourseTypeId()&&!"".equals(intercourse.getIntercourseTypeId())){
//				sb.append("and intercourse.intercourseTypeId = '" + intercourse.getIntercourseTypeId()+ "' ");
//			}
//		}
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

	public List<Intercourse> getAllProduct() {
		return this.findList("from Intercourse");
	}

	
	public List<Product> getAll(){
		StringBuffer sb = new StringBuffer("from Product");
		return this.getHandler().findListOfObj(sb.toString());
	}


	public boolean alterToClass(Product product, String classId) {

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
