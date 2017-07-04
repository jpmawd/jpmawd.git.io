package bounce;
// Note: all references to WindowGUI replaced with Main

import bounce.PageElement;

public class TextField
extends PageElement {
    private String text;
    private final int MAX_LENGTH;
    private String displayText;
    private boolean isConstrained;

    public TextField(int xPos, int yPos, int width, int height, int identifier) {
        super(xPos, yPos, width, height, identifier);
        this.displayText = this.text = "";
        this.isConstrained = true;
        this.MAX_LENGTH = width / 12;
    }

    public TextField(int xPos, int yPos, int identifier) {
        this(xPos, yPos, 60, 20, identifier);
    }

    public TextField(int xPos, int yPos, int width, int height, boolean isConstrained, int identifier) {
        this(xPos, yPos, width, height, identifier);
        this.isConstrained = isConstrained;
    }

    @Override
    public void update() {
        super.update();
        if (this.getContext() && Main.isKeyClicked() && Character.isDefined(Main.getKey())) {
            if (!Main.isKeyBackSpace()) {
                this.setText(String.valueOf(this.getText()) + Main.getKey());
            } else if (this.text.length() > 0) {
                this.setText(this.getText().substring(0, this.getText().length() - 1));
            }
        }
        this.displayText = this.text;
        if (this.isConstrained && this.displayText.length() > this.MAX_LENGTH) {
            this.displayText = this.displayText.substring(0, this.MAX_LENGTH);
        }
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDisplayText() {
        return this.displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public int getMAX_LENGTH() {
        return this.MAX_LENGTH;
    }

    public boolean isConstrained() {
        return this.isConstrained;
    }

    public void setConstrained(boolean isConstrained) {
        this.isConstrained = isConstrained;
    }
}