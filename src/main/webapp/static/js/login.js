var userName = '';
var password = "";
var footerInfo='';
var userInfo = {
		userName : "",
		password : "",
		userTrueName : "",
		department:"",
		position:"",
		contactNum : "",
		email : "",
		Reason : "",
};
$(document).ready(function() {
	$("#user_name").val("");
	$("#user_password").val("");
	$("#authcode").val("");
	countInfo();//统计网站、作者、作品和评论数
	showDisPlay ("iLogin","iRegister");
	showDisPlay ("iRegister","iLogin");
	
	add();
	getSysParam();
	
	$("#save").click(function() {
		if (option == 'add') {
			addUser();
		} 
	});
	$("#cancle").click(function(){
		clearText();
	});
	$('#loginbut').click(function() {
		$('#loginInfo').empty();
		userName = $('#user_name').val();
		password = $('#user_password').val();
		authcode = $("#authcode").val();
		if (userName.length <= 0) {
			/*var informationHtml = "<div class='alert alert-warning'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+"<h4>注意!</h4><strong>用户名不能为空!</strong>";
			$('#information').empty().append(informationHtml);*/
			$("#infoHtml").empty().append("提示");
			informationHtml="<strong>用户名不能为空!</strong>";
			$('#delLabel').html(informationHtml);
			$("#delModal").modal({
					'backdrop' : true
			});
			return false;		
		}
		if (password.length <= 0) {
			/*var informationHtml = "<div class='alert alert-warning'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+"<h4>注意!</h4><strong>密码不能为空!</strong>";
			$('#information').empty().append(informationHtml);*/
			$("#infoHtml").empty().append("提示");
			informationHtml="<strong>密码不能为空!</strong>";
			$('#delLabel').html(informationHtml);
			$("#delModal").modal({
					'backdrop' : true
			});
			return false;
		}
		if (authcode == null || authcode == "") {
			$("#authcode").focus();
			/*$('#information').empty().append("<div class='alert alert-warning'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button><h4>注意！</h4><strong> 验证码不能为空!</strong>");*/
			$("#infoHtml").empty().append("提示");
			informationHtml="<strong>验证码不能为空!</strong>";
			$('#delLabel').html(informationHtml);
			$("#delModal").modal({
					'backdrop' : true
			});
			return false;
		}
		login();
		return false;
	});
	reloadcode();
	
});

 function countInfo() {
	// 统计信息
	var url = '../../handler/author/countInfo';
	$.post(url, function(data) {
		if (data.ret && data.data.result) {
			var entry = data.data.result;
			$(".websites").append(entry.site);
			$(".authors").append(entry.author);
			$(".works").append(entry.works);
			$(".comments").append(entry.worksComments);
		}
	});
}

