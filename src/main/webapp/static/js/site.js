$(function() {
	var site = {
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
			viewaction = '../../handler/site/viewAllSite';
			paginationPage();// 键入页码，分页等标识
			params = {
				"pageArray" : new Array(),
				"recordPerPage" : 20
			};
			initialBind();// 绑定分页的一些操作响应
			initSearch();
			$('.iPopover').popover({
				toggle : 'popover',
				placement : 'top',
				html : 'true',
				trigger : 'hover'
			});
		},
		delDate : function() {
			$("a[id^='del'").one(
					"click",
					function() {
						var authorId = 0;
						authorId = $(this).attr("name");
						var url = '../../handler/site/delSite';
						$.post(url, {
							"authorId" : authorId
						}, function(data) {
							var informationHtml = "";
							if (data.ret) {
								if (data.data.result == "success") {
									informationHtml += "<div class='alert alert-success'>"
											+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
											+ "<h4>成功!</h4> <strong>删除作者成功!</strong>";
									site.showData();
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
		init : function() {
			// 初始化showMenu
			this.showMenu();
			// 初始化showData
			this.showData();
		}
	};
	site.init();
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
			var crawInfo = '作者信息 : ' + item.authorUpdate + "</br> 作品信息 : " + item.worksUpdate + " </br>其他信息 : "
					+ item.extraWorksInfo;
			var filter = '网页过滤 : ' + item.innerFilter + "</br> 作者过滤 : " + item.anthorFilter + " </br>作品过滤 : "
					+ item.worksFilter + "</br> 抽取文件 : " + item.extractFile;
			iHtml += "<tr class=''><td class='tdcenter'>" + startIndex + "</td>" + "<td class='tdcenter'><a id='"
					+ item.siteId + "' class='edit' href='siteEdit.html?firstColuId=6&secondColuId=11&siteId="
					+ item.siteId + "'>" + item.siteName + "</a>" + "</td><td class='tdcenter'>" + item.seedUrl
					+ "</td><td class='tdcenter'>" + item.encode + "</td><td class='tdcenter'>" + item.currencyUnit
					+ "</td><td class='tdcenter'><a href='#' class='iPopover' title='获取信息详情' data-content='" + crawInfo
					+ "'>" + item.authorUpdate
					+ "</a></td><td class='tdcenter'><a href='#' class='iPopover' title='过滤规则' data-content='" + filter
					+ "'>" + item.innerFilter.substr(0, 16) + "</a></td><td class='tdcenter'><a href='#' id='del"
					+ item.siteId + "' class='action btn-del' name='" + item.siteId + "'></a>" + "</td></tr>";
			startIndex = startIndex + 1;
		});
		$('#iData').append(iHtml);
	}
	return;
}