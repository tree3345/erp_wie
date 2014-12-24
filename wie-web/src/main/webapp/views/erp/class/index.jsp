<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>往来单位分类</title>
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/etree/jquery.etree.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/etree/jquery.etree.lang.js"/>"></script>
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
	<script type="text/javascript">
	   var para=null;
	   $(function(){
		   typetree();
		   getItems(para);
	   });
	   //树
		function typetree()
		{
			$('#type').etree({
				url: '<c:url value="/class/getTreeClass.tg"/>',
				createUrl: '<c:url value="/class/createClass.tg"/>',
			    updateUrl: '<c:url value="/class/updateClass.tg"/>',
			    destroyUrl: '<c:url value="/class/destroyClass.tg"/>',
				dndUrl: '<c:url value="/class/dndClass.tg"/>',
				onClick:function(node){
					getItems(node.id);
				}
				
			});
		}
		//详细信息
		function getItems(para)
		{
			var realurl='<c:url value="/class/getItemsClass.tg"/>';
			if(para!=null)
				realurl+='?parentId='+para;
			$(function(){
				var $tt = $("#tt");
				$('#tt').datagrid({
					iconCls:'icon-edit',
					width:function(){return document.body.clientWidth*0.5},
					height:function(){return document.body.clientheight*0.5},
					singleSelect:true,
					border:false,
					rownumbers:true,
					fit:true,
					url:realurl,
					columns:[[
						{field:'className',title:'名称',width:80,editor:'text'},
						{field:'icon',title:'图片路径',width:180,editor:'text'},
						{field:'sort',title:'排序',width:30,editor:'text'},
						{field:'action',title:'Action',width:120,align:'center',
							formatter:function(value,row,index){
								if (row.editing){
									var s = '<button  onclick="saverow(this)">保存</button> ';
									var c = '<button  onclick="cancelrow(this)">取消</button>';
									return s+c;
								} else {
									var e = '<button  onclick="editrow(this)">编辑</button>';
									var d = '<button  onclick="deleterow(this)">删除</button>';
									return e;
								}
							}
						}
					]],toolbar : [{
						text:"刷新",
						iconCls:"icon-reload",
						handler:function(){
							 $('#tt').datagrid('reload');
						}
					},{
		                text : "提交修改",
		                iconCls : "icon-save",
		                handler : function() {
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
		  
		                        $.post("<c:url value="/class/saveClass.tg"/>", effectRow, function(data) {
		                        	    $('#tt').datagrid('reload');
		                        	    $('#type').tree('reload');
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
		            }],
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
			function delclass(){
				
				var classObj=$('#type').tree('getSelected');
				var isLeaf=$('#type').tree('isLeaf',classObj.target);
				//console.log(isLeaf);
				  if(!isLeaf){
					$.messager.alert('提示','存在子分类，不能删除！','error');
					return;
				}  
				 
				 $.post('<c:url value="/class/destroyJudgeClass.tg"/>', {id:classObj.id}, function(data) {
            		data=eval('('+data+')');
            		//console.log(data);
					if(data.success){
						$('#type').etree('destroy');
						
					}
					if(data.error){
						$.messager.alert('提示',data.message,'error');
					}
					
				});  
				//console.log(classObj.id);
				//$('#type').etree('destroy');
			}
	</script>
</head>
<body>
	
	<div class="easyui-layout" fit="true">
		<div region="west" border="false" style="width:15%;padding:5px;border-right:1px solid #92B7D0">
			<div class="toolbar">
			 <table>
			    <tr>
			    <td>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="javascript:$('#type').etree('create')">创建</a>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"onclick="javascript:$('#type').etree('edit')">编辑</a>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="delclass()">删除</a>
				</td>
				</tr>
				</table>
	       </div>
	       <div>
			<ul id="type"></ul>
			</div>
		</div>
		<div region="center" border="false">
		   <table id="tt"></table>
		</div>
	</div>
</body>
</html>