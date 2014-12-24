<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div style="padding:20px">
	<table cellpadding="0" cellspacing="0" class="form-table">
	<tr>
		<td >角色名称：</td>
		<td><input type="text" id="functionName" style="width:200px"
			class="easyui-validatebox e-input" name="functions.functionName" required="true"/></td>
		<td style="padding-left:20px; ">视图名称：</td>
		<td><input type="text" id="controlName" style="width:200px" class="e-input" name="functions.controlName"></td>
	</tr>
	<tr>
		<td style="padding-left:20px; ">模块：</td>
		<td><input type="text" id="moduleId" style="width:200px" class="easyui-combobox" data-options="valueField:'moduleId',textField:'moduleName',url:'<c:url value="/module/getJsonAllModule.tg"/>',required:true" name="functions.module.moduleId"></td>
		<td style="padding-left:20px; ">备注：</td>
		<td><input type="text" id="remark" style="width:200px" class="e-input" name="functions.remark"></td>

		<td><input type="hidden"  id="functionId" name="functions.functionId" style="width:200px" >
		</td>
	</tr>
</table>
</div>