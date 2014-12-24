/**
 * Created by Administrator on 2014/12/19.
 */
function getChanges(id){
    var checkeds=$(id).datagrid('getChecked');
    var all=$(id).datagrid('getRows');
    var subArr=new Array();
    $.each(all,function(i){
        if($.inArray(all[i],checkeds)==-1){
            subArr.push(all[i]);
        }
    });

    var insertRows=new Array();
    $.each(checkeds,function(i){
        if(!checkeds[i].ischeck){
            insertRows.push(checkeds[i])
        }
    });

    var deleteRows=new Array();
    $.each(subArr,function(i){
        if(subArr[i].ischeck){
            deleteRows.push(subArr[i]);
        }
    });
    if(insertRows.length==0&&deleteRows.length==0)
        return false;
    var obj={insert:insertRows,delete:deleteRows};
    return obj;
}
