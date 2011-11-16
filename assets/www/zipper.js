/**
 *  
 * @return Object literal singleton instance of DirectoryListing
 */
var Zipper = function() {
};

/**
  * @param directory The directory for which we want the listing
  * @param successCallback The callback which will be called when directory listing is successful
  * @param failureCallback The callback which will be called when directory listing encouters an error
  */
Zipper.prototype.list = function(directory, successCallback, failureCallback) {
 return PhoneGap.exec(
	successCallback,    //Success callback from the plugin
    failureCallback,     //Error callback from the plugin
    'Zipper',  //Tell PhoneGap to run "Zipper" Plugin
    'list',              //Tell plugin, which action we want to perform
    [directory]);        //Passing list of args to the plugin
};
 
/**
 * @param zipfile The file which we want to decompress
 * @param successCallback The callback which will be called when decompression is successful
 * @param failureCallback The callback which will be called when decompression encouters an error
 */
Zipper.prototype.decompress = function(zipfile, successCallback, failureCallback) {
return PhoneGap.exec(
	successCallback,    //Success callback from the plugin
   failureCallback,     //Error callback from the plugin
   'Zipper',  //Tell PhoneGap to run "Zipper" Plugin
   'decompress',              //Tell plugin, which action we want to perform
   [zipfile]);        //Passing list of args to the plugin
};

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin("Zipper", new Zipper());
	PluginManager.addService("Zipper", "org.nypl.mover.Zipper");
});
