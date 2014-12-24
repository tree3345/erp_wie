/**   
  * @Title: RoleAuth.java 
  * @Package com.tgyt.permissions.model 
  * @Description: 
  * @author sunct sunchaotong18@163.com 
  * @date 2011-9-30 上午10:30:29 
  * @version V1.0   
  */

package com.wie.permissions.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/** 
 * @ClassName: RoleAuth 
 * @Description:角色权限实体
 * @author sunct sunchaotong18@163.com 
 * @date 2011-9-30 上午10:30:29 
 *  
 */
@Entity
@Table(name="c_roleauth")
public class RoleAuth {

	private String id;
	private String roleId;
	private String resourceId;
	private String actions;
	
	public RoleAuth() {
		super();
	}
	
	public RoleAuth(String id, String roleId, String resourceId,
			String actions) {
		super();
		this.id = id;
		this.roleId = roleId;
		this.resourceId = resourceId;
		this.actions = actions;
	}
	 @Id
	 @GenericGenerator(name="systemUUID",strategy="uuid")
	 @GeneratedValue(generator="systemUUID")
	 @Column(name = "id", insertable = true, updatable = true, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="roleid")
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	@Column(name="resourceid")
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	@Column(name="actions")
	public String getActions() {
		return actions;
	}
	public void setActions(String actions) {
		this.actions = actions;
	}
	
}
