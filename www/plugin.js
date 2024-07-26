cordova.define("cordova-image-compressor.plugin", function(require, exports, module) {

  var exec = require('cordova/exec');

  var PLUGIN_NAME = 'ImageCompressor';

  var ImageCompressor = {
    compress: function(options, successCallback, errorCallback) {
      exec(successCallback, errorCallback, PLUGIN_NAME, 'compress', [options]);
    },
    getFileFromContentUri: function(options, successCallback, errorCallback) {
      exec(successCallback, errorCallback, PLUGIN_NAME, 'getFileFromContentUri', [options]);
    },
    compressToTempFile: function(options, successCallback, errorCallback) {
      exec(successCallback, errorCallback, PLUGIN_NAME, 'compressToTempFile', [options]);
    }
  };

  module.exports = ImageCompressor;

});