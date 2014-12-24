<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head lang="en">
<#assign scores = {"语文":86,"数学":78} + {"数学":87,"Java":93}>
    <title>aaaaa</title>
</head>
<body>
<div>
    ${grade}<br/>
    语文成绩是${scores.语文}
    数学成绩是${scores.数学}
    Java成绩是${scores.Java}
</div>
</body>
</html>