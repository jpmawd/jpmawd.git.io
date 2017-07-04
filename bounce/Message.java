package bounce;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class Message {

	private String message = "";
	private int color;
	private int xPos = 0, yPos = 0;
	private static Font font = new Font("LCD5x8H", Font.PLAIN, 10);
	//private static Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 15);
	private final static Color[] COLORS = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.GRAY, Color.WHITE};
	private static ArrayList<Message> messages = new ArrayList<Message>();
	
	public Message(String message, int xPos, int yPos) {
		this.message = message;
		this.xPos = xPos;
		this.yPos = yPos;
		this.color = 0;
	}
	public Message(String message, int xPos, int yPos, int colorCode) {
		this.message = message;
		this.xPos = xPos;
		this.yPos = yPos;
		this.color = colorCode;
	}
	
	public static void display(Graphics g, int relativeX, int relativeY) {
		Font font0 = g.getFont();
		g.setFont(font);
		for(int i = 0; i < messages.size(); i++) {
			Message message = messages.get(i);
			g.setColor(COLORS[message.getColor()]);
			g.drawString(message.getMessage(), message.getxPos() + relativeX, message.getyPos() + relativeY);
		}
		g.setFont(font0);
		g.setColor(Color.BLACK);
	}
	
	public static void display(Graphics g, Color c, int relativeX, int relativeY) {
		g.setColor(c);
		display(g, relativeX, relativeY);
		g.setColor(Color.BLACK);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public static Font getFont() {
		return font;
	}

	public static void setFont(Font font) {
		Message.font = font;
	}

	public static ArrayList<Message> getMessages() {
		return messages;
	}

	public static void setMessages(ArrayList<Message> messages) {
		Message.messages = messages;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public static Color[] getColors() {
		return COLORS;
	}
	
}
