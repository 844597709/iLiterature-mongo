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
				var url = '../../handler/worksInfo/selectWorkById';
				$.post(url, {
					"workId" : this.workId
				}, function(data) {
					var iHtml = "没有相关数据";
					if (data.ret) {
						iHtml = "";
						var item = data.data.data;
						var info = '';
						var url = item.workUrl;
						if (!item.workUrl) {
							url = '';
						}
						var sTime = item.workLastUpdateTime;
						if (!item.workLastUpdateTime) {
							sTime = '';
						}
						var desc = item.workDesc;
						if(item.workDesc.length>20){
							desc = desc.substr(0, 20)+' ...';
						}
						/*if(item.workTotalHits.length>4){
							item.workTotalHits=item.workTotalHits.substr(0,item.workTotalHits.length-4)+"万";
						}*/
						info = item.workAuthor + "$" + url + "$" + item.workDesc + "$" + sTime;
						iHtml += "<tr><td class='tdcenter'><a id='" + item.workId + "' name='" + info
						+ "'  href='workDetailTemp.html?firstColuId=3&workId=" + item.workId + "'>" + item.workTitle
						+ "</a>" + "</td><td class='tdcenter'>" + item.workAuthor + "</td><td class='tdcenter'>"
						+ item.workType + "</td><td class='tdcenter' title='" + item.workDesc + "'>"
						+ desc + "</td><td class='tdcenter'>" + transforms(item.workTotalHits)
						+ "</td><td class='tdcenter' id='workLastUpdateTime'>" + sTime
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
				var url = '../../handler/worksInfo/selectCommentsByWorkId';
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
				// TODO 是否能变成selectWorkById
				/*var url = '../../handler/worksInfo/selectByWork';
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
				});*/
                var url = '../../handler/worksInfo/selectWeekOfWorkInfo';
                var timeArr = [];
                var hitsArr = [];
                var commentArr = [];
                var recomsArr = [];
                $.post(url, {
                    "workId" : this.workId,
					"endTime" : $('#workLastUpdateTime').text()
                }, function(data) {
                    var iHtml = "";
                    if (data.ret) {
                        var myData = data.data.data ;
                        $.each(myData, function (entryIndex, entry) {
                            timeArr[entryIndex] = entry.woupRoughTime;
							if(entry.woupTotalHits==-1)entry.woupTotalHits=0;
                            hitsArr[entryIndex] = entry.woupTotalHits;
							if(entry.woupCommentsNum==-1)entry.woupCommentsNum=0;
                            commentArr[entryIndex] = entry.woupCommentsNum;
							if(entry.woupTotalRecoms==-1)entry.woupTotalRecoms=0;
                            recomsArr[entryIndex] = entry.woupTotalRecoms;
                        });
                    }
                    $('#detail').empty().append(iHtml);
                });
				this.showLine(timeArr, hitsArr, commentArr, recomsArr, "iCharts");
				return;
			},
			// showLine : function(timeArr, hitsArr, commentArr, recomsArr, htmlId, text) {
			showLine : function(timeArr, hitsArr, commentArr, recomsArr, htmlId) {
				var myChart = echarts.init(document.getElementById(htmlId));
				option = {
					title : {
						text: '周统计',
					},
					tooltip : {
						trigger: 'axis'
					},
					legend: {
						data:['点击量','评论量','推荐量']
					},
					calculable : true,
					xAxis : [
						{
							type : 'category',
							boundaryGap : false,
							data : timeArr
						}
					],
					yAxis : [
						{
							type : 'value',
							axisLabel: {
								formatter: '{value} '
							}
						}
					],
					series : [
						{
							name:'点击量',
							type:'line',
							data:hitsArr,
							markPoint : {
								data : [
									{type : 'min', name: '最小值'}
								]
							},
							markLine : {
								data : [
									{type : 'average', name: '平均值'}
								]
							}
						}, {
							name:'推荐量',
							type:'line',
							data:recomsArr,
							markPoint : {
								data : [
									{type : 'min', name: '最小值'}
								]
							},
							markLine : {
								data : [
									{type : 'average', name: '平均值'}
								]
							}
						}, {
							name:'评论量',
							type:'line',
							data:commentArr,
							markPoint : {
								data : [
									{type : 'min', name: '最小值'}
								]
							},
							markLine : {
								data : [
									{type : 'average', name: '平均值'}
								]
							}
						}]
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
                // TODO 要修改
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

				// TODO 有时间再修改以下字段图表
				// this.showDetailsAgain();
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