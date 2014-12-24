<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<title>权限管理系统</title>
<jsp:include page="/views/include1.jsp"></jsp:include>
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
            	
            	 $('#t-users').datagrid({onDblClickRow:function dblClickRow(rowIndex,rowData){
    				$('#myform').formid('loadit',rowData);
    				$.each($('#myform input'),function(i){
    					$(this).attr("readonly","true");
    				});
    				$("#save").hide();
    				$('#dlg').dialog('setTitle','查看操作信息').dialog('open');
    			}}); 
            	
        	});
        </script>
<style type="text/css">
.e-input {
	width: 198px;
	border: 1px solid #A4BED4;
	height: 18px;
	line-height: 18px;
}
</style>
<script type="text/javascript">
$(function(){
	showdatagird();
});
//----------------------------------------------用来格式化显示状态，是否打开和是否叶子
var education;
$.getJSON("<c:url value='/permissions/actions/outDicJsonByNicknameActions.tg?nickName=education'/>", function(json){
	education=json;
});
var store;
$.getJSON("<c:url value='/store/getAllStore.tg'/>", function(json){
	store=json;
});
var sex;
$.getJSON("<c:url value='/permissions/actions/outDicJsonByNicknameActions.tg?nickName=sex'/>", function(json){
	sex=json;
	/* console.log('---json-');
	console.log(json); */
});

