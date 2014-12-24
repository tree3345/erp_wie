 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>权限管理系统</title>
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
	    var products=${products}
		$(function(){
			$('#t-dictionarys').datagrid({onClickRow:function dblClickRow(rowIndex,rowData){
				var row = $('#t-dictionarys').datagrid('getSelected');
				var ourl='<c:url value="/purchase/getItemsPurDetail.tg"/>';
				var allurl=ourl+'?purchaseId='+row.purchaseId;
				/* alert(rowIndex+"-"+JSON.stringify(rowData)); 
				alert(row.purchaseId);*/
				var purid=row.purchaseId;
				var $tt = $("#tt");
				$('#tt').datagrid({
					iconCls:'icon-edit',
					width:function(){return document.div.clientWidth*0.5},
					height:function(){return document.div.clientheight*0.5},
					singleSelect:true,
					border:true,
					rownumbers:true,
					idField:'productId',
					url:allurl,
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
						{field:'listprice',title:'总价',width:80,editor:'text'},
						{field:'purchaseId',title:'入库单号',width:80,editor:'text',hidden:true},
						{field:'orderId',title:'订单号',width:80,editor:'text',hidden:true},
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
		    					status:'P',
		    					purchaseId:purid,
		    					orderId:'512F6AA9-5322-4DF5-81AC-1B3EA2A7CFE7'
		    				}
		    			});
		    			$('#tt').datagrid('selectRow',index);
		    			$('#tt').datagrid('beginEdit',index);
		                }
		            }, {
		                text : "保存",
		                iconCls : "icon-save",
		                handler : function() {
		                    endEdit();
		                    if ($tt.datagrid('getChanges').length) {
		                        var inserted = $tt.datagrid('getChanges', "inserted");
		                        var deleted = $tt.datagrid('getChanges', "deleted");
		                        var updated = $tt.datagrid('getChanges', "updated");
		                        var effectRow = new Object();
		                        effectRow["purchaseId"] =row.purchaseId;
		                        if (inserted.length) {
		                            effectRow["inserted"] = JSON.stringify(inserted);
		                        }
		                        if (deleted.length) {
		                            effectRow["deleted"] = JSON.stringify(deleted);
		                        }
		                        if (updated.length) {
		                            effectRow["updated"] = JSON.stringify(updated);
		                        }
		  
		                        $.post("<c:url value="/purchase/savePurDetail.tg"/>", effectRow, function(data) {
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
			}
			});
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
		
		var status;
		var editables;
		$.getJSON("<c:url value='/permissions/actions/outDicJsonByNicknameActions.tg?nickName=status'/>", function(json){
			status=json;
		});
		$.getJSON("<c:url value='/permissions/actions/outDicJsonByNicknameActions.tg?nickName=icon'/>", function(json){
			editables=json;
		});
		function statusFormatter(value){
			for(var i=0; i<status.length; i++){
				if (status[i].value == value) return status[i].name;
			}
			return value;
		}
		function editableFormatter(value){
			for(var i=0; i<editables.length; i++){
				if (editables[i].value == value) return editables[i].name;
			}
			return value;
		}
	
		var actionUrl;
		function newItem(){
			$('#dg').datagrid('rejectChanges');
			$('#dlg').dialog('setTitle', '填写入库信息').dialog('open');
			$('#myform').form('clear');
			$.each($('#myform input'),function(i){
				$(this).removeAttr("readonly");
			});
			/* $("#dlg-buttons a::first-child").show(); */
			actionUrl = '<c:url value="/purchase/savePurchase.tg"/>';
		}
		
		function closewindow()
		{
			$('#dg').datagrid('rejectChanges');
			$('#dlg').dialog('close');
			$('#t-dictionarys').datagrid('reload');
		}
		function editItem(){
			var row = $('#t-dictionarys').datagrid('getSelected');
			if (row){
				$.each($('#myform input'),function(i){
					$(this).removeAttr("readonly");
				});
				$("#dlg-buttons a::first-child").show();
				$('#dicId').val(row.id);
				actionUrl = '<c:url value="/purchase/findPurchase.tg"/>';
				$('#myform').formid('loadit',row);
				$('#dlg').dialog('setTitle', '修改常量信息').dialog('open');
				actionUrl = '<c:url value="/purchase/updatePurchase.tg"/>';
			}else{
				alert('请选则一常量修改');
			}
		}
		
		function delItem(){
			var row = $('#t-dictionarys').datagrid('getSelected');
			if(row){
				$.messager.confirm('提示','确定要删除？',function(r){
					if(r){
						$('#dicId').val(row.id);
						actionUrl = '<c:url value="/purchase/delPurchase.tg"/>';
						$('#dicform').form('submit', {
							url:actionUrl,
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
				})
			}else{
				$.messager.alert('提示','请选择要删除的常量！','error');
			}
		}
		
		function advanceQuery(){
			showQueryDialog({
				width:350,
				height:230,
				form:'<c:url value="/views/erp/purchase/query.jsp"/>',
				callback:function(data){
					$('#t-dictionarys').datagrid('load',data);
				}
			});
		}
		
		//入库
		$(function() {
        var $dg = $("#dg");
        $dg.datagrid({
            width : 620,
            height : 300,
            idField:'productId',
            columns : [ [ {
                field : 'productId',
                title : '商品',
                width : 300,
                formatter:function(value){
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
            }, {
                field : 'quantity',
                title : '数量',
                width : 50,
                editor : "validatebox"
            }, {
                field : 'price',
                title : '单价',
                width : 50,
                align : 'right',
                editor : "numberbox"
            } ] ],
            toolbar : [ {
                text : "添加",
                iconCls : "icon-add",
                handler : function() {
                    $dg.datagrid('appendRow', {});
                    var rows = $dg.datagrid('getRows');
                    $dg.datagrid('beginEdit', rows.length - 1);
                }
            }, {
                text : "编辑",
                iconCls : "icon-edit",
                handler : function() {
                    var row = $dg.datagrid('getSelected');
                    if (row) {
                        var rowIndex = $dg.datagrid('getRowIndex', row);
                        $dg.datagrid('beginEdit', rowIndex);
                    }
                }
            }, {
                text : "删除",
                iconCls : "icon-remove",
                handler : function() {
                    var row = $dg.datagrid('getSelected');
                    if (row) {
                        var rowIndex = $dg.datagrid('getRowIndex', row);
                        $dg.datagrid('deleteRow', rowIndex);
                    }
                }
            }, {
                text : "结束编辑",
                iconCls : "icon-cancel",
                handler :endEdit
            }]
        });
    });
		 function endEdit(){
			    var $dg = $("#dg");
	            var rows = $dg.datagrid('getRows');
	            for ( var i = 0; i < rows.length; i++) {
	                $dg.datagrid('endEdit', i);
	            }
	        }
	
		 function saveItem(){
			   var $dg = $("#dg");
				$('#myform').form('submit', {
					url:actionUrl,
					onSubmit: function(){
						endEdit();
	                    if ($dg.datagrid('getChanges').length) {
	                        var inserted = $dg.datagrid('getChanges', "inserted");
	                        var deleted = $dg.datagrid('getChanges', "deleted");
	                        var updated = $dg.datagrid('getChanges', "updated");
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
		 //订单导入
		 function impfromorder(){
			 $('#dd').dialog('clear');
			 $('#dd').append("<iframe id='orderIn' style='width:99%;height:99%' src='<c:url value="/orderIn/indexOrderIn.tg"/>?flag=1'></iframe>")
             $('#dd').dialog({
                 width: 1000,
                 height: 600,
                 modal: true,
         		 buttons:[{
         				text:'保存',
         				iconCls:"icon-save",
         				handler:function(){
         					var actionUrl='<c:url value="/purchase/impOrderPurchase.tg"/>'
         					var getorder=window.frames["orderIn"].window.getOrder(); 
         					console.log(getorder);
         					if(getorder){
         					$('#orderId').val(getorder.orderId);
         					$('#impform').form('submit', {
         						url:actionUrl,
         						onSubmit: function(){
         								return $('#impform').form('validate');
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
         					 $('#dd').dialog('close');
         					 $('#t-dictionarys').datagrid('reload');
         				}
         					else{
         						$.messager.alert('警告','请选择订单再进行导入');   
         						}
         					}
         			},{
         				text:'关闭',
         				iconCls:"icon-cancel",
         				handler:function(){
         					 $('#dd').dialog('close');
         				}
         			}]
             });
		 }
	</script>