package com.asha.mnewborncareasha;

//import com.intrahealth.mnewborncare.control.BadgeView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
//import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

//import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
//import android.database.Cursor;
//import android.database.SQLException;

import android.database.Cursor;
//import android.net.ConnectivityManager;
//import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

public class Workflow extends Activity {

	int pending = 0;
	// boolean send_sms=false;
	String asha_id = "";
	boolean isBound = false;
	// Button btnPendSms;
	private DBAdapter mydb;
	ProgressDialog mProgressDialog;
	static String bfolder = "mcare_hindi";
	static final String fname = "mnbc_media_hindi_part1.zip";
	static final String vfname = "v3gp_hindi.zip";
	String tmp_fname = "tmp_mnbc_hindi.zip";
//	static String srv_url = "msakhi.org";
	static String srv_url = "msakhi.online";
	boolean gprs_enabled_fromapp = false;
	long last_sync = 0;
	SharedPreferences prefs;
	UpdateServerData obj_update;
	Context con;

	private SmsService serviceBinder;
	Intent i;
	private ServiceConnection connection = new ServiceConnection() {

		@Override
		@SuppressWarnings("unchecked")
		public void onServiceConnected(ComponentName className, IBinder service) {
			// ---called when the connection is made---
			serviceBinder = ((LocalBinder<SmsService>) service).getService();
			pending = serviceBinder.getPending();
			// if (pending==0) {
			// btnPendSms.setText("Clear");btnPendSms.setBackgroundResource(R.drawable.custom_button_green);}
			// else
			// {btnPendSms.setText("SMS pending");btnPendSms.setBackgroundResource(R.drawable.custom_button_red);}
			startService(i);

		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			// ---called when the service disconnects---
			serviceBinder = null;
			isBound = false;
		}
	};

	@Override
	public void onStop() {

		super.onStop();
	}

