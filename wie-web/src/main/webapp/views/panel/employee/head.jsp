<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<title>权限管理系统</title>
<jsp:include page="/views/include1.jsp"/>
<script type="text/javascript" src="<c:url value="/views/panel/commons.js"/>"></script>
<script type="text/javascript">
	$(function(){
		$('body').css('visibility','visible');
		setTimeout(function(){
			$('#loading-mask').remove();
		},50);
		sexformatter();
		showdatagird();
	});

	function showdatagird(){
		$('#t-employee').datagrid({
					width:function(){return document.body.clientWidth*0.5},
					height:function(){return document.body.clientHeight*0.5},
					singleSelect:true,
					border:false,
					rownumbers:true,
					pagination:true,
					fit:true,
					idField:'itemid',
					url:'<c:url value="/employee/getItemsEmployee.tg"/>',
					columns:[[
						{field:'userName',title:'登录名',width:100},
						{field:'employeeName',title:'实名',width:80},
						{field:'age',title:'年龄',width:50},
						{field:'sex',title:'性别',width:50,
							formatter:function(value){
								if(value=='F') return '男';
								if(value=='M') return '女';
							}
						},
						{field:'address',title:'地址',width:100},
						{field:'phone',title:'电话',width:100},
						{field:'email',title:'邮箱',width:150}
					]]
				}
		);
	}
	//---------------------------------------------------------------------

	var actionUrl;
	function newItem(){
		$('#myform').form('clear');
		$('#dlg').dialog('setTitle', '填写用户资料').dialog('open');
		actionUrl = '<c:url value="/employee/saveEmployee.tg"/>';
	}
	function saveItem(){
		$('#myform').form('submit', {
			url:actionUrl,
			onSubmit: function(){
				return $('#myform').form('validate');
			},
			success: function(data){
				$('#dlg').dialog('close');
				$('#t-employee').datagrid('reload');
				showMsg(data);
			}
		});
	}

	function editItem(){
		$('#myform').form('clear');
		var row = $('#t-employee').datagrid('getSelected');
		if (row){
			//console.log(row);
			$('#myform').formid('loadit',row);
			$('#dlg').dialog('setTitle', '修改账号资料').dialog('open');
			actionUrl = '<c:url value="/employee/updateEmployee.tg"/>';
		}else{
			alert('请选择商品修改');
		}
	}
	function delItem(){
		var row = $('#t-employee').datagrid('getSelected');
		if(row){
			$.messager.confirm('提示','确定要删除？',function(r){
				if(r){
					$('#uid').val(row.userId);
					actionUrl = '<c:url value="/employee/delEmployee.tg"/>';
					$('#userform').form('submit', {
						url:actionUrl,
						success: function(data){
							$('#dlg').dialog('close');
							$('#t-employee').datagrid('reload');
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
		$('#t-employee').datagrid('load',{
			userName: $('#userName_q').val(),
			employeeName: $('#employeeName_q').val()
		});
	}
	function reset()
	{
		$('#userName_q').searchbox('setValue', null);
		$('#employeeName_q').searchbox('setValue', null);
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
	function sexformatter(){
		$('#sex').combobox({
			valueField:'id',
			textField:'text',
			editable:false,
			data:[{
				"id":"F",
				"text":"男"
			},{
				"id":"M",
				"text":"女"
			}]
		});
		yesorno('isSkipAnimation');
		yesorno('isAdmin');
		yesorno('isActive');
	}
	function yesorno(id){
		$('#'+id).combobox({
			valueField:'id',
			textField:'text',
			editable:false,
			data:[{
				"id":"Y",
				"text":"是"
			},{
				"id":"N",
				"text":"否"
			}]
		});
	}
	/*分配角色*/
	function asRole(flag){
		var employee=$('#t-employee').datagrid('getSelected');
		if(!employee) {
			$.messager.alert('提示','请选择用户进行角色分配');
			return;
		}

		var url='<c:url value="/erole/getItemsEmpErole.tg"/>';
		var dlgName='已分配角色';
		$('#saverole').hide();
		if(flag==1){
			url='<c:url value="/erole/getItemsEmpUErole.tg"/>';
			dlgName='角色分配';
			$('#saverole').show();
		}
		var dlg=$('#dlg-asrole');
		var options={
			title: dlgName,
			width: 600,
			height: 400,
			closed: false,
			cache: false,
			modal: true,
			buttons:'#dlg-asrole-buttons'
		};
		dlg.dialog(options);
		dlg.append('<ul id="dg-role"></ul>');

		var dg=$('#dg-role');
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
						id:employee.userId
					},
					columns:[[
						{field:'ck',title:'角色名',width:100,checkbox:true},
						{field:'roleName',title:'角色名',width:100},
						{field:'remark',title:'备注',width:180}
					]],
					onLoadSuccess:function(){
						var rows=$('#dg-role').datagrid('getRows');
						$.each(rows,function(i){
							if(rows[i].ischeck)
								$('#dg-role').datagrid('checkRow',i);
						});
					}
				});

	}


	function saveRoles(){
	    var employee=$('#t-employee').datagrid('getSelected');
		var id=employee.userId;
		var obj=getChanges('#dg-role');
		if(obj){
			var jsonObj=JSON.stringify(obj);
			$.post('/employee/saveRoleToEmployee.tg',{data:jsonObj,id:id},function(data){
				showMsg(data);
				data=eval('('+data+')');
				if(data.success){
					$('#dg-role').datagrid('reload');
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