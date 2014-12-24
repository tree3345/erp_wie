 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>销售统计</title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
        <script type="text/javascript">
		$(function(){
			$('#bill').datagrid({
				url:'<c:url value="/bill/getItemsStatisticsBill.tg"/>',
				iconCls:'icon-edit',
				width:620,
				height:360,
				singleSelect:true,
				border:false,
				pagination:true,
				rownumbers:true,
				fit:true,
                onClickRow:details,
				columns:[[
					{field:'productName',title:'商品名称',width:150,sortable:true},
					{field:'unit',title:'单位',width:50,sortable:true},
					{field:'dq',title:'销售量',width:80,align:'right',sortable:true},
					{field:'account',title:'金额(￥)',width:80,align:'right',sortable:true}
//                    {field:'profits',title:'利润(￥)',width:80,align:'right',sortable:true}
				]]
			});
		});
        function details(){
			addregion();
			var entity=$('#bill').datagrid('getSelected');
            $('#details').datagrid({
                title:entity.productName,
                url:'<c:url value="/bill/getItemsBDetail.tg"/>',
                queryParams:{
                    productId:entity.productId,
                    saleMan:$('#saleMan').val(),
					storeId:$('#sid').combobox('getValue'),
                    startTime:$('#startTime').datebox('getValue'),
                    endTime:$('#endTime').datebox('getValue')
                },
                iconCls:'icon-edit',
                singleSelect:true,
                border:false,
                pagination:true,
                rownumbers:true,
                fit:true,
                columns:[[
                    {field:'bill.billNo',title:'单号',width:180,sortable:true},
                    {field:'bill.billDateTime',title:'日期',width:180,sortable:true},
                    {field:'price',title:'价格',width:50,align:'right',sortable:true},
                    {field:'quantity',title:'数量',width:80,align:'right',sortable:true},
                    {field:'bill.createBy.userName',title:'操作员',width:70,sortable:true}
                ]]
            });
        }
		function addregion(){
			var options={
				id:'east',
				region:'east',
				width:'67%'
			};
			$('#main').layout('add',options);
			$('#east').append($('#details'));

		}
		function doSearch(){
		    $('#bill').datagrid('load',{  
		    	productName: $('#productName').val(),
                saleMan:$('#saleMan').val(),
				storeId:$('#sid').combobox('getValue'),
		    	startTime:$('#startTime').datebox('getValue'),
		    	endTime:$('#endTime').datebox('getValue')
		    });  
		}  
		function reset()
		{

			$('#startTime').searchbox('setValue', null);
			$('#endTime').searchbox('setValue', null);
			$('#sid').searchbox('setValue', null);
			$('#productName').searchbox('setValue', null);
			//清空记录
			var rows=$('#bill').datagrid('getRows');
			if(rows.length>0)
				location.reload();

		}
		//打开图表
		function openCharts(){
			var startTime=$('#startTime').datebox('getValue').replace(" ","_");
			 var endTime=$('#endTime').datebox('getValue').replace(" ","_");
			 if(startTime==''||endTime==''){
				 $.messager.alert('警告','时间不能为空'); 
				 return;
			 }
			 var storeId=$('#sid').combobox('getValue');
			 var product=$("#bill").datagrid("getSelected");
			 var url='<c:url value="/bill/showChartsBill.tg"/>?productId='+product.productId;
			 url+='&startTime='+startTime+'&endTime='+endTime+'&storeId='+storeId;
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