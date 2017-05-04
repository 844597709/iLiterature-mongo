var authorInfo = {};//change by Cz
$(function() {
	authorInfo = {
			workId : getRequest("workId"),
			/**
			 * 头部header逻辑
			 * 
			 * @param 参数列表
			 */
			showMenu : function() {
				$("#topMenu").find('li').each(function() {
					$(this).removeClass();
				});
				var parentColuId = getRequest("firstColuId");
				$("#firstColuId" + parentColuId + "").attr("class", "active"); // 添加菜单选中样式
				$("#pagination").empty();
			},
			/**
			 * 
			 * 头部header逻辑
			 * 
			 * @param 参数列表
			 */
			showData : function() {
				if (this.authorId <= 0) {
					$('#iData').empty().append("没有相关数据");
					return;
				}
				var url = '../../handler/worksInfo/viewWork';
				$.post(url, {
					"workId" : this.workId
				}, function(data) {
					var iHtml = "没有相关数据";
					if (data.ret) {
						iHtml = "";
						var item = data.data.data;
						var info = '';
						var url = '';
						if (item.url != null && item.url != "") {
							url = item.url;
						}
						var sTime = '';
						if (item.sTime != null && item.sTime != "") {
							sTime = item.sTime;
						}
						if(item.totalHits.length>4){
							item.totalHits=item.totalHits.substr(0,item.totalHits.length-4)+"万";
						}
						info = item.authorName + "$" + url + "$" + item.description + "$" + sTime;
						iHtml += "<tr><td class='tdcenter'><a id='" + item.workId + "' name='" + info
						+ "'  href='workDetail.html?firstColuId=3&workId=" + item.workId + "'>" + item.title
						+ "</a>" + "</td><td class='tdcenter'>" + item.author + "</td><td class='tdcenter'>"
						+ item.type + "</td><td class='tdcenter' title='" + item.description + "'>"
						+ item.description.substr(0, 20) + "</td><td class='tdcenter'>" + transforms(item.totalHits)
						+ "</td><td class='tdcenter'>" + sTime.substr(0, 16)
						+ "</td><td class='tdcenter'><a title='" + url + "' href='" + url
						+ "'target='_blank'><i class='icon-globe'></i></a></td></tr>";
						$('#iData').append(iHtml);
						return;
					}
				});
			},
			showComments : function() {
				if (this.authorId <= 0) {
					return;
				}
				var url = '../../handler/worksInfo/commentsByWork';
				$.post(url,{"workId" : this.workId},function(data) {
					var iHtml="";
					if (data.ret) {
						var myData = data.data.data;
						if(myData[0].freq==0){
							iHtml+="<br><br><br><br><h4 align='center' style='color:red'>没有相关数据！</h4>";
							$('#tagbox').append(iHtml);
							return;
						}
						$.each(myData,function(itemIndex, item) {
							 if(itemIndex%3==2)
								 iHtml+="<div class='turning'>&nbsp;"+item.word+"&nbsp;</div>";
							 else if(itemIndex%3==0)
								 iHtml+="<div class='turning1'>&nbsp;"+item.word+"&nbsp;</div>";
							 else
								 iHtml+="<div class='none'>&nbsp;"+item.word+"&nbsp;</div>";
						});
						$(function() {
					        $("#tagbox").append(iHtml);
					      });	 
					}
				});
				
				return;
			},
			showDetails : function() {
				if (this.authorId <= 0) {
					$('#iData').empty().append("没有相关数据");
					return;
				}
				var url = '../../handler/worksInfo/selectByWork';
				var timeArr = [];
				var nameArr = [];
				var dataArr = [];
				$.post(url, {
					"workId" : this.workId
				}, function(data) {
					var iHtml = "";
					if (data.ret) {
						var myData = data.data.data ;
						for (var key in myData) { 
							  if(key=="time"){
								  timeArr=myData[key];
							  }
							  if(key=="info"){
								  var i=0;
								  $.each(myData[key],function(itemIndex, item) {
									  nameArr[i]=item.attrName;
									  dataArr[i++]=item.attrValue;
								  });
							  }
				          }
					}
					$('#detail').empty().append(iHtml);
				});
				this.showLine(timeArr, dataArr[0], dataArr[1], dataArr[2], "iCharts", "");
				return;
			},
			showLine : function(timeArr, hitsArr, recomsArr, commentArr, htmlId, text) {
				var myChart = echarts.init(document.getElementById(htmlId));
				option = {
						title : {
							text :'',
						},
						tooltip : {
							trigger : 'axis'
						},
						legend: {
					        data:['点击量','推荐数','评论数']
					    },
						toolbox : {
							show : true,
							feature : {
								/*mark : {
									show : true
								},
								dataView : {
									show : true,
									readOnly : false
								},*/
								magicType : {
									show : true,
									type : [ 'line', 'bar' ]
								},
								restore : {
									show : true
								},
								saveAsImage : {
									show : true
								}
							}
						},
						calculable : true,
						xAxis : [ {
							type : 'category',
							boundaryGap : false,
							data : timeArr
						} ],
						yAxis : [ {
							type : 'value',
							scale : true,
							splitNumber : 9,
							splitArea : {
								show : true
							},
							axisLabel : {
								formatter : '{value} '
							}
						} ],
						series : [ {
							name : '点击量',
							type : 'line',
							data : hitsArr,
						},
						{
							name : '推荐数',
							type : 'line',
							data : recomsArr,
						} ,
						{
							name : '评论数',
							type : 'line',
							data : commentArr,
						} ]
						
				};
				
				
				// 为echarts对象加载数据
				myChart.setOption(option);
			},
			/*以上为原版本，以下为修改的*/
			showDetailsAgain : function() {
				if (this.authorId <= 0) {
					$('#iData').empty().append("没有相关数据");
					return;
				}
				//ljf
				//var url = '../../handler/worksInfo/selectByWork';
				var url = '../../handler/worksInfo/selectWorksUpdateById';
				var timeArr=[];
				var attrArr=new Array();
				var nameArr=[];
				var attrAverage=[];
				var flag=true;
				for(var i=0;i<8;i++){
					attrArr[i]=new Array();
				}
				$.post(url, {
					"workId" : this.workId
				}, function(data) {
					if (data.ret) {
						var myData = data.data.data ;
						for (var key in myData) { 
							if(key==="none"){
								  flag=false;
								  return;
							  }
							  if(key=="time"){
								  timeArr=myData[key];
							  }
							  if(key=="info"){
								  var i=0;
								  $.each(myData[key],function(itemIndex, item) {
									  nameArr[i]=item.attrName;
									  attrArr[i]=item.attrValue;
									  attrAverage[i++]=item.attrAverage;
								  });
							  }
				          }
						
					}
				});
				if(flag==false){
					return;
				}
				var i=0;
				var iHtml="";
				for(i=1;i<9;i++){
					iHtml+="<li class='span6'><div class='thumbnail'><div id='attr"+i+"' style='height: 300px;'></div></div></li>";
				}
				$('#hehe').append(iHtml);
				i=0;
				this.showLineAgain(timeArr, attrArr[i], "attr1",attrAverage[i] ,nameArr[i++]);
				this.showLineAgain(timeArr, attrArr[i], "attr2",attrAverage[i] ,nameArr[i++]);
				this.showLineAgain(timeArr, attrArr[i], "attr3",attrAverage[i] ,nameArr[i++]);
				this.showLineAgain(timeArr, attrArr[i], "attr4",attrAverage[i] ,nameArr[i++]);
				this.showLineAgain(timeArr, attrArr[i], "attr5",attrAverage[i] ,nameArr[i++]);
				this.showLineAgain(timeArr, attrArr[i], "attr6",attrAverage[i] ,nameArr[i++]);
				this.showLineAgain(timeArr, attrArr[i], "attr7",attrAverage[i] ,nameArr[i++]);
				this.showLineAgain(timeArr, attrArr[i], "attr8",attrAverage[i] ,nameArr[i]);
				
				return;
			},
			showLineAgain : function(timeArr, dataArr, htmlId,aver, text) {
				var myChart = echarts.init(document.getElementById(htmlId));
				option = {
						title : {
							text : text ,
						},
						tooltip : {
							trigger : 'axis'
						},
						toolbox : {
							show : true,
							feature : {
								mark : {
									show : true
								},
								dataView : {
									show : true,
									readOnly : false
								},
								magicType : {
									show : true,
									type : [ 'line', 'bar' ]
								},
								restore : {
									show : true
								},
								saveAsImage : {
									show : true
								}
							}
						},
						calculable : true,
						xAxis : [ {
							type : 'category',
							boundaryGap : false,
							
							data : timeArr
						} ],
						yAxis : [ {
							type : 'value',
							scale : true,
							splitNumber : 8,
							splitArea : {
								show : true
							},
							axisLabel : {
								formatter : '{value} '
							}
						} ],
						series : [ {
							name : '最低',
							type : 'line',
							data : dataArr,
						}]
				};
				// 为echarts对象加载数据
				myChart.setOption(option);
			},
			init : function() {
				// 初始化showMenu
				this.showMenu();
				this.showData();
				this.showComments();
				this.showDetails();
				this.showDetailsAgain();
			}
	};
	authorInfo.init();
});
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