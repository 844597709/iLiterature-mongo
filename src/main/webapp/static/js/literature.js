/**
 * Created by on 14-12-1.
 */
$(function () {
    // 首页信息js
    var homePage = {
        welcomeInfo: function () {
            var htmlinfo = '请先登录系统！';
            // cookie 获取指定名称的cookie的值
            var cookies = document.cookie;
            if (cookies) {
                var cookie = cookies.split("; ");
                for (var i = 0; i < cookie.length; i++) {
                    var temp = cookie[i].split("=");
                    if (temp[0] == "user") {
                        var iCookie = temp[1].split("$");
                        userInfo.userId = iCookie[0];
                        userInfo.userName = unescape(iCookie[1]);
                        userInfo.userTrueName = unescape(iCookie[3]);
                        userInfo.department = unescape(iCookie[5]);
                        userInfo.position = unescape(iCookie[6]);
                        htmlinfo = userInfo.userTrueName + ' （' + userInfo.userName + '）: 欢迎登陆！';
                        $("#welcomemsg").empty().append(htmlinfo);
                    }
                }
            }
        },
        countInfo: function () {
            // 统计信息
            var url = '../../handler/base/countInfo';
            $.post(url, function (data) {
                if (data.ret && data.data.result) {
                    var entry = data.data.result;
                    var iHtml = "";
                    iHtml = iHtml + "<a href='siteInfo.html?firstColuId=4'> 采集网站：" + entry.site
                        + "</a></br><a  href='authorInfo.html?firstColuId=2'>  作者：" + entry.author
                        + "</a> </br><a  href='workInfo.html?firstColuId=3'>  作品：" + entry.works + "</a>";
                    $('#countInfo').empty().append(iHtml);
                }
            });
        },
        /*作者类别统计*/
        showPie: function (htmlId) {
            var myChart1 = echarts.init(document.getElementById(htmlId));
            var myUrl = '../../handler/worksInfo/selectWorkType';
            var myData = '';
            $.post(myUrl, {
                "siteId": 0
            }, function (data) {
                if (data.ret) {
                    myData = data.data.data;
                }
            });
            option = {
                title: {
                    text: '作品类别统计',
                    x: 'center'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
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
                        center: ['50%', '55%'],
                        data: myData
                    }
                ]
            };
            // 为echarts对象加载数据
            myChart1.setOption(option);
        },
        /*作者性别统计*/
        showSex: function (htmlId) {
            var myChart2 = echarts.init(document.getElementById(htmlId));
            var myUrl = '../../handler/author/countInfoGender';
            var Boy = [];
            var Girl = [];
            var Other = [];
            //var All = [];
            $.post(myUrl, {
                "siteId": 0
            }, function (data) {
                if (data.ret) {
                    var myData = data.data.result;
                    Boy = myData.authorMan;
                    Girl = myData.authorWoman;
                    Other = myData.authorOther;
                    //All=myData.authorAll;
                }
            });
            option = {
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    data: ['男', '女', '未知']
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    x: 'left',
                    y: 'center',

                },
                calculable: true,
                series: [
                    {
                        name: '作者性别分布',
                        type: 'pie',
                        radius: ['50%', '70%'],
                        left: ['50%', '60%'],
                        itemStyle: {
                            normal: {
                                label: {
                                    show: false
                                },
                                labelLine: {
                                    show: false
                                }
                            },
                            emphasis: {
                                label: {
                                    show: true,
                                    position: 'center',
                                    textStyle: {
                                        fontSize: '30',
                                        fontWeight: 'bold'
                                    }
                                }
                            }
                        },
                        data: [
                            {value: Girl, name: '女'},
                            {value: Other, name: '未知'},
                            {value: Boy, name: '男'}
                        ]
                    }
                ]
            };
            // 为echarts对象加载数据
            myChart2.setOption(option);
        },
        /*最近更新情况--曲线图*/
        showUpdate: function (htmlId) {
            var myChart2 = echarts.init(document.getElementById(htmlId));
            var myUrl = '../../handler/worksInfo/selectWorkUpdateByTime';
            var Time = [];
            var Works = [];
            var AuthorNum = [];
            $.post(myUrl, {
                "siteId": 0
            }, function (data) {
                //var iHtml = "";
                if (data.ret) {
                    var myData = data.data.data;
                    $.each(myData, function (entryIndex, entry) {
                        Time[entryIndex] = $.trim(entry.time);
                        AuthorNum[entryIndex] = parseInt(entry.totalUpdateAuthors);
                        Works[entryIndex] = parseInt(entry.totalUpdateWorks);
                    });
                }
            });
            option = {
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['更新作品数', '更新作者数']
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    x: 'left',
                    y: 'center',
                    feature: {
                        mark: {show: true},
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: Time
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value} '
                        }
                    }
                ],
                series: [
                    {
                        name: '更新作品数',
                        type: 'line',
                        data: Works,
                        markPoint: {
                            data: [
                                {type: 'max', name: '最大作品数'},
                                {type: 'min', name: '最小作品数'}
                            ]
                        },
                        markLine: {
                            data: [
                                {type: 'average', name: '平均值'}
                            ]
                        }
                    },
                    {
                        name: '更新作者数',
                        type: 'line',
                        data: AuthorNum,
                        markPoint: {
                            data: [
                                {type: 'max', name: '最大作者数'},
                                {type: 'min', name: '最小作者数'}

                            ]
                        },
                        markLine: {
                            data: [
                                {type: 'average', name: '平均值'}
                            ]
                        }
                    }
                ]


            };
            // 为echarts对象加载数据
            myChart2.setOption(option);
        },
        /*作者区域百分比 --表格*/
        showMapList: function (data2, allAuthorNum, iLength) {
            var iHtml = "";
            var obj = this;
            var Prec = [];
            var NumOther = 0;
            var ListLength = 14;
            if (ListLength <= iLength) {
                for (var i = 0; i <= ListLength; i++) {
                    if (i <= ListLength - 1) {
                        Prec[i] = obj.Percentage(data2[i][1], allAuthorNum);
                        NumOther += data2[i][1];
                        iHtml = iHtml + "<tr><td>" + data2[i][0]
                            + "</td><td>" + data2[i][1] + "</td><td>"
                            + Prec[i] + "</td>";
                    }
                    else {
                        iHtml = iHtml + "<tr><td>" + "其他"
                            + "</td><td>" + (allAuthorNum - NumOther) + "</td><td>"
                            + obj.Percentage((allAuthorNum - NumOther), allAuthorNum) + "</td>";
                    }

                }
                iHtml += "</tr>";
                $('#AreaProvince').empty().append(iHtml);
            }
        },
        Percentage: function (number1, number2) {
            return (Math.round(number1 / number2 * 10000) / 100.00 + "%");// 小数点后两位百分比
        },
        //查询区域统计信息
        showAreaDetails: function () {
            var url = "../../handler/author/countInfoArea";
            var AreaData = {};
            var max = 0;
            var iLength = 0;
            var allAuthorNum = 0;
            $.post(url, {
                "siteId": 0
            }, function (data) {
                if (data.ret) {
                    AreaData = data.data.result;
                    //区域人数最大数，便于确定数值范围
                    max = data.data.maxProvinceNum;
                    //区域值的长度，便于显示区域百分比的列数
                    iLength = AreaData.length;
                    //区域所有作者数，便于算出区域省份的百分比
                    allAuthorNum = data.data.allAuthorNum;
                }
            });
            this.showMap(AreaData, max, iLength, allAuthorNum, "iCharts");
            return;
        },
        /*作者区域分布 --地图*/
        showMap: function (AreaData, max, iLength, allAuthorNum, htmlId) {
            var obj = this;
            var data2 = [];   //先声明一维
            for (var k = 0; k < 60; k++) {        //一维长度为i,i为变量，可以根据实际情况改变
                data2[k] = new Array();
                for (var j = 0; j < 2; j++) {      //一维数组里面每个元素数组可以包含的数量p，p也是一个变量；
                    data2[k][j] = '';       //这里将变量初始化，我这边统一初始化为空，后面在用所需的值覆盖里面的值
                }
            }
            var data1 = "[";
            for (var i = 0; i < AreaData.length; i++) {
                if (i == AreaData.length - 1) {
                    data1 += "{name:'" + AreaData[i].name + "',value:" + AreaData[i].value + "}";
                } else {
                    data1 += "{name:'" + AreaData[i].name + "',value:" + AreaData[i].value + "},";
                }
                data2[i][0] = AreaData[i].name;
                data2[i][1] = parseInt(AreaData[i].value);
            }
            data1 += "]";
            var myobj = eval(data1);
            data2 = data2.sort(function (x, y) {
                return (y[1] - x[1]);
            });
            obj.showMapList(data2, allAuthorNum, iLength);
            var myChart = echarts.init(document.getElementById(htmlId));
            option = {
                title: {
                    text: "作者区域分布",
                    x: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                dataRange: {
                    precision: 0,
                    min: 0,
                    max: (max - max % 100 + 100),
                    x: 'left',
                    y: 'bottom',
                    text: ['高', '低'],           // 文本，默认为数值文本
                    calculable: true
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    x: 'right',
                    y: 'center',
                },
                series: [
                    {
                        name: '作者数',
                        type: 'map',
                        mapType: 'china',
                        roam: false,
                        itemStyle: {
                            normal: {label: {show: true}},
                            emphasis: {label: {show: true}}
                        },
                        data: myobj
                    }
                ]
            };
            myChart.setOption(option);
        },

        init: function () {
            // 初始化welcomeInfo
            this.welcomeInfo();
            this.countInfo();
            this.showPie("iAparts");
            /*作者类别统计*/
            this.showUpdate("iUpdate");
            /*最新一周作品作者更新情况*/
            this.showSex("iSex");
            /*作者性别统计*/
            //*作者区域分布*/
            this.showAreaDetails();
            // this.showTable(1,0);
        }
    };
    homePage.init();
    showTop(1, "iData");
    showTable(1); //显示作者的top10
    sortclick();
    // showMap( "iCharts" );
    showAuthorList("work_Count", "work_List");
    showAuthorList("author_Count", "author_List");
    showAuthorList("work_List", "work_Count");
    showAuthorList("author_List", "author_Count");
    clickWorkSort("workHits", 1);
    clickWorkSort("workComments", 2);
    clickWorkSort("workRecommends", 3);
});
/**
 * 显示作品列表的top10
 */
