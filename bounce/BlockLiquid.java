package bounce;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class BlockLiquid extends Block{

	int spreadSpeed = 0;
	
	public BlockLiquid(int xPos, int yPos) {
		super(xPos, yPos);
	}
	

	public void procedure() {
		if(random.nextInt(20) < spreadSpeed) {
			try {
				spread(xPos, yPos, this.getClass());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
	}
	public static void spread(int xPos, int yPos, Class<? extends BlockLiquid> class1) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		boolean isDown = false, isLeft = false, isRight = false, isWaterfall = false, isRightRight = false, isLeftLeft = false, isRightDown = false, isLeftDown = false;
		//int xTest;
		int yTest;
		Block block;
		ArrayList<Block> column;
		int index;
		index = xPos / 20;
		if(index >= 0 && index < Main.columns.length) {
		column = Main.columns[index];
		for(int i = 0; i < column.size(); i++) {
			block = column.get(i);
			//xTest = block.xPos;
			yTest = block.yPos;
			if(yTest == yPos + 20) {
				isDown = true;
				if(class1.isInstance(block)) {
					isWaterfall = true;
				}
			}
			if(isDown) {
				break;
			}
		}
		}
		index = xPos / 20 + 1;
		if(index >= 0 && index < Main.columns.length) {
		column = Main.columns[index];
		for(int i = 0; i < column.size(); i++) {
			block = column.get(i);
			//xTest = block.xPos;
			yTest = block.yPos;
			if(yTest == yPos) {
				isRight = true;
			}
			else if(yTest == yPos + 20 && class1.isInstance(block)) {
				isRightDown = true;
			}
			if(isRight && isRightDown) {
				break;
			}
		}
		}
		index = xPos / 20 - 1;
		if(index >= 0 && index < Main.columns.length) {
		column = Main.columns[index];
		for(int i = 0; i < column.size(); i++) {
			block = column.get(i);
			//xTest = block.xPos;
			yTest = block.yPos;
			if(yTest == yPos) {
				isLeft = true;
			}
			else if(yTest == yPos + 20 && class1.isInstance(block)) {
				isLeftDown = true;
			}
			if(isLeft && isLeftDown) {
				break;
			}
		}
		}
		index = xPos / 20 + 2;
		if(index >= 0 && index < Main.columns.length) {
		column = Main.columns[index];
		for(int i = 0; i < column.size(); i++) {
			block = column.get(i);
			//xTest = block.xPos;
			yTest = block.yPos;
			if(yTest == yPos) {
				isRightRight = true;
			}
			if(isRightRight) {
				break;
			}
		}
		}
		index = xPos / 20 - 2;
		if(index >= 0 && index < Main.columns.length) {
		column = Main.columns[index];
		for(int i = 0; i < column.size(); i++) {
			block = column.get(i);
			//xTest = block.xPos;
			yTest = block.yPos;
			if(yTest == yPos && class1.isInstance(block)) {
				isLeftLeft = true;
			}
			if(isLeftLeft) {
				break;
			}
		}
		}
		if(!isWaterfall) {
			if(isDown) {
				if(!isRight) {
					//Main.world1.add(new Block(xPos + 20, yPos, 20, 20, "ZEROG"));
					index = xPos / 20 + 1;
					if(index >= 0 && index < Main.columns.length) {
						Main.columns[index].add(createLiquidBlock(class1, xPos + 20, yPos));
					}
				}
				if(!isLeft) {
					//Main.world1.add(new Block(xPos - 20, yPos, 20, 20, "ZEROG"));
					index = xPos / 20 - 1;
					if(index >= 0 && index < Main.columns.length) {
						Main.columns[index].add(createLiquidBlock(class1, xPos - 20, yPos));
					}
				}
			}
			else {
				//Main.world1.add(new Block(xPos, yPos + 20, 20, 20, "ZEROG"));
				index = xPos / 20;
				if(index >= 0 && index < Main.columns.length) {
					Main.columns[index].add(createLiquidBlock(class1, xPos, yPos + 20));
				}
			}
		}
		if((!isRight) && isRightRight && isRightDown) {
			//Main.world1.add(new Block(xPos + 20, yPos, 20, 20, "ZEROG"));
			index = xPos / 20 + 1;
			if(index >= 0 && index < Main.columns.length) {
				Main.columns[index].add(createLiquidBlock(class1, xPos + 20, yPos));
			}
		}
		if((!isLeft) && isLeftLeft && isLeftDown) {
			//Main.world1.add(new Block(xPos - 20, yPos, 20, 20, "ZEROG"));
			index = xPos / 20 - 1;
			if(index >= 0 && index < Main.columns.length) {
				Main.columns[index].add(createLiquidBlock(class1, xPos - 20, yPos));
			}
		}
	}
	public static Block createLiquidBlock(Class<?> class1, int xPos, int yPos) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return (Block) class1.getConstructor(new Class[]{int.class, int.class}).newInstance(xPos, yPos);
	}
}
