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
<body
	style="margin: 0; padding: 0; height: 100%; overflow: hidden; background: #F2FBFF">
	<div class="easyui-layout" fit="true" border="false">
		<div region="center"  border="false" style="width: 40%;">
            <div id="searcher" style="height: 12%">
                <table>
                    <tr>
                        <td>
                            <span class="inp1">商品名称：</span></td>
                        <td>
                            <input  class="easyui-textbox" id="productName"  name="productName"/>
                            </td>
                        <td>
                            <span class="inp1">开始时间：</span>
                            </td>
                        <td>
                            <input class="easyui-datetimebox" id="startTime" editable="false" name="startTime" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <span class="inp1">销售人员：</span>
                            </td>
                        <td>
                            <input  class="easyui-textbox" id="saleMan"  name="productName"/>
                            </td>
                        <td>
                            <span class="inp1">结束时间：</span>
                            </td>
                        <td>
                            <input class="easyui-datetimebox"  id="endTime" editable="false" name="endTime" />
                            </td>
                    </tr><tr>
                    <th><span>商店名称:</span></th>
                    <td><input id="sid" class="easyui-combobox" name="sid"
                               data-options="valueField:'storeId',textField:'storeName',url:'/store/users_storesStore.tg?id=${sessionScope.userId}'" /></td>
                    <td></td>
                    <td style="text-align: right">
                            <a class="easyui-linkbutton" href="javascript:void(0);" onclick="doSearch();">查找</a>
                            <a class="easyui-linkbutton" href="javascript:void(0);" onclick="reset();">清空</a>
                            <%--<a class="easyui-linkbutton" href="javascript:void(0);"data-options="iconCls:'icon-large_chart'" onclick="openCharts();">生成报表</a>--%>
                        </td>
                    </tr>
                </table>
            </div>
            <div style="height: 88%"><table id="bill"></table></div>

		</div>
        <div region="east" style="width: 60%;">
            <div style="height: 50%">
                <table id="details"></table>
               </div>
            <div style="height: 50%">
                <table id="details2"></table>
            </div>

        </div>
	</div>
 <div id="container"></div>

</body>

