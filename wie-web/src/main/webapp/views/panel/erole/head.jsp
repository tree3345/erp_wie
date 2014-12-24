<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<title>权限管理系统</title>
<jsp:include page="/views/include1.jsp"/>
<script type="text/javascript" src="<c:url value="/js/easyui/extension/jquery-easyui-datagridview/datagrid-groupview.js"/>"></script>
<script type="text/javascript" src="<c:url value="/views/panel/commons.js"/>"></script>

<script type="text/javascript">
	$(function(){
		$('body').css('visibility','visible');
		setTimeout(function(){
			$('#loading-mask').remove();
		},50);
		showdatagird();
	});

	function showdatagird(){
		$('#t-erole').datagrid({
					width:function(){return document.body.clientWidth*0.5},
					height:function(){return document.body.clientHeight*0.5},
					singleSelect:true,
					border:false,
					rownumbers:true,
					pagination:true,
					fit:true,
					idField:'itemid',
					url:'<c:url value="/erole/getItemsErole.tg"/>',
					columns:[[
						{field:'roleName',title:'角色名',width:100},
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
		actionUrl = '<c:url value="/erole/saveErole.tg"/>';
	}
	function saveItem(){
		$('#myform').form('submit', {
			url:actionUrl,
			onSubmit: function(){
				return $('#myform').form('validate');
			},
			success: function(data){
				$('#dlg').dialog('close');
				$('#t-erole').datagrid('reload');
				showMsg(data);
			}
		});
	}

	function editItem(){
		$('#myform').form('clear');
		var row = $('#t-erole').datagrid('getSelected');
		if (row){
			//console.log(row);
			$('#myform').formid('loadit',row);
			$('#dlg').dialog('setTitle', '修改角色信息').dialog('open');
			actionUrl = '<c:url value="/erole/updateErole.tg"/>';
		}else{
			alert('请选择角色修改');
		}
	}
	function delItem(){
		var row = $('#t-erole').datagrid('getSelected');
		if(row){
			$.messager.confirm('提示','确定要删除？',function(r){
				if(r){
					console.log(row.roleId);
					$('#id').val(row.roleId);
					console.log($('#id').val());
					actionUrl = '<c:url value="/erole/delErole.tg"/>';
					$('#userform').form('submit', {
						url:actionUrl,
						success: function(data){
							$('#dlg').dialog('close');
							$('#t-erole').datagrid('reload');
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
		$('#t-erole').datagrid('load',{
			roleName: $('#roleName_q').val()
		});
	}
	function reset()
	{
		$('#roleName_q').searchbox('setValue', null);
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

	/**分配模块*/
	function asmodule(flag){
		var role=$('#t-erole').datagrid('getSelected');
		if(!role) {
			$.messager.alert('提示','请选择角色进行模块分配');
			return;
		}

		var url='<c:url value="/module/getItemsroleModule.tg"/>';
		var dlgName='已分配模块';
		$('#savemodules').hide();
		if(flag==1){
			url='<c:url value="/module/getItemsroleUModule.tg"/>';
			dlgName='模块分配';
			$('#savemodules').show();
		}
		var dlg=$('#dlg-modules');
		var options={
			title: dlgName,
			width: 600,
			height: 400,
			closed: false,
			cache: false,
			modal: true,
			buttons:'#dlg-asmodules-buttons'
		};
		dlg.dialog(options);
		dlg.append('<ul id="dg-modules"></ul>');
		var dg=$('#dg-modules');
		console.log(role.roleId);
		dg.datagrid(
				{
					width:function(){return document.body.clientWidth*0.5},
					height:function(){return document.body.clientHeight*0.5},
					singleSelect:false,
					border:false,
					rownumbers:true,
					fit:true,
					url:url,
					queryParams:{
						roleId:role.roleId
					},
					columns:[[
						{field:'ck',title:'角色名',width:100,checkbox:true},
						{field:'moduleName',title:'模块名称',width:100},
						{field:'viewName',title:'视图名称',width:220},
						{field:'remark',title:'备注',width:180}
					]],
					onLoadSuccess:function(){
						var rows=$('#dg-modules').datagrid('getRows');
						$.each(rows,function(i){
							if(rows[i].ischeck)
								$('#dg-modules').datagrid('checkRow',i);
						});
					}
				});

	}

	function saveModules(){
		var erole=$('#t-erole').datagrid('getSelected');
		var id=erole.roleId;
		var obj=getChanges('#dg-modules');
		//console.log(obj)
		if(obj){
			var jsonObj=JSON.stringify(obj);
			$.post('/erole/saveModulesToErole.tg',{data:jsonObj,id:id},function(data){
				showMsg(data);
				data=eval('('+data+')');
				if(data.success){
					$('#dg-modules').datagrid('reload');
				}
			});
		}else{
			$.messager.show({
				title:'提示',
				msg:'没有进行操作',
				showType:'fade',
				style:{
					right:'',
					bottom:''
				}
			});
		}
	}
	/**分配功能**/
	function asfunction(flag){
		var role=$('#t-erole').datagrid('getSelected');
		if(!role) {
			$.messager.alert('提示','请选择角色进行模块分配');
			return;
		}

		var url='<c:url value="/function/getItemsroleFunction.tg"/>';
		var dlgName='已分配模块';
		var hideflag=true;
		$('#savefunctions').hide();
		if(flag==1){
			url='<c:url value="/function/getItemsroleUFunction.tg"/>';
			dlgName='模块分配';
			hideflag=false;
			$('#savefunctions').show();
		}
		var dlg=$('#dlg-functions');
		var options={
			title: dlgName,
			width: 600,
			height: 400,
			closed: false,
			cache: false,
			modal: true,
			buttons:'#dlg-asfunctions-buttons'
		};
		dlg.dialog(options);
		dlg.append('<ul id="dg-functions"></ul>');
		var dg=$('#dg-functions');
		//console.log(role.roleId);
		dg.datagrid(
				{
					width:function(){return document.body.clientWidth*0.5},
					height:function(){return document.body.clientHeight*0.5},
					singleSelect:hideflag,
					border:false,
					rownumbers:true,
					fit:true,
					url:url,
					queryParams:{
						roleId:role.roleId
					},
					columns:[[
						{field:'ck',title:'',width:100,checkbox:true,hidden:hideflag},
						{field:'functionName',title:'功能名称',width:100},
						{field:'controlName',title:'control名称',width:220},
						{field:'moduleName',title:'所属模块',width:180}
					]],
					groupField:'moduleName',
					view: groupview,
					groupFormatter:function(value, rows){
						var ids='';
						$.each(rows,function(i){
							if(ids!='')ids+=",";
							ids+=rows[i].functionId;
						});
						if(flag==1)
						   return '<input type="checkbox" onclick=checkgroup(this,"'+ids+'") />'+value;
						if(flag==0)
						   return value;
					},
					onLoadSuccess:function(){
						var rows=dg.datagrid('getRows');
						$.each(rows,function(i){
							if(rows[i].ischeck){
								dg.datagrid('checkRow',i);
							}
						});
					}
				});

	}
    function moduleFormatter(value,rows){
		return rows.module.moduleName;
	}
	function checkgroup(target,ids){
		var dg=$('#dg-functions');
		var rows=dg.datagrid('getRows');
		var arr=ids.split(",");
        var check=target.checked?"checkRow":"uncheckRow";

			$.each(rows, function (i) {
				$.each(arr, function (j) {
					if (rows[i].functionId == arr[j]) {
						dg.datagrid(check, i)
					}
				});
			});

//			$.each(rows, function (i) {
//				$.each(arr, function (j) {
//					if (rows[i].functionId == arr[j]) {
//						dg.datagrid('uncheckRow', i)
//					}
//				});
//			});

	}
	function saveFunctions(){
		var erole=$('#t-erole').datagrid('getSelected');
		var id=erole.roleId;
		var obj=getChanges('#dg-functions');
		//console.log(obj)
		if(obj){
			var jsonObj=JSON.stringify(obj);
			$.post('/erole/saveFunctionsToErole.tg',{data:jsonObj,id:id},function(data){
				showMsg(data);
				data=eval('('+data+')');
				if(data.success){
					$('#dg-functions').datagrid('reload');
				}
			});
		}else{
			$.messager.show({
				title:'提示',
				msg:'没有进行操作',
				showType:'fade',
				style:{
					right:'',
					bottom:''
				}
			});
		}
	}
 </script>