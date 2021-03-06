package gt2.utils;

import gt2.listeners.DialogButtonOnClickListener;
import gt2.listeners.DialogButtonOnTouchListener;

import gt2.main.AlarmDialog;
import gt2.main.GT2Activity;
import gt2.main.R;
import gt2.main.TimerService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class Methods {

	static int tempRecordNum = 20;
	
	public static enum DialogTags {
		// Generics
		dlg_generic_dismiss, dlg_generic_dismiss_second_dialog,
		
		
	}//public static enum DialogTags
	
	public static enum ButtonTags {
		// main.xml
		main_bt_start, 

	}//public static enum ButtonTags
	
	public static enum ItemTags {
		
		
	}//public static enum ItemTags

	public static enum MoveMode {
		// TIListAdapter.java
		ON, OFF,
		
	}//public static enum MoveMode

	public static enum ButtonModes {
		// Play
		READY, FREEZE, PLAY,
		// Rec
		REC,
	}

	public static enum LongClickTags {
		// MainActivity.java
		main_activity_list,
		
		
	}//public static enum LongClickTags

	public static enum DialogOnItemClickTags {
		
		// dlg_item_menu.xml
		dlg_item_menu,
		
		// dlg_register_patterns.xml
		dlg_register_patterns, dlg_register_patterns_gv,
		
	}//public static enum DialogOnItemClickListener
	
	//
	public static final int vibLength_click = 35;

	/*----------------------------
	 * makeComparator(Comparator comparator)
	 * 
	 * => Used 
	 * 
	 * REF=> C:\WORKS\WORKSPACES_ANDROID\Sample\09_Matsuoka\プロジェクト
	 * 					\Step10\10_LiveWallpaper\src\sample\step10\livewallpaper\FilePicker.java
		----------------------------*/
	public static void makeComparator(Comparator comparator){
		
		comparator=new Comparator<Object>(){
			
			public int compare(Object object1, Object object2) {
			
				int pad1=0;
				int pad2=0;
				
				File file1=(File)object1;
				File file2=(File)object2;
				
				if(file1.isDirectory())pad1=-65536;
				if(file2.isDirectory())pad2=-65536;
				
				return pad1-pad2+file1.getName().compareToIgnoreCase(file2.getName());
			}
		};
	}

	public static void sortFileList(File[] files) {
		// REF=> http://android-coding.blogspot.jp/2011/10/sort-file-list-in-order-by-implementing.html
		Comparator<? super File> filecomparator = new Comparator<File>(){
			
			public int compare(File file1, File file2) {
				int pad1=0;
				int pad2=0;
				
				if(file1.isDirectory())pad1=-65536;
				if(file2.isDirectory())pad2=-65536;
				
				// Order => folders, files
//				return pad1-pad2+file1.getName().compareToIgnoreCase(file2.getName());
				
				// Order => files, folders
				return pad2-pad1+file1.getName().compareToIgnoreCase(file2.getName());
				
//				return String.valueOf(file1.getName()).compareTo(file2.getName());
			} 
		 };//Comparator<? super File> filecomparator = new Comparator<File>()
		 
		 //
		Arrays.sort(files, filecomparator);

	}//public static void sortFileList(File[] files)

	/*----------------------------
	 * deleteDirectory(File target)()
	 * 
	 * 1. REF=> http://www.rgagnon.com/javadetails/java-0483.html
		----------------------------*/
	public static boolean deleteDirectory(File target) {
		
		if(target.exists()) {
			//
			File[] files = target.listFiles();
			
			//
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					
					deleteDirectory(files[i]);
					
				} else {//if (files[i].isDirectory())
					
					String path = files[i].getAbsolutePath();
					
					files[i].delete();
					
					// Log
					Log.d("Methods.java"
							+ "["
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "]", "Removed => " + path);
					
					
				}//if (files[i].isDirectory())
				
			}//for (int i = 0; i < files.length; i++)
			
		}//if(target.exists())
		
		return (target.delete());
	}

	public static void toastAndLog(Activity actv, String message, int duration) {
		// 
		// debug
		Toast.makeText(actv, message, duration)
				.show();
		
		// Log
		Log.d("ImageFileManager8Activity.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber() + "]", 
				message);
		
	}//public static void toastAndLog(Activity actv, String message)

	public static void toastAndLog(Activity actv, String fileName, String message, int duration) {
		// 
		// debug
		Toast.makeText(actv, message, duration)
				.show();
		
		// Log
		Log.d(fileName + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber() + "]", 
				message);
		
	}//public static void toastAndLog(Activity actv, String fileName, String message, int duration)

	public static void recordLog(Activity actv, String fileName, String message) {
		// Log
		Log.d(fileName + 
				"[" + Thread.currentThread().getStackTrace()[2].getLineNumber() + "]", 
				message);
		
	}//public static void toastAndLog(Activity actv, String message)

	public static long getMillSeconds(int year, int month, int date) {
		// Calendar
		Calendar cal = Calendar.getInstance();
		
		// Set time
		cal.set(year, month, date);
		
		// Date
		Date d = cal.getTime();
		
		return d.getTime();
		
	}//private long getMillSeconds(int year, int month, int date)

	public static List<String> getTableList(SQLiteDatabase wdb) {
		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT name FROM " + "sqlite_master"+
						" WHERE type = 'table' ORDER BY name";
		
		Cursor c = null;
		try {
			c = wdb.rawQuery(q, null);
		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
		}
		
		// Table names list
		List<String> tableList = new ArrayList<String>();
		
		// Log
		if (c != null) {
			c.moveToFirst();
			
			for (int i = 0; i < c.getCount(); i++) {
				// Log
				Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "c.getString(0) => " + c.getString(0));
				
				//
				tableList.add(c.getString(0));
				
				// Next
				c.moveToNext();
				
			}//for (int i = 0; i < c.getCount(); i++)

		} else {//if (c != null)
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c => null");
		}//if (c != null)
		
		return tableList;
	}//public static List<String> getTableList()

	public static Dialog dlg_template_okCancel(Activity actv, int layoutId, int titleStringId,
									int okButtonId, int cancelButtonId, DialogTags okTag, DialogTags cancelTag) {
		/*----------------------------
		 * Steps
		 * 1. Set up
		 * 2. Add listeners => OnTouch
		 * 3. Add listeners => OnClick
			----------------------------*/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(layoutId);
		
		// Title
		dlg.setTitle(titleStringId);
		
		/*----------------------------
		 * 2. Add listeners => OnTouch
			----------------------------*/
		//
		Button btn_ok = (Button) dlg.findViewById(okButtonId);
		Button btn_cancel = (Button) dlg.findViewById(cancelButtonId);
		
		//
		btn_ok.setTag(okTag);
		btn_cancel.setTag(cancelTag);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/*----------------------------
		 * 3. Add listeners => OnClick
			----------------------------*/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		//
//		dlg.show();
		
		return dlg;
		
	}//public static Dialog dlg_template_okCancel()

	public static Dialog dlg_template_okCancel_2_dialogues(
											Activity actv, int layoutId, int titleStringId,
											int okButtonId, int cancelButtonId, 
											DialogTags okTag, DialogTags cancelTag,
											Dialog dlg1) {
		/*----------------------------
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		----------------------------*/
		
		// 
		Dialog dlg2 = new Dialog(actv);
		
		//
		dlg2.setContentView(layoutId);
		
		// Title
		dlg2.setTitle(titleStringId);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_ok = (Button) dlg2.findViewById(okButtonId);
		Button btn_cancel = (Button) dlg2.findViewById(cancelButtonId);
		
		//
		btn_ok.setTag(okTag);
		btn_cancel.setTag(cancelTag);
		
		//
		btn_ok.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg2));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_ok.setOnClickListener(new DialogButtonOnClickListener(actv, dlg1, dlg2));
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg1, dlg2));
		
		//
		//dlg.show();
		
		return dlg2;
		
	}//public static Dialog dlg_template_okCancel_2_dialogues()

	public static Dialog dlg_template_cancel(
										// Activity, layout
										Activity actv, int layoutId,
										// Title
										int titleStringId,
										// Cancel button, DialogTags => Cancel
										int cancelButtonId, DialogTags cancelTag) {
		/*----------------------------
		* Steps
		* 1. Set up
		* 2. Add listeners => OnTouch
		* 3. Add listeners => OnClick
		----------------------------*/
		
		// 
		Dialog dlg = new Dialog(actv);
		
		//
		dlg.setContentView(layoutId);
		
		// Title
		dlg.setTitle(titleStringId);
		
		/*----------------------------
		* 2. Add listeners => OnTouch
		----------------------------*/
		//
		Button btn_cancel = (Button) dlg.findViewById(cancelButtonId);
		
		//
		btn_cancel.setTag(cancelTag);
		
		//
		btn_cancel.setOnTouchListener(new DialogButtonOnTouchListener(actv, dlg));
		
		/*----------------------------
		* 3. Add listeners => OnClick
		----------------------------*/
		//
		btn_cancel.setOnClickListener(new DialogButtonOnClickListener(actv, dlg));
		
		//
		//dlg.show();
		
		return dlg;
		
	}//public static Dialog dlg_template_okCancel()

	/****************************************

	 * findIndexFromArray(String[] ary, String target)
	 * 
	 * <Caller> 
	 * 1. convertAbsolutePathIntoPath()
	 * 
	 * <Desc> 
	 * 1. 
	 * 
	 * <Params> 1.
	 * 
	 * <Return> 
	 * 		-1		=> Couldn't find
	 * 		
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static int findIndexFromArray(String[] ary, String target) {
		
		int index = -1;
		
		for (int i = 0; i < ary.length; i++) {
			
			if (ary[i].equals(target)) {
				
				return i;
				
			}//if (ary[i].equals(tar))
			
		}//for (int i = 0; i < ary.length; i++)
		
		return index;
		
	}//public static int findIndexFromArray(String[] ary, String target)

	public static String  convert_millSeconds2digitsLabel(long millSeconds) {
		/*----------------------------
		 * Steps
		 * 1. Prepare variables
		 * 2. Build a string
		 * 3. Return
			----------------------------*/
		/*----------------------------
		 * 1. Prepare variables
			----------------------------*/
		int seconds = (int)(millSeconds / 1000);
		
		int minutes = seconds / 60;
		
		int digit_seconds = seconds % 60;
		
		/*----------------------------
		 * 2. Build a string
			----------------------------*/
		StringBuilder sb = new StringBuilder();
		
		if (String.valueOf(minutes).length() < 2) {
			
			sb.append("0");
			
		}//if (String.valueOf(minutes).length() < 2)
		
		sb.append(String.valueOf(minutes));
		sb.append(":");

		if (String.valueOf(digit_seconds).length() < 2) {
			
			sb.append("0");
			
		}//if (String.valueOf(digit_seconds).length() < 2)

		sb.append(String.valueOf(digit_seconds));
		
		/*----------------------------
		 * 3. Return
			----------------------------*/
		return sb.toString();
		
	}//public static void  convert_millSeconds2digitsLabel()

	
	private static long getDuration(String file_path) {
		/*----------------------------
		 * 2. Duration
		 * 		1. temp_mp
		 * 		2. Set source
		 * 		2-1. Prepare
		 * 		2-2. Get duration
		 * 		2-3. Release temp_mp
		 * 
		 * 		3. Prepare	=> NOP
		 * 		4. Start			=> NOP
			----------------------------*/
		MediaPlayer temp_mp = new MediaPlayer();
		
		try {
			
			temp_mp.setDataSource(file_path);
			
		} catch (IllegalArgumentException e) {
			
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "Exception: " + e.toString());
			
			return -1;
			
		} catch (IllegalStateException e) {
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "Exception: " + e.toString());
			
			return -1;
			
		} catch (IOException e) {
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "Exception: " + e.toString());
			
			return -1;
			
		}//try
		
		/*----------------------------
		 * 2.2-1. Prepare
			----------------------------*/
		try {
			
			temp_mp.prepare();
			
		} catch (IllegalStateException e) {
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "Exception: " + e.toString());
			
			return -1;
			
		} catch (IOException e) {
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "Exception: " + e.toString());
			
			return -1;
			
		}//try

		/*----------------------------
		 * 2.2-2. Get duration
			----------------------------*/
		long duration = temp_mp.getDuration();
		
		/*----------------------------
		 * 2.2-3. Release temp_mp
			----------------------------*/
		temp_mp.reset();
		temp_mp.release();
		temp_mp = null;
		
		return duration;
		
	}//private static long getDuration(String file_path)

	public static boolean dropTable(Activity actv, String tableName) {
		DBUtils dbu = new DBUtils(actv, DBUtils.dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		boolean res = dbu.dropTable(actv, wdb, tableName);
		
		if (res == false) {

			Toast.makeText(actv, "Drop table => Failed", 2000)
			.show();
			
			wdb.close();
			
			return false;
			
		} else {//if (res == false)

			Toast.makeText(actv, "Table dropped: " + tableName, 2000)
			.show();
			
			wdb.close();
			
			return true;
			
		}//if (res == false)
		

	}//public static boolean dropTable(Activity actv, String tableName)

	private static boolean insertDataIntoDB(
			Activity actv, String tableName, String[] colNames, String[] values, String[] colTypes) {
		/*----------------------------
		* Steps
		* 1. Set up db
		* 1-2. Table exists?
		* 2. Insert data
		* 3. Show message
		* 4. Close db
		----------------------------*/
		/*----------------------------
		* 1. Set up db
		----------------------------*/
		DBUtils dbu = new DBUtils(actv, DBUtils.dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*----------------------------
		 * 1-2. Table exists?
			----------------------------*/
		boolean result = dbu.tableExists(wdb, tableName);
		
		// If doesn't exist...
		if (result == false) {
			// Create one
			result = dbu.createTable(wdb, tableName, colNames, colTypes);
			
			if (result == true) {
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + tableName);
				
				
			} else {//if (result == true)

				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create table => Failed: " + tableName);
				
				wdb.close();
				
				return false;
				
			}//if (result == true)
			
			
		}//if (result == false)
		
		/*----------------------------
		* 2. Insert data
		----------------------------*/
		result = false;
		
		try {
			
			result = dbu.insertData(wdb, tableName, colNames, values);
			
		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
		}
		
		/*----------------------------
		* 3. Show message
		----------------------------*/
		if (result == true) {
		
//			// debug
//			Toast.makeText(actv, "Data stored", 2000).show();
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "dbu.insertData => true");
			
			
			/*----------------------------
			* 4. Close db
			----------------------------*/
			wdb.close();
			
			return true;
		
		} else {//if (result == true)
		
//			// debug
//			Toast.makeText(actv, "Store data => Failed", 200).show();
			
			// Log
			Log.d("Methods.java"
					+ "["
					+ Thread.currentThread().getStackTrace()[2]
							.getLineNumber() + "]", "dbu.insertData => false");
			/*----------------------------
			* 4. Close db
			----------------------------*/
			wdb.close();
			
			return false;
		
		}//if (result == true)
		
		/*----------------------------
		* 4. Close db
		----------------------------*/
		
	}//private static int insertDataIntoDB()


	public static void test_postDataToRemote(Activity actv) {
		/*----------------------------
		 * 1. url
			----------------------------*/
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "test_postDataToRemote => Started");
		
		
		/*----------------------------
		 * 1. url
			----------------------------*/
