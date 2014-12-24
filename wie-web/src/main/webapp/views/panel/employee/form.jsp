<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div style="padding:20px">
	<table cellpadding="0" cellspacing="0" class="form-table">
	<tr>
		<td >登录名：</td>
		<td><input type="text" id="userName" style="width:200px"
			class="easyui-validatebox e-input" name="employee.userName"
			required="true"/></td>
		<td style="padding-left:20px; ">密码：</td>
		<td><input type="text" id="password" style="width:200px"
			class="easyui-validatebox e-input" name="employee.password"
			required="true"></td>
	</tr>
	<tr>
		<td >用户实名：</td>
		<td><input type="text" id="employeeName" style="width:200px"
			class="easyui-validatebox e-input" name="employee.employeeName" required="true">
		</td>
		<td  style="padding-left:20px; ">电话：</td>
		<td><input type="text" id="phone" style="width:200px"
				   class="easyui-numberbox e-input" name="employee.phone" required="true">
		</td>
	</tr>
	<tr>
		<td>年龄：</td>
		<td><input type="text" id="age" class="easyui-numberbox e-input" name="employee.age" validType="number" style="width:200px" /></td>
		<td  style="padding-left:20px; ">性别：</td>
		<td><input type="text" id="sex" class="easyui-combobox" name="employee.sex"   style="width:200px" /></td>
	</tr>
   <tr>
	   <td>地址：</td>
	   <td><input type="text" id="address" class="e-input" style="width:200px"  name="employee.address"></td>
	   <td  style="padding-left:20px; ">邮箱：</td>
	   <td><input type="text" id="email" class="easyui-validatebox e-input" validType="email" style="width:200px"  name="employee.email"></td>
   </tr>
		<tr>
			<td>IsSkipAnimation：</td>
			<td><input type="text" id="isSkipAnimation" class="e-input" style="width:200px"  name="employee.isSkipAnimation"></td>
			<td  style="padding-left:20px; ">pageStyle：</td>
			<td><input type="text" id="pageStyle" class="easyui-numberbox e-input"  style="width:200px"  name="employee.PageStyle"></td>
		</tr>
		<tr>
			<td>管理员：</td>
			<td><input type="text" id="isAdmin" class="e-input" style="width:200px"  name="employee.isAdmin"></td>
			<td  style="padding-left:20px; ">是否激活：</td>
			<td><input type="text" id="isActive" class="easyui-validatebox e-input"  style="width:200px"  name="employee.isActive"></td>
		</tr>
		<tr>
		<td>商店选择：</td>
		<td>
			<input id="storeId" class="easyui-combobox"  name="employee.storeId" style="width:200px"
				   required="true" editable="false"
				   url="<c:url value='/store/getAllStore.tg'/>"
				   valueField="storeId" textField="storeName">
		</td>
	<tr>
		<td></td>
		<td><input type="hidden"  id="userId" name="employee.userId" style="width:200px" >
		</td>
	</tr>
</table>
</div>