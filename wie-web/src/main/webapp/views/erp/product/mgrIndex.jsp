<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://com.wie.com.cn/tag/easyui" prefix="tgEasyui"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/default/easyui.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:url value="/js/easyui/themes/icon.css"/>">
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.easyui.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/jquery.formid.js"/>"></script>
	<script src="<c:url value="/js/easyui/locale/easyui-lang-zh_CN.js"/>"></script>
	<script type="text/javascript">
		var data=[{
			"id": '0',
			"text": "所有商品",
			"state": "open",
			"children": [{
				"id": '1',
				"text": "特有商品"
			},{
				"id": '2',
				"text": "公共商品"
			}]
		}];

		$(function(){
			getOpt({id:'0'});
		});
		function getOpt(opt){
			var url='<c:url value="/product/indexProduct.tg"/>?flag=';
			console.log(opt.id);
			url+=opt.id;
			console.log(url);
			$('#content').remove();
			$('#center').append('<div id="content" border="false"></div>');
			$('#content').append('<iframe id="good" style="width:99%;height:99%;border:none" src="'+url+'"></iframe>');
		}
	</script>
</head>
<body>
<div class="easyui-layout" style="width:100%;height:100%;" border="false">
	<div data-options="region:'east'" title="商品管理"  border="false" style="width:180px;">
		<ul class="easyui-tree" id="goods" data-options="data:data,onClick:getOpt"></ul>
	</div>
	<div  id="center" data-options="region:'center'" border="false">
	</div>
</div>
</body>