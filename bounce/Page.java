package bounce;


import java.util.ArrayList;
import bounce.BasicButton;
import bounce.TextField;

public class Page {
    private int pageNumber;
    private ArrayList<BasicButton> buttons = new ArrayList();
    private ArrayList<TextField> textFields = new ArrayList();
    private boolean isAnyContext = false;

    public Page() {
    }

    public Page(ArrayList<BasicButton> buttons, ArrayList<TextField> textFields, int pageNumber) {
        this.buttons = buttons;
        this.textFields = textFields;
        this.pageNumber = pageNumber;
    }

    public void update() {
        int i;
        this.isAnyContext = false;
        for (i = 0; i < this.buttons.size(); ++i) {
            BasicButton b = this.buttons.get(i);
            b.update();
            if (b.getContext()) {
                this.isAnyContext = true;
            }
            this.buttons.set(i, b);
        }
        for (i = 0; i < this.textFields.size(); ++i) {
            TextField t = this.textFields.get(i);
            t.update();
            if (t.getContext()) {
                this.isAnyContext = true;
            }
            this.textFields.set(i, t);
        }
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public ArrayList<BasicButton> getButtons() {
        return this.buttons;
    }

    public void setButtons(ArrayList<BasicButton> buttons) {
        this.buttons = buttons;
    }

    public ArrayList<TextField> getTextFields() {
        return this.textFields;
    }

    public void setTextFields(ArrayList<TextField> textFields) {
        this.textFields = textFields;
    }

    public boolean isAnyContext() {
        return this.isAnyContext;
    }

    public void setAnyContext(boolean isAnyContext) {
        this.isAnyContext = isAnyContext;
    }
}