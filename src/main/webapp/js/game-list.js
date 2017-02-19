$(document).ready(function () {

    var t = $('#game_list').DataTable();
    var creatorLogin = "";

    $('.modal').modal();

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
                    t.row.add([i + 1, bid.vs, bid.stones + "/" + bid.holes]).draw();
                }
            } else {

            }
        },
        error: function (xhr, ajaxOptions, thrownError) {

        }
    }), 500);

    $("#join").on("click", "#no", function () {
    })
        .on("click", "#yes", function () {
            var data = {};
            data.creatorLogin = creatorLogin;
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
                            $("#inform-message").text("Игры, к которой вы хотите присоединится, не существует, или она удалена.");
                            $('#inform').modal('open');
                        } else if (res.type == "block") {
                            $("#inform-message").text("К игре пытается присоединится кто-то другой.");
                            $('#inform').modal('open');
                        }
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    alert("Error status code: " + xhr.status);
                    alert("Error details: " + thrownError);
                }
            });
        });

    $('#game_list').on("click", "tr", function () {

        console.log('click');
        if (!$(this).hasClass("head")) {
            creatorLogin = $(this).find("td").eq(1).text();
            $('#join').modal('open');
        }
    });
});