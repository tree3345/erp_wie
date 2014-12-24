<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/js/easyui/themes/icon.css"/>">
<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
<script type="text/javascript">
	var pclass = "";
	var classId = null;
	var productId=null;
	var status=${status};
	$(function() {
		//初始化
		classtree();
		getclassjson();
		products();
		   $('#products').datagrid({onClickRow:function dblClickRow(rowIndex,rowData){
			pricehistory(rowData);
		}
		});
	});
	function getclassjson() {
		$.getJSON("<c:url value='/class/getAllClass.tg'/>", function(json) {
			pclass = json;
		});
	}
	function classtree() {
		$('#classtree').tree({
			url : '<c:url value="/class/getTreeClass.tg"/>',
			onClick : function(node) {
				$('#productName').searchbox('setValue', null);
				doSearch();
				products(node.id);
			}
		});
	}
	function products(classId) {
		var storeId=$('#sid').combobox('getValue');
		var url = '<c:url value="/product/getItemsProduct.tg"/>?product.flag=0';
		if (classId != null) {
			url += '&product.productClass.classId=' + classId;
		}
		if(storeId=="")
			console.log(storeId);
		$("#products").datagrid({
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			fit : true,
			url : url,
			columns : [ [ {
				field : 'productName',
				title : '产品名称',
				width : 170
			}, {
				field : 'unit',
				title : '单位',
				width : 40,
				align : 'right'
			}, {
				field : 'salesPrice',
				title : '销售单价',
				width : 80,
				hidden : true
			}, {
				field : 'storeId',
				title : '3',
				hidden : true
			}, {
				field : 'purchasePrice',
				title : '3',
				hidden : true
			}, {
				field : 'priceStatus',
				title : '标示',
				hidden : true
			}, {
				field : 'productNo',
				title : '条形码',
				width : 130
			}, {
				field : 'spelling',
				title : '缩写',
				width : 80
			}, {
				field : 'classId',
				title : '分类',
				width : 80,
				formatter : function(value) {
					for (var i = 0; i < pclass.length; i++) {
						if (pclass[i].classId == value)
							return pclass[i].className;
					}
					return value;
				}
			} ] ]
		});
	}
	//历史价格
	function pricehistory(product){
		console.log($('#sid').combobox('getValue'));
		var title="";
		var url = '<c:url value="/priceHistory/getItemsPriceHistory.tg"/>?productPriceHistory.status='+status;
		if($('#sid').combobox('getValue')!="")
		    url+='&storeId='+$('#sid').combobox('getValue');
		if (product != null) {
			url += '&productPriceHistory.productId=' + product.productId;
		}
		
		if(status==0)
			title=product.productName+'-销售历史价格'
		if(status==1)
			title=product.productName+'-入库历史价格'
		$("#price").datagrid({
			title:title,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			fit : true,
			url : url,
			columns : [ [ {
				field : 'status',
				title : '标识',
				hidden:true
			}, 
			{
				field : 'unitPrice',
				title : '价格',
				width : 50
			}, 
			{
				field : 'activeDT',
				title : '更新日期',
				sortable:true,
				width : 100
			},
			{
				field : 'storeId',
				title : '商店',
				sortable:true,
				width : 100

			}
			] ],
		toolbar:'#searcher'

		});
	}

	function storeformatter(value){
		var options=$('#sid').combobox(options);
		console.log(options);
		return value;
	}
	function doSearch() {
		 $('#products').datagrid('load', {
			productName: $('#productName').val(),
			 storeId:$('#sid').combobox('getValue')
		});
	}
	//价格时间查询
	function dateSearch(){
		
		 $('#price').datagrid('load',{  
		    	startTime:$('#startTime').datebox('getValue'),
		    	endTime:$('#endTime').datebox('getValue')
		    });  
		 $('#price').datagrid('reload');
	}
	function reset()
	{
		$('#startTime').searchbox('setValue', null);
		$('#endTime').searchbox('setValue', null);
	}
	//打开图表
	function openCharts(){
		 dateSearch();
		var startTime=$('#startTime').datebox('getValue').replace(" ","_");
		 var endTime=$('#endTime').datebox('getValue').replace(" ","_");
		 if(startTime==''||endTime==''){
			 $.messager.alert('警告','时间不能为空'); 
			 return;
		 }
		 var product=$("#products").datagrid("getSelected");
		 var url='<c:url value="/priceHistory/showChartsPriceHistory.tg"/>?productId='+product.productId+'&status='+status;
		 url+='&startTime='+startTime+'&endTime='+endTime;
		 $('#container').dialog('clear');
		 $('#container').append('<iframe id="chart" style="width:99%;height:99%" src='+url+'></iframe>');
		 $('#container').dialog({    
		    title: '报表',    
		    width: 1000,    
		    height: 600,  
		    maximizable:true,
		    modal: true   
		});     
	}

</script>
</head>
<body>
	<div class="easyui-layout" fit="true" border="false">
		<div region="west" title="商品信息" style="width: 47%;margin:0 auto" split="true">
		   <div id="ctree" style="width:20%;height:100%;float:left" >
		   <ul id="classtree"></ul>
		   </div>
			<div id="pdata" style="width:80%;height:100%;float:left">
			   <div id="search" style="height:4% ;margin:0.5% 0">
				   <input id="sid" name="storeId" class="easyui-combobox" prompt="商店名称" style="width:180px"
						  data-options="valueField:'storeId',textField:'storeName',url:'/store/users_storesStore.tg?id=${sessionScope.userId}'" />
				<input id="productName" class="easyui-searchbox"
					style="width: 300px"
					data-options="searcher:doSearch,prompt:'商品名称|缩写'">
			   </div>
			   <div id="datagrid" style="height:94.5%">
				<table id="products" border="false"></table>
			  </div>
			</div>
		</div>
		<div region="center"  style="width: 36%"  split="true">
		   <table id="price"></table>
		</div>
	</div>
    <div id="container"></div>
	<div id="searcher">
		<table>
			<tr>
				<th><span class="inp1">开始时间:</span></th>
				<td>
					<input class="easyui-datetimebox" id="startTime" editable="false" name="startTime" /></td>
				<th><span class="inp1">结束时间:</span></th>
				<td>
					<input class="easyui-datetimebox"  id="endTime" editable="false" name="endTime" /></td>
			</tr>
			<tr>
				<td style="text-align:right;">
					<a class="easyui-linkbutton" href="javascript:void(0);" onclick="dateSearch();">查找</a>
					<a class="easyui-linkbutton" href="javascript:void(0);" onclick="reset();">清空</a>
					</td>
				<td>
					<a class="easyui-linkbutton" href="javascript:void(0);"data-options="iconCls:'icon-large_chart'" onclick="openCharts();">报表</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>