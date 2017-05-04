function addLoadEvent(func) {
	var oldonload = window.onload;
	if (typeof window.onload != "function") {
		window.onload = func;
	} else {
		window.onload = function() {
			oldonload();
			func();
		}
	}
}
function tagCloud() {
	if (document.getElementById('tagbox')) {
		var tags = document.getElementById('tagbox');
		var tagLinks = tags.getElementsByTagName('div');
		var styles = new Array();
		styles[0] = new Array('#00FF00', '#888888', '#00DDDD', '#FF0000', '#0044BB',  '#FF44AA','#20B2AA', '#FF7F50');
		styles[1] = new Array('28px', '24px', '22px','24px', '28px', '24px', '28px'); 
		styles[2] = new Array('400', '500', '600'); 
		styles[3] = new Array('5px','25px','12px','3px','25px','12px','3px');
		for (var i = 0; i<tagLinks.length;i++){
			tagLinks[i].style.color =styles[0][Math.floor(styles[0].length * Math.random())];
			tagLinks[i].style.fontSize = styles[1][Math.floor(styles[1].length * Math.random())]; 
			tagLinks[i].style.fontWeight = styles[2][Math.floor(styles[2].length * Math.random())];
			tagLinks[i].style.paddingLeft = styles[3][Math.floor(styles[3].length * Math.random())];
			tagLinks[i].style.height=tagLinks[i].style.fontSize;
		}
		for (var i = 0; i<tagLinks.length;i++){	
			tagLinks[i].style.float="left";
		}
	}
}
addLoadEvent(tagCloud);