package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockWood extends BlockLiving{

	public BlockWood(int xPos, int yPos) {
		super(xPos, yPos, 500, "CRUMBLE");
		this.isFlammable = true;
		setEffectColor(Main.woodColor);
	}

	@Override
	public void render(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(Main.woodColor);
		g.fillRect(screenXPos, screenYPos, this.width, this.height);
		g.setColor(Color.BLACK);
		g.drawLine(screenXPos + 5, screenYPos, screenXPos + 5, screenYPos + this.height);
		g.drawLine(screenXPos + 10, screenYPos, screenXPos + 10, screenYPos + this.height);
		g.drawLine(screenXPos + 15, screenYPos, screenXPos + 15, screenYPos + this.height);
	}

}
