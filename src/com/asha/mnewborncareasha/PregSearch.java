package com.asha.mnewborncareasha;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

public class PregSearch extends ListActivity {

	private DBAdapter mydb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preg_search);
        mydb=DBAdapter.getInstance(getApplicationContext());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preg_search, menu);
		return true;
	}

	  private void showResults(String query) {
		    Cursor cursor = mydb.searchPregQuery(query);
		    startManagingCursor(cursor);
		    String[] from = new String[] { "name" };
		    int[] to = new int[] { R.id.name_entry };

		    SimpleCursorAdapter records = new SimpleCursorAdapter(this,
		        R.layout.list_row, cursor, from, to);
		    setListAdapter(records);
		  }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
