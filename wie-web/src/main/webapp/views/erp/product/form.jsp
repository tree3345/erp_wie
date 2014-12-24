<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<style></style>
<div style="padding:20px">
	<table cellpadding="0" cellspacing="0" class="form-table">
	<tr>
		<td >商品名称：</td>
		<td><input type="text" id="productName" style="width:200px"
			class="easyui-validatebox e-input" name="product.productName"
			required="true"/></td>
			<td>&nbsp;商品分类：</td>
		<td id="classtext">
			<input id="classId" class="easyui-combotree" name="product.productClass.classId" style="width:200px"
				   data-options="valueField:'classId',textField:'className',url:'/class/getTreeClass.tg'" required="true"/>
		</td>
	</tr>
	<tr>
		<td>所属商店：</td>
		<td><input id="storeId" class="easyui-combobox" name="product.storeId" style="width:200px"
				   data-options="valueField:'storeId',textField:'storeName',
				   url:'/store/users_storesStore.tg?id=${sessionScope.userId}',
				   onSelect:vaStore,
				   required:true,editable:false,disabled:${flag==2}" /></td>
		<td>&nbsp;名称全拼：</td>
		<td><input type="text" id="spelling" style="width:200px"
			class="easyui-validatebox e-input" name="product.spelling"
			required="true"></td>
	</tr>
	<tr>
		<td>销售价格：</td>
		<td><input type="text" id="salesPrice" style="width:200px" class="e-input" name="product.productPrice.salesPrice">
		</td>
		<td>&nbsp;货物编号：</td>
		<td><input type="text" id="productNo" class="e-input" style="width:200px" name="product.productNo"/></td>
	</tr>
	<tr>
		<td>商品编码：</td>
		<td>
			<input type="text" id="goodNumber" style="width:200px" class="easyui-validatebox e-input" name="product.goodNumber" required="true">
		</td>
		<td>&nbsp;单位：</td>
		<td>
			<input type="text" id="unit" class="e-input" style="width:200px" name="product.unit"/>
			<input type="hidden" id="status"   name="product.status" value="1"/>
			<input type="hidden" id="productId"   name="product.productId" />
		</td>
	</tr>
</table>
</div>