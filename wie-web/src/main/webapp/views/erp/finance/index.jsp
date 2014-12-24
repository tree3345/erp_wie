<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui" %>

<head>
	<jsp:include page="head.jsp"></jsp:include>
	<style type="text/css">
	 span.inp1 {font-size:12px;}
	</style>
</head>
<body style="margin: 0; padding: 0; height: 100%; overflow: hidden; background: #F2FBFF">
	<div class="easyui-layout" fit="true">
		<div region="west" style="width:40%;height:100%" border="false">
		<div id="searcher" style="width:100%;height:11%" border="false">
		  <table>
                        <tr>
                            <th><span class="inp1">会员编号：<span></span></th>
                            <td><input  class="easyui-textbox" id="memberId"  name="bill.memberId"/></td>
                            <th><span class="inp1">销售员：</span></th>
                            <td><input class="easyui-textbox"  id="createBy" name="bill.createBy.userName"/></td>
                        </tr>
                        <tr>
                            <th><span class="inp1">开始时间:</span></th>
                            <td>
                                <input class="easyui-datetimebox" id="startTime" editable="false" name="bill.startTime" /></td>
                            <th><span class="inp1">结束时间:</span></th>
                            <td>
                                <input class="easyui-datetimebox"  id="endTime" editable="false" name="bill.endTime" /></td>
                            <td>
                            <a class="easyui-linkbutton" href="javascript:void(0);" onclick="doSearch();">查找</a>
                            <a class="easyui-linkbutton" href="javascript:void(0);" onclick="reset();">清空</a>
                            </td>
                        </tr>
                    </table>
                    </div>
                    <div id="div-bill" style="width:100%;height:88%" border="false">
                    <table id="bill" ></table>
                    </div>
		</div>
		<div region="center" style="width:60%;height:100%" >
		<table id="tt"></table>
		</div>
		</div>
		
</body>

