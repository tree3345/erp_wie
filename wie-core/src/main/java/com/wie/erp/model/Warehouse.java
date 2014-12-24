package com.wie.erp.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.wie.common.poi.excel.annotation.Excel;



/** 
  * @classname:JN_Warehouse
  * @Description: 操作管理实体类 
  * @author liudehai
  */
@Entity
@Table(name="JN_Warehouse")
public class Warehouse  implements java.io.Serializable {


    // Fields    

     private String warehouseId= UUID.randomUUID().toString();    //入库id
	private String storeId;
	
     
     @Id
	 @GeneratedValue(generator = "paymentableGenerator")
	 @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	 @Column(name = "warehouseId", insertable = true, updatable = true, nullable = false)
     public String getWarehouseId() {
 		return warehouseId;
 	}
     public void setWarehouseId(String warehouseId) {
 		this.warehouseId = warehouseId;
 	}
 	

    @Column(name="storeId", nullable=false, length=32)
     public String getStoreId() {
  		return storeId;
  	}
    public void setStoreId(String storeId) {
   		this.storeId = storeId;
   	}
}