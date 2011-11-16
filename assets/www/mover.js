function onDeviceReady() {
	var btn = document.getElementById("loadbutton");
	btn.onclick = function() {
		var path = document.getElementById("filepath").value;
		// validate path
		if (path=="") {
			path = "/sdcard/Florodora.epub";
		}
		window.plugins.Zipper.decompress(path,
				function(r){printResult(r)},
				function(e){console.log(e)}
		);
	}
	btn.disabled=false;
};

function printResult(fileInfo){
	var innerHtmlText=getHtml(fileInfo);    
	document.getElementById("contents").innerHTML=innerHtmlText;
}

function getHtml(fileInfo){
	var htmlText="<ul><li>filename: "+fileInfo.filename;
	htmlText=htmlText+"<ul><li>is dir: "+fileInfo.isdir+"</ul></li>";
	if(fileInfo.contents){
		/**
		htmlText=htmlText+"<ul><li>contents: "+fileInfo.contents+"</ul></li>";
		/**/
		for(var index=0;index<fileInfo.contents.length;index++){
			htmlText=htmlText+getHtml(fileInfo.contents[index]);
		}
	}
	htmlText=htmlText+"</li></ul>";
	return htmlText;

} 

function get_contacts() {
    var obj = new ContactFindOptions();
    obj.filter = "";
    obj.multiple = true;
    navigator.contacts.find(
            [ "displayName", "name" ], contacts_success,
            fail, obj);
}

function init() {
    // the next line makes it impossible to see Contacts on the HTC Evo since it
    // doesn't have a scroll button
    // document.addEventListener("touchmove", preventBehavior, false);
    document.addEventListener("deviceready", onDeviceReady, true);
}
