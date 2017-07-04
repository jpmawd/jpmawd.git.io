package bounce;


import java.awt.Color;
import bounce.PageElement;

public class BasicButton
extends PageElement {
    private String label = " ";
    private Color color = Color.WHITE;

    public BasicButton(int xPos, int yPos, int width, int height, String label, Color color, int identifier) {
        super(xPos, yPos, width, height, identifier);
        this.setLabel(label);
        this.setColor(color);
    }

    public BasicButton(int xPos, int yPos, int identifier) {
        this(xPos, yPos, 20, 20, "", Color.WHITE, identifier);
    }

    public void update() {
        super.update();
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}