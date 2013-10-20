package Controller;

import java.util.ArrayList;

import Chat.ChatLine;
import Chat.Participant;

public class Controller {
	private ArrayList<ChatLine> chat = new ArrayList<ChatLine>();
	private ArrayList<Participant> participants = new ArrayList<Participant>();
	private int currentLine = -1;
	
	public int addParticipant(String name) {
		Participant chater = new Participant(name);
		participants.add(chater);
		return currentLine;
	}
	
	public void addLine(ChatLine line, int id) {
		chat.add(line);
		currentLine++;
	}
	
	public void updateParticipant(int cursorPos) {
		if (cursorPos < currentLine) {
			ArrayList<ChatLine> messagesSuivant = new ArrayList<ChatLine>();
			messagesSuivant = (ArrayList<ChatLine>) chat.subList(cursorPos, currentLine);
			//send(messagesSuivant)
		}
	}
}
