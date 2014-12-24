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
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
	<script>
	    var classId=null;
	    var pclass=${pclass};
	    $(function(){
	    	pdata(classId);
	    	classtree();
	    });
	    function pdata(classId){
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
				url:'<c:url value="/product/getItemsProduct.tg"/>?product.flag=1&product.classId='+classId,
				columns:[[
					{field:'productName',title:'产品名称',width:200,editor:{
						type:'validatebox',
						options:{
							required:true
						}
					}
					},
					{field:'unit',title:'单位',width:80,align:'right',editor:'text'},
					{field:'salesPrice',title:'导入价'},
					{field:'productPrice',title:'单价',width:80,align:'right',editor:'text'},
					{field:'productNo',title:'条形码',width:80,editor:'text'},
					{field:'spelling',title:'缩写',width:80,editor:'text'},
					{field:'storeId',title:'商店id',hidden:true},
					{field:'classId',title:'分类',width:80,
						formatter:function(value){
							for(var i=0; i<pclass.length; i++){
								if (pclass[i].classId == value) return pclass[i].className;
							}
							return value;
						},
						editor:{
							type:'combobox',
							options:{
								valueField:'classId',
								textField:'className',
								data:pclass,
								required:true
							}
						}
					},
					{field:'action',title:'Action',width:80,align:'center',
						formatter:function(value,row,index){
							if (row.editing){
								var s = '<a href="#" onclick="saverow(this)">保存</a> ';
								var c = '<a href="#" onclick="cancelrow(this)">取消</a>';
								return s+c;
							} else {
								var e = '<a href="#" onclick="editrow(this)">编辑</a> ';
								var d = '<a href="#" onclick="deleterow(this)">删除</a>';
								return e+d;
							}
						}
					}
				]],
				onBeforeEdit:function(index,row){
					row.editing = true;
					updateActions(index);
				},
				onAfterEdit:function(index,row){
					row.editing = false;
					updateActions(index);
				},
				onCancelEdit:function(index,row){
					row.editing = false;
					updateActions(index);
				}
			});
		});
	    }
		function updateActions(index){
			$('#tt').datagrid('updateRow',{
				index: index,
				row:{}
			});
		}
		function getRowIndex(target){
			var tr = $(target).closest('tr.datagrid-row');
			return parseInt(tr.attr('datagrid-row-index'));
		}
		function editrow(target){
			$('#tt').datagrid('beginEdit', getRowIndex(target));
		}
		function deleterow(target){
			$.messager.confirm('Confirm','确定删除吗?',function(r){
				if (r){
					$('#tt').datagrid('deleteRow', getRowIndex(target));
				}
			});
		}
		function saverow(target){
			$('#tt').datagrid('endEdit', getRowIndex(target));
		}
		function cancelrow(target){
			$('#tt').datagrid('cancelEdit', getRowIndex(target));
		}
		//toolbar
		function newItem()
		{
			var row = $('#tt').datagrid('getSelected');
			var r = $('#ts').tree('getSelected');
			var id ='';
			var sid=$('#sid').val();
			if(r)
			id=r.id;
			if (row){
				var index = $('#tt').datagrid('getRowIndex', row);
			} else {
				index = 0;
				}
			if(index<0)index=0;
		$('#tt').datagrid('insertRow', {
			index: index,
			row:{
				status:'P',
				classId:id,
				storeId:sid
			}
		});
		$('#tt').datagrid('selectRow',index);
		$('#tt').datagrid('beginEdit',index);
		}
		
		function saveItem()
		{
			var $tt = $("#tt");
			if ($tt.datagrid('getChanges').length) {
                var inserted = $tt.datagrid('getChanges', "inserted");
                var deleted = $tt.datagrid('getChanges', "deleted");
                var updated = $tt.datagrid('getChanges', "updated");
                var effectRow = new Object();
                if (inserted.length) {
                    effectRow["inserted"] = JSON.stringify(inserted);
                }
                if (deleted.length) {
                    effectRow["deleted"] = JSON.stringify(deleted);
                }
                if (updated.length) {
                    effectRow["updated"] = JSON.stringify(updated);
                }

                $.post("<c:url value="/product/saveSelfSp.tg"/>", effectRow, function(data) {
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
					}
					if(data.error){
						$.messager.alert('提示','操作失败！','error');
					}
				});
            }
		}
		function rejItem()
		{
			$('#tt').datagrid('rejectChanges');
		}
		//分类
		function classtree()
		{
			$('#ts').tree({
				url: '<c:url value="/class/getTreeClass.tg"/>',
				onClick: function(node){
					   $('#productName').searchbox('setValue', null);
					    doSearch();
				    	pdata(node.id);  // 在用户点击的时候提示
					}
			});
		}
		//所有商品
		function loadAll()
		{
			 $('#productName').searchbox('setValue', null);
			    doSearch();
			pdata(null);
		}
			
		function doSearch(){  
		    $('#tt').datagrid('load',{  
		        productName: $('#productName').val()
		    });  
		}  
		//导入商品
		function impgoods()
		{
			$('#dlg-import').dialog('setTitle', '填写用户资料').dialog('open');
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
		<div region="north" border="false">
			<div class="toolbar">
				<table cellpadding="0" cellspacing="0" style="width:95%;" fit="true">
							<tr>
								<td style="text-align:left">
				<tgEasyui:easyuiButton iconCls="icon-add" method="newItem()" permission="product:add" operationName="新增"/>
				<tgEasyui:easyuiButton iconCls="icon-save" method="saveItem()" permission="product:modify" operationName="保存"/>
				<tgEasyui:easyuiButton iconCls="icon-undo" method="rejItem()" permission="product:modify" operationName="撤销"/>
				<tgEasyui:easyuiButton iconCls="icon-reload" method="loadAll()" permission="product:modify" operationName="所有商品"/>&nbsp;&nbsp;
                <input id="productName" class="easyui-searchbox" style="width:300px" data-options="searcher:doSearch,prompt:'商品名称|缩写|'"> 
                <input id="sid"  type="hidden" value="${ sessionScope.storeId}"/>
						</td>
						<td style="float:right;">
						<tgEasyui:easyuiButton iconCls="icon-mini-add" method="impgoods()" permission="product:modify" operationName="导入商品"/>
						</td>
							</tr>
					</table>
			</div>
		</div>
		
		<div region="center" style="width:70%" border="true">
			<table id="tt"></table>
		</div>
	</div>
	
	<div id="dlg-import" class="easyui-dialog" title="商品导入" style="width:850px;height:600px;position:relative"
			closed="true">
			<jsp:include page="/views/erp/_public/importGood.jsp"></jsp:include>
	</div>
   <form id="myform" method="post" >
			<input id="ids" name="productIds" type="hidden">
		</form>
			
</body>
</html>