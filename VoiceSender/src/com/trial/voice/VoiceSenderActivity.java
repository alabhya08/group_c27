package com.trial.voice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.trial.voicesend.R;

public class VoiceSenderActivity extends Activity {
    
	private EditText target,sendET;
	private TextView streamingLabel;
	private Button startButton,stopButton;
	
	public static DatagramSocket socket;
	private int port=50005;			//which port??
	AudioRecord recorder;
	
	//Audio Configuration. 
	private int sampleRate = 16000;		//How much will be ideal?
	private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;    //Mono Makes sense
	private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;		//PCM 16BIT is compatible with most
	
	private boolean status = true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        target = (EditText) findViewById (R.id.target_IP);
        
        streamingLabel = (TextView) findViewById(R.id.streaming_label);
        startButton = (Button) findViewById (R.id.start_button);
        stopButton = (Button) findViewById (R.id.stop_button);
        
        streamingLabel.setText("Press Start! to begin");
        
        startButton.setOnClickListener (startListener);
        
        stopButton.setOnClickListener (stopListener);
        
        /*For Text
        sendET = (EditText) findViewById(R.id.send_ET);
         */
        
        
    }
    
    private final OnClickListener stopListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
					status = false;
					recorder.release();
					Log.d("VS","Recorder released");
		}
    	
    };
    
    private final OnClickListener startListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
					status = true;
					startStreaming();			
		}
    	
    };
    
    public void startStreaming() {
    	
    	
    	Thread streamThread = new Thread(new Runnable() {
    		
    		/* For Text
			@Override
			public void run() {
				try {
					DatagramSocket socket = new DatagramSocket();
					Log.d("VS", "Socket Created");
					
					InetAddress destination = InetAddress.getByName(target.getText().toString());
					
					String sendtext = sendET.getText().toString();
					byte[] buf = sendtext.getBytes();
					
					DatagramPacket packet = new DatagramPacket (buf, buf.length,destination,50005);
					
					socket.send(packet);
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
					
					DatagramSocket socket = new DatagramSocket();
					Log.d("VS", "Socket Created");
					
					InetAddress destination = InetAddress.getByName(target.getText().toString());
					Log.d("VS", "Address retrieved");
					
					
					DatagramPacket packet;
					
					//minimum buffer size. need to be careful. might cause problems. try setting manually if any problems faced
					//int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
					int minBufSize = 256;
					
					byte[] buffer = new byte[minBufSize];
					Log.d("Vs", "Buffer created of size "+minBufSize);
					
					recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,audioFormat,minBufSize*20);
					Log.d("VS", "Recorder initialized");
					
					recorder.startRecording();
					Log.d("VS", "Recording Started");
					
					
					while(status == true) {
						
						
						//reading data from MIC into buffer
						recorder.read(buffer, 0, buffer.length);			//3rd parameter = no. of requested bytes = buffer.length or minbufsize
						Log.d("VS", "Audio read into buffer");
						
						//putting buffer in the packet
						packet = new DatagramPacket (buffer,buffer.length,destination,port);
						Log.d("VS", "Packet created");
					
						socket.send(packet);
						Log.d("VS", "Packet sent");
						
						
					}
					
					
					
				} catch(UnknownHostException e) {
					Log.e("VS", "UnknownHostException");
				} catch (IOException e) {
					Log.e("VS", "IOException");
				} 
				
				
			}
    		
    	});
    	streamThread.start();
    }
    
}