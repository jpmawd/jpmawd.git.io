package bounce;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class FirePS extends ParticleSystem {
	
	public static final int TYPE_0 = 0;
	public static final int TYPE_1 = 1;
	private final int type;

	public FirePS(ArrayList<Particle> particles, ArrayList<Force> forces, int type) {
		super(particles, forces);
		this.type = type;
	}
	
	public FirePS(ArrayList<Particle> particles, ArrayList<Force> forces) {
		this(particles, forces, 0);
	}

	public void drawParticle(Graphics g, int x, int y, Particle p) {
		int red = 200 + (int) (Math.random() * 50 * Math.sin(Math.toRadians((System.currentTimeMillis() - getTimeInstance()))));
		int green = 125 + (int) (Math.random() * (125 * Math.sin(Math.toRadians(4 * (System.currentTimeMillis() - getTimeInstance())))));
		g.setColor(new Color(red, green, 0));
		if(type == TYPE_0) {
			g.drawLine(x, y, x, y);
		}
		else if(type == TYPE_1) {
			g.fillOval(x, y, 6, 6);
		}
		else {
			
		}
	}
	
}
