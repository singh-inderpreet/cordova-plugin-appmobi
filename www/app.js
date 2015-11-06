var module=angular.module('starter', ['ngCordovaOauth'])


module.controller('MyCtrl', function ($scope, $cordovaOauth) {
    
	//Facebook Login
    $scope.facebookLogin = function () {
        var dataObjAuth = AppMobiCloud.oauth.getOAuthDataList();
        for (var key in dataObjAuth) {
            if (dataObjAuth[key].provider == "facebook") {
                try{
                  var clientId = dataObjAuth[key].clientId;
                  var appScope = dataObjAuth[key].appScope;
                  var res = [];
                  var arrayScope = [];
                  res = appScope.split(",");
                  for (var i = 0; i < res.length; i++)
                  {
                    arrayScope.push(res[i].replace(/'/g, ""));
                  }
                 $cordovaOauth.facebook(clientId, [arrayScope]).then(function (result) {
                     var launchImageID = document.getElementById('launchImage');
                     launchImageID.style.display = 'block';
                     document.getElementById('light1').style.display = 'none';
                     document.getElementById('fade').style.display = 'none';
  					 document.getElementById('background').style.display = 'block';
                     var resultParse = JSON.parse(JSON.stringify(result));
                     var access_token = "";
                     for (var key in resultParse) {
                         if (key == "access_token") {
                             access_token = resultParse[key]
                         }
                     }
                     var tokenObject = {
                         access_token: access_token,
                         provider: "facebook",                       
                     };
                     var resultant = JSON.stringify(tokenObject);
                     AppMobiCloud.oauth.registerOAuth(resultant);
                     }, function (error) {
                     alert(error);
                  });
                }
                catch(e){
                    console.log("Exception : "+e.message);
                }
                
            }
        }
    }

	//Google Login
    $scope.googleLogin = function () {
        var dataObjAuth = AppMobiCloud.oauth.getOAuthDataList();
        for (var key in dataObjAuth) {
            if (dataObjAuth[key].provider == "google") {
                clientId = dataObjAuth[key].clientId;
                var appScope = dataObjAuth[key].appScope;
                var res = [];
                var arrayScope = [];
                res = appScope.split(",");
                for (var i = 0; i < res.length; i++) {
                    arrayScope.push(res[i].replace(/'/g, ""));
                }
                $cordovaOauth.google(clientId, [arrayScope]).then(function (result) {
                    var launchImageID = document.getElementById('launchImage');
                    launchImageID.style.display = 'block';
                    document.getElementById('light1').style.display = 'none';
                    document.getElementById('fade').style.display = 'none';
  					document.getElementById('background').style.display = 'block';
                    var resultParse = JSON.parse(JSON.stringify(result));
                    var access_token = "";
                    for (var key in resultParse) {
                        if (key == "access_token") {
                            access_token = resultParse[key]
                        }
                    }
                    var tokenObject = {
                        access_token: access_token,
                        provider: "google",
                    };
                    var resultant = JSON.stringify(tokenObject);
                    AppMobiCloud.oauth.registerOAuth(resultant);
                }, function (error) {
                    alert("Error -> " + error);
                });
            }
        }
    }
	
	//linkedIN Login
    $scope.linkedInLogin = function () {
        var dataObjAuth = AppMobiCloud.oauth.getOAuthDataList();
        for (var key in dataObjAuth) {
            if (dataObjAuth[key].provider == "linkedin") {
                clientId = dataObjAuth[key].clientId;
                clientSecert = dataObjAuth[key].clientSecert;
                var appScope = dataObjAuth[key].appScope;
                var res = [];
                var arrayScope = [];
                res = appScope.split(",");
                for (var i = 0; i < res.length; i++) {
                    arrayScope.push(res[i].replace(/'/g, ""));
                }
                $cordovaOauth.linkedin(clientID, clientSecert, [arrayScope]).then(function (result) {
                    var launchImageID = document.getElementById('launchImage');
                    launchImageID.style.display = 'block';
                    document.getElementById('light1').style.display = 'none';
                    document.getElementById('fade').style.display = 'none';
  					document.getElementById('background').style.display = 'block';
                    var resultParse = JSON.parse(JSON.stringify(result));
                    var access_token = "";
                    for (var key in resultParse) {
                        if (key == "access_token") {
                            access_token = resultParse[key]
                        }
                    }
                    var tokenObject = {
                        access_token: access_token,
                        provider: "linkedin",
                    };
                    var resultant = JSON.stringify(tokenObject);
                    AppMobiCloud.oauth.registerOAuth(resultant);
                }, function (error) {
                    alert("Error -> " + error);
                });
            }
        }
    }
   
});