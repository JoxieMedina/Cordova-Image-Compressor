# Cordova Image Compressor Plugin

This Cordova plugin compresses images and supports transforming content URIs to file paths.

## Installation

To install this plugin, you need to add it to your Cordova project:

```bash
cordova plugin add path/to/cordova-image-compressor
```

## Usage

### Compress an Image

To compress an image, use the `compress` method. You need to provide options such as the URI of the original image, the file name for the compressed image, and the desired quality.

```javascript
var options = {
  uri: "file:///storage/emulated/0/DCIM/Camera/sample.jpg",
  fileName: "compressed_sample.jpg",
  quality: 80,
  width: 800,  // Optional
  height: 600  // Optional
};

ImageCompressor.compress(options, function(successMessage) {
  console.log("Compression successful: " + successMessage);
}, function(errorMessage) {
  console.error("Compression failed: " + errorMessage);
});
```

### Compress an Image to a Temporary File

To compress an image to a temporary file, use the `compressToTempFile` method. You need to provide options such as the URI of the original image and the desired quality.

```javascript
var options = {
  uri: "file:///storage/emulated/0/DCIM/Camera/sample.jpg",
  quality: 80,
  width: 800,  // Optional
  height: 600  // Optional
};

ImageCompressor.compressToTempFile(options, function(successMessage) {
  console.log("Temporary compression successful: " + successMessage);
  var tempFilePath = successMessage;
  // Use the temporary file path as needed
}, function(errorMessage) {
  console.error("Temporary compression failed: " + errorMessage);
});
```

### Convert Content URI to File Path

To convert a content URI to a file path, use the `getFileFromContentUri` method. You need to provide the content URI and the desired file name.

```javascript
var contentUri = "content://media/external/images/media/1000000045";
var fileName = "compressed_sample.jpg";

ImageCompressor.getFileFromContentUri(
  { uri: contentUri, fileName: fileName },
  function(filePath) {
    console.log("File path: " + filePath);
    // Use the file path as needed
  },
  function(errorMessage) {
    console.error("Failed to get file path: " + errorMessage);
  }
);
```

## Methods

### `compress(options, successCallback, errorCallback)`

Compresses an image.

- `options`: An object containing the following properties:
  - `uri`: The URI of the original image.
  - `fileName`: The file name for the compressed image.
  - `quality`: The quality of the compressed image (1-100).
  - `width`: (Optional) The width of the compressed image.
  - `height`: (Optional) The height of the compressed image.
- `successCallback`: A function to be called when the compression is successful.
- `errorCallback`: A function to be called when there is an error.

### `compressToTempFile(options, successCallback, errorCallback)`

Compresses an image to a temporary file.

- `options`: An object containing the following properties:
  - `uri`: The URI of the original image.
  - `quality`: The quality of the compressed image (1-100).
  - `width`: (Optional) The width of the compressed image.
  - `height`: (Optional) The height of the compressed image.
- `successCallback`: A function to be called when the compression is successful.
- `errorCallback`: A function to be called when there is an error.

### `getFileFromContentUri(options, successCallback, errorCallback)`

Converts a content URI to a file path.

- `options`: An object containing the following properties:
  - `uri`: The content URI of the image.
  - `fileName`: The file name for the converted file.
- `successCallback`: A function to be called when the conversion is successful.
- `errorCallback`: A function to be called when there is an error.

## Example

```javascript
var options = {
  uri: "file:///storage/emulated/0/DCIM/Camera/sample.jpg",
  fileName: "compressed_sample.jpg",
  quality: 80,
  width: 800,  // Optional
  height: 600  // Optional
};

ImageCompressor.compress(options, function(successMessage) {
  console.log("Compression successful: " + successMessage);

  // Convert content URI to file path
  var contentUri = successMessage; // Assuming successMessage returns the content URI
  var fileName = "compressed_sample.jpg";

  ImageCompressor.getFileFromContentUri(
    { uri: contentUri, fileName: fileName },
    function(filePath) {
      console.log("File path: " + filePath);
      // Use the file path as needed
    },
    function(errorMessage) {
      console.error("Failed to get file path: " + errorMessage);
    }
  );

}, function(errorMessage) {
  console.error("Compression failed: " + errorMessage);
});
```

## License

This project is licensed under the MIT License.