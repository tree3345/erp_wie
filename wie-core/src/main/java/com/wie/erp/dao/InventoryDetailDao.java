package com.wie.erp.dao;

import java.math.BigDecimal;
import java.util.List;

import com.wie.common.tools.util.ObjectUtils;
import org.springframework.stereotype.Repository;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.model.InventoryDetail;
import com.wie.framework.dao.hibernate.Finder;
import com.wie.framework.dao.hspring.BaseDAO;

/** 
  * @ClassName: InventoryDetailDao 
  * @Description:
  *  
  */
@Repository
public class InventoryDetailDao extends BaseDAO<InventoryDetail> {
	
	/** 
	  * @Title: getPageList 
	  * @Description: 获取具体某页的信息
	  * @param @param sys 一条具体Systems对象的信息
	  * @param @param page 当前页
	  * @param @param rows 一页显示多少记录
	  * @param @return
	  * @return Pagination
	  * @throws 
	  */
	public List getPageList(InventoryDetail inventoryDetail,int page, int rows,String sort,String order) {
		StringBuffer sb = new StringBuffer(
                "select product.productId,product.productName,product.unit,sum(damageQuantity) as dq ,sum(damageSum) as dqsum from InventoryDetail  where inventory.status=1 ");

		if (inventoryDetail != null) {
			if (inventoryDetail.getProduct() != null)

				if (inventoryDetail.getProduct().getProductName() != null
						&& !"".equals(inventoryDetail.getProduct().getProductName())) {
					sb.append(" and product.productName like '%"+ inventoryDetail.getProduct().getProductName()+ "%'");
				}
            
			if (inventoryDetail.getInventory() != null)
				if ((inventoryDetail.getInventory().getStartTime() != null && !""
						.equals(inventoryDetail.getInventory().getStartTime()))) {
					sb.append(" and inventory.checkDate>'"+ inventoryDetail.getInventory().getStartTime()+ "'");
				}
				if ((inventoryDetail.getInventory().getEndTime() != null && !""
						.equals(inventoryDetail.getInventory().getEndTime()))) {
					sb.append(" and inventory.checkDate<'"+ inventoryDetail.getInventory().getEndTime() + "'");
				}
	
				if (inventoryDetail.getInventory().getStoreId() != null
						&& !"".equals(inventoryDetail.getInventory().getStoreId())) {
					sb.append(" and inventory.storeId in("+ inventoryDetail.getInventory().getStoreId() + ")");
				}
		}
		//分组、排序
		sb.append(" group by product.productId,product.productName,product.unit");
		if (sort != null && !"".equals(sort)) {
			sb.append(" order by " + sort);
			if (order != null && !"".equals(order)) {
				sb.append(" " + order);
			} else {
				sb.append(" desc");
			}
			
		}
		return  super.getHandler().findListOfObj(sb.toString(), page, rows);/*(Finder.create(sb.toString()), page, rows);*/
	}
	
	/** 
	  * @Title: getAll 
	  * @Description: 获取所有的系统信息
	  * @param @return
	  * @return List<Systems>
	  * @throws 
	  */
	public List<InventoryDetail> getAll(){
		StringBuffer sb = new StringBuffer("from InventoryDetail where 1=1");
		return this.getHandler().findListOfObj(sb.toString());
	}

