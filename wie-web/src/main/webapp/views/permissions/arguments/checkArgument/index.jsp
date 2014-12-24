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
	<script type="text/javascript">
	var stores,auditItems;
	$(function(){
		$.ajaxSettings.async = false;
		$.getJSON('<c:url value="/store/getJSONAllStore.tg"/>',function(json){
			stores=json;
		});
	 	$.getJSON('<c:url value="/permissions/arguments/outDicJsonByNicknameCheckArgument.tg?nickName=auditItem"/>',function(json){
	 		auditItems=json;
		}); 
	});
	function formatterStore(value){
		for(var i=0;i<stores.length;i++){
			if(value==stores[i].storeId)
				return stores[i].storeName;
		}
	}
	function formattervalue(value){
		if(value=='true') return '否';
		if(value=='false') return '是';
	}
	function formatterItem(value){
		console.log(auditItems);
		for(var i=0;i<auditItems.length;i++)
			{
			 if(auditItems[i].value==value) return auditItems[i].name;
			} 
	}
	</script>
	</head>
	<body>
    <table id="dg" class="easyui-datagrid"  style="width:auto;height:auto"
            data-options="
                iconCls: 'icon-edit',
                singleSelect: true,
                rownumbers:true,
                pagination:true,
                fit:true,
                toolbar: '#tb',
                url: '<c:url value="/permissions/arguments/getItemsCheckArgument.tg"/>',
                method: 'get',
                onClickRow: onClickRow
            ">
        <thead>
            <tr>
                <th data-options="field:'name',width:130,formatter:formatterItem,
                 editor:{
                            type:'combobox',
                            options:{
                                valueField:'value',
                                textField:'name',
                                url:'<c:url value="/permissions/arguments/outDicJsonByNicknameCheckArgument.tg?nickName=auditItem"/>',
                                required:true
                            }
                        }">名称</th>
                <th data-options="field:'value',width:70,formatter:formattervalue,
                 editor:{
                            type:'combobox',
                            options:{
                                valueField:'value',
                                textField:'name',
                                url:'<c:url value="/permissions/arguments/outDicJsonByNicknameCheckArgument.tg?nickName=isaudit"/>',
                                required:true
                            }
                        }">对应值</th>
                <th data-options="field:'memo',width:110,editor:'text'">备注</th>
                <th data-options="field:'storeId',width:110, formatter:formatterStore,
                        editor:{
                            type:'combobox',
                            options:{
                                valueField:'storeId',
                                textField:'storeName',
                                url:'<c:url value="/store/getJSONAllStore.tg"/>',
                                required:true
                            }
                        }">适用范围</th>
            </tr>
        </thead>
    </table>
 
    <div id="tb" style="height:auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="append()">增加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">删除</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="saveItem()">提交</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="reject()">撤销</a>
         <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="endEditing()">结束编辑</a>
    </div>
    
    <script type="text/javascript">
        var editIndex = undefined;
        function endEditing(){
            if (editIndex == undefined){return true}
            if ($('#dg').datagrid('validateRow', editIndex)){
                $('#dg').datagrid('endEdit', editIndex);
                editIndex = undefined;
                return true;
            } else {
                return false;
            }
        }
        function onClickRow(index){
            if (editIndex != index){
                if (endEditing()){
                    $('#dg').datagrid('selectRow', index)
                            .datagrid('beginEdit', index);
                    editIndex = index;
                } else {
                    $('#dg').datagrid('selectRow', editIndex);
                }
            }
        }
        function append(){
            if (endEditing()){
                $('#dg').datagrid('appendRow',{status:'P'});
                editIndex = $('#dg').datagrid('getRows').length-1;
                $('#dg').datagrid('selectRow', editIndex)
                        .datagrid('beginEdit', editIndex);
            }
        }
        function removeit(){
            if (editIndex == undefined){return}
            $('#dg').datagrid('cancelEdit', editIndex)
                    .datagrid('deleteRow', editIndex);
            editIndex = undefined;
        }
        function reject(){
            $('#dg').datagrid('rejectChanges');
            editIndex = undefined;
        }
        function saveItem()
		{
			var $dg = $("#dg");
			if ($dg.datagrid('getChanges').length) {
                var inserted = $dg.datagrid('getChanges', "inserted");
                var deleted = $dg.datagrid('getChanges', "deleted");
                var updated = $dg.datagrid('getChanges', "updated");
                var effectRow = new Object();
                if (inserted.length) {
                    effectRow["inserted"] = JSON.stringify(inserted);
                    console.log('inserted:'+JSON.stringify(inserted));
                }
                if (deleted.length) {
                    effectRow["deleted"] = JSON.stringify(deleted);
                    console.log('deleted:'+JSON.stringify(deleted));
                }
                if (updated.length) {
                    effectRow["updated"] = JSON.stringify(updated);
                    console.log('updated:'+JSON.stringify(updated));
                }

                 $.post("<c:url value="/permissions/arguments/saveCheckArgument.tg"/>", effectRow, function(data) {
            	    $('#dg').datagrid('reload');
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
    </script>
</body>
</html>
