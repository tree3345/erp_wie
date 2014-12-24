<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui" %>

<head>
	<jsp:include page="head.jsp"></jsp:include>
</head>
<body style="margin: 0; padding: 0; height: 100%; overflow: hidden; background: #F2FBFF">
	<form id="dicform" method="post">
		<input type="hidden" id="dicId" name="id" value=""/>
	</form>
	<div margin:0 auto;width:auto;height:100%>
	<div style=" float:left; width:40%; height:100%;">
	<div class="easyui-layout" fit="true">
		<div region="north" border="false">
			<div class="toolbar">
				<table cellpadding="0" cellspacing="0" style="width:95%;" fit="true">
						<tr>
							<td style="text-align: left;">
				<tgEasyui:easyuiButton iconCls="icon-add" method="newItem()" permission="purchase:add" operationName="新增"/>
				
				<tgEasyui:easyuiButton iconCls="icon-cancel" method="delItem()" permission="purchase:delete" operationName="删除"/>
				
				
							</td>
							<td style="text-align: right;"> 
							<tgEasyui:easyuiButton iconCls="icon-mini-add" method="impfromorder()" permission="purchase:modify" operationName="导入"/>
							</td>
						</tr>
					</table>
			</div>
		</div>
		<div region="center" border="false">
			<table id="t-dictionarys" class="easyui-datagrid"
					url="<c:url value='/purchase/getItemsPurchase.tg'/>"
					singleSelect="true"  rownumbers="true"
					border="true" fit="true" onClickRow="true">
				<thead>
					<tr>
						<th field="purchaseId" hidden="true" width="260" sortable="true">单据号</th>
						<th field="totalPrice" width="100" >总价</th>
						<th field="purchaseTime" width="150">入库时间</th>
						<th field="orderId" width="150">订单号</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	
	<div id="dlg" style="width:680px;height:500px;"
			class="easyui-dialog" closed="true" modal="true" buttons="#dlg-buttons">
			<div style="padding:20px">
			<form id="impform" method="post" style="margin:0;padding:0">
			  <input type="hidden" id="orderId" name="orderId">
			</form>
				<form id="myform" method="post" style="margin:0;padding:0">
					<table>
						<tr>
							<td>订单号</td>
							<td><input type="text" id="purchase.storeId" class="easyui-validatebox e-input" name="purchase.storeId" required="true"></input></td>
							<td>总价：</td>
							<td>
								<input type="text" id="totalprice" class="easyui-validatebox e-input" name="purchase.totalPrice" required="true" />
							    <input type="hidden" id="ins" name="ins">
							    <input type="hidden" id="del" name="del">
							    <input type="hidden" id="upd" name="upd">
							</td>
						</tr>
					</table>
					<table id="dg"></table>
				</form>
			</div>
		<div id="dlg-buttons" style="text-align:center;">
			<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="saveItem()">保存</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="closewindow()">关闭</a>
		</div>
	</div>
	</div>
	<div style="float:left; width:60%;height:100%;border:true">
	     <div class="easyui-layout" fit="true">
		<div region="north" border="false">
			
		</div>
		
		<div region="center" border="false">
			<table id="tt"></table>
		</div>
	</div>
	</div>
	<div id="dd" title="订单导入" style="width:400px;height:200px;">
	</div> 
</body>

