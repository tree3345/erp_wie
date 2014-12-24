<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui" %>
<div style="padding:5px;background:#fafafa;border-bottom:1px solid #eee;">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td>
				<c:if test="${editable==true }">
					<a id="btn-submit1" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-saves" onclick="javascript:submitBill(1)">保存单据</a>
					<a id="btn-submit2" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-saves" onclick="javascript:submitBill(2)">保存并新建</a>
				</c:if>
				<span id="ischeck">
				<tgEasyui:easyuiButton iconCls="icon-check" method="checkbill()" permission="inventory:validate" operationName="审核单据"/>
				</span>
				<%-- <c:if test="${checkable==true }">
				    <input type="text" name="aaaaa" />
					<a id="btn-check" href="#" class="easyui-linkbutton"  plain="true" 	iconCls="icon-check" onclick="javascript:checkBill1('${inventory.inventoryId}')">审核单据</a> 
				</c:if>  --%>
				<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-cancel" onclick="javascript:$('#dlg-bill').dialog('close')">关闭</a>
			</td>
			<td style="text-align:right">
				<c:if test="${editable==true }">
					<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" onclick="javascript:$('#dg-details').detailgrid('addGood',{storeId:$('#storeId').combobox('getValue'),stock:1})">添加商品</a>
					<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-remove" onclick="javascript:$('#dg-details').detailgrid('delGood')">删除商品</a>
				</c:if>
			</td>
		</tr>
	</table>
</div>

<div style="padding:10px">
	<div style="padding-left:90px">
		<table cellpadding="0" cellspacing="0" class="form-table">
			<tr>
				<td style="width:60px">单据编号</td>
				<td style="width:260px"><input name="inventory.code" value="${inventory.code}" readOnly="true" style="width:174px"/></td>
				<td style="width:60px">单据日期</td>
				<td><input name="inventory.createdt" class="easyui-datebox" value="${inventory.createdt }"/></td>
			</tr>
			<tr>
				<td>;盘点仓库</td>
				<td>
					<input id="store" name="store" type="hidden" value="${inventory.storeId}" />
					<input id="storeId" name="inventory.storeId" class="easyui-combobox" style="width:180px" value="${inventory.storeId }"
						   data-options="valueField:'storeId',textField:'storeName',url:'/store/users_storesStore.tg?id=${sessionScope.userId}',onSelect:isStore,required:true,editable:false" />
				</td>
			</tr>
			<tr>
				<td>备注</td>
				<td colspan="3"><input  class="easyui-validatebox" data-options="validType:['length[0,36]']" name="inventory.memo" value="${inventory.memo }" style="width:455px"/></td>
			</tr>
		</table>
	</div>
	<div style="margin:10px 0;">
		<table id="dg-details" style="height:200px"
				url="<c:url value="${detailUrl}"/>"
				fitColumns="true" singleSelect="true"
				rownumbers="true" showFooter="true">
			<thead>
				<tr>
					<th field="productNo" width="100">商品编码</th>
					<th field="productName" width="120">商品名称</th>
					<th field="purchasePrice" width="100">单价</th>
					<th field="storeId" hidden="true">规格</th>
					<th field="unit" width="60">单位</th> 
					<th field="inventoryQuantity" width="80" align="right" formatter="formatDecimal" editor="{type:'numberbox',options:{precision:2}}">数量</th>
					<th field="warehouseQuantity" width="80" align="right" formatter="formatDecimal">库存</th>
					<th field="billCost" width="100" align="right" formatter="formatDecimal">货损</th>
					<th field="damageSum" width="100" align="right" formatter="formatDecimal">货损金额</th>
				</tr>
			</thead>
		</table>
	</div>
	<div style="margin-top:10px">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="width:50px;text-align:right">制单人：</td>
				<td style="width:50px;text-align:center;border-bottom:1px solid #ccc">${inventory.createBy.name }</td>
				<td style="width:80px;text-align:right">制单时间：</td>
				<td style="width:130px;text-align:center;border-bottom:1px solid #ccc">${inventory.createdt }</td>
				<td style="width:50px;text-align:right">审核人：</td>
				<td style="width:50px;text-align:center;border-bottom:1px solid #ccc">${inventory.checkBy.name }</td>
				<td style="width:80px;text-align:right">审核时间：</td>
				<td style="width:130px;text-align:center;border-bottom:1px solid #ccc">${inventory.checkDate }</td>
			</tr>
			<tr><td><span id="checkvalue" style="display:none">${checkable==true }</span></td></tr>
		</table>
	</div>
</div>
<input id=inserted name="inserted" type="hidden"/>
<input id="deleted" name="deleted" type="hidden"/>
<input id="updated" name="updated" type="hidden"/>
<input id="updated" name="updated" type="hidden"/>
<input id="totaldge" name="inventory.totalInventory" type="hidden"/>
<input id="totalsum" name="inventory.totalSum" type="hidden"/>
<input id="status" name="inventory.status" value="${inventory.status }" type="hidden"/>
<input id="bill-editable" type="hidden" value="${editable }"/>
<input  id="id" name="id" type="hidden" value="${inventory.inventoryId }"/>

<script type="text/javascript">
	$('#intercourseId').combointercourse({
		url:'<c:url value="/intercourse/getItemsIntercourse.tg"/>',
		onClickRow:function(rowIndex,rowData){
			$('#intercourse').combogrid('setValue', rowData.shortName);
			$('#intercourseId').val(rowData.id);
			//console.log(rowData);
		},
		pageSize:2,
		pageList:[2,5,10,20]
	});
	$('#intercourseId').combogrid('setText','${bill.intercourse.shortName}');
	$('#dg-details').detailgrid({
		editable: ($('#bill-editable').val()=='true')
	});
	
	$(function(){
		var checkvalue=$('#checkvalue').text();
		if(checkvalue=='true'){
			$('#ischeck').show();
		}
		else{
			$('#ischeck').hide();
		}
		
	});
	
	function checkbill()
	{
		var inventoryId=$("#id").val();
		var url='/inventory/checkInventory.tg';
		checkBillcomm(inventoryId,url) ;
	}
</script>
