package com.wie.panelClient.controls;


import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IEmployeeService;
import com.wie.erp.biz.IEroleService;
import com.wie.erp.biz.IFunctionsService;
import com.wie.erp.biz.IModuleService;
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.panelClient.model.Employee;
import com.wie.panelClient.model.Erole;
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
@Controller("permissionControl")
public class PermissionTg extends BaseTg {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IEroleService eroleService;

    @Autowired
    private IFunctionsService functionsService ;

    @Autowired
    private IModuleService moduleService;

    /**
     * 获得employee列表
     */
   public void emps(){
       List<Employee> employees=employeeService.findList("from Employee order by userName");
       JsonConfig jsonConfig=new JsonConfig();
       jsonConfig.setExcludes(new String []{"eroles","password"});
       JSONArray jsonArray=JSONArray.fromObject(employees,jsonConfig);
       outJsonString(response,jsonArray.toString());
   }
    public void doEmp(){
        String properties=request.getParameter("rqStr");
        String[] employeeStr=properties.split("#");
        Employee employee=new Employee();
        employeeService.save(employee);

    }

    /**
     * 获得erole列表
     */
    public void eroles(){
        List<Erole> eroles =eroleService.findList("from Erole order by roleName");
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String []{"employees","modules","functions"});
        JSONArray jsonArray=JSONArray.fromObject(eroles,jsonConfig);
        outJsonString(response,jsonArray.toString());
    }

    /**
     * 获得模块列表
     */
    public void modules(){
        List<Module> modules =moduleService.findList("from Module order by moduleName");
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String []{"eroles","functions"});
        JSONArray jsonArray=JSONArray.fromObject(modules,jsonConfig);
        outJsonString(response,jsonArray.toString());
    }

    /**
     * 获得模块列表
     */
    public void functions(){
        List<Functions> functionses =functionsService.findList("from Functions order by functionName");
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String []{"eroles"});
        JSONArray jsonArray=JSONArray.fromObject(functionses,jsonConfig);
        outJsonString(response,jsonArray.toString());
    }

}
