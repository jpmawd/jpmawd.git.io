package bounce;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class RectParticleSystem extends ParticleSystem{

	int numberPoints;
	
	public RectParticleSystem(ArrayList<Particle> particles, ArrayList<Force> forces, int numberPoints, Color color) {
		super(particles, forces, color);
		this.numberPoints = numberPoints;
	}
	public RectParticleSystem(ArrayList<Particle> particles, ArrayList<Force> forces, int numberPoints) {
		this(particles, forces, numberPoints, Color.BLACK);
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
		double angle = Math.atan2(p.getyVelocity(), p.getxVelocity());
		angle += Math.PI / 4 + Math.hypot(p.getxVelocity(), p.getyVelocity()) * ((double)(System.currentTimeMillis() - this.getTimeInstance())) / (500);
		int[] pointXs = new int[numberPoints];
		int[] pointYs = new int[numberPoints];
		double a = 2 * Math.PI / numberPoints;
		for(int i = 0; i < numberPoints; i++) {
			pointXs[i] = (int) (x + 100 * p.getMASS() * Math.cos(angle + i * a));
			pointYs[i] = (int) (y + 100 * p.getMASS() * Math.sin(angle + i * a));
		}
		g.setColor(this.getColor());
		for(int i = 0; i < numberPoints - 1; i++) {
			g.drawLine(pointXs[i], pointYs[i], pointXs[i + 1], pointYs[i + 1]);
		}
		g.drawLine(pointXs[numberPoints - 1], pointYs[numberPoints - 1], pointXs[0], pointYs[0]);
	}
}
