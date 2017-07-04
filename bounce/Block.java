package bounce;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public abstract class Block {
	public static Random random = new Random();
	public static Color normalColor = new Color(238, 238, 238);
	int xPos;
	int yPos;
	int width;
	int height;
	double xCenter;
	double yCenter;
	long timeLastUpdate;
	long timeInstance;
	boolean isFlammable = false, isOnFire = false;
	boolean isTerminated;
	Block(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = 20;
		this.height = 20;
		xCenter = (this.xPos + .5 * this.width);
		yCenter = (this.yPos + .5 * this.height);
		timeLastUpdate = System.currentTimeMillis();
		timeInstance = System.currentTimeMillis();
	}
	
	public void update() {
		if(timeLastUpdate < System.currentTimeMillis() - 10) {
			if(yPos > 600) {
				isTerminated = true;
			}
			if(isOnFire) {
				burn();
			}
			
			procedure();
			
			timeLastUpdate = System.currentTimeMillis();
		}
	}
	
	public abstract void procedure();
	
	public void renderBlock(Graphics g, int screenXPos, int screenYPos) {
		render(g, screenXPos, screenYPos);
		if(!(this instanceof BlockEnemyMachine || this instanceof BlockLiquid || this instanceof BlockPow)) {
			g.drawRect(screenXPos, screenYPos, this.width, this.height);
			g.drawRect(screenXPos + 2, screenYPos + 2, this.width - 4, this.height - 4);
		}
	}
	
	public abstract void render(Graphics g, int screenXPos, int screenYPos);
	
	public void burn() {
		
		if(random.nextInt(10) == 0) {
			if(isFlammable) {
				
				Main.fires.add(new Fire(xCenter + 30 * random.nextDouble(), yCenter + 30 * random.nextDouble(), (random.nextDouble() - .5), -(random.nextDouble()), 1));
				
			}
			
			else { 
				Main.fires.add(new Fire(xCenter + 30 * random.nextDouble(), yCenter + 30 * random.nextDouble(), (random.nextDouble() - .5), -(random.nextDouble())));
			}
			
		}
	
		
	}
}

