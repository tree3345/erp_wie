/**
 * 可编辑的单据明细表格
 */
(function($){
	$.fn.datagrid.defaults.editors.numberbox.getValue = function(target){
		$(target).numberbox('fix');
		return $(target).val();
	};
	
	function updateFooter(target){
		var billCount = 0,billCost = 0,paid=0,unpaid=0,damageSum=0;
		var rows = $(target).datagrid('getRows');
		for(var i=0; i<rows.length; i++){
			var row = rows[i];
		//	alert(row.warehouseQuantity+' '+row.inventoryQuantity);
			if (row.billCount){
				billCount += parseInt(row.billCount);
			}
			if (row.billCost){
				billCost += parseFloat(row.billCost);
			}
			if(row.inventoryQuantity){
				billCount+=parseFloat(row.inventoryQuantity)
			}
			if(row.damageQuantity){
				billCost+=parseFloat(row.damageQuantity);
			}
			if(row.paid){
				paid+=parseFloat(row.paid);
			}
			if(row.unpaid){
				unpaid+=parseFloat(row.unpaid);
			}
			if(row.damageSum){
				damageSum+=parseFloat(row.damageSum);
			}
		}
		$(target).datagrid('reloadFooter',[{productNo:'合计',billCount:billCount,billCost:billCost,paid:paid,unpaid:unpaid,damageSum:damageSum}]);
	}
	function editCell(target, index, field){
		var opts = $(target).datagrid('options');
		if (opts.editIndex != index){
//			var editor = $(target).datagrid('getEditor', {index:opts.editIndex,field:field});
//			if (editor){
//				editor.target.triggerHandler('blur');
//			}
			$(target).datagrid('endEdit', opts.editIndex);
			opts.editIndex = index;
			$(target).datagrid('selectRow', index);
			$(target).datagrid('beginEdit', index);
			var editor = $(target).datagrid('getEditor', {index:index,field:field});
			if (editor){
				setTimeout(function(){
					editor.target.focus();
				}, 0);
			}
			
			var editors = $(target).datagrid('getEditors', index);
			for(var i=0; i<editors.length; i++){
				editors[i].target.bind('keydown',{field:editors[i].field},function(e){
					if (e.keyCode == 13){
						$(target).datagrid('endEdit', opts.editIndex);
						opts.editIndex = undefined;
					} else if (e.keyCode == 38){	// up
						if (opts.editIndex > 0){
							editCell(target, opts.editIndex-1, e.data.field);
						}
					} else if (e.keyCode == 40){	// down
						if (opts.editIndex < $(target).datagrid('getRows').length-1){
							editCell(target, parseInt(opts.editIndex)+1, e.data.field);
						}
					}
				});
			}
		}
	}
	
	function buildGrid(target, options){
		$(target).datagrid($.extend({},options,{
			onClickCell:function(index,field){
				if (options.editable){
					editCell(target, index, field);
				}
			},
			onRowContextMenu:function(e,index){
				e.preventDefault();
				
				if (options.editable){
					var opts = $(this).datagrid('options');
					$(this).datagrid('endEdit', opts.editIndex);
					opts.editIndex = undefined;
					
					$(target).datagrid('selectRow',index);
					menu = $(target).datagrid('options').menu;
					menu.menu('show',{
						left:e.pageX,
						top:e.pageY
					});
				}
			},
			onAfterEdit:function(index){
				var dt = $(this);
				var row = dt.datagrid('getRows')[index];
				row.billCost = row.totalCount * row.totalPrice;
				if(row.warehouseQuantity){
					row.billCost=Number(row.inventoryQuantity)-Number(row.warehouseQuantity);
					row.damageSum= Number(row.billCost)*row.purchasePrice;
				}
				
				row.unpaid=row.billCost-row.paid;
				dt.datagrid('refreshRow',index);
				updateFooter(target);
			}
		}));
		
		$(target).datagrid('getPanel').unbind('.detail').bind('mousedown.detail',function(e){
			e.stopPropagation();
		});
		$(document).unbind('.detail').bind('mousedown.detail',function(){
			var dt = $(target);
			var opts = dt.datagrid('options');
			if (opts.editIndex){
				dt.datagrid('endEdit',opts.editIndex);
				opts.editIndex = undefined;
			}
		});
		
		var menu = $(target).datagrid('options').menu;
		if (!menu){
			menu = $('<div style="width:120px"></div>').appendTo('body');
			$(target).datagrid('options').menu = menu;
			menu.menu();
			menu.menu('appendItem',{
				text:'添加商品',
				iconCls:'icon-add',
				handler:function(){
					$(target).detailgrid('addGood');
				}
			});
			menu.menu('appendItem',{
				text:'删除商品',
				iconCls:'icon-remove',
				handler:function(){
					$(target).detailgrid('delGood');
				}
			});
			menu.menu('appendItem',{
				text:'上移',
				handler:function(){
					$(target).detailgrid('move','up');
				}
			});
			menu.menu('appendItem',{
				text:'下移',
				handler:function(){
					$(target).detailgrid('move','down');
				}
			});
		}
	}
	
	var methods = {
		exists: function(jq, id){
			var dt = jq;
			var rows = dt.datagrid('getRows');
			for(var i=0; i<rows.length; i++){
				if (rows[i].id == id){
					return true;
				}
			}
			return false;
		},
		addGood: function(jq,params){
			if(params.storeId==""){
				$.messager.alert("警告","请选择商店")
				return;
			}

			return jq.each(function(){
				var dt = $(this);
				function isExists(id){
					var rows = dt.datagrid('getRows');
					for(var i=0; i<rows.length; i++){
						if (rows[i].productId == id){
							return true;
						}
					}
					return false;
				}
				
				selectGood(function(rows){
					//console.log(rows);
					for(var i=0; i<rows.length; i++){
						var row = $.extend({}, rows[i]);
						if (!isExists(row.productId)){
								dt.datagrid('appendRow', row);
						}
					}
				},params);
			});
		},
		addGood1: function(jq, param){
			
			return jq.each(function(){
				var dt = $(this);
				selectGood1(function(rows){
					for(var i=0; i<rows.length; i++){
						var row = $.extend({}, rows[i]);
						if (!dt.detailgrid('exists', row.id)){
							row.billCount = undefined;
							dt.datagrid('appendRow', row);
						}
					}
				}, param.intercourseId, param.depotId);
			});
		},
		addGood2: function(jq, stock){
			return jq.each(function(){
				var dt = $(this);
				function isExists(id){
					var rows = dt.datagrid('getRows');
					for(var i=0; i<rows.length; i++){
						if (rows[i].productId == id){
							return true;
						}
					}
					return false;
				}
				selectGood(function(rows){
					
					for(var i=0; i<rows.length; i++){
						var row = $.extend({}, rows[i]);
						if (!isExists(row.productId)){
								dt.datagrid('appendRow', row);
						}
					}
				},stock);
			});
		},
		delGood: function(jq){
			return jq.each(function(){
				var opts = $(this).datagrid('options');
				var row = $(this).datagrid('getSelected');
				if (row){
					var index = $(this).datagrid('getRowIndex',row);
					$(this).datagrid('cancelEdit',index);
					$(this).datagrid('deleteRow',index);
					updateFooter(this);
					opts.editIndex = undefined;
				}
			});
		},
		move: function(jq, dir){	// dir: up or down
			return jq.each(function(){
				var row = $(this).datagrid('getSelected');
				if (row){
					var index = $(this).datagrid('getRowIndex', row);
					if (dir == 'up'){
						var insIndex = index> 0 ? index - 1 : index;
					} else {
						var insIndex = index + 1;
					}
					$(this).datagrid('deleteRow', index);
					$(this).datagrid('insertRow', {
						index: insIndex,
						row: row
					});
					index = $(this).datagrid('getRowIndex', row);
					$(this).datagrid('selectRow', index);
				}
			});
		}
	};
	
	$.fn.detailgrid = function(options, param){
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
			$.data(this, 'detailgrid', {
				options: options
			});
			buildGrid(this, options);
		});
	};
})(jQuery);