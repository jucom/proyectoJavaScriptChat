package Test;

import Chat.Chat;
import Chat.ChatLine;
import convertion.ChatConvert;

public class TestChat {
	public static void main (String[] args) {
		test2();
	}

	public static void test1() {
		System.out.println("#########");
		System.out.println("# TEST1 #");
		System.out.println("#########");
		Chat chat = new Chat();
		ChatLine line1 = new ChatLine("Hola todos","gabriel","17:24:26");
		ChatLine line2 = new ChatLine("Hola","justine","17:24:40");
		ChatLine line3 = new ChatLine("¿Hola gabriel que estas haciendo?","thomas","17:25:00");
		chat.addLine(line1);
		chat.addLine(line2);
		chat.addLine(line3);
		chat.addParticipant("Gabriel");
		chat.addParticipant("Justine");
		chat.addParticipant("Thomas");

		String[] theChat = chat.sgetChatFrom(1);
		String[] theParticipants = chat.sgetParticipants();
		System.out.println("The chat lines :");
		for (int i=0; i < theChat.length; i++) {
			System.out.println(theChat[i]);
		}
		System.out.println("\nThe chat participants :");
		for (int i=0; i < theParticipants.length; i++) {
			System.out.println(theParticipants[i]);
		}
	}
	
	public static void test2() {
		System.out.println("#########");
		System.out.println("# TEST2 #");
		System.out.println("#########");
		Chat chat = new Chat();
		ChatLine line1 = new ChatLine("Hola todos","gabriel","17:24:26");
		ChatLine line2 = new ChatLine("Hola","justine","17:24:40");
		ChatLine line3 = new ChatLine("¿Hola gabriel que estas haciendo?","thomas","17:25:00");
		chat.addLine(line1);
		chat.addLine(line2);
		chat.addLine(line3);
		chat.addParticipant("Gabriel");
		chat.addParticipant("Justine");
		chat.addParticipant("Thomas");
		System.out.println("Convertion of ChatLines to JSON notation :\n");
		System.out.println("----\n"+ChatConvert.convertChatLinesToJSON(chat.getChatFrom(2),2)+"----\n");
		System.out.println("Convertion of Participants to JSON notation :\n");
		System.out.println("----\n"+ChatConvert.convertChatParticipantsToJS(chat.getParticipants(),1)+"----\n");

	}
}
