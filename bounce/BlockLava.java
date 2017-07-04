package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockLava extends BlockLiquid{

	public BlockLava(int xPos, int yPos) {
		super(xPos, yPos);
		this.isOnFire = true;
		this.spreadSpeed = 1;
	}

	public void render(Graphics g, int screenXPos, int screenYPos) {
		int red = 200 + (int) (50 * Math.sin(Math.toRadians((System.currentTimeMillis() - this.timeInstance))));
		int green = 125 + (int) (Math.random() * (125 * Math.sin(Math.toRadians(4 * (System.currentTimeMillis() - this.timeInstance)))));
		g.setColor(new Color(red, green, 0));
		g.fillRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.BLACK);
	}
}
