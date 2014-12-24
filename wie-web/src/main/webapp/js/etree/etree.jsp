<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>jQuery EasyUI</title>
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/etree/jquery.etree.js"/>"></script>
	<script type="text/javascript">
		$(function(){
			$('#tt').etree({
				url: 'tree_data.json'
			});
		});
	</script>
</head>
<body>
	<h1>Editable Tree</h1>
	<div style="margin-bottom:10px">
		<a href="#" onclick="javascript:$('#tt').etree('create')">Create</a>
		<a href="#" onclick="javascript:$('#tt').etree('edit')">Edit</a>
		<a href="#" onclick="javascript:$('#tt').etree('destroy')">Destroy</a>
	</div>
	<ul id="tt"></ul>
</body>
</html>