package bounce;

import java.awt.Color;
import java.awt.Graphics;


public class Item extends Entity{
	
	private String type; 
	private int value;
	
	public Item(String type, int value, double xPos, double yPos, double xVelocity, double yVelocity, boolean gravity) {
		super(xPos, yPos, xVelocity, yVelocity, 20, gravity);	
		this.type = type;
		this.value = value;
	}
	public void procedure() {
		if(grounded()) {
			xVelocity = 0;
			yVelocity = 0;
		}
		if(isTouchingPlayer()) {
			uponAcquisition();
			isTerminated = true;
		}
	}
	
	public void uponAcquisition() {
		if(type.equals("HEALTH")) {
			Main.player1.setHealth(Main.player1.getHealth() + this.value);
		}
		else if(type.equals("AMMO")) {
			Main.player1.setAmmo(Main.player1.getAmmo() + this.value);
		}
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public void renderEntity(Graphics g, int screenXPos, int screenYPos) {
		if(this.getType().equals("HEALTH")) {
			g.setColor(Color.RED);
			g.fillArc(screenXPos, screenYPos, 10, 10, 0, 180);
			g.fillArc(screenXPos + 10, screenYPos, 10, 10, 0, 180);
			g.fillPolygon(new int[]{screenXPos, screenXPos + 10, screenXPos + 20}, new int[]{screenYPos + 10, screenYPos + 20, screenYPos + 10}, 3);
			g.fillRect(screenXPos, screenYPos + 5, 20, 5);
			g.setColor(Color.BLACK);
			g.drawArc(screenXPos, screenYPos, 10, 10, 0, 180);
			g.drawArc(screenXPos + 10, screenYPos, 10, 10, 0, 180);
			g.drawLine(screenXPos, screenYPos + 10, screenXPos + 10, screenYPos + 20);
			g.drawLine(screenXPos + 20, screenYPos + 10, screenXPos + 10, screenYPos + 20);
			g.drawLine(screenXPos, screenYPos + 5, screenXPos, screenYPos + 10);
			g.drawLine(screenXPos + 20, screenYPos + 5, screenXPos + 20, screenYPos + 10);
		}
		else if(this.getType().equals("AMMO")) {
			
			g.setColor(Main.bluePlasmaColor);
			g.fillRect(screenXPos, screenYPos, 10, 20);
			g.setColor(Color.BLACK);
			g.drawRect(screenXPos, screenYPos + 1, 10, 18);
			g.drawRect(screenXPos, screenYPos, 10, 20);
		}
	}
	
}
