package com.wie.erp.model;

import java.math.BigDecimal;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



/** 
  * @classname:JN_Purchase
  * @Description: 入库明细实体类 
  * @author liudehai
  */
@Entity
@Table(name="JN_PurchaseDetail")
public class PurchaseDetail  implements java.io.Serializable {


     /*
      * 明细主键id
      */
     private String id =UUID.randomUUID().toString();    
     /*
      * 商品id
      */
     private Product product ;
     /*
      * 入库量
      */
     private  BigDecimal quantity;
     /*
      * 单价
      */
     private BigDecimal price;
     
     private String orderId;
     
    

	private Purchase purchase;
/**
 * 
 * @param id
 * @param storeId
 */

     @Id
     @GeneratedValue(generator = "paymentableGenerator") 
     @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	 @Column(name = "id", insertable = true, updatable = true, nullable = false)
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
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
	@JoinColumn(name="purchaseId")
	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}


	@Column(name="quantity", nullable=false, length=36)
	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	@Column(name="price", nullable=false, length=36)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	 @Column(name="orderId", length=32)
     public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}