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
	
	$(function () {
	   if(${data})
		   chartLine();
	});
	function chartLine(){
		 var chart = new Highcharts.Chart({
		    	
		        chart: {
		            renderTo: 'container'
		        },
		        title: {
		            text: '入库情况',
		            x: -20 
		        },
		     
		        xAxis: {
		            type: 'datetime',
		             labels: {
		                formatter: function() {
		                	console.log(this.value);
		                               return  Highcharts.dateFormat('%Y-%m-%d %H:%M', this.value);
		                },
		                }
		        },
		        yAxis: [{ //第一个Y轴，序号为0
		            labels: {
		                format: '{value} ￥',
		                style: {
		                    color: '#89A54E'
		                }
		            },
		            title: {
		                text: '价格',
		                style: {
		                    color: '#89A54E'
		                }
		            }
		        }, { //第二个Y轴，序号为1
		            title: {
		                text: '数量',
		                style: {
		                    color: '#FFFFFF'
		                }
		            },
		            labels: {
		                format: '{value} ${unit}',
		                style: {
		                    color: '#FFFFFF'
		                }
		            },
		            opposite: true
		        }],
		series: [{ //第二个Y轴的数据
		            name: '数量',
		            color: '#4572A7',
		            type: 'column',
		            yAxis: 1,//坐标轴序号
		            data: [${data}],
		            tooltip: {
		                valueSuffix: ' ${unit}'
		            }
		        }, { //第一个Y轴的数据
		            name: '价格',
		            color: '#89A54E',
		            type: 'spline',
		            data: [${data2}],
		            tooltip: {
		                valueSuffix: '￥'
		            }
		        }
		        ,{ //第一个Y轴的数据
			            name: '价格',
			            color: '#FFFFFF',
			            type: 'spline',
			            data: [${data3}],
			            tooltip: {
			                valueSuffix: '￥'
			            }
		        }],
		         tooltip: {  
		            shared : true,
		            xDateFormat: '%Y-%m-%d %H:%M'//鼠标移动到趋势线上时显示的日期格式  
		        }
		    
		    });
	}
	
	</script>
</head>

<body>
	<div id="container" style="min-width:800px;height:400px"></div>
</body>

</html>