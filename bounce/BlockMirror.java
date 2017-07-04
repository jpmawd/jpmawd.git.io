package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockMirror extends BlockLiving{

	public BlockMirror(int xPos, int yPos) {
		super(xPos, yPos, 500, "SHATTER");
	}
	

	public void render(Graphics g, int screenXPos, int screenYPos) {
		if(random.nextBoolean()) {
			g.setColor(Color.WHITE);
		}
		else {
			g.setColor(normalColor);
		}
		g.fillRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.BLACK);
		g.drawRect(screenXPos, screenYPos, 16, 16);
		g.drawRect(screenXPos + 4, screenYPos + 4, 16, 16);
	}
}
