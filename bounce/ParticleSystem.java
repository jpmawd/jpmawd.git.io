package bounce;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class ParticleSystem {

	private ArrayList<Particle> particles;
	private ArrayList<Force> forces;
	private Color color = Color.BLACK;
	private long timeInstance;
	private boolean isTerminated = false;
	
	public ParticleSystem(ArrayList<Particle> particles, ArrayList<Force> forces, Color color) {
		this.particles = particles;
		this.forces = forces;
		this.setColor(color);
		timeInstance = System.currentTimeMillis();
	}
	public ParticleSystem(ArrayList<Particle> particles, ArrayList<Force> forces) {
		this(particles, forces, Color.BLACK);
	}

	public static ArrayList<Particle> generateRandomParticles(double xPos, double yPos, double stratification, int numberParticles) {
		ArrayList<Particle> particles = new ArrayList<Particle>();
		for(int i = numberParticles; i > 0; i--) {
			particles.add(new Particle(xPos + (Math.random() - .50) * stratification, yPos + (Math.random() - .50) * stratification, .01, (Math.random() - .50), (Math.random() - .50)));
		}
		
		return particles;
	}
	
	public static ArrayList<Particle> generateRandomParticles(double xPos, double yPos, double xVelocity, double yVelocity, double stratification, int numberParticles) {
		ArrayList<Particle> particles = new ArrayList<Particle>();
		for(int i = numberParticles; i > 0; i--) {
			particles.add(new Particle(xPos + (Math.random() - .50) * stratification, yPos + (Math.random() - .50) * stratification, .01, xVelocity + xVelocity * (Math.random() - .50), yVelocity + yVelocity * (Math.random() - .50)));
		}
		
		return particles;
	}
	
	public static ArrayList<Force> generateRandomForces(double xPos, double yPos, double magnitude, double stratification, int numberForces) {
		ArrayList<Force> forces = new ArrayList<Force>();
		for(int i = numberForces; i > 0; i--) {
			forces.add(new Force(xPos + (Math.random() - .50) * stratification, yPos + (Math.random() - .50) * stratification, magnitude * (Math.random())));
		}
		
		return forces;
	}
	
	public static ArrayList<Force> addGravity(ArrayList<Force> forces) {
		forces.add(new Force(0, -100000, 1000));
		return forces;
	}
	
	public void render(Graphics g, double relativeXPos, double relativeYPos) {
		for(int i = 0; i < particles.size(); i++) {
			Particle particle = particles.get(i);
			for(int ii = 0; ii < forces.size(); ii++) {
				Force force = forces.get(ii);
				particle = force.applyToParticle(particle);
			}
			particle.update();
			int x = (int)(particle.getxPos() + relativeXPos);
			int y = (int)(particle.getyPos() + relativeYPos);
			g.setColor(getColor());
			drawParticle(g, x, y, particle);
			g.setColor(Color.BLACK);
			if(particle.isTerminated()) {
				particles.remove(i);
			}
			else {
				particles.set(i, particle);
			}
		}
		if(particles.size() == 0) {
			isTerminated = true;
		}
	}
	
	public void drawParticle(Graphics g, int x, int y, Particle p) {
		g.drawLine(x, y, x, y);
	}
	@SuppressWarnings("unchecked")
	public <T> void setParticles(ArrayList<T> particles) {
		this.particles = (ArrayList<Particle>) particles;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Particle> ArrayList<T> getParticles(Class<T> particleType){
		return (ArrayList<T>) particles;
	}

	public ArrayList<Force> getForces() {
		return forces;
	}

	public void setForces(ArrayList<Force> forces) {
		this.forces = forces;
	}

	public boolean isTerminated() {
		return isTerminated;
	}

	public void setTerminated(boolean isTerminated) {
		this.isTerminated = isTerminated;
	}

	public long getTimeInstance() {
		return timeInstance;
	}

	public void setTimeInstance(long timeInstance) {
		this.timeInstance = timeInstance;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	} 

	
}
