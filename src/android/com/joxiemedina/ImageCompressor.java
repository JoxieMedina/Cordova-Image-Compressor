package com.joxiemedina;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;
import android.graphics.Bitmap;
import android.net.Uri;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImageCompressor extends CordovaPlugin {
  private static final String TAG = "ImageCompressor";
  private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    Log.d(TAG, "Initializing ImageCompressor Plugin");
  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (action.equals("compress")) {
      JSONObject jsonObject = args.getJSONObject(0);
      String originalFileUri = jsonObject.getString("uri");
      String compressedFileName = jsonObject.getString("fileName");
      int quality = jsonObject.getInt("quality");
      int width = jsonObject.optInt("width", -1); // Use -1 as a flag to indicate that width is not set
      int height = jsonObject.optInt("height", -1); // Use -1 as a flag to indicate that height is not set

      return compressToFile(callbackContext, originalFileUri, compressedFileName, width, height, this.compressFormat, quality);
    } else if (action.equals("getFileFromContentUri")) {
      JSONObject jsonObject = args.getJSONObject(0);
      String contentUri = jsonObject.getString("uri");
      String fileName = jsonObject.getString("fileName");

      return getFileFromContentUri(callbackContext, contentUri, fileName);
    } else if (action.equals("compressToTempFile")) {
      JSONObject jsonObject = args.getJSONObject(0);
      String originalFileUri = jsonObject.getString("uri");
      int quality = jsonObject.getInt("quality");
      int width = jsonObject.optInt("width", -1); // Use -1 as a flag to indicate that width is not set
      int height = jsonObject.optInt("height", -1); // Use -1 as a flag to indicate that height is not set

      return compressToTempFile(callbackContext, originalFileUri, width, height, this.compressFormat, quality);
    } else {
      return false;
    }
  }

  public boolean compressToFile(CallbackContext callbackContext, String originalFileUri, String compressedFileName, int width, int height, Bitmap.CompressFormat compressFormat, int quality) {
    boolean returnval = false;
    PluginResult result = null;
    try {
      Bitmap bitmap = ImageUtil.getBitmapFromUri(this.cordova.getContext(), originalFileUri);

      // If width and height are not specified, use the original dimensions
      if (width == -1 || height == -1) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();
      }

      Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

      ContentValues values = new ContentValues();
      values.put(MediaStore.Images.Media.DISPLAY_NAME, compressedFileName);
      values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
      values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CompressedImages");

      ContentResolver resolver = this.cordova.getContext().getContentResolver();
      Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

      if (uri != null) {
        try (OutputStream out = resolver.openOutputStream(uri)) {
          if (resizedBitmap.compress(compressFormat, quality, out)) {
            result = new PluginResult(PluginResult.Status.OK, uri.toString());
            returnval = true;
          } else {
            result = new PluginResult(PluginResult.Status.ERROR, "Failed to compress image");
          }
        }
      } else {
        result = new PluginResult(PluginResult.Status.ERROR, "Failed to create new MediaStore entry");
      }
    } catch (IOException e) {
      result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
    }
    callbackContext.sendPluginResult(result);
    return returnval;
  }

  public boolean compressToTempFile(CallbackContext callbackContext, String originalFileUri, int width, int height, Bitmap.CompressFormat compressFormat, int quality) {
    boolean returnval = false;
    PluginResult result = null;
    try {
      Bitmap bitmap = ImageUtil.getBitmapFromUri(this.cordova.getContext(), originalFileUri);

      // If width and height are not specified, use the original dimensions
      if (width == -1 || height == -1) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();
      }

      Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

      File tempFile = File.createTempFile("compressed_", ".jpg", this.cordova.getContext().getCacheDir());

      try (OutputStream out = new FileOutputStream(tempFile)) {
        if (resizedBitmap.compress(compressFormat, quality, out)) {
          result = new PluginResult(PluginResult.Status.OK, tempFile.getAbsolutePath());
          returnval = true;
        } else {
          result = new PluginResult(PluginResult.Status.ERROR, "Failed to compress image");
        }
      }
    } catch (IOException e) {
      result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
    }
    callbackContext.sendPluginResult(result);
    return returnval;
  }

  private boolean getFileFromContentUri(CallbackContext callbackContext, String contentUri, String fileName) {
    try {
      Uri uri = Uri.parse(contentUri);
      File file = UriToFileUtil.getFileFromContentUri(this.cordova.getContext(), uri, fileName);
      PluginResult result = new PluginResult(PluginResult.Status.OK, file.getAbsolutePath());
      callbackContext.sendPluginResult(result);
      return true;
    } catch (IOException e) {
      PluginResult result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
      callbackContext.sendPluginResult(result);
      return false;
    }
  }
}
