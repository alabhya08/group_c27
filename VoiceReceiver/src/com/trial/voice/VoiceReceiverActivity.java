package com.trial.voice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.trial.voicereceive.R;

public class VoiceReceiverActivity extends Activity {
    
	
	private Button receiveButton,stopButton;
	private TextView recTV;
	
	public static DatagramSocket socket;
	private AudioTrack speaker;
	
	private AudioManager am;
	
	private int port=50005;			//which port??
	
	//Audio Configuration. 
	private int sampleRate = 16000;		//How much will be ideal?
	private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;    //Mono Makes sense
	private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;		//PCM 16BIT is compatible with most
		
	private boolean status = true;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        receiveButton = (Button) findViewById (R.id.receive_button);
        stopButton = (Button) findViewById (R.id.stop_button);
        findViewById(R.id.receive_label);
        
        receiveButton.setOnClickListener(receiveListener);
        stopButton.setOnClickListener(stopListener);
        
        /*ForText 
        recTV = (TextView) findViewById(R.id.rec_TV);
        recTV.setText("Ready To receive");
        */
        
       am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
       am.setMode(AudioManager.MODE_IN_CALL);
       am.setSpeakerphoneOn(false);
       
        
        
    }
    
    
    private final OnClickListener stopListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			status = false;
			speaker.release();
			Log.d("VR","Speaker released");
			am.setMode(AudioManager.MODE_NORMAL);
			
		}
    	
    };
    
    
    private final OnClickListener receiveListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			status = true;
			startReceiving();
			
		}
    	
    };
    
    public void startReceiving() {
    	
    	Thread receiveThread = new Thread (new Runnable() {
    		/*For Text
    		 
			@Override
			public void run() {
				try {
					String temp = "tempStr";
					Log.d("VR", "button pressed"+temp);
					String recMsg;
					DatagramSocket socket = new DatagramSocket(50005);
				
					byte[] buf = new byte[1024];
					DatagramPacket packet = new DatagramPacket(buf,buf.length);
				
					socket.receive(packet);
					
					recMsg=new String(packet.getData(),0,packet.getLength());
					
					Log.d("VR", "Message received"+recMsg);
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
    		*/
    		
    		
			@Override
			public void run() {
				
				try {
					
					//Is setting thread priority needed? If yes then to what?
					android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
					
					DatagramSocket socket = new DatagramSocket(port);
					Log.d("VR", "Socket Created");
					
					//minimum buffer size. need to be careful. might cause problems. try setting manually if any problems faced
					//int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
					int minBufSize = 256;
					byte[] buffer = new byte[minBufSize];
					Log.d("VR", "Buffer Created of size "+minBufSize);
					
					speaker = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRate,channelConfig,audioFormat,minBufSize*20,AudioTrack.MODE_STREAM);
					Log.d("VR", "AudioTrack obj created");
					
					speaker.play();
					
					while(status == true) {
						try {
							DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
							socket.receive(packet);
							Log.d("VR", "Packet Received");
							
							//reading content from packet
							buffer=packet.getData();
							Log.d("VR", "Packet data read into buffer");
							
							//sending data to the Audiotrack obj i.e. speaker
							speaker.write(buffer, 0, buffer.length);
							Log.d("VR", "Writing buffer content to speaker");
							
						} catch(IOException e) {
							Log.e("VR","IOException");
						}
					}
					
					
				} catch (SocketException e) {
					Log.e("VR", "SocketException");
				}
				
				
			}
    		
    	});
    	receiveThread.start();
    }
    
    
    
    
    
    
    
}