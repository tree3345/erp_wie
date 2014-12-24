package com.wie.erp.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

import com.wie.permissions.model.Users;



/** 
  * @classname:JN_Purchase
  * @Description: 操作管理实体类 
  * @author liudehai
  */
@Entity
@Table(name="JN_Store")
public class Store  implements java.io.Serializable {


    // Fields    

     private String storeId=UUID.randomUUID().toString();    //入库id
     private String storeName;
     private Integer status;
	 private Set<Users> users = new HashSet<Users>();

     
     @Id
 	 @GeneratedValue(generator = "paymentableGenerator") 
     @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	 @Column(name = "storeId", insertable = true, updatable = true, nullable = false)
     public String getStoreId() {
 		return storeId;
 	}
 	public void setStoreId(String storeId) {
 		this.storeId = storeId;
 	}

    @Column(name="StoreName", nullable=false, length=32)
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	@Column(name="status", length=32)
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	@ManyToMany(cascade={CascadeType.MERGE})
	@JoinTable(
			name="JN_user_store",
			joinColumns={@JoinColumn(name="storeid")},
			inverseJoinColumns={@JoinColumn(name="userid")}
	)
	public Set<Users> getUsers() {
		return users;
	}

	public void setUsers(Set<Users> users) {
		this.users = users;
	}
}