package com.wie.erp.model;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



/** 
  * @classname:JN_Purchase
  * @Description: 盘库明细实体类 
  */
@Entity
@Table(name="JN_InventoryDetail")
public class InventoryDetail  implements java.io.Serializable {


     /**
      * 明细主键id
      */
     private String id =UUID.randomUUID().toString();    
     /**
      * 入库id
      */
     /**
      * 商品id
      */
     private Product product;
     /**
      * 仓库数量
      */
     private BigDecimal warehouseQuantity;
     /**
      * 盘库数量
      */
     private BigDecimal inventoryQuantity;
     /**
      * 货损数量
      */
     private BigDecimal damageQuantity;
    /**
     * 盘点时价格
     */
    private BigDecimal price;
     /**
      * 货损金额
      */
    private BigDecimal damageSum;
    /**
     * 货损主表
     */
	 private Inventory inventory;

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
	
	@Column(name="warehouseQuantity", nullable=false, length=36)
	public BigDecimal getWarehouseQuantity() {
		return warehouseQuantity;
	}

	public void setWarehouseQuantity(BigDecimal warehouseQuantity) {
		this.warehouseQuantity = warehouseQuantity;
	}

	@Column(name="inventoryQuantity", nullable=false, length=36)
	public BigDecimal getInventoryQuantity() {
		return inventoryQuantity;
	}

	public void setInventoryQuantity(BigDecimal inventoryQuantity) {
		this.inventoryQuantity = inventoryQuantity;
	}

    @Column(name="price", nullable=false, length=36)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name="damageQuantity", length=36)
	public BigDecimal getDamageQuantity() {
		return damageQuantity;
	}

	public void setDamageQuantity(BigDecimal damageQuantity) {
		this.damageQuantity = damageQuantity;
	}
	@Column(name="damageSum", length=36)
	public BigDecimal getDamageSum() {
		return damageSum;
	}

	public void setDamageSum(BigDecimal damageSum) {
		this.damageSum = damageSum;
	}

	@ManyToOne
	@JoinColumn(name="productId")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne
	@JoinColumn(name="inventoryId")
	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

}