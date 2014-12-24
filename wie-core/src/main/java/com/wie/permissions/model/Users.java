package com.wie.permissions.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.wie.erp.model.Store;
import org.hibernate.annotations.GenericGenerator;


/** 
  * @ClassName: Users 
  * @Description: 用户实体类
  */
@Entity
@Table(name = "c_user")
public class Users implements Serializable {
	private String   address;              
	private String   age;          
	private String   birthday;
	private String   education;
	private String   email;
	private String   employeddate;
	private String   enname;
	private String   id;
	private String   lastlogoffdate;
	private String   lastlogondate;
	private String   lastlogonip;
	private String  logonid;
	private String   marriage;
	private String   memo;
	private String   name;
	private String   nativeplace;
	private String   orderid;
	private String   password;
	private String   phone;
	private String   position;
	private String   registerdate;
	private String   sex;       
	private String   status;
	private String   usertype;

	//员工工号
	private String jobmember;

    //private String salt; //加密密码的盐
	
	@Column(name="jobmember",length=100)
	public String getJobmember() {
		return jobmember;
	}

	public void setJobmember(String jobmember) {
		this.jobmember = jobmember;
	}
	private Set<Groups> groups = new HashSet<Groups>();
	private Set<Store> store = new HashSet<Store>();
	
	@Column(name="address",length=50)
	public String getAddress() {
		return address;
	}
	@Column(name="age",length=20)
	public String getAge() {
		return age;
	}
	@Column(name="birthday",length=20)
	public String getBirthday() {
		return birthday;
	}
	@Column(name="education",length=20)
	public String getEducation() {
		return education;
	}
	@Column(name="email",length=30)
	public String getEmail() {
		return email;
	}
	@Column(name="employeddate",length=20)
	public String getEmployeddate() {
		return employeddate;
	}
	@Column(name="enname",length=30)
	public String getEnname() {
		return enname;
	}
	 @Id
	 @GenericGenerator(name="systemUUID",strategy="uuid")
	 @GeneratedValue(generator="systemUUID")
	 @Column(name = "id", insertable = true, updatable = true, nullable = false)
	public String getId() {
		return id;
	}
	@Column(name="lastlogoffdate",length=20)
	public String getLastlogoffdate() {
		return lastlogoffdate;
	}
	@Column(name="lastlogondate",length=20)
	public String getLastlogondate() {
		return lastlogondate;
	}
	@Column(name="lastlogonip",length=20)
	public String getLastlogonip() {
		return lastlogonip;
	}
	@Column(name="logonid",nullable=false,length=80)
	public String getLogonid() {
		return logonid;
	}
	@Column(name="marriage",length=10)
	public String getMarriage() {
		return marriage;
	}
	@Column(name="memo",length=50)
	public String getMemo() {
		return memo;
	}
	@Column(name="name",nullable=false,length=30)
	public String getName() {
		return name;
	}
	@Column(name="nativeplace",length=30)
	public String getNativeplace() {
		return nativeplace;
	}
	@Column(name="orderid",length=20)
	public String getOrderid() {
		return orderid;
	}
	@Column(name="password",nullable=false,length=64)
	public String getPassword() {
		return password;
	}
	@Column(name="phone",length=30)
	public String getPhone() {
		return phone;
	}
	@Column(name="position",length=30)
	public String getPosition() {
		return position;
	}
	@Column(name="registerdate",length=20)
	public String getRegisterdate() {
		return registerdate;
	}
	@Column(name="sex",length=20)
	public String getSex() {
		return sex;
	}
	@Column(name="status",length=20)
	public String getStatus() {
		return status;
	}
	@Column(name="usertype",length=20)
	public String getUsertype() {
		return usertype;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setEmployeddate(String employeddate) {
		this.employeddate = employeddate;
	}
	public void setEnname(String enname) {
		this.enname = enname;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setLastlogoffdate(String lastlogoffdate) {
		this.lastlogoffdate = lastlogoffdate;
	}
	public void setLastlogondate(String lastlogondate) {
		this.lastlogondate = lastlogondate;
	}
	public void setLastlogonip(String lastlogonip) {
		this.lastlogonip = lastlogonip;
	}
	public void setLogonid(String logonid) {
		this.logonid = logonid;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setNativeplace(String nativeplace) {
		this.nativeplace = nativeplace;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public void setRegisterdate(String registerdate) {
		this.registerdate = registerdate;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
   /* @Column(name="salt",nullable=false,length=80)
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
     @Transient
    public String getCredentialsSalt() {
        return logonid + salt;
    }*/
    @ManyToMany(cascade={CascadeType.MERGE})
	@JoinTable(
			name="c_usergroup",
			joinColumns={@JoinColumn(name="userid")},
			inverseJoinColumns={@JoinColumn(name="groupid")}
	)
	public Set<Groups> getGroups() {
		return groups;
	}
	public void setGroups(Set<Groups> groups) {
		this.groups = groups;
	}

	@ManyToMany(cascade={CascadeType.MERGE})
	@JoinTable(
			name="JN_user_store",
			joinColumns={@JoinColumn(name="userid")},
			inverseJoinColumns={@JoinColumn(name="storeid")}
	)
	public Set<Store> getStore() {
		return store;
	}

	public void setStore(Set<Store> store) {
		this.store = store;
	}
}
