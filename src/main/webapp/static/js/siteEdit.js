$(function() {
	var site = {
		url : "",
		iData : {
			siteId : 0
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
			this.iData.siteId = getRequest("siteId");
		},
		showDate : function() {
			var siteId = 0;
			siteId = this.iData.siteId;
			url = "../../handler/site/viewMySite";
			$.post(url, {
				"siteId" : siteId
			}, function(data) {
				if (data.ret && data.data != null) {
					var myData = data.data.iSite;
					$("#siteName").val(myData.siteName);
					$("#encode").val(myData.encode);
					$("#seedUrl").val(myData.seedUrl);
					$("#crawlStyle").val(myData.crawlStyle);
					$("#updateStyle").val(myData.updateStyle);
					$("#maxThread").val(myData.maxThread);
					$("#depth").val(myData.depth);
					$("#currencyUnit").val(myData.currencyUnit);
					$("#authorUpdate").val(myData.authorUpdate);
					$("#worksUpdate").val(myData.worksUpdate);
					$("#extraWorksInfo").val(myData.extraWorksInfo);
					$("#authAttrInWorks").val(myData.authAttrInWorks);
					$("#innerFilter").val(myData.innerFilter);
					$("#anthorFilter").val(myData.anthorFilter);
					$("#worksFilter").val(myData.worksFilter);
					$("#extractFile").val(myData.extractFile);
				}
			});
		},
		editDate : function() {
			$("#saveBUtton").click(
					function() {
						var iDate = {};
						iDate.siteId = getRequest("siteId");
						iDate.siteName = $('#siteName').val();
						iDate.encode = $('#encode').val();
						iDate.seedUrl = $('#seedUrl').val();
						iDate.crawlStyle = $('#crawlStyle').val();
						iDate.updateStyle = $('#updateStyle').val();
						iDate.maxThread = $('#maxThread').val();
						iDate.depth = $('#depth').val();
						iDate.currencyUnit = $('#currencyUnit').val();
						iDate.authorUpdate = $('#authorUpdate').val();
						iDate.worksUpdate = $('#worksUpdate').val();
						iDate.extraWorksInfo = $('#extraWorksInfo').val();
						iDate.authAttrInWorks = $('#authAttrInWorks').val();
						iDate.innerFilter = $('#innerFilter').val();
						iDate.anthorFilter = $('#anthorFilter').val();
						iDate.worksFilter = $('#worksFilter').val();
						iDate.extractFile = $('#extractFile').val();
						if (iDate.siteName.length <= 0 || iDate.encode.length <= 0 || iDate.seedUrl.length <= 0) {
							var informationHtml = "";
							informationHtml += "<div class='alert alert-info'>"
									+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
									+ "<h4>注意!</h4> <strong>信息填写不全，请查看所填内容是否完整!</strong>";
							$('#modalInfo').append(informationHtml);
							return false;
						}
						var url = '../../handler/site/editSite';
						$.post(url, iDate, function(data) {
							var informationHtml = "";
							if (data.ret) {
								if (data.data.result == "success") {
									informationHtml += "<div class='alert alert-success'>"
											+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
											+ "<h4>成功!</h4> <strong>操作成功!</strong>";
								}
							} else {
								informationHtml = "";
								informationHtml += "<div class='alert alert-error'>"
										+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
										+ "<h4>失败!</h4> <strong>作者信息操作失败!</strong>";
							}
							informationHtml += "</div>";
							$('#pageInfo').empty().append(informationHtml);
							return;
						});
					});
		},
		init : function() {
			// 初始化showMenu
			this.showMenu();
			// 初始化showDate
			this.showDate();
			// 初始化editDate
			this.editDate();
		}
	};
	site.init();
});