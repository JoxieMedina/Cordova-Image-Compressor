#import <Cordova/CDV.h>

@interface CDVImageCompressor : CDVPlugin

- (void)compress:(CDVInvokedUrlCommand*)command;
- (void)getFileFromContentUri:(CDVInvokedUrlCommand*)command;
- (void)compressToTempFile:(CDVInvokedUrlCommand*)command;

@end