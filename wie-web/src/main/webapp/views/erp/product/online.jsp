<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui" %>
<head>
	 <link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
	<script type="text/javascript">
	function datareload(){
		$('#eb').datagrid('reload');
	}
	
	 $(function() {  
	        setInterval("datareload()", 5000);  
	    });  
	</script>
</head>
<body style= "margin:0px;">
	<div class="easyui-layout" fit="true">
		<div region="north" border="false">
			
		</div>
		
		<div region="center" border="false">
			<table id="eb" class="easyui-datagrid"
					url="<c:url value='/product/onlineProduct.tg'/>"
					singleSelect="true"   rownumbers="true"
					border="false" fit="true">
				<thead>
					<tr>
					    <th field="store" width="80">商店</th>
						<th field="ip" width="100">ip地址</th>
						<th field="username" width="50">操作员</th>
						
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>
