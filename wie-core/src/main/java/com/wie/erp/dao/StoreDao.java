package com.wie.erp.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.Store;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;
import com.wie.framework.dao.hspring.handler.IHandler;

/** 
  * @ClassName: ActionsDAO 
  * @Description: 操作管理DAO 
  *  
  */
@Repository(value="storeDao")
public class StoreDao extends BaseDAO<Store> {

	
	
	/** 
	  * @Title: getAll 
	  * @Description: 获得所有记录 
	  * @param @return
	  * @return List<Actions>
	  * @throws 
	  */
	public List<Store> getAllStore(){
		return this.findList("from Store");
	}

	public Pagination getPageList(Store store, int page, int rows, String sort,
			String order) {
		StringBuffer sb = new StringBuffer("from Store where 1=1 ");
		if(null != store){
			if(null != store.getStoreName() && !"".equals(store.getStoreName())){
				sb.append("and storeName like '%" +store.getStoreName() + "%' ");
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

	public String storeIdsByUserId(String userid) {
		 List<Store> list=this.findList("select s from Store as s left join s.users as u where u.id='"+userid+"'");
		String storeIds="";
		if(list.size()>0) {
			for(int i=0;i<list.size();i++) {
				if(i!=0)
					storeIds+=",";
				storeIds += "'" + list.get(i).getStoreId() + "'";
			}
		}
		return storeIds;
	}

	public List getStores(String storeIds) {
		String hql="from Store s where 1=1";
		if(storeIds!=null&&!"".equals(storeIds)){
			String[] ids=storeIds.split(",");
			String sids="";
			for(int i=0;i<ids.length;i++){
				if(i!=0){
					sids+=",";
				}
				sids+="'"+ids[i]+"'";
			}
			hql+=" and s.storeId not in("+sids+")";
		}
		List list=this.findList(hql);
		return list;
	}

	public String getUserStores(String userId, String storeId) {
		String storeIds="";
		String hql="select s from Store as s left join s.users as u where u.id='"+userId+"'";
		if(storeId!=null&&!"".equals(storeId)) hql+=" and s.storeId='"+storeId+"'";
		List<Store> storelist=this.findList(hql);
		for(int i=0;i<storelist.size();i++){
			if(i!=0)
				storeIds+=",";
			storeIds+="'"+storelist.get(i).getStoreId()+"'";
		}
		return storeIds;
	}
}
