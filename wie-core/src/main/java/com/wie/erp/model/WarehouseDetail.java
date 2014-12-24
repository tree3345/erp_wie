package com.wie.erp.model;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.wie.common.poi.excel.annotation.Excel;
import com.wie.common.poi.excel.annotation.Excel;
import com.wie.common.poi.excel.annotation.ExcelCollection;
import com.wie.common.poi.excel.annotation.ExcelEntity;
import com.wie.common.poi.excel.annotation.ExcelTarget;

/** 
  * @classname:JN_Warehouse
  * @Description: 操作管理实体类 
  * @author liudehai
  */
@Entity
@Table(name="JN_WarehouseDetail")
public class WarehouseDetail  implements java.io.Serializable {


    // Fields    
	 @Excel(exportName="库存明细标识",exportFieldWidth=50)
     private String id =UUID.randomUUID().toString();
     private Warehouse warehouse;
	 @ExcelEntity()
     private Product product;
     @Excel(exportName="数量")
     private BigDecimal quantity;
     
     @Id
     @GeneratedValue(generator = "paymentableGenerator") 
     @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	 @Column(name = "id", insertable = true, updatable = true, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@OneToOne(cascade=CascadeType.REMOVE)
	@JoinColumn(name="warehouseId")
	public Warehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	@OneToOne(cascade=CascadeType.REMOVE)
	@JoinColumn(name="productId")
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	@Column(name="quantity", nullable=false, length=32)
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
}