package com.asha.mnewborncareasha;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class LazyCursorAdapterAnc extends SimpleCursorAdapter {

	// private Context context;
	private int layout;
	public ImageLoader imageLoader;
	private LayoutInflater inflater = null;
	int nameCol, eddCol, slCol, dobCol, mnCol, seqCol, triCol, server_idcol;
	Context con;
	Typeface face;
	DBAdapter dba;

	public LazyCursorAdapterAnc(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		// this.context = context;
		this.layout = layout;
		inflater = LayoutInflater.from(context);
		imageLoader = new ImageLoader(context);
		dba = DBAdapter.getInstance(context);
		nameCol = c.getColumnIndex("name");
		eddCol = c.getColumnIndex("edd");
		slCol = c.getColumnIndex("_id");
		mnCol = c.getColumnIndex("dy");
		triCol = c.getColumnIndex("last_anc_visit");
		server_idcol = c.getColumnIndex("server_id");
		face = Typeface
				.createFromAsset(context.getAssets(), "monospace821.ttf");
		con = context;

		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		View v = inflater.inflate(layout, parent, false);
		Cursor c = getCursor();
		// int position= cursor.getPosition();
		int cpos = (int) Math.ceil(c.getFloat(mnCol));
		TextView tvName = (TextView) v.findViewById(R.id.name_entry);
		TextView tvEdd = (TextView) v.findViewById(R.id.edd_entry);
		TextView tvSlno = (TextView) v.findViewById(R.id.slno);
		TextView tvHvInd = (TextView) v.findViewById(R.id.home_visit_ind);
		TextView tvHvLeg = (TextView) v.findViewById(R.id.home_visit_leg);
		TextView tvmobile_no = (TextView) v.findViewById(R.id.mobile_no);
		ImageView thumb_image = (ImageView) v.findViewById(R.id.list_image);
		tvName.setText(c.getString(nameCol));
		// String dt_str[]=plmp.split("\\-");
		// dtPicker.init(Integer.parseInt(dt_str[0]),Integer.parseInt(dt_str[1])-1,
		// Integer.parseInt(dt_str[2]), new OnDateChangedListener() {
		tvHvInd.setTypeface(face);
		tvHvLeg.setTypeface(face);
		tvHvInd.setText(str_fmt_visit(c.getString(triCol), cpos));
		tvEdd.setText("सम्भावित: " + DBAdapter.strtodate(c.getString(eddCol))
				+ " (" + cpos + ")");
		tvSlno.setText(dba.getID(c.getString(server_idcol)) + "");
		tvHvInd.setTextColor(getTextColor(
				str_fmt_visit(c.getString(triCol), cpos), tvSlno, cpos));
		String imgfile = null;
		if (c.getInt(server_idcol) > 0) {
			imgfile = Environment.getExternalStorageDirectory()
					+ File.separator + Workflow.bfolder + "/pdata/"
					+ c.getString(server_idcol) + ".jpg";
		} else {

			imgfile = Environment.getExternalStorageDirectory()
					+ File.separator + Workflow.bfolder + "/pdata/"
					+ c.getString(slCol) + ".jpg";
		}
		// imageLoader.DisplayImage(imgfile, thumb_image);
		try {
			imageLoader.DisplayThumbnail(imgfile, thumb_image);
		} catch (NullPointerException e) {
			e.printStackTrace();
			// imageLoader.DisplayThumbnail(context.getResources().getDrawable(R.drawable.no_image),
			// thumb_image);
		}
		;
		return v;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {

		// int position= c.getPosition();
		int cpos = (int) Math.ceil(c.getFloat(mnCol));
		TextView tvName = (TextView) v.findViewById(R.id.name_entry);
		TextView tvEdd = (TextView) v.findViewById(R.id.edd_entry);
		TextView tvSlno = (TextView) v.findViewById(R.id.slno);
		TextView tvHvInd = (TextView) v.findViewById(R.id.home_visit_ind);
		TextView tvHvLeg = (TextView) v.findViewById(R.id.home_visit_leg);
		ImageView thumb_image = (ImageView) v.findViewById(R.id.list_image);
		tvHvInd.setTypeface(face);
		tvHvLeg.setTypeface(face);
		tvHvInd.setText(str_fmt_visit(c.getString(triCol), cpos));
		tvName.setText(c.getString(nameCol));
		tvEdd.setText("सम्भावित: " + DBAdapter.strtodate(c.getString(eddCol))
				+ " (" + cpos + ")");
		tvSlno.setText(dba.getID(c.getString(server_idcol)) + "");
		tvHvInd.setTextColor(getTextColor(
				str_fmt_visit(c.getString(triCol), cpos), tvSlno, cpos));
		String imgfile = null;
		if (c.getInt(server_idcol) > 0) {
			imgfile = Environment.getExternalStorageDirectory()
					+ File.separator + Workflow.bfolder + "/pdata/"
					+ c.getString(server_idcol) + ".jpg";
		} else {

			imgfile = Environment.getExternalStorageDirectory()
					+ File.separator + Workflow.bfolder + "/pdata/"
					+ c.getString(slCol) + ".jpg";
		}
		try {
			imageLoader.DisplayThumbnail(imgfile, thumb_image);
		} catch (NullPointerException e) {
			e.printStackTrace();
			// imageLoader.DisplayThumbnail(context.getResources().getDrawable(R.drawable.no_image),
			// thumb_image);
		}

	}

	private int getTextColor(String string, TextView tvSlno, int c) {
		char[] str = string.toCharArray();
		String firsttoken = Character.toString(str[2]);
		String secondtoken = Character.toString(str[6]);
		String thirdtoken = Character.toString(str[10]);
		if (c == 1) {
			if (firsttoken.equals("√")) {
				return Color.BLACK;
			} else {
				return con.getResources().getColor(R.color.green);
			}
		} else if (c == 2) {
			if (secondtoken.equals("√")) {
				return Color.BLACK;
			} else if (firsttoken.equals("√")) {
				return con.getResources().getColor(R.color.green);
			} else if (firsttoken.equals("√") && secondtoken.equals("√")) {
				return Color.BLACK;
			} else if (firsttoken.equals("x") && secondtoken.equals("*")) {
				return Color.RED;
			} else {
				return con.getResources().getColor(R.color.green);
			}
		} else if (c == 3) {

			if (thirdtoken.equals("√")) {
				return Color.BLACK;
			} else if (secondtoken.equals("√")) {
				return con.getResources().getColor(R.color.green);
			} else if (secondtoken.equals("√") && thirdtoken.equals("√")) {
				return Color.BLACK;
			} else if (secondtoken.equals("x") && thirdtoken.equals("*")) {
				return Color.RED;
			} else {
				return con.getResources().getColor(R.color.green);
			}
		} else {
			return con.getResources().getColor(R.color.green);
		}

	}

	/*
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) { // TODO Auto-generated method stub //inflater =
	 * LayoutInflater.from(context); //View view =
	 * inflater.inflate(R.layout.preg_reg_list, null); //super.getView(position,
	 * convertView, parent); View v = inflater.inflate(layout, parent, false);
	 * if (position == Preg_reg_list.getCurrentSelectedItemId()) {
	 * v.setBackgroundDrawable
	 * (v.getContext().getResources().getDrawable(R.drawable
	 * .gradient_bg_hover)); }
	 * 
	 * return v; }
	 */

	private String str_fmt_visit(String instr, int cpos) {
		StringBuilder tmp = new StringBuilder("| * | * | * |");
		char tick = '√';
		for (int i = 1; i < 4; i++) {
			if (instr.contains(String.valueOf(i))) {
				tmp.setCharAt(i * 4 - 2, tick);
			} else if (i < cpos) {
				tmp.setCharAt(i * 4 - 2, 'x');

			}
		}
		return tmp.toString();
	}

}
