<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
  <link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/layout.css"/>">
  <link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
  <script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
  <script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
  <script>
    var contextPath = '<%=request.getContextPath()%>';
    $(function(){
      pdata();
    });
    function pdata() {
      var url=contextPath+'/payment/profitLossPayment.tg';
        $('#tt').datagrid({
          singleSelect:true,
          border:false,
          rownumbers:true,
          pagination:true,
          fit:true,
          border:false,
          idField:'itemid',
          url:url,
          onClickRow:onClickRow,
          columns:[[
            {field:'productName',title:'名称',sortable:true,width:160},
            {field:'quantity_b',title:'销售数量',align:'right',align:'right',sortable:true,width:120},
            {field:'quantity_p',title:'入库数量',align:'right',sortable:true,width:120},
            {field:'quantity_i',title:'货损数量',align:'right',sortable:true,width:120},
            {field:'quantity_w',title:'库存数量',align:'right',sortable:true,width:120},
            {field:'acount_b',title:'销售金额',align:'right',sortable:true,width:120},
            {field:'acount_p',title:'入库金额',align:'right',sortable:true,width:120},
            {field:'acount_i',title:'货损金额',align:'right',sortable:true,width:120},
            {field:'acount_w',title:'库存金额',align:'right',sortable:true,width:120},
          ]]
        });
    }
    function doSearch(){
      $('#tt').datagrid('load',{
        productName: $('#productName').val(),
        storeId:$('#sid').combobox('getValue'),
        classId: $('#pclass').combotree('getValue'),
        //isBusiness:'aaa',
        startTime:$('#startTime').datebox('getValue')
      });
    }
    function reset()
    {
      $('#pclass').searchbox('setValue', null);
      $('#sid').searchbox('setValue', null);
      $('#startTime').searchbox('setValue', null);
      $('#productName').searchbox('setValue', null);
    }
    function onClickRow(){
      addregion();
      var billArr=['bill.billNo','bill.billDateTime','price','quantity','bill.createBy.userName'];
      var purchaseArr=['purchase.code','purchase.checkDate','price','quantity','purchase.inby.name'];
      var inventoryArr=['inventory.code','inventory.checkDate','price','damageQuantity','inventory.createBy.name'];

     details(contextPath+'/inventory/getItemsVDetail.tg','inventory',inventoryArr);
     details(contextPath+'/purchase/getItemsPurDetail.tg','purchase',purchaseArr);
     details(contextPath+'/bill/getItemsBDetail.tg','bill',billArr);
    }
    function addregion(){
      if(!$('#south').length>0){
       // $('#main').layout('expand','south');
    /*  }else{*/
      var options={
        id:'south',
        region:'south',
        split:true,
        height:'50%'
      };
      $('#main').layout('add',options);
      $('#south').append($('#tabs'));
      }
    }
    function removeItems(){
      $('#main').layout('collapse','south');
    }
    function details(url,id,arr){
      var entity=$('#tt').datagrid('getSelected');
      $('#'+id).datagrid({
        singleSelect:true,
        border:false,
        rownumbers:true,
        pagination:true,
        fit:true,
        border:false,
        idField:'itemid',
        url:url,
        queryParams:{
          productId:entity.productId,
          startTime:$('#startTime').datebox('getValue')
        },
        columns:[[
          {field:arr[0],title:'单号',align:'right',align:'right',sortable:true,width:120},
          {field:arr[1],title:'日期',align:'right',sortable:true,width:120},
          {field:arr[2],title:'单价',align:'right',sortable:true,width:120},
          {field:arr[3],title:'数量',align:'right',sortable:true,width:120},
          {field:arr[4],title:'操作员',align:'right',sortable:true,width:120}
        ]]
      });
    }
  </script>
</head>
<body>
<div id="main" class="easyui-layout" fit="true" border="false">
  <div region="north" border="false" >
      <table>
        <tr>
          <td>&nbsp;</td>
          <td>分类：</td>
          <td>
            <input id="pclass" class="easyui-combotree"  url="<c:url value='/class/getTreeClass.tg'/>"/>
          </td>
          <td>商品名称：</td>
          <td>
            <input id="productName" class="easyui-textbox" style="width:200px"/>
          </td>
          <td>商店：</td>
          <td>
            <input id="sid" name="purchase.storeId" class="easyui-combobox" prompt="商店名称" style="width:180px"
                   data-options="valueField:'storeId',textField:'storeName',url:'/store/users_storesStore.tg?id=${sessionScope.userId}'" />
          </td>

          <td>起始时间：</td>
          <td>
            <input class="easyui-datetimebox" id="startTime" editable="false" name="bill.startTime" />
          </td>
          <td>
            <a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0);" plain="true" onclick="doSearch();">查找</a>
          </td>
          <td>
            <a class="easyui-linkbutton" iconCls="icon-clear" href="javascript:void(0);" plain="true" onclick="reset();">清空</a>
          </td>
        </tr>
      </table>
  </div>
  <div  region="center"   border="false">
      <table id="tt"></table>
  </div>
</div>
<div id="tabs"  style="height: 100%">
<div   class="easyui-tabs" fit="true" data-options="tools:'#tab-tools'">
  <div title="销售">
    <table id="bill"></table>
  </div>
  <div title="入库">
    <table id="purchase"></table>
  </div>
  <div title="货损">
    <table id="inventory"></table>
  </div>
</div>
</div>
<div id="tab-tools">
  <a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'layout-button-down'" onclick="removeItems()"></a>
</div>
</body>
</html>