function login() {
	var informationHtml = "";
	password = hex_md5(password);
	$.ajax({
		type : 'POST',
		contentType : 'application/x-www-form-urlencoded;charset=UTF-8',// 发送信息至服务器时内容编码类型
		url : '../../handler/user/login',
		async : false, // 需要同步请求数据
		data : {
			"userName" : userName,
			"password" : password,
			"authcode" : authcode,
		},
		dataType : 'json',
		success : function(data) {
			if (data.ret) {
				var resultData = data.data.result;
				if (resultData.result == "success") {
					var userData = resultData.user;
					informationHtml = "<strong>正在登陸，請稍等……!</strong>";
					// add cookie
					var str = "user=" + userData.userId + "$" + escape(userData.userName) + "$"
							+ escape(userData.password) + "$" + escape(userData.userTrueName) + "$"
							+ escape(userData.jobNum) + "$" + escape(userData.department) + "$"
							+ escape(userData.position) + "$" + escape(userData.contactNum) + "$"
							+ escape(userData.email) + "$" + userData.verify + "$" + escape(userData.rejectReason)
							+ "$" + userData.role;
					var date = new Date();
					var ms = 24 * 3600 * 1000;
					date.setTime(date.getTime() + ms);
					str += "; expires=" + date.toGMTString();
					document.cookie = str;
					window.location.href = "literature.html?firstColuId=1";
				} else if (resultData.result == "passwordError") {
					//alert("密码错误！");
				/*	informationHtml += "<div class='alert alert-info'>"
						+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
						+ "<h4>注意!</h4> <strong>密码错误!</strong>";*/
					informationHtml="<strong>您输入的密码有误，请重新输入!</strong>";
					$('#delLabel').html(informationHtml);
					$("#delModal").modal({
							'backdrop' : true
					});
				} else if (resultData.result == "null") {
					//alert("该用户不存在!");
				/*	informationHtml += "<div class='alert alert-info'>"
						+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
						+ "<h4>注意!</h4> <strong>该用户不存在!</strong>";*/
					informationHtml="<strong>该用户不存在，请认真核实信息后再登录！</strong>";
					$('#delLabel').html(informationHtml);
							$("#delModal").modal({
								'backdrop' : true
							});
				}else if(resultData.result=="时间过期"){
					//alert("该用户已过期!");
					/*informationHtml += "<div class='alert alert-info'>"
						+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
						+ "<h4>注意!</h4> <strong>该用户已过期!</strong>";*/
					informationHtml="<strong>该用户已过期，如需继续使用，请与管理员联系！</strong>";
					$('#delLabel').html(informationHtml);
							$("#delModal").modal({
								'backdrop' : true
							});
				}else if(resultData.result=="没有认证"){
					//alert("该用户未认证!");
					/*informationHtml += "<div class='alert alert-info'>"
						+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
						+ "<h4>注意!</h4> <strong>该用户未认证!</strong>";*/
					informationHtml=" <strong>该用户未认证，请耐心等待管理员认证！</strong>";
					$('#delLabel').html(informationHtml);
							$("#delModal").modal({
								'backdrop' : true
							});
				}
			} else if(data.errmsg="验证码有误,请检查")
			  {
				//alert(data.errmsg);
				informationHtml = "<strong>您输入的验证码有误,请重新输入!</strong>";
			    $('#delLabel').html(informationHtml);
			     $("#delModal").modal({
				   'backdrop' : true
			   });
		      }
			else{
				
					//alert(data.errmsg);
					informationHtml = "<strong>未知错误,请与管理员联系!</strong>";
				$('#delLabel').html(informationHtml);
				$("#delModal").modal({
					'backdrop' : true
				});
			}
		}
	});
	
	return false;
}
// 重新加载验证码
function reloadcode() {
	document.getElementById('imgcode').src = 'checkcode.jsp?' + Math.random();
}
// 设置设为首页功能
function SetHome(url) {
	if (document.all) {
		document.body.style.behavior = 'url(#default#homepage)';
		document.body.setHomePage(url);
	} else {
		alert("您好,您的浏览器不支持自动设置页面为首页功能,请您手动在浏览器里设置该页面为首页!");
	}

}
function showDisPlay (id,id1){
	$('#'+id).click(function(){	
		$("#"+id1+"1").hide();
		$('#'+id+"1").show();		
	});
}


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
	userInfo.department = $("#department").val();
	userInfo.position = $("#position").val();
	userInfo.contactNum = $("#contactNum").val();
	userInfo.email = $("#email").val();
	userInfo.Reason = $("#Reason").val();
	/*if (userInfo.Reason.value == null||userInfo.Reason.value =="") {
		alert(userInfo.Reason.length);
		var Info = "";
		Info += "<div class='alert alert-warning'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>用戶申请理由不能为空!</strong></div>";
		$('#information').append(Info);
		return false;
	}*/
	if (userInfo.userName.length <= 0) {
		alert(userInfo.userName+"!");
		var info = "";
		info += "<div class='alert alert-warning'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>用戶名不能为空!</strong>";
		$('#information').empty().append(info);
		return false;
	}
	if (userInfo.userTrueName.length <= 0) {
		var info = "";
		info += "<div class='alert alert-warning'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>用戶真实名不能为空!</strong></div>";
		$('#information').empty().append(info);
		return false;
	}
	if(password.length<=0){
		var info = "";
		info += "<div class='alert alert-warning'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>用戶密码不能为空!</strong></div>";
		$('#information').empty().append(info);
		return false;
	}
	if (password != password2) {
		var info = "";
		info += "<div class='alert alert-warning'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>两次密码不同!</strong></div>";
		$('#information').empty().append(info);
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
	password = hex_md5(password);
	userInfo.password = password;
	$.post(url, userInfo, function(data) {
		var informationHtml = "";
		if (data.ret) {
			if (data.data.result == "success") {
				informationHtml = "";
				/*informationHtml += "<div class='alert alert-success'>"
						+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
						+ "<h4>注册成功!</h4> <strong>用户注册成功!</strong></div>";
				$('#information').empty().append(informationHtml);*/
				 informationHtml="<strong>用户注册成功,请耐心等待管理员认证!</strong>";
				$("#infoHtml").empty().append("注册成功");
				 $('#delLabel').html(informationHtml);
				$("#delModal").modal({
						'backdrop' : true
				});			
				setTimeout(function(){window.location.href=window.location.href;},5000);
			} else if (data.data.result == "fail") {
				informationHtml = "";
			/*	informationHtml += "<div class='alert alert-error'>"
						+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
						+ "<h4>注册失败!</h4> <strong>用户注册失败!</strong></div>";
				$('#information').empty().append(informationHtml);*/
				 informationHtml="<strong>用户注册失败,请重新注册!</strong>";
					$('#delLabel').html(informationHtml);
					$("#delModal").modal({
							'backdrop' : true
					});
			} else if (data.data.result == "isExist") {
				informationHtml = "";
				/*informationHtml += "<div class='alert alert-error'>"
						+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
						+ "<h4>注册失败!</h4> <strong>已有该用户!</strong></div>";
				$('#information').append(informationHtml);*/
				 informationHtml="<strong>用户注册失败,已有该用户，请重新注册!</strong>";
					$('#delLabel').html(informationHtml);
					$("#delModal").modal({
							'backdrop' : true
					});
			}
		} else {
			informationHtml = "";
			/*informationHtml += "<div class='alert alert-error'>"
					+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
					+ "<h4>注册失败!</h4> <strong>用户注册失败!</strong></div>";
			$('#information').append(informationHtml);*/
			 informationHtml="<strong>用户注册失败,未知错误，请与管理员联系!</strong>";
				$('#delLabel').html(informationHtml);
				$("#delModal").modal({
						'backdrop' : true
				});
		}
	  });
	return;
}
function add() {
	//$("#myModalLabel").empty().append("用户注册");
	$("#userName").removeAttr("readonly");
	$("#userName").removeClass();
	$("#userName").addClass("input-big");
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
function validEmail(email)
{
	var msg='';
	/*if(email=='')
	{
		msg+="邮件不能为空";
	}*/
	if(email!=''&&email.replace(/\S+@\w+(\.\S+)+/,'')!='')
	{
		msg+=" 邮件格式错误";
	}	
	return msg;
}
/*function validTel(tel)
{
	if(tel!=''&&tel.replace(/^(0(10|[3-9]\d\d|2\d)[-]\d{7,8}|0?1[358]\d{9})|[1-9]\d\d{6,7}$/,'')!='')
		return "联系电话格式错误";
	return "";
}*/

function validTel(value){  
	    var rel=/^1[3578]\d{9}$|^(0\d{2,3}-?|\(0\d{2,3}\))?[1-9]\d{4,7}(-\d{1,8})?$/;
	  if(!rel.test(value)){ 
	     return '联系电话格式错误'; 
		 }
		 else  return "";
		 } 


function showFooter() {
	$.ajaxSettings.async = false;
	var footerHtml = "";
	var currdate = new Date();
	var copydate = "2015";
	if (currdate.getFullYear() != copydate)
		copydate = copydate + " - " + currdate.getFullYear();
	footerHtml = footerInfo + "";
	alert(footerInfo+"11111");
	$("#footer").append(footerHtml);
	return;
}

function getSysParam() {
	// 请求两个参数信息
	$.post("../../handler/systemparameter/querySysParam", function(data) {
		getdata = data.data.data;
		footerInfo =getdata[1].sypaValue;
		var footerHtml = "";
		footerHtml = footerInfo + "";
		$("#footer").append(footerHtml);
		return;
	});
}