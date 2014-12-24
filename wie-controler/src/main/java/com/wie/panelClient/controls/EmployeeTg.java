package com.wie.panelClient.controls;


import com.wie.common.tools.encode.AES;
import com.wie.common.tools.page.Pagination;
import com.wie.erp.biz.IEmployeeService;
import com.wie.framework.controls.struts2.BaseTg;
import com.wie.panelClient.model.Employee;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @ClassName: InventoryTg
 * @Description:
 */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("employeeControl")
public class EmployeeTg extends BaseTg {
    @Autowired
    private IEmployeeService employeeService;

    private Employee employee;

    private String id;

    public String index() {
        return SUCCESS;
    }

    /**
     * @description 列表
     */
    public void getItems() {
        if (employee == null) employee = new Employee();
        String userName = request.getParameter("userName");
        String employeeName = request.getParameter("employeeName");
        employee.setUserName(userName);
        employee.setEmployeeName(employeeName);
        Pagination pagination = employeeService.getPageList(employee, page, rows, sort, order);
        List<Employee> list = pagination.getList();
        //List lists = doList(list);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"password", "eroles"});
        JSONArray jsonArray = JSONArray.fromObject(list, jsonConfig);
        String baseStr = "{\"total\":" + pagination.getTotalCount() + ",\"rows\":";
        baseStr = baseStr + jsonArray.toString() + "}";
        returnJsion(baseStr, response);
    }

    /**
     * @Title:save
     * @Description
     */
    public void save() {
        employee.setUserId(UUID.randomUUID().toString());
        employee.setPassword(AES.Encrypt(employee.getPassword()));
        if (this.employeeService.save(employee)) {
            returnJsion("{\"success\":\"true\"}", response);
        } else {
            returnJsion("{\"error\":\"true\"}", response);
        }
    }

    /**
     * @Discription 为用户指定角色
     */
    public  void saveRoleTo(){
        String data=request.getParameter("data");
        Map<String,Object> map= JSONObject.fromObject(data);
        System.out.println(data);
        if (this.employeeService.saveRoleTo(map,id)) {
            returnJsion("{\"success\":\"true\"}", response);
        } else {
            returnJsion("{\"error\":\"true\"}", response);
        }
    }
    /**
     * @param
     * @return void
     * @throws
     * @Title: update
     * @Description: 更新常量
     */
    public void update() {
        employee.setPassword(AES.Encrypt(employee.getPassword()));
        if (this.employeeService.alter(employee)) {
            returnJsion("{\"success\":\"true\"}", response);
        } else {
            returnJsion("{\"error\":\"true\"}", response);
        }
    }

    /**
     * @description 删除
     */
    public void del() {

        if (this.employeeService.deleteById(id)) {
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
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
