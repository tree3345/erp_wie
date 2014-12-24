package com.wie.erp.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wie.panelClient.model.Employee;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "JN_Bill")
public class Bill {

	private String billId=UUID.randomUUID().toString();
	
	private String billNo ;
	
	private Date billDateTime;
	
	private String startTime;
	
	private String endTime;
	
	private String memberId;
	
	private Double sumMoney;
	
	private String storeId;
	
	private Employee createBy;
	
	private Boolean isUpload;
	
	private String remark;
	
	private Double saleMoney;
	
	private Boolean uploadTerminal;

	@Id
	@GeneratedValue(generator = "paymentableGenerator") 
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
    @Column(name = "billId", insertable = true, updatable = true, nullable = false)
	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	@Column(name="billNo",length=50)
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	@Column(name="billDateTime",length=50)
	public Date getBillDateTime() {
		return billDateTime;
	}

	public void setBillDateTime(Date billDateTime) {
		this.billDateTime = billDateTime;
	}

	@Column(name="memberId",length=50)
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	@Column(name="sumMoney",length=50)
	public Double getSumMoney() {
		return sumMoney;
	}

	
	public void setSumMoney(Double sumMoney) {
		this.sumMoney = sumMoney;
	}

	@Column(name="storeId",length=50)
	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}


	@Column(name="isUpload",length=5)
	public Boolean getIsUpload() {
		return isUpload;
	}

	
	public void setIsUpload(Boolean isUpload) {
		this.isUpload = isUpload;
	}

	@Column(name="remark",length=50)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="saleMoney",length=50)
	public Double getSaleMoney() {
		return saleMoney;
	}

	public void setSaleMoney(Double saleMoney) {
		this.saleMoney = saleMoney;
	}

	@Column(name="uploadTerminal",length=50)
	public Boolean getUploadTerminal() {
		return uploadTerminal;
	}

	@Transient
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Transient
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setUploadTerminal(Boolean uploadTerminal) {
		this.uploadTerminal = uploadTerminal;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="createBy", nullable=false)
	@NotFound(action=NotFoundAction.IGNORE) 
	public Employee getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Employee createBy) {
		this.createBy = createBy;
	}

	public Bill(String billId, String billNo, Date billDateTime,
			String memberId, Double sumMoney, String storeId,
			Employee createBy, Boolean isUpload, String remark,
			Double saleMoney, Boolean uploadTerminal) {
		super();
		this.billId = billId;
		this.billNo = billNo;
		this.billDateTime = billDateTime;
		this.memberId = memberId;
		this.sumMoney = sumMoney;
		this.storeId = storeId;
		this.createBy = createBy;
		this.isUpload = isUpload;
		this.remark = remark;
		this.saleMoney = saleMoney;
		this.uploadTerminal = uploadTerminal;
	}


	public Bill() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
