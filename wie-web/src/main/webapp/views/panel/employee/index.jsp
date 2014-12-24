<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui" %>
<head>
	 <jsp:include page="head.jsp"/>
</head>
<body style= "margin:0px;">
	<div id="main" class="easyui-layout" fit="true">
		<div region="north" border="false">
			<div class="toolbar">
				<table cellpadding="0" cellspacing="0" style="width: 95%;" fit="true">
					<tr>
						<td style="text-align: left">
						   <tgEasyui:easyuiButton iconCls="icon-add" method="newItem()" permission="employee:add" operationName="新增" />
							<tgEasyui:easyuiButton iconCls="icon-edit" method="editItem()" permission="employee:modify" operationName="修改" />
							<tgEasyui:easyuiButton iconCls="icon-cancel" method="delItem()" permission="employee:delete" operationName="删除" />
							<tgEasyui:easyuiButton iconCls="icon-mini-add" method="asRole(1)" permission="employee:delete" operationName="角色分配" />
							<tgEasyui:easyuiButton iconCls="icon-search" method="asRole(0)" permission="employee:delete" operationName="已分配角色" />
						&nbsp;
						登录名：
						<input id="userName_q" class="easyui-textbox"/>
						&nbsp;
							实名：
							<input id="employeeName_q" class="easyui-textbox"/>
							&nbsp;
							 <a class="easyui-linkbutton" href="javascript:void(0);" onclick="doQuery();">查找</a>
                            <a class="easyui-linkbutton" href="javascript:void(0);" onclick="reset();">清空</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
		
		<div region="center" border="false">
			<table id="t-employee"></table>
		</div>
	</div>
	
	<div id="dlg" style="width:650px;height:280px;"
			class="easyui-dialog" closed="true" modal="true" buttons="#dlg-buttons">
		<form id="myform" method="post" >
			<jsp:include page="form.jsp"/>
		</form>
		<div id="dlg-buttons" style="text-align:center;">
			<a href="#" id="save" class="easyui-linkbutton" iconCls="icon-save" onclick="saveItem()">保存</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
		</div>
	</div>
<form id="userform">
	<input id="uid" name="id"/>
</form>
<div id="dlg-asrole"></div>
	<div id="dlg-asrole-buttons" style="text-align:center;">
		<a href="#" id="saverole" class="easyui-linkbutton" iconCls="icon-save" onclick="saveRoles()">保存</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg-asrole').dialog('close')">关闭</a>
	</div>
</body>
