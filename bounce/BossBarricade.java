package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BossBarricade extends BlockLiving{
	
	public BossBarricade(int xPos, int yPos) {
		super(xPos, yPos, 5000, "DUST");
	}

	public void procedure() {
		super.procedure();
		if(Main.bosses.size() > 0) {
			health = maxHealth;
		}
		else {
			health = 0;
		}
	}
	
	public void render(Graphics g, int screenXPos, int screenYPos) {
		g.fillRect(screenXPos, screenYPos, this.width, this.height);
		g.setColor(Color.DARK_GRAY);
		g.drawLine(screenXPos, screenYPos + 5, screenXPos + 20, screenYPos + 5);
		g.drawLine(screenXPos, screenYPos + 10, screenXPos + 20, screenYPos + 10);
		g.drawLine(screenXPos, screenYPos + 15, screenXPos + 20, screenYPos + 15);
		g.setColor(Color.BLACK);
	}
}
