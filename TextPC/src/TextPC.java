
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class TextPC {
	
	public static void main (String[] args) {
		
		byte[] inData = new byte[1024];
		byte[] outData = new byte[1024];
		
	
		String message;
		
		try {
			DatagramSocket socket = new DatagramSocket(50005);
			while (true) {
				
				//Receiving Data
				DatagramPacket in = new DatagramPacket(inData,inData.length);
				socket.receive(in);
				InetAddress senderIP = in.getAddress();
				int senderPort = in.getPort();
				message=new String(in.getData(),0,in.getLength());
				
				String sender = senderIP.getHostAddress();
				
				System.out.println("Message received from "+senderIP+", Host Address" + sender +", Port:"+senderPort+" : "+message);
				
				//Sending Data back
				outData = "Yes I Got The Message!".getBytes();
			    DatagramPacket out = new DatagramPacket(outData,outData.length, senderIP,senderPort);
			    socket.send(out);
			
        		}
		} catch (SocketException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}