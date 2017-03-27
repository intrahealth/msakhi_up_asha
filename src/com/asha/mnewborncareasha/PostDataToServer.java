package com.asha.mnewborncareasha;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public final class PostDataToServer {
	private static final int MAX_ATTEMPTS = 5;
	private static final int BACKOFF_MILLI_SECONDS = 2000;
	private static final Random random = new Random();
	static String TAG = "test";
	static Context ctx;

	static void register(final Context context, String name, String email,
			final String regId) {
		Log.i(TAG, "registering device (regId = " + regId + ")");
		String serverUrl = CommonUtilities.SERVER_URL;
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);
		params.put("name", name);
		params.put("email", email);
		ctx = context;
		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				post(context, serverUrl, name, email, regId);
				return;
			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		String message = context.getString(R.string.hello_world, MAX_ATTEMPTS);
		CommonUtilities.displayMessage(context, message);
		System.out.println(CommonUtilities.EXTRA_MESSAGE);
	}

	/**
	 * Issue a POST request to the server.
	 * 
	 * @param endpoint
	 *            POST address.
	 * @param params
	 *            request parameters.
	 * 
	 * @throws IOException
	 *             propagated from POST.
	 */
	private static void post(Context ctx, String endpoint, String name,
			String email, final String regId) throws IOException {

		try {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(endpoint);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("regId", regId));
			nameValuePairs.add(new BasicNameValuePair("name", name));
			nameValuePairs.add(new BasicNameValuePair("email", email));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpclient.execute(httppost, responseHandler);

			// This is the response from a php application
			String reverseString = response;
			// Toast.makeText(ctx, "response" + reverseString,
			// Toast.LENGTH_LONG).show();
			System.out.println("responce=" + reverseString);
		} catch (ClientProtocolException e) {
			// Toast.makeText(ctx, "CPE response " + e.toString(),
			// Toast.LENGTH_LONG).show();
			System.out.println("ClientProExcp=" + e.toString());
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// Toast.makeText(ctx, "IOE response " + e.toString(),
			// Toast.LENGTH_LONG).show();
			// TODO Auto-generated catch block
			System.out.println("IO Excp=" + e.toString());
		}
		;

		// end postData()
	}

}
