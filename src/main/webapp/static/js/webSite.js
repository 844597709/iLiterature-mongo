$(document).ready(function() {
	var webSite = {
			site : {
				siteId : 0,
				siteName : "",
				homePage : "",
				worksInfo : "",
				authorInfo : "",
				concernDegree : "",
			},
			/**
			 * 头部header逻辑
			 * 
			 * @param 参数列表
			 */
			showMenu : function() {
				var coluId = getRequest("secondColuId");
				$("#coluId" + coluId + "").attr("class", "active"); // 添加菜单选中样式
			},
			/**
			 * 头部header逻辑
			 * 
			 * @param 参数列表
			 */
			showData : function() {
				viewaction = '../../handler/webSite/viewAllSite';
				paginationPage();// 键入页码，分页等标识
				params = {
						"pageArray" : new Array(),
						"recordPerPage" : 20
				};
				initialBind();// 绑定分页的一些操作响应
				initSearch();
				// 初始化editSite
				webSite.editSite(this.site);
				// 初始化delSite
				webSite.delSite();
			},
			/**
			 * 头部header逻辑
			 * 
			 * @param 参数列表
			 */
			addSite : function(site) {
				$("#addButton").click(function() {
					clearText();
					$('#tModalLabel').html("添加网站");
					$('#modalInfo').empty();
					webSite.add(site);
				});
			},
			add:function(site){
				$("#saveSite").one("click",function(){
					var url = '../../handler/webSite/addSite';
					site.siteName = $('#siteName').val();
					site.homePage = $('#homePage').val();
					site.worksInfo = $('#worksInfo').val();
					site.authorInfo = $('#authorInfo').val();
					site.concernDegree = $('#concernDegree').val();
					if (site.siteName.length <= 0 || site.homePage.length <= 0
							|| site.worksInfo.length <= 0 || site.authorInfo.length <= 0) {
						var informationHtml = "";
						informationHtml += "<div class='alert alert-info'>"
							+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
							+ "<h4>注意!</h4> <strong>网站信息填写不全，请查看所填内容是否完整!</strong>";
						$('#modalInfo').append(informationHtml);
						webSite.add(site);
						return false;
					}
					$.post(url,site,function(data) {
						var informationHtml = "";
						if (data.ret) {
							if (data.data.result == "success") {
								informationHtml += "<div class='alert alert-success'>"
									+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
									+ "<h4>成功!</h4> <strong>用戶添加成功!</strong>";
							} else if (data.data.result == "fail") {
								informationHtml = "";
								informationHtml += "<div class='alert alert-error'>"
									+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
									+ "<h4>失败!</h4> <strong>用戶添加失败!</strong>";
							}
						} else {
							informationHtml = "";
							informationHtml += "<div class='alert alert-error'>"
								+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
								+ "<h4>失败!</h4> <strong>用戶添加失败!</strong>";
						}
						informationHtml += "</div>";
						$('#pageInfo').empty().append(informationHtml);
					});
					webSite.showData();
					$("#siteModal").modal("hide");
				});
			},
			/**
			 * 头部header逻辑
			 * 
			 * @param 参数列表
			 */
			editSite :function (site) {
				$(".edit").one("click",function(){
					var obj = $(this);
					var siteInfo = '';
					siteInfo = obj.attr("name");
					var infos = siteInfo.split("$");
					$('#tModalLabel').html("修改网站");
					$('#modalInfo').empty();
					site.siteId = obj.attr("id");
					$('#siteName').val(infos[0]);
					$('#homePage').val(infos[1]);
					$('#worksInfo').val(infos[2]);
					$('#authorInfo').val(infos[3]);
					$('#concernDegree').val(infos[4]);
					webSite.edit(site);
				});
			},
			edit : function(site) {
				$("#saveSite").one("click",function(){
					var url = '../../handler/webSite/editSite';
					site.siteName = $('#siteName').val();
					site.homePage = $('#homePage').val();
					site.worksInfo = $('#worksInfo').val();
					site.authorInfo = $('#authorInfo').val();
					site.concernDegree = $('#concernDegree').val();
					if (site.siteId < 0 || site.siteName.length <= 0 || site.homePage.length <= 0
							|| site.worksInfo.length <= 0 || site.authorInfo.length <= 0) {
						var informationHtml = "";
						informationHtml += "<div class='alert alert-info'>"
							+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
							+ "<h4>注意!</h4> <strong>网站信息填写不全，请查看所填内容是否完整!</strong>";
						$('#modalInfo').append(informationHtml);
						webSite.edit(site);
						return false;
					}
					$.post(url, site, function(data) {
						var informationHtml = "";
						if (data.ret) {
							if (data.data.result == "success") {
								informationHtml += "<div class='alert alert-success'>"
									+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
									+ "<h4>成功!</h4> <strong>修改网站信息成功!</strong>";
							} else if (data.data.result == "fail") {
								informationHtml = "";
								informationHtml += "<div class='alert alert-error'>"
									+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
									+ "<h4>失败!</h4> <strong>修改网站信息失败!</strong>";
							}
						} else {
							informationHtml = "";
							informationHtml += "<div class='alert alert-error'>"
								+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
								+ "<h4>失败!</h4> <strong>修改网站信息失败!</strong>";
						}
						informationHtml += "</div>";
						$('#pageInfo').empty().append(informationHtml);
						webSite.showData();
					});
					$("#siteModal").modal("hide");
				});
			},
			delSite : function() {
				$("a[id^='del'").one("click",function(){
					var siteId = 0;
					siteId = $(this).attr("name");
					console.log(siteId);
					var url = '../../handler/webSite/delSite';
					$.post(url,{"siteId" : siteId},function(data) {
						var informationHtml = "";
						if (data.ret) {
							if (data.data.result == "success") {
								informationHtml += "<div class='alert alert-success'>"
									+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
									+ "<h4>成功!</h4> <strong>删除网站成功!</strong>";
							} else if (data.data.result == "fail") {
								informationHtml = "";
								informationHtml += "<div class='alert alert-error'>"
									+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
									+ "<h4>失败!</h4> <strong>删除网站失败!</strong>";
							}
						} else {
							informationHtml = "";
							informationHtml += "<div class='alert alert-error'>"
								+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
								+ "<h4>失败!</h4> <strong>删除网站失败!</strong>";
						}
						informationHtml += "</div>";
						$('#pageInfo').empty().append(informationHtml);
					});
					webSite.showData();
				});
			},
			init : function() {
				// 初始化showMenu
				this.showMenu();
				// 初始化showData
				this.showData();
				// 初始化addSite
				this.addSite(this.site);
			}
	};
	webSite.init();
});
//加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	$("#siteData").empty();
	var siteHtml = "";
	if (pageRecords.data.length == 0) {
		$('#siteData').append(
				"<tr><td colspan=\"10\" class=\"tdcenter\">"
				+ "<i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>");
	} else {
		$.each(pageRecords.data, function(itemIndex, item) {
			var siteInfo = '';
			var concernDegree = '一般关注';
			if (item.concernDegree == 2) {
				concernDegree = '比较关注';
			} else if (item.concernDegree == 3) {
				concernDegree = '特别关注';
			}
			siteInfo = item.siteName + "$" + item.homePage + "$" + item.worksInfo + "$" + item.authorInfo + "$"
			+ item.concernDegree;
			siteHtml += "<tr class=''><td class='tdcenter'>" + startIndex + "</td>" + "<td class='tdcenter'><a id='"
			+ item.siteId + "' name='" + siteInfo + "' class='edit' data-toggle='modal' href='#siteModal'>"
			+ item.siteName + "</a>" + "</td><td class='tdcenter'>" + item.homePage
			+ "</td><td class='tdcenter'>" + item.worksInfo + "</td><td class='tdcenter'>" + item.authorInfo
			+ "</td><td class='tdcenter'>" + concernDegree + "</td><td class='tdcenter'><a href='' id='del"
			+ item.siteId + "' class='action btn-del' name='" + item.siteId + "'></a>" + "</td></tr>";
			startIndex = startIndex + 1;
		});
		$('#siteData').append(siteHtml);
	}
	return ;
}