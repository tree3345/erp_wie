/**   
  * @Title: TgRuntimeException.java 
  * @Package com.tgyt.common.tools.core 
  * @Description: 
  * @author zhangfeng 13940488705@163.com 
  * @date 2011-8-9 上午11:15:27 
  * @version V1.0   
  */

package com.wie.common.tools.core;

/** 
 * @ClassName: TgRuntimeException 
 * @Description: 运行时异常处理
 *  
 */
public class TgRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public TgRuntimeException() {
		super();
	}

	public TgRuntimeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TgRuntimeException(String arg0) {
		super(arg0);
	}

	public TgRuntimeException(Throwable arg0) {
		super(arg0);
	}
}
