#import "CDVImageCompressor.h"
#import <Cordova/CDV.h>
#import <UIKit/UIKit.h>

@implementation CDVImageCompressor

- (void)pluginInitialize {
    [super pluginInitialize];
    NSLog(@"CDVImageCompressor plugin initialized");
}

- (void)compress:(CDVInvokedUrlCommand*)command {
    [self.commandDelegate runInBackground:^{
        NSDictionary* options = [command argumentAtIndex:0];
        NSString* sourceUri = options[@"uri"];
        NSString* fileName = options[@"fileName"];
        NSNumber* quality = options[@"quality"];
        NSNumber* width = options[@"width"];
        NSNumber* height = options[@"height"];
        
        NSURL* url = [NSURL URLWithString:sourceUri];
        UIImage* image = [UIImage imageWithData:[NSData dataWithContentsOfURL:url]];
        
        if (width != nil && height != nil) {
            CGSize newSize = CGSizeMake([width floatValue], [height floatValue]);
            UIGraphicsBeginImageContextWithOptions(newSize, NO, 1.0);
            [image drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
            image = UIGraphicsGetImageFromCurrentImageContext();
            UIGraphicsEndImageContext();
        }
        
        NSData* imageData = UIImageJPEGRepresentation(image, [quality floatValue] / 100.0);
        
        NSString* documentsDirectory = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) firstObject];
        NSString* filePath = [documentsDirectory stringByAppendingPathComponent:fileName];
        
        if ([imageData writeToFile:filePath atomically:YES]) {
            CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:filePath];
            [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        } else {
            CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Failed to save compressed image"];
            [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        }
    }];
}

- (void)getFileFromContentUri:(CDVInvokedUrlCommand*)command {
    [self.commandDelegate runInBackground:^{
        NSDictionary* options = [command argumentAtIndex:0];
        NSString* contentUri = options[@"uri"];
        NSString* fileName = options[@"fileName"];
        
        NSURL* url = [NSURL URLWithString:contentUri];
        NSData* imageData = [NSData dataWithContentsOfURL:url];
        
        NSString* documentsDirectory = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) firstObject];
        NSString* filePath = [documentsDirectory stringByAppendingPathComponent:fileName];
        
        if ([imageData writeToFile:filePath atomically:YES]) {
            CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:filePath];
            [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        } else {
            CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Failed to save file"];
            [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        }
    }];
}

- (void)compressToTempFile:(CDVInvokedUrlCommand*)command {
    [self.commandDelegate runInBackground:^{
        NSDictionary* options = [command argumentAtIndex:0];
        NSString* sourceUri = options[@"uri"];
        NSNumber* quality = options[@"quality"];
        NSNumber* width = options[@"width"];
        NSNumber* height = options[@"height"];
        
        NSURL* url = [NSURL URLWithString:sourceUri];
        UIImage* image = [UIImage imageWithData:[NSData dataWithContentsOfURL:url]];
        
        if (width != nil && height != nil) {
            CGSize newSize = CGSizeMake([width floatValue], [height floatValue]);
            UIGraphicsBeginImageContextWithOptions(newSize, NO, 1.0);
            [image drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
            image = UIGraphicsGetImageFromCurrentImageContext();
            UIGraphicsEndImageContext();
        }
        
        NSData* imageData = UIImageJPEGRepresentation(image, [quality floatValue] / 100.0);
        
        NSString* tempDirectory = NSTemporaryDirectory();
        NSString* fileName = [NSString stringWithFormat:@"compressed_%@.jpg", [[NSUUID UUID] UUIDString]];
        NSString* filePath = [tempDirectory stringByAppendingPathComponent:fileName];
        
        if ([imageData writeToFile:filePath atomically:YES]) {
            CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:filePath];
            [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        } else {
            CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Failed to save compressed image"];
            [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        }
    }];
}

@end