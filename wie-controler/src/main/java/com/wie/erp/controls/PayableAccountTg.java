package com.wie.erp.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wie.panelClient.model.Employee;
import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Action;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IBillService;
import com.wie.erp.model.Bill;
import com.wie.framework.controls.struts2.BaseTg;


/** 
  * @ClassName: DictionaryTg 
  * @Description: 系统字典控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("payableAccountControl")
public class PayableAccountTg extends BaseTg {
	/** 
	  * @Fields dictionaryService : IDictionaryService业务层接口注入
	  */ 
	@Autowired
	private IBillService billService;
	
	
	/** 
	  * @Fields dic : struts2.0接受前台信息（domain接受）
	  */ 
	private Bill bill;

	/** 
	  * @Fields nickName : 昵称
	  */ 
	private String nickName;
	
	/** 
	  * @Fields id : 删除，更新所需要的id，struts2.0接受
	  */ 
	private String id;

	/** 
	  * @Title: index 
	  * @Description: 系统字典首页
	  * @param @return
	  * @return String
	  * @throws 
	  */
	public String index(){
		return Action.SUCCESS;
	}
	
	/**
	 * @throws ParseException  
	  * @Title: getItems 
	  * @Description: 系统字典表所有常量信息
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void getItems() throws ParseException{
		if(bill==null)bill=new Bill();
		String startTime=(String)request.getParameter("startTime");
		String endTime=(String)request.getParameter("endTime"); 
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
//		System.err.println(request.getParameter("memberId")+" "+request.getParameter("createBy"));
		
		if(request.getParameter("memberId")!=null&&!request.getParameter("memberId").equals(""))
		bill.setMemberId(request.getParameter("memberId"));
		if(request.getParameter("createBy")!=null&&!request.getParameter("createBy").equals("")){
		Employee createBy=new Employee();
		createBy.setUserName(request.getParameter("createBy"));
		bill.setCreateBy(createBy);
		}
		if(startTime!=null&&!"".equals(startTime))
			bill.setStartTime(startTime);
		if(endTime!=null&&!"".equals(endTime))
			bill.setEndTime(endTime);
		Pagination pagination = billService.getPageList(bill, page, rows,sort,order);
		List<Bill> list = pagination.getList();
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
		
		/*String inserted = request.getParameter("ins");
		Set<PurchaseDetail> pdset=new LinkedHashSet<PurchaseDetail>();
		if(inserted != null){
			System. out.println("inserted"+inserted);
		    List<PurchaseDetail> listInserted = JSONArray.toList(JSONArray.fromObject(inserted), PurchaseDetail.class);
		    pdset.addAll(listInserted);
		}
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat();
		purchase.setIndt(new Date());
		String userid=(String) request.getSession().getAttribute("userid");
		
		purchase.setInby(userid);  //登录id
		
		String storeId="";
		String warehouseDetailId="";
		List getstoreidlist=msSQLpurchaseService.getWarehouse(userid);
		if(getstoreidlist.size()>0){
			Object[] obj=(Object[])getstoreidlist.get(0);
			storeId=obj[0].toString();
			warehouseDetailId=obj[1].toString();
		}
		purchase.setStoreId(storeId);//登录id对应的
		purchase.setOrderId("6981AAA3-E142-4DFC-B53F-05A222026BDB");//订单id暂时固定  
		purchase.setPurchaseDetail(pdset);
		//dic.setCreateTime(format.format(new Date()));
		if(this.msSQLpurchaseService.savePuerchase(this.purchase,warehouseDetailId)!=0){
			returnJsion("{\"success\":\"true\"}",response);
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}*/
	}

	/** 
	  * @Title: find 
	  * @Description: 查找一常量
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void find(){
		Bill bill = this.billService.findById(id);
		List<Bill> list = new ArrayList<Bill>();
		if(null != bill){
			list.add(bill);
		}
		JSONArray json = JSONArray.fromObject(list);
		returnJsion("{\"rows\":" +  json.toString() +"}", response);
	}

	/** 
	  * @Title: update 
	  * @Description:  更新常量
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void update(){
		/*if(this.msSQLpurchaseService.alter(this.purchase)){
			returnJsion("{\"success\":\"true\"}",response);
		}else{
			returnJsion("{\"error\":\"true\"}",response);
		}*/
	}

	/** 
	  * @Title: del 
	  * @Description: 删除常量
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void del(){
		/*if(this.msSQLpurchaseService.deleteById(id)){
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
	private List<Map<String, String>> doList(List<Bill> list) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		//dic.setCreateTime(format.format(new Date()));
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				Bill bill = list.get(i);
				map.put("billId", bill.getBillId());
				map.put("billNo", bill.getBillNo());
				map.put("createBy", bill.getCreateBy().getUserName());
				map.put("memberId", bill.getMemberId());
				map.put("remark", bill.getRemark());
				map.put("storeId", bill.getStoreId());
				map.put("billDateTime", bill.getBillDateTime()+"");
				map.put("saleMoney", bill.getSaleMoney()+"");
				map.put("sumMoney", bill.getSumMoney()+"");
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

	
	
	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	
}
