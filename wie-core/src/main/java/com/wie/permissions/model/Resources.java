package com.wie.permissions.model;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



/** 
  * @ClassName: Resources 
  * @Description:资源信息实体类
  *  
  */
@Entity
@Table(name="c_resource")
public class Resources  implements java.io.Serializable {

     private String id;
     private Resources parent;
     private Systems system;
     private String name;
     private String enname;
     private String resourcetype;
     private String link;
     private String icon;
     private String iconopen;
     private String isopen;
     private String isleaf;
     private String status;
     private Integer orderid;
     private String memo;
     private Set<Resources> children = new HashSet<Resources>(0);
     private Set<Actions> resActions = new HashSet<Actions>(0);


    // Constructors

    /** default constructor */
    public Resources() {
    }

	/** minimal constructor */
    public Resources(String id, Systems system, String name, String isleaf, String status) {
        this.id = id;
        this.system = system;
        this.name = name;
        this.isleaf = isleaf;
        this.status = status;
    }
    
    /** full constructor */
  /*  public Resources(Integer id, Resources parent, Systems system, String name, String enname, String resourcetype, String link, String icon, String iconopen, String isopen, String isleaf, String status, Integer orderid, String memo, Set<Resources> children) {
        this.id = id;
        this.parent = parent;
        this.system = system;
        this.name = name;
        this.enname = enname;
        this.resourcetype = resourcetype;
        this.link = link;
        this.icon = icon;
        this.iconopen = iconopen;
        this.isopen = isopen;
        this.isleaf = isleaf;
        this.status = status;
        this.orderid = orderid;
        this.memo = memo;
        this.children = children;
    }*/

  

	// Property accessors
     @Id
	 @GenericGenerator(name="systemUUID",strategy="uuid")
	 @GeneratedValue(generator="systemUUID")
	 @Column(name = "id", insertable = true, updatable = true, nullable = false)
    public String getId() {
        return this.id;
    }
    
    /** 
	  * <p>Title: </p> 
	  * <p>Description: </p> 
	  * @param id
	  * @param parent
	  * @param system
	  * @param name
	  * @param enname
	  * @param resourcetype
	  * @param link
	  * @param icon
	  * @param iconopen
	  * @param isopen
	  * @param isleaf
	  * @param status
	  * @param orderid
	  * @param memo
	  * @param children
	  * @param resActions 
	  */ 
	
	public Resources(String id, Resources parent, Systems system, String name,
			String enname, String resourcetype, String link, String icon,
			String iconopen, String isopen, String isleaf, String status,
			Integer orderid, String memo, Set<Resources> children,
			Set<Actions> resActions) {
		super();
		this.id = id;
		this.parent = parent;
		this.system = system;
		this.name = name;
		this.enname = enname;
		this.resourcetype = resourcetype;
		this.link = link;
		this.icon = icon;
		this.iconopen = iconopen;
		this.isopen = isopen;
		this.isleaf = isleaf;
		this.status = status;
		this.orderid = orderid;
		this.memo = memo;
		this.children = children;
		this.resActions = resActions;
	}

	public void setId(String id) {
        this.id = id;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="parent_id")

    public Resources getParent() {
        return this.parent;
    }
    
    public void setParent(Resources parent) {
        this.parent = parent;
    }
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="systemid", nullable=false)
    public Systems getSystem() {
        return this.system;
    }
    
    public void setSystem(Systems system) {
        this.system = system;
    }
    
    @Column(name="name", nullable=false, length=50)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="enname", length=30)

    public String getEnname() {
        return this.enname;
    }
    
    public void setEnname(String enname) {
        this.enname = enname;
    }
    
    @Column(name="resourcetype", length=20)

    public String getResourcetype() {
        return this.resourcetype;
    }
    
    public void setResourcetype(String resourcetype) {
        this.resourcetype = resourcetype;
    }
    
    @Column(name="link", length=300)

    public String getLink() {
        return this.link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    @Column(name="icon", length=50)

    public String getIcon() {
        return this.icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    @Column(name="iconopen", length=50)

    public String getIconopen() {
        return this.iconopen;
    }
    
    public void setIconopen(String iconopen) {
        this.iconopen = iconopen;
    }
    
    @Column(name="isopen", length=50)

    public String getIsopen() {
        return this.isopen;
    }
    
    public void setIsopen(String isopen) {
        this.isopen = isopen;
    }
    
    @Column(name="isleaf", nullable=false, length=50)

    public String getIsleaf() {
        return this.isleaf;
    }
    
    public void setIsleaf(String isleaf) {
        this.isleaf = isleaf;
    }
    
    @Column(name="status", nullable=false, length=50)

    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Column(name="orderid")

    public Integer getOrderid() {
        return this.orderid;
    }
    
    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }
    
    @Column(name="memo", length=50)

    public String getMemo() {
        return this.memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="parent")
    public Set<Resources> getChildren() {
        return this.children;
    }
    
    public void setChildren(Set<Resources> children) {
        this.children = children;
    }

	/**
	 * @return the resActions
	 */
    @ManyToMany(cascade={CascadeType.MERGE})
	@JoinTable(
			name="c_resource_action",
			joinColumns={@JoinColumn(name="resource_id")},
			inverseJoinColumns={@JoinColumn(name="action_id")}
	) 
	public Set<Actions> getResActions() {
		return resActions;
	}

	/**
	 * @param resActions the resActions to set
	 */
	public void setResActions(Set<Actions> resActions) {
		this.resActions = resActions;
	}

	/**
	 * @return the resource
	 */
  
    







}