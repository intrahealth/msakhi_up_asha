package com.asha.mnewborncareasha;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Locale;

//import org.apache.http.impl.conn.DefaultClientConnection;

import org.json.JSONObject;

//import com.goebl.david.Response;
//import com.goebl.david.Webb;

import com.asha.mnewborncareasha.control.NumberPicker;

import android.app.Activity;
import android.app.AlertDialog;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
//import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
//import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.DatePicker;
import android.widget.TableRow;
//import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker.OnDateChangedListener;

public class Birth_reg_entry extends Activity {
	int slno = 0, p_cstat, p_mstat, p_pob;
	double p_weight;
	// private static int TAKE_PICTURE = 1;
	private DBAdapter mydb;
	// Typeface face;
	DatePicker dtp;
	RadioButton rbAbrt, rbStillBirth, rbLive;
	Boolean change = false, modified = false;
	NumberPicker np;
	Context mContext = this;
	// Spinner spnCstat,spnMstat;
	RadioButton rbHome, rbHosp, rbBoy, rbGirl;
	String hname, pname, plmp, mobile;// plmp,
	int caste, religion, aid;

	String q_msg, uid, p_sex = "B", dob = "";
	int server_id;
	Calendar mc;
	TextView tvMname, tvSlno;
	boolean pobSel = false, weightSel = false, statSel = false,
			gendSel = false, dbreg = false;

