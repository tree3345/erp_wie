<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui" %>
<head>
	 <jsp:include page="head.jsp"/>
</head>
<body style= "margin:0px;">
	<div id="main" class="easyui-layout" fit="true">
		<div region="east" style="width: 30%">
			<table id="t-module" ></table>
		</div>
		<div region="north" border="false">
			<div class="toolbar">
				<table cellpadding="0" cellspacing="0" style="width: 95%;" fit="true">
					<tr>
						<td style="text-align: left">
						   <tgEasyui:easyuiButton iconCls="icon-add" method="newItem()" permission="function:add" operationName="新增" />
							<tgEasyui:easyuiButton iconCls="icon-edit" method="editItem()" permission="function:modify" operationName="修改" />
							<tgEasyui:easyuiButton iconCls="icon-cancel" method="delItem()" permission="function:delete" operationName="删除" />
						    &nbsp;
							功能名称：
							<input id="functionName_q" class="easyui-textbox"/>
							&nbsp;
							 <a class="easyui-linkbutton" href="javascript:void(0);" onclick="doQuery();">查找</a>
                            <a class="easyui-linkbutton" href="javascript:void(0);" onclick="reset();">清空</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
		
		<div region="center" border="false" style="width: 70%">
			<table id="t-function"></table>
		</div>
	</div>
	
	<div id="dlg" style="width:650px;height:160px;"
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
	<input id="id" name="id"/>
</form>
</body>
