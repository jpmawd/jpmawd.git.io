package bounce;
// Note: all references to WindowGUI replaced with Main


public class PageElement {
    private int xPos;
    private int yPos;
    private int width;
    private int height;
    private int leftBound;
    private int rightBound;
    private int upperBound;
    private int lowerBound;
    private int identifier;
    private boolean isOver;
    private boolean isContext;
    private boolean isClicked;
    private boolean isHeld;

    public PageElement(int xPos, int yPos, int width, int height, int identifier) {
        this.setxPos(xPos);
        this.setyPos(yPos);
        this.setWidth(width);
        this.setHeight(height);
        this.identifier = identifier;
        this.leftBound = xPos;
        this.rightBound = xPos + width;
        this.upperBound = yPos;
        this.lowerBound = yPos + height;
    }

    public void update() {
        this.isOver();
        this.isContext();
        if (this.getContext()) {
            this.isHeld = this.isOver && Main.isMousePressed();
        }
    }

    public void isOver() {
        this.isOver = Main.getMouseX() > this.leftBound && Main.getMouseX() < this.rightBound && Main.getMouseY() > this.upperBound && Main.getMouseY() < this.lowerBound;
    }

    public void isContext() {
        if (Main.isMouseClicked()) {
            if (this.isOver) {
                this.setContext(true);
                this.isClicked = true;
            } else {
                this.setContext(false);
                this.isClicked = false;
            }
        } else {
            this.isClicked = false;
        }
    }

    public boolean getContext() {
        return this.isContext;
    }

    public void setContext(boolean isContext) {
        this.isContext = isContext;
    }

    public int getxPos() {
        return this.xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return this.yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLeftBound() {
        return this.leftBound;
    }

    public void setLeftBound(int leftBound) {
        this.leftBound = leftBound;
    }

    public int getRightBound() {
        return this.rightBound;
    }

    public void setRightBound(int rightBound) {
        this.rightBound = rightBound;
    }

    public int getUpperBound() {
        return this.upperBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    public int getLowerBound() {
        return this.lowerBound;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public int getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public boolean isClicked() {
        return this.isClicked;
    }

    public void setClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public boolean isHeld() {
        return this.isHeld;
    }

    public void setHeld(boolean isHeld) {
        this.isHeld = isHeld;
    }

    public boolean getOver() {
        return this.isOver;
    }

    public void setOver(boolean isOver) {
        this.isOver = isOver;
    }
}