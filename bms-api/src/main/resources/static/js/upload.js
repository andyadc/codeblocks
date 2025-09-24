$(document).ready(function () {
	// 存储已选文件
	let selectedFiles = [];

	// 点击选择文件按钮触发文件输入
	$('#selectFileBtn').click(function () {
		$('#fileInput').click();
	});

	// 文件选择变化时处理
	$('#fileInput').change(function (e) {
		const files = e.target.files;
		if (files.length > 0) {
			addFilesToFileList(files);
		}
	});

	// 添加文件到文件列表
	function addFilesToFileList(files) {
		for (let i = 0; i < files.length; i++) {
			const file = files[i];

			// 检查文件大小限制 (10MB)
			if (file.size > 10 * 1024 * 1024) {
				showAlert('文件 "' + file.name + '" 超过10MB限制', 'danger');
				continue;
			}

			// 检查文件类型
			const allowedTypes = [
				'image/jpeg',
				'image/png',
				'application/pdf',
				'application/msword',
				'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
			];

			if (!allowedTypes.includes(file.type)) {
				showAlert('不支持的文件类型: ' + file.type, 'danger');
				continue;
			}

			// 检查是否已存在同名文件
			if (selectedFiles.some(f => f.name === file.name && f.size === file.size)) {
				showAlert('文件 "' + file.name + '" 已存在', 'warning');
				continue;
			}

			selectedFiles.push(file);
			renderFileItem(file);
		}

		// 重置文件输入，允许重复选择相同文件
		$('#fileInput').val('');
	}

	// 渲染文件项
	function renderFileItem(file) {
		const fileSize = formatFileSize(file.size);
		const fileId = 'file-' + Date.now() + '-' + Math.floor(Math.random() * 1000);

		const fileItem = `
            <div class="file-item" id="${fileId}">
                <div class="file-info">
                    <div class="file-name">${file.name}</div>
                    <div class="file-size">${fileSize}</div>
                </div>
                <div class="delete-btn" data-file-id="${fileId}">
                    <i class="bi bi-trash"></i>
                </div>
            </div>
        `;

		$('#fileList').append(fileItem);
	}

	// 格式化文件大小
	function formatFileSize(bytes) {
		if (bytes === 0) return '0 Bytes';
		const k = 1024;
		const sizes = ['Bytes', 'KB', 'MB', 'GB'];
		const i = Math.floor(Math.log(bytes) / Math.log(k));
		return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
	}

	// 删除文件
	$(document).on('click', '.delete-btn', function () {
		const fileId = $(this).data('file-id');
		const fileName = $('#' + fileId).find('.file-name').text();

		// 从selectedFiles中移除
		selectedFiles = selectedFiles.filter(file => file.name !== fileName);

		// 从DOM中移除
		$('#' + fileId).remove();
	});

	// 显示提示信息
	function showAlert(message, type) {
		const alert = $(`
            <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        `);

		$('.upload-container').prepend(alert);

		// 5秒后自动消失
		setTimeout(() => {
			alert.alert('close');
		}, 5000);
	}

	// 表单提交
	$('#submitBtn').click(function () {
		// 验证表单
		const userName = $('#name').val().trim();
		const userEmail = $('#email').val().trim();

		if (!userName) {
			showAlert('请输入姓名', 'danger');
			$('#userName').focus();
			return;
		}

		if (!userEmail) {
			showAlert('请输入邮箱', 'danger');
			$('#userEmail').focus();
			return;
		}

		if (!validateEmail(userEmail)) {
			showAlert('请输入有效的邮箱地址', 'danger');
			$('#userEmail').focus();
			return;
		}

		// 准备表单数据
		const formData = new FormData();
		formData.append('name', userName);
		formData.append('email', userEmail);
		formData.append('phone', $('#phone').val().trim());

		// 添加文件到表单数据
		if (selectedFiles.length > 0) {
			selectedFiles.forEach((file, index) => {
				formData.append('files', file);
			});
		}

		// 显示进度条
		$('#progressContainer').show();
		$('#submitBtn').prop('disabled', true);

		// AJAX提交
		$.ajax({
			url: '/submit-form', // 替换为你的后端接口
			type: 'POST',
			data: formData,
			processData: false,
			contentType: false,
			xhr: function () {
				const xhr = new window.XMLHttpRequest();

				xhr.upload.addEventListener('progress', function (e) {
					if (e.lengthComputable) {
						const percent = Math.round((e.loaded / e.total) * 100);
						updateProgress(percent);
					}
				}, false);

				return xhr;
			},
			success: function (response) {
				showAlert('提交成功！', 'success');
				resetForm();
			},
			error: function (xhr, status, error) {
				showAlert('提交失败: ' + (xhr.responseText || error), 'danger');
				$('#progressContainer').hide();
				$('#submitBtn').prop('disabled', false);
			}
		});
	});

	// 更新进度条
	function updateProgress(percent) {
		$('#progressBar').css('width', percent + '%');
		$('#progressText').text(percent + '%');
	}

	// 验证邮箱格式
	function validateEmail(email) {
		const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
		return re.test(email);
	}

	// 重置表单
	function resetForm() {
		// 清空表单字段
		$('#userName').val('');
		$('#userEmail').val('');
		$('#userPhone').val('');

		// 清空文件列表
		selectedFiles = [];
		$('#fileList').empty();

		// 重置进度条
		$('#progressContainer').hide();
		$('#progressBar').css('width', '0%');
		$('#progressText').text('0%');

		// 重新启用提交按钮
		$('#submitBtn').prop('disabled', false);
	}

	// 拖放功能
	const uploadArea = $('#uploadArea')[0];

	uploadArea.addEventListener('dragover', function (e) {
		e.preventDefault();
		e.stopPropagation();
		$(this).css({
			'border-color': '#4CAF50',
			'background-color': '#f5fff5'
		});
	});

	uploadArea.addEventListener('dragleave', function (e) {
		e.preventDefault();
		e.stopPropagation();
		$(this).css({
			'border-color': '#ccc',
			'background-color': '#fff'
		});
	});

	uploadArea.addEventListener('drop', function (e) {
		e.preventDefault();
		e.stopPropagation();
		$(this).css({
			'border-color': '#ccc',
			'background-color': '#fff'
		});

		const files = e.dataTransfer.files;
		if (files.length > 0) {
			addFilesToFileList(files);
		}
	});
});
