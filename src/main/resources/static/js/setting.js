function checkcodeOffsetValidate() {
    var messageLi = []
    var checkcodeOffsetXValue = $.trim($("#checkcodeOffsetX").val());
    var checkcodeOffsetYValue = $.trim($("#checkcodeOffsetY").val());
    var numReg = /^\d{1,3}$/;
    if (!numReg.test(checkcodeOffsetXValue)) {
        messageLi.push("验证码X偏移必须由数字组成，长度&lt;=3");
    }
    if (!numReg.test(checkcodeOffsetYValue)) {
        messageLi.push("验证码Y偏移必须由数字组成，长度&lt;=3");
    }
    return messageLi;
}

/**
 * 验证码测试
 */
function pingCheckcode() {
    var checkcodeOffsetXValue = $.trim($("#checkcodeOffsetX").val());
    var checkcodeOffsetYValue = $.trim($("#checkcodeOffsetY").val());
    var messageLi = checkcodeOffsetValidate();
    if (messageLi.length > 0) {
        $.messager.alert('提示', messageLi.join("<br />"));
        return;
    }

    $.ajax({
        async: true,
        url: contextPath + "/corpSetting/pingCheckcode.json",
        data: {
            checkcodeOffsetX: checkcodeOffsetXValue,
            checkcodeOffsetY: checkcodeOffsetYValue
        },
        success: function (result) {
            if (result.success == "true") {
                var html = "验证码接口测试成功!";
                html += "<br />图片为:<img src='data:image/png;base64,{base64}' />".replace("{base64}", result.base64);
                html += "<br />程序识别为:{checkcode}".replace("{checkcode}", result.checkcode);
                $.messager.alert('提示',html);
            } else {
                $.messager.alert('提示','验证码接口测试失败!');
            }
        }
    });
}

function browserPathValidate() {
    var messageLi = [];
    var browserPathValue = $.trim($("#browserPath").val());
    if (!browserPathValue) {
        messageLi.push("浏览器路径不能为空");
        return;
    }
    if (browserPathValue.length > 150) {
        messageLi.push("浏览器路径长度不能超过150");
        return;
    }
    return messageLi;
}

/**
 * selenium 接口测试
 */
function pingSelenium() {
    var browserPathValue = $.trim($("#browserPath").val());
    var messageLi = browserPathValidate();
    if (messageLi.length > 0) {
        $.messager.alert('提示', messageLi.join("<br />"));
        return;
    }
    $.ajax({
        async: true,
        url: contextPath + "/corpSetting/pingSelenium.json",
        data: {
            browserPath: browserPathValue
        },
        success: function (result) {
            if (result.success == "true") {
                $.messager.alert('提示','启动浏览器测试成功!');
            } else {
                if (result.message) {
                    $.messager.alert('提示',result.message);
                } else {
                    $.messager.alert('提示','启动浏览器测试失败!');
                }
            }
        }
    });
}

/**
 * 点击确定
 */
function clickConfirm() {
    var messageLi = [];
    var usernameValue = $.trim($("#username").val());
    if (!usernameValue) {
        messageLi.push("账益达用户名不能为空！");
    } else if (usernameValue.length > 200) {
        messageLi.push("账益达用户名长度不能超过200！");
    }
    var passwordValue = $.trim($("#password").val());
    if (!passwordValue) {
        messageLi.push("账益达密码不能为空！");
    } else if (passwordValue.length > 200) {
        messageLi.push("账益达密码长度不能超过200！");
    }
    var domainNameValue = $.trim($("#domainName").val());
    if (!domainNameValue) {
        messageLi.push("登录域名不能为空！");
    } else if (domainNameValue.length > 200) {
        messageLi.push("登录域名长度不能超过200！");
    }

    var taxThreadCountValue = $.trim($("#taxThreadCount").val());
    var numReg = /^\d$/;
    if (!numReg.test(taxThreadCountValue)) {
        messageLi.push("报税用户数必须由数字组成");
    }
    if (parseInt(taxThreadCountValue) > 5) {
        messageLi.push("报税用户数必须&lt;=5");
    }

    var dragCheckCodeRepeatCountValue = $.trim($("#dragCheckCodeRepeatCount").val());
    var numReg = /^\d+$/;
    if (!numReg.test(dragCheckCodeRepeatCountValue)) {
        messageLi.push("拖动验证码重试次数必须由数字组成");
    }
    if (parseInt(dragCheckCodeRepeatCountValue) > 10) {
        messageLi.push("拖动验证码重试次数必须&lt;=10");
    }

    /*
    var checkcodeOffsetXValue = $.trim($("#checkcodeOffsetX").val());
    var checkcodeOffsetYValue = $.trim($("#checkcodeOffsetY").val());
    var checkcodeMessageLi = checkcodeOffsetValidate();
    for (var i = 0; i < checkcodeMessageLi.length; i++) {
        messageLi.push(checkcodeMessageLi[i]);
    }
    */

    var browserPathValue = $.trim($("#browserPath").val());
    var browserPathMessageLi = browserPathValidate();
    for (var i = 0; i < browserPathMessageLi.length; i++) {
        messageLi.push(browserPathMessageLi[i]);
    }

    if (messageLi.length > 0) {
        $.messager.alert('提示', messageLi.join("<br />"));
        return;
    }

    $.ajax({
        async: true,
        url: contextPath + "/corpSetting/saveSetting.json",
        data: {
            username: usernameValue,
            password: passwordValue,
            domainName: domainNameValue,
            taxThreadCount: taxThreadCountValue,
            dragCheckCodeRepeatCount: dragCheckCodeRepeatCountValue
            // ,checkcodeOffsetX: checkcodeOffsetXValue
            // ,checkcodeOffsetY: checkcodeOffsetYValue
            ,browserPath: browserPathValue
        },
        success: function (result) {
            if (result.success == "true") {
                $.messager.alert({
                    title:'提示',
                    msg: '<div class="tip-text-area pt20">保存成功</div>',
                    fn: function(){
                        location.href = contextPath + "/";
                    }
                });
                /*
                $.messager.show({
                    title:'提示',
                    msg:'保存成功',
                    showType:'slide',
                    timeout:500,
                    style:{
                        right:'',
                        top:document.body.scrollTop+document.documentElement.scrollTop,
                        bottom:''
                    }
                });
                setTimeout(function(){
                    location.href = contextPath + "/";
                }, 500);
                */
            } else {
                if (result.message) {
                    $.messager.alert('提示',result.message);
                } else {
                    $.messager.alert('提示','保存失败!');
                }
            }
        }
    });
}

/**
 * 点击取消
 */
function clickCancel() {
    location.href = contextPath + "/";
}

$(document).ready(function(){

});
