function showQueryDialog(options){
	var opts = options || {};
	var dlg = $('#dlg-query');
	if (!dlg.length){
		dlg = $('<div id="dlg-query"></div>').appendTo('body');
		dlg.dialog({
			title:opts.title||'高级查询',
			width:opts.width||400,
			height:opts.height||300,
			closed:true,
			modal:true,
			href:opts.form,
			buttons:[{
				text:'查询',
				iconCls:'icon-search',
				handler:function(){
					dlg.dialog('close');
					var param = {};
					dlg.find('.query').each(function(){
						var name = $(this).attr('name');
						var val = $(this).val();
						if ($(this).hasClass('datebox-f')){
							name = $(this).attr('comboname');
							val = $(this).datebox('getValue');
						} else if ($(this).hasClass('combogrid-f')){
							name = $(this).attr('comboname');
							val = $(this).combogrid('getValue');
						} else if ($(this).hasClass('combobox-f')){
							name = $(this).attr('comboname');
							val = $(this).combobox('getValue');
						}
						param[name] = val;
					});
					opts.callback(param);
				}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){dlg.dialog('close');}
			}]
		});
	}
	dlg.dialog('open');
}

/**
 * 选择商品资料
 * @param callback:function(rows){}
 * @return
 */
function selectGood(callback,params){
	console.log(params);
	var dlg = $('#dlg-selectGood');

	var ahref=contextPath+'/product/queryGoodProduct.tg?storeId='+params.storeId;
	if(params.stock==1)
		ahref+='&stockflag='+params.stock;
	//if (!dlg.length){
	$('#dlg-selectGood').remove();
		dlg = $('<div id="dlg-selectGood"></div>').appendTo('body');
		dlg.dialog({
			title:'选择商品',
			width:800,
			height:400,
			closed:true,
			href:ahref,
			onLoad:function(){
				bindEvents();
			}
		});

	//}
	function bindEvents(){
		$('#queryGood-ok').unbind('click').bind('click', function(){
			dlg.dialog('close');
			var rows = $('#queryGood-dt-goods').datagrid('getSelections');
			callback(rows);
			$('#queryGood-dt-goods').datagrid('clearSelections');
		});
	}
	bindEvents();
	dlg.dialog('open');
}

/**
 * 选择采购退货商品资料
 * @param callback
 * @param intercourseId
 * @param depotId
 * @return
 */
function selectGood1(callback, intercourseId, depotId){
	var dlg = $('#dlg-selectGood1');
	if (!dlg.length){
		dlg = $('<div id="dlg-selectGood1"></div>').appendTo('body');
		dlg.dialog({
			title:'选择退货商品',
			width:800,
			height:400,
			closed:true,
			href:contextPath+'/views/erp/_public/_queryGood1.jsp',
			onLoad:function(){
				$('#queryGood-intercourseId').val(intercourseId);
				$('#queryGood-depotId').val(depotId);
				$('#queryGood-dt-goods').datagrid({
					queryParams:{
						intercourseId:intercourseId,
						depotId:depotId,
						dateFrom:$('#queryGood-dateFrom').datebox('getValue'),
						dateTo:$('#queryGood-dateTo').datebox('getValue')
					}
				});
				bindEvents();
			}
		});
	}
	if ($('#queryGood-intercourseId').val() != intercourseId || $('#queryGood-depotId').val() != depotId){
		$('#queryGood-intercourseId').val(intercourseId);
		$('#queryGood-depotId').val(depotId);
		setTimeout(function(){
			$('#queryGood-search').triggerHandler('click');
		}, 0);
	}
	function bindEvents(){
		$('#queryGood-ok').unbind('click').bind('click', function(){
			dlg.dialog('close');
			var rows = $('#queryGood-dt-goods').datagrid('getSelections');
			callback(rows);
			$('#queryGood-dt-goods').datagrid('clearSelections');
		});
	}
	bindEvents();
	dlg.dialog('open');
}
/**
 * 变换商店验证商品归属
 */
