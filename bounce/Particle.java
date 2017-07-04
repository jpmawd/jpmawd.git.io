package bounce;

import java.util.ArrayList;

public class Particle {

	private double xPos, yPos;
	private double xVelocity = 0, yVelocity = 0;
	private final double MASS;
	private long timeInstance;
	private boolean isTerminated = false;
	
	public Particle(double xPos, double yPos, double MASS) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.MASS = MASS;
		timeInstance = System.currentTimeMillis();
	}
	public Particle(double xPos, double yPos, double MASS, double xVelocity, double yVelocity) {
		this(xPos, yPos, MASS);
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
	}
	
	public void update() {
		if(timeInstance < System.currentTimeMillis() - 2000) {
			isTerminated = true;
		}
		else {
			xPos -= xVelocity;
			yPos -= yVelocity;
			//System.out.println(xPos + " , " + yPos);
		}
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
	public double getxVelocity() {
		return xVelocity;
	}
	public void setxVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}
	public double getyVelocity() {
		return yVelocity;
	}
	public void setyVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}
	public long getTimeInstance() {
		return timeInstance;
	}
	public void setTimeInstance(long timeInstance) {
		this.timeInstance = timeInstance;
	}
	public boolean isTerminated() {
		return isTerminated;
	}
	public void setTerminated(boolean isTerminated) {
		this.isTerminated = isTerminated;
	}
	public double getMASS() {
		return MASS;
	}
	
}
