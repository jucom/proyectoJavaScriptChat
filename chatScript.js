var bouton = document.getElementById('boutonSend');
var chatBody = document.getElementById('chatBody');
var chatSide = document.getElementById('chatSide');
var cursor = 1;

alert('bouton est :' + bouton);
alert('chatBody est :' + chatBody);
alert('chatSide est :' + chatSide);


bouton.addEventListener('click',function(){},false);

function timer() {
	var d = new Date();
	var time = d.toLocaleTimeString();
	return time;
}

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

var updateChatCycle = setInterval(function(){updateChat();},1500);

//to stop the update call this function
//function stopUpdate() {
//	clearInterval(updateChat);
//}
