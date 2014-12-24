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
	<div id="main" class="easyui-layout" fit="true" border="false">
		<div id="center" region="center"  border="false" >
            <div id="searcher" style="height: 13%;">
                <table>
                    <tr>
                        <td><span class="inp1">开始时间：</span></td>
                        <td><input class="easyui-datetimebox" id="startTime" editable="false" name="startTime" /></td>
                        <td><span class="inp1">结束时间：</span></td>
                        <td><input class="easyui-datetimebox"  id="endTime" editable="false" name="endTime" /></td>
                    </tr>
                    <tr>
                        <td><span class="inp1">商品名称：</span></td>
                        <td><input  class="easyui-textbox" id="productName"  name="productName"/></td>
                        <td><span class="inp1">销售人员：</span></td>
                        <td><input  class="easyui-textbox" id="saleMan"  name="productName"/> </td>
                    </tr>
                    <tr>
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
            <div id="content" style="height:87%" >
				<table id="bill"></table>
                </div>
		</div>
	</div>
<!-- 附加-->
 <div id="container"></div>
    <table id="details"></table>
</body>

