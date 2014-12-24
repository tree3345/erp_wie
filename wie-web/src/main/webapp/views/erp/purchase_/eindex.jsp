<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
	<script>
	    var products=${products};
		$(function(){
			var $tt = $("#tt");
			$('#tt').datagrid({
				iconCls:'icon-edit',
				width:620,
				height:360,
				singleSelect:true,
				border:true,
				rownumbers:true,
				idField:'itemid',
				columns:[[
					{field:'productId',title:'产品名称',width:200,formatter:function(value){
						for(var i=0; i<products.length; i++){
							if (products[i].productId == value) return products[i].productName;
						}
						return value;
					},
					editor:{
						type:'combobox',
						options:{
							valueField:'productId',
							textField:'productName',
							data:products,
							required:true
						}
					}
					},
					{field:'price',title:'价格',width:80,align:'right',editor:{type:'numberbox',options:{precision:1}}},
					{field:'quantity',title:'数量',width:80,align:'right',editor:'text'},
					{field:'action',title:'操作',width:80,align:'center',
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
				]],toolbar : [ {
	                text : "添加",
	                iconCls : "icon-add",
	                handler : function() {
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
			function endEdit(){
	            var rows = $tt.datagrid('getRows');
	            for ( var i = 0; i < rows.length; i++) {
	                $tt.datagrid('endEdit', i);
	            }
	        }
		});
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
		function newItem(){
			$('#tt').datagrid('reload');
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
		 //公共方法
		var actionUrl;
		function newItem(){
			$('#dlg').dialog('setTitle', '填写入库信息').dialog('open');
			$('#myform').form('clear');
			$.each($('#myform input'),function(i){
				$(this).removeAttr("readonly");
			});
			/* $("#dlg-buttons a::first-child").show(); */
			actionUrl = '<c:url value="/purchase/savePurchase.tg"/>';
		}
		function saveItem(){
			   var $tt = $("#tt");
				$('#myform').form('submit', {
					url:actionUrl,
					onSubmit: function(){
						endEdit();
	                    if ($tt.datagrid('getChanges').length) {
	                        var inserted = $tt.datagrid('getChanges', "inserted");
	                        var deleted = $tt.datagrid('getChanges', "deleted");
	                        var updated = $tt.datagrid('getChanges', "updated");
	                        var effectRow = new Object();
	                        if (inserted.length) {
	                            $('#ins').val(JSON.stringify(inserted));
	                        }
	                        if (deleted.length) {
	                        	$('#del').val(JSON.stringify(deleted));
	                        }
	                        if (updated.length) {
	                        	$('#upd').val(JSON.stringify(updated));
	                        }
	                    }
							return $('#myform').form('validate');
					},
					success: function(data){
						$('#dlg').dialog('close');
						$('#t-dictionarys').datagrid('reload');
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
				});
			}
		function closewindow()
		{
			$('#dlg').dialog('close');
			$('#t-dictionarys').datagrid('reload');
		}
		function endEdit(){
		    var $tt = $("#tt");
            var rows = $tt.datagrid('getRows');
            for ( var i = 0; i < rows.length; i++) {
                $tt.datagrid('endEdit', i);
            }
        }
		
		//单击行
		$(function(){
			$('#t-dictionarys').datagrid({onClickRow:function dblClickRow(rowIndex,rowData){
				var row = $('#t-dictionarys').datagrid('getSelected');
				var ourl='<c:url value="/purchase/getItemsPurDetail.tg"/>';
				var allurl=ourl+'?purchaseId='+row.purchaseId;
				/* alert(rowIndex+"-"+JSON.stringify(rowData)); 
				alert(row.purchaseId);*/
				var $tt2 = $("#tt2");
				$('#tt2').datagrid({
					iconCls:'icon-edit',
					width:function(){return document.div.clientWidth*0.5},
					height:function(){return document.div.clientheight*0.5},
					singleSelect:true,
					border:true,
					rownumbers:true,
					idField:'itemid',
					url:allurl,
					columns:[[
						{field:'productName',title:'产品名称',width:100,editor:{
							type:'validatebox',
							options:{
								required:true
							}
						}
						},
						{field:'price',title:'价格',width:80,align:'right',editor:{type:'numberbox',options:{precision:1}}},
						{field:'unit',title:'单位',width:80,align:'right',editor:'text'},
						{field:'productNo',title:'条形码',width:180,editor:'text'},
						{field:'spelling',title:'缩写',width:180,editor:'text'},
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
					]],toolbar : [ {
		                text : "添加",
		                iconCls : "icon-add",
		                handler : function() {
		                	var row = $('#tt2').datagrid('getSelected');
		    				if (row){
		    					var index = $('#tt2').datagrid('getRowIndex', row);
		    				} else {
		    					index = 0;
		    					}
		    			$('#tt2').datagrid('insertRow', {
		    				index: index,
		    				row:{
		    					status:'P'
		    				}
		    			});
		    			$('#tt2').datagrid('selectRow',index);
		    			$('#tt2').datagrid('beginEdit',index);
		                }
		            }, {
		                text : "保存",
		                iconCls : "icon-save",
		                handler : function() {
		                    endEdit();
		                    if ($tt2.datagrid('getChanges').length) {
		                        var inserted = $tt2.datagrid('getChanges', "inserted");
		                        var deleted = $tt2.datagrid('getChanges', "deleted");
		                        var updated = $tt2.datagrid('getChanges', "updated");
		                        var effectRow = new Object();
		                        if (inserted.length) {
		                            effectRow["inserted"] = JSON.stringify(inserted);
		                            alert(JSON.stringify(inserted));
		                        }
		                        if (deleted.length) {
		                            effectRow["deleted"] = JSON.stringify(deleted);
		                            alert(JSON.stringify(deleted));
		                        }
		                        if (updated.length) {
		                            effectRow["updated"] = JSON.stringify(updated);
		                            alert(JSON.stringify(updated));
		                        }
		 
		                        $.post("<c:url value="/purchase/savePurDetail.tg"/>", effectRow, function(rsp) {
		                            if(rsp.status){
		                                $.messager.alert("提示", "提交成功！");
		                                $tt2.datagrid('acceptChanges');
		                            }
		                        }, "JSON").error(function() {
		                            $.messager.alert("提示", "提交错误了！");
		                        });
		                    }
		                }
		            }],
					onBeforeEdit:function(index,row){
						row.editing = true;
						updateActions2(index);
					},
					onAfterEdit:function(index,row){
						row.editing = false;
						updateActions2(index);
					},
					onCancelEdit:function(index,row){
						row.editing = false;
						updateActions2(index);
					}
				});
			}
			});
		});
		function updateActions2(index){
			$('#tt2').datagrid('updateRow',{
				index: index,
				row:{}
			});
		}
		function getRowIndex2(target){
			var tr = $(target).closest('tr.datagrid-row');
			return parseInt(tr.attr('datagrid-row-index'));
		}
		function editrow2(target){
			$('#tt2').datagrid('beginEdit', getRowIndex(target));
		}
		function deleterow2(target){
			$.messager.confirm('Confirm','确定删除吗?',function(r){
				if (r){
					$('#tt2').datagrid('deleteRow', getRowIndex(target));
				}
			});
		}
		function saverow2(target){
			$('#tt2').datagrid('endEdit', getRowIndex(target));
		}
		function cancelrow2(target){
			$('#tt2').datagrid('cancelEdit', getRowIndex(target));
		}
	</script>
</head>
<body style="margin: 0; padding: 0; height: 100%; overflow: hidden; background: #F2FBFF">
	<form id="dicform" method="post">
		<input type="hidden" id="dicId" name="id" value=""/>
	</form>
	<div margin:0 auto;width:auto;height:100%>
	<div style=" float:left; width:40%; height:100%;">
	<div class="easyui-layout" fit="true">
		<div region="north" border="false">
			<div class="toolbar">
				<table cellpadding="0" cellspacing="0" style="width:95%;">
						<tr>
							<td style="text-align: left;">
				<tgEasyui:easyuiButton iconCls="icon-add" method="newItem()" permission="dictionary:add" operationName="新增"/>
				<tgEasyui:easyuiButton iconCls="icon-edit" method="editItem()" permission="dictionary:modify" operationName="修改"/>
				<tgEasyui:easyuiButton iconCls="icon-cancel" method="delItem()" permission="dictionary:delete" operationName="删除"/>
							</td>
							<td style="text-align: right;"> 
				<tgEasyui:easyuiButton iconCls="icon-search" method="advanceQuery()" permission="dictionary:query" operationName="高级查询"/>
							</td>
						</tr>
					</table>
			</div>
		</div>
		<div region="center" border="false">
			<table id="t-dictionarys" class="easyui-datagrid"
					url="<c:url value='/purchase/getItemsPurchase.tg'/>"
					singleSelect="true" pagination="true" rownumbers="true"
					border="true" fit="true" onClickRow="true">
				<thead>
					<tr>
						<th field="purchaseId" width="260" sortable="true">单据号</th>
						<th field="totalPrice" width="100" >总价</th>
						<th field="purchaseTime" width="110">入库时间</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	
	<div id="dlg" style="width:680px;height:500px;"
			class="easyui-dialog" closed="true" modal="true" buttons="#dlg-buttons">
			<div style="padding:20px">
				<form id="myform" method="post" style="margin:0;padding:0">
					<table>
						<tr>
							<td>订单号</td>
							<td><input type="text" id="purchase.storeId" class="easyui-validatebox e-input" name="purchase.storeId" required="true"></input></td>
							<td>总价：</td>
							<td>
								<input type="text" id="totalprice" class="easyui-validatebox e-input" name="purchase.totalPrice" required="true" />
							    <input type="hidden" id="ins" name="ins">
							    <input type="hidden" id="del" name="del">
							    <input type="hidden" id="upd" name="upd">
							</td>
						</tr>
					</table>
					<table id="tt"></table>
				</form>
			</div>
		<div id="dlg-buttons" style="text-align:center;">
			<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="saveItem()">保存</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="closewindow()">关闭</a>
		</div>
	</div>
	</div>
	<div style="float:left; width:60%;height:100%;">
	     <div class="easyui-layout" fit="true">
		<div region="north" border="false">
			
		</div>
		
		<div region="center" border="false">
			<table id="tt2"></table>
		</div>
	</div>
	</div>
</body>
