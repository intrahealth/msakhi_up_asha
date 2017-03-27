package com.asha.mnewborncareasha;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Preg_reg_list extends ListActivity {
	private DBAdapter mydb;
	TextView txtCount;
	String[] filterlist = { "सभी गर्भवती", "0-3", "3-6", "6-9" };
	SpinShow filter_adapter;
	Spinner filter_spin;
	LazyCursorAdapter ca;
	LayoutInflater inflate;
	public static boolean watcher = false;
	ListView lst;
	int preg_id = -1;
	Cursor c;
	MediaPlayer mediaPlayer = new MediaPlayer();

	static int lastSel = -1;
	private ArrayList<String> Id = new ArrayList<String>();
	private ArrayList<String> Name = new ArrayList<String>();
	private ArrayList<String> Edd = new ArrayList<String>();

	public static int getCurrentSelectedItemId() {
		return lastSel;
	}

	public static ImageView expandedImageView;
	public static RelativeLayout ll;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.preg_reg);
		inflate = getLayoutInflater();
		// TextView tvHead=(TextView)findViewById(R.id.tvHead);
		// face=Typeface.createFromAsset(getAssets(),getString(R.string.hindi_font));
		// tvHead.setTypeface(face);
		filter_adapter = new SpinShow();
		filter_spin = (Spinner) findViewById(R.id.spin_filter);
		filter_spin.setAdapter(filter_adapter);
		filter_spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (filterlist[arg2].equals("0-3")) {
					c = mydb.getAllFilterdPregList(0, 3);

				} else if (filterlist[arg2].equals("3-6")) {
					c = mydb.getAllFilterdPregList(3, 6);
				} else if (filterlist[arg2].equals("6-9")) {
					c = mydb.getAllFilterdPregList(6, 9);
				} else {
					c = mydb.getAllPregList();
				}

				showinfo();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		txtCount = (TextView) findViewById(R.id.txtCount);

		// mydb = new DBAdapter(this);

		expandedImageView = (ImageView) findViewById(R.id.expanded_image);
		ll = (RelativeLayout) findViewById(R.id.container);

		mydb = DBAdapter.getInstance(getApplicationContext());
		/*
		 * try { mydb.createDataBase(); mydb.openDataBase(); } catch
		 * (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */

		c = mydb.getAllPregList();
		showinfo();

		// txtCount.setText("कुल गर्भवती: "+String.valueOf(c.getCount()));
		// startManagingCursor(c);
		// // the desired columns to be bound
		// String[] from = new String[] { DBAdapter.KEY_NAME,
		// "edd",DBAdapter.KEY_ROWID };
		// // the XML defined views which the data will be bound to
		// int[] to = new int[] { R.id.name_entry, R.id.edd_entry,R.id.slno };
		//
		// //SimpleCursorAdapter ca=new
		// SimpleCursorAdapter(this,android.R.layout.simple_spinner_item, c, new
		// String [] {DatabaseHelper.colDeptName}, new int
		// []{android.R.id.text1});
		// ca=new LazyCursorAdapter(this,R.layout.list_row_new,c,from,to);
		// //ca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// setListAdapter(ca);
		// lst=(ListView)findViewById(android.R.id.list);
		// lst.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lst.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v,
					int position, long arg3) {
				lastSel = position;
				v.setSelected(true);
				Cursor cursor = (Cursor) adapterView
						.getItemAtPosition(position);
				int key_id = cursor.getInt(cursor
						.getColumnIndex(DBAdapter.KEY_ROWID));
				preg_id = key_id;
				// Toast.makeText(getApplicationContext(), "" + key_id,
				// Toast.LENGTH_SHORT).show();
				try {
					String audioFile;
					audioFile = Environment.getExternalStorageDirectory()
							.getAbsolutePath()
							+ "/"
							+ Workflow.bfolder
							+ "/pdata/" + String.valueOf(key_id) + ".3gp";

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
			}
		});

		lst.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View v,
					int position, long arg3) {

				Cursor cursor = (Cursor) adapterView
						.getItemAtPosition(position);
				int key_id = cursor.getInt(cursor
						.getColumnIndex(DBAdapter.KEY_ROWID));
				lastSel = position;
				ImageView img = new ImageView(getApplicationContext());
				String imgfile = Environment.getExternalStorageDirectory()
						+ File.separator + "/" + Workflow.bfolder + "/pdata/"
						+ String.valueOf(key_id) + ".jpg";
				Bitmap bmp = BitmapFactory.decodeFile(imgfile);
				img.setImageBitmap(bmp);
				return false;
			}

		});

		addListenerOnButton();

	}

	private void showinfo() {
		txtCount.setText("कुल गर्भवती: " + String.valueOf(c.getCount()));
		startManagingCursor(c);
		// the desired columns to be bound
		String[] from = new String[] { DBAdapter.KEY_NAME, "edd",
				DBAdapter.KEY_ROWID };
		// the XML defined views which the data will be bound to
		int[] to = new int[] { R.id.name_entry, R.id.edd_entry, R.id.slno };

		// SimpleCursorAdapter ca=new
		// SimpleCursorAdapter(this,android.R.layout.simple_spinner_item, c, new
		// String [] {DatabaseHelper.colDeptName}, new int
		// []{android.R.id.text1});
		ca = new LazyCursorAdapter(this, R.layout.list_row_new, c, from, to);
		// ca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		setListAdapter(ca);
		lst = (ListView) findViewById(android.R.id.list);
		lst.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

	}

	private void addListenerOnButton() {
		// TODO Auto-generated method stub
		Button btnChange = (Button) findViewById(R.id.btnChange);
		// btnChange.setTypeface(face);
		Button btn = (Button) findViewById(R.id.btnNew);
		// btn.setTypeface(face);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Context ctx = arg0.getContext();
				Intent intent = new Intent(ctx, preg_entry.class);
				// Intent intent = new Intent(ctx,PregSearch.class);
				intent.putExtra("slno", mydb.getPregNo() + 1);
				intent.putExtra("change", false);
				ctx.startActivity(intent);
			}
		});

		btnChange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (preg_id == -1)
					Toast.makeText(getApplicationContext(), "लाभार्थी चुने",
							Toast.LENGTH_LONG).show();
				else {
					Context ctx = arg0.getContext();
					Intent intent = new Intent(ctx, preg_entry.class);
					intent.putExtra("slno", preg_id);
					intent.putExtra("change", true);
					ctx.startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		try {
			txtCount.setText("कुल गर्भवती: " + String.valueOf(c.getCount()));
			preg_id = -1;
			lastSel = -1;
			mydb.createDataBase();
			mydb.openDataBase();
			dispData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		mydb.close();
		super.onPause();
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

	public void dispData() {
		// dataBase = mydb.getWritableDatabase();
		// Cursor mCursor = dataBase.rawQuery("SELECT * FROM preg_reg", null);
		Cursor mCursor = mydb.getAllPreg();
		Id.clear();
		Name.clear();
		Edd.clear();
		if (mCursor.moveToFirst()) {
			do {
				Id.add(mCursor.getString(mCursor.getColumnIndex("_id")));
				Name.add(mCursor.getString(mCursor.getColumnIndex("name")));
				// Edd.add(mCursor.getString(mCursor.getColumnIndex("edd")));
				Edd.add(mCursor.getString(mCursor.getColumnIndex("mobile")));
			} while (mCursor.moveToNext());
		}
		// CustomPregnantAdopter disadpt = new
		// CustomPregnantAdopter(Preg_reg_list.this,Id, Name, Edd);
		// lst.setAdapter(disadpt);
		mCursor.close();
		System.out.println("preg_reg data" + Id + "\n" + Name + "\n" + Edd);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (watcher) {
				ca.thumb_image.setAlpha(1f);
				Preg_reg_list.expandedImageView.setVisibility(View.GONE);
				ca.mCurrentAnimator = null;
				watcher = false;
				return false;
			} else {

			}

		}
		return super.onKeyDown(keyCode, event);
	}
}
