<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<script>
	var storeId='${storeId}';
	var stockflag='${stockflag}';
	$(function(){
		showGoods(0);
	});
		/*<table id="queryGood-dt-goods" class="easyui-datagrid"
		fit="true" fitColumns="true" border="false" pagination="true">
		<thead>
		<tr>
		<th field="ck" checkbox="true"></th>
		<th field="classId" width="80" formatter="queryGood_formatType">类别</th>
		<th field="productNo" width="80">编码</th>
		<th field="productName" width="130">名称</th>
		<th field="unit" width="60">单位</th>
		<!-- <th field="model" width="100">型号</th>
		<th field="spec" width="100">规格</th>
		<th field="color" width="100">颜色</th>
		<th field="brand" width="100">品牌</th> -->
		</tr>
		</thead>
		</table>*/

function showGoods(flag){
	var url='<c:url value='/product/getItemsProduct.tg'/>?product.flag='+flag;
	if(stockflag==1)
		url+='&product.stockflag='+stockflag;
	var options={
		iconCls:'icon-edit',
		width:function(){return document.body.clientWidth*0.5},
		height:function(){return document.body.clientHeight*0.5},
		border:false,
		fitColumns:true,
		rownumbers:true,
		pagination:true,
		fit:true,
		idField:'productId',
		url:url,
		queryParams:{
			storeId:storeId
		},
		columns:[[
			{field:'ck',title:'',checkbox:true},
			{field:'productName',title:'名称',width:80},
			{field:'unit',title:'单位',width:80},
			{field:'warehouseQuantity',title:'库存',hidden:true,width:80},
			{field:'productNo',title:'编码',width:80},
			{field:'classId',title:'类别',width:80,formatter: function (value,row) {
				if (row.classId){
					return row.className;
				} else {
					return value;
				}
			}},
			{field:'storeId',title:'所属商店',width:80,formatter:function(value){
				console.log(value);
				if(value==null) return "公共商品";
				else return "特有商品";
			}}
		]]
	};
	$('#queryGood-dt-goods').datagrid(options);
	if(stockflag==1)
	$('#queryGood-dt-goods').datagrid('showColumn','warehouseQuantity');

}
</script>
<div class="easyui-layout" fit="true">
	<div region="north" border="false" style="background:#fafafa;height:40px;padding:5px;border-bottom:1px solid #92B7D0;overflow:hidden">
		<span style="float: left">
		<select id="goodsown" class="easyui-combobox" name="goodown" style="width:100px;text-align:left;" data-options="onSelect:filter">
			<option value="0">所售商品</option>
			<option value="1">特有商品</option>
			<option value="2">公共商品</option>
		</select>
		</span>
		<span style="float: right">
		<input id="productName" class="easyui-searchbox" style="width:300px" data-options="searcher:queryGood_query,prompt:'商品名称|缩写'">
		<a id="queryGood-ok" href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true">选定</a>
			</span>
	</div>
	<div region="west" border="false" split="true" style="width:150px;padding:5px;">
		<ul id="queryGood-t-types" url="<c:url value='/class/getTreeClass.tg'/>"></ul>
	</div>
	<div region="center" border="false">
		<table id="queryGood-dt-goods">

		</table>
	</div>
</div>
<script type="text/javascript">
	function filter(){
		var value=$('#goodsown').combobox('getValue');
		showGoods(value);
	}
	function queryGood_formatType(value,row){
		if (row.classId){
			return row.className;
		} else {
			return value;
		}
	}
	function getCheck(){
		var tt=$('#queryGood-dt-goods').datagrid('getSelections');
		console.log(tt);
	}
	function queryGood_query(){
		var node = $('#queryGood-t-types').tree('getSelected');
		$('#queryGood-dt-goods').datagrid('load',{
			storeId:storeId,
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