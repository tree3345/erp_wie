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
@Table(name="JN_PaymentDetail")
public class PaymentDetail  implements java.io.Serializable {


     /*
      * 明细主键id
      */
     private String id =UUID.randomUUID().toString();    
     /*
      * 入库id
      */
     /*
      * 商品id
      */
     private String productId ;
     /*
      * 入库量
      */
     private  BigDecimal quantity;
     /*
      * 单价
      */
     private BigDecimal price;
     
     private BigDecimal paid;
     
     private BigDecimal unpaid;
     
     private String orderId;
     
    

	private String paymentId;
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


	@Column(name="productId", nullable=false, length=36)
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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
	

	@Column(name="paymentId", nullable=false, length=36)
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	
	@Column(name="paid", length=50)
	public BigDecimal getPaid() {
		return paid;
	}

	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}

	@Column(name="unpaid", length=50)
	public BigDecimal getUnpaid() {
		return unpaid;
	}

	public void setUnpaid(BigDecimal unpaid) {
		this.unpaid = unpaid;
	}
}