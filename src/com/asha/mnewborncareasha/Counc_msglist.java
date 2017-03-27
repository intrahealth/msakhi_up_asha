package com.asha.mnewborncareasha;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class Counc_msglist extends ListActivity {
	private DBAdapter mydb;
	TextView txtCount;
	int id=0;
	String mname;
	Cursor c;
	String[] items;
	int[] keys;
	 

	 @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.counc_msglist);
	    //TextView tvHead=(TextView)findViewById(R.id.tvHead);
		    mydb=DBAdapter.getInstance(getApplicationContext());
		int mod_id=getIntent().getIntExtra("id", 0);    
	    c=mydb.getMsgList(mod_id);
	    startManagingCursor(c);
	    String[] from = new String[] {"msg"};
	    int[] to = new int[] {R.id.tvItem};

	    SimpleCursorAdapter ca=new SimpleCursorAdapter(getApplicationContext(),R.layout.list_sel_row,c,from,to);
	    setListAdapter(ca);
	    ListView lst=(ListView)findViewById(android.R.id.list);
	    lst.setOnItemClickListener(new OnItemClickListener()
	    {

			@Override
			public void onItemClick(AdapterView<?> adapterView,  View v,int position, long arg3) {
				// TODO Auto-generated method stub
				v.setSelected(true);
				Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
				Intent intent = new Intent(getApplicationContext(),Counc_info.class);
				intent.putExtra("gid", cursor.getInt(0));
				startActivity(intent);
			}

	    });
	  }
	
}