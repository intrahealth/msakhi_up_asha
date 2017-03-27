package com.asha.mnewborncareasha;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class Connectivity {
    /*
     * HACKISH: These constants aren't yet available in my API level (7), but I need to handle these cases if they come up, on newer versions
     */
    public static final int NETWORK_TYPE_EHRPD=14; // Level 11
    public static final int NETWORK_TYPE_EVDO_B=12; // Level 9
    public static final int NETWORK_TYPE_HSPAP=15; // Level 13
    public static final int NETWORK_TYPE_IDEN=11; // Level 8
    public static final int NETWORK_TYPE_LTE=13; // Level 11

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
    /**
     * Check if there is fast connectivity
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() && Connectivity.isConnectionFast(info.getType(),info.getSubtype()));
    }

    public static boolean isConnectingToInternet(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }
    
    /**
     * Check if the connection is fast
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType){
        if(type==ConnectivityManager.TYPE_WIFI){
            System.out.println("CONNECTED VIA WIFI");
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            // NOT AVAILABLE YET IN API LEVEL 7
            case Connectivity.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case Connectivity.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case Connectivity.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case Connectivity.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps 
            case Connectivity.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            // Unknown
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                return false;
            }
        }else{
            return false;
        }
    }

    public static void setMobileDataEnabled(Context context, boolean enabled) {
  	  try {  
  	  final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  	    final Class conmanClass = Class.forName(conman.getClass().getName());
  	    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
  	    iConnectivityManagerField.setAccessible(true);
  	    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
  	    final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
  	    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
  	    setMobileDataEnabledMethod.setAccessible(true);	    
				setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  	}	      

    
}