//		String url = "http://cosmos-cm.herokuapp.com";
		String url = "http://cosmos-cm.herokuapp.com/test1_post";
		
		/*----------------------------
		 * 2. httpPost
			----------------------------*/
		HttpPost httpPost = new HttpPost(url);
		
		/*----------------------------
		 * 3. Parameters
			----------------------------*/
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		
//		paramList.add(new BasicNameValuePair("file_name", "1234.wav"));
		paramList.add(new BasicNameValuePair("test_data", "1234.wav"));
		
		try {
			
			httpPost.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
			
		} catch (UnsupportedEncodingException e) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
		}//try
		
		HttpUriRequest request = httpPost;
		
		/*----------------------------
		 * 4. Client object
			----------------------------*/
		DefaultHttpClient dhc = new DefaultHttpClient();
		
		/*----------------------------
		 * 5. Http response
			----------------------------*/
		HttpResponse hr = null;
		
		try {
			hr = dhc.execute(request);
		} catch (ClientProtocolException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		/*----------------------------
		 * 6. Response null?
			----------------------------*/
		if (hr == null) {
			
			// debug
			Toast.makeText(actv, "hr == null", 2000).show();
			
			return;
			
		}//if (hr == null)
		
		/*----------------------------
		 * 7. Status
			----------------------------*/
		int statusCode = hr.getStatusLine().getStatusCode();
		
		// debug
		Toast.makeText(actv, "statusCode: " + statusCode, 2000).show();
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "statusCode: " + statusCode);
		
		
//		// debug
//		Toast.makeText(actv, "test_postDataToRemote", 2000).show();
		
	}//public static void test_postDataToRemote(Activity actv)

	/****************************************
	 * 	test_postDataToRemote_json(Activity actv)
	 * 
	 * <Caller> 
	 * 1. ButtonOnClickListener
	 * 
	 * <Desc> 
	 * 1. Instead of setting an entity to an http client, create an HttpParams object,
	 * 			set a name-value pair to it, then parse it to a DefaultHttpClient object.
	 * 
	 * <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static void test_postDataToRemote_json(Activity actv) {
		/*----------------------------
		 * 1. url
		 * 2. httpPost
		 * 3. Parameters
		 * 4. Client object
		 * 5. Http response
		 * 6. Response null?
		 * 7. Status
			----------------------------*/
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "test_postDataToRemote => Started");
		
		
		/*----------------------------
		 * 1. url
			----------------------------*/
