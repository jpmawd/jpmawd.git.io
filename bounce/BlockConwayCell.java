package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockConwayCell extends BlockLiving{

	public BlockConwayCell(int xPos, int yPos) {
		super(xPos, yPos, 500, "CRUMBLE");
		setEffectColor(Main.purple);
	}
	
	public void render(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(Main.purple);
		g.fillRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Main.conwayCellColor);
		g.drawRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.BLACK);
	}
}
