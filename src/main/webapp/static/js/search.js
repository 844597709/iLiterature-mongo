var params;// 请求的参数
var startTime = '';// 查询的开始时间
var endTime = '';// 查询的结束时间
var days = '';// 查询的天数。0表示选择的是时间段
var topNum = '';// 查询top "number" : 10
var viewaction = '';// 分页请求的action
$(document).ready(function() {
	params = {
		"startTime" : "",
		"endTime" : "",
		"query" : "",
		"recentDays" : 0,
		"pageArray" : new Array(),
		"recordPerPage" : 20,
		"sentiment" : "",
		"metaSearch" : "",
		"indexLoc" : "",
		"isPubDate" : ""
	};
	$("#searchTime").hide();
	//回车键的键值为13  
	document.onkeydown = function(event) {
		var e = event || window.event || arguments.callee.caller.arguments[0];
		if (e && e.keyCode == 13) {
			//to do something;
			document.getElementById("search").click(); //调用登录按钮的登录事件  
		}
	};
});
//获取参数
function GetRadioValue(RadioName) {
	var obj;
	obj = document.getElementsByName(RadioName);
	if (obj != null) {
		var i;
		for (i = 0; i < obj.length; i++) {
			if (obj[i].checked) {
				return obj[i].value;
			}
		}
	}
	return null;
}