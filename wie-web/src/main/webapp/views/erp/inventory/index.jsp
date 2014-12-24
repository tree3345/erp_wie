<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui" %>
    
<head>
    <link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/main.css"/>">
	<style>
		span#sp{color:red}
	</style>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.patch.js"/>"></script>
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
	 <script type="text/javascript">
        	var contextPath = '<%=request.getContextPath()%>';
        	var isaudit='inventory';
        	var checkUrl =contextPath+'/inventory/checkInventory.tg';
        	$.parser.onComplete = function(){
            	$('body').css('visibility','visible');
            	setTimeout(function(){
	            	$('#loading-mask').remove();
            	},50);
        	};
        	$(function(){
            	$(window).resize(function(){
                	$('#mainlayout').layout('resize');
            	});
        	});
        </script>
	
	<script type="text/javascript" src="<c:url value='/js/commons/jquery.combointercourse.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/commons/jquery.billgrid.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/commons/jquery.detailgrid.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/commons/commons.js'/>"></script>
	<script type="text/javascript">
		$(function(){
			$('#dg-bills').billgrid({
				createUrl:'<c:url value="/inventory/createInventory.tg"/>',
				editUrl:'<c:url value="/inventory/editInventory.tg"/>',
				destroyUrl:'<c:url value="/inventory/destroyInventory.tg"/>',
				query:{
					title:'查询采购入库单',
					form:'<c:url value="/views/erp/inventory/_query.jsp"/>',
					width:450,
					height:230,
					callback:function(){$('#q').searchbox('setValue','');}
				}
			});
		});
		function normalQuery(value){
			$('#dg-bills').billgrid('load',{
				storeId:$('#sid').combobox('getValue'),
				code: value
			});
		}
		function formatDecimal(value){
			var val = parseFloat(value);
			return isNaN(val) ? value : val.toFixed(2);
		}
		function formatStatus(value,row){
			if (value == 0){
				return '编制';
			} else if (value == 1){
				return '<span id="sp">审核</span>';
			} else {
				return value;
			}
		}
		function formatType(value,row){
			if (row.createBy){
				var data = jQuery.parseJSON(row.createBy); 
				return data.logonid;
			} else {
				return value;
			}
		}
		var store;
		$.getJSON("<c:url value='/store/getAllStore.tg'/>", function (json){
		       store=json;
		});
		function storeFormatter(value){
		        for(var i=0; i<store.length; i++){
		               if (store[i].storeId == value) return store[i].storeName;
		       }
		        return value;
		}
		
		function getOrder() {
			var v = $('#dg-bills').datagrid('getSelected');
			return v;
		}
		
		function openform(para){
			$.post('<c:url value="/isexistLogin.tg"/>',function(data){
				//console.log(data);
				var d=eval('('+data+')');
				if(d.status){
				    $('#dg-bills').billgrid(para);
				}
				else{
					$.messager.confirm('警告','登录超时，请重新登录！',function(r){
						if(r)
						 location.href='/loginLogin.tg';
					});   
				}
			});
			
		}
	</script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div region="north" border="false">
			<div class="toolbar" >
				<table cellpadding="0" cellspacing="0" style="width:100%">
					<tr>
						<td id="flag">
							<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openform('create')">增加</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-open" plain="true" onclick="openform('edit')">打开</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="openform('destroy')">删除</a>
						</td>
						<td style="text-align:right">
							<input id="sid" name="purchase.storeId" class="easyui-combobox" prompt="商店名称" style="width:180px"
								   data-options="valueField:'storeId',textField:'storeName',url:'/store/users_storesStore.tg?id=${sessionScope.userId}'" />
							<input id="q" class="easyui-searchbox" prompt="按单据编号查询" searcher="normalQuery" style="width:200px"/>
							<!-- <a href="#" class="easyui-linkbutton" plain="true" onclick="$('#dg-bills').billgrid('query')">高级查询</a> -->
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div region="center" border="false">
			<table id="dg-bills" 
					url="<c:url value='/inventory/getItemsInventory.tg'/>"
					idField="id" fit="true" fitColumns="true" singleSelect="true"
					pagination="true" border="false" rownumbers="true">
				<thead>
					<tr>
						<th field="code" width="80">单据编号</th>
						<th field="createdt" width="60" sortable="true">单据日期</th>
						<th field="storeId" width="60" formatter="storeFormatter">库房</th>
						<th field="createBy" width="50" formatter="formatType">制单人</th>
						<th field="remark" width="100"">备注</th>
						<th field="totalInventory" width="30" align="right">货损总量</th>
						<th field="totalSum" width="30" align="right">货损金额</th>
						<th field="status" width="40" align="center" sortable="true" formatter="formatStatus">状态</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	
	<div id="dlg-bill" class="easyui-dialog" title="货损单" style="width:850px;height:450px;position:relative"
			closed="true">
	</div>
	<div id="dd" title="订单导入" style="width:400px;height:200px;">
	</div> 
</body>
