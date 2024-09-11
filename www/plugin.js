  var exec = require('cordova/exec');

  var ImageCompressor = {
    compress: function(options, success, error) {
      exec(success, error, "ImageCompressor", "compress", [options]);
    },
    compressToTempFile: function(options, success, error) {
      exec(success, error, "ImageCompressor", "compressToTempFile", [options]);
    },
    getFileFromContentUri: function(options, success, error) {
      exec(success, error, "ImageCompressor", "getFileFromContentUri", [options]);
    }
  };

  module.exports = ImageCompressor;