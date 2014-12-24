<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<html> 
<head><title>test</title> 

<style type="text/css">@import url(js/jquery.plupload.queue/css/jquery.plupload.queue.css);</style>
<script src="/js/jquery-1.6.4.js" type="text/javascript"></script> 
<script type="text/javascript" src="js/plupload.full.js"></script>
<script type="text/javascript" src="js/jquery.plupload.queue/jquery.plupload.queue.js"></script>
<script type="text/javascript" src="js/i18n/cn.js"></script>
<script type="text/javascript">
// Convert divs to queue widgets when the DOM is ready
	$(function() {
		$("#uploader").pluploadQueue({
			// General settings
			runtimes : 'gears,flash,silverlight,browserplus,html5,html4',
			url : 'uploadFileUpload.action',
			max_file_size : '10mb',
			unique_names : true,
			multiple_queues : true,
			chunk_size: '2mb',
			// Specify what files to browse for
			filters : [
				{title : "xls, xlsx文档", extensions : "xls,xlsx"}
			],
	
			// Flash settings
			flash_swf_url : '/plupload/js/plupload.flash.swf',
			// Silverlight settings
			silverlight_xap_url : '/plupload/js/plupload.silverlight.xap'
		});
		$('form').submit(function(e) {
	        var uploader = $('#uploader').pluploadQueue();
	        if (uploader.files.length > 0) {
	            // When all files are uploaded submit form
	            uploader.bind('StateChanged', function() {
	                if (uploader.files.length === (uploader.total.uploaded + uploader.total.failed)) {
	                    $('form')[0].submit();
	                }
	            });
	            uploader.start();
	        } else {
				alert('请先上传数据文件.');
			}
	        return false;
    	});
	});
	
	
</script>

</head>

<body>
	<div>
		<div style="width: 750px; margin: 0px auto">
			<form id="formId" action="submitFileUpload.action" method="post">
				<div id="uploader">
					<p>您的浏览器未安装 Flash, Silverlight, Gears, BrowserPlus 或者支持 HTML5 .</p>
				</div>
				<input type="submit" value="完成"/>
			</form>
		</div>
	</div>
</body>

</html>