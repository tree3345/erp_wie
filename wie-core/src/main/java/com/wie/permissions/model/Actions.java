package com.wie.permissions.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



/** 
  * @ClassName: Actions 
  * @Description: 操作管理实体类 
  *  
  */
@Entity
@Table(name="c_action")
public class Actions  implements java.io.Serializable {


    // Fields    

     private String id;
     private String name;
     private String enname;
     private String methodName;
     private String icon;
     private String status;
     private Integer orderid;
     private String memo;
     private Set<Resources> resource = new HashSet<Resources>(
 			0);

    // Constructors

    /** default constructor */
    public Actions() {
    }

	/** minimal constructor */
    public Actions(String name, String enname, String status) {
        this.name = name;
        this.enname = enname;
        this.status = status;
    }
    
    /** full constructor */
    public Actions(String name, String enname, String methodName, String icon, String status, Integer orderid, String memo) {
        this.name = name;
        this.enname = enname;
        this.methodName = methodName;
        this.icon = icon;
        this.status = status;
        this.orderid = orderid;
        this.memo = memo;
    }

   
    /** 
	  * <p>Title: </p> 
	  * <p>Description: </p> 
	  * @param id
	  * @param name
	  * @param enname
	  * @param methodName
	  * @param icon
	  * @param status
	  * @param orderid
	  * @param memo
	  * @param resource 
	  */ 
	
	public Actions(String id, String name, String enname, String methodName,
			String icon, String status, Integer orderid, String memo,
			Set<Resources> resource) {
		super();
		this.id = id;
		this.name = name;
		this.enname = enname;
		this.methodName = methodName;
		this.icon = icon;
		this.status = status;
		this.orderid = orderid;
		this.memo = memo;
		this.resource = resource;
	}

  // Property accessors
     @Id
	 @GenericGenerator(name="systemUUID",strategy="uuid")
	 @GeneratedValue(generator="systemUUID")
	 @Column(name = "id", insertable = true, updatable = true, nullable = false)
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name="name", nullable=false, length=30)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="enname", nullable=false, length=30)

    public String getEnname() {
        return this.enname;
    }
    
    public void setEnname(String enname) {
        this.enname = enname;
    }
    
    @Column(name="handler", length=100)

    public String getMethodName() {
        return this.methodName;
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    @Column(name="icon", length=60)

    public String getIcon() {
        return this.icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    @Column(name="status", nullable=false)

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

	/**
	 * @return the resource
	 */
    @ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(
			name="c_resource_action",//中间表名
			joinColumns={@JoinColumn(name="action_id")},//设置自己在中间表的对应外键
			inverseJoinColumns={@JoinColumn(name="resource_id")}//设置对方()在中间表的对应外键
	)
	public Set<Resources> getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(Set<Resources> resource) {
		this.resource = resource;
	}
    
}