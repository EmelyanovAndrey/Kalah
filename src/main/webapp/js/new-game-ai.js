$(document).ready(function () {
    console.log("initial");
    $("#new-game-ai-btn").on("click", function () {
        var data = {};
        data.holeCount = $("#count_lunk").val();
        data.stoneCount = $("#count_stouns").val();
        data.level = $("#level").val();
        data.prior = $("#prior").val();
        console.log(data);
        $.ajax({
            type: "POST",
            url: "/kalah-1.0/createAIGame",
            async: false,
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (res) {
                location.href = '/kalah-1.0/game-ai.jsp';
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Error status code: " + xhr.status);
                alert("Error details: " + thrownError);
            }
        });
    });
});