package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockUnbreaking extends Block{

	public BlockUnbreaking(int xPos, int yPos) {
		super(xPos, yPos);
	}

	@Override
	public void procedure() {
		
	}

	@Override
	public void render(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(screenXPos, screenYPos, this.width, this.height);
		g.setColor(Color.BLACK);
		g.drawLine(screenXPos, screenYPos + 5, screenXPos + 20, screenYPos + 5);
		g.drawLine(screenXPos, screenYPos + 10, screenXPos + 20, screenYPos + 10);
		g.drawLine(screenXPos, screenYPos + 15, screenXPos + 20, screenYPos + 15);
	}

}
