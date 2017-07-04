package bounce;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Player {

	private Random random;
	private final int width;
	private final int height;
	private final double radius;
	private double xVelocity = 0.000;
	private double yVelocity = 0.000;
	private double xPos;
	private double yPos;
	private double xCenter;
	private double yCenter;
	private int health;
	private int ammo;
	private int oxygen;
	private int shield = 480;
	private int shieldMax = 480;
	private int shieldRadius = 60;
	private boolean isShielding = false;
	private String powerUp = "NONE";
	private int powerUpLength = 10000;
	private int healthCap = 2000;
	private int ammoCap = 2000;
	private int oxygenCap = 500;
	private boolean isUnderwater, isInCart = false;
	private String weaponName = "SHOT";
	private String status = "NORMAL";
	private boolean isRegenerate = false, isInvincible = false;
	private int nAnimations = 1;
	BufferedImage currentImage;
	BufferedImage[] animations;
	private int currentAnimation = 0;
	private byte damageSource = 1;
	public static final byte SHOT = 1, EXPLOSION = 2, DROWNING = 3, FIRE = 4, VOID = 5;
	private boolean dead = false;
	
	private long timeLastUpdate;
	private long timeLastAnimation;
	private long timeLastCollision;
	private long timeLastWeaponSwitch;
	private long timeLastShot;
	private long timeLastBomb;
	private long timeLastShieldUpdate;
	private long timePowerUp;
	
	int updateDelay = 14;
	
	public Player(int xPos, int yPos, int width, int height, int health, int initialAmmo) {
		random = new Random();
		this.setxPos(xPos);
		this.setyPos(yPos);
		this.width = width;
		this.height = height;
		setxCenter((this.getxPos() + .5 * this.getWidth()));
		setyCenter((this.getyPos() + .5 * this.getHeight()));
		radius = (.5 * this.getHeight());
		setHealth(health);
		setAmmo(initialAmmo);
		setOxygen(500);
		setTimeLastUpdate(System.currentTimeMillis());
		setTimeLastCollision(System.currentTimeMillis());
		setTimeLastWeaponSwitch(System.currentTimeMillis());
		setTimeLastShot(System.currentTimeMillis());
		setTimeLastBomb(System.currentTimeMillis());
		setTimeLastShieldUpdate(System.currentTimeMillis());
		setTimePowerUp(System.currentTimeMillis());
	}
	
	public void restore(boolean heal) {
		setxVelocity(0);
		setyVelocity(0);			
		if(heal) {
			setHealth(getHealthCap());
		}
		setAmmo(getAmmoCap());
		setWeaponName("SHOT");
		setPowerUp("NONE");
		setOxygen(500);
		setShield(getShieldMax());
		setInCart(false);
		setDead(false);
	}
	
	public boolean attack(int damage, byte damageSource) {
		if(!dead) {
			setHealth(getHealth() - damage);
			this.damageSource = damageSource;
		}
		return dead;
	}
	
	public void collision(double theta, Block block) {
		double multiplier;
		
		isInCart = false;
		
		if(block instanceof BlockBounce) {
			multiplier = 6;
		}
		else if(block instanceof BlockPowerup) {
			multiplier = 1;
			if(!this.powerUp.equals("NONE")) {
				this.weaponName = "SHOT";
			}
			this.powerUp = ((BlockPowerup)block).powerup;
			this.timePowerUp = System.currentTimeMillis();
		}
		else if(block instanceof BlockPortal) {
			multiplier = 0;
			Main.loadNextLevel = true;
		}
		else {
			multiplier = 1;
		}
			if(Math.abs(theta) > Math.PI / 4) {
				setyVelocity(-.4 * getyVelocity() * multiplier);
				if(block instanceof BlockPow) {
					((BlockPow)block).setPressed(true);
					this.yVelocity -= 1;
				}
				/*if(Math.abs(theta) < 3 * Math.PI / 8) {
					xVelocity = -1.2 * xVelocity;
				}*/
			}
			else {
				setxVelocity(-getxVelocity() * multiplier);
				if(Math.abs(theta) > Math.PI / 8) {
					setyVelocity(-.3 * getyVelocity() * multiplier);
				}
			}
			setxPos(getxPos() + 6 * Math.signum(getxVelocity()));
			setyPos(getyPos() + 6 * Math.signum(getyVelocity()));
		
			if(Main.UP && yCenter < block.yPos + block.height) {
				setyVelocity(getyVelocity() - 3);
			}			
	}
	public void update() {
		
		if(!Main.isPaused && getTimeLastUpdate() < System.currentTimeMillis() - updateDelay) {
			if(getTimeLastCollision() < System.currentTimeMillis() - 10) {
				setUnderwater(false);
				for(int i = 0; i < 5; i++) {
					//boolean isCollision = false;
					int index = (int)(xCenter / 20) + (i - 2);
					if(index >= 0 && index < Main.columns.length) {
						ArrayList<Block> column = Main.columns[index];
						for(int ii = 0; ii < column.size(); ii++) {
							Block block = column.get(ii);
					
							//if(Main.distance(getxCenter(), getyCenter(), block.xCenter, block.yCenter) < getRadius() + block.width / 2) {
							double angle = Math.atan((-1 * getyCenter() + block.yCenter)/(getxCenter() - block.xCenter));
							//double mPlayer = yVelocity / xVelocity;
							//double bPlayer = yPos;
							
							//if() {
								
							//}
							if(Main.distance(getxCenter() + xVelocity, getyCenter() + yVelocity, block.xCenter, block.yCenter) < getRadius() + block.width / 2) {
								if(block instanceof BlockLiquid) {
									
									setxVelocity(getxVelocity() * .9);
									setyVelocity(getyVelocity() * .9);
									if(Main.UP) {
										setyVelocity(getyVelocity() - .4);
									}
									if(Main.LEFT) {
										setxVelocity(getxVelocity() - .2);
									}
									if(Main.RIGHT) {
										setxVelocity(getxVelocity() + .2);
									}
									
									if(Main.DOWN) {
										setyVelocity(getyVelocity() + .2);
									}
									if(block instanceof BlockWater) {
										setUnderwater(true);
									}
									else if(block instanceof BlockLava) {
										attack(10, FIRE);
									}
								}
								else {
									collision(angle, block);
									if(block instanceof BlockLiving) { 
										((BlockLiving)block).health -= 1;
									}
									
									else if(block instanceof BlockPowerup) {
										block.isTerminated = true;
									}
									if(block instanceof BlockEnemyVoid) {
										attack(healthCap, VOID);
									}
									column.set(ii, block);
								}
								/*
								isCollision = true;
								break;
								*/
							}
						}
						Main.columns[index] = column;
					}
					/*
					if(isCollision) {
						break;
					}
					*/
				}
				setTimeLastCollision(System.currentTimeMillis());
			}
			setxPos(getxPos() + getxVelocity());
			setyPos(getyPos() + getyVelocity()); //1.5?
			setxCenter((this.getxPos() + .5 * this.getWidth()));
			setyCenter((this.getyPos() + .5 * this.getHeight()));
			setyVelocity(getyVelocity() + .1);
			if(!isInCart) {
			if(Main.LEFT) {
				setxVelocity(getxVelocity() - .075);
			}
			if(Main.RIGHT) {
				setxVelocity(getxVelocity() + .075);
			}
			
			if(Main.DOWN) {
				setyVelocity(getyVelocity() + .1);
			}
			/*if(!(Main.DOWN || Main.UP)) {
				yVelocity = yVelocity * .9;
				if(Math.abs(yVelocity) < .2) {
					yVelocity = 0;
				}
			}*/
			if(!(Main.LEFT || Main.RIGHT)) {
				setxVelocity(getxVelocity() * .9);
				if(Math.abs(getxVelocity()) < .2) {
					setxVelocity(0);
				}
			}
			if(getxVelocity() > 2) {
				setxVelocity(getxVelocity() - .1);
			}
			else if(getxVelocity() < -2) {
				setxVelocity(getxVelocity() + .1);
			}
			if(getyVelocity() > 5) {
				setyVelocity(getyVelocity() - .1);
			}
			else if(getyVelocity() < -5) {
				setyVelocity(getyVelocity() + .1);
			}
		}
			else if(Main.DOWN) {
				this.isInCart = false;
			}
			if(getTimeLastShieldUpdate() < System.currentTimeMillis() - 250) {
				isShielding = Main.SHIELD;
				
			}
			if(isShielding) {
				if(shield < 1) {
					isShielding = false;
				}
				else {
					shield--;
				}
				shieldRadius = shield / 8;
			}
			else {
				shieldRadius = 0;
				if(shield < shieldMax) {
					shield += 2;
				}
			}
			if(!powerUp.equals("NONE")) {
				if(powerUp.equals("POWERUP_RADIOACTIVE")) {
					if(random.nextInt(10) == 0) {
						double magnitude = 60 * ((System.currentTimeMillis() % 500) / 500);
						Main.gfx.add(GraphicEffect.EXPLOSION(xCenter, yCenter, magnitude));
					}
					/*
					for(int i = 0; i < Main.world1.size(); i++) {
						Block b = Main.world1.get(i);
						if(b.type.equals("TURRET") || b.type.equals("ROCKET") || b.type.contains("BOSS")) {
							if(Main.distance(getxCenter(), getyCenter(), b.xCenter, b.yCenter) < 10 * (getRadius() + b.width / 2)) {
								b.health -= 100 / Main.distance(getxCenter(), getyCenter(), b.xCenter, b.yCenter);
							}
						}
					}
					*/
					for(int i = 0; i < 13; i++) {
						int index = (int)(xCenter / 20) + (i - 6);
						if(index >= 0 && index < Main.columns.length) {
							ArrayList<Block> column = Main.columns[index];
							for(int ii = 0; ii < column.size(); ii++) {
								Block block = column.get(ii);
								if(block instanceof BlockEnemy) {
									((BlockEnemy)block).health -= 100 / Main.distance(getxCenter(), getyCenter(), block.xCenter, block.yCenter);
								}
							}
							Main.columns[index] = column;
						}
					}
				}
				else if(powerUp.equals("POWERUP_BAZOOKA")) {
					this.weaponName = "BAZOOKA";
					if(Main.MOUSE_CLICKED && this.timeLastBomb < System.currentTimeMillis() - 200) {
						double angle = Math.atan2(-1 * (getyPos() - Main.MOUSE_Y), (Main.MOUSE_X - (getxPos() + (Main.worldXOrigin - Main.worldX))));
						Main.bombs.add(new Bomb(xPos, yPos, 10 * Math.cos(angle), 10 * Math.sin(angle), 150, false));
						Main.gfx.add(GraphicEffect.FIRE(xCenter, yCenter, 100));
						this.timeLastBomb = System.currentTimeMillis();
					}
				}
				else if(powerUp.equals("POWERUP_FLAMETHROWER")) {
					this.weaponName = "FLAMETHROWER";
					if(Main.MOUSE_CLICKED && this.timeLastShot < System.currentTimeMillis() - 25) {
						double angle = Math.atan2(-1 * (getyPos() - Main.MOUSE_Y), (Main.MOUSE_X - (getxPos() + (Main.worldXOrigin - Main.worldX))));
						for(int i = 0; i < 1; i++) {
							Main.shots.add(new Shot(xCenter, yCenter, 10 * Math.cos(angle) + random.nextDouble() - .50, 10 * Math.sin(angle) + random.nextDouble() - .50, 5, true));
						}
						Main.gfx.add(GraphicEffect.FLAMETHROWER(xCenter, yCenter, 10 * Math.cos(angle), 10 * Math.sin(angle), 50));
						this.timeLastShot = System.currentTimeMillis();
					}
				}
				else if(powerUp.equals("POWERUP_HYPER-SHIELD")) {
					isShielding = true;
					this.shieldRadius = this.shieldMax / 4 + (int) (this.shieldMax * (Math.sin((double)System.currentTimeMillis() / 500) + 1) / 2);
				}
				
				if(timePowerUp < System.currentTimeMillis() - powerUpLength) {
					powerUp = "NONE";
					this.weaponName = "SHOT";
				}
			}
			if(getTimeLastWeaponSwitch() < System.currentTimeMillis() - 250) {
				if(Main.WEAPON_SWITCH) {
					
					if(getWeaponName() == "SHOT") {
						setWeaponName("BOMB");
					}
					else if(getWeaponName() == "BOMB") {
						setWeaponName("LASER");
					}
					else if(getWeaponName() == "LASER") {
						setWeaponName("MAGMA");
					}
					else if(getWeaponName() == "MAGMA") {
						setWeaponName("EMP");
					}
					else if(getWeaponName() == "EMP") {
						setWeaponName("SHOT");
					}
					//System.out.println(getWeaponName());
				}
				setTimeLastWeaponSwitch(System.currentTimeMillis());
			}
			
			if(Main.MOUSE_CLICKED) {
				//System.out.println("MOUSE");
				if(getTimeLastShot() < System.currentTimeMillis() - 200) {
					if(getWeaponName() == "SHOT") {
						fireShot(5, 10, 0, Shot.NONE);
						setTimeLastShot(System.currentTimeMillis());
					}
					else if(getWeaponName() == "LASER") {
						if(getAmmo() > 40) {
							fireShot(15, 5, 40, Shot.LASER);
						}
						setTimeLastShot(System.currentTimeMillis() - 150);
					}
					else if(getWeaponName() == "MAGMA") {
						if(getAmmo() > 400) {
							fireShot(20, 20, 400, Shot.FIRE_BALL);
						}
						setTimeLastShot(System.currentTimeMillis() + 300);
					}
				}
				if(getTimeLastBomb() < System.currentTimeMillis() - 500) {
					if(getWeaponName() == "BOMB") {
						if(getAmmo() > 400) {
							fireBomb();
						}
					}
					if(getWeaponName() == "EMP") {
						if(getAmmo() > 300) {
							fireEMP();
						}
					}
					setTimeLastBomb(System.currentTimeMillis());
				}
			}
			if(isUnderwater()) {
				if(getOxygen() > 0) {
					setOxygen(getOxygen() - 1);
				}
				else {
					attack(4, DROWNING);
				}
				if(Math.random() > .95) {
					Main.gfx.add(GraphicEffect.BUBBLE(10 * (Math.random() - .50) + xPos, 10 * (Math.random() - .50) + yPos, (int) (3 * Math.random())));
				}
			}
			else {
				setOxygen(500);
			}
			if(isRegenerate && Math.random() > .50) {
				if(ammo < ammoCap) {
					ammo++;
				}
			}
			
			if(yPos > 600) {
				attack(healthCap, VOID);
			}
			
			if (health < 1 && !isInvincible()) {
				dead = true;
			}
			
			setTimeLastUpdate(System.currentTimeMillis());
		}
		
	}
	public void fireShot(double velocity, double radius, int ammo, byte specialProperty) {
		setAmmo(getAmmo() - ammo);
		double angle = Math.atan2(-1 * (getyPos() - Main.MOUSE_Y), (Main.MOUSE_X - (getxPos() + (Main.worldXOrigin - Main.worldX))));
		Main.shots.add(new Shot(getxPos() + 10 * Math.cos(angle), getyPos() + 10 * Math.sin(angle), velocity * Math.cos(angle), velocity * Math.sin(angle), radius, true, specialProperty));
		Main.sounds.add(Main.gun);
	}
	public void fireBomb() {
		setAmmo(getAmmo() - 400);
		Main.bombs.add(new Bomb(getxPos(), getyPos(), 0, 0, 150, true));
	}
	public void fireEMP() {
		setAmmo(getAmmo() - 400);
		double angle = Math.atan2(-1 * (getyPos() - Main.MOUSE_Y), (Main.MOUSE_X - (getxPos() + (Main.worldXOrigin - Main.worldX))));
		Main.bombs.add(new EMP(getxPos() + 10 * Math.cos(angle), getyPos() + 10 * Math.sin(angle), 5 * Math.cos(angle), 5 * Math.sin(angle)));
	}
	double getxVelocity() {
		return xVelocity;
	}
	void setxVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}
	double getyVelocity() {
		return yVelocity;
	}
	void setyVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}
	double getxPos() {
		return xPos;
	}
	void setxPos(double xPos) {
		this.xPos = xPos;
	}
	double getyPos() {
		return yPos;
	}
	void setyPos(double yPos) {
		this.yPos = yPos;
	}
	double getxCenter() {
		return xCenter;
	}
	void setxCenter(double xCenter) {
		this.xCenter = xCenter;
	}
	double getyCenter() {
		return yCenter;
	}
	void setyCenter(double yCenter) {
		this.yCenter = yCenter;
	}
	int getHealth() {
		return health;
	}
	void setHealth(int health) {
		this.health = health;
		if(this.health > healthCap) {
			this.health = healthCap;
		}
	}
	int getAmmo() {
		return ammo;
	}
	void setAmmo(int ammo) {
		this.ammo = ammo;
		if(this.ammo > ammoCap) {
			this.ammo = ammoCap;
		}
	}
	int getOxygen() {
		return oxygen;
	}
	void setOxygen(int oxygen) {
		this.oxygen = oxygen;
		if(this.oxygen > oxygenCap) {
			this.oxygen = oxygenCap;
		}
	}
	boolean isUnderwater() {
		return isUnderwater;
	}
	void setUnderwater(boolean isUnderwater) {
		this.isUnderwater = isUnderwater;
	}
	String getWeaponName() {
		return weaponName;
	}
	void setWeaponName(String weaponName) {
		this.weaponName = weaponName;
	}
	String getStatus() {
		return status;
	}
	void setStatus(String status) {
		this.status = status;
	}
	int getWidth() {
		return width;
	}
	int getHeight() {
		return height;
	}
	double getRadius() {
		return radius;
	}
	private long getTimeLastUpdate() {
		return timeLastUpdate;
	}
	private void setTimeLastUpdate(long timeLastUpdate) {
		this.timeLastUpdate = timeLastUpdate;
	}
	private long getTimeLastCollision() {
		return timeLastCollision;
	}
	private void setTimeLastCollision(long timeLastCollision) {
		this.timeLastCollision = timeLastCollision;
	}
	private long getTimeLastWeaponSwitch() {
		return timeLastWeaponSwitch;
	}
	private void setTimeLastWeaponSwitch(long timeLastWeaponSwitch) {
		this.timeLastWeaponSwitch = timeLastWeaponSwitch;
	}
	private long getTimeLastShot() {
		return timeLastShot;
	}
	private void setTimeLastShot(long timeLastShot) {
		this.timeLastShot = timeLastShot;
	}
	private long getTimeLastBomb() {
		return timeLastBomb;
	}
	private void setTimeLastBomb(long timeLastBomb) {
		this.timeLastBomb = timeLastBomb;
	}
	public int getnAnimations() {
		return nAnimations;
	}
	public void setnAnimations(int nAnimations) {
		this.nAnimations = nAnimations;
	}
	public BufferedImage[] getAnimations() {
		return animations;
	}
	public void setAnimations(BufferedImage[] animations) {
		this.animations = animations;
	}
	public int getCurrentAnimation() {
		return currentAnimation;
	}
	public void setCurrentAnimation(int currentAnimation) {
		this.currentAnimation = currentAnimation;
	}
	public long getTimeLastAnimation() {
		return timeLastAnimation;
	}
	public void setTimeLastAnimation(long timeLastAnimation) {
		this.timeLastAnimation = timeLastAnimation;
	}
	public boolean isInCart() {
		return isInCart;
	}
	public void setInCart(boolean isInCart) {
		this.isInCart = isInCart;
	}
	public BufferedImage getCurrentImage() {
		return currentImage;
	}
	public void setCurrentImage(BufferedImage currentImage) {
		this.currentImage = currentImage;
	}
	public int getHealthCap() {
		return healthCap;
	}
	public void setHealthCap(int healthCap) {
		this.healthCap = healthCap;
	}
	public int getAmmoCap() {
		return ammoCap;
	}
	public void setAmmoCap(int ammoCap) {
		this.ammoCap = ammoCap;
	}
	public int getOxygenCap() {
		return oxygenCap;
	}
	public void setOxygenCap(int oxygenCap) {
		this.oxygenCap = oxygenCap;
	}
	public boolean isRegenerate() {
		return isRegenerate;
	}
	public void setRegenerate(boolean isRegenerate) {
		this.isRegenerate = isRegenerate;
	}
	public boolean isInvincible() {
		return isInvincible;
	}
	public void setInvincible(boolean isInvincible) {
		this.isInvincible = isInvincible;
	}
	public int getShield() {
		return shield;
	}
	public void setShield(int shield) {
		if(shield < 0) {
			this.shield = 0;
		}
		else {
			this.shield = shield;
		}
	}
	public boolean isShielding() {
		return isShielding;
	}
	public void setShielding(boolean isShielding) {
		this.isShielding = isShielding;
	}
	public long getTimeLastShieldUpdate() {
		return timeLastShieldUpdate;
	}
	public void setTimeLastShieldUpdate(long timeLastShieldUpdate) {
		this.timeLastShieldUpdate = timeLastShieldUpdate;
	}
	public int getShieldRadius() {
		return shieldRadius;
	}
	public void setShieldRadius(int shieldRadius) {
		this.shieldRadius = shieldRadius;
	}
	public int getShieldMax() {
		return shieldMax;
	}
	public void setShieldMax(int shieldMax) {
		this.shieldMax = shieldMax;
	}
	public String getPowerUp() {
		return powerUp;
	}
	public void setPowerUp(String powerUp) {
		this.powerUp = powerUp;
	}
	public int getPowerUpLength() {
		return powerUpLength;
	}
	public void setPowerUpLength(int powerUpLength) {
		this.powerUpLength = powerUpLength;
	}
	public long getTimePowerUp() {
		return timePowerUp;
	}
	public void setTimePowerUp(long timePowerUp) {
		this.timePowerUp = timePowerUp;
	}
	public int getUpdateDelay() {
		return updateDelay;
	}
	public void setUpdateDelay(int updateDelay) {
		this.updateDelay = updateDelay;
	}
	public byte getDamageSource() {
		return damageSource;
	}

	public void setDamageSource(byte damageSource) {
		this.damageSource = damageSource;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
	

	/*
	@Override
	public void run() {
		while(true) { 
			this.update();
		}
	}
	*/
	
}
