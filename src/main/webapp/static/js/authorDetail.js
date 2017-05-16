var authorInfo = {};
var showflag=false;
$(function() {
	authorInfo = {
			authorId : getRequest("authorId"),
			siteId:getRequest("siteId"),
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
				if(authorInfo.siteId==undefined)authorInfo.siteId=0;
				$("#firstColuId" + parentColuId + "").attr("class", "active"); // 添加菜单选中样式
				$("#pagination").empty();
			},
			/**
			 * 头部header逻辑
			 * 
			 * @param 参数列表
			 */
			showData : function() {
				if (this.authorId <= 0) {
					$('#iData').empty().append("没有相关数据");
					return;
				}
				var url = '../../handler/author/viewAuthorById';
				$.post(url, {
					"authorId" : this.authorId
				}, function(data) {
					var iHtml = "没有相关数据";
					if (data.ret) {
						iHtml = "";
						var item = data.data.result;
						var info = '';
						var gender = '';
						if(item.authGender!=null&&item.authGender!=""){
							gender=item.authGender;
						}
						/*if (item.gender == 2) {
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
						var description = '无';
						if (item.authDesc != null && item.authDesc != "") {
							description = item.authDesc;
						}
						if(item.authWorksRecomsNum<0){
							item.authWorksRecomsNum=0;
						}
						if(item.authWorksHitsNum<0){
							item.authWorksHitsNum=0;
						}
						if(item.authWorksCommentsNum<0){
							item.authWorksCommentsNum=0;
						}
						if(item.authWorksNum<0){
							item.authWorksNum=0;
						}
						var description1=titleFormat(description,9);
						info = item.authName + "$" + url + "$" + gender + "$" + area + "$" + description + "$" + inTime;
						iHtml += "<tr class=''><td class='tdcenter'><a id='"
							+ item.authId + "' name='" + info + "'  href='authorDetail.html?firstColuId=2&authorId="
							+ item.authId + "'>" + item.authName + "</a>" + "</td><td class='tdcenter'>" + gender
							+ "</td><td class='tdcenter'>" + area + "</td><td class='tdcenter' title='"+description+"'>"+description1
							+ "</td><td class='tdcenter'>" + item.crawlWebsiteList[0].crwsSiteName + "</td><td class='tdcenter'>"
							+ item.authWorksNum + "</td><td class='tdcenter'>" + transforms(item.authWorksHitsNum) + "</td><td class='tdcenter'>"
							+ transforms(item.authWorksCommentsNum) + "</td><td class='tdcenter'>" + transforms(item.authWorksRecomsNum)
							+ "</td><td class='tdcenter'><a title='" + url + "' href='" + url
							+ "'target='_blank'><i class='icon-globe'></i></a></td></tr>";
						$('#iData').append(iHtml);	
						
						if(item.authWorksNum >0){
							showflag=true;
						}
					}
					return;
				});
			},
			showDescription : function() {
				var workId=[];
				if (this.authorId <= 0) {
					$('#iData').empty().append("没有相关数据");
					return;
				}
				var url = '../../handler/worksInfo/selectWorksByAuthId';
				$.post(url,{"authorId" : this.authorId},function(data) {
					var iHtml="";
					if (data.ret) {
						var myData = data.data.data;
						if(myData!=''){
							if(myData[0].word=="none"){
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
						}
						$(function() {
					        $("#tagbox").append(iHtml);
					      });
						}
				});
			    
				return;
			},
			showDetails : function() {
				var workId=[];
				if (this.authorId <= 0) {
					$('#iData').empty().append("没有相关数据");
					return;
				}
				var url = '../../handler/worksInfo/selectByAuthor';
				var myData = '';
				$.post(url,{"authorId" : this.authorId},function(data) {
					var iHtml = "";
					var hideHtml="";
					if (data.ret) {
						myData = data.data.data;
						if(myData!=''){
							$.each(myData,function(itemIndex, item) {
								workId[itemIndex]=item.workId;
								if(item.workDesc.length>200){
									hideHtml="<li> 简介：<div id='short"+itemIndex+"'><span>"
									+ item.workDesc.substring(0,200)
									+ "</span><a href='javascript:void(0)' id='shorta' onclick='description(1,"+itemIndex+");'>(展开全部)</a></div>"
									+"<div id='showAll"+itemIndex+"' style='display:none'><span>"+item.workDesc+"</span>"
									+"<a href='javascript:void(0)'  id='showAlla' onclick='description(2,"+itemIndex+");'>(收缩)</a></div>"
									+"</li>";
								}else{
									hideHtml="<li>简介："+item.workDesc+"</li>";
								}
								iHtml += "<div class='accordion-group'>"
									+ "<div class='accordion-heading'>"
									+ "<a class='accordion-toggle' data-toggle='collapse' data-parent='#detail' href='#detail"
									+ item.workId
									+ "' >"
									+ item.workTitle
									+ "</a></div><div id='detail"
									+ item.workId
									+ "' class='accordion-body collapse in'>"
									+ "<div class='accordion-inner'><ul><li><a href='workDetailTemp.html?firstColuId=3&siteId="+authorInfo.siteId+"&workId="+
									item.workId
									+ "' target='_blank'> 作品："
									+ item.workTitle
									+ "</a></li><li> 类型："
									+ item.workType
									+ "</li><li>内容标签 :"
									+ item.workMark
									+ "</li>"+hideHtml+"<li> 书号："
									+ item.workNum
									+ " </li><li> 作品性质："
									+ item.workNature
									+ " </li><li> 授权状态/签约状态："
									+ item.workAuthorization
									+ " </li><li> 其他信息："
									+ item.workOtherInfo + " </li></ul></div></div></div>";
							});
						}
					}
					$('#detail').empty().append(iHtml);
				});
				if(myData!=''){
					this.showWorkDetail(myData);
				}
			},
			showWorkDetail:function(works){
				if(showflag){
					$("#iAttrs").show();
					$("#iAttrs1").show();
					var totalHits=[];
					var title=[];
					var comms=[];
					var recom=[];
					$.each(works,function(itemIndex, item){
						if(item.workTotalHits<0){
							item.workTotalHits=0;
						}
						if(item.workCommentsNum<0){
							item.workCommentsNum=0;
						}
						if(item.workTotalRecoms<0){
							item.workTotalRecoms=0;
						}
						totalHits[itemIndex]=item.workTotalHits;
						title[itemIndex]=item.workTitle;
						comms[itemIndex]=item.workCommentsNum;
						recom[itemIndex]=item.workTotalRecoms;
					});
					this.showLine(title, totalHits, "iAttrs", "作者作品");
					this.showLine1(title, totalHits, comms, recom, 'iAttrs1', "作者作品");
				}
			},
			/*showWorkDetail:function(workId){
				if(showflag){
					$("#iAttrs").show();
					$("#iAttrs1").show();
					var url="../../handler/worksInfo/selectWorkById";
					var totalHits=[];
					var title=[];
					var comms=[];
					var recom=[];
					for(var i=0;i<workId.length;i++){
						$.post(url,{
							"workId":workId[i]
						},function(data){
							if(data.ret){
								var item = data.data.data;
								if(item!=null){
									if(item.workTotalHits<0){
										item.workTotalHits=0;
									}
									if(item.workCommentsNum<0){
										item.workCommentsNum=0;
									}
									if(item.workTotalRecoms<0){
										item.workTotalRecoms=0;
									}
									totalHits[i]=item.workTotalHits;
									title[i]=item.workTitle;
									comms[i]=item.workCommentsNum;
									recom[i]=item.workTotalRecoms;
								}
							}
						});
					}
					this.showLine(title, totalHits, "iAttrs", "作者作品");
					this.showLine1(title, totalHits, comms, recom, 'iAttrs1', "作者作品");
				}
			},*/
			showAuthorAttrs:function(){
				var url="../../handler/author/viewAuthorUpdate";
				$.post(url,{
					"auupAuthId":this.authorId
				},function(data){
					if(data.ret){
						var myData=data.data.result;
						var iHtml = "<i class='icon-align-right'></i>作者属性<br/>" ;
						/*if(myData.length>0){
							if(myData[0].auupAttr1==null  ||  myData[0].auupAttr1==undefined){
								myData[0].auupAttr1="";
							}else{
								iHtml+=myData[0].auupAttr1;
							}
							if(myData[0].auupAttr2==null  ||  myData[0].auupAttr2==undefined){
								myData[0].auupAttr2="";
							}else{
								iHtml+="<br/>"+myData[0].auupAttr2;
							}
							if(myData[0].auupAttr3==null  ||  myData[0].auupAttr3==undefined){
								myData[0].auupAttr3="";
							}else{
								iHtml+="<br/>"+myData[0].auupAttr3;
							}
							if(myData[0].auupAttr4==null  ||  myData[0].auupAttr4==undefined){
								myData[0].auupAttr4="";
							}else{
								iHtml+="<br/>"+myData[0].auupAttr4;
							}
							if(myData[0].auupAttr5==null  ||  myData[0].auupAttr5==undefined){
								myData[0].auupAttr5="";
							}else{
								iHtml+="<br/>"+myData[0].auupAttr5;
							}
							if(myData[0].auupGrade!=-1){
								 iHtml +="<br/>他的等级："+myData[0].auupGrade+"<br/>";
							}
							$('#author').empty().append(iHtml);
						}*/
                        if(myData){
                            if(myData.auupAttr1==null  ||  myData.auupAttr1==undefined){
                                myData.auupAttr1="";
                            }else{
                                iHtml+=myData.auupAttr1;
                            }
                            if(myData.auupAttr2==null  ||  myData.auupAttr2==undefined){
                                myData.auupAttr2="";
                            }else{
                                iHtml+="<br/>"+myData.auupAttr2;
                            }
                            if(myData.auupAttr3==null  ||  myData.auupAttr3==undefined){
                                myData.auupAttr3="";
                            }else{
                                iHtml+="<br/>"+myData.auupAttr3;
                            }
                            if(myData.auupAttr4==null  ||  myData.auupAttr4==undefined){
                                myData.auupAttr4="";
                            }else{
                                iHtml+="<br/>"+myData.auupAttr4;
                            }
                            if(myData.auupAttr5==null  ||  myData.auupAttr5==undefined){
                                myData.auupAttr5="";
                            }else{
                                iHtml+="<br/>"+myData.auupAttr5;
                            }
                            if(myData.auupGrade!=-1){
                                iHtml +="<br/>他的等级："+myData.auupGrade+"<br/>";
                            }
                            $('#author').empty().append(iHtml);
                        }
					}
				});
			},
			showLine : function(title,totalHits, htmlId, text) {
				var myChart = echarts.init(document.getElementById(htmlId));
				//点击数排序，找出其中最大的10个，更多的就显示为其他
				var count=10;
				if(title.length>count){
					var data2=[];   //先声明一维
					for(var k=0;k<title.length;k++){        //一维长度为i,i为变量，可以根据实际情况改变
						data2[k]=new Array(); 
						for(var j=0;j<2;j++){      //一维数组里面每个元素数组可以包含的数量p，p也是一个变量；
							data2[k][j]='';       //这里将变量初始化，我这边统一初始化为空，后面在用所需的值覆盖里面的值
						}
					}
					for(var i=0;i<title.length;i++){
						data2[i][0]=title[i];
						data2[i][1]=totalHits[i];
					}
					data2=data2.sort(function(x,y){return y[1]-x[1]});
					for(var i=0;i<title.length;i++){
						title[i]=data2[i][0];
						totalHits[i]=data2[i][1];
					}
					var other=0;
					for(i=count;i<title.length;i++){
						other+=totalHits[i];
					}
					title[count]='其他';
					totalHits[count]=other;
					title.length=(count+1);
				}
				var data="[";
				for(var i=0;i<title.length;i++){
					if(i==title.length-1){
						data+="{name:'"+title[i]+"','value':"+totalHits[i]+"}";
					}else{
						data+="{name:'"+title[i]+"','value':"+totalHits[i]+"},";
					}
				}
				data+="]";
				var myobj=eval(data);
				option = {
					    tooltip : {
					        trigger: 'item',
					        formatter: "{a} <br/>{b} : {c} ({d}%)"
					    },
					    legend: {				      
					        data:title
					    },
					    toolbox: {
					        show : true,
					        orient : 'vertical',
					           x: 'left',
					           y: 'center',
					        feature : {
					            mark : {show: true},
					            dataView : {show: true, readOnly: false},
					            magicType : {
					                show: true, 
					                type: ['pie', 'funnel'],
					                option: {
					                    funnel: {
					                        x: '25%',
					                        width: '50%',
					                        funnelAlign: 'center',
					                        max: 1548
					                    }
					                }
					            },
					            restore : {show: true},
					            saveAsImage : {show: true}
					        }
					    },
					    calculable : true,
					    series : [
					        {
					            name:'作品点击量统计',
					            type:'pie',
					            radius : ['50%', '70%'],
					            center:['50%','60%'],
					            itemStyle : {
					                normal : {
					                    label : {
					                        show : false
					                    },
					                    labelLine : {
					                        show : false
					                    }
					                },
					                emphasis : {
					                    label : {
					                        show : true,
					                        position : 'center',
					                        textStyle : {
					                            fontSize : '30',
					                            fontWeight : 'bold'
					                        }
					                    }
					                }
					            },
					            data:myobj
					        }
					    ]
					};
				// 为echarts对象加载数据
				myChart.setOption(option);
			},
			showLine1:function(title,totleHits,comms,recoms,htmlId,text){
				var myChart = echarts.init(document.getElementById(htmlId));
				option = {
						 title : {
						        text: text,
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
					            data : title
					        }
					    ],
					    yAxis : [
					        {
					            type : 'value'
					        }
					    ],
					    series : [
					        {
					            name:'点击量',
					            type:'bar',
					            data:totleHits,
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
					            type:'bar',
					            data:recoms,
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
					            type:'bar',
					            data:comms,
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
				myChart.setOption(option);
			},
			
			init : function() {
				// 初始化showMenu
				this.showMenu();
				this.showData();
				this.showDescription();
				this.showDetails();
				this.showAuthorAttrs();
			}
	};
	authorInfo.init();

});
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
function description(type,itemIndex){
		if(type==1){
			$("#short"+itemIndex).hide();
			$("#showAll"+itemIndex).show();
		}else{
			$("#showAll"+itemIndex).hide();
			$("#short"+itemIndex).show();
		}
}
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
