package bounce;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public abstract class Entity {

	Random random;
	Color normalColor = new Color(238, 238, 238);
	double xPos;
	double yPos;
	double xCenter;
	double yCenter;
	double xVelocity;
	double yVelocity;
	double radius;
	boolean gravity;
	boolean touchingPlayer = false;
	boolean grounded;
	boolean isImpervious = true;
	boolean isTerminated = false;
	boolean canFallOut = true;
	long timeInstance;
	long timeLastUpdate;
	
	public Entity(double xPos, double yPos, double xVelocity, double yVelocity, double radius, boolean gravity) {
		random = new Random();
		this.gravity = gravity;
		this.xPos = xPos;
		this.yPos = yPos;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		this.radius = radius;
		xCenter = this.xPos + .5 * radius;
		yCenter = this.yPos + .5 * radius;
		timeLastUpdate = System.currentTimeMillis();
		timeInstance = System.currentTimeMillis();
	}
	
	public abstract void renderEntity(Graphics g, int screenXPos, int screenYPos);
	
	public void update() {
		if(timeLastUpdate < System.currentTimeMillis() - 10) {
			
				xPos += xVelocity;
				yPos += yVelocity;
				xCenter = this.xPos + radius * .5;
				yCenter = this.yPos + radius * .5;
				if(gravity) {
					yVelocity += .1;
				}
				
				
				procedure();
				
			timeLastUpdate = System.currentTimeMillis();
		}
		else if(yPos > 600 && canFallOut) {
			isTerminated = true;
		}
	}
	
	public abstract void procedure();
	
	public boolean isTouchingPlayer() {
		this.touchingPlayer = (Main.distance(xCenter, yCenter, Main.player1.getxCenter(), Main.player1.getyCenter()) < radius);
		return touchingPlayer;
	}
	
	public boolean grounded() {
		if(isImpervious) {
			isImpervious = (timeInstance > System.currentTimeMillis() - 40);
		}
		else {
			grounded = false;
			for(int i = 0; i < 5; i++) {
				int index = (int)(xCenter / 20) + (i - 2);
				if(index >= 0 && index < Main.columns.length) {
					ArrayList<Block> column = Main.columns[index];
					for(int ii = 0; ii < column.size(); ii++) {
						Block block = column.get(ii);
						if(!(block instanceof BlockEnemyMachine) && (Main.distance(xCenter, yCenter, block.xCenter, block.yCenter) < 10 + block.width / 2)) {
							if(block instanceof BlockLiquid) {
								xVelocity = xVelocity * .90;
								yVelocity = yVelocity * .90;
								return false;
							}
							
							grounded = true;
							return true;
						}
					}
				}
			}
		}
		grounded = false;
		return false;
	}

	public double getxPos() {
		return xPos;
	}

	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	public double getyPos() {
		return yPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

	public double getxCenter() {
		return xCenter;
	}

	public void setxCenter(double xCenter) {
		this.xCenter = xCenter;
	}

	public double getyCenter() {
		return yCenter;
	}

	public void setyCenter(double yCenter) {
		this.yCenter = yCenter;
	}

	public double getxVelocity() {
		return xVelocity;
	}

	public void setxVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}

	public double getyVelocity() {
		return yVelocity;
	}

	public void setyVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public boolean isGravity() {
		return gravity;
	}

	public void setGravity(boolean gravity) {
		this.gravity = gravity;
	}

	public boolean isGrounded() {
		return grounded;
	}

	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}

	public boolean isImpervious() {
		return isImpervious;
	}

	public void setImpervious(boolean isImpervious) {
		this.isImpervious = isImpervious;
	}

	public boolean isTerminated() {
		return isTerminated;
	}

	public void setTerminated(boolean isTerminated) {
		this.isTerminated = isTerminated;
	}

	public long getTimeInstance() {
		return timeInstance;
	}

	public void setTimeInstance(long timeInstance) {
		this.timeInstance = timeInstance;
	}

	public long getTimeLastUpdate() {
		return timeLastUpdate;
	}

	public void setTimeLastUpdate(long timeLastUpdate) {
		this.timeLastUpdate = timeLastUpdate;
	}

	public void setTouchingPlayer(boolean touchingPlayer) {
		this.touchingPlayer = touchingPlayer;
	}

	public boolean isCanFallOut() {
		return canFallOut;
	}

	public void setCanFallOut(boolean canFallOut) {
		this.canFallOut = canFallOut;
	}
	
	
}
