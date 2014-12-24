package com.wie.erp.controls;


import com.wie.erp.biz.IAbcService;
import com.wie.framework.controls.struts2.BaseTg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
  * @ClassName: InventoryTg 
  * @Description: 
  *  
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("abcControl")
public class AbcTg extends BaseTg {
@Autowired
private IAbcService abcService ;

    public String index(){
      String path =request.getRealPath("/");
      abcService.generate(path);
      return SUCCESS;
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
}
