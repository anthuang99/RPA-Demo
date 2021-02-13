function onDialogMove(x,y) {
    //alert("(left,top)==>("+x+","+y+")");
    if (y<0) {
        $("#div_for_easyui_window").dialog('move',{left:x, top:0});
    }
}

function closeDialog(){
    if ($("#div_for_easyui_window")){
        $("#div_for_easyui_window").dialog('destroy');
    };
}

function showDialog(options){
    if ($("#div_for_easyui_window")){
        $("#div_for_easyui_window").dialog('destroy');
    };
    //隐藏最小化、收起、最大化按钮
    options = $.extend(options, {collapsible: false, minimizable : false, maximizable: false, resizable: true});
    //标题处理
    if(options.title == null || options.title == '') {
        options.title = '.';
    }
    //移动事件
    if(options.onMove == null || options.onMove == '') {
        options.onMove = onDialogMove;
    }
    //自适应大小
    var clientWidth = $(this).width();
    var clientHeight = $(this).height();
    if(options.width && options.width > clientWidth-15) {
        options.width = clientWidth-15;
    } else if (!options.width) {
        options.width = clientWidth-15;
    }
    if(options.height && options.height > clientHeight-20) {
        options.height = clientHeight-20;
    } else if (!options.height) {
        options.height = clientHeight-20;
    }
    var href = options.href;
    options.href = '';
    if(href == null || href == '') {
        href = options.url;
    }
    if(href) {
        var date = new Date();
        if(href.indexOf('?') != -1) {
            href = href + '&ms_=' + date.getTime();
        } else {
            href = href + '?ms_=' + date.getTime();
        }
        var iframeHtml = "<div id='div_for_easyui_window'><iframe class='iframe_for_easyui_window' style='{style}' frameborder='0' src='"+href+"'></iframe></div>";
        var style="width: {width}px; height: {height}px;";
        style = style.replace(/{width}/g, options.width - 30);
        style = style.replace(/{height}/g, options.height - 10);
        iframeHtml = iframeHtml.replace(/{style}/g, style);
        return $(iframeHtml).dialog(options);
    } else if (options.content) {
        var html = "<div id='div_for_easyui_window'>{content}</div>";
        html = html.replace(/{content}/g, options.content);
        return $(html).dialog(options);
    }
}

/**
 * 账益达企业登录账号/密码设置,弹出框点击确定
 */
function corpSettingOkFunc() {
    var username = $.trim($("input[name='username']", corpSettingDialog).val());
    var password = $.trim($("input[name='password']", corpSettingDialog).val());
    var domainName = $.trim($("input[name='domainName']", corpSettingDialog).val());
    if (!username || !password) {
        $.messager.alert('提示','用户名或密码为空!');
        return;
    }

    $.ajax({
        async: true,
        url: contextPath + "/corpSetting/saveCorpSetting.json",
        method: "post",
        contentType: "application/json",
        data: JSON.stringify({
            username: username,
            password: password,
            domainName: domainName
        }),
        success: function (result) {
            closeDialog();
            applyCorpSettingBodyData();
        }
    });
}

/**
 * 启动一键报税
 */
function startTaxDeclareStatus() {
    toggleTaxDeclareStatus('确定启动一键报税?', '1');
}

/**
 * 暂停一键报税
 */
function stopTaxDeclareStatus() {
    toggleTaxDeclareStatus('确定暂停一键报税?', '0');
}

/**
 * 启动或停止一键报税
 * @param status
 */
function toggleTaxDeclareStatus(msg, status) {
    $.messager.confirm({
        title: '提示',
        msg: msg,
        ok: '确定',
        cancel: '取消',
        fn: function(r){
            if (r){
                $.ajax({
                    async: true,
                    url: contextPath + "/taxDeclareStatus/saveTaxDeclareStatus.json",
                    method: "post",
                    data: {
                        status: status
                    },
                    success: function (result) {
                        applyTaxParameterBodyData();
                    }
                });
            }
        }
    });
}

/**
 * 账益达企业登录账号/密码设置,弹出框点击取消
 */
function corpSettingCancelFunc() {
    closeDialog();
}

/**
 * 账益达企业登录账号/密码设置弹窗
 */
