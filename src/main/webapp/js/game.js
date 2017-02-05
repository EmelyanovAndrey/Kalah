var wsUri = "ws://localhost:8080/kalah-1.0/game";
var WS = new WebSocket(wsUri);
var login = "";
var role = "";

var board = [];
var length;
var sum;

WS.onerror = function (evt) {
    alert('ERROR: ' + evt.data);
};

WS.onmessage = function (evt) {
    console.log("received: " + evt.data);
    var response = JSON.parse(evt.data);
    if (response.operation == "create") {
        // alert("Сессия вебсокета успешно добавлена к заявке.")
        var user = $("<img src='"+response.avatar+"'><div class='nick'>"+login+"</div>");
        $(".player").append(user);
        var message = {operation: "getBoard"};
        sendMessage(JSON.stringify(message));
    }
    if (response.operation == "join") {
        if (response.role == "joined") {
            var message = {operation: "getBoard"};
            sendMessage(JSON.stringify(message));
            //   alert("Сессия вебсокета успешно добавлена к заявке. Ожидайте пока ее одобрят.");
            var user = $("<img src='"+response.yourAvatar+"'><div class='nick'>"+login+"</div>");
            $(".player").append(user);
            user = $("<img src='"+response.enemyAvatar+"'><div class='nick'>"+response.enemyLogin+"</div>");
            $(".apponent").append(user);
        }
        if (response.role == "creator") {
            //    alert("Поступила заявка на подтверждение.")
            var message = {};
            message.operation = "conf";
            message.login = login;
            var answer = confirm("К вам хочет присоединится " + response.joinedLogin);
            if (answer == true) {
                var user = $("<img src='"+response.enemyAvatar+"'><div class='nick'>"+response.joinedLogin+"</div>");
                $(".apponent").append(user);
                message.conf = "yes";
            } else {
                message.conf = "no";
            }

            sendMessage(JSON.stringify(message));
        }
    }
    if (response.operation == "conf") {
        if (response.role == "joined") {
            var conf = response.conf;
            if (conf == "yes") {
                if (response.priority == "true") {
                    alert("Ваше присоединение одобрено, и вы ходите первым.");
                    enableLunks();
                } else if (response.priority == "false") {
                    alert("Ваше присоединение одобрено, но вы ходите вторым.");
                }
            } else if (conf == "no") {
                alert("Вас послали!");
                location.href = '/kalah-1.0/game-list.html';
            }
        }
        if (response.role == "creator") {

            if (response.priority == "true") {
                alert("Вы ходите первым.");
                enableLunks();

            } else if (response.priority == "false") {
                alert("Вы ходите вторым.");
            }
        }
    }
    if (response.operation == "getBoard") {
        createBoard(response.holeCount, response.stoneCount);
    }
    if (response.operation == "step") {

        var num = response.num;

        console.log("пришли данные о ходе " + response.toString());
        var clickedLunk = $("#" + num);
        clickLunk(num, clickedLunk, 1);
        if (!isYourLunksEmpty()) {
            enableLunks();
        }
    }
    if (response.operation == "creator closed") {
        alert("Противник отсоединился.");
        location.href = '/kalah-1.0/game-list.html';
    }
    if (response.operation == "joined closed") {
        alert("Противник отсоединился.");
    }
    if (response.operation == "creator closed in game") {
        alert("Противник отсоединился.");
        location.href = '/kalah-1.0/game-list.html';
    }
    if (response.operation == "joined closed in game") {
        alert("Противник отсоединился.");
        location.href = '/kalah-1.0/game-list.html';
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
                if (role == "creator") {
                    var message = {};
                    message.operation = "create";
                    message.login = login;
                    sendMessage(JSON.stringify(message));
                } else if (role == "joined") {
                    var message = {};
                    message.operation = "join";
                    message.login = login;
                    message.creatorLogin = res.creatorLogin;
                    sendMessage(JSON.stringify(message));
                }
            } else {
                alert("Каким-то хреном вы добрались сюда неавторизованным ;(");
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
        $(el).removeClass("enemy-obj");

    });
    $(".board").on("click", ".your-lunk", onLunkClick);
}
function disableLunks() {
    $(".your-lunk").each(function (i, el) {
        $(el).addClass("enemy-obj");

    });
    $(".board").off("click", ".your-lunk", onLunkClick);

}

