<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui" %>
    
<head>
    <link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/main.css"/>">
	<style>
		.icon-filter{
			background:url('filter.png') no-repeat center center;
		}
		span#sp{color:red}
	</style>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.patch.js"/>"></script>
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
	 <script type="text/javascript">
        	var contextPath = '<%=request.getContextPath()%>';
        	var isaudit='purchase';
        	var checkUrl =contextPath+'/purchase/checkPurchase.tg';
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
	<script type="text/javascript" src="<c:url value='/js/commons/jquery.combointercourse.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/commons/jquery.billgrid.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/commons/jquery.detailgrid.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/commons/commons.js'/>"></script>
	<script type="text/javascript">
		$(function(){
			$('#dg-bills').billgrid({
				createUrl:'<c:url value="/purchase/createPurchase.tg"/>',
				editUrl:'<c:url value="/purchase/editPurchase.tg"/>',
				destroyUrl:'<c:url value="/purchase/destroyPurchase.tg"/>',
				query:{
					title:'查询采购入库单',
					form:'<c:url value="/views/erp/purchase/_query.jsp"/>',
					width:450,
					height:230,
					callback:function(){$('#q').searchbox('setValue','');}
				}
			});
		});
		function normalQuery(value){
			$('#dg-bills').billgrid('load',{
				code: value
			});
		}
		function doSearch(){  
		    $('#dg-bills').datagrid('load',{
				storeId:$('#sid').combobox('getValue'),
		    	code: $('#code').val(),  
		    	ordercode: $('#ordercode').val()
		    });  
		}
		function reset()
		{
			$('#code').searchbox('setValue', null);
			$('#sid').searchbox('setValue', null);
			$('#ordercode').searchbox('setValue', null);
		}
		function formatDecimal(value){
			var val = parseFloat(value);
			return isNaN(val) ? value : val.toFixed(2);
		}
		function formatStatus(value,row){
			if (value == 0){
				return '编制';
			} else if (value == 1){
				return '<span id="sp">审核</span>';
			} else {
				return value;
			}
		}
		function formatType(value,row){
			if (row.inby){
				var data = jQuery.parseJSON(row.inby); 
				//console.log(data);
				return data.name;
			} else {
				return value;
			}
		}
		var store;
		$.getJSON("<c:url value='/store/getAllStore.tg'/>", function (json){
		       store=json;
		});
		function storeFormatter(value){
		        for(var i=0; i<store.length; i++){
		               if (store[i].storeId == value) return store[i].storeName;
		       }
		        return value;
		}
		
		function getOrder() {
			var v = $('#dg-bills').datagrid('getSelected');
			return v;
		}
		$(function(){
			flag=${flag};
			if(flag==1)
			 $('#flag').hide(); 
		});
		
		//订单导入
		 function impfromorder(){
			 $('#dd').dialog('clear');
			 $('#dd').append("<iframe id='orderIn' style='width:99%;height:99%' src='<c:url value="/orderIn/indexOrderIn.tg"/>?flag=1'></iframe>")
            $('#dd').dialog({
                width: 950,
                height: 450,
                modal: true,
        		 buttons:[{
        				text:'确定导入',
        				iconCls:"icon-save",
        				handler:function(){
        					var actionUrl='<c:url value="/purchase/impOrderPurchase.tg"/>'
        					 var getorder=$("#orderIn")[0].contentWindow.getOrder(); 
        					/* var test=$("#orderIn")[0].contentWindow.getOrder();  */
        					//console.log(getorder);
        					 if(getorder){
        					$('#orderId').val(getorder.orderId);
        					$('#impform').form('submit', {
        						url:actionUrl,
        						onSubmit: function(){
        								return $('#impform').form('validate');
        						},
        						success: function(data){
        							$('#dd').dialog('close');
        							$('#dg-bills').datagrid('reload');
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
		
		 function openform(para){
				$.post('<c:url value="/isexistLogin.tg"/>',function(data){
					//console.log(data);
					var d=eval('('+data+')');
					if(d.status){
					    $('#dg-bills').billgrid(para);
					   
					}
					else{
						$.messager.confirm('警告','登录超时，请重新登录！',function(r){
							if(r)
							 location.href='/loginLogin.tg';
						});   
						
					}
				});
				
			}
	</script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div region="north" border="false">
			<div class="toolbar" >
				<table cellpadding="0" cellspacing="0" style="width:100%">
					<tr>
						<td id="flag">
							<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="openform('create')">增加</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-open" plain="true" onclick="openform('edit')">打开</a>
							<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="openform('destroy')">删除</a>
							<tgEasyui:easyuiButton iconCls="icon-mini-add" method="impfromorder()" permission="purchase:modify" operationName="导入"/>
						</td>
						<td style="text-align:right">
							<input id="sid" name="purchase.storeId" class="easyui-combobox" prompt="商店名称" style="width:180px"
								   data-options="valueField:'storeId',textField:'storeName',url:'/store/users_storesStore.tg?id=${sessionScope.userId}'" />
							<%--<input  class="easyui-textbox" style="width:150px" prompt="商店名称" id="sid"  name="purchase.storeId"/>--%>
						    <input  class="easyui-textbox" style="width:150px" prompt="入库编号" id="code"  name="purchase.code"/>
                            <input class="easyui-textbox"  style="width:150px" prompt="采购计划编号" id="ordercode" name="purchase.order.code"/>
                            <a class="easyui-linkbutton" href="javascript:void(0);" onclick="doSearch();">查找</a>
                            <a class="easyui-linkbutton" href="javascript:void(0);" onclick="reset();">清空</a>
						</td>
						 <td>
						 &nbsp;&nbsp;&nbsp;	
                         </td>
					</tr>
				</table>
			</div>
		</div>
		<div region="center" border="false">
			<table id="dg-bills" 
					url="<c:url value='/purchase/getItemsPurchase.tg'/>"
					idField="id" fit="true" fitColumns="true" singleSelect="true"
					pagination="true" border="false" rownumbers="true">
				<thead>
					<tr>
						<th field="code" sortable="true" width="80">单据编号</th>
						<th field="indt" width="60" sortable="true">单据日期</th>
						<th field="intercourseName" width="80">往来单位</th>
						<th field="storeId" width="60" formatter="storeFormatter">库房</th>
						<th field="inby" width="50" formatter="formatType">制单人</th>
						<th field="remark" width="100">备注</th>
						<th field="order.orderNumber"  width="60">订单编号</th>
						<th field="totalCount" width="30" align="right">数量</th>
						<th field="totalPrice" width="40" align="right" formatter="formatDecimal">金额</th>
						<th field="status" width="40" align="center" sortable="true" formatter="formatStatus">状态</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	
	<div id="dlg-bill" class="easyui-dialog" title="入库单" style="width:65%;height:60%;position:relative;margin-top: 10%"
			closed="true">
	</div>
	<div id="dd"  title="订单导入">
	</div>
	<form id="impform" method="post" style="margin:0;padding:0">
	<input id="orderId" type="hidden"  name="orderId"/>
	</form>
</body>
