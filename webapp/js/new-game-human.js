$(document).ready(function() {
    $("#new-game-human-btn").on("click", function () {
        var data = {};
        data.holeCount = $("#count_lunk").val();
        data.stoneCount = $("#count_stouns").val();
        data.friendLogin = $("#friend").val().trim();
        console.log(data);
        $.ajax({
            type: "POST",
            url: "/kalah-1.0/createNewGame",
            async: false,
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (res) {
                if (res.result == "success") {
                    location.href = '/kalah-1.0/game.html';
                } else {
                    alert("Вы указали имя несуществующего друга.");
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Error status code: " + xhr.status);
                alert("Error details: " + thrownError);
            }
        });
    });
});