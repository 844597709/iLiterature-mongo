var author = {};
$(function() {
	author = {
		url : "",
		iDate : {
			authorId : 0,
			authorName : "",
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
		showData : function() {
			viewaction = '../../handler/author/viewAllAuthor';
			paginationPage();// 键入页码，分页等标识
			params = {
				"pageArray" : new Array(),
				"recordPerPage" : 20,
				"field":1,
				"desc":1
			};
			initialBind();// 绑定分页的一些操作响应
			initSearch();
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
				var url = '../../handler/author/addAuthor';
				// 初始化saveData
				author.saveData(iDate, url);
			});
		},
		/**
		 * 头部header逻辑
		 * 
		 * @param 参数列表
		 */
		editDate : function(iDate) {
			$(".edit").one("click", function() {
				clearText();
				var obj = $(this);
				var siteInfo = '';
				siteInfo = obj.attr("name");
				var infos = siteInfo.split("$");
				$('#tModalLabel').html("修改作者");
				var url = '../../handler/author/editAuthor';
				iDate.authorId = obj.attr("id");
				$('#authorName').val(infos[0]);
				$('#url').val(infos[1]);
				$('#gender').val(infos[2]);
				$('#area').val(infos[3]);
				$('#description').val(infos[4]);
				$('#inTime').val(infos[5]);
				// 初始化saveData
				author.saveData(iDate, url);
			});
		},
		saveData : function(iDate, url) {
			$("#saveBUtton").one(
					"click",
					function() {
						$('#pageInfo').empty();
						iDate.authorName = $('#authorName').val();
						iDate.url = $('#url').val();
						iDate.gender = $('#gender').val();
						iDate.area = $('#area').val();
						iDate.description = $('#description').val();
						iDate.inTime = $('#inTime').val();
						if (iDate.authorName.length <= 0) {
							var informationHtml = "";
							informationHtml += "<div class='alert alert-info'>"
									+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
									+ "<h4>注意!</h4> <strong>作者信息填写不全，请查看所填内容是否完整!</strong>";
							$('#modalInfo').append(informationHtml);
							// 初始化saveData
							author.saveData(iDate, url);
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
							author.showData();
							return;
						});
						$("#modal").modal("hide");
					});
		},
		delDate : function() {
			$("a[id^='del'").one(
					"click",
					function() {
						var authorId = 0;
						authorId = $(this).attr("name");
						var url = '../../handler/author/delAuthor';
						$.post(url, {
							"authorId" : authorId
						}, function(data) {
							var informationHtml = "";
							if (data.ret) {
								if (data.data.result == "success") {
									informationHtml += "<div class='alert alert-success'>"
											+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
											+ "<h4>成功!</h4> <strong>删除作者成功!</strong>";
									author.showData();
								} else if (data.data.result == "fail") {
									informationHtml = "";
									informationHtml += "<div class='alert alert-error'>"
											+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
											+ "<h4>失败!</h4> <strong>删除作者失败!</strong>";
								}
							} else {
								informationHtml = "";
								informationHtml += "<div class='alert alert-error'>"
										+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
										+ "<h4>失败!</h4> <strong>删除作者失败!</strong>";
							}
							informationHtml += "</div>";
							$('#pageInfo').empty().append(informationHtml);
						});
					});
		},
		showTime : function() {
			$('#inTime').datetimepicker({
				autoclose : true,
				todayBtn : true,
				minView : 2
			});
		},
		init : function() {
			// 初始化showMenu
			this.showMenu();
			// 初始化showData
			this.showData();
			// 初始化showTime
			this.showTime();
			// 初始化addDate
			this.addDate(this.iDate);
		}
	};
	author.init();
});
// 加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	$("#iData").empty();
	var iHtml = "";
	if (pageRecords.data.length == 0) {
		$('#iData').append(
				"<tr><td colspan=\"10\" class=\"tdcenter\">"
						+ "<i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>");
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			var info = '';
			var gender = '';
			if (item.gender == 2) {
				gender = '女';
			} else if (item.gender == 1) {
				gender = '男';
			}
			var url = '';
			if (item.url != null && item.url != "") {
				url = item.url;
			}
			var inTime = '';
			if (item.inTime != null && item.inTime != "") {
				inTime = item.inTime;
			}
			var area = '';
			if (item.area != null && item.area != "") {
				area = item.area;
			}
			var description = '';
			if (item.description != null && item.description != "") {
				description = item.description;
			}
			var description1=titleFormat(description,9);
			info = item.authorName + "$" + url + "$" + gender + "$" + area + "$" + description + "$" + inTime;
			iHtml += "<tr class=''><td class='tdcenter'>" + startIndex + "</td>" + "<td class='tdcenter'><a id='"
					+ item.authorId + "' name='" + info + "' class='edit' data-toggle='modal' href='#modal'>"
					+ item.authorName + "</a>" + "</td><td class='tdcenter'><a title='" + url + "' href='" + url
					+ "'target='_blank'>" + url.substr(0, 30) + "</a></td><td class='tdcenter'>" + gender
					+ "</td><td class='tdcenter'>" + area + "</td><td class='tdcenter' title='"+description+"'>"+description1
					+ "</td><td class='tdcenter'>" + inTime.substr(0, 16)
					+ "</td><td class='tdcenter'><a href='' id='del" + item.authorId
					+ "' class='action btn-del' name='" + item.authorId + "'></a>" + "</td></tr>";
			startIndex = startIndex + 1;
		});
		$('#iData').append(iHtml);
	}
	window.author.editDate(window.author.iDate);
	window.author.delDate;
	return;
}
//格式化标题长度
function titleFormat(titlestr, length) {
	var r = /[^\x00-\xff]/g;
	var tmp_str = titlestr.replace(r, "**");
	length = length * 2;
	if (tmp_str.length > length) {
		var m = Math.floor(length / 2);
		for ( var i = m; i < titlestr.length; i++) {
			if (titlestr.substr(0, i).replace(r, "**").length >= (length - 1))
				return titlestr.substr(0, i) + "..";
		}
	}
	return titlestr;
}