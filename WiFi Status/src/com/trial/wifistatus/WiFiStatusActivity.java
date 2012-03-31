package com.trial.wifistatus;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

public class WiFiStatusActivity extends Activity {
    /** Called when the activity is first created. */
	
	private TextView stateText;
	private TextView networkState;
	private TextView ssid;
	private TextView mac;
	private TextView bssid;
	private TextView rssi;
	private WifiManager wifi = null;
	private WifiInfo wifi_info = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        stateText = (TextView) findViewById(R.id.state_text);
        networkState=(TextView) findViewById(R.id.network_state);
        findViewById(R.id.ssid_label);
        ssid = (TextView) findViewById(R.id.ssid);
        findViewById(R.id.bssid_label);
        bssid=(TextView) findViewById(R.id.bssid);
        findViewById(R.id.mac_label);
        mac= (TextView) findViewById (R.id.mac);
        findViewById(R.id.rssi_label);
        rssi=(TextView) findViewById(R.id.rssi);
        
        check_status();
        
        wifi_details();
                
    }
    
    void check_status(){
    	
    	final WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
    	wifi_info = wifi.getConnectionInfo();
    	stateText.setText("Wi-Fi is OFF");
    	if(wifi.isWifiEnabled()){
    		//Toast.makeText(this, "WiFi Available", Toast.LENGTH_LONG).show();
    		stateText.setText("Wi-Fi is ON");
    	}
    	else{
    		//Toast.makeText(this, "WiFi NOT Available", Toast.LENGTH_LONG).show();
    		stateText.setText("Wi-Fi is OFF");
    	}
    
    }
    
    void wifi_details(){
    	
    	if(wifi_info.getSSID() == null){
    		networkState.setText("Not connected to a Wi-Fi Network");
    	}
    	else{
    		networkState.setText("Connected to a Wi-Fi Network");
    		ssid.setText(wifi_info.getSSID());
    		bssid.setText(wifi_info.getBSSID());
    		mac.setText(wifi_info.getMacAddress());
    		
    		int strength = wifi.calculateSignalLevel(wifi_info.getRssi(), 5);
    		String rssi_str= String.format("%s",strength);
    		rssi.setText(rssi_str);
    		 		
    		
    	}
    	
    
    }
    
}