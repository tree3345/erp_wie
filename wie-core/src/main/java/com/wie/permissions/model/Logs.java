package com.wie.permissions.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/** 
  * @ClassName: Logs 
  * @Description: 日志实体类
  * @author Administrator
  */
@Entity
@Table(name="c_operlog")
public class Logs implements java.io.Serializable {

	// 操作的内容
	private String content;
	// 操作耗时
	private Long cost;
	// 操作时间
	private String createdate;
	// 操作的ip
	private String createip;
	// 操作者
	private String createuser;
	private String id;
	// 操作的解释
	private String memo;
	// 操作类型
	private String opertype;

	public Logs() {
		super();
	}

	public Logs(String id, String opertype, String content, Long cost,
			String createip, String createuser, String createdate, String memo) {
		super();
		this.id = id;
		this.opertype = opertype;
		this.content = content;
		this.cost = cost;
		this.createip = createip;
		this.createuser = createuser;
		this.createdate = createdate;
		this.memo = memo;
	}

	@Column(name="content",length=200)
	public String getContent() {
		return content;
	}
	@Column(name="cost",length=100)
	public Long getCost() {
		return cost;
	}
	@Column(name="createdate",length=20)
	public String getCreatedate() {
		return createdate;
	}
	@Column(name="createip",length=20)
	public String getCreateip() {
		return createip;
	}
	@Column(name="createuser",length=30)
	public String getCreateuser() {
		return createuser;
	}
	 @Id
	 @GenericGenerator(name="systemUUID",strategy="uuid")
	 @GeneratedValue(generator="systemUUID")
	 @Column(name = "id", insertable = true, updatable = true, nullable = false)
	public String getId() {
		return id;
	}
	@Column(name="memo",length=50)
	public String getMemo() {
		return memo;
	}
	@Column(name="opertype",length=20)
	public String getOpertype() {
		return opertype;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCost(long cost) {
		this.cost = cost;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public void setCreateip(String createip) {
		this.createip = createip;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setOpertype(String opertype) {
		this.opertype = opertype;
	}

}
