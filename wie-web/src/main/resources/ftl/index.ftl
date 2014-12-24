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
    <div class="easyui-layout" fit="true">
    <div region="north" border="false"> </div>
    <div region="center" style="width:70%" border="true">
            <div id="toolbar1" class="toolbar" style="height:5%">
            <table cellpadding="0" cellspacing="0" style="width:95%;" fit="true">
                <tr>
                  <td>
                  <#list actionlist as actions>
                        <tgEasyui:easyuiButton iconCls="${actions.icon}" method="${actions.methodName}" permission="${resourceName}:${actions.enname}" operationName="${actions.name}"/>
                  </#list>
                   </td>
               </tr>
           </table>
            </div>
    </div>
    </div>
    </body>
    </html>