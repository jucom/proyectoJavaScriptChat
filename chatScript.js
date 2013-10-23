var bouton = document.getElementById('boutonSend');
var chatBody = document.getElementById('chatBody');
var chatSide = document.getElementById('chatSide');

alert('bouton est :' + bouton);
alert('chatBody est :' + chatBody);
alert('chatSide est :' + chatSide);


bouton.addEventListener('click',function(){},false);

function timer() {
	var d = new Date();
	var time = d.toLocaleTimeString();
	return time;
}


var updateChat = setInterval(function(){alert(timer());},1000);

//to stop the update call this function
//function stopUpdate() {
//	clearInterval(updateChat);
//}
