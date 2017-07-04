package bounce;

public class VPortal {

	private int xPos, yPos, xCenter, yCenter;
	private int width = 20;
	private int level = 0;
	
	public VPortal(int xPos, int yPos, int level) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.xCenter = (int) (xPos + .50 * width);
		this.yCenter = (int) (yPos + .50 * width);
		this.level = level;
	}
	
	public void update() {
		if(Main.distance(xCenter, yCenter, Main.player1.getxCenter(), Main.player1.getyCenter()) < Main.player1.getRadius() + width / 2) {
			Main.levelNumber = level;
			Main.loadNextLevel = true;
		}
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public int getxCenter() {
		return xCenter;
	}

	public void setxCenter(int xCenter) {
		this.xCenter = xCenter;
	}

	public int getyCenter() {
		return yCenter;
	}

	public void setyCenter(int yCenter) {
		this.yCenter = yCenter;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
}
