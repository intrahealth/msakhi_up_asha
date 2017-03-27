package com.asha.mnewborncareasha;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
//import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
//import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LazyCursorAdapter extends SimpleCursorAdapter {

	// private Context context;
	private int layout;
	public ImageLoader imageLoader;
	private LayoutInflater inflater = null;
	int nameCol, eddCol, slCol, dobCol, mnCol, mobileno, hname, mstat, cstat;
	MediaPlayer aplayer;
	int m_flag;
	String audioFile = "";
	File path;
	String imgfile1;
	public ImageView thumb_image;
	// Hold a reference to the current animator,
	// so that it can be canceled mid-way.
	public Animator mCurrentAnimator;
	Bitmap b = null;
	String imgfile;
	// The system "short" animation time duration, in milliseconds. This
	// duration is ideal for subtle animations or animations that occur
	// very frequently.
	private int mShortAnimationDuration;
	private int server_id;

	public LazyCursorAdapter(Context context, int layout, Cursor c,
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
		mobileno = c.getColumnIndex("mobile");
		server_id = c.getColumnIndex("server_id");
		hname = c.getColumnIndex("hname");
		m_flag = c.getColumnIndex("media_flag");

		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(final Context context, Cursor cursor, ViewGroup parent) {

		View v = inflater.inflate(layout, parent, false);
		final Cursor c = getCursor();

		int position = cursor.getPosition();

		TextView tvName = (TextView) v.findViewById(R.id.name_entry);
		TextView tvhname = (TextView) v.findViewById(R.id.edd_entry);
		TextView tvDob = (TextView) v.findViewById(R.id.dob_entry);
		TextView tvSlno = (TextView) v.findViewById(R.id.slno);
		TextView tvEdd = (TextView) v.findViewById(R.id.mobile_no);
		final ImageView thumb_image = (ImageView) v
				.findViewById(R.id.list_image);
		if (c.getInt(server_id) > 0) {
			thumb_image.setTag(c.getInt(server_id));

		} else {
			thumb_image.setTag(Integer.parseInt(c.getString(slCol)));
		}

		tvName.setText(c.getString(nameCol));
		Log.e("name", c.getString(nameCol));
		tvhname.setText(c.getString(hname));
		Log.e("name", c.getString(hname));
		Log.e("server_id", c.getInt(server_id) + "");

		tvEdd.setText("सम्भावित: " + DBAdapter.strtodate(c.getString(eddCol))
				+ " (" + c.getString(mnCol) + " माह)");

		tvSlno.setText(c.getString(slCol));
		tvDob.setText("मोबाइल नंबर : " + c.getString(mobileno));

		if (c.getInt(server_id) == 0) {
			Log.i("msakhi", "0_id");
			imgfile = Environment.getExternalStorageDirectory()
					+ File.separator + Workflow.bfolder + "/pdata/"
					+ c.getString(slCol) + ".jpg";
			Log.i("msakhi", imgfile);

		} else {
			Log.i("msakhielse", "datatest1_id");
			imgfile = Environment.getExternalStorageDirectory()
					+ File.separator + Workflow.bfolder + "/pdata/"
					+ c.getInt(server_id) + ".jpg";
			Log.d("datatest", "datatest" + c.getInt(server_id));
			Log.i("msakhi", "datatest" + imgfile);

		}

		try {
			imageLoader.DisplayThumbnail(imgfile, thumb_image);
		} catch (NullPointerException e) {
			e.printStackTrace();

		}
		// }
		// thumb_image.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// try{
		// // TouchImageView iv = new TouchImageView(context);
		// // Bitmap bit = BitmapFactory.decodeFile(imgfile);
		// // Bitmap bMapRotate=null;
		// // Matrix mat=new Matrix();
		// // if(bit.getWidth() > bit.getHeight())
		// // {
		// // mat.postRotate(90);
		// // bMapRotate = Bitmap.createBitmap(bit, 0,
		// 0,bit.getWidth(),bit.getHeight(), mat, true);
		// // bit.recycle();
		// // bit=null;
		// // iv.setImageBitmap(bMapRotate);
		// // }else
		// //
		// // iv.setImageBitmap(bit);
		// //
		// // // layout.addView(iv);
		// // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		// // context);
		// // alertDialogBuilder.setTitle(c.getString(nameCol));
		// //
		// // alertDialogBuilder.setView(iv);
		// // //AlertDialog alertDialog = alertDialogBuilder.create();
		// //
		// //
		// // // show it
		// // alertDialogBuilder.show();
		// // Drawable d = Drawable.createFromPath(imgfile);
		// // File f=new File(Environment.getExternalStorageDirectory()
		// // + File.separator + Workflow.bfolder + "/pdata/"
		// // + c.getString(slCol) + ".jpg");
		// if(imgfile==null){
		// Toast.makeText(context, "Image file Not Found", 2000).show();
		// }else{
		// int i=(Integer) v.getTag();
		// if( i>0)
		// {
		//
		// Log.d("onClickcalled","dataTest"+i);
		// imgfile = Environment.getExternalStorageDirectory()
		// + File.separator + Workflow.bfolder + "/pdata/"
		// + i + ".jpg";
		// zoomImageFromThumb(thumb_image, imgfile);
		// }
		// else
		// {
		// zoomImageFromThumb(thumb_image, imgfile);
		// }
		// }
		//
		// }catch(NullPointerException nl){
		// nl.printStackTrace();
		//
		// Toast.makeText(context, "Image file Not Found", 2000).show();
		//
		// }
		//
		// }
		// });
		// play_name.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View arg0) {
		// path = new
		// File(Environment.getExternalStorageDirectory().getAbsolutePath()
		// +"/"+ Workflow.bfolder+"/pdata/");
		// audioFile=path+"/"+String.valueOf(c.getString(slCol))+".3gp";
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
		// } else Toast.makeText(context,"Not recorded yet "+audioFile,
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		return v;
	}

	@Override
	public void bindView(View v, final Context context, Cursor c) {

		int position = c.getPosition();
		// if (position == Preg_reg_list.getCurrentSelectedItemId()) {
		// v.setBackgroundDrawable(v.getContext().getResources().getDrawable(R.drawable.gradient_bg_hover));
		// } else
		// v.setBackgroundDrawable(v.getContext().getResources().getDrawable(R.drawable.gradient_bg));
		TextView tvName = (TextView) v.findViewById(R.id.name_entry);
		TextView tvhname = (TextView) v.findViewById(R.id.edd_entry);
		TextView tvDob = (TextView) v.findViewById(R.id.dob_entry);
		TextView tvSlno = (TextView) v.findViewById(R.id.slno);
		TextView tvEdd = (TextView) v.findViewById(R.id.mobile_no);
		thumb_image = (ImageView) v.findViewById(R.id.list_image);
		// ImageView play_name = (ImageView) v.findViewById(R.id.play);
		tvName.setText(c.getString(nameCol));
		Log.e("name", c.getString(nameCol));
		tvhname.setText(c.getString(hname));
		tvEdd.setText("सम्भावित: " + DBAdapter.strtodate(c.getString(eddCol))
				+ " (" + c.getString(mnCol) + " माह)");
		tvSlno.setText(c.getString(slCol));
		tvDob.setText("मोबाइल नंबर : " + c.getString(mobileno));
		Log.e("cursorposition", position + "");
		// if(c.getInt(m_flag)==1)
		// {
		imgfile1 = Environment.getExternalStorageDirectory() + File.separator
				+ Workflow.bfolder + "/pdata/" + c.getString(slCol) + ".jpg";
		if (c.getInt(server_id) == 0) {
			Log.i("msakhi", "0_id");
			imgfile1 = Environment.getExternalStorageDirectory()
					+ File.separator + Workflow.bfolder + "/pdata/"
					+ c.getString(slCol) + ".jpg";
			Log.i("msakhi", imgfile1);
			// imageLoader.DisplayImage(imgfile, thumb_image);

		} else {
			Log.i("msakhielse", "datatest1_id");
			imgfile1 = Environment.getExternalStorageDirectory()
					+ File.separator + Workflow.bfolder + "/pdata/"
					+ c.getInt(server_id) + ".jpg";
			Log.d("datatest", "datatest" + c.getInt(server_id));
			Log.i("msakhi", "datatest" + imgfile1);

		}

		try {
			imageLoader.DisplayThumbnail(imgfile1, thumb_image);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if (c.getInt(server_id) > 0) {
			thumb_image.setTag(c.getInt(server_id));

		} else {
			thumb_image.setTag(Integer.parseInt(c.getString(slCol)));
		}

		thumb_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					// TouchImageView iv = new TouchImageView(context);
					// Bitmap bit = BitmapFactory.decodeFile(imgfile);
					// Bitmap bMapRotate=null;
					// Matrix mat=new Matrix();
					// if(bit.getWidth() > bit.getHeight())
					// {
					// mat.postRotate(90);
					// bMapRotate = Bitmap.createBitmap(bit, 0,
					// 0,bit.getWidth(),bit.getHeight(), mat, true);
					// bit.recycle();
					// bit=null;
					// iv.setImageBitmap(bMapRotate);
					// }else
					//
					// iv.setImageBitmap(bit);
					//
					// // layout.addView(iv);
					// AlertDialog.Builder alertDialogBuilder = new
					// AlertDialog.Builder(
					// context);
					// alertDialogBuilder.setTitle(c.getString(nameCol));
					//
					// alertDialogBuilder.setView(iv);
					// //AlertDialog alertDialog = alertDialogBuilder.create();
					//
					//
					// // show it
					// alertDialogBuilder.show();
					// Drawable d = Drawable.createFromPath(imgfile);
					// File f=new File(Environment.getExternalStorageDirectory()
					// + File.separator + Workflow.bfolder + "/pdata/"
					// + c.getString(slCol) + ".jpg");
					if (imgfile1 == null) {
						Toast.makeText(context, "Image file Not Found", 2000)
								.show();
					} else {
						int i = (Integer) v.getTag();
						if (i > 0) {

							Log.d("onClickcalled", "dataTest" + i);
							imgfile1 = Environment
									.getExternalStorageDirectory()
									+ File.separator
									+ Workflow.bfolder
									+ "/pdata/" + i + ".jpg";
							zoomImageFromThumb(thumb_image, imgfile1);
						} else {
							zoomImageFromThumb(thumb_image, imgfile1);
						}
					}

				} catch (NullPointerException nl) {
					nl.printStackTrace();

					Toast.makeText(context, "Image file Not Found", 2000)
							.show();
					Preg_reg_list.watcher = false;

				}

			}
		});

		// }

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
	private void zoomImageFromThumb(final View thumbView, String imageResId) {
		// If there's an animation in progress, cancel it
		// immediately and proceed with this one.
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}
		Preg_reg_list.watcher = true;
		// Load the high-resolution "zoomed-in" image.
		BitmapFactory.Options opts = new BitmapFactory.Options();
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inTempStorage = new byte[1 * 1024];
		// Bitmap bmp = BitmapFactory.decodeStream(b, null, opts);

		b = BitmapFactory.decodeFile(imageResId, o);
		Bitmap bMapRotate = null;
		Matrix mat = new Matrix();
		if (b.getWidth() > b.getHeight()) {
			mat.postRotate(90);
			bMapRotate = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
					b.getHeight(), mat, true);
			b.recycle();
			b = null;
			Preg_reg_list.expandedImageView.setImageBitmap(bMapRotate);
		} else
			// bMapRotate = Bitmap.createBitmap(b, 0,
			// 0,b.getWidth(),b.getHeight(), mat, true);
			Preg_reg_list.expandedImageView.setImageBitmap(b);

		// Calculate the starting and ending bounds for the zoomed-in image.
		// This step involves lots of math. Yay, math.
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		// The start bounds are the global visible rectangle of the thumbnail,
		// and the final bounds are the global visible rectangle of the
		// container
		// view. Also set the container view's offset as the origin for the
		// bounds, since that's the origin for the positioning animation
		// properties (X, Y).
		thumbView.getGlobalVisibleRect(startBounds);
		Preg_reg_list.ll.getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the "center crop" technique. This prevents undesirable
		// stretching during the animation. Also calculate the start scaling
		// factor (the end scaling factor is always 1.0).
		float startScale;
		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
				.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}

		// Hide the thumbnail and show the zoomed-in view. When the animation
		// begins, it will position the zoomed-in view in the place of the
		// thumbnail.
		thumbView.setAlpha(0f);
		Preg_reg_list.expandedImageView.setVisibility(View.VISIBLE);

		// Set the pivot point for SCALE_X and SCALE_Y transformations
		// to the top-left corner of the zoomed-in view (the default
		// is the center of the view).
		Preg_reg_list.expandedImageView.setPivotX(0f);
		Preg_reg_list.expandedImageView.setPivotY(0f);

		// Construct and run the parallel animation of the four translation and
		// scale properties (X, Y, SCALE_X, and SCALE_Y).
		AnimatorSet set = new AnimatorSet();
		set.play(
				ObjectAnimator.ofFloat(Preg_reg_list.expandedImageView, View.X,
						startBounds.left, finalBounds.left))
				.with(ObjectAnimator.ofFloat(Preg_reg_list.expandedImageView,
						View.Y, startBounds.top, finalBounds.top))
				.with(ObjectAnimator.ofFloat(Preg_reg_list.expandedImageView,
						View.SCALE_X, startScale, 1f))
				.with(ObjectAnimator.ofFloat(Preg_reg_list.expandedImageView,
						View.SCALE_Y, startScale, 1f));
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;

		// Upon clicking the zoomed-in image, it should zoom back down
		// to the original bounds and show the thumbnail instead of
		// the expanded image.
		final float startScaleFinal = startScale;
		Preg_reg_list.expandedImageView
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (mCurrentAnimator != null) {
							mCurrentAnimator.cancel();

						}

						// Animate the four positioning/sizing properties in
						// parallel,
						// back to their original values.
						AnimatorSet set = new AnimatorSet();
						set.play(
								ObjectAnimator.ofFloat(
										Preg_reg_list.expandedImageView,
										View.X, startBounds.left))
								.with(ObjectAnimator.ofFloat(
										Preg_reg_list.expandedImageView,
										View.Y, startBounds.top))
								.with(ObjectAnimator.ofFloat(
										Preg_reg_list.expandedImageView,
										View.SCALE_X, startScaleFinal))
								.with(ObjectAnimator.ofFloat(
										Preg_reg_list.expandedImageView,
										View.SCALE_Y, startScaleFinal));
						set.setDuration(mShortAnimationDuration);
						set.setInterpolator(new DecelerateInterpolator());
						set.addListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								thumbView.setAlpha(1f);
								Preg_reg_list.expandedImageView
										.setVisibility(View.GONE);
								mCurrentAnimator = null;
								Preg_reg_list.watcher = false;
							}

							@Override
							public void onAnimationCancel(Animator animation) {
								thumbView.setAlpha(1f);
								Preg_reg_list.expandedImageView
										.setVisibility(View.GONE);
								mCurrentAnimator = null;
							}
						});
						set.start();
						mCurrentAnimator = set;
						// ((BitmapDrawable)Preg_reg_list.expandedImageView.getDrawable()).getBitmap().recycle();

					}

				});
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFile(String res, int reqWidth,
			int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(res, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(res, options);
	}

	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 100;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}
}
