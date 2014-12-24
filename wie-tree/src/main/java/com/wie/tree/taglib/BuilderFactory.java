/**   
 * @Title: BuilderFactory.java 
 * @Package com.tgyt.tree.taglib 
 * @Description: 
 * @author zhangfeng 13940488705@163.com 
 * @date 2011-8-9 下午04:57:48 
 * @version V1.0   
 */

package com.wie.tree.taglib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wie.common.tools.util.ClassUtils;
import com.wie.tree.support.WebTreeBuilder;
import com.wie.tree.support.WebTreeNode;
import com.wie.tree.xtree.CheckXLoadTreeBuilder;
import com.wie.tree.xtree.CheckXTreeBuilder;
import com.wie.tree.xtree.CompositeXLoadTreeBuilder;
import com.wie.tree.xtree.CompositeXTreeBuilder;
import com.wie.tree.xtree.PrvCheckXTreeBuilder;
import com.wie.tree.xtree.RadioXLoadTreeBuilder;
import com.wie.tree.xtree.RadioXTreeBuilder;
import com.wie.tree.xtree.XLoadTreeBuilder;
import com.wie.tree.xtree.XTreeBuilder;

/**
 * @ClassName: BuilderFactory
 * @Description: 构建树factory
 * 
 */
public class BuilderFactory {

	private static final Log logger = LogFactory.getLog(NodeFactory.class);

	private BuilderFactory() {

	}

	public static WebTreeBuilder getInstance(String pClassName) {

		if ("default".equalsIgnoreCase(pClassName)) {
			return new CompositeXTreeBuilder();
		}

		if ("XTree".equalsIgnoreCase(pClassName)) {
			return new XTreeBuilder();
		}
		if ("XLoadTree".equalsIgnoreCase(pClassName)) {
			return new XLoadTreeBuilder();
		}

		if ("RadioXTree".equalsIgnoreCase(pClassName)) {
			return new RadioXTreeBuilder();
		}
		if ("RadioXLoadTree".equalsIgnoreCase(pClassName)) {
			return new RadioXLoadTreeBuilder();
		}

		if ("CheckXTree".equalsIgnoreCase(pClassName)) {
			return new CheckXTreeBuilder();
		}
		if ("PrvCheckXTree".equalsIgnoreCase(pClassName)) {
			return new PrvCheckXTreeBuilder();
		}

		if ("CheckXLoadTree".equalsIgnoreCase(pClassName)) {
			return new CheckXLoadTreeBuilder();
		}

		if ("CompositeXTree".equalsIgnoreCase(pClassName)) {
			return new CompositeXTreeBuilder();
		}
		if ("CompositeXLoadTree".equalsIgnoreCase(pClassName)) {
			return new CompositeXLoadTreeBuilder();
		}


		try {
			Object obj = ClassUtils.getNewInstance(pClassName);
			if (obj instanceof WebTreeNode == false) {
				final String msg = "类:" + pClassName + "的父类不是:"
						+ WebTreeBuilder.class.getName();
				logger.error(msg);
				throw new IllegalArgumentException(msg);
			}
			return (WebTreeBuilder) obj;
		} catch (Exception e) {
			final String msg = "创建类:" + pClassName + "实例失败";

			throw new CreateObjectException(msg, e);

		}
	}
}
