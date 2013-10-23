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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	
}