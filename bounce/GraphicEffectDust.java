package bounce;

import java.awt.Color;
import java.util.ArrayList;

public class GraphicEffectDust extends GraphicEffect{

	public GraphicEffectDust(double xPos, double yPos, Color color) {
		this.color = color;
		ArrayList<Particle> particles = new ArrayList<Particle>();
		ArrayList<Force> forces = new ArrayList<Force>();
		for(int i = 0; i < 400; i++) {
			
				particles.add(new Particle(xPos + (i % 20), yPos + i / 20, .05, Math.random() - .50, -Math.random()));
			
		}
		ParticleSystem p0 = new ParticleSystem(particles, forces, color);
		getPs().add(p0);
	}
	public GraphicEffectDust(double xPos, double yPos) {
		this(xPos, yPos, Color.BLACK);
	}
}
