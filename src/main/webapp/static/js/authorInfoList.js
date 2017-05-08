var authorInfo = {};
$(function() {
	authorInfo = {
		url : "",
		iDate : {
			authorId : 0,
			siteId:0,
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
		parseData:function(){
			var searchWord=getRequest("searchWord");
			searchWord=unescape(searchWord);
			if(searchWord=='null')searchWord="";
			var area=getRequest("area");
			area=unescape(area);
			if(area=='null') area="";
			var siteId=getRequest("siteId");
			$("#searchWord").val(searchWord);
			$("#Autharea").val(area);
			$("#site").val(siteId);
			authorInfo.iDate.siteId=siteId;
		},
		showMenu : function() {
			$("#topMenu").find('li').each(function() {
				$(this).removeClass();
			});
			var parentColuId = getRequest("firstColuId");
			this.siteId=getRequest("siteId");
			if(this.siteId==undefined)this.siteId=0;
			$("#firstColuId" + parentColuId + "").attr("class", "active"); // 添加菜单选中样式
			$("#pagination").empty();
		},
		/**
		 * 头部header逻辑
		 * 
		 * @param 参数列表
		 */
		showData : function(field,desc) {
			viewaction = '../../handler/author/viewAllAuthor';
			paginationPage();// 键入页码，分页等标识
			var searchWord ="";
			var Autharea="";
			if ($("#searchWord").val() != '') {
				searchWord = $("#searchWord").val();
			}
			if ($("#Autharea").val() != '') {
				Autharea = $("#Autharea").val();
			}
			var siteId=$("#site").val();
			params = {
				"pageArray" : new Array(),
				"recordPerPage" : 20,
				"field":field,
				"desc":desc,
				"searchWord":searchWord,
				"Autharea":Autharea,
				"siteId":siteId
			};
			initialBind();// 绑定分页的一些操作响应
			initSearch();
		},
		showAllSite:function(){
			var url="../../handler/site/selectSite";
			var html="<option value='0'>所有网站</option>";
			$.post(url,{},function(data){
				if(data.ret){
					$.each(data.data.result,function(index,item){
						if(item.siteId==authorInfo.iDate.siteId){
							html+="<option value='"+item.siteId+"' selected='selected'>"+item.siteName+"</option>";
						}else{
							html+="<option value='"+item.siteId+"'>"+item.siteName+"</option>";
						}
					});
					$("#site").empty().append(html);
					
				}
			});
		},
		searchData : function(field,desc) {
			$("#searchButton").click(function() {
				var searchWord = "";
				var Autharea="";
				if ($("#searchWord").val() != '') {
					searchWord = $("#searchWord").val();
				}
				if ($("#Autharea").val() != '') {
					Autharea = $("#Autharea").val();
				} 
				var siteId=$("#site").val();
				viewaction = '../../handler/author/viewAllAuthor';
				paginationPage();// 键入页码，分页等标识
				params = {
					"pageArray" : new Array(),
					"recordPerPage" : 20,
					"searchWord" : searchWord,
					"field":field,
					"desc":desc,
					"Autharea":Autharea,
					"siteId":siteId
				};
				initialBind();// 绑定分页的一些操作响应
				initSearch();
			});
		},
		sortclick:function(){
			var obj=this;
			$(".sortclick").click(function(){
				if($(this).attr("class")=="icon-arrow-down sortclick"){
					$(".sortclick").attr("class","icon-arrow-down sortclick");
					$(this).attr("class","icon-arrow-up sortclick");
					obj.showData($(this).attr("id"),1);
				}
				else {
					$(".sortclick").attr("class","icon-arrow-down sortclick");
					$(this).attr("class","icon-arrow-down sortclick");
					obj.showData($(this).attr("id"),0);
				}
			});
		},
		init : function() {
			// 初始化showMenu
			this.parseData();
			this.showMenu();
			this.showAllSite();
			this.showData(1,1);
			this.searchData(1,1);
			this.sortclick();
		}
	};
	authorInfo.init();
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
			/*var gender = '';
			if (item.gender == 2) {
				gender = '女';
			} else if (item.gender == 1) {
				gender = '男';
			}*/
			var url = '';
			if (item.authUrl != null && item.authUrl != "") {
				url = item.authUrl;
			}
			var inTime = '';
			if (item.authInTime != null && item.authInTime != "") {
				inTime = item.authInTime;
			}
			var area = '';
			if (item.authArea != null && item.authArea != "") {
				area = item.authArea;
			}
			var description = '';
			if (item.authDesc != null && item.authDesc != "") {
				description = item.authDesc;
			}
			if(item.authWorksHitsNum<=-1){
				item.authWorksHitsNum="无";
			}
			if(item.authWorksRecomsNum<=-1){
				item.authWorksRecomsNum="无";
			}
			if(item.authWorksCommentsNum<=-1){
				item.authWorksCommentsNum="无";
			}
			if(item.authGender==null || item.authGender==undefined){
				item.authGender='';
			}
			var description1=titleFormat(description,9);
			info = item.authName + "$" + url + "$" + item.authGender + "$" + area + "$" + description + "$" + inTime;
			iHtml += "<tr class=''><td class='tdcenter'>" + startIndex + "</td>" + "<td class='tdcenter'><a id='"
					+ item.authId + "' name='" + info + "'  href='authorDetail.html?firstColuId=2&authorId="
					+ item.authId + "'>" + item.authName + "</a>" + "</td><td class='tdcenter'>" + item.authGender
					+ "</td><td class='tdcenter'>" + area + "</td><td class='tdcenter' title='"+description+"'>"+description1
					+ "</td><td class='tdcenter'>" +item.wesiName+ "</td><td class='tdcenter'>"
					+ item.worksCount + "</td><td class='tdcenter'>" + transforms(item.authWorksHitsNum) + "</td><td class='tdcenter'>"
					+transforms(item.authWorksCommentsNum) + "</td><td class='tdcenter'>" + transforms(item.authWorksRecomsNum)
					+ "</td><td class='tdcenter'><a title='" + url + "' href='" + url
					+ "'target='_blank'><i class='icon-globe'></i></a></td></tr>";
			startIndex = startIndex + 1;
		});
		$('#iData').append(iHtml);
	}
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
/**
 * 数据转换 */

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
