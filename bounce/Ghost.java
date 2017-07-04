package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class Ghost extends Entity{

	Color color = Color.WHITE;
	int health;
	int speed = 1;
	double xAcceleration = 0, yAcceleration = 0;
	
	public Ghost(double xPos, double yPos, double radius) {
		super(xPos, yPos, 0, 0, radius, false);
		this.health = 600;
		canFallOut = false;
	}

	public void procedure() {
		if(this.health < 1) {
			this.isTerminated = true;
		}
		if(Main.distance(this.xCenter, this.yCenter, Main.player1.getxCenter(), Main.player1.getyCenter()) < 900) {
			if(this.xCenter < Main.player1.getxCenter()) {
				this.xAcceleration = .01;
			}
			else {
				this.xAcceleration = -.01;
			}
			if(this.yCenter < Main.player1.getyCenter()) {
				this.yAcceleration = .01;
			}
			else {
				this.yAcceleration = -.01;
			}
			this.xVelocity += this.xAcceleration * speed;
			this.yVelocity += this.yAcceleration * speed;
			if(this.isTouchingPlayer()) {
				this.attack();
			}
		}
		else {
			this.xAcceleration = 0;
			this.yAcceleration = 0;
			this.xVelocity /= 2;
			this.yVelocity /= 2;
		}
	}
	public void attack() {
		//Main.player1.setHealth(Main.player1.getHealth() - 50);
		Main.player1.setxVelocity(Main.player1.getxVelocity() + .5 * this.xVelocity);
		Main.player1.setyVelocity(Main.player1.getyVelocity() + .5 * this.yVelocity);
	}

	public void renderEntity(Graphics g, int screenXPos, int screenYPos) {
		
		g.setColor(color);
		g.drawRoundRect(screenXPos, screenYPos, (int)this.radius, (int)this.radius, 4, 4);
		g.setColor(Color.BLACK);
	}
}