/*
package bounce;

import java.util.ArrayList;

public class Block {
	int xPos;
	int yPos;
	int width;
	int height;
	double xCenter;
	double yCenter;
	long timeLastUpdate;
	long timeLastFire;
	long timeInstance;
	int health, maxHealth;
	boolean isSpreadable = false, isFlammable = false, isOnFire = false;
	boolean isTerminated;
	String type;
	Block(int xPos, int yPos, int width, int height, String type) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		xCenter = (this.xPos + .5 * this.width);
		yCenter = (this.yPos + .5 * this.height);
		this.type = type;
		if (this.type == "REFLECT") {
			health = 500;
			maxHealth = health;
		}
		else if (this.type == "BOMB") {
			health = 1;
			maxHealth = health;
		}
		else if (this.type == "UNBREAKABLE" || this.type == "PORTAL" || this.type == "ZEROG" || this.type == "LAVA" || this.type.contains("POWERUP")) {
			health = 1000000;
			maxHealth = health;
			if(this.type == "ZEROG" || this.type == "LAVA") {
				isSpreadable = true;
			}
		}
		else if (this.type == "TURRET" || this.type == "ROCKET") {
			health = 300;
			maxHealth = health;
			timeLastFire = System.currentTimeMillis();
		}
		else if (this.type == "BOUNCE") {
			health = 2000;
			maxHealth = health;
		}
		else {
			health = 500;
			maxHealth = health;
		}
		if(this.type == "WOOD" || this.type == "BOMB") {
			isFlammable = true;
		}
		if(this.type == "LAVA") {
			isOnFire = true;
		}
		timeLastUpdate = System.currentTimeMillis();
		timeInstance = System.currentTimeMillis();
	}
	
	public void update() {
		if(timeLastUpdate < System.currentTimeMillis() - 10) {
			procedure();
			if(health < 1 || isTerminated) {
				isTerminated = true;
				if(type.equals("NORMAL") || type.equals("WOOD") || type.equals("BOUNCE")) {
					Main.gfx.add(GraphicEffect.CRUMBLE(xPos, yPos));
				}
				else if(type.equals("REFLECT")) {
					Main.gfx.add(GraphicEffect.SHATTER(xPos, yPos));
				}
				else if(type == "TURRET" || type == "ROCKET" || type.contains("BOSS")) {
					Main.gfx.add(GraphicEffect.DUST(xPos, yPos));
					if(!(type == "BOSSB")) {
						if(Math.random() < .50) {
							if(Math.random() < .50) {
								Main.items.add(new Item("HEALTH", 100, xPos, yPos, 4 * (Math.random() - .50), -4, true));
							}
							else {
								Main.items.add(new Item("AMMO", 500, xPos, yPos, 4 * (Math.random() - .50), -4, true));
							}
						}
					}
				}
			}
			else {
				if(Math.random() > .99 && health < maxHealth / 2) {
					Main.ps.add(new ParticleSystem(ParticleSystem.generateRandomParticles(xCenter, yPos, 10, 30 * health / maxHealth), (ParticleSystem.generateRandomForces(xCenter, yCenter + 20, .1, 5, 3))));
				}
			}
			timeLastUpdate = System.currentTimeMillis();
		}
	}
	public void procedure() {
		
		if (this.type == "BOMB") {
			if(health < 1) {
				if(health == 0) {
					Main.bombs.add(new Bomb(xPos, yPos, 0, 0, 150, true));
				}
				else {
					Main.bombs.add(new Bomb(xPos, yPos, 0, 0, 0, true));
				}
			}
		}
		else if (this.type == "UNBREAKABLE" || this.type.equals("PORTAL")) {
			health = maxHealth;
		}
		
		else if (this.type == "TURRET") {
			if(timeLastFire < System.currentTimeMillis() - 400) {
				if(Main.distance(xCenter, yPos, Main.player1.getxCenter(), Main.player1.getyCenter()) < 300) {
					double angle = Math.atan2(-1 * (yPos - Main.player1.getyPos()), (Main.player1.getxPos() - xCenter));
					Main.shots.add(new Shot(xCenter, yPos, 5 * Math.cos(angle), 5 * Math.sin(angle), 10, false));
					//System.out.println(xPos);
				}
				timeLastFire = System.currentTimeMillis();
			}
		}
		else if (this.type == "ROCKET") {
			if(timeLastFire < System.currentTimeMillis() - 400) {
				if(Main.distance(xPos, yPos, Main.player1.getxCenter(), Main.player1.getyCenter()) < 300) {
					double angle = Math.atan2(-1 * (yPos - Main.player1.getyPos()), (Main.player1.getxPos() - xPos));
					Main.bombs.add(new Bomb(xPos, yPos, 10 * Math.cos(angle), 10 * Math.sin(angle), 150, false));
					//System.out.println(10 * Math.cos(angle));
				}
				timeLastFire = System.currentTimeMillis();
			}
		}
		
		else if (isSpreadable) {
			if(yPos > 600) {
				isTerminated = true;
			}
			else {
				double speed = .00;
				if(type == "ZEROG") {
					speed = .10;
				}
				else if(type == "LAVA") {
					speed = .05;
				}
				
				if(Math.random() < speed) {
					spread(xPos, yPos, type);
					
				}
			}
			health = maxHealth;
	}
		
		else if(this.type.contains("POWERUP")) {
			health = maxHealth;
		}
		else if(this.type.equals("BOSSB")) {
			if(Main.bosses.size() > 0) {
				health = maxHealth;
			}
			else {
				health = 0;
				isTerminated = true;
			}
		}
		
		else {
			
		}
		if(isOnFire) {
			burn();
		}
	}
	
	public static void spread(int xPos, int yPos, String type) {
		
			boolean isDown = false, isLeft = false, isRight = false, isWaterfall = false, isRightRight = false, isLeftLeft = false, isRightDown = false, isLeftDown = false;
			//int xTest;
			int yTest;
			Block block;
			ArrayList<Block> column;
			int index;
			index = xPos / 20;
			if(index >= 0 && index < Main.columns.length) {
			column = Main.columns[index];
			for(int i = 0; i < column.size(); i++) {
				block = column.get(i);
				//xTest = block.xPos;
				yTest = block.yPos;
				if(yTest == yPos + 20) {
					isDown = true;
					if(block.type.equals(type)) {
						isWaterfall = true;
					}
				}
				if(isDown) {
					break;
				}
			}
			}
			index = xPos / 20 + 1;
			if(index >= 0 && index < Main.columns.length) {
			column = Main.columns[index];
			for(int i = 0; i < column.size(); i++) {
				block = column.get(i);
				//xTest = block.xPos;
				yTest = block.yPos;
				if(yTest == yPos) {
					isRight = true;
				}
				else if(yTest == yPos + 20 && block.type.equals(type)) {
					isRightDown = true;
				}
				if(isRight && isRightDown) {
					break;
				}
			}
			}
			index = xPos / 20 - 1;
			if(index >= 0 && index < Main.columns.length) {
			column = Main.columns[index];
			for(int i = 0; i < column.size(); i++) {
				block = column.get(i);
				//xTest = block.xPos;
				yTest = block.yPos;
				if(yTest == yPos) {
					isLeft = true;
				}
				else if(yTest == yPos + 20 && block.type.equals(type)) {
					isLeftDown = true;
				}
				if(isLeft && isLeftDown) {
					break;
				}
			}
			}
			index = xPos / 20 + 2;
			if(index >= 0 && index < Main.columns.length) {
			column = Main.columns[index];
			for(int i = 0; i < column.size(); i++) {
				block = column.get(i);
				//xTest = block.xPos;
				yTest = block.yPos;
				if(yTest == yPos) {
					isRightRight = true;
				}
				if(isRightRight) {
					break;
				}
			}
			}
			index = xPos / 20 - 2;
			if(index >= 0 && index < Main.columns.length) {
			column = Main.columns[index];
			for(int i = 0; i < column.size(); i++) {
				block = column.get(i);
				//xTest = block.xPos;
				yTest = block.yPos;
				if(yTest == yPos && block.type.equals(type)) {
					isLeftLeft = true;
				}
				if(isLeftLeft) {
					break;
				}
			}
			}
			if(!isWaterfall) {
				if(isDown) {
					if(!isRight) {
						//Main.world1.add(new Block(xPos + 20, yPos, 20, 20, "ZEROG"));
						index = xPos / 20 + 1;
						if(index >= 0 && index < Main.columns.length) {
							Main.columns[index].add(new Block(xPos + 20, yPos, 20, 20, type));
						}
					}
					if(!isLeft) {
						//Main.world1.add(new Block(xPos - 20, yPos, 20, 20, "ZEROG"));
						index = xPos / 20 - 1;
						if(index >= 0 && index < Main.columns.length) {
							Main.columns[index].add(new Block(xPos - 20, yPos, 20, 20, type));
						}
					}
				}
				else {
					//Main.world1.add(new Block(xPos, yPos + 20, 20, 20, "ZEROG"));
					index = xPos / 20;
					if(index >= 0 && index < Main.columns.length) {
						Main.columns[index].add(new Block(xPos, yPos + 20, 20, 20, type));
					}
				}
			}
			if((!isRight) && isRightRight && isRightDown) {
				//Main.world1.add(new Block(xPos + 20, yPos, 20, 20, "ZEROG"));
				index = xPos / 20 + 1;
				if(index >= 0 && index < Main.columns.length) {
					Main.columns[index].add(new Block(xPos + 20, yPos, 20, 20, type));
				}
			}
			if((!isLeft) && isLeftLeft && isLeftDown) {
				//Main.world1.add(new Block(xPos - 20, yPos, 20, 20, "ZEROG"));
				index = xPos / 20 - 1;
				if(index >= 0 && index < Main.columns.length) {
					Main.columns[index].add(new Block(xPos - 20, yPos, 20, 20, type));
				}
			}
		
	}
	
	public void burn() {
		
		if(Math.random() > .90) {
			if(isFlammable) {
				
				Main.fires.add(new Fire(xCenter + 30 * Math.random(), yCenter + 30 * Math.random(), (Math.random() - .5), -(Math.random()), 1));
				
			}
			
			else { 
				Main.fires.add(new Fire(xCenter + 30 * Math.random(), yCenter + 30 * Math.random(), (Math.random() - .5), -(Math.random())));
			}
			
		}
	
		
	}
}
*/