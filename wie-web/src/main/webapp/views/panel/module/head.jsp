<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<title>权限管理系统</title>
<jsp:include page="/views/include1.jsp"/>
<script type="text/javascript">
	$(function(){
		$('body').css('visibility','visible');
		setTimeout(function(){
			$('#loading-mask').remove();
		},50);
		showdatagird();
	});

	function showdatagird(){
		$('#t-module').datagrid({
					width:function(){return document.body.clientWidth*0.5},
					height:function(){return document.body.clientHeight*0.5},
					singleSelect:true,
					border:false,
					rownumbers:true,
					pagination:true,
					fit:true,
					url:'<c:url value="/module/getItemsModule.tg"/>',
					columns:[[
						{field:'moduleName',title:'模块名称',width:100},
						{field:'viewName',title:'视图名称',width:220},
						{field:'remark',title:'备注',width:180}
					]]
				}
		);
	}
	//---------------------------------------------------------------------

	var actionUrl;
	function newItem(){
		$('#myform').form('clear');
		$('#dlg').dialog('setTitle', '填写角色信息').dialog('open');
		actionUrl = '<c:url value="/module/saveModule.tg"/>';
	}
	function saveItem(){
		$('#myform').form('submit', {
			url:actionUrl,
			onSubmit: function(){
				return $('#myform').form('validate');
			},
			success: function(data){
				$('#dlg').dialog('close');
				$('#t-module').datagrid('reload');
				showMsg(data);
			}
		});
	}

	function editItem(){
		$('#myform').form('clear');
		var row = $('#t-module').datagrid('getSelected');
		if (row){
			//console.log(row);
			$('#myform').formid('loadit',row);
			$('#dlg').dialog('setTitle', '修改角色信息').dialog('open');
			actionUrl = '<c:url value="/module/updateModule.tg"/>';
		}else{
			alert('请选择角色修改');
		}
	}
	function delItem(){
		var row = $('#t-module').datagrid('getSelected');
		if(row){
			$.messager.confirm('提示','确定要删除？',function(r){
				if(r){
					$('#id').val(row.moduleId);
					actionUrl = '<c:url value="/module/delModule.tg"/>';
					$('#userform').form('submit', {
						url:actionUrl,
						success: function(data){
							$('#dlg').dialog('close');
							$('#t-module').datagrid('reload');
							showMsg(data);
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
		$('#t-module').datagrid('load',{
			moduleName: $('#moduleName_q').val(),
			viewName: $('#viewName_q').val()
		});
	}
	function reset()
	{
		$('#moduleName_q').searchbox('setValue', null);
		$('#viewName_q').searchbox('setValue', null);
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
</script>