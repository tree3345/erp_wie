package com.wie.erp.controls;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wie.common.poi.excel.ExcelExportUtil;
import com.wie.common.poi.excel.ExcelWorkSheet;
import com.wie.common.poi.excel.entity.ExcelTitle;
import com.wie.common.tools.page.Pagination;
import com.wie.common.tools.util.BrowserUtils;
import com.wie.erp.biz.IProductService;
import com.wie.erp.biz.IStoreService;
import com.wie.erp.biz.IWarehouseDetailService;
import com.wie.erp.model.Inventory;
import com.wie.erp.model.InventoryDetail;
import com.wie.erp.model.Product;
import com.wie.erp.model.ProductClass;
import com.wie.erp.model.Warehouse;
import com.wie.erp.model.WarehouseDetail;
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.permissions.biz.IDictionaryService;
import com.wie.permissions.biz.IUserService;
import com.wie.permissions.model.Users;


/** 
  * @ClassName: DictionaryTg 
  * @Description: 系统字典控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("warehouseDetailControl")
public class WarehouseDetailTg extends BaseTg {
	/** 
	  * @Fields dictionaryService : IDictionaryService业务层接口注入
	  */ 
	@Autowired
	private IWarehouseDetailService warehouseDetailService;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private IUserService userService;
	/**
	  * @Fields dic : struts2.0接受前台信息（domain接受）
	  */ 
	
	private WarehouseDetail warehouseDetail;
    
	private File excelFile;
	
	private String excelFileFileName;
	private ExcelWorkSheet<WarehouseDetail> excelWorkSheet; 

	public ExcelWorkSheet<WarehouseDetail> getExcelWorkSheet() {
		return excelWorkSheet;
	}

	public void setExcelWorkSheet(ExcelWorkSheet<WarehouseDetail> excelWorkSheet) {
		this.excelWorkSheet = excelWorkSheet;
	}

	public String getExcelFileFileName() {
		return excelFileFileName;
	}

	public void setExcelFileFileName(String excelFileFileName) {
		this.excelFileFileName = excelFileFileName;
	}

	/** 
	  * @Title: index 
	  * @Description: 系统字典首页
	  * @param @return
	  * @return String
	  * @throws 
	  */
	public String index(){
		return this.SUCCESS;
	}
	
	/** 
	  * @Title: getItems 
	  * @Description: 系统字典表所有常量信息
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void getItems(){
		if(warehouseDetail==null)warehouseDetail=new WarehouseDetail();
		String classId=request.getParameter("classId");
		String q=request.getParameter("q");
		Product product=new Product();
		if(classId!=null&&!"".equals(classId)){
			ProductClass productClass=new ProductClass();
			productClass.setClassId(classId);
			product.setProductClass(productClass);
		}
		if(q!=null)
			product.setProductName(q);
		warehouseDetail.setProduct(product);
		String userId=(String)request.getSession().getAttribute("userId");
		String storeId=request.getParameter("storeId");
		String storeIds=storeService.getUserStores(userId,storeId);
		Warehouse warehouse=new Warehouse();
		warehouse.setStoreId(storeIds);
		warehouseDetail.setWarehouse(warehouse);
		Pagination pagination=warehouseDetailService.getPageList(warehouseDetail,page,rows,sort, order);
		List list=pagination.getList();
		List lists = doList(list);	
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + pagination.getTotalCount() + ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr,response);
	}

	/** 
	  * @Title: save 
	  * @Description: 保存一条系统字典表中的常量信息
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void save(){
		/*String num = dictionaryService.findMax(dic.getNickName());
		dic.setValue(num);
		
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		dic.setCreateTime(format.format(new Date()));
		if(this.dictionaryService.save(this.dic)){
			returnJsion("{\"success\":\"true\"}",response);
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}*/
	}

	public void exportXls() {
		response.setContentType("application/vnd.ms-excel");
		String storeId=request.getParameter("storeId");
		String userId=(String)request.getSession().getAttribute("userId");
		String storeIds=storeService.getUserStores(userId,storeId);
		String codedFileName = null;
		OutputStream fOut = null;
		try {
			codedFileName = "用户信息";
			// 根据浏览器进行转码，使其支持中文文件名
			if (BrowserUtils.isIE(request)) {
				response.setHeader(
					"content-disposition","attachment;filename="
					+ java.net.URLEncoder.encode(codedFileName,"UTF-8") + ".xls");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"),"ISO8859-1");
				response.setHeader("content-disposition","attachment;filename=" + newtitle + ".xls");
			}
			// 产生工作簿对象
			HSSFWorkbook workbook = null;
			
			String hql="from WarehouseDetail wd where wd.warehouse.storeId in("+storeIds+")";
			String storeName="";
			if(storeId!=null&&!"".equals(storeId))
			storeName=storeService.findById(storeId).getStoreName();
			String userName =userService.findById(userId).getName();
			List<WarehouseDetail> warehouseDetail = this.warehouseDetailService.findList(hql);
			
			workbook = ExcelExportUtil.exportExcel(new ExcelTitle(storeName+"库存信息", "导出人:"+userName,
					"导出信息"), WarehouseDetail.class, warehouseDetail);
			fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (Exception e) {
		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {

			}
		}
	}
	/*public void importExcel() throws FileNotFoundException{
			FileInputStream file=new FileInputStream(excelFile);
			try {
				ImportParams params = new ImportParams();
				params.setTitleRows(2);
				params.setSecondTitleRows(2);
				params.setNeedSave(true);
				List<WarehouseDetail> listWarehouseDetail = (List<WarehouseDetail>)ExcelImportUtil.importExcelByIs(file,WarehouseDetail.class,params);
				for (WarehouseDetail warehouseDetail : listWarehouseDetail) {
					if(warehouseDetail.getClass()!=null){
						warehouseDetailService.save(warehouseDetail);
					}
				}
				returnJsion("{\"success\":\"true\"}",response);
			} catch (Exception e) {
				e.printStackTrace();
				returnJsion("{\"error\":\"true\"}",response);
			}finally{
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
	}*/
	public void impexcel() {
		String userId=(String)request.getSession().getAttribute("userId");
		String storeId=request.getParameter("storeId");
		Users createBy = new Users();
		createBy.setId(userId);
		//String storeId=(String)request.getSession().getAttribute("storeId");
		try {
			Workbook book = createWorkBook(new FileInputStream(excelFile));  
			//book.getNumberOfSheets();  判断Excel文件有多少个sheet   
			Sheet sheet =  book.getSheetAt(0);    
			excelWorkSheet = new ExcelWorkSheet<WarehouseDetail>();  
			Row firstRow = sheet.getRow(0);  
			Iterator<Cell> iterator = firstRow.iterator();  
			  
			//保存列名   
			List<String> cellNames = new ArrayList<String>();  
			while (iterator.hasNext()) {  
			    cellNames.add(iterator.next().getStringCellValue());  
			}  
			excelWorkSheet.setColumns(cellNames);
			Inventory inventory=new Inventory();
			Date date=new Date();
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMddHHmmss" );
			inventory.setCode("JNPK"+sdf.format(date));
			inventory.setCreateBy(createBy);
			inventory.setCreatedt(new java.sql.Timestamp(System.currentTimeMillis()));
            inventory.setCheckBy(createBy);
            inventory.setCheckDate(inventory.getCreatedt());
			inventory.setMemo("excel导入库存产生的货损");
			inventory.setStatus(1);
			inventory.setStoreId(storeId);
			BigDecimal dmgs=new BigDecimal(0);
			List<InventoryDetail> inventoryDetailList=new ArrayList<InventoryDetail>();
			for (int i = 4; i <= sheet.getLastRowNum(); i++) {  
			    Row ros = sheet.getRow(i); 
			    BigDecimal quantity=new BigDecimal(0);
			    if(ros.getCell(3).getCellType()==0)
			    	quantity=new BigDecimal(ros.getCell(3).getNumericCellValue()+"");
			    if(ros.getCell(3).getCellType()==1)
			    	quantity=new BigDecimal(ros.getCell(3).getStringCellValue());
			   
			    WarehouseDetail wd = warehouseDetailService.findById(ros.getCell(0).getStringCellValue());  
			    InventoryDetail invdetail=new InventoryDetail();
			    invdetail.setInventory(inventory);
			    invdetail.setProduct(wd.getProduct());
			    invdetail.setInventoryQuantity(quantity);
			    invdetail.setWarehouseQuantity(wd.getQuantity());
			    invdetail.setDamageQuantity(quantity.subtract(wd.getQuantity()));
               // invdetail.setDamageSum();
			    dmgs=dmgs.add(quantity.subtract(wd.getQuantity()));
			    wd.setQuantity(quantity);
			    excelWorkSheet.getData().add(wd);
			    inventoryDetailList.add(invdetail);
			}  
			inventory.setTotalInventory(dmgs);
			
			warehouseDetailService.saveList(excelWorkSheet.getData(),inventory,inventoryDetailList);
			
			returnJsion("{\"success\":\"true\"}",response);
		
		} catch (Exception e) {
			e.printStackTrace();
			returnJsion("{\"error\":\"true\"}",response);
		}  
	}
	//判断文件类型   
    public Workbook createWorkBook(InputStream is) throws IOException{  
        if(excelFileFileName.toLowerCase().endsWith("xls")){  
            return new HSSFWorkbook(is);  
        }  
        if(excelFileFileName.toLowerCase().endsWith("xlsx")){  
            return new XSSFWorkbook(is);  
        }  
        return null;  
    }  
	/** 
	  * @Title: find 
	  * @Description: 查找一常量
	  * @param 
	  * @return void
	  * @throws 
	  *//*
	public void find(){
		Dictionarys dic = this.dictionaryService.findById(id);
		List<Dictionarys> list = new ArrayList<Dictionarys>();
		if(null != dic){
			list.add(dic);
		}
		JSONArray json = JSONArray.fromObject(list);
		returnJsion("{\"rows\":" +  json.toString() +"}", response);
	}*/

	public File getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}

	/** 
	  * @Title: update 
	  * @Description:  更新常量
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void update(){
		/*if(this.dictionaryService.alter(this.dic)){
			returnJsion("{\"success\":\"true\"}",response);
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}*/
	}

	
	
	/** 
	  * @Title: returnJsion 
	  * @Description:  解决json问题
	  * @param @param baseStr 拼好的jsion串
	  * @param @param response 
	  * @return void
	  * @throws 
	  */
	private void returnJsion(String baseStr, HttpServletResponse response) {
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(baseStr);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (out != null) {
				out.close();
			}
		}
	}

	/** 
	  * @Title: doList 
	  * @Description: 解决struts2.0domal接受参数的方法
	  * @param @param list 装Dictionarys的具体信息。
	  * @param @return
	  * @return List
	  * @throws 
	  */
	private List doList(List<WarehouseDetail> list) {
		List lists = new ArrayList();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				WarehouseDetail detail = list.get(i);
				map.put("productId", detail.getProduct().getProductId());
				map.put("productName", detail.getProduct().getProductName());
				ProductClass pclass=detail.getProduct().getProductClass();
				map.put("classId", pclass==null?"":pclass.getClassId());
				map.put("unit", detail.getProduct().getUnit());
				map.put("storeId", detail.getWarehouse().getStoreId());
				map.put("quantity", detail.getQuantity()+"");
				map.put("id", detail.getId() );
				lists.add(map);
			}
		}
		return lists;
	}
	private List doListObj(List<Object[]> list,String[] params) {
		List lists = new ArrayList();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				Object[] obj =(Object[]) list.get(i);
				for(int j=0;j<params.length;j++)
					map.put(params[j], obj[j]+"");
				lists.add(map);
			}
		}
		return lists;
	}
	

	public WarehouseDetail getWarehouseDetail() {
		return warehouseDetail;
	}

	public void setWarehouseDetail(WarehouseDetail warehouseDetail) {
		this.warehouseDetail = warehouseDetail;
	}
	
}
