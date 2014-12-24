<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/jquery.formid.js"/>"></script>
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
	<script>
		var contextPath = '<%=request.getContextPath()%>';
		var userId=${sessionScope.userId};
	    var flag=${flag};
		var userstores;
	    $(function(){
			$.getJSON(contextPath+'/store/users_storesStore.tg',{id:userId},function(json){
				userstores=json;
			});
			if(flag=="1"){
				$('#flag2').hide();
			}else if(flag=="2"){
				$('#flag1').hide();
				$('#searchstore').hide();
			}else{
				$('#flag1').hide();
				$('#flag2').hide();
			}
	    	pdata();
	    	classtree();
	    });
	    function pdata(classId){
			var url='<c:url value="/product/getItemsProduct.tg"/>?product.flag=${flag}';
			if(classId!=null){
				url+='&product.productClass.classId='+classId;
			}
			$('#tt').datagrid({
				iconCls:'icon-edit',
				width:function(){return document.body.clientWidth*0.5},
				height:function(){return document.body.clientHeight*0.5},
				singleSelect:true,
				border:false,
				rownumbers:true,
				pagination:true,
				fit:true,
				url:url,
				columns:[[
					{field:'productName',title:'产品名称',width:180},
					{field:'unit',title:'单位',width:80,align:'right'},
					{field:'salesPrice',title:'销售单价',width:80,align:'right'},
					{field:'costPrice',title:'1',hidden:true},
					{field:'orderPrice',title:'2',hidden:true},
					{field:'storeId',title:'所属商店',width:80,
						formatter:function(value){
							for(var i=0; i<userstores.length; i++){
								if (userstores[i].storeId == value) return userstores[i].storeName;
							}
							return value;
						}
					},
					{field:'purchasePrice',title:'3',hidden:true},
					{field:'priceStatus',title:'标示',hidden:true},
					{field:'productNo',title:'条形码',width:180},
					{field:'spelling',title:'缩写',width:180},
					{field:'classId',title:'分类',width:180,
						formatter:function(value,row){
							return row.className;
						}
					}
				]]
			});
	    }

		function classtree()
		{
			$('#ts').tree({
				url: '<c:url value="/class/getTreeClass.tg"/>',
				onClick: function(node){
					   $('#productName_q').searchbox('setValue', null);
					    doSearch();
				    	pdata(node.id);  // 在用户点击的时候提示
					}
			});
		}
		//所有商品
		function loadAll()
		{
			$('#productName_q').searchbox('setValue', null);
			$('#ts').tree('reload');
			doSearch();
			pdata(null);
		}
			
		function doSearch(){
		    $('#tt').datagrid('load',{
		        productName: $('#productName_q').val(),
				storeId: $('#sid').combobox('getValue')
		    });  
		}

		function showMsg(data){
			data=eval('('+data+')');
			if(data.success){
				$.messager.show(
						{
							title:'提示',
							msg:'操作成功！',
							showType:'slide'
						}
				);
			}
			if(data.error){
				$.messager.alert('提示','操作失败！','error');
			}
		}
        var url;
		function newItem(){
			$('#pfm').form('clear');
			var options={
				title: '商品信息',
				width: 600,
				height: 300,
				closed: false,
				cache: false,
				modal: true
			}
			options.buttons='#buttons';
			$('#dlg').dialog(options);
			$('#dlg').append($('#pfm'));
			$('#status').val(1);
			var treenode=$('#ts').tree('getSelected');
			if(treenode)
				$('#classId').combotree('setValue', treenode.id);
			url=contextPath+'/product/saveProduct.tg';
		}
		function editItem(){
			$('#pfm').form('clear');
			var row = $('#tt').datagrid('getSelected');
			if (row){
				var options={
					title: '商品信息',
					width: 600,
					height: 300,
					closed: false,
					cache: false,
					modal: true
				}
				options.buttons='#buttons';
				$('#dlg').dialog(options);
				$('#dlg').append($('#pfm'));
				$('#pfm').formid('loadit',row);
				url=contextPath+'/product/updateProduct.tg';
			}else{
				alert('请选择商品修改');
			}
		}
		function saveItem(){
			$('#pfm').form({
					url:url,
					onSubmit: function(){
				       return true;
			},
			success:function(data){
				$('#dlg').dialog('close');
				$('#tt').datagrid('reload');
				showMsg(data);
			}
		});
		$('#pfm').submit();
		}
		function del(id){
			url=contextPath+'/product/delProduct.tg';
			$('#id').val(id);
			$('#delform').form({
				url:url,
				onSubmit: function(){
								return true;
				},
				success:function(data){
					$('#tt').datagrid('reload');
					showMsg(data);
				}
			});
			$('#delform').submit();
		}
		function validatedel(){
			var product=$('#tt').datagrid('getSelected');
			var id=product.productId;
			$.post(contextPath+'/product/isExistDataProduct.tg',{id:id},function(data){
				data=eval('('+data+')');
				if(data.success){
					del(id);
				}
				if(data.error){
					$.messager.alert('警告','商品数据已经使用不能删除','error')
				}
			});

		}
		/**
		*   商品商店切换验证是否产生业务数据
		 */
		function vaStore(record){
			var product=$('#tt').datagrid('getSelected');
			var id=product.productId;
			$.post(contextPath+'/product/isExistDataProduct.tg',{id:id},function(data){
				data=eval('('+data+')');
				if(data.error){
					$.messager.alert('警告','商品数据已有业务数据产生，不能更改商店','error')
					$('#storeId').combobox('setValue',product.storeId);
				}
			});
		}
		function closeItem(){
			$('#dlg').dialog('close');
		}
	</script>
	<style type="text/css">
	  #_ts{float:left;width:60%;height:100%}
	</style>
