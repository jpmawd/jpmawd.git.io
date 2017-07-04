package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockPortal extends Block{

	public BlockPortal(int xPos, int yPos) {
		super(xPos, yPos);
	}

	public void render(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(normalColor);
		g.fillRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.WHITE);
		g.fillOval(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.BLACK);
		int radius = (int) (20 * (double)((double)(System.currentTimeMillis() % 200) / 200));
		g.drawOval((int)(screenXPos - radius / 2 + this.width / 2), (int)(screenYPos - radius / 2 + this.height / 2), radius, radius);
	}

	@Override
	public void procedure() {
		
	}
}
