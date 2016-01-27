package com.appMobiCloud;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class AppMobiCloud extends CordovaPlugin {
	public static AppMobiCloud plugin;
	private static CordovaInterface cordova;
	private static CordovaWebView webView;
	private static String userID, pass, email;
	private static String notificationID;
	public CallbackContext callback;
	boolean shouldExecuteOnResume;

	@Override
	public void initialize(CordovaInterface _cordova, CordovaWebView _webView) {
		super.initialize(cordova, webView);
		cordova = _cordova;
		webView = _webView;

	}

	// ************************************************* //
	// Initialize Plugin
	// ************************************************* //

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

			final String userName= args.getString(0);
			final String password= args.getString(1);
			cordova.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					AppMobiCloudController.sharedController.initializeApp(
							callbackContext,userName,password);
				}
			});
		}		
		if (action.equalsIgnoreCase("checkPushUser")) {
			userID = args.getString(0);
			pass = args.getString(1);
			cordova.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					if(AppMobiCloudController.sharedController.cloudPush!=null){
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
					if(AppMobiCloudController.sharedController.cloudPush!=null)
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
					if(AppMobiCloudController.sharedController.cloudPush!=null)
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
					if(AppMobiCloudController.sharedController.cloudPush!=null)
					AppMobiCloudController.sharedController.cloudPush
							.editPushUser(email, password, "");
				}
			});

		}

		if (action.equalsIgnoreCase("deletePushUser")) {
			cordova.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					if(AppMobiCloudController.sharedController.cloudPush!=null)
					AppMobiCloudController.sharedController.cloudPush
							.deletePushUser();
				}
			});

		}
		if (action.equalsIgnoreCase("refreshPushNotifications")) {
			cordova.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					if(AppMobiCloudController.sharedController.cloudPush!=null)
					AppMobiCloudController.sharedController.cloudPush
							.refreshPushNotifications();
				}
			});

		}

		if (action.equalsIgnoreCase("sendPushUserPass")) {
			cordova.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					if(AppMobiCloudController.sharedController.cloudPush!=null)
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
					if(AppMobiCloudController.sharedController.cloudPush!=null)
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
					if(AppMobiCloudController.sharedController.cloudPush!=null)
					AppMobiCloudController.sharedController.cloudPush
							.findPushUser(userID, emailID);
				}
			});

		}

		if (action.equalsIgnoreCase("setPushUserAttributes")) {

			final String attributes = args.getString(0);
			Log.v("[appMobi]", attributes);
			cordova.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					if(AppMobiCloudController.sharedController.cloudPush!=null)
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
					if(AppMobiCloudController.sharedController.cloudPush!=null)
					AppMobiCloudController.sharedController.cloudPush.alert(
							msg, title, btn);
				}
			});

		}

		// ************************************************* //
		// appAds
		// ************************************************* //

		if (action.equalsIgnoreCase("runPromotion")) {
			final String appname = args.getString(0);
			final String storeurl = args.getString(1);
			final String promotion = args.getString(2);
			final String protocol = args.getString(3);
			final String packageStr = args.getString(4);
			final String query = args.getString(5);
			cordova.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					if(AppMobiCloudController.sharedController.cloudAdvertising!=null)
					AppMobiCloudController.sharedController.cloudAdvertising
							.runPromotion(appname, storeurl, promotion,
									protocol, packageStr, query);
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
					AppMobiCloudController.sharedController.registerOAuth(token);
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
					if(AppMobiCloudController.sharedController.cloudSecure!=null)
					AppMobiCloudController.sharedController.cloudSecure
							.saveSecureData(key, value, isMasterData,
									isSyncRequired,isJSON);
				}
			});

		}
	
		if (action.equalsIgnoreCase("syncSecureData")) {	
			cordova.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					if(AppMobiCloudController.sharedController.cloudSecure!=null)
					AppMobiCloudController.sharedController.cloudSecure.sync();
				}
			});

		}
		
		if (action.equalsIgnoreCase("readSecureData")) {		
			final String key = args.getString(0);
			final String isMasterData = args.getString(1);
			cordova.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					if(AppMobiCloudController.sharedController.cloudSecure!=null)
					AppMobiCloudController.sharedController.cloudSecure.readSecureData(key,isMasterData);
				}
			});

		}
		

		
		return true;
	}

	@Override
	public void onResume(boolean multitasking) {
		// TODO Auto-generated method stub
		super.onResume(multitasking);
		if (shouldExecuteOnResume) {
			cordova.getThreadPool().execute(new Runnable() {
				public void run() {
					if (AppMobiCloudController.sharedController != null&&AppMobiCloudController.sharedController.appName!=null)
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
		super.onDestroy();
		webView = null;
	}

	@Override
	public void onPause(boolean multitasking) {
		// TODO Auto-generated method stub
		super.onPause(multitasking);
		if (AppMobiCloudController.sharedController != null&&AppMobiCloudController.sharedController.configData != null )
			AppMobiCloudController.sharedController.enterBackground();

	}

	public static boolean isActive() {
		return webView != null;
	}
	
	

}
