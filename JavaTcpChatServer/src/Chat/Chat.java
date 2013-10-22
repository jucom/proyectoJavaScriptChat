package Chat;

import java.util.ArrayList;
import java.util.ListIterator;


public class Chat {
	private ArrayList<ChatLine> chat = new ArrayList<ChatLine>();
	private ArrayList<Participant> participants = new ArrayList<Participant>();

	//The currentLine variable represent the current number of lines present in the chat
	private int currentLine = 0;

	public int addParticipant(String name) {
		Participant chater = new Participant(name);
		participants.add(chater);
		return currentLine;
	}

	public void addLine(ChatLine line) {
		chat.add(line);
		currentLine++;
	}

	public ArrayList<ChatLine> getChatFrom(int cursorPos) {
		ArrayList<ChatLine> messagesSuivant = new ArrayList<ChatLine>();
		ChatLine line;
		if (cursorPos <= currentLine) {
			ListIterator chatIt = chat.listIterator(cursorPos-1);
			while (chatIt.hasNext()) {
				line = (ChatLine) chatIt.next();
				messagesSuivant.add(line);
			}
		}
		return messagesSuivant;
	}

	public String[] sgetChatFrom(int cursorPos) {
		ArrayList<ChatLine> messagesSuivant = getChatFrom(cursorPos);
		String[] messagesSuivantString = new String[messagesSuivant.size()];
		ListIterator messagesIt = messagesSuivant.listIterator();
		ChatLine line;
		int i=0;
		while (messagesIt.hasNext()) {
			line = (ChatLine) messagesIt.next();
			messagesSuivantString[i] = line.toString();
			i++;
		}
		
		return messagesSuivantString;
	}

	public ArrayList<Participant> getParticipants() {
		return participants;
	}
	
	public String[] sgetParticipants() {
		ListIterator participantsIt = participants.listIterator();
		String[] participantsString = new String[participants.size()];
		int i = 0;
		while (participantsIt.hasNext()) {
			participantsString[i] = participantsIt.next().toString();
			i++;
		}
		return participantsString;
	}
}
