package bounce;

import java.awt.Color;
import java.awt.Graphics;

public class BlockPow extends Block{

	private boolean isPressed = false;
	private static long timeLastPow;
	public static int[] xCoords, yCoords;
	
	public BlockPow(int xPos, int yPos) {
		super(xPos, yPos);
	}

	@Override
	public void procedure() {
		if(isPressed) {
			onPow();
			this.height = 10;
			isPressed = false;
		}
		if(System.currentTimeMillis() - timeLastPow > 500) {
			this.height = 20;
		}
	}
	
	public void onPow() {
		ConwayCell.setPaused(!ConwayCell.isPaused());
		if(ConwayCell.isPaused()) {
			boolean[][] cells = ConwayCell.getCells();
			int nCoords = 0;
			for(int x = 0; x < ConwayCell.getNumColumns(); x++) {
				for(int y = 0; y < ConwayCell.getNumRows(); y++) {
					if(cells[x][y]) {
						Main.placeThisBlock(new BlockConwayCell(x * 20, y * 20));
						nCoords++;
					}
				}
			}
			xCoords = new int[nCoords];
			yCoords = new int[nCoords];
			int i = 0;
			for(int x = 0; x < ConwayCell.getNumColumns(); x++) {
				for(int y = 0; y < ConwayCell.getNumRows(); y++) {
					if(cells[x][y]) {
						xCoords[i] = x * 20;
						yCoords[i] = y * 20;
						i++;
					}
					cells[x][y] = false;
				}
			}
			ConwayCell.setCells(cells);
		}
		else {
			boolean[][] cells = ConwayCell.getCells();
			for(int i = 0; i < xCoords.length; i++) {
				int x = xCoords[i];
				int y = yCoords[i];
				boolean live = Main.blockAt(x, y) instanceof BlockConwayCell;
				cells[x / 20][y / 20] = live;
				if(live) {
					Main.removeBlock(x, y);
				}
			}
			ConwayCell.setCells(cells);
		}
		timeLastPow = System.currentTimeMillis();
	}

	@Override
	public void render(Graphics g, int screenXPos, int screenYPos) {
		g.setColor(Main.purple);
		g.fillRect(screenXPos, screenYPos + 20 - this.height, this.width, this.height);
		g.setColor(Main.woodColor);
		g.fillRect(screenXPos, screenYPos + 16, this.width, 4);
		g.setColor(Color.BLACK);
		g.drawRect(screenXPos, screenYPos + 20 - this.height, this.width, this.height);
		g.drawRect(screenXPos, screenYPos + 16, this.width, 4);
	}

	public boolean isPressed() {
		return isPressed;
	}

	public void setPressed(boolean isPressed) {
		this.isPressed = isPressed;
	}

	public static long getTimeLastPow() {
		return timeLastPow;
	}

	public static void setTimeLastPow(long timeLastPow) {
		BlockPow.timeLastPow = timeLastPow;
	}
	
}
