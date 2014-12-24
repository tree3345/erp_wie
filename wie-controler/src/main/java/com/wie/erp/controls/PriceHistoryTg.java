package com.wie.erp.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wie.erp.biz.IStoreService;
import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IProductPriceHistoryService;
import com.wie.erp.biz.IProductService;
import com.wie.erp.model.ProductPriceHistory;
import com.wie.framework.controls.struts2.BaseTg;


/** 
  * @ClassName: DictionaryTg 
  * @Description: 系统字典控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("priceHistoryControl")
public class PriceHistoryTg extends BaseTg {
	/** 
	  * @Fields dictionaryService : IDictionaryService业务层接口注入
	  */ 
	@Autowired
	private IProductPriceHistoryService productPriceHistoryService;
	@Autowired
	private IProductService productService;
	@Autowired
	private IStoreService storeService;
	
	private ProductPriceHistory productPriceHistory;

	/** 
	  * @Title: index 
	  * @Description: 系统字典首页
	  * @param @return
	  * @return String
	  * @throws 
	  */
	public String index(){
		String status=request.getParameter("status");
		request.setAttribute("status", status);
		return SUCCESS;
	}
	
	/** 
	  * @Title: getItems 
	  * @Description: 系统字典表所有常量信息
	  * @param 
	  * @return void
	  * @throws 
	  */
	public void getItems(){
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		String userId=(String)request.getSession().getAttribute("userId");
		String storeId=request.getParameter("storeId");
		String storeIds=storeService.getUserStores(userId,storeId);
		productPriceHistory.setStoreId(storeIds);
		productPriceHistory.setStartTime(startTime);
		productPriceHistory.setEndTime(endTime);
		Pagination pagination=productPriceHistoryService
				.getPageList(productPriceHistory, page, rows, sort, order);
		List<ProductPriceHistory> list = pagination.getList();
		List<Map<String, String>> lists = doList(list);
		JSONArray jsonArray = JSONArray.fromObject(lists);
		String baseStr = "{\"total\":" + pagination.getTotalCount()+ ",\"rows\":";
		baseStr = baseStr + jsonArray.toString() + "}";
		returnJsion(baseStr, response);

	}

	public String showCharts(){
		String postData=request.getParameter("postData");
		String productId=request.getParameter("productId");
		String status=request.getParameter("status");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		
		String storeId=(String)request.getSession().getAttribute("storeId");
		String hql="from ProductPriceHistory pph where (pph.storeId='"+storeId+"' or pph.storeId is null) and pph.productId='"+productId+"' and pph.status="+status;
		if(!"".equals(startTime)&&startTime!=null)	
			hql+="and pph.activeDT > '"+startTime.replace("_", " ")+"' ";
		if(!"".equals(endTime)&&endTime!=null)	
			hql+="and pph.activeDT < '"+endTime.replace("_", " ")+"' ";
		hql	+=" order by pph.activeDT asc";
		List<ProductPriceHistory> list=productPriceHistoryService.findList(hql);
		String data="";
		for(ProductPriceHistory pph:list)
		{
			Date date=pph.getActiveDT();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmm");
			String dateStr=format.format(date);
			int year=Integer.parseInt(dateStr.substring(0, 4));
			int month=Integer.parseInt(dateStr.substring(4, 6));
			int day=Integer.parseInt(dateStr.substring(6, 8));
			int hour=Integer.parseInt(dateStr.substring(9, 11));
			int min=Integer.parseInt(dateStr.substring(11, 13));
			data+="[Date.UTC("+year+", "+(month-1)+", "+(day)+","+(hour)+","+(min)+"), "+pph.getUnitPrice()+"],";
		}
		if(list.size()>0)
		  data="["+data.substring(0, data.length()-1)+"]";
		System.err.println("数据"+data);
		
		request.setAttribute("data", data);
		return "showChart";
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
	private List doList(List<ProductPriceHistory> list) {
		List lists = new ArrayList();
		if(list != null && list.size() > 0){
			for(int i =0;i<list.size();i++){
				Map<String,String> map = new HashMap<String, String>();
				ProductPriceHistory productPriceHistory = list.get(i);
				map.put("unitPrice", productPriceHistory.getUnitPrice()+"");
				map.put("activeDT", productPriceHistory.getActiveDT()+"");
				map.put("storeId",productPriceHistory.getStoreId());
				map.put("status", productPriceHistory.getStatus()+"");
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

	public ProductPriceHistory getProductPriceHistory() {
		return productPriceHistory;
	}

	public void setProductPriceHistory(ProductPriceHistory productPriceHistory) {
		this.productPriceHistory = productPriceHistory;
	}

}
