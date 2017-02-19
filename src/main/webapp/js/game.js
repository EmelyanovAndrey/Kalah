var wsUri = "ws://localhost:8080/kalah-1.0/game";
var WS = new WebSocket(wsUri);
var login = "";
var role = "";
var vs = "";

var stepAI;
var makeStepAI = false;

var theEnd = 0;
var exit = 0;
var blockGetStone = 1;

var board = [];
var length;
var sum;

var mes;
var res;

WS.onerror = function (evt) {
    alert('ERROR: ' + evt.data);
};

WS.onmessage = function (evt) {
    console.log("received: " + evt.data);
    var response = JSON.parse(evt.data);
    if (response.operation == "continue") {
        if (response.priority == "true") {
            $("#inform-message").text("Ваш друг зашел, и сейчас ваш ход.");
            $('#inform').modal('open');
            enableLunks();
        } else {
            $("#inform-message").text("Ваш друг зашел, и сейчас его ход.");
            $('#inform').modal('open');
        }
    }
    if (response.operation == "load") {
        if (response.suboperation == "create") {
            var user = $("<img src='" + response.yourAvatar + "'><div class='nick'>" + login + "</div>");
            $(".player").append(user);
            user = $("<img src='" + response.enemyAvatar + "'><div class='nick'>" + response.enemyLogin + "</div>");
            $(".apponent").append(user);
            createBoard(response.holeCount, response.stoneCount);
            $("#inform-message").text("Ваш друг еще не зашел.");
            $('#inform').modal('open');

        }
        if (response.suboperation == "join") {
            var user = $("<img src='" + response.yourAvatar + "'><div class='nick'>" + login + "</div>");
            $(".player").append(user);
            user = $("<img src='" + response.enemyAvatar + "'><div class='nick'>" + response.enemyLogin + "</div>");
            $(".apponent").append(user);
            createBoard(response.holeCount, response.stoneCount);
            if (response.priority == "true") {
                $("#inform-message").text("Ваш друг уже зашел, и сейчас ваш ход.");
                $('#inform').modal('open');
                enableLunks();
            } else {
                $("#inform-message").text("Ваш друг уже зашел, и сейчас его ход.");
                $('#inform').modal('open');
            }
        }
        board = [];
        for (var i = 0; i < +response.holeCount * 2 + 2; i++) {
            $("#" + i).text(response[i]);
            board.push(response[i]);
        }

    }
    if (response.operation == "createAI") {
        var user = $("<img src='" + response.avatar + "'><div class='nick'>" + login + "</div>");
        $(".player").append(user);
        user = $("<img src='" + response.avatarAI + "'><div class='nick'>AI(" + response.level + ")</div>");
        $(".apponent").append(user);
        createBoard(response.holeCount, response.stoneCount);
        if (response.prior) {
            enableLunks();
        }
    }
    if (response.operation == "create") {
        // alert("Сессия вебсокета успешно добавлена к заявке.")
        var user = $("<img src='" + response.avatar + "'><div class='nick'>" + login + "</div>");
        $(".player").append(user);
        var message = {operation: "getBoard"};
        sendMessage(JSON.stringify(message));
    }
    if (response.operation == "join") {
        if (response.role == "joined") {
            var message = {operation: "getBoard"};
            sendMessage(JSON.stringify(message));
            //   alert("Сессия вебсокета успешно добавлена к заявке. Ожидайте пока ее одобрят.");
            var user = $("<img src='" + response.yourAvatar + "'><div class='nick'>" + login + "</div>");
            $(".player").append(user);
            user = $("<img src='" + response.enemyAvatar + "'><div class='nick'>" + response.enemyLogin + "</div>");
            $(".apponent").append(user);
        }
        if (response.role == "creator") {
            //    alert("Поступила заявка на подтверждение.")
            var message = {};
            message.operation = "conf";
            message.login = login;
            message.joinedLogin = response.joinedLogin;
            res = response;
            mes = message;
            $("#join-name").text(response.joinedLogin);
            $('#messege').modal('open');
        }
    }
    if (response.operation == "conf") {
        if (response.role == "joined") {
            var conf = response.conf;
            if (conf == "yes") {
                if (response.priority == "true") {
                    $("#inform-message").text("Ваше присоединение одобрено, и вы ходите первым.");
                    $('#inform').modal('open');
                    enableLunks();
                } else if (response.priority == "false") {
                    $("#inform-message").text("Ваше присоединение одобрено, но вы ходите вторым.");
                    $('#inform').modal('open');
                }
            } else if (conf == "no") {
                $("#inform-message").text("С вами не захотели играть!");
                $('#inform').modal('open');
                location.href = '/kalah-1.0/game-list.jsp';
            }
        }
        if (response.role == "creator") {

            if (response.priority == "true") {
                $("#inform-message").text("Вы ходите первым.");
                $('#inform').modal('open');
                enableLunks();

            } else if (response.priority == "false") {
                $("#inform-message").text("Вы ходите вторым.");
                $('#inform').modal('open');
            }
        }
    }
    if (response.operation == "getBoard") {
        createBoard(response.holeCount, response.stoneCount);
    }
    if (response.operation == "step") {

        var num = response.num;

        var clickedLunk = $("#" + num);
        clickLunk(num, clickedLunk, 1);
    }
    if (response.operation == "stepAI") {
        stepAI = response.num;
        makeStepAI = false;
    }
    if (response.operation == "creator closed") {
        disableLunks();
        if (theEnd == 0) {
            $("#inform-message").text("Противник отсоединился.");
            $('#inform').modal('open');
            exit = 1;
        }
    }
    if (response.operation == "joined closed") {
        disableLunks();
        if (theEnd == 0) {
            $("#inform-message").text("Противник отсоединился.");
            $('#inform').modal('open');
            exit = 1;
        }
    }
    if (response.operation == "creator closed in game") {
        disableLunks();
        if (theEnd == 0) {
            $("#inform-message").text("Противник отсоединился.");
            $('#inform').modal('open');
            exit = 1;
        }
    }
    if (response.operation == "joined closed in game") {
        disableLunks();
        if (theEnd == 0) {
            $("#inform-message").text("Противник отсоединился.");
            $('#inform').modal('open');
            exit = 1;
        }
    }
};
function sendMessage(json) {
    console.log("sending message to websocket: " + json);
    WS.send(json);
}

