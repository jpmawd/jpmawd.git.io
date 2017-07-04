package bounce;

import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JOptionPane;

public class CustomPortal extends VPortal {
	
	boolean active;
	
	public CustomPortal(int xPos, int yPos, int level) {
		super(xPos, yPos, level);
		active = true;
	}
	
	public void update() {
		if(active && Main.distance(getxCenter(), getyCenter(), Main.player1.getxCenter(), Main.player1.getyCenter()) < Main.player1.getRadius() + getWidth() / 2) {
			Main.isPaused = true;
			String path = "";
			path = JOptionPane.showInputDialog(null, "[enter file path of custom level]", "CUSTOM PORTAL", JOptionPane.PLAIN_MESSAGE);
			if(path != null) {
				try {
					Main.shots.clear();
					Main.bombs.clear();
					Main.rollercoasters.clear();
					Main.carts.clear();
					Main.items.clear();
					Main.bosses.clear();
					Main.chests.clear();
					Main.ghosts.clear();
					Main.fires.clear();
					Main.ps.clear();
					Main.gfx.clear();
					Main.variablePortals.clear();
					Message.getMessages().clear();
					Main.isSlender = false;
					
					Main.loadLevel(new File(path).toURI().toURL());
					Main.columns = Main.createColumns(Main.world1, Main.levelWidth / 20);
					
					Main.worldX = (int) Main.player1.getxPos();
					Main.player1.restore(false);
					Main.levelNumber = getLevel();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			active = false;
		}
	}

}
