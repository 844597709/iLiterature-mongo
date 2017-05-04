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
		showMenu : function() {
			$("#topMenu").find('li').each(function() {
				$(this).removeClass();
			});
			var parentColuId = getRequest("firstColuId");
			authorInfo.iDate.siteId=getRequest("siteId");
			if(authorInfo.iDate.siteId==undefined)authorInfo.iDate.siteId=0;
			
			$("#firstColuId" + parentColuId + "").attr("class", "active"); // 添加菜单选中样式
			$("#pagination").empty();
		},
		/**
		 * 头部header逻辑
		 * 
		 * @param 参数列表
		 */
		searchData : function() {
			$('#site').change(function(){
				var siteId=$("#site").val();
				window.location.href="authorInfo.html?firstColuId=2&siteId="+siteId;
			});
			$("#searchButton").click(function() {
				var searchWord = "";
				var Autharea="";
				if ($("#searchWord").val() != null || $("#searchWord").val() != "") {
					searchWord = $("#searchWord").val();
				} 
				if($("#Autharea").val!=null || $("#Autharea").val!=""){
					Autharea=$("#Autharea").val();
				}
				var siteId=$("#site").val();
				searchWord=escape(searchWord);
				Autharea=escape(Autharea);
				window.location.href="authorInfoList.html?firstColuId=2&searchWord="+searchWord+"&area="+Autharea+"&siteId="+siteId;
			});
		},
		showAllSite:function(){
			var url="../../handler/site/selectSite";
			var html="<option value='0'>所有网站</option>";
			$.post(url,{},function(data){
				if(data.ret){
					$.each(data.data.result,function(index,item){
						if(item.siteId==authorInfo.iDate.siteId){
							$("#siteName").append(" <strong>"+item.siteName+"作者统计</strong>");
							html+="<option value='"+item.siteId+"' selected='selected'>"+item.siteName+"</option>";
						}else{
							html+="<option value='"+item.siteId+"'>"+item.siteName+"</option>";
						}
					});
					$("#site").empty().append(html);
					if(authorInfo.iDate.siteId==0){
						$("#siteName").append(" <strong>所有作者统计</strong>");
					}
				}
			});
		},
		showSexDetails : function() {
			//查询男女比例
			var url = '../../handler/author/countInfoGender';
			var sexArr=[];
			$.post(url, {
				//网站Id，每个网站作者不一样
				"siteId" : authorInfo.iDate.siteId
			}, function(data) {
				if (data.ret) {
					var myData = data.data.result;
					sexArr[0]=myData.authorMan;
					sexArr[1]=myData.authorWoman;
					sexArr[2]=myData.authorOther;
				}
			});
			this.showLine(sexArr, "iSexs");
			return;
		},
		showAreaDetails:function(){
			//查询区域统计信息
			var url="../../handler/author/countInfoArea";
			var AreaData={};
			var max=0;
			$.post(url,{
				"siteId":authorInfo.iDate.siteId
			},function(data){
				if(data.ret){
					AreaData=data.data.result;
					//区域人数最大数，便于确定数值范围
					max=data.data.maxProvinceNum;
				}
			});
			this.showMap(AreaData,max,"iAreas");
			return;
		},
		showLine : function(sexArr, htmlId) {
			var myChart = echarts.init(document.getElementById(htmlId));
			option = {
					title : {
						text :"作者性别分布",
					},
				    tooltip : {
				        trigger: 'item',
				        formatter: "{a} <br/>{b} : {c} ({d}%)"
				    },
				    legend: {				      
				        data:['男','女','未知']
				    },
				    toolbox: {
				        show : true,
				        orient : 'vertical',
				           x: 'left',
				           y: 'center',
				    },
				    calculable : true,
				    series : [
				        {
				            name:'',
				            type:'pie',
				            radius : ['50%', '70%'],
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
				            data:[
				                {value:sexArr[1], name:'女'},
				                {value:sexArr[2], name:'未知'},
				                {value:sexArr[0], name:'男'}
				            ]
				        }
				    ]
				};
			// 为echarts对象加载数据
			myChart.setOption(option);
		},
		showMap:function(AreaData,max,htmlId){
			var data1="[";
			for(var i=0;i<AreaData.length;i++){
				if(i==AreaData.length-1){
					data1+="{name:'"+AreaData[i].name+"',value:"+AreaData[i].value+"}";
				}else{
					data1+="{name:'"+AreaData[i].name+"',value:"+AreaData[i].value+"},";
				}
			}
			data1+="]";
			var myobj=eval(data1);
			var myChart = echarts.init(document.getElementById(htmlId));
			option = {
				    title : {
				        text: "作者区域分布",
				        x:'center'
				    },
				    tooltip : {
				        trigger: 'item'
				    },
				    dataRange: {
				    	precision:0,
				        min: 0,
				        max: (max-max%100+100),
				        x: 'left',
				        y: 'bottom',
				        text:['高','低'],           // 文本，默认为数值文本
				        calculable : true
				    },
				    toolbox: {
				        show: true,
				        orient : 'vertical',
				        x: 'right',
				        y: 'center',
				    },
				    roamController: {
				        show: true,
				        x: 'right',
				        mapTypeControl: {
				            'china': true
				        }
				    },
				    series : [
				        {
				            name: '作者数',
				            type: 'map',
				            mapType: 'china',
				            roam: false,
				            itemStyle:{
				                normal:{label:{show:true}},
				                emphasis:{label:{show:true}}
				            },
				            data:myobj
				        }
				    ]
				};
			myChart.setOption(option);
		},
		showAuthorWorks:function(datarange1,datarange2,datarange3,datarange4){
			//查询作者作品统计
			//var url="../../handler/author/countInfoNum";
			var url="../../handler/author/countInfoNumAll";
			var WorksData={};
			var worksCountValue=[];
			var totalHitsValue=[];
			var totalRecomsValue=[];
			var commentsNumValue=[];
			$.post(url,{
				"range1":datarange1,
				"range2":datarange2,
				"range3":datarange3,
				"range4":datarange4,
				"siteId":authorInfo.iDate.siteId
			},function(data){
				if(data.ret){
					WorksData=data.data.result;
					/*$.each(WorksData,function(index,item){
						name[index]=item.name;
						value[index]=parseInt(item.value);
					});*/
					var worksCount=WorksData.worksCount;
					$.each(worksCount,function(index,item){
						worksCountValue[index]=parseInt(item.value);
					});
					var totalHits=WorksData.totalHits;
					$.each(totalHits,function(index,item){
						totalHitsValue[index]=parseInt(item.value);
					});
					var totalRecoms=WorksData.totalRecoms;
					$.each(totalRecoms,function(index,item){
						totalRecomsValue[index]=parseInt(item.value);
					});
					var commentsNum=WorksData.commentsNum;
					$.each(commentsNum,function(index,item){
						commentsNumValue[index]=parseInt(item.value);
					});
				}
			});
			this.showWorks(totalHitsValue,"Hits");//点击量
			this.showWorks(worksCountValue,"Authorworks");//作品量
			this.showWorks(totalRecomsValue,"Recom"); //推荐数
			this.showWorks(commentsNumValue,"Comms"); //评论数
			return;
		},
		showWorks:function(value,htmlId){
			var text="作者点击量统计";
			var name=["0-10万","10-50万","50-100万","100-200万","200-300万","300-500万","500-700万","700-900万","900-1000万","1亿及以上"];
			if(htmlId=="Authorworks"){
				text="作者作品数统计";
				name=["0部","1部","2部","3部","4部","5部","6部及以上"];
			}else if(htmlId=="Comms"){
				text="作者评论数统计";
				name=["1-5条","5-10条","10-20条","20-30条","30-40条","40-50条","50-100条","100-200条","200-500条","500条及以上"]
			}else if(htmlId=="Recom"){
				text="作者推荐数统计";
				name=["1-1000","1000-5000","5000-1万","1-2万","2-3万","3-5万","5-7万","7-9万","9-10万","10万及以上"]
			}
			var myChart = echarts.init(document.getElementById(htmlId));
			option = {
					 title : {
					        text: text,
					 },
				    tooltip : {
				        trigger: 'axis'
				    },
				    legend: {
				        data:['作者人数']
				    },
				    calculable : true,
				    xAxis : [
				        {
				            type : 'category',
				            data : name
				        }
				    ],
				    yAxis : [
				        {
				            type : 'value'
				        }
				    ],
				    series : [
				        {
				            name:'作者人数',
				            type:'bar',
				            data:value,
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
			this.showSexDetails();
			this.showAreaDetails();
			this.showAllSite(); //网站查询下拉框
			//this.showAuthorWorks("0,1,2,3,4,5,6",4,"Authorworks");
			//this.showAuthorWorks("0,100000,500000,1000000,2000000,3000000,5000000,7000000,9000000,10000000",1,"Hits");
			//this.showAuthorWorks("0,5,10,20,30,40,50,100,200,500",2,"Comms");
			//this.showAuthorWorks("0,1000,5000,10000,20000,30000,50000,70000,90000,100000",3,"Recom");
			this.showAuthorWorks("0,100000,500000,1000000,2000000,3000000,5000000,7000000,9000000,10000000","1,5,10,20,30,40,50,100,200,500","1,1000,5000,10000,20000,30000,50000,70000,90000,100000","0,1,2,3,4,5,6");
			this.searchData();
		}
	};
	authorInfo.init();
});