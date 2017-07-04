package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockEnemyVoid extends BlockEnemy{
	
	public BlockEnemyVoid(int xPos, int yPos) {
		super(xPos, yPos);
		this.maxHealth = 100;
		this.health = this.maxHealth;
	}

	public void procedure() {
		super.procedure();
		if(getStunTime()>0) {
			return;
		}
		if(random.nextInt(4) == 0) {
			int x = this.xPos;
			int y = this.yPos;
			if(random.nextBoolean()) {
				if(random.nextBoolean()) {
					x += 20;
				}
				else {
					x -= 20;
				}
			}
			else {
				if(random.nextBoolean()) {
					y += 20;
				}
				else {
					y -= 20;
				}
			}
			int index = x / 20;
			if(index > 0 && index < Main.levelWidth / 20 && y <  600 && y >  0 && !(x == xPos && y == yPos) && Main.blockAt(x, y) == null) {
				Main.columns[index].add(new BlockEnemyVoid(x, y));
			}
			
		}
	}
	
	public void render(Graphics g, int screenXPos, int screenYPos) {
		super.render(g, screenXPos, screenYPos);
		g.setColor(Color.WHITE);
		g.drawOval(screenXPos + 4 + random.nextInt(12), screenYPos + 4 + random.nextInt(12), random.nextInt(6), random.nextInt(6));
		g.setColor(Color.BLACK);
	}
}