function showTop(type, htmlId) {
    var url = '../../handler/worksInfo/selectHotTopWork';
    showWorkPost(url, type);
}
/*点击作品排序*/
function clickWorkSort(id, type) {
    $('#' + id).click(function () {
        showTop(type, "iData");
    });
}

function showAuthorList(id, id1) {
    $('#' + id).click(function () {
        $("#" + id1 + "1").hide();
        $('#' + id + "1").show();
    });
}

function showWorkPost(iUrl, type) {
    $.post(iUrl, {
        siteId: 0,
        field: type
    }, function (data) {
        if (data.ret && data.data.data) {
            var iHtml = "";
            $.each(data.data.data, function (entryIndex, entry) {
                if (entryIndex < 10) {
                    if (entry.workCommentsNum <= 0) {
                        entry.workCommentsNum = '无';
                    } else entry.workCommentsNum = transforms(entry.workCommentsNum);
                    if (entry.workTotalRecoms <= 0) {
                        entry.workTotalRecoms = '无';
                    } else  entry.workTotalRecoms = transforms(entry.workTotalRecoms);
                    if (entry.workTotalHits <= 0) {
                        entry.workTotalHits = '无';
                    } else entry.workTotalHits = transforms(entry.workTotalHits);
                    var lastUpdateTime = entry.workLastUpdateTime;
                    if (!entry.workLastUpdateTime)lastUpdateTime = '';
                    iHtml = iHtml + "<tr><td><a href='workDetail.html?firstColuId=3&workId=" + entry.workId + "'>" + entry.workTitle + "</a></td><td>" + entry.workAuthor
                        + "</td><td>" + entry.workType + "</td><td>" + entry.workTotalHits + "</td><td>" + entry.workCommentsNum
                        + "</td><td>" + entry.workTotalRecoms + "</td><td>"
                        + lastUpdateTime + "</td></tr>";
                }
            });
            $('#iData').empty().append(iHtml);
        }
    });
}

