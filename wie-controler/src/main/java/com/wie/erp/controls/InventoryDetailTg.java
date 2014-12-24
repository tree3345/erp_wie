package com.wie.erp.controls;

import com.opensymphony.xwork2.Action;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IInventoryDetailService;
import com.wie.erp.biz.IProductService;
import com.wie.erp.biz.IPurchaseDetailService;
import com.wie.erp.biz.IStoreService;
import com.wie.erp.model.*;
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.permissions.model.Users;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** 
  * @ClassName: DictionaryTg 
  * @Description: 系统字典控制层
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("inventoryDetailControl")
public class InventoryDetailTg extends BaseTg {
    /**
     * @Fields dictionaryService : IDictionaryService业务层接口注入
     */
    @Autowired
    private IInventoryDetailService inventoryDetailService;
    @Autowired
    private IStoreService storeService;

    /**
     * @Fields dic : struts2.0接受前台信息（domain接受）
     */
    private InventoryDetail inventoryDetail;


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
        return SUCCESS;
    }

    public void getItems(){
        if(inventoryDetail==null)inventoryDetail=new InventoryDetail();

        String productId=request.getParameter("productId");
        String startTime=request.getParameter("startTime");
        String endTime =request.getParameter("endTime");
        String createBy = request.getParameter("createBy");
        String storeId=request.getParameter("storeId");
        String userId=(String)request.getSession().getAttribute("userId");
        String storeIds=storeService.getUserStores(userId,storeId);
        Inventory inventory=new Inventory();
        inventory.setStoreId(storeIds);
        inventory.setStartTime(startTime);
        inventory.setEndTime(endTime);
        if(createBy!=null){
            Users createUser = new Users();
            createUser.setName(createBy);
            inventory.setCheckBy(createUser);
        }
        Product product = new Product();
        product.setProductId(productId);
        inventoryDetail.setProduct(product);
        inventoryDetail.setInventory(inventory);

        Pagination pagination = inventoryDetailService.getPageDetailList(inventoryDetail, page, rows, sort, order);
        List<InventoryDetail> list = pagination.getList();
        List lists = doList(list);
        JSONArray jsonArray = JSONArray.fromObject(lists);
        String baseStr = "{\"total\":" + pagination.getTotalCount() + ",\"rows\":";
        baseStr = baseStr + jsonArray.toString() + "}";
        returnJsion(baseStr,response);
    }

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
    private List<Map<String, String>> doList(List<InventoryDetail> list) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
        if(list != null && list.size() > 0){
            for(int i =0;i<list.size();i++){
                Map<String,String> map = new HashMap<String, String>();
                InventoryDetail inventoryDetail1 = list.get(i);
                map.put("inventory.code", inventoryDetail1.getInventory().getCode());
                map.put("price",inventoryDetail1.getPrice()+"");
                map.put("damageQuantity",inventoryDetail1.getDamageQuantity()+"");
                map.put("inventory.checkDate", format.format(inventoryDetail1.getInventory().getCheckDate()));
                map.put("inventory.createBy.name",inventoryDetail1.getInventory().getCreateBy().getName());
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

    public InventoryDetail getInventoryDetail() {
        return inventoryDetail;
    }

    public void setInventoryDetail(InventoryDetail inventoryDetail) {
        this.inventoryDetail = inventoryDetail;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
	
}