var corpSettingDialog = null;
function showCorpSettingDialog() {
    var content = UnderscoreUtil.getHtmlById("corpSettingDialogTemplate", {});
    corpSettingDialog = showDialog({
        modal: true,
        title: "账益达企业登录账号/密码设置",
        width: 400,
        height: 200,
        content: content,
        iconCls: 'icon-save',
        buttons: [{
            text: '确定',
            iconCls: 'icon-ok',
            handler: corpSettingOkFunc
        }, {
            text: '取消',
            handler: corpSettingCancelFunc
        }],
        onOpen: function () {
            var self = this;
            $.ajax({
                async: false,
                url: contextPath + "/corpSetting/getCorpSetting.json?date=" + (new Date().getTime()),
                data: {},
                success: function (result) {
                    if (result.corpSetting) {
                        $("input[name='username']", self).val(result.corpSetting.username);
                        $("input[name='password']", self).val(result.corpSetting.password);
                        $("input[name='domainName']", self).val(result.corpSetting.domainName);
                    }
                }
            });
        }
    });
}

/**
 * 并行报税用户数,弹出框点击确定
 */
function taxThreadCountOkFunc() {
    var taxThreadCount = $.trim($("input[name='taxThreadCount']", taxThreadCountDialog).val());
    if (!taxThreadCount) {
        $.messager.alert('提示','并行报税用户数为空!');
        return;
    }
    if (!(/^\d+$/.test(taxThreadCount))) {
        $.messager.alert('提示','并行报税用户数必须为数字!');
        return;
    }

    $.ajax({
        async: false,
        url: contextPath + "/taxParameter/saveTaxThreadCount.json",
        method: "post",
        //contentType: "application/json",
        data: {
            taxThreadCount: taxThreadCount
        },
        success: function (result) {
            closeDialog();
            applyTaxParameterBodyData();
        }
    });
}

/**
 * 并行报税用户数,弹出框点击取消
 */
function taxThreadCountCancelFunc() {
    closeDialog();
}

/**
 * 参数设置,修改并行报税用户数
 */
var taxThreadCountDialog = null;
function showTaxThreadCountDialog() {
    var content = UnderscoreUtil.getHtmlById("taxThreadCountDialogTemplate", {});
    taxThreadCountDialog = showDialog({
        modal: true,
        title: "并行报税用户数设置",
        width: 400,
        height: 200,
        content: content,
        iconCls: 'icon-save',
        buttons: [{
            text: '确定',
            iconCls: 'icon-ok',
            handler: taxThreadCountOkFunc
        }, {
            text: '取消',
            handler: taxThreadCountCancelFunc
        }],
        onOpen: function () {
            var self = this;
            $.ajax({
                async: false,
                url: contextPath + "/taxParameter/getTaxParameter.json?date=" + (new Date().getTime()),
                data: {},
                success: function (result) {
                    if (result.taxParameterLi) {
                        for (var i = 0; i < result.taxParameterLi.length; i++) {
                            if (result.taxParameterLi[i].key == "taxThreadCount") {
                                $("input[name='taxThreadCount']", self).val(result.taxParameterLi[i].value);
                            }
                        }
                    }
                }
            });
        }
    });
}

/**
 * 验证码X偏移,弹出框点击确定
 */
function checkcodeOffsetXOkFunc() {
    var checkcodeOffsetX = $.trim($("input[name='checkcodeOffsetX']", checkcodeOffsetXDialog).val());
    if (!checkcodeOffsetX) {
        $.messager.alert('提示','验证码X偏移为空!');
        return;
    }
    if (!(/^[-\d]+$/.test(checkcodeOffsetX))) {
        $.messager.alert('提示','验证码X偏移必须为数字!');
        return;
    }

    $.ajax({
        async: false,
        url: contextPath + "/taxParameter/saveCheckcodeOffsetX.json",
        method: "post",
        //contentType: "application/json",
        data: {
            checkcodeOffsetX: checkcodeOffsetX
        },
        success: function (result) {
            closeDialog();
            applyTaxParameterBodyData();
        }
    });
}

/**
 * 验证码X偏移,弹出框点击取消
 */
function checkcodeOffsetXCancelFunc() {
    closeDialog();
}

/**
 * 参数设置,修改验证码X偏移
 */