/*格式化描述*/
function titleFormat(titlestr, length) {
    var r = /[^\x00-\xff]/g;
    var tmp_str = titlestr.replace(r, "**");
    length = length * 2;
    if (tmp_str.length > length) {
        var m = Math.floor(length / 2);
        for (var i = m; i < titlestr.length; i++) {
            if (titlestr.substr(0, i).replace(r, "**").length >= (length - 1))
                return titlestr.substr(0, i) + "..";
        }
    }
    return titlestr;
}

/**
 * 显示作者列表的top10
 */
/*function showTable(field) {
 var url = '../../handler/author/viewAllAuthor';
 $.post(url, {
 "field":field,
 "desc":1,
 "siteId":0,
 }, function(data) {
 if (data.ret) {
 var iHtml = "";
 $.each(data.data.pageData[0].data, function(entryIndex, entry) {
 if(entryIndex<10){
 if(entry.workCommentsNum <=0){
 entry.workCommentsNum='无';
 }
 else entry.workCommentsNum=transforms(entry.workCommentsNum);
 if(entry.workTotalRecoms <=0){
 entry.workTotalRecoms='无';
 }
 else  entry.workTotalRecoms=transforms(entry.workTotalRecoms);
 if(entry.workTotalHits <=0){
 entry.workTotalHits='无';
 }
 else entry.workTotalHits=transforms(entry.workTotalHits);
 //var descript=titleFormat(entry.description,9);
 if(entry.authArea=="")entry.authArea="未知";
 iHtml = iHtml + "<tr><td><a href='authorDetail.html?firstColuId=2&authorId=" + entry.authId + "'>" + entry.authName
 + "</a></td><td>" +entry.authWorksNum + "</td><td>" + entry.authArea + "</a></td><td>" + entry.workTotalHits
 +" </td><td>" + entry.workCommentsNum  +"</td><td>" + entry.workTotalRecoms + "</td></tr>";
 }
 });
 $('#iData1').empty().append(iHtml);
 $("[rel=popover]").popover({
 placement : 'top',
 trigger : 'hover',
 html : 'true', // needed to show html of course
 });
 }
 });
 }*/
