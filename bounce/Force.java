package bounce;

import java.util.ArrayList;

public class Force {

	private double xPos, yPos;
	private double magnitude;
	
	public Force(double xPos, double yPos, double magnitude) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.magnitude = magnitude;
	}
	
	public Particle applyToParticle(Particle particle) {
		double acceleration =  magnitude / (particle.getMASS() * (Math.pow((this.xPos - particle.getxPos()), 2) + Math.pow((this.yPos - particle.getyPos()), 2)));
		double angle = Math.atan2((this.yPos - particle.getyPos()), this.xPos - particle.getxPos());
		particle.setxVelocity(particle.getxVelocity() + acceleration * Math.cos(angle));
		particle.setyVelocity(particle.getyVelocity() + acceleration * Math.sin(angle));
		
		return particle;
	}

	public double getxPos() {
		return xPos;
	}

	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	public double getyPos() {
		return yPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

	public double getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	} 
	
	

}
