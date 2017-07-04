package bounce;

import java.awt.Color;

public class BlockRocket extends BlockEnemyMachine{

	public BlockRocket(int xPos, int yPos) {
		super(xPos, yPos, Color.RED);
	}

	@Override
	public void attack(double angle) {
		Main.bombs.add(new Bomb(xCenter, yPos, 10 * Math.cos(angle), 10 * Math.sin(angle), 150, false));
	}

}
