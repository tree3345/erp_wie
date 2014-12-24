<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui" %>
<head>
     <jsp:include page="head.jsp"/>
    </head>
	<body style="margin:0;padding:0;height:100%;overflow:hidden;background:#F2FBFF">
  			<div id="mainlayout" class="easyui-layout" fit="true">
			<div region="north" border="false">
				<div class="toolbar">
					<table cellpadding="0" cellspacing="0" style="width:95%;">
						<tr>
							<td style="text-align:left">
								<tgEasyui:easyuiButton iconCls="icon-add" method="newItem()" permission="role:add" operationName="新增"/>
								<tgEasyui:easyuiButton iconCls="icon-edit" method="editItem()" permission="role:modify" operationName="修改"/>
								<tgEasyui:easyuiButton iconCls="icon-cancel" method="removeItem()" permission="role:delete" operationName="删除"/>
								<tgEasyui:easyuiButton iconCls="icon-reload" method="back()" permission="role:refresh" operationName="刷新"/>
								<tgEasyui:easyuiButton iconCls="icon-search" method="authorizate()" permission="role:assignResource" operationName="分配资源"/>
								<tgEasyui:easyuiButton iconCls="icon-search" method="resAct()" permission="role:assignAction" operationName="分配操作"/>
								<tgEasyui:easyuiButton iconCls="icon-search" method="resActTree()" permission="role:assignAction" operationName="分配操作2"/>
							</td>
							<td style="text-align:right">
							<tgEasyui:easyuiButton iconCls="icon-search" method="advanceQuery()" permission="role:advanceQuery" operationName="高级查询"/>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div region="center" border="false">
				<table id="dt-role" class="easyui-datagrid"
						url="<c:url value='/permissions/role/getItemsRole.tg'/>"
						fit="true" border="false" 
						pagination="true" 						
						singleSelect="true" rownumbers="true">
					<thead>
						<tr>
							<th field="name" width="100" sortable="true">角色名称</th>
							<!-- <th field="enname" width="100" sortable="true">英文名称</th> 
							<th field="orderid" width="100" sortable="true">排序</th>-->
							<th field="status" width="100" sortable="true" formatter="statusFormatter">状态</th>
							<th field="memo" width="250">备注</th>
						</tr>
					</thead>
				</table>
			</div>
		<div id="dlg" class="easyui-dialog" style="width:350px;height:430px;"
				closed="true" modal="true" buttons="#dlg-buttons">
			<form id="myform" method="post">
				<jsp:include page="_form.jsp"></jsp:include>
			</form>
			<div id="dlg-buttons" style="text-align:center">
				<a  href="#" class="easyui-linkbutton"  iconCls="icon-save" onclick="saveItem()">保存</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
			</div>
		</div>
		<div id="auth" class="easyui-dialog" style="width:350px;height:250px;"
				closed="true" modal="true" buttons="#auth-buttons">
			<form id="authorization" method="post">
				<ul id="tt"></ul>
				<br/><br/>
			</form>
			<div id="auth-buttons" style="text-align:center">
				<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="saveAuthorizate()">保存</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#auth').dialog('close')">关闭</a>
			</div>
		</div>
		<div id="ac" class="easyui-dialog" style="width:900px;height:500px;"
				closed="true" modal="true" buttons="#actions-buttons">
			<form id="actions" method="post">
				<div id="resActions"></div>
			</form>
			<div id="actions-buttons" style="text-align:center">
				<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="saveResActions()">保存</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#ac').dialog('close')">关闭</a>
			</div>
		</div>
		<div id="resAct" class="easyui-dialog" style="width:900px;height:500px;"
				closed="true" modal="true" buttons="#resAct-buttons">
			<form id="resActForm" method="post">
<!-- 				<div id="resActDiv"></div> -->
				<table id="dt-resAct"  border="false" singleSelect="true" 
						rownumbers="true"  checkbox="true">
					<thead>
						<tr>
							<th field="sys" width="100" formatter="systemFormatter">系统名称</th>
							<th field="res" width="100" formatter="resourceFormatter">资源名称</th>
							<th field="acts" width="630" formatter="actionsFormatter">操作</th>
						</tr>
					</thead>
				</table>
			</form>
			<div id="resAct-buttons" style="text-align:center">
				<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="saveResAct()">保存</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#resAct').dialog('close')">关闭</a>
			</div>
		</div>
		<div id="detail" class="easyui-dialog" style="width:350px;height:450px;"
				closed="true" modal="true" buttons="#detail-buttons">
			<form id="detailform" >
				<%-- <jsp:include page="_detail.jsp"></jsp:include> --%>
			</form>
			<form id="saveResActForm" method="post">
			<input type="text" name="roleActions" id="saveResActInput"/>
		    </form>
			<div id="detail-buttons" style="text-align:center">
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#detail').dialog('close')">关闭</a>
			</div>
		</div>
		
	</div>
<div id="dlg-tree"></div>
<div id="dlg-tree-buttons" style="text-align:center">
	<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="actionSubmit()">保存</a>
	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg-tree').dialog('close')">关闭</a>
</div>
</body>