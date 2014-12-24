package com.wie.panelClient.controls;


import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IModuleService;
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.panelClient.model.Functions;
import com.wie.panelClient.model.Module;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: InventoryTg
 * @Description:
 *
 */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("moduleControl")
public class ModuleTg extends BaseTg {
    @Autowired
    private IModuleService moduleService ;

    private Module module;

    private Integer id;

    public String index(){
        return SUCCESS;
    }

    /**
     * @description 列表
     */
    public void getItems(){
        if(module==null)module=new Module();
        String moduleName=request.getParameter("moduleName");
        String viewName=request.getParameter("viewName");
        module.setModuleName(moduleName);
        module.setViewName(viewName);
        Pagination pagination = moduleService.getPageList(module, page, rows,sort,order);
        List<Module> list = pagination.getList();
        JsonConfig jsonConfig =new JsonConfig();
        jsonConfig.setExcludes(new String[]{"eroles"});
        JSONArray jsonArray = JSONArray.fromObject(list,jsonConfig);
        String baseStr = "{\"total\":" + pagination.getTotalCount() + ",\"rows\":";
        baseStr = baseStr + jsonArray.toString() + "}";
        returnJsion(baseStr,response);
    }

    public void getJsonAll(){
        List<Module> list=moduleService.findList("from Module");
        JsonConfig jsonConfig =new JsonConfig();
        jsonConfig.setExcludes(new String[]{"eroles","remark","ischeck"});
        JSONArray jsonArray = JSONArray.fromObject(list,jsonConfig);
        returnJsion(jsonArray.toString(),response);
    }
    /**
     * @Description 角色拥有模块
     */
    public void getItemsrole(){
        String roleId=request.getParameter("roleId");
        List modules=moduleService.getModulesByErole(roleId);
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String[]{"eroles"});
        JSONArray jsonArray = JSONArray.fromObject(modules,jsonConfig);
        returnJsion(jsonArray+"",response);
    }

    /**
     * @Description 模块分配列表，含有全部模块，角色拥有的模块为选中状态
     */
    public void getItemsroleU(){
        String roleId=request.getParameter("roleId");
        List<Module> modulesRole=moduleService.getModulesByErole(roleId);
        List<Module> modules=moduleService.findList("from Module");
        for(Module ml:modules){
            if(modulesRole.contains(ml))
                ml.setIscheck(true);
        }
        //eroles.removeAll(eroleEmp);
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String[]{"eroles"});
        JSONArray jsonArray = JSONArray.fromObject(modules,jsonConfig);
        returnJsion(jsonArray+"",response);
    }
    /**
     * @Title:save
     * @Description
     */
    public void save(){
        module.setModuleId(moduleService.getMaxId()+1);
        if(this.moduleService.save(module)){
            returnJsion("{\"success\":\"true\"}",response);
        }else{
            returnJsion("{\"error\":\"true\"}",response);
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
        if (this.moduleService.alter(module)) {
            returnJsion("{\"success\":\"true\"}", response);
        } else {
            returnJsion("{\"error\":\"true\"}", response);
        }
    }

    /**
     * @description 删除
     */
    public void del() {
        if (this.moduleService.deleteById(id)) {
            returnJsion("{\"success\":\"true\"}", response);
        } else {
            returnJsion("{\"error\":\"true\"}", response);
        }
    }

    /**
     * @Description 添加功能
     */
    public void addFunctions(){
       moduleService.saveModuleFuncions();
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

    public IModuleService getModuleService() {
        return moduleService;
    }

    public void setModuleService(IModuleService moduleService) {
        this.moduleService = moduleService;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
