package com.asha.mnewborncareasha;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
//import android.database.SQLException;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Counc_info extends Activity {
	int mnth=0,gid=0,yind,nind,nqid,qtype=-1,y,n,grp=0,clr=0;
	private DBAdapter mydb;
	//Typeface face;
	Button btnQuest,btnBack,btnNext;
	LinearLayout llBack;
	ImageView imgCounc;
	ImageButton btnHelp;
	TextView tvHead;
	MediaPlayer mp,beep_mp;
	Cursor c;
	//String grpinfo[]={"f'k'kq ds tkap","ek¡ fd tkap","thok.kq laØe.k tkap","nLrjksx fd tkap",
	//		"Lruiku lEcaf/kr tkap","Vhdkdj.k lEcaf/kr tkap"};
	//String grpinfo[]={"शिश�? की जांच","मां की जांच","जीवाण�? संक�?रमण जांच","दस�?तरोग की जांच","स�?तनपान सम�?बन�?धित जांच"};
	int click_cnt=1;
	String pghead="",ghead="";
	Context ctx;
	String imgfile;
	// Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.counc_info);
	    tvHead=(TextView)findViewById(R.id.tvHead);
	    imgCounc=(ImageView)findViewById(R.id.imgCounc);
	    llBack=(LinearLayout)findViewById(R.id.llRemedyBack);
	    //tvCatgr=(TextView)findViewById(R.id.tvCatgr);
      //  face=Typeface.createFromAsset(getAssets(),getString(R.string.hindi_font));
        //tvHead.setTypeface(face);
        //tvCatgr.setTypeface(face);
        btnQuest=(Button)findViewById(R.id.btnQuest);
        
        
        //LinearLayout llRemedyBack=(LinearLayout)findViewById(R.id.llRemedyBack);
        //btnQuest.setTypeface(face);
        tvHead.setText("सलाह");//Classification
        //btnQuest.setText("¼1½ igyh tkap");
        ctx=getApplicationContext();

        Bundle extras = getIntent().getExtras();
        if (extras!= null) {
        	mnth = extras.getInt("mnth");
        	gid = extras.getInt("gid");
        }
        beep_mp=MediaPlayer.create(getApplicationContext(), R.raw.beep7);
        try {
			beep_mp.prepare();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
        mp=MediaPlayer.create(getApplicationContext(), R.raw.hv1);
        AssetFileDescriptor afd;
		try {
			afd = getAssets().openFd("cosnd/"+String.valueOf(mnth)+"bcoa.3gp"); //Beginning counsel audio
			mp.reset();
			mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			mp.prepare();
			mp.start();	
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
	    mydb=DBAdapter.getInstance(getApplicationContext());
	    /*
        mydb = new DBAdapter(this);
	    try {
	    	//mydb.createDataBase();
			mydb.openDataBase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	    c=mydb.getCouncInfo(mnth,gid);
	    if (c.moveToFirst())
	    {
	    //clr=c.getInt(c.getColumnIndex("clr"));
	    	pghead=c.getString(c.getColumnIndex("grphead"));ghead=pghead;
	    btnQuest.setText(ghead);
	    llBack.setBackgroundResource(R.color.clrYellow);
		imgfile=Environment.getExternalStorageDirectory()+ File.separator +Workflow.bfolder+ "/counsel/coimg/"+
       		 String.valueOf(nqid)+"gco.jpg";
        Bitmap bmp = BitmapFactory.decodeFile(imgfile);
        imgCounc.setImageBitmap(bmp);

	    //tvCatgr.setText(btnQuest.getText());
	    }
	    addListenerOnButton();
    }
	
	private void addListenerOnButton() {
		
		btnHelp=(ImageButton)findViewById(R.id.btnHelp);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnBack =(Button) findViewById(R.id.btnBack);
		//btnNext.setTypeface(face);
		//btnBack.setTypeface(face);
		btnBack.setVisibility(android.view.View.INVISIBLE);
		//npNumInput=(NumberPicker)findViewById(R.id.npNumInput);
		nqid=0;				
		c=mydb.getCouncInfo(mnth,gid);
		//c.moveToFirst();		
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				mp.reset();
				beep_mp.start();
				click_cnt++;
				if (click_cnt==1) {
					btnNext.setBackgroundResource(R.drawable.custom_button_green);
				} else if (click_cnt==2)
				{
				click_cnt=0;
				btnNext.setBackgroundResource(R.drawable.custom_button_yellow);
					// TODO Auto-generated method stub
				String qtext="";
				//c=mydb.getNextQuest(nqid);
				c.moveToNext();
				if(!c.isAfterLast())
				{
					String prompt=c.getString(c.getColumnIndex("prompt"));
					tvHead.setText(prompt);
					ghead=c.getString(c.getColumnIndex("grphead"));
					String gbreak="";
					if (!ghead.equals(pghead)) 
						{ 
						pghead=ghead;
						c.moveToPrevious(); 
						qtext=ghead;
						gbreak="g";
						llBack.setBackgroundResource(R.color.clrYellow);
						} 
					else
						{
						llBack.setBackgroundResource(R.color.clrOffwhite);
						String ctext="";
						if (!c.isNull(c.getColumnIndex("ctext"))) ctext=c.getString(c.getColumnIndex("ctext")); 
						//qtext=String.valueOf(nqid)+") "+ctext;
						qtext=ctext;
						}
					try {
						nqid=c.getInt(c.getColumnIndex("_id"));
				   		String audioFile;
			    		audioFile=Environment.getExternalStorageDirectory().getAbsolutePath()
			    				+"/"+Workflow.bfolder+ "/counsel/cosnd/"+String.valueOf(nqid)+"coa.3gp";
			    		mp.reset();
			    		mp.setDataSource(audioFile);
						mp.prepare();
						mp.start();	
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
			
					imgfile=Environment.getExternalStorageDirectory()+ "/"+Workflow.bfolder+ "/counsel/coimg/"+
		                		 String.valueOf(nqid)+gbreak+"co.jpg";
		                 Bitmap bmp = BitmapFactory.decodeFile(imgfile);
		                 imgCounc.setImageBitmap(bmp);
				
				//tvHead.setText(grpinfo[c.getInt(c.getColumnIndex("grp"))]);
				if (c.getInt(c.getColumnIndex("help"))==1)
					btnHelp.setVisibility(android.view.View.VISIBLE);
				else btnHelp.setVisibility(android.view.View.GONE);
				btnBack.setVisibility(android.view.View.VISIBLE);
				btnQuest.setText(qtext);
				//c.moveToNext();
				} else {
					//Ending No more questions are left
					//Toast.makeText(getApplicationContext(),"Going back..", Toast.LENGTH_SHORT).show();
					openDialog();
					/*
					btnNext.setVisibility(android.view.View.INVISIBLE);
					mp.reset();
					mp.release();
					beep_mp.release();
					mydb.close();
					finish();
					*/
				}
				
				//Toast.makeText(getApplicationContext(),"Record added successfully ..", Toast.LENGTH_SHORT).show();
			}
		}
		});
	
		btnQuest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {				
				try {
			   		String audioFile;
		    		audioFile=Environment.getExternalStorageDirectory().getAbsolutePath()
		    				+"/"+Workflow.bfolder+ "/counsel/cosnd/"+String.valueOf(nqid)+"coa.3gp";
		    		mp.reset();
		    		mp.setDataSource(audioFile);
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
				// TODO Auto-generated method stub
				String qtext="";
				//c=mydb.getNextQuest(nqid);
				c.moveToPrevious();
				if(!c.isBeforeFirst() && !c.isAfterLast())
				{
					try {
						nqid=c.getInt(c.getColumnIndex("_id"));
				   		String audioFile;
			    		audioFile=Environment.getExternalStorageDirectory().getAbsolutePath()
			    				+"/"+Workflow.bfolder+"/counsel/cosnd/"+String.valueOf(nqid)+"coa.3gp";
			    		mp.reset();
			    		mp.setDataSource(audioFile);
						mp.prepare();
						mp.start();	
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				tvHead.setText(c.getString(c.getColumnIndex("prompt")));	
				//qtext=String.valueOf(nqid)+" "+c.getString(c.getColumnIndex("ctext"));
				qtext=c.getString(c.getColumnIndex("ctext"));
				//tvHead.setText(grpinfo[c.getInt(c.getColumnIndex("grp"))]);
				if (c.getInt(c.getColumnIndex("help"))==1)
					btnHelp.setVisibility(android.view.View.VISIBLE);
				else btnHelp.setVisibility(android.view.View.GONE);
				btnQuest.setText(qtext);
				
				} else {
					//Ending No more questions are left
					//Toast.makeText(getApplicationContext(),"Going back..", Toast.LENGTH_SHORT).show();
					btnBack.setVisibility(android.view.View.INVISIBLE);
					mp.reset();
					//finish();
				}
				
				//Toast.makeText(getApplicationContext(),"Record added successfully ..", Toast.LENGTH_SHORT).show();

			}
		});

		
		btnHelp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String movieurl = Environment.getExternalStorageDirectory() + "/"+Workflow.bfolder+"/v3gp/"+String.valueOf(nqid)+"cov.3gp";
				if (new File(movieurl.toString()).exists())
				{
				mp.reset();	
				Intent intentToPlayVideo = new Intent(Intent.ACTION_VIEW);
				intentToPlayVideo.setDataAndType(Uri.parse(movieurl), "video/*");
				startActivity(intentToPlayVideo);
				} else
					Toast.makeText(getApplicationContext(),"Resource not available..", Toast.LENGTH_SHORT).show();
			}
		});
		imgCounc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	
            	Drawable d = Drawable.createFromPath(imgfile);
                zoomImageFromThumb(imgCounc, d);
            }
        });

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    	
		
	}
	
	@Override
	public void onBackPressed() {
		Toast.makeText(getApplicationContext(),"सलाह समाप्त नहीं हुआ ..", Toast.LENGTH_SHORT).show();
	}

	public void openDialog()
	{
		AlertDialog.Builder customDialog 
	     = new AlertDialog.Builder(Counc_info.this);
		customDialog
         .setMessage("सलाह समाप्त! आगे ?")
         .setCancelable(false)
         .setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
             @Override
			public void onClick(DialogInterface dialog, int id) {
					btnNext.setVisibility(android.view.View.INVISIBLE);
					mp.reset();
					mp.release();
					beep_mp.release();
					mydb.close();
					finish();
             }
         })
         .setNegativeButton("ना", null)
         .show();
	}
	private void zoomImageFromThumb(final View thumbView, Drawable imageResId) {
	    // If there's an animation in progress, cancel it
	    // immediately and proceed with this one.
	    if (mCurrentAnimator != null) {
	        mCurrentAnimator.cancel();
	    }

	    // Load the high-resolution "zoomed-in" image.
	    final ImageView expandedImageView = (ImageView) findViewById(
	            R.id.expanded_image);
	    expandedImageView.setImageDrawable(imageResId);

	    // Calculate the starting and ending bounds for the zoomed-in image.
	    // This step involves lots of math. Yay, math.
	    final Rect startBounds = new Rect();
	    final Rect finalBounds = new Rect();
	    final Point globalOffset = new Point();

	    // The start bounds are the global visible rectangle of the thumbnail,
	    // and the final bounds are the global visible rectangle of the container
	    // view. Also set the container view's offset as the origin for the
	    // bounds, since that's the origin for the positioning animation
	    // properties (X, Y).
	    thumbView.getGlobalVisibleRect(startBounds);
	    findViewById(R.id.container)
	            .getGlobalVisibleRect(finalBounds, globalOffset);
	    startBounds.offset(-globalOffset.x, -globalOffset.y);
	    finalBounds.offset(-globalOffset.x, -globalOffset.y);

	    // Adjust the start bounds to be the same aspect ratio as the final
	    // bounds using the "center crop" technique. This prevents undesirable
	    // stretching during the animation. Also calculate the start scaling
	    // factor (the end scaling factor is always 1.0).
	    float startScale;
	    if ((float) finalBounds.width() / finalBounds.height()
	            > (float) startBounds.width() / startBounds.height()) {
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
	    expandedImageView.setVisibility(View.VISIBLE);

	    // Set the pivot point for SCALE_X and SCALE_Y transformations
	    // to the top-left corner of the zoomed-in view (the default
	    // is the center of the view).
	    expandedImageView.setPivotX(0f);
	    expandedImageView.setPivotY(0f);

	    // Construct and run the parallel animation of the four translation and
	    // scale properties (X, Y, SCALE_X, and SCALE_Y).
	    AnimatorSet set = new AnimatorSet();
	    set
	            .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
	                    startBounds.left, finalBounds.left))
	            .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
	                    startBounds.top, finalBounds.top))
	            .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
	            startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
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
	    expandedImageView.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
	            if (mCurrentAnimator != null) {
	                mCurrentAnimator.cancel();
	            }

	            // Animate the four positioning/sizing properties in parallel,
	            // back to their original values.
	            AnimatorSet set = new AnimatorSet();
	            set.play(ObjectAnimator
	                        .ofFloat(expandedImageView, View.X, startBounds.left))
	                        .with(ObjectAnimator
	                                .ofFloat(expandedImageView, 
	                                        View.Y,startBounds.top))
	                        .with(ObjectAnimator
	                                .ofFloat(expandedImageView, 
	                                        View.SCALE_X, startScaleFinal))
	                        .with(ObjectAnimator
	                                .ofFloat(expandedImageView, 
	                                        View.SCALE_Y, startScaleFinal));
	            set.setDuration(mShortAnimationDuration);
	            set.setInterpolator(new DecelerateInterpolator());
	            set.addListener(new AnimatorListenerAdapter() {
	                @Override
	                public void onAnimationEnd(Animator animation) {
	                    thumbView.setAlpha(1f);
	                    expandedImageView.setVisibility(View.GONE);
	                    mCurrentAnimator = null;
	                }

	                @Override
	                public void onAnimationCancel(Animator animation) {
	                    thumbView.setAlpha(1f);
	                    expandedImageView.setVisibility(View.GONE);
	                    mCurrentAnimator = null;
	                }
	            });
	            set.start();
	            mCurrentAnimator = set;
	        }
	    });
	}
}
