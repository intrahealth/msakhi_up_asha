package com.asha.mnewborncareasha;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Calendar;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText edtPasswd;
	Button imageButton;
	int a;
	String asha_id = "";
	SharedPreferences prefs = null;
	ProgressDialog mProgressDialog;
	Context mContext = this;
	TextView tvSlno;
	/*
	 * int pending=0;
	 * 
	 * private SmsService serviceBinder; Intent i; private ServiceConnection
	 * connection = new ServiceConnection() { public void
	 * onServiceConnected(ComponentName className, IBinder service) {
	 * //---called when the connection is made--- serviceBinder =
	 * ((SmsService.MyBinder)service).getService(); startService(i); }
	 * 
	 * public void onServiceDisconnected(ComponentName className) { //---called
	 * when the service disconnects--- serviceBinder = null; } };
	 */
	public static final String TAG = MainActivity.class.getSimpleName();

	private static MainActivity mInstance;

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
		setContentView(R.layout.login_screen);
		mInstance = this;

		AnalyticsTrackers.initialize(this);
		AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		edtPasswd = (EditText) findViewById(R.id.pinEDTXT);
		edtPasswd.setInputType(InputType.TYPE_CLASS_NUMBER);
		edtPasswd.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		edtPasswd.setSelectAllOnFocus(true);
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		tvSlno = (TextView) findViewById(R.id.slno);
		asha_id = prefs.getString("id", "101").trim();
		tvSlno.setText(asha_id);
		setTitle(getResources().getString(R.string.app_name) + getVersion());
		addListenerOnButton();
	}

	private void addListenerOnButton() {
		Button btnAbout = (Button) findViewById(R.id.about);
		btnAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Context ctx = v.getContext();
				Intent intent = new Intent(ctx, Aboutus.class);
				ctx.startActivity(intent);
			}
		});
		imageButton = (Button) findViewById(R.id.btnStart);

		imageButton.setOnClickListener(new OnClickListener() {

			// @Override
			@Override
			public void onClick(View arg0) {
				Context ctx = arg0.getContext();
				String pass_str = prefs.getString("user_password", "0000");
				String admpass_str = prefs.getString("adm_password", "123456");
				String url = prefs.getString("server_url", "192.168.1.34");
				Log.i("msakhi", url);
				// Boolean send_sms=prefs.getBoolean("send_sms", false);
				if (arg0.getId() == R.id.btnStart) {
					Intent intent = null;

					if (edtPasswd.getText().toString()
							.equalsIgnoreCase(admpass_str)) {
						intent = new Intent(ctx, EditPreference.class);
						ctx.startActivity(intent);
					} else if (!edtPasswd.getText().toString()
							.equalsIgnoreCase(pass_str))
						Toast.makeText(ctx, getString(R.string.errInvPass),
								Toast.LENGTH_LONG).show();
					else {
						// if
						// (Connectivity.isConnectingToInternet(getApplicationContext()))
						// Toast.makeText(ctx,getString(R.string.errInvPass),
						// Toast.LENGTH_LONG).show();
						/*
						 * if (send_sms) { i = new Intent(MainActivity.this,
						 * SmsService.class); bindService(i, connection,
						 * Context.BIND_AUTO_CREATE);
						 * pending=serviceBinder.pending; //startService(new
						 * Intent(getBaseContext(), SmsService.class)); }
						 */
						// intent = new Intent(ctx,Workflow.class);
						exportDB();// Add Hero
						// Intent i = new Intent(MainActivity.this,
						// Workflow.class);
						// startActivity(i);

						Intent i = new Intent(MainActivity.this,
								SendDataService.class);
						startService(i);
						new DownloadDataAsync(mContext).execute();
					}

					// if (intent!=null) ctx.startActivity(intent);
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {

		new AlertDialog.Builder(this)
				.setMessage("सोच लीजिये! क्या आपको बाहर निकलना है?")
				.setCancelable(false)
				.setPositiveButton("हाँ",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								try {

									stopService(new Intent(getBaseContext(),
											SmsService.class));
									String pnm = getApplicationContext()
											.getPackageName();
									File sd = Environment
											.getExternalStorageDirectory();
									// String pfname=getFilesDir().getPath()+
									// "../shared_prefs/"+pnm+"_preferences.xml";

									if (sd.canWrite()) {
										File locDB = new File(getFilesDir(),
												"../databases/");
										// String backupDBPath =
										// "backupdata.db";
										String backupDBPath = asha_id
												+ "mnewborncare_back.db";
										File currentDB = new File(locDB,
												DBAdapter.DATABASE_NAME);
										File backupDB = new File(sd,
												backupDBPath);

										if (currentDB.exists()) {
											FileChannel src = new FileInputStream(
													currentDB).getChannel();
											FileChannel dst = new FileOutputStream(
													backupDB).getChannel();
											dst.transferFrom(src, 0, src.size());
											src.close();
											dst.close();
										}

										if (true) {
											File locPF = new File(
													getFilesDir(),
													"../shared_prefs/");
											File currentPF = new File(locPF,
													pnm + "_preferences.xml");
											File backupPF = new File(sd,
													"prefs.xml");
											FileChannel src = new FileInputStream(
													currentPF).getChannel();
											FileChannel dst = new FileOutputStream(
													backupPF).getChannel();
											dst.transferFrom(src, 0, src.size());
											src.close();
											dst.close();
										}

									}
								} catch (Exception e) {
									e.printStackTrace();

								}
								finish();
							}
						}).setNegativeButton("ना", null).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// The activity is about to be destroyed.
	}

	@Override
	protected void onResume() {
		asha_id = prefs.getString("id", "101").trim();
		tvSlno.setText(asha_id);
		super.onResume();
	}

	public static synchronized MainActivity getInstance() {
		return mInstance;
	}

	public synchronized Tracker getGoogleAnalyticsTracker() {
		AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
		return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
	}

	/***
	 * Tracking screen view
	 * 
	 * @param screenName
	 *            screen name to be displayed on GA dashboard
	 */
	public void trackScreenView(String screenName) {
		Tracker t = getGoogleAnalyticsTracker();

		// Set screen name.
		t.setScreenName(screenName);

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());

		GoogleAnalytics.getInstance(this).dispatchLocalHits();
	}

	/***
	 * Tracking exception
	 * 
	 * @param e
	 *            exception to be tracked
	 */
	public void trackException(Exception e) {
		if (e != null) {
			Tracker t = getGoogleAnalyticsTracker();

			t.send(new HitBuilders.ExceptionBuilder()
					.setDescription(
							new StandardExceptionParser(this, null)
									.getDescription(Thread.currentThread()
											.getName(), e)).setFatal(false)
					.build());
		}
	}

	/***
	 * Tracking event
	 * 
	 * @param category
	 *            event category
	 * @param action
	 *            action of the event
	 * @param label
	 *            label
	 */
	public void trackEvent(String category, String action, String label) {
		Tracker t = getGoogleAnalyticsTracker();

		// Build and send an Event.
		t.send(new HitBuilders.EventBuilder().setCategory(category)
				.setAction(action).setLabel(label).build());
	}

	private void exportDB() {

		File sd = Environment.getDataDirectory();
		File f = new File(Environment.getExternalStorageDirectory().toString()
				+ "/com.asha.mnewborncareasha");
		if (!f.isDirectory()) {
			f.mkdir();
		}
		String SAMPLE_DB_NAME = "mnewborncare.db";
		File data = Environment.getDataDirectory();
		FileChannel source = null;
		FileChannel destination = null;
		String currentDBPath = "/data/" + "com.asha.mnewborncareasha"
				+ "/databases/" + SAMPLE_DB_NAME;
		String backupDBPath = SAMPLE_DB_NAME;
		File currentDB = new File(data, currentDBPath);
		File backupDB = new File(f, backupDBPath);

		try {
			source = new FileInputStream(currentDB).getChannel();
			destination = new FileOutputStream(backupDB).getChannel();
			destination.transferFrom(source, 0, source.size());
			source.close();
			destination.close();
			// alerttoast(R.string.DBexport);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
