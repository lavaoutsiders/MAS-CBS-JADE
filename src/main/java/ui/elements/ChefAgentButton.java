package ui.elements;

import agents.ChefAgent;
import ui.StatusEnum;

import javax.swing.*;
import javax.swing.border.Border;

public class ChefAgentButton extends JButton implements AgentButton {


    public ChefAgentButton(int i, int j){
        this.setBorder(BorderFactory.createTitledBorder("Chef"));
    }

    public void setStatus(StatusEnum status) {
        this.setBackground(status.getColor());
        this.setText(status.getValue());
    }

    public static int getBoxWidth() {
        return 40;
    }
    public static int getBoxHeight() {
        return 50;
    }

}
