package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockIce extends BlockMirror{

	public BlockIce(int xPos, int yPos) {
		super(xPos, yPos);
		isFlammable = true;
		setEffectColor(Main.ice0Color);
	}

	public void procedure() {
		if(isOnFire) {
			isTerminated = true;
		}
		super.procedure();
		
	}
	
	public void onTermination() {
		super.onTermination();
		Main.columns[xPos / 20].add(Main.createBlockByName("ZEROG", xPos, yPos));
	}
	
	public void render(Graphics g, int screenXPos, int screenYPos) {
		if(random.nextBoolean()) {
			g.setColor(Main.ice0Color);
		}
		else {
			g.setColor(Main.ice1Color);
		}
		g.fillRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.BLACK);
		
	}
}
