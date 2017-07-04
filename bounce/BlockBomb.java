package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockBomb extends BlockLiving{

	public BlockBomb(int xPos, int yPos) {
		super(xPos, yPos, 1, null);
		this.isFlammable = true;
	}

	public void onTermination() {
		super.onTermination();
		
		if(health == 0) {
			Main.bombs.add(new Bomb(xPos, yPos, 0, 0, 150, true));
		}
		else {
			Main.bombs.add(new Bomb(xPos, yPos, 0, 0, 0, true));
		}
		
	}
	
	public void render(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(normalColor);
		g.fillRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.RED);
		g.drawOval(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.BLACK);
	}
	
}