function isStore(record){
	var rows=$('#dg-details').datagrid('getRows');
	var storeId=$('#store').val();
	//console.log(storeId);
	var productNames="";
	$.each(rows,function(i){
		if(rows[i].storeId!=null&&rows[i].storeId!=record.storeId){
			productNames+=rows[i].productName+",";
			if(storeId=="")storeId=rows[i].storeId;
		}
	});
	if(productNames!=""){
		$('#storeId').combobox('setValue',storeId);
		$.messager.alert('警告','['+productNames.substr(0,productNames.lastIndexOf(","))+']不属于'+record.storeName+',删除后再切换仓库')
		//console.log(record);
	}
}
/**
 * 提交单据表单
 * @param submitType 保存类型：0保存，1保存并新建
 * @return
 */
function submitBill(submitType){
	var rows = $('#dg-details').datagrid('getRows');
	console.log(rows.length);
	if(rows.length==0){
		$.messager.alert('警告','没有明细，请添加商品','question');
		return;
	}
	 for ( var i = 0; i < rows.length; i++) {
		 $('#dg-details').datagrid('endEdit', i);
     }
	var inserted =  $('#dg-details').datagrid('getChanges', "inserted");
    var deleted =  $('#dg-details').datagrid('getChanges', "deleted");
    var updated = $('#dg-details').datagrid('getChanges', "updated");
    var billcounts=0;
    var billcosts=0;
    var paids=0;
    var unpaids=0;
    var dmgnumber=0;
    var totalsum=0;
    for(var i=0; i<rows.length; i++){
		var row = rows[i];
		//console.log(row);
		billcounts+=Number((row.totalCount||0));
		billcosts+=Number(row.totalCount||0)*Number(row.totalPrice||0);
		dmgnumber+=Number((row.inventoryQuantity||0))-Number((row.warehouseQuantity||0));
		totalsum+=(Number((row.inventoryQuantity||0))-Number((row.warehouseQuantity||0)))*Number(row.purchasePrice||0);
		paids+=Number((row.paid||0));
		unpaids+=Number((row.unpaid||0));
		//s += row.id + ':' + (row.billCount||0) + ':' + (row.billPrice||0) + ':' + (row.billDetailId||'') + ',';
	}
    //console.log(dmgnumber+" : "+totalsum);
    $('#totalcounts').val(billcounts);
    $('#totalcosts').val(billcosts);
    $('#paids').val(paids);
    $('#unpaids').val(unpaids);
    $('#totaldge').val(dmgnumber);
    $('#totalsum').val(totalsum);
//    alert(billcounts+'   '+billcosts);
    if (inserted.length) {
    	$('#inserted').val(JSON.stringify(inserted));
    }
    if (deleted.length) {
    	$('#deleted').val(JSON.stringify(deleted));
//    	alert(JSON.stringify(deleted));
    	//console.log(JSON.stringify(deleted));
    }
    if (updated.length) {
    	$('#updated').val(JSON.stringify(updated));
    	//console.log(JSON.stringify(updated));
    }
	
	$('#fm-bill').form('submit', {
		onSubmit:function(){
			var isValid = $(this).form('validate');
			if (isValid){
				$.messager.progress();
			}
			return isValid;
		},
		success:function(result){
			$.messager.progress('close');
			var result = eval('(' + result + ')');
			if (result.success){
				$.post('/permissions/arguments/findByNameCheckArgument.tg',{param:isaudit},function(rs){
					var rs = eval('(' + rs + ')');
					if(rs.isaudit){
						checkBillcomm(result.id,checkUrl);
					}
					else{
						$('#dg-bills').datagrid('reload');
						var createUrl = $('#dg-bills').datagrid('options').createUrl;
						var editUrl = $('#dg-bills').datagrid('options').editUrl + '?id=' + result.id;
						$('#dlg-bill').dialog('refresh', (submitType==2 ? createUrl : editUrl));
					}
				});
				
				//
			} else {
				$.messager.show({
					title:'提示',
					msg:result.msg
				});
			}
		}
	});
}

/**
 * 审核单据
 * @param id 单据ID
 * @return
 */
function checkBillcomm(id,url){
	$.messager.progress();
	$.post(contextPath+url, {id:id}, function(result){
		$.messager.progress('close');
		result=eval('('+result+')');
		//console.log(result+" "+result.success);
		if (result.success){
			var url = $('#dg-bills').datagrid('options').editUrl + '?id=' + result.id;
			$('#dg-bills').datagrid('reload');
			$('#dlg-bill').dialog('refresh', url);
		} else {
			$.messager.show({
				title:'提示',
				msg:result.msg
			});
		}
	});
}

