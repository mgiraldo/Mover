/**
 * 
 */
package org.nypl.mover;

import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.api.PluginResult.Status;;

/**
 * @author mga
 *
 */
public class Zipper extends Plugin {
	/** List Action */
	public static final String LISTACTION="list";
	public static final String DECOMPRESSACTION="decompress";

	/* (non-Javadoc)
	 * @see com.phonegap.api.Plugin#execute(java.lang.String, org.json.JSONArray, java.lang.String)
	 */
	@Override
	public PluginResult execute(String action, JSONArray data, String callbackId) {
		Log.d("DirectoryListPlugin", "Plugin Called");
		PluginResult result = null;
		if (LISTACTION.equals(action)) {
			try {
				String fileName = data.getString(0);
				JSONObject fileInfo = getDirectoryListing(new File(fileName));
				Log.d("ZipperPlugin", "Returning "+ fileInfo.toString());
				result = new PluginResult(Status.OK, fileInfo);
			} catch (JSONException jsonEx) {
				Log.d("ZipperPlugin", "Got JSON Exception "+ jsonEx.getMessage());
				result = new PluginResult(Status.JSON_EXCEPTION);
			}
		} else if (DECOMPRESSACTION.equals(action)) {
			try {
				String fileName = data.getString(0);
				JSONObject fileInfo = decompress(new File(fileName));
				Log.d("ZipperPlugin", "Returning "+ fileInfo.toString());
				result = new PluginResult(Status.OK, fileInfo);
			} catch (JSONException jsonEx) {
				Log.d("ZipperPlugin", "Got JSON Exception "+ jsonEx.getMessage());
				result = new PluginResult(Status.JSON_EXCEPTION);
			}
		} else {
			result = new PluginResult(Status.INVALID_ACTION);
			Log.d("ZipperPlugin", "Invalid action : "+action+" passed");
		}
		return result;
	}
	
	/**
	* Gets the Directory listing for file, in JSON format
	* @param file The file for which we want to do directory listing
	* @return JSONObject representation of directory list. 
	*  e.g {"filename":"/sdcard","isdir":true,"children":[{"filename":"a.txt","isdir":false},{..}]}
	* @throws JSONException
	*/
	private JSONObject getDirectoryListing(File file) throws JSONException {
		JSONObject fileInfo = new JSONObject();
		fileInfo.put("filename", file.getName());
		fileInfo.put("isdir", file.isDirectory());
		if (file.isDirectory()) {
			JSONArray children = new JSONArray();
			fileInfo.put("children", children);
			if (null != file.listFiles()) {
				for (File child : file.listFiles()) {
					children.put(getDirectoryListing(child));
				}
			}
		}
		return fileInfo;
	}

	/**
	* Gets the contents of a compressed file, in JSON format
	* Based on sample found in
	* @see http://www.jondev.net/articles/Unzipping_Files_with_Android_(Programmatically)
	* @param file The file which we want to uncompress
	* @return JSONObject representation of contents list. 
	*  e.g {"filename":"/sdcard/myBook.epub","contents":[{"filename":"a.txt","isdir":false},{..}]}
	* @throws JSONException
	*/
	private JSONObject decompress(File file) throws JSONException {
		JSONObject fileInfo = new JSONObject();
		try  {
			// base structure
			fileInfo.put("filename", file.getName());
			fileInfo.put("isdir", file.isDirectory());
			JSONArray contents = new JSONArray();
			fileInfo.put("contents", contents);
			// compression stuff
			FileInputStream fin = new FileInputStream(file); 
			ZipInputStream zin = new ZipInputStream(fin); 
			ZipEntry ze = null;
			JSONObject tmp;
			while ((ze = zin.getNextEntry()) != null) { 
				Log.v("Zipper", "Unzipping " + ze.getName()); 
				tmp = new JSONObject();
				tmp.put("isdir", ze.isDirectory());
				tmp.put("filename", ze.getName());
				contents.put(tmp);
				zin.closeEntry();
				/**
				if(ze.isDirectory()) { 
					_dirChecker(ze.getName()); 
				} else { 
					FileOutputStream fout = new FileOutputStream(_location + ze.getName()); 
					for (int c = zin.read(); c != -1; c = zin.read()) { 
						fout.write(c); 
					} 

					zin.closeEntry(); 
					fout.close(); 
				}
				/**/

			} 
			zin.close();
		} catch(Exception e) {
			Log.e("Decompress", "unzip", e); 
		} 
		return fileInfo;
	}

}
