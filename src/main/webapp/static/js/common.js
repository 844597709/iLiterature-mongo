var secondColData = new Object();
var option = "";
var userId = 0;
var editType = 0;
var fristPage = 0;// 初始页码
var footerInfo = "";// 页脚信息
var coluDate;
var role=1;
var userInfo = {
		userId : "",
		userName : "",
		password : "",
		userTrueName : "",
		jobNum : "",
		department : "",
		position : "",
		contactNum : "",
		email : "",
		verify : "",
		rejectReason : "",
		role : "",
		time : ""
};
//全局权限判断
$(document).ajaxComplete(function(event, XMLHttpRequest, settings) {
	/*if (XMLHttpRequest.status == "602" || XMLHttpRequest.status == "404") {
		window.location = "login.html";
		return false;
	}*/
});
$(document).ready(function() {
	// cookie 获取指定名称的cookie的值
	var cookies = document.cookie;
	if (cookies) {
		var cookie = cookies.split("; ");
		for ( var i = 0; i < cookie.length; i++) {
			var temp = cookie[i].split("=");
			if (temp[0] == "user") {
				var iCookie = temp[1].split("$");
				userInfo.userId = iCookie[0];
				userInfo.userName = unescape(iCookie[1]);
				userInfo.password = unescape(iCookie[2]);
				userInfo.userTrueName = unescape(iCookie[3]);
				userInfo.jobNum = unescape(iCookie[4]);
				userInfo.department = unescape(iCookie[5]);
				userInfo.position = unescape(iCookie[6]);
				userInfo.contactNum = unescape(iCookie[7]);
				userInfo.email = unescape(iCookie[8]);
				userInfo.verify = iCookie[9];
				userInfo.rejectReason = unescape(iCookie[10]);
				userInfo.role = iCookie[11];
				userInfo.time = unescape(iCookie[12]);
			}
			if (temp[0] == "system") {
				var sysCookie = temp[1].split("$");
				fristPage = sysCookie[0];
				footerInfo = unescape(sysCookie[1]);
			}
		}
		showFooter();
		getSysParam();
	} else {
		getSysParam();
	}
	if (document.URL.indexOf("login.html") < 0) {
		getnavbar();
		showLocation();
		moduserHtml();
		showUserInfo();
	}
	$("#logout").click(function() {// 退出
		$.post("../../handler/user/logout", function(data) {
			// 删除cookie
			document.cookie = "user=;expires=" + (new Date(0)).toGMTString();
			alert("退出成功");
		});
	});
	// option button 用户信息修改
	$("#saveButton").click(function() {
		if (option == 'add') {
			addUser();
		} else if (option = "edit") {
			editUser();
		}
	});

});

