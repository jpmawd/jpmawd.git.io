package bounce;


import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphicEffect {

	Color color = Color.BLACK;
	
	private ArrayList<ParticleSystem> ps = new ArrayList<ParticleSystem>();
	private boolean isTerminated = false;
	private static Map<String, Class<?>> stringToClassMapping = new HashMap<String, Class<?>>();
	
	static{
		stringToClassMapping.put("CRUMBLE", GraphicEffectCrumble.class);
		stringToClassMapping.put("SHATTER", GraphicEffectShatter.class);
		stringToClassMapping.put("DUST", GraphicEffectDust.class);
		stringToClassMapping.put("SNOW", GraphicEffectSnow.class);
	}
	
	public static GraphicEffect createGraphicEffectByName(String name, double xPos, double yPos, Color color) {
		
		GraphicEffect effect = null;

        try
        {
            Class<?> var3 = (Class<?>)stringToClassMapping.get(name);

            if (var3 != null)
            {
                effect = (GraphicEffect)var3.getConstructor(new Class[]{double.class, double.class, Color.class}).newInstance(xPos, yPos, color);
            }
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
        }

        return effect;
        
			
	}
	
	public static GraphicEffect createGraphicEffectByName(String name, double xPos, double yPos) {
		return createGraphicEffectByName(name, xPos, yPos, Color.BLACK);
	}
	
	public void render(Graphics g, int relativeXPos, int relativeYPos) {
		for(int i = 0; i < ps.size(); i++) {
			ParticleSystem update = ps.get(i);
			update.render(g, relativeXPos, relativeYPos);
			if(update.isTerminated()) {
				ps.remove(i);
			}
			else {
				ps.set(i, update);
			}
			if(ps.size() == 0) {
				isTerminated = true;
			}
		}
	}
	
	public static GraphicEffect EXPLOSION(double xPos, double yPos, double magnitude) {
		GraphicEffect EXPLOSION = new GraphicEffect();
		ParticleSystem p0 = new ParticleSystem(ParticleSystem.generateRandomParticles(xPos, yPos, 20, 200), ParticleSystem.generateRandomForces(xPos, yPos, magnitude, 0, 1));
		ParticleSystem p1 = new ParticleSystem(ParticleSystem.generateRandomParticles(xPos + 10, yPos, 5, 100), ParticleSystem.generateRandomForces(xPos - 10, yPos, magnitude, 0, 1));
		ParticleSystem p2 = new ParticleSystem(ParticleSystem.generateRandomParticles(xPos - 10, yPos, 5, 100), ParticleSystem.generateRandomForces(xPos + 10, yPos, magnitude, 0, 1));
		EXPLOSION.getPs().add(p0);
		EXPLOSION.getPs().add(p1);
		EXPLOSION.getPs().add(p2);
		return EXPLOSION;
	}
	
	public static GraphicEffect FIRE_EXPLOSION(double xPos, double yPos, double magnitude) {
		GraphicEffect FIRE_EXPLOSION = new GraphicEffect();
		ParticleSystem p0 = new FirePS(ParticleSystem.generateRandomParticles(xPos, yPos, 20, 200), ParticleSystem.generateRandomForces(xPos, yPos, magnitude, 0, 1));
		ParticleSystem p1 = new FirePS(ParticleSystem.generateRandomParticles(xPos + 10, yPos, 5, 100), ParticleSystem.generateRandomForces(xPos - 10, yPos, magnitude, 0, 1));
		ParticleSystem p2 = new FirePS(ParticleSystem.generateRandomParticles(xPos - 10, yPos, 5, 100), ParticleSystem.generateRandomForces(xPos + 10, yPos, magnitude, 0, 1));
		FIRE_EXPLOSION.getPs().add(p0);
		FIRE_EXPLOSION.getPs().add(p1);
		FIRE_EXPLOSION.getPs().add(p2);
		return FIRE_EXPLOSION;
	}
	
	public static GraphicEffect FIRE_EXPLOSION(double xPos, double yPos, double magnitude, int type) {
		GraphicEffect FIRE_EXPLOSION = new GraphicEffect();
		ParticleSystem p0 = new FirePS(ParticleSystem.generateRandomParticles(xPos, yPos, 20, 200), ParticleSystem.generateRandomForces(xPos, yPos, magnitude, 0, 1), type);
		ParticleSystem p1 = new FirePS(ParticleSystem.generateRandomParticles(xPos + 10, yPos, 5, 100), ParticleSystem.generateRandomForces(xPos - 10, yPos, magnitude, 0, 1), type);
		ParticleSystem p2 = new FirePS(ParticleSystem.generateRandomParticles(xPos - 10, yPos, 5, 100), ParticleSystem.generateRandomForces(xPos + 10, yPos, magnitude, 0, 1), type);
		FIRE_EXPLOSION.getPs().add(p0);
		FIRE_EXPLOSION.getPs().add(p1);
		FIRE_EXPLOSION.getPs().add(p2);
		return FIRE_EXPLOSION;
	}
	
	public static GraphicEffect FIRE(double xPos, double yPos, int size) {
		GraphicEffect FIRE = new GraphicEffect();
		FirePS p0 = new FirePS(ParticleSystem.generateRandomParticles(xPos, yPos, 20, size), ParticleSystem.generateRandomForces(xPos, yPos + 40, .2, 10, 2));
		FirePS p1 = new FirePS(ParticleSystem.generateRandomParticles(xPos, yPos, 10, size / 2), ParticleSystem.generateRandomForces(xPos, yPos + 40, 5, .05, 2));
		FIRE.getPs().add(p0);
		FIRE.getPs().add(p1);
		return FIRE;
	}
	
	public static GraphicEffect FIRE(double xPos, double yPos, int size, int type) {
		GraphicEffect FIRE = new GraphicEffect();
		FirePS p0 = new FirePS(ParticleSystem.generateRandomParticles(xPos, yPos, 20, size), ParticleSystem.generateRandomForces(xPos, yPos + 40, .2, 10, 2), type);
		FirePS p1 = new FirePS(ParticleSystem.generateRandomParticles(xPos, yPos, 10, size / 2), ParticleSystem.generateRandomForces(xPos, yPos + 40, 5, .05, 2), type);
		FIRE.getPs().add(p0);
		FIRE.getPs().add(p1);
		return FIRE;
	}
	
	public static GraphicEffect FLAMETHROWER(double xPos, double yPos, double xVelocity, double yVelocity, int size) {
		GraphicEffect FLAMETHROWER = new GraphicEffect();
		FirePS p0 = new FirePS(ParticleSystem.generateRandomParticles(xPos, yPos, -xVelocity + Math.random() - .50, -yVelocity + Math.random() - .50, 10, size), ParticleSystem.generateRandomForces(xPos - 10 * xVelocity, yPos - 10 * yVelocity, .2, 5, 2));
		FLAMETHROWER.getPs().add(p0);
		return FLAMETHROWER;
	}
	
	public static GraphicEffect FLAMETHROWER(double xPos, double yPos, double xVelocity, double yVelocity, int size, int type) {
		GraphicEffect FLAMETHROWER = new GraphicEffect();
		FirePS p0 = new FirePS(ParticleSystem.generateRandomParticles(xPos, yPos, -xVelocity + Math.random() - .50, -yVelocity + Math.random() - .50, 10, size), ParticleSystem.generateRandomForces(xPos - 10 * xVelocity, yPos - 10 * yVelocity, .2, 5, 2), type);
		FLAMETHROWER.getPs().add(p0);
		return FLAMETHROWER;
	}
	
	public static GraphicEffect FRAGMENT(double xPos, double yPos, double xVelocity, double yVelocity, int numberParticles) {
		GraphicEffect FRAGMENT = new GraphicEffect();
		ParticleSystem p0 = new ParticleSystem(ParticleSystem.generateRandomParticles(xPos, yPos, -xVelocity, -yVelocity, 5, numberParticles), ParticleSystem.generateRandomForces(xPos - 10 * xVelocity, yPos - 10 * yVelocity, .1, 0, 1));
		FRAGMENT.getPs().add(p0);
		return FRAGMENT;
	}

	public static GraphicEffect CRUMBLE(double xPos, double yPos) {
		GraphicEffect CRUMBLE = new GraphicEffect();
		ArrayList<Particle> particles = new ArrayList<Particle>();
		ArrayList<Force> forces = new ArrayList<Force>();
		for(int i = 0; i < 25; i++) {
			particles.add(new Particle(xPos + (i % 5) * 5, yPos + i / 5 * 5, .04, Math.random() - .50, Math.random() - .50));
		}
		forces.add(new Force(xPos + 10, yPos - 40, 3));
		RectParticleSystem p0 = new RectParticleSystem(particles, forces, 4);
		CRUMBLE.getPs().add(p0);
		return CRUMBLE;
	}
	
	public static GraphicEffect SHATTER(double xPos, double yPos) {
		GraphicEffect SHATTER = new GraphicEffect();
		ArrayList<Particle> particles = new ArrayList<Particle>();
		ArrayList<Force> forces = new ArrayList<Force>();
		for(int i = 0; i < 25; i++) {
			particles.add(new Particle(xPos + (i % 5) * 5, yPos + i / 5 * 5, .04, Math.random() - .50, Math.random() - .50));
		}
		forces.add(new Force(xPos + 10, yPos - 40, 3));
		RectParticleSystem p0 = new RectParticleSystem(particles, forces, 2);
		SHATTER.getPs().add(p0);
		return SHATTER;
	}
	
	public static GraphicEffect BUBBLE(double xPos, double yPos, int size) {
		GraphicEffect BUBBLE = new GraphicEffect();
		ArrayList<Force> forces = new ArrayList<Force>();
		forces.add(new Force(xPos, yPos + 40, .05));
		BubblePS p0 = new BubblePS(ParticleSystem.generateRandomParticles(xPos, yPos, 0, 0, 20, size), forces);
		BUBBLE.getPs().add(p0);
		return BUBBLE;
	}
	
	public static GraphicEffect DUST(double xPos, double yPos) {
		GraphicEffect DUST = new GraphicEffect();
		ArrayList<Particle> particles = new ArrayList<Particle>();
		ArrayList<Force> forces = new ArrayList<Force>();
		for(int i = 0; i < 400; i++) {
			
				particles.add(new Particle(xPos + (i % 20), yPos + i / 20, .05, Math.random() - .50, -Math.random()));
			
		}
		ParticleSystem p0 = new ParticleSystem(particles, forces);
		DUST.getPs().add(p0);
		return DUST;
	}
	
	public ArrayList<ParticleSystem> getPs() {
		return ps;
	}

	public void setPs(ArrayList<ParticleSystem> ps) {
		this.ps = ps;
	}

	public boolean isTerminated() {
		return isTerminated;
	}

	public void setTerminated(boolean isTerminated) {
		this.isTerminated = isTerminated;
	}



	
}
