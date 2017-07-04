package bounce;

public class BossBlock extends BlockEnemy{

	String signature;
	
	public BossBlock(int xPos, int yPos, String signature) {
		super(xPos, yPos);
		this.signature = signature;
		this.health = 500;
	}

}
