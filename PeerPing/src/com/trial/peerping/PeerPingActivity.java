package com.trial.peerping;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PeerPingActivity extends Activity {
	
	private EditText subnetInput;
	private Button pingButton;
	private TextView pingingLabel, peerIp, pingResult, availableCount;
	private TextView avail1,avail2,avail3;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViewById(R.id.subnet_label);
        subnetInput = (EditText) findViewById(R.id.subnet_input);
        pingButton = (Button) findViewById(R.id.ping_button);
        pingingLabel = (TextView) findViewById(R.id.pinging_label);
        peerIp = (TextView) findViewById(R.id.peer_ip);
        pingResult = (TextView) findViewById(R.id.ping_result);        
        findViewById(R.id.available_label);
        availableCount = (TextView) findViewById(R.id.available_count);
        pingingLabel.setText("Idle");
        pingButton.setOnClickListener(pingButtonListener);  
        
        avail1 = (TextView) findViewById(R.id.avail_1);
        avail2 = (TextView) findViewById(R.id.avail_2);
        avail3 = (TextView) findViewById(R.id.avail_3);
        
        String peer = null;
        String result = null;
        
        while (true) {
        peerIp.setText(peer);
        pingResult.setText(result);
        }
    }
    
    private final OnClickListener pingButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			
			startPinging();
			
			
			
		}
    	
    };
    
    public void startPinging() {
    	Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				int timeout = 1000;
				int count = 0;
				
				for(int i=199;i<205;i++) {
					String peer =  subnetInput.getText() + "." + i;
					//peerIp.setText(peer);
					
					
					
					try {
						if(InetAddress.getByName(peer).isReachable(timeout)) {
							pingResult.setText("Is Available");
							
							count++;
							if(count==1)
								avail1.setText(peer);
							if(count==2)
								avail2.setText(peer);
							if(count==3)
								avail3.setText(peer);
							
						}
						else
							
							pingResult.setText("Is Not Available");
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				availableCount.setText(Integer.toString(count));
					
						
					
				}
				
			}
    		
    	});
    	t.start();
    }
    
}