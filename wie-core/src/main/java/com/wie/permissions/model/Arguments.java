package com.wie.permissions.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/** 
  * @ClassName: Arguments 
  * @Description: 全局参数实体类 
  * @author sunct sunchaotong18@163.com 
  * @date 2011-9-19 下午2:48:19 
  *  
  */
@Entity
@Table(name="c_arguments")
public class Arguments  implements java.io.Serializable {


    // Fields    

     private String id;
     private String name;
     private String value;
     private String memo;
     private Integer orderid;


    // Constructors

    /** default constructor */
    public Arguments() {
    }

	/** minimal constructor */
    public Arguments(String id) {
        this.id = id;
    }
    
    /** full constructor */
    public Arguments(String id, String name, String value, String memo, Integer orderid) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.memo = memo;
        this.orderid = orderid;
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
    
    @Column(name="name", length=30)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="value", length=100)
    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    @Column(name="memo", length=100)
    public String getMemo() {
        return this.memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }
    
    @Column(name="orderid")
    public Integer getOrderid() {
        return this.orderid;
    }
    
    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }
   








}