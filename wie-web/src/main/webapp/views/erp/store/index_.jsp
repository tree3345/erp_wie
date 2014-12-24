<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/extension/jquery-easyui-edatagrid/jquery.edatagrid.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/extension/jquery-easyui-edatagrid/jquery.edatagrid.lang.js"/>"></script>
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
	<script type="text/javascript">
		$(function(){
			$('#tt').edatagrid({
				saveUrl: '<c:url value="/store/testStore.tg"/>',    
			    updateUrl: '<c:url value="/store/testStore.tg"/>',    
			    destroyUrl: '<c:url value="/store/testStore.tg"/>'    
			});
		});
	</script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div region="north" border="false">
			<div class="toolbar">
			<table>
			 <tr><td>
				    <a href="#" class="easyui-linkbutton"
					onclick="javascript:$('#tt').edatagrid('addRow')">AddRow</a> 
					
					<a href="#" class="easyui-linkbutton"
					onclick="javascript:$('#tt').edatagrid('saveRow')">SaveRow</a> 
					
					<a href="#" class="easyui-linkbutton"
					onclick="javascript:$('#tt').edatagrid('cancelRow')">CancelRow</a>
					
				    <a href="#" class="easyui-linkbutton"
					onclick="javascript:$('#tt').edatagrid('destroyRow')">destroyRow</a>
					</td></tr>
			</table>
			</div>
		</div>
		<div region="center" border="false">
			<table id="tt"  rownumbers="true" class="easyui-datagrid"
				url='<c:url value="/store/getItemsStore.tg"/>' 
				fit="true" border="false" pagination="true" singleSelect="true" rownumbers="true">
				<thead>
					<tr>
						<th field="storeName" width="150" editor="text">商店名称</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>
</html>

<%-- <div class="easyui-layout" fit="true">
		<div region="north" border="false">
			<div class="toolbar">
				<table cellpadding="0" cellspacing="0" style="width: 95%;" fit="true">
					<tr>
						<td style="text-align: left">
						   <tgEasyui:easyuiButton
								iconCls="icon-add" method="newItem()" permission="users:add" operationName="新增" /> 
							<tgEasyui:easyuiButton iconCls="icon-edit"
								method="editItem()" permission="users:modify" operationName="修改" />
							<tgEasyui:easyuiButton iconCls="icon-cancel" method="delItem()"
								permission="users:delete" operationName="删除" />
						|&nbsp;
						用户名称：
						<input id="q" class="easyui-textbox"></input>
						&nbsp;
						性别：
							<input id="sex" class="easyui-combobox" data-options="valueField:'value',textField:'name',url:'<c:url value="/permissions/actions/outDicJsonByNicknameActions.tg?nickName=sex"/>'"/>
						
						&nbsp;
							 <a class="easyui-linkbutton" href="javascript:void(0);" onclick="doQuery();">查找</a>
                            <a class="easyui-linkbutton" href="javascript:void(0);" onclick="reset();">清空</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
		
		<div region="center" border="false">
			<table id="t-users"></table>
		</div>
	</div> --%>