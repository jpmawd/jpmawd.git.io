package bounce;

import java.awt.Color;
import java.awt.Graphics;

public abstract class BlockEnemyMachine extends BlockEnemy{

	private long timeLastAttack = 0;
	Color c;
	
	public BlockEnemyMachine(int xPos, int yPos, Color c) {
		super(xPos, yPos);
		this.c = c;
	}

	public void procedure() {
		super.procedure();
		if(getStunTime()>0) {
			return;
		}
		if(timeLastAttack < System.currentTimeMillis() - 400) {
			if(Main.distance(xCenter, yPos, Main.player1.getxCenter(), Main.player1.getyCenter()) < 300) {
				double angle = Math.atan2(-1 * (yPos - Main.player1.getyCenter()), (Main.player1.getxCenter() - xCenter));
				attack(angle);
			}
			timeLastAttack = System.currentTimeMillis();
		}
	}
	
	public void render(Graphics g, int screenXPos, int screenYPos) {
		super.render(g, screenXPos, screenYPos);
		g.fillRect(screenXPos, screenYPos, 20, 20);
		g.setColor(c);
		int radius = (getStunTime()>0) ? 10 : (int) (20 * (double)((double)((System.currentTimeMillis()) % 100) / 100));
		g.drawOval((int)(screenXPos - radius / 2 + this.width / 2), (int)(screenYPos - radius / 2 + this.height / 2), radius, radius);
		g.setColor(Color.BLACK);
	}
	
	public void onTermination() {
		super.onTermination();
		if(random.nextBoolean()) {
			if(random.nextBoolean()) {
				Main.items.add(new Item("HEALTH", 100, xPos, yPos, 4 * (random.nextDouble() - .50), -4, true));
			}
			else {
				Main.items.add(new Item("AMMO", 500, xPos, yPos, 4 * (random.nextDouble() - .50), -4, true));
			}
		}
	}

	public abstract void attack(double angle);
}
