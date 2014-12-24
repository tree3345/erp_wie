package com.wie.erp.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "JN_Intercourse")
public class Intercourse {

	private String id=UUID.randomUUID().toString();
	
	     private String intercourseTypeId;
	     private String code;
	     private String shortName;
	     private String fullName;
	     private String remark;
	     private String addr;
	     private String postcode;
	     private String phone;
	     private String fax;
	     private String www;
	     private String email;
	     private String contactMan;

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

	/*@Column(name="intercourseTypeId",length=50)
	public String getIntercourseTypeId() {
		return intercourseTypeId;
	}

	public void setIntercourseTypeId(String intercourseTypeId) {
		this.intercourseTypeId = intercourseTypeId;
	}*/

	@Column(name="code",length=100)
	public String getCode() {
		return code;
	}

	@Column(name="intercourseTypeId",length=100)
	public String getIntercourseTypeId() {
		return intercourseTypeId;
	}

	public void setIntercourseTypeId(String intercourseTypeId) {
		this.intercourseTypeId = intercourseTypeId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name="shortName",length=100)
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Column(name="fullName",length=100)
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name="remark",length=100)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="addr",length=100)
	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	@Column(name="postcode",length=100)
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@Column(name="phone",length=100)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name="fax",length=100)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name="www",length=100)
	public String getWww() {
		return www;
	}

	public void setWww(String www) {
		this.www = www;
	}

	@Column(name="email",length=100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="contactMan",length=100)
	public String getContactMan() {
		return contactMan;
	}

	public void setContactMan(String contactMan) {
		this.contactMan = contactMan;
	}

	public Intercourse() {
		super();
	}
}
