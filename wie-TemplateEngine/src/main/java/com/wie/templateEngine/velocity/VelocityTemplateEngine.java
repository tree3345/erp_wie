/**   
  * @Title: VelocityTemplateEngine.java 
  * @Package com.tgyt.templateEngine.velocity 
  * @Description: 
  * @version V1.0   
  */

package com.wie.templateEngine.velocity;

import java.io.Writer;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.wie.templateEngine.Context;
import com.wie.templateEngine.MergeTemplateException;
import com.wie.templateEngine.Template;
import com.wie.templateEngine.support.TemplateEngineSupport;

/** 
 * @ClassName: VelocityTemplateEngine 
 * @Description: velocity模板引擎
 *  
 */
public class VelocityTemplateEngine extends TemplateEngineSupport{
	
    private final Log log = LogFactory.getLog( this.getClass() );
    private VelocityEngine ve = null;

    public void init(){
    	ve = VelocityHelper.getVeocityEngine(VelocityHelper.getDefaultProperties());
    }
    
    public void init(Properties pProperties){
    	Properties props = VelocityHelper.getDefaultProperties();
    	props.putAll(pProperties);
    	ve = VelocityHelper.getVeocityEngine(props);	
    }
    
	public void mergeStringTemplate(Template pTemplate,
			Context pContext,
            Writer pWriter) throws MergeTemplateException{
		if ( ve == null ){
			throw new NullPointerException("没有初始化模板引擎!");
		}
		VelocityContext context = VelocityHelper.context2VelocityContext(pContext);
        String templateStr = this.getStrTemplate(pTemplate);		
		try {
			ve.evaluate(context, pWriter,"VelocityMergeString.log", templateStr);
		} catch (Exception e) {
			final String MSG =
				"合并字符串 \"" + templateStr + "\" 失败!" + e.getMessage();
 			if ( log.isErrorEnabled()){
				  log.error(MSG, e);
			}
			throw new MergeTemplateException(MSG, e);
		}
	}


	public void mergeFileTemplate(Template pTemplate, Context pContext, Writer pWriter) throws MergeTemplateException {
		if ( ve == null ){
			throw new NullPointerException("没有初始化模板引擎!");
		}
		
		VelocityContext context = VelocityHelper.context2VelocityContext(pContext);
		String path = pTemplate.getResource();
		
		if ( log.isDebugEnabled() ){
          log.debug("模板文件: \"" + path + "\" 采用Velocity引擎进行合并.");
          log.debug("模板文件: \"" + path + "\" 输入编码方式是：" + pTemplate.getInputEncoding());
		}
		
		org.apache.velocity.Template template = null;
		try {
			if ( pTemplate.getInputEncoding() == null ){
			  template = ve.getTemplate(path);
			}else{
				template = ve.getTemplate(path, pTemplate.getInputEncoding());	
			}
		} catch (Exception e) {
 			final String MSG = "合并模板文件 \"" + path + "\"  失败!" + e.getMessage();
 			if ( log.isErrorEnabled()){
 				  log.error(MSG, e);
 	 			}
			throw new MergeTemplateException(MSG, e);
		}
		
		try {
			template.merge(context,pWriter);
		} catch (Exception e) {
 			final String MSG = "合并模板文件 \"" + path + "\"  失败!" + e.getMessage();
 			if ( log.isErrorEnabled()){
				  log.error(MSG, e);
			}
			throw new MergeTemplateException(MSG, e);
		}
		
		
	}

}
