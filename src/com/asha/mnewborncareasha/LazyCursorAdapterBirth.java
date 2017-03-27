package com.asha.mnewborncareasha;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
//import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class LazyCursorAdapterBirth extends SimpleCursorAdapter {

	// private Context context;
	private int layout;
	public ImageLoader imageLoader;
	private LayoutInflater inflater = null;
	int nameCol, eddCol, slCol, dobCol, mnCol, dbregCol, hname, child_name;//
	private int server;

	public LazyCursorAdapterBirth(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		// this.context = context;
		this.layout = layout;
		inflater = LayoutInflater.from(context);
		imageLoader = new ImageLoader(context);
		nameCol = c.getColumnIndex("name");
		eddCol = c.getColumnIndex("edd");
		dobCol = c.getColumnIndex("dob");
		slCol = c.getColumnIndex("_id");
		mnCol = c.getColumnIndex("mnth");
		dbregCol = c.getColumnIndex("dbreg");
		server = c.getColumnIndex("server_id");
		hname = c.getColumnIndex("hname");
		child_name = c.getColumnIndex("child_name");

		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		View v = inflater.inflate(layout, parent, false);
		Cursor c = getCursor();
		int position = cursor.getPosition();

		TextView tvName = (TextView) v.findViewById(R.id.name_entry);
		TextView tvhname = (TextView) v.findViewById(R.id.edd_entry);
		TextView tvDob = (TextView) v.findViewById(R.id.dob_entry);
		TextView tvEdd = (TextView) v.findViewById(R.id.mobile_no);
		TextView tvSlno = (TextView) v.findViewById(R.id.slno);
		ImageView thumb_image = (ImageView) v.findViewById(R.id.list_image);
		tvName.setText(c.getString(nameCol));
		tvhname.setText(c.getString(hname));

		// Hero Add
		TextView childname = (TextView) v.findViewById(R.id.childname);
		String chldnme = "बच्चे के नाम: " + ""
				+ String.valueOf(c.getString(child_name));
		if (c.getString(child_name) != null) {
			childname.setText(chldnme);
		}

		// if (c.getInt(dbregCol)==0)
		tvEdd.setText("सम्भावित: " + DBAdapter.strtodate(c.getString(eddCol))
				+ " (" + c.getString(mnCol) + " माह)");
		// else
		// // {
		// tvEdd.setText("");
		// //tvEdd.setVisibility(View.GONE);
		// }
		tvSlno.setText(c.getString(slCol));
		if (!c.isNull(dobCol) && !c.getString(dobCol).equals("null")) {
			tvDob.setText("जन्म की तारीख: "
					+ DBAdapter.strtodate(c.getString(dobCol)));
			tvDob.setTextColor(Color.BLUE);
		} else {
			tvDob.setText("");
			// tvDob.setVisibility(View.GONE);
		}
		String imgfile = null;
		if (c.getInt(server) > 0) {
			imgfile = Environment.getExternalStorageDirectory()
					+ File.separator + Workflow.bfolder + "/pdata/"
					+ c.getInt(server) + ".jpg";
		} else {
			imgfile = Environment.getExternalStorageDirectory()
					+ File.separator + Workflow.bfolder + "/pdata/"
					+ c.getInt(slCol) + ".jpg";
		}
		try {
			imageLoader.DisplayThumbnail(imgfile, thumb_image);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return v;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {

		int position = c.getPosition();
		TextView tvName = (TextView) v.findViewById(R.id.name_entry);
		TextView tvhname = (TextView) v.findViewById(R.id.edd_entry);
		TextView tvDob = (TextView) v.findViewById(R.id.dob_entry);
		TextView tvEdd = (TextView) v.findViewById(R.id.mobile_no);
		TextView tvSlno = (TextView) v.findViewById(R.id.slno);
		ImageView thumb_image = (ImageView) v.findViewById(R.id.list_image);
		tvName.setText(c.getString(nameCol));
		tvhname.setText(c.getString(hname));

		// Hero Add
		TextView childname = (TextView) v.findViewById(R.id.childname);
		String chldnme = "बच्चे के नाम: " + ""
				+ String.valueOf(c.getString(child_name));
		if (c.getString(child_name) != null) {
			childname.setText(chldnme);
		}
		// if (c.getInt(dbregCol)==0)

		try {
			if ((c.getString(mnCol)) != null) {

				tvEdd.setText("सम्भावित: "
						+ DBAdapter.strtodate(c.getString(eddCol)) + " ("
						+ c.getString(mnCol) + " माह)");
			}

			else {
				tvEdd.setText("सम्भावित: "
						+ DBAdapter.strtodate(c.getString(eddCol)));
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		// else
		// {
		// tvEdd.setText("");
		// tvEdd.setVisibility(View.GONE);
		// }
		tvSlno.setText(c.getString(slCol));
		if (!c.isNull(dobCol) && !c.getString(dobCol).equals("null")) {
			tvDob.setText("जन्म की तारीख: "
					+ DBAdapter.strtodate(c.getString(dobCol)));
			tvDob.setTextColor(Color.BLUE);
		} else {
			tvDob.setText("");
		}
		String imgfile = null;
		if (c.getInt(server) > 0) {
			imgfile = Environment.getExternalStorageDirectory()
					+ File.separator + Workflow.bfolder + "/pdata/"
					+ c.getInt(server) + ".jpg";
		} else {
			imgfile = Environment.getExternalStorageDirectory()
					+ File.separator + Workflow.bfolder + "/pdata/"
					+ c.getInt(slCol) + ".jpg";
		}
		try {
			imageLoader.DisplayThumbnail(imgfile, thumb_image);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