/*  $('#sex').combobox({    
    url:'<c:url value="/permissions/actions/outDicJsonByNicknameActions.tg?nickName=sex"/>',    
    valueField:'value',    
    textField:'name'   
});   */
//-----------------------------datagrid 
function showdatagird(){
			$('#t-users').datagrid({
				width:function(){return document.body.clientWidth*0.5},
				height:function(){return document.body.clientheight*0.5},
				singleSelect:true,
				border:false,
				rownumbers:true,
				pagination:true, 
				fit:true,
				idField:'itemid',
				url:'<c:url value="/user/getPageUser.tg"/>',
				onClickRow:showStores,
				columns:[[
					{field:'name',title:'用户名称',width:100},
					{field:'position',title:'职位',width:50},
					{field:'age',title:'年龄',width:50},
					{field:'sex',title:'性别',width:50,
						formatter:function(value){
							for(var i=0; i<sex.length; i++){
								if (sex[i].value == value) return sex[i].name;
							}
							return value;
					    }
					},
					{field:'education',title:'教育',width:100,
					   formatter:function(value){
						   for(var i=0; i<education.length; i++){
								if (education[i].value == value) return education[i].name;
							}
							return value;
					   }	
					},
					{field:'phone',title:'电话',width:100},
					{field:'email',title:'邮箱',width:150}
				]]
			}
			);
}
               function showStores(rowIndex, rowData){
				   addregion('east',rowData.id);
				   console.log(rowData.id);
			   }
               function addregion(value,id){
				  // console.log($('#t-users').datagrid('options'));
				   var options={
					   id: 'east',
					   region:value,
					   width:'30%',
					   toolbar:'#storeTools'
				   };
				   $('#main').layout('add',options);
				   $('#east').append('<table id="stores"></table>');
				   storeList(id);
			   }
               function storeList(id){
				   var options;
				   options = {
					   width: function () {
						   return document.body.clientWidth * 0.5
					   },
					   height: function () {
						   return document.body.clientHeight * 0.5
					   },
					   singleSelect: false,
					   border: false,
					   rownumbers: true,
					   fit: true,
					   url: '<c:url value="/store/users_storesStore.tg"/>',
					   queryParams:{id:id},
					   columns: [[
						   {field: 'id', title: '', width: 100, checkbox: true},
						   {field: 'storeName', title: '商店名称', width: 100}
					   ]],
					   toolbar:'#storeTools'
				   };

				   $('#stores').datagrid(options);
			   }

               function addstores(){
				   $('#storeall').dialog({
					   title: '商店列表',
					   width: 300,
					   height: 400,
					   closed: false,
					   cache: false,
					   modal: true,
					   buttons:'#tb'
				   });
				   $('#storeall').append('<table id="storeItems"></table>');
				   storeItems();
			   }
				function storeItems() {
					var stores=$('#stores').datagrid('getRows');
					var storeIds="";
					$.each(stores,function(i,n){
						storeIds+= n.storeId+",";
					});
					var options;
					options = {
						width: function () {
							return document.body.clientWidth * 0.5
						},
						height: function () {
							return document.body.clientHeight * 0.5
						},
						singleSelect: false,
						border: false,
						rownumbers: true,
						fit: true,
						url: '<c:url value="/store/getAllStore.tg"/>',
						columns: [[
							{field: 'id', title: '', width: 100, checkbox: true},
							{field: 'storeName', title: '商店名称', width: 100}
						]]
					};
					if(storeIds!=""){
						console.log(123);
						options.queryParams={storeIds:storeIds};
					}
					$('#storeItems').datagrid(options);
				}
                function doStores(type){
					var id,url;
					if(type=='save'){
						id='#storeItems';
						url='/user/savestoresToUser.tg';}
					if(type=='delete'){
						id='#stores';
						url='/user/deletestoresFromUser.tg';}

					var storeItems=$(id).datagrid('getChecked');
					var user=$('#t-users').datagrid('getSelected');
					console.log(storeItems);
					var storeIds="";
					$.each(storeItems,function(i,n){
						console.log(n.storeId);
						storeIds+= n.storeId+",";
					});

					$('#userId').val(user.id);
					$('#storeIds').val(storeIds);

					$('#userform').form({
								url:contextPath+url,
							onSubmit: function(){
						    return true;
					},
					success:function(data){
						if(type=='save')
							$('#storeall').dialog('close');
						$('#stores').datagrid('reload');
						showMsg(data);
					}
				});
               $('#userform').submit();
				}
       function deletestores(){
		  doStores('delete');
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
//---------------------------------------------------------------------

		var actionUrl;
		function newItem(){
			$('#myform').form('clear');
			$.each($('#myform input'),function(i){
				$(this).removeAttr("readonly");
			});
			$("#save").show();
			/* $("#dlg-buttons a::first-child").show(); */
			
			$('#parentId').combotree({
				url:'<c:url value="/group/getTreeGroup.tg"/>',
				valueField:'id',
				textField:'text'
			});
			$('#dlg').dialog('setTitle', '填写用户资料').dialog('open');
			actionUrl = '<c:url value="/user/saveUser.tg"/>';
		}
		function saveItem(){
			$('#myform').form('submit', {
				url:actionUrl,
				onSubmit: function(){
					return $('#myform').form('validate');
				},
				success: function(data){
					$('#dlg').dialog('close');
					$('#t-users').datagrid('reload');
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
		
		function editItem(){
			$('#myform').form('clear');
			var row = $('#t-users').datagrid('getSelected');
			if (row){
				$('#userId').val(row.id);
				actionUrl = '<c:url value="/user/findUser.tg"/>'; 
				$('#myform').formid('loadit',row);
				
				$('#userform').form('submit', {
					url:actionUrl,
					onSubmit: function(){
						return true;
					},
					success: function(data){
						data=eval('('+data+')');
						var tempObj = data.rows[0];
						$('#parentId').combotree({
							url:'<c:url value="/group/getTreeGroup.tg"/>',
							valueField:'id',
							textField:'text'
						});
						$('#parentId').combotree('setText',data.parentName);
						$('#parentId').combotree('setValue',data.parentid);
						
						$('#employeddate').datebox('setValue',tempObj.employeddate);
						$('#birthday').datebox('setValue',tempObj.birthday);
						$('#lastlogondate').datebox('setValue',tempObj.lastlogondate);
						$('#lastlogoffdate').datebox('setValue',tempObj.lastlogoffdate);
					}
				});
				
				$('#dlg').dialog('setTitle', '修改用户资料').dialog('open');
				$.each($('#myform input'),function(i){
					$(this).removeAttr("readonly");
				});
				$("#save").show();
				actionUrl = '<c:url value="/user/updateUser.tg"/>';
			}else{
				alert('请选则用户修改');
			}
		}
		function delItem(){
			var row = $('#t-users').datagrid('getSelected');
			if(row){
				$.messager.confirm('提示','确定要删除？',function(r){
					if(r){
						$('#userId').val(row.id);
						actionUrl = '<c:url value="/user/delUser.tg"/>';
						$('#userform').form('submit', {
							url:actionUrl,
							success: function(data){
								$('#dlg').dialog('close');
								$('#t-users').datagrid('reload');
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
				});
			}else{
				alert('请选择要删除的用户！谢谢'); 
			}
		}
		//过滤
		function doQuery(){
			$('#t-users').datagrid('load',{
				sex: $('#sex').combotree('getValue'),
				q: $('#q').val()
			});
		}
		function reset()
		{
			$('#sex').searchbox('setValue', null);
			$('#q').searchbox('setValue', null);
		}

		function advanceQuery(){
			showQueryDialog({
				width:350,
				height:230,
				form:'<c:url value="/views/permissions/user/query.jsp"/>',
				callback:function(data){
					$('#t-users').datagrid('load',data);
				}
			});
		}
	</script>