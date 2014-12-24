<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui"%>

<head>
<jsp:include page="head.jsp"></jsp:include>
<style type="text/css">
span.inp1 {
	font-size: 12px;
}
</style>
</head>
<body>

<div class="easyui-layout" style="width:100%;height:100%;">
    <div data-options="region:'east',split:false"  style="width:60%;">
        <table id="details"></table>
    </div>
    <div data-options="region:'center',iconCls:'icon-ok',border:false" >
        <div class="easyui-layout" data-options="fit:true">
            <div data-options="region:'north',split:false,border:false" style="height:15%">
                <div id="searcher" >
                    <table>
                        <tr>
                            <td>
                                <span class="inp1">商品名称：</span></td>
                            <td><input  class="easyui-textbox" id="productName"  name="productName"/></td>
                            <td>
                                <span class="inp1">入库人员：</span></td>
                            <td><input  class="easyui-textbox" id="inby"  name="inby"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <span class="inp1">开始时间：</span></td>
                            <td>
                                <input class="easyui-datetimebox" id="startTime" editable="false" name="startTime" />
                            </td>
                            <td>
                                <span class="inp1">结束时间：</span></td>
                            <td>
                                <input class="easyui-datetimebox"  id="endTime" editable="false" name="endTime" />
                            </td>
                        </tr><tr>
                        <td>商店</td><td><input id="sid" class="easyui-combobox" name="sid"
                                              data-options="valueField:'storeId',textField:'storeName',url:'/store/users_storesStore.tg?id=${sessionScope.userId}'" /></td> <td></td>
                        <td style="text-align:right;">
                            <a class="easyui-linkbutton" href="javascript:void(0);" onclick="doSearch();">查找</a>
                            <a class="easyui-linkbutton" href="javascript:void(0);" onclick="reset();">清空</a>
                            <a class="easyui-linkbutton" href="javascript:void(0);"data-options="iconCls:'icon-large_chart'" onclick="openCharts();">报表</a>
                        </td>
                    </tr>
                    </table>
                </div>
            </div>
            <div data-options="region:'center',border:false">
                <table id="bill"></table>
            </div>
        </div>
    </div>
</div>
<div id="container"></div>
</body>


