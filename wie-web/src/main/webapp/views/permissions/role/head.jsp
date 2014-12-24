 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
	 });
 </script>
 <script type="text/javascript">
	 //----------------------格式化显示状态名称
	 var status;
	 $.getJSON("<c:url value='/permissions/resources/outDicJsonByNicknameResources.tg?nickName=status'></c:url>", function(json){
		 status=json;
	 });
	 function statusFormatter(value){
		 for(var i=0; i<status.length; i++){
			 if (status[i].value == value) return status[i].name;
		 }
		 return value;
	 }
	 //------------------------------------
	 $(function(){
		 init();
		 $('#dt-role').datagrid({onDblClickRow:function dblClickRow(rowIndex,rowData){
			 //alert(rowIndex+"-"+JSON.stringify(rowData));
			 //$('#detailform').form('load',rowData);
			 //$('#detail').dialog('setTitle','更新角色信息').dialog('open');
			 $('#myform').formid('loadit',rowData);
			 $.each($('#myform input'),function(i){
				 $(this).attr("readonly","true");
			 });
			 /* $("#dlg-buttons a:first-child").hide(); */
			 $('#dlg').dialog('setTitle','查看角色信息').dialog('open');
		 }});
	 });
	 function init(){
		 $('#dlg').dialog({
			 onOpen:function(){
				 $('#dt-role').datagrid('resize');
			 }
		 });

	 }
	 function advanceQuery(){
		 showQueryDialog({
			 width:350,
			 height:300,
			 form:'<c:url value="/views/permissions/role/_query.jsp"/>',
			 callback:function(data){
				 $('#dt-role').datagrid('loadData', {total:0,rows:[]});
				 $('#dt-role').datagrid('load',data);
			 }
		 });
	 }
	 function back(){
		 $('#dt-role').datagrid('loadData', {total:0,rows:[]});
		 $('#dt-role').datagrid('load',{});
	 }

	 var url;
	 function initTree(row){
		 $('#tt').tree({
			 checkbox:true,
			 url: '<c:url value="/permissions/role/getAllTreesRole.tg"/>?id='+row["id"],
			 onClick:function(node){
				 $(this).tree('toggle', node.target);
				 //alert('you dbclick '+node.text);
			 },
			 onContextMenu: function(e, node){
				 e.preventDefault();
				 $('#tt').tree('select', node.target);
				 $('#mm').menu('show', {
					 left: e.pageX,
					 top: e.pageY
				 });
			 },
			 toolbar:[{
				 text:'保存',
				 iconCls:'icon-add',
				 handler:function(){
					 saveAuthorizate();
				 }
			 },'-',{
				 text:'关闭',
				 iconCls:'icon-cancel',
				 handler:function(){
					 $('#auth').dialog('close');
				 }
			 }]
		 });
	 }
	 function checkbox(row,col){
		 if(!row.children){
			 if(row[col.dataField].indexOf("_")<0){
				 //alert(row[col.dataField].indexOf("_"));
				 return '<input type="checkbox" name="'+row[col.dataField]+'" />';
			 }else{
				 return '<input type="checkbox" name="'+row[col.dataField].substr(1)+'" checked=true/>';
			 }
		 }
		 return '';
	 }
	 function customResName(row, col){
		 var name = row[col.dataField] || "";
		 if(!row.children){
			 return name+'<input type="checkbox" >';
		 }
		 return row[col.dataField]+'';

	 }
	 function initTreeGrid(row){
		 url = '<c:url value="/permissions/role/getActionsRole.tg"/>?id='+row["id"];
		 $('#actions').form('submit',{
			 url:url,
			 onSubmit:function(){return true;},
			 success:function(data){
				 var col=eval('('+data+')');
				 var config = {
					 id: "tgActions",
					 width: "900",
					 renderTo: "resActions",
					 headerAlign: "left",
					 headerHeight: "30",
					 dataAlign: "left",
					 indentation: "20",
					 hoverRowBackground: "false",
					 folderColumnIndex: "0",
					 //folderOpenIcon:"../../../images/tree_folder_open.gif",
					 //folderCloseIcon:"../../../images/tree_folder.gif",
					 //defaultLeafIcon:"../../../images/tree_file.gif",
					 folderOpenIcon:"../../images/tree_folder_open.gif",
					 folderCloseIcon:"../../images/tree_folder.gif",
					 defaultLeafIcon:"../../images/tree_file.gif",
					 columns:[],
					 data:[]
				 };

				 config.columns=col;
				 $.ajax({url: '<c:url value="/permissions/role/getRAMappingsRole.tg"/>?id='+row["id"],
					 dataType : "json",
					 type: "post",
					 timeout: 5000000,
					 success: function(data){
						 config.data=data;
						 //创建一个组件对象
						 var treeGrid = new TreeGrid(config);
						 treeGrid.show();
						 //查找id=tgActions的表格，下面除了第一个tr，下面的第一个td，所有的复习框的点击事件。
						 $("#tgActions tr:not(:first) > td:nth-child(1)").find("input[type='checkbox']").bind("click", function () {
							 if (!!this.checked ) {
								 $(this).parent().nextAll().find("input[type='checkbox']").attr("checked", true);
							 }
							 else {
								 $(this).parent().nextAll().find("input[type='checkbox']").attr("checked", false);
							 }
						 });
						 //查找id=tgActions的表格，下面第一个tr，下面除了第一个td下，所有的复习框的点击事件。
						 $("#tgActions tr:first > td:not(:first)").find("input[type='checkbox']").bind("click", function () {
							 var index = $(this).parent().index()+1;
							 if (!!this.checked ) {
								 $("#tgActions tr:not(:first) > td:nth-child("+index+")").find("input[type='checkbox']").attr("checked", true);
							 }
							 else {
								 $("#tgActions tr:not(:first) > td:nth-child("+index+")").find("input[type='checkbox']").attr("checked", false);
							 }

						 });
						 //查找id=tgActions的表格，下面第一个tr，下面第一个td下，的复习框的点击事件。
						 $("#tgActions tr:first>td:first").find("input[type='checkbox']").bind("click", function () {
							 if (!!this.checked ) {
								 $("#tgActions tr>td").find("input[type='checkbox']").attr("checked", true);
							 }
							 else {
								 $("#tgActions tr>td").find("input[type='checkbox']").attr("checked", false);
							 }
						 });
					 },
					 error: function() { alert("error"); }
				 });

			 }
		 });

	 }
	 function resActions(){
		 var row = $('#dt-role').datagrid('getSelected');
		 if (row){
			 $("#resActions").html("");
			 initTreeGrid(row);
			 url = '<c:url value="/permissions/role/saveResActionsRole.tg"/>?id='+row["id"];
			 row = JSON.stringify(row).replace(/\./g,"\\\\.");
			 $('#ac').dialog('setTitle','资源分配操作').dialog('open');
		 } else {
			 $.messager.show({
				 title:'注意',
				 msg:'请先选择角色，再进行修改。'
			 });
		 }
	 }
	 //=======================处理授权操作==================================================================
	 //使用自己生成的资源操作列表给角色分配操作
	 function resAct(){
		 var row = $('#dt-role').datagrid('getSelected');
		 if (row){
			 $("#dt-resAct").datagrid({
				 url:'<c:url value="/permissions/role/getResActMappingsRole.tg"/>?id='+row["id"]
			 });
			 url = '<c:url value="/permissions/role/saveResActionsRole.tg"/>?id='+row["id"];
			 $('#resAct').dialog('setTitle','资源分配操作').dialog('open');
		 } else {
			 $.messager.show({
				 title:'注意',
				 msg:'请先选择角色，再进行修改。'
			 });
		 }
	 }
	 //当点击第二列的checkbox时，将当前行的所有checkbox全选或全不选
	 function selectCurrentRow(select,id){
		 //查找id=resActForm下的所有checkbox，并选中或取消选中
		 $("#resActForm").find("input[type='checkbox']").each(function(index){
			 if(this.name.indexOf(id+":")==0){
				 this.checked = select;
			 }
		 });
	 }
	 //系统列的formatter
	 function systemFormatter(value,rowData,rowIndex){
		 return value.name;
	 }
	 //资源列的formatter
	 function resourceFormatter(value,rowData,rowIndex){
		 return '<input type="checkbox" onchange="selectCurrentRow(this.checked,this.id);" id="'+value.id+'" name="'+value.ename+'"/>'+value.name;
	 }
	 //操作列的formatter
	 function actionsFormatter(value,rowData,rowIndex){
// 			alert(JSON.stringify(value));
		 var ownActions = value.ownActions;
		 var allActions = value.allActions;
		 if(!!allActions && allActions.length>0){
			 var html = "";
			 for(var i=0;i<allActions.length;i++){
				 html+='<input type="checkbox" ';
				 if(!!ownActions && ownActions.indexOf(allActions[i].ename)!=-1){
					 html+=' checked="checked" ';
				 }
				 //在每个操作的name属性的格式为：资源id:操作ename
				 html+=' name="'+rowData.res.id+':'+allActions[i].ename+'"/>'+allActions[i].name;
			 }
			 return html;
		 }else{
			 return '<div style="color:red">该资源暂无操作</div>';
		 }
	 }
	 //保存授权操作
	 function saveResAct(){
		 var actions="";
		 //存储所有选择的操作
		 var allSelections = new Array();
		 //存储所有的资源
		 var allResources = new Array();

		 $("#resActForm").find("input[type='checkbox']").each(function(index){
			 //如果name属性里面没有冒号的话，表示是资源，则存入资源数组
			 if(this.name.indexOf(":")==-1 ){
				 allResources[allResources.length] = this.id;
			 }else if(this.checked){
				 //不是资源数组并且被选中了，就存入操作数组
				 allSelections[allSelections.length] = this.name;
			 }
		 });
		 //循环遍历资源数组，从操作数组中找出对应的操作依次加入actions变量中
		 for(var i=0;i<allResources.length;i++){
			 var flag=false;
			 for(var j=0;j<allSelections.length;j++){
				 //如果操作中包含资源ID，表示当前操作输入该资源，就顺序加入actions中
				 if(allSelections[j].indexOf(allResources[i]+":")==0){
					 flag=true;
					 actions+=allSelections[j]+",";
				 }
			 }
			 if(flag){
				 actions=actions.substring(0,actions.length-1)+";";
			 }
		 }
		 $("#saveResActInput").val(actions);
		 $('#saveResActForm').form('submit',{
			 url:url,
			 onSubmit:function(){return true;},
			 success:function(json){
				 json=eval('('+json+')');
				 $('#resAct').dialog('close');
				 if(json.success){
					 $.messager.show(
							 {
								 title:'提示',
								 msg:'操作成功！',
								 showType:'slide'
							 }
					 );
				 }
				 if(json.error){
					 $.messager.alert('警告','操作失败！','error');
				 }
			 }
		 });
// 			$.getJSON(url, { roleActions: actions }, function(json){
// 				$('#resAct').dialog('close');
// 				if(json.success){
// 					$.messager.show(
// 						{
// 							title:'提示',
// 							msg:'操作成功！',
// 							showType:'slide'
// 						}
// 					);
// 				}
// 				if(json.error){
// 					$.messager.alert('警告','操作失败！','error');
// 				}
// 			});
// 			var data = $('#resActForm').form('submit',{
// 				url:url,
// 				onSubmit:function(){return true;},
// 				success:function(data){
// 					$('#resAct').dialog('close');
// 					data=eval('('+data+')');
// 					if(data.success){
// 						$.messager.show(
// 							{
// 								title:'提示',
// 								msg:'操作成功！',
// 								showType:'slide'
// 							}
// 						);
// 					}
// 					if(data.error){
// 						$.messager.alert('警告','操作失败！','error');
// 					}
// 				}
// 			});

	 }

	 //=========================================================================================

	 //原来用treegrid的时候的保存操作的方法
	 function saveResActions(){
// 			alert(JSON.stringify($("#dt-resAct").datagrid("getRows")));
// 			alert($("#resActForm").html());
// 			return;
		 var actions="";
		 for(var i=1;i<$("#tgActions tr").length;i++){
			 var flag=false;
			 $("#tgActions tr:nth-child("+i+") > td:not(:first)").find("input[type='checkbox']").each(function (i) {
				 if (!!this.checked ) {
					 if(!!this.name){
						 flag=true;
						 actions+=this.name+",";
					 }
				 }
			 });
			 if(flag){
				 actions+=";";
			 }
		 }
		 url=url+"&roleActions="+actions;
		 var data = $('#authorization').form('submit',{
			 url:url,
			 onSubmit:function(){return true;},
			 success:function(data){
				 $('#ac').dialog('close');
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
					 $.messager.alert('警告','操作失败！','error');
				 }
			 }
		 });

	 }
	 function authorizate(){
		 var row = $('#dt-role').datagrid('getSelected');
		 if (row){
			 initTree(row);
			 url = '<c:url value="/permissions/role/saveAuthorizateRole.tg"/>?id='+row["id"];
			 row = JSON.stringify(row).replace(/\./g,"\\\\.");
			 $('#auth').dialog('setTitle','角色分配资源').dialog('open');
		 } else {
			 $.messager.show({
				 title:'注意',
				 msg:'请先选择角色，再进行修改。'
			 });
		 }
	 }
	 function saveAuthorizate(){
		 var nodes = $('#tt').tree('getChecked');
		 var s = '';
		 for(var i=0; i<nodes.length; i++){
			 if (s != '') s += ',';
			 s += nodes[i].id;
		 }
		 url = url+'&rids='+s;
		 var data = $('#authorization').form('submit',{
			 url:url,
			 onSubmit:function(){return true;},
			 success:function(data){
				 $('#auth').dialog('close');
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
					 $.messager.alert('警告','操作失败！','error');
				 }
			 }
		 });

	 }
	 function newItem(){
		 url = '<c:url value="/permissions/role/saveRole.tg"/>';
		 $('#myform').form('clear');
		 $.each($('#myform input'),function(i){
			 $(this).removeAttr("readonly");
		 });
		 $("#dlg-buttons a:first-child").show();
		 $('#dlg').dialog('setTitle','增加角色').dialog('open');
	 }
	 function editItem(){
		 var row = $('#dt-role').datagrid('getSelected');
		 if (row){
			 url = '<c:url value="/permissions/role/updateRole.tg"/>?';
			 //row = JSON.stringify(row).replace(/\./g,"\\\\.");
			 //$('#myform').form('load',eval('('+row+')'));
			 $('#myform').formid('loadit',row);
			 $.each($('#myform input'),function(i){
				 $(this).removeAttr("readonly");
			 });
			 $("#dlg-buttons a:first-child").show();
			 $('#dlg').dialog('setTitle','更新角色信息').dialog('open');
		 } else {
			 $.messager.show({
				 title:'注意',
				 msg:'请先选择数据，再进行修改。'
			 });
		 }
	 }
	 function removeItem(){
		 var row = $('#dt-role').datagrid('getSelected');
		 if (row){
			 $.messager.confirm('提示','确定要删除？',function(r){
				 if(r){
					 url = '<c:url value="/permissions/role/deleteRole.tg"/>?id='+row["id"];
					 $('#myform').form('submit',{
						 url:url,
						 onSubmit:function(){return true;},
						 success:function(data){
							 $('#dt-role').datagrid('reload');
							 data=eval('('+data+')');
							 if(data.success){
								 $.messager.show(
										 {
											 title:'提示',
											 msg:'删除操作成功！',
											 showType:'slide'
										 }
								 );
							 }
							 if(data.error){
								 $.messager.alert('警告','删除操作失败！','error');
							 }
						 }
					 });
				 }
			 });
		 } else {
			 $.messager.show({
				 title:'注意',
				 msg:'请先选择角色信息，再进行删除。'
			 });
		 }
	 }
	 function saveItem(){
		 var data = $('#myform').form('submit',{
			 url:url,
			 onSubmit:function(){return $(this).form('validate');},
			 success:function(data){
				 $('#dlg').dialog('close');
				 $('#dt-role').datagrid('reload');
				 data=eval('('+data+')');
				 if(data.success){
					 $.messager.show(
							 {
								 title:'提示',
								 msg:'保存操作成功！',
								 showType:'slide'
							 }
					 );
				 }
				 if(data.error){
					 $.messager.alert('警告','操作失败！','error');
				 }
			 }
		 });

	 }
	 /*授权操作树形形式
	  * @author ldh
	  * @date 2014-12-18
	  * */
	 function getChecked(check){
		 var nodes = $('#asActsTree').tree('getChecked',check);
		 var arr = new Array();
		 var arrid = new Array();
		 $.each(nodes,function(i){
			 if(nodes[i].resename!=null) {
				 if(arrid.indexOf(nodes[i].authid)==-1)
					 arrid.push(nodes[i].authid);
				 var acts=nodes[i].resename+':'+nodes[i].ename;
				 if(check=='unchecked')acts='';
				 arr.push({
					 id:nodes[i].authid,
					 actions:acts,
					 roleId:nodes[i].roleId,
					 resourceId:nodes[i].resourceId
				 });
			 }
		 });
		 return groupby(arrid,arr);
	 }
	 function groupby(arrid,arr)
	 {
		 var arr1 = new Array();
		 $.each(arrid,function(i){
			 var id=arrid[i],actions='',roleId,resourceId;
			 $.each(arr,function(j){
				 if(arrid[i]==arr[j].id){
					 if(actions!='')actions+=',';
					 actions+=arr[j].actions;
					 roleId=arr[j].roleId;
					 resourceId=arr[j].resourceId;
				 }
			 });
			 arr1.push({
				 id:id,
				 actions:actions,
				 roleId:roleId,
				 resourceId:resourceId
			 });
		 });
		 return arr1;
	 }
	 function actionSubmit(){
		 var array= $.merge(getChecked('unchecked'),getChecked('checked'));
		 var arrid=new Array();
		 $.each(array,function(i){
			 if(arrid.indexOf(array[i].id)==-1)
				 arrid.push(array[i].id);
		 });
		 var newArr=groupby(arrid,array);
		 var jsonarray=JSON.stringify(newArr);
		 $.post('/permissions/role/saveResActRole.tg',{arr:jsonarray},function(data){
			 showMsg(data);
		 });
	 }
	 function showMsg(data){
		 data=eval('('+data+')');
		 if(data.success){
			 $('#dlg-tree').dialog('close');
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
	 function resActTree(){
		 var role=$('#dt-role').datagrid('getSelected');
		 if(!role){
			 $.messager.alert('提示','请选择角色')
			 return;
		 }
		 var id=role.id;
		 var dlg=$('#dlg-tree');
		 dlg.dialog('clear');

		 dlg.append('<ul id="asActsTree"></ul>');
		 var actTree=$('#asActsTree');
		 actTree.tree({
			 url:'/permissions/role/getResActTreeRole.tg',
			 queryParams:{id:id},
			 lines:true,
			 animate:true,
			 checkbox:true
		 });
		 dlg.dialog({
			 title: '分配操作',
			 width: 300,
			 height: 400,
			 closed: false,
			 cache: false,
			 modal: true,
			 buttons:'#dlg-tree-buttons'
		 });
	 }
 </script>