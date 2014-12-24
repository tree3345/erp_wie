<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<div class="easyui-layout" fit="true">
	<div region="north" border="false" style="background:#fafafa;height:40px;padding:5px;border-bottom:1px solid #92B7D0;text-align:right;overflow:hidden">
		 <input id="productName" class="easyui-searchbox" style="width:300px" data-options="searcher:queryGood_query,prompt:'商品名称|缩写'"> 
		<a id="queryGood-ok" href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true">选定</a>
	</div>
	<div region="west" border="false" split="true" style="width:150px;padding:5px;">
		<ul id="queryGood-t-types" url="<c:url value='/class/getTreeClass.tg'/>"></ul>
	</div>
	<div region="center" border="false">
		<table id="queryGood-dt-goods" class="easyui-datagrid"
				url="<c:url value='/product/getItemsProduct.tg?product.stockflag=1'/>"
				fit="true" fitColumns="true" border="false" 
				pagination="true">
			<thead>
				<tr>
					<th field="ck" checkbox="true"></th>
					<th field="classId" width="80" formatter="queryGood_formatType">类别</th>
					<th field="productNo" width="80">编码</th>
					<th field="productName" width="130">名称</th>
					<th field="unit" width="60">单位</th>
					<th field="purchasePrice">单价</th>
					<!-- <th field="model" width="100">型号</th>
					<th field="spec" width="100">规格</th>
					<th field="color" width="100">颜色</th>
					<th field="brand" width="100">品牌</th> -->
					<th field="warehouseQuantity" width="100">库存</th>
				</tr>
			</thead>
		</table>
	</div>
</div>

<script type="text/javascript">
	function queryGood_formatType(value,row){
		if (row.classId){
			return row.className;
		} else {
			return value;
		}
	}
	function queryGood_query(){
		var node = $('#queryGood-t-types').tree('getSelected');
		$('#queryGood-dt-goods').datagrid('load',{
			classId: (node?node.id:null),
			productName: $('#productName').val()
		});
	}
	$(function(){
		$('#queryGood-t-types').tree({
			onClick:function(){
				$('#productName').searchbox('setValue', null);
				queryGood_query();
			}
		});
	});
</script>