	// public static final String
	// hmstr[]={"जनवरी","फरवरी","मार�?च","अप�?रैल","मई","जून","ज�?लाई","अगस�?त","सितम�?बर","अक�?टूबर","नवम�?बर","दिसम�?बर"};
	// Add Hero
	EditText child_name;
	TableRow child_name_tblrow;
	// Add Hero
	Global global;
	String Childreg_flag = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.birth_reg_entry);

		// TextView tvHead=(TextView)findViewById(R.id.tvHead);
		// face=Typeface.createFromAsset(getAssets(),getString(R.string.hindi_font));
		// tvHead.setTypeface(face);
		// spnCstat=(Spinner)findViewById(R.id.spnCstat);
		// spnMstat=(Spinner)findViewById(R.id.spnMstat);

		// Add Hero
		global = (Global) getApplicationContext();
		child_name = (EditText) findViewById(R.id.child_name);
		child_name_tblrow = (TableRow) findViewById(R.id.child_name_tblrow);

		rbHome = ((RadioButton) findViewById(R.id.rbHome));

		rbHosp = ((RadioButton) findViewById(R.id.rbHosp));

		rbBoy = ((RadioButton) findViewById(R.id.rbBoy));

		rbGirl = ((RadioButton) findViewById(R.id.rbGirl));
		rbLive = ((RadioButton) findViewById(R.id.rbLive));

		np = ((NumberPicker) findViewById(R.id.npWeight));
		np.setValue(0.0);
		rbAbrt = (RadioButton) findViewById(R.id.rbAbort);
		rbStillBirth = (RadioButton) findViewById(R.id.rbStillBirth);

		// Add Hero
		if (global.getChild_Entry_flag() == 0) {
			rbAbrt.setVisibility(View.GONE);
			rbStillBirth.setVisibility(View.GONE);
		}

		final LinearLayout llPob = (LinearLayout) findViewById(R.id.llPob);
		final LinearLayout llGender = (LinearLayout) findViewById(R.id.llGender);
		final LinearLayout llWeight = (LinearLayout) findViewById(R.id.llWeight);
		rbLive.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					llPob.setVisibility(android.view.View.VISIBLE);
					llGender.setVisibility(android.view.View.VISIBLE);
					llWeight.setVisibility(android.view.View.VISIBLE);
					// Add Hero
					child_name_tblrow.setVisibility(View.VISIBLE);
				} else {
					llPob.setVisibility(android.view.View.INVISIBLE);
					llGender.setVisibility(android.view.View.INVISIBLE);
					llWeight.setVisibility(android.view.View.INVISIBLE);
					child_name_tblrow.setVisibility(View.GONE);
				}
			}
		});
		// ((TextView)findViewById(R.id.tvlblMname)).setTypeface(face);
		// ((TextView)findViewById(R.id.tvDob)).setTypeface(face);
		// ((TextView)findViewById(R.id.tvCstat)).setTypeface(face);
		// ((TextView)findViewById(R.id.tvMstat)).setTypeface(face);
		// ((TextView)findViewById(R.id.tvPob)).setTypeface(face);
		// ((Button)findViewById(R.id.btnSaveBirth)).setTypeface(face);
		// rbHome.setTypeface(face);
		// rbHosp.setTypeface(face);
		dtp = (DatePicker) findViewById(R.id.dtpDob);
		dtp.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

		tvMname = (TextView) findViewById(R.id.tvMname);
		tvSlno = (TextView) findViewById(R.id.slno);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			slno = extras.getInt("id");
			tvSlno.setText(String.valueOf(slno));
			tvMname.setText(extras.getString("mname"));
			change = extras.getBoolean("change");
			dbreg = extras.getBoolean("dbreg");
			dob = extras.getString("dob");
		}

		// mydb = new DBAdapter(this);
		mydb = DBAdapter.getInstance(getApplicationContext());
		/*
		 * try { //mydb.createDataBase(); mydb.openDataBase(); } catch
		 * (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */
		String actinfo = "New Birth registration " + String.valueOf(slno);
		addListenerOnButton();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		uid = prefs.getString("id", "1");
		modified = true;
		Calendar defCalendar = Calendar.getInstance();
		Cursor c = mydb.getPreg(slno);
		mc = Calendar.getInstance();
		String dt_str[];
		dt_str = c.getString(c.getColumnIndex("lmp")).split("\\-");
		mc.set(Integer.parseInt(dt_str[0]), Integer.parseInt(dt_str[1]),
				Integer.parseInt(dt_str[2]));
		// hero Add
		if (global.getChild_Entry_flag() == 0) {
			Childreg_flag = "N";
		}
		if (change) {
			actinfo = "Birth registration (change) " + String.valueOf(slno);
			modified = false;
			p_pob = c.getInt(c.getColumnIndex("pob"));
			p_sex = c.getString(c.getColumnIndex("sex"));
			p_weight = c.getFloat(c.getColumnIndex("weight"));
			p_cstat = c.getInt(c.getColumnIndex("c_stat"));
			p_mstat = c.getInt(c.getColumnIndex("m_stat"));
			pname = c.getString(c.getColumnIndex("name"));
			// Add Hero
			if (global.getChild_Entry_flag() == 1) {
				plmp = c.getString(c.getColumnIndex("lmp"));
			} else if (global.getChild_Entry_flag() == 0) {
				plmp = "";
			}
			// aid =c.getInt(c.getColumnIndex("aid"));
			hname = c.getString(c.getColumnIndex("hname"));
			mobile = c.getString(c.getColumnIndex("mobile"));
			caste = c.getInt(c.getColumnIndex("caste"));
			religion = c.getInt(c.getColumnIndex("religion"));
			// Add Hero
			String Child_name = c.getString(c.getColumnIndex("child_name"));
			if (global.getChild_Entry_flag() == 0) {
				Childreg_flag = "N";
			} else if (global.getChild_Entry_flag() == 1) {
				String flag_Value = c.getString(c
						.getColumnIndex("childreg_flag"));
				if (flag_Value == null || flag_Value == "") {
					Childreg_flag = flag_Value;
				} else {
					Childreg_flag = "M";
				}
			}

			// Add Hero
			child_name.setText(Child_name);

			System.out.println(p_pob);
			System.out.println(pname);
			// spnAwid.setSelection((((ArrayAdapter)spnAwid.getAdapter()).getPosition(c.getInt(c.getColumnIndex("aid")))));
			aid = c.getInt(c.getColumnIndex("aid"));
			// server_id = c.getInt(c.getColumnIndex("server_id"));
			System.out.println(server_id);
			np.setValue(p_weight);
			if (p_pob == 0) {
				rbHome.setChecked(true);
				rbHosp.setChecked(false);
			} else {
				rbHome.setChecked(false);
				rbHosp.setChecked(true);
			}
			if (p_sex.equals("B")) {
				rbBoy.setChecked(true);
				rbGirl.setChecked(false);
			} else {
				rbBoy.setChecked(false);
				rbGirl.setChecked(true);
			}
			if ((p_cstat == 1) && (p_mstat == 1)) {
				rbAbrt.setChecked(true);
				rbStillBirth.setChecked(false);
				llPob.setVisibility(android.view.View.INVISIBLE);
				child_name_tblrow.setVisibility(View.GONE);
				llGender.setVisibility(android.view.View.INVISIBLE);
			} else if (p_cstat == 1) {
				rbAbrt.setChecked(false);
				rbStillBirth.setChecked(true);
				llPob.setVisibility(android.view.View.INVISIBLE);
				child_name_tblrow.setVisibility(View.GONE);
				llGender.setVisibility(android.view.View.INVISIBLE);
			} else {
				rbAbrt.setChecked(false);
				rbStillBirth.setChecked(false);
				llPob.setVisibility(android.view.View.VISIBLE);
				child_name_tblrow.setVisibility(View.VISIBLE);
				llGender.setVisibility(android.view.View.VISIBLE);
			}

			dt_str = c.getString(c.getColumnIndex("dob")).split("\\-");
			defCalendar.set(Integer.parseInt(dt_str[0]),
					Integer.parseInt(dt_str[1]) - 1,
					Integer.parseInt(dt_str[2]));
		}
		if (dbreg) {
			dt_str = dob.split("\\-");
			defCalendar.set(Integer.parseInt(dt_str[0]),
					Integer.parseInt(dt_str[1]) - 1,
					Integer.parseInt(dt_str[2]));

		} else
			mydb.logAct(actinfo);

		dtp.init(defCalendar.get(Calendar.YEAR),
				defCalendar.get(Calendar.MONTH),
				defCalendar.get(Calendar.DAY_OF_MONTH),
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						modified = true;
						if (isDateInvalid(view)) {
							Toast.makeText(getBaseContext(), "गलत तारीख ",
									Toast.LENGTH_SHORT).show();
							// Calendar mCalendar = Calendar.getInstance();
							// view.init(mCalendar.get(Calendar.YEAR),
							// mCalendar.get(Calendar.MONTH),
							// mCalendar.get(Calendar.DAY_OF_MONTH), this);
						}
					}
				});
		dtp.setEnabled(!dbreg);
	}

	@Override
	public void onDestroy() {
		mydb.close();
		super.onDestroy();
	}

	private void addListenerOnButton() {

		Button btnSave = (Button) findViewById(R.id.btnSaveBirth);

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pobSel = rbHome.isChecked() || rbHosp.isChecked()
						|| rbAbrt.isChecked() || rbStillBirth.isChecked();
				gendSel = rbBoy.isChecked() || rbGirl.isChecked()
						|| rbAbrt.isChecked() || rbStillBirth.isChecked();
				// if (rbAbrt.isChecked()) {pobSel=true;gendSel=true;};
				if (!change && !(pobSel && gendSel))
					Toast.makeText(getBaseContext(), "सभी जानकारी भरे",
							Toast.LENGTH_SHORT).show();
				else if (dbreg || !isDateInvalid(dtp))
					openPreviewDialog();
				else
					Toast.makeText(getBaseContext(), "गलत तारीख  ",
							Toast.LENGTH_SHORT).show();
				// TODO Auto-generated method stub
				/*
				 * int mstat=0,cstat=0,pob; NumberPicker
				 * npWeight=(NumberPicker)findViewById(R.id.npWeight); double
				 * weight=npWeight.getValue(); String longMessage; Context
				 * ctx=arg0.getContext(); if (rbHosp.isChecked()) pob=1;else
				 * pob=0; //mstat=(int)spnMstat.getSelectedItemId();
				 * //cstat=(int)spnCstat.getSelectedItemId(); if (change) if
				 * (pob!=p_pob) modified=true; //if ((pob!=p_pob) ||
				 * (mstat!=p_mstat) || (cstat!=p_cstat)) modified=true;
				 * //EditText edtName=(EditText)findViewById(R.id.edtName); if
				 * (modified) { String
				 * pdte=String.format("%04d-%02d-%02d",dtp.getYear
				 * (),dtp.getMonth()+1,dtp.getDayOfMonth()); mydb.regChild(slno,
				 * pdte, pob, (float) weight, cstat,mstat); String
				 * pktHeader=getString
				 * (R.string.sms_prefix)+" "+getString(R.string
				 * .signature)+" "+uid; String
				 * phoneNumber=getString(R.string.smsno); if (change)
				 * longMessage
				 * =String.format("%s:CM:%d:%d:%d:%d:%s:",pktHeader,slno
				 * ,pob,mstat,cstat,pdte); else
				 * longMessage=String.format("%s:CA:%d:%d:%d:%d:%s:"
				 * ,pktHeader,slno,pob,mstat,cstat,pdte); if (send_sms)
				 * sendSMS(phoneNumber, longMessage);
				 * Toast.makeText(ctx,"जन�?म की सूचना बदल/सेव हो गयी",
				 * Toast.LENGTH_SHORT).show(); } finish();
				 */
			}
		});
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setMessage("सोच लीजिये! क्या आपको बाहर निकलना है?")
				.setCancelable(false)
				.setPositiveButton("हां",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								if (dbreg)
									mydb.deletePreg(slno);
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
	 * SmsManager sms = SmsManager.getDefault();
	 * sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI); }
	 */
	private boolean isDateInvalid(DatePicker tempView) {

		Calendar mCalendar = Calendar.getInstance();
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.set(tempView.getYear(), tempView.getMonth(),
				tempView.getDayOfMonth(), 0, 0, 0);
		if (tempCalendar.after(mCalendar) || tempCalendar.before(mc))
			return true;
		else
			return false;

	}

	void openPreviewDialog() {

		String pob, sex;
		NumberPicker npWeight = (NumberPicker) findViewById(R.id.npWeight);
		double weight = npWeight.getValue();
		Context ctx = getApplicationContext();
		if (rbHosp.isChecked())
			pob = "अस्पताल";
		else
			pob = "घर";
		if (rbBoy.isChecked())
			sex = "लड़का";
		else
			sex = "लड़की";
		// final String
		// pktHeader=getString(R.string.sms_prefix)+" "+getString(R.string.signature)+" "+uid;
		AlertDialog.Builder customDialog = new AlertDialog.Builder(
				Birth_reg_entry.this);
		customDialog.setTitle("ध्यान दें!");
		// Context ctx=getApplicationContext();
		LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.preg_infodlg, null);
		LinearLayout llContent = (LinearLayout) view
				.findViewById(R.id.llContent);
		llContent.removeAllViews();
		TextView tvH = new TextView(ctx);
		tvH.setText(Html.fromHtml("<font color=\"#191919\"><Big>" + "नाम :"
				+ "</Big></font>" + tvMname.getText()));
		tvH.setTextSize(18);
		tvH.setTextColor(Color.BLUE);
		TextView tvEdd = new TextView(ctx);
		tvEdd.setText(Html.fromHtml("<font color=\"#191919\"><Big>"
				+ "तारीख:"
				+ "</Big></font>"
				+ String.format(" %d-%s-%d", dtp.getDayOfMonth(),
						DBAdapter.mstr[dtp.getMonth()], dtp.getYear())));
		tvEdd.setTextSize(18);
		tvEdd.setTextColor(Color.BLUE);

		TextView tvPlace = new TextView(ctx);
		tvPlace.setText(Html.fromHtml("<font color=\"#191919\"><Big>"
				+ "जन्म का स्थान:" + "</Big></font>" + pob));
		tvPlace.setTextSize(18);
		tvPlace.setTextColor(Color.BLUE);
		TextView tvWeight = new TextView(ctx);
		tvWeight.setText(Html.fromHtml("<font color=\"#191919\"><Big>" + "वजन:"
				+ "</Big></font>" + String.format("%3.1f", weight)));
		tvWeight.setTextSize(18);
		tvWeight.setTextColor(Color.BLUE);
		TextView tvSex = new TextView(ctx);
		tvSex.setText(Html.fromHtml("<font color=\"#191919\"><Big>" + "परिणाम:"
				+ "</Big></font>" + sex));
		tvSex.setTextSize(18);
		tvSex.setTextColor(Color.BLUE);

		llContent.addView(tvH);
		llContent.addView(tvEdd);
		if (rbAbrt.isChecked())
			tvSex.setText(Html.fromHtml("<font color=\"#191919\"><Big>"
					+ "परिणाम:" + "</Big></font>" + "गर्भपात"));
		else if (rbStillBirth.isChecked())
			tvSex.setText(Html.fromHtml("<font color=\"#191919\"><Big>"
					+ "परिणाम:" + "</Big></font>" + " मृत शिशु"));
		else {
			llContent.addView(tvPlace);
			llContent.addView(tvWeight);
		}
		llContent.addView(tvSex);
		customDialog.setPositiveButton("हां",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub

						// String
						// pdte=String.format("%04d-%02d-%02d",dtp.getYear(),dtp.getMonth()+1,dtp.getDayOfMonth());
						// String longMessage;
						int mstat = 0, cstat = 0, pob;
						String sex, url = "", actinfo;
						String ChildName = "";
						String childreg_flag = Childreg_flag;
						ChildName = child_name.getText().toString();
						// childreg_flag

						float weight = (float) np.getValue();
						weight = (float) Math.round(weight * 10) / 10;
						Context ctx = getApplicationContext();
						if (rbHosp.isChecked())
							pob = 1;
						else
							pob = 0;
						if (rbBoy.isChecked())
							sex = "B";
						else
							sex = "G";
						if (rbAbrt.isChecked()) {
							mstat = 1;
							cstat = 1;
							if (cstat == 1 && mstat == 1) {
								ChildName = "";
							}
						} else if (rbStillBirth.isChecked())
							cstat = 1;
						if (cstat == 1) {
							ChildName = "";
						}

						// mstat=(int)spnMstat.getSelectedItemId();
						// cstat=(int)spnCstat.getSelectedItemId();
						if (change) // if (pob!=p_pob) modified=true;
							if ((pob != p_pob) || (p_sex != sex)
									|| (p_weight != weight)
									|| (p_cstat != cstat) || (p_mstat != mstat))
								modified = true;
						// EditText
						// edtName=(EditText)findViewById(R.id.edtName);

						if (modified) {
							String pdte = String.format(Locale.ENGLISH,
									"%04d-%02d-%02d", dtp.getYear(),
									dtp.getMonth() + 1, dtp.getDayOfMonth());
							mydb.regChild(slno, pdte, pob, weight, mstat,
									cstat, sex, ChildName, childreg_flag);
							// Add Hero Childname
							if (dbreg)
								actinfo = "Direct birth registration saved ";
							else
								actinfo = "Birth registration completed";
							mydb.logAct(actinfo);
							/*
							 * String
							 * pktHeader=getString(R.string.sms_prefix)+" "
							 * +getString(R.string.signature)+" "+uid; String
							 * phoneNumber=getString(R.string.smsno);
							 * 
							 * if (change) longMessage=String.format(
							 * "%s:CM:%d:%3.1f:%d:%s:%s:%s:%s"
							 * ,pktHeader,slno,weight,pob,pdte,sex,mstat,cstat);
							 * else longMessage=String.format(
							 * "%s:CA:%d:%3.1f:%d:%s:%s:%s:%s"
							 * ,pktHeader,slno,weight,pob,pdte,sex,mstat,cstat);
							 */
							// Toast.makeText(ctx,"pre message",
							// Toast.LENGTH_SHORT).show();
							if (DBAdapter.send_sms)
								try {
									if (!dbreg) {
										JSONObject payload = new JSONObject();
										payload.put("aid", uid);
										payload.put("id", slno);
										payload.put("weight", weight);
										payload.put("dob", pdte);
										payload.put("pob", pob);
										payload.put("sex", sex);
										payload.put("mstat", mstat);
										payload.put("cstat", cstat);
										// Add Hero
										payload.put("child_name", ChildName);
										payload.put("childreg_flag",
												childreg_flag);
										Cursor s = mydb.getServer_id(slno);
										if (s.moveToFirst()) {
											server_id = s.getInt(0);
										}
										payload.put("server_id", server_id);
										mydb.sendGPRS(AppVariable.API(mContext)
												+ "save.php",
												payload.toString(), 1);
									} else {
										int awid = 0;
										Cursor c = mydb.getALLpreg(slno);
										if (c.moveToFirst()) {
											p_pob = c.getInt(c
													.getColumnIndex("pob"));
											p_sex = c.getString(c
													.getColumnIndex("sex"));
											p_weight = c.getFloat(c
													.getColumnIndex("weight"));
											p_cstat = c.getInt(c
													.getColumnIndex("c_stat"));
											p_mstat = c.getInt(c
													.getColumnIndex("m_stat"));
											pname = c.getString(c
													.getColumnIndex("name"));
											// Add Hero
											if (global.getChild_Entry_flag() == 1) {
												plmp = c.getString(c
														.getColumnIndex("lmp"));
											} else if (global
													.getChild_Entry_flag() == 0) {
												plmp = "";
											}
											awid = c.getInt(c
													.getColumnIndex("awid"));
											hname = c.getString(c
													.getColumnIndex("hname"));
											mobile = c.getString(c
													.getColumnIndex("mobile"));
											caste = c.getInt(c
													.getColumnIndex("caste"));
											religion = c.getInt(c
													.getColumnIndex("religion"));

											// Add Hero
											ChildName = c.getString(c
													.getColumnIndex("child_name"));
											childreg_flag = c.getString(c
													.getColumnIndex("childreg_flag"));

										}
										SimpleDateFormat sdf = new SimpleDateFormat(
												"yyyy-MM-dd");
										Calendar mc = Calendar.getInstance();
										String dor = sdf.format(mc.getTime());
										JSONObject payload = new JSONObject();

										Log.i("aid", uid);
										Log.i("id", slno + "");
										Log.i("name", pname);
										Log.i("lmp", plmp);
										Log.i("hname", hname);
										Log.i("caste", caste + "");
										Log.i("religion", religion + "");
										Log.i("mobile", mobile);
										Log.i("awid", awid + "");
										Log.i("dor", dor);
										Log.i("dbreg", dbreg + "");
										Log.i("image_flag", 0 + "");
										Log.i("weight", weight + "");
										Log.i("dob", pdte);
										Log.i("pob", pob + "");
										Log.i("sex", sex + "");
										Log.i("mstat", mstat + "");
										Log.i("cstat", cstat + "");
										// Add hero
										Log.i("child_name", ChildName + "");
										Log.i("child_name", childreg_flag + "");

										payload.put("aid", uid);
										payload.put("id", slno);
										payload.put("name", pname);
										payload.put("lmp", plmp);
										payload.put("hname", hname);
										payload.put("caste", caste);
										payload.put("religion", religion);
										payload.put("mobile", mobile);
										payload.put("awid", awid);
										payload.put("dor", dor);
										payload.put("dbreg", dbreg);
										payload.put("image_flag", 0);
										payload.put("anm_flag", 1);
										payload.put("weight", weight);
										payload.put("dob", pdte);
										payload.put("pob", pob);
										payload.put("sex", sex);
										payload.put("mstat", mstat);
										payload.put("cstat", cstat);
										payload.put("asha_awc_flag", 1);
										// Add Hero
										payload.put("child_name", ChildName);
										payload.put("childreg_flag",
												childreg_flag);
										mydb.sendGPRS(AppVariable.API(mContext)
												+ "save.php",
												payload.toString(), 1);
									}
									Toast.makeText(ctx,
											"जन्म की सूचना बदल/सेव हो गयी",
											Toast.LENGTH_SHORT).show();
									/*
									 * Webb webb = Webb.create();
									 * url=DBAdapter.resturl
									 * +"/breg"+String.format("/%s/%d",
									 * uid,slno); Response<JSONObject> response
									 * = webb .put(url) .body(payload)
									 * .ensureSuccess() .asJsonObject();
									 * Toast.makeText(getApplicationContext(),
									 * response.toString(),
									 * Toast.LENGTH_SHORT).show();
									 */
								} catch (Exception e) {
									Log.d("error", e.getMessage());
									Toast.makeText(getApplicationContext(),
											url + e.getMessage(),
											Toast.LENGTH_LONG).show();
								}

							// if (send_sms) mydb.sendSMS(phoneNumber,
							// longMessage);

						}
						finish();
					}
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

}
