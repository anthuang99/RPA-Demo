$(document).ready(function(){
    setInterval(function(){
        $.ajax({
            async: false,
            url: contextPath + "/isInitReady.json",
            data: {},
            success: function (result) {
                console.log(result);
                if (result.isInitReady) {
                    location.href = contextPath + "/";
                }
            }
        });
    }, 1000);
});
