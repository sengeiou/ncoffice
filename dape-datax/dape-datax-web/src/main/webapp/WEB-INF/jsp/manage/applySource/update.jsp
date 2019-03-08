﻿<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<div id="updateDialog" class="crudDialog">
	<form id="updateForm" method="post">

		<div class="form-group">
			<label for="linkIp">IP</label>
			<input id="linkIp" type="text" class="form-control" name="linkIp" maxlength="20" value="${applySource.linkIp}">
		</div>

		<div class="form-group">
			<label for="dbUser">用户</label>
			<input id="dbUser" type="text" class="form-control" name="dbUser" maxlength="5" value="${applySource.dbUser}">
		</div>

		<div class="form-group">
			<label for="dbPwd">密码</label>
			<input id="dbPwd" type="text" class="form-control" name="dbPwd" maxlength="5" value="${applySource.dbPwd}">
		</div>

		<div class="form-group">
			<label for="dbPort">端口</label>
			<input id="dbPort" type="text" class="form-control" name="dbPort" maxlength="5" value="${applySource.dbPort}">
		</div>

		<div class="form-group">
			<label for="typeId">类型</label>
			<input id="typeId" type="text" class="form-control" name="typeId" maxlength="5" value="${applySource.typeId}">
		</div>

		<div class="form-group">
			<label for="taskId">任务</label>
			<input id="taskId" type="text" class="form-control" name="taskId" maxlength="5" value="${applySource.taskId}">
		</div>


		<div class="form-group text-right dialog-buttons">
			<a class="waves-effect waves-button" href="javascript:;" onclick="createSubmit();">保存</a>
			<a class="waves-effect waves-button" href="javascript:;" onclick="updateDialog.close();">取消</a>
		</div>
	</form>
</div>
<script>
	function createSubmit() {
		$.ajax({
			type: 'post',
			url: '${basePath}/manage/applySource/update/${applySource.id}',
			data: $('#updateForm').serialize(),
			beforeSend: function() {
				if ($('#linkIp').val() == '') {
					$('#linkIp').focus();
					return false;
				}
				if ($('#driverStr').val() == '') {
					$('#driverStr').focus();
					return false;
				}
			},
			success: function(result) {
				if (result.code != 1) {
					if (result.data instanceof Array) {
						$.each(result.data, function(index, value) {
							$.confirm({
								theme: 'dark',
								animation: 'rotateX',
								closeAnimation: 'rotateX',
								title: false,
								content: value.errorMsg,
								buttons: {
									confirm: {
										text: '确认',
										btnClass: 'waves-effect waves-button waves-light'
									}
								}
							});
						});
					} else {
						$.confirm({
							theme: 'dark',
							animation: 'rotateX',
							closeAnimation: 'rotateX',
							title: false,
							content: result.data.errorMsg,
							buttons: {
								confirm: {
									text: '确认',
									btnClass: 'waves-effect waves-button waves-light'
								}
							}
						});
					}
				} else {
					updateDialog.close();
					$table.bootstrapTable('refresh');
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				$.confirm({
					theme: 'dark',
					animation: 'rotateX',
					closeAnimation: 'rotateX',
					title: false,
					content: textStatus,
					buttons: {
						confirm: {
							text: '确认',
							btnClass: 'waves-effect waves-button waves-light'
						}
					}
				});
			}
		});
	}
</script>