//		String url = "http://cosmos-cm.herokuapp.com";
		String url = "http://cosmos-cm.herokuapp.com/test2_post_json";
		
		/*----------------------------
		 * 2. httpPost
		 * 		1. Instantiate
		 * 		2. Set header
			----------------------------*/
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.setHeader("Content-type", "application/json");
		
		/*----------------------------
		 * 3. Parameters
			----------------------------*/
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		
		
		JSONObject jso = new JSONObject();
		JSONObject jso2 = new JSONObject();
		
		try {
			
//			jso.put("file_name", "4567.wav");
			
			jso.put("file_name", "8910.wav");
			jso.put("file_path", "/mnt/sdcard-extra/CM");
			
		} catch (JSONException e) {
			
			// debug
			Toast.makeText(actv, e.toString(), 2000).show();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", e.toString());
			
		}
		
		try {
			
			jso2.put("test_json", jso);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", jso2.toString());
			
			
		} catch (JSONException e) {
			// debug
			Toast.makeText(actv, "jso2: " + e.toString(), 2000).show();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "jso2: " + e.toString());
		}
		
		StringEntity stringEntity = null;
		
		try {
			
			stringEntity = new StringEntity(jso2.toString());
			
		} catch (UnsupportedEncodingException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		
//		paramList.add(new BasicNameValuePair("file_name", "1234.wav"));
//		paramList.add(new BasicNameValuePair("test_data", "1234.wav"));
//		paramList.add(new BasicNameValuePair("test_data", jso));
		
//		paramList.add(new BasicNameValuePair("test_data", "1234.wav"));
		
		
		
//		try {
//			
//			httpPost.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
//			
//		} catch (UnsupportedEncodingException e) {
//			
//			// Log
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Exception: " + e.toString());
//			
//		}//try

		if (stringEntity != null) {
			
			httpPost.setEntity(stringEntity);
			
		} else {//if (stringEntity != null)
			
			// debug
			Toast.makeText(actv, "stringEntity != null", 2000).show();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "stringEntity != null");
			
			return;
			
		}//if (stringEntity != null)
		
		HttpUriRequest postRequest = httpPost;
		
		/*----------------------------
		 * 4. Client object
			----------------------------*/
