package ui.elements;

import agents.ChefAgent;
import ui.StatusEnum;

import javax.swing.*;
import javax.swing.border.Border;

public class KitchenAgentButton extends JButton implements AgentButton {


    public KitchenAgentButton(int i, int j, String title){
        this.setBorder(BorderFactory.createTitledBorder(title));
        this.setLocation(i, j);
        this.setSize(getBoxWidth(), getBoxHeight());
    }

    public void setStatus(StatusEnum status) {
        this.setBackground(status.getColor());
        this.setText(status.getValue());
    }

    public static int getBoxWidth() {
        return 70;
    }
    public static int getBoxHeight() {
        return 50;
    }

}
