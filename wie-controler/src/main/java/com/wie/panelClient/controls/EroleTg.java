package com.wie.panelClient.controls;


import com.wie.common.tools.encode.AES;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IEroleService;
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.panelClient.model.Erole;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName: InventoryTg
 * @Description:
 *
 */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("eroleControl")
public class EroleTg extends BaseTg {
    @Autowired
    private IEroleService eroleService ;

    private Erole erole;

    private String id;

    public String index(){
        return SUCCESS;
    }

    /**
     * @description 列表
     */
    public void getItems(){
        if(erole==null)erole=new Erole();
        String roleName=request.getParameter("roleName");
        erole.setRoleName(roleName);
        Pagination pagination = eroleService.getPageList(erole, page, rows,sort,order);
        List<Erole> list = pagination.getList();
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String[]{"employees","modules","functions"});
        JSONArray jsonArray = JSONArray.fromObject(list,jsonConfig);
        String baseStr = "{\"total\":" + pagination.getTotalCount() + ",\"rows\":";
        baseStr = baseStr + jsonArray.toString() + "}";
        returnJsion(baseStr,response);
    }

    /**
     * @Description 用户拥有角色
     */
    public void getItemsEmp(){
        List eroles=eroleService.getEroleByEmp(id);
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String[]{"employees","modules","functions"});
        JSONArray jsonArray = JSONArray.fromObject(eroles,jsonConfig);
        returnJsion(jsonArray+"",response);
    }

    /**
     * @Description 角色分配列表，含有全部角色，用户拥有角色为选中状态
     */
    public void getItemsEmpU(){
        List<Erole> eroleEmp=eroleService.getEroleByEmp(id);
        List<Erole> eroles=eroleService.findList("from Erole");
        for(Erole role:eroles){
            if(eroleEmp.contains(role))
                 role.setIscheck(true);
        }
        //eroles.removeAll(eroleEmp);
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String[]{"employees","modules","functions"});
        JSONArray jsonArray = JSONArray.fromObject(eroles,jsonConfig);
        returnJsion(jsonArray+"",response);
    }

    /**
     * @Title:save
     * @Description
     */
    public void save(){
        erole.setRoleId(UUID.randomUUID().toString());
        if(this.eroleService.save(erole)){
            returnJsion("{\"success\":\"true\"}",response);
        }else{
            returnJsion("{\"error\":\"true\"}",response);
        }
    }

    /**
     * @Description 分配模块
     */
    public void saveModulesTo(){
        String data=request.getParameter("data");
        Map<String,Object> map= JSONObject.fromObject(data);
        System.out.println(data);
        if (this.eroleService.saveModulesTo(map,id)) {
            returnJsion("{\"success\":\"true\"}", response);
        } else {
            returnJsion("{\"error\":\"true\"}", response);
        }
    }
    /**
     * @Description 分配模块
     */
    public void saveFunctionsTo(){
        String data=request.getParameter("data");
        Map<String,Object> map= JSONObject.fromObject(data);
        System.out.println(data);
        if (this.eroleService.saveFunctionsTo(map,id)) {
            returnJsion("{\"success\":\"true\"}", response);
        } else {
            returnJsion("{\"error\":\"true\"}", response);
        }
    }
    /**
     * @Title: update
     * @Description: 更新常量
     * @param
     * @return void
     * @throws
     */
    public void update() {
        if (this.eroleService.alter(erole)) {
            returnJsion("{\"success\":\"true\"}", response);
        } else {
            returnJsion("{\"error\":\"true\"}", response);
        }
    }

    /**
     * @description 删除
     */
    public void del() {
        if (this.eroleService.deleteById(id)) {
            returnJsion("{\"success\":\"true\"}", response);
        } else {
            returnJsion("{\"error\":\"true\"}", response);
        }
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

    public Erole getErole() {
        return erole;
    }

    public void setErole(Erole erole) {
        this.erole = erole;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
