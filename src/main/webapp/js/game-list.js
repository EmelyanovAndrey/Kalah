$(document).ready(function () {
    setTimeout($.ajax({
        type: "GET",
        url: "/kalah-1.0/getBids",
        async: true,
        contentType: "application/json",
        success: function (res) {
            console.log(res);
            if (res.result == "success") {
                var bids = res.bids;
                for (var i = 0; i < bids.length; i++) {
                    var bid = bids[i];
                    var tr = $("<tr class='bid'><td>"+(i+1)+"</td><td class='creator-login'>"+bid.vs+"</td><td>"+bid.stones+"/"+bid.holes+"</td></tr>");
                    $("tbody").append(tr);
                }
            } else {

            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error status code: " + xhr.status);
            alert("Error details: " + thrownError);
        }
    }), 500);

    $(".game-list").on("click", ".bid", function () {
        var data = {};
        data.creatorLogin = $(this).find(".creator-login").text();
        console.log(data);

        $.ajax({
            type: "POST",
            url: "/kalah-1.0/chooseBid",
            async: true,
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