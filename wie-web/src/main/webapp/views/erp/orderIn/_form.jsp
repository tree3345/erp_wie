<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<div style="padding:5px;background:#fafafa;border-bottom:1px solid #eee;">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td>
				<c:if test="${editable==true }">
					<a id="btn-submit1" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-saves" onclick="javascript:submitBill(1)">保存单据</a>
					<a id="btn-submit2" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-saves" onclick="javascript:submitBill(2)">保存并新建</a>
				</c:if>
				<c:if test="${checkable==true }">
					<a id="btn-check" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-check" onclick="javascript:checkBillcomm('${orderIn.orderId}','/orderIn/checkOrderIn.tg')">审核单据</a>
				</c:if>
				<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-cancel" onclick="javascript:$('#dlg-bill').dialog('close')">关闭</a>
			</td>
			<td style="text-align:right">
				<c:if test="${editable==true }">
					<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add" onclick="javascript:$('#dg-details').detailgrid('addGood',{storeId:$('#storeId').combobox('getValue')})">添加商品</a>
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
				<td style="width:220px"><input name="orderIn.orderNumber" value="${orderIn.orderNumber }" readOnly="true" style="width:220px"/></td>
				<td style="width:60px">&nbsp;单据日期</td>
				<td style="width: 180px"><input name="orderIn.createdt" class="easyui-datebox" value="${orderIn.createdt }" style="width:180px;"/></td>
			</tr>
			<tr>
				<td>往来单位</td>
				<td>
					<input id="intercourseId" name="intercourse.id" value="${orderIn.intercourse.id }" required="true"/>
				    <input id="intercourse" name="orderIn.intercourse.id" type="hidden" value="${orderIn.intercourse.id }" />
				</td>
				<td>&nbsp;计划仓库</td>
				<td>
					<input id="store" name="store" type="hidden" value="${orderIn.storeId}" />
					<input id="storeId" name="orderIn.storeId" class="easyui-combobox" style="width:180px" value="${orderIn.storeId }"
						   data-options="valueField:'storeId',textField:'storeName',url:'/store/users_storesStore.tg?id=${sessionScope.userId}',onSelect:isStore,required:true,editable:false" />
				</td>
			</tr>
			<tr>
				<td>备注</td>
				<td colspan="3"><input  class="easyui-validatebox" data-options="validType:['length[0,36]']" name="orderIn.remark" value="${orderIn.remark }" style="width:494px"/></td>
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
					 <th field="storeId" hidden="true">商店</th>
					<!--<th field="spec" width="100">规格</th> -->
					<th field="unit" width="60">单位</th>
					<th field="totalCount" width="80" align="right" formatter="formatDecimal" editor="{type:'numberbox',options:{precision:2}}">数量</th>
					<th field="totalPrice" width="100" align="right" formatter="formatDecimal" editor="{type:'numberbox',options:{precision:2}}">单价</th>
					<th field="billCost" width="100" align="right" formatter="formatDecimal">合价</th>
				</tr>
			</thead>
		</table>
	</div>
	<div style="margin-top:10px">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="width:50px;text-align:right">制单人：</td>
				<td style="width:50px;text-align:center;border-bottom:1px solid #ccc">${orderIn.createBy.name }</td>
				<td style="width:80px;text-align:right">制单时间：</td>
				<td style="width:130px;text-align:center;border-bottom:1px solid #ccc">${orderIn.createdt }</td>
				<td style="width:50px;text-align:right">审核人：</td>
				<td style="width:50px;text-align:center;border-bottom:1px solid #ccc">${orderIn.checkBy.name }</td>
				<td style="width:80px;text-align:right">审核时间：</td>
				<td style="width:130px;text-align:center;border-bottom:1px solid #ccc">${orderIn.checkDate }</td>
			</tr>
		</table>
	</div>
</div>
<input id=inserted name="inserted" type="hidden"/>
<input id="deleted" name="deleted" type="hidden"/>
<input id="updated" name="updated" type="hidden"/>
<input id="updated" name="updated" type="hidden"/>
<input id="totalcounts" name="orderIn.totalCount" type="hidden"/>
<input id="totalcosts" name="orderIn.totalPrice" type="hidden"/>
<input id="status" name="orderIn.status" value="${orderIn.status }" type="hidden"/>
<input id="bill-editable" type="hidden" value="${editable }"/>
<input  id="orderId" name="id" type="hidden" value="${orderIn.orderId }"/>

<script type="text/javascript">
	$('#intercourseId').combointercourse({
		url:'<c:url value="/intercourse/getItemsIntercourse.tg"/>',
		onClickRow:function(rowIndex,rowData){
			$('#intercourseId').combogrid('setValue', rowData.shortName);
			$('#intercourse').val(rowData.id);
			//console.log(rowData);
		},
		pageSize:2,
		pageList:[2,5,10,20]
	});
	$('#intercourseId').combogrid('setText','${orderIn.intercourse.shortName}');
	$('#dg-details').detailgrid({
		editable: ($('#bill-editable').val()=='true')
	});
</script>
