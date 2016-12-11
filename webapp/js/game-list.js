$(document).ready(function() {
    $(".game-list").on("click", ".bid", function () {
        var data = {};
        data.creatorLogin = $(this).find(".creator-login").text();
        console.log(data);

        $.ajax({
            type: "POST",
            url: "/kalah-1.0/chooseBid",
            async: false,
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (res) {
                if (res.result == "success") {
                    location.href = '/kalah-1.0/game.html';
                } else {
                    if (res.type == "not exist") {
                        alert("Игры, к которой вы хотите присоединится, не существует, или она удалена.");
                    } else if (res.type == "block") {
                        alert("К игре пытается присоединится кто-то другой.");
                    }
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Error status code: " + xhr.status);
                alert("Error details: " + thrownError);
            }
        });
    });
});