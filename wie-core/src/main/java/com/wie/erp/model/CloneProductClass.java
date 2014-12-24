/**   
  * @Title: CloneResources.java 
  * @Package com.tgyt.permissions.model 
  * @Description: 
  * @author sunct sunchaotong18@163.com 
  * @date 2011-9-20 下午1:20:07 
  * @version V1.0   
  */

package com.wie.erp.model;


/** 
 * @ClassName: CloneResources 
 * @Description: 该类是作为Resources的复制类使用，也就是在转换成json串时直接使用此类
 *  
 */
public class CloneProductClass {

	private String classId;
    private String parentId;
    private String parentName;
    private String categoryId;
    private String categoryName;
    private String className;
    private String icon;
    
	/** 
	  * <p>Title: </p> 
	  * <p>Description: </p>  
	  */ 
	
	public CloneProductClass() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CloneProductClass(String classId, String parentId,
			String parentName, String categoryId, String categoryName,
			String className, String icon) {
		super();
		this.classId = classId;
		this.parentId = parentId;
		this.parentName = parentName;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.className = className;
		this.icon = icon;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
