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
 * @param month1 格式:201810
 * @param month2 格式:201811
 * @result 当 month1 与 month2 相隔
 */
function isMonthDiffOne(month1, month2) {
    if (!month1 || !month2) {
        return false;
    }
    if (!(/\d{6}/g.test(month1))) {
        return false;
    }
    if (!(/\d{6}/g.test(month2))) {
        return false;
    }
    // 相同年,
    if (month1.substring(0, 4) == month2.substring(0, 4)) {
        var monthSub1 = parseInt(month1.substring(4, 6));
        var monthSub2 = parseInt(month2.substring(4, 6));
        if (monthSub1 + 1 == monthSub2) {
            return true;
        }
    }
    // 不同年,
    if (parseInt(month1.substring(0, 4)) + 1 == parseInt(month2.substring(0, 4))) {
        var monthSub1 = parseInt(month1.substring(4, 6));
        var monthSub2 = parseInt(month2.substring(4, 6));
        if (monthSub1 == 12 && monthSub2 == 1) {
            return true;
        }
    }
    return false;
}

/**
 * @param month 格式:201810
 * @param month
 */
function getNextMonth(month) {
    if (!month) {
        return "";
    }
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

$(document).ready(function(){
    $("#createM").val(getCurrentMonth());

    $('#dg').datagrid({
        url: contextPath + '/taxDeclareDetail/getTaxDeclareDetail.json',
        pagination: true,
        columns:[[
            {field:'id',title:'id',width:100,hidden: true},
            {field:'name',title:'公司名称',halign: 'center',width:180},
            {field:'nationaltaxLoginname',title:'国税登录号',halign: 'center',width:200},
            {field:'month',title:'申报所属期（月份）',halign: 'center',width:150},
            {field:'sbywbm',title:'申报税种',halign: 'center',width:130
                ,formatter: function(value,row,index){
                    var dict = {};
                    dict["xmZzs"] = "增值税";
                    dict["xmXgmZzs"] = "小规模增值税";
                    dict["xmYbZzs"] = "一般增值税";
                    dict["xmYhs"] = "印花税";
                    dict["xmQtsl"] = "其他收入";
                    dict["xmDfjyfj"] = "地方教育附加";
                    dict["xmCswhjss"] = "城市维护建设税";
                    dict["xmJyffj"] = "教育费附加";
                    dict["xmCjrjybzj"] = "残疾人就业保障金";
                    dict["xmGrsds"] = "个人所得税";
                    dict["xmSbf"] = "社保费";
                    dict["xmQysds"] = "企业所得税";
                    return dict[value] || "";
                }
            },
            {field:'status',title:'状态',width:100,halign: 'center', align:'left'
                ,formatter: function(value,row,index){
                    if (value == 3) {
                        return "失败";
                    } else if (value == 4) {
                        return "成功";
                    }
                    return "";
                }
            },
            {field:'createTs',title:'创建时间',halign: 'center',width:160}
        ]],
        onBeforeLoad: function(param) {
            param.name = $("#name").val();
            param.nationaltaxLoginname = $("#nationaltaxLoginname").val();
            param.createM = getNextMonth(getCreateM());// 创建月份=申报月份 + 1

            return true;
        },
        // 如果后端的返回与 datagrid 要求的值不同,可以在这个方法里面进行修改,例如 datagrid 要求 total 和 rows,
        loadFilter: function(data){
            // console.log("loadFilter");
            // console.log(data);
            var rowsLi = [];
            var createMValue = getCreateM();
            for (var i = 0; i < data.rows.length; i++) {
                var line = data.rows[i];
                var templateObj = {
                    id: line.id,
                    name: line.name,
                    nationaltaxLoginname: line.nationaltaxLoginname
                };
                if (line.invtoryVOList && line.invtoryVOList.length > 0) {
                    var atLeastOne = false;
                    for (var j = 0; j < line.invtoryVOList.length; j++) {
                        var innerLine = line.invtoryVOList[j];
                        if (isMonthDiffOne(createMValue, innerLine.createM)) {
                            atLeastOne = true;
                            var obj = cloneObj(templateObj);
                            obj.month = createMValue;
                            obj.sbywbm = innerLine.sbywbm;
                            obj.status = innerLine.status;
                            obj.createTs = innerLine.createTsShow;
                            rowsLi.push(obj);
                        }
                    }
                    if (!atLeastOne) {
                        rowsLi.push(templateObj);
                    }
                } else {
                    rowsLi.push(templateObj);
                }
            }
            data.rows = rowsLi;

            return data;
        },
        onLoadSuccess: function(data) {
            // console.log("onLoadSuccess,");
        }
    });

    $("#queryBtn").click(function(){
        var value = $("#createM").val();
        if (value && !(/\d{6}/g.test(value))) {
            $.messager.alert('提示','申报所属期格式错误，正确格式为:201801');
        } else {
            $('#dg').datagrid('load');
        }
    });
});
