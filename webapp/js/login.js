$(document).ready(function() {
    $("#login").on("click", function () {
        var data = {};
        data.login = $("#name").val();
        data.password = $("#password").val();
        console.log(data);
        $.ajax({
            type: "POST",
            url: "/kalah-1.0/login",
            async: true,
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (res) {
                if (res.result == "success") {
                    location.href = '/kalah-1.0/game-list.html';
                } else {
                    alert("Неверные данные аутентификации.");
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Error status code: " + xhr.status);
                alert("Error details: " + thrownError);
            }
        });
    });
});