//		HttpParams hp = new BasicHttpParams();
//		
//		hp.setParameter("test_json", jso);
		
		
		DefaultHttpClient dhc = new DefaultHttpClient();
//		DefaultHttpClient dhc = new DefaultHttpClient(hp);
		
		/*----------------------------
		 * 5. Http response
			----------------------------*/
		HttpResponse hr = null;
		
		try {
			
			hr = dhc.execute(postRequest);
			
		} catch (ClientProtocolException e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", e.toString());
		} catch (IOException e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", e.toString());
		}
		
		/*----------------------------
		 * 6. Response null?
			----------------------------*/
		if (hr == null) {
			
			// debug
			Toast.makeText(actv, "hr == null", 2000).show();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "hr == null");
			
			return;
			
		}//if (hr == null)
		
		/*----------------------------
		 * 7. Status
			----------------------------*/
		int statusCode = hr.getStatusLine().getStatusCode();
		
		// debug
		Toast.makeText(actv, "statusCode: " + statusCode, 2000).show();
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "statusCode: " + statusCode);
	}//public static void test_postDataToRemote_json(Activity actv)

	/****************************************
	 * 	test_postDataToRemote_json_with_params(Activity actv, String param)
	 * 
	 * <Caller> 
	 * 1. SendMusicDataTask
	 * 
	 * <Desc> 
	 * 1. Instead of setting an entity to an http client, create an HttpParams object,
	 * 			set a name-value pair to it, then parse it to a DefaultHttpClient object.
	 * 
	 * <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static boolean test_postDataToRemote_json_with_params(Activity actv, String param) {
		/*----------------------------
		 * 1. url
		 * 2. httpPost
		 * 3. Parameters
		 * 4. Client object
		 * 5. Http response
		 * 6. Response null?
		 * 7. Status
		 * 
		 * 8. Return
			----------------------------*/
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "test_postDataToRemote => Started");
		
		
		/*----------------------------
		 * 1. url
			----------------------------*/
