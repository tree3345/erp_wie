package com.wie.erp.model;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.wie.common.poi.excel.annotation.Excel;
import com.wie.common.poi.excel.annotation.ExcelEntity;

@Entity
@Table(name="JN_Product")
public class Product {

	private String productId =UUID.randomUUID().toString();
	@Excel(exportName="商品名称",exportFieldWidth=35)
	private String productName;
	
//	private String classId;
	
	private String categoryId;
	
	private String unit;
	
	private String Icon;
	
	private String image;
	
	private String descr;
	
	private Integer sort;
	
	
	private String mouseImage;
	
	private String spelling;
	
	private String productNo;
	
	private String imageName;
	
	private Integer weightlower;
	
	private String goodNumber;
	
	private Integer status; //0为不显示假删除，1为显示
	
	private Integer flag;
	
	private Integer stockflag;
	
	private String storeId;
	
	private ProductPrice productPrice;
//	private Set<ProductClass> productClass = new HashSet<ProductClass>();
	@ExcelEntity()
	private ProductClass productClass;
	
	@ManyToOne
	@JoinColumn(name="classId")
	 public ProductClass getProductClass() {
		return productClass;
	}

	public void setProductClass(ProductClass productClass) {
		this.productClass = productClass;
	}

	@Id
	 @GeneratedValue(generator = "paymentableGenerator") 
     @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	 @Column(name = "productId", insertable = true, updatable = true, nullable = false)
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}


	@Column(name="categoryId",length=50)
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name="unit", length=50)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name="Icon", length=50)
	public String getIcon() {
		return Icon;
	}

	public void setIcon(String icon) {
		Icon = icon;
	}

	@Column(name="image", length=50)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name="descr", length=50)
	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Column(name="sort", length=10)
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}


	@Column(name="mouseImage", length=50)
	public String getMouseImage() {
		return mouseImage;
	}

	public void setMouseImage(String mouseImage) {
		this.mouseImage = mouseImage;
	}

	@Column(name="spelling",length=50)
	public String getSpelling() {
		return spelling;
	}

	public void setSpelling(String spelling) {
		this.spelling = spelling;
	}

	@Column(name="productNO", length=50)
	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	@Column(name="imageName", length=50)
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Column(name="weightlower",  length=10)
	public Integer getWeightlower() {
		return weightlower;
	}

	public void setWeightlower(Integer weightlower) {
		this.weightlower = weightlower;
	}

	@Column(name="goodNumber", length=10)
	public String getGoodNumber() {
		return goodNumber;
	}

	public void setGoodNumber(String goodNumber) {
		this.goodNumber = goodNumber;
	}

	@Column(name="productName", length=50)
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	 @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 @JoinColumn(name ="productId")
	public ProductPrice getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(ProductPrice productPrice) {
		this.productPrice = productPrice;
	}

	@Column(name="status", length=10)
	public Integer getStatus() {
		return status;
	}
	
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Transient
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Column(name="storeId", length=100)
	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Transient
	public Integer getStockflag() {
		return stockflag;
	}

	public void setStockflag(Integer stockflag) {
		this.stockflag = stockflag;
	}

	/*@ManyToMany(cascade={CascadeType.MERGE})
	@JoinTable(
			name="c_product_class",
			joinColumns={@JoinColumn(name="productid")},
			inverseJoinColumns={@JoinColumn(name="classid")}
	)
	public Set<ProductClass> getProductClass() {
		return productClass;
	}

	public void setProductClass(Set<ProductClass> productClass) {
		this.productClass = productClass;
	}*/
	
	
}
