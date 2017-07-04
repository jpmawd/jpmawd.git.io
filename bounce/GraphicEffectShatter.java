package bounce;

import java.awt.Color;
import java.util.ArrayList;

public class GraphicEffectShatter extends GraphicEffect{

	public GraphicEffectShatter(double xPos, double yPos, Color color) {
		this.color = color;
		ArrayList<Particle> particles = new ArrayList<Particle>();
		ArrayList<Force> forces = new ArrayList<Force>();
		for(int i = 0; i < 25; i++) {
			particles.add(new Particle(xPos + (i % 5) * 5, yPos + i / 5 * 5, .04, Math.random() - .50, Math.random() - .50));
		}
		forces.add(new Force(xPos + 10, yPos - 40, 3));
		RectParticleSystem p0 = new RectParticleSystem(particles, forces, 2, color);
		getPs().add(p0);
	}
	public GraphicEffectShatter(double xPos, double yPos) {
		this(xPos, yPos, Color.BLACK);
	}
}
