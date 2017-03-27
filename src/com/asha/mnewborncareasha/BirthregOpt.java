package com.asha.mnewborncareasha;

import android.app.Activity;
import android.content.Intent;
//import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

import android.widget.Button;

public class BirthregOpt extends Activity {

	Intent i;
	private DBAdapter mydb;
	Global global;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.birthregopt);
		// Add Hero
		global = (Global) getApplicationContext();

		Button btnBreg = (Button) findViewById(R.id.btnBreg);
		Button btnDBreg = (Button) findViewById(R.id.btnDBreg);
		mydb = DBAdapter.getInstance(getApplicationContext());
		btnBreg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Add Hero
				global.setChild_Entry_flag(1);
				Intent intent = new Intent(getApplicationContext(),
						Birth_reg_list.class);
				startActivity(intent);
			}
		});

		btnDBreg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Add Hero
				global.setChild_Entry_flag(0);
				Intent intent = new Intent(getApplicationContext(),
						preg_entry.class);
				intent.putExtra("slno", mydb.getPregNo() + 1);
				intent.putExtra("change", false);
				intent.putExtra("dbreg", true);
				startActivity(intent);
			}
		});
	}

}
