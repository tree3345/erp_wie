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
	    var intercourseTypeId='';
	    var intercourseType=${intercourseType};
	    $(function(){
	    	pdata(intercourseTypeId);
	    	classtree();
	    });
	    function pdata(intercourseTypeId){
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
				url:'<c:url value="/intercourse/getItemsIntercourse.tg"/>?intercourse.intercourseTypeId='+intercourseTypeId,
				columns:[[
					{field:'shortName',title:'单位名称',width:200,editor:{
						type:'validatebox',
						options:{
							required:true
						}
					}
					},
					{field:'intercourseTypeId',title:'单位分类',width:100,formatter:function(value,row){
						//console.log(row.intercourseTypeId);
						//console.log(intercourseType);
						for(var i=0;i<intercourseType.length;i++){
							if(intercourseType[i].id==row.intercourseTypeId){
								//console.log(intercourseType[i].id+':'+intercourseType[i].name);
								return intercourseType[i].name;
							}
						}
						return value;
					},
					editor:{
						type:'combotree',
						options:{
							url:'<c:url value="/intercourse/getTreeInterType.tg"/>',
							valueField:'id',
							textField:'text',
							required:true
						}
					}
					},
					{field:'code',title:'编码',width:80,editor:'text'},
					{field:'addr',title:'地址',width:180,editor:'text'},
					{field:'contactMan',title:'联系人',width:80,editor:'text'},
					{field:'email',title:'电子邮箱',width:180,editor:'text'},
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
			var rows=$('#tt').datagrid('getRows');
			var index=getRowIndex(target);
			console.log(rows[index]);
			 $.post('<c:url value="/intercourse/isExistDataIntercourse.tg"/>',{intercourseId:rows[index].id},function(data){
				data=eval('('+data+')');
				if(data.error){
					$.messager.alert('提示','与入库或采购计划有关联数据,不能删除','error');
				}
				if(data.success){
					$.messager.confirm('Confirm','确定删除吗?',function(r){
						if (r){
							$('#tt').datagrid('deleteRow', getRowIndex(target));
						}
					});
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
			if(r)
			id=r.id;
			//	console.log(row);
			if (row){
				var index = $('#tt').datagrid('getRowIndex', row);
			} else {
				index = 0;
				}
			console.log(row);
		$('#tt').datagrid('insertRow', {
			index: index,
			row:{
				status:'P',
				intercourseTypeId:id
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

                $.post("<c:url value="/intercourse/saveIntercourse.tg"/>", effectRow, function(data) {
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
				url: '<c:url value="/intercourse/getTreeInterType.tg"/>',
				onClick: function(node){
					   $('#paraName').searchbox('setValue', null);
					    doSearch();
				    	pdata(node.id);  // 在用户点击的时候提示
					}
			});
		}
		//所有商品
		function loadAll()
		{
			$('#paraName').searchbox('setValue', null);
		    doSearch();
			pdata('');
		}
			
		function doSearch(){  
		    $('#tt').datagrid('load',{  
		    	q: $('#paraName').val()
		    });  
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
                <input id="paraName" class="easyui-searchbox" style="width:300px" data-options="searcher:doSearch,prompt:'单位名称'"> 
						</td>
							</tr>
					</table>
			</div>
		</div>
		
		<div region="center" style="width:70%" border="true">
			<table id="tt" ></table>
		</div>
	</div>
</body>
</html>