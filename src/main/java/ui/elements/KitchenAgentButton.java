package ui.elements;

import ui.StatusEnum;

import javax.swing.*;

public class KitchenAgentButton extends AgentButton {


    public KitchenAgentButton(int i, int j, String title){
        this.setBorder(BorderFactory.createTitledBorder(title));
        this.setLocation(i, j);
        this.setSize(getBoxWidth(), getBoxHeight());
    }

    public static int getBoxWidth() {
        return 70;
    }
    public static int getBoxHeight() {
        return 50;
    }

}
