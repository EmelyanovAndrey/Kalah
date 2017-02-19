$(document).ready(function () {

    var t = $('#save_games').DataTable();

    var creatorLogin = "";
    var gameId = "";

    $('.modal').modal();

    $('#change').click(function () {

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
            $(".profile").append("<img src='" + res.avatar + "'><div class='nick'>" + res.login + "</div>");
            $("#name").val(res.login);
            $("#email").val(res.email);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error status code: " + xhr.status);
            alert("Error details: " + thrownError);
        }
    });

    $.ajax({
        type: "GET",
        url: "/kalah-1.0/getSavedGames",
        async: false,
        contentType: "application/json",
        success: function (res) {
            console.log(res);
            if (res.result == "success") {
                var games = res.games;
                for (var i = 0; i < games.length; i++) {
                    var game = games[i];
                    t.row.add([i + 1, game.vs, game.set, game.date, game.stones, game.holes, game.creatorLogin, game.id]).draw();
                    $("tr").eq(i + 1).find("td").eq(6).css("display", "none");
                    $("tr").eq(i + 1).find("td").eq(7).css("display", "none");
                }
            } else {

            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error status code: " + xhr.status);
            alert("Error details: " + thrownError);
        }
    });

    $("#load").on("click", "#no", function () {
    })
        .on("click", "#yes", function () {
            var data = {};
            data.creatorLogin = creatorLogin;
            data.id = gameId;
            data.login = $("#name").val();
            console.log(data);

            $.ajax({
                type: "POST",
                url: "/kalah-1.0/loadGame",
                async: true,
                data: JSON.stringify(data),
                contentType: "application/json",
                success: function (res) {
                    if (res.result == "success") {
                        location.href = '/kalah-1.0/game.html';
                    } else {

                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    alert("Error status code: " + xhr.status);
                    alert("Error details: " + thrownError);
                }
            });
        });

    $('#save_games').on("click", "tr", function () {

        console.log('click');
        if (!$(this).hasClass("head")) {
            creatorLogin = $(this).find("td").eq(6).text();
            gameId = $(this).find("td").eq(7).text();
            $('#load').modal('open');
        }
    });
});