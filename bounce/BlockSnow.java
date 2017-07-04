package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockSnow extends BlockLiving{

	public BlockSnow(int xPos, int yPos) {
		super(xPos, yPos, 200, "SNOW");
		isFlammable = true;
	}
	
	public void procedure() {
		super.procedure();
		if(random.nextInt(400) == 0) {
			Main.gfx.add(GraphicEffect.createGraphicEffectByName("SNOW", xPos, yPos + 10));
		}
		if(this.isOnFire) {
			this.isTerminated = true;
		}
	}

	@Override
	public void render(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(Color.WHITE);
		g.fillRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.BLACK);
	}

}
