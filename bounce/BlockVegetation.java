package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockVegetation extends BlockLiving{
	
	public BlockVegetation(int xPos, int yPos) {
		super(xPos, yPos, 10, "SHATTER");
		setEffectColor(Main.plantColor);
		isFlammable = true;
	}
	@Override
	public void render(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(this.getEffectColor());
		g.fillRect(screenXPos, screenYPos, this.width, this.height);
		g.setColor(Color.BLACK);
		for(int i = 0; i < 9; i++) {
			g.drawOval(screenXPos + 5 * (i % 3), screenYPos + 5 * (int)(i / 3), 5, 5);
		}
	}

}
