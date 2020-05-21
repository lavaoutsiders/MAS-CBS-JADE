package ui.elements;


import agents.OrderAgent;
import ui.StatusEnum;

import javax.swing.*;

public class OrderAgentButton extends JButton implements AgentButton {
    public OrderAgentButton(int x, int y) {
        this.setLocation(x,y);
        this.setSize(getBoxLength(), getBoxLength());
    }


    public static int getBoxLength(){
        return 45;
    }

    public void setStatus(StatusEnum status) {
        this.setText(status.getValue());
        this.setBackground(status.getColor());
    }


}
