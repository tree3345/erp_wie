package com.wie.permissions.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="c_checkArgument")
public class CheckArgument  implements java.io.Serializable {


    // Fields    

     private String id=UUID.randomUUID().toString();;
     private String name;
     private String value;
     private String storeId;
     private String memo;
     
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
	
	@Column(name="name", nullable=false, length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="value", nullable=false, length=5)
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Column(name="storeId", length=50)
	public String getStoreId() {
		return storeId;
	}
	
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	
	@Column(name="memo", length=200)
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
     

}