	public int findCountBySql(InventoryDetail inventoryDetail) {
        StringBuffer sb = new StringBuffer("select product.productId,product.productName,sum(damageQuantity) as dq "
        		+ "from InventoryDetail  where 1=1 and inventory.status=1");
		
		if (inventoryDetail != null) {

			if (inventoryDetail.getProduct() != null)
				if (inventoryDetail.getProduct().getProductName() != null
						&& !"".equals(inventoryDetail.getProduct().getProductName())) {
					sb.append(" and product.productName like '%"+ inventoryDetail.getProduct().getProductName()+ "%'");
				}
            
			if (inventoryDetail.getInventory() != null)
				if ((inventoryDetail.getInventory().getStartTime() != null && !""
						.equals(inventoryDetail.getInventory().getStartTime()))) {
					sb.append(" and inventory.checkDate>'"+ inventoryDetail.getInventory().getStartTime()+ "'");
				}
				if ((inventoryDetail.getInventory().getEndTime() != null && !""
						.equals(inventoryDetail.getInventory().getEndTime()))) {
					sb.append(" and inventory.checkDate<'"+ inventoryDetail.getInventory().getEndTime() + "'");
				}
	
				if (inventoryDetail.getInventory().getStoreId() != null
						&& !"".equals(inventoryDetail.getInventory().getStoreId())) {
					sb.append(" and inventory.storeId in("+ inventoryDetail.getInventory().getStoreId() + ")");
				}
		}
		//分组、排序
		sb.append(" group by product.productId,product.productName");
		return this.findList(sb.toString()).size();
	}

    public Pagination getPageDetailList(InventoryDetail inventoryDetail, Integer page, Integer rows, String sort, String order) {
        StringBuffer sb = new StringBuffer("from InventoryDetail d where d.inventory.status=1 ");
        if(null != inventoryDetail){
            if(null != inventoryDetail.getProduct()){
                sb.append(" and d.product.productId ='" + inventoryDetail.getProduct().getProductId() + "' ");
            }

            if (inventoryDetail.getInventory() != null) {
                if(inventoryDetail.getInventory().getCreateBy()!=null){
                    sb.append(" and inventory.createBy.name like '%"+inventoryDetail.getInventory().getCreateBy().getName()+"%'");
                }
                if ((inventoryDetail.getInventory().getStartTime() != null && !""
                        .equals(inventoryDetail.getInventory().getStartTime()))) {
                    sb.append(" and inventory.checkDate>'" + inventoryDetail.getInventory().getStartTime() + "'");
                }
                if ((inventoryDetail.getInventory().getEndTime() != null && !""
                        .equals(inventoryDetail.getInventory().getEndTime()))) {
                    sb.append(" and inventory.checkDate<'" + inventoryDetail.getInventory().getEndTime() + "'");
                }

                if (inventoryDetail.getInventory().getStoreId() != null
                        && !"".equals(inventoryDetail.getInventory().getStoreId())) {
                    sb.append(" and inventory.storeId in(" + inventoryDetail.getInventory().getStoreId() + ")");
                }
            }
        }


        //排序
        if(sort!=null && !"".equals(sort)){
            sb.append(" order by "+sort);
            if(order!=null && !"".equals(order)){
                sb.append(" "+order);
            }else{
                sb.append(" desc");
            }
        }
        return super.getHandler().getPageList(Finder.create(sb.toString()), page, rows);
    }

    public BigDecimal findInventoryByProductId(String productId ,String startTime,String endTime,String storeId) {
        StringBuffer sb = new StringBuffer("select product.productId,sum(damageSum) as dqsum "
                + "from InventoryDetail  where inventory.status=1");

            if (productId != null
                    && !"".equals(productId)) {
                sb.append(" and product.productId = '"+ productId+ "'");
            }

                if (startTime!= null && !"".equals(startTime)) {
                    sb.append(" and inventory.checkDate>'"+ startTime+ "'");
                }
            if (endTime != null && !"" .equals(endTime)) {
                sb.append(" and inventory.checkDate<'"+ endTime + "'");
            }

            if (storeId != null && !"".equals(storeId)) {
                sb.append(" and inventory.storeId='"+storeId + "'");
            }
        //分组、排序
        sb.append(" group by product.productId,product.productName");
        List list=this.findList(sb.toString());
        if(list.size()>0)
        {
            Object[] obj =(Object[]) list.get(0);
            return  new BigDecimal(obj[1]==null?"0":obj[1]+"");
        }else{
            return new BigDecimal(0);
        }

    }
}
