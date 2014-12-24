package com.wie.panelClient.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "JN_Employee")
public class Employee {

	private String userId=UUID.randomUUID().toString();
	
	private String storeId;
	
	private String userName;
	
	private String password;
	
	private String employeeName;
	
	private String mobile;
	
	private String phone;
	
	private String email;
	
	private String address;
	
	private String sex;
	
	private Integer age;
	
	private String isSkipAnimation;
	
	private Integer pageStyle;
	
	private String isAdmin;
	
	private String isActive;

	private Set<Erole> eroles=new HashSet<Erole>();

	@Id
	@GeneratedValue(generator = "paymentableGenerator") 
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
    @Column(name = "userId", insertable = true, updatable = true, nullable = false)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name="storeId",length=50)
	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Column(name="userName",length=50)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name="password",length=50)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name="employeeName",length=50)
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	@Column(name="email",length=50)
	public String getEmail() {
		return email;
	}

	@Column(name="Mobile",length=50)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name="phone",length=50)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="address",length=50)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name="sex",length=50)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name="age",length=50)
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Column(name="isSkipAnimation",length=5)
	public String getIsSkipAnimation() {
		return isSkipAnimation;
	}

	public void setIsSkipAnimation(String isSkipAnimation) {
		this.isSkipAnimation = isSkipAnimation;
	}

	@Column(name="pageStyle",length=50)
	public Integer getPageStyle() {
		return pageStyle;
	}

	public void setPageStyle(Integer pageStyle) {
		this.pageStyle = pageStyle;
	}

	@Column(name="isAdmin",length=5)
	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Column(name="isActive",length=5)
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(
			name="JN_Emp_Role",
			joinColumns={@JoinColumn(name="UserID")},
			inverseJoinColumns={@JoinColumn(name="RoleID")}
	)
	public Set<Erole> getEroles() {
		return eroles;
	}

	public void setEroles(Set<Erole> eroles) {
		this.eroles = eroles;
	}

	public Employee(String userId, String storeId, String userName,
			String password, String employeeName, String mobile, String phone,
			String email, String address, String sex, Integer age,
			String isSkipAnimation, Integer pageStyle, String isAdmin,
			String isActive) {
		super();
		this.userId = userId;
		this.storeId = storeId;
		this.userName = userName;
		this.password = password;
		this.employeeName = employeeName;
		this.mobile = mobile;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.sex = sex;
		this.age = age;
		this.isSkipAnimation = isSkipAnimation;
		this.pageStyle = pageStyle;
		this.isAdmin = isAdmin;
		this.isActive = isActive;
	}

	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
