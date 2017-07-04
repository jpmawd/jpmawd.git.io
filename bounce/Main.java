package bounce;


import javax.swing.JApplet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import bounce.BasicButton;
import bounce.Page;
import bounce.TextField;


import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.nfunk.jep.ParseException;


public class Main extends JApplet implements MouseMotionListener, MouseListener, KeyListener, AudioClip {
	public static final int SCREEN_WIDTH = 600, SCREEN_HEIGHT = 600;
	
	public static Player player1;
	static boolean UP = false, DOWN = false, LEFT = false, RIGHT = false, WEAPON_SWITCH = false, SHIELD = false;
	static boolean MOUSE_PRESSED = false, MOUSE_CLICKED = false;
	static int MOUSE_X;
	static int MOUSE_Y;
	final static int ARROW_KEYS = 0;
	final static int WASD = 1;
	static int directionKeys = WASD;
	static long timeStart;
	public static int worldX;
	public static int worldY;
	public static int worldXOrigin;
	public static int worldYOrigin;
	static int levelWidth = 0;
	static boolean loadNextLevel = false;
	static int levelNumber = 0;
	static String levelName = "RANDOM TERRAIN";
	static int checkPoint = 1;
	//static String filePath = JOptionPane.showInputDialog(null, "Enter file path for game files", "BOUNCE", JOptionPane.INFORMATION_MESSAGE);
	
	
	static String filePath = "/gamefiles/";
	static String levelsFP = filePath + "levels/";
	static String audioFP = filePath + "audio/";
	static String imagesFP = filePath + "images/";
	
	URL url = Main.class.getResource(filePath); 
	
	static boolean[] defaultSettings = {true,true,true,false};
	
