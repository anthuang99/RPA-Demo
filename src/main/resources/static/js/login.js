function clearTip() {
    $("#username").parent("div").removeClass("item-error");
    $("#password").parent("div").removeClass("item-error");
    $("#domainName").parent("div").removeClass("item-error");

    $("#usernameTip").hide();
    $("#passwordTip").hide();
    $("#domainNameTip").hide();
}

function applyEvent() {
    $("#loginBtn").click(function(){
        clearTip();

        var isValidate = true;
        var usernameValue = $.trim($("#username").val());
        if (!usernameValue || (usernameValue.length > 200)) {
            $("#username").parent("div").addClass("item-error");
            $("#usernameTip").show();
            isValidate = false;
        }
        var passwordValue = $.trim($("#password").val());
        if (!passwordValue || (passwordValue.length > 200)) {
            $("#password").parent("div").addClass("item-error");
            $("#passwordTip").show();
            isValidate = false;
        }
        var domainNameValue = $.trim($("#domainName").val());
        if (!domainNameValue || (domainNameValue.length > 200)) {
            $("#domainName").parent("div").addClass("item-error");
            $("#domainNameTip").show();
            isValidate = false;
        }

        if (!isValidate) {
            return;
        }

        $.ajax({
            async: true,
            url: contextPath + "/corpSetting/saveCorpSetting.json",
            method: "post",
            contentType: "application/json",
            data: JSON.stringify({
                username: usernameValue,
                password: passwordValue,
                domainName: domainNameValue
            }),
            success: function (result) {
                if (result.success == "true") {
                    location.href = contextPath + "/indexFirst.htm";
                } else {
                    alert("保存出错")
                }
            }
        });
    });
}

$(document).ready(function(){
    applyEvent();
});
