$(function() {
	var site = {
		url : "",
		iDate : {
			authorId : 0,
			authorName : "",
			nickName : "",
			gender : "",
			area : "",
			description : "",
			inTime : "",
		},
		/**
		 * 头部header逻辑
		 * 
		 * @param 参数列表
		 */
		showMenu : function() {
			leftColumShow(6);
			var coluId = getRequest("secondColuId");
			var parentColuId = getRequest("firstColuId");
			$("#topMenu").find('li').each(function() {
				$(this).removeClass();
			});
			$("#coluId" + coluId + "").attr("class", "active"); // 添加菜单选中样式
			$("#firstColuId" + parentColuId + "").attr("class", "active"); // 添加菜单选中样式
		},
		/**
		 * 头部header逻辑
		 * 
		 * @param 参数列表
		 */
		addDate : function(iDate) {
			$("#addButton").click(function() {
				clearText();
				$('#tModalLabel').html("添加作者");
				var url = '../../handler/site/addSite';
				// 初始化saveData
				site.saveData(iDate, url);
			});
		},
		saveData : function(iDate, url) {
			$("#saveBUtton").one(
					"click",
					function() {
						$('#pageInfo').empty();
						iDate.authorName = $('#authorName').val();
						iDate.nickName = $('#nickName').val();
						iDate.gender = $('#gender').val();
						iDate.area = $('#area').val();
						iDate.description = $('#description').val();
						iDate.inTime = $('#inTime').val();
						if (iDate.authorName.length <= 0 || iDate.nickName.length <= 0 || iDate.gender.length <= 0
								|| iDate.area.length <= 0) {
							var informationHtml = "";
							informationHtml += "<div class='alert alert-info'>"
									+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
									+ "<h4>注意!</h4> <strong>作者信息填写不全，请查看所填内容是否完整!</strong>";
							$('#modalInfo').append(informationHtml);
							// 初始化saveData
							site.saveData(iDate, url);
							return false;
						}
						$.post(url, iDate, function(data) {
							var informationHtml = "";
							if (data.ret) {
								if (data.data.result == "success") {
									informationHtml += "<div class='alert alert-success'>"
											+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
											+ "<h4>成功!</h4> <strong>作者信息操作成功!</strong>";
								} else if (data.data.result == "isExist") {
									informationHtml = "";
									informationHtml += "<div class='alert alert-error'>"
											+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
											+ "<h4>失败!</h4> <strong>该作者信的名称已经存在，请核对信息再进行!</strong>";
								}
							} else {
								informationHtml = "";
								informationHtml += "<div class='alert alert-error'>"
										+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
										+ "<h4>失败!</h4> <strong>作者信息操作失败!</strong>";
							}
							informationHtml += "</div>";
							$('#pageInfo').empty().append(informationHtml);
							site.showData();
							return;
						});
						$("#modal").modal("hide");
					});
		},
		init : function() {
			// 初始化showMenu
			this.showMenu();
		}
	};
	site.init();
});