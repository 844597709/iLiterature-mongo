$(function() {
	var siteInfo = {
		/**
		 * 头部header逻辑
		 * 
		 * @param 参数列表
		 */
		parseData:function(){
			var searchWord=getRequest("searchWord");
			searchWord=unescape(searchWord);
			if(searchWord=='null')searchWord="";
			$("#searchWord").val(searchWord);
		},
		showMenu : function() {
			var coluId = getRequest("secondColuId");
			var parentColuId = getRequest("firstColuId");
			$("#topMenu").find('li').each(function() {
				$(this).removeClass();
			});
			$("#coluId" + coluId + "").attr("class", "active"); // 添加菜单选中样式
			$("#firstColuId" + parentColuId + "").attr("class", "active"); // 添加菜单选中样式
		},
		showData : function() {
			viewaction = '../../handler/site/viewAuthorAndWorkNum';
			paginationPage();// 键入页码，分页等标识
			//--zd--
			var searchWord ="";
			if ($("#searchWord").val() != null || $("#searchWord").val() != "") {
				searchWord = $("#searchWord").val();
			}
			//--至此--
			params = {
				"pageArray" : new Array(),
				"recordPerPage" : 20,
				"searchWord":searchWord,
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
		searchData:function(){
			$("#searchButton").click(function(){
				var searchWord="";
				if($("#searchWord").val()!=null || $("#searchWord").val()!=""){
					searchWord=$("#searchWord").val();
				}
				viewaction = '../../handler/site/viewAuthorAndWorkNum';
				paginationPage();// 键入页码，分页等标识
				params = {
						"pageArray" : new Array(),
						"recordPerPage" : 20,
						"searchWord" : searchWord,
					};
					initialBind();// 绑定分页的一些操作响应
					initSearch();
			});
		},
		init : function() {
			this.parseData();
			// 初始化showMenu
			this.showMenu();
			// 初始化showData
			this.showData();
			this.searchData();
		}
	};
	siteInfo.init();
});
//加载数据到数据区域
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
			if(item.authorUpdate == '0'){
				item.authorUpdate = '';
			}
			iHtml += "<tr class=''><td class='tdcenter'>" + startIndex + "</td>" + "<td class='tdcenter'><a id='"
					+ item.siteId + "' class='edit' href='"+item.seedUrl+"' target='_black'>"
					+ item.siteName + "</a>" + "</td><td class='tdcenter'>" + item.seedUrl
					+ "</td><td class='tdcenter'>" + item.totalAuthors + "</td><td class='tdcenter'><a href='authorInfo.html?firstColuId=2&siteId=" + item.siteId
					+ "''><i class='icon-globe'></i></a><td class='tdcenter'>" + item.totalWorks + "</td></td>"
					+ "<td class='tdcenter'><a href='workInfo.html?firstColuId=3&siteId=" + item.siteId
					+ "'><i class='icon-globe'></i></a></td><td class='tdcenter'>" + item.authorUpdate.substring(0,19) + "</td></tr>";
			startIndex = startIndex + 1;
		});
		$('#iData').append(iHtml);
	}
	return;
}