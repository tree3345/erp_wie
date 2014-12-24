 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>权限管理系统</title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
        <script type="text/javascript">
        	var contextPath = '<%=request.getContextPath()%>';
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
	<style type="text/css">
		.e-input{
			width:198px;
			border:1px solid #A4BED4;
			height:18px;
			line-height:18px;
		}
	</style>
	<script type="text/javascript">
	    var products=${products}
		$(function(){
			$('#bill').datagrid({onClickRow:function dblClickRow(rowIndex,rowData){
				detail();
			}
			});
		});
		function detail()
		{
			var row = $('#bill').datagrid('getSelected');
			var ourl='<c:url value="/bill/getItemsBDetail.tg"/>';
			var allurl=ourl+'?billId='+row.billId;
			/* alert(rowIndex+"-"+JSON.stringify(rowData)); 
			alert(row.purchaseId);*/
			var $tt = $("#tt");
			$('#tt').datagrid({
				iconCls:'icon-edit',
				width:function(){return document.div.clientWidth*0.5},
				height:function(){return document.div.clientheight*0.5},
				singleSelect:true,
				border:true,
				order:true,
				rownumbers:true,
				pagination:true,
				fit:true,
				url:allurl,
				columns:[[
					{field:'billNo',title:'销售单号',width:200},
					{field:'productName',title:'产品名称',width:200},
					{field:'price',title:'单价',width:80,align:'right'},
					{field:'quantity',title:'数量',width:80,align:'right'},
					{field:'discount',title:'折扣',width:80}
					]],
			});
		}
		var grid;
		$(function(){
			grid=$('#bill').datagrid({
				url:'<c:url value="/bill/getItemsBill.tg"/>',
				iconCls:'icon-edit',
				width:620,
				height:360,
				singleSelect:true,
				border:true,
				pagination:true,
				rownumbers:true,
				fit:true,
				columns:[[
					{field:'memberId',title:'会员编号',width:80,sortable:true},
					{field:'billNo',title:'销售单号',width:80,sortable:true},
					{field:'saleMoney',title:'应收金额',width:80,sortable:true},
					{field:'sumMoney',title:'实收金额',width:80,sortable:true},
					{field:'createBy',title:'销售员',width:80,sortable:true},
					{field:'billDateTime',title:'销售日期',width:100,sortable:true}
				]]
			});
		});
		function doSearch(){  
		    $('#bill').datagrid('load',{  
		    	memberId: $('#memberId').val(),  
		    	createBy: $('#createBy').val(),
		    	startTime:$('#startTime').datebox('getValue'),
		    	endTime:$('#endTime').datebox('getValue')
		    });  
		}  
		function reset()
		{
			$('#startTime').searchbox('setValue', null);
			$('#endTime').searchbox('setValue', null);
			$('#memberId').searchbox('setValue', null);
			$('#createBy').searchbox('setValue', null);
		}
	</script>