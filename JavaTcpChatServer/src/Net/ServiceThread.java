package Net;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Chat.Chat;
import Chat.ChatLine;
import Chat.Participant;
import convertion.ChatConvert;

public class ServiceThread extends Thread {
	private Socket s;
	private Chat chat;

	public ServiceThread(Socket s,Chat chat) {
		super();
		this.s = s;
		this.chat = chat;
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
					sendElementForJavaScriptChat(pageRequested);
				}
			}
			else if (request.startsWith("POST ")){
				if (pageRequested.startsWith("/chat.java?")) {
					String functionRequested = pageRequested.substring("/chat.java?".length());
					// if request.substring = sendLine
					if (functionRequested.startsWith("sendLine")) {
						ChatLine line = getChatLineFromJavaScriptChat(data);
						if (line != null) {
							//Add the line to the chat
							chat.addLine(line);
						}
					}
					else if (functionRequested.startsWith("sendParticipant")) { 
						// if request.substring = sendParticipant
						Participant participant = getParticipantFromJavaScriptChat(data);
						if (participant != null) {
							//Add the participant to the chat participants
							System.out.println(participant.toString());
						}
					}
				}
			}

			inp.close();
			s.close();
		} 
				catch (IOException e) {
			System.err.println("---------------------------------");
			System.err.println("Error in run : ");
			e.printStackTrace();
			System.err.println("---------------------------------");
		}
	}

	//Extract the page requested from the packet header
	public String pageRequested(String request) {
		System.out.println("pageRequested : ");
		System.out.println(request);
		String pageRequested = request.split(" ")[1];
		System.out.println("pageRequested : ");
		System.out.println("---------------------------------");
		System.out.println(pageRequested);
		System.out.println("---------------------------------");

		return pageRequested;		
	}

	//Extract the data from the packet
	public String dataReceived(String request) {
		String newRequest = new String();
		newRequest = getRidOfEmptyBytes(request.split("\r\n\r\n")[1]);

		System.out.println("Data received : ");
		System.out.println("---------------------------------");
		System.out.println(newRequest);
		System.out.println("It's length : " + newRequest.length());
		System.out.println("---------------------------------");
		return newRequest;		
	}
	
	//Get rid of the 00000000.... in the end of a string
	public String getRidOfEmptyBytes(String toTreat) {
		byte[] result = null;
		byte[] translate = toTreat.getBytes();
		boolean endFound = false;
		int i = 0;
		while (i<translate.length && !endFound) {
			i++;
			if (translate[i]==0) {
				endFound = true;
			}
		}
		result = new byte[i];
		for(int j=0; j<i; j++){
			result[j] = translate[j];
		}
		return new String(result);
	}

	// Send element for the page creation, those request are usually from mozilla
	// return true if the file requested was needed for the page creation
	public boolean sendElementForPageCreation(String pageRequested) throws IOException {
		boolean sent = true;

		if (pageRequested.equals("/chat.html")) {
			sendFile(Paths.get("./../chat.html"),"text/html");
			System.out.println("In sendElementForPageCreation : page sent.");
		}
		else if (pageRequested.equals("/stylechat.css")) {
			sendFile(Paths.get("./../stylechat.css"),"text/css");		
			System.out.println("In sendElementForPageCreation : page sent.");			
		}
		else if (pageRequested.equals("/fond_chat.jpeg")) {
			sendFile(Paths.get("./../fond_chat.jpeg"),"image/jpeg");	
			System.out.println("In sendElementForPageCreation : page sent.");				
		}
		else if (pageRequested.equals("/fond_side.jpg")) {
			sendFile(Paths.get("./../fond_side.jpg"),"image/jpeg");		
			System.out.println("In sendElementForPageCreation : page sent.");			
		}
		else if (pageRequested.equals("/favicon.ico")) {
			sendFile(Paths.get("./../favicon.ico"),"image/x-icon");		
			System.out.println("In sendElementForPageCreation : page sent.");			
		}
		else if (pageRequested.equals("/chatScript.js")) {
			sendFile(Paths.get("./../chatScript.js"),"application/x-javascript ");	
			System.out.println("In sendElementForPageCreation : page sent.");				
		}
		else {
			System.out.println("In sendElementForPageCreation : This is not a pageCreationRequest.");
			sent = false;
		}
		return sent;
	}

	// Send element for the chat, those request are from the JavaScript running on the client side
	// return true if the data requested was needed by the chat
	public boolean sendElementForJavaScriptChat(String pageRequested) throws IOException {
		boolean sent = true;
		if (pageRequested.startsWith("/chat.java?")) {
			String functionRequested = pageRequested.substring("/chat.java?".length());
			if (functionRequested.startsWith("chatFrom")) {
				String sCursor = functionRequested.substring("chatFrom(".length(), functionRequested.length()-1);
				int cursor = Integer.parseInt(sCursor);
				ArrayList<ChatLine> subChat = chat.getChatFrom(cursor);
				String nextMessages = ChatConvert.convertChatLinesToJSON(subChat, cursor);
				System.out.println("In sendElementForJavaScriptChat : sending\n" + nextMessages);
				sendMessage(nextMessages, "text/plain");
			} else if (functionRequested.startsWith("participantsToTheChat")) {
				
			}
		}
		else {
			System.out.println("In sendElementForJavaScriptChat : This is not an element for the chat");
			sent = false;
		}
		return sent;
	}

	// Add a ChatLine to the the chat, this request is from the JavaScript running on the client side
	// return null if the data is emtpy
	public ChatLine getChatLineFromJavaScriptChat(String Data) {
		if (Data.isEmpty()){
			return null;
		}
		else {
			ChatLine line = ChatConvert.convertChatParticipantsFromJSON(Data);
			return line;
		}
		
	}
	// Add a participant to the chat, this request is from the JavaScript running on the client side
	// return null if the data is empty
	public Participant getParticipantFromJavaScriptChat(String Data) {
		if (Data.isEmpty()) {
			return null;
		}
		else {
			Participant newParticipant = new Participant(Data);
			return newParticipant;
		}
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
	public void sendMessage(String message, String contentType) throws IOException{
		PrintStream outp = null;

		outp = new PrintStream(s.getOutputStream());
		byte[] fileToSend = message.getBytes();

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

}
