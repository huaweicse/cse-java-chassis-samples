function uploadAction() {
     var username = document.getElementById("username").value;
     var password = document.getElementById("paasword").value;
     var formData = {};
     formData.userName = username;
     formData.password = password;

     $.ajax({
        type: 'POST',
        url: "/api/user-service/v1/user/login",
        data: formData,
        success: function (data) {
            console.log(data);
            setCookie("session-id", data.sessiondId, 1);
            window.location = "/ui/upload.html";
        },
        error: function(data) {
            console.log(data);
            var error = document.getElementById("error");
            error.textContent="登录失败";
            error.hidden=false;
        },
        async: true
    });
}