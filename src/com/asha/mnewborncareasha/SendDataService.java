package com.asha.mnewborncareasha;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import com.goebl.david.Response;
import com.goebl.david.Webb;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class SendDataService extends Service {

	DBAdapter db;
	private int pending = 0;
	int idle;
	JSONArray ja = new JSONArray();
	static boolean radio_on = false;;
	String url;
	private Intent alarmIntent;
	Context mContext = this;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		new DataSend().execute();
		return super.onStartCommand(intent, flags, startId);
	}

	class DataSend extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// JSONObject jo = new JSONObject();

			db = DBAdapter.getInstance(getBaseContext());

			radio_on = Connectivity
					.isConnectingToInternet(getApplicationContext());

			try {

				if (radio_on) {
					Cursor pendSms = db.getPdataList();
					pending = pendSms.getCount();
					// intent.putExtra("counter", pending);
					// sendBroadcast(intent);
					System.out.println(pendSms.getCount());
					if (pending != 0) {
						if (pendSms != null) {
							if (pendSms.moveToFirst()) {

								idle = 0;
								int msgid = pendSms.getInt(pendSms
										.getColumnIndex("_id"));
								// int
								// retry=pendSms.getInt(pendSms.getColumnIndex("retry"))+1;
								String msg = pendSms.getString(pendSms
										.getColumnIndex("msg"));
								url = pendSms.getString(pendSms
										.getColumnIndex("url"));
								// String url= DBAdapter.resturl;
								int rt = pendSms.getInt(pendSms
										.getColumnIndex("rtype"));
								int rec_flag = pendSms.getInt(pendSms
										.getColumnIndex("recive_flag"));
								if (rec_flag == 0) {
									try {
										JSONObject jo = new JSONObject();
										jo.put("key", msgid);
										jo.put("data", msg);
										Log.i("data", jo + "");

										ja.put(jo);
										System.out.println(pendSms.getCount());
										System.out.println("in");
										System.out.println(ja);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							Log.i("msakhi", ja + "");
							Webb webb = Webb.create();

							Response<String> rs = webb.post(url)
									.body(ja.toString()).ensureSuccess()
									.asString();
							Log.i("response", rs.getBody());

							JSONArray jsa = new JSONArray(rs.getBody());
							for (int i = 0; i < jsa.length(); i++) {

								JSONObject jdata = (JSONObject) jsa.get(i);
								int rowid = jdata.getInt("key");
								Log.i("insert_delete", rowid + "");
								db.deleteWeb1(rowid);
								System.out.println(pendSms.getCount());
								int serverid = jdata.getInt("server_id");
								int id = jdata.getInt("id");
								db.updatepreg_serverid(id, serverid);
								File from = new File(
										Environment.getExternalStorageDirectory()
												+ "/"
												+ Workflow.bfolder
												+ "/pdata/", id + ".jpg");
								File to = new File(
										Environment.getExternalStorageDirectory()
												+ "/"
												+ Workflow.bfolder
												+ "/pdata/", serverid + ".jpg");
								from.renameTo(to);
								Log.i("Msakhi", "change_success");
							}

						}

					}
					pendSms.close();
				}

				Cursor photo_data = db.getPregdata_photo();
				Log.e("data_photo SendData", photo_data.getCount() + "");
				if (photo_data.getCount() != 0) {
					if (photo_data.moveToFirst()) {
						do {
							{
								File file = new File(
										Environment.getExternalStorageDirectory()
												+ "/"
												+ Workflow.bfolder
												+ "/pdata/",
										String.valueOf(photo_data
												.getInt(photo_data
														.getColumnIndex("_id")))
												+ ".jpg");
								String file_path = Environment
										.getExternalStorageDirectory()
										+ "/"
										+ Workflow.bfolder
										+ "/pdata/"
										+ String.valueOf(photo_data.getInt(photo_data
												.getColumnIndex("server_id")))
										+ ".jpg";
								String filename = photo_data.getInt(photo_data
										.getColumnIndex("server_id")) + ".jpg";
								// String filename=
								// String.valueOf(photo_data.getInt(photo_data.getColumnIndex("_id")))+
								// ".jpg";
								Log.i("filepath", file_path);
								Log.i("filename", filename);

								uploadFile(file_path, AppVariable.API(mContext)
										+ "upload.php", filename);
							}
						} while (photo_data.moveToNext());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			stopSelf();
			newsdownload();

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	}

	public void newsdownload() {
		Log.i("ImageUpload", " Setting up auto image capture alarm");
		long triggerTime = SystemClock.elapsedRealtime() + 60 * 1000;

		alarmIntent = new Intent(getApplicationContext(),
				SendDataReciever.class);

		CancelAlarm();
		Log.i("ImageUpload", "New alarm intent");
		PendingIntent sender = PendingIntent.getBroadcast(mContext, 0,
				alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, sender);

		Log.i("ServiceLOgin", "Alarm has been set");
	}

	public void CancelAlarm() {
		Log.i("login", "LoginService.CancelAlarm");

		if (alarmIntent != null) {
			Log.i("LNJP1", "LoginService.CancelAlarm");
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			PendingIntent sender = PendingIntent.getBroadcast(mContext, 0,
					alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			Log.i("ImageUpload",
					"Pending alarm intent was null? "
							+ String.valueOf(sender == null));
			am.cancel(sender);
		}

	}

	public int uploadFile(String sourceFileUri, String upLoadServerUri,
			String fname) {

		int serverResponseCode = 0;
		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {
			Log.e("uploadFile", "Source File not exist :" + sourceFileUri);
			return 0;
		} else {
			try {
				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);
				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fname);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ fname + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				// conn.getResponseMessage();

				InputStream is = conn.getInputStream();
				if (is != null) {
					BufferedReader theReader = new BufferedReader(
							new InputStreamReader(is, "UTF-8"));
					String reply1 = "";

					StringBuilder sb = new StringBuilder();
					while ((reply1 = theReader.readLine()) != null) {
						sb.append(reply1);
					}
					theReader.close();
					Log.i("response_data", sb.toString());
					String response_data[] = sb.toString().split(":");
					if (response_data[0].equals("success")) {
						Log.e("ServerInfo", response_data[1].trim());
						db.updatepreg_mediaflag(
								Integer.parseInt(response_data[1].trim()), 1);
					}
				}
				String serverResponseMessage = conn.getResponseMessage();

				conn.getContent();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (Exception e) {

				e.printStackTrace();

				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}

			return serverResponseCode;

		} // End else block
	}

	private File getDir() {
		File sdDir = Environment.getExternalStorageDirectory();
		return new File(sdDir, "Appolo/Background");
	}
}
