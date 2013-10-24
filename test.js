function chatLine(date,name,message) {
    this.date = date;
    this.name = name;    
    this.message = message;
}
var line1 = new chatLine('20:47:09','gabriel','hello');
if (line1 instanceof chatLine) {
    alert('line1 is a chatLine');
    alert('line1 -> ' + line1.date + ' ' + line1.name + ' : ' + line1.message); 
}
var line2 = new chatLine('20:47:10','gabriel','no one there?');
if (line2 instanceof chatLine) {
    alert('line2 is a chatLine');
    alert('line2 -> ' + line2.date + ' ' + line2.name + ' : ' + line2.message); 
}
var obj = new Array(line1,line2);
var sObj = JSON.stringify(obj);
alert('La stringification du tableau des deux lignes donne : ' + sObj);

var xhr = new XMLHttpRequest();
xhr.open('POST', '/test.html');
    
xhr.send(sObj);