var checkcodeOffsetXDialog = null;
function showCheckcodeOffsetXDialog() {
    var content = UnderscoreUtil.getHtmlById("checkcodeOffsetXDialogTemplate", {});
    checkcodeOffsetXDialog = showDialog({
        modal: true,
        title: "验证码X偏移设置",
        width: 400,
        height: 200,
        content: content,
        iconCls: 'icon-save',
        buttons: [{
            text: '确定',
            iconCls: 'icon-ok',
            handler: checkcodeOffsetXOkFunc
        }, {
            text: '取消',
            handler: checkcodeOffsetXCancelFunc
        }],
        onOpen: function () {
            var self = this;
            $.ajax({
                async: false,
                url: contextPath + "/taxParameter/getTaxParameter.json?date=" + (new Date().getTime()),
                data: {},
                success: function (result) {
                    if (result.taxParameterLi) {
                        for (var i = 0; i < result.taxParameterLi.length; i++) {
                            if (result.taxParameterLi[i].key == "checkcodeOffsetX") {
                                $("input[name='checkcodeOffsetX']", self).val(result.taxParameterLi[i].value);
                            }
                        }
                    }
                }
            });
        }
    });
}

/**
 * 验证码Y偏移,弹出框点击确定
 */
function checkcodeOffsetYOkFunc() {
    var checkcodeOffsetY = $.trim($("input[name='checkcodeOffsetY']", checkcodeOffsetYDialog).val());
    if (!checkcodeOffsetY) {
        $.messager.alert('提示','验证码Y偏移为空!');
        return;
    }
    if (!(/^[-\d]+$/.test(checkcodeOffsetY))) {
        $.messager.alert('提示','验证码Y偏移必须为数字!');
        return;
    }

    $.ajax({
        async: false,
        url: contextPath + "/taxParameter/saveCheckcodeOffsetY.json",
        method: "post",
        //contentType: "application/json",
        data: {
            checkcodeOffsetY: checkcodeOffsetY
        },
        success: function (result) {
            closeDialog();
            applyTaxParameterBodyData();
        }
    });
}

/**
 * 验证码Y偏移,弹出框点击取消
 */
function checkcodeOffsetYCancelFunc() {
    closeDialog();
}

/**
 * 参数设置,修改验证码Y偏移
 */
var checkcodeOffsetYDialog = null;
function showCheckcodeOffsetYDialog() {
    var content = UnderscoreUtil.getHtmlById("checkcodeOffsetYDialogTemplate", {});
    checkcodeOffsetYDialog = showDialog({
        modal: true,
        title: "验证码Y偏移设置",
        width: 400,
        height: 200,
        content: content,
        iconCls: 'icon-save',
        buttons: [{
            text: '确定',
            iconCls: 'icon-ok',
            handler: checkcodeOffsetYOkFunc
        }, {
            text: '取消',
            handler: checkcodeOffsetYCancelFunc
        }],
        onOpen: function () {
            var self = this;
            $.ajax({
                async: false,
                url: contextPath + "/taxParameter/getTaxParameter.json?date=" + (new Date().getTime()),
                data: {},
                success: function (result) {
                    if (result.taxParameterLi) {
                        for (var i = 0; i < result.taxParameterLi.length; i++) {
                            if (result.taxParameterLi[i].key == "checkcodeOffsetY") {
                                $("input[name='checkcodeOffsetY']", self).val(result.taxParameterLi[i].value);
                            }
                        }
                    }
                }
            });
        }
    });
}

/**
 * 浏览器路径,弹出框点击确定
 */
function browserPathOkFunc() {
    var browserPath = $.trim($("input[name='browserPath']", browserPathDialog).val());
    if (!browserPath) {
        $.messager.alert('提示','浏览器路径为空!');
        return;
    }

    $.ajax({
        async: false,
        url: contextPath + "/taxParameter/saveBrowserPath.json",
        method: "post",
        //contentType: "application/json",
        data: {
            browserPath: browserPath
        },
        success: function (result) {
            closeDialog();
            applyTaxParameterBodyData();
        }
    });
}

/**
 * 浏览器路径,弹出框点击取消
 */
function browserPathCancelFunc() {
    closeDialog();
}

/**
 * 参数设置,修改浏览器路径
 */
