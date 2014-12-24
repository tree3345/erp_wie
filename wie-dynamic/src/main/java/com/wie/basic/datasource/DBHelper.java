package com.wie.basic.datasource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DBHelper {

	Statement stmt;
	// Statement uploadStmt;
	Connection con;
	// Connection uploadCon;
	CallableStatement ct;

	private static DBHelper dbHelper;

	public DBHelper() {

		String JDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";// SQL数据库引擎

		String connectDB = "jdbc:sqlserver://192.168.1.200:1433;databaseName=JuNongZYD";

		try {
			Class.forName(JDriver);// 加载数据库引擎，返回给定字符串名的类
		} catch (ClassNotFoundException e) {

			e.printStackTrace();

			System.out.println("加载数据库引擎失败");

			System.exit(0);
		}

		try {
			String user = "sa";

			String password = "sa";

			con = DriverManager.getConnection(connectDB, user, password);// 连接数据库对象

			// uploadCon =
			// DriverManager.getConnection("jdbc:sqlserver://218.61.195.76:9001;databaseName=JuNongZYD",
			// "sa",
			// "dljn76sa2013");

			System.out.println("连接数据库成功");

		} catch (SQLException e) {

			e.printStackTrace();
			System.out.println("连接超时");
		}
	}

	public static DBHelper getDBHelper() {

		if (dbHelper == null) {
			dbHelper = new DBHelper();
		}

		return dbHelper;
	}
	public static void main(String[] args) {
		DBHelper dbh=new DBHelper();
		dbh.getDBHelper();
		try {
			System.out.println(dbh.getvege(""));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getvege(String sqlStr) throws SQLException
	{
		String result="";
		sqlStr = "select ProductID,SalesPrice from JN_ProductPrice";
        stmt = con.createStatement();
		ResultSet resultSet = stmt.executeQuery(sqlStr);

		if (resultSet != null) {

			while (resultSet.next()) {
				result += resultSet.getString(1) + "#" + resultSet.getDouble(2);
				result += "|";
			}

			if (!result.equals("")) {

				result = result.substring(0, result.lastIndexOf("|"));
			}

			resultSet = null;

		} else {
			result = "null";
		}
		return result;
	}
	public String uploadSale(ArrayList<String> arraylist1,ArrayList<String> arraylist2)
	{
		String result="";
		try {
			stmt = con.createStatement();
			con.setAutoCommit(false);
			for (int i = 0; i < arraylist1.size(); i++) {
				stmt.addBatch(arraylist1.get(i));
			}
			for (int i = 0; i < arraylist2.size(); i++) {
				stmt.addBatch(arraylist2.get(i));
			}
			stmt.executeBatch();
			// 事务提交
			con.commit();
			// 设置为自动提交,改为TRUE
			con.setAutoCommit(true);
			result = "1";
		} catch (SQLException se) {
			se.printStackTrace();
			try {
				if (con != null) {
					con.rollback();
					con.setAutoCommit(true);
				}
				result = "null";
			} catch (SQLException se1) {
				se1.printStackTrace();
				result = "null";
			}
		}
		return result;
	}
}
