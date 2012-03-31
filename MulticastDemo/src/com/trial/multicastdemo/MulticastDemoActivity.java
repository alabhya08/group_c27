package com.trial.multicastdemo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MulticastDemoActivity extends Activity {
	
	
	private TextView message;
	private Button recButton,stopButton;

    InetAddress inet = null;
    
    MulticastSocket msoc = null;
    
    byte[] buffer = new byte[1024];
    
    int port = 5500;
    
    private volatile boolean clientStatus;
	
    WifiManager wm;
    
    WifiManager.MulticastLock multicastLock;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        
        WifiManager.MulticastLock multicastLock = wm.createMulticastLock("myLock");
        
        multicastLock.acquire();
        
        Log.d("MD","multicast lock acquired");
        
        message = (TextView) findViewById(R.id.message_tv);
        findViewById(R.id.message_label);
        recButton = (Button) findViewById(R.id.rec_button);
        stopButton = (Button) findViewById(R.id.stop_button);
        
        recButton.setOnClickListener(recListener);
        stopButton.setOnClickListener(stopListener);
        
        
        
    }
    
    
    public Handler myHandler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    			String recMsg = (String) msg.obj;
    			message.setText(recMsg);
    		    		
    	}
    };
    
    private final OnClickListener recListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			clientStatus = true;
			mcastClient.start();
		}
    	
    };
    
    private final OnClickListener stopListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			clientStatus = false;
			/*
			 * MulticastLock is released anyway when app crashes or exits.
			 * try {
				multicastLock.release();
			} catch(Exception e) {
				Log.e("MD", "Exception thrown while releasing lock");
			}
			*/
			
		}
		
    	
    };
    
    
    Thread mcastClient = new Thread(new Runnable() {

		@Override
		public void run() {
			while(clientStatus) {
			try {
	        	inet = InetAddress.getByName("224.2.76.24");
	        	Log.d("MD","Address retrieved");
	        	
	        	DatagramPacket packet = new DatagramPacket(buffer, buffer.length,inet,port);
	        	
	        	msoc = new MulticastSocket(port);
	        	Log.d("MD","Socket created");
	        	
	        	msoc.joinGroup(inet);
	        	Log.d("MD","multicast group joined");
	        	
	        	msoc.receive(packet);
	        	Log.d("MD","packet received with "+packet.getData());
	        	String msgStr = new String(packet.getData(),0,packet.getLength()); 
	        	
	        	Message msg = Message.obtain();
	        	msg.obj = msgStr;
	        	myHandler.sendMessage(msg);
	        	String senderAddr = (packet.getAddress()).getHostAddress();
	        	Log.d("MD","Message: "+msgStr+"  From: "+senderAddr);
	        	      	
	        	DatagramSocket socket = new DatagramSocket();
				
	        	//Sending own name to source of multicast
	        	InetAddress senderInet = InetAddress.getByName(senderAddr);
				String sendStr = "My Name";
				byte[] sendData = (sendStr).getBytes();
				DatagramPacket sendPacket = new DatagramPacket (sendData, sendData.length,senderInet,50005);
				socket.send(sendPacket);
				Log.d("MD","Reply Sent to "+senderInet);
	        	
	        					
	        } catch(UnknownHostException e) {
	        	message.setText(e.getMessage());
	        } catch (IOException e) {
				e.printStackTrace();
			}
	        
			
			
		}
				
		if(clientStatus == false) {
			try {
				Log.d("MD","Thread Stopped");
	            msoc.leaveGroup(inet);
	            Log.d("MD","multicast group left");
	            
	            msoc.close();
	        } catch (IOException e) {
	            Log.e("MD", "IOException while leaving group");
	        }
		}
		}
    });
    
    
    
    
}