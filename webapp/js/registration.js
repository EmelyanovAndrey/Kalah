$(document).ready(function() {
    $("#signup").on("click", function () {
        var data = {};
        data.login = $("#name").val();
        data.password = $("#password").val();
        data.email = $("#email").val();
        console.log(data);
        $.ajax({
            type: "POST",
            url: "/kalah-1.0/registration",
            async: true,
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (res) {
                console.log(res);
                if (res.result == "success") {
                    location.href = '/kalah-1.0/profile.html';
                } else {
                    alert("Пользователь с таким именем уже существует.");
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Error status code: " + xhr.status);
                alert("Error details: " + thrownError);
            }
        });
    });
});