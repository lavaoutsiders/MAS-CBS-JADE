package ui.elements;


import ui.StatusEnum;

import javax.swing.*;

public class OrderAgentButton extends AgentButton {
    public OrderAgentButton(int x, int y) {
        this.setLocation(x,y);
        this.setSize(getBoxLength(), getBoxLength());
    }


    public static int getBoxLength(){
        return 50;
    }




}
