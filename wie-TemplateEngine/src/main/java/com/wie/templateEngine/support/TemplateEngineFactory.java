/**   
  * @Title: TemplateEngineFactory.java 
  * @Package com.tgyt.templateEngine.support 
  * @Description: 
  * @author zhangfeng 13940488705@163.com 
  */

package com.wie.templateEngine.support;

import java.util.HashMap;
import java.util.Map;

//import com.tgyt.templateEngine.jxp.JxpTemplateEngine;


import com.wie.templateEngine.TemplateEngine;
import com.wie.templateEngine.freemarker.FreeMarkerTemplateEngine;
import com.wie.templateEngine.velocity.VelocityTemplateEngine;
import com.wie.templateEngine.webmacro.WebMacroTemplateEngine;

/** 
 * @ClassName: TemplateEngineFactory 
 * @Description: 根据引擎类别选择引擎实例
 *  
 */
public class TemplateEngineFactory {
	private static Map ENGINES = new HashMap();

	static {
		//Velocity
		if ( isExistClass("org.apache.velocity.app.VelocityEngine") ){
			VelocityTemplateEngine ve = new VelocityTemplateEngine();
			ve.init();
			ENGINES.put(TemplateType.VELOCITY, ve);
		}
		//FreeMarker
		if ( isExistClass("freemarker.template.Template") ){
			ENGINES.put(TemplateType.FREE_MARKER, new FreeMarkerTemplateEngine());
		}
		//WebMacro
		if ( isExistClass("org.webmacro.WM") ){		
			WebMacroTemplateEngine wm = new WebMacroTemplateEngine();
			wm.init();
			ENGINES.put(TemplateType.WEB_MACRO, wm);
		}
		//JxpProcessor
//		if ( isExistClass("org.onemind.jxp.JxpProcessor") ){
//			ENGINES.put(TemplateType.JXP, new JxpTemplateEngine());
//		}
	}

	private TemplateEngineFactory() {

	}

	/**
	 * 检查当前ClassLoader种,是否存在指定class
	 * @param pClass
	 * @return
	 */
	private static boolean isExistClass(String pClass) {
		try {
			Class.forName(pClass);
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	/**
	 * 获取模板引擎.
	 * @param
	 * @return
	 */
	public static TemplateEngine getInstance(TemplateType pType) {
		if (pType == null) {
			return null;
		}
		if (ENGINES.containsKey(pType) == false) {
			throw new IllegalArgumentException("不支持的模板类别:" + pType.getType());
		}
		return (TemplateEngine) ENGINES.get(pType);
	}
}