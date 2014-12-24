<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="keywords" content="jquery,ui,easy,easyui,web">
	<meta name="description" content="easyui help you build your web page easily!">
	<title>DataGrid inline editing - jQuery EasyUI Demo</title>
	<%-- <link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>"> --%>
		<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/datagrid-filter.js"/>"></script>
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
	<script>
		var store;
		$.getJSON("<c:url value='/store/getAllStore.tg'/>", function (json){
		       store=json;
		       getclass();
		});
		
		function getclass(){
		$.getJSON("<c:url value='/class/getAllClass.tg'/>", function (json){
			   var pclass;
		       pclass=json;
		       $(function(){
					var $tt = $("#tt");
					$('#tt').datagrid({
						iconCls:'icon-edit',
						width:function(){return document.body.clientWidth*0.5},
						height:function(){return document.body.clientheight*0.5},
						singleSelect:true,
						border:false,
						rownumbers:true,
						pagination:true,
						fit:true,
						idField:'itemid',
						url:'<c:url value="/warehouse/getItemsWareDetail.tg"/>',
						columns:[[
							{field:'productName',title:'产品名称',width:150},
							{field:'classId',title:'类别',width:80,formatter:function(value){
									for(var i=0; i<pclass.length; i++){
										if (pclass[i].classId == value) return pclass[i].className;
									}
									return value;
							}},
							{field:'quantity',title:'数量',width:80,align:'right'},
							{field:'unit',title:'单位',width:80},
							{field:'storeId',title:'所属仓库',width:80,formatter:function(value){
								 for(var i=0; i<store.length; i++){
						               if (store[i].storeId == value) return store[i].storeName;
						       }
						        return value;
							}}
						]]
					});
				});
		});
		}
		
		function doQuery(){
			$('#tt').datagrid('load',{
				classId: $('#pclass').combotree('getValue'),
				q: $('#q').val()
			});
		}
		function reset()
		{
			$('#pclass').searchbox('setValue', null);
			$('#q').searchbox('setValue', null);
		}
	</script>
</head>
<body>    
	<div class="easyui-layout" fit="true" border="false">
	    <div region="north" style="height:35px" border="false">
			<div class="toolbar">
				<table cellpadding="0" cellspacing="0">
					<tr>
					    <td style="width:10px">&nbsp;</td>
						<td>分类：</td>
						<td>
							<input id="pclass" class="easyui-combotree"
									url="<c:url value='/class/getTreeClass.tg'/>"></input>
						</td>
						<td style="width:10px">&nbsp;</td>
						<td>商品：</td>
						<td><input id="q" class="easyui-textbox"></input></td>
						<td style="width:10px">&nbsp;</td>
						<td>
							 <a class="easyui-linkbutton" href="javascript:void(0);" onclick="doQuery();">查找</a>
                            <a class="easyui-linkbutton" href="javascript:void(0);" onclick="reset();">清空</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div region="center" border="false">
			<table id="tt"></table>
		</div>
	</div>
			
</body>
</html>