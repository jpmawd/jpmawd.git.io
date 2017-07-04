package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockPowerup extends Block{

	String powerup;
	
	public BlockPowerup(int xPos, int yPos) {
		super(xPos, yPos);
		this.powerup = randomPowerup();
	}
	
	public static String randomPowerup() {
		int randNum = random.nextInt(5);
		return (randNum == 0 || randNum == 1) ? "POWERUP_RADIOACTIVE" : ((randNum == 2) ? "POWERUP_FLAMETHROWER" : ((randNum == 3) ? "POWERUP_BAZOOKA" : "POWERUP_HYPER-SHIELD"));
	}

	@Override
	public void procedure() {
		
	}

	@Override
	public void render(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int) (Math.random() * 255)));
		g.fillRect(screenXPos, screenYPos, 20, 20);
		g.setColor(Color.BLACK);
	}
}
