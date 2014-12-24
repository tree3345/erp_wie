 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>错误日志</title>
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
		var grid;
		$(function(){
			grid=$('#bill').datagrid({
				url:contextPath+'/errors/getItemsErrors.tg',
				iconCls:'icon-edit',
				width:620,
				height:360,
				singleSelect:true,
				border:true,
				pagination:true,
				rownumbers:true,
				fit:true,
                onClickRow:errordetail,
				columns:[[
					{field:'errorContent',title:'内容',width:80,sortable:true},
					{field:'macAddress',title:'mac地址',width:80,sortable:true},
					{field:'errorDateTime',title:'时间',width:120,sortable:true}
				]]
			});
		});
		function doSearch(){  
		    $('#bill').datagrid('load',{
                errorContent: $('#errorContent').val(),
                macAddress: $('#macAddress').val(),
		    	startTime:$('#startTime').datebox('getValue'),
		    	endTime:$('#endTime').datebox('getValue')
		    });  
		}  
		function reset()
		{
			$('#startTime').searchbox('setValue', null);
			$('#endTime').searchbox('setValue', null);
			$('#errorContent').searchbox('setValue', null);
			$('#macAddress').searchbox('setValue', null);
		}
        function errordetail(){
            var error=$('#bill').datagrid("getSelected");
            var url=contextPath+'errors/findErrors.tg';
            $.post(url,{id:error.id},function(rs){
                rs = eval('(' + rs + ')');
                $('#errorDetail').text(rs.errorContent);
            });
        }
	</script>