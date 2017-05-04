var worksInfo = {};
$(function() {
	worksInfo = {
		url : "",
		site: "",
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
			site=getRequest("siteId");
			site=unescape(site);
			if(site==null)site=0;
			$("#siteId").val(site);		
		},
		showMenu : function() {
			$("#topMenu").find('li').each(function() {
				$(this).removeClass();
			});
			var parentColuId = getRequest("firstColuId");
			/*this.siteId=getRequest("siteId");
			if(this.siteId==undefined)this.siteId=0;*/
			$("#firstColuId" + parentColuId + "").attr("class", "active"); // 添加菜单选中样式
			$("#pagination").empty();
		},
		/**
		 * 头部header逻辑
		 * 
		 * @param 参数列表
		 */
		//搜索功能
		showData : function(parameter, orderDesc) {
			viewaction = '../../handler/worksInfo/viewAllWorksInfo';
			paginationPage();// 键入页码，分页等标识
			var searchWord ="";	
			var siteId="";
			if ($("#searchWord").val() != null || $("#searchWord").val() != "") {
				searchWord = $("#searchWord").val();
			}			
				siteId = site;  		
			params = {
				"pageArray" : new Array(),
				"recordPerPage" : 20,
				"searchWord":searchWord,
				"siteId":siteId,
				"parameter":parameter,
				"orderDesc":orderDesc,				
			};
			initialBind();// 绑定分页的一些操作响应
			initSearch();
		},
		searchData : function(parameter, orderDesc) {
			$("#searchButton").click(function() {
				var searchWord = "";	
				var siteId="";
				if ($("#searchWord").val() != null || $("#searchWord").val() != "") {
					searchWord = $("#searchWord").val();
				}
		        if ($("#siteId").val() != null) {
					siteId = $("#siteId").val();
				}
		        else siteId=0;
				viewaction = '../../handler/worksInfo/viewAllWorksInfo';
				paginationPage();// 键入页码，分页等标识
				params = {
					"pageArray" : new Array(),
					"recordPerPage" : 20,
					"searchWord" : searchWord,
					"siteId":siteId,
					"parameter":parameter,
					"orderDesc":orderDesc,
				};
				initialBind();// 绑定分页的一些操作响应
				initSearch();
			});
		},
		/*网站列表的下拉框*/
		showSite:function(){
			var myUrl = '../../handler/site/selectSite';
			var infoHtml='';
			$.post(myUrl, {
			},function(data) {				
				if (data.ret) {
					infoHtml="<option value='0'>所有网站</option>";
					myData = data.data.result;
					$.each(myData, function(entryIndex, entry) {
						if(site==entry.siteId)
						{
							infoHtml+="<option selected value="+entry.siteId+'>'+entry.siteName+"</option>";
						}
							else infoHtml+="<option value="+entry.siteId+">"+entry.siteName+"</option>";
					});
					$("#siteId").empty().append(infoHtml);
				}
			});		
		},
		//点击排序函数
		sortclick : function (){
			var obj=this;
			$(".sortclick").click(function(){
				if($(this).attr("class")=="icon-arrow-down"){
					$(this).attr("class","icon-arrow-up");
					obj.showData($(this).attr("id"),1);
				}
				else {
					$(this).attr("class","icon-arrow-down");
					obj.showData($(this).attr("id"),0);
				}
			});
		},
		init : function() {
			// 初始化showMenu
			this.parseData();       //获取URL中传过来的关键字参数
			this.showMenu();       //显示菜单栏
			this.showData(1, 1);     //显示数据
			this.searchData(1, 1);  //搜索功能
			this.showSite();       //网站的下拉列表框
			this.sortclick();       //点击排序函数
		}
	};
	worksInfo.init();
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
			var info = '';
			var url = '';
			if (item.url != null && item.url != "") {
				url = item.url;
			}
			var sTime = '';
			if (item.sTime != null && item.sTime != "") {
				sTime = item.sTime;
			}
			if(item.totalHits<=0)item.totalHits="无";
			if(item.commentsNum<=0)item.commentsNum="无";
			if(item.totalRecoms<=0)item.totalRecoms="无";
			info = item.worksName + "$" + url + "$" + item.description + "$" + sTime;
			iHtml += "<tr><td class='tdcenter'>" + startIndex + "</td>" + "<td class='tdcenter'><a id='" + item.workId
					+ "' name='" + info + "'  href='workDetail.html?firstColuId=3&workId=" + item.workId + "'>"
					+ item.title + "</a>" + "</td><td class='tdcenter'>" + item.author + "</td><td class='tdcenter'>"
					+ item.type + "</td><td class='tdcenter' title='" + item.crwsSiteName + "'>"
					+ item.crwsSiteName+  "</td><td class='tdcenter'>" + transforms(item.totalHits)
					+ "</td><td class='tdcenter'>"+transforms(item.commentsNum) +"</td><td class='tdcenter'>" + transforms(item.totalRecoms)
					+"</td><td class='tdcenter'>" + sTime.substr(0, 16) + "</td><td class='tdcenter'><a title='" + url
					+ "' href='" + url + "'target='_blank'><i class='icon-globe'></i></a></td></tr>";
			startIndex = startIndex + 1;
		});
		$('#iData').append(iHtml);
		$("[rel=popover]").popover({
			placement : 'top',
			trigger : 'hover',
			html : 'true', // needed to show html of course
		});
	}
	return;
}
/**
 * 数据转换
 */
 function  transforms(number){
	var chaneseNumber;
	var name = number.toString();
	nameLength = name.length;
	if(nameLength < 5){
		chaneseNumber = number;
	}
	else{
		var i,j;
		i = parseInt(number/10000);
		j = i.toString();
		chaneseNumber = j +"万";
	}
	return chaneseNumber;
}
