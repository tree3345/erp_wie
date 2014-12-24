package com.wie.basic.datasource.interceptor;

import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.ClassUtils;
import org.springframework.beans.factory.InitializingBean;

import com.wie.basic.datasource.CustomerContextHolder;

 
/**
 *  动态设置数据源拦截器
 * @file DataSourceMethodInterceptor.java
 * @package com.hoo.framework.spring.interceptor
 * @project SHMB
 * @version 1.0
 */
public class DataSourceMethodInterceptor  implements MethodInterceptor, InitializingBean {
 
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> clazz = invocation.getThis().getClass();
        String className = clazz.getName();
        if (ClassUtils.isAssignable(clazz, Proxy.class)) {
            className = invocation.getMethod().getDeclaringClass().getName();
        }
        String methodName = invocation.getMethod().getName();
        Object[] arguments = invocation.getArguments();
        
        if (className.contains("MySQL")) {
            CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_MYSQL);
        } else if (className.contains("Oracle")) {
            CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_ORACLE);
        } else if(className.contains("MsSQL")){
            	 CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_MSSQL);  	
        } else if (methodName.contains("MySQL")) {
            CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_MYSQL);
        } else if (methodName.contains("Oracle")) {
            CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_ORACLE);
        } else if (methodName.contains("MsSQL")) {
        	CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_MSSQL);
        } else {
            CustomerContextHolder.clearCustomerType();
        }
        
        /*
        if (className.contains("MySQL") || methodName.contains("MySQL")) {
            CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_MYSQL);
        } else if (className.contains("Oracle") || methodName.contains("Oracle")) {
            CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_ORACLE);
        } else {
            CustomerContextHolder.clearCustomerType();
        }
        */
        Object result = invocation.proceed();
        return result;
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		System.err.println("error...");
	}
 
}
