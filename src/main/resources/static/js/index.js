function initForm() {
    $('#declareStatus').combobox({
        // url:'combobox_data.json',
        valueField: 'id',
        textField: 'text',
        multiple: true,
        limitToList: true,
        // 已申报(0),未申报(1),部分申报(2)
        data: [{
            id: '0',
            text: '已申报'
        }, {
            id: '1',
            text: '未申报'
        }, {
            id: '2',
            text: '部分申报'
        }],
        onChange: function (newValue, oldValue) {
            $('#dg').datagrid('reload');
        }
    });
    // 点击搜索框触发搜索,
    $("#nameIcon").click(function () {
        $('#dg').datagrid('reload');
    });
    // 输入框回车触发搜索
    $('#name').bind('keypress', function (event) {
        if (event.keyCode == "13") {
            $("#nameIcon").click();
        }
    });
    // 全部状态下拉框的 placeholder,
    $("#declareStatus").siblings("span").find("input").attr("placeholder", $("#declareStatus").attr("placeholder"));
    //$("#declareStatus").siblings("span").find("input[name='declareStatus']").siblings("input").attr("placeholder", $("#declareStatus").attr("placeholder"));
}

/**
 * 刷新报税进度
 */
function refreshTaxDeclareProgress() {
    $('#taxDeclareProgress').datagrid("reload");
}

/**
 * 点击获取企业列表
 */
function getCorpList() {
    $("#nameIcon").click();
}

/**
 * 启动一键报税
 */
function startTaxDeclareStatus() {
    var msg = "确定启动一键报税?";
    var li = $('#dg').datagrid('getSelections');
    if (li.length == 0) {
        msg = "请确认启动<span style=\"color:red\">全部</span>列表一键报税";
    }
    toggleTaxDeclareStatus(msg, '1');
}

/**
 * 停止一键报税
 */
function stopTaxDeclareStatus() {
    toggleTaxDeclareStatus('确定停止一键报税?', '0');
}

/**
 * 启动或停止一键报税
 * @param status
 */
function toggleTaxDeclareStatus(msg, status) {
    $.messager.confirm({
        title: '提示',
        msg: '<div class="tip-text-area">'+msg+'</div>',
        ok: '确定',
        cancel: '取消',
        fn: function (r) {
            if (r) {
                $.ajax({
                    async: true,
                    contentType: "application/json",
                    dataType: "json",
                    url: contextPath + "/taxDeclareStatus/saveTaxDeclareStatus.json",
                    method: "post",
                    data: JSON.stringify({
                        status: status,
                        name: $.trim($("#name").val()),
                        declareStatus: $("#declareStatus").val(),
                        createM: "",
                        array: $('#dg').datagrid('getSelections')
                    }),
                    success: function (result) {
                        if (result.success == "true") {
                            //$.messager.alert('提示','操作成功!');
                        } else {
                            if (result.message) {
                                $.messager.alert('提示', result.message);
                            } else {
                                $.messager.alert('提示', '操作失败!');
                            }
                        }
                    }
                });
            }
        }
    });
}

/**
 * 退出系统
 */
function exitSystem() {
    $.messager.confirm({
        title: '提示',
        msg: "确定退出系统？",
        ok: '确定',
        cancel: '取消',
        fn: function (r) {
            if (r) {
                $.ajax({
                    async: true,
                    url: contextPath + "/exit.json",
                    data: {},
                    success: function (result) {
                        // do nothing
                        console.log("exit return success");
                        window.close();
                    },
                    error: function () {
                        // alert("error");
                        console.log("exit return error, maybe system is exited.");
                        $.messager.alert("提示", "程序已退出，请手动关闭浏览器！");
                        window.close();

                    }
                });
            }
        }
    });
}

function initGrid() {
    initCorpGrid();
    initTaxDeclareProgress();
}

/**
 * 初始化企业列表表格
 */