//		String url = "http://cosmos-cm.herokuapp.com";
		String url = "http://cosmos-cm.herokuapp.com/test2_post_json";
		
		/*----------------------------
		 * 2. httpPost
		 * 		1. Instantiate
		 * 		2. Set header
			----------------------------*/
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.setHeader("Content-type", "application/json");
		
		/*----------------------------
		 * 3. Parameters
			----------------------------*/
		JSONObject jso = new JSONObject();
		JSONObject jso2 = new JSONObject();
		
		try {
			
//			jso.put("file_name", "8910.wav");
			jso.put("file_name", param);
			jso.put("file_path", "/mnt/sdcard-extra/CM");
			
		} catch (JSONException e) {
			
			// debug
			Toast.makeText(actv, e.toString(), 2000).show();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", e.toString());
			
		}
		
		try {
			
			jso2.put("test_json", jso);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", jso2.toString());
			
			
		} catch (JSONException e) {
			// debug
			Toast.makeText(actv, "jso2: " + e.toString(), 2000).show();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "jso2: " + e.toString());
		}
		
		StringEntity stringEntity = null;
		
		try {
			
			stringEntity = new StringEntity(jso2.toString());
			
		} catch (UnsupportedEncodingException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		if (stringEntity != null) {
			
			httpPost.setEntity(stringEntity);
			
		} else {//if (stringEntity != null)
			
			// debug
			Toast.makeText(actv, "stringEntity != null", 2000).show();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "stringEntity != null");
			
			return false;
			
		}//if (stringEntity != null)
		
		HttpUriRequest postRequest = httpPost;
		
		/*----------------------------
		 * 4. Client object
			----------------------------*/
		DefaultHttpClient dhc = new DefaultHttpClient();
		
		/*----------------------------
		 * 5. Http response
			----------------------------*/
		HttpResponse hr = null;
		
		try {
			
			hr = dhc.execute(postRequest);
			
		} catch (ClientProtocolException e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", e.toString());
		} catch (IOException e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", e.toString());
		}
		
		/*----------------------------
		 * 6. Response null?
			----------------------------*/
		if (hr == null) {
			
			// debug
			Toast.makeText(actv, "hr == null", 2000).show();
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "hr == null");
			
			return false;
			
		}//if (hr == null)
		
		/*----------------------------
		 * 7. Status
			----------------------------*/
		int statusCode = hr.getStatusLine().getStatusCode();
		
		// debug
