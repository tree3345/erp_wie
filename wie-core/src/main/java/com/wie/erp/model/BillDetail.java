package com.wie.erp.model;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "JN_BillDetail")
public class BillDetail {

	private String billEntry=UUID.randomUUID().toString();
	
	private Bill bill ;
	
	private Product product;
	
	private BigDecimal quantity;
	
	private String remark;
	
	private BigDecimal price;
	
	
	private Profit profit;
	
	private Double discount;

	@Id
	@GeneratedValue(generator = "paymentableGenerator") 
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
    @Column(name = "billEntry", insertable = true, updatable = true, nullable = false)
	public String getBillEntry() {
		return billEntry;
	}

	public void setBillEntry(String billEntry) {
		this.billEntry = billEntry;
	}

	


	@Column(name="quantity",length=50)
	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	@Column(name="remark",length=50)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="price",length=50)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	 @OneToOne
	 @JoinColumn(name ="billEntry")
	public Profit getProfit() {
		return profit;
	}

	public void setProfit(Profit profit) {
		this.profit = profit;
	}

	@Column(name="discount",length=50)
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	@ManyToOne
    @JoinColumn(name="billId", nullable=false)
	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="productId", nullable=false)
	@NotFound(action=NotFoundAction.IGNORE) 
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BillDetail(String billEntry, Bill bill, Product product,
			BigDecimal quantity, String remark, BigDecimal price, Double discount) {
		super();
		this.billEntry = billEntry;
		this.bill = bill;
		this.product = product;
		this.quantity = quantity;
		this.remark = remark;
		this.price = price;
		this.discount = discount;
	}

	public BillDetail() {
		super();
		// TODO Auto-generated constructor stub
	}


	
}
