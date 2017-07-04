package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockWater extends BlockLiquid{

	public BlockWater(int xPos, int yPos) {
		super(xPos, yPos);
		this.spreadSpeed = 2;
	}
	
	public void render(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(Main.waterColor);
		g.fillRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.BLACK);
	}

}
