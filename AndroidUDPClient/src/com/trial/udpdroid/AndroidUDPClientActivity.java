package com.trial.udpdroid;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AndroidUDPClientActivity extends Activity {
	
	
	private EditText sendText,targetIP;
	private Button sendButton;
	private TextView receivedText,peerStatus,packetStatus;
	
	     
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        findViewById(R.id.ip_label);
        targetIP = (EditText) findViewById(R.id.target_ip);
        findViewById(R.id.msg_label);
        sendText = (EditText) findViewById(R.id.send_text);
        sendButton = (Button) findViewById(R.id.send_button);
        peerStatus = (TextView) findViewById(R.id.peer_status);
        packetStatus = (TextView) findViewById(R.id.packet_status);
        findViewById(R.id.received_label);
        receivedText = (TextView) findViewById(R.id.received_text);
        
        sendButton.setOnClickListener(sendListener);
        
        
    	
        
    }
    
    private final OnClickListener sendListener = new OnClickListener(){
    	
		@Override
		public void onClick(View arg0){
			int timeout = 1000;
			String peer = targetIP.getText().toString();
			
			//Pinging the target
			try {
				if (InetAddress.getByName(peer).isReachable(timeout)) {
					peerStatus.setText(peer + " is available");
				}
				else {
					peerStatus.setText(peer + " is not available");
				}
			}
			catch (UnknownHostException e) {
				Log.d("AUC","Unknown Host Exception");
			}
			catch (IOException e) {
				Log.d("AUC","IO Exception");
			}
			
			
			//Sending message
			
			try {
				DatagramSocket socket = new DatagramSocket();
				
				//Sending data
				InetAddress targetInet = InetAddress.getByName(peer);
				String sendStr = sendText.getText().toString();
				byte[] sendData = (sendStr).getBytes();
				DatagramPacket sendPacket = new DatagramPacket (sendData, sendData.length,targetInet,50005);
				socket.send(sendPacket);
				packetStatus.setText("Packet sent");
				
				//Receiving data
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket (receiveData, receiveData.length);
				socket.receive(receivePacket);
				String recStr = new String(receivePacket.getData());
				receivedText.setText(recStr);
				socket.close();
			}
			catch (SocketException e) {
				Log.d("ADU","SocketException");		
			}
			catch (UnknownHostException e) {
				Log.d("ADU","UnknownHostException");
			}
			catch (IOException e) {
				Log.d("ADU","IOException");
			}
			
			
			
		}
    	
    	
    };
}