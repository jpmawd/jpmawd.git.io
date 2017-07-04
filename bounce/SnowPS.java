package bounce;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class SnowPS extends ParticleSystem{

	
	
	public SnowPS(ArrayList<Particle> particles, ArrayList<Force> forces, Color color) {
		super(particles, forces, color);
	}
	public SnowPS(ArrayList<Particle> particles, ArrayList<Force> forces) {
		super(particles, forces, Color.WHITE);
	}
	
	public void drawParticle(Graphics g, int x, int y, Particle p) {
		g.setColor(getColor());
		int bubbleRadius = (int) (500 * p.getMASS());
		g.drawLine(x + bubbleRadius / 2, y + bubbleRadius / 2, x - bubbleRadius / 2, y - bubbleRadius / 2);
		g.drawLine(x - bubbleRadius / 2, y - bubbleRadius / 2, x + bubbleRadius / 2, y + bubbleRadius / 2);
		g.drawLine(x, y + bubbleRadius / 2, x, y - bubbleRadius / 2);
		g.drawLine(x + bubbleRadius / 2, y, x - bubbleRadius / 2, y);
		g.drawOval(x + bubbleRadius / 4, y + bubbleRadius / 4, (int)(bubbleRadius / 4), (int) (bubbleRadius / 4));
	
	}
}