	static boolean useDefaultSettings = true;
	static boolean isSound = (useDefaultSettings) ? defaultSettings[0] : (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Sound on?",  "BOUNCE", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE));
	static boolean isPSOn = (useDefaultSettings) ? defaultSettings[1] : (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Particles on?",  "BOUNCE", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE));
	static boolean isBackground = (useDefaultSettings) ? defaultSettings[2] : (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Backgrounds on?",  "BOUNCE", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE));
	static boolean loadLevelsFromHTML = (useDefaultSettings) ? defaultSettings[3] : (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Load levels from site HTML? (must specify URL)",  "BOUNCE", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE));
	static ArrayList<Block>[] columns;
	static ArrayList<Block> world1 = new ArrayList<Block>();
	static ArrayList<Shot> shots = new ArrayList<Shot>();
	static ArrayList<Bomb> bombs = new ArrayList<Bomb>();
	static ArrayList<RollerCoaster> rollercoasters = new ArrayList<RollerCoaster>();
	static ArrayList<Cart> carts = new ArrayList<Cart>();
	static ArrayList<Item> items = new ArrayList<Item>();
	static ArrayList<Boss> bosses = new ArrayList<Boss>();
	static ArrayList<Chest> chests = new ArrayList<Chest>();
	static ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
	static ArrayList<Fire> fires = new ArrayList<Fire>();
	static ArrayList<VPortal> variablePortals = new ArrayList<VPortal>();
	static ArrayList<ParticleSystem> ps = new ArrayList<ParticleSystem>();
	static ArrayList<GraphicEffect> gfx = new ArrayList<GraphicEffect>(), gfx2 = new ArrayList<GraphicEffect>();
	static ArrayList<AudioClip> sounds = new ArrayList<AudioClip>();
	static BufferedImage TITLE = null;
	static BufferedImage TITLE_NEGATIVE = null;
	static BufferedImage SLENDER = null;
	static BufferedImage BACKGROUND = null;
	static Color waterColor = Color.blue.darker().darker().darker();
	//static Color bluePlasmaColor = new Color(0, 100, 250).brighter();
	static Color bluePlasmaColor = Color.CYAN;
	static Color woodColor = new Color(175, 125, 25);
	static Color ice0Color = Color.CYAN.brighter().brighter();
	static Color ice1Color = ice0Color.brighter().brighter();
	static Color conwayCellColor = new Color(60, 255, 200);
	static Color purple = new Color(200, 60, 255);
	static Color plantColor = new Color(20, 255, 20);
	static Color skyColor = new Color(125, 240, 255);
	static AudioClip gun, ray;
	static boolean isPaused = true , isComplete = false, isDeath = false;
	static boolean hasBackground;
	static int worldNumber = 0;
	static int bossCount = 0;
	static int totalBossMaxHealth = 0;
	static String[] functions = {
		"sin(x)",
		"sin(x)*cos(x)",
		"exp(x)",
		"sin(x^2)",
		"1/x",
		"sqrt(x)",
		"atan(x)",
		"x",
		"0"
		
	};
	public static boolean isSlender = false;
	public static int slenderX = 0, slenderY = 0;
	
	private static Map<String, Class<?>> stringToClassMapping = new HashMap<String, Class<?>>();
	private static Map<Class<?>, String> classToStringMapping = new HashMap<Class<?>, String>();
	
	static{
		addMapping("NORMAL", BlockNormal.class);
		addMapping("REFLECT", BlockMirror.class);
		addMapping("BOMB", BlockBomb.class);
		addMapping("UNBREAKABLE", BlockUnbreaking.class);
		addMapping("PORTAL", BlockPortal.class);
		addMapping("TURRET", BlockTurret.class);
		addMapping("ROCKET", BlockRocket.class);
		addMapping("BOUNCE", BlockBounce.class);
		addMapping("ZEROG", BlockWater.class);
		addMapping("LAVA", BlockLava.class);
		addMapping("WOOD", BlockWood.class);
		addMapping("POWERUP", BlockPowerup.class);
		addMapping("ICE", BlockIce.class);
		addMapping("SNOW", BlockSnow.class);
		addMapping("VOID", BlockEnemyVoid.class);
		addMapping("PLANT", BlockVegetation.class);
		addMapping("POW", BlockPow.class);
		addMapping("CRYSTAL", BlockCrystal.class);
	}
	
	public static boolean isGameOfLife = false;
	
	static Random random = new Random();
	
	static byte transitionCode = 0;
	static long timeTransitionStart = 0;
	static String transitionText = "";
	static BufferedImage screenShot;
	
	static boolean displayLogo = true;
	static BufferedImage LOGO = createBufferedImage(imagesFP + "inductivecapacitor.bmp");
	
	static BufferedImage ICON = createBufferedImage(imagesFP + "ICON.bmp");
	
	static URL levelsURL;
	
	// from WindowGUI
	private Image dbImage;
    private Graphics dbGraphics;
    private Color bgColor = Color.GRAY;
    private ArrayList<Page> pages = new ArrayList();
    private int currentPageNumber = 0;
    private Page currentPage;
    private boolean isAnyContext = false;
    private static int mouseX;
    private static int mouseY;
    private static boolean isMousePressed;
    private static boolean isMouseHeld;
    private static boolean isMouseClicked;
    private static boolean isAlreadyMousePressed;
    private static boolean isMousePressedBuffer;
    private static boolean isKeyPressed;
    private static boolean isKeyHeld;
    private static boolean isKeyReleased;
    private static boolean isKeyClicked;
    private static boolean isAlreadyKeyPressed;
    private static boolean isKeyPressedBuffer;
    private static char key;
    private static char keyBuffer;
    private static int keyCode;
    private static int keyCodeBuffer;
    private static boolean isKeyBackSpace;
    
    private boolean gameStarted = false;
    
    static int[] keyTimer = new int[256];

	
	
	/**************************************************************************************
	 *****^*********************************|**********************************************
	 *****|**INITIALIZATIONS*******LOOPING**|**********************************************
	 *****|*********************************V**********************************************
	 **************************************************************************************/
	
	private static void addMapping(String par0Str, Class par1Class)
    {
        stringToClassMapping.put(par0Str, par1Class);
        classToStringMapping.put(par1Class, par0Str);
    }
	
	
		
	
	
	public void createGame(int width, int height, String title, ArrayList<Page> pages) {
		this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        //this.setTitle(title);
        this.setName(title);
        this.setSize(width, height);
        //this.setResizable(isResizable);
        this.setVisible(true);
        this.setFocusable(true);
        this.requestFocusInWindow();
        //this.setDefaultCloseOperation(defaultCloseOperation);
        this.setBackground(bgColor);
        this.bgColor = bgColor;
        this.pages = pages;
        this.currentPage = pages.get(this.currentPageNumber);
		//setIconImage(ICON);
		this.setBackground(new Color(238, 238, 238));
		timeStart = System.currentTimeMillis();
		//addKeyListener(new KeyBoard());
		//addKeyListener(this);
		//addMouseListener(new Mouse());
		//addMouseMotionListener(this);
		//addMouseListener(this);
		//setTitle("BOUNCE");
		//setSize(600,600);
		//setResizable(false);
		//setVisible(true);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	

		
		
		worldX = getWidth()/2;
		worldY = getHeight()/2;
		worldXOrigin = worldX;
		worldYOrigin = worldY;
		player1 = new Player(worldX, worldY, 15, 15, 2000, 2000);
		player1.setRegenerate(true);
		/*
		URL checkPointURL = createURL(filePath + "data/checkpoint.txt");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(checkPointURL.openStream()));
			String line = br.readLine();
			while(line != null) {
				checkPoint = Integer.parseInt(line);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		*/
		
		world1 = new ArrayList<Block>();
		world1 = randomWorld(3600);
		BlockPortal portal = new BlockPortal(2400, 400);
		world1.add(portal);
		columns = createColumns(world1, levelWidth / 20);
		try {
			TITLE = createBufferedImage(imagesFP + "TITLE.bmp");
			TITLE_NEGATIVE = createBufferedImage(imagesFP + "TITLE.bmp");	
			SLENDER = imageToBufferedImage(makeColorTransparent(createBufferedImage(imagesFP + "SLENDER.bmp"), Color.WHITE));
		}	catch(Exception e) {
			
		}
		try {
			gun = createAudioClip(audioFP + "gun.wav");
			ray = createAudioClip(audioFP + "ray.wav");
		}	catch(Exception e) {
			
		}
		/*
		Thread playerThread = new Thread(player1);
		playerThread.setPriority(7);
		playerThread.start();
		*/
				
	}
	public static BufferedImage createBufferedImage(String filePath) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(createURL(filePath));
			//image = imageToBufferedImage(makeColorTransparent(image, Color.WHITE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	public static AudioClip createAudioClip(String filePath) {
		AudioClip clip = null;
		clip = Applet.newAudioClip(createURL(filePath));
		return clip;
	}
	public static URL createURL(String filePath) {
		URL url = null;
		url = Main.class.getResource(filePath);
		return url;
	}
	/*
	public void paint(Graphics g) {
		dbimage = createImage(getWidth(), getHeight());
		dbgraphics = dbimage.getGraphics();
		paintComponent(dbgraphics);
		g.drawImage(dbimage, 0, 0 , this);
	}
	*/
	public void drawOver(Graphics g) {
		if(displayLogo) {
			g.drawImage(LOGO, 0, 0, this);
			displayLogo = (System.currentTimeMillis() - timeStart < 1500);
		}
	}
	
	public void drawUnder(Graphics g) {
		if(transitionCode == 0) {
		if(isPaused) {
			
			g.drawImage(TITLE, 0, 0, 600, 600, this);
			g.drawString("Press \"P\" to toggle PAUSE", 200, 550);
			
			if(isComplete && false) {  //dead code
				Font font0 = g.getFont();
				g.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 30));
				g.drawString("Congratulations!", 200, 100);
				g.setFont(font0);
				if(worldNumber == 1) {
					if(Math.random() > .99) {
						gfx2.add(GraphicEffect.FIRE_EXPLOSION(600 * Math.random(), 600 * Math.random(), Math.random()));
					}
				}
				else if(worldNumber == 2) {
					if(Math.random() > .99) {
						gfx2.add(GraphicEffect.createGraphicEffectByName("SHATTER", 600 * Math.random(), 600 * Math.random(), new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))));
					}
				}
			}
			if(isDeath) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
				Font tempFont = getFont();
				g.setFont(new Font("LCD5x8H", Font.PLAIN, 70));
				g.setColor(Color.RED);
				
				String deathMessage = "";
				String completeMessage = "YOU_DIED";
				for(int i = 0; i < ((double) (System.currentTimeMillis() % 3000)) / 3000 * 8; i++) {
					deathMessage += completeMessage.charAt(i);
				}
				String s = "";
				for(int boxY = -1; boxY < 15; boxY++) {
					for(int boxX = 0; boxX < 16; boxX++) {
						if(boxY == 2 && boxX > 0 && boxX < deathMessage.length() + 1) {
							s = deathMessage.charAt(boxX - 1) + "";
						}
						else {
							s = random.nextInt(10) + "";
						}
						g.drawString(s, 50 * boxX,  100 + 50 * boxY);
					}
				}
				
				g.setColor(Color.BLACK);
				g.setFont(tempFont);
				
				//g.drawString("YOU", 475, 100);
				//g.drawString("DIED.", 475, 120);
			}
			else {
				//Interesting code
				/*
				int x = getX();
				int y = getY();
				Rectangle rectangle = new Rectangle(x, y, this.getWidth(),
				this.getHeight());
				Robot robot = null;
				try {
					robot = new Robot();
				} catch (AWTException e) {
					e.printStackTrace();
				}
				BufferedImage image = robot.createScreenCapture(rectangle);
				g.drawImage(image, 0, 0, 500, 500, this);
				*/
				int x = (int) (400 * (System.currentTimeMillis() % 1500) / 1500);
				BufferedImage b = null;
				if(TITLE_NEGATIVE != null) {
					b = negative(TITLE_NEGATIVE);
					b = b.getSubimage(x, 203, 83, 83);
					g.drawImage(b, (int) (1.2 * x), 243, 100, 100, this);
				}
				
				int nLines = 200;
				int[] randXs1 = new int[nLines];
				int[] randYs1 = new int[nLines];
				int[] randXs2 = new int[nLines];
				int[] randYs2 = new int[nLines];
				int[] randXs3 = new int[nLines];
				int[] randYs3 = new int[nLines];
				for(int i = 0; i < nLines; i++) {
					double angle = 2 * Math.PI * Math.random();
					randXs1[i] = (int) (50 * Math.cos(angle) + 280);
					randYs1[i] = (int) (50 * Math.sin(angle) + 415);
					angle +=  2 * (Math.random() - .50);
					randXs2[i] = (int) (75 * Math.cos(angle) + 280);
					randYs2[i] = (int) (75 * Math.sin(angle) + 415);
					angle += (Math.random() - .50);
					randXs3[i] = (int) (100 * Math.cos(angle) + 280);
					randYs3[i] = (int) (100 * Math.sin(angle) + 415);
				}
				g.setColor(new Color((int)(255 * Math.random()), (int)(255 * Math.random()), (int)(255 * Math.random())));
				for(int i = 0; i < (int)(nLines * Math.random()); i++) {
					int index = (int) (nLines * Math.random());
					int x1 = 280;
					int y1 = 415;
					int x2 = randXs1[index];
					int y2 = randYs1[index];
					int x3 = randXs2[index];
					int y3 = randYs2[index];
					int x4 = randXs3[index];
					int y4 = randYs3[index];
					g.drawLine(x1, y1, x2, y2);
					g.drawLine(x2, y2, x3, y3);
					g.drawLine(x3, y3, x4, y4);
				}
				
				g.setColor(Color.BLACK);
				g.fillOval(264, 400, 32, 32);
				g.setColor(Color.RED);
				g.drawLine(275, 415, 285, 415);
				g.drawLine(280, 410, 280, 420);
				g.setColor(Color.BLACK);
				
			}
			for(int i = 0; i < gfx2.size(); i++) {
				GraphicEffect update = gfx2.get(i);
				update.render(g, 0, 0);
				if(update.isTerminated()) {
					gfx2.remove(i);
				}
				else {
					gfx2.set(i, update);
				}
			}
			for(int i = 0; i < getPages().size(); i++) {
				if(getPages().get(i).getPageNumber() == 0) {
					for(int ii = 0; ii < getPages().get(i).getButtons().size(); ii++) {
						BasicButton b = getPages().get(i).getButtons().get(ii);
						if(b.getIdentifier() == 0) {
							if(b.isClicked()) {
								isPaused = false;
								b.setClicked(false);
								setCurrentPageNumber(1);
								gfx2.clear();
							}
						}
						else if(b.getIdentifier() == 1) {
							if(b.isClicked()) {
								if(directionKeys == ARROW_KEYS) {
									directionKeys = WASD;
									b.setLabel("  WASD");
								}
								else {
									directionKeys = ARROW_KEYS;
									b.setLabel("  ARROWS");
								}
								b.setClicked(false);
							}
						}
						else if(b.getIdentifier() == 2) {
							if(b.isClicked()) {
								isPaused = false;
								b.setClicked(false);
								setCurrentPageNumber(1);
								levelNumber = 0;
								checkPoint = 1;
								/*
								URL checkPointURL = createURL(filePath + "data/checkpoint.txt");
								try {
									BufferedWriter bw = new BufferedWriter(new FileWriter(new File(checkPointURL.getFile())));
									System.out.println(checkPointURL.getFile());
									bw.write(""+checkPoint);
									bw.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								*/
								loadNextLevel = true;
							}
						}
						getPages().get(i).getButtons().set(ii, b);
					}
					for(int ii = 0; ii < getPages().get(i).getTextFields().size(); ii++) {
						TextField t = getPages().get(i).getTextFields().get(ii);
						if(t.getIdentifier() == 0) {
							if(!isPaused) {
								if(t.getText().equals("super")) {
									player1.setInvincible(true);
								}
								else if(t.getText().equals("!super")) {
									player1.setInvincible(false);
								}
								else if(t.getText().startsWith("GOLVL:")) {
									//String urlStr = t.getText().substring(6);
									try {
										loadLevel(Integer.parseInt(t.getText().substring(6)));
									} catch(NumberFormatException e) {
										e.printStackTrace();
									}
									
								}
							}
						}
					}
					break;
				}
			}
			if(!isPaused) {
				isComplete = false;
				TITLE = createBufferedImage(imagesFP + "TITLE.bmp");
			}
		}
		else {
			isDeath = false;
			player1.update();
			if((player1.getxPos() > worldX + 40) && (player1.getxVelocity() > 0)) {
				if(player1.getxPos() > worldX + 140) {
					worldX++;
				}
				if(player1.getxVelocity() < 4) {
					worldX++;
				}
				else {
					worldX += player1.getxVelocity();
				}
			}
			if((player1.getxPos() < worldX - 40) && (player1.getxVelocity() < 0)) {
				if(player1.getxPos() < worldX - 140) {
					worldX--;
				}
				if(player1.getxVelocity() > -4) {
					worldX--;
				}
				else {
					worldX += player1.getxVelocity();
				}
			}
			int relativeX = worldXOrigin - worldX;
			int relativeY = worldYOrigin - worldY;
			//player1.update();
			
			if(isBackground && hasBackground) {
				g.drawImage(BACKGROUND, 0, 0, 600, 600, null);
			}
			
			
			if(isSlender) {
				slenderX-=4;
				gfx.add(GraphicEffect.SHATTER(slenderX + 40, slenderY + Math.random() * 200));
				g.drawImage(SLENDER, slenderX + relativeX, slenderY + relativeY, 200, 200, this);
			}
			Message.display(g, relativeX, relativeY);
			
			for(int i = 30; i >= 0; i--) {
				int index = ((worldX / 20) + (i - 15));
				if(index >= 0 && index < columns.length) {
					ArrayList<Block> column = columns[index];
					for(int ii = 0; ii < column.size(); ii++) {
						Block block = column.get(ii);
						int screenXPos = index * 20 + (relativeX);
						int screenYPos = block.yPos;
						block.renderBlock(g, screenXPos, screenYPos);
							block.update();
							if(block.isTerminated) {
								column.remove(ii);
							}
							else {
								column.set(ii, block);
							}
						}
					columns[index] = column;
				}
			}
			
			if(isGameOfLife && ConwayCell.getNumLiveCells() != 0) {
				ConwayCell.updateCells();
				//g.setColor(Main.conwayCellColor);
				for(int x = 30; x >= 0; x--) {
					int index = ((worldX / 20) + (x - 15));
					if(index >= 0 && index < ConwayCell.getNumColumns()) {
						for(int y = 0; y < ConwayCell.getNumRows(); y++) {
							if(ConwayCell.getCells()[index][y]) {
								g.setColor(Main.purple);
								g.fillRect(index * 20 + relativeX, y * 20, 20, 20);
								g.setColor(Main.conwayCellColor);
								g.drawRect(index * 20 + relativeX, y * 20, 20, 20);
								//g.drawString("" + ConwayCell.getNeighborCounter()[index][y], index * 20 + 5 + relativeX, y * 20 + 15);
							}
							//g.drawString("" + ConwayCell.getNeighborCounter()[index][y], index * 20 + 5 + relativeX, y * 20 + 15);
						}
					}
				}
				g.setColor(Color.BLACK);
			}
			
			
				for(int i = 0; i < variablePortals.size(); i++) {
				VPortal vp = variablePortals.get(i);
				int screenXPos = vp.getxPos() + (relativeX);
				int screenYPos = vp.getyPos();
				g.setColor(Color.WHITE);
				g.fillOval(screenXPos, screenYPos, vp.getWidth(), vp.getWidth());
				if(vp instanceof CustomPortal) {
					g.setColor(conwayCellColor);
				}
				else {
					g.setColor(Color.BLACK);
				}
				int radius = (int) (vp.getWidth() * (double)((double)(System.currentTimeMillis() % 200) / 200));
				g.drawOval((int)(vp.getxCenter() - radius / 2) + relativeX, (int)(vp.getyCenter() - radius / 2), radius, radius);
				vp.update();
				variablePortals.set(i, vp);
			}
			
				for(int ii = 0; ii < shots.size(); ii++) {
					Shot updateShot = shots.get(ii);
					int screenXPos = (int) updateShot.xPos + (relativeX);
					int screenYPos = (int) updateShot.yPos;
					updateShot.renderEntity(g, screenXPos, screenYPos);
					updateShot.update();
					if(shots.get(ii).isTerminated) {
						shots.remove(ii);
					}
					else {
						shots.set(ii, updateShot);
					}
				}
					for(int iii = 0; iii < bombs.size(); iii++) {
						int screenXPos = (int) bombs.get(iii).xPos + (relativeX);
						int screenYPos = (int) bombs.get(iii).yPos;
						
						Bomb updateBomb = bombs.get(iii);
						updateBomb.renderEntity(g, screenXPos, screenYPos);
						updateBomb.update();
						if(bombs.get(iii).isTerminated) {
							bombs.remove(iii);
						}
						else {
							bombs.set(iii, updateBomb);
						}
					}
				
				for(int i = 0; i < rollercoasters.size(); i++) {
					int screenXPos = (int) rollercoasters.get(i).getStartX() + (relativeX);
					int screenYPos = (int) rollercoasters.get(i).getStartY();
					g.setColor(Color.ORANGE);
					g.fillRect(screenXPos, screenYPos, 20, 20);
					if(System.currentTimeMillis() / 500 % 2 == 0) {
						g.setColor(Color.RED);
						g.drawLine(screenXPos + 5, screenYPos + 10, screenXPos + 15, screenYPos + 10);
						g.drawLine(screenXPos + 15, screenYPos + 10, screenXPos + 10, screenYPos + 5);
						g.drawLine(screenXPos + 15, screenYPos + 10, screenXPos + 10, screenYPos + 15);
						
					}
					g.setColor(Color.BLACK);
					g.drawRect(screenXPos, screenYPos, 20, 20);
					
					
					RollerCoaster updateRC = rollercoasters.get(i);
					updateRC.update();
					rollercoasters.set(i, updateRC);
				}
				for(int i = 0; i < carts.size(); i++) {
					int screenXPos = (int) carts.get(i).getxPos() + (relativeX);
					int screenYPos = (int) (carts.get(i).getyPos());
					
					g.setColor(Color.RED);
					g.fillRect(screenXPos - 20, screenYPos + 10, 40, 20);
					g.fillPolygon(new int[]{screenXPos + 20, screenXPos + 40, screenXPos + 20}, new int[]{screenYPos + 5, screenYPos + 15, screenYPos + 35}, 3);
					g.setColor(Color.BLACK);
					g.drawRect(screenXPos - 20, screenYPos + 10, 40, 20);
					g.drawPolygon(new int[]{screenXPos + 20, screenXPos + 40, screenXPos + 20}, new int[]{screenYPos + 5, screenYPos + 15, screenYPos + 35}, 3);
					Cart updateCart = carts.get(i);
					updateCart.update();
					if(updateCart.isTerminated()) {
						carts.remove(i);
					}
					else {
						carts.set(i, updateCart);
					}
				}
				
					for(int iii = 0; iii < items.size(); iii++) {
						Item updateItem = items.get(iii);
						int screenXPos = (int) updateItem.getxPos() + (relativeX);
						int screenYPos = (int) updateItem.getyPos();
						updateItem.renderEntity(g, screenXPos, screenYPos);
						updateItem.update();
						if(updateItem.isTerminated()) {
							items.remove(iii);
						}
						else {
							items.set(iii, updateItem);
						}
					}
				
					for(int iii = 0; iii < chests.size(); iii++) {
						Chest updateChest = chests.get(iii);
						int screenXPos = (int) updateChest.getxPos() + (relativeX);
						int screenYPos = (int) updateChest.getyPos();
						updateChest.renderEntity(g, screenXPos, screenYPos);
						updateChest.update();
						if(updateChest.isTerminated()) {
							chests.remove(iii);
						}
						else {
							chests.set(iii, updateChest);
						}
					}
					
					for(int iii = 0; iii < ghosts.size(); iii++) {
						Ghost updateGhost = ghosts.get(iii);
						int screenXPos = (int) updateGhost.getxPos() + (relativeX);
						int screenYPos = (int) updateGhost.getyPos();
						updateGhost.renderEntity(g, screenXPos, screenYPos);
						updateGhost.update();
						if(updateGhost.isTerminated()) {
							ghosts.remove(iii);
						}
						else {
							ghosts.set(iii, updateGhost);
						}
					}
					
					for(int i = 0; i < fires.size(); i++) {
						Fire updateFire = fires.get(i);
						updateFire.update();
						if(updateFire.isTerminated()) {
							fires.remove(i);
						}
						else {
							fires.set(i, updateFire);
						}
					}
					
					{
						int screenXPos = (int) FinalBoss.getxPos() + (relativeX);
						int screenYPos = (int) FinalBoss.getyPos();
						FinalBoss.updateAndRender(g, screenXPos, screenYPos);
					}
				
				//player1.update();
				
				g.fillOval((int) player1.getxPos() + (relativeX), (int) player1.getyPos(), player1.getWidth(), player1.getHeight());
				g.fillOval((int) (player1.getxPos() - .25 * player1.getWidth() + (relativeX)), (int) ((int) player1.getyPos() + player1.getHeight() - 2 * player1.getyVelocity()), player1.getWidth() / 2, player1.getHeight() / 2);
				g.fillOval((int) (player1.getxPos() + .75 * player1.getWidth() + (relativeX)), (int) ((int) player1.getyPos() + player1.getHeight() - 2 * player1.getyVelocity()), player1.getWidth() / 2, player1.getHeight() / 2);
				g.fillOval((int) (player1.getxPos() - .33 * player1.getWidth() - player1.getxVelocity() + (relativeX)), (int) ((int) player1.getyPos() + .75 * player1.getHeight() - player1.getyVelocity()), player1.getWidth() / 3, player1.getHeight() / 3);
				g.fillOval((int) (player1.getxPos() + player1.getWidth() - player1.getxVelocity() + (relativeX)), (int) ((int) player1.getyPos() + .75 * player1.getHeight() - player1.getyVelocity()), player1.getWidth() / 3, player1.getHeight() / 3);
				
				
				
				
				
				
				
				
				
				if(player1.isShielding()) {
					if(player1.getPowerUp().equals("POWERUP_HYPER-SHIELD")) {
						g.setColor(new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int) (Math.random() * 255)));
					}
				g.drawOval((int)(player1.getxCenter() - player1.getShieldRadius() / 2) + relativeX, (int)(player1.getyCenter() - player1.getShieldRadius() / 2), player1.getShieldRadius(), player1.getShieldRadius());
				int radius = (int) (player1.getShieldRadius() * (double)((double)(System.currentTimeMillis() % 200) / 200));
				g.drawOval((int)(player1.getxCenter() - radius / 2) + relativeX, (int)(player1.getyCenter() - radius / 2), radius, radius);
				g.setColor(Color.BLACK);
				}
				//g.fillOval(MOUSE_X, MOUSE_Y, 5, 5);
				
				
				g.setFont(Message.getFont());
				g.setColor(new Color(0, 0, 0, 100));
				g.fillRect(0, 0, 600, 100);
				
				g.setColor(Color.RED);
				g.drawString("HEALTH", 25, 50);
				g.drawString("ENERGY [" + player1.getWeaponName() + "]", 25, 75);
				g.drawString("LEVEL " + levelNumber, 450, 50);
				g.drawString("\"" + levelName + "\"", 450, 75);
				if(player1.isUnderwater()) {
					g.drawString("OXYGEN", 25, 100);
				}
				if(!player1.getPowerUp().equals("NONE")) {
					g.drawString("POWERUP", 25, 125);
					g.drawString("[" + player1.getPowerUp().substring(8) + "]", 25, 150);
				}
				if(FinalBoss.isActivated()) {
					g.drawString("FINAL", 250, 50);
				}
				g.setColor(Color.BLACK);
				
				
				g.fillRect(125, 40, 100, 10);
				g.setColor(fillBarColor((double)player1.getHealth() / player1.getHealthCap()));
				g.fillRect(125, 40, 100 * player1.getHealth() / player1.getHealthCap(), 10);
				g.setColor(Color.BLACK);
				g.drawRect(125, 40, 100, 10);
				
				g.fillRect(125, 65, 100, 10);
				g.setColor(fillBarColor((double)player1.getAmmo() / player1.getAmmoCap()));
				g.fillRect(125, 65, 100 * player1.getAmmo() / player1.getAmmoCap(), 10);
				g.setColor(Color.BLACK);
				g.drawRect(125, 65, 100, 10);
				
				if(player1.isUnderwater()) {
					
					g.fillRect(125, 90, 100, 10);
					g.setColor(fillBarColor((double)player1.getOxygen() / player1.getOxygenCap()));
					g.fillRect(125, 90, 100 * player1.getOxygen() / player1.getOxygenCap(), 10);
					g.setColor(Color.BLACK);
					g.drawRect(125, 90, 100, 10);
				}
				if(!player1.getPowerUp().equals("NONE")) {
					
					g.fillRect(125, 115, 100, 10);
					g.setColor(fillBarColor(100 * ((double)System.currentTimeMillis() - player1.getTimePowerUp()) / player1.getPowerUpLength()));
					g.fillRect(125, 115, (int) (100 * (System.currentTimeMillis() - player1.getTimePowerUp()) / player1.getPowerUpLength()), 10);
					g.setColor(Color.BLACK);
					g.drawRect(125, 115, 100, 10);
				}
				if(FinalBoss.isActivated()) {
					
					g.fillRect(300, 40, 100, 10);
					if(FinalBoss.getHealth() == 0) {
						FinalBoss.reset(false);
					}
					g.setColor(fillBarColor((double)FinalBoss.getHealth() / FinalBoss.MAX_HEALTH));
					g.fillRect(300, 40, 100 * FinalBoss.getHealth() / FinalBoss.MAX_HEALTH, 10);
					g.setColor(Color.BLACK);
					g.drawRect(300, 40, 100, 10);
				}
				else if(bosses.size() > 0) {
					g.drawString("BOSS", 250, 50);
					g.fillRect(300, 40, 100, 10);
					int totalBossHealth = 0;
					for(int i = 0; i < bosses.size(); i++) {
						Boss boss = bosses.get(i);
						boss.procedure();
						totalBossHealth += boss.getHealth();
						if(boss.isTerminated()) {
							bosses.remove(i);
						}
						else {
							bosses.set(i, boss);
						}
					}
					g.setColor(fillBarColor(totalBossHealth / totalBossMaxHealth));
					g.fillRect(300, 40, 100 * totalBossHealth / totalBossMaxHealth, 10);
					g.setColor(Color.BLACK);
					g.drawRect(300, 40, 100, 10);
					
				}
				if(player1.isDead()) {
					levelNumber = checkPoint - 1;
					loadNextLevel = true;
					player1.restore(true);
					isPaused = true;
					isComplete = false;
					TITLE = createBufferedImage(imagesFP + "DEATH.bmp"); 
					isDeath = true;
					
				}
				if(loadNextLevel) {
					isPaused = true;
					if(isDeath) {
						transitionCode = player1.getDamageSource();
					}
					else {
						transitionCode = 10;
					}
					timeTransitionStart = System.currentTimeMillis();
					screenShot = imageToBufferedImage(getDbImage());
					
					levelNumber++;
					
					shots.clear();
					bombs.clear();
					rollercoasters.clear();
					carts.clear();
					items.clear();
					bosses.clear();
					chests.clear();
					ghosts.clear();
					fires.clear();
					ps.clear();
					gfx.clear();
					variablePortals.clear();
					Message.getMessages().clear();
					isSlender = false;
					
					if(!loadLevelsFromHTML) {
						loadLevel(levelNumber);
					}
					else {
						loadLevelFromHTML(levelNumber);
					}
					columns = createColumns(world1, levelWidth / 20);
					
					worldX = (int) player1.getxPos();
					player1.restore(false);
					
					loadNextLevel = false;
				}
				
				if(isSound) {
					for(int i = 0; i < sounds.size(); i++) {
						sounds.get(i).play();
						sounds.remove(i);
					}
				}
				else {
					sounds.clear();
				}
				if(isPSOn) {
					//System.out.println(ps.size());
					for(int i = 0; i < ps.size(); i++) {
						ParticleSystem update = ps.get(i);
						update.render(g, (relativeX), 0);
						if(update.isTerminated()) {
							ps.remove(i);
						}
						else {
							ps.set(i, update);
						}
					}
					for(int i = 0; i < gfx.size(); i++) {
						GraphicEffect update = gfx.get(i);
						update.render(g, (relativeX), 0);
						if(update.isTerminated()) {
							gfx.remove(i);
						}
						else {
							gfx.set(i, update);
						}
					}
				}
				else {
					ps.clear();
				}
		}
		}
		else {
			
			BufferedImage tintedImage;
			int red = 0, green = 0, blue = 0, alpha = 50;			
			
			if(transitionCode == 10) {
				red = 255;
				green = 255;
				blue = 255;
			}
			else if(transitionCode == Player.SHOT || transitionCode == Player.EXPLOSION || transitionCode == Player.FIRE) {
				red = 255;
			}
			else if(transitionCode == Player.DROWNING) {
				red = 25;
				green = 25;
				blue = 110;
			}
			
			
			
			tintedImage = tintImage(screenShot, red, green, blue, alpha);
			
			g.drawImage(tintedImage, 0, 0, this);
			
			if(System.currentTimeMillis() - timeTransitionStart > 2000) {
				transitionCode = 0;
				isPaused = true;
				setCurrentPageNumber(0);
			}
		}
		screenShot = imageToBufferedImage(getDbImage());
					
		updateKeyStates();
		
		repaint();
	}
	
	
	
	public void init() {
		ArrayList<Page> pages = new ArrayList<Page>();
		ArrayList<BasicButton> buttons0 = new ArrayList<BasicButton>();
		ArrayList<BasicButton> buttons1 = new ArrayList<BasicButton>();
		ArrayList<TextField> textFields0 = new ArrayList<TextField>();
		ArrayList<TextField> textFields1 = new ArrayList<TextField>();
		
		BasicButton b;
		b = new BasicButton(250, 560, 80, 30, "  CONTINUE?", Color.GREEN, 0);
		buttons0.add(b);
		BasicButton b2;
		b2 = new BasicButton(375, 560, 80, 30, "  ARROWS", Color.YELLOW, 1);
		buttons0.add(b2);
		BasicButton b3;
		b3 = new BasicButton(500, 560, 80, 30, "  RESTART?", Color.ORANGE, 2);
		buttons0.add(b3);
		
		TextField t;
		t = new TextField(50, 560, 150, 30, 0);
		textFields0.add(t);
				
		Page menu = new Page(buttons0, textFields0, 0);
		Page inGame = new Page(buttons1, textFields1, 1);
		
		pages.add(menu);
		pages.add(inGame);
		
		if(loadLevelsFromHTML) {
			try {
				if(!(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Load levels from default site HTML? (select NO for custom URL)",  "BOUNCE", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE))) {
					levelsURL = new URL(JOptionPane.showInputDialog(null, "Enter URL of levels HTML", "BOUNCE", JOptionPane.PLAIN_MESSAGE));
				}
				else {
					levelsURL = new URL("http://findingresonance.tumblr.com/BOUNCEGAME");
				}
				
			    
				
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Loading levels from HTML URL failed (bad URL?). Will load local levels instead.");
				loadLevelsFromHTML = false;
			}
		}
		
		createGame(SCREEN_WIDTH, SCREEN_HEIGHT, "BOUNCE", pages);
		System.out.println("created game");
		gameStarted = true;
		this.repaint();
				
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Block>[] createColumns(ArrayList<Block> world, int numberColumns) {
		ArrayList<Block>[] columns;
		columns = (ArrayList<Block>[])new ArrayList[numberColumns];
		for(int i = 0; i < numberColumns; i++) {
			columns[i] = new ArrayList<Block>();
		}
		for(int i = 0; i < world.size(); i++) {
			Block block = world.get(i);
			ArrayList<Block> column = columns[block.xPos / 20];
			column.add(block);
			columns[block.xPos / 20] = column;
		}
		return columns;
	}
	public static ArrayList<Block> randomWorld(int width) {
		ArrayList <Block> world = new ArrayList<Block>();
		for(int i = 0; i < width; i++) {
			for(int ii = 0; ii < 30; ii++) {
				if(Math.random() > (double)((width - ii)%30)/5) {
					world.add(new BlockNormal(i*20, (ii%30)*20));
				}
			}
		}
		if(width * 20 > levelWidth) {
			levelWidth = width * 20;
		}
		return world;
	}
	
	public static void loadLevel(int levelNumber) {
		loadLevel(levelsFP + "LEVEL_" + levelNumber + ".txt");
	}
	public static void loadLevel(String levelPath) {
		loadLevel(createURL(levelPath));
	}
	public static void loadLevel(URL textURL) {
		ArrayList <Block> level = new ArrayList<Block>();
		
		//System.out.println(levelName);
		//File levelFile = new File(levelName);
		try {
			
			FileReader levelFileReader = null;
			BufferedReader levelBufferedReader = null;
			//levelFileReader = new FileReader(new File(Main.class.getResource(levelPath).getPath()));
			//levelBufferedReader = new BufferedReader(levelFileReader);
			levelBufferedReader = new BufferedReader(new InputStreamReader(textURL.openStream()));
			
			String[] lines;
			String line, longLine = "";
			int numLines = 0;
		    while ((line = levelBufferedReader.readLine()) != null) {
		    	longLine += line + '\n';
		    	numLines++;
		    }
		    levelBufferedReader.close();
			
		    lines = new String[numLines];
		    for(int i = 0; i < numLines; i++) {
		    	int index = longLine.indexOf('\n');
		    	lines[i] = longLine.substring(0, index);
		    	longLine = longLine.substring(index + 1);
		    }
		    
			level = processLevel(lines);
			world1 = level;
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} 
	}
	
	public static void loadLevelFromHTML(int levelNumber) {
		
		
		ArrayList <Block> level = new ArrayList<Block>();
		
		//System.out.println(levelName);
		//File levelFile = new File(levelName);
		try {
			
			FileReader levelFileReader = null;
			BufferedReader levelBufferedReader = null;
			//levelFileReader = new FileReader(new File(Main.class.getResource(levelPath).getPath()));
			//levelBufferedReader = new BufferedReader(levelFileReader);
			levelBufferedReader = new BufferedReader(new InputStreamReader(levelsURL.openStream()));
			
			String[] lines;
			String line, longLine = "";
			int numLines = 0;
		    while ((line = levelBufferedReader.readLine()) != null) {
		    	longLine += line + '\n';
		    	numLines++;
		    }
		    levelBufferedReader.close();
			
		    lines = new String[numLines];
		    for(int i = 0; i < numLines; i++) {
		    	int index = longLine.indexOf('\n');
		    	lines[i] = longLine.substring(0, index);
		    	longLine = longLine.substring(index + 1);
		    	//System.out.println(lines[i]);
		    }
		    
		    int trueNumLines = 0;
		    String trueLongLine = "";
		    boolean inTextArea = false;
		    for(int i = 0; i < numLines; i++) {
		    	String testLine = lines[i];
		    	if(testLine.contains("</textarea>")) {
		    		if(inTextArea) {
		    			break;
		    		}
		    		inTextArea = false;
		    	}
		    	if(inTextArea) {
		    		trueLongLine += testLine + '\n';
		    		trueNumLines++;
		    	}
		    	if(testLine.contains("<textarea name=\"LEVEL_" + levelNumber + "\">")) {
		    		inTextArea = true;
		    	}
		    }
		    String[] trueLines = new String[trueNumLines];
		    for(int i = 0; i < trueNumLines; i++) {
		    	int index = trueLongLine.indexOf('\n');
		    	trueLines[i] = trueLongLine.substring(0, index);
		    	//System.out.println(trueLines[i]);
		    	trueLongLine = trueLongLine.substring(index + 1);
		    }
		    
			level = processLevel(trueLines);
			world1 = level;
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} 
	}
	
	public static ArrayList<Block> processLevel(String[] lines) {
		ArrayList <Block> level = new ArrayList<Block>();
		
		String line;
		levelWidth = 0;
		for(int i = 0; i < lines.length; i++) {
			line = lines[i];
			if(line.length() * 20 > levelWidth) {
				levelWidth = line.length() * 20;
			}
		}
		
		levelName = "UNNAMED";
		bossCount = 0;
		totalBossMaxHealth = 0;
		isGameOfLife = false;
		FinalBoss.reset(false);
		boolean[][] conwayCells;
		ConwayCell.setNumLiveCells(0);
		ConwayCell.setTick(200);
		hasBackground = false;
		int messageColorCode = 0;
		conwayCells = new boolean[levelWidth / 20][30];
		
		for(int i = 0; i < 30; i++) {
			line = lines[i];
			//System.out.println(line);
			//System.out.println(line.length());
			for(int ii = 0; ii < line.length(); ii++) {
				char blockChar = line.charAt(ii);
					if(blockChar != ' ' && blockChar != 'S' && !Character.isDigit(blockChar) && blockChar != 'B' && blockChar != 'G' && blockChar != 'i' && blockChar != 'f' && blockChar != 'c') {
						
							String blockType;
							if(blockChar == 'X') {
								blockType = "NORMAL";
							}
							else if(blockChar == '%') {
								blockType = "REFLECT";
							}
							else if(blockChar == '!') {
								blockType = "BOMB";
							}
							else if(blockChar == '=') {
								blockType = "UNBREAKABLE";
							}
							else if(blockChar == 'P') {
								blockType = "PORTAL";
							}
							else if(blockChar == '*') {
								blockType = "TURRET";
							}
							else if(blockChar == '&') {
								blockType = "ROCKET";
							}
							else if(blockChar == '~') {
								blockType = "BOUNCE";
							}
							else if(blockChar == 'W') {
								blockType = "ZEROG";
							}
							else if(blockChar == 'L') {
								blockType = "LAVA";
							}
							else if(blockChar == '|') {
								blockType = "WOOD";
							}
							else if(blockChar == '(') {
								blockType = "POWERUP";
							}
							else if(blockChar == '{') {
								blockType = "POWERUP";
							}
							else if(blockChar == '[') {
								blockType = "POWERUP";
							}
							else if(blockChar == 'I') {
								blockType = "ICE";
							}
							else if(blockChar == ':') {
								blockType = "SNOW";
							}
							else if(blockChar == '@') {
								blockType = "VOID";
							}
							else if(blockChar == '\\') {
								blockType = "PLANT";
							}
							else if(blockChar == '^') {
								blockType = "POW";
							}
							else if(blockChar == 'u') {
								blockType = "CRYSTAL";
							}
							else {
								blockType = "NORMAL";
							}
							
							level.add(createBlockByName(blockType, ii * 20, i * 20));
							
							}
						
					else {
						if(blockChar == 'S') {
							player1.setxPos(ii * 20);
							player1.setyPos(i * 20);
						}
						else if(blockChar == 'B') {
							level.add(new BossBarricade(ii * 20, i * 20));
						}
						else if(blockChar == 'G') {
							ghosts.add(new Ghost(ii * 20, i * 20, 20));
						}
						else if(blockChar == 'i') {
							ghosts.add(new GhostIce(ii * 20, i * 20));
						}
						else if(blockChar == 'f') {
							ghosts.add(new GhostFire(ii * 20, i * 20));
						}
						else if(blockChar == 'c') {
							isGameOfLife = true;
							conwayCells[ii][i] = true;
							ConwayCell.setNumLiveCells(ConwayCell.getNumLiveCells() + 1);
							ConwayCell.setPaused(false);
						}
						else if(Character.isDigit(blockChar)) {
							Boss boss = null;
							if(blockChar == '1') {
								boss = new Boss("UFO", 10000, ii * 20, i * 20, bossCount);
								
							}
							else if(blockChar == '2') {
								boss = new Boss("CYCLOPS", 2500, ii * 20, i * 20, bossCount);
								
							}
							else if(blockChar == '3') {
								boss = new Boss("DRAGON", 20000, ii * 20, i * 20, bossCount);
								
							}
							else if(blockChar == '4') {
								boss = new Boss("TROLL", 15000, ii * 20, i * 20, bossCount);
							}
							if(boss != null) {
								for(int n = 0; n < boss.getnNodes(); n++) {
									level.add(new BossBlock(boss.getMaxColumn(), -20 * i, boss.getSignature()));
								}
								if(boss.getMaxColumn() > Main.levelWidth) {
									Main.levelWidth = boss.getMaxColumn();
								}
								bosses.add(boss);
								totalBossMaxHealth += boss.getMaxHealth();
								bossCount++;
							}
						}
					}
				}
			ConwayCell.createCells(levelWidth / 20, 30);
			ConwayCell.setCells(conwayCells);
			//ConwayCell.setNumColumns(levelWidth / 20);
			//ConwayCell.setNumRows(30);
			}
		
		for(int n = 30; n < lines.length; n++) {
			line = lines[n];
			if(line.charAt(0) == '#') {
				try {
					rollercoasters.add(new RollerCoaster(Integer.parseInt(line.substring(line.indexOf('{') + 1, line.indexOf(','))), Integer.parseInt(line.substring(line.indexOf(',') + 1, line.indexOf('}'))), line.substring(line.indexOf('#') + 1, line.indexOf(':'))));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			else if(line.charAt(0) == '$') {
				levelName = line.substring(1);
			}
			else if(line.charAt(0) == '?') {
				level.addAll(randomWorld(Integer.parseInt(line.substring(1))));
			}
			else if(line.charAt(0) == '\'') {
				messageColorCode = Integer.parseInt(line.substring(1));
			}
			else if(line.charAt(0) == '"') {
				String message = line.substring(1, line.indexOf('@'));
				int xPos = Integer.parseInt(line.substring(line.indexOf('@') + 1, line.indexOf('&')));
				int yPos = Integer.parseInt(line.substring(line.indexOf('&') + 1));
				Message.getMessages().add(new Message(message, xPos, yPos, messageColorCode));
			}
			else if(line.charAt(0) == '^') {
				if(levelNumber > checkPoint) {
					System.out.println("checkPoint");
					checkPoint = levelNumber;
					/*
					URL checkPointURL = createURL(filePath + "data/checkpoint.txt");
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(new File(checkPointURL.getFile())));
						bw.write(""+checkPoint);
						bw.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					*/
				}
				Message.getMessages().add(new Message("CHECK POINT", 40, 300, 2));   //"CHECK POINT@40&300
			}
			else if(line.charAt(0) == '+') {
				int lvl = Integer.parseInt(line.substring(1, line.indexOf('x')));
				int xPos = Integer.parseInt(line.substring(line.indexOf('x') + 1, line.indexOf('y')));
				int yPos = Integer.parseInt(line.substring(line.indexOf('y') + 1));
				if(lvl == -1) {
					variablePortals.add(new CustomPortal(xPos, yPos, -1));
				}
				else {
					variablePortals.add(new VPortal(xPos, yPos, lvl));
				}
			}
			
			else if(line.charAt(0) == '=') {
				worldNumber = Integer.parseInt(line.substring(1));
				//isPaused = true;
				//isComplete = true;
				//TITLE = createBufferedImage(imagesFP + "world_" + worldNumber + ".bmp");
				
			}
			else if(line.charAt(0) == 'C') {
				String text = line.substring(1);
				String contentsString;
				String valuesString;
				String xPosString;
				String yPosString;
				String xVelocityString;
				String yVelocityString;
				char gravityChar;
				contentsString = text.substring(text.indexOf('{') + 1, text.indexOf('}'));
				text = text.substring(text.indexOf('}') + 2);
				valuesString = text.substring(text.indexOf('{') + 1, text.indexOf('}'));
				text = text.substring(text.indexOf('}') + 2);
				xPosString = text.substring(0, text.indexOf(','));
				text = text.substring(text.indexOf(',') + 1);
				yPosString = text.substring(0, text.indexOf(','));
				text = text.substring(text.indexOf(',') + 1);
				xVelocityString = text.substring(0, text.indexOf(','));
				text = text.substring(text.indexOf(',') + 1);
				yVelocityString = text.substring(0, text.indexOf(','));
				text = text.substring(text.indexOf(',') + 1);
				gravityChar = text.charAt(0);
				
				String[] contents = null;
				int[] values = null;
				int xPos;
				int yPos;
				double xVelocity;
				double yVelocity;
				boolean gravity;
				{
					int nCommas = 0;
					for(int i = 0; i < contentsString.length(); i++) {
						if(contentsString.charAt(i) == ',') {
							nCommas++;
						}
					}
					String[] localArray = new String[nCommas];
					for(int i = 0; i < nCommas; i++) {
						localArray[i] = contentsString.substring(0, contentsString.indexOf(','));
						contentsString = contentsString.substring(contentsString.indexOf(',') + 1);
					}
					contents = localArray;
				}
				{
					int nCommas = 0;
					for(int i = 0; i < valuesString.length(); i++) {
						if(valuesString.charAt(i) == ',') {
							nCommas++;
						}
					}
					int[] localArray = new int[nCommas];
					for(int i = 0; i < nCommas; i++) {
						localArray[i] = Integer.parseInt(valuesString.substring(0, valuesString.indexOf(',')));
						valuesString = valuesString.substring(valuesString.indexOf(',') + 1);
					}
					values = localArray;
				}
				xPos = Integer.parseInt(xPosString);
				yPos = Integer.parseInt(yPosString);
				xVelocity = Integer.parseInt(xVelocityString);
				yVelocity = Integer.parseInt(yVelocityString);
				if(gravityChar == 'T') {
					gravity = true;
				}
				else {
					gravity = false;
				}
				chests.add(new Chest(contents, values, xPos, yPos, xVelocity, yVelocity, gravity));
			}
			else if(line.charAt(0) == 'Z') {
				isSlender = true;
				slenderX = Integer.parseInt(line.substring(1, line.indexOf(',')));
				slenderY = Integer.parseInt(line.substring(line.indexOf(',') + 1, line.length()));
			}
			else if(line.charAt(0) == 'c') {
				String text = line.substring(1);
				ConwayCell.setTick(Integer.parseInt(text));
			}
			else if(line.charAt(0) == '_') {
				String text = line.substring(1);
				String imageAddress = (imagesFP + "background_" + Integer.parseInt(text) + ".bmp");
				BACKGROUND = createBufferedImage(imageAddress);
				hasBackground = true;
			}
			else if(line.charAt(0) == '.') {
				FinalBoss.reset(true);
				int xPos;
				xPos = Integer.parseInt(line.substring(1));
				FinalBoss.setPosition(xPos, 100);
			}
			else if(line.charAt(0) == 'n') {
				levelNumber = Integer.parseInt(line.substring(1));
			}
			else if(line.startsWith("HTML:")) {
				line = line.substring(5);
				if(line.startsWith("goto")) {
					line = line.substring(4);
				}
			}
		}
		return level;
	}
	
	public static Block createBlockByName(String name, int xPos, int yPos) {
		
		Block block = null;

        try
        {
            Class<?> var3 = (Class<?>)stringToClassMapping.get(name);

            if (var3 != null)
            {
                block = (Block)var3.getConstructor(new Class[]{int.class, int.class}).newInstance(xPos, yPos);
            }
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
        }

        return block;
        
			
	}
	
	public static String getBlockString(Block block)
    {
        return (String)classToStringMapping.get(block.getClass());
    }

	public static Block blockAt(int x, int y) {
		int index = x / 20;
		if(index >= 0 && index < columns.length) {
			for(int i = 0; i < columns[index].size(); i++) {
				Block block = columns[index].get(i);
				if(block.yPos == y) {
					return block;
				}
			}
		}
		return null;
	}
	
	public static void replaceBlockWith(Block block, Class <? extends Block> c) {
		replaceBlockWith(block.xPos, block.yPos, c);
	}
	public static void replaceBlockWith(int xPos, int yPos, Class <? extends Block> c) {
		int index = xPos / 20;
		if(index >= 0 && index < columns.length) {
			for(int i = 0; i < columns[index].size(); i++) {
				Block b = columns[index].get(i);
				if(b.yPos == yPos) {
					columns[index].set(i, createBlockByName(classToStringMapping.get(c), xPos, yPos));
					return;
				}
			}
		}
	}
	public static void placeThisBlock(Block newBlock) {
		int xPos = newBlock.xPos;
		int yPos = newBlock.yPos;
		int index = xPos / 20;
		if(index >= 0 && index < columns.length) {
			for(int i = 0; i < columns[index].size(); i++) {
				Block b = columns[index].get(i);
				if(b.yPos == yPos) {
					columns[index].set(i, newBlock);
					return;
				}
			}
			columns[index].add(newBlock);
		}
	}
	public static void destroyBlock(int xPos, int yPos) {
		int index = xPos / 20;
		if(index >= 0 && index < columns.length) {
			for(int i = 0; i < columns[index].size(); i++) {
				Block b = columns[index].get(i);
				if(b.yPos == yPos) {
					b.isTerminated = true;
					columns[index].set(i, b);
					return;
				}
			}
		}
	}
	public static void removeBlock(int xPos, int yPos) {
		int index = xPos / 20;
		if(index >= 0 && index < columns.length) {
			for(int i = 0; i < columns[index].size(); i++) {
				Block b = columns[index].get(i);
				if(b.yPos == yPos) {
					columns[index].remove(i);
					return;
				}
			}
		}
	}
	public static ArrayList<Block> getBlocksAdjacentTo(int xPos, int yPos) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		int x0 = xPos / 20;
		int y0 = yPos / 20;
		int xMin, xMax, yMin, yMax;
		xMin = Math.max(x0 - 1, 0);
		xMax = Math.min(x0 + 1, columns.length - 1);
		yMin = Math.max(y0 - 1, 0);
		yMax = Math.min(y0 + 1, 30);
		for(int x = xMin; x <= xMax; x++) {
			for(int y = yMin; y <= yMax; y++) {
				Block b = Main.blockAt(x * 20, y * 20);
				if(!(x == x0 && y == y0) && b != null) {
					blocks.add(b);
				}
			}
		}
		return blocks;
	}
	public static ArrayList<Block> getBlocksAdjacentTo(Block block) {
		return getBlocksAdjacentTo(block.xPos, block.yPos);
	}
	
	public static Color fillBarColor(double ratio) {
		Color color;
		float h, s, b;
		h = (float) (.240 * (ratio + .120));
		s = 1;
		b = 1;
		color = new Color(Color.HSBtoRGB(h, s, b));
		return color;
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		return (Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)));
	}
	
	private static BufferedImage imageToBufferedImage(Image image) { 
		 
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB); 
        Graphics2D g2 = bufferedImage.createGraphics(); 
        g2.drawImage(image, 0, 0, null); 
        g2.dispose(); 
 
        return bufferedImage; 
 
    } 
 
    public static Image makeColorTransparent(BufferedImage im, final Color color) { 
        ImageFilter filter = new RGBImageFilter() { 
 
                // the color we are looking for... Alpha bits are set to opaque 
                public int markerRGB = color.getRGB() | 0xFF000000; 
 
                public final int filterRGB(int x, int y, int rgb) { 
                        if ((rgb | 0xFF000000) == markerRGB) { 
                                // Mark the alpha bits as zero - transparent 
                                return 0x00FFFFFF & rgb; 
                        } else { 
                                // nothing to do 
                                return rgb; 
                        } 
                } 
        }; 
 
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter); 
        return Toolkit.getDefaultToolkit().createImage(ip); 
    }
    
    //dreamincode.net
    public BufferedImage negative(BufferedImage img) {
        Color col;
        for (int x = 0; x < img.getWidth(); x++) { //width
            for (int y = 0; y < img.getHeight(); y++) { //height

                int RGBA = img.getRGB(x, y); //gets RGBA data for the specific pixel

                col = new Color(RGBA, true); //get the color data of the specific pixel

                col = new Color(Math.abs(col.getRed() - 255),
                        Math.abs(col.getGreen() - 255), Math.abs(col.getBlue() - 255)); //Swaps values
                //i.e. 255, 255, 255 (white)
                //becomes 0, 0, 0 (black)
                
                img.setRGB(x, y, col.getRGB()); //set the pixel to the altered colors
            }
        }
        return img;
    }

