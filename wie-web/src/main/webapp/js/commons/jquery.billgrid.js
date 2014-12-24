/**
 * $('#dg').billgrid({
 *   createUrl:'',
 *   editUrl:'',
 *   destroyUrl:''
 * });
 */
(function($){
	function buildGrid(target, options){
		//location.href='/loginLogin.tg';
		
		var opts = $.extend({}, {
			onDblClickRow:function(){
				$.post(contextPath+'/isexistLogin.tg',function(data){
					var d=eval('('+data+')');
					if(d.status){
					    $('#dg-bills').billgrid('edit');
					}
					else{
						$.messager.confirm('警告','登录超时，请重新登录！',function(r){
							if(r)
							 location.href='/loginLogin.tg';
						});   
					}
				});
				
			}
		}, options);
		$(target).datagrid(opts);
	}
	
	var methods = {
		create: function(jq){
			return jq.each(function(){
				var opts = $(this).datagrid('options');
				$('#dlg-bill').dialog('open').dialog('refresh', opts.createUrl);
			});
		},
		edit: function(jq){
			return jq.each(function(){
				var opts = $(this).datagrid('options');
				var row = $(this).datagrid('getSelected');
				if (row){
					var id=row.orderId;
					if(row.purchaseId!=null)
						id=row.purchaseId;
					if(row.inventoryId!=null)
						id=row.inventoryId;
					if(row.paymentId!=null)
						id=row.paymentId;
					$('#dlg-bill').dialog('open').dialog('refresh', opts.editUrl+'?id='+id);
				} else {
					$.messager.show({
						title:'警告',
						msg:'请先选择单据后再打开。'
					});
				}
			});
		},
		query: function(jq){
			return jq.each(function(){
				var opts = $(this).datagrid('options');
				var dg = $(this);
				showQueryDialog({
					title:opts.query.title,
					width:opts.query.width,
					height:opts.query.height,
					form:opts.query.form,
					callback:function(data){
						dg.datagrid('load', data);
						if (opts.query.callback){
							opts.query.callback();
						}
					}
				});
			});
		},
		destroy: function(jq){
			return jq.each(function(){
				var dg = $(this);
				var opts = dg.datagrid('options');
				var row = dg.datagrid('getSelected');
				if (row){
					$.messager.confirm('警告','是否真的删除该单据？',function(r){
						if (r){
							var id=row.orderId;
							if(row.purchaseId!=null)
								id=row.purchaseId;
							if(row.inventoryId!=null)
								id=row.inventoryId;
							$.post(opts.destroyUrl, {id:id}, function(result){
								var result = eval('(' + result + ')');
								if (result.success){
									dg.datagrid('reload');
								} else {
									$.messager.show({
										title:'警告',
										msg:result.msg
									});
								}
							});
						}
					});
				} else {
					$.messager.show({
						title:'警告',
						msg:'请先选择单据后再进行删除。'
					});
				}
			});
		}
	};
	
	$.fn.billgrid = function(options, param){
		if (typeof options == 'string'){
			var method = methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.datagrid(options, param);
			}
		}
		
		options = options || {};
		return this.each(function(){
			buildGrid(this, options);
		});
	};
})(jQuery);