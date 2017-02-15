module.exports = function(context) {
    var fs = require('fs');
    var path = require('path');
	  var packageName="";

    // no need to configure below
    var sourceDir = context.opts.projectRoot+"/platforms/android/assets/www";

    var destinationDir = context.opts.projectRoot+"platforms/android";
    var fs = context.requireCordovaModule('fs'),
    projectPath = context.requireCordovaModule('path');

	var platformRoot = projectPath.join(context.opts.projectRoot, 'platforms/android');

	var manifestFile = projectPath.join(platformRoot, 'AndroidManifest.xml');

  if (fs.existsSync(manifestFile)) {

    fs.readFile(manifestFile, 'utf8', function (err,data) {
      if (err) {
        throw new Error('Unable to find AndroidManifest.xml: ' + err);
      }
		packageName=getValue(data)
		var googleJSON = fs.readFileSync("platforms/android/google-services.json").toString();
		googleJSON= googleJSON.replace('PACKAGE',packageName);
		fs.writeFileSync("platforms/android/google-services.json", googleJSON);
	  });

  }
	//Copy file from www to android folder.
    var paths = ["platforms/android/assets/www/google-services.json"];
		for (var i = 0; i < paths.length; i++) {

	 if (fileExists(paths[i])) {
		try{
		var contents = fs.readFileSync(paths[i]).toString();
		fs.writeFileSync("platforms/android/google-services.json", contents);
	    var json = JSON.parse(contents);
		}

		catch(Exception){
			console.log(Exception);
		}
		}else{
		console.log("Push will not work as google-services.json is not uploaded");
			}
		}


function fileExists(path) {
  try  {
    return fs.statSync(path).isFile();
  }
  catch (e) {
	console.log(e);
    return false;
  }
 }

var getValue = function(config) {
    var value = config.match(new RegExp('package=(?:"[^"]*"|^[^"]*$)'))
    if(value) {
        return value.toString().split("=")[1]
    } else {
        return null
    }
}
}
