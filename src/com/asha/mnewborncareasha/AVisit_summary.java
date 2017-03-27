package com.asha.mnewborncareasha;

//import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
//import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AVisit_summary extends Activity {
	int id = 0, yind, nind, nqid, qtype = -1, y, n, grp = 0, attch_stat = 0,
			seq = 0, pid, pqid = 0, dstat = 0;
	boolean bflag = false;
	// boolean bflag=false,learn=false,fvisit=false;
	// private static int TAKE_PICTURE = 1;
	// private int qind=0;
	private DBAdapter mydb;
	// Typeface face;
	Button btnBack, btnNext;
	TextView tvHead;
	Cursor c;
	String hv_str = "", lmp_str, edd = "";
	// String
	// grpinfo[]={"f'k'kq ds tkap","ek¡ fd tkap","thok.kq laØe.k tkap","nLrjksx fd tkap",
	// "Lruiku lEcaf/kr tkap","Vhdkdj.k lEcaf/kr tkap"};
	// String grpinfo[]={"शिश�? की जांच", "मां की जांच",
	// "जीवाण�? संक�?रमण जांच", "दस�?तरोग की जांच",
	// "स�?तनपान सम�?बन�?धित जांच",""};
	String grpinfo[] = { "Newborn check-up", "Mother's check-up",
			"Bacterial infection check-up", "Diarrhea check-up",
			"BreastFeeding check-up", "" };

	// String
	// vst_seq[]={"¼1½ igyh tkap","¼2½ nwljh tkap","¼3½ rhljh tkap","¼4½ pkSFkh tkap","¼5½ ikapoh tkap","¼6½ Ìëh tkap","¼7½ lkroh tkap"};
	String vst_seq[] = { "1. पहली जांच", "2. दूसरी जांच", "3. तीसरी जांच",
			"4. चौथी जांच", "5. पांचवी जांच", "6. छठी जांच", "7. सातवीं जांच" };
	// String
	// vst_seq[]={"1. First check-up","2. Second check-up","3. Third check-up","4. Fourth check-up","5. Fifth check-up","6. Sixth check-up","7. Seventh check-up"};

	Context ctx;
	LinearLayout ll;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.visit_summary);
		ll = (LinearLayout) findViewById(R.id.llSummary);
		tvHead = (TextView) findViewById(R.id.tvHead);
		// face=Typeface.createFromAsset(getAssets(),getString(R.string.hindi_font));
		// tvHead.setTypeface(face);
		ctx = this;

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id = extras.getInt("id");
			hv_str = extras.getString("hv_str");
			seq = extras.getInt("seq");
			if (seq < 1)
				seq = 1;
			pid = extras.getInt("pid");
			bflag = extras.getBoolean("bflag");
			lmp_str = extras.getString("lmp");
			edd = extras.getString("edd");

		}
		tvHead.setText(vst_seq[seq - 1]);
		// if (fvisit) tvHead.setText("Optional check-up");
		// TextView txtRowId=(TextView)findViewById(R.id.txtRowId);
		// txtRowId.setText(String.valueOf(slno));
		mydb = DBAdapter.getInstance(getApplicationContext());
		/*
		 * mydb = new DBAdapter(this); try { //mydb.createDataBase();
		 * mydb.openDataBase(); } catch (SQLException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		addListenerOnButton();
	}

	private String dtconvert(long n) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy",
				Locale.ENGLISH);
		Date resultdate = new Date(n);
		return sdf.format(resultdate);
	}

	private void addListenerOnButton() {

		btnBack = (Button) findViewById(R.id.btnBack);
		btnNext = (Button) findViewById(R.id.btnNext);
		TextView tvPname = (TextView) findViewById(R.id.tvPname);
		TextView tvHeadVisit = (TextView) findViewById(R.id.tvHeadVisits);
		// btnBack.setVisibility(android.view.View.INVISIBLE);
		// tvHeadVisit.setTypeface(face);
		tvHeadVisit.setText("पिछले गृहभ्रमण के दौरान ");
		btnBack.setVisibility(android.view.View.VISIBLE);
		c = mydb.getAVisitSummary(pid, seq);
		// else { tvHeadVisit.setText("Summary (This visit)");
		// btnBack.setVisibility(android.view.View.INVISIBLE);c=mydb.getVisitSummary(pid,fvisit);
		// }
		// btnNext.setTypeface(face);
		// btnBack.setTypeface(face);
		nqid = 0;
		Calendar calender = Calendar.getInstance();
		Date date = calender.getTime();
		if (c.moveToFirst()) {
			tvPname.setText("जाँच की तारीख :" + date.toString() + "");
			// Add the edd
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			TextView tv = new TextView(ctx);
			tv.setText("a) सम्भावित : " + edd);
			llp.setMargins(5, 5, 0, 5); // llp.setMargins(left, top, right,
										// bottom);
			tv.setLayoutParams(llp);
			ll.addView(tv);
			String ans = "";
			Cursor myc;
			int selans;

			TextView tv1 = new TextView(ctx);
			myc = mydb.getAnsAnc(pid, 11, "");
			selans = 0;
			if (myc.isFirst())
				selans = myc.getInt(myc.getColumnIndex("ans"));
			if (selans == 2)
				ans = dtconvert(myc.getLong(myc.getColumnIndex("dtval")));
			else if (selans == 1)
				ans = "Not given";
			if (!ans.isEmpty()) {
				tv1.setText("b) टी टी 1 : " + ans);
				llp.setMargins(5, 5, 0, 5); // llp.setMargins(left, top, right,
											// bottom);
				tv1.setLayoutParams(llp);
				ll.addView(tv1);
			}
			TextView tv2 = new TextView(ctx);
			ans = "";
			myc = mydb.getAnsAnc(pid, 12, "");
			selans = 0;
			if (myc.isFirst())
				selans = myc.getInt(myc.getColumnIndex("ans"));
			if (selans == 2)
				ans = dtconvert(myc.getLong(myc.getColumnIndex("dtval")));
			else if (selans == 1)
				ans = "Not given";
			if (!ans.isEmpty()) {
				tv2.setText("c) टी टी 2 : " + ans);
				llp.setMargins(5, 5, 0, 5); // llp.setMargins(left, top, right,
											// bottom);
				tv2.setLayoutParams(llp);
				ll.addView(tv2);
			}
			TextView tv3 = new TextView(ctx);
			ans = "";
			myc = mydb.getAnsAnc(pid, 13, "");
			selans = 0;
			if (myc.isFirst())
				selans = myc.getInt(myc.getColumnIndex("ans"));
			if (selans == 3)
				ans = dtconvert(myc.getLong(myc.getColumnIndex("dtval")));
			else if (selans == 2)
				ans = "Not required";
			else if (selans == 1)
				ans = "Not given";
			if (!ans.isEmpty()) {
				tv3.setText("d) टी टी बूस्टर : " + ans);
				llp.setMargins(5, 5, 0, 5); // llp.setMargins(left, top, right,
											// bottom);
				tv3.setLayoutParams(llp);
				ll.addView(tv3);
			}
			TextView tv4 = new TextView(ctx);
			ans = "";
			myc = mydb.getAnsAnc(pid, 14, "");
			selans = 0;
			if (myc.isFirst())
				selans = myc.getInt(myc.getColumnIndex("ans"));
			if (selans == 2)
				ans = myc.getString(myc.getColumnIndex("qnty"));
			else if (selans == 1)
				ans = "Not done";
			myc = mydb.getAnsAnc(pid, 15, "");
			selans = 0;
			if (myc.isFirst())
				selans = myc.getInt(myc.getColumnIndex("ans"));
			if (selans == 2)
				ans = ans + ";" + myc.getString(myc.getColumnIndex("qnty"));
			else if (selans == 1)
				ans = ans + "; Not done";
			myc = mydb.getAnsAnc(pid, 16, "");
			selans = 0;
			if (myc.isFirst())
				selans = myc.getInt(myc.getColumnIndex("ans"));
			if (selans == 2)
				ans = ans + ";" + myc.getString(myc.getColumnIndex("qnty"));
			else if (selans == 1)
				ans = ans + "; Not done";
			if (!ans.isEmpty()) {
				tv4.setText("e) एचबी : " + ans);
				llp.setMargins(5, 5, 0, 5); // llp.setMargins(left, top, right,
											// bottom);
				tv4.setLayoutParams(llp);
				ll.addView(tv4);
			}
			TextView tv5 = new TextView(ctx);
			ans = "";
			myc = mydb.getAnsAnc(pid, 17, "");
			selans = 0;
			if (myc.isFirst())
				selans = myc.getInt(myc.getColumnIndex("ans"));
			if (selans == 2)
				ans = myc.getString(myc.getColumnIndex("qnty"));
			else if (selans == 1)
				ans = "Not done";
			myc = mydb.getAnsAnc(pid, 18, "");
			selans = 0;
			if (myc.isFirst())
				selans = myc.getInt(myc.getColumnIndex("ans"));
			if (selans == 2)
				ans = ans + ";" + myc.getString(myc.getColumnIndex("qnty"));
			else if (selans == 1)
				ans = ans + "; Not done";
			myc = mydb.getAnsAnc(pid, 19, "");
			selans = 0;
			if (myc.isFirst())
				selans = myc.getInt(myc.getColumnIndex("ans"));
			if (selans == 2)
				ans = ans + ";" + myc.getString(myc.getColumnIndex("qnty"));
			else if (selans == 1)
				ans = ans + "; Not done";
			if (!ans.isEmpty()) {
				tv5.setText("f) आयरन की गोलियां : " + ans);
				llp.setMargins(5, 5, 0, 5); // llp.setMargins(left, top, right,
											// bottom);
				tv5.setLayoutParams(llp);
				ll.addView(tv5);
			}
			TextView tv6 = new TextView(ctx);
			ans = "";
			myc = mydb.getAnsAnc(pid, 21, "");
			selans = 0;
			if (myc.isFirst())
				selans = myc.getInt(myc.getColumnIndex("ans"));
			if (selans == 2)
				ans = dtconvert(myc.getLong(myc.getColumnIndex("dtval")));
			else if (selans == 1)
				ans = "Not done";
			if (!ans.isEmpty()) {
				tv6.setText("g) पहली जाँच : " + ans);
				llp.setMargins(5, 5, 0, 5); // llp.setMargins(left, top, right,
											// bottom);
				tv6.setLayoutParams(llp);
				ll.addView(tv6);
			}
			TextView tv7 = new TextView(ctx);
			ans = "";
			myc = mydb.getAnsAnc(pid, 22, "");
			selans = 0;
			if (myc.isFirst())
				selans = myc.getInt(myc.getColumnIndex("ans"));
			if (selans == 2)
				ans = dtconvert(myc.getLong(myc.getColumnIndex("dtval")));
			else if (selans == 1)
				ans = "Not done";
			if (!ans.isEmpty()) {
				tv7.setText("h) दूसरी जाँच : " + ans);
				llp.setMargins(5, 5, 0, 5); // llp.setMargins(left, top, right,
											// bottom);
				tv7.setLayoutParams(llp);
				ll.addView(tv7);
			}
			TextView tv8 = new TextView(ctx);
			ans = "";
			myc = mydb.getAnsAnc(pid, 23, "");
			selans = 0;
			if (myc.isFirst())
				selans = myc.getInt(myc.getColumnIndex("ans"));
			if (selans == 2)
				ans = dtconvert(myc.getLong(myc.getColumnIndex("dtval")));
			else if (selans == 1)
				ans = "Not done";
			if (!ans.isEmpty()) {
				tv8.setText("i) तीसरी जाँच : " + ans);
				llp.setMargins(5, 5, 0, 5); // llp.setMargins(left, top, right,
											// bottom);
				tv8.setLayoutParams(llp);
				ll.addView(tv8);
			}
			TextView tv9 = new TextView(ctx);
			ans = "";
			myc = mydb.getAnsAnc(pid, 24, "");
			selans = 0;
			if (myc.isFirst())
				selans = myc.getInt(myc.getColumnIndex("ans"));
			if (selans == 2)
				ans = dtconvert(myc.getLong(myc.getColumnIndex("dtval")));
			else if (selans == 1)
				ans = "Not done";
			if (!ans.isEmpty()) {
				tv9.setText("j) चौथी जाँच : " + ans);
				llp.setMargins(5, 5, 0, 5); // llp.setMargins(left, top, right,
											// bottom);
				tv9.setLayoutParams(llp);
				ll.addView(tv9);
			}

			/*
			 * a) EDD: date b) TT1 : Date or Not given 11 c) TT2 : Date or Not
			 * given 12 d) TT Booster : Date or Not given 13 e) Hb: reading
			 * 1/not taken 14; reading 2/ or not taken 15; reading 3/or not
			 * taken 16 f) IFA: nos 1/not given; nos 2/not given; nos 3/not
			 * given 17 18 19 g) ANC 1: date 21 h) ANC 2: date 22 i) ANC 3: date
			 * 23 j) ANC 4: date 24
			 * 
			 * StringBuilder gsumm=new
			 * StringBuilder(c.getString(c.getColumnIndex("gsumm"))); for(int
			 * i=0;i<gsumm.length();i++) { char stch=gsumm.charAt(i); String
			 * st="Green"; // TextView tv=new TextView(ctx);
			 * //+" "+gsumm.charAt(i)); if (stch=='R') {
			 * tv.setBackgroundColor(Color.RED);st="Red";} else if (stch=='Y') {
			 * tv.setBackgroundColor(Color.YELLOW);st="Yellow";} else
			 * tv.setBackgroundColor(Color.GREEN);
			 * //tv.setText((i+1)+") "+grpinfo[i]+" - "+st); if
			 * (!grpinfo[i].trim().equals("")) {
			 * tv.setText((i+1)+") "+grpinfo[i]); //tv.setTypeface(face);
			 * tv.setTextSize(20); LinearLayout.LayoutParams llp = new
			 * LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
			 * LayoutParams.WRAP_CONTENT); llp.setMargins(5, 5, 0, 5); //
			 * llp.setMargins(left, top, right, bottom);
			 * tv.setLayoutParams(llp); ll.addView(tv); } }
			 */
		} else {
			TextView tv = new TextView(ctx);
			tv.setText("No ANC home visit yet");

			ll.addView(tv);
		}

		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (bflag) {
					Intent intent = new Intent(ctx, QuestANC.class);
					intent.putExtra("id", id);
					intent.putExtra("hv_str", hv_str);
					intent.putExtra("seq", seq);
					intent.putExtra("pid", pid);
					// intent.putExtra("learn", learn);
					intent.putExtra("dstat", dstat);
					intent.putExtra("lmp", lmp_str);
					ctx.startActivity(intent);
				}
				finish();
			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}

}
