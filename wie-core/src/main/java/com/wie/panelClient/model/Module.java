package com.wie.panelClient.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "JN_Module")
public class Module implements java.io.Serializable{

	private int moduleId;

	private String moduleName;

	private String viewName;

	private String remark;

	private Set<Erole> eroles=new HashSet<Erole>();

	private boolean ischeck=false;



	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	@Column(name = "moduleId", insertable = true, updatable = true, nullable = false)
	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	@Column(name ="moduleName",length = 100)
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Column(name ="viewName",length = 100)
	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	@Column(name ="remark",length = 100)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(
			name="JN_Role_Module",
			joinColumns={@JoinColumn(name="ModuleID")},
			inverseJoinColumns={@JoinColumn(name="RoleID")}
	)
	public Set<Erole> getEroles() {
		return eroles;
	}

	public void setEroles(Set<Erole> eroles) {
		this.eroles = eroles;
	}


	@Transient
	public boolean isIscheck() {
		return ischeck;
	}

	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}
}