	@Override
	public void onDestroy() {
		try {

			if (isBound)
				unbindService(connection);
			if (gprs_enabled_fromapp)
				Connectivity.setMobileDataEnabled(getApplicationContext(),
						false);
			mydb.myclose();
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onDestroy();
	}

	public String getVersion() {
		String mVersionNumber;
		Context mContext = getApplicationContext();
		try {
			String pkg = mContext.getPackageName();
			mVersionNumber = mContext.getPackageManager()
					.getPackageInfo(pkg, 0).versionName;
		} catch (NameNotFoundException e) {
			mVersionNumber = "?";
		}
		return " V " + mVersionNumber;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.updated_workflow);
		con = this;
		Button btnMPreg = (Button) findViewById(R.id.btnMPreg);
		Button btnMBirth = (Button) findViewById(R.id.btnMBirth);
		Button btnMVisit = (Button) findViewById(R.id.btnMVisit);
		Button btnMLearn = (Button) findViewById(R.id.btnMLearn);
		Button btnDReport = (Button) findViewById(R.id.btnDreport);
		Button btnReport = (Button) findViewById(R.id.btnReport);
		Button btnCounc = (Button) findViewById(R.id.btnCouncel);
		Button btnAncVisit = (Button) findViewById(R.id.btnAncVisit);
		Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
		// Hero Add
		Button btnWebview = (Button) findViewById(R.id.btnWebview);

		TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// btnPendSms=(Button)findViewById(R.id.btnSMSind);

		obj_update = new UpdateServerData(this);

		// instantiate it within the onCreate method
		mProgressDialog = new ProgressDialog(Workflow.this);

		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// Typeface
		// face=Typeface.createFromAsset(getAssets(),getString(R.string.hindi_font));
		// btnMPreg.setTypeface(face);
		// btnMBirth.setTypeface(face);
		// btnMVisit.setTypeface(face);
		// btnMLearn.setTypeface(face);
		// btnDReport.setTypeface(face);
		DBAdapter.send_sms = prefs.getBoolean("gprs_sync", true);
		if (DBAdapter.send_sms)
			if (!Connectivity.isConnectingToInternet(getApplicationContext())) {
				// Connectivity.setMobileDataEnabled(getApplicationContext(),
				// true);
				gprs_enabled_fromapp = true;
			}
		asha_id = prefs.getString("id", "1").trim();
		last_sync = prefs.getLong("lastsync", 0);
		boolean dialog_flag = prefs.getBoolean("dialog", true);
		// tvTitle.setText(getResources().getString(R.string.app_name)+
		// getVersion());
		// setTitle();
		// send_sms=prefs.getBoolean("send_sms", false);

		createDirIfNotExists(bfolder + "/pdata");
		// createDirIfNotExists("/v3gp");
		createDirIfNotExists(bfolder + "/counsel");
		if (Connectivity.isConnectingToInternet(getApplicationContext())) {
			if (dialog_flag)
				download_media();
		}

		restoreDb();
		mydb = DBAdapter.getInstance(getApplicationContext());
		mydb.UpdateQc();
		if (DBAdapter.send_sms) {
			// btnPendSms.setVisibility(android.view.View.INVISIBLE);
			i = new Intent(Workflow.this, SmsService.class);
			isBound = getApplicationContext().bindService(i, connection,
					Context.BIND_AUTO_CREATE);
			// btnPendSms=(Button)findViewById(R.id.btnSMSind);
			// startService(new Intent(getBaseContext(), SmsService.class));
		} // else btnPendSms.setVisibility(android.view.View.INVISIBLE);

		// BadgeView badge = new BadgeView(this, btnMPreg);
		// badge.setText("1");
		// badge.show();

		btnUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// forceUpdateTest(v);
				try {
					obj_update.execute("");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// Hero Add
		btnWebview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						GuestDonationWebView.class);
				startActivity(intent);
			}
		});
		btnMPreg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Preg_reg_list.class);
				startActivity(intent);
			}
		});

		btnCounc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Counc_sel.class);
				startActivity(intent);
			}
		});

		btnMBirth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						BirthregOpt.class);
				startActivity(intent);
			}
		});

		btnAncVisit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Anc_visit_list.class);
				startActivity(intent);
			}
		});

		btnMVisit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Home_visits_list.class);
				intent.putExtra("learn", false);
				startActivity(intent);
			}
		});

		btnMLearn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						Home_visits_list.class);
				intent.putExtra("learn", true);
				startActivity(intent);
			}
		});

		btnDReport.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						Home_visits_list.class);
				intent.putExtra("death", true);
				startActivity(intent);
				return false;
			}
		});
		btnReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						Report_list.class);
				startActivity(intent);
			}
		});

		/*
		 * ListView listView = (ListView) findViewById(R.id.lstMenu);
		 * //listView.setTypeface(face); listView.setOnItemClickListener(new
		 * OnItemClickListener() {
		 * 
		 * public void onItemClick(AdapterView<?> parent, View view,int
		 * position, long id) { Intent intent=null; switch (position) { case 0:
		 * intent = new Intent(parent.getContext(),Preg_reg_list.class); break;
		 * case 1: intent = new
		 * Intent(parent.getContext(),Birth_reg_list.class); break; default:
		 * break; } if (intent!=null) parent.getContext().startActivity(intent);
		 * } });
		 */

	}

	public void forceUpdateTest(View view) {
		if (view.getId() == R.id.btnUpdate) {
			// new UpdateRunnable(this, new Handler()).force(true).start();
		}
	}

	public void restoreDb() {
		File sd = Environment.getExternalStorageDirectory();
		// String pfname=getFilesDir().getPath()+
		// "../shared_prefs/"+pnm+"_preferences.xml";

		if (sd.canWrite()) {
			File locDB = new File(getFilesDir(), "../databases/");
			String backupDBPath = asha_id + "mnewborncare_rest.db";
			final File currentDB = new File(locDB, DBAdapter.DATABASE_NAME);
			final File backupDB = new File(sd, backupDBPath);
			// Log.e("dbrest",backupDB.getPath());
			if (backupDB.exists()) {
				// Log.e("dbrest1",backupDB.getPath());
				new AlertDialog.Builder(this)
						.setMessage("Do you want to restore ?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										try {
											FileChannel dst = new FileOutputStream(
													currentDB).getChannel();
											FileChannel src = new FileInputStream(
													backupDB).getChannel();
											dst.transferFrom(src, 0, src.size());
											src.close();
											dst.close();
											backupDB.delete();
										} catch (Exception e) {
											e.printStackTrace();
										}

									}
								}).setNegativeButton("No", null).show();
			}
		}
	}

	public void download_media() {

		File sd = Environment.getExternalStorageDirectory();
		// String pfname=getFilesDir().getPath()+
		// "../shared_prefs/"+pnm+"_preferences.xml";

		if (sd.canWrite()) {

			final File mediaFile = new File(sd, fname);
			final File vmediaFile = new File(sd, vfname);
			// Log.e("dbrest",backupDB.getPath());
			if (!mediaFile.exists()) {
				if (Connectivity
						.isConnectingToInternet(getApplicationContext())) {
					// Log.e("dbrest1",backupDB.getPath());
					new AlertDialog.Builder(this)
							.setMessage(
									"Do you want to download essential media and help files ?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int id) {

											try {
												Editor ed = prefs.edit();
												ed.putBoolean("dialog", false);
												ed.commit();
												Log.e("run", "running");

												// execute this when the
												// downloader must be fired
												DownloadFile downloadFile = new DownloadFile();
												mProgressDialog
														.setMessage("Downloading essential media ..");
												downloadFile.execute("http://"
														+ srv_url + "/dnld/"
														+ fname);

											} catch (Exception e) {
												e.printStackTrace();
											}

										}

									}).setNegativeButton("No", null).show();
				} else
					Toast.makeText(getApplicationContext(),
							"Internet not available", Toast.LENGTH_SHORT)
							.show();

			} else if (!vmediaFile.exists()) {
				if (Connectivity
						.isConnectingToInternet(getApplicationContext())) {
					// Log.e("dbrest1",backupDB.getPath());
					new AlertDialog.Builder(this)
							.setMessage(
									"Do you want to download optional video tutorials files ?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int id) {
											try {
												// execute this when the
												// downloader must be fired
												DownloadFile downloadFile = new DownloadFile();
												mProgressDialog
														.setMessage("Downloading optional video tutorials ..");
												downloadFile.execute("http://"
														+ srv_url + "/dnld/"
														+ vfname);

											} catch (Exception e) {
												e.printStackTrace();
												Log.e("error", "error comming");
											}

										}

									}).setNegativeButton("No", null).show();
				} else
					Toast.makeText(getApplicationContext(),
							"Internet not available", Toast.LENGTH_SHORT)
							.show();
			}

		}
	}

	public void decompress_zip() {
		String zipFilename = Environment.getExternalStorageDirectory() + "/"
				+ fname;
		String unzipLocation = Environment.getExternalStorageDirectory() + "/"
				+ bfolder + "/";

		Decompress d = new Decompress(zipFilename, unzipLocation);
		d.unzip();
	}

	public void decompress_vzip() {
		String zipFilename = Environment.getExternalStorageDirectory() + "/"
				+ vfname;
		String unzipLocation = Environment.getExternalStorageDirectory() + "/"
				+ bfolder + "/";

		Decompress d = new Decompress(zipFilename, unzipLocation);
		d.unzip();
	}

	@Override
	public void onBackPressed() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		String pdte = df.format(cal.getTime());
		if ((cal.getTimeInMillis() - last_sync) > 1500000) {
			prefs.edit();
			SharedPreferences.Editor editor = prefs.edit();
			try {
				Cursor c = mydb.getPendLog(pdte);
				if (c.moveToFirst())
					while (!c.isAfterLast()) {

						JSONObject payload = new JSONObject();
						payload.put("aid", asha_id);
						payload.put("dtime",
								c.getString(c.getColumnIndex("dtime")));
						payload.put("info",
								c.getString(c.getColumnIndex("info")));
						mydb.sendGPRS(
								"/logact" + String.format("/%s", asha_id),
								payload.toString(), 1);
						c.moveToNext();
					}
				mydb.custom_qry("delete from actlog where dtime<'" + pdte + "'");
				editor.putLong("lastsync", cal.getTimeInMillis());
				editor.commit();
			} catch (Exception e) {
				Log.d("error", e.getMessage());
			}
		}
		finish();
	}

	/*
	 * private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
	 * 
	 * @Override public void onReceive(Context context, Intent intent) {
	 * pending=intent.getIntExtra("pending", 0); if (pending==0) {
	 * btnPendSms.setText
	 * ("Clear");btnPendSms.setBackgroundResource(R.drawable.custom_button_green
	 * );} else
	 * {btnPendSms.setText("SMS pending");btnPendSms.setBackgroundResource
	 * (R.drawable.custom_button_red);} } };
	 */

	// usually, subclasses of AsyncTask are declared inside the activity class.
	// that way, you can easily modify the UI thread from here
	private class DownloadFile extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... sUrl) {
			try {

				URL url = new URL(sUrl[0]);
				URLConnection connection = url.openConnection();
				connection.connect();

				// this will be useful so that you can show a typical 0-100%
				// progress bar
				int fileLength = connection.getContentLength();
				Log.d("dnld", "" + fileLength);

				// download the file
				File sd = Environment.getExternalStorageDirectory();
				// File dest=new File(sd,"/mnbc_media_part1.zip");
				if (url.getFile().contains("v3gp"))
					tmp_fname = "tmp_v3gp.zip";
				else
					tmp_fname = "tmp_mnbc.zip";
				File dest = new File(sd, tmp_fname);
				if (dest.exists())
					dest.delete();
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(dest);

				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
				Editor ed = prefs.edit();
				ed.putBoolean("dialog", true);
				ed.commit();
				Log.e("run", "error trace");
				Log.e("error", "err");
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			mProgressDialog.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
			try {
				mProgressDialog.dismiss();
				File sd = Environment.getExternalStorageDirectory();
				File from = new File(sd, tmp_fname);
				String t_vfname = fname;
				if (tmp_fname.contains("v3gp")) {
					t_vfname = vfname;
					File to = new File(sd, t_vfname);
					from.renameTo(to);
					decompress_vzip();
				} else {
					File to = new File(sd, t_vfname);
					from.renameTo(to);
					decompress_zip();
				}
			} catch (Exception e) {
				Log.e("error", "err");

			}

		}
	}

	public static boolean createDirIfNotExists(String path) {
		boolean ret = true;

		File file = new File(Environment.getExternalStorageDirectory(), path);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				Log.e("TravellerLog :: ", "Problem creating folder");
				ret = false;
			}
		}
		return ret;
	}

}
