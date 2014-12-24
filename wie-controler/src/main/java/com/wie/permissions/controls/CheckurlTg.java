package com.wie.permissions.controls;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wie.framework.controls.struts2.BaseTg;
import com.wie.permissions.common.HasParent;
import com.wie.permissions.model.Resources;


/** 
  * @ClassName: ActionsTg 
  * @Description: 操作管理控制类 
  *
  */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller(value="permissions.checkurlTgControl")
public class CheckurlTg extends BaseTg {
	
	private String pid;
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	/** 
	  * @Title: check 
	  * @Description: 空url时的处理函数
	  * @return String
	  * @throws 
	  */
	public String check(){
//		System.out.println(pid);
		HasParent.returnCurrentObj(pid,request);
//		System.out.println(request.getAttribute("jsonOne"));
//		System.out.println(request.getAttribute("jsonTwo"));
		return SUCCESS;
	}

	
}
