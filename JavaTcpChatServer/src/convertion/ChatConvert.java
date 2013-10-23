package convertion;

import java.util.ArrayList;
import java.util.ListIterator;

import Chat.ChatLine;
import Chat.Participant;

public class ChatConvert {
	//Convert messages to JSON starting notation at beginNumerotation
	//ex beginNumerotation = 3 : 
	//{
	//		ChatLine3:	{
	//					name:'Gabriel',
	//					sendTime: '21:26:10',
	//					message: 'hello'
	//				}
	//}
	public static String convertChatLinesToJSON(ArrayList<ChatLine> messages, int beginNumerotation) {
		String result = null;
		result = "{\n";
		
		ListIterator<ChatLine> messIt = messages.listIterator();
		ChatLine currentLine;
		boolean first = true;
		while (messIt.hasNext()) {
			if (first) {
				first = false;
			} 
			else {
				//need to put that after the object we did to separate it from the new object following
				result += "},\n";
			}
			currentLine = messIt.next();
			result += "ChatLine" + beginNumerotation+":{\n";
			result += "name:\'" + currentLine.getName()+"\',\n";
			result += "sendTime:\'" + currentLine.getSendTime()+"\',\n";
			result += "message:\'" + currentLine.getMessage()+"\'\n";
			beginNumerotation ++;
		}
		result += "}\n}\n";
		return result;
	}

	//Convert participants to JSON starting notation at beginNumerotation
	//ex beginNumerotation = 3 : 
	//{
	//		Participant3:	{
	//					name:'Gabriel'
	//					}
	//}
	public static String convertChatParticipantsToJS(ArrayList<Participant> participants, int beginNumerotation) {
		String result = null;
		result = "{\n";
		
		ListIterator<Participant> partIt = participants.listIterator();
		Participant participant;
		boolean first = true;
		while (partIt.hasNext()) {
			if (first) {
				first = false;
			} 
			else {
				//need to put that after the object we did to separate it from the new object following
				result += "},\n";
			}
			participant = partIt.next();
			result += "ChatLine" + beginNumerotation+":{\n";
			result += "name:\'" + participant.getName() +"\'\n";
			beginNumerotation ++;
		}
		result += "}\n}\n";
		return result;
	}
}