function getSysParam() {
	// 请求两个参数信息
	$.post("../../handler/systemparameter/querySysParam", function(data) {
		getdata = data.data.data;
		$.each(getdata, function(entryIndex, entry) {
			if (entry.sypaId == 1) {
				fristPage = entry.sypaValue;
			}
			if (entry.sypaId == 2) {
				footerInfo = entry.sypaValue;
			}
			// add cookie
			var str = "system=" + fristPage + "$" + escape(footerInfo);
			var date = new Date();
			var ms = 24 * 3600 * 1000;
			date.setTime(date.getTime() + ms);
			str += "; expires=" + date.toGMTString();
			document.cookie = str;
		});
	});
}
function getcurrentfile() {
	var filestr = document.URL;
	if (filestr.search("html") == -1)
		filestr = "login";
	else
		filestr = filestr.substring(filestr.lastIndexOf('/') + 1, filestr.lastIndexOf('.'));
	coluurl = filestr;
	return filestr;
}
//显示一级导航
function getnavbar() {
	var currColuId = getRequest("firstColuId");
	$.ajax({
		type : "POST",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",// 发送信息至服务器时内容编码类型
		url : '../../handler/column/viewFirstMenu',
		async : false, // 需要同步请求数据
		dataType : 'json',
		success : function(data) {
			coluDate = data.data.column;
			var navbarMsg = "<ul class=\"nav\">";
			/*var filepre = getcurrentfile();
			if (filepre.search('-') != -1) {
				filepre = filepre.substring(0, filepre.search('-'));
			}*/
			$.each(coluDate, function(entrtIndex, entry) {
				if (entry.url != "login.html") {
					if (userInfo.role == 1) {
						var actstr = "";
						if (entry.id == currColuId/* || entry.url.indexOf(filepre) == 0*/) {
							actstr = "active";
						}
						navbarMsg += "<li id='firstColuId" + entry.id + "' class='" + actstr + "'><a href='"
						+ entry.url + "?firstColuId=" + entry.id + "'><i class='" + entry.icon
						+ " icon-white'></i>" + entry.name + "</a> </li>";
					}else {
						if(currColuId==4 || currColuId==6){
							window.history.go(-1);
						}
						var actstr = "";
						if(entry.id!=4 && entry.id!=6){
							if (entry.id == currColuId) {
								actstr = "active";
							}
							navbarMsg += "<li id='firstColuId" + entry.id + "' class='" + actstr + "'><a href='" + entry.url
							+ "?firstColuId=" + entry.id + "'><i class='" + entry.icon + " icon-white'></i>&nbsp;"
							+ entry.name + "</a> </li>";
						}
					}
				} 
			});
			navbarMsg = navbarMsg + "<li id='userinfo' class='nav nav-pills pull-right'></li></ul>";
			$("#navbar").append(navbarMsg);
		}
	});

	// 显示banner
	var bannerstr = "<div class='logotitle'>网络文学监测系统</div>";
	$("#logo").empty().append(bannerstr);
}

