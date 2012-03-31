package com.trial.voicebasic;
/*
 * A full duplex voice communication app. Only the call button is to be used. Terminate for some reason does
 * not work. The receive button as of now does the same thing as the call button. Modifications to be done
 * to that, here or elsewhere
 */


import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class VoiceBasicActivity extends Activity {
    


	public int port=50005;			//which port??

	private AudioManager am;



	//UI Elements
	private EditText targetIP;
	private Button callButton,receiveButton,terminateButton;

	VoiceSender vs;
	VoiceReceiver vr;

	Thread sender,receiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        targetIP = (EditText) findViewById(R.id.target_ip);
        callButton = (Button) findViewById(R.id.call_button);
        receiveButton = (Button) findViewById(R.id.receive_button);
        terminateButton = (Button) findViewById(R.id.terminate_button);
        
        callButton.setOnClickListener(callListener);
        receiveButton.setOnClickListener(receiveListener);
        terminateButton.setOnClickListener(terminateListener);
        
        
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setMode(AudioManager.MODE_IN_CALL);
        am.setSpeakerphoneOn(false);
        
       
        
    }
    
    private final OnClickListener callListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {


			initiateCall();


		}
    	
    };
    
    
    private final OnClickListener receiveListener = new OnClickListener() {

		@Override
		public void onClick(View arg0)  {

			acceptCall();
		}
    	
    };
    
    
    private final OnClickListener terminateListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			terminate();

		}
    };
    
    
    
    public void initiateCall() {
    	
    	vr = new VoiceReceiver (50005);
    	Log.d("VoiceComm", "VoiceReceiver obj created");
    	receiver = new Thread(vr);
    	receiver.start();
    	Log.d("VoiceComm", "Receiver Thread started");
    	
    	vs = new VoiceSender (targetIP.getText().toString(),50005);
    	Log.d("VoiceComm", "VoiceSender obj created");
    	sender = new Thread(vs);
    	sender.start();
    	Log.d("VoiceComm", "Sender Thread started");
    	
    	
    }
    
    
    //For now this function does the same thing. No Point in it.
    public void acceptCall() {
    	
    	vr = new VoiceReceiver (50005);
    	Log.d("VoiceComm", "VoiceReceiver obj created");
    	receiver = new Thread(vr);
    	receiver.start();
    	Log.d("VoiceComm", "Receiver Thread started");
    	
    	vs = new VoiceSender (targetIP.getText().toString(),50005);
    	Log.d("VoiceComm", "VoiceSender obj created");
    	sender = new Thread(vs);
    	sender.start();
    	Log.d("VoiceComm", "Sender Thread started");
    	
    }
    
    public void terminate() {
    	sender.stop();
    	receiver.stop();
    	
    	am.setMode(AudioManager.MODE_NORMAL);
    	
    	Log.d("VoiceComm", "Terminated");
    	System.exit(0);
    }
    
    
    
}