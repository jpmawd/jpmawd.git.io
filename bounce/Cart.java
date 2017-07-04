package bounce;

import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;

public class Cart {

	private double x = 0;
	private String timeFunction, derivative;
	private double xPos, yPos, xVelocity, yVelocity, endX;
	private final boolean isRandom;
	private JEP j = new JEP();
	private DJep dj = new DJep();
	private boolean isTerminated;
	private long timeLastUpdate;
	
	public Cart(String timeFunction, double xPos, double endX, double startY, double xVelocity) {
		this.timeFunction = timeFunction;
		this.xPos = xPos;
		this.yPos = startY;
		this.xVelocity = xVelocity;
		this.endX = endX;
		j.addVariable("x", 0);
		j.addStandardConstants();
		j.addStandardFunctions();
		j.addComplex();
		j.setAllowUndeclared(true);
		j.setAllowAssignment(true);
		j.setImplicitMul(true);
		dj.addStandardConstants();
		dj.addStandardFunctions();
		dj.addComplex();
		dj.setAllowUndeclared(true);
		dj.setAllowAssignment(true);
		dj.setImplicitMul(true);
		dj.addStandardDiffRules();
		if(timeFunction.contains("random")) {
			isRandom = true;
			timeFunction = Main.functions[(int)(Math.random()*7.5)];
		}
		else {
			isRandom = false;
		}	
		try {
			derivative = dj.toString(dj.differentiate(dj.parse(timeFunction), "x"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public void update()  {
		if(timeLastUpdate < System.currentTimeMillis() - 10) {
			//System.out.println(timeFunction);
			x += .001;
			j.setVarValue("x", x);
			try {
				yVelocity = xVelocity * -Double.parseDouble(j.evaluate(j.parse(this.derivative)).toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			while(Math.abs(yVelocity) > 1000000000) {
				newFunction();
				try {
					yVelocity = xVelocity * -Double.parseDouble(j.evaluate(j.parse(this.derivative)).toString());
					//System.out.println(yVelocity);
					//System.out.println(timeFunction);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(isRandom && Math.random() == .01) {
				newFunction();
			}
			xPos += xVelocity;
			yPos += yVelocity;
			//System.out.println(xPos + " , " + yPos);
			
			if((!Main.player1.isInCart()) || xPos > endX) {
				isTerminated = true;
				Main.player1.setInCart(false);
			}
			else {
				Main.player1.setxPos(xPos);
				Main.player1.setyPos(yPos);
				Main.player1.setxVelocity(xVelocity);
				Main.player1.setyVelocity(yVelocity);
				if(Math.random() < .5) {
					//Main.shots.add(new Shot(xPos - 20, yPos + 20, -5, yVelocity, 10, true));
					if(Math.random() < .25) {
						Main.gfx.add(GraphicEffect.FLAMETHROWER(xPos, yPos, -xVelocity, -yVelocity, 300));
					}
				}
				//System.out.println(Main.player1.getxPos()  + " , " + Main.player1.getyPos());
			}
			timeLastUpdate = System.currentTimeMillis();
		}
	}
	public void newFunction()   {			
		timeFunction = Main.functions[(int)(Math.random()*7.5)];
		try {
			derivative = dj.toString(dj.differentiate(dj.parse(timeFunction), "x"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public String getTimeFunction() {
		return timeFunction;
	}
	public void setTimeFunction(String timeFunction) {
		this.timeFunction = timeFunction;
	}
	public String getDerivative() {
		return derivative;
	}
	public void setDerivative(String derivative) {
		this.derivative = derivative;
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
	public double getEndX() {
		return endX;
	}
	public void setEndX(double endX) {
		this.endX = endX;
	}
	public JEP getJ() {
		return j;
	}
	public void setJ(JEP j) {
		this.j = j;
	}
	public DJep getDj() {
		return dj;
	}
	public void setDj(DJep dj) {
		this.dj = dj;
	}
	public boolean isTerminated() {
		return isTerminated;
	}
	public void setTerminated(boolean isTerminated) {
		this.isTerminated = isTerminated;
	}
	public long getTimeLastUpdate() {
		return timeLastUpdate;
	}
	public void setTimeLastUpdate(long timeLastUpdate) {
		this.timeLastUpdate = timeLastUpdate;
	}
	public boolean isRandom() {
		return isRandom;
	}
}
