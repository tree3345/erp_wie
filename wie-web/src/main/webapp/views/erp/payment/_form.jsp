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
				</c:if>
				<span id="ischeck">
				<tgEasyui:easyuiButton iconCls="icon-check" method="checkbill()" permission="payment:validate" operationName="审核入账"/>
				</span>
				<a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-cancel" onclick="javascript:$('#dlg-bill').dialog('close')">关闭</a>
			</td>
		</tr>
	</table>
</div>

<div style="padding:10px">
	<div style="padding-left:90px">
		<table cellpadding="0" cellspacing="0" class="form-table">
			<tr>
				<td style="width:60px">单据编号</td>
				<td style="width:260px"><input name="payment.code" value="${payment.code}" readOnly="true" style="width:174px"/></td>
				<td style="width:60px">单据日期</td>
				<td><input name="payment.indt" class="easyui-datebox" value="${payment.indt }" readOnly="true"/></td>
			</tr>
			<tr>
				<td>往来单位</td>
				<td>
					<input id="intercourseId"  name="payment.intercourseId" value="${payment.intercourseId }"  style="width:160px" readOnly="true"/>
				</td>
				<td>入库仓库</td>
				 <td>
					 <input id="storeId" name="payment.storeId" class="easyui-combobox" style="width:180px" value="${payment.storeId }"
							data-options="valueField:'storeId',textField:'storeName',url:'/store/users_storesStore.tg?id=${sessionScope.userId}'" readOnly="true" />
							
				</td>
			</tr>
			<tr>
				<td>备注</td>
				<td colspan="3"><input class="easyui-validatebox" data-options="validType:['length[0,36]']" 
				                       name="payment.remark" value="${payment.remark }" style="width:455px"/></td>
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
					<!-- <th field="model" width="100">型号</th>
					<th field="spec" width="100">规格</th> -->
					<th field="unit" width="60">单位</th>
					<th field="totalCount" width="80" align="right" formatter="formatDecimal" >数量</th>
					<th field="totalPrice" width="100" align="right" formatter="formatDecimal">单价</th>
					<th field="billCost" width="100" align="right" formatter="formatDecimal">应付金额</th>
					<th field="paid" width="100" align="right" formatter="formatDecimal" editor="{type:'numberbox',options:{precision:2}}">已付金额</th>
					<th field="unpaid" width="100" align="right" formatter="formatDecimal">未付金额</th>
				</tr>
			</thead>
		</table>
	</div>
	<div style="margin-top:10px">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="width:50px;text-align:right">制单人：</td>
				<td style="width:50px;text-align:center;border-bottom:1px solid #ccc">${payment.inby.name }</td>
				<td style="width:80px;text-align:right">制单时间：</td>
				<td style="width:130px;text-align:center;border-bottom:1px solid #ccc">${payment.indt }</td>
				<td style="width:50px;text-align:right">审核人：</td>
				<td style="width:50px;text-align:center;border-bottom:1px solid #ccc">${payment.checkBy.name }</td>
				<td style="width:80px;text-align:right">审核时间：</td>
				<td style="width:130px;text-align:center;border-bottom:1px solid #ccc">${payment.checkDate }</td>
			</tr>
			<tr><td><span id="checkvalue" style="display:none">${checkable==true }</span></td></tr>
		</table>
	</div>
</div>
<input id="oid" name="payment.purchaseId" type="hidden" value="${payment.purchaseId }" />
<input id=inserted name="inserted" type="hidden"/>
<input id="deleted" name="deleted" type="hidden"/>
<input id="updated" name="updated" type="hidden"/>
<input id="updated" name="updated" type="hidden"/>
<input id="totalcounts" name="payment.totalCount" type="hidden"/>
<input id="totalcosts" name="payment.totalPrice" type="hidden"/>
<input id="paids" name="payment.paid" type="hidden"/>
<input id="unpaids" name="payment.unpaid" type="hidden"/>
<input id="status" name="payment.status" value="${payment.status }" type="hidden"/>
<input id="bill-editable" type="hidden" value="${editable }"/>
<input  id="id" name="id" type="hidden" value="${payment.paymentId }"/>

<script type="text/javascript">
	$('#intercourseId').combointercourse({
		url:'<c:url value="/intercourse/getItemsIntercourse.tg"/>',
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
		var paymentId=$("#id").val();
		var url='/payment/checkPayment.tg';
		 checkBillcomm(paymentId,url) ;
	}
</script>
