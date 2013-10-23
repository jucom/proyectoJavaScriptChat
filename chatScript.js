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
            alert(received);
            var obj = JSON.parse(received);
            //chatBody.innerHTML += 'update ' + cursor + ' <br/>';
            //cursor++;
            cursor += obj.length;
            //for (int i=0, c=received.length; i<c; i++) {
            //   chatBody.innerHTML += received[i] + '<br/>';
            //}
        }
    }
}

var updateChatCycle = setInterval(function(){updateChat();},1500);

//to stop the update call this function
//function stopUpdate() {
//	clearInterval(updateChat);
//}
