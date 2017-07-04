package bounce;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class BubblePS extends ParticleSystem{

	
	
	public BubblePS(ArrayList<Particle> particles, ArrayList<Force> forces) {
		super(particles, forces);
	}

	/*
	public void drawParticle(Graphics g, int x, int y, Particle p) {
		double angle = Math.atan2(p.getyVelocity(), p.getxVelocity());
		angle += Math.PI / 4 + Math.hypot(p.getxVelocity(), p.getyVelocity()) * ((double)(System.currentTimeMillis() - this.getTimeInstance())) / (500);
		int x1 = (int) (x + 100 * p.getMASS() * Math.cos(angle));
		int y1 = (int) (y + 100 * p.getMASS() * Math.sin(angle));
		int x2 = (int) (x + 100 * p.getMASS() * Math.cos(angle + Math.PI / 2));
		int y2 = (int) (y + 100 * p.getMASS() * Math.sin(angle + Math.PI / 2));
		int x3 = (int) (x + 100 * p.getMASS() * Math.cos(angle + Math.PI));
		int y3 = (int) (y + 100 * p.getMASS() * Math.sin(angle + Math.PI));
		int x4 = (int) (x + 100 * p.getMASS() * Math.cos(angle - Math.PI / 2));
		int y4 = (int) (y + 100 * p.getMASS() * Math.sin(angle - Math.PI / 2));
		g.setColor(Color.BLACK);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x2, y2, x3, y3);
		g.drawLine(x3, y3, x4, y4);
		g.drawLine(x4, y4, x1, y1);
	}
	*/
	
	public void drawParticle(Graphics g, int x, int y, Particle p) {
		g.setColor(Color.BLUE);
		int bubbleRadius = (int) (500 * p.getMASS());
		g.drawOval(x, y, (int)(bubbleRadius), (int) (bubbleRadius));
		g.drawOval(x + bubbleRadius / 4, y + bubbleRadius / 4, (int)(bubbleRadius / 4), (int) (bubbleRadius / 4));
	
	}
}