function createBoard(holeCount, stoneCount) {
    // alert("Щас сгенерится таблица.");
    length = holeCount * 2 + 2;
    sum = holeCount * 2 * stoneCount;
    console.log("длина массива" + length);
    if (role == "creator") {
        var tr1 = $("<tr></tr>").append('<td rowspan="2" id="' + (length - 1) + '" class="kalah enemy-kalah enemy-obj">0</td>');
        var tr2 = $("<tr></tr>");
        for (var i = length - 2; i > length / 2 - 1; i--) {
            tr1.append('<td id="' + i + '" class="lunka enemy-lunk enemy-obj">' + stoneCount + '</td>');
        }
        tr1.append('<td id="' + (length / 2 - 1) + '" rowspan="2" class="kalah your-kalah enemy-obj">0</td>');
        for (var i = 0; i < length / 2 - 1; i++) {
            tr2.append('<td id="' + i + '" class="your-lunk lunka enemy-obj">' + stoneCount + '</td>');
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
        console.log("создается таблица для присоединившегося");
        var tr1 = $("<tr></tr>").append('<td rowspan="2" id="' + (length / 2 - 1) + '" class="kalah enemy-kalah enemy-obj">0</td>');
        var tr2 = $("<tr></tr>");
        for (var i = length / 2 - 2; i >= 0; i--) {
            tr1.append('<td id="' + i + '" class="lunka enemy-lunk enemy-obj">' + stoneCount + '</td>');
        }
        tr1.append('<td id="' + (length - 1) + '" rowspan="2" class="kalah your-kalah enemy-obj">0</td>');
        for (var i = length / 2; i < length - 1; i++) {
            tr2.append('<td id="' + i + '" class="your-lunk lunka enemy-obj">' + stoneCount + '</td>');
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
    console.log(board);
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
    if (isEmpty) {
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
        alert("Вы проиграли!");
    }
    return isEmpty;
}

function isWinnerExist() {
    var yourKalah = $(".your-kalah");
    var enemyKalah = $(".enemy-kalah");
    if (+yourKalah.text() > sum / 2) {
        alert("Вы выиграли!");
    }
    if (+enemyKalah.text() > sum / 2) {
        alert("Вы проиграли!");
    }
}

$(document).ready(function () {
    setTimeout(createWSconnect, 500);
});

var onLunkClick = function onLunkClickListener() {
    var clickedLunk = $(this);
    if (clickedLunk.text() != 0) {
        disableLunks();

        var num = +clickedLunk.attr("id");
        var message = {};
        message.operation = "step";
        message.num = num;
        message.role = role;
        sendMessage(JSON.stringify(message));
        console.log("вы кликнули на лунку номер " + num);
        clickLunk(num, clickedLunk, 0);
    }
}

function clickLunk(num, clickedLunk, par) {
    var localRole = role;
    if (par == 1) {
        if (localRole == "creator") {
            localRole = "joined";
        } else if (localRole == "joined") {
            localRole = "creator";
        }
    }
    console.log("ход: " + num + " " + par + " " + localRole);
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
        console.log("     Ход: " + i + ":" + basket);
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
        console.log("i after put stone: " + i);
        i--;
        console.log(board);
        console.log("остановились " + i);
        console.log("par: " + par);
        if (par == 0) {
            console.log("par: " + par + ", full " + fullCycle);
            if (fullCycle && $("#" + i).hasClass("your-lunk") && +$("#" + i).text() != 1) {
                console.log("par: dceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                clickLunk(i, $("#" + i), 0);
            }
        } else {
            console.log("par: " + par + ", full " + fullCycle);
            if (fullCycle && $("#" + i).hasClass("enemy-lunk") && +$("#" + i).text() != 1) {
                console.log("par: dceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                clickLunk(i, $("#" + i), 1);
            }
        }
        getStone(i, par);

    }

}

function getStone(i, par) {
    console.log("getStoneFirst: " + i);
    var lunk = $("#" + i);
    var count = +lunk.text();
    console.log("Count: " + count);

    var hasClass = false;
    if (par == 0) {
        hasClass = lunk.hasClass("enemy-lunk");
    } else {
        hasClass = lunk.hasClass("your-lunk");
    }
    console.log("Has class enemy: " + hasClass);
    if (i >= 0 && hasClass && ( count == 2 || count == 3)) {
        console.log("get stone " + i);
        lunk.text(0);
        if (par == 0) {
            if (role == "creator") {
                console.log("par: " + par + ", role: " + role);
                board[length / 2 - 1] = board[length / 2 - 1] + count;
                $("#" + (length / 2 - 1)).text(board[length / 2 - 1]);
            } else if (role == "joined") {
                console.log("par: " + par + ", role: " + role);
                board[length - 1] = board[length - 1] + count;
                $("#" + (length - 1)).text(board[length - 1]);
            }
        } else {
            if (role == "joined") {
                console.log("par: " + par + ", role: " + role);
                board[length / 2 - 1] = board[length / 2 - 1] + count;
                $("#" + (length / 2 - 1)).text(board[length / 2 - 1]);
            } else if (role == "creator") {
                console.log("par: " + par + ", role: " + role);
                board[length - 1] = board[length - 1] + count;
                $("#" + (length - 1)).text(board[length - 1]);
            }
        }
        lunk.css("background-color", "#a5a5a5").css("color", "#37474f");
        setTimeout(function () {

            lunk.css("background-color", "#37474f").css("color", "#f5f5f5");
            i--;
            getStone(i, par);
        }, 500);
    } else {
        console.log("i after get stone: " + i);
        isWinnerExist();
    }
}