function initCorpGrid() {
    $('#dg').datagrid({
        height: 400,
        emptyMsg: "暂无数据",
        url: contextPath + '/corpList/getCorpList.json',
        pagination: true,
        rownumbers: true,
        striped: true,// 隔行变色
        checkOnSelect: true,
        columns: [[
            {field: 'id', title: '序号', width: 100, checkbox: true},
            {field: 'name', title: '企业名称', align: 'left', width: 330},
            {field: 'taxDateShow', title: '报税时间', width: 330},
            {
                field: 'declareStatus', title: '报税状态', width: 330, align: 'left'
                , formatter: function (value, row, index) {
                if (value == "0") {
                    return "<span class=\"c-green\">已申报</span>";
                } else if (value == "1") {
                    return "未申报";
                } else if (value == "2") {
                    return "部分申报";
                }
                return "";
            }
            }
        ]],
        onBeforeLoad: function (param) {
            param.name = $.trim($("#name").val());
            param.declareStatus = $("#declareStatus").val();
            param.createM = "";// 创建月份=申报月份 + 1

            return true;
        },
        // 如果后端的返回与 datagrid 要求的值不同,可以在这个方法里面进行修改,例如 datagrid 要求 total 和 rows,
        loadFilter: function (data) {
            // console.log("loadFilter");
            // console.log(data);
            /*
            var rowsLi = [];
            data.rows = rowsLi;
            */

            return data;
        },
        onLoadSuccess: function (data) {
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

/**
 * 初始化报税进度表格
 */
function initTaxDeclareProgress() {
    $('#taxDeclareProgress').datagrid({
        url: contextPath + "/taxDeclareProgress/getTaxDeclareProgress.json?date=" + (new Date().getTime()),
        pagination: false,
        rownumbers: false,
        checkOnSelect: false,
        emptyMsg: "暂无数据",
        columns: [[
            {field: 'startTimeShow', title: '报税启动时间', align: 'left', width: 130},
            {field: 'userTotalCount', title: '报税总企业数', align: 'left', width: 120},
            {
                field: 'userFinishCount', title: '已报完成数', align: 'left', width: 120
                , formatter: function (value, row, index) {
                var href = contextPath + "/taxDeclareDetail/gotoTaxDeclareDetail.htm";
                var result = "<a href=\"{href}\" style=\"text-decoration: underline\">{value}</a>";
                result = result.replace("{href}", href);
                result = result.replace("{value}", value);
                return result;
            }
            },
            {
                field: 'userFinishSuccessCount', title: '已报成功数', align: 'left', width: 120
                , formatter: function (value, row, index) {
                var href = contextPath + "/taxDeclareDetail/gotoTaxDeclareDetail.htm?detailStatus=4";
                var result = "<a href=\"{href}\" style=\"text-decoration: underline\">{value}</a>";
                result = result.replace("{href}", href);
                result = result.replace("{value}", value);
                return result;
            }
            },
            {
                field: 'userFinishFailCount', title: '已报失败数', align: 'left', width: 120
                , formatter: function (value, row, index) {
                var href = contextPath + "/taxDeclareDetail/gotoTaxDeclareDetail.htm?detailStatus=3";
                var result = "<a href=\"{href}\" style=\"text-decoration: underline\">{value}</a>";
                result = result.replace("{href}", href);
                result = result.replace("{value}", value);
                return result;
            }
            },
            {field: 'currentUser', title: '当前报税人', align: 'left', width: 180},
            {field: 'currentTaxTimeShow', title: '当前报税时间', align: 'left', width: 130},
            {field: 'timeSpendMinute', title: '已耗时(分钟)', align: 'left', width: 100}
        ]]
    });
    setTimeout(function () {
        setInterval(function () {
            $('#taxDeclareProgress').datagrid("load");
        }, 5 * 1000);
    }, 10 * 1000);
}

$(document).ready(function () {
    initForm();
    initGrid();
});
