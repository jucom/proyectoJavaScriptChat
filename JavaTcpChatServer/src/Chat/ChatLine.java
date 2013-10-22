package Chat;

import java.sql.Time;

public class ChatLine {
	private String message;
	private String name;
	private String sendTime;
	
	public ChatLine(String message, String name, String sendTime) {
		super();
		this.message = message;
		this.name = name;
		this.sendTime = sendTime;
	}
	
	public String toString() {
		return sendTime + ' ' + name + " : " + message;
	}
}