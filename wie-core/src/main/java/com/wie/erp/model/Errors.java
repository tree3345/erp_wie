package com.wie.erp.model;

import javax.persistence.*;
import java.util.Date;


/**
  * @classname:JN_Purchase
  * @Description: 操作管理实体类 
  * @author liudehai
  */
@Entity
@Table(name="JN_Error")
public class Errors implements java.io.Serializable {


    // Fields    

     private String id;
     private String macAddress;
     private Date errorDateTime;
     private String startTime;
     private String endTime;
     private String errorContent;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)//主键自增
    @Column(name = "id", nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name="MacAdress")
    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Column(name="ErrorDateTime")
    public Date getErrorDateTime() {
        return errorDateTime;
    }

    public void setErrorDateTime(Date errorDateTime) {
        this.errorDateTime = errorDateTime;
    }

    @Transient
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Transient
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    @Column(name="ErrorContent")
    public String getErrorContent() {
        return errorContent;
    }

    public void setErrorContent(String errorContent) {
        this.errorContent = errorContent;
    }
}