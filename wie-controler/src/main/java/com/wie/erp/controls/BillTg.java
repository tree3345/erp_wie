package com.wie.erp.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wie.erp.biz.IInventoryDetailService;
import com.wie.erp.biz.IStoreService;
import com.wie.erp.model.*;
import com.wie.panelClient.model.Employee;
import net.sf.json.JSONArray;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.Logical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.opensymphony.xwork2.Action;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IBillDetailService;
import com.wie.erp.biz.IBillService;
import com.wie.framework.controls.struts2.BaseTg;


/** 
  * @ClassName: DictionaryTg 
  * @Description: 系统字典控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("billControl")
public class BillTg extends BaseTg {
	/** 
	  * @Fields dictionaryService : IDictionaryService业务层接口注入
	  */ 
	@Autowired
	private IBillService billService;
    @Autowired
    private IInventoryDetailService inventoryDetailService;
	@Autowired
	private IBillDetailService billDetailService;
	@Autowired
	private IStoreService storeService;
	
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

    public String index() throws UnauthorizedException {
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
	public void getItems() {
		if(bill==null)bill=new Bill();
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String storeId=request.getParameter("storeId");
		String userId=(String)request.getSession().getAttribute("userId");
        String storeIds=storeService.getUserStores(userId,storeId);
//		System.err.println(request.getParameter("memberId")+" "+request.getParameter("createBy"));
		bill.setStoreId(storeIds);
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

	//销售统计
	public String statistics(){
		return "statistics";
	}
	
	public void getItemsStatistics()
	{
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if((!"".equals(startTime)&&startTime!=null)&&(!"".equals(endTime)&&endTime!=null)) {
			String productName = request.getParameter("productName");
			String saleMan = request.getParameter("saleMan");
			String storeId=request.getParameter("storeId");
			String userId=(String)request.getSession().getAttribute("userId");
			String storeIds=storeService.getUserStores(userId,storeId);
			BillDetail billDetail = new BillDetail();

			//生成利润
			//billDetailService.updatePurchasePrice(storeId);

			Product productCon = new Product();
			productCon.setProductName(productName);
			billDetail.setProduct(productCon);
			Bill bl = new Bill();
			bl.setStoreId(storeIds);
			bl.setStartTime(startTime);
			bl.setEndTime(endTime);
			if (saleMan != null && !"".equals(saleMan)) {
				Employee employee = new Employee();
				employee.setUserName(saleMan);
				bl.setCreateBy(employee);
			}
			billDetail.setBill(bl);
			List<Object[]> list = billDetailService.getStatisticsPageList(billDetail, page, rows, sort, order);
			int totalcount = billDetailService.findCountBySql(billDetail);
			List<Map<String, String>> lists = doListObj(list, startTime, endTime, storeId);
			JSONArray jsonArray = JSONArray.fromObject(lists);
			String baseStr = "{\"total\":" + totalcount + ",\"rows\":";
			baseStr = baseStr + jsonArray.toString() + "}";
			returnJsion(baseStr, response);
		}
	}
	
	public String showCharts(){
		String productId=request.getParameter("productId");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");

		String storeId=request.getParameter("storeId");
		String userId=(String)request.getSession().getAttribute("userId");
		String storeIds=storeService.getUserStores(userId,storeId);
		String hql="from BillDetail ind where ind.bill.storeId in("+storeIds+") and ind.product.productId='"+productId+"' ";
		if(!"".equals(startTime)&&startTime!=null)	
			hql+=" and ind.bill.billDateTime > '"+startTime.replace("_", " ")+"' ";
		if(!"".equals(endTime)&&endTime!=null)	
			hql+=" and ind.bill.billDateTime < '"+endTime.replace("_", " ")+"' ";
		hql	+=" order by ind.bill.billDateTime asc";
		List<BillDetail> list=billDetailService.findList(hql);
		String data="", data2="",data3="",productName="",unit="";
    
		for(BillDetail bd:list)
		{
			Date date=bd.getBill().getBillDateTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmm");
			String dateStr=format.format(date);
			int year=Integer.parseInt(dateStr.substring(0, 4));
			int month=Integer.parseInt(dateStr.substring(4, 6));
			int day=Integer.parseInt(dateStr.substring(6, 8));
			int hour=Integer.parseInt(dateStr.substring(9, 11));
			int min=Integer.parseInt(dateStr.substring(11, 13));
			data+="[Date.UTC("+year+", "+(month-1)+", "+(day)+","+(hour)+","+(min)+"), "+bd.getQuantity()+"],";
			data2+="[Date.UTC("+year+", "+(month-1)+", "+(day)+","+(hour)+","+(min)+"), "+(bd.getPrice())+"],";
			//data3+="[Date.UTC("+year+", "+(month-1)+", "+(day)+","+(hour)+","+(min)+"), "+(bd.getProfit().getProfitPrice().multiply(bd.getQuantity()))+"],";
			productName=bd.getProduct().getProductName();
			unit=bd.getProduct().getUnit();
		}
		if(list.size()>0){
		  data=data.substring(0, data.length()-1);
		  data2=data2.substring(0, data2.length()-1);
		//  data3=data3.substring(0, data3.length()-1);
		}
		
		request.setAttribute("data", data);
		request.setAttribute("data2", data2);
		//request.setAttribute("data3", data3);
		request.setAttribute("unit", unit);
		request.setAttribute("productName", productName);
		return "showChart";
	}
	/** 
	  * @Title: save 
	  * @Description: 保存一条系统字典表中的常量信息
	  * @param 
	  * @return void
	  * @throws 
	  */
	public String getProfit(){
		long startTime=System.currentTimeMillis();
		String storeId=request.getParameter("storeId");
		String userId=(String)request.getSession().getAttribute("userId");
		String storeIds=storeService.getUserStores(userId,storeId);
		billDetailService.updatePurchasePrice(storeIds);
		long endTime=System.currentTimeMillis();
		System.err.println("耗时："+(endTime-startTime));
        return "profit";
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
	
	private List doListObj(List<Object[]> list,String startTime,String endTime,String storeId) {
       /* String[] params=new String[]
                {"productId","productName","unit","dq","account","profits"}*/
		List lists = new ArrayList();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				Object[] obj =(Object[]) list.get(i);
                BigDecimal dqsum=inventoryDetailService.findInventoryByProductId(obj[0]+"",startTime,endTime,storeId);
			    map.put("productId",obj[0]+"");
                map.put("productName",obj[1]+"");
                map.put("unit",obj[2]+"");
                map.put("dq",obj[3]+"");
                map.put("account",obj[4]+"");
                BigDecimal profits=new BigDecimal(obj[5]+"");
                map.put("profits",profits+"");
                map.put("dqsum",dqsum+"");
                map.put("finalprofits",profits.add(dqsum)+"");
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
