<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8"> <!-- 文件编码一定是UTF-8 -->
	<title>价格曲线</title>
	<meta name="description" content="Highcharts Exporting Server" />
	<script type="text/javascript" src="<c:url value="/js/easyui/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/highcharts/highcharts.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/highcharts/exporting.js"/>"></script>
	<script>
	var data=null;
	data+=${data};
	var labelsDate;
	$(function () {
	   if(data)
		   chartLine();
	});
	function chartLine(){
		 var chart = new Highcharts.Chart({
		    	
		        chart: {
		            renderTo: 'container'
		            
		        },
		        title: {
		            text: '价格曲线图',
		            x: -20 
		        },
		     
		        xAxis: {
		            type: 'datetime',
		             labels: {
		                formatter: function() {
		                               return  Highcharts.dateFormat('%Y-%m-%d %H:%M', this.value);
		                },
		                
		                }
		        },
		        yAxis: {
		            title: {
		                text: '价格'
		            },
		            plotLines: [{
		                value: 0,
		                width: 1,
		                color: '#808080'
		            }]
		        },
		         tooltip: {  
		            shared : true,
		            xDateFormat: '%Y-%m-%d %H:%M'//鼠标移动到趋势线上时显示的日期格式  
		        },  
		        series: [{
		            name: '价格',
		            data: ${data}
		        }
		               ]
		    
		    });
	}
	
	</script>
</head>

<body>
	<div id="container" style="min-width:800px;height:400px"></div>
</body>

</html>