var browserPathDialog = null;
function showBrowserPathDialog() {
    var content = UnderscoreUtil.getHtmlById("browserPathDialogTemplate", {});
    browserPathDialog = showDialog({
        modal: true,
        title: "浏览器路径设置",
        width: 400,
        height: 200,
        content: content,
        iconCls: 'icon-save',
        buttons: [{
            text: '确定',
            iconCls: 'icon-ok',
            handler: browserPathOkFunc
        }, {
            text: '取消',
            handler: browserPathCancelFunc
        }],
        onOpen: function () {
            var self = this;
            $.ajax({
                async: false,
                url: contextPath + "/taxParameter/getTaxParameter.json?date=" + (new Date().getTime()),
                data: {},
                success: function (result) {
                    if (result.taxParameterLi) {
                        for (var i = 0; i < result.taxParameterLi.length; i++) {
                            if (result.taxParameterLi[i].key == "browserPath") {
                                $("input[name='browserPath']", self).val(result.taxParameterLi[i].value);
                            }
                        }
                    }
                }
            });
        }
    });
}

/**
 * 账益达接口测试
 */
function pingZhangYiDaRequest() {
    $.ajax({
        async: true,
        url: contextPath + "/corpSetting/pingZhangYiDaRequest.json",
        data: {},
        success: function (result) {
            if (result.success == "true") {
                $.messager.alert('提示',result.message);
            } else {
                if (result.message) {
                    $.messager.alert('提示',result.message);
                } else {
                    $.messager.alert('提示','接口测试失败!');
                }
            }
        }
    });
}

/**
 * selenium 接口测试
 */
function pingSelenium() {
    $.ajax({
        async: true,
        url: contextPath + "/corpSetting/pingSelenium.json",
        data: {},
        success: function (result) {
            if (result.success == "true") {
                $.messager.alert('提示','selenium接口测试成功!');
            } else {
                $.messager.alert('提示','selenium接口测试失败!');
            }
        }
    });
}

/**
 * 验证码测试
 */
function pingCheckcode() {
    $.ajax({
        async: true,
        url: contextPath + "/corpSetting/pingCheckcode.json",
        data: {},
        success: function (result) {
            if (result.success == "true") {
                var html = "checkcode接口测试成功!";
                html += "<br />图片为:<img src='data:image/png;base64,{base64}' />".replace("{base64}", result.base64);
                html += "<br />程序识别为:{checkcode}".replace("{checkcode}", result.checkcode);
                $.messager.alert('提示',html);
            } else {
                $.messager.alert('提示','checkcode接口测试失败!');
            }
        }
    });
}

/**
 * 企业用户名密码 body 部分补全
 */
function applyCorpSettingBodyData() {
    $.ajax({
        async: true,
        url: contextPath + "/corpSetting/getCorpSetting.json?date=" + (new Date().getTime()),
        data: {},
        success: function (result) {
            var html = UnderscoreUtil.getHtmlById("corpSettingBodyTemplate", {
                corpSetting: result.corpSetting
            });
            $("#corpSettingBody").html(html);
        }
    });
}

/**
 * 参数设置, body 的补全
 */
function applyTaxParameterBodyData() {
    $.ajax({
        async: true,
        url: contextPath + "/taxParameter/getTaxParameter.json?date=" + (new Date().getTime()),
        data: {},
        success: function (result) {
            var html = UnderscoreUtil.getHtmlById("taxParameterBodyTemplate", {
                taxParameterLi: result.taxParameterLi
            });
            $("#taxParameterBody").html(html);
        }
    });
}

/**
 * 每隔1秒取一次
 */
function applyTaxDeclareProgressBodyData() {
    setInterval(function(){
        $.ajax({
            async: true,
            url: contextPath + "/taxDeclareProgress/getTaxDeclareProgress.json?date=" + (new Date().getTime()),
            data: {},
            success: function (result) {
                var html = UnderscoreUtil.getHtmlById("taxDeclareProgressBodyTemplate", {
                    taxDeclareProgressLi: result.taxDeclareProgressLi
                });
                $("#taxDeclareProgressBody").html(html);
            }
        });
    }, 10 * 1000);
}

/**
 * 刷新报税进度
 */
function refreshTaxDeclareProgress() {
    $.ajax({
        async: true,
        url: contextPath + "/taxDeclareProgress/getTaxDeclareProgress.json?date=" + (new Date().getTime()),
        data: {},
        success: function (result) {
            var html = UnderscoreUtil.getHtmlById("taxDeclareProgressBodyTemplate", {
                taxDeclareProgressLi: result.taxDeclareProgressLi
            });
            $("#taxDeclareProgressBody").html(html);
            $.messager.alert('提示','报税进度刷新成功!');
        }
    });
}

$(document).ready(function(){
    applyCorpSettingBodyData();
    applyTaxParameterBodyData();
    applyTaxDeclareProgressBodyData();
});
