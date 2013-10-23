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

import Chat.ChatLine;
import Chat.Participant;

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
			String data = new String();

			//reading message received
			inp = new BufferedReader(new InputStreamReader(s.getInputStream()));
			length = inp.read(buffer);
			request = new String(buffer);

			System.out.println("Received packet : ");
			System.out.println("---------------------------------");
			System.out.println(request);
			System.out.println("---------------------------------");
			
			pageRequested = pageRequested(request);
			data = dataReceived(request);
			
			if (request.startsWith("GET ")){ 
				// if it wasn't an element needed for the page creation
				// then it must be an element needed for the chat
				if (!sendElementForPageCreation(pageRequested)) {
					sendElementForJavaScriptChat(pageRequested,data);
				}
			}
			else if (request.startsWith("POST ")){
				ChatLine line = getChatLineFromJavaScriptChat(pageRequested, data);
				if (line != null) {
					//Add the line to the chat
				}
				else {
					Participant participant = getParticipantFromJavaScriptChat(pageRequested,data);
					if (participant != null) {
						//Add the participant to the chat participants
					}
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

	//Extract the page requested from the packet header
	public String pageRequested(String request) {
		String pageRequested = request.split(" ")[1];
		System.out.println("pageRequested : ");
		System.out.println("---------------------------------");
		System.out.println(pageRequested);
		System.out.println("---------------------------------");
		
		return pageRequested;		
	}

	//Extract the data from the packet
	public String dataReceived(String request) {
		return null;		
	}
	
	// Send element for the page creation, those request are usually from mozilla
	// return true if the file requested was needed for the page creation
	public boolean sendElementForPageCreation(String pageRequested) throws IOException {
		boolean sent = true;
		
		if (pageRequested.equals("/chat.html")) {
			sendFile(Paths.get("./../chat.html"),"text/html");
		}
		else if (pageRequested.equals("/stylechat.css")) {
			sendFile(Paths.get("./../stylechat.css"),"text/css");					
		}
		else if (pageRequested.equals("/fond_chat.jpeg")) {
			sendFile(Paths.get("./../fond_chat.jpeg"),"image/jpeg");					
		}
		else if (pageRequested.equals("/fond_side.jpg")) {
			sendFile(Paths.get("./../fond_side.jpg"),"image/jpeg");					
		}
		else if (pageRequested.equals("/favicon.ico")) {
			sendFile(Paths.get("./../favicon.ico"),"image/x-icon");					
		}
		else {
			System.out.println("In doPageCreationRequest : This is not a pageCreationRequest.");
			sent = false;
		}
		return sent;
	}

	// Send element for the chat, those request are from the JavaScript running on the client side
	// return true if the data requested was needed by the chat
	public boolean sendElementForJavaScriptChat(String pageRequested, String Data) {
		return false;
	}
	
	// Get ChatLine from the chat, this request is from the JavaScript running on the client side
	// return null if the data received was not a ChatLine from the chat
	public ChatLine getChatLineFromJavaScriptChat(String pageRequested, String Data) {
		return null;
	}
	// Get participant from the chat, this request is from the JavaScript running on the client side
	// return null if the data received was not a participant from the chat
	public Participant getParticipantFromJavaScriptChat(String pageRequested, String Data) {
		return null;
	}
	
	//Makes the header of the HTML packet that we will send
	public String makeHeader(int lengthData, String contentType) {

		String packetHeader = new String("HTTP/1.0 200 OK\n");
		//date
		Date today = Calendar.getInstance().getTime();
		packetHeader += today.toString() + " Server : Serveur Java\n";
		//type of data
		packetHeader += "Content-Type : " + contentType + "\n";
		//length of data
		packetHeader += "Content-Length : " + lengthData + "\n";
		//data last modified
		packetHeader += "Last-Modified : " + today.toString() + "\n\n";

		return packetHeader;		
	}


	//Send a file to the connected pair
	public void sendFile(Path filePath, String contentType) throws IOException{
		PrintStream outp = null;

		outp = new PrintStream(s.getOutputStream());
		byte[] fileToSend = Files.readAllBytes(filePath);

		String packetHeader = makeHeader(fileToSend.length,contentType);

		outp.write(packetHeader.getBytes());
		outp.write(fileToSend);

//		System.out.println("In sendFile : ");
//		System.out.println("---------------------------------");
//		System.out.println(packetHeader);
//		System.out.println(new String(fileToSend));
//		System.out.println("---------------------------------");
		outp.close();	
	}
	
	//Send a String to the connected pair
	public void sendMessage(String message){
	}
	
}
