package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockBounce extends Block{

	public BlockBounce(int xPos, int yPos) {
		super(xPos, yPos);
	}

	@Override
	public void procedure() {
		
	}

	@Override
	public void render(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(Color.GREEN);
		g.fillRect(screenXPos, screenYPos, this.width, this.height);
		g.setColor(Color.BLACK);
		g.drawRoundRect(screenXPos + 4, screenYPos + 4, this.width - 8, this.height - 8, 4, 4);
	}
}
