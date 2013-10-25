var bouton = document.getElementById('boutonSend');
var chatBody = document.getElementById('chatBody');
var chatSide = document.getElementById('chatSide');
var messageToSend = document.getElementById('messageToSend');
var nameParticipant = " ";
var cursor = 1;

function sendParticipant() {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/chat.java?sendParticipant');
    xhr.send(nameParticipant);
}
// ask the name of the user if nameParticipant is empty
if (nameParticipant == " ") {
    nameParticipant = prompt('Entrez votre prenom ? ');
    var send = sendParticipant();
}


//alert('bouton est :' + bouton);
//alert('chatBody est :' + chatBody);
//alert('chatSide est :' + chatSide);
//alert('messageToSend est :' + messageToSend);
//alert('nom :' + nameParticipant);

function chatLine(date,name,message) {
    this.sendTime = date;
    this.name = name;    
    this.message = message;
}

function sendLine (date,name,message) {
    //alert('sending line : ' + date + ' ' + name + ':' + message);
    var line = new chatLine(date,name,message);
    var sLine = JSON.stringify(line);
    var xhr = new XMLHttpRequest();
    //alert(sLine);
    xhr.open('POST','/chat.java?sendLine');    
    xhr.send(sLine);
    if (xhr.readyState == xhr.DONE) {
       updateChat();
    }
}

function timer() {
	var d = new Date();
	var time = d.toLocaleTimeString();
	return time;
}

bouton.addEventListener('click',function(){sendLine(timer(),nameParticipant,messageToSend.value)},false);

function updateChat() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/chat.java?chatFrom('+cursor+')');
    
    xhr.send(null);
    
    xhr.onreadystatechange = function() {
        if (xhr.readyState == xhr.DONE) {
            var received = xhr.responseText;
            var lines = JSON.parse(received);
            cursor += lines.length;
            for (var i=0, c=lines.length; i<c; i++) {
                chatBody.innerHTML += lines[i].sendTime + ' ' + lines[i].name + ' : ' + lines[i].message + '<br/>';
            }
        }
   }
}

var updateChatCycle = setInterval(function(){updateChat();},3000);

//to stop the update call this function
//function stopUpdate() {
//	clearInterval(updateChat);
//}
