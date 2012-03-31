import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;


public class MulticastServer {
	
	private static MulticastSocket msoc;
	
	static InetAddress mcastAddr = null;
	
	static int port = 5500;
	
	public static void main(String[] args) throws IOException {
		
		try {
			InetAddress mcastAddr = InetAddress.getByName("224.2.76.24");
			
			msoc = new MulticastSocket(port);
			
			msoc.joinGroup(mcastAddr);
			
			
			byte[] message = new byte[1024];
				
			message = "Hi".getBytes();
				
			DatagramPacket packet = new DatagramPacket(message, message.length,mcastAddr,port);
				
			msoc.send(packet);
				
			DatagramSocket socket = new DatagramSocket(50005);
			
			byte[] replyData = new byte[1024];
			
			while (true) {
				//Receiving replies to multicast
				DatagramPacket replyPacket = new DatagramPacket(replyData,replyData.length);
				socket.receive(replyPacket);
				
				String replymsg=new String(replyPacket.getData(),0,replyPacket.getLength());
					
				String senderAddr = replyPacket.getAddress().getHostAddress();
					
				System.out.println("Name: "+replymsg+"\tAddress:"+senderAddr);
					
			}
			
		} catch(SocketException e) {
			e.printStackTrace();
		}
		
		
		
		
	}

}
