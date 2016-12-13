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
        alert("Сессия вебсокета успешно добавлена к заявке.")
        var message = {operation: "getBoard"};
        sendMessage(JSON.stringify(message));
    }
    if (response.operation == "join") {
        if (response.role == "joined") {
            var message = {operation: "getBoard"};
            sendMessage(JSON.stringify(message));
            alert("Сессия вебсокета успешно добавлена к заявке. Ожидайте пока ее одобрят.");
        }
        if (response.role == "creator") {
            alert("Поступила заявка на подтверждение.")
            var message = {};
            message.operation = "conf";
            message.login = login;
            var answer = confirm("К вам хочет присоединится " + response.joinedLogin);
            if (answer == true) {
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
                } else if (response.priority == "false") {
                    alert("Ваше присоединение одобрено, но вы ходите вторым.");
                }
            } else if (conf == "no") {
                alert("Вас послали!");
                location.href = '/kalah-1.0/game-list.html';
                //TODO: надо удалить вебсокет, но он вроде сам удаляатся, когда мы покидаем страницу
            }
        }
        if (response.role == "creator") {

            if (response.priority == "true") {
                alert("Вы ходите первым.");
            } else if (response.priority == "false") {
                alert("Вы ходите вторым.");
            }
        }
    }
    if (response.operation == "getBoard") {
        createBoard(response.holeCount, response.stoneCount);
    }
    if (response.operation == "step") {
        isYourLunksEmpty();

        var num = response.num;
        var clickedLunk = $("#"+num);
        console.log("пришла лунка " + num);
        clickLunk(num, clickedLunk);
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


function createBoard(holeCount, stoneCount) {
    alert("Щас сгенерится таблица.");
    length = holeCount * 2 + 2;
    sum = holeCount * 2 * stoneCount;
    console.log("длина массива" + length);
    if (role == "creator") {
        var tr1 = $("<tr></tr>").append('<td rowspan="2" id="' + (length - 1) + '" class="kalah enemy-kalah enemy-obj">0</td>');
        var tr2 = $("<tr></tr>");
        for (var i = length - 2; i > length / 2 - 1; i--) {
            tr1.append('<td id="' + i + '" class="lunka enemy-lunk enemy-obj">' + stoneCount + '</td>');
        }
        tr1.append('<td id="' + (length / 2 - 1) + '" rowspan="2" class="kalah your-kalah">0</td>');
        for (var i = 0; i < length / 2 - 1; i++) {
            tr2.append('<td id="' + i + '" class="your-lunk lunka">' + stoneCount + '</td>');
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
        tr1.append('<td id="' + (length - 1) + '" rowspan="2" class="kalah your-kalah">0</td>');
        for (var i = length / 2; i < length - 1; i++) {
            tr2.append('<td id="' + i + '" class="your-lunk lunka">' + stoneCount + '</td>');
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
                enemyKalah.text(+enemyKalah.text()+$("#"+i).text());
                $("#"+i).text(0);
            }
        } else {
            for (var i = length / 2 - 2; i >= 0; i--) {
                enemyKalah.text(+enemyKalah.text()+$("#"+i).text());
                $("#"+i).text(0);
            }
        }
        alert("Вы проиграли!");
    }
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
    setTimeout(createWSconnect, 1000);
    $(".board").on("click", ".your-lunk", function () {
        var clickedLunk = $(this);
        var num = +clickedLunk.attr("id");
        console.log("вы кликнули на лунку номер " + num);
        clickLunk(num, clickedLunk);
    });
});

function clickLunk(num, clickedLunk) {
    var basket = +clickedLunk.text();
    var fullCycle = false;
    if (basket != 0) {
        board[num] = 0;
        clickedLunk.text(0);
        var i = num + 1;
        for (; basket != 0; i++) {
            console.log(num);
            if (role == "creator" && i == length - 1) {
                i = 0;
                fullCycle = true;
            } else if (role == "joined" && i == length / 2 - 1) {
                i = length / 2;
                fullCycle = true;
            } else if (role == "joined" && i == length) {
                i = 0;
            }
            board[i] = +board[i] + 1;
            $("#" + i).text(board[i]);
            basket--;
        }
        i--;
        console.log(board);
        console.log("остпновились " + i);
        if (fullCycle && $("#" + i).hasClass("your-lunk") && +$("#" + i).text() != 1) {
            clickLunk(i, $("#" + i));
        }
        getStone(i);
        isWinnerExist();

        var message = {};
        message.operation = step;
        message.num = num;
        sendMessage(JSON.stringify(message));

    }
}

function getStone(i) {
    var lunk = $("#" + i);
    var count = +lunk.text();
    if (i >= 0 && lunk.hasClass("enemy-lunk") && ( count == 2 || count == 3)) {
        board[i] = 0;
        lunk.text(0);
        if (role == "creator") {
            board[length / 2 - 1] = board[length / 2 - 1] + count;
            $("#" + length / 2 - 1).text(+$("#" + length / 2 - 1).text() + count);
        } else if (role == "joined") {
            board[length - 1] = board[length - 1] + count;
            $("#" + length - 1).text(+$("#" + length - 1).text() + count);
        }
    }
    i--;
    getStone(i);
}