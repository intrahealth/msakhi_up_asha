package com.asha.mnewborncareasha;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class Birth_reg_list extends ListActivity {
	private DBAdapter mydb;
	String[] filterlist = { "Default", "सभी गर्भवती", "0-3(Month)",
			"3-12(Month)", "1-2(Year)", "2-4(Year)", "4-6(Year)" };
	LayoutInflater inflate;
	Spinner filter_spin;
	LazyCursorAdapterBirth ca;
	SpinShow filter_adapter;
	ListView lst;
	TextView txtCount;
	int id = 0;
	String mname;
	Cursor c;
	static int lastSel = -1;
	MediaPlayer mediaPlayer = new MediaPlayer();
	Button btnModBirth, btn;

	@Override
	public void onDestroy() {
		if (c != null)
			c.close();
		mydb.close();
		super.onDestroy();
	}

	public static int getCurrentSelectedItemId() {
		return lastSel;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.birth_reg_list);
		inflate = getLayoutInflater();
		filter_adapter = new SpinShow();

		filter_spin = (Spinner) findViewById(R.id.spin_filter);
		filter_spin.setAdapter(filter_adapter);
		filter_spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (filterlist[arg2].equals("0-3(Month)")) {
					c = mydb.getAllFilterdPreg(0, 3);
				} else if (filterlist[arg2].equals("3-12(Month)")) {
					c = mydb.getAllFilterdPreg(4, 12);
				} else if (filterlist[arg2].equals("1-2(Year)")) {
					c = mydb.getAllFilterdPreg(13, 24);
				} else if (filterlist[arg2].equals("2-4(Year)")) {
					c = mydb.getAllFilterdPreg(25, 48);
				} else if (filterlist[arg2].equals("4-6(Year)")) {
					c = mydb.getAllFilterdPreg(49, 72);
				} else if (filterlist[arg2].equals("सभी गर्भवती")) {
					c = mydb.getAllFilterdPregNonDelivered();
				} else {
					c = mydb.getAllPreg();
				}

				showInfo();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		// TextView tvHead=(TextView)findViewById(R.id.tvHead);
		txtCount = (TextView) findViewById(R.id.txtCount);
		btnModBirth = (Button) findViewById(R.id.btnModBirth);
		btn = (Button) findViewById(R.id.btnNewBirth);
		// btnModBirth.setTypeface(face);
		// mydb = new DBAdapter(this);
		mydb = DBAdapter.getInstance(getApplicationContext());
		/*
		 * try { mydb.createDataBase(); mydb.openDataBase(); } catch
		 * (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */
		// txtCount.setText(String.valueOf(mydb.getPregNo()));
		c = mydb.getAllPreg();
		showInfo();

		lst.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v,
					int position, long arg3) {
				v.setSelected(true);
				Cursor cursor = (Cursor) adapterView
						.getItemAtPosition(position);
				lastSel = position;
				id = cursor.getInt(cursor.getColumnIndex(DBAdapter.KEY_ROWID));
				if (cursor.isNull(cursor.getColumnIndex("dob"))
						|| cursor.getString(cursor.getColumnIndex("dob"))
								.equals("null")) {
					btnModBirth.setVisibility(android.view.View.INVISIBLE);
					btn.setVisibility(android.view.View.VISIBLE);
				} else {
					btnModBirth.setVisibility(android.view.View.VISIBLE);
					btn.setVisibility(android.view.View.INVISIBLE);
				}
				mname = cursor.getString(cursor.getColumnIndex("name"));
				// Toast.makeText(getApplicationContext(), "" + id,
				// Toast.LENGTH_SHORT).show();
				try {
					String audioFile;
					audioFile = Environment.getExternalStorageDirectory()
							.getAbsolutePath()
							+ "/"
							+ Workflow.bfolder
							+ "/pdata/" + String.valueOf(id) + ".3gp";

					// mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/tmp/test.3gp");
					mediaPlayer.reset();
					mediaPlayer.setDataSource(audioFile);
					mediaPlayer.prepare();
					mediaPlayer.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// return true;
				// cursor.close();
			}
		});

		// lst.setOnItemLongClickListener(new OnItemLongClickListener() {
		//
		// public boolean onItemLongClick(AdapterView<?> adapterView, View v,int
		// position, long arg3) {
		// // TODO Auto-generated method stub
		// Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
		// int key_id =
		// cursor.getInt(cursor.getColumnIndex(DBAdapter.KEY_ROWID));
		// lastSel = position;
		// ImageView img=new ImageView(getApplicationContext());
		// String imgfile=Environment.getExternalStorageDirectory()+
		// File.separator + "/"+Workflow.bfolder+"/pdata/"+
		// String.valueOf(key_id) +".jpg";
		// Bitmap bmp = BitmapFactory.decodeFile(imgfile);
		// img.setImageBitmap(bmp);
		// loadPhoto(img, 100, 100);
		// cursor.close();
		// return false;
		// }
		//
		// });

		addListenerOnButton();
	}

	private void showInfo() {
		txtCount.setText("कुल लाभार्थी : " + String.valueOf(c.getCount()));
		startManagingCursor(c);
		// the desired columns to be bound
		String[] from = new String[] { DBAdapter.KEY_NAME, "edd",
				DBAdapter.KEY_ROWID };
		// the XML defined views which the data will be bound to
		int[] to = new int[] { R.id.name_entry, R.id.number_entry };

		ca = new LazyCursorAdapterBirth(this, R.layout.list_row, c, from, to);
		setListAdapter(ca);
		lst = (ListView) findViewById(android.R.id.list);

	}

	private void addListenerOnButton() {
		// TODO Auto-generated method stub

		// btn.setTypeface(face);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (id == 0)
					Toast.makeText(getApplicationContext(), "लाभार्थी चुने",
							Toast.LENGTH_LONG).show();
				else {
					Context ctx = arg0.getContext();
					Intent intent = new Intent(ctx, Birth_reg_entry.class);
					intent.putExtra("id", id);
					intent.putExtra("mname", mname);
					intent.putExtra("change", false);
					ctx.startActivity(intent);
				}
			}

		});

		btnModBirth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (id == 0)
					Toast.makeText(getApplicationContext(), "लाभार्थी चुने",
							Toast.LENGTH_LONG).show();
				else {
					Context ctx = arg0.getContext();
					Intent intent = new Intent(ctx, Birth_reg_entry.class);
					intent.putExtra("id", id);
					intent.putExtra("mname", mname);
					intent.putExtra("change", true);
					ctx.startActivity(intent);
				}
			}

		});

	}

	@Override
	protected void onResume() {
		lastSel = -1;
		txtCount.setText("कुल लाभार्थी : " + String.valueOf(c.getCount()));
		super.onResume();
	}

	private void loadPhoto(ImageView imageView, int width, int height) {

		ImageView tempImageView = imageView;

		AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
				(ViewGroup) findViewById(R.id.layout_root));
		ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
		image.setImageDrawable(tempImageView.getDrawable());
		imageDialog.setView(layout);
		imageDialog.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				});

		imageDialog.create();
		imageDialog.show();
	}

	class SpinShow extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return filterlist.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflate.inflate(R.layout.spinnerrow, null);
			}
			TextView tv_month = (TextView) convertView
					.findViewById(R.id.tv_months);
			tv_month.setText(filterlist[position]);

			return convertView;
		}

	}

}
