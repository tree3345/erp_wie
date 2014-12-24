<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui" %>
<head>
   	<jsp:include page="head.jsp"></jsp:include>
</head>
	<body style="margin:0;padding:0;height:100%;overflow:hidden;background:#F2FBFF">
	<form id="groupForm" >
		<input type="hidden" id="id" name="id" value=""/>
		<input type="hidden" id="groupid" name="groupid" value=""/>
	</form>
	<div id="mainlayout" class="easyui-layout" fit="true">
		<div region="north" border="false" >
			<div class="toolbar">
				<table cellpadding="0" cellspacing="0" style="width:95%;" fit="true">
					<tr>
						<td style="text-align: left;">
							<tgEasyui:easyuiButton iconCls="icon-add" method="newItem()" permission="group:add" operationName="新增"/>
							<tgEasyui:easyuiButton iconCls="icon-edit" method="editItem()" permission="group:modify" operationName="修改"/>
							<tgEasyui:easyuiButton iconCls="icon-cancel" method="delItem()" permission="group:delete" operationName="删除"/>
							<tgEasyui:easyuiButton iconCls="icon-search" method="lookItem()" permission="group:detail" operationName="查看"/>
							<tgEasyui:easyuiButton iconCls="icon-add" method="addUserItem()" permission="group:addUser" operationName="添加用户"/>
							<tgEasyui:easyuiButton iconCls="icon-cancel" method="delUserItem()" permission="group:deleteGroupUser" operationName="删除组内用户"/>
							<tgEasyui:easyuiButton iconCls="icon-add" method="groupToRole()" permission="group:groupAuthRole" operationName="组授权角色"/>
							<tgEasyui:easyuiButton iconCls="icon-search" method="lookGroupHaveRole()" permission="group:detailGroupRole" operationName="查看组拥有的角色"/>
							<tgEasyui:easyuiButton iconCls="icon-cancel" method="removeGroupHaveRole()" permission="group:removeGroupRole" operationName="移除组拥有的角色"/>
				
 							<!-- <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newItem()">新增组</a> 
 							<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editItem()">修改组</a> 
 							<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="delItem()">删除组</a> 
 							<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="lookItem()">查看组信息</a> 
 							<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addUserItem()">添加用户</a> 
 							<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="delUserItem()">删除组内用户</a> 
 							<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="groupToRole()">组授权角色</a> 
 							<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="lookGroupHaveRole()">查看组拥有的角色</a> 
 							<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="removeGroupHaveRole()">移除组拥有的角色</a> 
 -->
						</td>
					</tr>
				</table>
			</div>
		</div>
		
		<div region="west" border="false" style="border-right:1px solid #92B7D0;width:150px;padding:5px;">
			<div id="allTrees">
				<%=request.getAttribute("treeScript")%>
			</div>
		</div>
		<div region="center" border="false">
			<table id="dt-resources" class="easyui-datagrid"
					url="<c:url value='/user/getItemsUser.tg'/>"
					fit="true" border="false" 
					pagination="true"
					singleSelect="false" rownumbers="true" disabled="true" >
				<thead>
					<tr>
						<th field="name" width="100" >用户名称</th>
						<th field="position" width="50">职位</th>
						<th field="age" width="50">年龄</th>
						<th field="sex" width="50" formatter="sexFormatter">性别</th>
						<th field="education" width="100" formatter="educationFormatter">教育情况</th>
						<th field="address" width=100>地址</th>
						<th field="phone" width="100">电话</th>
						<th field="email" width="150">邮箱</th>
						<th field="memo" width="300">备注</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
				<!-- 李刚英写 -->
	 <!-- <div id="rolGroup" style="width:250px;height:400px;" class="easyui-dialog" closed="true" modal="true" buttons="#role-buttons">
		<div>
			<h4 style="text-align: center;">可选角色</h4>
			<form id="selectRole">
				<input type="hidden" value="" name="groupid" id="gsid"/>
				<table style="border-color: #8DB2E3 ;" >
					<thead>
						<tr>
							<td>
							<input type="checkbox" name="all" onclick="clickeAll(this);"/></td> 
							<td>角色名称</td><td>&nbsp;</td><td>备注</td>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</form>
		</div>
		<div id="role-buttons" style="text-align:center;">
			<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="addRole();">确定</a>
			<a href="#" class="easyui-linkbutton" onclick="removeRole();">确定</a>
		</div>
	</div>  -->
	
	 <form id="self" method="post">
	 <input type="hidden" id="gid" name="groupid" value=""/>
	 </form>
	<div id="adduser" style="width:400px;height:400px;" class="easyui-dialog" closed="true" modal="true" buttons="#dlg-buttons0">
	        
			<table id="dt-otheruser" class="easyui-datagrid"
					fit="true" border="false" 
					url="<c:url value='/user/getOtherUser.tg'/>"
					pagination="true"
					singleSelect="false" rownumbers="true" disabled="true" >
				<thead>
					<tr>
						<th field="name" width="100" >用户名称</th>
						<th field="position" width="50">职位</th>
						<th field="email" width="150">邮箱</th>
					</tr>
				</thead>
			</table>

		<div id="dlg-buttons0" style="text-align:center;">
			<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="addUsers()">确定</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#adduser').dialog('close')">关闭</a>
		</div>
	</div>
		
				<!-- ==== -->
				
				
				<div id="dlg" class="easyui-dialog" style="width:350px;height:300px;"
						closed="true" modal="true" buttons="#dlg-buttons1">
					<div style="padding: 20px;">
						<form id="myform" method="post">
							<table>
								<tr>
									<td style="width:80px">组名称</td>
									<td><input type="text" id="name" class="easyui-validatebox e-input" name="group.name" required="true" style="width:200px"></input></td>
								</tr>
								<tr>
									<td>组英文名称</td>
									<td>
										<input type="text" id="enName" class="e-input" name="group.enName" style="width:200px">
									</td>
								</tr>
								<tr>
									<td>组类别</td>
									<td>
										<input type="text" id="groupType" class="e-input" name="group.groupType" style="width:200px">
									</td>
								</tr>
								
								<tr>
									<td>所属组</td>
									<td>
										<input id="parentId" name="group.parentId" class="easyui-combotree" style="width:200px" ></input>
									</td>
								</tr>
								
								<tr>
									<td>状态</td>
									<td>
									<input id="status" class="easyui-combobox"  name="group.status" style="width:200px" 
											required="true" editable="false"="true" 
											url="<c:url value='/permissions/actions/outDicJsonByNicknameActions.tg?nickName=status'/>"
											valueField="value" textField="name">
									</td>
								</tr>
								
								<tr>
									<td>排序</td>
									<td><input type="text" id="orderId" class="e-input" name="group.orderId" style="width:200px"></input></td>
								</tr>
								<tr>
									<td>备注</td>
									<td><input type="text" id="memo" class="e-input" name="group.memo" style="width:200px"></input></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>
										<input type="hidden" id="myhomeid" name="group.id" value="1">
									</td>
								</tr>
							</table>
						</form>
					</div>
					<div id="dlg-buttons1" style="text-align:center">
						<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="saveItem()">保存</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
					</div>
				</div>
				
				<div id="look" style="width:350px;height:350px;"
			class="easyui-dialog" closed="true" modal="true" buttons="#dlg-buttons2">
		<div style="padding:20px;">
				<form id="myform" method="post" style="margin:0;padding:0">
					<table>
						<tr>
							<td>组名称：</td>
							<td><input editable="false" type="text" id="lname" class="easyui-validatebox e-input" name="group.name" required="true"></input></td>
						</tr>
						<tr>
							<td>组英文名称：</td>
							<td>
								<input editable="false" type="text" id="lenname" class="e-input" name="group.enName" >
							</td>
						</tr>
						<tr>
							<td>组类别：</td>
							<td>
								<input editable="false" type="text" id="lgrouptype" class="e-input" name="group.groupType" >
							</td>
						</tr>
						
						<tr>
							<td>状态：</td>
							<td>
							<input editable="false" type="text" id="lstatus" class="e-input" name="group.parentId" >
							</td>
						</tr>
						
						<tr>
							<td>排序：</td>
							<td><input editable="false" type="text" id="lorderid" class="e-input" name="group.orderId"></input></td>
						</tr>
						<tr>
							<td>备注：</td>
							<td><input editable="false" type="text" id="lmemo" class="e-input" name="group.memo"></input></td>
						</tr>
					</table>
				</form>
			</div>
		<div id="dlg-buttons2" style="text-align:center;">
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#look').dialog('close')">关闭</a>
		</div>
	</div>
	<!-- 为组添加角色的dialogue by 孙强 -->
	<form id="addRole" method="post">
	<input type="hidden" id="gsid" name="groupid" value=""/>
	</form>
	 <div id="dlgwindow" class="easyui-dialog" resizable="true"
			maximizable="true" style="width: 340px; height: 380px;" closed="true"
			modal="true" buttons="#dlg-button">
			<table id="dt-group" class="easyui-datagrid" fit="true"
				border="false" pagination="true" 
				rownumbers="true">
				
				<thead>
					<tr>
						<th field="ck" width="100" sortable="true" checkbox="true" align="center"></th>
						<th field="name" width="100" sortable="true" align="center">角色名称</th>
						<th field="memo" width="100" sortable="true" align="center">备注</th>
					</tr>
				</thead>
			</table>
			<div id="dlg-button" style="text-align: center">
			<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="addRole()">添加</a>
			
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
					onclick="javascript:$('#dlgwindow').dialog('close')">关闭</a>
				<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="removeRole()">移除</a>	
			</div>
			
		</div>		
	
</body>