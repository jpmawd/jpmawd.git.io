package bounce;

public class BlockTurret extends BlockEnemyMachine{

	public BlockTurret(int xPos, int yPos) {
		super(xPos, yPos, Main.bluePlasmaColor);
	}

	@Override
	public void attack(double angle) {
		Main.shots.add(new Shot(xCenter, yPos, 5 * Math.cos(angle), 5 * Math.sin(angle), 10, false));
	}

	
}
