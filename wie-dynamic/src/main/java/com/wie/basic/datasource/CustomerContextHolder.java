package com.wie.basic.datasource;

public class CustomerContextHolder {

	public static final String DATA_SOURCE_MYSQL = "dataSource_mysql";
	
	public static final String DATA_SOURCE_ORACLE = "dataSource_oracle";
	
	public static final String DATA_SOURCE_MSSQL = "dataSource_mssql";
	
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	
	public static void setCustomerType(String customerType) {
		contextHolder.set(customerType);
	}
	
	public static String getCustomerType() {
		return contextHolder.get();
	}
	
	public static void clearCustomerType() {
		contextHolder.remove();
	}
}
