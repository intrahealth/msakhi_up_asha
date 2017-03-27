package com.asha.mnewborncareasha;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;

public class DataUploadService {

	public static String dataSyncUrl = "http://192.168.1.82/webbackup06jun15/asha_test/insertashatest.php";
	DBAdapter db;
	JSONObject json;

	public DataUploadService(Context ctx, JSONObject jsn) {
		db = new DBAdapter(ctx);
		json = jsn;
	}

	public void syncSQLiteMySQLDB(JSONObject jsn) {
		// Create AsycHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		// ArrayList<HashMap<String, String>> userList =
		// controller.getAllUsers();
		// if(userList.size()!=0){
		// if(controller.dbSyncCount() != 0){

		params.put("usersJSON", jsn);
		System.out.println("params=" + params);
		client.post(dataSyncUrl, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				System.out.println(response);

				try {
					JSONArray arr = new JSONArray(response);
					System.out.println(arr.length());
					for (int i = 0; i < arr.length(); i++) {
						JSONObject obj = (JSONObject) arr.get(i);
						System.out.println(obj.get("_id"));
						db.setPdataList(obj.get("_id").toString(), "1");
					}
					// Toast.makeText(getApplicationContext(),
					// "DB Sync completed!", Toast.LENGTH_LONG).show();
					System.out.println("DB Sync completed!");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					// Toast.makeText(getApplicationContext(),
					// "Error Occured [Server's JSON response might be invalid]!",
					// Toast.LENGTH_LONG).show();
					System.out
							.println("Error Occured [Server's JSON response might be invalid]!");
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Throwable error,
					String content) {
				// TODO Auto-generated method stub
				// prgDialog.hide();
				if (statusCode == 404) {
					System.out.println("Requested resource not found");
					// Toast.makeText(getApplicationContext(),
					// "Requested resource not found",
					// Toast.LENGTH_LONG).show();
				} else if (statusCode == 500) {
					System.out.println("Something went wrong at server end");
					// Toast.makeText(getApplicationContext(),
					// "Something went wrong at server end",
					// Toast.LENGTH_LONG).show();
				} else {
					// Toast.makeText(getApplicationContext(),
					// "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
					// Toast.LENGTH_LONG).show();
					System.out.println("Unexpected Error occcured!");
				}
			}
		});
		// }else{
		// //Toast.makeText(getApplicationContext(),
		// "SQLite and Remote MySQL DBs are in Sync!",
		// Toast.LENGTH_LONG).show();
		// }
		// }else{
		// //Toast.makeText(getApplicationContext(),
		// "No data in SQLite DB, please do enter User name to perform Sync action",
		// Toast.LENGTH_LONG).show();
		// }
	}

}
