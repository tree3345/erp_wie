package com.wie.panelClient.controls;


import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IFunctionsService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: InventoryTg
 * @Description:
 *
 */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("functionControl")
public class FunctionsTg extends BaseTg {
    @Autowired
    private IFunctionsService functionsService ;

    @Autowired
    private IModuleService moduleService;

    private Functions functions;

    private Integer id;

    public String index(){
        return SUCCESS;
    }

    /**
     * @description 列表
     */
    public void getItems(){

        if(functions==null)functions=new Functions();
        String functionsName=request.getParameter("functionName");
        String controlName=request.getParameter("controlName");
        String moduleId=request.getParameter("moduleId");
        functions.setFunctionName(functionsName);
        functions.setControlName(controlName);
        Module module=new Module();
        if(moduleId!=null&&!moduleId.equals("")) {
            module.setModuleId(Integer.parseInt(moduleId));
            functions.setModule(module);
        }

        Pagination pagination = functionsService.getPageList(functions, page, rows,sort,order);
        List<Functions> list = pagination.getList();
        List lists=doList(list);
        JSONArray jsonArray = JSONArray.fromObject(lists);
        String baseStr = "{\"total\":" + pagination.getTotalCount() + ",\"rows\":";
        baseStr = baseStr + jsonArray.toString() + "}";
        returnJsion(baseStr,response);
    }
    /**
     * @Description 角色拥有功能
     */
    public void getItemsrole(){
        String roleId=request.getParameter("roleId");
        List<Functions> functionses=functionsService.getFunctionsByErole(roleId);
        for(Functions fc:functionses){
            fc.setModuleName(fc.getModule().getModuleName());
        }
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String[]{"eroles"});
        JSONArray jsonArray = JSONArray.fromObject(functionses,jsonConfig);
        returnJsion(jsonArray+"",response);
    }

    /**
     * @Description 功能分配列表，含有全部功能，角色拥有的功能为选中状态
     */
    public void getItemsroleU(){
        String roleId=request.getParameter("roleId");
        List<Functions> functionssRole=functionsService.getFunctionsByErole(roleId);
        List<Functions> functions=functionsService.findList("from Functions order by module.moduleId,functionId");
        for(Functions fc:functions){
            fc.setModuleName(fc.getModule().getModuleName());
            if(functionssRole.contains(fc))
                fc.setIscheck(true);
        }
        //eroles.removeAll(eroleEmp);
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String[]{"eroles"});
        JSONArray jsonArray = JSONArray.fromObject(functions,jsonConfig);
        returnJsion(jsonArray+"",response);
    }


    /**
     * @Title:save
     * @Description
     */
    public void save(){
        int maxid=functionsService.getMaxId();
        functions.setFunctionId(maxid+1);
        if(this.functionsService.save(functions)){
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
        if (this.functionsService.alter(functions)) {
            returnJsion("{\"success\":\"true\"}", response);
        } else {
            returnJsion("{\"error\":\"true\"}", response);
        }
    }

    /**
     * @description 删除
     */
    public void del() {
        if (this.functionsService.deleteById(id)) {
            returnJsion("{\"success\":\"true\"}", response);
        } else {
            returnJsion("{\"error\":\"true\"}", response);
        }
    }

    private List doList(List<Functions> list) {
        List lists = new ArrayList();
        if(list != null && list.size() > 0){
            for(int i =0;i<list.size();i++){
                Map<String,String> map = new HashMap<String, String>();
                Functions function = list.get(i);
                map.put("functionId", function.getFunctionId() + "");
                map.put("functionName",function.getFunctionName());
                map.put("controlName",function.getControlName());
                map.put("remark",function.getRemark());
                map.put("moduleId",function.getModule().getModuleId()+"");
                map.put("moduleName",function.getModule().getModuleName()+"");
                lists.add(map);
            }
        }
        return lists;
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


    public Functions getFunctions() {
        return functions;
    }

    public void setFunctions(Functions functions) {
        this.functions = functions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
