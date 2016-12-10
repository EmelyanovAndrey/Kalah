var wsUri = "ws://localhost:8080/kalah-1.0/game";
var WS = new WebSocket(wsUri);

WS.onerror = function (evt) {
    alert('ERROR: ' + evt.data);
};

WS.onmessage = function (evt) {
    console.log("received: " + evt.data);
    var mesg = JSON.parse(evt.data);
    if (mesg.operation == "create"){
        alert("Сессия вебсокета успешно добавлена к заявке.")
    }
};

var login = "";
var role = "";

$(document).ready(function () {

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

                }
            } else {
                alert("Почему то нет сохраненного логина в сессии, печалька ;(");
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert("Error status code: " + xhr.status);
            alert("Error details: " + thrownError);
        }
    });
    function sendMessage(json) {
        console.log("sending message to websocket: " + json);
        WS.send(json);
    }
});