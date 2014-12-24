/**   
  * @Title: FreeMarkerTemplateEngine.java 
  * @Package com.tgyt.templateEngine.freemarker 
  * @Description: 
  * @author zhangfeng 13940488705@163.com 
  * @date 2011-8-9 下午03:05:32 
  * @version V1.0   
  */

package com.wie.templateEngine.freemarker;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wie.templateEngine.Context;
import com.wie.templateEngine.MergeTemplateException;
import com.wie.templateEngine.Template;
import com.wie.templateEngine.support.TemplateEngineSupport;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/** 
 * @ClassName: FreeMarkerTemplateEngine 
 * @Description: 提供FilePathTemplate
 *  
 */
public class FreeMarkerTemplateEngine extends TemplateEngineSupport {
    private final Log log = LogFactory.getLog( this.getClass() );
    
	public void mergeFileTemplate(Template pTemplate, Context pContext, Writer pWriter) throws MergeTemplateException {
		Configuration cfg = FreeMarkerHelper.getConfiguration();
		String path = pTemplate.getResource();
		
		if ( log.isDebugEnabled() ){
          log.debug("模板文件: \"" + path + "\" 采用freemarker引擎进行合并.");
          log.debug("模板文件: \"" + path + "\" 输入编码方式是：" + pTemplate.getInputEncoding());
		}
		

		freemarker.template.Template  template = null;
		try {
			template = cfg.getTemplate(path);
			if ( pTemplate.getInputEncoding() != null ){
				template.setEncoding(pTemplate.getInputEncoding());
			}			
    	} catch (IOException e) {
 			final String MSG = "合并模板文件 \"" + path + "\"  失败!" + e.getMessage();
 			if ( log.isErrorEnabled()){
			  log.error(MSG, e);
 			}
			throw new MergeTemplateException(MSG, e);
		}
    	
		try {
			template.process(pContext.getParameters(), pWriter);
		} catch (TemplateException e) {
 			final String MSG = "合并模板文件 \"" + path + "\"  失败!" + e.getMessage();
 			if ( log.isErrorEnabled()){
			  log.error(MSG, e);
 			}
			throw new MergeTemplateException(MSG, e);
		} catch (IOException e) {
 			final String MSG = "合并模板文件 \"" + path + "\"  失败!" + e.getMessage();
 			if ( log.isErrorEnabled()){
			  log.error(MSG, e);
 			}
			throw new MergeTemplateException(MSG, e);
		}
	
	}


}
