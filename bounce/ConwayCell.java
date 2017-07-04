package bounce;

public class ConwayCell {

	private static boolean[][] cells, updatedCells;
	private static int[][] neighborCounter;
	private static int numColumns, numRows;
	private static int tick = 200;
	private static long timeLastTick;
	private static boolean isPaused = false;
	private static int numLiveCells;
	
	public static void updateCells() {
		
		if(!isPaused && System.currentTimeMillis() - tick > timeLastTick) {
			for(int x = 0; x < numColumns; x++) {
				for(int y = 0; y < numRows; y++) {
					neighborCounter[x][y] = getNumberOfNeighbors(x, y);
				}
			}
			updatedCells = cells;
			int numLiveCells = 0;
			for(int x = 0; x < numColumns; x++) {
				for(int y = 0; y < numRows; y++) {
					Block block = Main.blockAt(x * 20, y * 20);
					if(block == null || block instanceof BlockNormal || block instanceof BlockBomb || block instanceof BlockSnow) {
						if(updateRules(x, y)) {
							if(block instanceof BlockLiving) { 
								((BlockLiving)block).health = -1;
								Main.placeThisBlock(block);
							}
							numLiveCells++;
						}
					}
					else {
						updatedCells[x][y] = false;
					}
				}
			}
			cells = updatedCells;
			ConwayCell.numLiveCells = numLiveCells;
			timeLastTick = System.currentTimeMillis();
			
			for(int x = 0; x < numColumns; x++) {
				for(int y = 0; y < numRows; y++) {
					neighborCounter[x][y] = getNumberOfNeighbors(x, y);
				}
			}
		}
	}
	
	public static boolean updateRules(int x, int y) {
		boolean live = cells[x][y];
		int numNeighbors;
		//numNeighbors = getNumberOfNeighbors(x, y);
		numNeighbors = neighborCounter[x][y];
		//neighborCounter[x][y] = numNeighbors;
		if(numNeighbors < 2 || numNeighbors > 3) {
			live = false;
		}
		else if(numNeighbors == 3) {
			live = true;
		}
		updatedCells[x][y] = live;
		return live;
	}
	
	public static int getNumberOfNeighbors(int x0, int y0) {
		int numNeighbors = 0;
		int xMin, xMax, yMin, yMax;
		//xMin = x0 - 1;
		//xMax = x0 + 1;
		//yMin = y0 - 1;
		//yMax = y0 + 1;
		xMin = Math.max(x0 - 1, 0);
		xMax = Math.min(x0 + 1, numColumns - 1);
		yMin = Math.max(y0 - 1, 0);
		yMax = Math.min(y0 + 1, numRows - 1);
		for(int x = xMin; x <= xMax; x++) {
			for(int y = yMin; y <= yMax; y++) {
				if(cells[x][y] && !(x == x0 && y == y0)) {
					numNeighbors++;
				}
			}
		}
		return numNeighbors;
	}
	
	public static void createCells(int numColumns, int numRows) {
		cells = new boolean[numColumns][numRows];
		updatedCells = new boolean[numColumns][numRows];
		neighborCounter = new int[numColumns][numRows];
		ConwayCell.numColumns = numColumns;
		ConwayCell.numRows = numRows;
	}

	public static boolean[][] getCells() {
		return cells;
	}

	public static void setCells(boolean[][] cells) {
		ConwayCell.cells = cells;
		ConwayCell.updatedCells = cells;
		for(int x = 0; x < numColumns; x++) {
			for(int y = 0; y < numRows; y++) {
				neighborCounter[x][y] = getNumberOfNeighbors(x, y);
			}
		}
	}

	public static boolean[][] getUpdatedCells() {
		return updatedCells;
	}

	public static void setUpdatedCells(boolean[][] updatedCells) {
		ConwayCell.updatedCells = updatedCells;
	}

	public static int getNumColumns() {
		return numColumns;
	}

	public static void setNumColumns(int numColumns) {
		ConwayCell.numColumns = numColumns;
	}

	public static int getNumRows() {
		return numRows;
	}

	public static void setNumRows(int numRows) {
		ConwayCell.numRows = numRows;
	}

	public static int getTick() {
		return tick;
	}

	public static void setTick(int tick) {
		ConwayCell.tick = tick;
	}

	public static long getTimeLastTick() {
		return timeLastTick;
	}

	public static void setTimeLastTick(long timeLastTick) {
		ConwayCell.timeLastTick = timeLastTick;
	}

	public static boolean isPaused() {
		return isPaused;
	}

	public static void setPaused(boolean isPaused) {
		ConwayCell.isPaused = isPaused;
		timeLastTick = 0;
	}

	public static int getNumLiveCells() {
		return numLiveCells;
	}

	public static void setNumLiveCells(int numLiveCells) {
		ConwayCell.numLiveCells = numLiveCells;
	}

	public static int[][] getNeighborCounter() {
		return neighborCounter;
	}

	public static void setNeighborCounter(int[][] neighborCounter) {
		ConwayCell.neighborCounter = neighborCounter;
	}
	
	
}
