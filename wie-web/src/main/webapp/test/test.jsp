<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2014/11/18
  Time: 11:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.wie.erp.model.Product" %>
<%@ page import="java.lang.reflect.Field" %>
<html>
<head>
    <title></title>
</head>
<body>
<%
    for (Field field : Product.class.getFields()) {
        System.out.println(field);
    }
    ;
%>
</body>
</html>
