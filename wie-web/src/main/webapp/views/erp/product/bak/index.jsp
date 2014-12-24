<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui" %>
<head>
	 <jsp:include page="../head.jsp"></jsp:include>
</head>
<body style= "margin:0px;">
	<form id="userform" method="post">
		<input type="hidden" id="userId" name="id" value=""/>
	</form>
	<div class="easyui-layout" fit="true">
		<div region="north" border="false">
			<div class="toolbar">
				<table cellpadding="0" cellspacing="0" style="width:95%;" fit="true">
							<tr>
								<td style="text-align:left">
				<tgEasyui:easyuiButton iconCls="icon-add" method="newItem()" permission="product:add" operationName="新增"/>
				<tgEasyui:easyuiButton iconCls="icon-edit" method="editItem()" permission="product:modify" operationName="修改"/>
				<tgEasyui:easyuiButton iconCls="icon-cancel" method="delItem()" permission="product:delete" operationName="删除"/>
								</td>
								<td style="text-align:right;">
				<tgEasyui:easyuiButton iconCls="icon-search" method="advanceQuery()" permission="product:query" operationName="高级查询"/>
								</td>
							</tr>
					</table>
			</div>
		</div>
		
		<div region="center" border="false">
			<table id="t-users" class="easyui-datagrid"
					url="<c:url value='/product/getItemsProduct.tg'/>"
					singleSelect="true" pagination="true"  rownumbers="true"
					border="false" fit="true">
				<thead>
					<tr>
						<th field="productName" width="200">名称</th>
						<th field="unit" width="50">单位</th>
						<th field="productNo" width="150">条形码</th>
						<th field="spelling" width="50">名称缩写</th>
						<th field="classId" width="300">小类类别</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	
	<div id="dlg" style="width:650px;height:420px;"
			class="easyui-dialog" closed="true" modal="true" buttons="#dlg-buttons">
		<form id="myform" method="post" >
			<jsp:include page="../form.jsp"></jsp:include>
		</form>
		<div id="dlg-buttons" style="text-align:center;">
			<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="saveItem()">保存</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">关闭</a>
		</div>
	</div>
</body>
