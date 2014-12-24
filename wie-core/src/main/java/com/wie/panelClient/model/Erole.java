package com.wie.panelClient.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "JN_role")
public class Erole {

	private String roleId=UUID.randomUUID().toString();

	private String roleName;

	private String remark;

	private boolean ischeck=false;

	private Set<Employee> employees =new HashSet<Employee>();
	private Set<Module> modules = new HashSet<Module>();
	private Set<Functions> functions = new HashSet<Functions>();


	@Id
	@GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
    @Column(name = "roleId", insertable = true, updatable = true, nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "roleName", length = 100)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "remark", length = 100)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(
			name="JN_Emp_Role",
			joinColumns={@JoinColumn(name="RoleID")},
			inverseJoinColumns={@JoinColumn(name="UserID")}
	)
	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(
			name="JN_Role_Module",
			joinColumns={@JoinColumn(name="RoleID")},
			inverseJoinColumns={@JoinColumn(name="ModuleID")}
	)
	public Set<Module> getModules() {
		return modules;
	}

	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}

	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(
			name="JN_Role_Function",
			joinColumns={@JoinColumn(name="RoleID")},
			inverseJoinColumns={@JoinColumn(name="FunctionID")}
	)
	public Set<Functions> getFunctions() {
		return functions;
	}

	public void setFunctions(Set<Functions> functions) {
		this.functions = functions;
	}

	@Transient
	public boolean isIscheck() {
		return ischeck;
	}

	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}
}
