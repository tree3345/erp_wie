<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui" %>
<head>
	 <jsp:include page="head.jsp"></jsp:include>
</head>
<body style="margin:0;padding:0;height:100%;overflow:hidden;background:#F2FBFF">
	<form id="sysform" method="post">
		<input type="hidden" id="sysId" name="id" value=""/>
	</form>
	<div id="mainlayout" class="easyui-layout" fit="true">
		<div region="north" border="false">
			<div class="toolbar">
				<table cellpadding="0" cellspacing="0" style="width:95%;" fit="true">
					<tr>
						<td style="text-align:left">
				<tgEasyui:easyuiButton iconCls="icon-add" method="newItem()" permission="system:add" operationName="新增"/>
				<tgEasyui:easyuiButton iconCls="icon-edit" method="editItem()" permission="system:modify" operationName="修改"/>
				<tgEasyui:easyuiButton iconCls="icon-cancel" method="delItem()" permission="system:delete" operationName="删除"/>
				<!-- 				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newItem()">新增系统</a> -->
<!-- 				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editItem()">修改系统</a> -->
<!-- 				<a href="#" class="easyui-linkbutton" iconCls="icon-delete" plain="true" onclick="delItem()">删除系统</a> -->
<!-- 				<a href="javascript:advanceQuery()" class="easyui-linkbutton"plain="true">查询系统</a> -->
						</td>
					<td style="text-align:right">
						<tgEasyui:easyuiButton iconCls="icon-search" method="advanceQuery()" permission="system:query" operationName="高级查询"/>
					</td>
					</tr>
				</table>
			</div>
		</div>
		
		<div region="center" border="false">
			<table id="t-systems" class="easyui-datagrid"
					url="<c:url value='/sys/getItemsMessage.tg'/>"
					singleSelect="true" pagination="true"
					border="false" fit="true">
				<thead>
					<tr>
						<th field="name" width="100" sortable="true">系统名称</th>
						<th field="ename" width="100" sortable="true">系统英文名称</th>
						<th field="contextPath" width="100">上下文</th>
						<th field="tablePrefix" width="100">表名前缀</th>
						<th field="logo" width="100">系统标志</th>
						<th field="order" width=50>排序</th>
						<th field="status" width="50" formatter="statusFormatter">状态</th>
						<th field="version" width="50">版本</th>
						<th field="builddate" width="100" sortable="true">创建日期</th>
						<th field="memo" width="300">备注</th>
					</tr>
				</thead>
			</table>
		</div>

	
	<div id="dlg" style="width:350px;height:350px;"
			class="easyui-dialog" closed="true" modal="true" buttons="#dlg-buttons">
			<div style="padding:20px">
				<form id="myform" method="post" style="margin:0;padding:0">
					<table>
						<tr>
							<td >系统名称：</td>
							<td><input type="text" id="name" class="easyui-validatebox e-input" name="system.name" required="true"></input></td>
						</tr>
						<tr>
							<td>系统英文名称：</td>
							<td>
								<input type="text" id="ename" class="easyui-validatebox e-input" name="system.ename" required="true">
							</td>
						</tr>
						<tr>
							<td>上下文：</td>
							<td>
								<input type="text" id="contextPath" class="easyui-validatebox e-input" name="system.contextPath" required="true">
							</td>
						</tr>
						<tr>
							<td>表名前缀：</td>
							<td><input type="text" id="tablePrefix" class="easyui-validatebox e-input" name="system.tablePrefix" required="true"></input></td>
						</tr>
						<tr>
							<td>系统标志：</td>
							<td><input type="text" id="logo" class="easyui-validatebox e-input" name="system.logo" required="true"></input></td>
						</tr>
						<tr>
							<td>排序：</td>
							<td><input type="text" id="order" class="e-input" name="system.order"></input></td>
						</tr>
						<tr>
							<td>状态：</td>
							<td>
								<input id="status" class="easyui-combobox"  name="system.status" style="width:200px" 
									required="true" editable="false" 
									url="<c:url value='/permissions/actions/outDicJsonByNicknameActions.tg?nickName=status'/>"
									valueField="value" textField="name">
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<input type="hidden" id="builddate" name="system.builddate"></input>
							</td>
						</tr>
						<tr>
							<td>版本：</td>
							<td>
								<input type="text" id="version" class="e-input" name="system.version"></input>
							</td>
						</tr>
						<tr>
							<td>备注：</td>
							<td><input type="text" id="memo" class="e-input" name="system.memo"></input></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>
								<input type="hidden" id="icon" name="system.icon" value="/u/images"><input type="hidden" id="id" name="system.id" value="1">
							</td>
						</tr>
					</table>
				</form>
		</div>
		<div id="dlg-buttons" style="text-align:center;">
			<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="saveItem()">保存</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
		</div>
	</div>
	</div>
</body>
