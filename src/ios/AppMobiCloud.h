//
//  PushPlugin.h
//  PushPlugin
//
//  Created by iOS on 17/11/14.
//
//

#import <Cordova/CDVPlugin.h>

@interface AppMobiCloud : CDVPlugin

-(void) initialize:(CDVInvokedUrlCommand*)command;
-(void) checkPushUser:(CDVInvokedUrlCommand*)command;
-(void) addPushUser:(CDVInvokedUrlCommand*)command;
-(void) readPushNotifications:(CDVInvokedUrlCommand*)command;
-(void) findPushUser:(CDVInvokedUrlCommand*)command;
-(void) editPushUser:(CDVInvokedUrlCommand*)command;
-(void) refreshPushNotifications:(CDVInvokedUrlCommand*)command;
-(void) refreshUserPushNotifications:(CDVInvokedUrlCommand*)command;
-(void) sendPushNotification:(CDVInvokedUrlCommand*)command;
-(void) broadcastPushNotification:(CDVInvokedUrlCommand*)command;
-(void) sendPushUserPass:(CDVInvokedUrlCommand*)command;
-(void) deletePushUser:(CDVInvokedUrlCommand*)command;
-(void) runPromotion:(CDVInvokedUrlCommand*)command;
-(void) installUpdate:(CDVInvokedUrlCommand*)command;
-(void) saveSecureData:(CDVInvokedUrlCommand*)command;
-(void) readSecureData:(CDVInvokedUrlCommand*)command;
-(void) syncSecureData:(CDVInvokedUrlCommand*)command;
-(void) registerOAuth:(CDVInvokedUrlCommand*)command;

@end
