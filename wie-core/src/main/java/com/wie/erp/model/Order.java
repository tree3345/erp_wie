package com.wie.erp.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.wie.permissions.model.Users;

@Entity
@Table(name = "JN_Order")
public class Order {

	private String orderId=UUID.randomUUID().toString();
	
	private String storeId;
	
	private String orderNumber;
	
	private BigDecimal totalPrice;
	
	private Double totalCount;
	
	private Users createBy;
	
	private Date createdt;
	
	private String remark;
	
	private Users checkBy;
	
	private Date checkDate;
	
	private Integer status;
	
	private Intercourse intercourse;
	
	private String flag;
	

	@Id
	@GeneratedValue(generator = "paymentableGenerator") 
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
    @Column(name = "orderId", nullable = false)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Column(name="storeId", length=100)
	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Column(name="orderNumber", length=100)
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Column(name="totalPrice",  length=100)
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Column(name="totalCount", length=100)
	public Double getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Double totalCount) {
		this.totalCount = totalCount;
	}

	@Column(name="createdt", nullable=false, length=100)
	public Date getCreatedt() {
		return createdt;
	}

	public void setCreatedt(Date createdt) {
		this.createdt = createdt;
	}

	@Column(name="remark", length=100)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="checkDate", length=100)
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Column(name="status",  length=100)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="createBy", nullable=false)
	@NotFound(action=NotFoundAction.IGNORE) 
	public Users getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Users createBy) {
		this.createBy = createBy;
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

	/*@Column(name="intercourseId",  length=100)
	public String getIntercourseId() {
		return intercourseId;
	}

	public void setIntercourseId(String intercourseId) {
		this.intercourseId = intercourseId;
	}*/
	@ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, optional = true)  
    @JoinColumn(name="intercourseId")
	public Intercourse getIntercourse() {
		return intercourse;
	}

	public void setIntercourse(Intercourse intercourse) {
		this.intercourse = intercourse;
	}

	

	public Order(String orderId, String storeId, String orderNumber,
			BigDecimal totalPrice, Double totalCount, Users createBy,
			Date createdt, String remark, Users checkBy, Date checkDate,
			Integer status, Intercourse intercourse, String flag) {
		super();
		this.orderId = orderId;
		this.storeId = storeId;
		this.orderNumber = orderNumber;
		this.totalPrice = totalPrice;
		this.totalCount = totalCount;
		this.createBy = createBy;
		this.createdt = createdt;
		this.remark = remark;
		this.checkBy = checkBy;
		this.checkDate = checkDate;
		this.status = status;
		this.intercourse = intercourse;
		this.flag = flag;
	}

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}
	
    @Transient
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
