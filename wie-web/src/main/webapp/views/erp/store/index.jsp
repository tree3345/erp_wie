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
	    $(function(){
	    	pdata();
	    });
	    function pdata(){
		$(function(){
			var $tt = $("#tt");
			$('#tt').datagrid({
				iconCls:'icon-edit',
				width:function(){return document.body.clientWidth*0.5},
				height:function(){return document.body.clientHeight*0.5},
				singleSelect:true,
				border:false,
				rownumbers:true,
				pagination:true, 
				fit:true,
				idField:'itemid',
				url:'<c:url value="/store/getItemsStore.tg"/>',
				columns:[[
					{field:'storeName',title:'单位',width:160,editor:'text'},
					{field:'action',title:'Action',width:120,align:'center',
						formatter:function(value,row,index){
							if (row.editing){
								var s = '<button  onclick="saverow(this)">保存</button> ';
								var c = '<button  onclick="cancelrow(this)">取消</button>';
								return s+c;
							} else {
								var e = '<button  onclick="editrow(this)">编辑</button>';
								var d = '<button  onclick="deleterow(this)">删除</button>';
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
			console.log(target);
			/* $.getJSON('<c:url value="/product/isExistSelfSp.tg"/>?storeId='+this.storeId, function(json){
				
			}); */
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
		function newItem(){
			var row = $('#tt').datagrid('getSelected');
			if (row){
				var index = $('#tt').datagrid('getRowIndex', row);
			} else {
				index = 0;
			}
			$('#tt').datagrid('insertRow', {
				index: index,
				row:{
					status:'P'
				}
			});
			$('#tt').datagrid('selectRow',index);
			$('#tt').datagrid('beginEdit',index);
		}
		//单击事件
		$(function(){
			$('#tt').datagrid({onClickRow:function dblClickRow(rowIndex,rowData){
				var row = $('#tt').datagrid('getSelected');
				
				$.getJSON('<c:url value="/product/isExistSelfSp.tg"/>?storeId='+row.storeId, function(json){
					  if(json.success){
						  $('#init').text('已初始化');
					  }
					  else
						  {
						  $('#init').text('未进行初始化');
						  }
					});
				
			}});
			});
		//toolbar
		function newItem()
		{
			var row = $('#tt').datagrid('getSelected');
			if (row){
				var index = $('#tt').datagrid('getRowIndex', row);
			} else {
				index = 0;
				}
		$('#tt').datagrid('insertRow', {
			index: index,
			row:{
				status:'P'
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

                $.post("<c:url value="/store/saveStore.tg"/>", effectRow, function(data) {
                	
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
			
		function doSearch(){  
		    $('#tt').datagrid('load',{  
		        storeName: $('#storeName').val()
		    });  
		}  
	</script>
</head>
<body>    
	<div class="easyui-layout" fit="true">
	    <div region="center" style=" width:25%" hide="true">
	     
	     <div class="toolbar" style="height:5%">
			<table cellpadding="0" cellspacing="0" style="width:95%;" fit="true">
			  <tr>
			    <td>
				
				 <tgEasyui:easyuiButton iconCls="icon-add" method="newItem()" permission="store:add" operationName="新增"/>
				 <tgEasyui:easyuiButton iconCls="icon-save" method="saveItem()" permission="store:modify" operationName="提交"/>
				 <tgEasyui:easyuiButton iconCls="icon-undo" method="rejItem()" permission="store:modify" operationName="撤销"/>
				 
				  <input id="storeName" name="store.storeName" class="easyui-searchbox" style="width:110px" data-options="searcher:doSearch,prompt:'商店名称'"> 
				</td>
				
		     </tr>
			</table>
		</div>
	    <div style="height:95%">
        <table id="tt"></table>
        </div>
        </div>
		<div region="north" border="false">
			
		</div>
		
	</div>
	
   
			
</body>
</html>