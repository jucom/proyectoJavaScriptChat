package Net;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

public class ServiceThread extends Thread {
	private Socket s;

	public ServiceThread(Socket s) {
		super();
		this.s = s;
	}
	public void run() {
		BufferedReader inp = null;

		System.out.println("Doing Service with " + s.getInetAddress());
		try {
			char[] buffer = new char[1024];
			int length = 0;
			String request = new String();
			String pageRequested = new String();

			//reading message received
			inp = new BufferedReader(new InputStreamReader(s.getInputStream()));
			length = inp.read(buffer);
			request = new String(buffer);

			System.out.println("Received packet : ");
			System.out.println("---------------------------------");
			System.out.println(request);
			System.out.println("---------------------------------");
			if (request.startsWith("GET ") || request.startsWith("POST ")){
				pageRequested = request.split(" ")[1];
				System.out.println("pageRequested : ");
				System.out.println("---------------------------------");
				System.out.println(pageRequested);
				System.out.println("---------------------------------");
				if (pageRequested.equals("/chat.html")) {
					sendFile(Paths.get("./../chat.html"));
				}
				else if (pageRequested.equals("/stylechat.css")) {
					sendFile(Paths.get("./../stylechat.css"));					
				}
				else if (pageRequested.equals("/fond_chat.jpeg")) {
					sendFile(Paths.get("./../fond_chat.jpeg"));					
				}
				else if (pageRequested.equals("/fond_side.jpg")) {
					sendFile(Paths.get("./../fond_side.jpg"));					
				}
			}
			inp.close();
			s.close();
		} catch (IOException e) {
			System.err.println("---------------------------------");
			System.err.println("Error in run : ");
			e.printStackTrace();
			System.err.println("---------------------------------");
		}
	}

	public String makeHeader(int lengthData) {

		String packetHeader = new String("HTTP/1.0 200 OK\n");
		//date
		Date today = Calendar.getInstance().getTime();
		packetHeader += today.toString() + " Server : Serveur Java\n";
		//type of data
		packetHeader += "Content-Type : text/HTML\n";
		//length of data
		packetHeader += "Content-Length : " + lengthData + "\n";
		//data last modified
		packetHeader += "Last-Modified : " + today.toString() + "\n\n";

		return packetHeader;		
	}

	public void sendFile(Path filePath) throws IOException{
		PrintStream outp = null;

		outp = new PrintStream(s.getOutputStream());
		byte[] fileToSend = Files.readAllBytes(filePath);

		String packetHeader = makeHeader(fileToSend.length);

		outp.write(packetHeader.getBytes());
		outp.write(fileToSend);

//		System.out.println("In sendFile : ");
//		System.out.println("---------------------------------");
//		System.out.println(packetHeader);
//		System.out.println(new String(fileToSend));
//		System.out.println("---------------------------------");
		outp.close();	
	}
}
