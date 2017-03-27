package com.asha.mnewborncareasha;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.goebl.david.Webb;

import android.app.Activity;
import android.app.AlertDialog;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
//import android.database.SQLException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
//import android.telephony.SmsManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class preg_entry extends Activity {
	int slno = 0;
	int k = 0, s = 0, z = 0, da = 0, m = 0;
	java.util.Date d = null;
	java.util.Date d1 = null;
	private static int TAKE_PICTURE = 0;
	AsyncTask<Void, Void, Void> mRegisterTask;
	private DBAdapter mydb;
	boolean recStarted = false, modified = false;
	// Typeface face;
	// final AudioRecorder recorder = new AudioRecorder("tmp/test.3gp");
	RecordAmplitude recordAmplitude;
	MediaRecorder recorder;
	MediaPlayer mediaPlayer, beep_mp;
	String audioFile = "", pname = "", plmp;
	ProgressBar prg;
	MediaPlayer aplayer;
	private int image_flag = 1;
	Context mContext = this;
	boolean change = false, isRecording = false, dbreg = false,
			backPressed = false;
	TextView tv, tvId, tvMname, tvheadLMP, tvEDD, tvPhotoCap, tvHead, tvHname;//
	LinearLayout llMnamein, llRecPhoto, llRecMon, llOptInfo, llLMP, llAwid;
	ImageButton btnPhoto;
	ImageView imgPreview;
	Button btnSave;
	String phoneNumber;
	public static String q_msg = "", uid;
	DatePicker dtp;
	Calendar mc;
	File path;
	EditText edtName, edtHname, edtMobile;
	Spinner spnCaste, spnRel, spnAwid;
	InputMethodManager imm;
	// String resturl;
	public int stage = 0;
	DatePicker dtPicker;
	// public static final String
	// hmstr[]={"जनवरी","फरवरी","मार�?च","अप�?रैल","मई","जून","ज�?लाई","अगस�?त","सितम�?बर","अक�?टूबर","नवम�?बर","दिसम�?बर"};
	OnClickListener myonclick;
	protected int diffInDays;
	private TextView diff;
	String eddvalue = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.preg_entry);
		// TextView tvHead=(TextView)findViewById(R.id.tvHead);
		btnPhoto = (ImageButton) findViewById(R.id.btnPhotocap);
		// btnRec = (ImageButton) findViewById(R.id.btnRec);
		btnSave = (Button) findViewById(R.id.btnSave);
		tvId = (TextView) findViewById(R.id.slno);
		tvHead = (TextView) findViewById(R.id.tvHead);
		tvMname = (TextView) findViewById(R.id.tvMname);
		tvHname = (TextView) findViewById(R.id.tvHname);
		tvheadLMP = (TextView) findViewById(R.id.tvheadLMP);
		diff = (TextView) findViewById(R.id.diff);
		// tvRecName=(TextView)findViewById(R.id.tvRecName);
		tvEDD = (TextView) findViewById(R.id.tvEDD);
		tvPhotoCap = (TextView) findViewById(R.id.tvPhotoCap);
		llMnamein = (LinearLayout) findViewById(R.id.llMnamein);
		// llRecAudio=(LinearLayout)findViewById(R.id.llRecAudio);
		llRecPhoto = (LinearLayout) findViewById(R.id.llRecPhoto);
		llRecMon = (LinearLayout) findViewById(R.id.llRecMon);
		llOptInfo = (LinearLayout) findViewById(R.id.llOptInfo);
		llAwid = (LinearLayout) findViewById(R.id.llAwid);
		llLMP = (LinearLayout) findViewById(R.id.llLMP);
		imgPreview = (ImageView) findViewById(R.id.imgPreview);
		// face=Typeface.createFromAsset(getAssets(),getString(R.string.hindi_font));
		// tvHead.setTypeface(face);

		// tvMname.setTypeface(face);
		phoneNumber = getString(R.string.smsno);
		dtPicker = (DatePicker) findViewById(R.id.dtpLmp);
		// Set up view for 1st stage
		// tvheadLMP.setVisibility(android.view.View.GONE);
		// dtPicker.setVisibility(android.view.View.GONE);
		tvPhotoCap.setVisibility(android.view.View.GONE);
		llRecPhoto.setVisibility(android.view.View.GONE);
		llRecMon.setVisibility(android.view.View.GONE);
		llOptInfo.setVisibility(android.view.View.GONE);
		llLMP.setVisibility(android.view.View.GONE);
		btnSave.setText("आगे");
		btnSave.setBackgroundResource(R.drawable.arrowimage);
		// End of 1st stage view
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			change = extras.getBoolean("change");
			slno = extras.getInt("slno");
			dbreg = extras.getBoolean("dbreg");
		}

		// ViewGroup childpicker;
		// childpicker=(ViewGroup)findViewById(Resources.getSystem().getIdentifier("month",
		// "id", "android"));
		// EditText
		// tv1=(EditText)childpicker.findViewById(Resources.getSystem().getIdentifier("datepicker_input",
		// "id", "android"));
		// tv1.setTextColor(Color.BLUE);;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		uid = prefs.getString("id", "1");
		// send_sms=prefs.getBoolean("send_sms", false);
		// resturl=prefs.getString("resturl", "");

		// dtPicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
		mc = Calendar.getInstance();
		// mc.add(Calendar.DAY_OF_MONTH,-285);
		mc.add(Calendar.MONTH, -13);
		Calendar defCalendar = Calendar.getInstance();
		dtPicker.init(defCalendar.get(Calendar.YEAR),
				defCalendar.get(Calendar.MONTH),
				defCalendar.get(Calendar.DAY_OF_MONTH),
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						if (isDateInvalid(view)) {
							Toast.makeText(getBaseContext(), "गलत तारीख ",
									Toast.LENGTH_SHORT).show();
							Calendar mCalendar = Calendar.getInstance();
							view.init(mCalendar.get(Calendar.YEAR),
									mCalendar.get(Calendar.MONTH),
									mCalendar.get(Calendar.DAY_OF_MONTH), this);
						} else {
							Calendar mc = Calendar.getInstance();
							mc.setTimeInMillis(view.getCalendarView().getDate());
							Calendar mc1 = Calendar.getInstance();
							if (!dbreg)
								mc.add(Calendar.DATE, 277);
							SimpleDateFormat sdf = new SimpleDateFormat(
									"dd-MMM-yyyy");
							tvEDD.setText("सम्भावित: "
									+ sdf.format(mc.getTime()));
							
							SimpleDateFormat sdf1 = new SimpleDateFormat(
									"yyyy-MM-dd");
							eddvalue = sdf1.format(mc.getTime());
							try {
								// d= sdf.parse("17-jul-2015");
								d = sdf.parse(sdf.format(mc1.getTime()));
								d1 = sdf.parse(sdf.format(mc.getTime()));
							} catch (java.text.ParseException e) {
								e.printStackTrace();
							}

							diffInDays = (int) ((d1.getTime() - d.getTime()) / (1000 * 60 * 60 * 24));
							z = diffInDays;
							da = z % 30;
							m = z / 30;
							if (da < 0 || m < 0) {
								diff.setText("अंतर :संभव नहीं");
							} else {
								diff.setText("अंतर :" + m + " माह " + da
										+ " दिन");
							}
						}
					}
				});
		Calendar mc = Calendar.getInstance();
		mc.setTimeInMillis(dtPicker.getCalendarView().getDate());

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		if (!dbreg) {
			mc.add(Calendar.DATE, 277);
			tvEDD.setText("सम्भावित: " + sdf.format(mc.getTime()));
			diff.setText("अंतर :" + m + " माह " + da + " दिन");
			// eddvalue=sdf.format(mc.getTime());
			tvEDD.setVisibility(android.view.View.VISIBLE);
		} else
			tvEDD.setVisibility(android.view.View.INVISIBLE);
		// tvEDD.setText("जन्म की तारीख: "+sdf.format(mc.getTime()));

		path = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + Workflow.bfolder + "/pdata/");
		if (!path.exists())
			path.mkdirs();

		mediaPlayer = new MediaPlayer();
		recorder = new MediaRecorder();
		beep_mp = MediaPlayer.create(getApplicationContext(), R.raw.beep7);
		try {
			beep_mp.prepare();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		;

		if (dbreg) {
			tvHead.setText("नया शिशु");
			tvMname.setText("माँ का नाम लिखे");
			// tvRecName.setText("माँ का नाम रिकॉर्ड करें");
			tvPhotoCap.setText("शिशु की तस्वीर खीचें");
			tvHname.setText("पिता का नाम");
			tvheadLMP.setText("जन्म की तारीख");
		} else {
			tvHead.setText("नई गर्भवती");
			// tvRecName.setText("गर्भवती का नाम रिकॉर्ड करें");
			tvHname.setText("पति का नाम");
			tvMname.setText("गर्भवती का नाम लिखे");
			tvheadLMP.setText("आखिरी माहवारी");
			tvPhotoCap.setText("गर्भवती का तस्वीर खीचें");
		}

		tv = (TextView) findViewById(R.id.tvRecMon);
		prg = (ProgressBar) findViewById(R.id.prgLvlVol);
		tv.setText("..");
		tvId.setText(String.valueOf(slno));

		// ImageButton v = (ImageButton)findViewById(R.id.btnStart);
		// View v = findViewById(R.id.btnStart);
		// v.setOnClickListener(this);
		mydb = DBAdapter.getInstance(getApplicationContext());
		Cursor tmpc = mydb.getAwlist();
		String[] from = new String[] { "descr" };
		int[] to = new int[] { android.R.id.text1 };
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter sca = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, tmpc, from, to);
		sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnAwid = (Spinner) this.findViewById(R.id.spnAWid);
		spnAwid.setAdapter(sca);
		/*
		 * mydb = new DBAdapter(this); try { //mydb.createDataBase();
		 * mydb.openDataBase(); } catch (SQLException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		edtName = (EditText) findViewById(R.id.edtName);
		edtHname = (EditText) findViewById(R.id.edtHname);
		edtMobile = (EditText) findViewById(R.id.edtMobile);
		// edtMobile.addTextChangedListener(new
		// PhoneNumberFormattingTextWatcher());
		spnCaste = (Spinner) findViewById(R.id.spnCaste);
		spnRel = (Spinner) findViewById(R.id.spnReligion);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
		String actinfo;
		if (!dbreg)
			actinfo = "Pregnent registration " + slno;
		else
			actinfo = "Direct birth registration " + slno;
		if (change) {
			actinfo = "Pregnent registration change " + slno;
			Cursor c = mydb.getPreg(slno);
			pname = c.getString(c.getColumnIndex("name"));
			plmp = c.getString(c.getColumnIndex("lmp"));
			edtName.setText(pname);
			edtHname.setText(c.getString(c.getColumnIndex("hname")));
			edtMobile.setText(c.getString(c.getColumnIndex("mobile")));
			spnCaste.setSelection(c.getInt(c.getColumnIndex("caste")));
			spnRel.setSelection(c.getInt(c.getColumnIndex("religion")));
			// spnAwid.setSelection((((ArrayAdapter)spnAwid.getAdapter()).getPosition(c.getInt(c.getColumnIndex("aid")))));
			long awid = c.getInt(c.getColumnIndex("awid"));
			for (int position = 0; position < sca.getCount(); position++) {
				if (sca.getItemId(position) == awid) {
					spnAwid.setSelection(position);
					Log.e("sele", spnAwid.getSelectedItem().toString());
					break;
				}
			}
			edtName.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable s) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					modified = true;
				}
			});
			edtHname.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable s) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					modified = true;
				}
			});
			edtMobile.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable s) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					modified = true;
				}
			});
			spnCaste.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {

					modified = true;

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});
			spnAwid.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {

					modified = true;

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});
			spnRel.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {

					modified = true;

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});
			String dt_str[] = plmp.split("\\-");
			dtPicker.init(Integer.parseInt(dt_str[0]),
					Integer.parseInt(dt_str[1]) - 1,
					Integer.parseInt(dt_str[2]), new OnDateChangedListener() {
						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							modified = true;
							if (isDateInvalid(view)) {
								Toast.makeText(getBaseContext(), "गलत तारीख",
										Toast.LENGTH_SHORT).show();
								Calendar mCalendar = Calendar.getInstance();
								view.init(mCalendar.get(Calendar.YEAR),
										mCalendar.get(Calendar.MONTH),
										mCalendar.get(Calendar.DAY_OF_MONTH),
										this);
							} else {
								Calendar mc = Calendar.getInstance();
								mc.setTimeInMillis(view.getCalendarView()
										.getDate());
								Calendar mc1 = Calendar.getInstance();
								if (!dbreg)
									mc.add(Calendar.DATE, 277);
								SimpleDateFormat sdf = new SimpleDateFormat(
										"dd-MMM-yyyy");
								tvEDD.setText("सम्भावित: "
										+ sdf.format(mc.getTime()));
								// eddvalue=sdf.format(mc.getTime());
								try {
									// d= sdf.parse("17-jul-2015");
									d = sdf.parse(sdf.format(mc1.getTime()));
									d1 = sdf.parse(sdf.format(mc.getTime()));
								} catch (java.text.ParseException e) {
									e.printStackTrace();
								}

								diffInDays = (int) ((d1.getTime() - d.getTime()) / (1000 * 60 * 60 * 24));
								z = diffInDays;
								da = z % 30;
								m = z / 30;
								if (da < 0 || m < 0) {
									diff.setText("अंतर :संभव नहीं");
								} else {
									diff.setText("अंतर :" + m + " माह " + da
											+ " दिन");
								}
							}
						}

					});

			mc.setTimeInMillis(dtPicker.getCalendarView().getDate());
			if (!dbreg) {
				mc.add(Calendar.DATE, 277);
				tvEDD.setText("सम्भावित: " + sdf.format(mc.getTime()));
				// eddvalue=sdf.format(mc.getTime());
			} else
				tvEDD.setText("जन्म की तारीख: " + sdf.format(mc.getTime()));
			// eddvalue=sdf.format(mc.getTime());

			String imgfile = Environment.getExternalStorageDirectory()
					+ File.separator + Workflow.bfolder + "/pdata/"
					+ String.valueOf(slno) + ".jpg";
			File file = new File(imgfile);
			if (file.exists()) {
				Bitmap b = BitmapFactory.decodeFile(imgfile);
				Bitmap bMapRotate = null;
				Matrix mat = new Matrix();
				if (b.getWidth() > b.getHeight()) {
					mat.postRotate(90);
					bMapRotate = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
							b.getHeight(), mat, true);
					b.recycle();
					b = null;
					imgPreview.setImageBitmap(bMapRotate);
				} else
					imgPreview.setImageBitmap(b);
			}
		} else {
			edtName.setText("");
			// edtName.setText("* नाम डालें  *");
		}
		mydb.logAct(actinfo);
		addListenerOnButton();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	@Override
	public void onStop() {

		recorder.release();
		mediaPlayer.release();
		// mydb.close();
		super.onStop();
	}

	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		// check if no view has focus:
		View view = this.getCurrentFocus();
		if (view != null) {
			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onResume() {

		if (mediaPlayer == null)
			mediaPlayer = new MediaPlayer();
		if (recorder == null)
			recorder = new MediaRecorder();
		super.onResume();
	}

	private void addListenerOnButton() {

		// btnSave.setTypeface(face);
		myonclick = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// String longMessage;
				// String
				// pktHeader=getString(R.string.sms_prefix)+" "+getString(R.string.signature)+" "+uid;
				// Context ctx=arg0.getContext();
				boolean validated = true;
				String errmsg = "";
				// Toast.makeText(getApplicationContext(),""+stage,
				// Toast.LENGTH_SHORT).show();
				if (!backPressed)
					switch (stage) {
					case 0:
						errmsg = "सही नाम डाले";
						validated = !edtName.getText().toString()
								.equals("* नाम डालें  *")
								&& !edtName.getText().toString().equals("");
						break;
					case 1:
						break;
					case 2:
						errmsg = "सही नाम डाले";
						validated = !edtHname.getText().toString().equals("");
						if (validated) {
							errmsg = "सही मोबाइल नंबर डाले";
							validated = !(edtMobile.getText().toString()
									.length() < 10);
						}
						;
						break;
					case 3:
					}
				if (!validated)
					Toast.makeText(arg0.getContext(), errmsg,
							Toast.LENGTH_SHORT).show();
				else {
					if (!backPressed)
						stage++;
					if (stage > 4)
						stage = 4;
					backPressed = false;
					switch (stage) {
					case 0:
						// Set up view for 2nd stage
						tvMname.setVisibility(android.view.View.VISIBLE);
						llMnamein.setVisibility(android.view.View.VISIBLE);
						llAwid.setVisibility(android.view.View.VISIBLE);
						llLMP.setVisibility(android.view.View.GONE);
						// tvheadLMP.setVisibility(android.view.View.VISIBLE);
						// dtPicker.setVisibility(android.view.View.VISIBLE);
						// llRecAudio.setVisibility(android.view.View.VISIBLE);
						// tvRecName.setVisibility(android.view.View.VISIBLE);
						llRecMon.setVisibility(android.view.View.VISIBLE);
						tvPhotoCap.setVisibility(android.view.View.GONE);
						llRecPhoto.setVisibility(android.view.View.GONE);
						llOptInfo.setVisibility(android.view.View.GONE);
						btnSave.setText("आगे");

						break;
					case 1:
						hideKeyboard();
						// Set up view for 2nd stage
						tvMname.setVisibility(android.view.View.GONE);
						llAwid.setVisibility(android.view.View.GONE);
						llMnamein.setVisibility(android.view.View.GONE);
						llLMP.setVisibility(android.view.View.VISIBLE);
						// tvheadLMP.setVisibility(android.view.View.VISIBLE);
						// dtPicker.setVisibility(android.view.View.VISIBLE);
						// llRecAudio.setVisibility(android.view.View.GONE);
						// tvRecName.setVisibility(android.view.View.GONE);
						llRecMon.setVisibility(android.view.View.GONE);
						tvPhotoCap.setVisibility(android.view.View.GONE);
						llRecPhoto.setVisibility(android.view.View.GONE);
						llOptInfo.setVisibility(android.view.View.GONE);
						btnSave.setText("आगे");
						// End of 2nd stage view
						break;
					case 2:
						// Set up view for 2nd stage
						tvMname.setVisibility(android.view.View.GONE);
						llMnamein.setVisibility(android.view.View.GONE);
						llLMP.setVisibility(android.view.View.GONE);
						llAwid.setVisibility(android.view.View.GONE);
						// tvheadLMP.setVisibility(android.view.View.GONE);
						// dtPicker.setVisibility(android.view.View.GONE);
						// llRecAudio.setVisibility(android.view.View.GONE);
						// tvRecName.setVisibility(android.view.View.GONE);
						llRecMon.setVisibility(android.view.View.GONE);
						tvPhotoCap.setVisibility(android.view.View.GONE);
						llRecPhoto.setVisibility(android.view.View.GONE);
						llOptInfo.setVisibility(android.view.View.VISIBLE);
						btnSave.setText("आगे");
						// End of 2nd stage view
						break;

					case 3:
						imm.hideSoftInputFromWindow(edtName.getWindowToken(), 0);
						// Set up view for 2nd stage
						tvMname.setVisibility(android.view.View.GONE);
						llMnamein.setVisibility(android.view.View.GONE);
						llAwid.setVisibility(android.view.View.GONE);
						llLMP.setVisibility(android.view.View.GONE);

						// tvheadLMP.setVisibility(android.view.View.GONE);
						// dtPicker.setVisibility(android.view.View.GONE);
						// llRecAudio.setVisibility(android.view.View.GONE);
						llRecMon.setVisibility(android.view.View.GONE);
						// tvRecName.setVisibility(android.view.View.GONE);
						tvPhotoCap.setVisibility(android.view.View.VISIBLE);
						llRecPhoto.setVisibility(android.view.View.VISIBLE);
						llOptInfo.setVisibility(android.view.View.GONE);
						if (!dbreg)
							btnSave.setText("सेव");
						// End of 2nd stage view
						break;
					case 4:
						dtp = (DatePicker) findViewById(R.id.dtpLmp);
						edtName = (EditText) findViewById(R.id.edtName);
						imm.hideSoftInputFromWindow(edtName.getWindowToken(), 0);
						openPreviewDialog();
						break;
					}
				}
			}
		};
		btnSave.setOnClickListener(myonclick);

		btnPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				image_flag = 0;
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/" + Workflow.bfolder + "/pdata/", String
						.valueOf(slno) + ".jpg");
				if (file.exists())
					Toast.makeText(arg0.getContext(), "Overwriting photo ..",
							Toast.LENGTH_SHORT).show();
				Uri outputFileUri = Uri.fromFile(file);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				startActivityForResult(intent, TAKE_PICTURE);
			}

		});

		// btnRec.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View arg0) {
		// if (!recStarted)
		// {
		// //---------------------------
		// beep_mp.start();
		// if (recorder!=null) recorder.release();
		// recorder=new MediaRecorder();
		// recorder.reset();
		// recorder.setOnErrorListener(new OnErrorListener(){
		//
		// public void onError(MediaRecorder arg0, int arg1, int arg2) {
		// // TODO Auto-generated method stub
		// tv.setText("Finished err");
		// }
		//
		// });
		//
		// recorder.setOnInfoListener(new OnInfoListener() {
		// public void onInfo(MediaRecorder mr, int what, int extra) {
		// if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
		// //btnRec.setImageResource(R.drawable.crimson);
		// tv.setText("Finished ..");
		// isRecording = false;
		// recordAmplitude.cancel(true);
		// recorder.stop();
		// recorder.reset();
		// //recorder.release();
		// }
		// }
		// });
		//
		//
		// recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		// recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		// //File path = new
		// File(Environment.getExternalStorageDirectory().getAbsolutePath()
		// // + "/mcare/pdata/");
		// //if (!path.exists()) path.mkdirs();
		//
		// //audioFile = File.create("recording", ".3gp", path);
		// try {
		// audioFile=path+"/"+String.valueOf(slno)+".3gp";
		// recorder.setOutputFile(audioFile);
		// recorder.setMaxDuration(5000);
		// recorder.prepare();
		// } catch (IllegalStateException e) {
		// throw new RuntimeException(
		// "IllegalStateException on MediaRecorder.prepare", e);
		// } catch (Exception e) {
		// throw new RuntimeException(
		// "Exception on MediaRecorder.prepare", e);
		// }
		// llRecMon.setVisibility(android.view.View.VISIBLE);
		// tv.setText("Recording..");
		// recorder.start();
		// isRecording = true;
		// recordAmplitude = new RecordAmplitude();
		// recordAmplitude.execute();
		//
		//
		// //---------------------------
		//
		// recStarted=true;
		// //btnRec.setImageResource(R.drawable.green);
		// Toast.makeText(getApplicationContext(),"Recording started",
		// Toast.LENGTH_SHORT).show();
		// }
		// else
		// {
		// //btnRec.setImageResource(R.drawable.crimson);
		// recStarted=false;
		// isRecording = false;
		// if (isRecording) {
		// tv.setText("Finished ..");
		// try {
		// recorder.stop();
		// recordAmplitude.cancel(true);
		// //recorder.stop();
		// recorder.reset();
		// //recorder.release();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// Toast.makeText(getApplicationContext(),"Recording finished",
		// Toast.LENGTH_SHORT).show();
		// }
		// }
		// }
		// });

		// ImageButton btnPlay = (ImageButton) findViewById(R.id.btnPlay);

		// btnPlay.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View arg0) {
		// audioFile=path+"/"+String.valueOf(slno)+".3gp";
		// if (new File(audioFile).exists())
		// {
		// try {
		// if (aplayer==null) aplayer=new MediaPlayer();
		// aplayer.reset();
		// aplayer.setDataSource(audioFile);
		// aplayer.prepare();
		// aplayer.start();
		//
		// //MediaPlayer mediaPlayer = new MediaPlayer();
		// //mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/tmp/test.3gp");
		// //if (mediaPlayer.isPlaying())
		// /* mediaPlayer.reset();
		// mediaPlayer.setDataSource(audioFile);
		// mediaPlayer.prepare();
		// mediaPlayer.start();*/
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// } else
		// Toast.makeText(getApplicationContext(),"Not recorded yet "+audioFile,
		// Toast.LENGTH_SHORT).show();
		// }
		// });

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PICTURE) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				String imgfile = Environment.getExternalStorageDirectory()
						+ File.separator + Workflow.bfolder + "/pdata/"
						+ String.valueOf(slno) + ".jpg";
				File file = new File(imgfile);
				if (file.exists()) {
					// Bitmap bmp = BitmapFactory.decodeFile(imgfile);
					Bitmap b = BitmapFactory.decodeFile(imgfile);
					Bitmap bMapRotate = null;
					Matrix mat = new Matrix();
					if (b.getWidth() > b.getHeight()) {
						mat.postRotate(90);
						bMapRotate = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
								b.getHeight(), mat, true);
						b.recycle();
						b = null;
						imgPreview.setImageBitmap(bMapRotate);
					} else
						imgPreview.setImageBitmap(b);
					// imgPreview.setImageBitmap(bmp);
				}

			}
		}
	};

	private class RecordAmplitude extends AsyncTask<Void, Integer, Void> {
		int ma = 0;

		@Override
		protected Void doInBackground(Void... params) {
			while (isRecording) {
				try {
					ma = recorder.getMaxAmplitude();
					Thread.sleep(500);
				} catch (Exception e) {
					// e.printStackTrace();
				}
				publishProgress(ma);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {

			float max = 30000, fpos = progress[0];
			if (fpos > max)
				fpos = max;
			int pos = (int) ((fpos / max) * 100);
			// tv.setText(progress[0].toString()+" "+String.valueOf(pos));
			prg.setProgress(pos);
		}
	}

	@Override
	public void onBackPressed() {
		if (stage > 0) {
			stage--;
			backPressed = true;
			btnSave.performClick();
		} else
			new AlertDialog.Builder(this)
					.setMessage("सोच लीजिये! क्या आपको बाहर निकलना है?")
					.setCancelable(false)
					.setPositiveButton("हां",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									mydb.close();
									beep_mp.release();
									mediaPlayer.release();
									finish();
								}
							}).setNegativeButton("ना", null).show();
	}

	/*
	 * private void sendSMS(String phoneNumber, String message) { String SENT =
	 * "SMS_SENT"; String DELIVERED = "SMS_DELIVERED"; q_msg=message;
	 * 
	 * 
	 * PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new
	 * Intent(SENT), 0);
	 * 
	 * PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new
	 * Intent(DELIVERED), 0);
	 * 
	 * //---when the SMS has been sent--- registerReceiver(new
	 * BroadcastReceiver(){
	 * 
	 * @Override public void onReceive(Context arg0, Intent arg1) { switch
	 * (getResultCode()) { case Activity.RESULT_OK:
	 * Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
	 * break; case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	 * Toast.makeText(getBaseContext(), "Generic failure",
	 * Toast.LENGTH_SHORT).show(); mydb.insertSms(q_msg); break; case
	 * SmsManager.RESULT_ERROR_NO_SERVICE: Toast.makeText(getBaseContext(),
	 * "No service", Toast.LENGTH_SHORT).show(); mydb.insertSms(q_msg); break;
	 * case SmsManager.RESULT_ERROR_NULL_PDU: Toast.makeText(getBaseContext(),
	 * "Null PDU", Toast.LENGTH_SHORT).show(); mydb.insertSms(q_msg); break;
	 * case SmsManager.RESULT_ERROR_RADIO_OFF: Toast.makeText(getBaseContext(),
	 * "Radio off", Toast.LENGTH_SHORT).show(); mydb.insertSms(q_msg); break; }
	 * } }, new IntentFilter(SENT));
	 * 
	 * //---when the SMS has been delivered--- registerReceiver(new
	 * BroadcastReceiver(){
	 * 
	 * @Override public void onReceive(Context arg0, Intent arg1) { switch
	 * (getResultCode()) { case Activity.RESULT_OK:
	 * Toast.makeText(getBaseContext(), "SMS delivered",
	 * Toast.LENGTH_SHORT).show(); break; case Activity.RESULT_CANCELED:
	 * Toast.makeText(getBaseContext(), "SMS not delivered",
	 * Toast.LENGTH_SHORT).show(); break; } } }, new IntentFilter(DELIVERED));
	 * 
	 * SmsManager sms = SmsManager.getDefault(); Toast.makeText(this,message,
	 * Toast.LENGTH_LONG).show(); sms.sendTextMessage(phoneNumber, null,
	 * message, sentPI, deliveredPI); }
	 */
	private boolean isDateInvalid(DatePicker tempView) {

		Calendar mCalendar = Calendar.getInstance();
		Calendar tempCalendar = Calendar.getInstance();
		int y = tempView.getYear();
		int m = tempView.getMonth();
		int d = tempView.getDayOfMonth();
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.YEAR, -6);
		tempCalendar.set(y, m, d, 0, 0, 0);
		if (tempCalendar.after(mCalendar) || tempCalendar.before(cal))
			return true;
		else
			return false;

	}

	void openPreviewDialog() {

		final String pktHeader = getString(R.string.sms_prefix) + " "
				+ getString(R.string.signature) + " " + uid;
		AlertDialog.Builder customDialog = new AlertDialog.Builder(
				preg_entry.this);
		customDialog.setTitle("ध्यान दें!");
		Context ctx = getApplicationContext();
		LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.preg_infodlg, null);
		LinearLayout llContent = (LinearLayout) view
				.findViewById(R.id.llContent);
		llContent.removeAllViews();
		TextView tvH = new TextView(ctx);
		tvH.setText(Html.fromHtml("<font color=\"#191919\"><Big>" + "नाम :"
				+ "</Big></font>" + edtName.getText()));
		tvH.setTextSize(18);
		tvH.setTextColor(Color.BLUE);
		TextView tvEdd = new TextView(ctx);
		if (dbreg)
			tvEdd.setText(Html.fromHtml("<font color=\"#191919\"><Big>"
					+ "जन्म की तारीख:"
					+ "</Big></font>"
					+ String.format(" %d-%s-%d", dtp.getDayOfMonth(),
							DBAdapter.hmstr[dtp.getMonth()], dtp.getYear())));
		else
			tvEdd.setText(Html.fromHtml("<font color=\"#191919\"><Big>"
					+ "आखिरी माहवारी:"
					+ "</Big></font>"
					+ String.format(" %d-%s-%d", dtp.getDayOfMonth(),
							DBAdapter.hmstr[dtp.getMonth()], dtp.getYear())));
		tvEdd.setTextSize(18);
		tvEdd.setTextColor(Color.BLUE);
		TextView tvHname = new TextView(ctx);
		tvHname.setText(Html.fromHtml("<font color=\"#191919\"><Big>"
				+ "पति का नाम:" + "</Big></font>"
				+ String.format("%s", edtHname.getText())));
		tvHname.setTextSize(18);
		tvHname.setTextColor(Color.BLUE);
		TextView tvMob = new TextView(ctx);
		tvMob.setText(Html.fromHtml("<font color=\"#191919\"><Big>"
				+ "मोबाइल: " + "</Big></font>"
				+ String.format("%s", edtMobile.getText())));
		tvMob.setTextSize(18);
		tvMob.setTextColor(Color.BLUE);
		TextView tvCaste = new TextView(ctx);
		tvCaste.setText(Html.fromHtml("<font color=\"#191919\"><Big>"
				+ "जाति: " + "</Big></font>"
				+ String.format("%s", spnCaste.getSelectedItem().toString())));
		tvCaste.setTextSize(18);
		tvCaste.setTextColor(Color.BLUE);

		TextView tvRel = new TextView(ctx);
		tvRel.setText(Html.fromHtml("<font color=\"#191919\"><Big>" + "धर्म:"
				+ "</Big></font>"
				+ String.format("%s", spnRel.getSelectedItem().toString())));
		tvRel.setTextSize(18);
		tvRel.setTextColor(Color.BLUE);

		llContent.addView(tvH);
		llContent.addView(tvEdd);
		llContent.addView(tvHname);
		llContent.addView(tvMob);
		llContent.addView(tvCaste);
		llContent.addView(tvRel);

		customDialog.setPositiveButton("हां",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						String dob = "", pdte = "", dor = "";
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						Calendar mc = Calendar.getInstance();
						dor = sdf.format(mc.getTime());
						mc.setTimeInMillis(dtp.getCalendarView().getDate());
						if (dbreg) {
							dob = sdf.format(mc.getTime());
							mc.add(Calendar.DATE, -277);
							pdte = sdf.format(mc.getTime());
						} else {
							pdte = sdf.format(mc.getTime());
							mc.add(Calendar.DATE, +277);
							dob = sdf.format(mc.getTime());
						}
						// String
						// pdte=String.format("%04d-%02d-%02d",dtp.getYear(),dtp.getMonth()+1,dtp.getDayOfMonth());
						String longMessage, url = "", actinfo;

						if (change) {
							if (modified) {
								if (!dbreg) {
									actinfo = "Pregnent registration changed "
											+ slno;
									mydb.logAct(actinfo);
								}
								// Id, name, lmp, hname, caste, rel, mobile
								int server_id = 0;
								Cursor serverdata = mydb.getServer_id(slno);
								if (serverdata.getCount() != 0) {
									if (serverdata.moveToFirst()) {
										server_id = serverdata.getInt(0);
									}
								}
								mydb.updatePreg(slno, edtName.getText()
										.toString(), pdte, edtHname.getText()
										.toString(), spnCaste
										.getSelectedItemPosition(), spnRel
										.getSelectedItemPosition(), edtMobile
										.getText().toString(), Integer
										.parseInt(uid), spnAwid
										.getSelectedItemId(), 0, server_id, "");
								if (image_flag == 0) {
									String imgfile = Environment
											.getExternalStorageDirectory()
											+ File.separator
											+ Workflow.bfolder
											+ "/pdata/" + server_id + ".jpg";
									File file = new File(imgfile);
									if (file.exists()) {
										if (file.delete()) {
											Log.e("delete", "sucess");
										}
									} else {
										String imgfile1 = Environment
												.getExternalStorageDirectory()
												+ File.separator
												+ Workflow.bfolder
												+ "/pdata/"
												+ server_id + ".jpg";
										File file1 = new File(imgfile1);
										if (file.exists()) {
											if (file1.exists()) {
												if (file1.delete()) {
													Log.e("delete", "sucess");
												}
											}
										}

									}
								}

								Toast.makeText(getApplicationContext(),
										"जानकारी बदल गयी", Toast.LENGTH_SHORT)
										.show();
								finish();
								// Toast.makeText(ctx,longMessage,
								// Toast.LENGTH_LONG).show();
								if (DBAdapter.send_sms)
									try {
										if (!dbreg) {
											JSONObject payload = new JSONObject();
											payload.put("aid", uid);
											payload.put("id", slno);
											payload.put("name", edtName
													.getText().toString());
											payload.put("lmp", pdte);
											payload.put("hname", edtHname
													.getText().toString());
											payload.put("caste", spnCaste
													.getSelectedItemPosition());
											payload.put("religion", spnRel
													.getSelectedItemPosition());
											payload.put("mobile", edtMobile
													.getText().toString());
											payload.put("awid",
													spnAwid.getSelectedItemId());
											payload.put("dor", dor);
											payload.put("image_flag",
													image_flag);
											payload.put("anm_flag", 1);
											payload.put("server_id", server_id);
											mydb.sendGPRS(
													AppVariable.API(mContext)
															+ "save.php",
													payload.toString(), 1);
										}
									} catch (Exception e) {
										Log.d("error", e.getMessage());
									}
								// longMessage=String.format("%s:PM:%d:%s:%s",pktHeader,slno,edtName.getText(),pdte);
								// if (send_sms) mydb.sendSMS(phoneNumber,
								// longMessage);
							} else {
								Toast.makeText(getApplicationContext(),
										"कुछ भी नही बदला गया",
										Toast.LENGTH_SHORT).show();
								finish();
							}

						} else {
							longMessage = String.format("%s:PA:%d:%s:%s",
									pktHeader, slno, edtName.getText(), pdte);
							mydb.insertPreg(edtName.getText().toString(), pdte,
									edtHname.getText().toString(), spnCaste
											.getSelectedItemPosition(), spnRel
											.getSelectedItemPosition(),
									edtMobile.getText().toString(), dbreg,
									Integer.parseInt(uid), spnAwid
											.getSelectedItemId(), 0, 0,
									eddvalue);// hero add eddvalue vlaue

							if (!dbreg)
								mydb.logAct(actinfo = "Pregnent registration saved "
										+ slno);
							if (DBAdapter.send_sms)
								try {

									if (!dbreg) {
										JSONObject payload = new JSONObject();
										payload.put("aid", uid);
										payload.put("id", slno);
										payload.put("name", edtName.getText()
												.toString());
										payload.put("lmp", pdte);
										payload.put("hname", edtHname.getText()
												.toString());
										payload.put("caste", spnCaste
												.getSelectedItemPosition());
										payload.put("religion", spnRel
												.getSelectedItemPosition());
										payload.put("mobile", edtMobile
												.getText().toString());
										payload.put("awid",
												spnAwid.getSelectedItemId());
										payload.put("dor", dor);
										payload.put("dbreg", dbreg);
										payload.put("image_flag", image_flag);
										payload.put("anm_flag", 1);
										payload.put("asha_awc_flag", 1);
										Webb webb = Webb.create();
										Log.e("SEND DATA", image_flag
												+ " VALUE");
										/*
										 * Response<JSONObject> response = webb
										 * .post(DBAdapter.resturl+"/preg")
										 * .body(payload) .ensureSuccess()
										 * .asJsonObject();
										 */

										mydb.sendGPRS(AppVariable.API(mContext)
												+ "save.php",
												payload.toString(), 0);
									}
									// register();
									if (!dbreg)
										Toast.makeText(getApplicationContext(),
												"पंजीकरण हो गया",
												Toast.LENGTH_SHORT).show();
									finish();
									// Toast.makeText(getApplicationContext(),
									// "Successfully uploaded",
									// Toast.LENGTH_SHORT).show();
								} catch (Exception e) {
									Log.d("error", e.getMessage());
									Toast.makeText(getApplicationContext(),
											e.getMessage(), Toast.LENGTH_SHORT)
											.show();
								}

							// --if (send_sms) mydb.sendSMS(phoneNumber,
							// longMessage);
						}
						if (dbreg) {

							Intent intent = new Intent(getApplicationContext(),
									Birth_reg_entry.class);
							intent.putExtra("id", slno);
							intent.putExtra("mname", edtName.getText()
									.toString());
							intent.putExtra("dbreg", true);
							intent.putExtra("dob", dob);
							intent.putExtra("change", false);
							startActivity(intent);
						}
						// finish();
					}

					/*
					 * public void onClick(DialogInterface arg0, int arg1) { //
					 * TODO Auto-generated method stub
					 * 
					 * String
					 * pdte=String.format("%04d-%02d-%02d",dtp.getYear(),dtp
					 * .getMonth()+1,dtp.getDayOfMonth()); String longMessage;
					 * if (change) { if (modified) {
					 * mydb.updatePreg(slno,edtName.getText().toString(), pdte);
					 * Toast
					 * .makeText(getApplicationContext(),"Details modified",
					 * Toast.LENGTH_SHORT).show();
					 * //Toast.makeText(ctx,longMessage,
					 * Toast.LENGTH_LONG).show();
					 * longMessage=String.format("%s:PM:%d:%s:%s"
					 * ,pktHeader,slno,edtName.getText(),pdte); if (send_sms)
					 * mydb.sendSMS(phoneNumber, longMessage); } else
					 * Toast.makeText
					 * (getApplicationContext(),"No change detected ..",
					 * Toast.LENGTH_SHORT).show();
					 * 
					 * } else {
					 * longMessage=String.format("%s:PA:%d:%s:%s",pktHeader
					 * ,slno,edtName.getText(),pdte);
					 * mydb.insertPreg(edtName.getText().toString(), pdte);
					 * Toast
					 * .makeText(getApplicationContext(),"Registration completed"
					 * , Toast.LENGTH_SHORT).show(); if (send_sms)
					 * mydb.sendSMS(phoneNumber, longMessage); } finish(); }
					 */

				});

		customDialog.setNegativeButton("ना",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub

					}
				});

		customDialog.setView(view);
		customDialog.show();
	}

	public void register() {
		final Context context = this;
		mRegisterTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// Register on our server
				// On server creates a new user
				PostDataToServer.register(context,
						edtName.getText().toString(), edtMobile.getText()
								.toString(), "4");
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				mRegisterTask = null;
			}

		};
		mRegisterTask.execute(null, null, null);

	}

}
