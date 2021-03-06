package com.trial.voicebasic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class VoiceReceiver extends Thread {
	public DatagramSocket socket;
	private AudioTrack speaker;



	private int port;			//which port??

	//Audio Configuration. 
	private int sampleRate = 16000;		//How much will be ideal?
	private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;    //Mono Makes sense
	private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;		//PCM 16BIT is compatible with most

	private boolean status = true;

	public VoiceReceiver(int port) {
		this.port = port;

	}

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




}