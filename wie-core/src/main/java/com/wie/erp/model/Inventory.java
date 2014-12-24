package com.wie.erp.model;

import java.math.BigDecimal;
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
@Table(name="JN_Inventory")
public class Inventory  implements java.io.Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = -4431456304797396029L;
	// Fields    

     private String inventoryId =UUID.randomUUID().toString();    
     private String code;
     private String storeId;  
     private BigDecimal totalInventory;
     private BigDecimal totalSum;
     private Users createBy;
     private Date createdt;
     private Users checkBy;
     private Date checkDate;
     private Integer status;
     private String memo;
     
     private String startTime;
 	 private String endTime;
     

    

	@Id
     @GeneratedValue(generator = "paymentableGenerator")    
     @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")   
	 @Column(name = "inventoryId", insertable = true, updatable = true, nullable = false)
	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	@Column(name="storeId", length=50)
	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Column(name="totalInventory", length=50)
	public BigDecimal getTotalInventory() {
		return totalInventory;
	}

	public void setTotalInventory(BigDecimal totalInventory) {
		this.totalInventory = totalInventory;
	}

	@Column(name="createdt", length=50)
	public Date getCreatedt() {
		return createdt;
	}

	public void setCreatedt(Date createdt) {
		this.createdt = createdt;
	}
   
	@Column(name="memo", length=200)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name="code", length=50)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	@Column(name="checkDate", length=50)
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Column(name="totalSum", length=50)
	public BigDecimal getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(BigDecimal totalSum) {
		this.totalSum = totalSum;
	}
	@Column(name="status", length=5)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	//非数据库字段
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

	
	
}