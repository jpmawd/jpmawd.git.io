package bounce;

import java.awt.Color;
import java.util.ArrayList;

public class GraphicEffectSnow extends GraphicEffect{

	public GraphicEffectSnow(double xPos, double yPos, Color color) {
		this.color = color;
		ArrayList<Force> forces = new ArrayList<Force>();
		forces.add(new Force(xPos, yPos + 40, .05));
		SnowPS p0 = new SnowPS(ParticleSystem.generateRandomParticles(xPos, yPos, 0, 0, 20, 3), forces);
		this.getPs().add(p0);
	}
	public GraphicEffectSnow(double xPos, double yPos) {
		this(xPos, yPos, Color.WHITE);
	}
}