//获取url中的参数
function getRequest(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

//根据菜单类型构建对应的代码
function leftColumShow(parentId) {
	url = "../../handler/column/viewMenuById";
	$.post(url, {
		"parent" : parentId
	}, function(data) {
		if (data.ret && data.data.column != null) {
			var coluDate1 = data.data.column;
			var navbarMsg = "<ul class=\"nav nav-pills nav-stacked\">";
			$.each(coluDate1, function(entrtIndex, entry) {
				navbarMsg += "<li id=coluId" + entry.id + " class=''> <a href='" + entry.url + "?firstColuId="
				+ parentId + "&secondColuId=" + entry.id + "'>" + entry.name + "</a> </li>";
			});
			navbarMsg = navbarMsg + "</ul>";
			$("#topMenu").append(navbarMsg);
		}
	});
}
//获取url中的参数
function getRequest(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

function loadPage() {
	var fpage = "<div class=\"well\"><div class=\"container-fluid\"><table id='dataArea'"
		+ "class='table table-hover table-striped table-condensed'><thead><tr>"
		+ "<th width='33'><a href='#' id='chooseall'>全选</a></th><th>值</th>"
		+ "<th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;舆 情 摘 要 <span class='pull-right'>排序:<select id='listsort'"
		+ "class='selcombobox' style='height: 30px; width: 100px;'"
		+ "name='listsort' onchange='javascript:SearchByOrder(this);'>"
		+ "<option value='1' selected='selected'>按情感值</option>"
		+ "<option value='2'>按发表时间</option>"
		+ "<option value='3'>按采集时间</option>"
		+ "<option value='4'>按热度</option>"
		+ "<option value='5'>按相关度</option>"
		+ "</select></span>	</th></tr></thead><tbody></tbody></table></div></div><div class=\"btn-toolbar\"><div class=\"btn-group\"><button class=\"btn\">当前第</button><button id=\"currentPage\" class=\"btn\"></button>"
		+ "<button class=\"btn\">页</button></div><div class=\"btn-group\"><button class=\"btn\">共</button><button id=\"totalPage\" class=\"btn\"></button>"
		+ "<button class=\"btn\">页</button></div><div class=\"btn-group\"><button id=\"pagebackward\" class=\"btn\">上一页</button>"
		+ "<button id=\"pageforward\" class=\"btn\">下一页</button></div><div class=\"btn-group\"><button class=\"btn\">共</button><button id=\"recordCount\" class=\"btn\"></button>"
		+ "<button class=\"btn\">条</button></div><div class=\"btn-group\"><button class=\"btn\">每页</button><select id=\"recordPage\" class=\"span1 btn\">"
		+ "<option>10</option><option>20</option><option>30</option><option>40</option><option>50</option></select></div><div class=\"btn-group\"><input id=\"gotopage\" type=\"text\" class=\"input-mini btn\">"
		+ "<button id=\"goto\" class=\"btn\">跳转</button></div></div>";
	$("#levelone").append(fpage);
}
/**
 * 初始化select框
 * 
 * @param c
 * @param v
 */

function checkbyvalue(v) {
	for ( var i = 0; i < document.getElementById("recordPage").options.length; i++) {
		if (v == document.getElementById("recordPage").options[i].value) {
			document.getElementById("recordPage").options[i].selected = true;
		}
	}
}
function showLocation(){
	var firstColuId=getRequest("firstColuId");
	var secondColuId=getRequest("secondColuId");
	var iHtml="<ul class='breadcrumb'>"+
	  "<li><a href='literature.html?firstColuId=1'>首页</a> <span class='divider'>/</span></li>";
	$.each(coluDate,function(ItemIndex,item){
		if(item.id==firstColuId){
			iHtml+= "<li><a href='"+item.url+"?firstColuId="+item.id+"'>"+item.name+"</a>";
			var url = "../../handler/column/viewMenuById";
			$.post(url, {
				"parent" : firstColuId
			}, function(data) {
				if (data.ret && data.data.column != null) {
					var coluDate1 = data.data.column;
					$.each(coluDate1, function(entrtIndex, entry) {
						if(secondColuId==entry.id){
							iHtml+= "<span class='divider'>/</span></li><li><a href='"+entry.url+"?firstColuId="+item.id+"&secondColuId="+entry.id+"'>"+entry.name+"</a>";
						}
					});
				}
				else if(data.ret==false) {
					/*$.post("../../handler/user/logout", function(data) {
						// 删除cookie
						document.cookie = "user=;expires=" + (new Date(0)).toGMTString();
					});*/
					alert("您的权限不足，不能访问此页面，现为您跳转至首页");
					window.location.href="literature.html?firstColuId=1";
				}
			});
		}
	});
	iHtml+="</li></ul>";
	$("#location").empty().append(iHtml);
}
function showFooter() {
	$.ajaxSettings.async = false;
	var footerHtml = "";
	var currdate = new Date();
	var copydate = "2015";
	if (currdate.getFullYear() != copydate)
		copydate = copydate + " - " + currdate.getFullYear();
	footerHtml = footerInfo + "";
	$("#footer").append(footerHtml);
	return;
}
//清空文本框
function clearText() {
	var text = document.getElementsByTagName("input");
	var textarea = document.getElementsByTagName("textarea");
	for ( var i = 0; i < text.length; i++) {
		if (text[i].type == "text" || text[i].type == "password") {
			text[i].value = '';
		}
	}
	for ( var j = 0; j < textarea.length; j++) {
		textarea[j].value = " ";
	}
	//默认用户有效时间为60天
	$("#time").val(60);
}
//分页内容
function paginationPage() {
	var pageHtml = "<div class=\"btn-group\"><button class=\"btn\">第</button><button id=\"currentPage\" class=\"btn\"></button><button class=\"btn\">页</button></div><div class=\"btn-group\"><button class=\"btn\">共</button>"
		+ "<button id=\"totalPage\" class=\"btn\"></button>"
		+ "<button class=\"btn\">页</button></div><div class=\"btn-group\"><button id=\"pagebackward\" class=\"btn\">上一页</button><button id=\"pageforward\" class=\"btn\">下一页</button></div>"
		+ "<div class=\"btn-group\"><button class=\"btn\">共</button><button id=\"recordCount\" class=\"btn\"></button><button class=\"btn\">条</button>"
		+ "</div><div class=\"btn-group\"><button class=\"btn\">每页</button><select id=\"recordPage\" class=\"input-mini changePage\"><option>10</option>"
		+ "<option>20</option><option>30</option><option>40</option><option>50</option></select></div>"
		+ "<div class=\"btn-group\"><button id=\"goto\" class=\"btn\">跳转</button><input id=\"gotopage\" type=\"text\" class=\"input-mini changePage\" placeholder=\"页码\"/></div>";
	$("#pagination").html(pageHtml);
}
function showUserInfo() {
	var myInfo = userInfo.password + "$" + userInfo.userTrueName + "$" + userInfo.jobNum + "$" + userInfo.department
	+ "$" + userInfo.position + "$" + userInfo.contactNum + "$" + userInfo.email + "$" + userInfo.verify + "$"
	+ userInfo.rejectReason + "$" + userInfo.role;
	var iHtml = '<li class="dropdown"><a class="dropdown-toggle" href="#" data-toggle="dropdown"><i class=" icon-user icon-white"></i>'
		+ userInfo.userTrueName
		+ '<strong class="caret"></strong></a>'
		+ '<ul class="dropdown-menu">'
		+ '<li><a id="comUserId'
		+ userInfo.userId
		+ '" onclick="editUserModel(this,1)" href="#modUser" data-toggle="modal" info='
		+ myInfo
		+ ' name='
		+ userInfo.userName
		+ '><i class="icon-wrench"></i>个人资料</a></li><li class="divider"></li>'
		+ '<li><a id="logout" href="login.html"><i class="icon-off"></i>退出</a></li></ul></li>';
	$("#userinfo").html(iHtml);
}
function moduserHtml() {
	var html = '<div><div id="modUser" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'
		+ '<div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button><h3 id="myModalLabel">添加用户</h3></div>'
		+ '<div class="modal-body"><div id="information"></div><form class="form-horizontal">'
		+ '<div class="control-group"><label class="control-label " for="userName">用户名</label>'
		+ '<div class="controls"><input id="userName" class="input-big uneditable-input" readonly="readonly" type="text" name="userName"><span class="help-inline">请输入用户的登录名称</span></div></div>'
		+ '<div class="control-group"><label class="control-label" for="userTrueName">真实姓名</label><div class="controls">'
		+ '<input id="userTrueName" class="input-big valid" type="text" data-provide="typeahead" name="userTrueName"> '
		+ '<span class="help-inline">请输入用户的真实名称</span></div></div><div class="control-group"><label class="control-label" for="jobNum">用户编号</label><div class="controls">'
		+ '<input id="jobNum" class="input-big valid" type="text" data-provide="typeahead" name="jobNum"> '
		+ '<span class="help-inline">请输入用户的编号（学号、工号）</span></div></div><div class="control-group"><label class="control-label" for="department">部门</label><div class="controls">'
		+ '<input id="department" class="input-big valid" type="text" data-provide="typeahead" name="department"> '
		+ '<span class="help-inline">请输入用户的部门名称</span></div></div><div class="control-group"><label class="control-label" for="position">职位</label><div class="controls">'
		+ '<input id="position" class="input-big valid" type="text" data-provide="typeahead" name="position"> '
		+ '<span class="help-inline">请输入用户的职位</span></div></div><div class="control-group"><label class="control-label" for="contactNum">联系方式</label><div class="controls">'
		+ '<input id="contactNum" class="input-big valid" type="text" data-provide="typeahead" name="contactNum"> '
		+ '<span class="help-inline">请输入用户的联系方式</span></div></div><div class="control-group"><label class="control-label" for="email">邮箱</label><div class="controls">'
		+ '<input id="email" class="input-big valid" type="text" data-provide="typeahead" name="email"> '
		+ '<span class="help-inline">请输入用户的邮箱</span></div></div><div class="control-group"><label class="control-label" for="rejectReason">申请理由</label><div class="controls">'
		+ '<input id="rejectReason" class="input-big valid" type="text" data-provide="typeahead" name="rejectReason"> '
		+ '<span class="help-inline">请输入用户的申请理由</span></div></div><div class="control-group"><label class="control-label" for="password">用户密码</label>'
		+ '<div class="controls"><input id="password" class="input-big valid" type="password" name="password"> '
		+ '<span class="help-inline"></span></div></div><div class="control-group"><label class="control-label" for="password2">确认密码</label><div class="controls">'
		+ '<input id="password2" class="input-big valid" type="password" name="password2"> <span class="help-inline"></span></div></div>'
		+ '<div class="control-group"><label class="control-label" for="role">用户角色</label><div class="controls"><select id="role" class="span3"><option value ="0">一般用户</option><option value ="1">管理员</option></select>'
		+ ' <span class="help-inline">用户角色选择</span></div></div><div class="control-group"><label class="control-label" for="role">认证</label><div class="controls"><select id="verify" class="span3"><option value ="0">否</option><option value ="1">是</option></select>'
		+ ' <span class="help-inline">用户认证</span></div></div><div class="control-group"><label class="control-label" for="role">有效日期</label><div class="controls"><input id="time" class="span3" type="text" value="60">'
		+'</input><span class="help-inline">天  有效日期</span></div></div></form></div>'
		+ '<div id="operateButton" class="modal-footer"><button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>'
		+ '<button id="saveButton" class="btn btn-primary">保存</button></div></div></div>';
	$("#footer").after(html);
	if (role == 0) {
		$("#role").attr("disabled", "disabled");
		$("#verify").attr("disabled", "disabled");
	}
}
function editUser() {
	$.ajaxSettings.async = false;
	$('#information').empty();
	var info = $("#" + htmlId).attr("info").split("$");
	var oldPassword = info[0];
	var password1 = hex_md5($("#password").val());
	var password2 = hex_md5($("#password2").val());
	if (oldPassword == $("#password").val()) {
		password1 = $("#password").val();
		password2 = $("#password2").val();
	}
	userInfo.userId = userId;
	userInfo.userName = $("#userName").val();
	userInfo.password = password1;
	userInfo.userTrueName = $("#userTrueName").val();
	userInfo.jobNum = $("#jobNum").val();
	userInfo.department = $("#department").val();
	userInfo.position = $("#position").val();
	userInfo.contactNum = $("#contactNum").val();
	userInfo.email = $("#email").val();
	userInfo.verify = $("#verify").val();
	userInfo.rejectReason = $("#rejectReason").val();
	userInfo.role = $("#role").val();
	userInfo.time=$("#time").val();
	var url = '../../handler/user/editUser';
	var informationHtml = "";
	if (userInfo.userName.length <= 0) {
		informationHtml += "<div class='alert alert-info'>"
			+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
			+ "<h4>注意!</h4> <strong>用戶名不能为空!</strong>";
		$('#information').append(informationHtml);
		return false;
	}
	if (password1 != password2) {
		informationHtml += "<div class='alert alert-info'>"
			+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
			+ "<h4>注意!</h4> <strong>两次密码不同!</strong>";
		$('#information').append(informationHtml);
		return false;
	}
	if(userInfo.time.length>0){
		var re=/^[1-9]+[0-9]*]*$/;
		if(!re.test(userInfo.time)){
			var info = "<div class='alert alert-info'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>有效日期请输入正整数!</strong></div>";
			$('#information').append(info);
			return false;
		}
	}
	if(userInfo.time.length==""){
			var info = "<div class='alert alert-info'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>请输入有效日期!</strong></div>";
			$('#information').append(info);
			return false;
	}
	if(validTel(userInfo.contactNum)!=''){
		var error=validTel(userInfo.contactNum);
		var info = "";
		info += "<div class='alert alert-warning'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4>"+ "<strong>"+error+"</strong></div>";
		$('#information').empty().append(info);
		return false;
	}
	if(validEmail(userInfo.email)!=''){
		var error=validEmail(userInfo.email);
		var info = "";
		info += "<div class='alert alert-warning'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4>"+ "<strong>"+error+"</strong></div>";
		$('#information').empty().append(info);
		return false;
	}
	$.post(url, userInfo, function(data) {
		if (data.ret) {
			var resultData = data.data.data;
			if (resultData.result == "success") {
				informationHtml += "<div class='alert alert-success'>"
					+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
					+ "<h4>成功!</h4> <strong>用戶编辑成功!</strong>";
				if (editType == 1) {
					var userData = resultData.user;
					// add cookie
					var str = "user=" + userData.userId + "$" + escape(userData.userName) + "$"
					+ escape(userData.password) + "$" + escape(userData.userTrueName) + "$"
					+ escape(userData.jobNum) + "$" + escape(userData.department) + "$"
					+ escape(userData.position) + "$" + escape(userData.contactNum) + "$"
					+ escape(userData.email) + "$" + userData.verify + "$" + escape(userData.rejectReason)
					+ "$" + userData.role + "$" + escape(userData.time);
					var date = new Date();
					var ms = 24 * 3600 * 1000;
					date.setTime(date.getTime() + ms);
					str += "; expires=" + date.toGMTString();
					document.cookie = str;
				}
			} else if (resultData.result == "fail") {
				informationHtml = "";
				informationHtml += "<div class='alert alert-error'>"
					+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
					+ "<h4>失败!</h4> <strong>用戶编辑失败!</strong>";
			}
		} else {   
			informationHtml = "";
			informationHtml += "<div class='alert alert-error'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>失败!</h4> <strong>用戶编辑失败!</strong>";
		}
		informationHtml += "</div>";
		$('#information').empty().append(informationHtml);
		location.reload();
	});
	return;
}
function validTel(value){
	var relll = /^1[3578]\d{9}$|^(0\d{2,3}-?|\(0\d{2,3}\))?[1-9]\d{4,7}(-\d{1,8})?$/;
	    if(!relll.test(value)){ 
	    	return '联系电话格式错误'; 
		 }
		 else  return "";
} 

function validEmail(email)
{
	var msg='';
	if(email!=''&&email.replace(/\S+@\w+(\.\S+)+/,'')!='')
	{
		msg+=" 邮件格式错误";
	}	
	return msg;
}
function editUserModel(obj, type) {
	editType = type;
	option = "edit";
	if(type==0){
		userId = obj.id.replace("userId", "");
	}else{
		userId = obj.id.replace("comUserId", "");
	}
	htmlId = obj.id;
	var userName = obj.name;
	var info = "";
	info = $("#" + htmlId).attr("info").split("$");
	$("#myModalLabel").empty().append("修改用户");
	$("#userName").val(userName);
	$("#password").val(info[0]);
	$("#password2").val(info[0]);
	$("#userTrueName").val(info[1]);
	if(info[2]=="undefined"){
		$("#jobNum").val("");
	}else{
		$("#jobNum").val(info[2]);
	}
	if(info[3]=="undefined"){
		$("#department").val("");
	}else{
		$("#department").val(info[3]);
	}
	if(info[4]=="undefined"){
		$("#position").val("");
	}else{
		$("#position").val(info[4]);
	}
	$("#contactNum").val(info[5]);
	if(info[6]=="undefined"){
		$("#email").val("");
	}else{
		$("#email").val(info[6]);
	}
	$("#verify").val(info[7]);
	if(info[8]=="undefined"){
		$("#rejectReason").val("");
	}else{
		$("#rejectReason").val(info[8]);
	}
	$("#role").val(info[9]);
	//、$("#time").val(info[10]);
	$("#time").val(60);
	return;
}