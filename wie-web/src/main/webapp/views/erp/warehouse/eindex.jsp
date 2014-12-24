<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui"%>
<%@ taglib prefix ="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="keywords" content="jquery,ui,easy,easyui,web">
	<meta name="description" content="easyui help you build your web page easily!">
	<title>库存信息</title>
	<%-- <link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>"> --%>
		<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/datagrid-filter.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/commons/curdtools.js"/>"></script>
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
							/* {field:'ck',checkbox:true}, */
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
				storeId: $('#sid').combotree('getValue'),
				q: $('#q').val()
			});
		}
		function reset()
		{
			$('#pclass').searchbox('setValue', null);
			$('#sid').searchbox('setValue', null);
			$('#q').searchbox('setValue', null);
		}
		
		
		
		function ExcelExport(url,datagridId){
			var queryParams = $('#'+datagridId).datagrid('options').queryParams;
			$('#'+datagridId+'tb').find('*').each(function() {
			    queryParams[$(this).attr('name')] = $(this).val();
			});
			var params = '&';
			$.each(queryParams, function(key, val){
				params+='&'+key+'='+val;
			}); 
			var fields = '&field=';
			$.each($('#'+ datagridId).datagrid('options').columns[0], function(i, val){
				if(val.field != 'opt'){
					fields+=val.field+',';
				}
			});

			window.location.href = url+ encodeURI(fields+params);
			console.log(url+ encodeURI(fields+params));
		}
		
		//导出excel
		function expexcel()
		{

			var url = "<c:url value='/warehouse/exportXlsWareDetail.tg'/>?expflag=1";
			ExcelExport(url,"tt");
			/* window.location.href = url; */
		}
		
		function impexcel()
		{
			
			/* var url = "<c:url value='/warehouse/importExcelWareDetail.tg'/>?upload"; */
			$('#impform').form('clear');
			 $('#dlg-upload').dialog({
	                width: 300,
	                height: 250,
	                modal: true,
	        		 buttons:[{
	        				text:'确定导入',
	        				iconCls:"icon-save",
	        				handler:function(){var actionUrl='<c:url value='/warehouse/impexcelWareDetail.tg'/>';
        					$('#impform').form('submit', {
        						url:actionUrl,
        						onSubmit: function(){
        								return $('#impform').form('validate');
        						},
        						success: function(data){
        							$('#dd').dialog('close');
        							$('#tt').datagrid('reload');
        							data=eval('('+data+')');
        							if(data.success){
        								$.messager.show(
        									{
        										title:'提示',
        										msg:'操作成功！',
        										showType:'slide'
        									}
        								);
        								$('#impform').form('clear');
        							}
        							if(data.error){
        								$.messager.alert('提示','操作失败！','error');
        							}
        						}
        					});
        				}
        			},{
	        				text:'关闭',
	        				iconCls:"icon-cancel",
	        				handler:function(){
	        					 $('#dlg-upload').dialog('close');
	        				}
	        			}]
	            });
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
									url="<c:url value='/class/getTreeClass.tg'/>"/>
						</td>
						<td style="width:10px">&nbsp;</td>
						<td>&nbsp;商品：</td>
						<td><input id="q" class="easyui-textbox"/></td>
						<td>&nbsp;所属仓库：</td><td><input id="sid" class="easyui-combobox" name="sid"
											  data-options="valueField:'storeId',textField:'storeName',url:'/store/users_storesStore.tg?id=${sessionScope.userId}',onSelect:doQuery" /></td>
						<td></td>
						<td style="width:10px">&nbsp;</td>

						<td>
							 <a class="easyui-linkbutton" href="javascript:void(0);" onclick="doQuery();">查找</a>
                            <a class="easyui-linkbutton" href="javascript:void(0);" onclick="reset();">清空</a>
						</td>
						<td>&nbsp;&nbsp;&nbsp;</td>
						<td><a class="easyui-linkbutton" href="javascript:void(0);" onclick="expexcel();">excel导出</a></td>
						<td>&nbsp;&nbsp;&nbsp;</td>
						<td><a class="easyui-linkbutton" href="javascript:void(0);" onclick="impexcel();">excel导入</a></td>
					</tr>
				</table>
			</div>
		</div>
		<div region="center" border="false">
			<table id="tt"></table>
		</div>
	</div>
			<div id="dlg-upload">
			<s:form id="impform" method="post" enctype="multipart/form-data">
			<s:file id="excelFile" name="excelFile"></s:file>
				<
			</s:form>
			</div>
	
</body>
</html>