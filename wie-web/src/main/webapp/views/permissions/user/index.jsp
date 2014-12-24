<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui" %>
<head>
	 <jsp:include page="head.jsp"></jsp:include>
</head>
<body style= "margin:0px;">
	<form id="userform" method="post">
		<input type="hidden" id="userId" name="id" value=""/>
		<input type="hidden" id="storeIds" name="storeIds" value=""/>
	</form>

	<div id="main" class="easyui-layout" fit="true">
		<div region="north" border="false">
			<div class="toolbar">
				<table cellpadding="0" cellspacing="0" style="width: 95%;" fit="true">
					<tr>
						<td style="text-align: left">
						   <tgEasyui:easyuiButton iconCls="icon-add" method="newItem()" permission="users:add" operationName="新增" />
							<tgEasyui:easyuiButton iconCls="icon-edit" method="editItem()" permission="users:modify" operationName="修改" />
							<tgEasyui:easyuiButton iconCls="icon-cancel" method="delItem()" permission="users:delete" operationName="删除" />
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
	</div>
	
	<div id="dlg" style="width:650px;height:420px;"
			class="easyui-dialog" closed="true" modal="true" buttons="#dlg-buttons">
		<form id="myform" method="post" >
			<jsp:include page="form.jsp"></jsp:include>
		</form>
		<div id="dlg-buttons" style="text-align:center;">
			<a href="#" id="save" class="easyui-linkbutton" iconCls="icon-save" onclick="saveItem()">保存</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
		</div>
	</div>
	<div id="storeTools">
		<table cellpadding="0" cellspacing="0" style="width: 95%;" fit="true">
			<tr>
				<td style="text-align: left">
					<tgEasyui:easyuiButton iconCls="icon-add" method="addstores()" permission="users:addstores" operationName="添加" />
					<tgEasyui:easyuiButton iconCls="icon-cancel" method="deletestores()" permission="users:deletestores" operationName="删除" />
				</td>
			</tr>
		</table>
	</div>
<div id="storeall"></div>
<div id="tb" style="text-align:center;">
	<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="doStores('save')">确定</a>
	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#storeall').dialog('close')">关闭</a>
</div>
</body>
