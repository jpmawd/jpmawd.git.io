package bounce;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Boss {

	private ArrayList<Block> blocks = new ArrayList<Block>();
	private String name = "";
	private int nameInt = 0;
	private static final int UFO = 1, CYCLOPS = 2, DRAGON = 3, TROLL = 4;
	private String mode = "NONE";
	private long modeTimer = 0;
	private int health = 1;
	private int maxHealth = 1;
	private int xPos;
	private int yPos;
	private int identifier = 0;
	private String signature = "";
	private int nNodes;
	private int[] xNodes;
	private int[] yNodes;
	private int minColumn, maxColumn;
	private int difficulty = 0;
	private static Random random = new Random();
	private boolean isTerminated = false;
	
	public Boss(String name, int maxHealth, int xPos, int yPos, int identifier) {
		this.name = name;
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.xPos = xPos;
		this.yPos = yPos;
		this.identifier = identifier;
		this.signature = "BOSS" + this.name + this.identifier;
		modeTimer = System.currentTimeMillis();
		if(this.signature.contains("UFO")) {
			nameInt = UFO;
			difficulty = 20;
			nNodes = 11;
			minColumn = xPos - 60;
			maxColumn = xPos + 60;
		}
		else if(this.signature.contains("CYCLOPS")) {
			nameInt = CYCLOPS;
			difficulty = 2;
			nNodes = 42;
			minColumn = xPos;
			maxColumn = xPos + 100;
		}
		else if(this.signature.contains("DRAGON")) {
			nameInt = DRAGON;
			difficulty = 50;
			nNodes = 59;
			minColumn = xPos - 260;
			maxColumn = xPos - 20;
		}
		else if(this.signature.contains("TROLL")) {
			nameInt = TROLL;
			difficulty = 25;
			nNodes = 82;
			minColumn = xPos;
			maxColumn = xPos + 300;
		}
		
	}
	
	public void procedure() {
		//System.out.println(blocks.size());
		switch(nameInt) {
		
		case UFO : {
			/*
			 *    xxx
			 *  xxxxxxx
			 *     x
			 */
			
			
			int[] nodesX = {
					xPos - 60,
					xPos - 40,
					xPos - 20,
					xPos ,
					xPos + 20,
					xPos + 40,
					xPos + 60,
					xPos - 20,
					xPos ,
					xPos + 20,
					xPos 
			};
			
			int[] nodesY = {
					yPos ,
					yPos ,
					yPos ,
					yPos ,
					yPos ,
					yPos ,
					yPos ,
					yPos - 20,
					yPos - 20,
					yPos - 20,
					yPos + 20,
			};
			
			xNodes = nodesX;
			yNodes = nodesY;
			
			updateBlocks();
			
			minColumn = xPos - 60;
			maxColumn = xPos + 60;
			
			////////////////////do stuff after here/////////////////////////
			if(Math.random() < .50) {
				if(Main.distance(Main.player1.getxVelocity(), Main.player1.getyVelocity(), 0, 0) < 1) {
					double angle = Math.atan2(-1 * (yPos - Main.player1.getyPos()), (Main.player1.getxPos() - xPos));
					Main.shots.add(new Shot(xPos, yPos, 2 * Math.cos(angle), 2 * Math.sin(angle), 6, false, Shot.LASER));
				}
				else if(Math.abs(xPos - Main.player1.getxPos()) < 10) {
					if(Math.random() < .50) {
						Main.shots.add(new Shot(xPos, yPos, .5, 5, 6, false, Shot.LASER));
						Main.shots.add(new Shot(xPos, yPos, -.5, 5, 6, false, Shot.LASER));
					}
				}
				else if(Math.abs(yPos - Main.player1.getyPos()) < 20) {
					if(Math.random() < .25) {
						Main.shots.add(new Shot(xPos, yPos, 2 * Math.signum(Main.player1.getxPos() - xPos), 0, 20, false));
						Main.shots.add(new Shot(xPos - 20 * Math.signum(Main.player1.getxPos() - xPos), yPos + 20, 2 * Math.signum(Main.player1.getxPos() - xPos), 0, 8, false));
						Main.shots.add(new Shot(xPos - 10 * Math.signum(Main.player1.getxPos() - xPos), yPos + 15 , 2 * Math.signum(Main.player1.getxPos() - xPos), 0, 8, false));
						Main.shots.add(new Shot(xPos - 20 * Math.signum(Main.player1.getxPos() - xPos), yPos - 20, 2 * Math.signum(Main.player1.getxPos() - xPos), 0, 8, false));
						Main.shots.add(new Shot(xPos - 10 * Math.signum(Main.player1.getxPos() - xPos), yPos - 15 , 2 * Math.signum(Main.player1.getxPos() - xPos), 0, 8, false));
					}
				}
			}
			
			else if(Math.random() < .05) {
				teleport();
			}
			break;
		}
		case DRAGON : {
			/* 
			 * xxxx      xxxx
			 *  xxxx    xxxx
			 *    xx    xx
			 *     xxxxx
			 *      xxx  x
			 *   xxxxxxxxxxx
			 *   xxxxxxxxxx
			 *   x  x x  xxx
			 *   x
			 *   xx
			 */
			

			int[] nodesX = {
			xPos + -260,

			xPos + -240,

			xPos + -220,

			xPos + -200,

			xPos + -80,

			xPos + -60,

			xPos + -40,

			xPos + -20,

			xPos + -240,

			xPos + -220,

			xPos + -200,

			xPos + -180,

			xPos + -100,

			xPos + -80,

			xPos + -60,

			xPos + -40,

			xPos + -200,

			xPos + -180,

			xPos + -100,

			xPos + -80,

			xPos + -180,

			xPos + -160,

			xPos + -140,

			xPos + -120,

			xPos + -100,

			xPos + -160,

			xPos + -140,

			xPos + -120,

			xPos + -60,

			xPos + -220,

			xPos + -200,

			xPos + -180,

			xPos + -160,

			xPos + -140,

			xPos + -120,

			xPos + -100,

			xPos + -80,

			xPos + -60,

			xPos + -40,

			xPos + -20,

			xPos + -220,

			xPos + -200,

			xPos + -180,

			xPos + -160,

			xPos + -140,

			xPos + -120,

			xPos + -100,

			xPos + -80,

			xPos + -60,

			xPos + -40,

			xPos + -220,

			xPos + -160,

			xPos + -120,

			xPos + -60,

			xPos + -40,

			xPos + -20,

			xPos + -220,

			xPos + -220,

			xPos + -200,
			};

			int[] nodesY = {
			yPos + -180,

			yPos + -180,

			yPos + -180,

			yPos + -180,

			yPos + -180,

			yPos + -180,

			yPos + -180,

			yPos + -180,

			yPos + -160,

			yPos + -160,

			yPos + -160,

			yPos + -160,

			yPos + -160,

			yPos + -160,

			yPos + -160,

			yPos + -160,

			yPos + -140,

			yPos + -140,

			yPos + -140,

			yPos + -140,

			yPos + -120,

			yPos + -120,

			yPos + -120,

			yPos + -120,

			yPos + -120,

			yPos + -100,

			yPos + -100,

			yPos + -100,

			yPos + -100,

			yPos + -80,

			yPos + -80,

			yPos + -80,

			yPos + -80,

			yPos + -80,

			yPos + -80,

			yPos + -80,

			yPos + -80,

			yPos + -80,

			yPos + -80,

			yPos + -80,

			yPos + -60,

			yPos + -60,

			yPos + -60,

			yPos + -60,

			yPos + -60,

			yPos + -60,

			yPos + -60,

			yPos + -60,

			yPos + -60,

			yPos + -60,

			yPos + -40,

			yPos + -40,

			yPos + -40,

			yPos + -40,

			yPos + -40,

			yPos + -40,

			yPos + -20,

			yPos + 0,

			yPos + 0,
			};

			xNodes = nodesX;
			yNodes = nodesY;
			
			updateBlocks();
			
			minColumn = xPos - 260;
			maxColumn = xPos - 20;
						
			if(health < 1) {
				Main.gfx.add(GraphicEffect.FIRE_EXPLOSION(xPos - 140, yPos - 100, .5));
				Main.gfx.add(GraphicEffect.FIRE_EXPLOSION(xPos - 80, yPos, .8));
				Main.gfx.add(GraphicEffect.FIRE_EXPLOSION(xPos - 20, yPos - 60, 1));
			}
			else {
			double angle = Math.atan2(-1 * ((yPos - 60) - Main.player1.getyPos()), (Main.player1.getxPos() - (xPos - 20)));
			
			if(mode.equals("BEAM")) {
				if(modeTimer > System.currentTimeMillis() - 1000) {
					moveY((int) (-20 * Math.signum(yPos - 60 - Main.player1.getyPos())));
					if(Math.random() > .98) {
						Main.gfx.add(GraphicEffect.FIRE(xPos - 30, yPos - 50, 100));
					}
				}
				else if(Math.random() > .25) {
					Main.shots.add(new Shot(xPos - 20, yPos - 60, 10, Math.random() - .50, 10, false, Shot.FIRE_BALL));
					Main.gfx.add(GraphicEffect.FLAMETHROWER(xPos - 30, yPos - 50, 10, 0, 20));
				}
				if(modeTimer < System.currentTimeMillis() - 2000) {
					mode = "BOMB";
				}
			}
			else if(mode.equals("COMET")) {
				if(modeTimer > System.currentTimeMillis() - 1000) {
					if(yPos - 60 - Main.player1.getyPos() > -200 ) {
						moveY(-10);
					}
				}
				else {
					double newAngle = Math.atan2(-1 * ((yPos - 60) - (Main.player1.getyPos())), ((Main.player1.getxPos() - 40 * Main.player1.getxVelocity()) - (xPos - 20)));
					Main.shots.add(new Shot(xPos - 20, yPos - 60, 20 * Math.cos(newAngle), 20 * Math.sin(newAngle), 40, false, Shot.FIRE_BALL));
					//Main.shots.add(new Shot(xPos - 20, yPos - 60, 18 * Math.cos(newAngle), 18 * Math.sin(newAngle), 20, false));
					//Main.shots.add(new Shot(xPos - 20, yPos - 60, 16 * Math.cos(newAngle), 16 * Math.sin(newAngle), 10, false));
					moveY(10);
					mode = "NONE";
				}
			}
			else if(mode.equals("BOMB")) {
				if(yPos - 60 - Main.player1.getyPos() > -400 && modeTimer > System.currentTimeMillis() - 500) {
					moveY(-10);
					moveX((int) (-20 * Math.signum(xPos - 20 - Main.player1.getxPos())));
				}
				
				else {
					Main.bombs.add(new Bomb(xPos - 240, yPos + 60, 0, 6, 150, false));
					mode = "NONE";
				}
			}
			else if(mode.equals("LAUNCH")) {
				double deltaY = Main.player1.getyPos() - (yPos + 60);
				double t = Math.sqrt(20 * Math.abs(deltaY));
				double deltaX = Main.player1.getxPos() - (xPos - 240);
				double vi = deltaX / t;
				vi += Main.player1.getxVelocity();
				Main.bombs.add(new Bomb(xPos - 240, yPos + 60, vi, 0, (int) t + 50, true));
				moveX((int) (-1 * Math.signum(xPos - 20 - Main.player1.getxPos())));
				mode = "NONE";
			}
			else {
				if(Math.abs(xPos - 20 - Main.player1.getxPos()) > 100 && Math.random() > .25) {
					moveX((int) (-20 * Math.signum(xPos - 20 - Main.player1.getxPos())));
				}
				else {
					moveY((int) (-20 * Math.signum(yPos - 60 - Main.player1.getyPos())));
				}
				if(Math.random() > .99) {
					if(Math.abs(xPos - 240 - Main.player1.getxPos()) > 600 && yPos - 240 - Main.player1.getyPos() < 0) {
						mode = "LAUNCH";
						modeTimer = System.currentTimeMillis();
					}
				}
				else if(Math.random() > .99 && (xPos - 20 - Main.player1.getxPos()) < -60) {
					mode = "BEAM";
					modeTimer = System.currentTimeMillis();
					
				}
				else if(Math.random() > .99) {
					mode = "COMET";
					modeTimer = System.currentTimeMillis();
				}
				else if(Math.random() > .99) {
					mode = "BOMB";
					modeTimer = System.currentTimeMillis();
				}
				else {
					if(Math.random() > .90) {
						Main.shots.add(new Shot(xPos - 20, yPos - 60, 6 * Math.cos(angle), 6 * Math.sin(angle), 6, false));
					}
				}
			}
			}
			
			break;
		}
		case CYCLOPS : {
			/*      xxxx
			 *      xxxx 
			 *       xx
			 *       xx
			 *     xxxxxx
			 *     x xx x
			 *     x xx x
			 *     x xx x
			 *       xx 
			 *      xxxx
			 *     x    x 
			 *     x    x 
			 *     x    x 
			 *          
			 */  
			
			
			int[] nodesX = {
					xPos + 20,
					xPos + 40,
					xPos + 60,
					xPos + 80,
					
					xPos + 20,
					xPos + 40,
					xPos + 60,
					xPos + 80,
					
					xPos + 40,
					xPos + 60,
					
					xPos + 40,
					xPos + 60,
					
					xPos,
					xPos + 20,
					xPos + 40,
					xPos + 60,
					xPos + 80,
					xPos + 100,
					
					xPos,
					xPos + 40,
					xPos + 60,
					xPos + 100,
					
					xPos,
					xPos + 40,
					xPos + 60,
					xPos + 100,
					
					xPos,
					xPos + 40,
					xPos + 60,
					xPos + 100,
					
					xPos + 40,
					xPos + 60,
					
					xPos + 20,
					xPos + 40,
					xPos + 60,
					xPos + 80,
					
					xPos,
					xPos + 100,
					
					xPos,
					xPos + 100,
					
					xPos,
					xPos + 100,
			};
			
			int[] nodesY = {
					yPos - 240,
					yPos - 240,
					yPos - 240,
					yPos - 240,
					
					yPos - 220,
					yPos - 220,
					yPos - 220,
					yPos - 220,
					
					yPos - 200,
					yPos - 200,
					
					yPos - 180,
					yPos - 180,
					
					yPos - 160,
					yPos - 160,
					yPos - 160,
					yPos - 160,
					yPos - 160,
					yPos - 160,
					
					yPos - 140,
					yPos - 140,
					yPos - 140,
					yPos - 140,
					
					yPos - 120,
					yPos - 120,
					yPos - 120,
					yPos - 120,
					
					yPos - 100,
					yPos - 100,
					yPos - 100,
					yPos - 100,
					
					yPos - 80,
					yPos - 80,
					
					yPos - 60,
					yPos - 60,
					yPos - 60,
					yPos - 60,
					
					yPos - 40,
					yPos - 40,
					
					yPos - 20,
					yPos - 20,
					
					yPos,
					yPos,
			};
			
			xNodes = nodesX;
			yNodes = nodesY;
			
			updateBlocks();
			
			minColumn = xPos;
			maxColumn = xPos + 100;
			
			if(random.nextDouble() < .1 && Main.distance(xPos + 60, yPos - 220, Main.player1.getxPos(), Main.player1.getyPos()) < 600) {
				double angle = Math.atan2(-1 * ((yPos - 220) - Main.player1.getyPos()), (Main.player1.getxPos() - (xPos + 60)));
				Main.shots.add(new Shot(xPos + 60, yPos - 220, 6 * Math.cos(angle), 6 * Math.sin(angle), 6, false, Shot.LASER));
			}
			fall();
			if(Math.abs(xPos + 60 - Main.player1.getxPos()) > 100) {
				moveX((int) (-30 * Math.signum(xPos + 60 - Main.player1.getxPos())));
			}
			if(yPos - 240 > 600) {
				health = 0;
			}
			break;
		}
		case TROLL: {
			nNodes = 82;

			int[] nodesX = {
			xPos + 40,

			xPos + 60,

			xPos + 80,

			xPos + 100,

			xPos + 120,

			xPos + 140,

			xPos + 160,

			xPos + 180,

			xPos + 200,

			xPos + 220,

			xPos + 240,

			xPos + 20,

			xPos + 260,

			xPos + 0,

			xPos + 40,

			xPos + 60,

			xPos + 80,

			xPos + 120,

			xPos + 140,

			xPos + 160,

			xPos + 180,

			xPos + 280,

			xPos + 0,

			xPos + 60,

			xPos + 80,

			xPos + 160,

			xPos + 180,

			xPos + 220,

			xPos + 240,

			xPos + 300,

			xPos + 20,

			xPos + 60,

			xPos + 120,

			xPos + 240,

			xPos + 260,

			xPos + 300,

			xPos + 20,

			xPos + 80,

			xPos + 100,

			xPos + 180,

			xPos + 200,

			xPos + 220,

			xPos + 260,

			xPos + 300,

			xPos + 20,

			xPos + 40,

			xPos + 60,

			xPos + 80,

			xPos + 100,

			xPos + 120,

			xPos + 140,

			xPos + 160,

			xPos + 200,

			xPos + 280,

			xPos + 20,

			xPos + 60,

			xPos + 100,

			xPos + 140,

			xPos + 180,

			xPos + 260,

			xPos + 0,

			xPos + 40,

			xPos + 60,

			xPos + 80,

			xPos + 100,

			xPos + 120,

			xPos + 140,

			xPos + 160,

			xPos + 240,

			xPos + 0,

			xPos + 200,

			xPos + 220,

			xPos + 0,

			xPos + 120,

			xPos + 140,

			xPos + 160,

			xPos + 180,

			xPos + 20,

			xPos + 40,

			xPos + 60,

			xPos + 80,

			xPos + 100,
			};

			int[] nodesY = {
			yPos + 0,

			yPos + 0,

			yPos + 0,

			yPos + 0,

			yPos + 0,

			yPos + 0,

			yPos + 0,

			yPos + 0,

			yPos + 0,

			yPos + 0,

			yPos + 0,

			yPos + 20,

			yPos + 20,

			yPos + 40,

			yPos + 40,

			yPos + 40,

			yPos + 40,

			yPos + 40,

			yPos + 40,

			yPos + 40,

			yPos + 40,

			yPos + 40,

			yPos + 60,

			yPos + 60,

			yPos + 60,

			yPos + 60,

			yPos + 60,

			yPos + 60,

			yPos + 60,

			yPos + 60,

			yPos + 80,

			yPos + 80,

			yPos + 80,

			yPos + 80,

			yPos + 80,

			yPos + 80,

			yPos + 100,

			yPos + 100,

			yPos + 100,

			yPos + 100,

			yPos + 100,

			yPos + 100,

			yPos + 100,

			yPos + 100,

			yPos + 120,

			yPos + 120,

			yPos + 120,

			yPos + 120,

			yPos + 120,

			yPos + 120,

			yPos + 120,

			yPos + 120,

			yPos + 120,

			yPos + 120,

			yPos + 140,

			yPos + 140,

			yPos + 140,

			yPos + 140,

			yPos + 140,

			yPos + 140,

			yPos + 160,

			yPos + 160,

			yPos + 160,

			yPos + 160,

			yPos + 160,

			yPos + 160,

			yPos + 160,

			yPos + 160,

			yPos + 160,

			yPos + 180,

			yPos + 180,

			yPos + 180,

			yPos + 200,

			yPos + 200,

			yPos + 200,

			yPos + 200,

			yPos + 200,

			yPos + 220,

			yPos + 220,

			yPos + 220,

			yPos + 220,

			yPos + 220,
			};

			xNodes = nodesX;
			yNodes = nodesY;
			
			updateBlocks();
			
			minColumn = xPos;
			maxColumn = xPos + 300;
			if(health < 1) {
				for(int i = 0; i < nNodes; i++) {
					Main.bombs.add(new Bomb(xNodes[i], yNodes[i], 0, 0, 150, false));
				}
			}
			else if(random.nextBoolean()) {
				int x = random.nextInt(Main.levelWidth / 20) * 20;
				int y = random.nextInt(30) * 20;
				Block block = Main.blockAt(x, y);
				
				if(block instanceof BlockLiving && !(block instanceof BlockEnemy || block instanceof BossBarricade)) {
					
						int ranInt = random.nextInt(15);
						Class<? extends Block> c = BlockMirror.class;
						if(ranInt == 0) {
							c = BlockLava.class;
						}
						else if(ranInt == 1) {
							c = BlockLava.class;
						}
						else if(ranInt == 2) {
							c = BlockMirror.class;
						}
						else if(ranInt == 3) {
							c = BlockPowerup.class;
						}
						else if(ranInt == 4) {
							if(Main.distance(x, y, xPos + 100, yPos + 100) > 200) { 
								c = BlockBomb.class;
							}
						}
						else if(ranInt == 5) {
							c = BlockTurret.class;
						}
						else if(ranInt == 6) {
							c = BlockSnow.class;
						}
						else if(ranInt == 7) {
							c = BlockVegetation.class;
						}
						else if(ranInt == 8) {
							c = BlockWood.class;
						}
						else if(ranInt == 9) {
							c = BlockIce.class;
						}
						else {
							c = BlockTurret.class;
						}
						Main.replaceBlockWith(x, y, c);
					
						
						if(random.nextInt(200) == 0) {
							double playerX = Main.player1.getxCenter(), playerY = Main.player1.getyCenter();
							Main.gfx.add(GraphicEffect.createGraphicEffectByName("SHATTER", playerX, playerY, new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))));
							for(int i = 1; i < 11; i++) {
								Main.shots.add(new Shot(playerX, playerY - (280 + 20 * i), 0, 8, 10 / i + 5, false));
							}
						}
						else if(random.nextInt(15) == 0) {
							double playerX = Main.player1.getxCenter(), playerY = Main.player1.getyCenter();
							int blockX = (int)(playerX) - (int)(playerX) % 20, blockY = (int)(playerY) - (int)(playerY) % 20;
							double playerVelocityX = Main.player1.getxVelocity(), playerVelocityY = Main.player1.getyVelocity();
							Block block1 = new BlockNormal(blockX, blockY);
							if(playerVelocityY < -1) {
								block.yPos -= 40;
							}
							else if(playerVelocityX > 0){
								block.xPos -= 20;
							}
							else {
								block.xPos += 20;
							}
							Main.placeThisBlock(block1);
						}
				}
			}
			
			
			break;
		}
		
		default : {
			health = 1;
			break;
		}
		
		}
		if(health < 1) {
			for(int i = minColumn; i <= maxColumn; i += 20) {
				ArrayList<Block> column = Main.columns[i / 20];
				for(int ii = 0; ii < column.size(); ii++) {
					Block block = column.get(ii);
					if(block instanceof BossBlock && ((BossBlock)block).signature.equals(signature)) {
						block.isTerminated = true;
						column.set(ii, block);
						Main.items.add(new Item("HEALTH", 10 * difficulty, block.xPos, block.yPos, 4 * (Math.random() - .50), -4, true));
					}
				}
				Main.columns[i / 20] = column;
			}
			isTerminated = true;
		}
	}
	
	public void teleport(int x, int y) {
		
		for(int i = 0; i < nNodes; i++) {
			if((xNodes[i] - xPos + x) / 20 < 0 || (xNodes[i] - xPos + x) / 20 >= Main.columns.length) {
				return;
			}
			if((yNodes[i] - yPos + y) > 600) {
				return;
			}
			if(Main.blockAt(xNodes[i] - xPos + x, yNodes[i] - yPos + y) != null) {
				return;
			}
			if(Main.distance(xNodes[i] - xPos + x, yNodes[i] - yPos + y, Main.player1.getxPos(), Main.player1.getyPos()) < 40) {
				return;
			}
		}
		xPos = x;
		yPos = y;
	}
	public void teleport() {
		teleport((int) (20 * (int)(20 * (Math.random() - .5)) + 20 * (int)(Main.player1.getxPos()/20)), (int) (20 * (int)(20 * (Math.random() - .5)) + 20 * (int)(Main.player1.getyPos()/20)));
		
	}

	public void moveX(int pX) {
		if(pX != 0 && System.currentTimeMillis() % pX == 0) {
			int x = (int) (xPos + 20 * Math.signum(pX));
			
			for(int i = 0; i < nNodes; i++) {
				if((xNodes[i] - xPos + x) / 20 < 0 || (xNodes[i] - xPos + x) / 20 >= Main.columns.length) {
					return;
				}
				Block block = Main.blockAt(xNodes[i] - xPos + x, yNodes[i]);
				if(block != null && !(block instanceof BossBlock && ((BossBlock)block).signature.equals(this.signature))) {
					return;
				}
			}
			xPos = x;
		}
	}
	public void moveY(int pY) {
		if(pY != 0 && System.currentTimeMillis() % pY == 0) {
			int y = (int) (yPos + 20 * Math.signum(pY));
			for(int i = 0; i < nNodes; i++) {
				Block block = Main.blockAt(xNodes[i], yNodes[i] - yPos + y);
				if(block != null &&  !(block instanceof BossBlock && ((BossBlock)block).signature.equals(this.signature))) {
					return;
				}
			}
			yPos = y;
		}
	}
	public void fall() {
		moveY(10);
	}
	
	public void updateBlocks() {
		//System.out.println(xPos + " , " + yPos);
		int n = 0;
		ArrayList<Block> c = new ArrayList<Block>();
		for(int i = minColumn; i <= maxColumn; i += 20) {
			ArrayList<Block> column = Main.columns[i / 20];
			for(int ii = 0; ii < column.size(); ii++) {
				Block block = column.get(ii);
				if(block instanceof BossBlock && ((BossBlock)block).signature.equals(signature)) {
					health += ((BossBlock)block).health - 500;
					((BossBlock)block).health = 500;
					
					block.xPos = xNodes[n];
					block.yPos = yNodes[n];
					//System.out.println(n + " : " + (block.xPos) + " , " + (block.yPos));
					
					block.xCenter = block.xPos + block.width * .50;
					block.yCenter = block.yPos + block.height * .50;
					block.isTerminated = false;
					
					n++;
					c.add(block);
					//Block replacement = new BossBlock(0, 0, "NONE");
					//replacement.isTerminated = true;
					//column.set(ii, replacement);
					column.remove(ii);
					ii--;
				}
				if(n == nNodes) {
					break;
				}
			}
			Main.columns[i / 20] = column;
			if(n == nNodes) {
				break;
			}
		}
		for(int i = 0; i < c.size(); i++) {
			Block block = c.get(i);
			int index = block.xPos / 20;
			ArrayList<Block> column = Main.columns[index];
			column.add(block);
			Main.columns[index] = column;
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getMaxHealth() {
		return maxHealth;
	}
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getyPos() {
		return yPos;
	}
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	public int getnNodes() {
		return nNodes;
	}
	public void setnNodes(int nNodes) {
		this.nNodes = nNodes;
	}
	public int[] getxNodes() {
		return xNodes;
	}
	public void setxNodes(int[] xNodes) {
		this.xNodes = xNodes;
	}
	public int[] getyNodes() {
		return yNodes;
	}
	public void setyNodes(int[] yNodes) {
		this.yNodes = yNodes;
	}

	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public boolean isTerminated() {
		return isTerminated;
	}

	public void setTerminated(boolean isTerminated) {
		this.isTerminated = isTerminated;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public long getModeTimer() {
		return modeTimer;
	}

	public void setModeTimer(long modeTimer) {
		this.modeTimer = modeTimer;
	}

	public int getMinColumn() {
		return minColumn;
	}

	public void setMinColumn(int minColumn) {
		this.minColumn = minColumn;
	}

	public int getMaxColumn() {
		return maxColumn;
	}

	public void setMaxColumn(int maxColumn) {
		this.maxColumn = maxColumn;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}
	
}