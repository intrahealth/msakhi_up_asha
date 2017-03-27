package com.asha.mnewborncareasha;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SendDataReciever extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent service = new Intent(context, SendDataService.class);
	    Log.i("LNJP","OnRecievecalled");
	  
	    context.startService(service);	
		
	}

}
