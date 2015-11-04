/// <reference path="lib/xmlToJSON.js" />
var cordova = require('cordova'),
    appMobiCloud = require('./appMobiCloud');


module.exports = { 


    checkPushUser:function (successCallback, errorCallback, strInput) {

        WinJS
                       .xhr(
                               { url: " https://webservices.appmobi.com/pushmobiint.aspx/?CMD=ampush.checkuser&user={}&passcode=verma&appname=8rLsIAaolremzJQ_OBb7OVuhZIUAJID7vN1vaDpRDdw" }
                            )
                       .then
                            (
                               function (result) {
                                   var xmlResponse = result.responseXML;                       
                                   var items = xmlResponse.querySelector("function");
                                   var res = items.getAttribute("return");
                                   var msg = items.getAttribute("msg");
                               }
                                   
                            );       
    },

    addPushUser:function (successCallback, errorCallback, strInput) {

    WinJS
                   .xhr(
                           { url: " https://webservices.appmobi.com/pushmobiint.aspx/?CMD=ampush.checkuser&user={}&passcode=verma&appname=8rLsIAaolremzJQ_OBb7OVuhZIUAJID7vN1vaDpRDdw" }
                        )
                   .then
                        (
                           function (result) {
                               var xmlResponse = result.responseXML;                       
                               var items = xmlResponse.querySelector("function");
                               var res = items.getAttribute("return");
                               var msg = items.getAttribute("msg");                           
                           }
                                   
                        );       
}  
    
};
require("cordova/exec/proxy").add("AppMobiCloud", module.exports);
