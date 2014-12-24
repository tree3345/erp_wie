<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head id="Head1">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>后台管理系统</title>
	<link href="<c:url value="/css/default.css"/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value="/css/index.css"/>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<c:url value="/js/easyui/themes/default/easyui.css"/>" type="text/css" />
	<link rel="stylesheet" href="<c:url value="/js/easyui/themes/icon.css"/>" type="text/css" />
	<script src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
	<script type="text/javascript" src='<c:url value="/js/outlook2.js"/>'></script>
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
<noscript>
	<div style="position: absolute; z-index: 100000; height: 2046px; top: 0px; left: 0px; width: 100%; background: white; text-align: center;">
		<img src="images/noscript.gif" alt='抱歉，请开启脚本支持！' />
	</div>
</noscript>
<div region="north"  id="index-north" border="false"
	 style="overflow: hidden; height: 30px; background: url(images/layout-browser-hd-bg.gif) #7f99be repeat-x center 50%; line-height: 20px; color: #fff; font-family: Verdana, 微软雅黑, 黑体">
		<span style="float: right; padding-right: 20px;" class="head">欢迎
			<shiro:principal /> <a href="logoutLogin.tg" id="loginOut">安全退出</a>
		</span> <span style="padding-left: 10px; font-size: 16px;"><img
		src="images/blocks.gif" width="20" height="20" align="absmiddle" />
			${store.storeName }后台管理</span>
</div>
<div region="south"
	 style="height: 30px; background: #D2E0F2;">
	<div class="footer">后台管理</div>
</div>
<div region="west" hide="true"  title="导航菜单"
	 style="width: 173px;" id="west">
	<div id="nav" class="accordion accordion-noborder" fit="true"
		 border="false">
		${treescript}
	</div>
</div>
<div id="mainPanle" region="center"
	 style="background: #eee; overflow-y: hidden">
	<div id="tabs" class="easyui-tabs" fit="true" border="true">
		<div  title="首   页" border="false" style="overflow: hidden;">
			<iframe src="<c:url value='/layout/portal/portal.html'/>" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>
		</div>
	</div>
</div>
<!-- 右键操作 -->
<div id="mm" class="easyui-menu" style="width: 150px;">
	<div id="mm-tabupdate">刷新</div>
	<div class="menu-sep"></div>
	<div id="mm-tabclose">关闭</div>
	<div id="mm-tabcloseall">全部关闭</div>
	<div id="mm-tabcloseother">除此之外全部关闭</div>
	<div class="menu-sep"></div>
	<div id="mm-tabcloseright">当前页右侧全部关闭</div>
	<div id="mm-tabcloseleft">当前页左侧全部关闭</div>
	<div class="menu-sep"></div>
	<div id="mm-exit">退出</div>
</div>
</body>
</html>