function showTable(field) {
    var url = '../../handler/author/selectHotTopAuthor';
    $.post(url, {
        "field": field,
        "desc": 1,
        "siteId": 0,
    }, function (data) {
        if (data.ret) {
            var iHtml = "";
            $.each(data.data.result, function (entryIndex, entry) {
                if (entryIndex < 10) {
                    if (entry.authWorksCommentsNum <= 0) {
                        entry.authWorksCommentsNum = '无';
                    }
                    else entry.authWorksCommentsNum = transforms(entry.authWorksCommentsNum);
                    if (entry.authWorksRecomsNum <= 0) {
                        entry.authWorksRecomsNum = '无';
                    }
                    else  entry.authWorksRecomsNum = transforms(entry.authWorksRecomsNum);
                    if (entry.authWorksHitsNum <= 0) {
                        entry.authWorksHitsNum = '无';
                    }
                    else entry.authWorksHitsNum = transforms(entry.authWorksHitsNum);
                    //var descript=titleFormat(entry.description,9);
                    if (entry.authArea == "")entry.authArea = "未知";
                    iHtml = iHtml + "<tr><td><a href='authorDetail.html?firstColuId=2&authorId=" + entry.authId + "'>" + entry.authName
                        + "</a></td><td>" + entry.authWorksNum + "</td><td>" + entry.authArea + "</a></td><td>" + entry.authWorksHitsNum
                        + " </td><td>" + entry.authWorksCommentsNum + "</td><td>" + entry.authWorksRecomsNum + "</td></tr>"
                }
            });
            $('#iData1').empty().append(iHtml);
            $("[rel=popover]").popover({
                placement: 'top',
                trigger: 'hover',
                html: 'true', // needed to show html of course
            });
        }
    });
}

function sortclick() {
    $(".sortclick").click(function () {
        /*if($(this).attr("class")=="icon-arrow-down"){
         $(this).attr("class","icon-arrow-up");
         showTable($(this).attr("id"),1);
         }
         else {
         $(this).attr("class","icon-arrow-down");*/
        showTable($(this).attr("id"));
        /*}*/
    });
}
/**
 * 数据转换
 */
function transforms(number) {
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
}
 