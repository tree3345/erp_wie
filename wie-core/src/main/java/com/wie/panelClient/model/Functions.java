package com.wie.panelClient.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "JN_ModuleFunction")
public class Functions {

	private int functionId;

	private String functionName;

	private String controlName;

	private String remark;

	private Module module;

	private Set<Erole> eroles;


	private String moduleName;
	private boolean ischeck;



	@Id
	@GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
    @Column(name = "functionId", insertable = true, updatable = true, nullable = false)
	public int getFunctionId() {
		return functionId;
	}

	public void setFunctionId(int functionId) {
		this.functionId = functionId;
	}


	@Column(length = 100)
	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	@Column(length = 100)
	public String getControlName() {
		return controlName;
	}

	public void setControlName(String controlName) {
		this.controlName = controlName;
	}

	@Column(length = 100)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne
	@JoinColumn(name = "moduleId")
	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(
			name="JN_Role_Function",
			joinColumns={@JoinColumn(name="FunctionID")},
			inverseJoinColumns={@JoinColumn(name="RoleID")}
	)
	public Set<Erole> getEroles() {
		return eroles;
	}

	public void setEroles(Set<Erole> eroles) {
		this.eroles = eroles;
	}

	@Transient
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Transient
	public boolean isIscheck() {
		return ischeck;
	}

	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}
}
