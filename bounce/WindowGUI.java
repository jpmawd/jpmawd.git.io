package bounce;


import java.applet.Applet;
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
import bounce.WindowGUI;

public class WindowGUI
extends Applet
implements MouseMotionListener, MouseListener, KeyListener{
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

    public WindowGUI(int width, int height, String title, boolean isResizable, boolean isVisible, int defaultCloseOperation, Color bgColor) {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        //this.setTitle(title);
        this.setName(title);
        this.setSize(width, height);
        //this.setResizable(isResizable);
        this.setVisible(isVisible);
        //this.setDefaultCloseOperation(defaultCloseOperation);
        this.setBackground(bgColor);
        this.bgColor = bgColor;
    }

    public WindowGUI(int width, int height, String title, boolean isResizable, boolean isVisible, int defaultCloseOperation, Color bgColor, ArrayList<Page> pages) {
        this(width, height, title, isResizable, isVisible, defaultCloseOperation, bgColor);
        this.pages = pages;
        this.currentPage = pages.get(this.currentPageNumber);
    }

    public WindowGUI(int width, int height, String title) {
        this(width, height, title, false, true, 3, Color.GRAY);
    }

    public WindowGUI(int width, int height, String title, ArrayList<Page> pages) {
        this(width, height, title, false, true, 3, Color.GRAY, pages);
    }

    @Override
    public void paint(Graphics g) {
        this.dbImage = this.createImage(this.getWidth(), this.getHeight());
        this.dbGraphics = this.dbImage.getGraphics();
        this.paintComponent(this.dbGraphics);
        g.drawImage(this.dbImage, 0, 0, this);
    }

    public void paintComponent(Graphics g) {
        int i;
        this.drawUnder(g);
        this.update();
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

    public void update() {
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

    public void drawUnder(Graphics g) {
    }

    public void drawOver(Graphics g) {
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
        WindowGUI.isMousePressed = isMousePressed;
    }

    public static int getMouseX() {
        return mouseX;
    }

    public static void setMouseX(int mouseX) {
        WindowGUI.mouseX = mouseX;
    }

    public static int getMouseY() {
        return mouseY;
    }

    public static void setMouseY(int mouseY) {
        WindowGUI.mouseY = mouseY;
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
        WindowGUI.isMouseClicked = isMouseClicked;
    }

    public static boolean isAlreadyMousePressed() {
        return isAlreadyMousePressed;
    }

    public static void setAlreadyMousePressed(boolean isAlreadyMousePressed) {
        WindowGUI.isAlreadyMousePressed = isAlreadyMousePressed;
    }

    public static boolean isMousePressedBuffer() {
        return isMousePressedBuffer;
    }

    public static void setMousePressedBuffer(boolean isMousePressedBuffer) {
        WindowGUI.isMousePressedBuffer = isMousePressedBuffer;
    }

    public static boolean isKeyPressed() {
        return isKeyPressed;
    }

    public static void setKeyPressed(boolean isKeyPressed) {
        WindowGUI.isKeyPressed = isKeyPressed;
    }

    public static boolean isKeyReleased() {
        return isKeyReleased;
    }

    public static void setKeyReleased(boolean isKeyReleased) {
        WindowGUI.isKeyReleased = isKeyReleased;
    }

    public static boolean isKeyClicked() {
        return isKeyClicked;
    }

    public static void setKeyClicked(boolean isKeyClicked) {
        WindowGUI.isKeyClicked = isKeyClicked;
    }

    public static boolean isAlreadyKeyPressed() {
        return isAlreadyKeyPressed;
    }

    public static void setAlreadyKeyPressed(boolean isAlreadyKeyPressed) {
        WindowGUI.isAlreadyKeyPressed = isAlreadyKeyPressed;
    }

    public static boolean isKeyPressedBuffer() {
        return isKeyPressedBuffer;
    }

    public static void setKeyPressedBuffer(boolean isKeyPressedBuffer) {
        WindowGUI.isKeyPressedBuffer = isKeyPressedBuffer;
    }

    public static char getKey() {
        return key;
    }

    public static void setKey(char key) {
        WindowGUI.key = key;
    }

    public static char getKeyBuffer() {
        return keyBuffer;
    }

    public static void setKeyBuffer(char keyBuffer) {
        WindowGUI.keyBuffer = keyBuffer;
    }

    public static boolean isKeyBackSpace() {
        return isKeyBackSpace;
    }

    public static void setKeyBackSpace(boolean isKeyBackSpace) {
        WindowGUI.isKeyBackSpace = isKeyBackSpace;
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
        WindowGUI.keyCode = keyCode;
    }

    public static int getKeyCodeBuffer() {
        return keyCodeBuffer;
    }

    public static void setKeyCodeBuffer(int keyCodeBuffer) {
        WindowGUI.keyCodeBuffer = keyCodeBuffer;
    }

    public static boolean isMouseHeld() {
        return isMouseHeld;
    }

    public static void setMouseHeld(boolean isMouseHeld) {
        WindowGUI.isMouseHeld = isMouseHeld;
    }

    public static boolean isKeyHeld() {
        return isKeyHeld;
    }

    public static void setKeyHeld(boolean isKeyHeld) {
        WindowGUI.isKeyHeld = isKeyHeld;
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
    
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        e.consume();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        e.consume();
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        WindowGUI.setMouseX(e.getX());
        WindowGUI.setMouseY(e.getY());
        WindowGUI.setMousePressedBuffer(true);
        e.consume();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        WindowGUI.setMouseX(e.getX());
        WindowGUI.setMouseY(e.getY());
        WindowGUI.setMousePressedBuffer(false);
        WindowGUI.setAlreadyMousePressed(false);
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
	
    @Override
    public void keyPressed(KeyEvent e) {
        WindowGUI.setKeyPressed(true);
        WindowGUI.setKeyBuffer(e.getKeyChar());
        WindowGUI.setKeyCodeBuffer(e.getKeyCode());
        if (keyCodeBuffer == 8) {
            WindowGUI.setKeyBackSpace(true);
        } else {
            WindowGUI.setKeyBackSpace(false);
        }
        e.consume();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        WindowGUI.setKeyPressedBuffer(false);
        WindowGUI.setKeyBuffer(e.getKeyChar());
        WindowGUI.setKeyCodeBuffer(e.getKeyCode());
        e.consume();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        WindowGUI.setKeyPressedBuffer(true);
        WindowGUI.setKeyBuffer(e.getKeyChar());
        WindowGUI.setKeyCodeBuffer(e.getKeyCode());
        if (keyCodeBuffer == 8) {
            WindowGUI.setKeyBackSpace(true);
        }
        e.consume();
    }
}