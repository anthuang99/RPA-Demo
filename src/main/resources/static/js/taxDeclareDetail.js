function cloneObj(obj) {
    var result = {};
    for (var item in obj) {
        result[item] = obj[item];
    }
    return result;
}

function getCurrentMonth() {
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth();
    if (month == 0) {
        year -= 1;
    }
    if (month < 10) {
        month = "0" + month;
    }
    return date.getFullYear() + "" + date.getMonth();
}

function getCreateM() {
    var value = $("#createM").val();
    if (!value) {
        return "";
    }
    if (!(/\d{6}/g.test(value))) {
        return getCurrentMonth();
    }
    return value;
}

/**
 * @param month 格式:201810
 * @param month
 */
function getNextMonth(month) {
    if (!month) {
        return "";
    }
    month = month.replace("-", "");
    if (!(/\d{6}/g.test(month))) {
        return "";
    }
    var year = parseInt(month.substring(0, 4));
    var monthSub1 = parseInt(month.substring(4, 6));
    if (monthSub1 == 12) {
        year += 1;
        return year + "01";
    }
    monthSub1 += 1;
    if (monthSub1 < 10) {
        return year + "0" + monthSub1;
    }
    return year + "" + monthSub1;
}

/**
 * @param year
 * @param month
 * @param offset [1-12]
 */
function getYyyyMMOffset(year, month, offset) {
    month = month + offset;
    if (month > 12) {
        year += 1;
        month -= 12;
    } else if (month < 1) {
        year -= 1;
        month += 12;
    }
    if (month < 10) {
        return year + "-0" + month;
    }
    return year + "-" + month;
}

function initForm() {
    // 1.申报所属期(月份),最近的 12 个月,从上个月开始算,
    var dataLi = [];
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth();
    for (var i = 0; i > -12; i--) {
        var yyyyMM = getYyyyMMOffset(year, month, i);
        dataLi.push({
            id: yyyyMM.replace("-", ""),
            text: yyyyMM
        });
    }
    $('#createM').combobox({
        valueField:'id',
        textField:'text',
        multiple:false,
        limitToList: true,
        data: dataLi
    });
    // 2.报税状态,成功/失败
    $('#detailStatus').combobox({
        valueField:'id',
        textField:'text',
        multiple:false,
        limitToList: true,
        // 成功(4),失败(3包含税种为空)
        data: [{
            id: "4",
            text: "成功"
        },{
            id: "3",
            text: "失败"
        }]
    });
    var yyyyMM = getYyyyMMOffset(year, month, 0);
    $('#createM').combobox('setValue', yyyyMM);
    if (detailStatusParam) {
        $('#detailStatus').combobox('setValue', detailStatusParam);
    }

    // 全部状态下拉框的 placeholder,
    $("#createM").siblings("span").find("input[name='createM']").siblings("input").attr("placeholder", $("#createM").attr("placeholder"));
    $("#detailStatus").siblings("span").find("input[name='detailStatus']").siblings("input").attr("placeholder", $("#detailStatus").attr("placeholder"));

    // 输入框回车触发搜索
    $('#name').bind('keypress', function (event) {
        if (event.keyCode == "13") {
            search();
        }
    });
}

function search() {
    $('#dg').datagrid('reload');
}

function exportFunc() {
    document.forms["exportForm"].name.value = $.trim($("#name").val());
    document.forms["exportForm"].detailStatus.value = $("#detailStatus").val();
    document.forms["exportForm"].createM.value = getNextMonth($("#createM").val());
    document.forms["exportForm"].submit();
}

function showImg(id) {
    var rowLi = $("#dg").datagrid("getRows");
    var row = null;
    for (var i = 0; i < rowLi.length; i++) {
        if (rowLi[i].id == id) {
            row = rowLi[i];
        }
    }
    var content = "<img src=\"data:image/png;base64,{base64}\" />";
    content = content.replace(/{base64}/g, row.sbqkImg);
    var width = $(window).width();
    var height = $(window).height();
    showDialog({
        modal: true,
        maximizable: true,
        title: "查看图片",
        width: width - 20,
        height: height - 20,
        content: content,
        iconCls: 'icon-save'
    });
}

function initGrid() {
    $('#dg').datagrid({
        height: $(window).height() - 220,
        emptyMsg: "暂无数据",
        url: contextPath + '/taxDeclareDetail/getTaxDeclareDetail.json',
        pagination: true,
        rownumbers: true,
        striped: true,// 隔行变色
        checkOnSelect : true,
        columns:[[
            {field:'id',title:'id',width:100,hidden: true},
            {field:'status',title:'status',width:100,hidden: true},
            {field:'name',title:'公司名称',width:180},
            {field:'nationaltaxLoginname',title:'国税登录号',width:200},
            {field:'createMShow',title:'申报所属期（月份）',width:150},
            {field:'sbywbmShow',title:'申报税种',width:100},
            {field:'statusShow',title:'状态',width:70, align:'left'
                ,formatter: function(value,row,index){
                    if (value) {
                        if (row.status == "4") {// 成功
                            return value;
                        } else if (row.status == "3") {// 失败
                            return "<span class=\"c-red\">" + value + "</span>";
                        }
                    }
                    return value;
                }
            },
            {field:'sbqkImg',title:'截图',width:130
                ,formatter: function(value,row,index){
                    if (value) {
                        var html = "<a href=\"javascript:showImg('{id}')\"><img src=\"data:image/png;base64,{base64}\" height=\"32px\" /></a>";
                        html = html.replace(/{id}/g, row.id);
                        html = html.replace(/{base64}/g, value);
                        return html;
                    }
                    return "";
                }
            },
            {field:'createTsShow',title:'创建时间',width:150}
        ]],
        onBeforeLoad: function(param) {
            param.name = $.trim($("#name").val());
            param.createM = getNextMonth($("#createM").val());
            param.detailStatus = $("#detailStatus").val();

            return true;
        },
        // 如果后端的返回与 datagrid 要求的值不同,可以在这个方法里面进行修改,例如 datagrid 要求 total 和 rows,
        loadFilter: function(data){
            // console.log("loadFilter");
            // console.log(data);
            /*
            var rowsLi = [];
            data.rows = rowsLi;
            */

            return data;
        },
        onLoadSuccess: function(data) {
            // console.log("onLoadSuccess,");
        }
    });
    var pager = $('#dg').datagrid('getPager');
    pager.pagination({
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '共{total}条数据'
    });
}



$(document).ready(function(){
    initForm();
    initGrid();
});
