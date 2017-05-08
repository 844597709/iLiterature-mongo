var analyse = {};
$(function () {
    analyse = {
        myUrl: "",
        iDate: {
            siteId: 0,
        },
        /**
         * 头部header逻辑
         *
         * @param 参数列表
         */
        showMenu: function () {
            var parentColuId = getRequest("firstColuId");
            //var siteId= getRequest("siteId");
            $("#topMenu").find('li').each(function () {
                $(this).removeClass();
            });
            $("#firstColuId" + parentColuId + "").attr("class", "active"); // 添加菜单选中样式
            analyse.iDate.siteId = getRequest("siteId");
            if (analyse.iDate.siteId == undefined || analyse.iDate.siteId == null)
                analyse.iDate.siteId = 0;
            $("#pagination").empty();
        },

        searchData: function () {
            $('#site').change(function () {
                var siteId = $("#site").val();
                window.location.href = "workInfo.html?firstColuId=3&siteId=" + siteId;
            });
            $("#searchButton").click(function () {
                var searchWord = "";
                var siteId = "";
                if ($("#searchWord").val() != null || $("#searchWord").val() != "") {
                    searchWord = $("#searchWord").val();
                }
                siteId = $("#site").val();
                searchWord = escape(searchWord);
                siteId = escape(siteId);
                window.location.href = "workInfoList.html?firstColuId=3&searchWord=" + searchWord + "&siteId=" + siteId;
            });
        },
        /*网站列表的下拉框*/
        showSite: function () {
            var myUrl = '../../handler/site/selectSite';
            var infoHtml = '';
            $.post(myUrl, {}, function (data) {
                if (data.ret) {
                    infoHtml = "<option value='0'>所有网站</option>";
                    myData = data.data.result;
                    $.each(myData, function (entryIndex, entry) {
                        if (analyse.iDate.siteId == entry.siteId) {
                            $("#siteName").append(" <strong>" + entry.siteName + "作品统计</strong>");
                            infoHtml += "<option value='" + entry.siteId + "' selected='selected'>" + entry.siteName + "</option>";
                        }
                        else {
                            infoHtml += "<option value=" + entry.siteId + ">" + entry.siteName + "</option>";
                        }
                    });
                    $("#site").empty().append(infoHtml);
                    if (analyse.iDate.siteId == 0) {
                        $("#siteName").append(" <strong>所有作品统计</strong>");
                    }
                }
            });
        },

        /*作品类别统计*/
        showPie: function (htmlId) {
            var myChart1 = echarts.init(document.getElementById(htmlId));
            var myUrl = '../../handler/worksInfo/selectWorkType';
            var myData = '';
            $.post(myUrl, {
                "siteId": analyse.iDate.siteId
            }, function (data) {
                if (data.ret) {
                    myData = data.data.data;
                }
            });
            option = {
                title: {
                    text: '作品类别统计',
                    x: 'left'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'center',
                    data: ['其他类型']
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    x: 'left',
                    y: 'center',
                    feature: {
                        mark: {show: true},
                        dataView: {show: true, readOnly: false},
                        magicType: {
                            show: true,
                            orient: 'vertical',
                            x: 'right',
                            y: 'center',
                            type: ['pie', 'funnel'],
                            option: {
                                funnel: {
                                    x: '25%',
                                    width: '50%',
                                    funnelAlign: 'center',
                                    max: myData[0].value
                                }
                            }
                        },
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                calculable: true,
                series: [
                    {
                        name: '作品类别统计',
                        type: 'pie',
                        radius: '55%',
                        center: ['50%', '50%'],
                        data: myData
                    }
                ]
            };
            // 为echarts对象加载数据
            myChart1.setOption(option);
        },

        /**
         * 数据转换
         */
        transforms: function (number) {
            var chaneseNumber;
            var name = number.toString();
            nameLength = name.length;
            if (nameLength < 5) {
                chaneseNumber = number;
            }
            else {
                var i, j;
                i = parseInt(number / 10000);
                j = i.toString();
                chaneseNumber = j + "万";
            }
            return chaneseNumber;
        },
        //作品点击量统计
        /*showWorksHits: function () {
            var myObj = this;
            var myChart = echarts.init(document.getElementById("Hits"));
            var myUrl = '../../handler/worksInfo/selectWorkHit';
            var workHits = [];
            var totalWorks = [];
            var myData = '';
            $.post(myUrl, {
                "siteId": analyse.iDate.siteId
            }, function (data) {
                if (data.ret) {
                    myData = data.data.data;
                    $.each(myData, function (entryIndex, entry) {
                        workHits[entryIndex] = myObj.transforms(entry.name);
                        totalWorks[entryIndex] = parseInt(entry.value);
                    });
                }
            });
            option = {
                title: {
                    text: '作品点击量统计',
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['作品人数']
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        data: ['0-10万', '10-50万', '50-100万', '100-200万', '200-300万', '300-500万', '500-700万', '700-900万', '900-1000万', '1亿及以上']
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: '作品人数',
                        type: 'bar',
                        data: totalWorks,
                        markPoint: {
                            data: [
                                // {type : 'max', name: '最大值'},
                                {type: 'min', name: '最小值'}
                            ]
                        },
                        markLine: {
                            data: [
                                {type: 'average', name: '平均值'}
                            ]
                        }
                    }]
            };
            myChart.setOption(option);
        },*/
        /*showWorksComments: function () {
            var myObj = this;
            var myChart = echarts.init(document.getElementById("Comments"));
            var myUrl = '../../handler/worksInfo/selectWorkComment';
            var workComments = [];
            var totalWorks = [];
            var myData = '';
            $.post(myUrl, {
                "siteId": analyse.iDate.siteId
            }, function (data) {
                if (data.ret) {
                    myData = data.data.data;
                    $.each(myData, function (entryIndex, entry) {
                        workComments[entryIndex] = myObj.transforms(entry.name);
                        totalWorks[entryIndex] = parseInt(entry.value);
                    });
                }
            });
            option = {
                title: {
                    text: '作品评论数统计',
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['评论数']
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        data: ['1-5条', '5-10条', '10-20条', '20-30条', '30-40条', '40-50条', '50-100条', '100-200条', '200-500条', '500条及以上']
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: '评论数',
                        type: 'bar',
                        data: totalWorks,
                        markPoint: {
                            data: [
                                //{type : 'max', name: '最大值'},
                                {type: 'min', name: '最小值'}
                            ]
                        },
                        markLine: {
                            data: [
                                {type: 'average', name: '平均值'}
                            ]
                        }
                    }]
            };
            myChart.setOption(option);
        },*/
        /*showWorksRecoms: function () {
            var myObj = this;
            var myChart = echarts.init(document.getElementById("Recoms"));
            var myUrl = '../../handler/worksInfo/selectWorkRecom';
            var workRecoms = [];
            var totalWorks = [];
            var myData = '';
            $.post(myUrl, {
                "siteId": analyse.iDate.siteId
            }, function (data) {
                if (data.ret) {
                    myData = data.data.data;
                    $.each(myData, function (entryIndex, entry) {
                        workRecoms[entryIndex] = myObj.transforms(entry.name);
                        totalWorks[entryIndex] = parseInt(entry.value);
                    });
                }
            });
            option = {
                title: {
                    text: '作品推荐数统计',
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['推荐数']
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        data: ['1-1000', '1000-5000', '5000-1万', '1-2万', '2-3万', '3-5万', '5-7万', '7-9万', '9-10万', '10万及以上']
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: '推荐数',
                        type: 'bar',
                        data: totalWorks,
                        markPoint: {
                            data: [
                                //  {type : 'max', name: '最大值'},
                                {type: 'min', name: '最小值'}
                            ]
                        },
                        markLine: {
                            data: [
                                {type: 'average', name: '平均值'}
                            ]
                        }
                    }]
            };
            myChart.setOption(option);
        },*/

        showWorksHitsAndCommentsAndRecoms:function(datarange1,datarange2,datarange3){
            //查询作品统计
            var url="../../handler/worksInfo/countWorkInfoNumAll";
            var WorksData={};
            var totalHitsValue=[];
            var totalRecomsValue=[];
            var commentsNumValue=[];
            $.post(url,{
                "hitsRange":datarange1,
                "commentsRange":datarange2,
                "recomsRange":datarange3,
                "siteId":authorInfo.iDate.siteId
            },function(data){
                if(data.ret){
                    WorksData=data.data.result;
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
            this.showWorkHeader(totalHitsValue,"Hits");//点击量
            this.showWorkHeader(totalRecomsValue,"Recom"); //推荐数
            this.showWorkHeader(commentsNumValue,"Comms"); //评论数
            return;
        },
        showWorkHeader:function(value,htmlId){
            var text="作品点击量统计";
            var name=["0-10万","10-50万","50-100万","100-200万","200-300万","300-500万","500-700万","700-900万","900-1000万","1亿及以上"];
            if(htmlId=="Comms"){
                text="作品评论数统计";
                name=["1-5条","5-10条","10-20条","20-30条","30-40条","40-50条","50-100条","100-200条","200-500条","500条及以上"]
            }else if(htmlId=="Recom"){
                text="作品推荐数统计";
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

        init: function () {
            // 初始化showMenu
            this.showMenu();
            /*this.showData();*/
            this.searchData();
            this.showSite();
            this.showPie("worksType");

            /*this.showWorksHits();
            this.showWorksComments();
            this.showWorksRecoms();*/
            this.showWorksHitsAndCommentsAndRecoms("0,100000,500000,1000000,2000000,3000000,5000000,7000000,9000000,10000000","1,5,10,20,30,40,50,100,200,500","1,1000,5000,10000,20000,30000,50000,70000,90000,100000");
        }
    };
    analyse.init();
});