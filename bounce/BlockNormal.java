package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockNormal extends BlockLiving{
	
	public BlockNormal(int xPos, int yPos) {
		super(xPos, yPos, 500, "CRUMBLE");
		setEffectColor(normalColor);
	}

	@Override
	public void render(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(normalColor);
		g.fillRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.BLACK);
	}

}
