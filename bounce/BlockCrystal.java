package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockCrystal extends BlockMirror {

	
	
	public BlockCrystal(int xPos, int yPos) {
		super(xPos, yPos);
		setEffectColor(Color.CYAN);
	}

	public void render(Graphics g, int screenXPos, int screenYPos) {
		if(random.nextBoolean()) {
			g.setColor(Color.WHITE);
			g.fillRect(screenXPos, screenYPos, 20, 20);
			g.setColor(Color.CYAN);
			g.drawRect(screenXPos, screenYPos + 4, 16, 16);
			g.drawRect(screenXPos + 4, screenYPos, 16, 16);
		}
		else {
			g.setColor(Color.CYAN);
			g.fillRect(screenXPos, screenYPos, 20, 20);
			g.setColor(Color.WHITE);
			g.drawRect(screenXPos, screenYPos, 16, 16);
			g.drawRect(screenXPos + 4, screenYPos + 4, 16, 16);
		}
		g.setColor(Color.BLACK);
	}
}
