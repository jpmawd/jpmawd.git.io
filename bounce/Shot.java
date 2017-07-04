package bounce;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Shot extends Entity{

	
	int damage;
	boolean friendly;
	byte specialProperty = NONE;
	final static byte NONE = 0, FIRE_BALL = 1, LASER = 2;
	
	
	
	public Shot(double xPos, double yPos, double xVelocity, double yVelocity, double radius, boolean friendly) {
		super(xPos, yPos, xVelocity, yVelocity, radius, false);
		this.friendly = friendly;
		Main.sounds.add(Main.gun);
		
	}
	public Shot(double xPos, double yPos, double xVelocity, double yVelocity, double radius, boolean friendly, byte specialProperty) {
		this(xPos, yPos, xVelocity, yVelocity, radius, friendly);
		this.specialProperty = specialProperty;
	}
	public int collision(double theta, Object argument) {
		int damage = 0;
		boolean isCollision = false;
		if(isImpervious) {
			isImpervious = (timeInstance > System.currentTimeMillis() - 30);
		}
		else {
			if(argument instanceof BlockLiquid) {
				if(specialProperty != LASER) {
					xVelocity = xVelocity * .99;
					yVelocity = yVelocity * .99;
					if(specialProperty == FIRE_BALL) {
						isTerminated = true;
						Main.gfx.add(GraphicEffect.FRAGMENT(xPos, yPos, xVelocity / 2, yVelocity / 2, 20));
					}
				}
				
			}
			else if(argument instanceof BlockEnemy) {
				if(friendly) {
					isCollision = true;
				}
			}
			else {
				if(argument instanceof BlockMirror) {
					if(Math.abs(theta) > Math.PI / 4) {
						yVelocity = -.4 * yVelocity;
					}
					else {
						xVelocity = -1.2 * xVelocity;
						if(Math.abs(theta) > Math.PI / 8) {
							yVelocity = -.4 * yVelocity;
						}
					}
					xPos += 6 * Math.signum(xVelocity);
					yPos += 6 * Math.signum(yVelocity);
	
					friendly = false;
				}
				else {
					isCollision = true;
				}
			}
		}
		if(isCollision) {
			isTerminated = true;
			if(specialProperty == NONE) {
				Main.gfx.add(GraphicEffect.FRAGMENT(xCenter, yCenter, xVelocity / 4, yVelocity / 4, (int)radius));
			}
			if((specialProperty == FIRE_BALL) || (random.nextBoolean() && Math.hypot(xVelocity, yVelocity) > 6)) {
				Main.fires.add(new Fire(xCenter, yCenter, (Math.random() - .5), (Math.random() - .5)));
			}
			damage = (int) (radius * Main.distance(xVelocity, yVelocity, 0, 0));
			if(specialProperty == FIRE_BALL) {
				damage *= 1.5;
				Main.gfx.add(GraphicEffect.FIRE_EXPLOSION(xPos, yPos, 1, 1));
			}
		}
		return damage;
	}
	public void procedure() {
			if(System.currentTimeMillis() - timeInstance > 10000) {
				isTerminated = true;
			}
			for(int i = 0; i < 5; i++) {
				int index = (int)(xCenter / 20) + (i - 2);
				if(index >= 0 && index < Main.columns.length) {
					ArrayList<Block> column = Main.columns[index];
					for(int ii = 0; ii < column.size(); ii++) {
						Block block = column.get(ii);
						if(Main.distance(xCenter, yCenter, block.xCenter, block.yCenter) < radius + block.width / 2) {
							
								
								damage = collision(Math.atan((-1 * yCenter + block.yCenter)/(xCenter - block.xCenter)), block);
								
								if(block instanceof BlockLiving) { 
									((BlockLiving)block).health -= damage;
									column.set(ii, block);
								}
							
							
						}
					}
					Main.columns[index] = column;
				}
			}
			for(int i = 0; i < Main.chests.size(); i++) {
				if(Main.distance(xCenter, yCenter, Main.chests.get(i).getxCenter(), Main.chests.get(i).getyCenter()) < radius + Main.chests.get(i).getRadius() / 2) {
					Chest testChest = Main.chests.get(i);
										
					
					testChest.setHealth(testChest.getHealth() - collision(Math.atan((-1 * yCenter + testChest.getyCenter())/(xCenter - testChest.getxCenter())), testChest));
					
					
					Main.chests.set(i, testChest);
					
				}
			}
			if(!friendly) {
				if(Main.player1.isShielding() && Main.distance(xCenter, yCenter, Main.player1.getxCenter(), Main.player1.getyCenter()) < radius + Main.player1.getShieldRadius()) {
					Main.player1.setShield(Main.player1.getShield()
							- collision(Math.atan((-1 * yCenter + Main.player1.getyCenter()) / (xCenter - Main.player1.getxCenter())), "SHIELD"));
				}
				else if(Main.distance(xCenter, yCenter, Main.player1.getxCenter(), Main.player1.getyCenter()) < radius + Main.player1.getWidth() / 2) {
					Main.player1.attack(collision(Math.atan((-1 * yCenter + Main.player1.getyCenter()) / (xCenter - Main.player1.getxCenter())), "PLAYER"), Player.SHOT);
				
				}
			}
			else if(FinalBoss.isActivated()) {
				if(Main.distance(xCenter, yCenter, FinalBoss.getxCenter(), FinalBoss.getyCenter()) < radius + FinalBoss.getWidth() / 2) {
					FinalBoss.setHealth(FinalBoss.getHealth() - collision(0, "FINAL_BOSS"));
				}
			}
			
			if(specialProperty == FIRE_BALL) {
				if(random.nextInt(10) == 0) {
					Main.gfx.add(GraphicEffect.FLAMETHROWER(xPos, yPos, 0, 0, 40, 0));
				}
			}
	}
	@Override
	public void renderEntity(Graphics g, int screenXPos, int screenYPos) {
		if(specialProperty == NONE) {
			g.setColor(Main.bluePlasmaColor);
			g.fillOval(screenXPos, screenYPos, (int) (this.radius / 2), (int) (this.radius / 2));
			g.setColor(Color.BLACK);
			g.drawOval(screenXPos, screenYPos, (int) (this.radius / 2), (int) (this.radius / 2));
		}
		else if(specialProperty == FIRE_BALL) {
			if(random.nextBoolean()) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.ORANGE);
			}
			g.fillOval(screenXPos, screenYPos, (int) (this.radius / 2), (int) (this.radius / 2));
			g.setColor(Color.BLACK);
			g.drawOval(screenXPos, screenYPos, (int) (this.radius / 2), (int) (this.radius / 2));
		}
		else if(specialProperty == LASER) {
			g.setColor(Color.RED);
			double angle = Math.atan2(yVelocity, xVelocity);
			for(int i = (int) (-radius / 5); i < (int) (radius / 5); i++) {
				//g.drawLine((int) (screenXPos - xVelocity * i * radius / 10), (int) (screenYPos - xVelocity * i * radius / 10), (int) (screenXPos + xVelocity * i * radius / 10), (int) (screenYPos + yVelocity * i * radius / 10));
				g.drawLine((int) screenXPos + i, (int) screenYPos, (int) (screenXPos + radius * Math.cos(angle)) + i, (int) (screenYPos + radius * Math.sin(angle)));
			}
		}
		g.setColor(Color.BLACK);
	}
}
