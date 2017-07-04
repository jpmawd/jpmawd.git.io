package bounce;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Bomb extends Entity{
	
	int fuseDelay;
	int baseDamage;
	
	public Bomb(double xPos, double yPos, double xVelocity, double yVelocity, int fuseDelay, boolean gravity) {
		super(xPos, yPos, xVelocity, yVelocity, 20, gravity);
		this.fuseDelay = fuseDelay;
		this.baseDamage = 250;
	}
	public void procedure() {
		if(grounded()) {
			xVelocity = 0;
			yVelocity = 0;
		}
		if(grounded && !gravity && fuseDelay > 0) {
			fuseDelay = 0;
		}
			if(fuseDelay <= 0) {
			
				if(fuseDelay == 0) {
					Main.gfx.add(GraphicEffect.EXPLOSION(xCenter, yCenter, .5));
					Main.sounds.add(Main.ray);
					explosion(20);
				}
				else if(fuseDelay == -20) {
					explosion(60);
				}
				else if(fuseDelay == -40) {
					explosion(100);
					isTerminated = true;
				}
				
			}
			
			fuseDelay--;
	}
	public void explosion(int radius) {
		int numberColumns = (int)(radius / 20) + 2;
		for(int i = 0; i < numberColumns; i++) {
			int index = (int)(xCenter / 20) + (i - (int)(numberColumns / 2));
			if(index >= 0 && index < Main.columns.length) {
				ArrayList<Block> column = Main.columns[index];
				for(int ii = 0; ii < column.size(); ii++) {
					Block block = column.get(ii);
					if(!(block instanceof BlockLiquid) && Main.distance(xCenter, yCenter, block.xCenter + 10, block.yCenter) < radius) {
						if(Main.distance(xCenter, yCenter, block.xCenter, block.yCenter) < radius + block.width / 2 && block instanceof BlockLiving) {
							attackBlock(block);
							column.set(ii, block);
						}
					}
				}
				Main.columns[index] = column;
			}
		}
		for(int i = 0; i < Main.chests.size(); i++) {
			Chest testChest = Main.chests.get(i);
			if(Main.distance(xCenter, yCenter, testChest.getxCenter(), testChest.getyCenter()) < radius) {
				testChest.setHealth(testChest.getHealth() - baseDamage);
				Main.chests.set(i, testChest);
			}
		}
		if(Main.distance(xCenter, yCenter, Main.player1.getxCenter() + 10, Main.player1.getyCenter()) < radius) {
			Main.player1
					.attack(baseDamage, Player.EXPLOSION);
		}
	}
	
	public void attackBlock(Block block) {
		((BlockLiving)block).health -=	baseDamage;
	}
	
	public int getFuseDelay() {
		return fuseDelay;
	}
	public void setFuseDelay(int fuseDelay) {
		this.fuseDelay = fuseDelay;
	}
	public int getBaseDamage() {
		return baseDamage;
	}
	public void setBaseDamage(int baseDamage) {
		this.baseDamage = baseDamage;
	}
	public void renderEntity(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(normalColor);
		g.fillRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.BLACK);
		g.drawRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.RED);
		g.fillArc(screenXPos, screenYPos, 20, 20, 90, 360 - this.fuseDelay);
		if(this.fuseDelay < 0) {
			g.drawOval(screenXPos, screenYPos, 20, 20);
			if(this.fuseDelay < -10) {
				g.drawOval(screenXPos - 5, screenYPos - 5, 30, 30);
				if(this.fuseDelay < -20) {
					g.drawOval(screenXPos - 20, screenYPos - 20, 60, 60);
					if(this.fuseDelay < -30) {
						g.drawOval(screenXPos - 40, screenYPos - 40, 100, 100);
					}
				}
			}
		}
		g.setColor(Color.BLACK);
		g.drawOval(screenXPos, screenYPos, 20, 20);
	}
	
	
}
