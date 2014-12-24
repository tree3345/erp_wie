package com.wie.erp.model;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="JN_ProductPrice")
public class ProductPrice {

	private String productId =UUID.randomUUID().toString();
	
	private BigDecimal salesPrice;
	
	private BigDecimal costPrice;
	
	private BigDecimal purchasePrice;
	
	private BigDecimal orderPrice;
	
	private String priceStatus;
	
	

	 @Id
	 @GeneratedValue(generator = "paymentableGenerator")
     @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	 @Column(name = "productId", insertable = true, updatable = true, nullable = false)
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Column(name="salesPrice",length=50)
	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}

	@Column(name="costPrice",length=50)
	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
	@Column(name="purchasePrice",length=50)
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	@Column(name="orderPrice",length=50)
	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	@Transient
	public String getPriceStatus() {
		return priceStatus;
	}

	public void setPriceStatus(String priceStatus) {
		this.priceStatus = priceStatus;
	}

}
