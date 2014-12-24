/**   
 * @Title: UserDataUncoder.java 
 * @Package com.tgyt.tree 
 * @Description: 
 * @author zhangfeng 13940488705@163.com 
 * @date 2011-8-9 下午03:59:35 
 * @version V1.0   
 */

package com.wie.tree;

/**
 * @ClassName: UserDataUncoder
 * @Description: 负责业务对象解码,分解出ID和parentID
 * 
 */
public interface UserDataUncoder {
	public Object getID(Object pUserData) throws UncodeException;

	public Object getParentID(Object pUserData) throws UncodeException;
}
