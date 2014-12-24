<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div>
<div class="subtitle" style="border-bottom:1px solid #ccc">请输入查询参数：</div>
<div style="padding:10px 0 10px 30px">
		<table>
			<tr>
				<td style="width:80px">用户登录名</td>
					<td><input type="text" id="qname" class="query" name="user.logonid"/></td>
				</tr>
				<tr>
					<td>性别</td>
					<td><input type="text" id="qename" class="easyui-combobox" data-options="valueField:'value',textField:'name',url:'/permissions/actions/outDicJsonByNicknameActions.tg?nickName=sex'" name="user.sex"/></td>
				</tr>
				
				<tr>
					<td>出生日期</td>
					<td><input type="text" id="qbuilddate" class="query" name="user.birthday"/></td>
				</tr>
				
			</table>
	</div>
</div>

	