function createWSconnect() {
    $.ajax({
        type: "GET",
        url: "/kalah-1.0/getSessionData",
        async: true,
        contentType: "application/json",
        success: function (res) {
            console.log(res);
            if (res.result == "success") {
                login = res.login;
                role = res.role;
                vs = res.vs;
                var load = res.load;

                if (load == "true") {
                    if (role == "creator") {

                        var message = {};
                        message.operation = "load";
                        message.login = login;
                        message.creatorLogin = res.creatorLogin;
                        message.load = load;
                        message.role = role;
                        message.id = res.id;
                        sendMessage(JSON.stringify(message));

                    } else if (role == "joined") {
                        var message = {};
                        message.operation = "load";
                        message.role = role;
                        message.login = login;
                        message.creatorLogin = res.creatorLogin;
                        message.load = load;
                        message.id = res.id;
                        sendMessage(JSON.stringify(message));
                    }
                } else {
                    if (role == "creator") {
                        if (vs == "ai") {
                            var message = {};
                            message.operation = "createAI";
                            message.level = res.level;
                            message.holeCount = res.holeCount;
                            message.stoneCount = res.stoneCount;
                            message.login = login;
                            sendMessage(JSON.stringify(message));
                        } else {
                            var message = {};
                            message.operation = "create";
                            message.login = login;
                            message.load = load;
                            sendMessage(JSON.stringify(message));
                        }
                    } else if (role == "joined") {
                        var message = {};
                        message.operation = "join";
                        message.login = login;
                        message.load = load;
                        message.creatorLogin = res.creatorLogin;
                        sendMessage(JSON.stringify(message));
                    }
                }
            } else {
                $("#inform-message").text("Каким-то хреном вы добрались сюда неавторизованным ;(");
                $('#inform').modal('open');
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error status code: " + xhr.status);
            alert("Error details: " + thrownError);
        }
    });

}

function enableLunks() {
    $(".your-lunk").each(function (i, el) {
        $(el).removeClass("enemy-obj enemy");
        if ($(el).text() != 0) {
            $(el).addClass("active");
        }
    });
    $("#save-game").removeAttr("disabled");
    $(".board").on("click", ".your-lunk", onLunkClick);
}
function disableLunks() {
    $(".your-lunk").each(function (i, el) {
        $(el).addClass("enemy-obj enemy");
        $(el).removeClass("active");
    });
    $(".board").off("click", ".your-lunk", onLunkClick);
    $("#save-game").attr("disabled", "disabled");
}

function createBoard(holeCount, stoneCount) {
    // alert("Щас сгенерится таблица.");
    length = holeCount * 2 + 2;
    sum = holeCount * 2 * stoneCount;
    if (role == "creator") {
        var tr1 = $("<tr></tr>").append('<td rowspan="2" id="' + (length - 1) + '" class="kalah enemy-kalah enemy-obj enemy">0</td>');
        var tr2 = $("<tr></tr>");
        for (var i = length - 2; i > length / 2 - 1; i--) {
            tr1.append('<td id="' + i + '" class="lunka enemy-lunk enemy-obj enemy">' + stoneCount + '</td>');
        }
        tr1.append('<td id="' + (length / 2 - 1) + '" rowspan="2" class="kalah your-kalah enemy-obj enemy">0</td>');
        for (var i = 0; i < length / 2 - 1; i++) {
            tr2.append('<td id="' + i + '" class="your-lunk lunka enemy-obj enemy">' + stoneCount + '</td>');
        }
        $('.board').append(tr1).append(tr2);

        for (var i = 0; i < length; i++) {
            if ((i == length / 2 - 1) || i == length - 1) {
                board.push(0);
            } else {
                board.push(stoneCount);
            }
        }
    } else {
        var tr1 = $("<tr></tr>").append('<td rowspan="2" id="' + (length / 2 - 1) + '" class="kalah enemy-kalah enemy-obj ">0</td>');
        var tr2 = $("<tr></tr>");
        for (var i = length / 2 - 2; i >= 0; i--) {
            tr1.append('<td id="' + i + '" class="lunka enemy-lunk enemy-obj enemy">' + stoneCount + '</td>');
        }
        tr1.append('<td id="' + (length - 1) + '" rowspan="2" class="kalah your-kalah enemy-obj enemy">0</td>');
        for (var i = length / 2; i < length - 1; i++) {
            tr2.append('<td id="' + i + '" class="your-lunk lunka enemy-obj enemy">' + stoneCount + '</td>');
        }
        $('.board').append(tr1).append(tr2);

        for (var i = 0; i < length; i++) {
            if ((i == length / 2 - 1) || i == length - 1) {
                board.push(0);
            } else {
                board.push(stoneCount);
            }
        }
    }
}

function isEnemyLunksEmpty() {
    var isEmpty = true;
    if (role == "joined") {
        for (var i = 0; i < length / 2 - 1; i++) {
            if (+$("#" + i).text() != 0) {
                isEmpty = false;
            }
        }
    } else {
        for (var i = length / 2; i < length - 1; i++) {
            if (+$("#" + i).text() != 0) {
                isEmpty = false;
            }
        }
    }
    /*   if (isEmpty) {
     var yourKalah = $(".your-kalah");
     if (role == "joined") {
     for (var i = length - 2; i > length / 2 - 1; i--) {
     yourKalah.text(+yourKalah.text() + +$("#" + i).text());
     $("#" + i).text(0);
     }
     } else {
     for (var i = length / 2 - 2; i >= 0; i--) {
     yourKalah.text(+yourKalah.text() + +$("#" + i).text());
     $("#" + i).text(0);
     }
     }

     */
    return isEmpty;
}

function isYourLunksEmpty() {
    var isEmpty = true;
    if (role == "creator") {
        for (var i = 0; i < length / 2 - 1; i++) {
            if (+$("#" + i).text() != 0) {
                isEmpty = false;
            }
        }
    } else {
        for (var i = length / 2; i < length - 1; i++) {
            if (+$("#" + i).text() != 0) {
                isEmpty = false;
            }
        }
    }
    /* if (isEmpty) {
     var enemyKalah = $(".enemy-kalah");
     if (role == "creator") {
     for (var i = length - 2; i > length / 2 - 1; i--) {
     enemyKalah.text(+enemyKalah.text() + +$("#" + i).text());
     $("#" + i).text(0);
     }
     } else {
     for (var i = length / 2 - 2; i >= 0; i--) {
     enemyKalah.text(+enemyKalah.text() + +$("#" + i).text());
     $("#" + i).text(0);
     }
     }
     }*/
    return isEmpty;
}

function isWinnerExist() {
    var yourKalah = $(".your-kalah");
    var enemyKalah = $(".enemy-kalah");
    if (+yourKalah.text() > sum / 2) {
        $('#inform').modal('open');
        $("#inform-message").text("Вы выиграли!");
        disableLunks();
        theEnd = 1;
    }
    if (+enemyKalah.text() > sum / 2) {
        $('#inform').modal('open');
        $("#inform-message").text("Вы проиграли!");
        disableLunks();
        theEnd = 1;
    }
}

$(document).ready(function () {

    $("body").on("click", "#save-game", function () {
        var data = {};

        $.ajax({
            type: "GET",
            url: "/kalah-1.0/saveGame",
            async: true,
            contentType: "application/json",
            success: function (res) {
                if (res.result == "success") {
                    $("#inform-message").text("Игра сохранена.");
                    $('#inform').modal('open');

                } else {
                    $("#inform-message").text("Не удалось сохранить игру.");
                    $('#inform').modal('open');
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                alert("Error status code: " + xhr.status);
                alert("Error details: " + thrownError);
            }
        });
    });

    $("#inform").on("click", "#ok", function () {
        if (exit == 1)
            location.href = '/kalah-1.0/game-list.jsp';
    });

    $('.modal').modal();

    $("#messege").on("click", "#deny", function () {
        mes.conf = "no";
        sendMessage(JSON.stringify(mes));
    })
        .on("click", "#gran", function () {
            var user = $("<img src='" + res.enemyAvatar + "'><div class='nick'>" + res.joinedLogin + "</div>");
            $(".apponent").append(user);
            mes.conf = "yes";
            sendMessage(JSON.stringify(mes));
        });

    setTimeout(createWSconnect, 500);
});

var onLunkClick = function onLunkClickListener() {
    var clickedLunk = $(this);
    if (clickedLunk.text() != 0) {
        disableLunks();

        var num = +clickedLunk.attr("id");
        var message = {};
        if (vs == "ai") {
            message.operation = "stepAI";
            message.num = num;
        } else {
            message.operation = "step";
            message.num = num;
            message.role = role;
        }
        sendMessage(JSON.stringify(message));
        clickLunk(num, clickedLunk, 0);
    }
};

function clickLunk(num, clickedLunk, par) {
    var localRole = role;
    if (par == 1) {
        if (localRole == "creator") {
            localRole = "joined";
        } else if (localRole == "joined") {
            localRole = "creator";
        }
    }
    var basket = +clickedLunk.text();
    var fullCycle = false;
    if (basket > 0) {
        board[+num] = 0;
        clickedLunk.text(0);
        var i = +num + 1;
        putStone(i, basket, localRole, board, fullCycle, par);
    }
}

function putStone(i, basket, localRole, board, fullCycle, par) {
    if (basket > 0) {
        if (localRole == "creator" && i == length - 1) {
            i = 0;
            fullCycle = true;
        } else if (localRole == "joined" && i == length / 2 - 1) {
            i = length / 2;
            fullCycle = true;
        } else if (localRole == "joined" && i == length) {
            i = 0;
        }
        var el = $("#" + i);
        el.css("background-color", "#f5f5f5").css("color", "#37474f");

        board[i] = +board[i] + 1;
        $("#" + i).text(board[i]);
        basket--;
        i++;
        setTimeout(function () {

            el.css("background-color", "#37474f").css("color", "#f5f5f5");
            putStone(i, basket, localRole, board, fullCycle, par);

        }, 500);
    } else {
        i--;
        if (par == 0) {
            if (fullCycle && $("#" + i).hasClass("your-lunk") && +$("#" + i).text() != 1) {
                clickLunk(i, $("#" + i), 0);
            } else {
                blockGetStone = 0;
            }
        } else {
            if (fullCycle && $("#" + i).hasClass("enemy-lunk") && +$("#" + i).text() != 1) {
                clickLunk(i, $("#" + i), 1);
            }
            else {
                blockGetStone = 0;
            }
        }
        if (blockGetStone == 0) {
            getStone(i, par);
            blockGetStone = 1;
        }

    }

}

function getStone(i, par) {
    var lunk = $("#" + i);
    var count = +lunk.text();

    var hasClass = false;
    if (par == 0) {
        hasClass = lunk.hasClass("enemy-lunk");
    } else {
        hasClass = lunk.hasClass("your-lunk");
    }
    if (i >= 0 && hasClass && ( count == 2 || count == 3)) {
        lunk.text(0);
        if (par == 0) {
            if (role == "creator") {
                board[length / 2 - 1] = board[length / 2 - 1] + count;
                $("#" + (length / 2 - 1)).text(board[length / 2 - 1]);
            } else if (role == "joined") {
                board[length - 1] = board[length - 1] + count;
                $("#" + (length - 1)).text(board[length - 1]);
            }
        } else {
            if (role == "joined") {
                board[length / 2 - 1] = board[length / 2 - 1] + count;
                $("#" + (length / 2 - 1)).text(board[length / 2 - 1]);
            } else if (role == "creator") {
                board[length - 1] = board[length - 1] + count;
                $("#" + (length - 1)).text(board[length - 1]);
            }
        }
        board[i] = 0;
        lunk.css("background-color", "#a5a5a5").css("color", "#37474f");
        setTimeout(function () {

            lunk.css("background-color", "#37474f").css("color", "#f5f5f5");
            i--;
            getStone(i, par);
        }, 500);
    } else {
        var end = 0;
        if (par == 1 && isYourLunksEmpty()) {
            $('#inform').modal('open');
            $("#inform-message").text("Вы проиграли!");
            end = 1;
            theEnd = 1;
            return;
        } else if (par == 1 && !isYourLunksEmpty()) {
            enableLunks();
        } else if (par == 0 && isEnemyLunksEmpty()) {
            $('#inform').modal('open');
            $("#inform-message").text("Вы выиграли!");
            end = 1;
            theEnd = 1;
            return;
        }
        if (end == 0)
            isWinnerExist(par);

        if (vs == "ai" && !makeStepAI) {
            var clickedLunk = $("#" + stepAI);
            clickLunk(stepAI, clickedLunk, 1);
            makeStepAI = true;
        }
    }
}