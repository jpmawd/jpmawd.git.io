package bounce;

import java.util.ArrayList;

public class Lightning {

	private int startX, startY, endX, endY;
	private boolean isPermanent, isTerminated;
	
	
	public Lightning(int startX, int startY, int endX, int endY, boolean isPermanent) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.isPermanent = isPermanent;
	}
	public Lightning(int startX, int startY, int endX, int endY) {
		this(startX, startY, endX, endY, false);
	}
	public Lightning(int x) {
		this(x, 600, x, lightningRod(x).yPos);
	}
	public Lightning() {
		this((int) (Math.random() * Main.levelWidth));
	}
	
	public static Block lightningRod(int x) {
		
		ArrayList<Block> column = Main.columns[x / 20];
		Block block = column.get(0);
		for(int i = 0; i < column.size(); i++) {
			Block blockTest = column.get(i);
			if(blockTest.yPos < block.yPos) {
				block = blockTest;
			}
		}
		return block;
	}
	
	public void update() {
		
	}
}
