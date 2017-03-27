package com.asha.mnewborncareasha;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class UpdateServerData extends AsyncTask<String, Void, String> {

	Context context;
	DBAdapter mydb;
	private String url_data;
	ProgressDialog pd;
	URL url_Link;

	UpdateServerData(Context con) {
		context = con;
	}

	@Override
	protected String doInBackground(String... params) {

		try {
			url_data = AppVariable.getUpdate_API_INDEX(context);
			mydb = DBAdapter.getInstance(context);
			String data = POST(url_data, "data", null);
			Log.e("jsonString", data);
			ParseJson(data);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	private void ParseJson(String data) throws JSONException {
		JSONArray array_json = new JSONArray(data);
		for (int count = 0; count < array_json.length(); count++) {
			if (count == 0) {
				insertDataIntoAnc_Ans(array_json.getJSONArray(count));
			} else if (count == 1) {
				insertDataIntoAns_Pnc(array_json.getJSONArray(count));
			} else if (count == 2) {
				insertDataIntoImm_dets(array_json.getJSONArray(count));
			} else if (count == 3) {
				insertDataIntoSch_Visits(array_json.getJSONArray(count));
			} else {

			}

		}

	}

	private void insertDataIntoSch_Visits(JSONArray jsonArray)
			throws JSONException {
		mydb.deletAllAncData("sch_visits");
		for (int c = 0; c < jsonArray.length(); c++) {
			JSONObject obj_Json = jsonArray.getJSONObject(c);
			int _id = obj_Json.getInt("_id");
			int pid = obj_Json.getInt("pid");
			String svd = obj_Json.getString("svd");
			String avd = obj_Json.getString("avd");
			int seq = obj_Json.getInt("seq");
			// int optflag=obj_Json.getInt("optflag");
			String answrg1 = obj_Json.getString("answrg1");
			String answrg2 = obj_Json.getString("answrg2");
			String answrg3 = obj_Json.getString("answrg3");
			String answrg4 = obj_Json.getString("answrg4");
			String answrg5 = obj_Json.getString("answrg5");
			String answrg0 = obj_Json.getString("answrg0");
			String gsumm = obj_Json.getString("gsumm");
			mydb.insert_sch_visits(_id, pid, svd, avd, seq, answrg1, answrg2,
					answrg3, answrg4, answrg5, answrg0, gsumm);
		}

	}

	private void insertDataIntoImm_dets(JSONArray jsonArray)
			throws JSONException {
		mydb.deletAllAncData("imm_det");
		for (int c = 0; c < jsonArray.length(); c++) {
			JSONObject obj_Json = jsonArray.getJSONObject(c);
			int im_id = obj_Json.getInt("im_id");
			int pid = obj_Json.getInt("pid");
			String imdt = obj_Json.getString("imdt");
			mydb.insert_im_dets(im_id, pid, imdt);
		}

	}

	private void insertDataIntoAnc_Ans(JSONArray jsonArray)
			throws JSONException {
		mydb.deletAllAncData("ans_anc");
		for (int c = 0; c < jsonArray.length(); c++) {
			JSONObject obj_Json = jsonArray.getJSONObject(c);
			int pid = obj_Json.getInt("pid");
			int qid = obj_Json.getInt("qid");
			int tri = obj_Json.getInt("tri");
			int optflag = obj_Json.getInt("optflag");
			int ans = obj_Json.getInt("ans");
			int qnty = obj_Json.getInt("qnty");
			int dtval = obj_Json.getInt("dtval");
			int dtm = obj_Json.getInt("dtm");
			mydb.insertAns_Anc(pid, qid, tri, optflag, ans, qnty, dtval, dtm);

		}

	}

	private void insertDataIntoAns_Pnc(JSONArray arr) throws JSONException {
		mydb.deletAllAncData("ans_pnc");
		for (int c = 0; c < arr.length(); c++) {
			JSONObject obj_Json = arr.getJSONObject(c);
			int pid = obj_Json.getInt("pid");
			int qid = obj_Json.getInt("qid");
			int hvid = obj_Json.getInt("hvid");
			int optflag = obj_Json.getInt("optflag");
			int ans = obj_Json.getInt("ans");
			int qnty = obj_Json.getInt("qnty");
			int dtval = obj_Json.getInt("dtval");
			int dtm = obj_Json.getInt("dtm");
			mydb.insertAns_Pnc(pid, qid, hvid, optflag, ans, qnty, dtval, dtm);

		}

	}

	public String POST(String url, String type, String server_id)
			throws Exception {
		InputStream inputStream = null;
		String result = "";
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String uid = prefs.getString("id", "101");

		url_Link = new URL(url);
		Log.e("url", url + uid);
		inputStream = url_Link.openConnection().getInputStream();
		InputStream in = new BufferedInputStream(inputStream);
		if (inputStream != null)
			result = convertInputStreamToString(in);
		else
			result = "Did not work!";

		Log.i("result", result);
		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));

		String line;
		StringBuffer result = new StringBuffer();
		while ((line = bufferedReader.readLine()) != null) {
			result.append(line);
		}

		inputStream.close();
		return result.toString();

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pd = new ProgressDialog(context);
		pd.setMessage("Loading...");
		pd.show();
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		pd.dismiss();
		super.onPostExecute(result);
	}
}
