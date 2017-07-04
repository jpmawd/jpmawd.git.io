package bounce;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class EMP extends Bomb{

	public EMP(double xPos, double yPos, double xVelocity, double yVelocity) {
		super(xPos, yPos, xVelocity, yVelocity, 500, true);
		baseDamage = 10;
	}
	
	public void procedure() {
		super.procedure();
		float radEMP = 50;
		float magEMP = 100F;
		float timeStep = .001F;
		for(int i = 0; i < Main.shots.size(); i++) {
			Shot testShot = Main.shots.get(i);
			if(!testShot.friendly && Main.distance(xCenter, yCenter, testShot.getxCenter(), testShot.getyCenter()) < radEMP) {
				testShot.setxVelocity(testShot.getxVelocity()+timeStep*testShot.getyVelocity()*magEMP);
				testShot.setyVelocity(testShot.getyVelocity()-timeStep*testShot.getxVelocity()*magEMP);
				Main.shots.set(i, testShot);
			}
		}
	}
	
	public void attackBlock(Block block) {
		super.attackBlock(block);
		if(block instanceof BlockEnemy) {
			((BlockEnemy) block).stun(40);
		}
	}
	
	public void renderEntity(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(normalColor);
		g.fillRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.BLACK);
		g.drawRect(screenXPos, screenYPos, 20, 20);
		g.drawRect(screenXPos-3, screenYPos+8, 3, 4);
		g.drawRect(screenXPos+20, screenYPos+8, 3, 4);
		g.setColor((fuseDelay > 0 && 100%fuseDelay != 0) ? Color.WHITE : Main.bluePlasmaColor);
		g.fillArc(screenXPos, screenYPos, 20, 20, 90, 360 - this.fuseDelay/3);
		g.setColor(new Color(255,255,255,100));
		if(this.fuseDelay <= 0) {
			int pulseRadius = (int)(10*Math.pow(2,-fuseDelay/10));
			g.drawOval(screenXPos-pulseRadius/2, screenYPos-pulseRadius/2, 20+pulseRadius, 20+pulseRadius);
			g.fillOval(screenXPos, screenYPos, 20, 20);
			if(this.fuseDelay < -10) {
				g.fillOval(screenXPos - 5, screenYPos - 5, 30, 30);
				if(this.fuseDelay < -20) {
					g.fillOval(screenXPos - 20, screenYPos - 20, 60, 60);
					if(this.fuseDelay < -30) {
						g.fillOval(screenXPos - 40, screenYPos - 40, 100, 100);
					}
				}
			}
		}
		else {
			g.fillOval(screenXPos - 50, screenYPos - 50, 120, 120);
			int pulseRadius = 10*(fuseDelay%10);
			g.setColor(Color.WHITE);
			g.drawOval(screenXPos-pulseRadius/2, screenYPos-pulseRadius/2, 20+pulseRadius, 20+pulseRadius);
		}
		g.setColor(Color.BLACK);
		g.drawOval(screenXPos, screenYPos, 20, 20);
	}

}
