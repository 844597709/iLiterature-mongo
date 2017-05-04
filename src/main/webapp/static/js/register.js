var userInfo = {
		/*userId : "",*/
		userName : "",
		password : "",
		userTrueName : "",
		contactNum : "",
		email : "",
		rejectReason : "",
		role : "",
		time : ""
};
$(function() {
	// option button 用户信息修改
	add();
	$("#save").click(function() {
		if (option == 'add') {
			addUser();
		} 
	});
	$("#cancle").click(function(){
		clearText();
	});
	
});
var userParams;// user参数
var option = "";
var id = '';
function addUser() {
	$.ajaxSettings.async = false;
	$('#information').empty();
	var url = '../../handler/user/addUser';
	var password = $('#password').val();
	var password2 = $('#password2').val();
	userInfo.userName = $("#userName").val();
	userInfo.userTrueName = $("#userTrueName").val();
	userInfo.contactNum = $("#contactNum").val();
	/*userInfo.email = $("#email").val();
	*/
	userInfo.rejectReason = $("#rejectReason").val();
	userInfo.role = $("#role").val();
	userInfo.time=$("#time").val();
	if (userInfo.userName.length <= 0) {
		var info = "";
		info += "<div class='alert alert-info'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>用戶名不能为空!</strong>";
		$('#information').append(info);
		return false;
	}
	if (userInfo.userTrueName.length <= 0) {
		var info = "";
		info += "<div class='alert alert-info'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>用戶真实名不能为空!</strong></div>";
		$('#information').append(info);
		return false;
	}
	/*if (password.length <= 0 || password2.length <= 0) {
		var info = "";
		info += "<div class='alert alert-info'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>用戶密码不能为空!</strong>";
		$('#information').append(info);
		return false;
	}*/
	if (password != password2) {
		var info = "";
		info += "<div class='alert alert-info'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>两次密码不同!</strong></div>";
		$('#information').append(info);
		return false;
	}
	if (userInfo.rejectReason.length <= 0) {
		var info = "";
		info += "<div class='alert alert-info'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>用戶申请理由不能为空!</strong></div>";
		$('#information').append(info);
		return false;
	}
	password = hex_md5(password);
	userInfo.password = password;
	$.post(url, userInfo, function(data) {
		var informationHtml = "";
		if (data.ret) {
			if (data.data.result == "success") {
				informationHtml += "<div class='alert alert-success'>"
						+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
						+ "<h4>注册成功!</h4> <strong>用户注册成功!</strong></div>";
				$('#information').empty().append(informationHtml);
			} else if (data.data.result == "fail") {
				informationHtml = "";
				informationHtml += "<div class='alert alert-error'>"
						+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
						+ "<h4>注册失败!</h4> <strong>用户注册失败!</strong></div>";
				$('#information').empty().append(informationHtml);
			} else if (data.data.result == "isExist") {
				informationHtml = "";
				informationHtml += "<div class='alert alert-error'>"
						+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
						+ "<h4>注册失败!</h4> <strong>已有该用户!</strong></div>";
				$('#information').empty().append(informationHtml);
			}
		} else {
			informationHtml = "";
			informationHtml += "<div class='alert alert-error'>"
					+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
					+ "<h4>注册失败!</h4> <strong>用户注册失败!</strong></div>";
			$('#information').empty().append(informationHtml);
		}
		//informationHtml += "</div>";
		//$('#information').append(informationHtml);
		/*location.reload();*/
	});
	return;
}
function add() {
	$("#myModalLabel").empty().append("用户注册");
	$("#userName").removeAttr("readonly");
	$("#userName").removeClass();
	$("#userName").addClass("input-big span3");
	option = "add";
	clearText();
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
}