//		Toast.makeText(actv, "statusCode: " + statusCode, 2000).show();
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "statusCode: " + statusCode);
		
		/*----------------------------
		 * 8. Return
			----------------------------*/
		return true;
		
	}//public static boolean test_postDataToRemote_json_with_params(Activity actv)

	public static void showAlarm(Activity actv) {
			/*************************
			 * Steps
			 * 1. 
			 *************************/
		  // Stop the service
		  Intent i = new Intent(actv, TimerService.class);
		  
		  actv.stopService(i);
		  
		  // New intent
		  i = new Intent(actv, AlarmDialog.class);
		  
		  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		  
		  // Start
		  actv.startActivity(i);
		  
		}//  void showAlarm( )

	public static void countdown(Activity actv, int counter) {
		/*----------------------------
		 * 1. showTime()
			----------------------------*/
		Methods.showTime(actv, counter);
		
		GT2Activity.timeLeft = counter;
		
	}//public static void countdown(int counter)

	/****************************************
	 *	showTime(Activity actv, int counter)
	 * 
	 * <Caller> 
	 * 1. Methods.countdown(counter)
	 * 
	 * <Desc> 
	 * 1. "counter" is of seconds, so, you need to multiply the variable
	 * 			by 1000, getting the time in mill seconds
	 * 
	 * <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	private static void showTime(Activity actv, int counter) {
		/*----------------------------
		 * 1. 
			----------------------------*/
		// Format
		SimpleDateFormat form = new SimpleDateFormat("mm:ss");
		
		GT2Activity.tv_time.setText(form.format(counter * 1000));
		
	}//private static void showTime(int counter)

	public static List<String> getTableList(Activity actv) {
		DBUtils dbu = new DBUtils(actv, MainActv.dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT name FROM " + "sqlite_master"+
						" WHERE type = 'table' ORDER BY name";
		
		Cursor c = null;
		try {
			c = rdb.rawQuery(q, null);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount(): " + c.getCount());

		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
		}
		
		// Table names list
		List<String> tableList = new ArrayList<String>();
		
		// Log
		if (c != null) {
			c.moveToFirst();
			
			for (int i = 0; i < c.getCount(); i++) {
				//
				tableList.add(c.getString(0));
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "c.getString(0): " + c.getString(0));
				
				
				// Next
				c.moveToNext();
				
			}//for (int i = 0; i < c.getCount(); i++)

		} else {//if (c != null)
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c => null");
		}//if (c != null)

		rdb.close();
		
		return tableList;
		
	}//public static List<String> getTableList()

	public static String[] get_column_list(Activity actv, String dbName, String tableName) {
		/*********************************
		 * 1. Set up db
		 * 2. Cursor null?
		 * 3. Get names
		 * 
		 * 4. Close db
		 * 5. Return
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		//=> source: http://stackoverflow.com/questions/4681744/android-get-list-of-tables : "Just had to do the same. This seems to work:"
		String q = "SELECT name FROM " + tableName;
		
		/*********************************
		 * 2. Cursor null?
		 *********************************/
		Cursor c = null;
		
		try {
			c = rdb.rawQuery(q, null);
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "c.getCount(): " + c.getCount());

		} catch (Exception e) {
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			rdb.close();
			
			return null;
		}
		
		/*********************************
		 * 3. Get names
		 *********************************/
		String[] column_names = c.getColumnNames();
		
		/*********************************
		 * 4. Close db
		 *********************************/
		rdb.close();
		
		/*********************************
		 * 5. Return
		 *********************************/
		return column_names;
		
