package bounce;

import java.util.ArrayList;

import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;

public class RollerCoaster {

	private int startX, startY, endX;
	private String timeFunction;
	private String derivative;
	private JEP j = new JEP();
	private DJep dj = new DJep();
	private long timeLastCart;
	
	public RollerCoaster(int startX, int endX, String timeFunction) throws ParseException {
		this.startX = startX;
		this.endX = endX;
		this.timeFunction = timeFunction;
		j.addVariable("x", 0);
		j.addStandardConstants();
		j.addStandardFunctions();
		j.addComplex();
		j.setAllowUndeclared(true);
		j.setAllowAssignment(true);
		j.setImplicitMul(true);
		j.setVarValue("x", 0);
		//System.out.println(timeFunction);
		//System.out.println(j.evaluate(j.parse(timeFunction)));
		//System.out.println((int)(double)j.getValue());
		this.startY =  600 - (int)Double.parseDouble(j.evaluate(j.parse(this.timeFunction)).toString());
		
	}
	
	public void update()  {
		//System.out.println(timeFunction);
		//System.out.println(startX + " , " + startY);
		if(Main.distance(startX, startY, Main.player1.getxCenter(), Main.player1.getyCenter()) < 40 && !Main.player1.isInCart() && timeLastCart < System.currentTimeMillis() - 1000) {
			Main.player1.setInCart(true);
			if(timeFunction.contains("random")) {
				Main.carts.add(new Cart(timeFunction, startX, endX, Integer.parseInt(timeFunction.substring(6)), 5));
			}
			else {
				Main.carts.add(new Cart(timeFunction, startX, endX, startY, 5));
			}
			timeLastCart = System.currentTimeMillis();
		}
		else if(!Main.player1.isInCart()) {
			if(Math.random() > .95) {
				Main.gfx.add(GraphicEffect.FIRE_EXPLOSION(startX + 10 * (Math.random() - .50), startY - 80 * Math.random(), .1));
			}
		}
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getEndX() {
		return endX;
	}

	public void setEndX(int endX) {
		this.endX = endX;
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
	
}
