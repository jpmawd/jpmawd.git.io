package bounce;

import java.util.ArrayList;
import java.util.Random;

public class Fire {

	private double x, y, xVelocity, yVelocity;
	private long timeInstance;
	private static final long timeTermination = 2000;
	private boolean isTerminated = false;
	private final int TYPE;
	public static final int TYPE_0 = 0;
	public static final int TYPE_1 = 1;
	private static Random random = new Random();
	
	public Fire(double x, double y, double xVelocity, double yVelocity, int type) {
		this.x = x;
		this.y = y;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		this.timeInstance = System.currentTimeMillis();
		this.TYPE = type;
	}
	
	public Fire(double x, double y, double xVelocity, double yVelocity) {
		this(x, y, xVelocity, yVelocity, 0);
	}
	
	public void update() {
		if(System.currentTimeMillis() - timeInstance > timeTermination) {
			isTerminated = true;
		}
		else {
			x += xVelocity;
			y += yVelocity;
			if(random.nextInt(10) == 0) {
				Main.gfx.add(GraphicEffect.FIRE(x, y, 1, TYPE));
			}
			boolean isCollision = false;
			for(int i = 0; i < 5; i++) {
				int index = (int)(x / 20) + (i - 2);
				if(index >= 0 && index < Main.columns.length) {
					ArrayList<Block> column = Main.columns[index];
					for(int ii = 0; ii < column.size(); ii++) {
						Block block = column.get(ii);
						if(Main.distance(x, y, block.xCenter, block.yCenter) < block.width / 2) {
							isCollision = true;
							if(block.isFlammable) {
								if(block instanceof BlockLiving) {
									((BlockLiving)block).health -= 2;
								}
								block.isOnFire = true;
								column.set(ii, block);
							}
							else if(block instanceof BlockWater) {
								isTerminated = true;
							}
							break;
						}
					}
					
					if(isCollision) {
						Main.columns[index] = column;
						if(System.currentTimeMillis() - timeInstance > 500) {
							xVelocity = 0;
							yVelocity = 0;
						}
						break;
					}
				}
			}
			if(Main.distance(x, y, Main.player1.getxCenter() + 10, Main.player1.getyCenter()) < Main.player1.getRadius()) {
				Main.player1
						.attack(4, Player.FIRE);
			}
		}
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
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

	public static long getTimetermination() {
		return timeTermination;
	}
	
}