//		return null;
	}//public static String[] get_column_list(Activity actv, String tableName)

	public static void confirm_quit(Activity actv, int keyCode) {
		
		// TODO 自動生成されたメソッド・スタブ
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			
			AlertDialog.Builder dialog=new AlertDialog.Builder(actv);
			
	        dialog.setTitle("アプリの終了");
	        dialog.setMessage("アプリを終了しますか？");
	        
	        dialog.setPositiveButton("終了",new DialogListener(actv, dialog, 0));
	        dialog.setNegativeButton("キャンセル",new DialogListener(actv, dialog, 1));
	        
	        dialog.create();
	        dialog.show();
			
		}//if (keyCode==KeyEvent.KEYCODE_BACK)
		
	}//public static void confirm_quit(Activity actv, int keyCode)

	// REF=> http://www.coderanch.com/t/401142/java/java/check-if-String-value-numeric # Hurkpan Potgieter Greenhorn
	public static String join(String[] data, String separator) {
		
		StringBuilder sb = new StringBuilder();
		
		int i;
		
		for (i = 0; i < data.length - 1; i++) {
			
			sb.append(data[i] + separator);
		}
		
		sb.append(data[i]);
		
		return sb.toString();
	}

	
	private void restore_db(Activity actv, String dbName,
				String src, String dst) {
		/*********************************
		 * 1. Setup db
		 * 2. Setup: File paths
		 * 3. Setup: File objects
		 * 4. Copy file
		 * 
		 *********************************/
    	// Setup db
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		wdb.close();

		/*********************************
		 * 2. Setup: File paths
		 *********************************/
//    	String src = 
//    			"/mnt/sdcard-ext/ShoppingList_backup/shoppinglist_backup_20120906_201402.bk";
//    			"/mnt/sdcard-ext/CR4_backup/cr4_backup_20120907_184555.bk";

    	
//    	String dst =
////    			"/data/data/test.main/databases/shoppinglist.db";
//    			"/data/data/cr4.main/databases/cr4.db";

    	/*********************************
		 * 3. Setup: File objects
		 *********************************/
		File f_src = new File(src);
		File f_dst = new File(dst);

		/*********************************
		 * 4. Copy file
		 *********************************/
		try {
			FileChannel iChannel = new FileInputStream(src).getChannel();
			FileChannel oChannel = new FileOutputStream(dst).getChannel();
			iChannel.transferTo(0, iChannel.size(), oChannel);
			iChannel.close();
			oChannel.close();
			
			// Log
			Log.d("ThumbnailActivity.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "File copied");
			
			// debug
			Toast.makeText(actv, "DB restoration => Done", 3000).show();

		} catch (FileNotFoundException e) {
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
		} catch (IOException e) {
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
		}//try
		
	}//private void restore_db()

    private void drop_table(Activity actv, String dbName, String tableName) {
    	// Setup db
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		boolean res = 
				dbu.dropTable(actv, wdb, tableName);
		
		if (res == true) {
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table dropped: " + tableName);
		} else {//if (res == true)

			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Drop table => Failed: " + tableName);
			
		}//if (res == true)
		

		wdb.close();
		
		
	}//private void drop_table(String tableName)

	public static boolean is_numeric(String str) {
		
		// REF=> http://www.coderanch.com/t/401142/java/java/check-if-String-value-numeric # Hurkpan Potgieter Greenhorn
		String regex = "((-|\\+)?[0-9]+(\\.[0-9]+)?)+";
		
//		Pattern p = Pattern.compile( "([0-9]*)\\.[0]" );
		Pattern p = Pattern.compile(regex);

		Matcher m = p.matcher(str);
		
		return m.matches(); //TRUE
		
	}//public static boolean is_numeric(String str)

		public static boolean add_column_to_table(Activity actv, String dbName,
			String tableName, String column_name, String data_type) {
		/*********************************
		 * 1. Column already exists?
		 * 2. db setup
		 * 
		 * 3. Build sql
		 * 4. Exec sql
		 * 
		 * 5. Close db
		 *********************************/
		/*********************************
		 * 1. Column already exists?
		 *********************************/
		String[] cols = Methods.get_column_list(actv, dbName, tableName);
		
		//debug
		for (String col_name : cols) {

			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "col: " + col_name);
			
		}//for (String col_name : cols)

		
		for (String col_name : cols) {
			
			if (col_name.equals(column_name)) {
				
				// debug
				Toast.makeText(actv, "Column exists: " + column_name, Toast.LENGTH_SHORT).show();
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Column exists: " + column_name);
				
				return false;
				
			}
			
		}//for (String col_name : cols)
		
		// debug
		Toast.makeText(actv, "Column doesn't exist: " + column_name, Toast.LENGTH_SHORT).show();
		
		/*********************************
		 * 2. db setup
		 *********************************/
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		/*********************************
		 * 3. Build sql
		 *********************************/
		// REF[20121001_140817] => http://stackoverflow.com/questions/8291673/how-to-add-new-column-to-android-sqlite-database
		
		String sql = "ALTER TABLE " + tableName + 
					" ADD COLUMN " + column_name + 
					" " + data_type;
		
		/*********************************
		 * 4. Exec sql
		 *********************************/
		try {
//			db.execSQL(sql);
			wdb.execSQL(sql);
			
			// Log
			Log.d(actv.getClass().getName() + 
					"["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Column added => " + column_name);

			/*********************************
			 * 5. Close db
			 *********************************/
			wdb.close();
			
			return true;
			
		} catch (SQLException e) {
			// Log
			Log.d(actv.getClass().getName() + 
					"[" + Thread.currentThread().getStackTrace()[2].getLineNumber() + "]", 
					"Exception => " + e.toString());
			
			/*********************************
			 * 5. Close db
			 *********************************/
			wdb.close();

			return false;
		}//try

		/*********************************
		 * 5. Close db
		 *********************************/


		
	}//public static boolean add_column_to_table()

	public static void db_backup(Activity actv, Dialog dlg) {
		/*----------------------------
		 * 1. Prep => File names
		 * 2. Prep => Files
		 * 2-2. Folder exists?
		 * 3. Copy
			----------------------------*/
		String time_label = Methods.get_TimeLabel(Methods.getMillSeconds_now());
		
		String db_src = StringUtils.join(new String[]{MainActv.dirPath_db, MainActv.fileName_db}, File.separator);
		
		String db_dst = StringUtils.join(new String[]{MainActv.dirPath_db_backup, MainActv.fileName_db_backup_trunk}, File.separator);
		db_dst = db_dst + "_" + time_label + MainActv.fileName_db_backup_ext;
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "db_src: " + db_src + " * " + "db_dst: " + db_dst);
		
		/*----------------------------
		 * 2. Prep => Files
			----------------------------*/
		File src = new File(db_src);
		File dst = new File(db_dst);
		
		/*----------------------------
		 * 2-2. Folder exists?
			----------------------------*/
		File db_backup = new File(MainActv.dirPath_db_backup);
		
		if (!db_backup.exists()) {
			
			try {
				db_backup.mkdir();
				
				// Log
				Log.d("Methods.java" + "["
						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
						+ "]", "Folder created: " + db_backup.getAbsolutePath());
			} catch (Exception e) {
				
				// Log
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Create folder => Failed");
				
				return;
				
			}
			
		} else {//if (!db_backup.exists())
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Folder exists: ");
			
		}//if (!db_backup.exists())
		
		/*----------------------------
		 * 3. Copy
			----------------------------*/
		try {
			FileChannel iChannel = new FileInputStream(src).getChannel();
			FileChannel oChannel = new FileOutputStream(dst).getChannel();
			iChannel.transferTo(0, iChannel.size(), oChannel);
			iChannel.close();
			oChannel.close();
			
			// Log
			Log.d("ThumbnailActivity.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "File copied");
			
			// debug
			Toast.makeText(actv, "DB backup => Done", 3000).show();

			dlg.dismiss();
			
		} catch (FileNotFoundException e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
			
		} catch (IOException e) {
			// Log
			Log.e("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception: " + e.toString());
		}//try

		
	}//public static void db_backup(Activity actv, Dialog dlg, String item)

	
}//public class Methods
