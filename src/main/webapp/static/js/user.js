$(function() {
	var user = {
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
		showData : function() {
			var Isverify=$("#Isverify").val();
			var Isdead=$("#Isdead").val();
			viewaction = '../../handler/user/selectVerifyDateTimeUser';
			//viewaction = '../../handler/user/viewAllUser';
			paginationPage();// 键入页码，分页等标识
			params={
					"pageArray" : new Array(),
					"recordPerPage" : 20,
					"verify":Isverify,
					"isLegal":Isdead
			};
			initialBind();// 绑定分页的一些操作响应
			initSearch();
			//checkbyvalue(fristPage);// 初始化页码
		},
		searchData:function(){
			var obj=this;
			$("#Isverify").change(function(){
				obj.showData();
			});
			$("#Isdead").change(function(){
				obj.showData();
			});
		},
		init : function() {
			//moduserHtml();
			// 初始化showMenu
			this.showMenu();
			// 初始化showData
			this.showData(0,0);
			this.searchData();
		}
	};
	user.init();
});
var userParams;// user参数
var option = "";
var id = '';
// 加载数据到数据区域
function refreshContent(pageRecords) {
	$.ajaxSettings.async = false;
	var startIndex = (currentPage - 1) * recordPerPage + 1;
	$("#iData").empty();
	var userHtml = "";
	if (pageRecords.data.length == 0) {
		$('#iData')
				.append(
						"<tr><td colspan=\"10\" class=\"tdcenter\"><i class=\" icon-warning-sign\"></i><strong>没有相关结果！</strong></td></tr>");
	} else {
		var currentTime=new Date();
		$.each(pageRecords.data, function(itemIndex, item) {
			//var info = "编号：" + item.jobNum + " 联系方式：" + item.contactNum + " 邮箱：" + item.email;
			var ourInfo = item.password + "$" + item.userTrueName + "$" + item.jobNum + "$" + item.department + "$"
					+ item.position + "$" + item.contactNum + "$" + item.email + "$" + item.verify + "$"
					+ item.rejectReason + "$" + item.role+"$"+item.deadTime;
			var role = "一般人员";
			var operate="<td></td>";
			var background="";
			if (item.role == 1) { 
				role = "管理员";
			}else if(item.role==0){
				operate="<td class='tdcenter'><a href=\"javascript:void(0);\" id='del" + item.userId
				+ "'class='action btn-del' type='' onclick='delUser(this);'></a>" + "</td>";
			}
			//判断用户到期时间
			var str =item.deadTime.replace(/-/g,"/");
			var date = new Date(str );
			if(date<currentTime){
				background="text-error";
			}
			if( item.verify==1){
				item.verify="是";
			}else{
				item.verify="否";
			}
			if(item.department==null ||  item.department ==undefined){
				 item.department ="";
			}
			if(item.position==null || item.position==undefined){
				item.position="";
			}
			userHtml += "<tr class='"+background+"'><td class='tdcenter'>" + startIndex + "</td>"
					+ "<td class='tdcenter'><a id='userId" + item.userId + "' name='" + item.userName + "' info='"
					+ ourInfo + "' class='' data-toggle='modal' href='#modUser' onclick='editUserModel(this,0)'>"
					+ item.userName + "（" + item.userTrueName + "）</a>" + "</td><td class='tdcenter'>"
					+ item.department + "</td><td class='tdcenter'>" + item.position
					+ "</td><td class='tdcenter' title='" + item.contactNum + "'>" +  item.contactNum
					+ "</td><td class=' tdcenter'>" + item.verify + "</td><td class='tdcenter'>" + role
					+ "</td><td class='tdcenter'>"+item.deadTime+"</td>"+operate+"</tr>";
			startIndex = startIndex + 1;
		});
		$('#iData').append(userHtml);
	}
	return false;
}
function delUser(obj) {
	$.ajaxSettings.async = false;
	var userId = obj.id;
	var luserid = userId.length;
	userId = userId.substring(3, luserid);
	if (parseInt(userId) > 0) {
		$("#delModal").modal({
			'backdrop' : true
		});
		$("#delOamanage").click(
				function() {
					var url = '../../handler/user/delUser';
					$.post(url, {
						"userId" : userId
					}, function(data) {
						var informationHtml = "";
						if (data.ret) {
							if (data.data.result == "success") {
								informationHtml += "<div class='alert alert-success'>"
										+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
										+ "<h4>成功!</h4> <strong>用戶刪除成功!</strong>";
							} else if (data.data.result == "fail") {
								informationHtml = "";
								informationHtml += "<div class='alert alert-error'>"
										+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
										+ "<h4>失败!</h4> <strong>用戶刪除失败!</strong>";
							}
						} else {
							informationHtml = "";
							informationHtml += "<div class='alert alert-error'>"
									+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
									+ "<h4>失败!</h4> <strong>用戶刪除失败!</strong>";
						}
						informationHtml += ".</div>";
						$('#delInfo').empty().append(informationHtml);
						$("#delModal").modal('hide');
					});
					initSearch();
					return false;

				});
	} else {
		alert("请正确选择要删除的用户！");
	}
}
function addUser() {
	$.ajaxSettings.async = false;
	$('#information').empty();
	var url = '../../handler/user/addUser';
	var password = $('#password').val();
	var password2 = $('#password2').val();
	userInfo.userName = $("#userName").val();
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

	if (userInfo.userName.length <= 0) {
		var informationHtml = "";
		informationHtml += "<div class='alert alert-info'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>用戶名不能为空!</strong>";
		$('#information').empty().append(informationHtml);
		return false;
	}
	if (password != password2) {
		var informationHtml = "";
		informationHtml += "<div class='alert alert-info'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>两次密码不同!</strong>";
		$('#information').empty().append(informationHtml);
		return false;
	}
	if(userInfo.time.length>0){
		var re=/^[1-9]+[0-9]*]*$/;
		if(!re.test(userInfo.time)){
			var info = "<div class='alert alert-info'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>有效日期请输入正整数!</strong></div>";
			$('#information').empty().append(info);
			return false;
		}
	}
	if(userInfo.time.length==""){
			var info = "<div class='alert alert-info'>"
				+ "<button type='button' class='close' data-dismiss='alert'>×</button>"
				+ "<h4>注意!</h4> <strong>请输入有效日期!</strong></div>";
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
		$('#information').empty().append(informationHtml);
		location.reload();
	});
	return;
}
function addButtonDown() {
	$("#myModalLabel").empty().append("添加用户");
	$("#userName").removeAttr("readonly");
	$("#userName").removeClass();
	$("#userName").addClass("input-big");
	option = "add";
	clearText();
	return;
}