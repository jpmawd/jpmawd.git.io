package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class GhostIce extends Ghost{

	public GhostIce(double xPos, double yPos) {
		super(xPos, yPos, 20);
	}

	public void procedure() {
		super.procedure();
		if(random.nextInt(50) == 0) {
			Main.gfx.add(GraphicEffect.createGraphicEffectByName("SNOW", xPos, yPos));
		}
		if(random.nextBoolean()) {
			int x = (int)this.xPos - (int)this.xPos % 20;
			int y = (int)this.yPos - (int)this.yPos % 20;
			Block block = Main.blockAt(x, y);
			if(block instanceof BlockWater) {
				Main.replaceBlockWith(block, BlockIce.class);
			}
		}
	}
	
	public void attack() {
		Main.player1.setxVelocity(Main.player1.getxVelocity() / 2);
		Main.player1.setyVelocity(Main.player1.getyVelocity() / 2);
	}

	public void renderEntity(Graphics g, int screenXPos, int screenYPos) {
		
		g.setColor(Main.ice0Color);
		g.drawRoundRect(screenXPos, screenYPos, (int)this.radius, (int)this.radius, 4, 4);
		g.setColor(Color.BLACK);
	}
}
