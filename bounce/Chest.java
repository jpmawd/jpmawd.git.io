package bounce;

import java.awt.Color;
import java.awt.Graphics;


public class Chest extends Entity{
	private int[] values;
	private String[] contents;
	private double health = 500;
	
	public Chest(String[] contents, int[] values, double xPos, double yPos, double xVelocity, double yVelocity, boolean gravity) {
		super(xPos, yPos, xVelocity, yVelocity, 20, gravity);
		this.contents = contents;	
		this.values = values;
	}
	public void procedure() {
		if(grounded()) {
			xVelocity = 0;
			yVelocity = 0;
		}
				if(health < 1) {
					isTerminated = true;
				}
				if(isTerminated) {
					for(int i = 0; i < contents.length; i++) {
						Main.items.add(new Item(contents[i], values[i], xPos, yPos, 4 * (Math.random() - .50), -4, true));
					}
				}		
	}
	
	
	public String[] getContents() {
		return contents;
	}
	public void setContents(String[] contents) {
		this.contents = contents;
	}
	public int[] getValues() {
		return values;
	}
	public void setValues(int[] values) {
		this.values = values;
	}
	public double getHealth() {
		return health;
	}
	public void setHealth(double health) {
		this.health = health;
	}
	public void renderEntity(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(Color.YELLOW);
		g.fillRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.BLACK);
		g.drawRect(screenXPos, screenYPos, 20, 20);
		g.drawRect(screenXPos + 2, screenYPos + 2, 16, 16);
		g.fillPolygon(new int[]{screenXPos + 5, screenXPos + 10, screenXPos + 15}, new int[]{screenYPos + 15, screenYPos + 10, screenYPos + 15}, 3);
		g.fillOval(screenXPos + 7, screenYPos + 7, 6, 6);
	}
	
	
}
