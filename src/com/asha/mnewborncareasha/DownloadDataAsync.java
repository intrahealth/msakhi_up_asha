package com.asha.mnewborncareasha;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class DownloadDataAsync extends AsyncTask<Void, Void, Void> {

	DBAdapter db;
	private static final int CONN_TIMEOUT = 50000;
	Context mainactivity;
	String url_data;
	ProgressDialog mProgressDialog;
	ArrayList<Integer> server_Array, db_Array;

	public DownloadDataAsync(Context main) {
		mainactivity = main;
		url_data = AppVariable.API(mainactivity);
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();

		mProgressDialog = new ProgressDialog(mainactivity);
		// Set progressdialog title
		mProgressDialog.setTitle("LOGIN");
		// Set progressdialog message
		mProgressDialog.setMessage("Loading...");
		// mProgressDialog.setProgressDrawable(getWallpaper().)
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		// Show progressdialog
		mProgressDialog.show();

	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);

	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		mProgressDialog.dismiss();
		Intent i = new Intent(mainactivity, Workflow.class);
		mainactivity.startActivity(i);
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			server_Array = new ArrayList<Integer>();
			db_Array = new ArrayList<Integer>();
			ArrayList<String> server_iddata = new ArrayList<String>();
			String data = POST(url_data + "preg_reg_api.php", "data", null);
			Log.i("download_data", data);
			db = DBAdapter.getInstance(mainactivity.getApplicationContext());
			// downloadBitmap("http://192.168.1.4/msakhi/download.php");
			if (data != null) {

				// db.deleteAllData();
				Log.i("maskhi", "data_deleted");
				db.deleteUnknownData();

				try {
					JSONArray ja = new JSONArray(data);

					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = ja.getJSONObject(i);

						int asha_id = jo.getInt("asha_id");
						int _id = jo.getInt("_id");
						String name = jo.getString("name");
						String lmp = jo.getString("lmp");
						String edd = jo.getString("edd");
						String m_stat = jo.getString("m_stat");
						String c_stat = jo.getString("c_stat");
						String dob = jo.getString("dob");
						String pob = jo.getString("pob");
						String weight = jo.getString("weight");
						String hv_str = jo.getString("hv_str");
						String last_visit = jo.getString("last_visit");
						String m_death = jo.getString("m_death");
						String c_death = jo.getString("c_death");
						String sex = jo.getString("sex");
						String dor = jo.getString("dor");
						String hname = jo.getString("hname");
						String mobile = jo.getString("mobile");
						int imageflag = jo.getInt("image_flag");
						// Log.e("image_flag****************",imageflag+"");

						int caste;
						if (jo.getString("caste").equals(null)
								|| jo.getString("caste").equals("null")) {
							caste = 0;
						} else {
							caste = jo.getInt("caste");
						}

						int religion;
						if (jo.getString("religion").equals(null)
								|| jo.getString("religion").equals("null")) {
							religion = 0;
						} else {
							religion = jo.getInt("religion");
						}
						String dtime = jo.getString("dtime");
						int awc_id = jo.getInt("awc_id");
						int server_id = jo.getInt("server_id");
						int image_flag = jo.getInt("image_flag");
						server_Array.add(server_id);

						System.out.println(server_id);
						System.out.println(db.getserver_id(server_id));
						if (db.getserver_id(server_id)) {
							Log.e("msakhi", "inside_upadte");
							Log.e("msakhi", name + " " + hname + "  " + dob);
							try {// Hero Add
									// if (awc_id != 0) {
								db.updatePreg(_id, name, lmp, hname, caste,
										religion, mobile, asha_id, awc_id, 1,
										server_id, dob);
								// }
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						} else {
							Log.e("msakhi", "inside_insert");
							Log.e("msakhi", name + " " + hname + "  " + dob);
							db.insertPreg1(name, lmp, hname, caste, religion,
									mobile, true, asha_id, awc_id, 1,
									server_id, dob, sex, weight, pob, m_stat,
									c_stat, m_death, c_death, last_visit, edd);
						}
						if (image_flag == 1) {
							server_iddata.add(server_id + "");

						}
					}
					db_Array = db.selectserver_id();
					for (int count = 0; count < db_Array.size(); count++) {
						System.out.print(server_Array);
						if (!(server_Array.contains(db_Array.get(count)))) {
							Log.d("server_id", "" + db_Array.get(count));

							db.deleteLocalData(db_Array.get(count));
						}
					}
					for (int i = 0; i < server_iddata.size(); i++) {
						String server_id;
						server_id = server_iddata.get(i);
						System.out.println(server_id);
						try {
							Log.e("***", "*****dowmload start******");
							downlaodfile(url_data + "download.php", server_id
									+ "");
							POST(url_data + "download_success.php", "response",
									server_id);

						} catch (Exception e) {
							Log.i("cat", "mmmmmmmmmm");
							e.printStackTrace();

						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception");

		}
		return null;
	}

	public String POST(String url, String type, String server_id)
			throws Exception {
		InputStream inputStream = null;
		String result = "";

		JSONObject jsonObject = null;
		// 1. create HttpClient
		HttpClient httpclient = new DefaultHttpClient();

		// ConnManagerParams.setTimeout(httpclient, 1000);
		/*
		 * HttpConnectionParams.setConnectionTimeout(httpclient, CONN_TIMEOUT);
		 * HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);
		 */
		// 2. make POST request to the given URL
		HttpPost httpPost = new HttpPost(url);

		String json = "";

		if (type.equals("data")) {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(mainactivity);
			String uid = prefs.getString("id", "1");
			// 3. build jsonObject

			jsonObject = new JSONObject();
			jsonObject.put("asha_id", Integer.parseInt(uid));
			json = jsonObject.toString();

		}
		if (type.equals("response")) {
			json = server_id;
			Log.i("server_id_return", json);
		}
		// 4. convert JSONObject to JSON to String

		System.out.println(json);
		// ** Alternative way to convert Person object to JSON string usin
		// Jackson Lib
		// ObjectMapper mapper = new ObjectMapper();
		// json = mapper.writeValueAsString(person);

		// 5. set json to StringEntity
		StringEntity se = new StringEntity(json);

		// 6. set httpPost Entity
		httpPost.setEntity(se);

		// 7. Set some headers to inform server about the type of the content
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		// 8. Execute POST request to the given URL
		HttpResponse httpResponse = httpclient.execute(httpPost);

		// 9. receive response as inputStream
		inputStream = httpResponse.getEntity().getContent();

		// 10. convert inputstream to string
		if (inputStream != null)
			result = convertInputStreamToString(inputStream);
		else
			result = "Did not work!";

		Log.i("result", result);

		// 11. return result
		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	public String downlaodfile(String FileDownloadPath, String fileName)
			throws Exception {

		File root = android.os.Environment.getExternalStorageDirectory();
		File dir = new File(Environment.getExternalStorageDirectory() + "/"
				+ Workflow.bfolder + "/pdata/");
		if (dir.exists() == false) {
			dir.mkdirs();
		}
		Log.e("filename", fileName);
		File file = new File(dir, fileName + ".jpg");
		HttpClient httpclient = new DefaultHttpClient();

		// 2. make POST request to the given URL
		HttpPost httpPost = new HttpPost(FileDownloadPath);

		String json = "";

		// 3. build jsonObject

		// 4. convert JSONObject to JSON to String

		long startTime = System.currentTimeMillis();
		Log.e("download image", json);
		System.out.println(json);
		// ** Alternative way to convert Person object to JSON string usin
		// Jackson Lib
		// ObjectMapper mapper = new ObjectMapper();
		// json = mapper.writeValueAsString(person);

		// 5. set json to StringEntity
		StringEntity se = new StringEntity(fileName);

		// 6. set httpPost Entity
		httpPost.setEntity(se);

		// 7. Set some headers to inform server about the type of the content
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		// 8. Execute POST request to the given URL
		HttpResponse httpResponse = httpclient.execute(httpPost);

		// 9. receive response as inputStream
		InputStream inputStream = httpResponse.getEntity().getContent();

		BufferedInputStream bufferinstream = new BufferedInputStream(
				inputStream);

		ByteArrayBuffer baf = new ByteArrayBuffer(5000);
		int current = 0;
		while ((current = bufferinstream.read()) != -1) {
			baf.append((byte) current);
		}

		FileOutputStream fos = new FileOutputStream(file);
		fos.write(baf.toByteArray());
		fos.flush();
		fos.close();

		Log.d("DownloadManager",
				"download ready in"
						+ ((System.currentTimeMillis() - startTime) / 1000)
						+ "sec");
		int dotindex = fileName.lastIndexOf('.');
		if (dotindex >= 0) {
			fileName = fileName.substring(0, dotindex);

		}

		return fileName;

	}
}
