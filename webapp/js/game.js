var wsUri = "ws://localhost:8080/kalah-1.0/game";
var WS = new WebSocket(wsUri);
var login = "";
var role = "";

WS.onerror = function (evt) {
    alert('ERROR: ' + evt.data);
};

WS.onmessage = function (evt) {
    console.log("received: " + evt.data);
    var response = JSON.parse(evt.data);
    if (response.operation == "create") {
        alert("Сессия вебсокета успешно добавлена к заявке.")
    }
    if (response.operation == "join") {
        if (response.role == "joined") {
            alert("Сессия вебсокета успешно добавлена к заявке. Ожидайте пока ее одобрят.")
        }
        if (response.role == "creator") {
            alert("Поступила заявка на подтверждение.")
            var message = {};
            message.operation = "conf";
            message.login = login;
            var answer = confirm("К вам хочет присоединится " + response.joinedLogin);
            if(answer == true) {
                message.conf = "yes";
            } else {
                message.conf = "no";
            }

            sendMessage(JSON.stringify(message));
        }
    }
    if (response.operation == "conf") {
        var message = {};
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

$(document).ready(function () {
    setTimeout(createWSconnect, 1000);
});