package com.wie.erp.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.wie.permissions.model.Users;



/** 
  * @classname:JN_Purchase
  * @Description: 操作管理实体类 
  * @author liudehai
  */
@Entity
@Table(name="JN_Payment")
public class Payment  implements java.io.Serializable {


    // Fields    

     private String paymentId =UUID.randomUUID().toString();    //入库id
     
     private String storeId;  //商店id
     
     private String code; //入库单据
     
     private BigDecimal totalPrice;//总价
     
     private BigDecimal paid;
     
     private BigDecimal unpaid;
     
     private Users inby;//入库人员
     
     private Date indt;  //入库日期
     
     private String purchaseId;//单号
     
     private Double totalCount;  //总量
 	
 	
 	private String remark;  //备注
 	
 	private Users checkBy; //审核人
 	
 	private Date checkDate;//审核日期
 	
 	private Integer status;//审核状态：0 未审核，1已审核
 	
 	private String intercourseId; //供应单位
 	
     
     
     @Id
     @GeneratedValue(generator = "paymentableGenerator") 
     @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	 @Column(name = "paymentId", insertable = true, updatable = true, nullable = false)
    public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

    
    @Column(name="storeId", nullable=false, length=50)
	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}


	 @Column(name="totalPrice", length=32)
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="inby")
	@NotFound(action=NotFoundAction.IGNORE) 
	public Users getInby() {
		return inby;
	}

	public void setInby(Users inby) {
		this.inby = inby;
	}

	@Column(name="Indt", length=32)
	public Date getIndt() {
		return indt;
	}

	public void setIndt(Date indt) {
		this.indt = indt;
	}

	@Column(name="purchaseId", length=50)
	public String getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}
	 @Column(name="totalCount", length=32)
	public Double getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(Double totalCount) {
		this.totalCount = totalCount;
	}

	 @Column(name="remark", length=200)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="checkBy")
	@NotFound(action=NotFoundAction.IGNORE) 
	public Users getCheckBy() {
		return checkBy;
	}

	public void setCheckBy(Users checkBy) {
		this.checkBy = checkBy;
	}

	@Column(name="checkDate", length=50)
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Column(name="status", length=10)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name="intercourseId", length=50)
	public String getIntercourseId() {
		return intercourseId;
	}

	public void setIntercourseId(String intercourseId) {
		this.intercourseId = intercourseId;
	}

	@Column(name="code", length=50)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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