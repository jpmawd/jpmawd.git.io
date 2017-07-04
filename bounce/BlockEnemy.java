package bounce;

import java.awt.Graphics;

public abstract class BlockEnemy extends BlockLiving{
	
	private int stunTime = 0;

	public BlockEnemy(int xPos, int yPos) {
		super(xPos, yPos, 300, "DUST");
	}
	
	public void procedure() {
		super.procedure();
		if(stunTime>0) {
			stunTime--;
		}
	}
	
	public void render(Graphics g, int screenXPos, int screenYPos) {
		g.fillRect(screenXPos, screenYPos, 20, 20);
	}
	
	public int getStunTime() {
		return stunTime;
	}
	
	public void setStunTime(int stunTime) {
		this.stunTime = stunTime;
	}
	
	public void stun(int stunTime) {
		this.stunTime += stunTime;
	}
}
