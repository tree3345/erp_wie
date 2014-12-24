<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div style="padding:20px">
	<table cellpadding="0" cellspacing="0" class="form-table">
	<tr>
		<td >产品名称：</td>
		<td><input type="text" id="productName" style="width:200px" 
			class="easyui-validatebox e-input" name="product.productName"
			required="true"/></td>
			<td>请选择组：</td>
		<td><input id="parentId" name="product.classId" class="easyui-combotree" style="width:200px" /></td>
	</tr>

	<tr>
		<td>名称全拼：</td>
		<td><input type="text" id="goodNumber" style="width:200px" 
			class="easyui-validatebox e-input" name="product.goodNumber"
			required="true"></td>
		<td>名称缩写：</td>
		<td><input type="text" id="spelling" style="width:200px" 
			class="easyui-validatebox e-input" name="product.spelling" required="true">
		</td>
	</tr>
	<tr>
		<td>销售价格：</td>
		<td><input type="text" id=descr style="width:200px" 
			class="e-input" name="product.descr">
		</td>
		<td>货物编号：</td>
		<td><input type="text" id="productNO" class="e-input" style="width:200px" 
			name="product.productNO"/></td>
	</tr>
	<tr>
		<td>条形码：</td>
		<td><input type="text" id="unit" class="e-input" style="width:200px" 
			name="product.unit"/>
			<input type="hidden" value="" id="id" name="product.productId" style="width:200px" >
			</td>
	</tr>
</table>
</div>