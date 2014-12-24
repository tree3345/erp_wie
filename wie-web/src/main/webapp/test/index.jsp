<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/html">
<head>
    <title>商品信息</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
    <script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
    <script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
</head>
<body>
<div style="margin:20px 0;">
    <a href="#" class="easyui-linkbutton" onclick="actionSubmit()">GetChecked</a>
</div>
<div class="easyui-panel" style="padding:5px">
    <ul id="tt" class="easyui-tree" data-options="url:'/permissions/role/getResActTreeRole.tg?id=15',lines:true,animate:true,checkbox:true"></ul>
</div>
<script type="text/javascript">
    function getChecked(check){
        var nodes = $('#tt').tree('getChecked',check);
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
        //console.log(arrid);
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
            $('#tt').tree('reload');
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
</body>
</html>