//write the returned BufferedImage to a file (using ImageIO.write) to see the result!
    
    public static BufferedImage componentToImage(Component component, Rectangle region) throws IOException 
    { 
        BufferedImage img = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE); 
        Graphics g = img.getGraphics(); 
        g.setColor(component.getForeground()); 
        g.setFont(component.getFont()); 
        component.paintAll(g); 
        if (region == null) 
        { 
            region = new Rectangle(0, 0, img.getWidth(), img.getHeight()); 
        } 
        return img.getSubimage(region.x, region.y, region.width, region.height); 
    } 
    
    public static BufferedImage tintImage(Image original, int r, int g, int b, int a){
        int width = original.getWidth(null);
        int height = original.getHeight(null);
        BufferedImage tinted = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D graphics = (Graphics2D) tinted.getGraphics();
        graphics.drawImage(original, 0, 0, width, height, null);
        Color c = new Color(r,g,b,a);   //default alpha is 128
        Color n = new Color(0,0,0,0);
        BufferedImage tint = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        for(int i = 0 ; i < width ; i++){
            for(int j = 0 ; j < height ; j++){
                if(tinted.getRGB(i, j) != n.getRGB()){
                    tint.setRGB(i, j, c.getRGB());
                }
            }
        }
        graphics.drawImage(tint, 0, 0, null);
        graphics.dispose();
        return tinted;
    }

    
    private Image getImage(String pathName) {
         URL url = getClass().getResource(pathName);
         Image image = Toolkit.getDefaultToolkit().getImage(url);
         return image;
    }
    
	@Override
	public void loop() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void play() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	
	/******************************************************/
	
	
	

	
	    
	    

	    @Override
	    public void paint(Graphics g) {
	    	if(gameStarted) {
		        this.dbImage = this.createImage(this.getWidth(), this.getHeight());
		        this.dbGraphics = this.dbImage.getGraphics();
		        this.paintComponent(this.dbGraphics);
		        g.drawImage(this.dbImage, 0, 0, this);
	    	}
	    }

	    public void paintComponent(Graphics g) {
	        int i;
	        this.drawUnder(g);
	        this.updateGUI();
	        for (i = 0; i < this.currentPage.getTextFields().size(); ++i) {
	            TextField t = (TextField)this.currentPage.getTextFields().get(i);
	            g.setColor(Color.WHITE);
	            g.fillRect(t.getxPos(), t.getyPos(), t.getWidth(), t.getHeight());
	            g.setColor(Color.BLACK);
	            g.drawRect(t.getxPos(), t.getyPos(), t.getWidth(), t.getHeight());
	            g.drawString(t.getDisplayText(), t.getxPos(), t.getyPos() + t.getHeight() / 2);
	            if (!t.getContext()) continue;
	            g.drawRect(t.getxPos() - 3, t.getyPos() - 3, t.getWidth() + 6, t.getHeight() + 6);
	        }
	        for (i = 0; i < this.currentPage.getButtons().size(); ++i) {
	            BasicButton b = (BasicButton)this.currentPage.getButtons().get(i);
	            g.setColor(b.getColor());
	            g.fillRect(b.getxPos(), b.getyPos(), b.getWidth(), b.getHeight());
	            g.setColor(Color.BLACK);
	            g.drawRect(b.getxPos(), b.getyPos(), b.getWidth(), b.getHeight());
	            g.drawRoundRect(b.getxPos(), b.getyPos(), b.getWidth(), b.getHeight(), 8, 8);
	            g.drawString(b.getLabel(), b.getxPos(), b.getyPos() + b.getHeight() / 2);
	            if (!b.getOver()) continue;
	            g.drawRect(b.getxPos() + 3, b.getyPos() + 3, b.getWidth() - 6, b.getHeight() - 6);
	            if (!b.isHeld()) continue;
	            g.drawRect(b.getxPos() + 6, b.getyPos() + 6, b.getWidth() - 12, b.getHeight() - 12);
	        }
	        this.drawOver(g);
	        this.repaint();
	    }

	    public void updateGUI() {
	        isMousePressed = isMousePressedBuffer;
	        if (isMousePressed) {
	            if (!isAlreadyMousePressed) {
	                isMouseClicked = true;
	                isAlreadyMousePressed = true;
	            } else {
	                isMouseClicked = false;
	            }
	        } else {
	            isAlreadyMousePressed = false;
	            isMouseClicked = false;
	        }
	        isMousePressedBuffer = false;
	        isMouseHeld = isMousePressed || isAlreadyMousePressed;
	        isKeyPressed = isKeyPressedBuffer;
	        if (isKeyPressed) {
	            key = keyBuffer;
	            keyCode = keyCodeBuffer;
	            if (!isAlreadyKeyPressed) {
	                isKeyClicked = true;
	                isAlreadyKeyPressed = true;
	            } else {
	                isKeyClicked = false;
	            }
	        } else {
	            isAlreadyKeyPressed = false;
	            isKeyClicked = false;
	        }
	        isKeyPressedBuffer = false;
	        isKeyHeld = isKeyPressed || isAlreadyKeyPressed;
	        for (int i = 0; i < this.pages.size(); ++i) {
	            Page p = this.pages.get(i);
	            if (p.getPageNumber() != this.currentPageNumber) continue;
	            p.update();
	            this.pages.set(i, this.pages.get(0));
	            this.pages.set(0, p);
	            this.currentPage = p;
	            this.setAnyContext(this.currentPage.isAnyContext());
	            break;
	        }
	    }

	    public Image getDbImage() {
	        return this.dbImage;
	    }

	    public void setDbImage(Image dbImage) {
	        this.dbImage = dbImage;
	    }

	    public Graphics getDbGraphics() {
	        return this.dbGraphics;
	    }

	    public void setDbGraphics(Graphics dbGraphics) {
	        this.dbGraphics = dbGraphics;
	    }

	    public static boolean isMousePressed() {
	        return isMousePressed;
	    }

	    public static void setMousePressed(boolean isMousePressed) {
	        Main.isMousePressed = isMousePressed;
	    }

	    public static int getMouseX() {
	        return mouseX;
	    }

	    public static void setMouseX(int mouseX) {
	        Main.mouseX = mouseX;
	    }

	    public static int getMouseY() {
	        return mouseY;
	    }

	    public static void setMouseY(int mouseY) {
	        Main.mouseY = mouseY;
	    }

	    public ArrayList<Page> getPages() {
	        return this.pages;
	    }

	    public void setPages(ArrayList<Page> pages) {
	        this.pages = pages;
	    }

	    public int getCurrentPageNumber() {
	        return this.currentPageNumber;
	    }

	    public void setCurrentPageNumber(int currentPageNumber) {
	        this.currentPageNumber = currentPageNumber;
	    }

	    public Page getCurrentPage() {
	        return this.currentPage;
	    }

	    public void setCurrentPage(Page currentPage) {
	        this.currentPage = currentPage;
	    }

	    public static boolean isMouseClicked() {
	        return isMouseClicked;
	    }

	    public static void setMouseClicked(boolean isMouseClicked) {
	        Main.isMouseClicked = isMouseClicked;
	    }

	    public static boolean isAlreadyMousePressed() {
	        return isAlreadyMousePressed;
	    }

	    public static void setAlreadyMousePressed(boolean isAlreadyMousePressed) {
	        Main.isAlreadyMousePressed = isAlreadyMousePressed;
	    }

	    public static boolean isMousePressedBuffer() {
	        return isMousePressedBuffer;
	    }

	    public static void setMousePressedBuffer(boolean isMousePressedBuffer) {
	        Main.isMousePressedBuffer = isMousePressedBuffer;
	    }

	    public static boolean isKeyPressed() {
	        return isKeyPressed;
	    }

	    public static void setKeyPressed(boolean isKeyPressed) {
	        Main.isKeyPressed = isKeyPressed;
	    }

	    public static boolean isKeyReleased() {
	        return isKeyReleased;
	    }

	    public static void setKeyReleased(boolean isKeyReleased) {
	        Main.isKeyReleased = isKeyReleased;
	    }

	    public static boolean isKeyClicked() {
	        return isKeyClicked;
	    }

	    public static void setKeyClicked(boolean isKeyClicked) {
	        Main.isKeyClicked = isKeyClicked;
	    }

	    public static boolean isAlreadyKeyPressed() {
	        return isAlreadyKeyPressed;
	    }

	    public static void setAlreadyKeyPressed(boolean isAlreadyKeyPressed) {
	        Main.isAlreadyKeyPressed = isAlreadyKeyPressed;
	    }

	    public static boolean isKeyPressedBuffer() {
	        return isKeyPressedBuffer;
	    }

	    public static void setKeyPressedBuffer(boolean isKeyPressedBuffer) {
	        Main.isKeyPressedBuffer = isKeyPressedBuffer;
	    }

	    public static char getKey() {
	        return key;
	    }

	    public static void setKey(char key) {
	        Main.key = key;
	    }

	    public static char getKeyBuffer() {
	        return keyBuffer;
	    }

	    public static void setKeyBuffer(char keyBuffer) {
	        Main.keyBuffer = keyBuffer;
	    }

	    public static boolean isKeyBackSpace() {
	        return isKeyBackSpace;
	    }

	    public static void setKeyBackSpace(boolean isKeyBackSpace) {
	        Main.isKeyBackSpace = isKeyBackSpace;
	    }

	    public Color getBgColor() {
	        return this.bgColor;
	    }

	    public void setBgColor(Color bgColor) {
	        this.bgColor = bgColor;
	    }

	    public static int getKeyCode() {
	        return keyCode;
	    }

	    public static void setKeyCode(int keyCode) {
	        Main.keyCode = keyCode;
	    }

	    public static int getKeyCodeBuffer() {
	        return keyCodeBuffer;
	    }

	    public static void setKeyCodeBuffer(int keyCodeBuffer) {
	        Main.keyCodeBuffer = keyCodeBuffer;
	    }

	    public static boolean isMouseHeld() {
	        return isMouseHeld;
	    }

	    public static void setMouseHeld(boolean isMouseHeld) {
	        Main.isMouseHeld = isMouseHeld;
	    }

	    public static boolean isKeyHeld() {
	        return isKeyHeld;
	    }

	    public static void setKeyHeld(boolean isKeyHeld) {
	        Main.isKeyHeld = isKeyHeld;
	    }

	    public boolean isAnyContext() {
	        return this.isAnyContext;
	    }

	    public void setAnyContext(boolean isAnyContext) {
	        this.isAnyContext = isAnyContext;
	    }

	    @Override
	    public String toString() {
	        return "WindowGUI [dbImage=" + this.dbImage + ", dbGraphics=" + this.dbGraphics + "]";
	    }
	    
	    
	    public void mousePressed(MouseEvent e) {
			MOUSE_X = e.getX();
			MOUSE_Y = e.getY();
			if(!MOUSE_PRESSED) {
				MOUSE_CLICKED = true;
			}
			else {
				MOUSE_CLICKED = false;
			}
			MOUSE_PRESSED = true;
			Main.setMousePressedBuffer(MOUSE_PRESSED);
			
			e.consume();
		}
	    
		@Override
		public void mouseReleased(MouseEvent e) {
			MOUSE_X = e.getX();
			MOUSE_Y = e.getY();
			MOUSE_PRESSED = false;
			Main.setMousePressedBuffer(MOUSE_PRESSED);
			MOUSE_CLICKED = false;
			e.consume();
		}
		
	    @Override
	    public void mouseDragged(MouseEvent e) {
	    	MOUSE_X = e.getX();
			MOUSE_Y = e.getY();
	        mouseX = e.getX();
	        mouseY = e.getY();
	        e.consume();
	    }

	    @Override
	    public void mouseMoved(MouseEvent e) {
	    	MOUSE_X = e.getX();
			MOUSE_Y = e.getY();
	        mouseX = e.getX();
	        mouseY = e.getY();
	        e.consume();
	    }
	    
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	    

	    
	    
	    public void keyPressed(KeyEvent e) {
	    	int keyCode = e.getKeyCode();
	    	if(keyCode >= 0 && keyCode < 256) {
	    		keyTimer[keyCode] = Integer.MAX_VALUE;
	    	}
			if(keyCode == KeyEvent.VK_LEFT && directionKeys == ARROW_KEYS) {
				LEFT = true;
			}
			else if(keyCode == KeyEvent.VK_RIGHT && directionKeys == ARROW_KEYS) {
				RIGHT = true;
			}
			else if(keyCode == KeyEvent.VK_UP && directionKeys == ARROW_KEYS) {
				UP = true;
			}
			else if(keyCode == KeyEvent.VK_DOWN && directionKeys == ARROW_KEYS) {
				DOWN = true;
			}
			else if(keyCode == KeyEvent.VK_A && directionKeys == WASD) {
				LEFT = true;
			}
			else if(keyCode == KeyEvent.VK_D && directionKeys == WASD) {
				RIGHT = true;
			}
			else if(keyCode == KeyEvent.VK_W && directionKeys == WASD) {
				UP = true;
			}
			else if(keyCode == KeyEvent.VK_S && directionKeys == WASD) {
				DOWN = true;
			}
			else if(keyCode == KeyEvent.VK_SPACE) {
				SHIELD = true;
			}
			else if(keyCode == KeyEvent.VK_E) {
				WEAPON_SWITCH = true;
			}
			else if(keyCode == KeyEvent.VK_P) {
				if(isPaused && !isAnyContext()) {
					isPaused = false;
					setCurrentPageNumber(1);
					isComplete = false;
					TITLE = createBufferedImage(imagesFP + "TITLE.bmp");
				}
				else {
					isPaused = true;
					setCurrentPageNumber(0);
					Page page;
					if(getPages().get(0).getPageNumber() == 0) {
						page = getPages().get(0);
					}
					else {
						page = getPages().get(1);
					}
					page.getButtons().get(0).setContext(false);
					page.getButtons().get(1).setContext(false);
					page.getButtons().get(2).setContext(false);
				}
			}
			e.consume();
		}
		public void keyReleased(KeyEvent e) {

			int keyCode = e.getKeyCode();
			if(keyCode >= 0 && keyCode < 256) {
				keyTimer[keyCode] = 5;
			}
			
			
			e.consume();
		}





		@Override
		public void keyTyped(KeyEvent e) {
			
		}

		
		public void updateKeyStates() {
			
			int[] keyList = {KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT,KeyEvent.VK_UP,KeyEvent.VK_DOWN,
					KeyEvent.VK_A,KeyEvent.VK_D,KeyEvent.VK_W,KeyEvent.VK_S,
					KeyEvent.VK_SPACE,KeyEvent.VK_E,KeyEvent.VK_EQUALS,KeyEvent.VK_MINUS,KeyEvent.VK_X,KeyEvent.VK_P};
			for(int i = 0; i < keyList.length; i++) {
				if(keyTimer[keyList[i]] > 0) {
					keyTimer[keyList[i]] = keyTimer[keyList[i]] - 1;
				}
			}
			
			
			if(keyTimer[KeyEvent.VK_LEFT] <= 0 && directionKeys == ARROW_KEYS) {
				LEFT = false;
			}
			if(keyTimer[KeyEvent.VK_RIGHT] <= 0 && directionKeys == ARROW_KEYS) {
				RIGHT = false;
			}
			if(keyTimer[KeyEvent.VK_UP] <= 0 && directionKeys == ARROW_KEYS) {
				UP = false;
			}
			if(keyTimer[KeyEvent.VK_DOWN] <= 0 && directionKeys == ARROW_KEYS) {
				DOWN = false;
			}
			if(keyTimer[KeyEvent.VK_A] <= 0 && directionKeys == WASD) {
				LEFT = false;
			}
			if(keyTimer[KeyEvent.VK_D] <= 0 && directionKeys == WASD) {
				RIGHT = false;
			}
			if(keyTimer[KeyEvent.VK_W] <= 0 && directionKeys == WASD) {
				UP = false;
			}
			if(keyTimer[KeyEvent.VK_S] <= 0 && directionKeys == WASD) {
				DOWN = false;
			}
			if(keyTimer[KeyEvent.VK_SPACE] <= 0) {
				SHIELD = false;
			}
			if(keyTimer[KeyEvent.VK_E] <= 0) {
				WEAPON_SWITCH = false;
			}
			if(keyTimer[KeyEvent.VK_EQUALS] <= 0) {
				levelNumber++;
			}
			if(keyTimer[KeyEvent.VK_MINUS] <= 0) {
				levelNumber--;
			}
			if(keyTimer[KeyEvent.VK_X] == 1) {
				if(isPaused && !isAnyContext()) {
					items.add(new Item("HEALTH", 100, player1.getxPos(), player1.getyPos() - 60, 0, 0, true));
					items.add(new Item("AMMO", 100, player1.getxPos(), player1.getyPos() - 60, 0, 0, true));
				}
			}
				
		}
		
		

}
