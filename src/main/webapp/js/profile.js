$(document).ready(function () {

    $('.modal').modal();

    $('#change').click(function() {

        if (changeProfileValid()) {
            var data = {};
            data.login = $("#name").val();
            data.password = $("#password").val();
            data.email = $("#email").val();
            console.log(data);
            $.ajax({
                type: "POST",
                url: "/kalah-1.0/changeUserData",
                async: true,
                data: JSON.stringify(data),
                contentType: "application/json",
                success: function (res) {
                    if (res.result == "success") {
                        $(".profile .nick").text(data.login);
                        $("#password").val("");
                        $("#re-password").val("");
                        $("#inform-message").text("Настройки профиля изменены.");
                        $('#inform').modal('open');

                    } else {
                        $("#inform-message").text("Пользователь с таким именем уже существует.");
                        $('#inform').modal('open');
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    alert("Error status code: " + xhr.status);
                    alert("Error details: " + thrownError);
                }
            });
        } else {
            $("#inform-message").text("Некорректно введены данные.");
            $('#inform').modal('open');
        }
    });

    $.ajax({
        type: "GET",
        url: "/kalah-1.0/getUserData",
        async: false,
        contentType: "application/json",
        success: function (res) {
            $(".profile").append("<img src='"+res.avatar+"'><div class='nick'>"+res.login+"</div>");
            $("#name").val(res.login);
            $("#email").val(res.email);
            //TODO вытянуть сохраненные игры
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error status code: " + xhr.status);
            alert("Error details: " + thrownError);
        }
    });

});