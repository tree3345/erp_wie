package com.wie.erp.model;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "JN_Profit")
public class Profit {

	private String billEntry=UUID.randomUUID().toString();
	
	private BigDecimal purchasePrice;
	
	private BigDecimal profitPrice;
	
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

	@Column(name="purchasePrice",length=50)
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	@Column(name="profitPrice",length=50)
	public BigDecimal getProfitPrice() {
		return profitPrice;
	}

	public void setProfitPrice(BigDecimal profitPrice) {
		this.profitPrice = profitPrice;
	}

	public Profit() {
		super();
	}
}
