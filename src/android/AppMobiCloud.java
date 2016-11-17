package com.appMobiCloud;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AppMobiCloud extends CordovaPlugin {

    private static final int READ_PHONE_STATE_REQ_CODE = 1001;
    private static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static AppMobiCloud plugin;
    private static CordovaInterface cordova;
    private static CordovaWebView webView;
    private static String userID, pass, email;
    private static String notificationID;
    public CallbackContext callback;
    boolean shouldExecuteOnResume;
    String userName;
    String password;
    String passPhrase;
    public static boolean isActive() {
        return webView != null;
    }

    // ************************************************* //
    // Initialize Plugin
    // ************************************************* //

    @Override
    public void initialize(CordovaInterface _cordova, CordovaWebView _webView) {
        super.initialize(cordova, webView);
        cordova = _cordova;
        webView = _webView;
    }

    @Override
    protected void pluginInitialize() {
        shouldExecuteOnResume = false;
        AppMobiCloudController.pluginInitialize(cordova.getActivity(), webView);
    }

    public boolean execute(String action, final JSONArray args,
                           final CallbackContext callbackContext) throws JSONException {

        // ************************************************* //
        // pushMobi
        // ************************************************* //
        if (action.equalsIgnoreCase("initialize")) {
            userName = args.getString(0);
            password = args.getString(1);
            callback = callbackContext;
            int currentVer = android.os.Build.VERSION.SDK_INT;
            if (currentVer >= 23) {
                if (!PermissionHelper.hasPermission(this, READ_PHONE_STATE)) {
                    shouldExecuteOnResume = false;
                    getPermission(READ_PHONE_STATE_REQ_CODE, READ_PHONE_STATE);
                } else {
                    performInitialise();
                }
            } else {
                cordova.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (AppMobiCloudController.sharedController != null) {
                            AppMobiCloudController.sharedController.initializeApp(callback,
                                    userName, password);
                        }

                    }
                });

            }


        }


        if(action.equalsIgnoreCase("initializeWithPassPhrase")){
            passPhrase = args.getString(0);
            callback = callbackContext;
            if(passPhrase.trim().length()!=0&&passPhrase.length()>=4) {
                cordova.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppMobiCloudController.sharedController.initializeWithPassPhrase(callback,
                                passPhrase);
                    }
                });
            }else{
                final HashMap<String, Object> properties = new HashMap<String, Object>();
                properties.put("success", false);
                properties.put("oauth", false);
                properties.put("isPassPhraseEnabled", true);
                properties.put("message", "user input required");
                cordova.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppMobiEvent event = new AppMobiEvent(
                                "appMobi.user.input", properties);
                        AppMobiCloudController.sharedController
                                .fireJSEvent(event);
                        return;

                    }
                });
                callback.error("Passphrase must not be empty or less than 4 characters.");

            }


        }


        if (action.equalsIgnoreCase("getPassCode")) {
            callback = callbackContext;
            final String db = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController != null) {
                        AppMobiCloudController.sharedController.getPasscode(callback, db);
                    }

                }
            });
        }

        if (action.equalsIgnoreCase("GetCouchDBUser")) {
            callback = callbackContext;
            final String db = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController != null) {
                        AppMobiCloudController.sharedController.GetCouchDBUser(callback, db);
                    }

                }
            });
        }


        if (action.equalsIgnoreCase("checkPushUser")) {
            userID = args.getString(0);
            pass = args.getString(1);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudPush != null) {
                        AppMobiCloudController.sharedController.cloudPush
                                .checkPushUser(userID, pass);
                    }

                }
            });
        }
        if (action.equalsIgnoreCase("addPushUser")) {
            userID = args.getString(0);
            pass = args.getString(1);
            email = args.getString(2);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudPush != null)
                        AppMobiCloudController.sharedController.cloudPush
                                .addPushUser(userID, pass, email);
                }
            });

        }

        if (action.equalsIgnoreCase("readPushNotifications")) {
            notificationID = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudPush != null)
                        AppMobiCloudController.sharedController.cloudPush
                                .readPushNotifications(notificationID, true);
                }
            });

        }

        if (action.equalsIgnoreCase("editPushUser")) {
            final String email = args.getString(0);
            final String password = args.getString(1);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudPush != null)
                        AppMobiCloudController.sharedController.cloudPush
                                .editPushUser(email, password, "");
                }
            });

        }

        if (action.equalsIgnoreCase("deletePushUser")) {
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudPush != null)
                        AppMobiCloudController.sharedController.cloudPush
                                .deletePushUser();
                }
            });

        }
        if (action.equalsIgnoreCase("refreshPushNotifications")) {
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudPush != null)
                        AppMobiCloudController.sharedController.cloudPush
                                .refreshPushNotifications();
                }
            });

        }

        if (action.equalsIgnoreCase("sendPushUserPass")) {
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudPush != null)
                        AppMobiCloudController.sharedController.cloudPush
                                .sendPushUserPass();
                }
            });

        }

        if (action.equalsIgnoreCase("sendPushNotification")) {

            final String userID = args.getString(0);
            final String message = args.getString(1);
            final String data = args.getString(2);

            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudPush != null)
                        AppMobiCloudController.sharedController.cloudPush
                                .sendPushNotification(userID, message, data);
                }
            });

        }

        if (action.equalsIgnoreCase("findPushUser")) {
            final String userID = args.getString(0);
            final String emailID = args.getString(1);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudPush != null)
                        AppMobiCloudController.sharedController.cloudPush
                                .findPushUser(userID, emailID);
                }
            });

        }

        if (action.equalsIgnoreCase("setPushUserAttributes")) {

            final String attributes = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudPush != null)
                        AppMobiCloudController.sharedController.cloudPush
                                .setPushUserAttributes(attributes);
                }
            });

        }

        if (action.equalsIgnoreCase("alert")) {

            final String msg = args.getString(0);
            final String title = args.getString(1);
            final String btn = args.getString(2);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudPush != null)
                        AppMobiCloudController.sharedController.cloudPush
                                .alert(msg, title, btn);
                }
            });

        }

        // ************************************************* //
        // Live Update
        // ************************************************* //

        if (action.equalsIgnoreCase("installUpdate")) {
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    AppMobiCloudController.sharedController
                            .installManualUpdate();
                }
            });

        }

        // ************************************************* //
        // oAuth Registration
        // ************************************************* //
        if (action.equalsIgnoreCase("registerOAuth")) {
            final String token = args.getString(0);
            final String provider = args.getString(1);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    AppMobiCloudController.sharedController
                            .registerOAuth(token);
                }
            });
        }

        // ************************************************* //
        // Secure Storage Implementation
        // ************************************************* //

        if (action.equalsIgnoreCase("saveSecureData")) {
            final String key = args.getString(0);
            final String value = args.getString(1);
            final String isMasterData = args.getString(2);
            final String isSyncRequired = args.getString(3);
            final String isJSON = args.getString(4);

            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudSecure != null)
                        AppMobiCloudController.sharedController.cloudSecure
                                .saveSecureData(key, value, isMasterData,
                                        isSyncRequired, isJSON);
                }
            });

        }

        if (action.equalsIgnoreCase("syncSecureData")) {
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudSecure != null)
                        AppMobiCloudController.sharedController.cloudSecure
                                .sync();
                }
            });

        }

        if (action.equalsIgnoreCase("readSecureData")) {
            final String key = args.getString(0);
            final String isMasterData = args.getString(1);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudSecure != null)
                        AppMobiCloudController.sharedController.cloudSecure
                                .readSecureData(key, isMasterData);
                }
            });

        }



        /***************************************
         Analytics Implementation
         /****************************************/

        if (action.equalsIgnoreCase("logCustomEvent")) {
            callback = callbackContext;
            final String event = args.getString(0);
            final String value = args.getString(1);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController != null)
                        AppMobiCloudController.sharedController
                                .logCustomEvent(callback, event,value);
                }
            });

        }

        if (action.equalsIgnoreCase("logPageEvent")) {
            callback = callbackContext;
            final String value = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController != null)
                        AppMobiCloudController.sharedController.logPageEvent(callback,value);
                }
            });

        }

        if (action.equalsIgnoreCase("logMethodEvent")) {
            callback = callbackContext;
            final String value = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController != null)
                        AppMobiCloudController.sharedController
                                .logMethodEvent(callback,value);
                }
            });

        }


        /***************************************
                Couch/Pouch Implementation
        /****************************************/

        if (action.equalsIgnoreCase("getPassCode")) {
            callback = callbackContext;
            final String db = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController != null) {
                        AppMobiCloudController.sharedController.getPasscode(callback, db);
                    }

                }
            });
        }

        if (action.equalsIgnoreCase("GetCouchDBUser")) {
            callback = callbackContext;
            final String db = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController != null) {
                        AppMobiCloudController.sharedController.GetCouchDBUser(callback, db);
                    }

                }
            });
        }
        /***************************************
                 E2EE Implementation
         /****************************************/
        if (action.equalsIgnoreCase("getFileById")) {
            callback = callbackContext;
            final String fileId = args.getString(0);

            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudE2EE != null)
                        AppMobiCloudController.sharedController.cloudE2EE
                                .getFileFromServer(callback,fileId);
                }
            });
        }

        if (action.equalsIgnoreCase("getE2EEUsers")) {
            callback = callbackContext;
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudE2EE != null)
                        AppMobiCloudController.sharedController.cloudE2EE
                                .getUsersE2EE(callback);
                }
            });

        }
        if (action.equalsIgnoreCase("refreshE2EEUsers")) {
            callback = callbackContext;
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudE2EE != null)
                        AppMobiCloudController.sharedController.cloudE2EE
                                .refreshUsersE2EE(callback);
                }
            });

        }
        if (action.equalsIgnoreCase("sendEncryptedFile")) {
            callback = callbackContext;
            final String userName = args.getString(0);
            final String path = args.getString(1);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudE2EE != null)
                        AppMobiCloudController.sharedController.cloudE2EE
                                .EncryptFilewithUserName(callback, userName,path);
                }
            });

        }

        if (action.equalsIgnoreCase("getAllMessages")) {
            callback = callbackContext;
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudE2EE != null)
                        AppMobiCloudController.sharedController.cloudE2EE
                                .getAllMessages(callback);
                }
            });

        }
        if(action.equalsIgnoreCase("sendEncryptedMessage")){
            callback=callbackContext;
            final String userName = args.getString(0);
            final String message = args.getString(1);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudE2EE != null)
                        AppMobiCloudController.sharedController.cloudE2EE
                                .sendEncryptedMessage(userName,message,callback);
                }
            });

        }


       /* Encrypt Local Storage, DB and Custom Files*/
        if (action.equalsIgnoreCase("encryptLocalStorage")) {
            callback = callbackContext;
            final String isEncrypt = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudencrypt != null)
                        AppMobiCloudController.sharedController.cloudencrypt
                                .encryptStorage(isEncrypt, "Local Storage",
                                        callback);

                }
            });

        }

        if (action.equalsIgnoreCase("encryptCustomPath")) {
            callback = callbackContext;
            final String isEncrypt = args.getString(0);
            final String path = args.getString(1);

            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudencrypt != null) {
                        AppMobiCloudController.sharedController.cloudencrypt
                                .encryptCustomPath(isEncrypt, path, callback);

                    }

                }
            });

        }


        if (action.equalsIgnoreCase("encryptDatabase")) {
            callback = callbackContext;
            final String isEncrypt = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    if (AppMobiCloudController.sharedController.cloudencrypt != null)
                        AppMobiCloudController.sharedController.cloudencrypt
                                .encryptStorage(isEncrypt, "IndexedDB",
                                        callback);

                }
            });

        }

      if (action.equalsIgnoreCase("checkProtectionStatus")) {
        cordova.getThreadPool().execute(new Runnable() {
          @Override
          public void run() {
            if (AppMobiCloudController.sharedController != null)
              AppMobiCloudController.sharedController.checkProtectionStatus();

          }
        });

      }

        return true;
    }

    @Override
    public void onResume(boolean multitasking) {
        // TODO Auto-generated method stub
        super.onResume(multitasking);
        // Also check if not already initialising.
        if (shouldExecuteOnResume) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    if (AppMobiCloudController.sharedController != null
                            && AppMobiCloudController.sharedController.appName != null)
                        AppMobiCloudController.sharedController
                                .enterForeground();
                }
            });
        } else {
            shouldExecuteOnResume = true;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
      /*  if(AppMobiCloudPushListener.sharedListener!=null){
        AppMobiCloudPushListener.sharedListener.onDestroy();
       }*/
        super.onDestroy();
        webView = null;
    }

    @Override
    public void onPause(boolean multitasking) {
        // TODO Auto-generated method stub
        super.onPause(multitasking);
        if (AppMobiCloudController.sharedController != null
                && AppMobiCloudController.sharedController.configData != null)
            AppMobiCloudController.sharedController.enterBackground();

    }


    public void onRequestPermissionResult(int requestCode,
                                          String[] permissions, int[] grantResults) throws JSONException {
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                HashMap<String, Object> properties = new HashMap<String, Object>();
                properties.put("success", false);
                properties.put("message", "Error occured in plugin initialization");
                JSONObject obj = new JSONObject(properties);
                PluginResult result = new PluginResult(PluginResult.Status.ERROR, obj);
                result.setKeepCallback(true);
                callback.sendPluginResult(result);

                return;
            }
        }
        switch (requestCode) {
            case READ_PHONE_STATE_REQ_CODE:
                cordova.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        performInitialise();
                    }
                });
                break;

        }

    }

    private void performInitialise() {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                AppMobiCloudController.sharedController.initializeApp(callback,
                        userName, password);
            }
        });

    }

    protected void getPermission(int requestCode, String permission) {
        PermissionHelper.requestPermission(this, requestCode, permission);
    }


    @Override
    public Boolean shouldAllowRequest(final String url) {
        Log.v("message","shouldAllowRequest-"+url);
        return super.shouldAllowRequest(url);
    }

    @Override
    public Boolean shouldAllowNavigation(final String url) {
        Log.v("message","shouldAllowNavigation-"+url);
        return super.shouldAllowNavigation(url);
    }

    @Override
    public Boolean shouldOpenExternalUrl(String url) {
        return super.shouldOpenExternalUrl(url);
    }
}
