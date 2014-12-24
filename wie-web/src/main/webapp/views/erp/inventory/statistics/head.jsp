 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>货损统计</title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
        <script type="text/javascript">
      /* 格式化处理*/
      function nullformatter(value){
         // console.log(value);
          if(value==null){
              return '0.00';
          }
          else{
              return value;
          }
          //console.log(value);
      }
		var grid;
		$(function(){
			grid=$('#bill').datagrid({
				url:'<c:url value="/inventory/getItemsStatisticsInventory.tg"/>',
				iconCls:'icon-edit',
				singleSelect:true,
				pagination:true,
				rownumbers:true,
				fit:true,
				border:false,
                onClickRow:details,
				columns:[[
					{field:'productName',title:'商品名称',width:150,sortable:true},
                    {field:'unit',title:'单位',width:150,sortable:true},
					{field:'dq',title:'货损量',width:80,align:'right',sortable:true},
                    {field:'dqsum',title:'货损金额',width:80,align:'right',sortable:true,formatter:nullformatter}
				]]
			});
		});
        function details(){
            var entity=$('#bill').datagrid('getSelected');
            $('#details').datagrid({
                title:entity.productName,
                url:'<c:url value="/inventory/getItemsVDetail.tg "/>',
                queryParams:{
                    productId:entity.productId,
                    inby:$('#createBy').val(),
					storeId:$('#sid').combobox('getValue'),
                    startTime:$('#startTime').datebox('getValue'),
                    endTime:$('#endTime').datebox('getValue')
                },
                iconCls:'icon-edit',
                width:620,
                height:360,
                singleSelect:true,
                border:false,
                pagination:true,
                rownumbers:true,
                fit:true,
                columns:[[
                    {field:'inventory.code',title:'单号',width:150,sortable:true},
                    {field:'inventory.checkDate',title:'日期',width:150,sortable:true},
                    {field:'price',title:'单价',width:80,align:'right',sortable:true,formatter:nullformatter},
                    {field:'damageQuantity',title:'数量',width:80,align:'right',sortable:true},
                    {field:'inventory.createBy.name',title:'操作员',width:80,align:'right',sortable:true}
                ]]
            });
        }
		function doSearch(){  
		    $('#bill').datagrid('load',{
                createBy:$('#createBy').val(),
		    	productName: $('#productName').val(),
				storeId:$('#sid').combobox('getValue'),
		    	startTime:$('#startTime').datebox('getValue'),
		    	endTime:$('#endTime').datebox('getValue')
		    });  
		}  
		function reset()
		{
			$('#startTime').searchbox('setValue', null);
			$('#sid').searchbox('setValue', null);
			$('#endTime').searchbox('setValue', null);
			$('#productName').searchbox('setValue', null);
			var rows=$('#bill').datagrid('getRows');
			if(rows.length>0) location.reload();
		}
		//打开图表
		function openCharts(){
			var startTime=$('#startTime').datebox('getValue').replace(" ","_");
			// console.log(startTime);
			 var endTime=$('#endTime').datebox('getValue').replace(" ","_");
			// console.log(endTime);
			 if(startTime==''||endTime==''){
				 $.messager.alert('警告','时间不能为空'); 
				 return;
			 }
			var product=$("#bill").datagrid("getSelected");
			var storeId=$('#sid').combobox('getValue');
			 if(!product){
				 $.messager.alert('警告','没有选择数据！','error');    
				 return;
			 }
			 //console.log(product);
			 var url='<c:url value="/inventory/showChartsInventory.tg"/>?productId='+product.productId;
			 url+='&startTime='+startTime+'&endTime='+endTime+'&storeId='+storeId;;
			// console.log(url);
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