/**   
 * @Title: StrTemplateUtil.java 
 * @Package com.tgyt.templateEngine.support 
 * @Description: 
 * @author zhangfeng 13940488705@163.com 
 * @date 2011-8-9 下午03:16:31 
 * @version V1.0   
 */

package com.wie.templateEngine.support;

import java.io.StringWriter;

import com.wie.templateEngine.Context;
import com.wie.templateEngine.MergeTemplateException;
import com.wie.templateEngine.Template;
import com.wie.templateEngine.TemplateEngine;

/**
 * @ClassName: StrTemplateUtil
 * @Description: 模板合成
 * 
 */
public class StrTemplateUtil {
	private StrTemplateUtil() {

	}

	public static String merge(String pTemplateStr, Context pContext)
			throws MergeTemplateException {
		if (pTemplateStr == null) {
			return null;
		}
		if (pContext == null) {
			return pTemplateStr;
		}
		Template template = new DefaultTemplate();
		template.setResource(DefaultTemplate.STR_PREFIX + pTemplateStr);
		StringWriter out = new StringWriter();
		TemplateEngine engine = TemplateEngineFactory.getInstance(TemplateType.VELOCITY);
		engine.mergeTemplate(template, pContext, out);
		return out.getBuffer().toString();
	}
}
