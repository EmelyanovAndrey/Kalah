/**
 * Created by Igor on 06.12.2016.
 */
var creatorWsUri = "ws://localhost:8080/kalah-1.0/game/creator";
var creatorWS = new WebSocket(creatorWsUri);

creatorWS.onerror = function(evt) { onError(evt) };

function onError(evt) {
    alert('ERROR: ' + evt.data);
}

creatorWS.onmessage = function(evt) { onMessage(evt) };

function onMessage(evt) {
    console.log("received: " + evt.data);

}

function sendCreatorMessage(json) {
    console.log("sending creator message: " + json);
    creatorWS.send(json);
}