</head>
<body>    
	<div class="easyui-layout" fit="true">
	    <div region="west" style=" width:12%" hide="true">
	     <div id="_ts">
         <ul id="ts" ></ul>
         </div>
        </div>
		<div region="center" style="width:70%" border="false">
		    <div id="toolbar1" class="toolbar" style="height:5%">
				<table cellpadding="0" cellspacing="0" style="width:95%;" fit="true">
							<tr>
				<td id="flag2" style="text-align:left">
				<tgEasyui:easyuiButton iconCls="icon-add" method="newItem()" permission="product:add" operationName="新增"/>
				<tgEasyui:easyuiButton iconCls="icon-edit" method="editItem()" permission="product:modify" operationName="修改"/>
				<tgEasyui:easyuiButton iconCls="icon-cancel" method="validatedel()" permission="product:delete" operationName="删除"/>
				<tgEasyui:easyuiButton iconCls="icon-reload" method="loadAll()" permission="product:modify" operationName="所有商品"/>&nbsp;&nbsp;
				</td>
				<td id="flag1" style="text-align:left">
                <tgEasyui:easyuiButton iconCls="icon-add" method="newItem()" permission="product:add1" operationName="新增"/>
					<tgEasyui:easyuiButton iconCls="icon-edit" method="editItem()" permission="product:modify1" operationName="修改"/>
					<tgEasyui:easyuiButton iconCls="icon-cancel" method="validatedel()" permission="product:delete1" operationName="删除"/>
				<tgEasyui:easyuiButton iconCls="icon-reload" method="loadAll()" permission="product:modify1" operationName="所有商品"/>&nbsp;&nbsp;
						</td>
						<td id="searchstore">
							商店：<input id="sid" class="easyui-combobox" name="sid"
								   data-options="valueField:'storeId',textField:'storeName',url:'/store/users_storesStore.tg?id=${sessionScope.userId}',onSelect:doSearch" />
							</td><td>
						    商品名称：<input id="productName_q" class="easyui-searchbox" style="width:300px" data-options="searcher:doSearch,prompt:'商品名称|缩写'">
								</td>
							</tr>
					</table>
			</div>
			<div style="height:95%">
			<table id="tt"></table>
			</div>
		</div>
	</div>
<div id="dlg"></div>
	<div id="buttons" style="text-align: center">
		<a id="btn1" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onClick="saveItem()">保存</a>
		<a id="btn2" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onClick="closeItem()">关闭</a>
	</div>
<form id="pfm" method="post">
	<jsp:include page="form.jsp"/>
</form>
<form id="delform" method="post">
	<input id="id" name="id" type="hidden">
</form>
</body>
</html>