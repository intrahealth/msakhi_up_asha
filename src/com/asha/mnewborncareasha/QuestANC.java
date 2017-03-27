package com.asha.mnewborncareasha;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;

import com.asha.mnewborncareasha.control.NumberPicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.database.SQLException;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
//import android.telephony.SmsManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class QuestANC extends Activity {
	int id = 0, yind, nind, qtype = -1, y, n, grp = 0, attch_stat = 0, seq = 0,
			pid, pqid = 0, qind = 0;
	boolean branch = false, grp_finished = false, ans_clicked = true;

	// private static int TAKE_PICTURE = 1;
	// private int qind=0;
	private DBAdapter mydb;
	// Typeface face;
	Button btnQuest, btnBack, btnNext;
	RadioButton rbYes, rbNo;
	TextView tvNumInput;
	ImageView imgAnc;
	LinearLayout llNumInput, llspninput, llPans, llStopwatch, llBase;
	NumberPicker npNumInput;
	TextView tvHead;
	Spinner spnMchInput;
	ImageButton btnHelp;
	MediaPlayer mp, beep_mp;
	RadioGroup rg;
	List<String> mchlist;
	private ArrayAdapter<String> adapter;
	Cursor c;
	String ansstr = "", hv_str = "", lmp_str = "";
	StringBuilder gsumm = new StringBuilder("------");
	double ansarr[] = new double[90];
	// String
	// grpinfo[]={"f'k'kq ds tkap","ek¡ fd tkap","thok.kq laØe.k tkap","nLrjksx fd tkap",
	// "Lruiku lEcaf/kr tkap","Vhdkdj.k lEcaf/kr tkap"};
	Integer grpid[] = new Integer[] { 2, 6, 25, 32, 54, 60, 67, 75, 82, 91, 97,
			110, 120, 121, 129, 137, 144, 152, 158, 161 };
	ArrayList<Integer> grpqlist = new ArrayList<Integer>(Arrays.asList(grpid));
	String grpinfo[] = { "G1", "G2", "G3", "G4", "G4", "G5", "G6", "G7", "G8",
			"G9", "G10", "G11", "G12", "G13", "G14", "G15", "G16", "G17",
			"G18", "G19", "G20", "G21", "G22" };
	// String
	// vst_seq[]={"¼1½ igyh tkap","¼2½ nwljh tkap","¼3½ rhljh tkap","¼4½ pkSFkh tkap","¼5½ ikapoh tkap","¼6½ Ìëh tkap","¼7½ lkroh tkap"};
	// String
	// vst_seq[]={"1. First check-up","2. Second check-up","3. Third check-up","4. Fourth check-up","5. Fifth check-up","6. Sixth check-up","7. Seventh check-up"};
	// static final List<Integer> qlistdt =
	// Arrays.asList(10,11,12,13,14,15,16,17,18,20,21,22,23);
	// String
	// grpinfo[]={"शिशु की जांच","माँ की जांच","जीवाणु संक्रमण जांच","दस्त रोग की जांच","स्तनपान सम्बंधित जांच"};
	String vst_seq[] = { "1. पहली जाँच", "2. दूसरी जाँच", "3. तीसरी जाँच",
			"4. चौथी जाँच", "5. पांचवी जाँच", "6. छठी जाँच", "7. सातवी जाँच" };

	// if (fruit.contains(fruitname))
	Context ctx;
	int click_cnt = 1, last_sel = 0, qid = -1;
	boolean srun = false, stp_watch = false, cbranch = false, send_sms = false;
	boolean dtsel = false, inpval = false, dtvalid = true, mcpreg = false,
			spnval = false;
	private Button timeView;
	private DatePickerDialog dialog = null;
	private String initialDate;
	private String initialMonth;
	private String initialYear;
	private EditText edtInput = null;
	String mTimeFormat = "%02d:%02d:%02d", uid;
	char ccatgr = 'G';
	final int Date_Dialog_ID = 0;
	int cDay, cMonth, cYear;
	int sDay, sMonth, sYear;
	EditText etDate;
	private int ansind = 0, optflag = 0, mn = 0, mx = 9999, ttb = -1, tt1 = -1,
			tt2 = -1, hb = -1, ifa = -1, hbval = -1, ifaval = -1, mcp = -1;
	private long dtval = 0;
	Calendar lmpdt;
	private Anc_answr tansdata;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ancquest);
		tvHead = (TextView) findViewById(R.id.tvHead);
		npNumInput = (NumberPicker) findViewById(R.id.npNumInput);
		spnMchInput = (Spinner) findViewById(R.id.spnMchInput);
		llPans = (LinearLayout) findViewById(R.id.llPans);
		llStopwatch = (LinearLayout) findViewById(R.id.lldtp);
		llBase = (LinearLayout) findViewById(R.id.llBase);
		imgAnc = (ImageView) findViewById(R.id.imgAnc);
		LinearLayout llBase = (LinearLayout) findViewById(R.id.llBase);
		// face=Typeface.createFromAsset(getAssets(),getString(R.string.hindi_font));
		// tvHead.setTypeface(face);
		ctx = this;

		String mch = "One$Two$Three";
		// mchlist=new ArrayList(Arrays.asList(mch.split("\\$")));
		mchlist = new ArrayList<String>(Arrays.asList(mch.split("\\$")));
		// for(int i=0;i<90;i++) ansarr[i]=-1;

		btnQuest = (Button) findViewById(R.id.btnQuest);
		// btnQuest.setTypeface(face);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		uid = prefs.getString("id", "1");
		send_sms = prefs.getBoolean("send_sms", false);

		rg = (RadioGroup) findViewById(R.id.rdgpYesNo);
		rg.setVisibility(android.view.View.GONE);
		// ((RadioButton)findViewById(R.id.rbYes)).setTypeface(face);
		// ((RadioButton)findViewById(R.id.rbNo)).setTypeface(face);
		// Uri path = Uri.parse("android.resource://" + getPackageName() +
		// "/raw/h.3gp");

		beep_mp = MediaPlayer.create(getApplicationContext(), R.raw.beep7);
		try {
			beep_mp.prepare();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		;

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id = extras.getInt("id");
			hv_str = extras.getString("hv_str");
			seq = extras.getInt("seq");
			pid = extras.getInt("pid");
			lmp_str = extras.getString("lmp");

		}
		lmpdt = DBAdapter.getCalInstance();
		String dt_str[];
		dt_str = lmp_str.split("\\-");
		lmpdt.set(Integer.parseInt(dt_str[0]), Integer.parseInt(dt_str[1]) - 1,
				Integer.parseInt(dt_str[2]), 0, 0, 0);

		switch (seq) {
		case 1:
			mp = MediaPlayer.create(getApplicationContext(), R.raw.hv1);
			break;
		case 2:
			mp = MediaPlayer.create(getApplicationContext(), R.raw.hv2);
			break;
		case 3:
			mp = MediaPlayer.create(getApplicationContext(), R.raw.hv3);
			break;
		default:
			mp = MediaPlayer.create(getApplicationContext(), R.raw.hv1);
			break;
		}
		// if (!fvisit)
		mp.start();

		// if (learn)
		// llBase.setBackgroundResource(R.drawable.btn_default_normal_disable_focused);
		// else
		llBase.setBackgroundResource(R.color.clrOffwhite);
		// if (!fvisit)
		btnQuest.setText(vst_seq[seq - 1]);
		qtype = 3;
		// else btnQuest.setText("Optional visit");
		// TextView txtRowId=(TextView)findViewById(R.id.txtRowId);
		// txtRowId.setText(String.valueOf(slno));
		mydb = DBAdapter.getInstance(getApplicationContext());
		mydb.UpdateQlist(seq);

		optflag = mydb.getOptAnc(pid, seq);

		/*
		 * mydb = new DBAdapter(this); try { //mydb.createDataBase();
		 * mydb.openDataBase(); } catch (SQLException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		addListenerOnButton();
	}

	private void addListenerOnButton() {

		btnBack = (Button) findViewById(R.id.btnBack);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnHelp = (ImageButton) findViewById(R.id.btnHelp);
		rbYes = (RadioButton) findViewById(R.id.rbYes);
		rbNo = (RadioButton) findViewById(R.id.rbNo);
		// npNumInput=(NumberPicker)findViewById(R.id.npNumInput);
		llNumInput = (LinearLayout) findViewById(R.id.llnuminput);
		llspninput = (LinearLayout) findViewById(R.id.llspninput);
		tvNumInput = (TextView) findViewById(R.id.tvNumInput);
		llNumInput.setVisibility(android.view.View.INVISIBLE);
		llspninput.setVisibility(android.view.View.INVISIBLE);
		btnBack.setVisibility(android.view.View.INVISIBLE);
		btnNext.setBackgroundResource(R.drawable.custom_button_yellow);
		// tvNumInput.setTypeface(face);
		// btnNext.setTypeface(face);
		// btnBack.setTypeface(face);
		// rbYes.setTypeface(face);
		// rbNo.setTypeface(face);
		ansind = 0;
		qind = -1;
		qid = 0;
		mcp = 1;
		if (mydb.isAncReg(pid)) {
			/*
			 * tansdata=new Anc_answr();
			 * tansdata.dtm=System.currentTimeMillis(); tansdata.qtype=0;
			 * tansdata.optind=1; mydb.tansdata.put(1, tansdata);
			 */
			mcpreg = true;
			qid = 10;
			qind = mydb.getQind(qid) - 1;
		}
		// if (dstat==2) nqid=DBAdapter.qc[0];
		c = mydb.getNextQuestAnc(qid, branch, seq);
		yind = c.getColumnIndex("y_qid");
		nind = c.getColumnIndex("n_qid");
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mchlist);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnMchInput.setAdapter(adapter);
		spnMchInput.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// qtype 0 Yes/No, 1 NumberInput 2 Spinner 3 Label 4 Option with
				// date
				// 5 option with input
				ans_clicked = false;
				int sel = spnMchInput.getSelectedItemPosition();
				if (qtype == 2)
					ans_clicked = (sel > 0);
				else if (qtype > 3) {
					if (sel == last_sel + 1) {
						dtsel = false;
						dtvalid = false;
						inpval = false;
						spnval = true;
						llStopwatch.setVisibility(android.view.View.VISIBLE);
					} else {
						dtsel = true;
						dtvalid = true;
						inpval = true;
						spnval = (sel > 0);
						llStopwatch.setVisibility(android.view.View.INVISIBLE);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				dtsel = true;
				dtvalid = true;
				inpval = true;
				spnval = false;
				llStopwatch.setVisibility(android.view.View.INVISIBLE);
			}
		});
		/*
		 * spnMchInput.setOnTouchListener(new OnTouchListener() {
		 * 
		 * public boolean onTouch(View v, MotionEvent event) { // TODO
		 * Auto-generated method stub
		 * ans_clicked=(spnMchInput.getSelectedItemPosition()>0);
		 * 
		 * return false; }
		 * 
		 * });
		 */

		rbYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				click_cnt = 0;
				ans_clicked = true;
				btnNext.setBackgroundResource(R.drawable.custom_button_yellow);
				AssetFileDescriptor afd;
				try {
					mp.reset();
					afd = getAssets().openFd("snd/haan.3gp");
					mp.setDataSource(afd.getFileDescriptor(),
							afd.getStartOffset(), afd.getLength());
					mp.prepare();
					mp.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		rbNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				click_cnt = 0;
				ans_clicked = true;
				btnNext.setBackgroundResource(R.drawable.custom_button_yellow);
				AssetFileDescriptor afd;
				try {
					mp.reset();
					afd = getAssets().openFd("snd/nahin.3gp");
					mp.setDataSource(afd.getFileDescriptor(),
							afd.getStartOffset(), afd.getLength());
					mp.prepare();
					mp.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// if (qtype==0) click_cnt++;else click_cnt=2;
				// click_cnt++;
				// Play beep sound
				// ans_clicked=true;

				int tmpInp;
				if (qtype == 5 && !inpval && edtInput != null) {
					String inst = edtInput.getText().toString().trim();
					tmpInp = DBAdapter.strToint(inst);
					inpval = (tmpInp >= mn && tmpInp <= mx);
				}
				mp.reset();
				beep_mp.start();
				if ((npNumInput.changed) || (qtype == 3))
					ans_clicked = true;
				else if (qtype == 4 && dtsel && dtvalid && spnval)
					ans_clicked = true;
				else if (qtype == 5 && dtsel && dtvalid && inpval && spnval)
					ans_clicked = true;
				if (ans_clicked) {
					if (qtype == 3)
						click_cnt = 2;
					else
						click_cnt++;
					if (click_cnt == 1) {
						btnNext.setBackgroundResource(R.drawable.custom_button_green);

					} else if (click_cnt == 2)
						next_quest();
				} else
					Toast.makeText(getApplicationContext(),
							"आपने उत्तर नही दिया है", Toast.LENGTH_SHORT)
							.show();
			}
		});

		btnQuest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AssetFileDescriptor afd;
				try {
					mp.reset();
					afd = getAssets().openFd(
							"snd/" + String.valueOf(qid) + "ancvoice.3gp");
					mp.setDataSource(afd.getFileDescriptor(),
							afd.getStartOffset(), afd.getLength());
					mp.prepare();
					mp.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (llPans.getVisibility() == android.view.View.INVISIBLE) {
					String pans = mydb.getpans(pid, qid);
					String seq_arr[] = mydb.seq_str.split("\\ ");
					String pans_arr[] = pans.split("\\ ");
					llPans.removeAllViews();
					llPans.setBackgroundResource(R.drawable.custom_button_brown);
					TextView tvH = new TextView(ctx);
					tvH.setText(" Last visit: response");
					// tvH.setTypeface(face);
					tvH.setTextSize(15);
					llPans.addView(tvH);
					for (int i = 1; i < pans_arr.length; i++) {
						TextView tv = new TextView(ctx);
						tv.setTextSize(15);
						if (qtype == 0) {
							String st = " ";
							if (pans_arr[i].contentEquals("1"))
								st = "हाँ";
							else if (pans_arr[i].contentEquals("0"))
								st = "नहीं";
							tv.setText(seq_arr[i] + ". " + st);
							// tv.setTypeface(face);
						} else {
							tv.setText(seq_arr[i] + ") " + pans_arr[i]);
						}

						llPans.addView(tv);
					}
					llPans.setVisibility(android.view.View.VISIBLE);
				} else
					llPans.setVisibility(android.view.View.INVISIBLE);
			}
		});

		btnHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String movieurl = Environment.getExternalStorageDirectory()
						+ "/v3gp/" + String.valueOf(qid) + "qv.3gp";
				if (new File(movieurl.toString()).exists()) {
					mp.reset();
					Intent intentToPlayVideo = new Intent(Intent.ACTION_VIEW);
					intentToPlayVideo.setDataAndType(Uri.parse(movieurl),
							"video/*");
					startActivity(intentToPlayVideo);
				} else
					Toast.makeText(getApplicationContext(),
							"Resource not available..", Toast.LENGTH_SHORT)
							.show();
			}
		});

	}

	public void next_quest() {
		int qhid, jqid = -1;

		String qtext = "", dmsg = "";
		// String answrg0="",answrg1="",answrg2="",answrg3="",answrg4="";
		Boolean ended = false, oflag;
		// 0=good 1=not good 2=not atall
		click_cnt = 0; // reset click_count for next question
		ans_clicked = false;
		npNumInput.changed = false;
		Cursor ac;
		btnNext.setBackgroundResource(R.drawable.custom_button_yellow);
		llPans.setVisibility(android.view.View.INVISIBLE);
		llStopwatch.setVisibility(android.view.View.INVISIBLE);
		// dmsg="n:"+nqid+"p:"+pqid+"j:"+jqid;
		pqid = qid;
		// qid=mydb.qarr[nqid];
		// nqid++;
		// mp.reset();
		// decode answer
		// qtype 0 Yes/No, 1 NumberInput 2 Spinner 3 Label 4 Option with date
		// 5 option with input
		// int pansind=ansind;
		switch (qtype) {
		case 0:
			tansdata = new Anc_answr();
			// mydb.tansdata.put(mydb.qarr[qhid], value)
			// tansdata.qid=mydb.qarr[qhid];
			tansdata.dtm = System.currentTimeMillis();
			tansdata.qtype = qtype;
			if (rg.getCheckedRadioButtonId() == R.id.rbYes) {
				tansdata.optind = 1;
				if (y > 0)
					jqid = y;
				if (pqid == 1)
					mcpreg = true;
			} else {
				tansdata.optind = 0;
				if (n > 0)
					jqid = n;
			}
			mydb.tansdata.put(pqid, tansdata);

			break;
		case 1:
			tansdata = new Anc_answr();
			tansdata.dtm = System.currentTimeMillis();
			tansdata.qtype = qtype;
			tansdata.qnty = (int) npNumInput.getValue();
			mydb.tansdata.put(pqid, tansdata);
			break;
		case 2:
			tansdata = new Anc_answr();
			// tansdata.qid=qhid;
			tansdata.dtm = System.currentTimeMillis();
			tansdata.qtype = qtype;
			tansdata.optind = (int) spnMchInput.getSelectedItemId();
			mydb.tansdata.put(pqid, tansdata);
			break;
		case 4:
			tansdata = new Anc_answr();
			tansdata.dtm = System.currentTimeMillis();
			tansdata.qtype = qtype;
			tansdata.optind = (int) spnMchInput.getSelectedItemId();
			if (dtval > 0)
				tansdata.dte = dtval;
			mydb.tansdata.put(pqid, tansdata);
			break;
		case 5:
			tansdata = new Anc_answr();
			tansdata.dtm = System.currentTimeMillis();
			tansdata.qtype = qtype;
			tansdata.optind = (int) spnMchInput.getSelectedItemId();
			if (dtval > 0) {
				tansdata.qnty = DBAdapter.strToint(edtInput.getText()
						.toString());
				tansdata.dte = dtval;
			}
			mydb.tansdata.put(pqid, tansdata);
			break;
		}

		// Logic for ANC
		// tvHead.setText(pqid+" "+mcpreg);
		/*
		 * 1.No more children, 2. Yes, after few years, 3. Don’t Know
		 */
		/*
		 * · No more children - show s.nos 120 to 134, and EXIT [currently the
		 * messages 155,156 and 157 are also shown, INFINITE loop at the end] ·
		 * Yes, after few years - show s.nos 135 to 157, and EXIT [ INFINITE
		 * loop at the end] · Don’t Know - show s.nos 158 to 165, and EXIT
		 * [WORKING PROPERLY]
		 */

		try {
			if ((pqid == 9) && (!mcpreg))
				jqid = 54;
			else if ((pqid == 10) && (mydb.tansdata.get(pqid) != null)
					&& (mydb.tansdata.get(pqid).optind > 1)) {
				mcp = 0;
				jqid = 54;
			} else if (pqid == 119) {
				// if (mydb.tansdata.get(pqid).optind==1) jqid=120;
				if (mydb.tansdata.get(pqid).optind == 2)
					jqid = 135;
				else if (mydb.tansdata.get(pqid).optind == 3)
					jqid = 158;
			} else if ((pqid == 134) || (pqid == 157))
				jqid = 999; // exit condition
			if (jqid < 0)
				jqid = mydb.qarr[++qind];
			// Skip question if necessary
			// Q11,12,13 TT1 TT2 TT Booster
			if (jqid == 11) {
				ac = mydb.getAnsAnc(pid, jqid, "ans=2) and dtval is not null");
				if (ac.moveToFirst() && (!ac.isNull(0))) {
					jqid = mydb.qarr[++qind];
					tt1 = ac.getInt(ac.getColumnIndex("ans"));
				}
			}

			if (jqid == 12) {
				ac = mydb.getAnsAnc(pid, jqid, "ans=2) and dtval is not null");
				if (ac.moveToFirst() && (!ac.isNull(0))) {
					jqid = mydb.qarr[++qind];
					tt2 = ac.getInt(ac.getColumnIndex("ans"));
				}
			}

			if (jqid == 13) {
				ac = mydb.getAnsAnc(pid, jqid, "ans>1)");
				if (ac.moveToFirst() && (!ac.isNull(0))) {
					jqid = mydb.qarr[++qind];
					ttb = ac.getInt(ac.getColumnIndex("ans"));
				}
			}

			// Q14,15,16 HB test
			if ((jqid == 14) || (jqid == 15) || (jqid == 16)) {
				ac = mydb.getAnsAnc(pid, jqid, "ans=2) and dtval is not null");
				if (ac.moveToFirst() && (!ac.isNull(0))) {
					jqid = mydb.qarr[++qind];
					hb = ac.getInt(ac.getColumnIndex("ans"));
					hbval = ac.getInt(ac.getColumnIndex("qnty"));
				}
			}

			// Q17,18,19 IFA tablet
			if ((jqid == 17) || (jqid == 18) || (jqid == 19)) {
				ac = mydb.getAnsAnc(pid, jqid, "ans=2) and dtval is not null");
				if (ac.moveToFirst() && (!ac.isNull(0))) {
					jqid = mydb.qarr[++qind];
					ifa = ac.getInt(ac.getColumnIndex("ans"));
					ifaval = ac.getInt(ac.getColumnIndex("qnty"));
				}
			}

			if (jqid == 20) {
				if (ifa < 0)
					oflag = ((mydb.tansdata.get(17) != null) && (mydb.tansdata
							.get(17).qnty > 0))
							|| ((mydb.tansdata.get(18) != null) && (mydb.tansdata
									.get(18).qnty > 0))
							|| ((mydb.tansdata.get(19) != null) && (mydb.tansdata
									.get(19).qnty > 0));
				else
					oflag = ifaval > 0;
				// ac=mydb.getAnsAnc(17,
				// "qnty>0) or (qid=18 and qnty>0) or (qid=19 and qnty>0)");
				if (!oflag)
					jqid = mydb.qarr[++qind];
			}

			// Q21,22,23,24 ANC
			if ((jqid == 21) || (jqid == 22) || (jqid == 23) || (jqid == 24)) {
				ac = mydb.getAnsAnc(pid, jqid, "ans=2) and dtval is not null");
				if (ac.moveToFirst() && (!ac.isNull(0))) {
					jqid = mydb.qarr[++qind];
				}
			}

			if (jqid == 23) {
				ac = mydb.getAnsAnc(pid, jqid, "ans=2) and dtval is not null");
				if (ac.moveToFirst() && (!ac.isNull(0))) {
					jqid = mydb.qarr[++qind];
				}
			}

			if (jqid == 24) {
				ac = mydb.getAnsAnc(pid, jqid, "ans=2) and dtval is not null");
				if (ac.moveToFirst() && (!ac.isNull(0))) {
					jqid = mydb.qarr[++qind];
				}
			}

			if (jqid == 26) {
				// (If 11 = No) OR (If 11 = No,12=No)
				if (tt1 < 0)
					oflag = mydb.tansdata.get(11).optind == 2;
				else
					oflag = (tt1 == 2);
				// ac=mydb.getAnsAnc(11, "ans=1)");
				if (oflag)
					jqid = mydb.qarr[++qind];
			}
			if (jqid == 27) {
				// (If 11 = No) OR (If 11 = No,12=No)
				if (tt1 < 0)
					oflag = mydb.tansdata.get(11).optind == 2;
				else
					oflag = (tt1 == 2);
				// ac=mydb.getAnsAnc(11, "ans=1)");
				if (oflag)
					jqid = mydb.qarr[++qind];
			}
			if (jqid == 28) {
				// If 11 = Yes, 12=No
				oflag = ((tt1 == 2) && (tt2 == 1));
				if (!oflag)
					oflag = (((mydb.tansdata.get(11) != null) && (mydb.tansdata
							.get(11).optind == 2)) && ((mydb.tansdata.get(12) != null) && (mydb.tansdata
							.get(12).optind == 1)));
				// ac=mydb.getAnsAnc(12, "ans=1)");
				if (!oflag)
					jqid = mydb.qarr[++qind];
			}
			if (jqid == 31) {
				// (If 11 = Yes AND 12=YES) OR (13 = YES)
				oflag = (tt1 == 2) && (tt2 == 2) || (ttb == 2);
				if (!oflag)
					oflag = ((((mydb.tansdata.get(11) != null) && (mydb.tansdata
							.get(11).optind == 2)) && ((mydb.tansdata.get(12) != null) && (mydb.tansdata
							.get(12).optind == 2))) || ((mydb.tansdata.get(13) != null) && (mydb.tansdata
							.get(13).optind == 2)));
				// ac=mydb.getAnsAnc(13,
				// "ans=0) and ((qid=11 and ans=0) or (qid=12 and ans=0))");
				if (!oflag)
					jqid = mydb.qarr[++qind];
			}
			if (jqid == 33) {
				// if Hb<11
				if (hb < 0)
					oflag = (((mydb.tansdata.get(14) != null)
							&& (mydb.tansdata.get(14).optind == 2) && (mydb.tansdata
							.get(14).qnty < 11))
							|| ((mydb.tansdata.get(15) != null)
									&& (mydb.tansdata.get(15).optind == 2) && (mydb.tansdata
									.get(15).qnty < 11)) || ((mydb.tansdata
							.get(16) != null)
							&& (mydb.tansdata.get(16).optind == 2) && (mydb.tansdata
							.get(16).qnty < 11)));
				else
					oflag = ((hb == 2) && (hbval < 11));
				// ac=mydb.getAnsAnc(14,
				// "qnty>10) and (qid=15 and qnty>10) and (qid=16 and qnty>10)");
				if (!oflag)
					jqid = 40;
			}

			if (jqid == 40) {
				// if Hb<11
				if (hb < 0)
					oflag = ((mydb.tansdata.get(14) != null) && (mydb.tansdata
							.get(14).qnty >= 11))
							|| ((mydb.tansdata.get(15) != null)
									&& (mydb.tansdata.get(15).qnty >= 11) || ((mydb.tansdata
									.get(16) != null) && (mydb.tansdata.get(16).qnty >= 11)));
				else
					oflag = (hbval >= 11);
				// ac=mydb.getAnsAnc(14,
				// "qnty>10) and (qid=15 and qnty>10) and (qid=16 and qnty>10)");
				if (pqid == 39)
					jqid = 60;
				else if (!oflag)
					jqid = 46;
			}

			if (jqid == 46) {
				// ac=mydb.getAnsAnc(pid,14,
				// "qnty is null) and (qid=15 and qnty is null) and (qid=16 and qnty is null)");
				if (pqid == 45)
					jqid = 60;
				else if (hb == 1)
					jqid = 54;
				// else if (ac.moveToFirst() && (!ac.isNull(0))) jqid=54;
			}

			if (jqid == 54) {
				if (mcp == 1)
					jqid = 60;
			}

		} catch (Exception e) {
			Log.d("info", "err:" + e.getMessage());
		}

		/*
		 * if (qtype==0) { if (rg.getCheckedRadioButtonId()==R.id.rbYes)
		 * {ansarr[qid]=1;if (y>0) nqid=y;} else {ansarr[qid]=0;if (n>0)
		 * nqid=n;} } else if (qtype==1) { ansarr[qid]=npNumInput.getValue(); }
		 * else if (qtype==2) {
		 * ansarr[qid]=(int)spnMchInput.getSelectedItemId(); }
		 */

		// double tval=npNumInput.getValue();
		/*
		 * if (tmp_qid==28) if (tval>=4) nqid=29;else; else if (tmp_qid==30) if
		 * (tval<=5) nqid=31;else; else if (tmp_qid==32) { if
		 * ((tval>98.6)&&(tval<101.9)) nqid=33;else if (tval<=98.6) nqid=34;}
		 * else if (tmp_qid==37) if (rg.getCheckedRadioButtonId()==R.id.rbNo)
		 * nqid=38;else;
		 */
		// if (jqid>0) nqid=jqid;
		qind = mydb.getQind(jqid);
		ended = (qind < 0) || (qind >= mydb.anc_qcnt - 1);
		if (!ended) {
			qid = jqid;
			c = mydb.getNextQuestAnc(qid, branch, seq);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		if ((!ended) && (c.moveToFirst())) {
			AssetFileDescriptor afd;
			try {
				afd = getAssets().openFd(
						"snd/" + String.valueOf(qid) + "ancvoice.3gp");
				mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
						afd.getLength());
				mp.prepare();
				mp.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// qtext=String.valueOf(nqid)+") "+c.getString(c.getColumnIndex("qtext"));
			// if (qhid>0)
			// pqid=mydb.qarr[qhid-1];
			// pqids=Integer.toString(pqid);
			// else pqids="*";
			qhid = qid;
			int tmp_ind = c.getColumnIndex("qvid");
			if (!c.isNull(tmp_ind))
				qhid = c.getInt(tmp_ind);
			String qanc = c.getString(c.getColumnIndex("qtext"));
			qtext = String.valueOf(qid) + ") " + qanc;
			qtype = c.getInt(c.getColumnIndex("q_type"));
			// dmsg=dmsg+"i "+qind+"p "+pqid+"j "+jqid+"qc "+mydb.anc_qcnt;

			// tvHead.setText(dmsg);
			tvHead.setText(grpinfo[c.getInt(c.getColumnIndex("grp"))]);
			// mp.reset();
			if (!c.isNull(yind))
				y = c.getInt(yind);
			else
				y = -1;
			if (!c.isNull(nind))
				n = c.getInt(nind);
			else
				n = -1;
			btnQuest.setText(qtext);
			// btnQuest.setText(qtext+Integer.toString(pqid)+":"+Integer.toString(ansind)+
			// ":"+Integer.toString(pansind));
			if (c.getInt(c.getColumnIndex("help")) == 1)
				btnHelp.setVisibility(android.view.View.VISIBLE);
			else
				btnHelp.setVisibility(android.view.View.GONE);
			if (c.getInt(c.getColumnIndex("pans")) == 1)
				btnBack.setVisibility(android.view.View.VISIBLE);
			else
				btnBack.setVisibility(android.view.View.INVISIBLE);

			// if (qtype!=0) ans_clicked=true;
			double def = c.getDouble(c.getColumnIndex("def"));
			if (def == 0) {
				// rbNo.setChecked(true);
				rbNo.setTextColor(Color.parseColor("#669900"));
				rbYes.setTextColor(Color.RED);
			} else {
				// rbYes.setChecked(true);
				rbYes.setTextColor(Color.parseColor("#669900"));
				rbNo.setTextColor(Color.RED);
			}
			// Load the image
			String imgfile = Environment.getExternalStorageDirectory()
					+ File.separator + Workflow.bfolder + "/anc/.coimg/"
					+ String.valueOf(qid) + "ao.jpg";
			Bitmap bmp = BitmapFactory.decodeFile(imgfile);
			imgAnc.setImageBitmap(bmp);
			// qtype 0 Yes/No, 1 NumberInput 2 Spinner 3 Label 4 Option with
			// date 5 option with input
			switch (qtype) {
			case 0: { // 0 Yes/No
				// rbNo.setChecked(false);rbYes.setChecked(false);
				rg.clearCheck();
				btnQuest.setBackgroundResource(R.drawable.custom_button_lblue);
				rg.setVisibility(android.view.View.VISIBLE);
				llNumInput.setVisibility(android.view.View.GONE);
				llspninput.setVisibility(android.view.View.GONE);
			}
				;
				break;
			case 1: { // 1 NumberInput
				btnQuest.setBackgroundResource(R.drawable.custom_button_lblue);
				llNumInput.setVisibility(android.view.View.VISIBLE);
				rg.setVisibility(android.view.View.GONE);
				llspninput.setVisibility(android.view.View.GONE);
				tvNumInput.setText(c.getString(c.getColumnIndex("oinfo")));
				npNumInput.setValue(def);
				// if (nqid==8) npNumInput.setValue(0); //2.5
				if ((qid == 9) || (qid == 31))
					npNumInput.setIncrVal(0.1);
				else if ((qid == 27) || (qid == 29)) {
					npNumInput.setValue(0);
					npNumInput.setIncrVal(1);
				}
				// else if (nqid==29)
				// {npNumInput.setValue(0);npNumInput.setIncrVal(1);}
				// else if (nqid==63)
				// {npNumInput.setValue(0);npNumInput.setIncrVal(1);}
				// else if (nqid==67)
				// {npNumInput.setValue(0);npNumInput.setIncrVal(1);}
			}
				;
				break;
			case 2: { // 2 Spinner
				btnQuest.setBackgroundResource(R.drawable.custom_button_lblue);
				llspninput.setVisibility(android.view.View.VISIBLE);
				rg.setVisibility(android.view.View.GONE);
				llNumInput.setVisibility(android.view.View.GONE);
				if (!c.isNull(c.getColumnIndex("oinfo"))) {
					String mstr = c.getString(c.getColumnIndex("oinfo"));
					String mcharr[] = mstr.split("\\$");
					mchlist.clear();
					last_sel = mcharr.length - 1;
					mchlist.add("चुनिए");
					for (int i = 0; i <= last_sel; i++)
						mchlist.add(mcharr[i]);
					// mchlist.addAll(mstr.split("\\$"));
					// mchlist=new ArrayList(Arrays.asList(mstr.split("\\$")));
					adapter.notifyDataSetChanged();
					spnMchInput.setSelection(0);
					// llspninput.setSelected(false);
					// Toast.makeText(getApplicationContext(),"List changed",
					// Toast.LENGTH_SHORT).show();
				}
			}
				;
				break;
			case 4:
			case 5: { // 4 Option with date
						// 5 Option with input and date
				dtval = 0;
				btnQuest.setBackgroundResource(R.drawable.custom_button_lblue);
				llspninput.setVisibility(android.view.View.VISIBLE);
				rg.setVisibility(android.view.View.GONE);
				llNumInput.setVisibility(android.view.View.GONE);
				TextView tvQprompt = null, tvDprompt = null;
				edtInput = null;
				spnval = false;
				tvDprompt = new TextView(ctx);
				tvDprompt.setText("Date of " + qanc);
				tvDprompt.setTextSize(15);
				if (!c.isNull(c.getColumnIndex("oinfo"))) {
					String mstr = c.getString(c.getColumnIndex("oinfo"));
					String valmsg = "";
					if (qtype == 5) {
						if (qid == 14 || qid == 15 || qid == 16) {
							mn = 4;
							mx = 15;
							valmsg = String.format(Locale.ENGLISH, "%d-%d", mn,
									mx);
						} else if (qid == 17 || qid == 18 || qid == 19) {
							mn = 1;
							mx = 100;
							valmsg = String.format(Locale.ENGLISH, "%d-%d", mn,
									mx);
						}

						String oprompt = c.getString(c
								.getColumnIndex("oprompt"));
						;
						tvQprompt = new TextView(ctx);
						tvQprompt.setText(oprompt + valmsg);
						tvQprompt.setTextSize(15);
						edtInput = new EditText(ctx);
						edtInput.setSingleLine();
						edtInput.setInputType(InputType.TYPE_CLASS_NUMBER);

					}
					String mcharr[] = mstr.split("\\$");
					mchlist.clear();
					last_sel = mcharr.length - 1;
					mchlist.add("चुनिए");
					for (int i = 0; i <= last_sel; i++)
						mchlist.add(mcharr[i]);
					// mchlist.addAll(mstr.split("\\$"));
					// mchlist=new ArrayList(Arrays.asList(mstr.split("\\$")));
					adapter.notifyDataSetChanged();
					spnMchInput.setSelection(0);
					// llspninput.setSelected(false);
					// Toast.makeText(getApplicationContext(),"List changed",
					// Toast.LENGTH_SHORT).show();
					// Display date input button
					llStopwatch.removeAllViews();
					timeView = new Button(ctx);
					timeView.setBackgroundResource(R.drawable.custom_button_blue);
					// Calendar cal = Calendar.getInstance();
					// dtval=cal.getTimeInMillis();
					// SimpleDateFormat df = new
					// SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
					// timeView.setText(df.format(cal.getTime()));
					timeView.setText("दिनाक");
					dtsel = false;
					timeView.setTextSize(25);

					timeView.setTextColor(Color.WHITE);
					timeView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Calendar dtTxt = null;

							String preExistingDate = timeView
									.getText().toString();

							if (preExistingDate != null
									&& preExistingDate.contains("/")) {
								StringTokenizer st = new StringTokenizer(
										preExistingDate, "/");
								initialDate = st.nextToken();
								initialMonth = st.nextToken();
								initialYear = st.nextToken();
								if (dialog == null)
									dialog = new DatePickerDialog(v
											.getContext(), new PickDate(),
											Integer.parseInt(initialYear),
											Integer.parseInt(initialMonth) - 1,
											Integer.parseInt(initialDate));
								dialog.updateDate(
										Integer.parseInt(initialYear),
										Integer.parseInt(initialMonth) - 1,
										Integer.parseInt(initialDate));

							} else {
								dtTxt = Calendar.getInstance();
								// [int md=dtTxt.
								if (dialog == null)
									dialog = new DatePickerDialog(v
											.getContext(), new PickDate(),
											dtTxt.get(Calendar.YEAR), dtTxt
													.get(Calendar.MONTH), dtTxt
													.get(Calendar.DATE));
								dialog.updateDate(dtTxt.get(Calendar.YEAR),
										dtTxt.get(Calendar.MONTH),
										dtTxt.get(Calendar.DATE));
							}

							dialog.show();
						}
					});
					if (tvQprompt != null)
						llStopwatch.addView(tvQprompt);
					if (edtInput != null)
						llStopwatch.addView(edtInput);
					llStopwatch.addView(tvDprompt);
					llStopwatch.addView(timeView);
					// llStopwatch.setVisibility(android.view.View.VISIBLE);
				}
			}
				;
				break;
			default: { // 3 label
				qtype = 3;
				btnQuest.setBackgroundResource(R.drawable.btn_default_normal_disable_focused);
				llNumInput.setVisibility(android.view.View.GONE);
				llspninput.setVisibility(android.view.View.GONE);
				rg.setVisibility(android.view.View.GONE);
				btnBack.setVisibility(android.view.View.INVISIBLE);
			}
				;
				break;
			} // end of switch case

			if (grpqlist.contains(qid)) {
				llBase.setBackgroundResource(R.color.clrGrpmsg);
				btnQuest.setTextAppearance(ctx, R.style.boldText);
				btnQuest.setBackgroundResource(R.color.clrOffwhite);
			} else {
				llBase.setBackgroundResource(R.color.clrOffwhite);
				btnQuest.setTextAppearance(ctx, R.style.normalText);
				btnQuest.setBackgroundResource(R.drawable.btn_default_normal_lblue);
			}

		} else {
			// Ending No more questions are left
			// Calculate group summary
			/*
			 * Anchomevisit: [ { OrderDate: "9/18/1996", OrderID: 10308,
			 * ProductsInBasket: [ { ProductID: 69, ProductName:
			 * "Gudbrandsdalsost", Quantity: 1, UnitPrice: 36 }, { ProductID:
			 * 70, ProductName: "Outback Lager", Quantity: 5, UnitPrice: 15 } ]
			 * }
			 */
			// StringBuilder mhv_str=new StringBuilder(hv_str);
			// mhv_str.setCharAt(seq-1, '1');
			// Store answer array to tables

			try {
				JSONObject payload = new JSONObject();
				JSONArray answr = new JSONArray();
				payload.put("aid", uid);
				payload.put("id", pid);
				payload.put("server_id", mydb.getServer_idValue(pid));
				Log.e("TRI VALUE", seq + "");
				Log.e("opt VALUE", optflag + "");
				payload.put("tri", seq);
				payload.put("oflag", optflag);
				String sql = "INSERT INTO ans_anc(qid,tri,optflag,ans,qnty,dtval,dtm,pid)"
						+ " values(?, ?, ?, ?, ?, ?, ?, ?)";
				SQLiteStatement stmt = mydb.compileStatement(sql);
				Enumeration<Integer> qids = mydb.tansdata.keys();
				boolean ansflag = false;
				while (qids.hasMoreElements()) {
					// JSONObject tmpobj = new JSONObject();
					ansflag = true;
					JSONArray tmpobj = new JSONArray();
					int tqid = qids.nextElement();
					tansdata = mydb.tansdata.get(tqid);
					stmt.bindLong(1, tqid);
					stmt.bindLong(2, seq);
					stmt.bindLong(3, optflag);
					stmt.bindLong(4, tansdata.optind);
					// tmpobj.put("qnty", tansdata.qnty);
					if (tansdata.qtype == 5)
						stmt.bindLong(5, tansdata.qnty);
					else
						stmt.bindNull(5);
					if ((tansdata.qtype == 5) || (tansdata.qtype == 4))
						stmt.bindLong(6, tansdata.dte);
					else
						stmt.bindNull(6);
					tmpobj.put(tqid);
					tmpobj.put(tansdata.optind);
					tmpobj.put(tansdata.qnty);
					tmpobj.put(tansdata.dte);
					tmpobj.put(tansdata.dtm);
					answr.put(tmpobj);
					stmt.bindLong(7, tansdata.dtm);
					stmt.bindLong(8, mydb.getServer_idValue(pid));
					stmt.execute();
					stmt.clearBindings();
				}
				payload.put("answers", answr);
				if (ansflag && DBAdapter.send_sms)
					mydb.sendGPRS(AppVariable.API_INDEX(this) + "/ancv",
							payload.toString(), 1);
			} catch (Exception e) {
				Log.d("error", e.getMessage());
				// Toast.makeText(getApplicationContext(),e.getMessage(),
				// Toast.LENGTH_LONG).show();
			}

			// Calendar c = Calendar.getInstance();
			// SimpleDateFormat df = new
			// SimpleDateFormat("yyyy-MM-dd HH.mm",Locale.getDefault());
			// String pdte = df.format(c.getTime());
			// String
			// pktHeader=getString(R.string.sms_prefix)+" "+getString(R.string.signature)+" "+uid;
			// String longMessage,longMessage1;
			// --mydb.updatePregVisitStr(pid, mhv_str.toString());
			// --mydb.updateVisit(id, answrg0, answrg1, answrg2, answrg3,
			// answrg4,gsumm.toString());
			// longMessage=String.format(Locale.getDefault(),"%s:H1:%d:%d:%s:%s:%s:%s",pktHeader,pid,seq,pdte,answrg0,answrg1,gsumm);
			// longMessage1=String.format(Locale.getDefault(),"%s:H2:%d:%d:%s:%s:%s:%s",pktHeader,pid,seq,pdte,answrg2,answrg3,answrg4);
			/*
			 * String phoneNumber=getString(R.string.smsno); if (send_sms) {
			 * mydb.sendSMS(phoneNumber, longMessage); mydb.sendSMS(phoneNumber,
			 * longMessage1); }
			 */
			Toast.makeText(getApplicationContext(), "Questions over",
					Toast.LENGTH_SHORT).show();
			btnNext.setVisibility(android.view.View.INVISIBLE);
			Intent intentSumm = new Intent(ctx, AVisit_summary.class);
			intentSumm.putExtra("id", id);
			intentSumm.putExtra("hv_str", hv_str);
			intentSumm.putExtra("seq", seq);
			intentSumm.putExtra("pid", pid);
			intentSumm.putExtra("bflag", false);
			ctx.startActivity(intentSumm);
			// if (intent!=null) ctx.startActivity(intent);
			mp.reset();
			mp.release();
			beep_mp.release();
			finish();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private class PickDate implements DatePickerDialog.OnDateSetListener {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			Calendar cal = DBAdapter.getCalInstance();
			view.updateDate(year, monthOfYear, dayOfMonth);
			cal.set(year, monthOfYear, dayOfMonth);
			dtval = cal.getTimeInMillis();
			dtvalid = !isDateInvalid(view);
			timeView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
			dtsel = true;
			dialog.hide();
		}

	}

	private boolean isDateInvalid(DatePicker tempView) {

		Calendar mCalendar = DBAdapter.getCalInstance();
		Calendar tempCalendar = DBAdapter.getCalInstance();
		tempCalendar.set(tempView.getYear(), tempView.getMonth(),
				tempView.getDayOfMonth(), 0, 0, 0);
		// Toast.makeText(getApplicationContext(),String.valueOf(mCalendar.getTimeInMillis())+" "+String.valueOf(tempCalendar.getTimeInMillis()),
		// Toast.LENGTH_LONG).show();
		if (tempCalendar.after(mCalendar) || tempCalendar.before(lmpdt))
			return true;
		else
			return false;

	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setMessage("सोच लीजिये! क्या आपको बाहर निकलना है")
				.setCancelable(false)
				.setPositiveButton("हां",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								mp.reset();
								c.close();
								mydb.close();
								finish();
							}
						}).setNegativeButton("ना", null).show();
	}

}
