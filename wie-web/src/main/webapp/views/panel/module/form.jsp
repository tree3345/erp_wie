<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div style="padding:20px">
	<table cellpadding="0" cellspacing="0" class="form-table">
	<tr>
		<td >角色名称：</td>
		<td><input type="text" id="moduleName" style="width:200px"
			class="easyui-validatebox e-input" name="module.moduleName" required="true"/></td>
		<td style="padding-left:20px; ">视图名称：</td>
		<td><input type="text" id="viewName" style="width:200px" class="e-input" name="module.viewName"></td>
	</tr>
	<tr>
		<td style="padding-left:20px; ">备注：</td>
		<td><input type="text" id="remark" style="width:200px" class="e-input" name="module.remark"></td>
		<td><input type="hidden"  id="moduleId" name="module.moduleId" style="width:200px" >
		</td>
	</tr>
</table>
</div>