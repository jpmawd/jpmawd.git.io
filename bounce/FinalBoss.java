package bounce;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;

public class FinalBoss {

	private static double xPos, yPos, xCenter, yCenter;
	private static double width = 200, height = 200;
	private static int health;
	public final static int MAX_HEALTH = 5000;
	private static boolean activated = false;
	private static long timeLastUpdate;
	private static Random random = new Random();
	
	public static void reset(boolean activate) {
		activated = activate;
		health = MAX_HEALTH;
	}
	
	public static void setPosition(double x, double y) {
		xPos = x;
		yPos = y;
		xCenter = xPos + width / 2;
		yCenter = yPos + height / 2;
	}
	
	public static void updateAndRender(Graphics g, int screenXPos, int screenYPos) {
		boolean update = timeLastUpdate < System.currentTimeMillis() - 50;
		if(activated) {
			int numCrystals = 0;
			Block block;
			for(int i = 0; i < Main.world1.size(); i++) {
				block = Main.world1.get(i);
				if(block instanceof BlockCrystal && !block.isTerminated) {
					numCrystals++;
					if(update) {
						setHealth(health + 4);
					}
					for(int ii = 0; ii < 4; ii++) {
						g.setColor(Color.YELLOW);
						g.drawLine((int) (xCenter + screenXPos - xPos), (int) yCenter, (int) (screenXPos - xPos + block.xCenter + random.nextInt(10) - 5), (int) (block.yCenter + random.nextInt(10) - 5));
					}
				}
			}
			if(numCrystals == 0) {
				int radius;
				g.setColor(Color.YELLOW);
				for(int i = 0; i < 4; i++) {
					radius = (int)(400 * Math.cos(((double) System.currentTimeMillis()) / 1000 + Math.PI / 2 * i));
					g.drawOval((int) (xCenter + screenXPos - xPos - radius / 2), (int) yCenter - radius / 2, radius, radius);
				}
			}
			
			if(health < MAX_HEALTH / 2 && random.nextBoolean()) {
				g.setColor(Color.RED);
			}
			else {
				g.setColor(Color.ORANGE);
			}
			g.fillOval((int) screenXPos, (int) screenYPos, (int) width, (int) height);
			g.setColor(Color.RED);
			g.drawOval((int) screenXPos, (int) screenYPos, (int) width, (int) height);
			g.drawRect(screenXPos + 4 + random.nextInt((int) width), screenYPos + 4 + random.nextInt((int) height), random.nextInt(6), random.nextInt(6));
			g.setColor(Color.BLACK);
			
			if(update) {
				byte shotProperty = 0;
				if(health < MAX_HEALTH / 2) {
					shotProperty = Shot.FIRE_BALL;
				}
				if(random.nextInt(10) == 0) {
					double angle = Math.atan2(Main.player1.getyPos() - yCenter, Main.player1.getxPos() - xCenter);
					Main.shots.add(new Shot(xCenter, yCenter, 8 * Math.cos(angle), 8 * Math.sin(angle), 12, false, shotProperty));
				}
				else if(random.nextInt(100) == 0) {
					double angle = 0;
					for(int i = 0; i < 20; i++) {
						angle += Math.PI / 10;
						Main.shots.add(new Shot(xCenter, yCenter, 2 * Math.cos(angle), 2 * Math.sin(angle), 24, false, shotProperty));
					}
				}
				
				if(health < 1) {
					g.setColor(Color.RED);
					g.drawRect(0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
					Main.gfx.add(GraphicEffect.FRAGMENT(xCenter, yCenter, 0, 2, 100));
					Main.gfx.add(GraphicEffect.FIRE_EXPLOSION(xCenter, yCenter - 10, 100));
					Main.gfx.add(GraphicEffect.FIRE_EXPLOSION(xCenter - 20, yCenter, 100));
					Main.gfx.add(GraphicEffect.FIRE_EXPLOSION(xCenter + 20, yCenter, 100));
					double angle = 0;
					for(int i = 0; i < 20; i++) {
						angle += Math.PI / 10;
						Main.gfx.add(GraphicEffect.FLAMETHROWER(xCenter, yCenter, 2 * Math.cos(angle), 2 * Math.sin(angle), 20, 1));
					}
					Main.variablePortals.add(new VPortal((int) xPos, (int) yPos, 50));
					
					reset(false);
				}
				
				timeLastUpdate = System.currentTimeMillis();
			}
		}
		g.setColor(Color.BLACK);
	}
	
	public static boolean isActivated() {
		return activated;
	}
	
	public static int getHealth() {
		return health;
	}
	
	public static void setHealth(int newHealth) {
		if(newHealth > MAX_HEALTH) {
			health = MAX_HEALTH;
		}
		else {
			health = newHealth;
		}
	}

	public static double getxPos() {
		return xPos;
	}
	
	public static double getyPos() {
		return yPos;
	}

	public static double getxCenter() {
		return xCenter;
	}

	public static void setxCenter(double xCenter) {
		FinalBoss.xCenter = xCenter;
	}

	public static double getyCenter() {
		return yCenter;
	}

	public static void setyCenter(double yCenter) {
		FinalBoss.yCenter = yCenter;
	}

	public static double getWidth() {
		return width;
	}

	public static double getHeight() {
		return height;
	}
}
