package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class GhostFire extends Ghost{

	public GhostFire(double xPos, double yPos) {
		super(xPos, yPos, 20);
	}

	public void procedure() {
		super.procedure();
		if(System.currentTimeMillis() % 25 == 0) {
			Main.fires.add(new Fire(xCenter, yCenter, (Math.random() - .5), (Math.random() - .5)));
		}
	}
	public void attack() {
		Main.player1.attack(20, Player.FIRE);
	}

	public void renderEntity(Graphics g, int screenXPos, int screenYPos) {
		int red = 200 + (int) (Math.random() * 50 * Math.sin(Math.toRadians((System.currentTimeMillis() - getTimeInstance()))));
		int green = 125 + (int) (Math.random() * (125 * Math.sin(Math.toRadians(4 * (System.currentTimeMillis() - getTimeInstance())))));
		g.setColor(new Color(red, green, 0));
		g.drawRoundRect(screenXPos, screenYPos, (int)this.radius, (int)this.radius, 4, 4);
		g.setColor(Color.BLACK);
	}
}
